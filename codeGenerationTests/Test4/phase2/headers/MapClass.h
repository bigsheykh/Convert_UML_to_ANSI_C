#ifndef _MAPCLASS_H_
#define _MAPCLASS_H_

#include "AllClasses.h"



union Map
{
	struct
	{
		union Map* this;
		class Point points[8];
	};
};

void set_point_6xLE8tyZB(union Map* this, int index, int x, int y);
class Point get_point_pJ0cdCxyo(union Map* this, int index);


#endif
