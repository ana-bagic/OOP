#include <stdlib.h>

typedef char const *(*PTRFUN)();

typedef struct {
    PTRFUN *vtable;
    char const *name;
} Tiger;

char const* name(void* this) {
    return ((Tiger*)this)->name;
}
char const* greet() {
    return "Roar!";
}
char const* menu() {
    return "meso";
}

PTRFUN functions[3] = {name, greet, menu};

void* create(char const *name) {
    Tiger *tiger = (Tiger*)malloc(sizeof(Tiger));
    tiger->vtable = functions;
    tiger->name = name;
    return tiger;
}