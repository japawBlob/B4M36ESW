#ifndef EPOLLTIMERIN_H
#define EPOLLTIMERIN_H

#include "epollfd.h"

class EpollTimer : public EpollFd
{
public:
    EpollTimer(uint32_t timeMs, EpollInstance &e);
    ~EpollTimer();
    void handleEvent(uint32_t events);
};

#endif // EPOLLTIMERIN_H
