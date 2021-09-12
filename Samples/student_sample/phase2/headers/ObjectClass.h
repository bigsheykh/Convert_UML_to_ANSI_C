#ifndef _OBJECTCLASS_H_
#define _OBJECTCLASS_H_

#include "AllClasses.h"



union Object
{
	struct
	{
		union Object* this;
	};
};



#endif
