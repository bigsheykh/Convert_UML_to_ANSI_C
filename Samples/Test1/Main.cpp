#include <stdio.h>

using namespace std;

class Empty
{
public:
    Empty()
    {
        printf("Nothing is here!\n");
    }
    Empty(int i)
    {
        printf("input:%d\n", i);
    }
    ~Empty()
    {
        printf("Destructor called.\n");
    }
};

int testNonPointer()
{
    Empty e1(12), e2;
    return 7;
}

void testWithPointer()
{
    Empty* e3;
    e3 = new Empty(4);
    delete e3;
}

int main()
{
    testNonPointer();
    testWithPointer();
}