#!/bin/bash

#cron, redémarrage des serveurs jboss et mysql tous les jours à 3h
#0 3 * * * /root/reboot_jboss.sh

#JBOSS_HOME="/data1/jboss-as-7.1.3.Final"
#JBOSS_HOME="/data1/wildfly-8.1.0.Final"
#JBOSS_HOME="/data1/wildfly-8.2.0.Final"

BDG_FILESYSTEM_PATH=$1

PGUSER=$2
PGPASSWORD=$3

JBOSS_HOME=$4

LOGIN_ADMIN_LEGRAIN=$5
MDP_ADMIN_LEGRAIN=$6

SQL_BDG_PROG_PARAM=$7

SERVEUR_CLIENT="VRAI"
if [[ "$8" == "lgr" ]]
  then 
  SERVEUR_CLIENT="FAUX"
fi

export PGPASSWORD PGUSER

DATE=$(date +"%Y-%m-%d_%H-%M-%S")

echo "=========================================================================================================================================="
echo "=========================================================================================================================================="
echo "WildFly (8.2+) doit être installé sur la machine"
echo "PostgreSQL (9.3+) doit être installé sur la machine"
echo "=========================================================================================================================================="
echo "Date : $DATE"
echo "JBOSS_HOME : $JBOSS_HOME"
echo "BDG_FILESYSTEM_PATH : $BDG_FILESYSTEM_PATH"
echo "PGUSER : $PGUSER"
echo "PGPASSWORD : $PGPASSWORD"
echo "LOGIN_ADMIN_LEGRAIN : $LOGIN_ADMIN_LEGRAIN"
echo "MDP_ADMIN_LEGRAIN : $MDP_ADMIN_LEGRAIN"
echo "SQL_BDG_PROG_PARAM : $SQL_BDG_PROG_PARAM"
echo "Param 8 : $8 - $SERVEUR_CLIENT"
echo "=========================================================================================================================================="
echo "=========================================================================================================================================="

echo "Initialisation et préparation du déploiement ..."

