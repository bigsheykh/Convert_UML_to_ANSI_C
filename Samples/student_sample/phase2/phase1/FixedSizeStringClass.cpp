#include "FixedSizeStringClass.h"
#include <stdio.h>
#include <stdlib.h>

FixedSizeString::FixedSizeString()
{
	all = (char *) malloc(25);
}

FixedSizeString::FixedSizeString(int size_of_string)
{
	all = (char *) malloc(size_of_string);
}

FixedSizeString::~FixedSizeString()
{
	free(all);
}

