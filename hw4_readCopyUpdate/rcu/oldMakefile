CXXFLAGS = -static -O3 -g -Wall
LDFLAGS = -L/home/japaw/rcu_folder/mybuild/output/target/lib
LIBS = -lurcu -lpthread -lrt
export PKG_CONFIG_PATH = /home/japaw/rcu_folder/mybuild/output/target/lib/pkgconfig
INCLUDE = -I/home/japaw/rcu_folder/mybuild/output/target/include
SRCS = main.cpp list.cpp

ALL = list_mutex list_rwlock list_rcu

all: $(ALL)

list_%: *.cpp *.hpp Makefile
	$(CXX) $(CXXFLAGS) $(DEFINES) -o $@ $(SRCS) $(LDFLAGS) $(LIBS)

list_mutex: DEFINES = -DUSE_MUTEX
list_rwlock: DEFINES = -DUSE_RWLOCK
list_rcu: DEFINES = -DUSE_RCU
list_rcu: CXXFLAGS += $(shell pkg-config --cflags liburcu-qsbr) $(INCLUDE)
list_rcu: LIBS := $(shell pkg-config --libs liburcu-qsbr) $(LIBS)

clean:
	rm -f $(ALL)
