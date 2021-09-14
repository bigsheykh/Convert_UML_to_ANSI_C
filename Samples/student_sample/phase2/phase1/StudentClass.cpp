#include "FixedSizeStringClass.h"
#include "StudentClass.h"

Student::Student(int number_of_professors)
{
	// professors = new FixedSizeString[number_of_professors];
}

Student::Student(class FixedSizeString the_name, int number_of_professors)
{
	// professors = new FixedSizeString[number_of_professors];
	student_name = the_name;
}

class FixedSizeString Student::get_professor(int number)
{
	return professors[number];
}

void Student::set_professor(int number, FixedSizeString professor_name)
{
	professors[number] = professor_name;
}

Student::~Student()
{

}

