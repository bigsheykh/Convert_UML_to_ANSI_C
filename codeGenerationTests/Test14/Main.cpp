#include <stdio.h>
#include <stdlib.h>

using namespace std;

class Object
{
};

class String:public Object
{
    public:
    char *all;
    int size;
    void set_chars(char* characters, int size_of_string)
    {
        all = characters;
    	size = size_of_string;
    }
    char* get_chars()
    {
        return all;
    }
};

class FixedSizeString:public String
{
    public:
    FixedSizeString()
    {
        all = (char *) malloc(25);
	    size = 25;
    }
    FixedSizeString(int size_of_string)
    {
        all = (char *) malloc(size_of_string);
        size = size_of_string;
    }
    ~FixedSizeString()
    {
    }
};

class Student
{
    public:
    FixedSizeString student_name;
    FixedSizeString* professors;

    Student(int number_of_professors)
    {
        professors = (FixedSizeString *) malloc(number_of_professors * sizeof(FixedSizeString));
    }
    Student(FixedSizeString the_name, int number_of_professors)
    {
        professors = (FixedSizeString *) malloc(number_of_professors * sizeof(FixedSizeString));
        student_name = the_name;
    }
    FixedSizeString get_professor(int number)
    {
        return professors[number];
    }
    void set_professor(int number, FixedSizeString professor_name)
    {
        professors[number] = professor_name;
    }
    ~Student()
    {
        printf("%s\n", student_name.get_chars());
    }
};


void tm(FixedSizeString* pro)
{
    FixedSizeString m(30);
    m.all = "21";
    pro->set_chars(m.all, 3);
}

int main()
{
    Student vm(7);
    FixedSizeString fixed(),wed;
    char *student_name = "Amirreza";
    int i = 0;
    vm.student_name.set_chars(student_name, 12);
    for(i = 0;i< 7;i++)
    {
        tm(& (vm.professors[i]));
        printf("%s\n",vm.professors[i].get_chars());
    }
    printf("%s", vm.student_name.get_chars());
}