cmake_minimum_required(VERSION 2.8)

project(server)

SET(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")

find_package(PkgConfig REQUIRED)
FIND_PACKAGE(Boost 1.42.0 REQUIRED COMPONENTS system thread regex)
IF(Boost_FOUND)
  INCLUDE_DIRECTORIES(${Boost_INCLUDE_DIRS})
  LINK_DIRECTORIES(${Boost_LIBRARY_DIRS})
ENDIF(Boost_FOUND)

FIND_PACKAGE(Protobuf REQUIRED)
IF(Protobuf_FOUND)
  INCLUDE_DIRECTORIES(${Protobuf_INCLUDE_DIRS})
  LINK_DIRECTORIES(${Protobuf_LIBRARY_DIRS})
ENDIF(Protobuf_FOUND)

pkg_check_modules(JSONCPP jsoncpp)
link_libraries(${JSONCPP_LIBRARIES})

SET(USED_LIBS ${Boost_SYSTEM_LIBRARY} ${Boost_THREAD_LIBRARY} ${Boost_REGEX_LIBRARY} ${Protobuf_LIBRARY})

add_executable(${PROJECT_NAME} "main.cpp" "dataset.cpp" "measurementinfo.cpp" "result.cpp" "measurements.pb.cc")
target_link_libraries(${PROJECT_NAME} ${JSONCPP_LIBRARIES} ${USED_LIBS} avrocpp_s)