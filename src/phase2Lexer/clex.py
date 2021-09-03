# ----------------------------------------------------------------------
# clex.py
#
# A lexer for ANSI C.
# ----------------------------------------------------------------------

import ply.lex as lex
import os
import sys

# Reserved words
class Lexer:
    def __init__(self):
        self.reserved_map = {}
        for r in self.reserved:
            self.reserved_map[r.lower()] = r

    reserved = (
        'AUTO', 'BREAK', 'CASE', 'CHAR', 'CONST', 'CONTINUE', 'DEFAULT', 'DO', 'DOUBLE',
        'ELSE', 'ENUM', 'EXTERN', 'FLOAT', 'FOR', 'GOTO', 'IF', 'INT', 'LONG', 'REGISTER',
        'RETURN', 'SHORT', 'SIGNED', 'SIZEOF', 'STATIC', 'STRUCT', 'SWITCH', 'TYPEDEF',
        'UNION', 'UNSIGNED', 'VOID', 'VOLATILE', 'WHILE',  'CLASS', 'THIS' , 'MALLOC', 'NEW',
        'DELETE', 'PARENT', 'CONSTRUCTOR', 'DESTRUCTOR', 'METHOD', 'ATTRIBUTE'
    )

    tokens = reserved + (
        # Literals (identifier, integer constant, float constant, string constant,
        # char const)
        'ID', 'TYPEID', 'ICONST', 'FCONST', 'SCONST', 'CCONST',

        # Operators (+,-,*,/,%,|,&,~,^,<<,>>, ||, &&, !, <, <=, >, >=, ==, !=)
                                                      'PLUS', 'MINUS', 'TIMES', 'DIVIDE', 'MOD',
        'OR', 'AND', 'NOT', 'XOR', 'LSHIFT', 'RSHIFT',
        'LOR', 'LAND', 'LNOT',
        'LT', 'LE', 'GT', 'GE', 'EQ', 'NE',

        # Assignment (=, *=, /=, %=, +=, -=, <<=, >>=, &=, ^=, |=)
        'EQUALS', 'TIMESEQUAL', 'DIVEQUAL', 'MODEQUAL', 'PLUSEQUAL', 'MINUSEQUAL',
        'LSHIFTEQUAL', 'RSHIFTEQUAL', 'ANDEQUAL', 'XOREQUAL', 'OREQUAL',

        # Increment/decrement (++,--)
        'PLUSPLUS', 'MINUSMINUS',

        # Structure dereference (->)
        'ARROW',

        # Conditional operator (?)
        'CONDOP',

        # Delimeters ( ) [ ] { } , . ; : :: ~ ::~
        'LPAREN', 'RPAREN',
        'LBRACKET', 'RBRACKET',
        'LBRACE', 'RBRACE',
        'COMMA', 'PERIOD', 'SEMI', 'COLON', 'DOUBLECOLON', 'TILDA', 'DESTRUCT',

        # Ellipsis (...)
        'ELLIPSIS',

        # Ignore
        'preprocessor', 'comment', 'WHITESPACE', 'NEWLINE', 'LINECOMMENT','TAB',
    )


    # Operators
    t_WHITESPACE = r'\ '
    t_TAB = r'\t'
    t_NEWLINE = r'\n+'
    t_PLUS = r'\+'
    t_MINUS = r'-'
    t_TIMES = r'\*'
    t_DIVIDE = r'/'
    t_MOD = r'%'
    t_OR = r'\|'
    t_AND = r'&'
    t_NOT = r'~'
    t_XOR = r'\^'
    t_LSHIFT = r'<<'
    t_RSHIFT = r'>>'
    t_LOR = r'\|\|'
    t_LAND = r'&&'
    t_LNOT = r'!'
    t_LT = r'<'
    t_GT = r'>'
    t_LE = r'<='
    t_GE = r'>='
    t_EQ = r'=='
    t_NE = r'!='

    # Assignment operators

    t_EQUALS = r'='
    t_TIMESEQUAL = r'\*='
    t_DIVEQUAL = r'/='
    t_MODEQUAL = r'%='
    t_PLUSEQUAL = r'\+='
    t_MINUSEQUAL = r'-='
    t_LSHIFTEQUAL = r'<<='
    t_RSHIFTEQUAL = r'>>='
    t_ANDEQUAL = r'&='
    t_OREQUAL = r'\|='
    t_XOREQUAL = r'\^='

    # Increment/decrement
    t_PLUSPLUS = r'\+\+'
    t_MINUSMINUS = r'--'

    # ->
    t_ARROW = r'->'

    # ?
    t_CONDOP = r'\?'

    # Delimeters
    t_LPAREN = r'\('
    t_RPAREN = r'\)'
    t_LBRACKET = r'\['
    t_RBRACKET = r'\]'
    t_LBRACE = r'\{'
    t_RBRACE = r'\}'
    t_COMMA = r','
    t_PERIOD = r'\.'
    t_SEMI = r';'
    t_TILDA = r'~'
    t_DOUBLECOLON = r'::'
    t_DESTRUCT = r'::~'
    t_COLON = r':'
    t_ELLIPSIS = r'\.\.\.'
    t_preprocessor = r'\#(.)*?\n'
    t_comment = r'/\*(.|\n)*?\*/'
    t_LINECOMMENT = r'//.*'

    # Identifiers and reserved words
    def t_ID(self, t):
        r'[A-Za-z_][\w_]*'
        t.type = self.reserved_map.get(t.value, "ID")
        return t

    # Integer literal
    t_ICONST = r'\d+([uU]|[lL]|[uU][lL]|[lL][uU])?'

    # Floating literal
    t_FCONST = r'((\d+)(\.\d+)(e(\+|-)?(\d+))? | (\d+)e(\+|-)?(\d+))([lL]|[fF])?'

    # String literal
    t_SCONST = r'\"([^\\\n]|(\\.))*?\"'

    # Character constant 'c' or L'c'
    t_CCONST = r'(L)?\'([^\\\n]|(\\.))*?\''

    def t_error(self, t):
        print("Illegal character %s" % repr(t.value[0]))
        t.lexer.skip(1)

    def build(self, **kwargs):
        '''
        build the lexer
        '''
        self.lexer = lex.lex(module=self, **kwargs)

        return self.lexer


if __name__ == "__main__":
    CFileName = sys.argv[1]
    outputFile = sys.argv[2]
    lexer = Lexer().build()
    lexer.input(open(CFileName, "r").read())
    counter = 0
    os.makedirs("result", exist_ok=True)
    os.makedirs(os.path.dirname("result/" + outputFile), exist_ok=True)
    result = open("result/" + outputFile, "w")
    while True:
        tok = lexer.token()
        if not tok:
            break
        typeOutput = open("result/" + outputFile + str(counter) + ".type", "w")
        typeOutput.write(tok.type)
        valueOutput = open("result/" + outputFile + str(counter) +  ".value", "w")
        valueOutput.write(tok.value)
        counter += 1

#         result = open("result/" + outputFile, "w")
#         result.write(tok.type + " " + str(tok.lexpos) + " " + str(tok.lexpos + tok.value.__len__()))
#         result.write("\n")
    result.write(str(counter))
