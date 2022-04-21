#!/bin/bash
LOG=/var/lgr/wildfly-24.0.1.Final/standalone/log/server.log

echo "======================================================"
echo "======================================================"
#awk "/.*resolveCurrentTenantIdentifier.*/ { print $10 }" $LOG

#grep "Tenant (resolveCurrentTenantIdentifier)" $LOG | awk '{print $1,$2,$10}'

#dossierActif
#connection
#api

if [[ -z $1 ]];
then
        echo "Analyse depuis le debut de la journee"
        grep -E "Tenant \(resolveCurrentTenantIdentifier\)|login_db" $LOG | awk 'dossier!=$10 {print $1,$2,$10} {dossier=$10}'
        dossierActif=$(grep "Tenant (resolveCurrentTenantIdentifier)" $LOG | awk 'dossier!=$10 {print $10} {dossier=$10}')
        connection=$(grep "login_db" $LOG | awk 'dossier!=$10 {print $10} {dossier=$10}')
        api=$(grep "valid token" $LOG | awk 'dossier!=$11 {print $11} {dossier=$11}')
else
        echo $1
        now=$(date "+%Y-%m-%d %H:%M")
        passe=$(date -d "-$1 min" "+%Y-%m-%d %H:%M")
        echo $now
        echo $passe
        sed -n "/$passe/,/$now/p" $LOG | grep -E "Tenant \(resolveCurrentTenantIdentifier\)|login_db" | awk 'dossier!=$10 {print $1,$2,$10} {dossier=$10}'
        dossierActif=$(sed -n "/$passe/,/$now/p" $LOG | grep "Tenant (resolveCurrentTenantIdentifier)" | awk 'dossier!=$10 {print $10} {dossier=$10}')
        connection=$(sed -n "/$passe/,/$now/p" $LOG | grep "login_db" | awk 'dossier!=$10 {print $10} {dossier=$10}')
        api=$(sed -n "/$passe/,/$now/p" $LOG | grep "valid token" | awk 'dossier!=$11 {print $11} {dossier=$11}')
fi

echo "==== Charge ========="
uptime
echo "========= Memoire ==============="
free
echo "=================================="
free -m | awk 'NR==2{printf "Memory Usage: %s/%sMB (%.2f%%)\n", $3,$2,$3*100/$2 }'
df -h | awk '$NF=="/"{printf "Disk Usage: %d/%dGB (%s)\n", $3,$2,$5}'
top -bn1 | grep load | awk '{printf "CPU Load: %.2f\n", $(NF-2)}' 

echo "======== Dossiers actifs ===================="
(IFS=$'\n'; sort <<< "${dossierActif[*]}") | uniq -c

echo "=============== Connexions ====================="
(IFS=$'\n'; sort <<< "${connection[*]}") | uniq -c

echo "===== Utilisation API (espace-client WEB/ Appli mobile Dossier / Appli mobile espace-client) en uniquement legrain82 pour l'instant ====="
(IFS=$'\n'; sort <<< "${api[*]}") | uniq -c

echo "======================================================"
echo "======================================================"

