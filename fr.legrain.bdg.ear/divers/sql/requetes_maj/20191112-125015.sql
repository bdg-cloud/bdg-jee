--set schema 'demo';


CREATE TABLE ta_espace_client (
  id_espace_client serial NOT NULL,
  email dlib255nn,
  nom dlib100,
  prenom dlib100,
  password dlib255nn,
  actif boolean default false,
  id_tiers did_facultatif,
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_espace_client_pkey PRIMARY KEY (id_espace_client),
  CONSTRAINT fk_ta_espace_client_id_tiers FOREIGN KEY (id_tiers)
      REFERENCES ta_tiers (id_tiers) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_espace_client
  OWNER TO bdg;

CREATE INDEX idx_ta_espace_client_id_tiers
  ON ta_espace_client
  USING btree
  (id_tiers);
  
CREATE UNIQUE INDEX idx_ta_espace_client_email
  ON ta_espace_client
  USING btree
  (email);

CREATE TRIGGER tbi_ta_espace_client
  BEFORE INSERT
  ON ta_espace_client
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

CREATE TRIGGER tbu_ta_espace_client
  BEFORE UPDATE
  ON ta_espace_client
  FOR EACH ROW
  EXECUTE PROCEDURE before_update();
  
--------------------------------------------------------------------------------------------------------------------------------
CREATE TABLE ta_param_espace_client (
  id_param_espace_client serial NOT NULL,
  
  affiche_devis boolean default true,
  affiche_factures boolean default true,
  affiche_commandes boolean default true,
  affiche_livraisons boolean default true,
  affiche_avis_echeance boolean default true,
  paiement_cb boolean default true,
  espace_client_actif boolean default true,
  logo_login bytea,
  nom_image_logo_login dlib255,
  logo_pages_simples bytea,
  nom_image_logo_pages_simples dlib255,
  logo_header bytea,
  nom_image_logo_header dlib255,
  logo_footer bytea,
  nom_image_logo_footer dlib255,
  image_background_login bytea,
  nom_image_background_login dlib255,
  url_perso dlib255,
  texte_login_1 dlib255,
  texte_login_2 dlib255,
  texte_accueil dlib255,
  theme_defaut dlib255,
  
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_param_espace_client_pkey PRIMARY KEY (id_param_espace_client)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_param_espace_client
  OWNER TO bdg;

CREATE TRIGGER tbi_ta_param_espace_client
  BEFORE INSERT
  ON ta_param_espace_client
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

CREATE TRIGGER tbu_ta_param_espace_client
  BEFORE UPDATE
  ON ta_param_espace_client
  FOR EACH ROW
  EXECUTE PROCEDURE before_update();

ALTER TABLE ta_stripe_payment_intent ADD COLUMN id_avis_echeance did_facultatif;
