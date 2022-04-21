--set schema 'demo';


CREATE TABLE ta_groupe_preparation
(
  id_groupe_preparation serial NOT NULL,
  code_groupe_preparation dlgr_codel,
  date_groupe_preparation date_lgr,
  id_utilisateur did_facultatif NOT NULL,
  commentaire dlib_commentaire,
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50, 
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_groupe_preparation_pkey PRIMARY KEY (id_groupe_preparation),
  CONSTRAINT ta_groupe_preparation_code_unique UNIQUE(code_groupe_preparation),
  CONSTRAINT utilisateur FOREIGN KEY (id_utilisateur)
      REFERENCES ta_utilisateur (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_groupe_preparation
  OWNER TO bdg;


CREATE INDEX fki_utilisateur
  ON ta_groupe_preparation
  USING btree
  (id_utilisateur);



CREATE TRIGGER tbi_groupe_preparation
  BEFORE INSERT
  ON ta_groupe_preparation
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();



CREATE TRIGGER tbu_groupe_preparation
  BEFORE UPDATE
  ON ta_groupe_preparation
  FOR EACH ROW
  EXECUTE PROCEDURE before_update();




CREATE TABLE ta_infos_preparation
(
  id_infos_document did3 NOT NULL DEFAULT nextval('num_id_infos_document'::regclass),
  id_document did_facultatif,
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
  CONSTRAINT ta_infos_preparation_pkey PRIMARY KEY (id_infos_document),
  CONSTRAINT ta_infos_preparation_id_document_key UNIQUE (id_document)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_infos_preparation
  OWNER TO bdg;


CREATE INDEX ta_infos_preparation_id_document
  ON ta_infos_preparation
  USING btree
  (id_document);


CREATE INDEX ta_infos_preparation_id_preparation
  ON ta_infos_preparation
  USING btree
  (id_document);


CREATE INDEX unq1_ta_infos_preparation
  ON ta_infos_preparation
  USING btree
  (id_document);


CREATE TRIGGER tbi_ta_infos_preparation
  BEFORE INSERT
  ON ta_infos_preparation
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();


CREATE TRIGGER tbu_ta_infos_preparation
  BEFORE UPDATE
  ON ta_infos_preparation
  FOR EACH ROW
  EXECUTE PROCEDURE before_update();



CREATE TABLE ta_preparation
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
  id_infos_document did_facultatif NOT NULL,
  nb_decimales_qte integer,
  nb_decimales_prix integer,
  id_r_etat did_facultatif,
  id_gr_mouv_stock_prev did_facultatif,
  CONSTRAINT ta_preparation_pkey PRIMARY KEY (id_document),
  CONSTRAINT fk_id_infos_preparation FOREIGN KEY (id_infos_document)
      REFERENCES ta_infos_preparation (id_infos_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_mise_a_disposition_id_mise_a_disposition FOREIGN KEY (id_mise_a_disposition)
      REFERENCES ta_mise_a_disposition (id_mise_a_disposition) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_preparation_id_etat_fkey FOREIGN KEY (id_etat)
      REFERENCES ta_etat (id_etat) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_preparation_id_r_etat_fkey FOREIGN KEY (id_r_etat)
      REFERENCES ta_r_etat (id_r_etat) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_preparation_id_t_paiement_fkey FOREIGN KEY (id_t_paiement)
      REFERENCES ta_t_paiement (id_t_paiement) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_preparation_id_tiers_fkey FOREIGN KEY (id_tiers)
      REFERENCES ta_tiers (id_tiers) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_preparation_code_document_key UNIQUE (code_document)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_preparation
  OWNER TO bdg;


CREATE INDEX ta_preparation_code
  ON ta_preparation
  USING btree
  (code_document COLLATE pg_catalog."default");


CREATE INDEX ta_preparation_id_etat
  ON ta_preparation
  USING btree
  (id_etat);



CREATE INDEX ta_preparation_id_t_paiement
  ON ta_preparation
  USING btree
  (id_t_paiement);


CREATE INDEX ta_preparation_id_tiers
  ON ta_preparation
  USING btree
  (id_tiers);


CREATE INDEX unq1_ta_preparation
  ON ta_preparation
  USING btree
  (code_document COLLATE pg_catalog."default");


CREATE TRIGGER tbi_preparation
  BEFORE INSERT
  ON ta_preparation
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();


CREATE TRIGGER tbu_preparation
  BEFORE UPDATE
  ON ta_preparation
  FOR EACH ROW
  EXECUTE PROCEDURE before_update_code_doc();

alter table ta_infos_preparation add  CONSTRAINT ta_infos_preparation_id_document_fkey FOREIGN KEY (id_document)
      REFERENCES ta_preparation (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

CREATE TABLE ta_l_preparation
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
  codeBarre dlib100,
  id_mouvement_stock did_facultatif,
  lib_tva_l_document character varying(255),
  date_prevue date_heure_lgr,
  id_r_etat did_facultatif,
  id_mouvement_stock_prev did_facultatif,
  CONSTRAINT ta_l_preparation_pkey PRIMARY KEY (id_l_document),
  CONSTRAINT fk_ta_l_preparation_1 FOREIGN KEY (id_article)
      REFERENCES ta_article (id_article) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_l_preparation_2 FOREIGN KEY (id_document)
      REFERENCES ta_preparation (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_l_preparation_id_mouvement_stock_prev_fkey FOREIGN KEY (id_mouvement_stock_prev)
      REFERENCES ta_etat (id_etat) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_l_preparation_id_r_etat_fkey FOREIGN KEY (id_r_etat)
      REFERENCES ta_r_etat_ligne_doc (id_r_etat_ligne_doc) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_l_preparation_id_t_ligne_fkey FOREIGN KEY (id_t_ligne)
      REFERENCES ta_t_ligne (id_t_ligne) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_l_preparation
  OWNER TO bdg;


CREATE INDEX fk_ta_l_preparation_1
  ON ta_l_preparation
  USING btree
  (id_article);


CREATE INDEX fk_ta_l_preparation_2
  ON ta_l_preparation
  USING btree
  (id_document);


CREATE INDEX ta_l_preparation_3
  ON ta_l_preparation
  USING btree
  (id_t_ligne);


CREATE INDEX ta_l_preparation_id_preparation
  ON ta_l_preparation
  USING btree
  (id_document);


CREATE INDEX ta_l_preparation_id_t_ligne
  ON ta_l_preparation
  USING btree
  (id_t_ligne);



CREATE TRIGGER tbi_l_preparation
  BEFORE INSERT
  ON ta_l_preparation
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();


CREATE TRIGGER tbiid_article_l_preparation
  BEFORE INSERT
  ON ta_l_preparation
  FOR EACH ROW
  EXECUTE PROCEDURE ligne_vide();


CREATE TRIGGER tbu_l_preparation
  BEFORE UPDATE
  ON ta_l_preparation
  FOR EACH ROW
  EXECUTE PROCEDURE before_update();

