#!/bin/bash

#JAVA_HOME=/donnees/java/jdk1.8.0_45_x64
JAVA_HOME=/donnees/java/jdk1.8.0_111_x64
export JAVA_HOME

MAVEN_HOME=/home/nicolas/Téléchargements/apache-maven-3.2.3

WORKSPACE=/donnees/Projet/Java/Eclipse/GestionCommerciale_branche_2_0_13_JEE_E46

cd $WORKSPACE/fr.legrain.autorisations.parent
#mvn clean install
$MAVEN_HOME/bin/mvn clean install

