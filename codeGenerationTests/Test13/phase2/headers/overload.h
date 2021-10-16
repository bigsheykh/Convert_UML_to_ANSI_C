#ifndef _OVERLOAD_H_
#define _OVERLOAD_H_

#include <stdio.h>
#include <stdlib.h>
#include "Class1Class.h"
#include "class2Class.h"

#define CHOOSE __builtin_choose_expr
#define IFTYPE(X, T) __builtin_types_compatible_p(typeof(X), T)

#define SELECT_1(X1, ...) X1
#define SELECT_2(X1, X2, ...) X2
#define SELECT_3(X1, X2, X3, ...) X3

#define delete_keyword1(...) \
CHOOSE(IFTYPE(SELECT_1(__VA_ARGS__), union class2*), delete_keyword_g5s2SOfPV, \
delete_keyword_fChmxuvsQ) \
(__VA_ARGS__)


#define SELECT_N(X, _1, _2, _3, N, ...) N

#define delete_keyword(...) SELECT_N(X, ##__VA_ARGS__, delete_keyword3, delete_keyword2, delete_keyword1, delete_keyword0)(__VA_ARGS__)


#endif
