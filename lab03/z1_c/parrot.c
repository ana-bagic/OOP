#include <stdlib.h>
#include <stdio.h>

typedef char const *(*PTRFUN)();

typedef struct {
    PTRFUN *vtable;
    char const *name;
} Parrot;

char const* name(void* this) {
    return ((Parrot*)this)->name;
}
char const* greet() {
    return "Sqwak!";
}
char const* menu() {
    return "sjemenke";
}

PTRFUN functions[3] = {name, greet, menu};

void* create(char const *name) {
    Parrot *parrot = (Parrot*)malloc(sizeof(Parrot));
    parrot->vtable = functions;
    parrot->name = name;
    return parrot;
}