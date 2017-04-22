all: clean

clean: 
	find . -name "*.class" -exec rm -rf {} \;
	
compile:
	javac -cp . *.java

java:
	ajc -1.8 *.java
	
aspects:
	ajc -1.8 *.java *.aj


run: compile
	java -cp . Main


	