#include <unistd.h>
#include <stdio.h>
#include <iostream>
#include <string>
#include "epollstdin.h"

EpollStdIn::EpollStdIn(EpollInstance &e) : EpollFd(STDIN_FILENO, e)
{
    registerFd(EPOLLIN);
}

EpollStdIn::~EpollStdIn()
{
    unregisterFd();
}

void EpollStdIn::handleEvent(uint32_t events)
{
    std::string line;
    if ((events & EPOLLERR) || (events & EPOLLHUP) || !(events & EPOLLIN)) {
        unregisterFd();
    } else {
        std::getline(std::cin, line);
        std::cout << "stdin line: " << line << std::endl;
    }
}
