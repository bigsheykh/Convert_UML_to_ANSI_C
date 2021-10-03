#include "overload.h"

void set_chars_oOlWaiZec(union FixedSizeString* this, char* characters, int size_of_string)
{
	set_chars(&(this->unionString), characters, size_of_string);
}
char* get_chars_vJH68lMu2(union FixedSizeString* this)
{
	return get_chars(&(this->unionString));
}

union FixedSizeString* newFixedSizeString_05ltxcABX()
{
	union FixedSizeString* this = (union FixedSizeString*) malloc(sizeof(union FixedSizeString));
	constructorFixedSizeString_05ltxcABX(this);
	return this;
}

union FixedSizeString* newFixedSizeString_27gidrYIq(int size_of_string)
{
	union FixedSizeString* this = (union FixedSizeString*) malloc(sizeof(union FixedSizeString));
	constructorFixedSizeString_27gidrYIq(this, size_of_string);
	return this;
}


void delete_keyword_8GLaBbnUo(union FixedSizeString* this)
{
	destructorFixedSizeString(this);
}

