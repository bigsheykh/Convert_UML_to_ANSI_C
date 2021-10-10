#ifndef _NUMBERCLASS_H_
#define _NUMBERCLASS_H_

#include "AllClasses.h"



union Number
{
	struct
	{
		union Number* this;
		int number;
	};
};

int get_number_GVyIjl8aj(union Number* this);
void set_number_fdZplofJn(union Number* this, int the_number);


#endif
