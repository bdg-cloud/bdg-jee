
set JAVA_HOME=C:\Program Files\Java\jdk1.7.0_72

set WORKSPACE="C:\Users\DIMA Solstyce\Documents\workspace_svn_1"
set MAVEN="C:\apache-maven-3.2.5\bin"


%MAVEN%\mvn install:install-file -U -e -X -Dfile=%WORKSPACE%\fr.legrain.bdg.ear\lib\barcode4j-light.jar -DgroupId=net.sf.barcode4j -DartifactId=barcode4j-light -Dversion=2.1 -Dpackaging=jar

