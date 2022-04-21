--set schema 'demo';

ALTER TABLE ta_espace_client ADD COLUMN date_derniere_connexion date_heure_lgr default null;