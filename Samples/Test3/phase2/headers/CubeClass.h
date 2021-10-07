#ifndef _CUBECLASS_H_
#define _CUBECLASS_H_

#include "AllClasses.h"

#include "NumberClass.h"
#include "SquareClass.h"


union Cube
{
	union Number unionNumber;
	union Square unionSquare;
	struct
	{
		union Cube* this;
		int number;
	};
};

int get_number_Im1lUPzTF(union Cube* this);
void set_number_2pAFupUyz(union Cube* this, int the_number);
int get_square_XNMuXshEz(union Cube* this);
int get_cube_cxz4WH7zQ(union Cube* this);


#endif
