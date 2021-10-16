#ifndef _STUDENTCLASS_H_
#define _STUDENTCLASS_H_

#include "AllClasses.h"

#include "ObjectClass.h"
#include "FixedSizeStringClass.h"


union Student
{
	union Object unionObject;
	struct
	{
		union Student* this;
		class FixedSizeString student_name;
		class FixedSizeString* professors;
	};
};

class FixedSizeString get_professor_0lebQZOyj(union Student* this, int number);
void set_professor_XJ7DnUl62(union Student* this, int number, class FixedSizeString professor_name);

void constructorStudent_EvYa3CERu(union Student* this, int number_of_professors);
union Student* newStudent_EvYa3CERu(int number_of_professors);
void constructorStudent_baf15zIog(union Student* this, class FixedSizeString the_name, int number_of_professors);
union Student* newStudent_baf15zIog(class FixedSizeString the_name, int number_of_professors);

void destructorStudent(union Student* this);
void delete_keyword_Od7UHBQEk(union Student* this);

#endif
