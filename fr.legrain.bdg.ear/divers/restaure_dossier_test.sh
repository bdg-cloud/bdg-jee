#!/bin/bash

#Re-créer le dossier/schema demo vide à partr du schema public

NOM_DOSSIER="test"
PGUSER=$1
PGPASSWORD=$2
#mot de passe test : "test"
MDP=$(echo -n "test" | openssl dgst -sha256 -binary | openssl base64)

LOGIN_ADMIN_LEGRAIN="xxxxxxx"
MDP_ADMIN_LEGRAIN=$(echo -n "xxxxxxx" | openssl dgst -sha256 -binary | openssl base64)

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

#Création de l'utilisateur par défaut
psql -U $PGUSER -h localhost -d bdg -c "INSERT INTO $NOM_DOSSIER.ta_utilisateur (username, passwd, admin_dossier, version_obj) VALUES ('$NOM_DOSSIER', '$MDP', true, 0)"
#Attribution du role "admin" à l'utilisateur par défaut
E1=$(psql -d bdg -tAc "SELECT id FROM $NOM_DOSSIER.ta_utilisateur WHERE username='$NOM_DOSSIER'")
psql -U $PGUSER -h localhost -d bdg -c "INSERT INTO $NOM_DOSSIER.ta_r_role_utilisateur (id_utilisateur,id_role , version_obj) VALUES ($E1, 1, 0)"
#Ajout de l'utilisateur dans la base login_db pour le système de connexion
psql -U $PGUSER -h localhost -d login_db -c "INSERT INTO ta_utilisateur (username, passwd, version_obj) VALUES ('$NOM_DOSSIER"_"$NOM_DOSSIER', '$MDP', 0)"
E2=$(psql -d login_db -tAc "SELECT id FROM ta_utilisateur WHERE username='$NOM_DOSSIER"_"$NOM_DOSSIER'")
psql -U $PGUSER -h localhost -d login_db -c "INSERT INTO ta_r_role_utilisateur (id_utilisateur,id_role , version_obj) VALUES ($E2, 1, 0)"

##################################################################


LOGIN_ADMIN_LEGRAIN_LOGINDB=$LOGIN_ADMIN_LEGRAIN"_"$NOM_DOSSIER
R=$(psql -d bdg -tAc "SELECT 1 FROM $NOM_DOSSIER.ta_utilisateur WHERE username='$LOGIN_ADMIN_LEGRAIN'")
R1=$(psql -d login_db -tAc "SELECT 1 FROM ta_utilisateur WHERE username='$LOGIN_ADMIN_LEGRAIN_LOGINDB'")

if [[ "$R" != "1" ]]
then
  echo "Création du compte d'administration pour LeGrain dans $NOM_DOSSIER ** $R"
  #Création de l'utilisateur par défaut
  psql -h localhost -d bdg -c "INSERT INTO $NOM_DOSSIER.ta_utilisateur (username, passwd, admin_dossier, systeme, version_obj) VALUES ('$LOGIN_ADMIN_LEGRAIN', '$MDP_ADMIN_LEGRAIN', true, true, 0)"
  E1=$(psql -d bdg -tAc "SELECT id FROM $NOM_DOSSIER.ta_utilisateur WHERE username='$LOGIN_ADMIN_LEGRAIN'")
  #Attribution du role "admin" à l'utilisateur par défaut
  psql -h localhost -d bdg -c "INSERT INTO $NOM_DOSSIER.ta_r_role_utilisateur (id_utilisateur,id_role , version_obj) VALUES ($E1, 1, 0)"
fi
  
  if [[ "$R1" != "1" ]]
then
  #Ajout de l'utilisateur dans la base login_db pour le système de connexion
  echo "Création du compte d'administration pour LeGrain dans login_db ** $R1"
  psql -h localhost -d login_db -c "INSERT INTO ta_utilisateur (username, passwd, version_obj) VALUES ('$LOGIN_ADMIN_LEGRAIN_LOGINDB', '$MDP_ADMIN_LEGRAIN', 0)"
  E2=$(psql -d login_db -tAc "SELECT id FROM ta_utilisateur WHERE username='$LOGIN_ADMIN_LEGRAIN_LOGINDB'")
  psql -h localhost -d login_db -c "INSERT INTO ta_r_role_utilisateur (id_utilisateur,id_role , version_obj) VALUES ($E2, 1, 0)"
fi
