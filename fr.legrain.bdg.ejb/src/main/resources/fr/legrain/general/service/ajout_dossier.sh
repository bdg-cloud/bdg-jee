#!/bin/bash


NOM_DOSSIER=$1
NOM_DOSSIER_POSTGRES=$2
JBOSS_HOME=$3
NOM_DOMAINE=$4
#Laisser vide si prodcution (pas de dev.xxx.promethee.biz)
PREFIXE_SOUS_DOMAINE=$5
PGUSER=$6
PGPASSWORD=$7
export PGPASSWORD PGUSER

LOGIN_ADMIN_LEGRAIN=$8
MDP_ADMIN_LEGRAIN=$9

UTILISATEUR_DOSSIER=${10}
MDP_DOSSIER=${11}

if [ -z "$UTILISATEUR_DOSSIER" ] && [ -z "$MDP_DOSSIER" ] 
then
	UTILISATEUR_DOSSIER=$NOM_DOSSIER
	MDP_DOSSIER=$NOM_DOSSIER
fi

DATE=$(date +"%m-%d-%Y_%H-%M-%S")

echo "=========================================================================================================================================="
echo "=========================================================================================================================================="
echo " Création d'un nouveau dossier "
echo "=========================================================================================================================================="
echo "Date : $DATE"
echo "JBOSS_HOME : $JBOSS_HOME"
echo "BDG_FILESYSTEM_PATH : $NOM_DOSSIER"
echo "PGUSER : $PGUSER"
echo "PGPASSWORD : $PGPASSWORD"
echo "NOM_DOMAINE : $NOM_DOMAINE"
echo "PREFIXE_SOUS_DOMAINE : $PREFIXE_SOUS_DOMAINE"
echo "Param 10 et 11 : $10 ** $11"
echo "LOGIN_ADMIN_LEGRAIN : $LOGIN_ADMIN_LEGRAIN"
echo "MDP_ADMIN_LEGRAIN : $MDP_ADMIN_LEGRAIN"
echo "UTILISATEUR_DOSSIER : $UTILISATEUR_DOSSIER"
echo "MDP_DOSSIER : $MDP_DOSSIER"
echo "=========================================================================================================================================="
echo "=========================================================================================================================================="


#STRUCTURE_VIDE=/donnees/Projet/Java/Eclipse/GestionCommerciale_branche_2_0_13_JEE_E43/fr.legrain.bdg.ear/divers/sql/dump_postgresql/backup_demo.backup

echo "Création du schema de base de donnees vide : $NOM_DOSSIER"
#psql -U $PGUSER -h localhost -d bdg -c "drop schema public"
#psql -U $PGUSER -h localhost -d bdg -c "create schema public"
##DROP SCHEMA pp CASCADE;

#echo "Restauration d'une structure de dossier vide dans le nouveau schema"
#pg_restore -h localhost -p 5432 -U $PGUSER -d bdg -v -n "public" $STRUCTURE_VIDE

####
TMP_FILE="/tmp/create_schema_$NOM_DOSSIER.backup"
psql -U $PGUSER -h localhost -d bdg -c "alter schema public rename to $NOM_DOSSIER_POSTGRES"
pg_dump -h localhost -p 5432 -F c -b -v -n $NOM_DOSSIER_POSTGRES -f $TMP_FILE bdg
psql -U $PGUSER -h localhost -d bdg -c "alter schema $NOM_DOSSIER_POSTGRES rename to public"
psql -U $PGUSER -h localhost -d bdg -c "create schema $NOM_DOSSIER_POSTGRES"
pg_restore -h localhost -p 5432 -U $PGUSER -d bdg -v -n $NOM_DOSSIER_POSTGRES $TMP_FILE
####

#echo "Renommer le schema avec son nom définitif"
#psql -U $PGUSER -h localhost -d demo -c "alter schema public rename to $NOM_DOSSIER"

echo "Créer le sous domaine $NOM_DOSSIER dans jboss"
cp $JBOSS_HOME/standalone/configuration/standalone.xml "$JBOSS_HOME/standalone/configuration/standalone_"$(date +"%m-%d-%Y_%H-%M-%S")"_avant_ajout_"$NOM_DOSSIER".xml" 
SCRIPTPATH=$(dirname $0)
VALEUR=$($SCRIPTPATH/une_ligne_jboss.rb "$($JBOSS_HOME/bin/jboss-cli.sh -c "/subsystem=undertow/server=default-server/host=other-host:read-attribute(name=alias)")")
echo "Nouveau sous domaine pour jboss : $VALEUR"
$JBOSS_HOME/bin/jboss-cli.sh -c "/subsystem=undertow/server=default-server/host=other-host:write-attribute(name=alias,value=[$VALEUR,$PREFIXE_SOUS_DOMAINE$NOM_DOSSIER.$NOM_DOMAINE])"

MDP=$(echo -n $MDP_DOSSIER | openssl dgst -sha256 -binary | openssl base64)