rm $BDG_FILESYSTEM_PATH/bdg/update/*
cd install
./install.sh $BDG_FILESYSTEM_PATH $PGUSER $PGPASSWORD
cd ..
SECRET_PATH=$(pwd)/secrets
cp -r fr.legrain.bdg.ear.ear $BDG_FILESYSTEM_PATH/bdg/update/
cp -r JEE_Tiers_Article_Devis* $BDG_FILESYSTEM_PATH/bdg/bin/clients/
if [[ "$SERVEUR_CLIENT" != "VRAI" ]]
  then 
cp -r fr.legrain.autorisations.ear.ear $BDG_FILESYSTEM_PATH/bdg/update/
cp -r fr.legrain.moncompte.ear.ear $BDG_FILESYSTEM_PATH/bdg/update/
cp -r fr.legrain.espaceclient.ear.ear $BDG_FILESYSTEM_PATH/bdg/update/
fi

cp -r backup_bdg.backup $BDG_FILESYSTEM_PATH/bdg/update/
cp -r backup_bdg_prog.backup $BDG_FILESYSTEM_PATH/bdg/update/
cp -r backup_login_db.backup $BDG_FILESYSTEM_PATH/bdg/update/
if [[ "$SERVEUR_CLIENT" != "VRAI" ]]
  then 
cp -r backup_moncompte.backup $BDG_FILESYSTEM_PATH/bdg/update/
cp -r backup_autorisations.backup $BDG_FILESYSTEM_PATH/bdg/update/
cp -r backup_compteclient.backup $BDG_FILESYSTEM_PATH/bdg/update/
fi
cp -r *.sh $BDG_FILESYSTEM_PATH/bdg/bin/

cp -r postgresql-9.3-1101.jdbc41.jar $BDG_FILESYSTEM_PATH/bdg/lib/
cp -r postgresql-9.3-1101.jdbc41.jar $BDG_FILESYSTEM_PATH/bdg/bin/tools/liquibase/lib/

rm -rf $BDG_FILESYSTEM_PATH/bdg/update_db/*

cp -r install/update_db/* $BDG_FILESYSTEM_PATH/bdg/update_db/

cp -r update.sh $BDG_FILESYSTEM_PATH/bdg/update/

echo "Fin de l'initialisation => début du déploiement ..."

#echo "Mise à jour de l'application et de la bdd en arrière plan"


echo "=========================================================================================================================================="
echo "=========================================================================================================================================="

echo "Début (re)déploiement de l'application sur jboss ====== $(date)" >> /var/log/log_reboot_jboss.txt

ps -ae | grep java;ps -ae | grep sh

#PID_JAVA=$(pidof java) #attention au tomcat
#PID_JBOSS=$(pidof -x standalone.sh)
PID_JBOSS=$(cat $JBOSS_HOME/pid)

echo "PID_JAVA = $PID_JAVA  ==== PID_JBOSS = $PID_JBOSS"
echo "Arret de JBoss/WildFly"
$JBOSS_HOME/bin/jboss-cli.sh -c :shutdown
#kill -9 $(cat /data1/jboss-as-7.1.3.Final/pid)

kill -9 $PID_JAVA $PID_JBOSS

ps -ae | grep java;ps -ae | grep sh

#echo "Restauration d'une BDD de test"
#sudo  -u postgres

echo "Sauvegarde (pg_dump) de certains schema au debut de deploiement"
a=$(pwd)
cd $BDG_FILESYSTEM_PATH/bdg/update_db
$BDG_FILESYSTEM_PATH/bdg/update_db/sauvegarde_schemas.sh $PGUSER $PGPASSWORD $BDG_FILESYSTEM_PATH
cd $a

echo "..Dump ou création des bases de données"
#pg_dump  -h localhost -p 5432 -F p -b -v -f "bdg.sql" bdg
#pg_dump  -h localhost -p 5432 -F p -b -v -f "bdg.sql" -n "nom_schema" bdg
if [[ `psql -tAc "SELECT 1 FROM pg_database WHERE datname='bdg'"` == "1" ]]
then
    echo "Database bdg already exists => DUMP"
    pg_dump -h localhost -p 5432 -U bdg -F c -b -v -f "$BDG_FILESYSTEM_PATH/bdg/system/db_dumps/bdg_"$DATE"_bdg.backup" bdg
     echo "DUMP OK"
else
    echo "Database bdg does not exist => CREATE"
    createdb -O bdg -E UTF8 bdg
fi

if [[ `psql -tAc "SELECT 1 FROM pg_database WHERE datname='bdg_prog'"` == "1" ]]
then
    echo "Database bdg already exists => DUMP"
    pg_dump -h localhost -p 5432 -U bdg -F c -b -v -f "$BDG_FILESYSTEM_PATH/bdg/system/db_dumps/bdg_"$DATE"_bdg_prog.backup" bdg_prog
     echo "DUMP OK"
else
    echo "Database bdg does not exist => CREATE"
    createdb -O bdg -E UTF8 bdg_prog
fi

if [[ `psql -tAc "SELECT 1 FROM pg_database WHERE datname='login_db'"` == "1" ]]
then
    echo "Database login_db already exists => DUMP"
    pg_dump  -h localhost -p 5432 -U bdg -F c -b -v -f "$BDG_FILESYSTEM_PATH/bdg/system/db_dumps/bdg_"$DATE"_login_db.backup" login_db
else
    echo "Database login_db does not exist => CREATE"
    createdb -O bdg -E UTF8 login_db
   
fi

if [[ "$SERVEUR_CLIENT" != "VRAI" ]]
  then
  
  if [[ `psql -tAc "SELECT 1 FROM pg_database WHERE datname='moncompte'"` == "1" ]]
then
    echo "Database moncompte already exists => DUMP"
    pg_dump  -h localhost -p 5432 -U bdg -F c -b -v -f "$BDG_FILESYSTEM_PATH/bdg/system/db_dumps/bdg_"$DATE"_moncompte.backup" moncompte
else
    echo "Database moncompte does not exist => CREATE"
    createdb -O bdg -E UTF8 moncompte
fi


if [[ `psql -tAc "SELECT 1 FROM pg_database WHERE datname='autorisations'"` == "1" ]]
then
    echo "Database autorisations already exists => DUMP"
pg_dump  -h localhost -p 5432 -U bdg -F c -b -v -f "$BDG_FILESYSTEM_PATH/bdg/system/db_dumps/bdg_"$DATE"_autorisations.backup" autorisations
else
    echo "Database autorisations does not exist => CREATE"
    createdb -O bdg -E UTF8 autorisations
fi

if [[ `psql -tAc "SELECT 1 FROM pg_database WHERE datname='compteclient'"` == "1" ]]
then
    echo "Database compteclient already exists => DUMP"
pg_dump  -h localhost -p 5432 -U bdg -F c -b -v -f "$BDG_FILESYSTEM_PATH/bdg/system/db_dumps/bdg_"$DATE"_compteclient.backup" compteclient
else
    echo "Database compteclient does not exist => CREATE"
    createdb -O bdg -E UTF8 compteclient
fi

fi

#echo "..Suppression de la base de données"
#dropdb bdg
#dropdb moncompte
#dropdb autorisations
#dropdb login_db

#echo "..Création de la base de données"
#createdb -O bdg -E UTF8 bdg
#createdb -O bdg -E UTF8 moncompte
#createdb -O bdg -E UTF8 autorisations
#createdb -O bdg -E UTF8 login_db

#echo "..Restauration de la base de données"
##psql bdg < bdg.sql
#pg_restore  -h localhost -p 5432 -U bdg -d bdg -v $WORKSPACE"/svn/fr.legrain.bdg.ear/divers/sql/dump_postgresql/backup_bdg.backup"
#pg_restore  -h localhost -p 5432 -U bdg -d moncompte -v $WORKSPACE"/svn/fr.legrain.moncompte.ear/divers/sql/dump_postgresql/backup_moncompte.backup"
#pg_restore  -h localhost -p 5432 -U bdg -d autorisations -v $WORKSPACE"/svn/fr.legrain.autorisations.ear/divers/sql/dump_postgresql/backup_autorisations.backup"
#pg_restore  -h localhost -p 5432 -U bdg -d login_db -v $WORKSPACE"/svn/fr.legrain.bdg.ear/divers/sql_login_db/dump_postgresql/backup_login_db.backup"

echo "..Mise à jour de la structure des bases de données 'bdg' (tous les schémas/dossiers)"
echo "Restauration du schéma public à partir du dump de développement"
psql -U $PGUSER -h localhost -d bdg -c "DROP SCHEMA IF EXISTS public CASCADE"
psql -U $PGUSER -h localhost -d bdg -c "create schema public"
pg_restore  -h localhost -p 5432 -U $PGUSER -d bdg -n public -v $BDG_FILESYSTEM_PATH/bdg/update/backup_bdg.backup
echo "Mise à jour des dossiers utilisateurs"
#La mise a jour se fait via l'interface web ou via un client SQL (pour l'instant)
#a=$(pwd)
#cd $BDG_FILESYSTEM_PATH/bdg/update_db
#$BDG_FILESYSTEM_PATH/bdg/update_db/update_db_all_dossier.sh $PGUSER $PGPASSWORD $BDG_FILESYSTEM_PATH
#cd $a

echo "..Mise à jour de la structure des bases de données 'bdg_prog'"
echo "Restauration du schéma public à partir du dump de développement"
echo "Mise à jour ..."
#La mise a jour se fait via l'interface web ou via un client SQL (pour l'instant)
#a=$(pwd)
#cd $BDG_FILESYSTEM_PATH/bdg/update_db
#$BDG_FILESYSTEM_PATH/bdg/update_db/update_db_bdg_prog.sh $PGUSER $PGPASSWORD $BDG_FILESYSTEM_PATH
##cd $a

echo "..Mise à jour de la structure des bases de données 'login_db'"
echo "Restauration du schéma public à partir du dump de développement"
#psql -U $PGUSER -h localhost -d login_db -c "DROP SCHEMA IF EXISTS public CASCADE"
#psql -U $PGUSER -h localhost -d login_db -c "create schema public"
#pg_restore  -h localhost -p 5432 -U $PGUSER -d login_db -n public -v $BDG_FILESYSTEM_PATH/bdg/update/backup_login_db.backup
echo "Mise à jour ..."
#La mise a jour se fait via l'interface web ou via un client SQL (pour l'instant)
#a=$(pwd)
#cd $BDG_FILESYSTEM_PATH/bdg/update_db
#$BDG_FILESYSTEM_PATH/bdg/update_db/update_db_login_db.sh $PGUSER $PGPASSWORD $BDG_FILESYSTEM_PATH
##cd $a

if [[ "$SERVEUR_CLIENT" != "VRAI" ]]
  then
#A FINIR
echo "..Mise à jour de la structure de la base de données 'autorisations'"
# a faire
echo "..Mise à jour de la structure de la base de données 'moncompte'"
# a faire
fi

echo "..Restauration du dossier de démonstration 'vide'"
$BDG_FILESYSTEM_PATH/bdg/bin/restaure_dossier_demo.sh $PGUSER $PGPASSWORD 

echo "..Mise à jour des paramètres dans la base programme ta_parametre"
if [[ "$SERVEUR_CLIENT" != "VRAI" ]]
  then 
  #echo "$SECRET_PATH/$SQL_BDG_PROG_PARAM" > aa.txt
psql -U $PGUSER -h localhost -d bdg_prog -f "$SECRET_PATH/$SQL_BDG_PROG_PARAM"
fi

#Si installtion en local et le schema "local" n'existe, le créer
if [[ "$SERVEUR_CLIENT" == "VRAI" ]]
  then
  
  #NOM_DOSSIER_LOCAL="local"
  NOM_DOSSIER_LOCAL=$8
  MDP_DOSSIER_LOCAL=$(echo -n "admin" | openssl dgst -sha256 -binary | openssl base64)
  LOGIN_DOSSIER_LOCAL="admin"
  TMP_FILE="/tmp/create_schema_$NOM_DOSSIER_LOCAL.backup"
  
  schema_local=$(psql -U $PGUSER -h localhost -d bdg -tAc "SELECT 1 FROM information_schema.schemata where schema_name='$NOM_DOSSIER_LOCAL'")
  
  if [[ "$schema_local" != "1" ]]
  then
    psql -U $PGUSER -h localhost -d bdg -c "alter schema public rename to $NOM_DOSSIER_LOCAL"
    pg_dump  -h localhost -p 5432 -F c -b -v -n $NOM_DOSSIER_LOCAL -f $TMP_FILE bdg
    psql -U $PGUSER -h localhost -d bdg -c "alter schema $NOM_DOSSIER_LOCAL rename to public"
    psql -U $PGUSER -h localhost -d bdg -c "create schema $NOM_DOSSIER_LOCAL"
    pg_restore  -h localhost -p 5432 -U $PGUSER -d bdg -v -n $NOM_DOSSIER_LOCAL $TMP_FILE
    
    psql -U $PGUSER -h localhost -d bdg -c "DELETE FROM $NOM_DOSSIER_LOCAL.ta_r_role_utilisateur"
    psql -U $PGUSER -h localhost -d bdg -c "ALTER SEQUENCE $NOM_DOSSIER_LOCAL.ta_r_role_utilisateur_id_seq RESTART WITH 1"
    psql -U $PGUSER -h localhost -d bdg -c "DELETE FROM $NOM_DOSSIER_LOCAL.ta_utilisateur"
    psql -U $PGUSER -h localhost -d bdg -c "ALTER SEQUENCE $NOM_DOSSIER_LOCAL.ta_utilisateur_id_seq RESTART WITH 1"
    #Création de l'utilisateur par défaut
    psql -U $PGUSER -h localhost -d bdg -c "INSERT INTO $NOM_DOSSIER_LOCAL.ta_utilisateur (username, passwd, admin_dossier, version_obj) VALUES ('$LOGIN_DOSSIER_LOCAL', '$MDP_DOSSIER_LOCAL', true, 0)"
    #Attribution du role "admin" à l'utilisateur par défaut
    psql -U $PGUSER -h localhost -d bdg -c "INSERT INTO $NOM_DOSSIER_LOCAL.ta_r_role_utilisateur (id_utilisateur,id_role , version_obj) VALUES (1, 1, 0)"
    #Ajout de l'utilisateur dans la base login_db pour le système de connexion
    psql -U $PGUSER -h localhost -d login_db -c "INSERT INTO ta_utilisateur (username, passwd, version_obj) VALUES ('$LOGIN_DOSSIER_LOCAL"_"$NOM_DOSSIER_LOCAL', '$MDP_DOSSIER_LOCAL', 0)"
    psql -U $PGUSER -h localhost -d login_db -c "INSERT INTO ta_r_role_utilisateur (id_utilisateur,id_role , version_obj) VALUES (1, 1, 0)"
  fi
fi

#Restauration/création de l'accès LeGrain
schema=$(psql -U $PGUSER -h localhost -d bdg -t -c "SELECT schema_name FROM information_schema.schemata where schema_name<>'public' and schema_name<>'information_schema' and schema_name not like 'pg_%'")
echo "Liste des schema"
echo "$schema"
echo "============================================================================="
MDP_ADMIN_LEGRAIN=$(echo -n "$MDP_ADMIN_LEGRAIN" | openssl dgst -sha256 -binary | openssl base64)

while read schema_courant; 
do 
	schema_courant_trim=$(echo $schema_courant | sed 's/^ *//g' | sed 's/ *$//g')
	NOM_DOSSIER=$schema_courant_trim
	LOGIN_ADMIN_LEGRAIN_LOGINDB=$LOGIN_ADMIN_LEGRAIN"_"$NOM_DOSSIER
	
	R=$(psql -U $PGUSER -h localhost -d bdg -tAc "SELECT 1 FROM $NOM_DOSSIER.ta_utilisateur WHERE username='$LOGIN_ADMIN_LEGRAIN'")
	R1=$(psql -U $PGUSER -h localhost -d login_db -tAc "SELECT 1 FROM ta_utilisateur WHERE username='$LOGIN_ADMIN_LEGRAIN_LOGINDB'")

