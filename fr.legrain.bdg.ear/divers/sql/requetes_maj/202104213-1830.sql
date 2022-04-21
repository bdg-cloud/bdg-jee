
--set schema 'demo';

CREATE TABLE ta_utilisateur_webservice
(
  id serial NOT NULL,
  login dlib255nn,
  passwd dlib255,
  description text,
  email dlib255,
  dernier_acces date_heure_lgr,
  access_token text,
  refresh_token text,
  autorisations text,
  actif boolean DEFAULT true,
  systeme boolean DEFAULT false,
 
  quand_cree date_heure_lgr,
  quand_modif date_heure_lgr,
  qui_cree dlib50,
  qui_modif dlib50,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
 
  CONSTRAINT ta_utilisateur_webservice2_pkey PRIMARY KEY (id),
  CONSTRAINT ta_utilisateur_webservice_code_login UNIQUE (login)
)
WITH (
  OIDS=FALSE
);

ALTER TABLE ta_utilisateur ADD COLUMN  id_utilisateur_webservice did_facultatif;
alter table ta_utilisateur add CONSTRAINT fk_ta_utilisateur_id_utilisateur_webservice FOREIGN KEY (id_utilisateur_webservice)
      REFERENCES ta_utilisateur_webservice (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

CREATE TABLE ta_log_dossier
(
  id serial NOT NULL,
  quand date_heure_lgr,
  id_utilisateur_dossier did_facultatif,
  id_utilisateur_webservice did_facultatif,
  niveau_log did_facultatif, --trace, info, warning, erreur, ...
  type_log did_facultatif,--CRUD,login, logout
  message text,
  trace text,
  entite dlib255,
  service dlib255,
  id_entite did_facultatif,
  code_entite dlib255,
  cron_dossier boolean DEFAULT false,
  cron_systeme boolean DEFAULT false,
  ip dlib50,
  url_api text,
  
  --actif boolean DEFAULT true,
  --systeme boolean DEFAULT false,
 
  quand_cree date_heure_lgr,
  quand_modif date_heure_lgr,
  qui_cree dlib50,
  qui_modif dlib50,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
 
  CONSTRAINT ta_utilisateur_webservice_pkey PRIMARY KEY (id),
  CONSTRAINT fk_ta_utilisateur_webservice_id_utilisateur_dossier FOREIGN KEY (id_utilisateur_dossier)
      REFERENCES ta_utilisateur (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_utilisateur_webservice_id_utilisateur_webservice FOREIGN KEY (id_utilisateur_webservice)
      REFERENCES ta_utilisateur_webservice (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);

ALTER TABLE ta_log_dossier ADD COLUMN utilisateur dlib255;
ALTER TABLE ta_log_dossier ADD COLUMN dossier dlib255;
ALTER TABLE ta_log_dossier ADD COLUMN appel_distant boolean default false;
ALTER TABLE ta_log_dossier ADD COLUMN source text;
ALTER TABLE ta_log_dossier ADD COLUMN origine text;
ALTER TABLE ta_log_dossier ADD COLUMN ip_distante dlib255;
ALTER TABLE ta_log_dossier ADD COLUMN version_api dlib255;
ALTER TABLE ta_log_dossier ADD COLUMN parametre_entete_api text;
ALTER TABLE ta_log_dossier ADD COLUMN parametre_requete_api text;
ALTER TABLE ta_log_dossier ADD COLUMN corps_requete_api text;
ALTER TABLE ta_log_dossier ADD COLUMN corps_reponse_api text;
ALTER TABLE ta_log_dossier ADD COLUMN methode_http_api dlib255;
ALTER TABLE ta_log_dossier ADD COLUMN etat dlib255;
ALTER TABLE ta_log_dossier ADD COLUMN uuid dlib255;

