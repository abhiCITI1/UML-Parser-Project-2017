# UML-Parser-Project-2017

-------UML CLass Diagram----
Pre-requisites:  
1. Java 1.8 JRE and JDK
2. Javaparser-1.0.8.jar 
3. Plantuml.jar
4. Graphviz dot


Graphviz Installation Steps MAC OSX--

Press Command+Space and type Terminal and press enter/return key.
Run in Terminal app:
ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)" < /dev/null 2> /dev/null ; brew install caskroom/cask/brew-cask 2> /dev/null
and press enter/return key. Wait for the command to finish.
Run:
brew cask install graphviz

Libraries:  1. Javaparser-1.0.8.jar   2. Plantuml.jar
Steps to export the java parser project jar:
Right click on project -> export -> Runnable jar file -> select path to export and jar name(javaparser.jar) -> finish


Javaparser-1.0.8.jar is a dependency for accessing the packages required to use Java Grammar Parser api.
Plantuml.jar is a dependency for accessing the packages required to generate the plant UML diagram.
 
Execution of the Java Parser ---
Java Parser (is an eclipse java project) contains a Main parser class which takes the command line argument of the source test folder path and the output png image path.

Running the Java Parser project jar file(javaparser.jar) on command line:

The parser is executable on the command line with the following format:

java -jar "javaparser jar name" "<test source folder>" "<output image file name with path>"

Running the test4 folder ---
1. Download the test4.zip and extract on the local folder as below path.
2. Run the java parser eclipse project jar through command line

java -jar javaparser.jar "umlparser" "/Users/Abhishek/Documents/myWorkspace/test4/src" "/Users/Abhishek/ParserUMLDiagramsImages/test4.png"

-------UML Sequence Diagram-----

Pre-requisites:  
1. Java 1.8 JRE and JDK
2. Javaparser-1.0.8.jar 
3. Plantuml.jar
4. Graphviz dot
5. aspectj-1.8.10-src.jar

aspectj-1.8.10-src.jar -  It is a dependency to access AJDT(AspectJ development tools)








