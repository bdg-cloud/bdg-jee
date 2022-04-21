--set schema 'demo';

ALTER TABLE ta_param_espace_client ADD COLUMN id_adresse_point_retrait did_facultatif;
alter table ta_param_espace_client add CONSTRAINT ta_param_espace_client_id_adresse_point_retrait_fkey FOREIGN KEY (id_adresse_point_retrait)
      REFERENCES ta_adresse (id_adresse) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
      
ALTER TABLE ta_param_espace_client ADD COLUMN adresse1_point_retrait dlib100;
ALTER TABLE ta_param_espace_client ADD COLUMN adresse2_point_retrait dlib100;
ALTER TABLE ta_param_espace_client ADD COLUMN adresse3_point_retrait dlib100;
ALTER TABLE ta_param_espace_client ADD COLUMN code_postal_point_retrait dcodpos;
ALTER TABLE ta_param_espace_client ADD COLUMN ville_point_retrait dlib100;
ALTER TABLE ta_param_espace_client ADD COLUMN pays_point_retrait dlib100;
ALTER TABLE ta_param_espace_client ADD COLUMN latitude_dec_point_retrait dlib50;
ALTER TABLE ta_param_espace_client ADD COLUMN longitude_dec_point_retrait dlib50;

ALTER TABLE ta_param_espace_client ADD COLUMN horaires_ouverture_point_retrait text;

ALTER TABLE ta_param_espace_client ADD COLUMN active_point_retrait boolean DEFAULT false;

ALTER TABLE ta_param_espace_client ADD COLUMN active_paiement_panier_cheque boolean DEFAULT false;
ALTER TABLE ta_param_espace_client ADD COLUMN active_paiement_panier_virement boolean DEFAULT false;
ALTER TABLE ta_param_espace_client ADD COLUMN prix_catalogue_ht boolean DEFAULT false;
ALTER TABLE ta_param_espace_client ADD COLUMN active_planning_retrait boolean DEFAULT false;


ALTER TABLE ta_param_espace_client ADD COLUMN id_compte_banque_paiement_virement did_facultatif;
alter table ta_param_espace_client add CONSTRAINT ta_param_espace_client_id_compte_banque_paiement_virement_fkey FOREIGN KEY (id_compte_banque_paiement_virement)
      REFERENCES ta_compte_banque (id_compte_banque) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
      
ALTER TABLE ta_param_espace_client ADD COLUMN iban_paiement_virement dlib100;
ALTER TABLE ta_param_espace_client ADD COLUMN swift_paiement_virement dlgr_codel;

---------------------------------------------------------------------------------------------
ALTER TABLE ta_param_espace_client ADD COLUMN utilisateur_peu_creer_compte boolean DEFAULT false;
ALTER TABLE ta_param_espace_client ADD COLUMN liaison_nouveau_compte_email_auto boolean DEFAULT false;
ALTER TABLE ta_param_espace_client ADD COLUMN liaison_nouveau_compte_code_client_auto boolean DEFAULT false;
ALTER TABLE ta_param_espace_client ADD COLUMN autorise_maj_donnee_par_client boolean DEFAULT false;

INSERT INTO ta_t_tiers (code_t_tiers, libelle_t_tiers, compte_t_tiers,version_obj,systeme) VALUES ('VISIT', 'Visiteur Boutique', '421', 0, true);




