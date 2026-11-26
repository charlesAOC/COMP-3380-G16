JAVAC := javac
JAVA := java
SRC := src/
MAIN := Main
CLASSPATH := classFiles
JAR = mssql-jdbc-11.2.0.jre11.jar

all: app

# build dependencies
build:
	mkdir -p $(CLASSPATH)
	$(JAVAC) -cp "$(CLASSPATH);$(JAR)" -sourcepath $(SRC) -d $(CLASSPATH) $(SRC)Main.java

# build the app
app: build
	$(JAVA) -cp "$(CLASSPATH);$(JAR)" Main

# delete classFile and all its contents
clean:
	rm -rf $(CLASSPATH)