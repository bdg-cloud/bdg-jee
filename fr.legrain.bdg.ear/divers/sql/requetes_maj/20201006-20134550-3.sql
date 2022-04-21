--set schema 'demo';
INSERT INTO ta_t_service_web_externe ( code_t_service_web_externe, libelle, systeme) 
VALUES ('MENSURA', 'Service import depuis la plateforme Mensura', false);

--set schema 'demo';
INSERT INTO ta_service_web_externe (code_service_web_externe, libelle_service_web_externe,
description_service_web_externe, id_t_authentification_defaut, id_t_service_web_externe, url_editeur,defaut,api_multicompte, actif, systeme ) 
SELECT 'MENSURA', 'Mensura', 'Service import depuis la plateforme Mensura', 5, e.id_t_service_web_externe, 'https://www.geomensura.fr/', false, false, true, true FROM ta_t_service_web_externe e WHERE e.code_t_service_web_externe = 'MENSURA';

--set schema 'demo';
INSERT INTO ta_compte_service_web_externe ( code_compte_service_web_externe, libelle_compte_service_web_externe,
description_compte_service_web_externe, id_t_authentification, id_service_web_externe, actif, defaut, identifiant_de_test, systeme  ) 
SELECT 'Mensura', 'Mensura', 'Compte Mensura', 5, e.id_service_web_externe, true, true , false, false FROM ta_service_web_externe e WHERE e.code_service_web_externe = 'MENSURA';


--set schema 'demo';
INSERT INTO ta_preferences (groupe, cle, type_donnees, libelle, id_categorie_preference,visible, read_only)
VALUES ('importation', 'type_paiement_mensura', 'string',
		'Code du type de paiement par defaut pour les règlements des factures générées depuis Mensura',
	   43, true, false);
	   
