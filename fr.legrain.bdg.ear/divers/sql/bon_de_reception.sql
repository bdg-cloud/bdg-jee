-- Table: ta_bon_reception

-- DROP TABLE ta_bon_reception;

CREATE TABLE ta_bon_reception
(
  id_document serial NOT NULL,
  code_document character varying(20) NOT NULL,
  date_document date DEFAULT ('now'::text)::date,
  qui_cree character varying(50) DEFAULT ''::character varying,
  date_liv_document date DEFAULT ('now'::text)::date,
  libelle_document character varying(255) DEFAULT ''::character varying,
  id_tiers integer DEFAULT 0,
  id_t_paiement integer DEFAULT 0,
  rem_ht_document numeric(15,2) DEFAULT 0,
  tx_rem_ht_document numeric(15,2) DEFAULT 0,
  rem_ttc_document numeric(15,2) DEFAULT 0,
  tx_rem_ttc_document numeric(15,2) DEFAULT 0,
  nb_e_document integer DEFAULT 0,
  ttc smallint DEFAULT 0,
  commentaire character varying(2000) DEFAULT ''::character varying,
  mt_ttc_calc numeric(15,2) DEFAULT 0,
  mt_ht_calc numeric(15,2) DEFAULT 0,
  mt_tva_calc numeric(15,2) DEFAULT 0,
  net_ttc_calc numeric(15,2) DEFAULT 0,
  net_ht_calc numeric(15,2) DEFAULT 0,
  net_tva_calc numeric(15,2) DEFAULT 0,
  net_a_payer numeric(15,2) DEFAULT 0,
  mt_ttc_avt_rem_globale_calc numeric(15,2) DEFAULT 0,
  quand_cree timestamp with time zone DEFAULT now(),
  qui_modif character varying(50) DEFAULT ''::character varying,
  quand_modif timestamp with time zone DEFAULT now(),
  version character varying(20) DEFAULT ''::character varying,
  ip_acces character varying(50) DEFAULT '0'::character varying,
  version_obj integer DEFAULT 0,
  CONSTRAINT ta_bon_reception_pkey PRIMARY KEY (id_document),
  CONSTRAINT ta_bon_reception_id_t_paiement_fkey FOREIGN KEY (id_t_paiement)
      REFERENCES ta_t_paiement (id_t_paiement) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_bon_reception_id_tiers_fkey FOREIGN KEY (id_tiers)
      REFERENCES ta_tiers (id_tiers) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_bon_reception
  OWNER TO postgres;

-- Index: "rdb$foreign143"

-- DROP INDEX "rdb$foreign143";

CREATE INDEX "rdb$foreign_ta_bon_reception_1"
  ON ta_bon_reception
  USING btree
  (id_tiers);

-- Index: "rdb$foreign144"

-- DROP INDEX "rdb$foreign144";

CREATE INDEX "rdb$foreign_ta_bon_reception_2"
  ON ta_bon_reception
  USING btree
  (id_t_paiement);

-- Index: ta_bon_reception_code

-- DROP INDEX ta_bon_reception_code;

CREATE INDEX ta_bon_reception_code
  ON ta_bon_reception
  USING btree
  (code_document COLLATE pg_catalog."default");

-- Index: unq1_ta_bon_reception

-- DROP INDEX unq1_ta_bon_reception;

CREATE INDEX unq1_ta_bon_reception
  ON ta_bon_reception
  USING btree
  (code_document COLLATE pg_catalog."default");
  
  
  
  -- Function: tbdid_devis_etranger()

-- DROP FUNCTION tbdid_devis_etranger();

CREATE OR REPLACE FUNCTION tbdid_bon_reception_etranger()
  RETURNS trigger AS
$BODY$
begin
  delete from ta_l_bon_reception where id_DOCUMENT = old.id_DOCUMENT;
--  delete from TA_COM_DOC where ID_DOCUMENT = old.ID_DOCUMENT;
  delete from ta_infos_bon_reception where id_DOCUMENT = old.id_DOCUMENT;
 -- delete from TA_R_DOCUMENT where id_DEVIS = old.id_DOCUMENT;
end $BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION tbdid_devis_etranger()
  OWNER TO postgres;


-- Trigger: tbdid_bon_reception_etranger on ta_bon_reception

-- DROP TRIGGER tbdid_bon_reception_etranger ON ta_bon_reception;

CREATE TRIGGER tbdid_bon_reception_etranger
  AFTER DELETE
  ON ta_bon_reception
  FOR EACH ROW
  EXECUTE PROCEDURE tbdid_bon_reception_etranger();

-- Trigger: tbi_bon_reception on ta_bon_reception

-- DROP TRIGGER tbi_bon_reception ON ta_bon_reception;

CREATE TRIGGER tbi_bon_reception
  BEFORE INSERT
  ON ta_bon_reception
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

-- Trigger: tbu_bon_reception on ta_bon_reception

-- DROP TRIGGER tbu_bon_reception ON ta_bon_reception;

CREATE TRIGGER tbu_bon_reception
  BEFORE UPDATE
  ON ta_bon_reception
  FOR EACH ROW
  EXECUTE PROCEDURE before_update_code_doc();

-- Table: ta_l_bon_reception

-- DROP TABLE ta_l_bon_reception;

CREATE TABLE ta_l_bon_reception
(
  id_l_document serial NOT NULL,
  id_document integer DEFAULT 0,
  id_t_ligne integer NOT NULL,
  id_article integer DEFAULT NULL,
  id_lot integer DEFAULT NULL,
  id_entrepot integer DEFAULT NULL,
  num_ligne_l_document integer DEFAULT 0,
  lib_l_document character varying(255) DEFAULT ''::character varying,
  qte_l_document numeric(15,2) DEFAULT 0,
  qte2_l_document numeric(15,2) DEFAULT 0,
  u1_l_document character varying(20) DEFAULT ''::character varying,
  u2_l_document character varying(20) DEFAULT ''::character varying,
  prix_u_l_document numeric(15,2) DEFAULT 0,
  taux_tva_l_document numeric(15,4) DEFAULT 0,
  compte_l_document character varying(8) DEFAULT ''::character varying,
  code_tva_l_document character varying(20) DEFAULT ''::character varying,
  code_t_tva_l_document character varying(1) DEFAULT ''::character varying,
  mt_ht_l_document numeric(15,2) DEFAULT 0,
  mt_ttc_l_document numeric(15,2) DEFAULT 0,
  rem_tx_l_document numeric(15,2) DEFAULT 0,
  rem_ht_l_document numeric(15,2) DEFAULT 0,
  mt_ht_apr_rem_globale numeric(15,2) DEFAULT 0,
  mt_ttc_apr_rem_globale numeric(15,2) DEFAULT 0,
  qui_cree character varying(50) DEFAULT ''::character varying,
  quand_cree timestamp with time zone DEFAULT now(),
  qui_modif character varying(50) DEFAULT ''::character varying,
  quand_modif timestamp with time zone DEFAULT now(),
  version character varying(20) DEFAULT ''::character varying,
  ip_acces character varying(50) DEFAULT '0'::character varying,
  version_obj integer DEFAULT 0,
  CONSTRAINT ta_l_bon_reception_pkey PRIMARY KEY (id_l_document),
  CONSTRAINT fk_ta_l_bon_reception_1 FOREIGN KEY (id_document)
      REFERENCES ta_bon_reception (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_l_bon_reception_2 FOREIGN KEY (id_article)
      REFERENCES ta_article (id_article) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
      
      CONSTRAINT fk_ta_l_bon_reception_3 FOREIGN KEY (id_lot)
      REFERENCES ta_lot (id_lot) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
      
      CONSTRAINT fk_ta_l_bon_reception_4 FOREIGN KEY (id_entrepot)
      REFERENCES ta_entrepot (id_entrepot) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
      
  CONSTRAINT ta_l_bon_reception_id_t_ligne_fkey FOREIGN KEY (id_t_ligne)
      REFERENCES ta_t_ligne (id_t_ligne) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_l_bon_reception
  OWNER TO postgres;

-- Index: fk_ta_l_bon_reception_1

-- DROP INDEX fk_ta_l_bon_reception_1;

CREATE INDEX fk_ta_l_bon_reception_1
  ON ta_l_bon_reception
  USING btree
  (id_document);

-- Index: fk_ta_l_bon_reception_2

-- DROP INDEX fk_ta_l_bon_reception_2;

CREATE INDEX fk_ta_l_bon_reception_2
  ON ta_l_bon_reception
  USING btree
  (id_article);

-- Index: "rdb$foreign155"

-- DROP INDEX "rdb$foreign155";

CREATE INDEX "rdb$foreign_ta_l_bon_reception"
  ON ta_l_bon_reception
  USING btree
  (id_t_ligne);


-- Trigger: tbi_l_bon_reception on ta_l_bon_reception

-- DROP TRIGGER tbi_l_bon_reception ON ta_l_bon_reception;

CREATE TRIGGER tbi_l_bon_reception
  BEFORE INSERT
  ON ta_l_bon_reception
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

-- Trigger: tbiid_article_l_bon_reception on ta_l_bon_reception

-- DROP TRIGGER tbiid_article_l_bon_reception ON ta_l_bon_reception;

CREATE TRIGGER tbiid_article_l_bon_reception
  BEFORE INSERT
  ON ta_l_bon_reception
  FOR EACH ROW
  EXECUTE PROCEDURE ligne_vide();

-- Trigger: tbu_l_bon_reception on ta_l_bon_reception

-- DROP TRIGGER tbu_l_bon_reception ON ta_l_bon_reception;

CREATE TRIGGER tbu_l_bon_reception
  BEFORE UPDATE
  ON ta_l_bon_reception
  FOR EACH ROW
  EXECUTE PROCEDURE before_update();

-- Trigger: tbuid_article_l_bon_reception on ta_l_bon_reception

-- DROP TRIGGER tbuid_article_l_bon_reception ON ta_l_bon_reception;

CREATE TRIGGER tbuid_article_l_bon_reception
  BEFORE UPDATE
  ON ta_l_bon_reception
  FOR EACH ROW
  EXECUTE PROCEDURE ligne_vide();

-- Table: ta_infos_bon_reception

-- DROP TABLE ta_infos_bon_reception;

CREATE TABLE ta_infos_bon_reception
(
  id_infos_document serial NOT NULL,
  id_document integer DEFAULT 0,
  adresse1 character varying(100) DEFAULT ''::character varying,
  adresse2 character varying(100) DEFAULT ''::character varying,
  adresse3 character varying(100) DEFAULT ''::character varying,
  codepostal character varying(25) DEFAULT ''::character varying,
  ville character varying(100) DEFAULT ''::character varying,
  pays character varying(100) DEFAULT ''::character varying,
  adresse1_liv character varying(100) DEFAULT ''::character varying,
  adresse2_liv character varying(100) DEFAULT ''::character varying,
  adresse3_liv character varying(100) DEFAULT ''::character varying,
  codepostal_liv character varying(25) DEFAULT ''::character varying,
  ville_liv character varying(100) DEFAULT ''::character varying,
  pays_liv character varying(100) DEFAULT ''::character varying,
  code_compta character varying(8) DEFAULT ''::character varying,
  compte character varying(8) DEFAULT ''::character varying,
  nom_tiers character varying(100) DEFAULT ''::character varying,
  prenom_tiers character varying(100) DEFAULT ''::character varying,
  surnom_tiers character varying(20) DEFAULT ''::character varying,
  code_t_civilite character varying(20) DEFAULT ''::character varying,
  code_t_entite character varying(20) DEFAULT ''::character varying,
  tva_i_com_compl character varying(50) DEFAULT ''::character varying,
  code_c_paiement character varying(20) DEFAULT ''::character varying,
  lib_c_paiement character varying(255) DEFAULT ''::character varying,
  report_c_paiement integer DEFAULT 0,
  fin_mois_c_paiement integer DEFAULT 0,
  libl_entreprise character varying(100) DEFAULT ''::character varying,
  nom_entreprise character varying(100) DEFAULT ''::character varying,
  code_t_tva_doc character varying(20) DEFAULT ''::character varying,
  qui_cree character varying(50) DEFAULT ''::character varying,
  quand_cree timestamp with time zone DEFAULT now(),
  qui_modif character varying(50) DEFAULT ''::character varying,
  quand_modif timestamp with time zone DEFAULT now(),
  version character varying(20) DEFAULT ''::character varying,
  ip_acces character varying(50) DEFAULT '0'::character varying,
  version_obj integer DEFAULT 0,
  CONSTRAINT ta_infos_bon_reception_pkey PRIMARY KEY (id_infos_document),
  CONSTRAINT fk_ta_infos_bon_reception_1 FOREIGN KEY (id_document)
      REFERENCES ta_bon_reception (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_infos_bon_reception
  OWNER TO postgres;

-- Index: fk_ta_infos_bon_reception_1

-- DROP INDEX fk_ta_infos_bon_reception_1;

CREATE INDEX fk_ta_infos_bon_reception_1
  ON ta_infos_bon_reception
  USING btree
  (id_document);

-- Index: ta_infos_bon_reception_id_bon_reception

-- DROP INDEX ta_infos_bon_reception_id_bon_reception;

CREATE INDEX ta_infos_bon_reception_id_bon_reception
  ON ta_infos_bon_reception
  USING btree
  (id_document);

-- Index: unq1_ta_infos_bon_reception

-- DROP INDEX unq1_ta_infos_bon_reception;

CREATE INDEX unq1_ta_infos_bon_reception
  ON ta_infos_bon_reception
  USING btree
  (id_document);


-- Trigger: tbi_ta_infos_document on ta_infos_bon_reception

-- DROP TRIGGER tbi_ta_infos_document ON ta_infos_bon_reception;

CREATE TRIGGER tbi_ta_infos_document
  BEFORE INSERT
  ON ta_infos_bon_reception
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

-- Trigger: tbu_ta_infos_bon_reception on ta_infos_bon_reception

-- DROP TRIGGER tbu_ta_infos_bon_reception ON ta_infos_bon_reception;

CREATE TRIGGER tbu_ta_infos_bon_reception
  BEFORE UPDATE
  ON ta_infos_bon_reception
  FOR EACH ROW
  EXECUTE PROCEDURE before_update();

