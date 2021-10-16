#include "overload.h"

void test_class2()
{
    class class2 a1, a2;
    a1.constant = 5;
    a2.constant = a1.constant * 5;
    delete(&a1);
    delete(&a2);
}

void test_Class1()
{
    class Class1 a1, a2;
    a1.constant = 5;
    a2.constant = a1.constant / 5;
    delete(&a2);
    delete(&a1);
}

int main()
{
    test_Class1();
    test_class2();
    test_Class1();
}