#ifndef _OVERLOAD_H_
#define _OVERLOAD_H_

#include <stdio.h>
#include <stdlib.h>
#include "ObjectClass.h"
#include "StringClass.h"
#include "FixedSizeStringClass.h"
#include "StudentClass.h"

#define CHOOSE __builtin_choose_expr
#define IFTYPE(X, T) __builtin_types_compatible_p(typeof(X), T)

#define SELECT_1(X1, ...) X1
#define SELECT_2(X1, X2, ...) X2
#define SELECT_3(X1, X2, X3, ...) X3
#define SELECT_4(X1, X2, X3, X4, ...) X4
#define SELECT_5(X1, X2, X3, X4, X5, ...) X5

#define set_professor3(...) \
set_professor_XJ7DnUl62 \
(__VA_ARGS__)

#define get_chars1(...) \
CHOOSE(IFTYPE(SELECT_1(__VA_ARGS__), union FixedSizeString*), get_chars_vJH68lMu2, \
get_chars_aN0RIzoj0) \
(__VA_ARGS__)

#define newFixedSizeString0(...) \
newFixedSizeString_05ltxcABX \
(__VA_ARGS__)
#define newFixedSizeString1(...) \
newFixedSizeString_27gidrYIq \
(__VA_ARGS__)

#define set_chars3(...) \
CHOOSE(IFTYPE(SELECT_1(__VA_ARGS__), union FixedSizeString*) && IFTYPE(SELECT_2(__VA_ARGS__), char*) && IFTYPE(SELECT_3(__VA_ARGS__), int), set_chars_oOlWaiZec, \
set_chars_RxEHHuadD) \
(__VA_ARGS__)

#define constructorFixedSizeString1(...) \
constructorFixedSizeString_05ltxcABX \
(__VA_ARGS__)
#define constructorFixedSizeString2(...) \
constructorFixedSizeString_27gidrYIq \
(__VA_ARGS__)

#define delete_keyword1(...) \
CHOOSE(IFTYPE(SELECT_1(__VA_ARGS__), union Student*), delete_keyword_Od7UHBQEk, \
delete_keyword_8GLaBbnUo) \
(__VA_ARGS__)

#define newStudent1(...) \
newStudent_EvYa3CERu \
(__VA_ARGS__)
#define newStudent2(...) \
newStudent_baf15zIog \
(__VA_ARGS__)

#define constructorStudent2(...) \
constructorStudent_EvYa3CERu \
(__VA_ARGS__)
#define constructorStudent3(...) \
constructorStudent_baf15zIog \
(__VA_ARGS__)

#define get_professor2(...) \
get_professor_0lebQZOyj \
(__VA_ARGS__)


#define SELECT_N(X, _1, _2, _3, _4, _5, N, ...) N

#define set_professor(...) SELECT_N(X, ##__VA_ARGS__, set_professor5, set_professor4, set_professor3, set_professor2, set_professor1, set_professor0)(__VA_ARGS__)
#define get_chars(...) SELECT_N(X, ##__VA_ARGS__, get_chars5, get_chars4, get_chars3, get_chars2, get_chars1, get_chars0)(__VA_ARGS__)
#define newFixedSizeString(...) SELECT_N(X, ##__VA_ARGS__, newFixedSizeString5, newFixedSizeString4, newFixedSizeString3, newFixedSizeString2, newFixedSizeString1, newFixedSizeString0)(__VA_ARGS__)
#define set_chars(...) SELECT_N(X, ##__VA_ARGS__, set_chars5, set_chars4, set_chars3, set_chars2, set_chars1, set_chars0)(__VA_ARGS__)
#define constructorFixedSizeString(...) SELECT_N(X, ##__VA_ARGS__, constructorFixedSizeString5, constructorFixedSizeString4, constructorFixedSizeString3, constructorFixedSizeString2, constructorFixedSizeString1, constructorFixedSizeString0)(__VA_ARGS__)
#define delete_keyword(...) SELECT_N(X, ##__VA_ARGS__, delete_keyword5, delete_keyword4, delete_keyword3, delete_keyword2, delete_keyword1, delete_keyword0)(__VA_ARGS__)
#define newStudent(...) SELECT_N(X, ##__VA_ARGS__, newStudent5, newStudent4, newStudent3, newStudent2, newStudent1, newStudent0)(__VA_ARGS__)
#define constructorStudent(...) SELECT_N(X, ##__VA_ARGS__, constructorStudent5, constructorStudent4, constructorStudent3, constructorStudent2, constructorStudent1, constructorStudent0)(__VA_ARGS__)
#define get_professor(...) SELECT_N(X, ##__VA_ARGS__, get_professor5, get_professor4, get_professor3, get_professor2, get_professor1, get_professor0)(__VA_ARGS__)


#endif
