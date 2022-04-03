CXX = g++
CC = $(CXX) # Use g++ for linking
CXXFLAGS = -g -O3 -Wall -std=c++11 $(shell pkg-config --cflags opencv4)
LDLIBS = -lboost_filesystem -lboost_system $(shell pkg-config --libs opencv4)

all: find_ellipse

find_ellipse: find_ellipse.o imgproc.o ellipse.o

%.cpp: *.h

clean:
	rm -f find_ellipse *.o
