#include "ObjectClass.h"
#include "StringClass.h"
#include "FixedSizeStringClass.h"
#include "StudentClass.h"

void set_chars(union FixedSizeString* this, char* characters, int size_of_string)
{
	set_chars(&(this->unionString), characters, size_of_string);
}
char* get_chars(union FixedSizeString* this)
{
	return get_chars(&(this->unionString));
}

union FixedSizeString* newFixedSizeString()
{
	union FixedSizeString* this = (union FixedSizeString*) malloc(sizeof(union FixedSizeString));
	constructorFixedSizeString(this);
	return this;
}

union FixedSizeString* newFixedSizeString(int size_of_string)
{
	union FixedSizeString* this = (union FixedSizeString*) malloc(sizeof(union FixedSizeString));
	constructorFixedSizeString(this, size_of_string);
	return this;
}


void delete(union FixedSizeString* this)
{
	destructorFixedSizeString(this);
	free(this);
}

