--set schema 'demo';
INSERT INTO ta_t_service_web_externe ( code_t_service_web_externe, libelle, systeme) 
VALUES ( 'WOOCOMMERCE', 'Service import depuis la plateforme WooCommerce', false);

--set schema 'demo';
INSERT INTO ta_service_web_externe ( code_service_web_externe, libelle_service_web_externe,
description_service_web_externe, id_t_authentification_defaut, id_t_service_web_externe, url_editeur,defaut,api_multicompte, actif, systeme ) 
SELECT 'WOOCOMMERCE', 'WooCommerce', 'Service import depuis la plateforme WooCommerce', 4, e.id_t_service_web_externe, 'https://woocommerce.com/', false, false, true, true FROM ta_t_service_web_externe e WHERE e.code_t_service_web_externe = 'WOOCOMMERCE';

--set schema 'demo';
INSERT INTO ta_compte_service_web_externe ( code_compte_service_web_externe, libelle_compte_service_web_externe,
description_compte_service_web_externe, id_t_authentification, id_service_web_externe, actif, defaut, identifiant_de_test, systeme  ) 
SELECT 'Woocommerce', 'WooCommerce', 'Compte WooCommerce', 4, e.id_service_web_externe, true, true , false, false FROM ta_service_web_externe e WHERE e.code_service_web_externe = 'WOOCOMMERCE';


--set schema 'demo';
INSERT INTO ta_preferences (groupe, cle, type_donnees, libelle, id_categorie_preference,visible, read_only)
VALUES ('importation', 'type_paiement_woocommerce', 'string',
		'Code du type de paiement par defaut pour les règlements des factures générées depuis Woocommerce',
	   43, true, false);
	   
--set schema 'demo';
CREATE TABLE ta_synchro_service_externe
(
  id_synchro_service_externe serial NOT NULL,
  derniere_synchro date_heure_lgr,
  type_entite character varying(255),
  id_compte_service_web_externe integer,
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  ip_acces dlib50,
  version num_version,
  version_obj did_version_obj,
  CONSTRAINT ta_synchro_service_externe_pkey PRIMARY KEY (id_synchro_service_externe),
  CONSTRAINT ta_synchro_service_externe_id_compte_service_web_externe_fkey FOREIGN KEY (id_compte_service_web_externe)
      REFERENCES ta_compte_service_web_externe (id_compte_service_web_externe) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_synchro_service_externe
  OWNER TO bdg;
  
CREATE TRIGGER tbi_ta_synchro_service_externe
	BEFORE INSERT
	ON ta_synchro_service_externe
	FOR EACH ROW
	EXECUTE PROCEDURE before_insert();


CREATE TRIGGER tbu_ta_synchro_service_externe
  BEFORE UPDATE
  ON ta_synchro_service_externe
  FOR EACH ROW
  EXECUTE PROCEDURE before_update();
     