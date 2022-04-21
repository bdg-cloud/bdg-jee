--set schema 'demo';
ALTER TABLE ta_r_document ADD COLUMN id_abonnement did_facultatif;

ALTER TABLE ta_r_document ADD CONSTRAINT ta_r_document_id_abonnement_fkey FOREIGN KEY (id_abonnement) REFERENCES ta_abonnement(id_document);

CREATE INDEX fk_ta_r_doc_abonnement
  ON ta_r_document
  USING btree
  (id_abonnement);
  
--CREATION TABLE TA_FREQUENCE
CREATE TABLE ta_frequence (
id_frequence SERIAL PRIMARY KEY,
code_frequence character varying(20),
libl_frequence dlib255,
qui_cree dlib50,
quand_cree date_heure_lgr,
qui_modif date_heure_lgr,
quand_modif date_heure_lgr,
version num_version,
ip_access dlib50,
version_obj did_version_obj
);
ALTER TABLE ta_frequence OWNER TO bdg;

--INSERTION DE FREQUENCES
INSERT INTO ta_frequence (code_frequence,libl_frequence) VALUES ('JOUR','Jour');
INSERT INTO ta_frequence (code_frequence,libl_frequence) VALUES ('SEM','Semaine');
INSERT INTO ta_frequence (code_frequence,libl_frequence) VALUES ('MOIS','Mois');
INSERT INTO ta_frequence (code_frequence,libl_frequence) VALUES ('AN','Année');

--CREATION COLONNE ID_FREQUENCE DANS LA TABLE TA_ABONNEMENT
ALTER TABLE ta_abonnement ADD COLUMN id_frequence did_facultatif;
ALTER TABLE ta_abonnement ADD CONSTRAINT fk_ta_abonnement_id_frequence FOREIGN KEY (id_frequence) REFERENCES ta_frequence(id_frequence) MATCH SIMPLE;

--AJout de la colonne id_frequence dans la TABLE TA_STRIPE_PLAN
ALTER TABLE ta_stripe_plan ADD COLUMN id_frequence did_facultatif;
ALTER TABLE ta_stripe_plan ADD CONSTRAINT fk_ta_stripe_plan_id_frequence FOREIGN KEY (id_frequence) REFERENCES ta_frequence(id_frequence) MATCH SIMPLE;
