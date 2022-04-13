#include <iostream>

#include <jsoncpp/json/json.h>

#include <arpa/inet.h>

#include "dataset.h"
#include "result.h"

#include "cpx.hh"

#include <boost/asio.hpp>
#include <boost/algorithm/string.hpp>

#include "measurements.pb.h"

using namespace std;
using boost::asio::ip::tcp;

void processJSON(tcp::iostream& stream){
    Json::Value val;
    Json::Reader reader;

    std::vector<Dataset> datasets;
    std::vector<Result> results;

    /* Read json string from the stream */
    string s;
    getline(stream, s, '\0');

    /* Parse string */
    reader.parse(s, val);

    datasets.clear();
    results.clear();
    for (int i = 0; i < val.size(); i++) {
        datasets.emplace_back();
        datasets[i].Deserialize(val[i]);
        /* Calculate averages */
        results.emplace_back(datasets[i].getInfo(), datasets[i].getRecords());
    }

    /* Create output JSON structure */
    Json::Value out;
//    Json::FastWriter writer;
    Json::StyledWriter writer;
    for (int i = 0; i < results.size(); i++) {
        Json::Value result;
        results[i].Serialize(result);
        out[i] = result;
    }

    /* Send the result back */
    std::string output = writer.write(out);
    stream << output;
    cout << output;
}

void processAvro(tcp::iostream& stream){
    unsigned int msgSize;
    char sizeBytes[4];
    stream.read(sizeBytes, 4);
    std::memcpy(&msgSize, sizeBytes, sizeof(int));
    msgSize = ntohl(msgSize);

    char *buffer = new char[msgSize];
    stream.read(buffer, msgSize);

    a::AMeasurementsResponse response;
    a::AMeasurementsRequest request;

    std::unique_ptr<avro::InputStream> inStream = avro::memoryInputStream((uint8_t*) buffer, msgSize);
    avro::DecoderPtr decoder = avro::binaryDecoder();
    decoder->init(*inStream);

    avro::decode(*decoder, request);

    for (auto currentTuple : request.requestTuple){

        /* Get records data from request (download, upload and ping) */
        double downloads = 0; double uploads = 0; double pings = 0;

        for (unsigned j = 0; j < currentTuple.records.DOWNLOAD.size(); j++) {
            downloads += currentTuple.records.DOWNLOAD[j];
            uploads += currentTuple.records.UPLOAD[j];
            pings += currentTuple.records.PING[j];
        }

        /* Compute and store averages */
        a::AAverage average;
        average.DOWNLOAD = downloads / currentTuple.records.DOWNLOAD.size();
        average.UPLOAD = uploads / currentTuple.records.UPLOAD.size();
        average.PING = pings / currentTuple.records.PING.size();

        /* Build responseTuple for final response */
        a::AResponseTuple blob;
        blob.average = average;
        blob.measurementInfo = currentTuple.measurementInfo;
        response.responseTuple.push_back(blob);
    }

    std::unique_ptr<avro::OutputStream> outStream = avro::ostreamOutputStream(stream);
    avro::EncoderPtr encoder = avro::binaryEncoder();
    encoder->init(*outStream);

    avro::encode(*encoder, response);
    encoder->flush();

    delete [] buffer;
}

void processProtobuf(tcp::iostream& stream){
    /* Read size of message */
    int msgSize;
    char sizeBytes[4];
    stream.read(sizeBytes, 4);
    std::memcpy(&msgSize, sizeBytes, sizeof(int));
    msgSize = ntohl(msgSize);   // Converts u_long from TCP/IP network order to host byte order

    /* Parse the input stream */
    char *buffer = new char[msgSize];
    stream.read(buffer, msgSize);

    esw::MeasurementsResponse response;
    esw::MeasurementsRequest request;
    request.ParseFromArray(buffer, msgSize);

    for (int i = 0; i < request.requesttuple_size(); i++) {
        const esw::MeasurementsRequest_RequestTuple& requestTuple = request.requesttuple(i);

        /* Get records data from request (download, upload and ping) */
        const esw::Records& records = requestTuple.records();
        double downloads = 0; double uploads = 0; double pings = 0;

        for (int j = 0; j < records.download_size(); ++j) {
            downloads += records.download(j);
            uploads += records.upload(j);
            pings += records.ping(j);
        }

        /* Compute and store averages */
        auto *averages = new esw::Average();
        averages->set_download(downloads/records.download_size());
        averages->set_upload(uploads/records.upload_size());
        averages->set_ping(pings/records.ping_size());

        /* Recreate measurement info */
        auto *info = new esw::MeasurementInfo();
        info->set_id(requestTuple.measurementinfo().id());
        info->set_timestamp(requestTuple.measurementinfo().timestamp());
        info->set_measurername(requestTuple.measurementinfo().measurername());

        /* Build responseTuple for final response */
        esw::MeasurementsResponse_ResponseTuple* responseTuple = response.add_responsetuple();
        responseTuple->set_allocated_measurementinfo(info);
        responseTuple->set_allocated_average(averages);
    }

    /* Serialize and send back the result */
    response.SerializeToOstream(&stream);

    delete [] buffer;
}

int main(int argc, char *argv[]) {

    if (argc != 3) {
        cout << "Error: two arguments required - ./server  <port> <protocol>" << endl;
        return 1;
    }

    // unsigned short int port = 12345;
    unsigned short int port = atoi(argv[1]);

    // std::string protocol = "json";
    std::string protocol(argv[2]);
    boost::to_upper(protocol);
    try {
        boost::asio::io_service io_service;

        tcp::endpoint endpoint(tcp::v4(), port);
        tcp::acceptor acceptor(io_service, endpoint);

        while (true) {
            cout << "Waiting for message in " + protocol + " format..." << endl;
            tcp::iostream stream;
            boost::system::error_code ec;
            acceptor.accept(*stream.rdbuf(), ec);

            if(protocol == "JSON"){
                processJSON(stream);
            }else if(protocol == "AVRO"){
                processAvro(stream);
            }else if(protocol == "PROTO"){
                processProtobuf(stream);
            }else{
                throw std::logic_error("Protocol not yet implemented");
            }

        }

    }
    catch (std::exception &e) {
        std::cerr << e.what() << std::endl;
    }

    return 0;
}
