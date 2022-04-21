--set schema 'demo';

--penser a mettre a jour les sequences

CREATE TABLE ta_message_push
(
  id_message_push serial NOT NULL,
  id_utilisateur did_facultatif,
  sujet dlib255,
  resume dlib255,
  contenu text,
  url dlib255,
  url_image dlib255,
  date_creation date_heure_lgr,
  date_envoi date_heure_lgr,
  style dlib255,
  lu boolean,
  recu boolean,
  message_id dlib255,
  infos_service text,
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  type_service_externe dlib255,
  code_compte_service_web_externe dlib255,
  expedie boolean,
  etat_message_service_externe text,
  status_service_externe dlib255,
  CONSTRAINT ta_message_push_pkey PRIMARY KEY (id_message_push),
  CONSTRAINT fk_ta_message_push_id_utilisateur FOREIGN KEY (id_utilisateur)
      REFERENCES ta_utilisateur (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_message_push
  OWNER TO bdg;

-- Index: ta_message_push_id

-- DROP INDEX ta_message_push_id;

CREATE INDEX ta_message_push_id
  ON ta_message_push
  USING btree
  (id_message_push);


-- Trigger: tbi_message_push on ta_message_push

-- DROP TRIGGER tbi_message_push ON ta_message_push;

CREATE TRIGGER tbi_message_push
  BEFORE INSERT
  ON ta_message_push
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

CREATE TRIGGER tbu_message_push
  BEFORE UPDATE
  ON ta_message_push
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();




CREATE TABLE ta_contact_message_push
(
  id_contact_message_push serial NOT NULL,
  id_message_push did_facultatif NOT NULL,
  id_tiers did_facultatif,
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_contact_message_push_pkey PRIMARY KEY (id_contact_message_push),
  CONSTRAINT ta_contact_message_push_id_message_push_fkey FOREIGN KEY (id_message_push)
      REFERENCES ta_message_push (id_message_push) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_contact_message_push_id_tiers_fkey FOREIGN KEY (id_tiers)
      REFERENCES ta_tiers (id_tiers) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_contact_message_push
  OWNER TO bdg;

CREATE INDEX idx_ta_contact_message_push_id_message_push
  ON ta_contact_message_push
  USING btree
  (id_message_push);

CREATE INDEX idx_ta_contact_message_push_id_tiers
  ON ta_contact_message_push
  USING btree
  (id_tiers);

CREATE TRIGGER tbi_ta_contact_message_push
  BEFORE INSERT
  ON ta_contact_message_push
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

CREATE TRIGGER tbu_ta_contact_message_push
  BEFORE UPDATE
  ON ta_contact_message_push
  FOR EACH ROW
  EXECUTE PROCEDURE before_update();
  
  
  



