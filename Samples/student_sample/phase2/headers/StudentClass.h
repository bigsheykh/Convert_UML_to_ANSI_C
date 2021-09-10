#ifndef _STUDENTCLASS_H_
#define _STUDENTCLASS_H_

#include "AllClasses.h"

#include "ObjectClass.h"


union Student
{	union Object unionObject;
	struct
	{
		union Student* this;
		class FixedSizeString student_name;
		class FixedSizeString* professors;
	};
};

class FixedSizeString get_professor(union Student* this, int number);
void set_professor(union Student* this, int number, FixedSizeString professor_name);

constructorStudent(union Student* this, int number_of_professors);
constructorStudent(union Student* this, class FixedSizeString the_name, int number_of_professors);

void destructorStudent(union Student* this);

#endif
