// double_linked_list.c

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "../include/double_linked_list.h"

Node* create_node(const char* data) {
    Node* new_node = (Node*)malloc(sizeof(Node));
    if (new_node == NULL) {
        printf("Memory allocation failed\n");
        exit(1);
    }
    new_node->data = strdup(data);
    new_node->next = NULL;
    new_node->prev = NULL;
    return new_node;
}

DoubleLinkedList* create_double_linked_list() {
    DoubleLinkedList* new_list = (DoubleLinkedList*)malloc(sizeof(DoubleLinkedList));
    if (new_list == NULL) {
        printf("Memory allocation failed\n");
        exit(1);
    }
    new_list->head = NULL;
    new_list->tail = NULL;
    return new_list;
}

void append(DoubleLinkedList* list, const char* data) {
    Node* new_node = create_node(data);
    if (list->head == NULL) {
        list->head = new_node;
        list->tail = new_node;
        return;
    }

    list->tail->next = new_node;
    new_node->prev = list->tail;
    list->tail = new_node;
}

void prepend(DoubleLinkedList* list, const char* data) {
    Node* new_node = create_node(data);
    if (list->head == NULL) {
        list->head = new_node;
        list->tail = new_node;
        return;
    }

    list->head->prev = new_node;
    new_node->next = list->head;
    list->head = new_node;
}

void print_list(DoubleLinkedList* list) {
    Node* current = list->head;
    while (current != NULL) {
        printf("%s\n", current->data);
        current = current->next;
    }
}

void free_list(DoubleLinkedList* list) {
    Node* current = list->head;
    Node* next;
    while (current != NULL) {
        next = current->next;
        free(current->data);
        free(current);
        current = next;
    }
    free(list);
}
