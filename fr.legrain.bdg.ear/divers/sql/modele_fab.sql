
SELECT setval('ta_controle_id_controle_seq', COALESCE((SELECT MAX(id_controle)+1 FROM ta_controle), 1), false);

CREATE TABLE IF NOT EXISTS ta_modele_fabrication
(
  id_document did3 NOT NULL DEFAULT nextval('num_id_document'::regclass),
  code_document dlgr_code,
  date_document date_lgr,
  qui_cree dlib50,
  date_liv_document date_lgr,
  libelle_document dlib255nn,
  id_tiers did_facultatif,
  id_t_paiement did_facultatif,
  rem_ht_document did9 DEFAULT 0,
  tx_rem_ht_document did9 DEFAULT 0,
  rem_ttc_document did9 DEFAULT 0,
  tx_rem_ttc_document did9 DEFAULT 0,
  nb_e_document did_facultatif DEFAULT 0,
  ttc smallint DEFAULT 0,
  commentaire dlib_commentaire,
  mt_ttc_calc did9facult,
  mt_ht_calc did9facult,
  mt_tva_calc did9facult,
  net_ttc_calc did9facult,
  net_ht_calc did9facult,
  net_tva_calc did9facult,
  net_a_payer did9facult,
  mt_ttc_avt_rem_globale_calc did9facult,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  id_gr_mouv_stock did_facultatif,
  date_deb_t timestamp without time zone NOT NULL,
  date_fin_t timestamp without time zone NOT NULL,
  date_deb_r timestamp without time zone NOT NULL,
  date_fin_r timestamp without time zone NOT NULL,
  CONSTRAINT ta_modele_fabrication_pkey PRIMARY KEY (id_document),
  CONSTRAINT fk_ta_modelefabrication_id_gr_mouv_stock FOREIGN KEY (id_gr_mouv_stock)
      REFERENCES ta_gr_mouv_stock (id_gr_mouv_stock) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_modele_fabrication_id_t_paiement_fkey FOREIGN KEY (id_t_paiement)
      REFERENCES ta_t_paiement (id_t_paiement) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_modele_fabrication_id_tiers_fkey FOREIGN KEY (id_tiers)
      REFERENCES ta_tiers (id_tiers) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_modele_fabrication
  OWNER TO postgres;
  
  CREATE OR REPLACE FUNCTION tbdid_modele_fabrication_etranger()
  RETURNS trigger AS
$BODY$
begin
  delete from ta_l_modele_fabrication where id_DOCUMENT = old.id_DOCUMENT;
  delete from ta_infos_modele_fabrication where id_DOCUMENT = old.id_DOCUMENT;
  return null;
end
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION tbdid_modele_fabrication_etranger()
  OWNER TO postgres;


DROP INDEX IF EXISTS fki_ta_modele_fabrication_id_gr_mouv_stock;

CREATE INDEX fki_ta_modele_fabrication_id_gr_mouv_stock
  ON ta_fabrication
  USING btree
  (id_gr_mouv_stock);

DROP INDEX IF EXISTS "rdb$foreign_ta_modele_fabrication_1";

CREATE INDEX "rdb$foreign_ta_modele_fabrication_1"
  ON ta_modele_fabrication
  USING btree
  (id_tiers);

DROP INDEX IF EXISTS "rdb$foreign_ta_modele_fabrication_2";

CREATE INDEX "rdb$foreign_ta_modele_fabrication_2"
  ON ta_modele_fabrication
  USING btree
  (id_t_paiement);

DROP INDEX IF EXISTS ta_modele_fabrication_code;

CREATE INDEX ta_modele_fabrication_code
  ON ta_modele_fabrication
  USING btree
  (code_document COLLATE pg_catalog."default");

DROP INDEX IF EXISTS unq1_ta_modele_fabrication;

CREATE INDEX unq1_ta_modele_fabrication
  ON ta_modele_fabrication
  USING btree
  (code_document COLLATE pg_catalog."default");


 DROP TRIGGER  IF EXISTS  tbdid_modele_fabrication_etranger ON ta_modele_fabrication;

CREATE TRIGGER tbdid_modele_fabrication_etranger
  AFTER DELETE
  ON ta_modele_fabrication
  FOR EACH ROW
  EXECUTE PROCEDURE tbdid_modele_fabrication_etranger();

 DROP TRIGGER  IF EXISTS  tbi_modele_fabrication ON ta_modele_fabrication;

CREATE TRIGGER tbi_modele_fabrication
  BEFORE INSERT
  ON ta_modele_fabrication
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

 DROP TRIGGER  IF EXISTS  tbu_modele_fabrication ON ta_modele_fabrication;

CREATE TRIGGER tbu_modele_fabrication
  BEFORE UPDATE
  ON ta_modele_fabrication
  FOR EACH ROW
  EXECUTE PROCEDURE before_update_code_doc();

 DROP TRIGGER  IF EXISTS  tbu_modele_fabrication_2 ON ta_modele_fabrication;

CREATE TRIGGER tbu_modele_fabrication_2
  BEFORE UPDATE
  ON ta_modele_fabrication
  FOR EACH ROW
  EXECUTE PROCEDURE before_update();

  -------------------------------------------------------------------------------------------------------------------------------------------------------------
  -------------------------------------------------------------------------------------------------------------------------------------------------------------
  -------------------------------------------------------------------------------------------------------------------------------------------------------------
  -------------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS ta_l_modele_fabrication
(
  id_l_document did3 NOT NULL DEFAULT nextval('num_id_l_document'::regclass),
  id_document did_facultatif,
  id_t_ligne did_facultatif NOT NULL,
  type_l_document dlib8nn,
  id_article did_facultatif,
  id_lot did_facultatif,
  id_entrepot did_facultatif,
  num_ligne_l_document integer DEFAULT 0,
  lib_l_document dlib255,
  qte_l_document did9facult,
  qte2_l_document did9facult,
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
  emplacement_l_document dlib255,
  id_mouvement_stock did_facultatif,
  CONSTRAINT ta_l_modele_fabrication_pkey PRIMARY KEY (id_l_document),
  CONSTRAINT fk_ta_l_modele_fabrication_1 FOREIGN KEY (id_document)
      REFERENCES ta_modele_fabrication (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_l_modele_fabrication_2 FOREIGN KEY (id_article)
      REFERENCES ta_article (id_article) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_l_modele_fabrication_3 FOREIGN KEY (id_lot)
      REFERENCES ta_lot (id_lot) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_l_modele_fabrication_4 FOREIGN KEY (id_entrepot)
      REFERENCES ta_entrepot (id_entrepot) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_l_modele_fabrication_5 FOREIGN KEY (id_mouvement_stock)
      REFERENCES ta_mouvement_stock (id_mouvement_stock) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_l_modele_fabrication_id_t_ligne_fkey FOREIGN KEY (id_t_ligne)
      REFERENCES ta_t_ligne (id_t_ligne) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_l_modele_fabrication
  OWNER TO postgres;

DROP INDEX IF EXISTS fk_ta_l_modele_fabrication_1;

CREATE INDEX fk_ta_l_modele_fabrication_1
  ON ta_l_modele_fabrication
  USING btree
  (id_document);
  
DROP INDEX IF EXISTS fk_ta_l_modele_fabrication_2;

CREATE INDEX fk_ta_l_modele_fabrication_2
  ON ta_l_modele_fabrication
  USING btree
  (id_article);

DROP INDEX IF EXISTS fki_ta_l_modele_fabrication_5;

CREATE INDEX fki_ta_l_modele_fabrication_5
  ON ta_l_modele_fabrication
  USING btree
  (id_mouvement_stock);

DROP INDEX IF EXISTS "rdb$foreign_ta_l_modele_fabrication";

CREATE INDEX "rdb$foreign_ta_l_modele_fabrication"
  ON ta_l_modele_fabrication
  USING btree
  (id_t_ligne);

DROP TRIGGER IF EXISTS tbi_l_modele_fabrication ON ta_l_modele_fabrication;

CREATE TRIGGER tbi_l_modele_fabrication
  BEFORE INSERT
  ON ta_l_modele_fabrication
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

DROP TRIGGER IF EXISTS tbiid_article_l_modele_fabrication ON ta_l_modele_fabrication;

CREATE TRIGGER tbiid_article_l_modele_fabrication
  BEFORE INSERT
  ON ta_l_modele_fabrication
  FOR EACH ROW
  EXECUTE PROCEDURE ligne_vide();

DROP TRIGGER IF EXISTS tbu_l_modele_fabrication ON ta_l_modele_fabrication;

CREATE TRIGGER tbu_l_modele_fabrication
  BEFORE UPDATE
  ON ta_l_modele_fabrication
  FOR EACH ROW
  EXECUTE PROCEDURE before_update();

DROP TRIGGER IF EXISTS tbuid_article_l_modele_fabrication ON ta_l_modele_fabrication;

CREATE TRIGGER tbuid_article_l_modele_fabrication
  BEFORE UPDATE
  ON ta_l_modele_fabrication
  FOR EACH ROW
  EXECUTE PROCEDURE ligne_vide();
  
  
 -------------------------------------------------------------------------------------------------------------------------------------------------------------
  -------------------------------------------------------------------------------------------------------------------------------------------------------------
  -------------------------------------------------------------------------------------------------------------------------------------------------------------
  -------------------------------------------------------------------------------------------------------------------------------------------------------------

  
  
  CREATE TABLE IF NOT EXISTS ta_infos_modele_fabrication
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
  CONSTRAINT ta_infos_modele_fabrication_pkey PRIMARY KEY (id_infos_document),
  CONSTRAINT fk_ta_infos_modele_fabrication_1 FOREIGN KEY (id_document)
      REFERENCES ta_modele_fabrication (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_infos_modele_fabrication
  OWNER TO postgres;

DROP INDEX IF EXISTS fk_ta_infos_modele_fabrication_1;

CREATE INDEX fk_ta_infos_modele_fabrication_1
  ON ta_infos_modele_fabrication
  USING btree
  (id_document);


DROP INDEX IF EXISTS ta_infos_modele_fabrication_id_fabrication;

CREATE INDEX ta_infos_modele_fabrication_id_fabrication
  ON ta_infos_modele_fabrication
  USING btree
  (id_document);

DROP INDEX IF EXISTS unq1_ta_infos_modele_fabrication;

CREATE INDEX unq1_ta_infos_modele_fabrication
  ON ta_infos_modele_fabrication
  USING btree
  (id_document);

DROP TRIGGER IF EXISTS tbi_ta_infos_modele_document ON ta_infos_modele_fabrication;

CREATE TRIGGER tbi_ta_infos_modele_document
  BEFORE INSERT
  ON ta_infos_modele_fabrication
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

DROP TRIGGER IF EXISTS tbu_ta_infos_modele_fabrication ON ta_infos_modele_fabrication;

CREATE TRIGGER tbu_ta_infos_modele_fabrication
  BEFORE UPDATE
  ON ta_infos_modele_fabrication
  FOR EACH ROW
  EXECUTE PROCEDURE before_update();

----------------------------------------------------------------------------
------------------------------------------------------------------------------
----------------------------------------------------------------------------------
INSERT INTO TA_GEN_CODE (ENTITE, FIXE, EXO, COMPTEUR, VERSION_OBJ) VALUES ('TaModeleFabrication', 'MF', 'courant', 'TaModeleFabrication a, a.codeDocument,5', 0);

-------------------------------------------------------------------------------------------------------------------
--------------------------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------------------------
INSERT INTO ta_controle(champ, contexte, controle_defaut, controle_utilisateur) VALUES ( 'TaModeleFabrication.codeDocument', 'MODELE_FABRICATION', 100, 100);
INSERT INTO ta_controle(champ, contexte, controle_defaut, controle_utilisateur) VALUES ( 'TaModeleFabrication.dateDocument', 'MODELE_FABRICATION', 100, 100);
INSERT INTO ta_controle(champ, contexte, controle_defaut, controle_utilisateur) VALUES ( 'TaModeleFabrication.dateLivDocument', 'MODELE_FABRICATION', 100, 100);
INSERT INTO ta_controle(champ, contexte, controle_defaut, controle_utilisateur) VALUES ( 'TaModeleFabrication.libelleDocument', 'MODELE_FABRICATION', 100, 100);
INSERT INTO ta_controle(champ, contexte, controle_defaut, controle_utilisateur) VALUES ( 'TaModeleFabrication.nbEDocument', 'MODELE_FABRICATION', 100, 100);
INSERT INTO ta_controle(champ, contexte, controle_defaut, controle_utilisateur) VALUES ( 'TaModeleFabrication.ttc', 'MODELE_FABRICATION', 100, 100);
INSERT INTO ta_controle(champ, contexte, controle_defaut, controle_utilisateur) VALUES ( 'TaModeleFabrication.commentaire', 'MODELE_FABRICATION', 100, 100);

INSERT INTO ta_controle(champ, contexte, controle_defaut, controle_utilisateur) VALUES ( 'TaLModeleFabrication.numLigneLDocument', 'L_MODELE_FABRICATION', 100, 100);
INSERT INTO ta_controle(champ, contexte, controle_defaut, controle_utilisateur) VALUES ( 'TaLModeleFabrication.libLDocument', 'L_MODELE_FABRICATION', 100, 100);
INSERT INTO ta_controle(champ, contexte, controle_defaut, controle_utilisateur) VALUES ( 'TaLModeleFabrication.u1LDocument', 'L_MODELE_FABRICATION', 100, 100);
INSERT INTO ta_controle(champ, contexte, controle_defaut, controle_utilisateur) VALUES ( 'TaLModeleFabrication.u2LDocument', 'L_MODELE_FABRICATION', 100, 100);
INSERT INTO ta_controle(champ, contexte, controle_defaut, controle_utilisateur) VALUES ( 'TaLModeleFabrication.qteLDocument', 'L_MODELE_FABRICATION', 100, 100);
INSERT INTO ta_controle(champ, contexte, controle_defaut, controle_utilisateur) VALUES ( 'TaLModeleFabrication.qte2LDocument', 'L_MODELE_FABRICATION', 100, 100);

SELECT f_insert('ta_controle', 'champ =''TaModeleFabrication.codeDocument'' and contexte =''MODELE_FABRICATION''',
'INSERT INTO ta_controle(champ, contexte, controle_defaut, controle_utilisateur) VALUES (''TaLFabrication.numLigneLDocument'', ''L_FABRICATION'', 100, 100)',null);



















