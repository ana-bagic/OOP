#include <stdlib.h>
#include <stdio.h>
#include <string.h>

const void* mymax(const void *base, size_t nmemb, size_t size, int (*compar)(const void *, const void *)) {
    const void* max = base;
    for(int i = 1; i < nmemb; i++) {
        const void* curr = base + (i*size);
        if(compar(curr, max) == 1) {
            max = curr;
        }
    }
    return max;
}

int gt_int(const void * a, const void * b) {
    return (*(int*)a > *(int*)b) ? 1 : 0;
}

int gt_char(const void * a, const void * b) {
    return (*(char*)a > *(char*)b) ? 1 : 0;
}

int gt_str(const void * a, const void * b) {
    char* first = (char*)*(int*)a;
    char* second = (char*)*(int*)b;
    return strcmp(first, second) > 0 ? 1 : 0;
}

int main() {
    int arr_int[] = {1, 3, 5, 7, 4, 6, 9, 2, 0};
    char arr_char[] = "Suncana strana ulice";
    const char* arr_str[] = {
    "Gle", "malu", "vocku", "poslije", "kise",
    "Puna", "je", "kapi", "pa", "ih", "njise"
    };

    printf("%d\n", *(int *)mymax(arr_int, 9, sizeof(int), gt_int));
    printf("%c\n", *(char *)mymax(arr_char, 20, sizeof(char), gt_char));
    printf("%s\n", (char*)*(int*)mymax(arr_str, 11, sizeof(char*), gt_str));

    return 0;
}