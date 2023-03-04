#include <windows.h>
#include <string.h>

typedef void *(*CREATE)(char const*); 

void* myfactory(char const* libname, char const* ctorarg) {
    char libpath[50] = "./";
    strcat(libpath, libname);
    strcat(libpath, ".dll");

    HINSTANCE newClass = LoadLibrary(libpath);
    if(newClass != NULL) {
        CREATE constructor = (CREATE) GetProcAddress(newClass, "create");

        if(constructor != NULL) {
            return constructor(ctorarg);
        }
    }
    return NULL;
}