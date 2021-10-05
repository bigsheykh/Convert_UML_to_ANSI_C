#include <stdio.h>
#include "overload.h"

int testNonPointer()
{
    class Empty e1(12),e2();
    return 7;
}

void testWithPointer()
{
    class Empty* e3;
    e3 = new Empty(4);
    delete(e3);
}

int main()
{
    testNonPointer();
    testWithPointer();
}