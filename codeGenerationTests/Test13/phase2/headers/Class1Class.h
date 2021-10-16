#ifndef _CLASS1CLASS_H_
#define _CLASS1CLASS_H_

#include "AllClasses.h"



union Class1
{
	struct
	{
		union Class1* this;
		int constant;
	};
};



void destructorClass1(union Class1* this);
void delete_keyword_fChmxuvsQ(union Class1* this);

#endif
