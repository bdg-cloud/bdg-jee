#!/bin/bash

JAVA_HOME=/home/julien/apps/java/jdk1.7.0_51/
export JAVA_HOME

WORKSPACE=/home/julien/workspace_solstyce

cd $WORKSPACE/fr.legrain.autorisations.parent
mvn clean install

