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

void set_chars_oOlWaiZec(union FixedSizeString* this, char* characters, int size_of_string);
char* get_chars_vJH68lMu2(union FixedSizeString* this);

void constructorFixedSizeString_05ltxcABX(union FixedSizeString* this);
union FixedSizeString* newFixedSizeString_05ltxcABX();
void constructorFixedSizeString_27gidrYIq(union FixedSizeString* this, int size_of_string);
union FixedSizeString* newFixedSizeString_27gidrYIq(int size_of_string);

void destructorFixedSizeString(union FixedSizeString* this);
void delete_keyword_8GLaBbnUo(union FixedSizeString* this);

#endif
