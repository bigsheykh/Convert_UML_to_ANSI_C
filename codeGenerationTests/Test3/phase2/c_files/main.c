#include "overload.h"

int main()
{
    class Cube cube;
    cube.set_number(7);
    printf("%d\n", cube.get_number());
    printf("%d\n", cube.get_square());
    printf("%d\n", cube.get_cube());

    class Square square = cube.unionSquare;
    printf("%d\n", square.get_number());
    printf("%d\n", square.get_square());
    square.set_number(5);
    printf("%d\n", square.get_number());
    printf("%d\n", square.get_square());

    class Number number_1 = cube.unionNumber;
    printf("%d\n", number_1.get_number());

    class Number number_2 = square.unionNumber;
    printf("%d\n", number_2.get_number());
}