#ifndef LIST_H
#define LIST_H

#include "definitions.hpp"
#include <pthread.h>
#ifdef USE_RCU
#include <urcu-qsbr.h>		/* Userspace RCU flavor */
#include <urcu/rculist.h>	/* RCU list */
#include <urcu/compiler.h>	/* For CAA_ARRAY_SIZE */
#endif

#if defined (USE_MUTEX) || defined (USE_RWLOCK)
#define LIST_TYPE esw_list_t
#elif defined (USE_RCU)
#define LIST_TYPE struct cds_list_head
#else
#error "No lock type defined"
#endif

typedef struct esw_node {
    char * key;
    char * value;
    int checksum;
#if defined (USE_MUTEX) || defined (USE_RWLOCK)
    struct esw_node * next;
    struct esw_node * prev;
#elif defined (USE_RCU)
    // TODO
#else
#error "No lock type defined"
#endif
} esw_node_t;

typedef struct esw_list {
    esw_node_t * head;
#if defined (USE_MUTEX)
    pthread_mutex_t lock;
#elif defined (USE_RWLOCK)
    pthread_rwlock_t lock;
#endif
} esw_list_t;

void esw_list_init(LIST_TYPE * list);
void esw_list_push(LIST_TYPE * list, const char * const key, const char * const address);
void esw_list_update(LIST_TYPE * list, const char * const key, const char * const address);
bool esw_list_find(LIST_TYPE * list, const char * const key, char * address, const size_t max_len);
esw_node_t * esw_list_create_node(const char * const key, const char * const address);
void esw_list_free_node(esw_node_t * node);
void esw_list_free_content(LIST_TYPE * list);
void esw_list_free(LIST_TYPE * list);
void esw_list_print(LIST_TYPE * list);
void esw_list_node_print(esw_node_t * list);

#endif // LIST_H
