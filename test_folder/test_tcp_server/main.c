#include <sys/socket.h>
#include <sys/types.h>
#include <arpa/inet.h>
#include <fcntl.h>
#include <string.h>
#include <stdio.h>
#include <unistd.h>

#define BUF_SIZE 1042

int main(){
    int sfd = socket(AF_INET, SOCK_STREAM, 0);

    short int port = 12345;
    struct sockaddr_in saddr;

    memset(&saddr, 0, sizeof(saddr));
    saddr.sin_family      = AF_INET;              // IPv4
    saddr.sin_addr.s_addr = htonl(INADDR_ANY);    // Bind to all available interfaces
    saddr.sin_port        = htons(port);          // Requested port

    bind(sfd, (struct sockaddr *) &saddr, sizeof(saddr));

    int flags = fcntl(sfd, F_GETFL, 0);
    flags |= O_NONBLOCK;
    fcntl(sfd, F_SETFL, flags);
    
    listen(sfd, SOMAXCONN);
    int cfd;
    while (( cfd = accept(sfd, NULL, NULL)) == -1 ){}
    fcntl(cfd, F_SETFL, O_NONBLOCK);
    int count = 0;
    char buffer[BUF_SIZE];
    memset(buffer, 0, BUF_SIZE);
    while(1){
        char tempBuffer [42];
        int tempCount = read(cfd, tempBuffer, 41);

        if(tempCount != -1){
            memcpy(buffer+count, tempBuffer, tempCount);

            count += tempCount;
        }


        if( strstr(buffer, "\n") != NULL ) {
            //count = count >= BUF_SIZE ? BUF_SIZE - 1 : count;
            //unsigned bufferLen = strlen(buffer);
            sprintf(tempBuffer, "%lu\n", strlen(buffer)-1);
            count = write(cfd, tempBuffer, strlen(tempBuffer));
            memset(buffer, 0, BUF_SIZE);
            //sleep(1);

            //char *hello = "hello\n";

            //write(cfd, hello, strlen(hello));

            //fcntl(sfd, F_SETFL, O_NONBLOCK);
            //listen(sfd, SOMAXCONN);
            count = 0;
            if (count > 100) {
                break;
            }
        }
    }
    close(cfd);
    close(sfd);
}