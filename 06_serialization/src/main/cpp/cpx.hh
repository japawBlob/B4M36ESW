/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


#ifndef CPX_HH_2492565674__H_
#define CPX_HH_2492565674__H_


#include <sstream>
#include "boost/any.hpp"
#include "avro/Specific.hh"
#include "avro/Encoder.hh"
#include "avro/Decoder.hh"

namespace a {
struct AMeasurementInfo {
    int32_t id;
    int64_t timestamp;
    std::string measurerName;
    AMeasurementInfo() :
        id(int32_t()),
        timestamp(int64_t()),
        measurerName(std::string())
        { }
};

struct ARecords {
    std::vector<double > DOWNLOAD;
    std::vector<double > PING;
    std::vector<double > UPLOAD;
    ARecords() :
        DOWNLOAD(std::vector<double >()),
        PING(std::vector<double >()),
        UPLOAD(std::vector<double >())
        { }
};

struct ARequestTuple {
    ARecords records;
    AMeasurementInfo measurementInfo;
    ARequestTuple() :
        records(ARecords()),
        measurementInfo(AMeasurementInfo())
        { }
};

struct AMeasurementsRequest {
    std::vector<ARequestTuple > requestTuple;
    AMeasurementsRequest() :
        requestTuple(std::vector<ARequestTuple >())
        { }
};

struct AAverage {
    double DOWNLOAD;
    double PING;
    double UPLOAD;
    AAverage() :
        DOWNLOAD(double()),
        PING(double()),
        UPLOAD(double())
        { }
};

struct AResponseTuple {
    AAverage average;
    AMeasurementInfo measurementInfo;
    AResponseTuple() :
        average(AAverage()),
        measurementInfo(AMeasurementInfo())
        { }
};

struct AMeasurementsResponse {
    std::vector<AResponseTuple > responseTuple;
    AMeasurementsResponse() :
        responseTuple(std::vector<AResponseTuple >())
        { }
};

struct _measurements_avsc_Union__0__ {
private:
    size_t idx_;
    boost::any value_;
public:
    size_t idx() const { return idx_; }
    AMeasurementInfo get_AMeasurementInfo() const;
    void set_AMeasurementInfo(const AMeasurementInfo& v);
    ARecords get_ARecords() const;
    void set_ARecords(const ARecords& v);
    ARequestTuple get_ARequestTuple() const;
    void set_ARequestTuple(const ARequestTuple& v);
    AMeasurementsRequest get_AMeasurementsRequest() const;
    void set_AMeasurementsRequest(const AMeasurementsRequest& v);
    AAverage get_AAverage() const;
    void set_AAverage(const AAverage& v);
    AResponseTuple get_AResponseTuple() const;
    void set_AResponseTuple(const AResponseTuple& v);
    AMeasurementsResponse get_AMeasurementsResponse() const;
    void set_AMeasurementsResponse(const AMeasurementsResponse& v);
    _measurements_avsc_Union__0__();
};

inline
AMeasurementInfo _measurements_avsc_Union__0__::get_AMeasurementInfo() const {
    if (idx_ != 0) {
        throw avro::Exception("Invalid type for union");
    }
    return boost::any_cast<AMeasurementInfo >(value_);
}

inline
void _measurements_avsc_Union__0__::set_AMeasurementInfo(const AMeasurementInfo& v) {
    idx_ = 0;
    value_ = v;
}

inline
ARecords _measurements_avsc_Union__0__::get_ARecords() const {
    if (idx_ != 1) {
        throw avro::Exception("Invalid type for union");
    }
    return boost::any_cast<ARecords >(value_);
}

inline
void _measurements_avsc_Union__0__::set_ARecords(const ARecords& v) {
    idx_ = 1;
    value_ = v;
}

inline
ARequestTuple _measurements_avsc_Union__0__::get_ARequestTuple() const {
    if (idx_ != 2) {
        throw avro::Exception("Invalid type for union");
    }
    return boost::any_cast<ARequestTuple >(value_);
}

inline
void _measurements_avsc_Union__0__::set_ARequestTuple(const ARequestTuple& v) {
    idx_ = 2;
    value_ = v;
}

inline
AMeasurementsRequest _measurements_avsc_Union__0__::get_AMeasurementsRequest() const {
    if (idx_ != 3) {
        throw avro::Exception("Invalid type for union");
    }
    return boost::any_cast<AMeasurementsRequest >(value_);
}

inline
void _measurements_avsc_Union__0__::set_AMeasurementsRequest(const AMeasurementsRequest& v) {
    idx_ = 3;
    value_ = v;
}

inline
AAverage _measurements_avsc_Union__0__::get_AAverage() const {
    if (idx_ != 4) {
        throw avro::Exception("Invalid type for union");
    }
    return boost::any_cast<AAverage >(value_);
}

inline
void _measurements_avsc_Union__0__::set_AAverage(const AAverage& v) {
    idx_ = 4;
    value_ = v;
}

inline
AResponseTuple _measurements_avsc_Union__0__::get_AResponseTuple() const {
    if (idx_ != 5) {
        throw avro::Exception("Invalid type for union");
    }
    return boost::any_cast<AResponseTuple >(value_);
}

inline
void _measurements_avsc_Union__0__::set_AResponseTuple(const AResponseTuple& v) {
    idx_ = 5;
    value_ = v;
}

inline
AMeasurementsResponse _measurements_avsc_Union__0__::get_AMeasurementsResponse() const {
    if (idx_ != 6) {
        throw avro::Exception("Invalid type for union");
    }
    return boost::any_cast<AMeasurementsResponse >(value_);
}

inline
void _measurements_avsc_Union__0__::set_AMeasurementsResponse(const AMeasurementsResponse& v) {
    idx_ = 6;
    value_ = v;
}

inline _measurements_avsc_Union__0__::_measurements_avsc_Union__0__() : idx_(0), value_(AMeasurementInfo()) { }
}
namespace avro {
template<> struct codec_traits<a::AMeasurementInfo> {
    static void encode(Encoder& e, const a::AMeasurementInfo& v) {
        avro::encode(e, v.id);
        avro::encode(e, v.timestamp);
        avro::encode(e, v.measurerName);
    }
    static void decode(Decoder& d, a::AMeasurementInfo& v) {
        if (avro::ResolvingDecoder *rd =
            dynamic_cast<avro::ResolvingDecoder *>(&d)) {
            const std::vector<size_t> fo = rd->fieldOrder();
            for (std::vector<size_t>::const_iterator it = fo.begin();
                it != fo.end(); ++it) {
                switch (*it) {
                case 0:
                    avro::decode(d, v.id);
                    break;
                case 1:
                    avro::decode(d, v.timestamp);
                    break;
                case 2:
                    avro::decode(d, v.measurerName);
                    break;
                default:
                    break;
                }
            }
        } else {
            avro::decode(d, v.id);
            avro::decode(d, v.timestamp);
            avro::decode(d, v.measurerName);
        }
    }
};

template<> struct codec_traits<a::ARecords> {
    static void encode(Encoder& e, const a::ARecords& v) {
        avro::encode(e, v.DOWNLOAD);
        avro::encode(e, v.PING);
        avro::encode(e, v.UPLOAD);
    }
    static void decode(Decoder& d, a::ARecords& v) {
        if (avro::ResolvingDecoder *rd =
            dynamic_cast<avro::ResolvingDecoder *>(&d)) {
            const std::vector<size_t> fo = rd->fieldOrder();
            for (std::vector<size_t>::const_iterator it = fo.begin();
                it != fo.end(); ++it) {
                switch (*it) {
                case 0:
                    avro::decode(d, v.DOWNLOAD);
                    break;
                case 1:
                    avro::decode(d, v.PING);
                    break;
                case 2:
                    avro::decode(d, v.UPLOAD);
                    break;
                default:
                    break;
                }
            }
        } else {
            avro::decode(d, v.DOWNLOAD);
            avro::decode(d, v.PING);
            avro::decode(d, v.UPLOAD);
        }
    }
};

template<> struct codec_traits<a::ARequestTuple> {
    static void encode(Encoder& e, const a::ARequestTuple& v) {
        avro::encode(e, v.records);
        avro::encode(e, v.measurementInfo);
    }
    static void decode(Decoder& d, a::ARequestTuple& v) {
        if (avro::ResolvingDecoder *rd =
            dynamic_cast<avro::ResolvingDecoder *>(&d)) {
            const std::vector<size_t> fo = rd->fieldOrder();
            for (std::vector<size_t>::const_iterator it = fo.begin();
                it != fo.end(); ++it) {
                switch (*it) {
                case 0:
                    avro::decode(d, v.records);
                    break;
                case 1:
                    avro::decode(d, v.measurementInfo);
                    break;
                default:
                    break;
                }
            }
        } else {
            avro::decode(d, v.records);
            avro::decode(d, v.measurementInfo);
        }
    }
};

template<> struct codec_traits<a::AMeasurementsRequest> {
    static void encode(Encoder& e, const a::AMeasurementsRequest& v) {
        avro::encode(e, v.requestTuple);
    }
    static void decode(Decoder& d, a::AMeasurementsRequest& v) {
        if (avro::ResolvingDecoder *rd =
            dynamic_cast<avro::ResolvingDecoder *>(&d)) {
            const std::vector<size_t> fo = rd->fieldOrder();
            for (std::vector<size_t>::const_iterator it = fo.begin();
                it != fo.end(); ++it) {
                switch (*it) {
                case 0:
                    avro::decode(d, v.requestTuple);
                    break;
                default:
                    break;
                }
            }
        } else {
            avro::decode(d, v.requestTuple);
        }
    }
};

template<> struct codec_traits<a::AAverage> {
    static void encode(Encoder& e, const a::AAverage& v) {
        avro::encode(e, v.DOWNLOAD);
        avro::encode(e, v.PING);
        avro::encode(e, v.UPLOAD);
    }
    static void decode(Decoder& d, a::AAverage& v) {
        if (avro::ResolvingDecoder *rd =
            dynamic_cast<avro::ResolvingDecoder *>(&d)) {
            const std::vector<size_t> fo = rd->fieldOrder();
            for (std::vector<size_t>::const_iterator it = fo.begin();
                it != fo.end(); ++it) {
                switch (*it) {
                case 0:
                    avro::decode(d, v.DOWNLOAD);
                    break;
                case 1:
                    avro::decode(d, v.PING);
                    break;
                case 2:
                    avro::decode(d, v.UPLOAD);
                    break;
                default:
                    break;
                }
            }
        } else {
            avro::decode(d, v.DOWNLOAD);
            avro::decode(d, v.PING);
            avro::decode(d, v.UPLOAD);
        }
    }
};

template<> struct codec_traits<a::AResponseTuple> {
    static void encode(Encoder& e, const a::AResponseTuple& v) {
        avro::encode(e, v.average);
        avro::encode(e, v.measurementInfo);
    }
    static void decode(Decoder& d, a::AResponseTuple& v) {
        if (avro::ResolvingDecoder *rd =
            dynamic_cast<avro::ResolvingDecoder *>(&d)) {
            const std::vector<size_t> fo = rd->fieldOrder();
            for (std::vector<size_t>::const_iterator it = fo.begin();
                it != fo.end(); ++it) {
                switch (*it) {
                case 0:
                    avro::decode(d, v.average);
                    break;
                case 1:
                    avro::decode(d, v.measurementInfo);
                    break;
                default:
                    break;
                }
            }
        } else {
            avro::decode(d, v.average);
            avro::decode(d, v.measurementInfo);
        }
    }
};

template<> struct codec_traits<a::AMeasurementsResponse> {
    static void encode(Encoder& e, const a::AMeasurementsResponse& v) {
        avro::encode(e, v.responseTuple);
    }
    static void decode(Decoder& d, a::AMeasurementsResponse& v) {
        if (avro::ResolvingDecoder *rd =
            dynamic_cast<avro::ResolvingDecoder *>(&d)) {
            const std::vector<size_t> fo = rd->fieldOrder();
            for (std::vector<size_t>::const_iterator it = fo.begin();
                it != fo.end(); ++it) {
                switch (*it) {
                case 0:
                    avro::decode(d, v.responseTuple);
                    break;
                default:
                    break;
                }
            }
        } else {
            avro::decode(d, v.responseTuple);
        }
    }
};

template<> struct codec_traits<a::_measurements_avsc_Union__0__> {
    static void encode(Encoder& e, a::_measurements_avsc_Union__0__ v) {
        e.encodeUnionIndex(v.idx());
        switch (v.idx()) {
        case 0:
            avro::encode(e, v.get_AMeasurementInfo());
            break;
        case 1:
            avro::encode(e, v.get_ARecords());
            break;
        case 2:
            avro::encode(e, v.get_ARequestTuple());
            break;
        case 3:
            avro::encode(e, v.get_AMeasurementsRequest());
            break;
        case 4:
            avro::encode(e, v.get_AAverage());
            break;
        case 5:
            avro::encode(e, v.get_AResponseTuple());
            break;
        case 6:
            avro::encode(e, v.get_AMeasurementsResponse());
            break;
        }
    }
    static void decode(Decoder& d, a::_measurements_avsc_Union__0__& v) {
        size_t n = d.decodeUnionIndex();
        if (n >= 7) { throw avro::Exception("Union index too big"); }
        switch (n) {
        case 0:
            {
                a::AMeasurementInfo vv;
                avro::decode(d, vv);
                v.set_AMeasurementInfo(vv);
            }
            break;
        case 1:
            {
                a::ARecords vv;
                avro::decode(d, vv);
                v.set_ARecords(vv);
            }
            break;
        case 2:
            {
                a::ARequestTuple vv;
                avro::decode(d, vv);
                v.set_ARequestTuple(vv);
            }
            break;
        case 3:
            {
                a::AMeasurementsRequest vv;
                avro::decode(d, vv);
                v.set_AMeasurementsRequest(vv);
            }
            break;
        case 4:
            {
                a::AAverage vv;
                avro::decode(d, vv);
                v.set_AAverage(vv);
            }
            break;
        case 5:
            {
                a::AResponseTuple vv;
                avro::decode(d, vv);
                v.set_AResponseTuple(vv);
            }
            break;
        case 6:
            {
                a::AMeasurementsResponse vv;
                avro::decode(d, vv);
                v.set_AMeasurementsResponse(vv);
            }
            break;
        }
    }
};

}
#endif
