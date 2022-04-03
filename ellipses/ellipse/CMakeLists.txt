cmake_minimum_required(VERSION 2.8)

set(CMAKE_CXX_STANDARD 11)

set(CMAKE_CXX_FLAGS  "${CMAKE_CXX_FLAGS} -g -O3 -Wall")

project(find_ellipse)

find_package(OpenCV REQUIRED)
find_package(Boost 1.60 COMPONENTS filesystem REQUIRED )
include_directories( ${Boost_INCLUDE_DIRS} )

add_executable(${PROJECT_NAME} find_ellipse.cpp imgproc.cpp ellipse.cpp)

target_link_libraries(${PROJECT_NAME} ${OpenCV_LIBS})
target_link_libraries(${PROJECT_NAME} ${Boost_LIBRARIES})
