#include <iostream>
#include <sys/epoll.h>
#include <sys/timerfd.h>
#include <unistd.h>
#include "epollinstance.h"
#include "epolltimer.h"
#include "epollstdin.h"

using namespace std;

int main(int argc, char *argv[])
{
    EpollInstance ep;
    EpollTimer tim1(1000, ep);
    EpollTimer tim2(1500, ep);
    EpollStdIn sin(ep);
    

    while (1) {
        ep.waitAndHandleEvents();
    }

    return 0;
}
