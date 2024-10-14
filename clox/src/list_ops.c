#include "../include/double_linked_list.h"

int main() {
    DoubleLinkedList* list = create_double_linked_list();
    append(list, "Hello");
    append(list, "World");
    prepend(list, "Goodbye");
    print_list(list);
    return 0;
}
