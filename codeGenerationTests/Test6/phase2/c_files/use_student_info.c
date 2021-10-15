#include "overload.h"


void tm(class FixedSizeString* pro)
{
    class FixedSizeString m(30);
    m.all = "21";
    pro->set_chars(m.all, 3);
}

int main()
{
    class Student vm(7);
    class FixedSizeString fixed(),notFixed;
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