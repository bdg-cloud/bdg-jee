--set schema 'demo';

drop sequence ta_ref_article_fournisseur_id_seq;
  CREATE SEQUENCE ta_ref_article_fournisseur_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE ta_ref_article_fournisseur_id_seq
  OWNER TO bdg;

  
CREATE TABLE ta_ref_article_fournisseur
(
  id_ref_article_fournisseur integer NOT NULL DEFAULT nextval('ta_ref_article_fournisseur_id_seq'::regclass),
  id_fournisseur  did_facultatif NOT NULL,
  id_article did_facultatif NOT NULL,
  code_article_fournisseur dlib255,
  code_barre_fournisseur dlib255,
  descriptif dlib_commentaire,
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_ref_article_fournisseur_pkey PRIMARY KEY (id_ref_article_fournisseur),
  CONSTRAINT fk_ta_ref_article_fournisseur_1 FOREIGN KEY (id_article)
      REFERENCES ta_article (id_article) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_ref_article_fournisseur_2 FOREIGN KEY (id_fournisseur)
      REFERENCES ta_tiers (id_tiers) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);



CREATE TRIGGER tbi_ref_article_fournisseur
  BEFORE INSERT
  ON ta_ref_article_fournisseur
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();



CREATE TRIGGER tbu_ref_article_fournisseur
  BEFORE UPDATE
  ON ta_ref_article_fournisseur
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();
  

--CREATE SEQUENCE ta_r_ref_article_fournisseur_id_seq
--  INCREMENT 1
--  MINVALUE 1
--  MAXVALUE 9223372036854775807
--  START 1
--  CACHE 1;
--ALTER TABLE ta_r_ref_article_fournisseur_id_seq
--  OWNER TO bdg;
--
--  
--CREATE TABLE ta_r_ref_article_fournisseur
--(
--  id integer NOT NULL DEFAULT nextval('ta_r_ref_article_fournisseur_id_seq'::regclass),
--  id_article did_facultatif NOT NULL,
--  id_ref_article_fournisseur did_facultatif NOT NULL,
--  qui_cree dlib50,
--  quand_cree date_heure_lgr,
--  qui_modif dlib50,
--  quand_modif date_heure_lgr,
--  version num_version,
--  ip_acces dlib50,
--  version_obj did_version_obj,
--  CONSTRAINT ta_r_ref_article_fournisseur_pkey PRIMARY KEY (id),
--  CONSTRAINT fk_ta_r_ref_article_fournisseur_1 FOREIGN KEY (id_article)
--      REFERENCES ta_article (id_article) MATCH SIMPLE
--      ON UPDATE NO ACTION ON DELETE NO ACTION,
--  CONSTRAINT fk_ta_r_ref_article_fournisseur_2 FOREIGN KEY (id_ref_article_fournisseur)
--      REFERENCES ta_ref_article_fournisseur (id_ref_article_fournisseur) MATCH SIMPLE
--      ON UPDATE NO ACTION ON DELETE NO ACTION
--)
--WITH (
--  OIDS=FALSE
--);
--
--
--
--CREATE INDEX fk_ta_r_ref_article_fournisseur_1
--  ON ta_r_ref_article_fournisseur
--  USING btree
--  (id_article);
--
--
--CREATE INDEX fk_ta_r_ref_article_fournisseur_2
--  ON ta_r_ref_article_fournisseur
--  USING btree
--  (id_ref_article_fournisseur);
--
--
--
--CREATE TRIGGER tbi_r_ref_article_fournisseur
--  BEFORE INSERT
--  ON ta_r_ref_article_fournisseur
--  FOR EACH ROW
--  EXECUTE PROCEDURE before_insert();
--
--
--
--CREATE TRIGGER tbu_r_ref_article_fournisseur
--  BEFORE UPDATE
--  ON ta_r_ref_article_fournisseur
--  FOR EACH ROW
--  EXECUTE PROCEDURE before_insert();
--
--  
--  
 ALTER TABLE ta_tiers  ADD COLUMN ma_ref_tiers dlib255;
 
 ALTER TABLE ta_l_bon_reception   ADD COLUMN code_barre dlib255;
 ALTER TABLE ta_l_bonliv ADD COLUMN code_barre dlib255;
 ALTER TABLE ta_l_ticket_caisse ADD COLUMN code_barre dlib255;
 

INSERT INTO ta_preferences (groupe, cle, valeur, valeur_defaut, type_donnees, libelle, version_obj) VALUES ('caisse', 'tiers_defaut_caisse', 'CDIV', ' ', 'string', 'Code tiers (client) par defaut caisse ', 0);


ALTER TABLE ta_unite DISABLE TRIGGER tbuid_unite_etranger;

