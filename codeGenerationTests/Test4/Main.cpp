#include <stdio.h>

using namespace std;

class Point
{
public:
    int x,y;
    void set(int x,int y)
    {
        this->x = x;
        this->y = y;
    }
};

class Map
{
public:
    Point points[8];
    void set_point(int index, int x, int y)
    {
    	points[index].set(x, y);
    }

    Point get_point(int index)
    {
    	return points[index];
    }
};

int main()
{
    Map our_map;
    our_map.set_point(3, 4 , 5);
    our_map.set_point(2, 3 , 8);
    our_map.set_point(1, 2 , 5);
    Point third;
    third = our_map.get_point(3);
    printf("%d\n", third.x);
    printf("%d\n", our_map.get_point(2).y);

}