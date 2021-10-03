package com.project.lexicalAnalyzer;

public interface CLanguageTokens {
    String constKeyword = "const";
    String volatileKeyword = "volatile";
    String extern = "extern";
    String structKeyword = "struct";
    String unionKeyword = "union";
    String classKeyword = "class";
    String typedef = "typedef";
    String staticKeyword = "static";
    String auto = "auto";
    String register = "register";
    String enumKeyword = "enum";
    String[] reservedTypeDescribers = new String[]{
            constKeyword, volatileKeyword, extern, structKeyword, unionKeyword,
            classKeyword, typedef, staticKeyword, auto, register
    };

    String openParenthesis = "(";
    String closeParenthesis = ")";
    String openCurlyBracket = "{";
    String closeCurlyBracket = "}";
    String openSquareBracket = "[";
    String closeSquareBracket = "]";
    String lessThanSign = "<";
    String greaterThanSign = ">";
    String equalSign = "=";
    String ellipsis = "...";
    String star = "*";
    String arrow = "->";
    String dot = ".";
    String comma = ",";
    String semiColon = ";";
    String colon = ":";
    String tilde = "~";
    String doubleColon = "::";
    String destruct = "::~";
    String and = "&";
    String doubleQuotation = "\"";
    String underscore = "_";
    String newLine = "\n";
    String tab = "\t";
    String lineComment = "//";
    String whiteSpace = " ";
    String sharp = "#";
    String backSlashNewLine = "\\\n";

    String includeKeyword = "include";
    String defineKeyword = "define";
    String undefKeyword = "undef";
    String ifndef = "ifndef";
    String endif = "endif";
    String choose = "CHOOSE";
    String iftypeKeyword = "IFTYPE";
    String select_N = "SELECT_N";
    String thisKeyword = "this";
    String thisManipulated = "this_keyword";
    String constructorKeyword = "constructor";
    String destructorKeyword = "destructor";
    String voidKeyword = "void";
    String returnKeyword = "return";
    String parentKeyword = "parent";
    String methodKeyword = "method";
    String attributeKeyword = "attribute";
    String newKeyword = "new";
    String deleteKeyword = "delete";
    String deleteManipulate = "delete_keyword";
    String mallocKeyword = "malloc";
    String sizeofKeyword = "sizeof";
    String freeKeyword = "free";
    String vaArgsToken = "__VA_ARGS__";

    String[] reservedKeyword = new String[]{
            "_Packed", "break", "case", "continue", "default", "do",
            "else", "enum", "for", "goto", "if", returnKeyword,
            "signed", sizeofKeyword, "switch", "unsigned", "while",
            newKeyword, deleteKeyword, mallocKeyword, freeKeyword,
            thisKeyword, thisManipulated
    };

    String[] reservedTypes = new String[]{
            "char", "signed char", "unsigned char",
            "short", "short int", "signed short", "signed short int", "unsigned short", "unsigned short int",
            "int", "signed", "signed int", "unsigned", "unsigned int",
            "long", "long int", "signed long", "signed long int", "unsigned long", "unsigned long int",
            "long long", "long long int", "signed long long", "signed long long int",
            "unsigned long long", "unsigned long long int",
            "float", "double", "long double", voidKeyword
    };

}
