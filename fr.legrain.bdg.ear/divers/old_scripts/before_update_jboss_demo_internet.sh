#!/bin/bash

#cron, redémarrage des serveurs jboss et mysql tous les jours à 3h
#0 3 * * * /root/reboot_jboss.sh

#LOGIN_DEST=admin
LOGIN_DEST=root
HOST_DEST=92.243.1.160
TMP_DIR=/srv/lgrg001/tmp_update

echo "Début envoi des fichier pour la mise à jour de l'application sur internet ====== $(date)"
#echo "Envoie des fichier pour la mise à jour de l'application sur internet ====== $(date)" >> /var/log/log_reboot_jboss.txt

ssh $LOGIN_DEST@$HOST_DEST rm $TMP_DIR/*
scp -r /data1/hudson/jobs/solstyce/workspace/svn/fr.legrain.bdg.ear/target/fr.legrain.bdg.ear.ear $LOGIN_DEST@$HOST_DEST:$TMP_DIR/
scp -r /data1/hudson/jobs/solstyce/workspace/svn/fr.legrain.autorisations.ear/target/fr.legrain.autorisations.ear.ear $LOGIN_DEST@$HOST_DEST:$TMP_DIR/

scp -r $WORKSPACE"/svn/fr.legrain.bdg.ear/divers/sql/dump_postgresql/backup_demo.backup" $LOGIN_DEST@$HOST_DEST:$TMP_DIR/
scp -r $WORKSPACE"/svn/fr.legrain.moncompte.ear/divers/sql/dump_postgresql/backup_moncompte.backup" $LOGIN_DEST@$HOST_DEST:$TMP_DIR/
scp -r $WORKSPACE"/svn/fr.legrain.autorisations.ear/divers/sql/dump_postgresql/backup_autorisations.backup" $LOGIN_DEST@$HOST_DEST:$TMP_DIR/
scp -r $WORKSPACE"/svn/fr.legrain.bdg.ear/divers/sql_login_db/dump_postgresql/backup_login_db.backup" $LOGIN_DEST@$HOST_DEST:$TMP_DIR/

scp -r $WORKSPACE"/svn/fr.legrain.bdg.ear/divers/update_jboss_demo_internet.sh" $LOGIN_DEST@$HOST_DEST:$TMP_DIR/

echo "Fin envoi des fichier pour la mise à jour de l'application sur internet ====== $(date)"

echo "Mise à jour de l'application et de la bdd en arrière plan via SSH"

ssh $LOGIN_DEST@$HOST_DEST "/srv/lgrg001/tmp_update/update_jboss_demo_internet.sh > /dev/null 2>&1 &"

echo "Pour voir les logs du serveur : tail -f /srv/lgrg001/wildfly-8.2.0.Final/standalone/log/server.log"
