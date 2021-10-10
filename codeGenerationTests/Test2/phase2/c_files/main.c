#include "overload.h"

int main()
{
    class Car car;
    class Benz benz;
    car.info();
    car.model();
    benz.model(2007);
    benz.info();
    benz.model();
    benz.model(2019);
}