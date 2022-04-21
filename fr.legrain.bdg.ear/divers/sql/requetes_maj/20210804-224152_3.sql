--maj_stripe_variable
--set schema 'demo';

INSERT INTO ta_preferences
( groupe, cle, valeur, valeur_defaut, type_donnees, libelle,  id_categorie_preference, id_groupe_preference, fichier_vue, "position", position_dans_groupe, est_un_groupe, description, id_utilisateur, visible, read_only)
VALUES( 'Pourcentage prix variable', 'pourcentage_prix_variable', '10', '', 'numeric', 'Pourcentage prix variable', 37, NULL, NULL, NULL, NULL, false, NULL, NULL, true, false);

INSERT INTO ta_preferences
( groupe, cle, valeur, valeur_defaut, type_donnees, libelle,  id_categorie_preference, id_groupe_preference, fichier_vue, "position", position_dans_groupe, est_un_groupe, description, id_utilisateur, visible, read_only)
VALUES( 'Code article pour ajustement de prix variable', 'code_article_pour_ajustement_prix_variable', '', '', 'string', 'Code article pour ajustement de prix variable', 37, NULL, NULL, NULL, NULL, false, NULL, NULL, true, false);


ALTER TABLE ta_article ADD COLUMN prix_variable boolean default false;

ALTER TABLE ta_stripe_payment_intent ADD COLUMN capture_method dlib100;
ALTER TABLE ta_stripe_payment_intent ADD COLUMN amount_to_capture did9facult;
ALTER TABLE ta_stripe_payment_intent ADD COLUMN canceled_at date_heure_lgr;
ALTER TABLE ta_stripe_payment_intent ADD COLUMN cancellation_reason dlib100;

