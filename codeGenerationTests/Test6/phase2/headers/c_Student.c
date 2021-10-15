#include "overload.h"


union Student* newStudent_EvYa3CERu(int number_of_professors)
{
	union Student* this = (union Student*) malloc(sizeof(union Student));
	constructorStudent_EvYa3CERu(this, number_of_professors);
	return this;
}

union Student* newStudent_baf15zIog(class FixedSizeString the_name, int number_of_professors)
{
	union Student* this = (union Student*) malloc(sizeof(union Student));
	constructorStudent_baf15zIog(this, the_name, number_of_professors);
	return this;
}


void delete_keyword_Od7UHBQEk(union Student* this)
{
	destructorStudent(this);
}

