ALTER TABLE ta_tiers ADD COLUMN utilise_compte_client boolean;

ALTER TABLE ta_facture ADD COLUMN date_mise_a_dispo_cptclient timestamp with time zone;
ALTER TABLE ta_facture ADD COLUMN date_export timestamp with time zone;

ALTER TABLE ta_apporteur ADD COLUMN date_mise_a_dispo_cptclient timestamp with time zone;
ALTER TABLE ta_apporteur ADD COLUMN date_export timestamp with time zone;

ALTER TABLE ta_acompte ADD COLUMN date_mise_a_dispo_cptclient timestamp with time zone;
ALTER TABLE ta_acompte ADD COLUMN date_export timestamp with time zone;

ALTER TABLE ta_avoir ADD COLUMN date_mise_a_dispo_cptclient timestamp with time zone;
ALTER TABLE ta_avoir ADD COLUMN date_export timestamp with time zone;

ALTER TABLE ta_boncde ADD COLUMN date_mise_a_dispo_cptclient timestamp with time zone;
ALTER TABLE ta_boncde ADD COLUMN date_export timestamp with time zone;

ALTER TABLE ta_devis ADD COLUMN date_mise_a_dispo_cptclient timestamp with time zone;
ALTER TABLE ta_devis ADD COLUMN date_export timestamp with time zone;

ALTER TABLE ta_prelevement ADD COLUMN date_mise_a_dispo_cptclient timestamp with time zone;
ALTER TABLE ta_prelevement ADD COLUMN date_export timestamp with time zone;

ALTER TABLE ta_proforma ADD COLUMN date_mise_a_dispo_cptclient timestamp with time zone;
ALTER TABLE ta_proforma ADD COLUMN date_export timestamp with time zone;

ALTER TABLE ta_reglement ADD COLUMN date_mise_a_dispo_cptclient timestamp with time zone;
ALTER TABLE ta_reglement ADD COLUMN date_export timestamp with time zone;

ALTER TABLE ta_remise ADD COLUMN date_mise_a_dispo_cptclient timestamp with time zone;
ALTER TABLE ta_remise ADD COLUMN date_export timestamp with time zone;



ALTER TABLE ta_bon_reception ADD COLUMN heure_document time with time zone;
ALTER TABLE ta_tiers ADD COLUMN logo bytea;
ALTER TABLE ta_compl ADD COLUMN num_agrement_sanitaire dlib20;

ALTER TABLE ta_fabrication ADD COLUMN id_tiers_prestation_service did_facultatif;
ALTER TABLE ta_fabrication ADD CONSTRAINT fk_id_tiers_prest_serv FOREIGN KEY (id_tiers_prestation_service) REFERENCES ta_tiers (id_tiers) ON UPDATE NO ACTION ON DELETE NO ACTION;
CREATE INDEX fki_id_tiers_prest_serv ON ta_fabrication(id_tiers_prestation_service);

ALTER TABLE ta_fabrication ADD COLUMN id_utilisateur_redacteur did_facultatif;
ALTER TABLE ta_fabrication ADD COLUMN id_utilisateur_controleur did_facultatif;

ALTER TABLE ta_fabrication ADD CONSTRAINT fk_ta_fabrication_id_utilisateur_redacteur FOREIGN KEY (id_utilisateur_redacteur) REFERENCES ta_utilisateur (id) ON UPDATE NO ACTION ON DELETE NO ACTION;
CREATE INDEX fki_ta_fabrication_id_utilisateur_redacteur ON ta_fabrication(id_utilisateur_redacteur);

ALTER TABLE ta_fabrication ADD CONSTRAINT fk_ta_fabrication_id_utilisateur_controleur FOREIGN KEY (id_utilisateur_controleur) REFERENCES ta_utilisateur (id) ON UPDATE NO ACTION ON DELETE NO ACTION;
CREATE INDEX fki_ta_fabrication_id_utilisateur_controleur ON ta_fabrication(id_utilisateur_controleur);


ALTER TABLE ta_modele_fabrication ADD COLUMN id_tiers_prestation_service did_facultatif;
ALTER TABLE ta_modele_fabrication ADD CONSTRAINT fk_ta_modele_fabrication_id_tiers_prest_serv FOREIGN KEY (id_tiers_prestation_service) REFERENCES ta_tiers (id_tiers) ON UPDATE NO ACTION ON DELETE NO ACTION;
CREATE INDEX fki_ta_modele_fabrication_id_tiers_prest_serv ON ta_modele_fabrication(id_tiers_prestation_service);

ALTER TABLE ta_resultat_conformite ADD COLUMN id_utilisateur_controleur did_facultatif;
ALTER TABLE ta_resultat_conformite ADD CONSTRAINT fk_ta_resultat_conformite_controleur FOREIGN KEY (id_utilisateur_controleur) REFERENCES ta_utilisateur (id) ON UPDATE NO ACTION ON DELETE NO ACTION;
CREATE INDEX fki_ta_resultat_conformite_controleur ON ta_resultat_conformite(id_utilisateur_controleur);

ALTER TABLE ta_resultat_correction ADD COLUMN id_utilisateur_controleur did_facultatif;
ALTER TABLE ta_resultat_correction ADD CONSTRAINT fk_ta_resultat_correction_controleur FOREIGN KEY (id_utilisateur_controleur) REFERENCES ta_utilisateur (id) ON UPDATE NO ACTION ON DELETE NO ACTION;
CREATE INDEX fki_ta_resultat_correction_controleur ON ta_resultat_correction(id_utilisateur_controleur);

