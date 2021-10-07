#include <stdio.h>

using namespace std;

class Car
{
public:
    void info()
    {
    	puts("car");
    }

    void model()
    {
    	puts("none");
    }
};

class Benz:public Car
{
public:
    using Car::model;

    void info()
    {
    	puts("Benz");
    }

    virtual void model(int year)
    {
    	if(year < 2010)
    	    puts("old");
    	else
    	    puts("new");
    }
};

int main()
{
    Car car;
    Benz benz;
    car.info();
    car.model();
    benz.model(2007);
    benz.info();
    benz.model();
    benz.model(2019);
}