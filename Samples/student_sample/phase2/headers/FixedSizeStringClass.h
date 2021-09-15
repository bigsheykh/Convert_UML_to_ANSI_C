#ifndef _FIXEDSIZESTRINGCLASS_H_
#define _FIXEDSIZESTRINGCLASS_H_

#include "AllClasses.h"

#include "ObjectClass.h"
#include "StringClass.h"


union FixedSizeString
{
	union Object unionObject;
	union String unionString;
	struct
	{
		union FixedSizeString* this;
		char* all;
		int size;
	};
};

void set_chars(union FixedSizeString* this, char* characters, int size_of_string);
char* get_chars(union FixedSizeString* this);

void constructorFixedSizeString(union FixedSizeString* this);
union FixedSizeString* newFixedSizeString();
void constructorFixedSizeString(union FixedSizeString* this, int size_of_string);
union FixedSizeString* newFixedSizeString(int size_of_string);

void destructorFixedSizeString(union FixedSizeString* this);
void delete(union FixedSizeString* this);

#endif
