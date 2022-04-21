  CREATE TABLE ta_action_interne
(
  id serial NOT NULL,
  id_action dlib255,
  libelle dlib255,
  description dlib255,
  systeme boolean,
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_action_interne_pkey PRIMARY KEY (id),
  CONSTRAINT ta_action_interne_id_action UNIQUE (id_action)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_action_interne
  OWNER TO postgres;

CREATE TRIGGER tbi_ta_action_interne
  BEFORE INSERT
  ON ta_action_interne
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

CREATE TRIGGER tbu_ta_action_interne
  BEFORE UPDATE
  ON ta_action_interne
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

CREATE TABLE ta_r_action_edition
(
  id serial NOT NULL,
  id_action did_facultatif NOT NULL,
  id_edition did_facultatif NOT NULL,
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_r_action_edition_pkey PRIMARY KEY (id),
  CONSTRAINT fk_ta_r_action_edition_1 FOREIGN KEY (id_action)
      REFERENCES ta_action_interne (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_r_action_edition_2 FOREIGN KEY (id_edition)
      REFERENCES ta_edition_catalogue (id_edition_catalogue) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_r_action_edition
  OWNER TO postgres;

CREATE INDEX fk_ta_r_action_edition_1
  ON ta_r_action_edition
  USING btree
  (id_action);

CREATE INDEX fk_ta_r_action_edition_2
  ON ta_r_action_edition
  USING btree
  (id_edition);

CREATE INDEX ta_r_action_edition_id
  ON ta_r_action_edition
  USING btree
  (id);

CREATE TRIGGER tbi_ta_r_action_edition
  BEFORE INSERT
  ON ta_r_action_edition
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

CREATE TRIGGER tbu_ta_r_action_edition
  BEFORE UPDATE
  ON ta_r_action_edition
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();
  
  
  