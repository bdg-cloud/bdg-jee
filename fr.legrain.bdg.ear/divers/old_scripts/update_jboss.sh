#!/bin/bash

#cron, redémarrage des serveurs jboss et mysql tous les jours à 3h
#0 3 * * * /root/reboot_jboss.sh

#JBOSS_HOME="/data1/jboss-as-7.1.3.Final"
#JBOSS_HOME="/data1/wildfly-8.1.0.Final"
JBOSS_HOME="/data1/wildfly-8.2.0.Final"

echo "Début (re)déploiement de l'application sur jboss ====== $(date)" >> /var/log/log_reboot_jboss.txt

ps -ae | grep java;ps -ae | grep sh

#PID_JAVA=$(pidof java) #attention au tomcat
PID_JBOSS=$(pidof -x standalone.sh)

echo "PID_JAVA = $PID_JAVA  ==== PID_JBOSS = $PID_JBOSS"
echo "Arret de JBoss"
$JBOSS_HOME/bin/jboss-cli.sh -c :shutdown
#kill -9 $(cat /data1/jboss-as-7.1.3.Final/pid)

kill -9 $PID_JAVA $PID_JBOSS

ps -ae | grep java;ps -ae | grep sh

echo "Restauration d'une BDD de test"
#sudo -i -u postgres 

PGUSER=###_LOGIN_PG_BDG_###
PGPASSWORD=###_PASSWORD_PG_BDG_###
export PGPASSWORD PGUSER

echo "..Dump de la base de données"
#pg_dump -i -h localhost -p 5432 -F p -b -v -f "demo.sql" demo
#pg_dump -i -h localhost -p 5432 -F p -b -v -f "demo.sql" -n "nom_schema" demo
pg_dump -i -h localhost -p 5432 -U bdg -F c -b -v -f "/data1/backup_solstyce/solstyce_"$(date +"%m-%d-%Y_%H-%M-%S")"_demo.backup" demo
pg_dump -i -h localhost -p 5432 -U bdg -F c -b -v -f "/data1/backup_solstyce/solstyce_"$(date +"%m-%d-%Y_%H-%M-%S")"_moncompte.backup" moncompte
pg_dump -i -h localhost -p 5432 -U bdg -F c -b -v -f "/data1/backup_solstyce/solstyce_"$(date +"%m-%d-%Y_%H-%M-%S")"_autorisations.backup" autorisations
pg_dump -i -h localhost -p 5432 -U bdg -F c -b -v -f "/data1/backup_solstyce/solstyce_"$(date +"%m-%d-%Y_%H-%M-%S")"_login_db.backup" login_db

echo "..Suppression de la base de données"
dropdb demo
dropdb moncompte
dropdb autorisations
dropdb login_db

echo "..Création de la base de données"
createdb -O bdg -E UTF8 demo
createdb -O bdg -E UTF8 moncompte
createdb -O bdg -E UTF8 autorisations
createdb -O bdg -E UTF8 login_db

echo "..Restauration de la base de données"
#psql demo < demo.sql
pg_restore -i -h localhost -p 5432 -U bdg -d demo -v $WORKSPACE"/svn/fr.legrain.bdg.ear/divers/sql/dump_postgresql/backup_demo.backup"
pg_restore -i -h localhost -p 5432 -U bdg -d moncompte -v $WORKSPACE"/svn/fr.legrain.moncompte.ear/divers/sql/dump_postgresql/backup_moncompte.backup"
pg_restore -i -h localhost -p 5432 -U bdg -d autorisations -v $WORKSPACE"/svn/fr.legrain.autorisations.ear/divers/sql/dump_postgresql/backup_autorisations.backup"
pg_restore -i -h localhost -p 5432 -U bdg -d login_db -v $WORKSPACE"/svn/fr.legrain.bdg.ear/divers/sql_login_db/dump_postgresql/backup_login_db.backup"

echo "Mise à jour de l'EAR"
mv $JBOSS_HOME/standalone/deployments/fr.legrain.bdg.ear.ear /data1/backup_solstyce/fr.legrain.bdg.ear_$(date +"%m-%d-%Y_%H-%M-%S").ear
mv $JBOSS_HOME/standalone/deployments/fr.legrain.autorisations.ear.ear /data1/backup_solstyce/fr.legrain.autorisations.ear_$(date +"%m-%d-%Y_%H-%M-%S").ear
rm -rf $JBOSS_HOME/standalone/deployments/*
#cp $WORKSPACE/svn/fr.legrain.bdg.ear/target/fr.legrain.bdg.ear-0.0.1-SNAPSHOT.ear $JBOSS_HOME/standalone/deployments/
#cp /data1/hudson/jobs/solstyce/lastSuccessful/archive/svn/fr.legrain.bdg.ear/target/fr.legrain.bdg.ear-0.0.1-SNAPSHOT.ear $JBOSS_HOME/standalone/deployments/
cp /data1/hudson/jobs/solstyce/workspace/svn/fr.legrain.bdg.ear/target/fr.legrain.bdg.ear.ear $JBOSS_HOME/standalone/deployments/
cp /data1/hudson/jobs/solstyce/workspace/svn/fr.legrain.autorisations.ear/target/fr.legrain.autorisations.ear.ear $JBOSS_HOME/standalone/deployments/

echo "Redémarrage du SGBD"
#/usr/sbin/service mysql restart
/etc/init.d/postgresql restart

echo "Redémarrage de JBoss"
/usr/sbin/service jboss start

ps -ae | grep java;ps -ae | grep sh

echo "Fin (re)déploiement de l'application sur jboss  ====== $(date)" >> /var/log/log_reboot_jboss.txt

