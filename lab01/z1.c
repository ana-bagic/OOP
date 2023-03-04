#include <stdio.h>
#include <malloc.h>

typedef char const* (*PTRFUN)();

typedef struct {
    char *name;
    PTRFUN *functions;
} Animal;

void animalPrintGreeting(Animal *animal) {
    printf("%s pozdravlja: %s\n", animal->name, animal->functions[0]());
}
void animalPrintMenu(Animal *animal) {
    printf("%s voli %s\n", animal->name, animal->functions[1]());
}

char const* dogGreet() {
    return "vau!";
}
char const* dogMenu() {
    return "kuhanu govedinu";
}
char const* catGreet() {
    return "mijau!";
}
char const* catMenu() {
    return "konzerviranu tunjevinu";
}

PTRFUN DogFunctions[2] = {dogGreet, dogMenu};
PTRFUN CatFunctions[2] = {catGreet, catMenu};

void constructDog(Animal *animal, char *name) {
    animal->functions = DogFunctions;
    animal->name = name;
}
Animal* createDog(char *name) {
    Animal *animal = (Animal*)malloc(sizeof(Animal));
    constructDog(animal, name);
    return animal;
}
void constructCat(Animal *animal, char *name) {
    animal->functions = CatFunctions;
    animal->name = name;
}
Animal* createCat(char *name) {
    Animal *animal = (Animal*)malloc(sizeof(Animal));
    constructCat(animal, name);
    return animal;
}
Animal* createDogs(int n) {
    Animal *dogs = malloc(n*sizeof(Animal));
    Animal *it = dogs;
    for(int i = 0; i < n; i++) {
        constructDog(it++, "Dog");
    }
    return dogs;
}

void testAnimals() {
    Animal *p1 = createDog("Hamlet");
    Animal *p2 = createCat("Ofelija");
    Animal *p3 = createDog("Polonije");

    animalPrintGreeting(p1);
    animalPrintGreeting(p2);
    animalPrintGreeting(p3);

    animalPrintMenu(p1);
    animalPrintMenu(p2);
    animalPrintMenu(p3);

    free(p1); free(p2); free(p3);

    int numberOfDogs = 10;
    Animal *dogs = createDogs(numberOfDogs);

    for(int i = 0; i < numberOfDogs; i++) {
        animalPrintGreeting(dogs);
        animalPrintMenu(dogs++);
    }

    free(dogs);
}

int main() {
    testAnimals();

    return 0;
}