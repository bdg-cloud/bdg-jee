--set schema 'demo';


ALTER TABLE ta_stripe_subscription ADD COLUMN id_abonnement did_facultatif;
ALTER TABLE ta_stripe_subscription_item ADD COLUMN id_l_abonnement did_facultatif;

update ta_gen_code_ex set cle_gen_code = 'TaAbonnement' where cle_gen_code = 'TaStripeSubscription';

ALTER TABLE ta_abonnement RENAME TO ta_abonnement_old;
--DROP TABLE ta_abonnement;

CREATE TABLE ta_abonnement
(
  id_document did3 NOT NULL DEFAULT nextval('num_id_document'::regclass),
  id_ta_stripe_subscription did_facultatif,
  code_document character varying(20),
  date_document date_lgr,
  date_ech_document date_lgr,
  date_liv_document date_lgr,
  libelle_document dlib255nn,
  id_tiers did_facultatif NOT NULL,
  id_t_paiement did_facultatif,
  id_etat did_facultatif,
  regle_document did9 DEFAULT 0,
  rem_ht_document did9 DEFAULT 0,
  tx_rem_ht_document did9 DEFAULT 0,
  rem_ttc_document did9 DEFAULT 0,
  tx_rem_ttc_document did9 DEFAULT 0,
  nb_e_document did_facultatif DEFAULT 0,
  ttc smallint DEFAULT 0,
  export smallint DEFAULT 0,
  commentaire dlib_commentaire,
  mt_ttc_calc did9facult,
  mt_ht_calc did9facult,
  mt_tva_calc did9facult,
  net_ttc_calc did9facult,
  net_ht_calc did9facult,
  net_tva_calc did9facult,
  net_a_payer did9facult,
  mt_ttc_avt_rem_globale_calc did9facult,
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  date_export timestamp with time zone,
  id_mise_a_disposition did_facultatif,
  date_verrouillage timestamp with time zone,
  CONSTRAINT ta_abonnement_pkey_2 PRIMARY KEY (id_document),
  CONSTRAINT fk_ta_mise_a_disposition_id_mise_a_disposition FOREIGN KEY (id_mise_a_disposition)
      REFERENCES ta_mise_a_disposition (id_mise_a_disposition) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_abonnement_id_etat_fkey FOREIGN KEY (id_etat)
      REFERENCES ta_etat (id_etat) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_abonnement_id_t_paiement_fkey FOREIGN KEY (id_t_paiement)
      REFERENCES ta_t_paiement (id_t_paiement) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_abonnement_id_tiers_fkey FOREIGN KEY (id_tiers)
      REFERENCES ta_tiers (id_tiers) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_abonnement_code_unique UNIQUE (code_document)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_abonnement
  OWNER TO bdg;

-- Index: "rdb$foreign146"

-- DROP INDEX "rdb$foreign146";

CREATE INDEX "rdb$foreign146_2"
  ON ta_abonnement
  USING btree
  (id_tiers);

-- Index: "rdb$foreign147"

-- DROP INDEX "rdb$foreign147";

CREATE INDEX "rdb$foreign147_2"
  ON ta_abonnement
  USING btree
  (id_t_paiement);

-- Index: "rdb$foreign192"

-- DROP INDEX "rdb$foreign192";

CREATE INDEX "rdb$foreign192_2"
  ON ta_abonnement
  USING btree
  (id_etat);

-- Index: ta_abonnement_code

-- DROP INDEX ta_abonnement_code;

CREATE INDEX ta_abonnement_code
  ON ta_abonnement
  USING btree
  (code_document COLLATE pg_catalog."default");

-- Index: unq1_ta_abonnement

-- DROP INDEX unq1_ta_abonnement;

CREATE INDEX unq1_ta_abonnement
  ON ta_abonnement
  USING btree
  (code_document COLLATE pg_catalog."default");

-- Trigger: tbi_abonnement on ta_abonnement

-- DROP TRIGGER tbi_abonnement ON ta_abonnement;

CREATE TRIGGER tbi_abonnement
  BEFORE INSERT
  ON ta_abonnement
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

-- Trigger: tbu_abonnement on ta_abonnement

-- DROP TRIGGER tbu_abonnement ON ta_abonnement;

CREATE TRIGGER tbu_abonnement
  BEFORE UPDATE
  ON ta_abonnement
  FOR EACH ROW
  EXECUTE PROCEDURE before_update_code_doc();
  
  CREATE TABLE ta_l_abonnement
(
  id_l_document did3 NOT NULL DEFAULT nextval('num_id_l_document'::regclass),
  id_ta_stripe_subscription_item did_facultatif,
  id_document did_facultatif NOT NULL,
  id_t_ligne did_facultatif NOT NULL,
  id_article did_facultatif,
  num_ligne_l_document integer DEFAULT 0,
  lib_l_document dlib255,
  qte_l_document qte3,
  qte2_l_document qte3,
  u1_l_document character varying(20),
  u2_l_document character varying(20),
  prix_u_l_document did9facult,
  taux_tva_l_document numeric(15,4) DEFAULT 0,
  compte_l_document dlib8,
  code_tva_l_document character varying(20),
  code_t_tva_l_document character varying(1),
  mt_ht_l_document did9facult,
  mt_ttc_l_document did9facult,
  rem_tx_l_document did9facult,
  rem_ht_l_document did9facult,
  mt_ht_apr_rem_globale did9facult,
  mt_ttc_apr_rem_globale did9facult,
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  lib_tva_l_document character varying(255),
  CONSTRAINT ta_l_abonnement_pkey PRIMARY KEY (id_l_document),
  CONSTRAINT fk_ta_l_abonnement_1 FOREIGN KEY (id_article)
      REFERENCES ta_article (id_article) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_l_abonnement_2 FOREIGN KEY (id_document)
      REFERENCES ta_abonnement (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_l_abonnement_id_t_ligne_fkey FOREIGN KEY (id_t_ligne)
      REFERENCES ta_t_ligne (id_t_ligne) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_l_abonnement
  OWNER TO bdg;

-- Index: fk_ta_l_abonnement_1

-- DROP INDEX fk_ta_l_abonnement_1;

CREATE INDEX fk_ta_l_abonnement_1
  ON ta_l_abonnement
  USING btree
  (id_article);

-- Index: fk_ta_l_abonnement_2

-- DROP INDEX fk_ta_l_abonnement_2;

CREATE INDEX fk_ta_l_abonnement_2
  ON ta_l_abonnement
  USING btree
  (id_document);

-- Index: "rdb$foreign156"

-- DROP INDEX "rdb$foreign156";

CREATE INDEX "rdb$foreign156_2"
  ON ta_l_abonnement
  USING btree
  (id_t_ligne);

-- Index: ta_l_abonnement_id_abonnement

-- DROP INDEX ta_l_abonnement_id_abonnement;

CREATE INDEX ta_l_abonnement_id_abonnement
  ON ta_l_abonnement
  USING btree
  (id_document);

-- Index: ta_l_abonnement_id_t_ligne

-- DROP INDEX ta_l_abonnement_id_t_ligne;

CREATE INDEX ta_l_abonnement_id_t_ligne
  ON ta_l_abonnement
  USING btree
  (id_t_ligne);


-- Trigger: tbi_l_abonnement on ta_l_abonnement

-- DROP TRIGGER tbi_l_abonnement ON ta_l_abonnement;

CREATE TRIGGER tbi_l_abonnement
  BEFORE INSERT
  ON ta_l_abonnement
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

-- Trigger: tbiid_article_l_abonnement on ta_l_abonnement

-- DROP TRIGGER tbiid_article_l_abonnement ON ta_l_abonnement;

CREATE TRIGGER tbiid_article_l_abonnement
  BEFORE INSERT
  ON ta_l_abonnement
  FOR EACH ROW
  EXECUTE PROCEDURE ligne_vide();

-- Trigger: tbu_l_abonnement on ta_l_abonnement

-- DROP TRIGGER tbu_l_abonnement ON ta_l_abonnement;

CREATE TRIGGER tbu_l_abonnement
  BEFORE UPDATE
  ON ta_l_abonnement
  FOR EACH ROW
  EXECUTE PROCEDURE before_update();

-- Trigger: tbuid_article_l_abonnement on ta_l_abonnement

-- DROP TRIGGER tbuid_article_l_abonnement ON ta_l_abonnement;

CREATE TRIGGER tbuid_article_l_abonnement
  BEFORE UPDATE
  ON ta_l_abonnement
  FOR EACH ROW
  EXECUTE PROCEDURE ligne_vide();
  
  CREATE TABLE ta_infos_abonnement
(
  id_infos_document did3 NOT NULL DEFAULT nextval('num_id_infos_document'::regclass),
  id_document did_facultatif NOT NULL,
  adresse1 dlib100,
  adresse2 dlib100,
  adresse3 dlib100,
  codepostal dcodpos,
  ville dlib100,
  pays dlib100,
  adresse1_liv dlib100,
  adresse2_liv dlib100,
  adresse3_liv dlib100,
  codepostal_liv dcodpos,
  ville_liv dlib100,
  pays_liv dlib100,
  code_compta dlib8nn,
  compte dlib8nn,
  nom_tiers dlib100,
  prenom_tiers dlib100,
  surnom_tiers dlib20,
  code_t_civilite dlgr_codel,
  code_t_entite dlgr_codel,
  tva_i_com_compl dlib50,
  code_c_paiement dlgr_codel,
  lib_c_paiement dlib255,
  report_c_paiement integer DEFAULT 0,
  fin_mois_c_paiement integer DEFAULT 0,
  libl_entreprise dlib100,
  nom_entreprise dlib100,
  code_t_tva_doc dlgr_codel,
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_infos_abonnement_pkey PRIMARY KEY (id_infos_document),
  CONSTRAINT ta_infos_abonnement_id_document_fkey FOREIGN KEY (id_document)
      REFERENCES ta_abonnement (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_infos_abonnement
  OWNER TO bdg;

-- Index: "rdb$foreign151"

-- DROP INDEX "rdb$foreign151";

CREATE INDEX "rdb$foreign151_2"
  ON ta_infos_abonnement
  USING btree
  (id_document);

-- Index: ta_infos_abonnement_id_abonnement

-- DROP INDEX ta_infos_abonnement_id_abonnement;

CREATE INDEX ta_infos_abonnement_id_abonnement
  ON ta_infos_abonnement
  USING btree
  (id_document);

-- Index: unq1_ta_infos_abonnement

-- DROP INDEX unq1_ta_infos_abonnement;

CREATE INDEX unq1_ta_infos_abonnement
  ON ta_infos_abonnement
  USING btree
  (id_document);


-- Trigger: tbi_ta_infos_abonnement on ta_infos_abonnement

-- DROP TRIGGER tbi_ta_infos_abonnement ON ta_infos_abonnement;

CREATE TRIGGER tbi_ta_infos_abonnement
  BEFORE INSERT
  ON ta_infos_abonnement
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

-- Trigger: tbu_ta_infos_abonnement on ta_infos_abonnement

-- DROP TRIGGER tbu_ta_infos_abonnement ON ta_infos_abonnement;

CREATE TRIGGER tbu_ta_infos_abonnement
  BEFORE UPDATE
  ON ta_infos_abonnement
  FOR EACH ROW
  EXECUTE PROCEDURE before_update();


  ---------------------------------------------------
CREATE TABLE ta_stripe_t_source
(
  id_stripe_t_source serial NOT NULL,
  code_stripe_t_source dlgr_code,
  libl_stripe_t_source dlib100,
  automatique boolean default false,
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_stripe_t_source_pkey PRIMARY KEY (id_stripe_t_source),
  CONSTRAINT ta_stripe_t_source_code_unique UNIQUE (code_stripe_t_source)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_stripe_t_source
  OWNER TO bdg;


CREATE INDEX ta_stripe_t_source_code
  ON ta_stripe_t_source
  USING btree
  (code_stripe_t_source COLLATE pg_catalog."default");


CREATE INDEX unq1_ta_stripe_t_source
  ON ta_stripe_t_source
  USING btree
  (code_stripe_t_source COLLATE pg_catalog."default");


CREATE TRIGGER tbi_ta_stripe_t_source
  BEFORE INSERT
  ON ta_stripe_t_source
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

CREATE TRIGGER tbu_ta_stripe_t_source
  BEFORE UPDATE
  ON ta_stripe_t_source
  FOR EACH ROW
  EXECUTE PROCEDURE before_update();
  
  ALTER TABLE ta_stripe_source ADD COLUMN id_stripe_t_source did_facultatif;
  
  INSERT INTO ta_stripe_t_source (code_stripe_t_source, libl_stripe_t_source, version_obj, automatique) VALUES ('PREL_SEPA', 'Prélèvement SEPA',  0, true);
INSERT INTO ta_stripe_t_source (code_stripe_t_source, libl_stripe_t_source, version_obj, automatique) VALUES ('CB', 'Carte bancaire', 0, true);
INSERT INTO ta_stripe_t_source (code_stripe_t_source, libl_stripe_t_source, version_obj, automatique) VALUES ('CHEQUE', 'Chèque', 0, false);
INSERT INTO ta_stripe_t_source (code_stripe_t_source, libl_stripe_t_source, version_obj, automatique) VALUES ('VIREMENT', 'Virement', 0, false);

INSERT INTO ta_stripe_source (id_externe, id_stripe_customer, id_carte_bancaire, id_compte_banque, version_obj, id_stripe_t_source) VALUES (null, null, null, null, 0, 3);
INSERT INTO ta_stripe_source (id_externe, id_stripe_customer, id_carte_bancaire, id_compte_banque, version_obj, id_stripe_t_source) VALUES (null, null, null, null, 0, 4);
-------------------------------------------------------------
----------------------------------------------------------

---------------------------------------Creation table article composé YANN----------
--set schema 'demo';
CREATE TABLE ta_article_compose
(
  id_article_compose SERIAL,
  id_article_parent did_facultatif,
  id_article did_facultatif,
  ip_acces dlib50,
  quand_cree date_heure_lgr,
  quand_modif date_heure_lgr,
  qui_cree dlib50,
  qui_modif dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_article_compose_pkey PRIMARY KEY (id_article_compose),
  CONSTRAINT fk_ta_article_compose_pkey_id_article FOREIGN KEY (id_article)
      REFERENCES ta_article (id_article) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_article_compose_id_article_parent FOREIGN KEY (id_article_parent)
      REFERENCES ta_article (id_article) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);
--set schema 'demo';
ALTER TABLE ta_article_compose
  OWNER TO bdg;
---------Rajout des colonnes compose et abonnement à la table article
--set schema 'demo';
ALTER TABLE ta_article ADD COLUMN compose boolean NOT NULL DEFAULT false;
ALTER TABLE ta_article ADD COLUMN abonnement boolean NOT NULL DEFAULT false;
--------Ajout de 2 colonnes quantités a la table ta_article_compose
--set schema 'demo';
ALTER TABLE ta_article_compose 
ADD COLUMN qte qte3;
ALTER TABLE ta_article_compose 
ADD COLUMN qte2 qte3;
--------Ajout de 2 colonnes unités dans ta_article_compose
--set schema 'demo';
ALTER TABLE ta_article_compose 
ADD COLUMN u1 character varying(20);
ALTER TABLE ta_article_compose 
ADD COLUMN u2 character varying(20);

-----------CREATION TABLE ta_comportement_article_compose
--set schema 'demo';
CREATE TABLE ta_comportement_article_compose (
	id_comportement_article_compose SERIAL not null,
	code_comportement character varying(20),
	libl_comportement dlib255nn,
	desc_comportement dlib_commentaire,
	qui_cree dlib50,
	quand_cree date_heure_lgr,
	qui_modif dlib50,
	quand_modif date_heure_lgr,
	ip_acces dlib50,
	version_obj did_version_obj,
	CONSTRAINT ta_comportement_article_compose_pkey PRIMARY KEY (id_comportement_article_compose)
	
);
--------AJOUT CLE ETRANGERE dans ta_article qui référe,ce ta_comportement_article_compose
--set schema 'demo';
ALTER TABLE ta_article 
ADD COLUMN id_comportement_article_compose did_facultatif;
ALTER TABLE ta_article 
ADD CONSTRAINT fk_id_comportement_article_compose 
FOREIGN KEY (id_comportement_article_compose)
REFERENCES ta_comportement_article_compose (id_comportement_article_compose)
MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;

----------INSERT DES COMPORTEMENTS ARTICLE COMPOSE
--set schema 'demo';
INSERT INTO ta_comportement_article_compose(code_comportement, libl_comportement, desc_comportement) VALUES ('comp1', 'comportement1', 'description comportement 1');
INSERT INTO ta_comportement_article_compose(code_comportement, libl_comportement, desc_comportement) VALUES ('comp2', 'comportement2', 'description comportement 2');

----------AJOUT DE COLONNES nb_decimales_qte et nb_decimales_prix SUR TA_ABONNEMENT pour coller au autres documents
--set schema 'demo';
ALTER TABLE ta_abonnement ADD COLUMN nb_decimales_qte integer;
ALTER TABLE ta_abonnement ADD COLUMN nb_decimales_prix integer;


ALTER TABLE ta_param_espace_client ADD COLUMN mise_a_disposition_facture_auto boolean DEFAULT false;
ALTER TABLE ta_param_espace_client ADD COLUMN affiche_publicite boolean DEFAULT false;


------------------------------AJOUT D'UNE INSERTION DE TYPE DE PAIEMENT VIREMENT SI VOUS NE L'AVAIT PAS DEJA-----------------
--set schema 'demo';
INSERT INTO ta_t_paiement (code_t_paiement, lib_t_paiement, version_obj) VALUES ('VIR', 'Virement', 0);

ALTER TABLE ta_param_espace_client ADD COLUMN date_derniere_connexion date_heure_lgr;

--ALTER TABLE ta_comportement_article_compose  ADD COLUMN qui_modif dlib50;


