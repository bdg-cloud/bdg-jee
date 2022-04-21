#!/bin/bash

#cron, red  marrage des serveurs jboss et mysql tous les jours    3h
#0 3 * * * /root/reboot_jboss.sh

PGUSER=###_LOGIN_PG_BDG_###
PGPASSWORD=###_PASSWORD_PG_BDG_###

export PGPASSWORD PGUSER

DATE=$(date +"%Y-%m-%d_%H-%M-%S")

echo "="
echo "="
echo "WildFly (8.2+) doit   tre install   sur la machine"
echo "PostgreSQL (9.3+) doit   tre install   sur la machine"
echo "="
echo "Date : $DATE"
echo "JBOSS_HOME : $JBOSS_HOME"
echo "BDG_FILESYSTEM_PATH : $BDG_FILESYSTEM_PATH"
echo "PGUSER : $PGUSER"
echo "PGPASSWORD : $PGPASSWORD"
echo "Param 5 : $5 - $SERVEUR_CLIENT"
echo "="
echo "="


schema=$(psql -U $PGUSER -h localhost -d bdg -t -c "SELECT schema_name FROM information_schema.schemata where schema_name<>'public' and schema_name<>'information_schema' and schema_name not like 'pg_%'")
echo "Liste des schema"
echo "$schema"
echo "="

fich="modif_sql.sh"

echo "#!/bin/bash" > $fich
echo "PGUSER=$PGUSER"  >> $fich
echo "PGPASSWORD=$PGPASSWORD" >> $fich
echo "export PGPASSWORD PGUSER"  >> $fich

while read schema_courant; 
do 
        schema_courant_trim=$(echo $schema_courant | sed 's/^ *//g' | sed 's/ *$//g')
        NOM_DOSSIER=$schema_courant_trim
        sql="SET SCHEMA '$NOM_DOSSIER'; SELECT $NOM_DOSSIER.f_update_seq();"
        echo "psql -U $PGUSER -h localhost -d bdg -tAc \"$sql\"" >> $fich
        #psql -U $PGUSER -h localhost -d bdg -tAc "$sql"
# PGOPTIONS='--search_path=$NOM_DOSSIER'
# export PGOPTIONS
# psql -U $PGUSER -h localhost -d bdg -f update.sql -tA
done <<< "$schema"

chmod 777 $fich