ALTER TABLE ta_resultat_correction ADD COLUMN id_utilisateur_service_qualite did_facultatif;
ALTER TABLE ta_resultat_correction ADD CONSTRAINT fk_ta_resultat_correction_service_qualite FOREIGN KEY (id_utilisateur_service_qualite) REFERENCES ta_utilisateur (id) ON UPDATE NO ACTION ON DELETE NO ACTION;
CREATE INDEX fki_ta_resultat_correction_service_qualite ON ta_resultat_correction(id_utilisateur_service_qualite);

ALTER TABLE ta_resultat_correction ADD COLUMN id_utilisateur_direction did_facultatif;
ALTER TABLE ta_resultat_correction ADD CONSTRAINT fk_ta_resultat_correction_direction FOREIGN KEY (id_utilisateur_direction) REFERENCES ta_utilisateur (id) ON UPDATE NO ACTION ON DELETE NO ACTION;
CREATE INDEX fki_ta_resultat_correction_direction ON ta_resultat_correction(id_utilisateur_direction);

ALTER TABLE ta_resultat_correction ADD COLUMN action_immediate text;

ALTER TABLE ta_resultat_correction ADD COLUMN date_correction date_heure_lgr;
ALTER TABLE ta_resultat_conformite ADD COLUMN date_controle date_heure_lgr;

ALTER TABLE ta_lot ADD COLUMN lot_conforme boolean NOT NULL DEFAULT false;
ALTER TABLE ta_lot ADD COLUMN presence_de_non_conformite boolean NOT NULL DEFAULT false;
ALTER TABLE ta_lot ADD COLUMN id_tiers_prestation_de_service did_facultatif;
ALTER TABLE ta_lot ADD CONSTRAINT fk_lot_id_tiers_prestation_service FOREIGN KEY (id_tiers_prestation_de_service) REFERENCES ta_tiers (id_tiers) ON UPDATE NO ACTION ON DELETE NO ACTION;
CREATE INDEX fki_lot_id_tiers_prestation_service ON ta_lot(id_tiers_prestation_de_service);

--------------------------------
CREATE TABLE ta_r_famille_article (
  id serial NOT NULL,
  id_famille did_facultatif NOT NULL,
  id_article did_facultatif NOT NULL,
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_r_famille_article_pkey PRIMARY KEY (id),
  CONSTRAINT ta_r_famille_article_id_famille_fkey FOREIGN KEY (id_famille) REFERENCES ta_famille (id_famille) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_r_famille_article_id_article_fkey FOREIGN KEY (id_article) REFERENCES ta_article (id_article) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
);

ALTER TABLE ta_bon_reception ADD COLUMN id_tiers_prestation_service did_facultatif;
ALTER TABLE ta_bon_reception ADD CONSTRAINT fk_ta_bon_reception_id_tiers_prest_serv FOREIGN KEY (id_tiers_prestation_service) REFERENCES ta_tiers (id_tiers) ON UPDATE NO ACTION ON DELETE NO ACTION;
CREATE INDEX fki_ta_bon_reception_id_tiers_prest_serv ON ta_bon_reception(id_tiers_prestation_service);


ALTER TABLE ta_resultat_correction ADD COLUMN id_utilisateur_force_validation did_facultatif;
ALTER TABLE ta_resultat_correction ADD CONSTRAINT fk_ta_resultat_correction_force_validation FOREIGN KEY (id_utilisateur_force_validation) REFERENCES ta_utilisateur (id) ON UPDATE NO ACTION ON DELETE NO ACTION;
CREATE INDEX fki_ta_resultat_correction_force_validation ON ta_resultat_correction(id_utilisateur_force_validation);

ALTER TABLE ta_resultat_correction ADD COLUMN validation_forcee boolean NOT NULL DEFAULT false;

ALTER TABLE ta_resultat_correction ADD COLUMN date_visa_service_qualite date_heure_lgr;
ALTER TABLE ta_resultat_correction ADD COLUMN date_visa_direction date_heure_lgr;
ALTER TABLE ta_resultat_correction ADD COLUMN date_force_validation date_heure_lgr;
--------------------------------
CREATE TABLE ta_coefficient_unite (
  id serial NOT NULL,
  id_unite_a did_facultatif NOT NULL,
  id_unite_b did_facultatif NOT NULL,
  coeff_a_vers_b did9facult,
  coeff_b_vers_a did9facult,
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_coefficient_unite_pkey PRIMARY KEY (id),
  CONSTRAINT ta_coefficient_unite_id_unite_a_fkey FOREIGN KEY (id_unite_a)
      REFERENCES ta_unite (id_unite) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_coefficient_unite_id_unite_b_fkey FOREIGN KEY (id_unite_b)
      REFERENCES ta_unite (id_unite) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_coeff_unite_unicite UNIQUE (id_unite_a, id_unite_b)
);

ALTER TABLE ta_coefficient_unite ADD COLUMN nb_decimale_a_vers_b integer;
ALTER TABLE ta_coefficient_unite ADD COLUMN nb_decimale_b_vers_a integer;
ALTER TABLE ta_article ADD COLUMN id_unite_reference did_facultatif;
ALTER TABLE ta_article ADD CONSTRAINT ta_article_id_unite_reference FOREIGN KEY (id_unite_reference) REFERENCES ta_unite (id_unite) ON UPDATE NO ACTION ON DELETE NO ACTION;
CREATE INDEX fki_ta_article_id_unite_reference ON ta_article(id_unite_reference);
  
  
