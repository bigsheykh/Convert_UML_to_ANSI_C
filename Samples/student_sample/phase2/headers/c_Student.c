#include "ObjectClass.h"
#include "StringClass.h"
#include "FixedSizeStringClass.h"
#include "StudentClass.h"


union Student* newStudent(int number_of_professors)
{
	union Student* this = (union Student*) malloc(sizeof(union Student));
	constructorStudent(this, number_of_professors);
	return this;
}

union Student* newStudent(class FixedSizeString the_name, int number_of_professors)
{
	union Student* this = (union Student*) malloc(sizeof(union Student));
	constructorStudent(this, the_name, number_of_professors);
	return this;
}


void delete(union Student* this)
{
	destructorStudent(this);
	free(this);
}

