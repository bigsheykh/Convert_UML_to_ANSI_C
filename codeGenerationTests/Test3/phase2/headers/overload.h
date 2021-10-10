#ifndef _OVERLOAD_H_
#define _OVERLOAD_H_

#include <stdio.h>
#include <stdlib.h>
#include "NumberClass.h"
#include "SquareClass.h"
#include "CubeClass.h"

#define CHOOSE __builtin_choose_expr
#define IFTYPE(X, T) __builtin_types_compatible_p(typeof(X), T)

#define SELECT_1(X1, ...) X1
#define SELECT_2(X1, X2, ...) X2
#define SELECT_3(X1, X2, X3, ...) X3
#define SELECT_4(X1, X2, X3, X4, ...) X4

#define get_cube1(...) \
get_cube_cxz4WH7zQ \
(__VA_ARGS__)

#define get_square1(...) \
CHOOSE(IFTYPE(SELECT_1(__VA_ARGS__), union Cube*), get_square_XNMuXshEz, \
get_square_orOP2K4o8) \
(__VA_ARGS__)

#define set_number2(...) \
CHOOSE(IFTYPE(SELECT_1(__VA_ARGS__), union Square*) && IFTYPE(SELECT_2(__VA_ARGS__), int), set_number_PCiYHO8yM, \
CHOOSE(IFTYPE(SELECT_1(__VA_ARGS__), union Cube*) && IFTYPE(SELECT_2(__VA_ARGS__), int), set_number_2pAFupUyz, \
set_number_fdZplofJn)) \
(__VA_ARGS__)

#define get_number1(...) \
CHOOSE(IFTYPE(SELECT_1(__VA_ARGS__), union Square*), get_number_HhqFwykBD, \
CHOOSE(IFTYPE(SELECT_1(__VA_ARGS__), union Cube*), get_number_Im1lUPzTF, \
get_number_GVyIjl8aj)) \
(__VA_ARGS__)


#define SELECT_N(X, _1, _2, _3, _4, N, ...) N

#define get_cube(...) SELECT_N(X, ##__VA_ARGS__, get_cube4, get_cube3, get_cube2, get_cube1, get_cube0)(__VA_ARGS__)
#define get_square(...) SELECT_N(X, ##__VA_ARGS__, get_square4, get_square3, get_square2, get_square1, get_square0)(__VA_ARGS__)
#define set_number(...) SELECT_N(X, ##__VA_ARGS__, set_number4, set_number3, set_number2, set_number1, set_number0)(__VA_ARGS__)
#define get_number(...) SELECT_N(X, ##__VA_ARGS__, get_number4, get_number3, get_number2, get_number1, get_number0)(__VA_ARGS__)


#endif
