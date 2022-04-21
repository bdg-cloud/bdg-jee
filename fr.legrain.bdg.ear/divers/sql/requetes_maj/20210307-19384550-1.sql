
--set schema 'demo';

----- début maj liaison réglement avec tous les documents ---------

CREATE TABLE ta_r_reglement_liaison
(
  id serial NOT NULL,
  id_reglement did_facultatif NOT NULL,
  id_facture did_facultatif,
  id_boncde did_facultatif,	 
  id_boncde_achat  did_facultatif,	
  id_acompte did_facultatif,	
  id_avis_echeance did_facultatif,	
  id_abonnement did_facultatif,	
  id_bon_reception did_facultatif,	
  id_prelevement did_facultatif,	
  id_proforma did_facultatif,	
  id_preparation did_facultatif,	
  id_panier did_facultatif,	
  id_devis did_facultatif,	
  id_apporteur did_facultatif,	
  id_avoir did_facultatif,	
  id_bonliv did_facultatif,		
  id_ticket_caisse did_facultatif,	  
  affectation did9facult,
  export smallint DEFAULT 0,
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  etat bigint DEFAULT 0,
  date_export timestamp(0) without time zone,
  date_verrouillage timestamp with time zone,
  id_mise_a_disposition did_facultatif,
  CONSTRAINT ta_r_reglement_liaison_pkey PRIMARY KEY (id),
  CONSTRAINT fk_ta_r_reglement_liaison_facture FOREIGN KEY (id_facture)
      REFERENCES ta_facture (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_r_reglement_liaison_reglement FOREIGN KEY (id_reglement)
      REFERENCES ta_reglement (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_r_reglement_liaison_boncde FOREIGN KEY (id_boncde)
      REFERENCES ta_boncde (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_r_reglement_liaison_bcde_achat FOREIGN KEY (id_boncde_achat)
      REFERENCES ta_boncde_achat (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
            
  CONSTRAINT fk_ta_r_reglement_liaison_acompte FOREIGN KEY (id_acompte)
      REFERENCES ta_acompte (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
            
  CONSTRAINT fk_ta_r_reglement_liaison_avis FOREIGN KEY (id_avis_echeance)
      REFERENCES ta_avis_echeance (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
            
  CONSTRAINT fk_ta_r_reglement_liaison_abonnement FOREIGN KEY (id_abonnement)
      REFERENCES ta_abonnement (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
            
  CONSTRAINT fk_ta_r_reglement_liaison_reception FOREIGN KEY (id_bon_reception)
      REFERENCES ta_bon_reception (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
            
  CONSTRAINT fk_ta_r_reglement_liaison_prel FOREIGN KEY (id_prelevement)
      REFERENCES ta_prelevement (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
            
  CONSTRAINT fk_ta_r_reglement_liaison_prof FOREIGN KEY (id_proforma)
      REFERENCES ta_proforma (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
            
  CONSTRAINT fk_ta_r_reglement_liaison_prepa FOREIGN KEY (id_preparation)
      REFERENCES ta_preparation (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
            
  CONSTRAINT fk_ta_r_reglement_liaison_panier FOREIGN KEY (id_panier)
      REFERENCES ta_panier (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
            
  CONSTRAINT fk_ta_r_reglement_liaison_devis FOREIGN KEY (id_devis)
      REFERENCES ta_devis (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
            
  CONSTRAINT fk_ta_r_reglement_liaison_apporteur FOREIGN KEY (id_apporteur)
      REFERENCES ta_apporteur (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
            
  CONSTRAINT fk_ta_r_reglement_liaison_avoir FOREIGN KEY (id_avoir)
      REFERENCES ta_avoir (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
            
  CONSTRAINT fk_ta_r_reglement_liaison_bl FOREIGN KEY (id_bonliv)
      REFERENCES ta_bonliv (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,        
            
  CONSTRAINT fk_ta_r_reglement_liaison_id_ticket_caisse FOREIGN KEY (id_ticket_caisse)
      REFERENCES ta_ticket_caisse (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_r_reglement_liaison
  OWNER TO bdg;




CREATE TRIGGER tbi_r_reglement
  BEFORE INSERT
  ON ta_r_reglement_liaison
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();



CREATE TRIGGER tbu_r_reglement
  BEFORE UPDATE
  ON ta_r_reglement_liaison
  FOR EACH ROW
  EXECUTE PROCEDURE before_update();


----- fin maj liaison réglement avec tous les documents ---------


-- début optimisation des listes articles

INSERT INTO ta_categorie_preference
(id_categorie_preference, id_parent_categorie_preference, code_categorie_preference, libelle_categorie_preference, description_categorie_preference, identifiant_module, "position", version_obj)
VALUES(45, 1, NULL, 'Optimisation', NULL, NULL, 1,  0);

INSERT INTO ta_preferences
( groupe, cle, valeur, valeur_defaut, type_donnees, libelle,  id_categorie_preference, id_groupe_preference, fichier_vue, "position", position_dans_groupe, est_un_groupe, description, id_utilisateur, visible, read_only)
VALUES( 'optimisation', 'grosDossier', 'false', '', 'boolean', 'Gros dossier', 45, NULL, NULL, NULL, NULL, false, NULL, NULL, true, false);

INSERT INTO ta_preferences
( groupe, cle, valeur, valeur_defaut, type_donnees, libelle,  id_categorie_preference, id_groupe_preference, fichier_vue, "position", position_dans_groupe, est_un_groupe, description, id_utilisateur, visible, read_only)
VALUES( 'optimisation', 'grosFichierArticle', 'false', '', 'boolean', 'Vous avez un gros fichier d''articles''', 45, NULL, NULL, NULL, NULL, false, NULL, NULL, true, false);

INSERT INTO ta_preferences
( groupe, cle, valeur, valeur_defaut, type_donnees, libelle,  id_categorie_preference, id_groupe_preference, fichier_vue, "position", position_dans_groupe, est_un_groupe, description, id_utilisateur, visible, read_only)
VALUES( 'optimisation', 'grosFichierTiers', 'false', '', 'boolean', 'Vous avez un gros fichier de tiers', 45, NULL, NULL, NULL, NULL, false, NULL, NULL, true, false);

INSERT INTO ta_preferences
( groupe, cle, valeur, valeur_defaut, type_donnees, libelle,  id_categorie_preference, id_groupe_preference, fichier_vue, "position", position_dans_groupe, est_un_groupe, description, id_utilisateur, visible, read_only)
VALUES( 'optimisation', 'nbMaxChargeListeArticle', '10000', '', 'numeric', 'Indiquer le nombre d''article max. à afficher dans les listes d''article''', 45, NULL, NULL, NULL, NULL, false, NULL, NULL, true, false);

INSERT INTO ta_preferences
( groupe, cle, valeur, valeur_defaut, type_donnees, libelle,  id_categorie_preference, id_groupe_preference, fichier_vue, "position", position_dans_groupe, est_un_groupe, description, id_utilisateur, visible, read_only)
VALUES( 'optimisation', 'nbMaxChargeListeTiers', '10000', '', 'numeric', 'Indiquer le nombre de tiers max. à afficher dans les listes de tiers', 45, NULL, NULL, NULL, NULL, false, NULL, NULL, true, false);

-- fin optimisation des listes articles