if [[ "$R" != "1" ]]
then
  echo "Création du compte d'administration pour LeGrain dans $NOM_DOSSIER"
  #Création de l'utilisateur par défaut
  psql -U $PGUSER -h localhost -d bdg -c "INSERT INTO $NOM_DOSSIER.ta_utilisateur (username, passwd, admin_dossier, systeme, version_obj) VALUES ('$LOGIN_ADMIN_LEGRAIN', '$MDP_ADMIN_LEGRAIN', true, true, 0)"
  E1=$(psql -d bdg -tAc "SELECT id FROM $NOM_DOSSIER.ta_utilisateur WHERE username='$LOGIN_ADMIN_LEGRAIN'")
  #Attribution du role "admin" à l'utilisateur par défaut
  psql -U $PGUSER -h localhost -d bdg -c "INSERT INTO $NOM_DOSSIER.ta_r_role_utilisateur (id_utilisateur,id_role , version_obj) VALUES ($E1, 1, 0)"
fi
  
  if [[ "$R1" != "1" ]]
then
  #Ajout de l'utilisateur dans la base login_db pour le système de connexion
  echo "Création du compte d'administration pour LeGrain dans $login_db"
  psql -U $PGUSER -h localhost -d login_db -c "INSERT INTO ta_utilisateur (username, passwd, version_obj) VALUES ('$LOGIN_ADMIN_LEGRAIN_LOGINDB', '$MDP_ADMIN_LEGRAIN', 0)"
  E2=$(psql -d login_db -tAc "SELECT id FROM ta_utilisateur WHERE username='$LOGIN_ADMIN_LEGRAIN_LOGINDB'")
  psql -U $PGUSER -h localhost -d login_db -c "INSERT INTO ta_r_role_utilisateur (id_utilisateur,id_role , version_obj) VALUES ($E2, 1, 0)"
