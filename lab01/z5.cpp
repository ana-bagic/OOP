#include <stdio.h>

class B {
public:
    virtual int __cdecl prva() = 0;
    virtual int __cdecl druga(int) = 0;
};

class D: public B {
public:
    virtual int __cdecl prva() {return 42;}
    virtual int __cdecl druga(int x) {return prva() + x;}
};

typedef int (*PTRFUN1)(B*);
typedef int (*PTRFUN2)(B*, int);

void printValues(B *pb) {
    printf("prva: %d, druga: %d\n", (*(PTRFUN1**)pb)[0](pb), (*(PTRFUN2**)pb)[1](pb, 5));
}

int main() {
    D *d = new D();
    printValues(d);
    return 0;
}