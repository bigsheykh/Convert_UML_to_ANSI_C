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
    String includeKeyword = "include";
    String defineKeyword = "define";
    String ifndef = "ifndef";
    String endif = "endif";
    String thisKeyword = "this";
    String constructorKeyword = "constructor";
    String destructorKeyword = "destructor";
    String voidKeyword = "void";
    String returnKeyword = "return";
    String parentKeyword = "parent";
    String methodKeyword = "method";
    String attributeKeyword = "attribute";

    String[] reservedKeyword = new String[]{
            "_Packed", "break", "case", "continue", "default",
            "do", "else", "enum", "for", "goto", "if", returnKeyword,
            "signed", "sizeof", "switch", "unsigned", "while"
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