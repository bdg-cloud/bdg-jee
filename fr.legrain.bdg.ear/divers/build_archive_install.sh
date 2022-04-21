#!/bin/bash

echo "Création de l'archive d'installation ..."

INSTALL_FILE=setup_bdg.tar.gz
URL_HUDSON_RCP='http://192.168.1.12:8080/hudson/job/solstyce_rcp/lastSuccessfulBuild/artifact/svn/fr.legrain.bdg.client.product/target/products'

mkdir $WORKSPACE/make_install
mkdir $WORKSPACE/make_install/secrets

cp -r $WORKSPACE/svn/fr.legrain.bdg.ear/target/fr.legrain.bdg.ear.ear $WORKSPACE/make_install/
cp -r $WORKSPACE/svn/fr.legrain.autorisations.ear/target/fr.legrain.autorisations.ear.ear $WORKSPACE/make_install/
cp -r $WORKSPACE/svn/fr.legrain.moncompte.ear/target/fr.legrain.moncompte.ear.ear $WORKSPACE/make_install/
#cp -r $WORKSPACE/svn_compteclient/compteclient-ear/target/compteclient-ear.ear $WORKSPACE/make_install/
cp -r $WORKSPACE/svn/fr.legrain.espaceclient.ear/target/fr.legrain.espaceclient.ear.ear $WORKSPACE/make_install/

cp -r $WORKSPACE"/svn/fr.legrain.bdg.ear/divers/sql/dump_postgresql/backup_bdg.backup" $WORKSPACE/make_install/
cp -r $WORKSPACE"/svn/fr.legrain.bdg.ear/divers/sql_programme/dump_postgresql/backup_bdg_prog.backup" $WORKSPACE/make_install/
cp -r $WORKSPACE"/svn/fr.legrain.moncompte.ear/divers/sql/dump_postgresql/backup_moncompte.backup" $WORKSPACE/make_install/
cp -r $WORKSPACE"/svn/fr.legrain.autorisations.ear/divers/sql/dump_postgresql/backup_autorisations.backup" $WORKSPACE/make_install/
cp -r $WORKSPACE"/svn/fr.legrain.bdg.ear/divers/sql_login_db/dump_postgresql/backup_login_db.backup" $WORKSPACE/make_install/
#cp -r $WORKSPACE"/svn_compteclient/compteclient-ear/divers/sql/dump_postgresql/backup_compteclient.backup" $WORKSPACE/make_install/
cp -r $WORKSPACE"/svn/fr.legrain.espaceclient.ear/divers/sql/dump_postgresql/backup_compteclient.backup" $WORKSPACE/make_install/

cp -r $WORKSPACE"/svn/fr.legrain.bdg.ear/lib/postgresql-9.3-1101.jdbc41.jar" $WORKSPACE/make_install/

#cp -r $WORKSPACE"/svn/fr.legrain.bdg.ear/divers/prepare_update.sh" $WORKSPACE/make_install/
#cp -r $WORKSPACE"/svn/fr.legrain.bdg.ear/divers/update.sh" $WORKSPACE/make_install/
#cp -r $WORKSPACE"/svn/fr.legrain.bdg.ear/divers/deploy.sh" $WORKSPACE/make_install/
cp -r $WORKSPACE"/svn/fr.legrain.bdg.ear/divers/"*.sh $WORKSPACE/make_install/

cp -r $WORKSPACE/svn/fr.legrain.bdg.ear/install $WORKSPACE/make_install/

cp -r $WORKSPACE/svn_legrain-secrets/fr.legrain.secrets/bdg/bdg-cloud-serveur/* $WORKSPACE/make_install/secrets

#Embarquer les clients riches
#wget $URL_HUDSON_RCP/JEE_Tiers_Article_Devis-linux.gtk.x86_64.tar.gz
#wget $URL_HUDSON_RCP/JEE_Tiers_Article_Devis-win32.win32.x86_64.zip
#wget $URL_HUDSON_RCP/JEE_Tiers_Article_Devis-macosx.cocoa.x86_64.tar.gz
cp JEE_Tiers_Article_Devis* $WORKSPACE/make_install/

echo "Version SVN : r$SVN_REVISION\nDate : $(date)\nBuild : $BUILD_TAG" > $WORKSPACE/make_install/version.txt

cd $WORKSPACE
rm -f $INSTALL_FILE
tar -zcvf $INSTALL_FILE make_install

echo "Fin de la création de l'archive d'installation."
