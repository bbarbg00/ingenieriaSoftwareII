cd bin
# Compilacion
javac -cp ./:../lib/mysql-connector-java-5.1.38-bin.jar:../lib/javahelp-2.0.05.jar -d ./ ../src/colegio/*.java
javac -cp ./:../lib/mysql-connector-java-5.1.38-bin.jar:../lib/javahelp-2.0.05.jar -d ./ ../src/colegio/gui/*.java

# Copia de la ayuda, incluyendo el helpSet, al directorio bin; por alguna razón sólo lo encuentra si está en bin
#cp -r ../lib/help/* ./

# Ejecucion
java  -cp ./:../lib/mysql-connector-java-5.1.38-bin.jar:../lib/javahelp-2.0.05.jar colegio.gui.Main
