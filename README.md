# SWE 262P Programming Sytles
*Team Members: Can Wang, Ruokun Xu*

## MileStone 1
The program accept zero, one or two arguments as `java <The Program> <XML File Path> <Query String>`
- If it is zero, then the program use default xml file path as well as query sentence.
- If it is one, then the program use the given xml file path and the default query sentence.
- If it is two, then the program use the given xml file path adn the given query sentence.

The default XML file is located at `./testcase/books.xml` and the default query sentence is `/catalog/book/1`. 
If the input arguments are invalid, the program will print the notification on the console and stop early.

