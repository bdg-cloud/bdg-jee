--set schema 'demo';

CREATE TABLE ta_flash
(
  id_flash serial NOT NULL,
  id_groupe_preparation did_facultatif,
  code_flash dlgr_codel,
  date_flash date_heure_lgr,
  date_transfert date_heure_lgr,
  id_tiers did_facultatif,
  id_utilisateur did_facultatif,
  email_utilisateur dlib255,
  username dlib255,
  id_t_doc did_facultatif,
  gestion_lot boolean DEFAULT false,
  id_r_etat did_facultatif,
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  commentaire dlib_commentaire,
  libelle_flash dlib255,
  nb_decimales_qte integer,
  numero_commande_fournisseur dlib50,
  code_tiers dlib100,
  code_t_doc dlib100,
  CONSTRAINT ta_flash_pkey PRIMARY KEY (id_flash),
  CONSTRAINT ta_flash_id_groupe_fkey FOREIGN KEY (id_groupe_preparation)
      REFERENCES ta_groupe_preparation (id_groupe_preparation) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_flash_id_r_etat_fkey FOREIGN KEY (id_r_etat)
      REFERENCES ta_r_etat (id_r_etat) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_flash_id_t_doc_fkey FOREIGN KEY (id_t_doc)
      REFERENCES ta_t_doc (id_t_doc) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_flash_id_tiers_fkey FOREIGN KEY (id_tiers)
      REFERENCES ta_tiers (id_tiers) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_flash_id_utilisateur_fkey FOREIGN KEY (id_utilisateur)
      REFERENCES ta_utilisateur (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_flash_code_flash_key UNIQUE (code_flash)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_flash
  OWNER TO bdg;


CREATE TRIGGER tbi_flash
  BEFORE INSERT
  ON ta_flash
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();





CREATE TABLE ta_l_flash
(
  id_l_flash serial NOT NULL,
  id_flash did_facultatif NOT NULL,
  id_article did_facultatif,
   id_tiers did_facultatif,
  lib_l_flash dlib255,
  qte_l_flash qte3,
  qte2_l_flash qte3,
  u1_l_flash character varying(20),
  u2_l_flash character varying(20),
  id_lot did_facultatif,
  id_entrepot did_facultatif,
  emplacement_l_flash dlib255,
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  id_r_etat did_facultatif,
  code_barre dlib255,
  code_barre_lu dlib255,
  code_article dlgr_codel,
  numlot dlib100,
  code_entrepot dlib100,
  code_tiers dlib100,
  CONSTRAINT ta_l_flash_pkey PRIMARY KEY (id_l_flash),
  CONSTRAINT fk_ta_l_flash_1 FOREIGN KEY (id_article)
      REFERENCES ta_article (id_article) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_l_flash_id_tiers_fkey FOREIGN KEY (id_tiers)
      REFERENCES ta_tiers (id_tiers) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_l_flash_2 FOREIGN KEY (id_flash)
      REFERENCES ta_flash (id_flash) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_l_flash_id_r_etat_fkey FOREIGN KEY (id_r_etat)
      REFERENCES ta_r_etat_ligne_doc (id_r_etat_ligne_doc) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_l_flash
  OWNER TO bdg;


CREATE TRIGGER tbi_l_flash
  BEFORE INSERT
  ON ta_l_flash
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();


CREATE TRIGGER tbu_l_flash
  BEFORE UPDATE
  ON ta_l_flash
  FOR EACH ROW
  EXECUTE PROCEDURE before_update();

  
--------------------------------
INSERT INTO ta_gen_code_ex
( code_gen_code, cle_gen_code, valeur_gen_code )
VALUES( 'codeFlash', 'TaFlash', 'FL{@exo}{@num}' );

INSERT INTO ta_gen_code_ex
( code_gen_code, cle_gen_code, valeur_gen_code )
VALUES( 'codeDocument', 'TaPreparation', 'PA{@exo}{@num}' );

------------------------------  
  
ALTER TABLE ta_r_document ADD COLUMN id_preparation did_facultatif;  
ALTER TABLE ta_r_document ADD COLUMN id_flash did_facultatif; 

ALTER TABLE ta_ligne_a_ligne   ADD COLUMN id_l_flash did_facultatif;
ALTER TABLE ta_ligne_a_ligne   ADD COLUMN id_l_preparation did_facultatif;

ALTER TABLE ta_r_document
  ADD CONSTRAINT ta_r_document_id_preparation_fkey FOREIGN KEY (id_preparation)
      REFERENCES ta_preparation (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE ta_r_document
  ADD CONSTRAINT ta_r_document_id_flash_fkey FOREIGN KEY (id_flash)
      REFERENCES ta_flash (id_flash) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION; 
      
ALTER TABLE ta_ligne_a_ligne
  ADD CONSTRAINT ta_ligne_a_ligne_id_preparation_fkey FOREIGN KEY (id_l_preparation)
      REFERENCES ta_l_preparation (id_l_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;  

ALTER TABLE ta_ligne_a_ligne
  ADD CONSTRAINT ta_ligne_a_ligne_id_flash_fkey FOREIGN KEY (id_l_flash)
      REFERENCES ta_l_flash (id_l_flash) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;       
      
      
INSERT INTO ta_t_doc ( code_t_doc, lib_t_doc, version_obj, systeme) VALUES( 'Preparation', 'Préparation',  0, true);

ALTER TABLE ta_hist_r_etat_ligne_doc ADD COLUMN id_l_flash did_facultatif;
ALTER TABLE ta_r_etat_ligne_doc ADD COLUMN id_l_flash did_facultatif;

ALTER TABLE ta_hist_r_etat_ligne_doc ADD COLUMN id_l_preparation did_facultatif;
ALTER TABLE ta_r_etat_ligne_doc ADD COLUMN id_l_preparation did_facultatif;

ALTER TABLE ta_r_etat_ligne_doc
  ADD CONSTRAINT ta_r_etat_ligne_doc_l_flash FOREIGN KEY (id_l_flash)
      REFERENCES ta_l_flash (id_l_flash) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE ta_r_etat_ligne_doc
  ADD CONSTRAINT ta_r_etat_ligne_doc_l_preparation FOREIGN KEY (id_l_preparation)
      REFERENCES ta_l_preparation (id_l_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
            
ALTER TABLE ta_hist_r_etat_ligne_doc
  ADD CONSTRAINT ta_hist_r_etat_l_doc_id_l_flash FOREIGN KEY (id_l_flash)
      REFERENCES ta_l_flash (id_l_flash) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE ta_hist_r_etat_ligne_doc
  ADD CONSTRAINT ta_hist_r_etat_l_doc_id_l_preparation FOREIGN KEY (id_l_preparation)
      REFERENCES ta_l_preparation (id_l_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION; 
      
ALTER TABLE ta_hist_r_etat ADD COLUMN id_flash did_facultatif;
ALTER TABLE ta_hist_r_etat ADD COLUMN id_preparation did_facultatif;


ALTER TABLE ta_r_etat ADD COLUMN id_flash did_facultatif;
ALTER TABLE ta_r_etat ADD COLUMN id_preparation did_facultatif; 
alter table ta_l_preparation add column code_barre dlib100;     

--ALTER TABLE ta_flash ADD COLUMN email_utilisateur dlib255;
--ALTER TABLE ta_flash ADD COLUMN username dlib255;      



