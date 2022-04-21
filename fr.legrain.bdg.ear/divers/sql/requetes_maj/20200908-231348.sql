--set schema 'demo';
INSERT INTO ta_t_service_web_externe (id_t_service_web_externe, code_t_service_web_externe, libelle, systeme) 
VALUES (8, 'SHIPPINGBO', 'Service import depuis la plateforme ShippingBo', false);

--set schema 'demo';
INSERT INTO ta_service_web_externe (id_service_web_externe, code_service_web_externe, libelle_service_web_externe,
description_service_web_externe, id_t_authentification_defaut, id_t_service_web_externe, url_editeur,defaut,api_multicompte, actif, systeme ) 
VALUES (13, 'SHIPPINGBO', 'ShippingBo', 'Service import depuis la plateforme ShippingBo', 5, 8, 'https://shippingbo.com/', false, false, true, true);

--set schema 'demo';
INSERT INTO ta_compte_service_web_externe ( code_compte_service_web_externe, libelle_compte_service_web_externe,
description_compte_service_web_externe, id_t_authentification, id_service_web_externe, actif, defaut, identifiant_de_test, systeme  ) 
VALUES ('Shippingbo', 'Shippingbo', 'Compte ShippingBo', 5, 13, true, true , false, false);


--set schema 'demo';
CREATE TABLE ta_liaison_service_externe
(
  id_liaison_service_externe serial NOT NULL,
  id_entite integer,
  ref_externe character varying(255),
  type_entite character varying(255),
  id_service_web_externe integer,
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  ip_acces dlib50,
  version num_version,
  version_obj did_version_obj,
  UNIQUE(id_entite, type_entite, id_service_web_externe),
  UNIQUE(ref_externe, type_entite, id_service_web_externe),
  CONSTRAINT ta_liaison_service_externe_pkey PRIMARY KEY (id_liaison_service_externe),
  CONSTRAINT ta_liaison_service_externe_id_service_web_externe_fkey FOREIGN KEY (id_service_web_externe)
      REFERENCES ta_service_web_externe (id_service_web_externe) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_liaison_service_externe
  OWNER TO bdg;
  
CREATE TRIGGER tbi_ta_liaison_service_externe
	BEFORE INSERT
	ON ta_liaison_service_externe
	FOR EACH ROW
	EXECUTE PROCEDURE before_insert();


CREATE TRIGGER tbu_ta_liaison_service_externe
  BEFORE UPDATE
  ON ta_liaison_service_externe
  FOR EACH ROW
  EXECUTE PROCEDURE before_update();
  
--set schema 'demo';
CREATE TABLE ta_ligne_service_web_externe
(
  id_ligne_service_web_externe serial NOT NULL,
  libelle character varying(255),
  id_article integer,
  ref_article  character varying(255),
  nom_article  character varying(255),
  qte_article integer,
  unite_article character varying(255),
  id_tiers integer,
  ref_tiers character varying(255),
  nom_tiers character varying(255),
  prenom_tiers character varying(255),
  civilite_tiers character varying(255),
  id_mail_tiers integer,
  mail_tiers character varying(255),
  id_tel_tiers integer,
  tel_tiers character varying(255),
  nom_entreprise_tiers character varying(255),
  ref_entreprise_tiers character varying(255),
  id_adresse_livraison integer,
  adresse1_livraison character varying(255),
  adresse2_livraison character varying(255),
  adresse3_livraison character varying(255),
  code_postal_livraison character varying(255),
  ville_livraison character varying(255),
  pays_livraison character varying(255),
  montant_ht character varying(255),
  montant_ttc character varying(255),
  taux_tva character varying(255),
  rem_tx character varying(255),
  rem_ht character varying(255),
  mt_ht_apres_rem character varying(255),
  id_lot integer,
  ref_lot character varying(255),
  json_initial text,
  id_compte_service_web_externe integer,
  ref_commande_externe character varying(255), 
  id_commande_externe character varying(255),
  date_creation_externe character varying(255),
  date_update_externe character varying(255),
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  ip_acces dlib50,
  version num_version,
  version_obj did_version_obj,
  CONSTRAINT ta_ligne_service_web_externe_pkey PRIMARY KEY (id_ligne_service_web_externe),
  CONSTRAINT ta_ligne_service_web_externe_id_article_fkey FOREIGN KEY (id_article)
      REFERENCES ta_article (id_article) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_ligne_service_web_externe_id_tiers_fkey FOREIGN KEY (id_tiers)
  REFERENCES ta_tiers (id_tiers) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_ligne_service_web_externe_id_mail_tiers_fkey FOREIGN KEY (id_mail_tiers)
  REFERENCES ta_email (id_email) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_ligne_service_web_externe_id_tel_tiers_fkey FOREIGN KEY (id_tel_tiers)
  REFERENCES ta_telephone (id_telephone) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_ligne_service_web_externe_id_adresse_livraison_fkey FOREIGN KEY (id_adresse_livraison)
  REFERENCES ta_adresse (id_adresse) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_ligne_service_web_externe_id_lot_fkey FOREIGN KEY (id_lot)
  REFERENCES ta_lot (id_lot) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_ligne_service_web_externe_id_compte_service_web_externe_fkey FOREIGN KEY (id_compte_service_web_externe)
  REFERENCES ta_compte_service_web_externe (id_compte_service_web_externe) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION  
  
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_ligne_service_web_externe
  OWNER TO bdg;
  
CREATE TRIGGER tbi_ta_ligne_service_web_externe
	BEFORE INSERT
	ON ta_ligne_service_web_externe
	FOR EACH ROW
	EXECUTE PROCEDURE before_insert();


CREATE TRIGGER tbu_ta_ligne_service_web_externe
  BEFORE UPDATE
  ON ta_ligne_service_web_externe
  FOR EACH ROW
  EXECUTE PROCEDURE before_update();
  
--set schema 'demo';
ALTER TABLE ta_ligne_service_web_externe
ADD COLUMN valeur_1 character varying (255),
ADD COLUMN valeur_2 character varying (255),
ADD COLUMN valeur_3 character varying (255),
ADD COLUMN valeur_4 character varying (255),
ADD COLUMN termine boolean;


--set schema 'demo';
--INSERT INTO ta_categorie_preference (libelle_categorie_preference)
--VALUES ('Importation');
--set schema 'demo';
INSERT INTO ta_categorie_preference (id_categorie_preference,libelle_categorie_preference)
VALUES (43, 'Importation');

--set schema 'demo';
INSERT INTO ta_preferences (groupe, cle, type_donnees, libelle, id_categorie_preference,visible, read_only)
VALUES ('importation', 'type_paiement_shippingbo', 'string',
		'Code du type de paiement par defaut pour les règlements des factures générées depuis ShippingBo',
	   43, true, false);