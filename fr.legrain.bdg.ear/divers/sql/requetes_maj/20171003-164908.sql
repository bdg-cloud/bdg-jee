INSERT INTO ta_type_notification (code_type_notification, libelle_type_notification, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj) VALUES ('WEBAPP', 'Dans l''application web', 'postgres', '2017-08-18 09:29:35.640563', 'postgres', '2017-08-18 09:29:35.640563', '2.0.13', '127.0.0.1/32', 0);
INSERT INTO ta_type_notification (code_type_notification, libelle_type_notification, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj) VALUES ('EMAIL', 'Par email', 'postgres', '2017-08-18 09:29:06.488790', 'postgres', '2017-08-18 09:30:18.455791', '2.0.13', '127.0.0.1/32', 0);
INSERT INTO ta_type_notification (code_type_notification, libelle_type_notification, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj) VALUES ('SMS', 'Par SMS', 'postgres', '2017-08-18 09:32:00.166273', 'postgres', '2017-08-18 09:32:00.166273', '2.0.13', '127.0.0.1/32', 0);


ALTER TABLE ta_evenement ALTER COLUMN code_evenement TYPE dlgr_codel;

ALTER TABLE ta_notification ADD COLUMN nb_minutes_avant_notification integer;
ALTER TABLE ta_notification ADD COLUMN nb_heures_avant_notification integer;
ALTER TABLE ta_notification ADD COLUMN nb_semaines_avant_notification integer;
ALTER TABLE ta_notification ADD COLUMN au_debut_de_evenement boolean;
ALTER TABLE ta_notification ADD COLUMN lu boolean;
ALTER TABLE ta_notification ADD COLUMN texte_notification text;

update ta_notification set au_debut_de_evenement = true where au_debut_de_evenement is null;
update ta_notification set lu = true where lu is null;

ALTER TABLE ta_notification ADD COLUMN timer_handle bytea;

ALTER TABLE ta_etiquette_email ADD COLUMN ordre integer;

ALTER TABLE ta_notification DROP COLUMN heure_notification;

ALTER TABLE ta_recurrence_evenement ADD COLUMN quotidien boolean;
ALTER TABLE ta_recurrence_evenement ADD COLUMN hebdomadaire boolean;
ALTER TABLE ta_recurrence_evenement ADD COLUMN mensuelle boolean;
ALTER TABLE ta_recurrence_evenement ADD COLUMN annuelle boolean;

ALTER TABLE ta_recurrence_evenement ADD COLUMN chaque_nb_jour integer;
ALTER TABLE ta_recurrence_evenement ADD COLUMN chaque_jour_ouvrable boolean;

ALTER TABLE ta_recurrence_evenement ADD COLUMN toute_les_nb_semaines boolean;
ALTER TABLE ta_recurrence_evenement ADD COLUMN les_xx_du_mois integer; --les 1er, les 21, les ...
ALTER TABLE ta_recurrence_evenement ADD COLUMN les_dernier_jours_du_mois boolean;

ALTER TABLE ta_recurrence_evenement ADD COLUMN les_lundi boolean;
ALTER TABLE ta_recurrence_evenement ADD COLUMN les_mardi boolean;
ALTER TABLE ta_recurrence_evenement ADD COLUMN les_mercredi boolean;
ALTER TABLE ta_recurrence_evenement ADD COLUMN les_jeudi boolean;
ALTER TABLE ta_recurrence_evenement ADD COLUMN les_vendredi boolean;
ALTER TABLE ta_recurrence_evenement ADD COLUMN les_samedi boolean;
ALTER TABLE ta_recurrence_evenement ADD COLUMN les_dimanche boolean;

ALTER TABLE ta_recurrence_evenement ADD COLUMN se_repetera_nb_fois integer;


ALTER TABLE ta_notification ADD COLUMN notification_envoyee boolean;

ALTER TABLE ta_utilisateur ADD COLUMN id_agenda_defaut did_facultatif;

ALTER TABLE ta_type_notification ADD COLUMN systeme boolean;
ALTER TABLE ta_type_evenement ADD COLUMN systeme boolean;
ALTER TABLE ta_categorie_evenement ADD COLUMN systeme boolean;

