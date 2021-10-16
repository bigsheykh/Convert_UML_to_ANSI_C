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
        points[index].x = x;
        points[index].y = y;
    }

    Point get_point(int index)
    {
    	Point a;
        a.x = points[index].x;
        a.y = points[index].y;
        return a;
    }
};

void set_map_point(Map* our_map)
{
    our_map->set_point(3, 4 , 5);
    our_map->set_point(2, 3 , 8);
    our_map->set_point(1, 2 , 5);
}

int main()
{
    Map X;
    Map* our_map = &X;
    set_map_point(our_map);
    Point third;
    third = our_map->get_point(3);
    printf("%d\n", third.x);
    printf("%d\n", our_map->get_point(2).y);

}