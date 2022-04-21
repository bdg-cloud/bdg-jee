
CREATE TABLE ta_stripe_paiement_prevu (
  id_stripe_paiement_prevu serial NOT NULL,
  
  id_stripe_charge did_facultatif,
  id_stripe_customer did_facultatif,
  id_stripe_source did_facultatif,
  id_stripe_subscription did_facultatif,
  id_stripe_invoice did_facultatif,
  
  date_declenchement date_heure_lgr,
  montant did9facult,
  timer_handle bytea,
  raison_paiement dlib255,
  commentaire text,
  capture boolean default 'true',
  
  annule boolean default 'false',
  raison_annulation text,
  
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_stripe_paiement_prevu_pkey PRIMARY KEY (id_stripe_paiement_prevu)
  --,
  --CONSTRAINT fk_ta_stripe_card_id_carte_bancaire FOREIGN KEY (id_carte_bancaire)
  --    REFERENCES ta_carte_bancaire (id_carte_bancaire) MATCH SIMPLE
  --    ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE INDEX ta_stripe_paiement_prevu_id
  ON ta_stripe_paiement_prevu
  USING btree
  (id_stripe_paiement_prevu);

CREATE TRIGGER tbi_stripe_paiement_prevu
  BEFORE INSERT
  ON ta_stripe_paiement_prevu
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

CREATE TRIGGER tbu_stripe_paiement_prevu
  BEFORE UPDATE
  ON ta_stripe_paiement_prevu
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

-------------------------------------------------------

ALTER TABLE ta_l_echeance ADD COLUMN id_stripe_subscription did_facultatif;
ALTER TABLE ta_l_echeance ADD COLUMN date_echeance date_heure_lgr;

-------------------------------------------------------------------------------

-------------------------------------------------------------------------------------------
CREATE TABLE ta_t_moyen_paiement (
  id_t_moyen_paiement serial NOT NULL,
  
  code_t_moyen_paiement dlgr_code,
  libl_t_moyen_paiement dlgr_lib,
  systeme boolean DEFAULT false,
  
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_t_moyen_paiement_pkey PRIMARY KEY (id_t_moyen_paiement)
);

CREATE INDEX ta_t_moyen_paiement_id_t_moyen_paiement
  ON ta_t_moyen_paiement
  USING btree
  (id_t_moyen_paiement);

CREATE TRIGGER tbi_t_moyen_paiement
  BEFORE INSERT
  ON ta_t_moyen_paiement
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

CREATE TRIGGER tbu_t_moyen_paiement
  BEFORE UPDATE
  ON ta_t_moyen_paiement
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();
  
INSERT INTO ta_t_moyen_paiement (code_t_moyen_paiement, libl_t_moyen_paiement, systeme, version_obj) VALUES ('C', 'Chèque', true, 0);
INSERT INTO ta_t_moyen_paiement (code_t_moyen_paiement, libl_t_moyen_paiement, systeme, version_obj) VALUES ('E', 'Espèce', true, 0);
INSERT INTO ta_t_moyen_paiement (code_t_moyen_paiement, libl_t_moyen_paiement, systeme, version_obj) VALUES ('CB', 'CB', true, 0);
INSERT INTO ta_t_moyen_paiement (code_t_moyen_paiement, libl_t_moyen_paiement, systeme, version_obj) VALUES ('PREL', 'Prélèvement', true, 0);
INSERT INTO ta_t_moyen_paiement (code_t_moyen_paiement, libl_t_moyen_paiement, systeme, version_obj) VALUES ('LCR', 'Traite', true, 0);

ALTER TABLE ta_t_paiement ADD COLUMN id_t_moyen_paiement did_facultatif;

update ta_t_paiement tp set id_t_moyen_paiement = (select tmp.id_t_moyen_paiement from ta_t_moyen_paiement tmp where tmp.code_t_moyen_paiement = tp.code_t_paiement);

CREATE TABLE ta_mandat_prelevement (
  id_mandat_prelevement serial NOT NULL,
  id_compte_banque did_facultatif,
  titre dlib255,
  reference_unique_mandat_rum dlib255,
  identifiant_creancier_sepa_ics dlib255,
  validation_par_client_debiteur boolean default false,
  nom_creancier dlib255,
  adresse1_creancier dlib100,
  adresse2_creancier dlib100,
  adresse3_creancier dlib100,
  codepostal_creancier dcodpos,
  ville_creancier dlib100,
  pays_creancier dlib100,
  
  texte_mandat text,
  
  fichier bytea,
  type_mime_fichier dlib50,
  
  
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_mandat_prelevement_pkey PRIMARY KEY (id_mandat_prelevement)
);

CREATE INDEX ta_mandat_prelevement_id_mandat_prelevement
  ON ta_mandat_prelevement
  USING btree
  (id_mandat_prelevement);

CREATE TRIGGER tbi_mandat_prelevement
  BEFORE INSERT
  ON ta_mandat_prelevement
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

CREATE TRIGGER tbu_mandat_prelevement
  BEFORE UPDATE
  ON ta_mandat_prelevement
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

ALTER TABLE ta_compte_banque ADD COLUMN id_mandat_prelevement did_facultatif;

ALTER TABLE ta_l_echeance ALTER COLUMN id_abonnement DROP NOT NULL;

CREATE TABLE ta_cron (
  id_cron serial NOT NULL,
 
  systeme boolean default false,
  visible boolean default true,
  actif boolean default true,
  est_unique boolean default false,
  schema_tenant dlib100,
  code dlib100,
  libelle dlib100,
  description dlib255,
  type_cron dlib255,
  identifiant_unique dlib100,  
  timer_handle bytea,
  
  second dlib100,
  minute dlib100,
  hour dlib100,
  day_of_month dlib100,
  month dlib100,
  day_of_week dlib100,
  year dlib100,
  
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_cron_pkey PRIMARY KEY (id_cron)
);

CREATE INDEX ta_cron_id_cron
  ON ta_cron
  USING btree
  (id_cron);

--CREATE TRIGGER tbi_cron
--  BEFORE INSERT
--  ON ta_cron
--  FOR EACH ROW
--  EXECUTE PROCEDURE before_insert();

CREATE TRIGGER tbu_cron
  BEFORE UPDATE
  ON ta_cron
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();
  
 CREATE OR REPLACE FUNCTION before_insert_cron()
  RETURNS trigger AS
$BODY$
begin
 
   NEW.qui_cree := user;
   NEW.quand_cree := now();
   NEW.qui_modif := user;
   NEW.quand_modif := now();
   NEW.ip_acces := inet_client_addr();
   NEW.schema_tenant := TG_TABLE_SCHEMA;
   select num_version from ta_version into NEW.version;  
   RETURN NEW;
end $BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION before_insert()
  OWNER TO bdg;
  
CREATE TRIGGER tbi_cron
  BEFORE INSERT
  ON ta_cron
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert_cron();
  
INSERT INTO ta_cron (systeme, visible, actif, est_unique, schema_tenant, code, libelle, description, type_cron, identifiant_unique, 
second, minute, hour, day_of_month, month, day_of_week, year, version_obj) VALUES ( 
true, DEFAULT, true, true, null, 'CRON_SYS_CREATION_AVIS_ECHEANCE_ET_PROGRAMMATION_PAIEMENT', 
null, 'creation des avis d''échéances et programmation des paiements', null, null, 
'0', '0', '2', null, '*', null, '*', 0);
  
-------------------------------------------------------
-- FIN INTEGRATION MODELE STRIPE POUR ABONNEMENT
-------------------------------------------------------

-------------------------------------------------------
-- DEBUT INTEGRATION AVIS ECHEANCE
-------------------------------------------------------
INSERT INTO ta_gen_code_ex(
             code_gen_code, cle_gen_code,valeur_gen_code,version_obj)
    VALUES ( 'codeDocument', 'TaAvisEcheance','AE{@exo}{@num}',0);
    

ALTER TABLE ta_l_avis_echeance
  ADD COLUMN lib_tva_l_document character varying(255);
  
  
UPDATE ta_preferences SET libelle = 'Page break Totaux', id_categorie_preference = 8, id_groupe_preference = null,  position = null, position_dans_groupe = null WHERE groupe = 'avis_echeance' and cle = 'PageBreakTotaux';
UPDATE ta_preferences SET libelle = 'Page break Maxi',  id_categorie_preference = 8, id_groupe_preference = null,  position = null, position_dans_groupe = null WHERE groupe = 'avis_echeance' and cle = 'PageBreakMaxi';
UPDATE ta_preferences SET libelle = 'Impression direct', id_categorie_preference = 8, id_groupe_preference = null, position = null, position_dans_groupe = null WHERE groupe = 'avis_echeance' and cle = 'ImpressionDirect';
UPDATE ta_preferences SET libelle = 'Traite',  id_categorie_preference = 8, id_groupe_preference = null,  position = null, position_dans_groupe = null WHERE groupe = 'avis_echeance' and cle = 'Traite';
UPDATE ta_preferences SET libelle = '', id_categorie_preference = 8, id_groupe_preference = null,  position = null, position_dans_groupe = null WHERE groupe = 'avis_echeance' and cle = 'Commentaire';
UPDATE ta_preferences SET libelle = 'Coupure de ligne', id_categorie_preference = 8, id_groupe_preference = null,  position = null, position_dans_groupe = null WHERE groupe = 'avis_echeance' and cle = 'CoupureLigne';
UPDATE ta_preferences SET libelle = 'listeChoix',  id_categorie_preference = 8, id_groupe_preference = null,  position = null, position_dans_groupe = null WHERE groupe = 'avis_echeance' and cle = 'ParamChoix';

-------------------------------------------------------
-- FIN INTEGRATION AVIS ECHEANCE
-------------------------------------------------------    

---------------------------------------------------------------------------------------------------------------------------------------------

-------------------------------------------------------
-- DEBUT debugage
-------------------------------------------------------

INSERT INTO ta_categorie_preference( id_categorie_preference, libelle_categorie_preference,  "position")
    VALUES ( 37,'paiement_cb', 19);


INSERT INTO ta_preferences(groupe, cle, valeur,valeur_defaut,  type_donnees, libelle ,id_categorie_preference)
    VALUES ('paiement_cb', 'paiement_cb', '','CB', 'text','Type de Paiement par CB à utilser', 37);
-------------------------------------------------------
-- FIN debugage
-------------------------------------------------------

--------------------------------------------------------
-- INSERTION de type d'edition BONLIV
---------------------------------------------------------
    INSERT INTO ta_t_edition
(code_t_edition, libelle, description, systeme)
VALUES('BONLIV', 'Bon livraison', NULL, false);
--------------------------------------------------------
-- INSERTION d' action interne pour l'edition BONLIV
---------------------------------------------------------
INSERT INTO ta_action_interne
(id_action, libelle, description, systeme)
VALUES('fr.legrain.action.edition.bonliv.defaut', 'Edition d''un bon de livraison', NULL, false);
--------------------------------------------------------
-- INSERTION de la relation entre une action et une edition pour BONLIV
-- A FAIRE A LA MAIN AVEC LES ID OBTENUS !!!!!!!!!!------
---------------------------------------------------------
--INSERT INTO ta_r_action_edition
--( id_action, id_edition)
--VALUES( X, X);

