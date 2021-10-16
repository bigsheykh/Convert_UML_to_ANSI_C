#include <stdio.h>

using namespace std;

class Number
{
public:
    int number;
    
    void set_number(int the_number)
    {
        number = the_number;
    }
    
    int get_number()
    {
        return number;
    }  
};

class Square:public Number
{
public:
    int get_square()
    {
        return get_number() * number;
    }
};

class Cube:public Square
{
public:
    int get_cube()
    {
        return get_number() * get_square();
    }
};

int main()
{
    Cube cube;
    cube.set_number(7);
    printf("%d\n", cube.get_number());
    printf("%d\n", cube.get_square());
    printf("%d\n", cube.get_cube());

    Square square = static_cast<Square>(cube);
    printf("%d\n", square.get_number());
    printf("%d\n", square.get_square());
    square.set_number(5);
    printf("%d\n", square.get_number());
    printf("%d\n", square.get_square());

    Number number_1 = static_cast<Number>(cube);
    printf("%d\n", number_1.get_number());

    Number number_2 = static_cast<Number>(square);
    printf("%d\n", number_2.get_number());
}