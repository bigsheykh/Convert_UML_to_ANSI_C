#include "overload.h"

void set_map_point(class Map* our_map)
{
    our_map->set_point(3, 4 , 5);
    our_map->set_point(2, 3 , 8);
    our_map->set_point(1, 2 , 5);
}

int main()
{
    class Map X;
    class Map* our_map = &X;
    set_map_point(our_map);
    class Point third;
    third = our_map->get_point(3);
    printf("%d\n", third.x);
    printf("%d\n", our_map->get_point(2).y);
}
