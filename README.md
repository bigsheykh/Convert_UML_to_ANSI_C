# requirements
- An IDE like Intellij Idea
- JDK >= 11

# Tests

## test0

satisfy:
- consistency in c code

phase 1 command: -xml codeGenerationTests/Test0/test0.xml

phase 2 command: -i codeGenerationTests/Test0/phase2/diagram_info -h codeGenerationTests/Test0/phase2/headers -p1 codeGenerationTests/Test0/phase2/phase1 -c codeGenerationTests/Test0/phase2/c_files

## test1

satisfy:
- constructor
- destructor
- overloading constructor

phase 1 command: -xml codeGenerationTests/Test1/test1.xml

phase 2 command: -i codeGenerationTests/Test1/phase2/diagram_info -h codeGenerationTests/Test1/phase2/headers -p1 codeGenerationTests/Test1/phase2/phase1 -c codeGenerationTests/Test1/phase2/c_files


## test2

satisfy:
- inheritance
- method
- overloading method
- overriding method

phase 1 command: -xml codeGenerationTests/Test2/test2.xml

phase 2 command: -i codeGenerationTests/Test2/phase2/diagram_info -h codeGenerationTests/Test2/phase2/headers -p1 codeGenerationTests/Test2/phase2/phase1 -c codeGenerationTests/Test2/phase2/c_files

## test3

satisfy:
- more than one level inheritance
- upcast

phase 1 command: -xml codeGenerationTests/Test3/test3.xml

phase 2 command: -i codeGenerationTests/Test3/phase2/diagram_info -h codeGenerationTests/Test3/phase2/headers -p1 codeGenerationTests/Test3/phase2/phase1 -c codeGenerationTests/Test3/phase2/c_files

## test4

satisfy:
- use a class in another
- array

phase 1 command: -xml codeGenerationTests/Test4/test4.xml

phase 2 command: -i codeGenerationTests/Test4/phase2/diagram_info -h codeGenerationTests/Test4/phase2/headers -p1 codeGenerationTests/Test4/phase2/phase1 -c codeGenerationTests/Test4/phase2/c_files
