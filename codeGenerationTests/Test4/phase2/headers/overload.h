#ifndef _OVERLOAD_H_
#define _OVERLOAD_H_

#include <stdio.h>
#include <stdlib.h>
#include "PointClass.h"
#include "MapClass.h"

#define CHOOSE __builtin_choose_expr
#define IFTYPE(X, T) __builtin_types_compatible_p(typeof(X), T)

#define SELECT_1(X1, ...) X1
#define SELECT_2(X1, X2, ...) X2
#define SELECT_3(X1, X2, X3, ...) X3
#define SELECT_4(X1, X2, X3, X4, ...) X4
#define SELECT_5(X1, X2, X3, X4, X5, ...) X5
#define SELECT_6(X1, X2, X3, X4, X5, X6, ...) X6

#define set_point4(...) \
set_point_6xLE8tyZB \
(__VA_ARGS__)

#define set3(...) \
set_mJyYeh5po \
(__VA_ARGS__)

#define get_point2(...) \
get_point_pJ0cdCxyo \
(__VA_ARGS__)


#define SELECT_N(X, _1, _2, _3, _4, _5, _6, N, ...) N

#define set_point(...) SELECT_N(X, ##__VA_ARGS__, set_point6, set_point5, set_point4, set_point3, set_point2, set_point1, set_point0)(__VA_ARGS__)
#define set(...) SELECT_N(X, ##__VA_ARGS__, set6, set5, set4, set3, set2, set1, set0)(__VA_ARGS__)
#define get_point(...) SELECT_N(X, ##__VA_ARGS__, get_point6, get_point5, get_point4, get_point3, get_point2, get_point1, get_point0)(__VA_ARGS__)


#endif
