#!/bin/bash

#JAVA_HOME=/var/opt/java/jdk1.7.0_51_x64
#export JAVA_HOME

JAVA_HOME=/home/phil/dev/java/jdk1.8.0_45_x64
export JAVA_HOME

WORKSPACE=/home/phil/dev/workspace_bdg_4.6.2

cd $WORKSPACE/fr.legrain.bdg.parent
#mvn clean install

#/home/nicolas/Téléchargements/apache-maven-3.2.3/bin/mvn clean install -Dmaven.test.skip=true
/home/phil/Téléchargements/apache-maven-3.2.3/bin/mvn clean install