--set schema 'demo';

UPDATE ta_preferences SET "position"=1 where groupe='exportation' and cle='export_compta';  
UPDATE ta_preferences SET "position"=2 where groupe='exportation' and cle='journal_facture';  
UPDATE ta_preferences SET "position"=3 where groupe='exportation' and cle='journal_avoir';  
UPDATE ta_preferences SET "position"=4 where groupe='exportation' and cle='journal_apporteur';  
UPDATE ta_preferences SET "position"=5 where groupe='exportation' and cle='journal_reglement';  
UPDATE ta_preferences SET "position"=6 where groupe='exportation' and cle='journal_remise';
UPDATE ta_preferences SET "position"=7 where groupe='exportation' and cle='Repertoire_exportation';  
UPDATE ta_preferences SET "position"=8 where groupe='exportation' and cle='Remise';  
UPDATE ta_preferences SET "position"=9 where groupe='exportation' and cle='Apporteur';  
UPDATE ta_preferences SET "position"=10 where groupe='exportation' and cle='Centraliser_Les_Ecritures';  
UPDATE ta_preferences SET "position"=11 where groupe='exportation' and cle='Transfert_documents_lies_au_reglement';  
UPDATE ta_preferences SET "position"=12 where groupe='exportation' and cle='Transfert_pointages';  
UPDATE ta_preferences SET "position"=13 where groupe='exportation' and cle='Transfert_reglements_lies_au_document';  
UPDATE ta_preferences SET "position"=14 where groupe='exportation' and cle='Reglement_simple';  
UPDATE ta_preferences SET "position"=15 where groupe='exportation' and cle='Acomptes';  
UPDATE ta_preferences SET "position"=16 where groupe='exportation' and cle='email_adresse_comptable_defaut';

--set schema 'demo';

CREATE TABLE ta_terminal_mobile
(
  id_terminal_mobile serial NOT NULL,
  date_derniere_connexion date_heure_lgr,
  android_registration_token text,
  id_utilisateur did_facultatif,
  id_espace_client did_facultatif,
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_terminal_mobile_pkey PRIMARY KEY (id_terminal_mobile),
    CONSTRAINT ta_terminal_mobile_id_utilisateur_fkey FOREIGN KEY (id_utilisateur)
      REFERENCES ta_utilisateur (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
        CONSTRAINT ta_terminal_mobile_id_espace_client_fkey FOREIGN KEY (id_espace_client)
      REFERENCES ta_espace_client (id_espace_client) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_terminal_mobile
  OWNER TO bdg;


CREATE TRIGGER tbi_terminal_mobile
  BEFORE INSERT
  ON ta_terminal_mobile
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();


CREATE TRIGGER tbu_terminal_mobile
  BEFORE UPDATE
  ON ta_terminal_mobile
  FOR EACH ROW
  EXECUTE PROCEDURE before_update();