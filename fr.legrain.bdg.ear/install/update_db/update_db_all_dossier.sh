#!/bin/bash

PGUSER=$1
PGPASSWORD=$2
export PGPASSWORD PGUSER

BDG_FILESYSTEM_PATH=$3

BUILD_FILE="./db.build_MAJ.xml"

ANT_HOME=$BDG_FILESYSTEM_PATH/bdg/bin/tools/ant
ANT_LIB=$BDG_FILESYSTEM_PATH/bdg/lib


schema=$(psql -U $PGUSER -h localhost -d bdg -t -c "SELECT schema_name FROM information_schema.schemata where schema_name<>'public' and schema_name<>'information_schema' and schema_name not like 'pg_%'")
echo "Liste des schema qui seront mis à jour"
echo "$schema"
echo "============================================================================="

while read schema_courant; 
do 
	echo "================================"
	echo Schema courant: "$schema_courant";
	echo "================================"
	schema_courant_trim=$(echo $schema_courant | sed 's/^ *//g' | sed 's/ *$//g')
	
	NOM_DOSSIER=$schema_courant_trim
	
	echo "==================================================="
	version=$(psql -U $PGUSER -h localhost -d bdg -t -c "SELECT old_version from $NOM_DOSSIER.ta_version")
	ver_trim=$(echo $version | sed 's/^ *//g' | sed 's/ *$//g')
	echo "Version du schema $schema_courant : $ver_trim"
	ANT_TARGET="MAJ_$ver_trim"
	#ANT_TARGET="MAJ_0"
	echo "==================================================="

	echo "Mise à jour de la bdd de $NOM_DOSSIER à partir de la version $ver_trim ..."
	echo "ANT_TARGET = $ANT_TARGET"
	
	v_detail=$(psql -U $PGUSER -h localhost -d bdg -t -c "select count(*) from $NOM_DOSSIER.ta_version")
	if [[ "$v_detail" != "1" ]]
	then
	#Etre sur qu'il y a bien une ligne dans la table ta_version du dossier
		psql -U $PGUSER -h localhost -d bdg -c "insert into $NOM_DOSSIER.ta_version (ID_VERSION,NUM_VERSION,old_VERSION,date_version) values(1,'$ver_trim','0',current_date)"
	fi
	
	#MAJ du schema du dossier
	eval $ANT_HOME"/bin/ant -lib $ANT_LIB -buildfile $BUILD_FILE -DuserLoc=$PGUSER -DpassLoc=$PGPASSWORD -q -Ddossier=$NOM_DOSSIER $ANT_TARGET"
	
	#MAJ du numero de version dans ta_version
	#DATE_DERNIERE_VERSION="2016/02/15"
	#NUMERO_DERNIERE_VERSION="3.0.0"
	#psql -U $PGUSER -h localhost -d bdg -c "update $NOM_DOSSIER.ta_version set old_version = '$ver_trim', num_version = '$NUMERO_DERNIERE_VERSION', date_version='$DATE_DERNIERE_VERSION'"

	echo "Mise à jour de la bdd de $NOM_DOSSIER à partir de la version $ver_trim TERMINEE."
done <<< "$schema"
