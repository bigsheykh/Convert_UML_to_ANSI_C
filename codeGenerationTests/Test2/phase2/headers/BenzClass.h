#ifndef _BENZCLASS_H_
#define _BENZCLASS_H_

#include "AllClasses.h"

#include "CarClass.h"


union Benz
{
	union Car unionCar;
	struct
	{
		union Benz* this;
	};
};

void model_BUb2bJfj3(union Benz* this);
void info_0klPTXPmp(union Benz* this);
void model_C9PoYoVFF(union Benz* this, int year);


#endif
