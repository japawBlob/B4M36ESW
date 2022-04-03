#ifndef EPOLLSTDIN_H
#define EPOLLSTDIN_H

#include <sys/epoll.h>
#include "epollfd.h"
#include "epollinstance.h"

class EpollStdIn : public EpollFd
{
public:
    EpollStdIn(EpollInstance &e);
    ~EpollStdIn();
    void handleEvent(uint32_t events);
};

#endif // EPOLLSTDIN_H
