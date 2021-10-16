#ifndef _POINTCLASS_H_
#define _POINTCLASS_H_

#include "AllClasses.h"



union Point
{
	struct
	{
		union Point* this;
		int x;
		int y;
	};
};

void set_mJyYeh5po(union Point* this, int x, int y);


#endif
