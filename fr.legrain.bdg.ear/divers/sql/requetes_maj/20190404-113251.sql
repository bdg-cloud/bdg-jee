
------------------- Debut implémentation des bons d'achat ----------------
-------------------------------------------------------------------------
CREATE TABLE ta_boncde_achat
(
  id_document did3 NOT NULL DEFAULT nextval('num_id_document'::regclass),
  code_document dlgr_code,
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
  ttc dbool,
  export dbool,
  commentaire dlib_commentaire,
  mt_ttc_calc did9facult,
  mt_ht_calc did9facult,
  mt_tva_calc did9facult,
  net_ttc_calc did9facult,
  net_ht_calc did9facult,
  net_tva_calc did9facult,
  net_a_payer did9facult,
  mt_ttc_avt_rem_globale_calc did9facult,
  origine_import dlib100,
  id_import dlib100,
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  id_gr_mouv_stock did_facultatif,
  date_export timestamp with time zone,
  id_mise_a_disposition did_facultatif,
  date_verrouillage timestamp with time zone,
  numero_commande_fournisseur dlib20,
  id_transporteur did_facultatif,
  gestion_lot boolean DEFAULT false,
  CONSTRAINT ta_boncde_achat_pkey PRIMARY KEY (id_document),
  CONSTRAINT fk_ta_mise_a_disposition_id_mise_a_disposition FOREIGN KEY (id_mise_a_disposition)
      REFERENCES ta_mise_a_disposition (id_mise_a_disposition) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_boncde_achat_id_etat_fkey FOREIGN KEY (id_etat)
      REFERENCES ta_etat (id_etat) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_boncde_achat_id_t_paiement_fkey FOREIGN KEY (id_t_paiement)
      REFERENCES ta_t_paiement (id_t_paiement) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_boncde_achat_id_tiers_fkey FOREIGN KEY (id_tiers)
      REFERENCES ta_tiers (id_tiers) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_boncde_achat_code_document_key UNIQUE (code_document)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_boncde_achat
  OWNER TO bdg;



CREATE INDEX ta_boncde_achat_id_tiers
  ON ta_boncde_achat
  USING btree
  (id_tiers);



CREATE INDEX ta_boncde_achat_id_t_paiement
  ON ta_boncde_achat
  USING btree
  (id_t_paiement);



CREATE INDEX ta_boncde_achat_id_etat
  ON ta_boncde_achat
  USING btree
  (id_etat);



CREATE INDEX ta_boncde_achat_code
  ON ta_boncde_achat
  USING btree
  (code_document COLLATE pg_catalog."default");



CREATE INDEX unq1_ta_boncde_achat
  ON ta_boncde_achat
  USING btree
  (code_document COLLATE pg_catalog."default");




CREATE TRIGGER tbdid_boncde_etranger
  AFTER DELETE
  ON ta_boncde_achat
  FOR EACH ROW
  EXECUTE PROCEDURE tbdid_boncde_etranger();



CREATE TRIGGER tbi_boncde
  BEFORE INSERT
  ON ta_boncde_achat
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();



CREATE TRIGGER tbu_boncde
  BEFORE UPDATE
  ON ta_boncde_achat
  FOR EACH ROW
  EXECUTE PROCEDURE before_update_code_doc();


CREATE TABLE ta_infos_boncde_achat
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
  CONSTRAINT ta_infos_boncde_achat_pkey PRIMARY KEY (id_infos_document),
  CONSTRAINT ta_infos_boncde_achat_id_document_fkey FOREIGN KEY (id_document)
      REFERENCES ta_boncde_achat (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_infos_boncde_achat
  OWNER TO bdg;


CREATE INDEX boncde_achat_id_document
  ON ta_infos_boncde_achat
  USING btree
  (id_document);


CREATE INDEX boncde_achat_id_boncde_achat
  ON ta_infos_boncde_achat
  USING btree
  (id_document);



CREATE INDEX unq1_ta_infos_boncde_achat
  ON ta_infos_boncde_achat
  USING btree
  (id_document);



CREATE TRIGGER tbi_ta_infos_boncde_achat
  BEFORE INSERT
  ON ta_infos_boncde_achat
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();


CREATE TRIGGER tbu_ta_infos_boncde_achat
  BEFORE UPDATE
  ON ta_infos_boncde_achat
  FOR EACH ROW
  EXECUTE PROCEDURE before_update();



  
CREATE TABLE ta_l_boncde_achat
(
  id_l_document did3 NOT NULL DEFAULT nextval('num_id_l_document'::regclass),
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
  id_lot did_facultatif,
  id_entrepot did_facultatif,
  emplacement_l_document dlib255,
  id_mouvement_stock did_facultatif,
  lib_tva_l_document character varying(255),
  CONSTRAINT ta_l_boncde_achat_pkey PRIMARY KEY (id_l_document),
  CONSTRAINT fk_ta_l_boncde_achat_1 FOREIGN KEY (id_article)
      REFERENCES ta_article (id_article) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_l_boncde_achat_2 FOREIGN KEY (id_document)
      REFERENCES ta_boncde_achat (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_l_boncde_achat_id_t_ligne_fkey FOREIGN KEY (id_t_ligne)
      REFERENCES ta_t_ligne (id_t_ligne) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_l_boncde_achat
  OWNER TO bdg;



CREATE INDEX fk_ta_l_boncde_achat_1
  ON ta_l_boncde_achat
  USING btree
  (id_article);



CREATE INDEX fk_ta_l_boncde_achat_2
  ON ta_l_boncde_achat
  USING btree
  (id_document);



CREATE INDEX boncde_achat_id_t_ligne
  ON ta_l_boncde_achat
  USING btree
  (id_t_ligne);



CREATE INDEX ta_l_boncde_achat_id_boncde
  ON ta_l_boncde_achat
  USING btree
  (id_document);


CREATE INDEX ta_l_boncde_achat_id_t_ligne
  ON ta_l_boncde_achat
  USING btree
  (id_t_ligne);



CREATE TRIGGER tbi_l_boncde_achat
  BEFORE INSERT
  ON ta_l_boncde_achat
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();



CREATE TRIGGER tbiid_article_l_boncde_achat
  BEFORE INSERT
  ON ta_l_boncde_achat
  FOR EACH ROW
  EXECUTE PROCEDURE ligne_vide();



CREATE TRIGGER tbu_l_boncde_achat
  BEFORE UPDATE
  ON ta_l_boncde_achat
  FOR EACH ROW
  EXECUTE PROCEDURE before_update();


ALTER TABLE ta_r_document
  ADD COLUMN id_boncde_achat did_facultatif;

  
  
INSERT INTO ta_preferences
( groupe, cle, valeur, valeur_defaut, type_donnees, libelle, valeurs_possibles, id_categorie_preference, id_groupe_preference, fichier_vue, "position", position_dans_groupe, est_un_groupe, description, id_utilisateur)
VALUES( 'boncdeAchat', 'PageBreakMaxi', '36', '', 'numeric', 'Page break Maxi',   '', 7, NULL, NULL, NULL, NULL, false, NULL, NULL);
INSERT INTO ta_preferences
( groupe, cle, valeur, valeur_defaut, type_donnees, libelle, valeurs_possibles, id_categorie_preference, id_groupe_preference, fichier_vue, "position", position_dans_groupe, est_un_groupe, description, id_utilisateur)
VALUES( 'boncdeAchat', 'PageBreakTotaux', '23', '', 'numeric', 'Page break Totaux',  '', 7, NULL, NULL, NULL, NULL, false, NULL, NULL);
INSERT INTO ta_preferences
( groupe, cle, valeur, valeur_defaut, type_donnees, libelle, valeurs_possibles, id_categorie_preference, id_groupe_preference, fichier_vue, "position", position_dans_groupe, est_un_groupe, description, id_utilisateur)
VALUES( 'boncdeAchat', 'ParamChoix', 'choix 3', '', 'string', 'listeChoix',   '', 7, NULL, NULL, NULL, NULL, false, NULL, NULL);
INSERT INTO ta_preferences
( groupe, cle, valeur, valeur_defaut, type_donnees, libelle, valeurs_possibles, id_categorie_preference, id_groupe_preference, fichier_vue, "position", position_dans_groupe, est_un_groupe, description, id_utilisateur)
VALUES( 'boncdeAchat', 'ImpressionDirect', 'false', '', 'boolean', 'Impression direct',   '', 7, NULL, NULL, NULL, NULL, false, NULL, NULL);
INSERT INTO ta_preferences
( groupe, cle, valeur, valeur_defaut, type_donnees, libelle, valeurs_possibles, id_categorie_preference, id_groupe_preference, fichier_vue, "position", position_dans_groupe, est_un_groupe, description, id_utilisateur)
VALUES( 'boncdeAchat', 'Traite', 'true', '', 'boolean', 'Traite',   '', 7, NULL, NULL, NULL, NULL, false, NULL, NULL);
INSERT INTO ta_preferences
( groupe, cle, valeur, valeur_defaut, type_donnees, libelle, valeurs_possibles, id_categorie_preference, id_groupe_preference, fichier_vue, "position", position_dans_groupe, est_un_groupe, description, id_utilisateur)
VALUES( 'boncdeAchat', 'Commentaire', 'Commentaire Bcde achat par défaut', '', 'text', 'Commentaire par défaut Bcde achat',   '', 7, NULL, NULL, NULL, NULL, false, NULL, NULL);


INSERT INTO ta_gen_code_ex
( code_gen_code, cle_gen_code, valeur_gen_code )
VALUES( 'codeDocument', 'TaBoncdeAchat', 'BA{@exo}{@num}' );

------------------- fin implémentation des bons d'achat ----------------
-------------------------------------------------------------------------

--set schema 'demo';

ALTER TABLE ta_stripe_paiement_prevu ADD COLUMN id_avis_echeance did_facultatif;
ALTER TABLE ta_stripe_paiement_prevu ADD COLUMN id_prelevement did_facultatif;

ALTER TABLE ta_reglement ADD COLUMN id_avis_echeance did_facultatif;

ALTER TABLE ta_r_document DROP CONSTRAINT fk_ta_r_doc_avis;
ALTER TABLE ta_r_document ADD CONSTRAINT fk_ta_r_doc_avis FOREIGN KEY (id_avis_echeance) REFERENCES ta_avis_echeance (id_document);

ALTER TABLE ta_stripe_charge ADD COLUMN date_charge date_heure_lgr;

ALTER TABLE ta_stripe_subscription_item ADD COLUMN complement_1 dlib255;
ALTER TABLE ta_stripe_subscription_item ADD COLUMN complement_2 dlib255;
ALTER TABLE ta_stripe_subscription_item ADD COLUMN complement_3 dlib255;

ALTER TABLE ta_l_echeance ADD COLUMN id_stripe_subscription_item did_facultatif;

------------------
--ALTER TABLE ta_stripe_paiement_prevu ADD COLUMN annule boolean default false;

------------------
ALTER TABLE ta_stripe_paiement_prevu ADD COLUMN date_annulation date_heure_lgr default NULL;
ALTER TABLE ta_stripe_paiement_prevu ADD COLUMN commentaire_annulation text;

ALTER TABLE ta_stripe_subscription ADD COLUMN date_annulation date_heure_lgr default NULL;
ALTER TABLE ta_stripe_subscription ADD COLUMN commentaire_annulation text;

ALTER TABLE ta_l_echeance ADD COLUMN date_annulation date_heure_lgr default NULL;
ALTER TABLE ta_l_echeance ADD COLUMN commentaire_annulation text;

ALTER TABLE ta_stripe_subscription ADD COLUMN code_document character varying(20);
 INSERT INTO ta_gen_code_ex (code_gen_code, cle_gen_code, valeur_gen_code, version_obj) VALUES ('codeDocument', 'TaStripeSubscription', 'ABO{@num}', 0);
 
ALTER TABLE ta_etat ADD COLUMN systeme boolean default false;
ALTER TABLE ta_etat ADD COLUMN visible boolean default true;
ALTER TABLE ta_etat ADD COLUMN identifiant dlib255;
  
INSERT INTO ta_etat (code_etat, libelle_etat, identifiant, systeme, version_obj) VALUES ('ETAT_REG_VALIDE', 'Règlement validé', 'etat.reglement.valide', true, 0);
INSERT INTO ta_etat (code_etat, libelle_etat, identifiant, systeme, version_obj) VALUES ('ETAT_REG_REJETE', 'Règlement rejeté', 'etat.reglement.rejete', true, 0);
INSERT INTO ta_etat (code_etat, libelle_etat, identifiant, systeme, version_obj) VALUES ('ETAT_REG_ENCOURS', 'Règlement en cours', 'etat.reglement.encours', true, 0);
