#include "myfactory.h"

#include <stdio.h>
#include <stdlib.h>

typedef char const *(*PTRFUN)();

typedef struct {
    PTRFUN *vtable;
} Animal;

void animalPrintGreeting(Animal *animal) {
    printf("%s pozdravlja: %s\n", animal->vtable[0](animal), animal->vtable[1]());
}
void animalPrintMenu(Animal *animal) {
    printf("%s voli %s\n", animal->vtable[0](animal), animal->vtable[2]());
}

int main(int argc, char *argv[]) {
    for (int i = 1; i < argc; ++i) {
        Animal *p = (Animal *)myfactory(argv[i], "Modrobradi");

        if (!p) {
            printf("Creation of plug-in object %s failed.\n", argv[i]);
            continue;
        }

        animalPrintGreeting(p);
        animalPrintMenu(p);
        free(p);
    }
}