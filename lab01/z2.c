#include <stdio.h>
#include <stdlib.h>
#include <malloc.h>
#include <stdbool.h>

typedef double (*PTRFUN)();

typedef struct {
    int lower_bound;
    int upper_bound;
    PTRFUN *functions;
} UnaryFunction;

double negativeValueAt(UnaryFunction *func, double x) {
    return -(func->functions[0](func, x));
}

void tabulate(UnaryFunction *func) {
    for(int x = func->lower_bound; x <= func->upper_bound; x++) {
        printf("f(%d) = %lf\n", x, func->functions[0](func, (double)x));
    }
}

bool sameFunctionsForInts(UnaryFunction *f1, UnaryFunction *f2, double tolerance) {
    if(f1->lower_bound != f2->lower_bound)
        return false;
    if(f1->upper_bound != f2->upper_bound)
        return false;

    for(int x = f1->lower_bound; x <= f1->upper_bound; x++) {
        double delta = f1->functions[0](f1, x) - f2->functions[0](f2, x);
        if(delta < 0)
            delta = -delta;
        if(delta > tolerance)
            return false;
    }
    
    return true;
}

typedef struct {
    UnaryFunction super;
} Square;
typedef struct {
    UnaryFunction super;
    double a;
    double b;
} Linear;

double valueAtSquare(Square *func, double x) {
    return x*x;
}
double valueAtLinear(Linear *func, double x) {
    return (func->a)*x + func->b;
}

PTRFUN SquareFunctions[2] = {valueAtSquare, negativeValueAt};
PTRFUN LinearFunctions[2] = {valueAtLinear, negativeValueAt};

Square* createSquare(int lower_bound, int upper_bound) {
    Square* square = malloc(sizeof(Square));
    square->super.lower_bound = lower_bound;
    square->super.upper_bound = upper_bound;
    square->super.functions = SquareFunctions;
    return square;
}
Linear* createLinear(int lower_bound, int upper_bound, double a, double b) {
    Linear* linear = malloc(sizeof(Linear));
    linear->super.lower_bound = lower_bound;
    linear->super.upper_bound = upper_bound;
    linear->super.functions = LinearFunctions;
    linear->a = a;
    linear->b = b;
    return linear;
}

int main() {
    UnaryFunction *f1 = (UnaryFunction*)createSquare(-2, 2);
    tabulate(f1);
    printf("\n");
    UnaryFunction *f2 = (UnaryFunction*)createLinear(-2, 2, 5, -2);
    tabulate(f2);

    printf("\n");
    printf("f1 == f2: %s\n", sameFunctionsForInts(f1, f2, 1E-6) ? "DA" : "NE");
    printf("negVal f2(1) = %lf\n", f2->functions[1](f2, 1.0));
  
    free(f1); free(f2);

    return 0;
}