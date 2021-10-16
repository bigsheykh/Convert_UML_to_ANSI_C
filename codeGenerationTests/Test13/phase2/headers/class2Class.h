#ifndef _CLASS2CLASS_H_
#define _CLASS2CLASS_H_

#include "AllClasses.h"

#include "Class1Class.h"


union class2
{
	union Class1 unionClass1;
	struct
	{
		union class2* this;
		int constant;
	};
};



void destructorclass2(union class2* this);
void delete_keyword_g5s2SOfPV(union class2* this);

#endif
