package cz.esw.serialization.handler;

import cz.esw.serialization.ResultConsumer;
import cz.esw.serialization.json.DataType;
import cz.esw.serialization.json.Dataset;
import cz.esw.serialization.json.Result;
import cz.esw.serialization.proto.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Marek Cuchý (CVUT)
 */
public class ProtoDataHandler implements DataHandler {

	private final InputStream is;
	private final OutputStream os;
	protected ArrayList<MeasurementInfo> measurementsInfo;
	protected Map<Integer, Records.Builder> recordsMap;			// Saving builders because build data cannot be modified

	/**
	 * @param is : input stream from which the results will be read
	 * @param os : output stream to which the data have to be written
	 */
	public ProtoDataHandler(InputStream is, OutputStream os) {
		this.is = is;
		this.os = os;
	}

	@Override
	public void initialize( ) {
		// Initialize measurement info array and map for records
		// before messaging these two objects will be combined into request tuple
		measurementsInfo = new ArrayList<>();
		recordsMap = new HashMap<>();
	}

	@Override
	public void handleNewDataset(int datasetId, long timestamp, String measurerName) {
		// Create measurementsInfo with given information and related recordBuilder
		MeasurementInfo info = MeasurementInfo.newBuilder()
				.setId(datasetId)
				.setMeasurerName(measurerName)
				.setTimestamp(timestamp)
				.build();

		Records.Builder record = Records.newBuilder();

		measurementsInfo.add(info);
		recordsMap.put(datasetId, record);
	}

	@Override
	public void handleValue(int datasetId, DataType type, double value) {
		// Add value to recordBuilder according to datasetId and data type
		switch (type) {
			case DOWNLOAD -> recordsMap.get(datasetId).addDownload(value);
			case UPLOAD -> recordsMap.get(datasetId).addUpload(value);
			case PING -> recordsMap.get(datasetId).addPing(value);
		}
	}

	@Override
	public void getResults(ResultConsumer consumer) throws IOException {
		// Create final request by combining all measurement info and related records
		MeasurementsRequest.Builder requestBuild = MeasurementsRequest.newBuilder();

		for (MeasurementInfo info : measurementsInfo) {
			requestBuild.addRequestTuple(MeasurementsRequest.RequestTuple.newBuilder()
										.setMeasurementInfo(info)
										.setRecords(recordsMap.get(info.getId()).build())
										.build());
		}

		// Send size of message and after the Protobuf data to the output stream
		MeasurementsRequest request = requestBuild.build();
		int sizeInt = request.getSerializedSize();
		byte[] sizeByte = ByteBuffer.allocate(4).putInt(sizeInt).array();

		os.write(sizeByte);
		request.writeTo(os);

		// Parse response from the input stream and feed it to consumer xD
		MeasurementsResponse response = MeasurementsResponse.parseFrom(is);

		for (MeasurementsResponse.ResponseTuple responseTuple : response.getResponseTupleList()) {
			MeasurementInfo info = responseTuple.getMeasurementInfo();
			consumer.acceptMeasurementInfo(info.getId(), info.getTimestamp(), info.getMeasurerName());

			consumer.acceptResult(DataType.DOWNLOAD, responseTuple.getAverage().getDownload());
			consumer.acceptResult(DataType.UPLOAD, responseTuple.getAverage().getUpload());
			consumer.acceptResult(DataType.PING, responseTuple.getAverage().getPing());
		}
	}
}
