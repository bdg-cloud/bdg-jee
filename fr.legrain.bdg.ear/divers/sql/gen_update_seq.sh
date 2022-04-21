#!/bin/bash

PGUSER=###_LOGIN_PG_BDG_###
PGPASSWORD=###_PASSWORD_PG_BDG_###

export PGPASSWORD PGUSER

schema=$(psql -U $PGUSER -h localhost -d bdg -t -c "SELECT schema_name FROM information_schema.schemata where schema_name<>'public' and schema_name<>'information_schema' and schema_name not like 'pg_%'")
echo "Liste des schema"
echo "$schema"
echo "="

fich="modif_sql.sh"

echo "#!/bin/bash" > $fich
echo "PGUSER=###_LOGIN_PG_BDG_###"  >> $fich
echo "PGPASSWORD=###_PASSWORD_PG_BDG_###" >> $fich
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

