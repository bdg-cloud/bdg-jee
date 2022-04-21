--set schema 'demo';
ALTER TABLE ta_abonnement ADD COLUMN date_suspension date_lgr DEFAULT NULL;
ALTER TABLE ta_abonnement ADD COLUMN suspension bool;
ALTER TABLE ta_abonnement ADD COLUMN date_fin_engagement date_lgr DEFAULT NULL; 
ALTER TABLE ta_abonnement ADD COLUMN reconduction_tacite_abonnement bool;
ALTER TABLE ta_abonnement ADD COLUMN reconduction_tacite_engagement bool;
ALTER TABLE ta_abonnement ADD COLUMN duree_engagement did_facultatif;