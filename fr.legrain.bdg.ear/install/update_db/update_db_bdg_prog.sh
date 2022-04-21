#!/bin/bash

PGUSER=$1
PGPASSWORD=$2
export PGPASSWORD PGUSER

BDG_FILESYSTEM_PATH=$3

NOM_DOSSIER=$4

BUILD_FILE="./db.build_MAJ_bdg_prog.xml"

ANT_HOME=$BDG_FILESYSTEM_PATH/bdg/bin/tools/ant
ANT_LIB=$BDG_FILESYSTEM_PATH/bdg/lib


echo "==========================================="
echo "Mise à jour de la base de donnée 'bdg_prog'";
echo "==========================================="
	
echo "==================================================="
version=$(psql -U $PGUSER -h localhost -d bdg_prog -t -c "SELECT old_version from ta_version")
ver_trim=$(echo $version | sed 's/^ *//g' | sed 's/ *$//g')
echo "Version de la base de donnée : $ver_trim"
#ANT_TARGET="MAJ_$ver_trim"
ANT_TARGET="MAJ_0"
echo "==================================================="

echo "Mise à jour de la bdd à partir de la version $ver_trim ..."
echo "ANT_TARGET = $ANT_TARGET"
eval $ANT_HOME"/bin/ant -lib $ANT_LIB -buildfile $BUILD_FILE -DuserLoc=$PGUSER -DpassLoc=$PGPASSWORD -q $ANT_TARGET"
echo "Mise à jour de la bdd à partir de la version $ver_trim TERMINEE."
