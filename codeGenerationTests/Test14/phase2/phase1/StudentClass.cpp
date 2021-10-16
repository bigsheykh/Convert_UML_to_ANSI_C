#include "overload.h"

Student::Student_EvYa3CERu(int number_of_professors)
{
	this->professors = (class FixedSizeString *) malloc(number_of_professors * sizeof(class FixedSizeString));
}

Student::Student_baf15zIog(class FixedSizeString the_name, int number_of_professors)
{
	this->professors = (class FixedSizeString *) malloc(number_of_professors * sizeof(class FixedSizeString));
	this->student_name = the_name;
}

class FixedSizeString Student::get_professor_jnL1u6L1w(int number)
{
	return this->professors[number];
}

void Student::set_professor_G42ARCkI6(int number, class FixedSizeString professor_name)
{
	this->professors[number] = professor_name;
}

Student::~Student()
{
    printf("%s\n", this->student_name.get_chars());
}

