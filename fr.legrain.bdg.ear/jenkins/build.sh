#!/bin/bash

#JAVA_HOME=/var/opt/java/jdk1.7.0_51_x64
#JAVA_HOME=/var/opt/java/jdk1.8.0_111_x64
JAVA_HOME=/var/opt/java/jdk1.8.0_311_x64
export JAVA_HOME

#
# Remplacer le source de la librairie "fr.legrain.bdg.lib.osgi" par celui du dépot gerrit
# --- A CHANGER --- si on conserve l'utilisation de gerrit et un depot à part pour les librairies partager,
#      il faudra supprimer complètement ces projets du dépot principal
#
rm -rf $WORKSPACE/svn/fr.legrain.bdg.lib.osgi
cp -rap $WORKSPACE/svn_lib-legrain/fr.legrain.bdg.lib.osgi $WORKSPACE/svn/

#
# Suppression des modules nodejs qui proviendrait du dépot GIT pour être sur que le plugin Maven utilise les dernières versions
# Re installer par : xx@ser:espaceclient-app# npm install
#
rm -rf $WORKSPACE/svn/fr.legrain.espaceclient.webapp.angular/espaceclient-app/node_modules

#mkdir "$WORKSPACE/lib-legrain-gerrit"
#cp -rap $WORKSPACE/svn_lib-legrain/fr.legrain.bdg.lib.osgi $WORKSPACE/lib-legrain-gerrit/

#
# Mise à jour des login/password de connexion à la bdd de BDG
#
#chmod u+x $WORKSPACE/svn_legrain-secrets/fr.legrain.secrets/bdg/replace_login_password_bdd.sh
#$WORKSPACE/svn_legrain-secrets/fr.legrain.secrets/bdg/replace_login_password_bdd.sh
chmod u+x $WORKSPACE/svn/fr.legrain.bdg.ear/divers/replace_login_password_bdd.sh
$WORKSPACE/svn/fr.legrain.bdg.ear/divers/replace_login_password_bdd.sh

#
# Mise à jour du numéro de version dans le "A propos" avant compilation
#
$WORKSPACE/svn/fr.legrain.bdg.ear/divers/update_about_dialog.sh

#
# Mise à jour du numéro de version dans le "A propos" avant compilation dans l'espace client
#
$WORKSPACE/svn/fr.legrain.bdg.ear/divers/update_about_dialog_espace-client.sh

#
# Génération du changelog
#
$WORKSPACE/svn/fr.legrain.bdg.ear/divers/changelog/changelog_bugzilla.rb

#
# Serveur
#
#service jboss stop
cd $WORKSPACE/svn/fr.legrain.autorisations.parent
/var/opt/maven/apache-maven-3.6.3/bin/mvn -U clean install

cd $WORKSPACE/svn/fr.legrain.moncompte.parent
/var/opt/maven/apache-maven-3.6.3/bin/mvn -U -e clean install

cd $WORKSPACE/svn/fr.legrain.bdg.parent
#/var/opt/maven/apache-maven-3.6.3/bin/mvn -X -U clean install
#-Dmaven.test.skip=true
/var/opt/maven/apache-maven-3.6.3/bin/mvn -U clean install
#/var/opt/maven/apache-maven-3.6.3/bin/mvn -o site

#cd $WORKSPACE/svn_compteclient/compteclient
cd $WORKSPACE/svn/fr.legrain.espaceclient
/var/opt/maven/apache-maven-3.6.3/bin/mvn -U clean install

# Déplacer dans "post build task"
#$WORKSPACE/svn/fr.legrain.bdg.ear/divers/update_jboss.sh

$WORKSPACE/svn/fr.legrain.bdg.ear/divers/build_archive_install.sh
cp $WORKSPACE/svn/fr.legrain.espaceclient.webapp.angular/target/fr.legrain.espaceclient.webapp.angular.zip $WORKSPACE/