fi

done <<< "$schema"


echo "Mise à jour des EAR"
#Sauvegarde/archivage
mv $JBOSS_HOME/standalone/deployments/fr.legrain.bdg.ear.ear $BDG_FILESYSTEM_PATH/bdg/system/backup/fr.legrain.bdg.ear_$DATE.ear
if [[ "$SERVEUR_CLIENT" != "VRAI" ]]
  then
mv $JBOSS_HOME/standalone/deployments/fr.legrain.autorisations.ear.ear $BDG_FILESYSTEM_PATH/bdg/system/backup/fr.legrain.autorisations.ear_$DATE.ear
mv $JBOSS_HOME/standalone/deployments/fr.legrain.moncompte.ear.ear $BDG_FILESYSTEM_PATH/bdg/system/backup/fr.legrain.moncompte.ear_$DATE.ear
mv $JBOSS_HOME/standalone/deployments/fr.legrain.espaceclient.ear.ear $BDG_FILESYSTEM_PATH/bdg/system/backup/fr.legrain.espaceclient.ear.ear_$DATE.ear
fi
#Nettoyage
rm -rf $JBOSS_HOME/standalone/deployments/*
#Déploiement des nouveau EAR
cp $BDG_FILESYSTEM_PATH/bdg/update/fr.legrain.bdg.ear.ear $JBOSS_HOME/standalone/deployments/
if [[ "$SERVEUR_CLIENT" != "VRAI" ]]
  then
cp $BDG_FILESYSTEM_PATH/bdg/update/fr.legrain.autorisations.ear.ear $JBOSS_HOME/standalone/deployments/
cp $BDG_FILESYSTEM_PATH/bdg/update/fr.legrain.moncompte.ear.ear $JBOSS_HOME/standalone/deployments/
cp $BDG_FILESYSTEM_PATH/bdg/update/fr.legrain.espaceclient.ear.ear $JBOSS_HOME/standalone/deployments/
fi

echo "Redémarrage du SGBD"
#/usr/sbin/service mysql restart
#/etc/init.d/postgresql restart
#/etc/init.d/postgresql stop
#/etc/init.d/postgresql start
systemctl restart postgresql@9.5.service

echo "Redémarrage de JBoss/WildFly"
#/usr/sbin/service jboss start
/etc/init.d/jboss start


ps -ae | grep java;ps -ae | grep sh

echo "Fin (re)déploiement de l'application sur jboss  ====== $(date)" >> /var/log/log_reboot_jboss.txt

