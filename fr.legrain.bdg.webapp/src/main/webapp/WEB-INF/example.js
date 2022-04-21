//http://wildfly.org/news/2015/08/10/Javascript-Support-In-Wildfly/
//http://www.mastertheboss.com/jboss-web/jbosswebserver/undertowjs-tutorial

//décommenter la ligne avec le nom du fichier dans "undertow-scripts.conf" pour activer le js coté serveur, une ligne par fichier
//pour le déploiement à chaud, décommenter la ligne dans "undertow-external-mounts.conf" qui indique l'emplacement des scripts sur le disque, si pas de hotdeploy, les scripts doivent être dans la webapp

//https://dev.demo.promethee.biz:8443/hello
$undertow
    .onGet("/hello",
        {headers: {"content-type": "text/plain"}},
        [function ($exchange) {
            return "Hello World...";
        }])

//https://dev.demo.promethee.biz:8443/rest/endpoint
$undertow
    .onGet("/rest/endpoint",
        {headers: {"content-type": "application/json"}},
        [function ($exchange) {
            return {message: 'Hello World !'};
        }])

//https://dev.demo.promethee.biz:8443/rest/tiers
$undertow
    .alias('db', 'jndi:java:/BDGPostgresDS')
    .onGet("/rest/tiers",
        {headers: {"content-type": "application/json"}},
        ['db', function ($exchange, db) {
            return db.select("select nom_tiers from demo.ta_tiers");
        }])

//https://dev.demo.promethee.biz:8443/rest/tiers_cdi
$undertow
    .alias('bean', 'cdi:testJavaScriptServeur')
	//.alias('bean', 'cdi:articleController') //ne fonctionne pas ManagedBean!=Named
    .onGet("/rest/tiers_cdi",
        {headers: {"content-type": "application/json"}},
        ['bean', function ($exchange, db) {
            //return db.getValues;
        	return db.listeString;
        }])
