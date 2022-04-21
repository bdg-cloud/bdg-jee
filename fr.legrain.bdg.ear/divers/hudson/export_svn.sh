#!/bin/bash

FILES=/branches/b_2_0_13_JEE
REPOSITORY_PATH=/home/prog/svn
DEBUT_URL=file://
URL_SVN=$DEBUT_URL$REPOSITORY_PATH$FILES

EXPORT_PATH=b_2_0_13_JEE
DATE=$(date +"%m-%d-%Y_%H-%M-%S")

svn export $URL_SVN $EXPORT_PATH

rm -rf export_svn*.tgz
tar czf 'export_svn_'$EXPORT_PATH'_'$DATE'.tgz' $EXPORT_PATH

rm -rf $EXPORT_PATH

