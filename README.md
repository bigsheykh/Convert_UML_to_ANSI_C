# requirements
- An IDE like Intellij Idea
- JDK >= 11

# Tests

## test1

satisfy:
- constructor
- destructor
- overloading constructor

phase 1 command: -xml Samples/Test1/test1.xml
phase 2 command: -i Samples/Test1/phase2/diagram_info -h Samples/Test1/phase2/headers -p1 Samples/Test1/phase2/phase1 -c Samples/Test1/phase2/c_files


## test2

satisfy:
- method
- overloading method
- overriding method

phase 1 command: -xml Samples/Test2/test2.xml
phase 2 command: -i Samples/Test2/phase2/diagram_info -h Samples/Test2/phase2/headers -p1 Samples/Test2/phase2/phase1 -c Samples/Test2/phase2/c_files

## test3

satisfy:
- more than one level inheritance
- upcast

phase 1 command: -xml Samples/Test3/test3.xml
phase 2 command: -i Samples/Test3/phase2/diagram_info -h Samples/Test3/phase2/headers -p1 Samples/Test3/phase2/phase1 -c Samples/Test3/phase2/c_files

## test4

satisfy:
- use a class in another
- array

phase 1 command: -xml Samples/Test4/test4.xml
phase 2 command: -i Samples/Test4/phase2/diagram_info -h Samples/Test4/phase2/headers -p1 Samples/Test4/phase2/phase1 -c Samples/Test4/phase2/c_files
