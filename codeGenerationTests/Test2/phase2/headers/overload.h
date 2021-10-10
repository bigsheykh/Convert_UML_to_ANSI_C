#ifndef _OVERLOAD_H_
#define _OVERLOAD_H_

#include <stdio.h>
#include <stdlib.h>
#include "CarClass.h"
#include "BenzClass.h"

#define CHOOSE __builtin_choose_expr
#define IFTYPE(X, T) __builtin_types_compatible_p(typeof(X), T)

#define SELECT_1(X1, ...) X1
#define SELECT_2(X1, X2, ...) X2
#define SELECT_3(X1, X2, X3, ...) X3
#define SELECT_4(X1, X2, X3, X4, ...) X4

#define model1(...) \
CHOOSE(IFTYPE(SELECT_1(__VA_ARGS__), union Benz*), model_BUb2bJfj3, \
model_0ZgXXBEWz) \
(__VA_ARGS__)
#define model2(...) \
model_C9PoYoVFF \
(__VA_ARGS__)

#define info1(...) \
CHOOSE(IFTYPE(SELECT_1(__VA_ARGS__), union Benz*), info_0klPTXPmp, \
info_lH0778d69) \
(__VA_ARGS__)


#define SELECT_N(X, _1, _2, _3, _4, N, ...) N

#define model(...) SELECT_N(X, ##__VA_ARGS__, model4, model3, model2, model1, model0)(__VA_ARGS__)
#define info(...) SELECT_N(X, ##__VA_ARGS__, info4, info3, info2, info1, info0)(__VA_ARGS__)


#endif
