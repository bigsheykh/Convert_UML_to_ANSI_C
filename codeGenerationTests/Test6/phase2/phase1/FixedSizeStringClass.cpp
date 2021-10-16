#include "overload.h"
#include <stdio.h>
#include <stdlib.h>

FixedSizeString::FixedSizeString_05ltxcABX()
{
	this->all = (char *) malloc(25);
	this->size = 25;
}

FixedSizeString::FixedSizeString_27gidrYIq(int size_of_string)
{
	this->all = (char *) malloc(size_of_string);
	this->size = size_of_string;
}

FixedSizeString::~FixedSizeString()
{
}

