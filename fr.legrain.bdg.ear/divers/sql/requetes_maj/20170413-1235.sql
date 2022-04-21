ALTER TABLE ta_resultat_correction ADD COLUMN id_resultat_conformite_apres_action_corrective did_facultatif;


-- ajouter un booleen ctrl_bloquant sur les controles
ALTER TABLE ta_conformite ADD COLUMN ctrl_bloquant boolean;
ALTER TABLE ta_conformite ALTER COLUMN ctrl_bloquant SET DEFAULT false;
ALTER TABLE ta_modele_conformite ADD COLUMN ctrl_bloquant boolean;
ALTER TABLE ta_modele_conformite ALTER COLUMN ctrl_bloquant SET DEFAULT false;
-- ajouter un booleen ctrl_facultatif sur les controles
ALTER TABLE ta_conformite ADD COLUMN ctrl_facultatif boolean;
ALTER TABLE ta_conformite ALTER COLUMN ctrl_facultatif SET DEFAULT false;
ALTER TABLE ta_modele_conformite ADD COLUMN ctrl_facultatif boolean;
ALTER TABLE ta_modele_conformite ALTER COLUMN ctrl_facultatif SET DEFAULT false;

-- ajouter les champs pour gérérer la duplication et la modification des barèmes
ALTER TABLE ta_conformite ADD COLUMN id_modele_conformite_parent_avant_modif did_facultatif;

ALTER TABLE ta_conformite ADD CONSTRAINT fk_id_modele_conformite_parent_avant_modif FOREIGN KEY (id_modele_conformite_parent_avant_modif)
  REFERENCES ta_conformite (id_conformite) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;
  
ALTER TABLE ta_conformite ADD COLUMN version_ctrl did4;
ALTER TABLE ta_conformite ALTER COLUMN version_ctrl SET DEFAULT 0;
update ta_preferences SET cle = 'ParamChoix' where cle = 'ListeChoix';
update ta_gen_code_ex SET cle_gen_code = 'TaProforma' where cle_gen_code = 'Taproforma';

--***********************************************************************************************************************************
CREATE TABLE ta_status_conformite
(
  id_status_conformite serial NOT NULL,
  code_status_conformite dlib50nn,
  libelle_status_conformite dlib255,
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_status_conformite_pkey PRIMARY KEY (id_status_conformite),
  CONSTRAINT ta_status_conformite_code_unique UNIQUE (code_status_conformite)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_status_conformite
  OWNER TO postgres;

-- Index: ta_status_conformite_code

-- DROP INDEX ta_status_conformite_code;

CREATE INDEX ta_status_conformite_code
  ON ta_status_conformite
  USING btree
  (code_status_conformite COLLATE pg_catalog."default");

-- Index: ta_status_conformite_id_status_conformite

-- DROP INDEX ta_status_conformite_id_status_conformite;

CREATE INDEX ta_status_conformite_id_status_conformite
  ON ta_status_conformite
  USING btree
  (id_status_conformite);

-- Index: unq1_ta_status_conformite

-- DROP INDEX unq1_ta_status_conformite;

CREATE INDEX unq1_ta_status_conformite
  ON ta_status_conformite
  USING btree
  (code_status_conformite COLLATE pg_catalog."default");


-- Trigger: tbi_status_conformite on ta_status_conformite

-- DROP TRIGGER tbi_status_conformite ON ta_status_conformite;

CREATE TRIGGER tbi_status_conformite
  BEFORE INSERT
  ON ta_status_conformite
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

-- Trigger: tbu_status_conformite on ta_status_conformite

-- DROP TRIGGER tbu_status_conformite ON ta_status_conformite;

CREATE TRIGGER tbu_status_conformite
  BEFORE UPDATE
  ON ta_status_conformite
  FOR EACH ROW
  EXECUTE PROCEDURE before_update();

INSERT INTO ta_status_conformite (code_status_conformite, libelle_status_conformite, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj) VALUES ( 'ok', 'valide', 'postgres', '2017-04-12 14:47:44.614087', 'postgres', '2017-04-12 14:48:08.488281', '2.0.13', '127.0.0.1/32', 0);
INSERT INTO ta_status_conformite ( code_status_conformite, libelle_status_conformite, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj) VALUES ( 'corrige', 'correction effectuée avec succés', 'postgres', '2017-04-12 14:48:55.405113', 'postgres', '2017-04-12 14:49:23.262026', '2.0.13', '127.0.0.1/32', 0);
INSERT INTO ta_status_conformite ( code_status_conformite, libelle_status_conformite, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj) VALUES ( 'acorriger', 'correction à faire (invalide)', 'postgres', '2017-04-12 14:47:57.614330', 'postgres', '2017-04-12 14:49:32.236256', '2.0.13', '127.0.0.1/32', 0);
INSERT INTO ta_status_conformite ( code_status_conformite, libelle_status_conformite, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj) VALUES ( 'noncorrige', 'correction effectuée sans succés => encore invalide', 'postgres', '2017-04-12 14:48:34.541483', 'postgres', '2017-04-12 14:49:40.915939', '2.0.13', '127.0.0.1/32', 0);
INSERT INTO ta_status_conformite ( code_status_conformite, libelle_status_conformite, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj) VALUES ( 'vide', 'pas de valeur', 'postgres', '2017-04-12 14:48:37.965472', 'postgres', '2017-04-12 14:49:44.803937', '2.0.13', '127.0.0.1/32', 0);

----********************************************
ALTER TABLE ta_resultat_conformite ADD COLUMN id_status_conformite did_facultatif;
ALTER TABLE ta_resultat_conformite ADD CONSTRAINT fk_ta_resultat_conformite_id_status_conformite FOREIGN KEY (id_status_conformite) REFERENCES ta_status_conformite (id_status_conformite) ON UPDATE NO ACTION ON DELETE NO ACTION;
CREATE INDEX fki_ta_resultat_conformite_id_status_conformite ON ta_resultat_conformite(id_status_conformite);




