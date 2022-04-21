--set schema 'demo';

-------------------------------------------------------
-- DEBUT INTEGRATION CLIENT OAUTH (GOOGLE,MICROSOFT)
-------------------------------------------------------
CREATE TABLE ta_oauth_service_client (
  id serial NOT NULL,
  code dlgr_code,
  libelle dlib255,
  description TEXT,
  url dlib255,
  urlEndPoint dlib255,
  actif boolean DEFAULT true,
  systeme boolean DEFAULT false,
  quand_cree date_heure_lgr,
  quand_modif date_heure_lgr,
  qui_cree integer,
  qui_modif integer,
  ip_acces dlib50,
  version_obj did_version_obj,

  CONSTRAINT ta_oauth_service_client_pkey PRIMARY KEY (id),
  CONSTRAINT ta_oauth_service_client_code_unique UNIQUE (code)
);

CREATE TABLE ta_oauth_token_client (
  id serial NOT NULL,
  key TEXT,
  access_token TEXT,
  refresh_token TEXT,
  scope TEXT,
  expires_in bigint,
  expires_at bigint,
  expires_at_date date_heure_lgr,
  token_type dlib255,
  date_creation date_heure_lgr,
  date_mise_a_jour date_heure_lgr,
  id_utilisateur_sur_service dlib255,
  email_utilisateur_sur_service dlib255,
  id_utilisateur did_facultatif,
  id_ta_oauth_service_client did_facultatif,
  quand_cree date_heure_lgr,
  quand_modif date_heure_lgr,
  qui_cree integer,
  qui_modif integer,
  ip_acces dlib50,
  version_obj did_version_obj,

  CONSTRAINT ta_oauth_token_client_pkey PRIMARY KEY (id),
  CONSTRAINT fk_ta_oauth_token_client_1 FOREIGN KEY (id_utilisateur)
      REFERENCES ta_utilisateur (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_oauth_token_client_2 FOREIGN KEY (id_ta_oauth_service_client)
      REFERENCES ta_oauth_service_client (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

INSERT INTO ta_oauth_service_client (code, libelle, description, url, urlendpoint, actif, systeme, version_obj) VALUES('GOOGLE', 'Google', 'Google', NULL, NULL, true, true, 0);
INSERT INTO ta_oauth_service_client (code, libelle, description, url, urlendpoint, actif, systeme, version_obj) VALUES('MS', 'MICROSOFT', 'Microsoft', NULL, NULL, true, true, 0);

create table ta_oauth_scope_client(
  id serial NOT NULL,
  code dlgr_code,
  libelle dlib255,
  description text,
  id_oauth_service_client did_facultatif,
  identifiant_service text,
  actif boolean DEFAULT true,
  systeme boolean DEFAULT false,
  quand_cree date_heure_lgr,
  quand_modif date_heure_lgr,
  qui_cree integer,
  qui_modif integer,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_oauth_scope_client_pkey PRIMARY KEY (id),
  CONSTRAINT fk_ta_oauth_scope_client_1 FOREIGN KEY (id_oauth_service_client)
      REFERENCES ta_oauth_service_client (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

INSERT INTO ta_oauth_scope_client (code, libelle, description, id_oauth_service_client, identifiant_service, actif, systeme) VALUES ('MS_PROFIL', 'Information du profil', null, 2, null, true, false);
INSERT INTO ta_oauth_scope_client (code, libelle, description, id_oauth_service_client, identifiant_service, actif, systeme) VALUES ('MS_CALENDAR', 'Calendrier', null, 2, null, true, false);
INSERT INTO ta_oauth_scope_client (code, libelle, description, id_oauth_service_client, identifiant_service, actif, systeme) VALUES ('GOOGLE_PROFIL', 'Information du profil', null, 1, null, true, false);
INSERT INTO ta_oauth_scope_client (code, libelle, description, id_oauth_service_client, identifiant_service, actif, systeme) VALUES ('GOOGLE_CALENDAR', 'Calendrier', null, 1, null, true, false);
INSERT INTO ta_oauth_scope_client (code, libelle, description, id_oauth_service_client, identifiant_service, actif, systeme) VALUES ('GOOGLE_DRIVE', 'Drive', null, 1, null, true, false);
-------------------------------------------------------
-- FIN INTEGRATION CLIENT OAUTH (GOOGLE,MICROSOFT)
-------------------------------------------------------
ALTER TABLE ta_r_prix
  ADD CONSTRAINT fk_ta_t_tarif FOREIGN KEY (id_t_tarif) REFERENCES ta_t_tarif (id_t_tarif)
   ON UPDATE NO ACTION ON DELETE NO ACTION;

   CREATE INDEX fki_ta_t_tarif
  ON ta_r_prix(id_t_tarif); 
  