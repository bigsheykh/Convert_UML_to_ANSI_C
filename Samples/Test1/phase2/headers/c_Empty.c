#include "overload.h"


union Empty* newEmpty_KVNAQsecw()
{
	union Empty* this = (union Empty*) malloc(sizeof(union Empty));
	constructorEmpty_KVNAQsecw(this);
	return this;
}

union Empty* newEmpty_k8biul8f4(int i)
{
	union Empty* this = (union Empty*) malloc(sizeof(union Empty));
	constructorEmpty_k8biul8f4(this, i);
	return this;
}


void delete_keyword_C1OGpVHo9(union Empty* this)
{
	destructorEmpty(this);
}

