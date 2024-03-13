javac Main.java
echo "Main-Class: Main" >> MANIFEST.MF
jar cvmf MANIFEST.MF main.jar Main.class
java -jar main.jar