#ifndef _STRINGCLASS_H_
#define _STRINGCLASS_H_

#include "AllClasses.h"

#include "ObjectClass.h"


union String
{
	union Object unionObject;
	struct
	{
		union String* this;
		char* all;
		int size;
	};
};

void set_chars(union String* this, char* characters, int size_of_string);
char* get_chars(union String* this);


#endif
