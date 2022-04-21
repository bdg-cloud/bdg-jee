--maj_tunnel_vente.sql

--set schema 'demo';

ALTER TABLE ta_l_panier 
ADD COLUMN code_liaison_ligne dlib100;



CREATE TABLE ta_liaison_dossier_maitre
(
  id_liaison_dossier_maitre serial NOT NULL,
  email dlib255nn,
  password dlib255nn,
  token  text,
  refresh_token  text,
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  ip_acces dlib50,
  version num_version,
  version_obj did_version_obj,
  CONSTRAINT ta_liaison_dossier_maitre_pkey PRIMARY KEY (id_liaison_dossier_maitre)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_liaison_dossier_maitre
  OWNER TO bdg;
  
CREATE TRIGGER tbi_ta_liaison_dossier_maitre
	BEFORE INSERT
	ON ta_liaison_dossier_maitre
	FOR EACH ROW
	EXECUTE PROCEDURE before_insert();


CREATE TRIGGER tbu_ta_liaison_dossier_maitre
  BEFORE UPDATE
  ON ta_liaison_dossier_maitre
  FOR EACH ROW
  EXECUTE PROCEDURE before_update();
  
--set schema 'demo';

ALTER TABLE ta_liaison_dossier_maitre 
ADD COLUMN code_tiers dlib100;
  



--set schema 'demo';

ALTER TABLE ta_article
ADD COLUMN code_module_bdg character varying(255);
ALTER TABLE ta_article 
ADD COLUMN libl_module_bdg dlib255;
