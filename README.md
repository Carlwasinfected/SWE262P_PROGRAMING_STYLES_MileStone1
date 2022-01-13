# SWE 262P Programming Sytles
*Team Members: Can Wang, Ruokun Xu*

## MileStone 1
The program accept zero, one or two arguments as  `java <The Program> <XML File Path> <Query String>`
- If it is zero, then the program use default xml file path as well as query sentence.
- If it is one, then the program use the given xml file path and the default query sentence.
- If it is two, then the program use the given xml file path adn the given query sentence.

The default XML file is located at `./testcase/books.xml` and the default query sentence is `/catalog/book/1`. 
If the input arguments are invalid, the program will print the notification on the console and stop early.

The program failed to process the GB-Scale files because it will run out of the heap memory offered by JVM. We believe 
one approach to solve the issue is to apply the nature 'split-process-merge' idea and use some big data processing
framework(i.e., MapReduce) and run the program on a large distributed system.

*By [Can Wang](mailto:canwang7@uci.edu)*