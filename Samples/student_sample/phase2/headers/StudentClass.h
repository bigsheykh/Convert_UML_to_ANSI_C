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

class FixedSizeString get_professor(union Student* this, int number);
void set_professor(union Student* this, int number, class FixedSizeString professor_name);

void constructorStudent(union Student* this, int number_of_professors);
union Student* newStudent(int number_of_professors);
void constructorStudent(union Student* this, class FixedSizeString the_name, int number_of_professors);
union Student* newStudent(class FixedSizeString the_name, int number_of_professors);

void destructorStudent(union Student* this);
void delete(union Student* this);

#endif
