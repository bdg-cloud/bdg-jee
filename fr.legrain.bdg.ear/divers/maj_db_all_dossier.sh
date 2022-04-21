#!/bin/bash
#./maj_db_all_dossier.sh ###_LOGIN_PG_BDG_### ###_PASSWORD_PG_BDG_### test.sql

PGUSER=$1
PGPASSWORD=$2
export PGPASSWORD PGUSER

FICHIER_SQL=$3
BDG_FILESYSTEM_PATH=$4

DATE=$(date +"%m-%d-%Y_%H-%M-%S")

echo "Dump de la bdd 'bdg'"
pg_dump -h localhost -U $PGUSER -F c -b -v -f "$BDG_FILESYSTEM_PATH/bdg/system/db_dumps/bdg_"$DATE"_bdg.backup" bdg

echo "============================================================================="
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
	
	#MAJ du schema du dossier
	#eval $ANT_HOME"/bin/ant -lib $ANT_LIB -buildfile $BUILD_FILE -DuserLoc=$PGUSER -DpassLoc=$PGPASSWORD -q -Ddossier=$NOM_DOSSIER $ANT_TARGET"
	#psql -U $PGUSER -h localhost -d bdg -t -n $NOM_DOSSIER -f $FICHIER_SQL
	psql "dbname=bdg options=--search_path=$NOM_DOSSIER" -h localhost -a -U $PGUSER -f $FICHIER_SQL
	
	#Archivage du fichier SQL
	#cp $FICHIER_SQL $BDG_FILESYSTEM_PATH//bdg/update_db/manuel/sql_archives/$DATE.sql
	
	echo "Mise à jour du schema $NOM_DOSSIER à partir du fichier $FICHIER_SQL TERMINEE."
done <<< "$schema"

echo "============================================================================="
echo "Liste des schema mis à jour"
echo "$schema"
echo "============================================================================="
