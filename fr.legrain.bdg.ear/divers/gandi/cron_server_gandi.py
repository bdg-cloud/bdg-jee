#cron_server_gandi 

#BDG test_prod : à éteindre le 10 du mois et à rallumer le 20.
#pendant sa période ou il est allumé, entre le 20 et le 10,
#on l'éteint et le rallume toute les nuits (20h - 8h)
#0 20 10 * * python /home/yann/Bureau/cron_server_gandi.py stop 358908
#0 8 20 * * python /home/yann/Bureau/cron_server_gandi.py start 358908
#0 20 * * * python /home/yann/Bureau/cron_server_gandi.py stop 358908
#0 8 * * * python /home/yann/Bureau/cron_server_gandi.py start 358908

#BDG PPROD : à éteindre toutes les nuits et rallumer tous les matins (20h -8h)
#0 8 * * * python /home/yann/Bureau/cron_server_gandi.py start 241568
#0 20 * * * python /home/yann/Bureau/cron_server_gandi.py stop 241568

#BDG Dev : on l'éteint tous les soirs à 20
#0 20 * * * python /home/yann/Bureau/cron_server_gandi.py stop 333686


#import urllib2
#import requests
import sys
import datetime
import calendar
from os import system

#variable pour activer/désactiver les CRON : 
activation_cron = True

dossier = "demo"
XLgr = "XXXX"
Bdglogin = "demo"
BdgPassword = "demo"
cle_securite_gandi = "XXXXXXXXXXXXXXX"

#id Gandi IAAS BDG :
id_pprod = "241568";
id_testprod = "358908";
id_dev = "333686";

id_serveur = sys.argv[2];

baseurl = "https://dev.demo.promethee.biz:8443/v1/gandi/"

def startserver(id_server):
	system('curl -k  -H "X-Dossier:'+dossier+'"  -H "X-Lgr:'+XLgr+'" -H "X-Bdg-login:'+Bdglogin+'"  -H "X-Bdg-password:'+BdgPassword+'" -H "cle_securite_gandi:'+cle_securite_gandi+'"  -X POST '+baseurl+'startserver/'+id_server)

def stopserver(id_server):
	system('curl -k  -H "X-Dossier:'+dossier+'"  -H "X-Lgr:'+XLgr+'" -H "X-Bdg-login:'+Bdglogin+'"  -H "X-Bdg-password:'+BdgPassword+'" -H "cle_securite_gandi:'+cle_securite_gandi+'"  -X POST '+baseurl+'stopserver/'+id_server)



if sys.argv[1] == "start":
	#si c'est testprod
	if id_serveur == "358908":
		#on regarde qu'on soit bien entre le 10 et le 20 du mois
		d = datetime.datetime.today()
		print('Current date and time: ', d)
		#si c'est le cas, le serveur reste eteint
		if 10 <= d.day <= 20:
		    print "Le serveur reste éteint"
		else:
		    print "On peut démarré le serveur"
		    startserver(id_serveur)
	else:
		startserver(id_serveur)

elif sys.argv[1] == "stop":
	stopserver(id_serveur)
	
#cron dev
#startserver(id_dev)
#stopserver(id_dev)

d = datetime.datetime.today()
print('Today: ', d)
if 10 <= d.day <= 20:
    print "oui !"
else:
    print "No!"



	

#headers = {
#'Dossier': dossier,
#'X-Lgr': XLgr,
#'Bdg-login': Bdglogin,
#'Bdg-password': BdgPassword,
#'cle_securite_gandi': cle_securite_gandi,
#}
#response = requests.post(baseurl + "stopserver/" +id_server, headers=headers, verify=False) 
# create the request object and set some headers
#req = urllib2.Request(baseurl + "startserver/" +id_server)
#req.add_header('Dossier', dossier)
#req.add_header('X-Lgr', XLgr)
#req.add_header('Bdg-login', Bdglogin)
#req.add_header('Bdg-password', BdgPassword)
#req.add_header('cle_securite_gandi', cle_securite_gandi)
# make the request and print the results
#res = urllib2.urlopen(req)
