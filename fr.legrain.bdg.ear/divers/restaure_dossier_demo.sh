#!/bin/bash

#Re-créer le dossier/schema demo vide à partr du schema public

NOM_DOSSIER=demo
PGUSER=$1
PGPASSWORD=$2

export PGPASSWORD PGUSER

echo "Re-créer le dossier/schema demo vide à partr du schema public"

echo "Suppression du schema $NOM_DOSSIER de la base de donnees"
psql -U $PGUSER -h localhost -d bdg -c "DROP SCHEMA $NOM_DOSSIER CASCADE";

####
TMP_FILE="/tmp/create_schema_$NOM_DOSSIER.backup"
psql -U $PGUSER -h localhost -d bdg -c "alter schema public rename to $NOM_DOSSIER"
pg_dump  -h localhost -p 5432 -F c -b -v -n $NOM_DOSSIER -f $TMP_FILE bdg
psql -U $PGUSER -h localhost -d bdg -c "alter schema $NOM_DOSSIER rename to public"
psql -U $PGUSER -h localhost -d bdg -c "create schema $NOM_DOSSIER"
pg_restore  -h localhost -p 5432 -U $PGUSER -d bdg -v -n $NOM_DOSSIER $TMP_FILE
####
