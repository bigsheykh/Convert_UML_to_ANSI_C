package com.project.phase2CodeGeneration;

public enum TokenTypes {
    COMMENT,
    MACRO,
    STRING,
    EMPTY_STRING,

    CONST,
    VOLATILE,
    EXTERN,
    STRUCT,
    UNION,
    CLASS,
    TYPEDEF,
    STATIC,
    AUTO,
    REGISTER,
    ID,
    NUMBER,
    TYPES,
    ENUM,
    SIGN,
    THIS,

    RESERVED_KEYWORD,
    SIZEOF,
    MALLOC,
    NEW,

    DOUBLE_COLON,
    SEMI_COLON,
    COLON,
    COMMA,
    DOT,
    ELLIPSIS,
    ARROW,
    TILDA,
    DESTRUCT,
    STAR,
    REFERENCE,
    OPERATOR_OR_ASSIGN,

    OPEN_PARENTHESIS,
    CLOSE_PARENTHESIS,
    OPEN_CURLY_BRACKET,
    CLOSE_CURLY_BRACKET,
    OPEN_SQUARE_BRACKET,
    CLOSE_SQUARE_BRACKET,

    UNKNOWN
}
