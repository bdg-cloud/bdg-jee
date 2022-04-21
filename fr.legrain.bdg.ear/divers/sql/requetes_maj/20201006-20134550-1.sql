--set schema 'demo';

INSERT INTO ta_preferences
( groupe, cle, valeur, valeur_defaut, type_donnees, libelle,  id_categorie_preference, id_groupe_preference, fichier_vue, "position", position_dans_groupe, est_un_groupe, description, id_utilisateur, visible, read_only)
VALUES( 'preparation', 'Commentaire', '', '', 'text', 'Commentaire par défaut', 41, NULL, NULL, NULL, NULL, false, NULL, NULL, true, false);

--trop long en mise a jour
--select f_update_seq();