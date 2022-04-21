#!/bin/bash

#JAVA_HOME=/var/opt/java/jdk1.7.0_51_x64
#export JAVA_HOME

#JAVA_HOME=/donnees/java/jdk1.8.0_45_x64
JAVA_HOME=/donnees/java/jdk1.8.0_111_x64
export JAVA_HOME

MAVEN_HOME=/home/nicolas/Téléchargements/apache-maven-3.2.3

WORKSPACE=/donnees/Projet/Java/Eclipse/GestionCommerciale_branche_2_0_13_JEE_E46

cd $WORKSPACE/fr.legrain.bdg.parent
#mvn clean install

#/home/nicolas/Téléchargements/apache-maven-3.2.3/bin/mvn clean install -Dmaven.test.skip=true
#mvn clean install
$MAVEN_HOME/bin/mvn clean install