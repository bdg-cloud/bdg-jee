#!/bin/bash



NOM_DOSSIER=$1
NOM_DOSSIER_POSTGRES=$2
#Laisser vide si prodcution (pas de dev.xxx.promethee.biz)
JBOSS_HOME=$3
NOM_DOMAINE=$4
PREFIXE_SOUS_DOMAINE=$5
PGUSER=$6
PGPASSWORD=$7
export PGPASSWORD PGUSER

echo "Création d'un dump/sauvegarde du schema $NOM_DOSSIER avant suppression"
TMP_FILE="/tmp/dump_schema_$NOM_DOSSIER-$(date +"%m-%d-%Y_%H-%M-%S")_before_delete.backup"
pg_dump -h localhost -p 5432 -F c -b -v -n $NOM_DOSSIER_POSTGRES -f $TMP_FILE bdg

echo "Suppression du schema $NOM_DOSSIER de la base de donnees"
psql -U $PGUSER -h localhost -d bdg -c "DROP SCHEMA $NOM_DOSSIER_POSTGRES CASCADE";

echo "Supprimer le sous domaine $NOM_DOSSIER dans jboss"
cp $JBOSS_HOME/standalone/configuration/standalone.xml "$JBOSS_HOME/standalone/configuration/standalone_"$(date +"%m-%d-%Y_%H-%M-%S")"_avant_suppr_"$NOM_DOSSIER".xml" 
SCRIPTPATH=$(dirname $0)
VALEUR=$($SCRIPTPATH/une_ligne_jboss.rb "$($JBOSS_HOME/bin/jboss-cli.sh -c "/subsystem=undertow/server=default-server/host=other-host:read-attribute(name=alias)")")
echo "Ancien sous domaine pour jboss : $VALEUR"
PATERN=\"$PREFIXE_SOUS_DOMAINE$NOM_DOSSIER.$NOM_DOMAINE\"
VAL_SUPPR=$(echo $VALEUR | sed s/,$PATERN//g)
echo "Sous domaine après suppression pour jboss : $VAL_SUPPR"
$JBOSS_HOME/bin/jboss-cli.sh -c "/subsystem=undertow/server=default-server/host=other-host:write-attribute(name=alias,value=[$VAL_SUPPR])"

echo "Supprimer les utilisateurs de $NOM_DOSSIER dans la base de connection"

psql -U $PGUSER -h localhost -d login_db -c "DELETE FROM ta_utilisateur WHERE username like '%_$NOM_DOSSIER'"

echo "Dossier $NOM_DOSSIER supprimé"
echo "-- Penser à relancer jboss pour prendre en compte la suppression du sous domaine (mettre à jour DNS et/ou fichier hosts)"