#echo "Créer un utilisateur ($NOM_DOSSIER) par défaut pour ce dossier avec le nom du dossier en login et mot de passe (utilisateur admin, non supprimable), penser à changer le mot de passe."
echo "Créer un utilisateur ($UTILISATEUR_DOSSIER) par défaut pour ce dossier (utilisateur admin, non supprimable), penser à changer le mot de passe."
#Nettoyage
psql -U $PGUSER -h localhost -d bdg -c "DELETE FROM $NOM_DOSSIER_POSTGRES.ta_r_role_utilisateur"
psql -U $PGUSER -h localhost -d bdg -c "ALTER SEQUENCE $NOM_DOSSIER_POSTGRES.ta_r_role_utilisateur_id_seq RESTART WITH 1"
psql -U $PGUSER -h localhost -d bdg -c "DELETE FROM $NOM_DOSSIER_POSTGRES.ta_utilisateur"
psql -U $PGUSER -h localhost -d bdg -c "ALTER SEQUENCE $NOM_DOSSIER_POSTGRES.ta_utilisateur_id_seq RESTART WITH 1"
#Création de l'utilisateur par défaut
psql -U $PGUSER -h localhost -d bdg -c "INSERT INTO $NOM_DOSSIER_POSTGRES.ta_utilisateur (username, passwd, admin_dossier, version_obj) VALUES ('$UTILISATEUR_DOSSIER', '$MDP', true, 0)"
#Attribution du role "admin" à l'utilisateur par défaut
E1=$(psql -d bdg -h localhost -tAc "SELECT id FROM $NOM_DOSSIER_POSTGRES.ta_utilisateur WHERE username='$UTILISATEUR_DOSSIER'")
psql -U $PGUSER -h localhost -d bdg -c "INSERT INTO $NOM_DOSSIER_POSTGRES.ta_r_role_utilisateur (id_utilisateur,id_role , version_obj) VALUES ($E1, 1, 0)"
#Ajout de l'utilisateur dans la base login_db pour le système de connexion
psql -U $PGUSER -h localhost -d login_db -c "INSERT INTO ta_utilisateur (username, passwd, version_obj) VALUES ('$UTILISATEUR_DOSSIER"_"$NOM_DOSSIER', '$MDP', 0)"
E2=$(psql -d login_db -h localhost -tAc "SELECT id FROM ta_utilisateur WHERE username='$UTILISATEUR_DOSSIER"_"$NOM_DOSSIER'")
psql -U $PGUSER -h localhost -d login_db -c "INSERT INTO ta_r_role_utilisateur (id_utilisateur,id_role , version_obj) VALUES ($E2, 1, 0)"

###
##Création d'un compte LEGRAIN pour la maintenance
LOGIN_ADMIN_LEGRAIN_LOGINDB=$LOGIN_ADMIN_LEGRAIN"_"$NOM_DOSSIER
MDP_ADMIN_LEGRAIN=$(echo -n $MDP_ADMIN_LEGRAIN | openssl dgst -sha256 -binary | openssl base64)

 echo "Création du compte d'administration pour LeGrain dans $NOM_DOSSIER"
  #Création de l'utilisateur par défaut
  psql -U $PGUSER -h localhost -d bdg -c "INSERT INTO $NOM_DOSSIER_POSTGRES.ta_utilisateur (username, passwd, admin_dossier, systeme, version_obj) VALUES ('$LOGIN_ADMIN_LEGRAIN', '$MDP_ADMIN_LEGRAIN', true, true, 0)"
E3=$(psql -d bdg -h localhost -tAc "SELECT id FROM $NOM_DOSSIER_POSTGRES.ta_utilisateur WHERE username='$LOGIN_ADMIN_LEGRAIN'")
  #Attribution du role "admin" à l'utilisateur par défaut
  psql -U $PGUSER -h localhost -d bdg -c "INSERT INTO $NOM_DOSSIER_POSTGRES.ta_r_role_utilisateur (id_utilisateur,id_role , version_obj) VALUES ($E3, 1, 0)"

  #Ajout de l'utilisateur dans la base login_db pour le système de connexion
  echo "Création du compte d'administration pour LeGrain dans $login_db"
  psql -U $PGUSER -h localhost -d login_db -c "INSERT INTO ta_utilisateur (username, passwd, version_obj) VALUES ('$LOGIN_ADMIN_LEGRAIN_LOGINDB', '$MDP_ADMIN_LEGRAIN', 0)"
E4=$(psql -d login_db -h localhost -tAc "SELECT id FROM ta_utilisateur WHERE username='$LOGIN_ADMIN_LEGRAIN_LOGINDB'")
  psql -U $PGUSER -h localhost -d login_db -c "INSERT INTO ta_r_role_utilisateur (id_utilisateur,id_role , version_obj) VALUES ($E4, 1, 0)"

#
# Faire des INSERT par défaut ?
# Création de dossier/fichier physique hors bdd ?
#

echo "Nouveau dossier $NOM_DOSSIER créé :"
echo "URL : http://$PREFIXE_SOUS_DOMAINE$NOM_DOSSIER.$NOM_DOMAINE"
echo "-- Penser à modifier les DNS ou fichiers hosts et à relancer jboss pour prendre en compte le sous domaine"
echo "Login : $NOM_DOSSIER"
echo "Password : $NOM_DOSSIER"

