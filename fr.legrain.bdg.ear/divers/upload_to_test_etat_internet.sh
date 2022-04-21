#!/bin/bash

LOGIN_DEST=root
HOST_DEST=xxxxxxxxx

TMP_DIR=/tmp
INSTALL_FILE=setup_bdg.tar.gz

JBOSS_HOME=/var/lgr/wildfly-24.0.1.Final

BDG_FILESYSTEM_PATH=/var/lgr

PGUSER=###_LOGIN_PG_BDG_###
PGPASSWORD=###_PASSWORD_PG_BDG_###

LOGIN_ADMIN_LEGRAIN=xxxxxxxxx
MDP_ADMIN_LEGRAIN=xxxxxxxxx

SQL_BDG_PROG_PARAM=param_prod.sql

echo "Début envoi des fichier pour la mise à jour de l'application sur internet ====== $(date)"

ssh $LOGIN_DEST@$HOST_DEST rm $TMP_DIR/$INSTALL_FILE


scp -r "$WORKSPACE/$INSTALL_FILE" $LOGIN_DEST@$HOST_DEST:$TMP_DIR/

echo "Fin envoi des fichier pour la mise à jour de l'application sur internet ====== $(date)"

ssh $LOGIN_DEST@$HOST_DEST "cd $TMP_DIR;tar xvzf $INSTALL_FILE;cd $TMP_DIR/make_install;cat version.txt;./deploy.sh $BDG_FILESYSTEM_PATH $PGUSER $PGPASSWORD $JBOSS_HOME $LOGIN_ADMIN_LEGRAIN $MDP_ADMIN_LEGRAIN $SQL_BDG_PROG_PARAM lgr > /dev/null 2>&1 &"


echo "Mise à jour de l'application et de la bdd en arrière plan via SSH"

#ssh $LOGIN_DEST@$HOST_DEST "/srv/lgrg001/tmp_update/update_jboss_demo_internet.sh > /dev/null 2>&1 &"

echo "Pour voir les logs du serveur : tail -f $JBOSS_HOME/standalone/log/server.log"