ALTER TABLE ta_notification ADD COLUMN id_evenement_tiers did_facultatif;

----------------------------------------------------------------------------------------------------------------

ALTER TABLE ta_r_reglement ADD COLUMN etat bigint;
ALTER TABLE ta_r_reglement ALTER COLUMN etat SET DEFAULT 0;

CREATE TABLE ta_mise_a_disposition
(
  id_mise_a_disposition serial NOT NULL,
  accessible_sur_compte_client date_heure_lgr,
  envoye_par_email date_heure_lgr,
  imprime_pour_client date_heure_lgr,
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  systeme boolean DEFAULT false,
  CONSTRAINT ta_mise_a_disposition_pkey PRIMARY KEY (id_mise_a_disposition)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_mise_a_disposition
  OWNER TO postgres;

CREATE TRIGGER tbi_mise_a_disposition
  BEFORE INSERT
  ON ta_mise_a_disposition
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

CREATE TRIGGER tbu_mise_a_disposition
  BEFORE UPDATE
  ON ta_mise_a_disposition
  FOR EACH ROW
  EXECUTE PROCEDURE before_update();

  
ALTER TABLE ta_facture ADD COLUMN id_mise_a_disposition did_facultatif;
ALTER TABLE ta_facture DROP COLUMN date_mise_a_dispo_cptclient;

ALTER TABLE ta_facture
  ADD CONSTRAINT fk_ta_mise_a_disposition_id_mise_a_disposition FOREIGN KEY (id_mise_a_disposition)
      REFERENCES ta_mise_a_disposition (id_mise_a_disposition) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
      
ALTER TABLE ta_acompte ADD COLUMN id_mise_a_disposition did_facultatif;
ALTER TABLE ta_acompte DROP COLUMN date_mise_a_dispo_cptclient;

ALTER TABLE ta_acompte
  ADD CONSTRAINT fk_ta_mise_a_disposition_id_mise_a_disposition FOREIGN KEY (id_mise_a_disposition)
      REFERENCES ta_mise_a_disposition (id_mise_a_disposition) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE ta_apporteur ADD COLUMN id_mise_a_disposition did_facultatif;
ALTER TABLE ta_apporteur DROP COLUMN date_mise_a_dispo_cptclient;

ALTER TABLE ta_apporteur
  ADD CONSTRAINT fk_ta_mise_a_disposition_id_mise_a_disposition FOREIGN KEY (id_mise_a_disposition)
      REFERENCES ta_mise_a_disposition (id_mise_a_disposition) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE ta_avoir ADD COLUMN id_mise_a_disposition did_facultatif;
ALTER TABLE ta_avoir DROP COLUMN date_mise_a_dispo_cptclient;

ALTER TABLE ta_avoir
  ADD CONSTRAINT fk_ta_mise_a_disposition_id_mise_a_disposition FOREIGN KEY (id_mise_a_disposition)
      REFERENCES ta_mise_a_disposition (id_mise_a_disposition) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
      
ALTER TABLE ta_boncde ADD COLUMN id_mise_a_disposition did_facultatif;
ALTER TABLE ta_boncde DROP COLUMN date_mise_a_dispo_cptclient;

ALTER TABLE ta_boncde
  ADD CONSTRAINT fk_ta_mise_a_disposition_id_mise_a_disposition FOREIGN KEY (id_mise_a_disposition)
      REFERENCES ta_mise_a_disposition (id_mise_a_disposition) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
 
ALTER TABLE ta_bonliv ADD COLUMN id_mise_a_disposition did_facultatif;

ALTER TABLE ta_bonliv
  ADD CONSTRAINT fk_ta_mise_a_disposition_id_mise_a_disposition FOREIGN KEY (id_mise_a_disposition)
      REFERENCES ta_mise_a_disposition (id_mise_a_disposition) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
      
ALTER TABLE ta_devis ADD COLUMN id_mise_a_disposition did_facultatif;
ALTER TABLE ta_devis DROP COLUMN date_mise_a_dispo_cptclient;

ALTER TABLE ta_devis
  ADD CONSTRAINT fk_ta_mise_a_disposition_id_mise_a_disposition FOREIGN KEY (id_mise_a_disposition)
      REFERENCES ta_mise_a_disposition (id_mise_a_disposition) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
      
ALTER TABLE ta_prelevement ADD COLUMN id_mise_a_disposition did_facultatif;
ALTER TABLE ta_prelevement DROP COLUMN date_mise_a_dispo_cptclient;

ALTER TABLE ta_prelevement
  ADD CONSTRAINT fk_ta_mise_a_disposition_id_mise_a_disposition FOREIGN KEY (id_mise_a_disposition)
      REFERENCES ta_mise_a_disposition (id_mise_a_disposition) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
      

ALTER TABLE ta_proforma ADD COLUMN id_mise_a_disposition did_facultatif;
ALTER TABLE ta_proforma DROP COLUMN date_mise_a_dispo_cptclient;

ALTER TABLE ta_proforma
  ADD CONSTRAINT fk_ta_mise_a_disposition_id_mise_a_disposition FOREIGN KEY (id_mise_a_disposition)
      REFERENCES ta_mise_a_disposition (id_mise_a_disposition) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
      
ALTER TABLE ta_reglement ADD COLUMN id_mise_a_disposition did_facultatif;
ALTER TABLE ta_reglement DROP COLUMN date_mise_a_dispo_cptclient;

ALTER TABLE ta_reglement
  ADD CONSTRAINT fk_ta_mise_a_disposition_id_mise_a_disposition FOREIGN KEY (id_mise_a_disposition)
      REFERENCES ta_mise_a_disposition (id_mise_a_disposition) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
      
ALTER TABLE ta_remise ADD COLUMN id_mise_a_disposition did_facultatif;
ALTER TABLE ta_remise DROP COLUMN date_mise_a_dispo_cptclient;

ALTER TABLE ta_remise
  ADD CONSTRAINT fk_ta_mise_a_disposition_id_mise_a_disposition FOREIGN KEY (id_mise_a_disposition)
      REFERENCES ta_mise_a_disposition (id_mise_a_disposition) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
      
-----------------------------------------------------------------------------------------------------------------------
ALTER TABLE ta_status_conformite ADD COLUMN libelle_status_conformite_lot dlib255;

update ta_resultat_conformite set id_status_conformite = null;
delete from ta_status_conformite;

INSERT INTO ta_status_conformite (code_status_conformite, libelle_status_conformite, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj, importance, libelle_status_conformite_lot) VALUES ('ok', 'controle valide', 'postgres', '2017-04-12 17:39:12.193246', 'bdg', '2017-09-04 14:42:33.545677', '2.0.13', '127.0.0.1/32', 0, 200, 'lot valide');
INSERT INTO ta_status_conformite (code_status_conformite, libelle_status_conformite, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj, importance, libelle_status_conformite_lot) VALUES ('ok_apres_correction', 'correction effectuée avec succés', 'bdg', '2017-04-12 17:39:12.214146', 'bdg', '2017-09-04 14:42:59.201434', '2.0.13', '127.0.0.1/32', 0, 300, 'lot valide après correction');
INSERT INTO ta_status_conformite (code_status_conformite, libelle_status_conformite, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj, importance, libelle_status_conformite_lot) VALUES ('invalide_meme_apres_correction', 'correction effectuée sans succés => encore invalide', 'bdg', '2017-04-12 17:39:12.256091', 'bdg', '2017-09-04 14:43:24.265210', '2.0.13', '127.0.0.1/32', 0, 400, 'lot invalide même aprés correction');
INSERT INTO ta_status_conformite (code_status_conformite, libelle_status_conformite, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj, importance, libelle_status_conformite_lot) VALUES ('invalide_a_corrige', 'correction à faire (invalide)', 'bdg', '2017-04-12 17:39:12.235134', 'bdg', '2017-09-04 14:43:37.663328', '2.0.13', '127.0.0.1/32', 0, 500, 'lot invalide');
INSERT INTO ta_status_conformite (code_status_conformite, libelle_status_conformite, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj, importance, libelle_status_conformite_lot) VALUES ('non_fait', 'pas de valeur saisie', 'bdg', '2017-04-12 17:39:12.277026', 'bdg', '2017-09-04 14:45:26.025899', '2.0.13', '127.0.0.1/32', 0, 600, 'au moins 1 controle obligatoire non fait');

---------------------------------------------------------------------------------------------------------------------------
ALTER TABLE ta_remise DROP COLUMN IF EXISTS id_tiers;

ALTER TABLE ta_compte_banque  drop CONSTRAINT  IF EXISTS "ibanBicUnique"; 
   
ALTER TABLE ta_compte_banque  ADD CONSTRAINT "ibanBicUnique" UNIQUE (iban, code_b_i_c);
  
INSERT INTO ta_gen_code_ex(code_gen_code, cle_gen_code, valeur_gen_code,version_obj)values('codeDocument','TaRemise','RM{@exo}{@num}',0) ON CONFLICT DO NOTHING; 

INSERT INTO ta_compte_banque( id_tiers, nom_banque, compte, code_banque, code_guichet, cle_rib,  iban, code_b_i_c, titulaire, cptcomptable, 
            version_obj, nom_compte) VALUES (1, '?', '?', '?','?','?','?','?', '?', '512', 0, 'monCompte : ?') ON CONFLICT DO NOTHING; 
            
---------------------------------------------------------------------------------------------------------------------------
ALTER TABLE ta_lot ADD COLUMN lot_complet_definitivement_invalide boolean;
ALTER TABLE ta_lot ALTER COLUMN lot_complet_definitivement_invalide SET DEFAULT false;

ALTER TABLE ta_status_conformite ADD CONSTRAINT ta_status_conformite_code_key UNIQUE(code_status_conformite);

ALTER TABLE ta_modele_conformite ADD COLUMN code dlgr_codel;
ALTER TABLE ta_modele_conformite ADD CONSTRAINT ta_modele_conformite_code_key UNIQUE(code);

ALTER TABLE ta_conformite ADD COLUMN code dlgr_codel;
ALTER TABLE ta_conformite ADD CONSTRAINT ta_conformite_code_key UNIQUE(code);
ALTER TABLE ta_conformite ADD COLUMN code_modele_groupe_origine dlgr_codel;
ALTER TABLE ta_conformite ADD COLUMN code_modele_conformite_origine dlgr_codel;

ALTER TABLE ta_bareme ADD COLUMN code dlgr_codel;

ALTER TABLE ta_modele_bareme ADD COLUMN code dlgr_codel;

INSERT INTO ta_gen_code_ex (code_gen_code, cle_gen_code, valeur_gen_code, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj) VALUES ('code', 'TaConformite', 'CTRL{@num}', 'bdg', '2017-09-28 13:01:59.832177', 'bdg', '2017-09-28 13:01:59.832177', '2.0.13', '127.0.0.1/32', 0);
INSERT INTO ta_gen_code_ex (code_gen_code, cle_gen_code, valeur_gen_code, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj) VALUES ('code', 'TaModeleConformite', 'MCTRL{@num}', 'bdg', '2017-09-28 13:02:50.189668', 'bdg', '2017-09-28 13:02:50.189668', '2.0.13', '127.0.0.1/32', 0);
INSERT INTO ta_gen_code_ex (code_gen_code, cle_gen_code, valeur_gen_code, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj) VALUES ('code', 'TaBareme', 'BAR{@num}', 'bdg', '2017-09-28 13:03:33.389093', 'bdg', '2017-09-28 13:03:33.389093', '2.0.13', '127.0.0.1/32', 0);
INSERT INTO ta_gen_code_ex (code_gen_code, cle_gen_code, valeur_gen_code, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj) VALUES ('code', 'TaModeleBareme', 'MBAR{@num}', 'bdg', '2017-09-28 13:04:12.011528', 'bdg', '2017-09-28 13:04:12.011528', '2.0.13', '127.0.0.1/32', 0);

---------------------------------------------------------------------------------------------------------------------------

--ALTER TABLE ta_conformite  ADD CONSTRAINT version_ctrl_historique_unique UNIQUE (id_modele_conformite_parent_avant_modif, version_ctrl) where supprime = false;


            
