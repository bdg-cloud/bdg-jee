#!/bin/bash

USER_UPLOAD=root
IP_SERVEUR_UPLOAD=

NOM_FICHIER_SETUP=
#faire l'echange de cle si pas de mot de passe
#envoyer le nouveau setup sur le serveur distant SFTP, SSH ou webservice
sftp $USER_UPLOAD@$IP_SERVEUR_UPLOAD <<EOF
put $NOM_FICHIER_SETUP
EOF

LIEN_FICHIER_SETUP=http://www......./$NOM_FICHIER_SETUP
ID_PRODUIT=1
LIBELLE="lib setup"
DESCRIPTION="desc setup"
VERSION_PRODUIT=$SVN_REVISION
ACTIF=true
MAJ=true
DATE_SETUP=$(date +"%m-%d-%Y")
#Creer une nouvelle entree pour ce setup dans mon compte
curl -H "Content-Type: application/json" -X POST \
-d '{"libelle":"$LIBELLE","decription":"$DESCRIPTION","versionProduit":"$VERSION_PRODUIT","actif":"$ACTIF","maj":"$MAJ","dateSetup":"$DATE_SETUP","fichierSetup":"$LIEN_FICHIER_SETUP","taProduit":{"idProduit":$ID_PRODUIT}}' \
 http://dev.admin.moncompte.promethee.biz:8080/moncompte-admin/rest/setup/create
