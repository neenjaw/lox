// double_linked_list.h

#ifndef DOUBLE_LINKED_LIST_H
#define DOUBLE_LINKED_LIST_H

#include <stdio.h>
#include <stdlib.h>

typedef struct Node {
    char* data;
    struct Node* next;
    struct Node* prev;
} Node;

typedef struct DoubleLinkedList {
    Node* head;
    Node* tail;
} DoubleLinkedList;

Node* create_node(const char* data);

DoubleLinkedList* create_double_linked_list();

void append(DoubleLinkedList* list, const char* data);

void prepend(DoubleLinkedList* list, const char* data);

void print_list(DoubleLinkedList* list);

void free_list(DoubleLinkedList* list);

#endif // DOUBLE_LINKED_LIST_H
