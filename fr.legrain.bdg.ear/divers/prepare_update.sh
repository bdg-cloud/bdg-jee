#!/bin/bash

#LOGIN_DEST=admin
LOGIN_DEST=root
HOST_DEST=localhost
#TMP_DIR=/srv/lgrg001/tmp_update
BDG_FILESYSTEM_PATH=/data1/

JBOSS_HOME="/data1/wildfly-8.2.0.Final"
PGUSER=###_LOGIN_PG_BDG_###
PGPASSWORD=###_PASSWORD_PG_BDG_###


echo "Début envoi des fichier pour la mise à jour de l'application sur internet ====== $(date) === r$SVN_REVISION"
#echo "Envoie des fichier pour la mise à jour de l'application sur internet ====== $(date)" >> /var/log/log_reboot_jboss.txt

if [[ $HOST_DEST != 'localhost' ]]
  then 

echo "Copie sur un serveur distant par SSH"

#ssh $LOGIN_DEST@$HOST_DEST rm $BDG_FILESYSTEM_PATH/update/*
#scp -r $WORKSPACE"/svn/fr.legrain.bdg.ear/install" /tmp/
#/tmp/install.sh
#scp -r /data1/hudson/jobs/solstyce/workspace/svn/fr.legrain.bdg.ear/target/fr.legrain.bdg.ear.ear $LOGIN_DEST@$HOST_DEST:$BDG_FILESYSTEM_PATH/update/
#scp -r /data1/hudson/jobs/solstyce/workspace/svn/fr.legrain.autorisations.ear/target/fr.legrain.autorisations.ear.ear $LOGIN_DEST@$HOST_DEST:$BDG_FILESYSTEM_PATH/update/

#scp -r $WORKSPACE"/svn/fr.legrain.bdg.ear/divers/sql/dump_postgresql/backup_demo.backup" $LOGIN_DEST@$HOST_DEST:$BDG_FILESYSTEM_PATH/update/
#scp -r $WORKSPACE"/svn/fr.legrain.moncompte.ear/divers/sql/dump_postgresql/backup_moncompte.backup" $LOGIN_DEST@$HOST_DEST:$BDG_FILESYSTEM_PATH/update/
#scp -r $WORKSPACE"/svn/fr.legrain.autorisations.ear/divers/sql/dump_postgresql/backup_autorisations.backup" $LOGIN_DEST@$HOST_DEST:$BDG_FILESYSTEM_PATH/update/
#scp -r $WORKSPACE"/svn/fr.legrain.bdg.ear/divers/sql_login_db/dump_postgresql/backup_login_db.backup" $LOGIN_DEST@$HOST_DEST:$BDG_FILESYSTEM_PATH/update/

#scp -r $WORKSPACE"/svn/fr.legrain.bdg.ear/install/update_db/*" $LOGIN_DEST@$HOST_DEST:$BDG_FILESYSTEM_PATH/update_db/

#scp -r $WORKSPACE"/svn/fr.legrain.bdg.ear/divers/update_jboss_demo_internet.sh" $LOGIN_DEST@$HOST_DEST:$BDG_FILESYSTEM_PATH/update/

echo "Fin envoi des fichier pour la mise à jour de l'application sur internet ====== $(date)"

echo "Mise à jour de l'application et de la bdd en arrière plan via SSH"

#ssh $LOGIN_DEST@$HOST_DEST "/srv/lgrg001/tmp_update/update_jboss_demo_internet.sh > /dev/null 2>&1 &"

 else 

echo "Copie sur la machine locale (machine de test/dev avec Hudson) avec cp"

rm $BDG_FILESYSTEM_PATH/bdg/update/*
cp -r $WORKSPACE"/svn/fr.legrain.bdg.ear/install" /tmp/
cd /tmp/install
./install.sh $BDG_FILESYSTEM_PATH
cd $WORKSPACE
cp -r $WORKSPACE"/svn/fr.legrain.bdg.ear/target/fr.legrain.bdg.ear.ear" $BDG_FILESYSTEM_PATH/bdg/update/
cp -r $WORKSPACE"/svn/fr.legrain.autorisations.ear/target/fr.legrain.autorisations.ear.ear" $BDG_FILESYSTEM_PATH/bdg/update/
cp -r $WORKSPACE"/svn/fr.legrain.moncompte.ear/target/fr.legrain.moncompte.ear.ear" $BDG_FILESYSTEM_PATH/bdg/update/

cp -r $WORKSPACE"/svn/fr.legrain.bdg.ear/divers/sql/dump_postgresql/backup_bdg.backup" $BDG_FILESYSTEM_PATH/bdg/update/
cp -r $WORKSPACE"/svn/fr.legrain.bdg.ear/divers/sql_programme/dump_postgresql/backup_bdg_prog.backup" $WORKSPACE/make_install/
cp -r $WORKSPACE"/svn/fr.legrain.moncompte.ear/divers/sql/dump_postgresql/backup_moncompte.backup" $BDG_FILESYSTEM_PATH/bdg/update/
cp -r $WORKSPACE"/svn/fr.legrain.autorisations.ear/divers/sql/dump_postgresql/backup_autorisations.backup" $BDG_FILESYSTEM_PATH/bdg/update/
cp -r $WORKSPACE"/svn/fr.legrain.bdg.ear/divers/sql_login_db/dump_postgresql/backup_login_db.backup" $BDG_FILESYSTEM_PATH/bdg/update/

cp -r $WORKSPACE/svn/fr.legrain.bdg.ear/lib/postgresql-9.3-1101.jdbc41.jar $BDG_FILESYSTEM_PATH/bdg/lib/

rm -rf $BDG_FILESYSTEM_PATH/bdg/update_db/*

cp -r $WORKSPACE/svn/fr.legrain.bdg.ear/install/update_db/* $BDG_FILESYSTEM_PATH/bdg/update_db/

cp -r $WORKSPACE/svn/fr.legrain.bdg.ear/divers/update.sh $BDG_FILESYSTEM_PATH/bdg/update/

echo "Fin envoi des fichiers pour la mise à jour de l'application ====== $(date)"

echo "Mise à jour de l'application et de la bdd en arrière plan"

rm -rf "/tmp/install"

#$BDG_FILESYSTEM_PATH/update/update.sh > /dev/null 2>&1 &
$BDG_FILESYSTEM_PATH/bdg/update/update.sh $BDG_FILESYSTEM_PATH $PGUSER $PGPASSWORD $JBOSS_HOME

fi

echo "Pour voir les logs du serveur : tail -f $JBOSS_HOME/standalone/log/server.log"
