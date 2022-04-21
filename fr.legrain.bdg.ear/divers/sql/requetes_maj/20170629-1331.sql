CREATE TABLE ta_agenda
(
  id_agenda serial NOT NULL,
  nom dlib100,
  description text,
  couleur dlib100,
  id_utilisateur did_facultatif,
  prive boolean,
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_agenda_pkey PRIMARY KEY (id_agenda),
  CONSTRAINT fk_ta_agenda_id_utilisateur FOREIGN KEY (id_utilisateur)
      REFERENCES ta_utilisateur (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_agenda
  OWNER TO postgres;

CREATE INDEX fk_id_agenda_id_utilisateur
  ON ta_agenda
  USING btree
  (id_utilisateur);

CREATE TRIGGER tbi_ta_agenda
  BEFORE INSERT
  ON ta_agenda
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

CREATE TRIGGER tbu_ta_agenda
  BEFORE UPDATE
  ON ta_agenda
  FOR EACH ROW
  EXECUTE PROCEDURE before_update();

  ---------------------------------------------------------------------------------------------------
CREATE TABLE ta_categorie_evenement
(
  id_categorie_evenement serial NOT NULL,
  code_categorie_evenement dlgr_code,
  libelle_categorie_evenement dlgr_lib,
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_categorie_evenement_pkey PRIMARY KEY (id_categorie_evenement)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_categorie_evenement
  OWNER TO postgres;

CREATE TRIGGER tbi_ta_categorie_evenement
  BEFORE INSERT
  ON ta_categorie_evenement
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

CREATE TRIGGER tbu_ta_categorie_evenement
  BEFORE UPDATE
  ON ta_categorie_evenement
  FOR EACH ROW
  EXECUTE PROCEDURE before_update();
  ---------------------------------------------------------------------------------------------------
  CREATE TABLE ta_type_notification
(
  id_type_notification serial NOT NULL,
  code_type_notification dlgr_code,
  libelle_type_notification dlgr_lib,
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_type_notification_pkey PRIMARY KEY (id_type_notification)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_type_notification
  OWNER TO postgres;

CREATE TRIGGER tbi_ta_type_notification
  BEFORE INSERT
  ON ta_type_notification
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

CREATE TRIGGER tbu_ta_type_notification
  BEFORE UPDATE
  ON ta_type_notification
  FOR EACH ROW
  EXECUTE PROCEDURE before_update();

---------------------------------------------------------------------------------------------------
CREATE TABLE ta_type_evenement
(
  id_type_evenement serial NOT NULL,
  code_type_evenement dlgr_code,
  libelle_type_evenement dlgr_lib,
  id_categorie_evenement did_facultatif,
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_type_evenement_pkey PRIMARY KEY (id_type_evenement),
  CONSTRAINT fk_ta_type_evenement_id_categorie_evenement FOREIGN KEY (id_categorie_evenement)
      REFERENCES ta_categorie_evenement (id_categorie_evenement) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_type_evenement
  OWNER TO postgres;

CREATE INDEX fk_ta_type_evenement_id_categorie_evenement
  ON ta_type_evenement
  USING btree
  (id_categorie_evenement);

CREATE TRIGGER tbi_ta_type_evenement
  BEFORE INSERT
  ON ta_type_evenement
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

CREATE TRIGGER tbu_ta_type_evenement
  BEFORE UPDATE
  ON ta_type_evenement
  FOR EACH ROW
  EXECUTE PROCEDURE before_update();
  ---------------------------------------------------------------------------------------------------

CREATE TABLE ta_recurrence_evenement
(
  id_recurrence_evenement serial NOT NULL,
  date_debut_recurrence date_heure_lgr,
  date_fin_recurrence date_heure_lgr,
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_recurrence_evenement_pkey PRIMARY KEY (id_recurrence_evenement)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_recurrence_evenement
  OWNER TO postgres;

CREATE TRIGGER tbi_ta_recurrence_evenement
  BEFORE INSERT
  ON ta_recurrence_evenement
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

CREATE TRIGGER tbu_ta_recurrence_evenement
  BEFORE UPDATE
  ON ta_recurrence_evenement
  FOR EACH ROW
  EXECUTE PROCEDURE before_update();
---------------------------------------------------------------------------------------------------
CREATE TABLE ta_evenement
(
  id_evenement serial NOT NULL,
  id_type_evenement did_facultatif,
  id_utilisateur did_facultatif,
  id_agenda did_facultatif,
  id_recurrence_evenement did_facultatif,
  code_evenement dlgr_code,
  libelle_evenement dlgr_lib,
  description text,
  emplacement dlib100,
  couleur dlib100,
  date_debut date_heure_lgr,
  date_fin date_heure_lgr,
  en_cours boolean,
  termine boolean,
  recurrent boolean,
  prive boolean,
  toute_la_journee boolean,
  nbMinute_duree integer,
  nbHeure_duree integer,
  nbJours_duree integer,
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_evenement_pkey PRIMARY KEY (id_evenement),
  CONSTRAINT fk_ta_evenement_id_type_evenement FOREIGN KEY (id_type_evenement)
      REFERENCES ta_type_evenement (id_type_evenement) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_evenement_id_utilisateur FOREIGN KEY (id_utilisateur)
      REFERENCES ta_utilisateur (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_evenement_id_agenda FOREIGN KEY (id_agenda)
      REFERENCES ta_agenda (id_agenda) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_evenement_id_recurrence_evenement FOREIGN KEY (id_recurrence_evenement)
      REFERENCES ta_recurrence_evenement (id_recurrence_evenement) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_evenement
  OWNER TO postgres;

CREATE TRIGGER tbi_ta_evenement
  BEFORE INSERT
  ON ta_evenement
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

CREATE TRIGGER tbu_ta_evenement
  BEFORE UPDATE
  ON ta_evenement
  FOR EACH ROW
  EXECUTE PROCEDURE before_update();
---------------------------------------------------------------------------------------------------
CREATE TABLE ta_notification
(
  id_notification serial NOT NULL,
  id_type_notification did_facultatif,
  id_evenement did_facultatif,
  nb_jour_avant_notification integer,
  date_notification date_heure_lgr,
  heure_notification date_heure_lgr,
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_notification_pkey PRIMARY KEY (id_notification),
  CONSTRAINT fk_ta_notification_id_type_notification FOREIGN KEY (id_type_notification)
      REFERENCES ta_type_notification (id_type_notification) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_notification_id_evenement FOREIGN KEY (id_evenement)
      REFERENCES ta_evenement (id_evenement) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_notification
  OWNER TO postgres;

CREATE TRIGGER tbi_ta_notification
  BEFORE INSERT
  ON ta_notification
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

CREATE TRIGGER tbu_ta_notification
  BEFORE UPDATE
  ON ta_notification
  FOR EACH ROW
  EXECUTE PROCEDURE before_update();

-------------------------------------------------------------------------------------------------------------------
CREATE TABLE ta_r_tiers_evenement
(
  id serial NOT NULL,
  id_evenement did_facultatif NOT NULL,
  id_tiers did_facultatif NOT NULL,
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_r_tiers_evenement_pkey PRIMARY KEY (id),
  CONSTRAINT tta_r_tiers_evenement_id_evenement_fkey FOREIGN KEY (id_evenement)
      REFERENCES ta_evenement (id_evenement) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_r_tiers_evenement_id_tiers_fkey FOREIGN KEY (id_tiers)
      REFERENCES ta_tiers (id_tiers) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_r_tiers_evenement
  OWNER TO postgres;

CREATE INDEX idx_ta_r_tiers_evenement_id_evenement
  ON ta_r_tiers_evenement
  USING btree
  (id_evenement);

CREATE INDEX idx_ta_r_tiers_evenement_id_tiers
  ON ta_r_tiers_evenement
  USING btree
  (id_tiers);

CREATE TRIGGER tbi_ta_r_tiers_evenement
  BEFORE INSERT
  ON ta_r_tiers_evenement
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

CREATE TRIGGER tbu_ta_r_tiers_evenement
  BEFORE UPDATE
  ON ta_r_tiers_evenement
  FOR EACH ROW
  EXECUTE PROCEDURE before_update();

-------------------------------------------------------------------------------------------------------------------

CREATE TABLE ta_r_utilisateur_evenement
(
  id serial NOT NULL,
  id_evenement did_facultatif NOT NULL,
  id_utilisateur did_facultatif NOT NULL,
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_r_utilisateur_evenement_pkey PRIMARY KEY (id),
  CONSTRAINT ta_r_utilisateur_evenement_id_evenement_fkey FOREIGN KEY (id_evenement)
      REFERENCES ta_evenement (id_evenement) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_r_utilisateur_evenement_id_utilisateur_fkey FOREIGN KEY (id_utilisateur)
      REFERENCES ta_utilisateur (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_r_utilisateur_evenement
  OWNER TO postgres;

CREATE INDEX idx_ta_r_utilisateur_evenement_id_evenement
  ON ta_r_utilisateur_evenement
  USING btree
  (id_evenement);

CREATE INDEX idx_ta_r_utilisateur_evenement_id_utilisateur
  ON ta_r_utilisateur_evenement
  USING btree
  (id_utilisateur);

CREATE TRIGGER tbi_ta_r_utilisateur_evenement
  BEFORE INSERT
  ON ta_r_utilisateur_evenement
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

CREATE TRIGGER tbu_ta_r_utilisateur_evenement
  BEFORE UPDATE
  ON ta_r_utilisateur_evenement
  FOR EACH ROW
  EXECUTE PROCEDURE before_update();
-------------------------------------------------------------------------------------------------------------------
CREATE TABLE ta_r_document_evenement
(
  id_r_document_evenement serial NOT NULL,
  id_evenement did_facultatif NOT NULL,
  id_facture did_facultatif,
  id_devis did_facultatif,
  id_boncde did_facultatif,
  id_bonliv did_facultatif,
  id_avoir did_facultatif,
  id_apporteur did_facultatif,
  id_proforma did_facultatif,
  id_acompte did_facultatif,
  id_reglement did_facultatif,
  id_prelevement did_facultatif,
  id_avis_echeance did_facultatif,
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  id_doc_genere did_facultatif,
  CONSTRAINT ta_r_document_evenement_pkey PRIMARY KEY (id_r_document_evenement),
  CONSTRAINT ta_r_doc_evenement_id_evenement_fkey FOREIGN KEY (id_evenement)
      REFERENCES ta_evenement (id_evenement) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_r_doc_evenement_avis FOREIGN KEY (id_avis_echeance)
      REFERENCES ta_avis_echeance (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_r_doc_evenement_genere FOREIGN KEY (id_doc_genere)
      REFERENCES ta_r_document (id_r_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_r_document_evenement_apporteur FOREIGN KEY (id_apporteur)
      REFERENCES ta_apporteur (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_r_document_evenement_proforma FOREIGN KEY (id_proforma)
      REFERENCES ta_proforma (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_r_document_evenement_prelev FOREIGN KEY (id_prelevement)
      REFERENCES ta_prelevement (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_r_document_evenement_acompte FOREIGN KEY (id_acompte)
      REFERENCES ta_acompte (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_r_document_evenement_id_avoir_fkey FOREIGN KEY (id_avoir)
      REFERENCES ta_avoir (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_r_document_evenement_id_boncde_fkey FOREIGN KEY (id_boncde)
      REFERENCES ta_boncde (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_r_document_evenement_id_bonliv_fkey FOREIGN KEY (id_bonliv)
      REFERENCES ta_bonliv (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_r_document_evenement_id_devis_fkey FOREIGN KEY (id_devis)
      REFERENCES ta_devis (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_r_document_evenement_id_facture_fkey FOREIGN KEY (id_facture)
      REFERENCES ta_facture (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_r_document_evenement_id_reglement_fkey FOREIGN KEY (id_reglement)
      REFERENCES ta_reglement (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_r_document_evenement
  OWNER TO postgres;

CREATE INDEX idx_ta_r_document_evenement_id_avis_echeance
  ON ta_r_document_evenement
  USING btree
  (id_avis_echeance);

CREATE INDEX idx_ta_r_document_evenement_id_doc_genere
  ON ta_r_document_evenement
  USING btree
  (id_doc_genere);

CREATE INDEX fk_ta_r_documeidx_ta_r_document_evenement_id_apporteur
  ON ta_r_document_evenement
  USING btree
  (id_apporteur);

CREATE INDEX idx_ta_r_document_evenement_id_proforma
  ON ta_r_document_evenement
  USING btree
  (id_proforma);

CREATE INDEX idx_ta_r_document_evenement_id_prelevement
  ON ta_r_document_evenement
  USING btree
  (id_prelevement);

CREATE INDEX idx_ta_r_document_evenement_id_acompte
  ON ta_r_document_evenement
  USING btree
  (id_acompte);

CREATE INDEX idx_ta_r_document_evenement_id_facture
  ON ta_r_document_evenement
  USING btree
  (id_facture);

CREATE INDEX idx_ta_r_document_evenement_id_devis
  ON ta_r_document_evenement
  USING btree
  (id_devis);

CREATE INDEX idx_ta_r_document_evenement_id_boncde
  ON ta_r_document_evenement
  USING btree
  (id_boncde);

CREATE INDEX idx_ta_r_document_evenement_id_bonliv
  ON ta_r_document_evenement
  USING btree
  (id_bonliv);

CREATE INDEX idx_ta_r_document_evenement_id_avoir
  ON ta_r_document_evenement
  USING btree
  (id_avoir);

CREATE INDEX idx_ta_r_document_evenement_id_reglement
  ON ta_r_document_evenement
  USING btree
  (id_reglement);

CREATE TRIGGER tbi_r_document
  BEFORE INSERT
  ON ta_r_document_evenement
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

CREATE TRIGGER tbu_r_document
  BEFORE UPDATE
  ON ta_r_document_evenement
  FOR EACH ROW
  EXECUTE PROCEDURE before_update();
-------------------------------------------------------------------------------------------------------------------
CREATE TABLE ta_r_tiers_notification
(
  id serial NOT NULL,
  id_notification did_facultatif NOT NULL,
  id_tiers did_facultatif NOT NULL,
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_r_tiers_notification_pkey PRIMARY KEY (id),
  CONSTRAINT tta_r_tiers_notification_id_notification_fkey FOREIGN KEY (id_notification)
      REFERENCES ta_notification (id_notification) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_r_tiers_notification_id_tiers_fkey FOREIGN KEY (id_tiers)
      REFERENCES ta_tiers (id_tiers) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_r_tiers_notification
  OWNER TO postgres;

CREATE INDEX idx_ta_r_tiers_notification_id_notification
  ON ta_r_tiers_notification
  USING btree
  (id_notification);

CREATE INDEX idx_ta_r_tiers_notification_id_tiers
  ON ta_r_tiers_notification
  USING btree
  (id_tiers);

CREATE TRIGGER tbi_ta_r_tiers_notification
  BEFORE INSERT
  ON ta_r_tiers_notification
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

CREATE TRIGGER tbu_ta_r_tiers_notification
  BEFORE UPDATE
  ON ta_r_tiers_notification
  FOR EACH ROW
  EXECUTE PROCEDURE before_update();

-------------------------------------------------------------------------------------------------------------------

CREATE TABLE ta_r_utilisateur_notification
(
  id serial NOT NULL,
  id_notification did_facultatif NOT NULL,
  id_utilisateur did_facultatif NOT NULL,
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_r_utilisateur_notification_pkey PRIMARY KEY (id),
  CONSTRAINT ta_r_utilisateur_notification_id_notification_fkey FOREIGN KEY (id_notification)
      REFERENCES ta_notification (id_notification) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_r_utilisateur_notification_id_utilisateur_fkey FOREIGN KEY (id_utilisateur)
      REFERENCES ta_utilisateur (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_r_utilisateur_notification
  OWNER TO postgres;

CREATE INDEX idx_ta_r_utilisateur_notification_id_notification
  ON ta_r_utilisateur_notification
  USING btree
  (id_notification);

CREATE INDEX idx_ta_r_utilisateur_notification_id_utilisateur
  ON ta_r_utilisateur_notification
  USING btree
  (id_utilisateur);

CREATE TRIGGER tbi_ta_r_utilisateur_notification
  BEFORE INSERT
  ON ta_r_utilisateur_notification
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

CREATE TRIGGER tbu_ta_r_utilisateur_notification
  BEFORE UPDATE
  ON ta_r_utilisateur_notification
  FOR EACH ROW
  EXECUTE PROCEDURE before_update();
------------------------------------------------------------------------------------------------------------------
  
--
ALTER TABLE ta_notification ADD COLUMN qui_cree dlib50;
ALTER TABLE ta_evenement ADD COLUMN qui_cree dlib50;
ALTER TABLE ta_recurrence_evenement ADD COLUMN qui_cree dlib50;
ALTER TABLE ta_type_evenement ADD COLUMN qui_cree dlib50;
ALTER TABLE ta_type_notification ADD COLUMN qui_cree dlib50;
ALTER TABLE ta_categorie_evenement ADD COLUMN qui_cree dlib50;
ALTER TABLE ta_agenda ADD COLUMN qui_cree dlib50;

------------------------------------------------------------------------------------------------------------------
