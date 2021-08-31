package com.project.lexicalAnalyzer;

public interface CLanguageTokens {
    String[] reservedKeyword = new String[]{
            "_Packed", "break", "case", "continue", "default",
            "do", "else", "enum", "for", "goto", "if", "return",
            "signed", "sizeof", "switch", "unsigned", "while"
    };
    String constKeyword = "const";
    String volatileKeyword = "volatile";
    String extern = "extern";
    String struct = "struct";
    String union = "union";
    String classKeyword = "class";
    String typedef = "typedef";
    String staticKeyword = "static";
    String auto = "auto";
    String register = "register";
    String[] reservedTypeDescribers = new String[]{
            constKeyword, volatileKeyword, extern, struct, union,
            classKeyword, typedef, staticKeyword, auto, register
    };
    String enumKeyword = "enum";
    String[] reservedTypes = new String[]{
            "char", "signed char", "unsigned char",
            "short", "short int", "signed short", "signed short int", "unsigned short", "unsigned short int",
            "int", "signed", "signed int", "unsigned", "unsigned int",
            "long", "long int", "signed long", "signed long int", "unsigned long", "unsigned long int",
            "long long", "long long int", "signed long long", "signed long long int",
            "unsigned long long", "unsigned long long int",
            "float", "double", "long double", "void"
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
    String Dot = ".";
    String comma = ",";
    String semiColon = ";";
    String colon = ":";
    String tilde = "~";
    String doubleColon = "::";
    String destruct = "::~";
    String doubleQuotation = "\"";
    String underscore = "_";
    String newLine = "\n";
    String tab = "\t";
    String lineComment = "//";
    String whiteSpace = " ";
    String sharp = "#";
    String include = "include";
    String define = "define";
    String ifndef = "ifndef";
    String endif = "endif";
    String thisKeyword = "this";
    String constructor = "constructor";
    String destructor = "destructor";
    String voidKeyword = "void";
    String returnKeyword = "return";
}
