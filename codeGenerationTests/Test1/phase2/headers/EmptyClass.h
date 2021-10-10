#ifndef _EMPTYCLASS_H_
#define _EMPTYCLASS_H_

#include "AllClasses.h"



union Empty
{
	struct
	{
		union Empty* this;
	};
};


void constructorEmpty_KVNAQsecw(union Empty* this);
union Empty* newEmpty_KVNAQsecw();
void constructorEmpty_k8biul8f4(union Empty* this, int i);
union Empty* newEmpty_k8biul8f4(int i);

void destructorEmpty(union Empty* this);
void delete_keyword_C1OGpVHo9(union Empty* this);

#endif
