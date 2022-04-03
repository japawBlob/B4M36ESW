#include "list.hpp"
#include "definitions.hpp"
#include <assert.h>
#include <math.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <chrono>
#include <algorithm>
#include <mutex>
#include <vector>

#if !defined(USE_MUTEX) && !defined(USE_RWLOCK) && !defined(USE_RCU)
#error "Use one of those!"
#endif

#define MAX_ADDR_LEN 40
#define MAX_KEY_LEN 4
#define WRITERS_QUANTITY 1
#define WRITE_PERIOD_US 1000
#define ITEM_QUANTITY 100
#define PREDEF_ADDR_QUANTITY 4
#define TEST_DURATION 3

using namespace std;

constexpr char charset[] = "abcdefghijklmnopqrstuvwxyz";
const char predefined_addresses[][MAX_ADDR_LEN] = { "http://dddddddddddddddddddddddddd.cz",
                                                    "http://cccccccccccccccccccccccccc.cz",
                                                    "http://bbbbbbbbbbbbbbbbbbbbbbbbbb.cz",
                                                    "http://aaaaaaaaaaaaaaaaaaaaaaaaaa.cz" };

static LIST_TYPE list;

static __thread struct stats {
    long reads;
    long writes;
} stats;

mutex all_stats_mutex;
vector<struct stats*> all_stats;

static char used_keys[ITEM_QUANTITY][MAX_KEY_LEN + 1];

#define rand() not_thread_safe

__thread unsigned int seed;

void generate_random_key(char *key, int length)
{
    int len = strlen(charset);
    for (int i = 0; i < length; i++) {
        int r = rand_r(&seed) % len;
        key[i] = charset[r];
    }
    key[length] = '\0';
}

void init_list()
{
    char key[MAX_KEY_LEN + 1];
    int address_index;
    esw_list_init(&list);
    for (int i = 0; i < ITEM_QUANTITY; i++) {
        generate_random_key(key, MAX_KEY_LEN);

        address_index = rand_r(&seed) % PREDEF_ADDR_QUANTITY;

        esw_list_push(&list, key, predefined_addresses[address_index]);

        strcpy(used_keys[i], key);
    }
}

void *reader_thread(void *arg)
{
    char key[MAX_KEY_LEN + 1];
    char address[MAX_ADDR_LEN];

    {
        lock_guard<mutex> lock(all_stats_mutex);
        all_stats.push_back(&stats);
    }

    long hits = 0;
    long miss = 0;

    // TODO - Rcu should have someting here

    while (1) {
        generate_random_key(key, MAX_KEY_LEN);

        if (esw_list_find(&list, key, address, MAX_ADDR_LEN) == true) {
            hits++;
        } else {
            miss++;
        }

        stats.reads++;
    }
}

void *writer_thread(void *arg)
{
    {
        lock_guard<mutex> lock(all_stats_mutex);
        all_stats.push_back(&stats);
    }
    while (1) {
        esw_list_update(&list,
                        used_keys[rand_r(&seed) % ITEM_QUANTITY],
                        predefined_addresses[rand_r(&seed) % PREDEF_ADDR_QUANTITY]);

        usleep(WRITE_PERIOD_US);

        stats.writes++;
    }
}

int main(int argc, char *argv[])
{   
    long reads_local_last = 0;
    long writes_local_last = 0;

    int reader_quantity;

    if (argc != 2) {
        printf("usage: ./list_mutex <reader-thread quantity>\n");
        exit(EXIT_FAILURE);
    }

    reader_quantity = atoi(argv[1]);

    if (reader_quantity < 1 || reader_quantity > 100) {
        printf("min readers: 1, max readers: 100\n");
        exit(EXIT_FAILURE);
    }

    printf("Size of one list element: %zu\n", sizeof(struct esw_node));

    pthread_t *readers = (pthread_t *)calloc(sizeof(pthread_t), reader_quantity);
    pthread_t *writers = (pthread_t *)calloc(sizeof(pthread_t), WRITERS_QUANTITY);

    init_list();

    for (int i = 0; i < WRITERS_QUANTITY; i++)
        if (pthread_create(&writers[i], NULL, writer_thread, NULL) != 0)
            err(1, "pthread_create writer");

    for (int i = 0; i < reader_quantity; i++)
        if (pthread_create(&readers[i], NULL, reader_thread, NULL) != 0)
            err(1, "pthread_create reader");

    for (int i = 0; i < TEST_DURATION; i++) {
        sleep(1);
        struct stats sum = {0};
        for (auto thr : all_stats) {
            sum.reads += thr->reads;
            sum.writes += thr->writes;
        }
            printf("Reads: %-10ld  Writes: %-10ld\n",
                sum.reads - reads_local_last,
                    sum.writes - writes_local_last);
        reads_local_last = sum.reads;
        writes_local_last = sum.writes;
        fflush(stdout);
    }

    for (int i = 0; i < WRITERS_QUANTITY; i++)
        if (pthread_cancel(writers[i]) != 0)
            err(1, "pthread_cancel writer");

    for (int i = 0; i < reader_quantity; i++)
        if (pthread_cancel(readers[i]) != 0)
            err(1, "pthread_cancel reader");

    free(readers);
    free(writers);

    esw_list_free_content(&list);

    return EXIT_SUCCESS;
}
