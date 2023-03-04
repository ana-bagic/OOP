#include <iostream>
#include <stdlib.h>
#include <string.h>

template <typename Iterator, typename Predicate>
Iterator mymax(Iterator first, Iterator last, Predicate pred){
    Iterator max = first;
    while(first != last) {
        if(pred(*first, *max) == 1)
            max = first;
        first++;
    }
    return max;
}

int gt_int(const int a, const int b) {
    return a > b ? 1 : 0;
}

int gt_char(const char a, const char b) {
    return a > b ? 1 : 0;
}

int gt_str(const char* a, const char* b) {
    return strcmp(a, b) > 0 ? 1 : 0;
}

int main() {
    int arr_int[] = {1, 3, 5, 7, 4, 6, 9, 2, 0};
    char arr_char[] = "Suncana strana ulice";
    const char* arr_str[] = {
    "Gle", "malu", "vocku", "poslije", "kise",
    "Puna", "je", "kapi", "pa", "ih", "njise"
    };

    const int* maxInt = mymax(&arr_int[0], &arr_int[sizeof(arr_int)/sizeof(*arr_int)], gt_int);
    std::cout << *maxInt << "\n";
    const char* maxChar = mymax(&arr_char[0], &arr_char[sizeof(arr_char)/sizeof(*arr_char)], gt_char);
    std::cout << *maxChar << "\n";
    const char* maxStr = *mymax(&arr_str[0], &arr_str[sizeof(arr_str)/sizeof(*arr_str)], gt_str);
    std::cout << maxStr << "\n";

    return 0;
}