#!/bin/bash

PGUSER=$1
PGPASSWORD=$2
export PGPASSWORD PGUSER

BDG_FILESYSTEM_PATH=$3

#schema=$(psql -U $PGUSER -h localhost -d bdg -t -c "SELECT schema_name FROM information_schema.schemata where schema_name<>'public' and schema_name<>'information_schema' and schema_name not like 'pg_%'")

schema="demo
ltg
shopevasion
traiteurtmp
orgaveg
opiopi
bach
anscaire
apiyou
armagnacvidange
cambonp
canardises
chevrequisaourit
coeffard
entrepriseflaman
etangaumiroir
etche
fermelarrey
fontduloup
gaecdelapignole
geosol
larroude
legrain82
lemasdambane
loyes
mamprin
manda
mandarnaud
marzin
masagri"

echo "Liste des schema qui seront sauvegardes"
echo "$schema"
echo "============================================================================="

while read schema_courant; 
do 
	echo "================================"
	echo Schema courant: "$schema_courant";
	echo "================================"
	schema_courant_trim=$(echo $schema_courant | sed 's/^ *//g' | sed 's/ *$//g')
	
	NOM_DOSSIER=$schema_courant_trim
	
	##########################

	#pg_dump -h localhost -p 5432 -U $PGUSER -F c -b -f /var/lgr/bdg/system/db_dumps/bdg_alldb"_$(date +%Y-%m-%d_%H-%M-%S)".backup bdg;

	pg_dump -h localhost -p 5432 -U $PGUSER -F c -b -n $NOM_DOSSIER -f $BDG_FILESYSTEM_PATH/bdg/system/db_dumps/bdg_$NOM_DOSSIER"_$(date +%Y-%m-%d_%H-%M-%S)".backup bdg;
	
	#######################		

done <<< "$schema"
