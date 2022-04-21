#!/bin/bash

#JAVA_HOME=/var/opt/java/jdk1.7.0_51_x64
#export JAVA_HOME

JAVA_HOME=/home/julien/apps/java/jdk1.7.0_51/
export JAVA_HOME

WORKSPACE=/home/julien/workspace_solstyce

cd $WORKSPACE/fr.legrain.bdg.parent
mvn clean install

