#!/bin/bash

source $WORKSPACE/svn_legrain-secrets/fr.legrain.secrets/bdg/bdg-cloud-serveur/deploiement/param_global.sh

echo "**********************************************"
echo "* Modification des sources avant compilation *"
echo "**********************************************"
#Attention, si dans le processus de build svn:update est utilise, seul les fichiers nouveau ou mis a jour dans le svn sont importes du svn.
#Donc dans ce cas, les fichiers modifies ici mais pas dans le svn ne sont pas remplaces.
#Ex: si on recherche et remplace la chaine "HUSDON" par "OK" dans le fichier "test.txt", la premire fois elle sera remplace mais la seconde elle ne sera pas trouve
#car il y aura "OK" dans le fichier
#Solution : ne pas utiliser svn:update si on souhaite modifie les sources avant la compilation
#Solution (a tester) : supprimer les fichiers modifies apres compilation pour voir si svn:update les re-exporte correctement
######echo "r$SVN_REVISION *** $BUILD_ID *** $BUILD_TAG ($BRANCHE)"
#sed -i s/"HUDSON"/"r$SVN_REVISION *** $BUILD_ID *** $BUILD_TAG ($BRANCHE)"/g $BASE_SCM/GestionCommerciale/plugin.properties
#On utilise le # comme delimiteur a cause du slash présent dans la variable BRANCHE
#On pourrait utilise un autre caractere : /,#,@,A
#cf : http://www.coderetard.com/2008/11/11/sed-how-to-escape-forward-slash-with-the-right-delimiter/

#sed -i s#"HUDSON"#"r$SVN_REVISION *** $BUILD_ID *** $BUILD_TAG ($BRANCHE)"#g $BASE_SCM/GestionCommerciale/plugin.properties
######sed -i s#"HUDSON"#"r$SVN_REVISION *** $BUILD_ID *** $BUILD_TAG ($BRANCHE)"#g $WORKSPACE/svn/fr.legrain.espaceclient.webapp.angular/espaceclient-app/src/app/a-propos/a-propos.component.html

######sed -i s#"HUDSON"#"r$SVN_REVISION *** $BUILD_ID *** $BUILD_TAG ($BRANCHE)"#g $WORKSPACE/svn/fr.legrain.espaceclient.webapp.angular/espaceclient-app/src/app/a-propos/a-propos.component.html
######sed -i s#"REVISION_SVN"#"$SVN_REVISION"#g $WORKSPACE/svn/fr.legrain.espaceclient.webapp.angular/espaceclient-app/src/app/a-propos/a-propos.component.html
######sed -i s#"BUILD_DATE_MILLIS"#"$(date +%s)"#g $WORKSPACE/svn/fr.legrain.espaceclient.webapp.angular/espaceclient-app/src/app/a-propos/a-propos.component.html


liste_fichier=(
"fr.legrain.bdg.ejb/src/main/java/fr/legrain/data/AbstractApplicationDAOServer.java"
"fr.legrain.bdg.ejb/src/main/java/fr/legrain/general/service/DatabaseService.java"
"fr.legrain.bdg.ejb/src/main/resources/fr/legrain/general/service/ajout_dossier.sh"
"fr.legrain.bdg.ejb/src/main/resources/fr/legrain/general/service/supprimer_dossier.sh"
"fr.legrain.bdg.ejb/src/main/resources/META-INF/persistence.xml"
"fr.legrain.bdg.lib.server.osgi/old/data/AbstractApplicationDAOServer.java"
"fr.legrain.bdg.lib.osgi/src/fr/legrain/bdg/lib/osgi/Const.java"
"fr.legrain.bdg.lib.client.rcp.osgi/src/fr/legrain/lib/data/FireBirdManager.java"
"fr.legrain.moncompte.ejb/src/main/java/fr/legrain/moncompte/data/AbstractApplicationDAOServer.java"
"fr.legrain.espaceclient.ear/src/main/application/META-INF/compteclient-ds.xml"
"fr.legrain.bdg.ear/cluster_et_container/domaine/wildfly-17.0.1.Final.lgr/standalone/configuration/standalone_xml_history/standalone.initial.xml"
"fr.legrain.bdg.ear/configuration/bdg.properties"
"fr.legrain.bdg.ear/divers/old_scripts/update_jboss_demo_internet.sh"
"fr.legrain.bdg.ear/divers/old_scripts/update_jboss.sh"
"fr.legrain.bdg.ear/divers/upload_to_test_etat_internet.sh"
"fr.legrain.bdg.ear/divers/memo/installation_sur_ubuntu.txt"
"fr.legrain.bdg.ear/divers/maj_db_all_dossier.sh"
"fr.legrain.bdg.ear/divers/sql/gen_update_seq.sh"
"fr.legrain.bdg.ear/divers/memo/standalone.xml"
"fr.legrain.bdg.ear/divers/prepare_update.sh"
"ArchivageEpicea/src/fr/legrain/archivageepicea/importation_DB/connectionDB.java"
"ArchivageEpicea/src/fr/legrain/archivageepicea/importation_DB/makeDB.java"
"GestionCommerciale/src/fr/legrain/gestionCommerciale/UtilWorkspace.java"
"gestComBd/src/fr/legrain/gestCom/Appli/Const.java"
"ArchivageEpiceaTestReport/src/archivageepiceatestreport/actions/DECreateDynamicTable.java"
"ArchivageEpiceaTestReport/src/archivageepiceatestreport/actions/SampleAction.java"
"ImportinfosEgroupeWare/src/fr/legrain/importinfosegroupeware/constant/ConstantPlugin.java"
"LiasseFiscale/src/fr/legrain/liasseFiscale/db/ConstLiasse.java"
)

function replaceLoginPasswordFirebase {
   arr=( "$@" )
   for i in "${arr[@]}";
      do
          sed -i s/"###_LOGIN_FB_BDG_###"/"$FBUSER"/g $WORKSPACE/svn/"$i"
          sed -i s/"###_PASSWORD_FB_BDG_###"/"$FBPASSWORD"/g $WORKSPACE/svn/"$i"
      done
}

function replaceLoginPasswordPostgreSQL {
   arr=( "$@" )
   for i in "${arr[@]}";
      do
          sed -i s/"###_LOGIN_PG_BDG_###"/"$PGUSER"/g $WORKSPACE/svn/"$i"
          sed -i s/"###_PASSWORD_PG_BDG_###"/"$PGPASSWORD"/g $WORKSPACE/svn/"$i"
      done
}

replaceLoginPasswordFirebase "${liste_fichier[@]}"
replaceLoginPasswordPostgreSQL "${liste_fichier[@]}"



