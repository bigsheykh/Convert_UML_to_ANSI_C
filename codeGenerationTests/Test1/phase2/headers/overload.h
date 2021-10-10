#ifndef _OVERLOAD_H_
#define _OVERLOAD_H_

#include <stdio.h>
#include <stdlib.h>
#include "EmptyClass.h"

#define CHOOSE __builtin_choose_expr
#define IFTYPE(X, T) __builtin_types_compatible_p(typeof(X), T)

#define SELECT_1(X1, ...) X1
#define SELECT_2(X1, X2, ...) X2
#define SELECT_3(X1, X2, X3, ...) X3
#define SELECT_4(X1, X2, X3, X4, ...) X4

#define constructorEmpty1(...) \
constructorEmpty_KVNAQsecw \
(__VA_ARGS__)
#define constructorEmpty2(...) \
constructorEmpty_k8biul8f4 \
(__VA_ARGS__)

#define delete_keyword1(...) \
delete_keyword_C1OGpVHo9 \
(__VA_ARGS__)

#define newEmpty0(...) \
newEmpty_KVNAQsecw \
(__VA_ARGS__)
#define newEmpty1(...) \
newEmpty_k8biul8f4 \
(__VA_ARGS__)


#define SELECT_N(X, _1, _2, _3, _4, N, ...) N

#define constructorEmpty(...) SELECT_N(X, ##__VA_ARGS__, constructorEmpty4, constructorEmpty3, constructorEmpty2, constructorEmpty1, constructorEmpty0)(__VA_ARGS__)
#define delete_keyword(...) SELECT_N(X, ##__VA_ARGS__, delete_keyword4, delete_keyword3, delete_keyword2, delete_keyword1, delete_keyword0)(__VA_ARGS__)
#define newEmpty(...) SELECT_N(X, ##__VA_ARGS__, newEmpty4, newEmpty3, newEmpty2, newEmpty1, newEmpty0)(__VA_ARGS__)


#endif
