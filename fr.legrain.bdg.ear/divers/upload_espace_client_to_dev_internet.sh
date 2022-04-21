#!/bin/bash

source $WORKSPACE/svn_legrain-secrets/fr.legrain.secrets/bdg/bdg-cloud-serveur/deploiement/param_espace_client_dev.sh

echo "Début envoi des fichier pour la mise à jour de l'espace client sur internet ====== $(date) === $DOMAINE"

ssh $LOGIN_DEST@$HOST_DEST rm $TMP_DIR/$INSTALL_FILE
ssh $LOGIN_DEST@$HOST_DEST rm $TMP_DIR/espaceclient-app


scp -r "$WORKSPACE/$INSTALL_FILE" $LOGIN_DEST@$HOST_DEST:$TMP_DIR/

echo "Fin envoi des fichier pour la mise à jour de l'espace client sur internet ====== $(date) === $DOMAINE"


ssh $LOGIN_DEST@$HOST_DEST "cd $TMP_DIR;rm -rf /tmp/espaceclient-app;unzip $INSTALL_FILE;cd $TMP_DIR/espaceclient-app"
ssh $LOGIN_DEST@$HOST_DEST "rm -rf /var/www/html/espace-client.'$DOMAINE'_old; mv /var/www/html/espace-client.'$DOMAINE' /var/www/html/espace-client.'$DOMAINE'_old"
ssh $LOGIN_DEST@$HOST_DEST "mkdir /var/www/html/espace-client.'$DOMAINE'"
ssh $LOGIN_DEST@$HOST_DEST "cp -r $TMP_DIR/espaceclient-app/* /var/www/html/espace-client.'$DOMAINE'"



echo "Mise à jour de l'application et de la bdd en arrière plan via SSH"


