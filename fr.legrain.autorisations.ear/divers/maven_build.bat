

set JAVA_HOME=C:\Program Files\Java\jdk1.8.0_111

set WORKSPACE=D:\git_local\bdg-jee
set MAVEN=D:\apache-maven-3.2.2\bin

d:

cd %WORKSPACE%\fr.legrain.autorisations.parent
call %MAVEN%\mvn clean install


