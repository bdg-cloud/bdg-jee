#!/bin/bash

#Restauration d'un dossier/schema à partir d'un dump postgres

NOM_DOSSIER=$1
PGUSER=$2
PGPASSWORD=$3

echo "Restauration du dossier/schema $NOM_DOSSIER à partir d'un dump postgres ($BACKUP_FILE) ..."

#Le fichier contentant le dump du schema à restaurer
BACKUP_FILE=$4

export PGPASSWORD PGUSER

echo "Création d'un dump/sauvegarde du schema $NOM_DOSSIER avant suppression"
TMP_FILE="/tmp/dump_schema_$NOM_DOSSIER-$(date +"%m-%d-%Y_%H-%M-%S")_before_restaure.backup"
pg_dump  -h localhost -p 5432 -F c -b -v -n $NOM_DOSSIER -f $TMP_FILE bdg

echo "Suppression du schema $NOM_DOSSIER de la base de donnees"
psql -U $PGUSER -h localhost -d bdg -c "DROP SCHEMA $NOM_DOSSIER CASCADE";

#### 
psql -U $PGUSER -h localhost -d bdg -c "create schema $NOM_DOSSIER"
pg_restore  -h localhost -p 5432 -U $PGUSER -d bdg -v -n $NOM_DOSSIER $BACKUP_FILE
####
