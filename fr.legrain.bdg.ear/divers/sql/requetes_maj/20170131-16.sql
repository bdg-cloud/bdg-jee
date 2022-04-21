/*SET SCHEMA 'demo';*/

CREATE DOMAIN qte3  AS numeric(15,3);
delete from ta_type_conformite a where  a.code like 'intercal';


ALTER TABLE ta_article
  ADD COLUMN utilise_dlc boolean NOT NULL DEFAULT false;
  
ALTER TABLE ta_article
  ADD COLUMN id_unite_stock did_facultatif;  
  
ALTER TABLE ta_article
  ADD FOREIGN KEY (id_unite_stock) REFERENCES ta_unite (id_unite) ON UPDATE NO ACTION ON DELETE NO ACTION;
  
ALTER TABLE ta_t_civilite  alter COLUMN code_t_civilite type dlib100;
  
ALTER TABLE ta_etat_stock ALTER COLUMN qte2_etat_stock TYPE qte3;
ALTER TABLE ta_etat_stock ALTER COLUMN qte1_etat_stock TYPE qte3;
ALTER TABLE ta_l_acompte ALTER COLUMN  qte_l_document  TYPE qte3;
ALTER TABLE ta_l_acompte ALTER COLUMN  qte2_l_document TYPE qte3;
ALTER TABLE ta_l_echeance ALTER COLUMN   qte_l_document  TYPE qte3;
ALTER TABLE ta_l_fabrication_mp ALTER COLUMN   qte_l_document   TYPE qte3;
ALTER TABLE ta_l_fabrication_mp ALTER COLUMN   qte2_l_document  TYPE qte3;
ALTER TABLE ta_l_fabrication_pf ALTER COLUMN   qte_l_document  TYPE qte3; 
ALTER TABLE ta_l_fabrication_pf ALTER COLUMN   qte2_l_document TYPE qte3;
ALTER TABLE ta_l_facture ALTER COLUMN   qte_l_document   TYPE qte3;
ALTER TABLE ta_l_facture ALTER COLUMN   qte2_l_document  TYPE qte3;

ALTER TABLE ta_l_apporteur ALTER COLUMN  qte_l_document  TYPE qte3;
ALTER TABLE ta_l_apporteur ALTER COLUMN  qte2_l_document TYPE qte3;
ALTER TABLE ta_l_avis_echeance ALTER COLUMN  qte_l_document  TYPE qte3;
ALTER TABLE ta_l_avis_echeance ALTER COLUMN  qte2_l_document TYPE qte3;
ALTER TABLE ta_l_avoir ALTER COLUMN  qte_l_document  TYPE qte3;
ALTER TABLE ta_l_avoir ALTER COLUMN  qte2_l_document TYPE qte3;
ALTER TABLE ta_l_bon_reception ALTER COLUMN  qte_l_document  TYPE qte3;
ALTER TABLE ta_l_bon_reception ALTER COLUMN  qte2_l_document TYPE qte3;
ALTER TABLE ta_l_boncde ALTER COLUMN  qte_l_document  TYPE qte3;
ALTER TABLE ta_l_boncde ALTER COLUMN  qte2_l_document TYPE qte3;
ALTER TABLE ta_l_bonliv ALTER COLUMN  qte_l_document  TYPE qte3;
ALTER TABLE ta_l_bonliv ALTER COLUMN  qte2_l_document TYPE qte3;
ALTER TABLE ta_l_devis ALTER COLUMN  qte_l_document  TYPE qte3;
ALTER TABLE ta_l_devis ALTER COLUMN  qte2_l_document TYPE qte3;


ALTER TABLE ta_l_inventaire ALTER COLUMN    qte_reelle  TYPE qte3;
ALTER TABLE ta_l_inventaire ALTER COLUMN    qte_theorique TYPE qte3;
ALTER TABLE ta_l_inventaire ALTER COLUMN    qte2_l_inventaire   TYPE qte3;
ALTER TABLE ta_l_modele_fabrication_mp ALTER COLUMN   qte_l_document TYPE qte3;  
ALTER TABLE ta_l_modele_fabrication_mp ALTER COLUMN   qte2_l_document  TYPE qte3;
ALTER TABLE ta_l_modele_fabrication_pf ALTER COLUMN   qte_l_document TYPE qte3;  
ALTER TABLE ta_l_modele_fabrication_pf ALTER COLUMN   qte2_l_document  TYPE qte3; 
ALTER TABLE ta_l_prelevement ALTER COLUMN   qte_l_document  TYPE qte3; 
ALTER TABLE ta_l_prelevement ALTER COLUMN   qte2_l_document  TYPE qte3;
ALTER TABLE ta_l_proforma  ALTER COLUMN  qte_l_document   TYPE qte3;
ALTER TABLE ta_l_proforma  ALTER COLUMN  qte2_l_document TYPE qte3;
ALTER TABLE ta_l_recette ALTER COLUMN   qte_l_document   TYPE qte3;
ALTER TABLE ta_l_recette ALTER COLUMN   qte2_l_document   TYPE qte3;
ALTER TABLE ta_mouvement_stock ALTER COLUMN   qte1_stock   TYPE qte3;
ALTER TABLE ta_mouvement_stock ALTER COLUMN   qte2_stock   TYPE qte3;
ALTER TABLE ta_r_titre_transport ALTER COLUMN   qte_titre_transport  TYPE qte3; 
ALTER TABLE ta_recette ALTER COLUMN   qte  TYPE qte3;
ALTER TABLE ta_report_stock  ALTER COLUMN   qte1_report_stock  TYPE qte3; 
ALTER TABLE ta_report_stock  ALTER COLUMN  qte2_report_stock TYPE qte3;
ALTER TABLE ta_report_stock_capsules ALTER COLUMN   qte1_report_stock TYPE qte3;  
ALTER TABLE ta_report_stock_capsules ALTER COLUMN   qte2_report_stock TYPE qte3;
ALTER TABLE ta_stock_capsules ALTER COLUMN   qte1_stock  TYPE qte3; 
ALTER TABLE ta_stock_capsules ALTER COLUMN   qte2_stock  TYPE qte3;
ALTER TABLE ta_titre_transport ALTER COLUMN   qte_min_titre_transport  TYPE qte3; 

CREATE TABLE ta_marque_article
(
  id_marque_article serial NOT NULL,
  code_marque_article dlib255nn,
  libelle_marque_article dlib255,
  description_marque_article dlib255,
  chemin_image_marque_article dlib255,
  nom_image_marque_article dlib255,
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  id_image_marque_article did_facultatif NOT NULL,
  CONSTRAINT ta_marque_article_pkey PRIMARY KEY (id_marque_article),
  CONSTRAINT ta_marque_article_code_unique UNIQUE (code_marque_article)
)
WITH (
  OIDS=FALSE
);

ALTER TABLE ta_marque_article
  OWNER TO postgres;


CREATE INDEX ta_marque_article_id
  ON ta_marque_article
  USING btree
  (id_marque_article);


CREATE INDEX unq1_ta_marque_article
  ON ta_marque_article
  USING btree
  (code_marque_article COLLATE pg_catalog."default");




CREATE TRIGGER tbi_marque_article
  BEFORE INSERT
  ON ta_marque_article
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();



CREATE TRIGGER tbu_marque_article
  BEFORE UPDATE
  ON ta_marque_article
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();


ALTER TABLE ta_article  ADD COLUMN id_marque did_facultatif; 
ALTER TABLE ta_article   ADD FOREIGN KEY (id_marque) REFERENCES ta_marque_article (id_marque_article) ON UPDATE NO ACTION ON DELETE NO ACTION; 

 