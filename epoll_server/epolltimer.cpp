#include <sys/timerfd.h>
#include <sys/epoll.h>
#include <unistd.h>
#include <iostream>
#include <cstring>
#include <stdexcept>
#include "epolltimer.h"

EpollTimer::EpollTimer(uint32_t timeMs, EpollInstance &e) : EpollFd(-1, e)
{
    struct itimerspec its;
    memset(&its, 0, sizeof(its));

    fd = timerfd_create(CLOCK_MONOTONIC, TFD_NONBLOCK);
    if (fd == -1) {
        throw std::runtime_error(std::string("timerfd_create: ") + std::strerror(errno));
    }

    its.it_interval.tv_sec = timeMs / 1000;
    its.it_interval.tv_nsec = (timeMs % 1000) * 1000000;
    its.it_value.tv_sec = timeMs / 1000;
    its.it_value.tv_nsec = (timeMs % 1000) * 1000000;

    if (timerfd_settime(fd, 0, &its, NULL)) {
        throw std::runtime_error(std::string("timerfd_settime: ") + std::strerror(errno));
    }

    registerFd(EPOLLIN);
}

EpollTimer::~EpollTimer()
{
    unregisterFd();
}

void EpollTimer::handleEvent(uint32_t events)
{
    uint64_t value;
    if ((events & EPOLLERR) || (events & EPOLLHUP) || !(events & EPOLLIN)) {
        unregisterFd();
    } else {
        read(fd, &value, 8);
        std::cout << "timer: " << fd << std::endl;
    }
}
