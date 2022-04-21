

set JAVA_HOME=C:\Program Files\Java\jdk1.7.0_72

set WORKSPACE=C:\Users\DIMA Solstyce\Documents\workspace_svn_1
set MAVEN=C:\apache-maven-3.2.5\bin

C:

cd %WORKSPACE%\fr.legrain.bdg.parent
call %MAVEN%\mvn -U clean install
