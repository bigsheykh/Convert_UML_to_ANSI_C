#ifndef _SQUARECLASS_H_
#define _SQUARECLASS_H_

#include "AllClasses.h"

#include "NumberClass.h"


union Square
{
	union Number unionNumber;
	struct
	{
		union Square* this;
		int number;
	};
};

int get_number_HhqFwykBD(union Square* this);
void set_number_PCiYHO8yM(union Square* this, int the_number);
int get_square_orOP2K4o8(union Square* this);


#endif
