--maj_base_prog.sql

----------- A APPLIQUER SUR BASE PROGRAMME-------------

CREATE TABLE ta_parametre
(
  id_parametre serial NOT NULL,
  dossier_maitre dlib100,
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  ip_acces dlib50,
  version num_version,
  version_obj did_version_obj,
  CONSTRAINT ta_parametre_pkey PRIMARY KEY (id_parametre)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_parametre
  OWNER TO bdg;
  
INSERT INTO ta_parametre(dossier_maitre) values ('legrain82');