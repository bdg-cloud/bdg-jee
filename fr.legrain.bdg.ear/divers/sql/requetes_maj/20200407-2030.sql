--set schema 'demo';

ALTER TABLE ta_espace_client ADD COLUMN android_registration_token text;
ALTER TABLE ta_espace_client ADD COLUMN dernier_acces_mobile timestamp with time zone;

INSERT INTO ta_preferences
( groupe, cle, valeur, valeur_defaut, type_donnees, libelle, version_obj, valeurs_possibles, id_categorie_preference, id_groupe_preference, fichier_vue, "position", position_dans_groupe, est_un_groupe, description, id_utilisateur, visible, read_only)
VALUES( 'exportation', 'email_adresse_comptable_defaut', '', '', 'string', 'Adresse Email comptable', 0, NULL, 11, NULL, NULL, NULL, NULL, false, NULL, NULL, true, false);

INSERT INTO ta_preferences
( groupe, cle, valeur, valeur_defaut, type_donnees, libelle, version_obj, valeurs_possibles, id_categorie_preference, id_groupe_preference, fichier_vue, "position", position_dans_groupe, est_un_groupe, description, id_utilisateur, visible, read_only)
VALUES( 'exportation', 'journal_facture', 'VT', 'VT', 'string', 'journal de l''exportation des factures', 0, NULL, 11, NULL, NULL, NULL, NULL, false, NULL, NULL, true, false);

INSERT INTO ta_preferences
( groupe, cle, valeur, valeur_defaut, type_donnees, libelle, version_obj, valeurs_possibles, id_categorie_preference, id_groupe_preference, fichier_vue, "position", position_dans_groupe, est_un_groupe, description, id_utilisateur, visible, read_only)
VALUES( 'exportation', 'journal_avoir', 'AV', 'AV', 'string', 'journal de l''exportation des avoirs', 0, NULL, 11, NULL, NULL, NULL, NULL, false, NULL, NULL, true, false);

INSERT INTO ta_preferences
( groupe, cle, valeur, valeur_defaut, type_donnees, libelle, version_obj, valeurs_possibles, id_categorie_preference, id_groupe_preference, fichier_vue, "position", position_dans_groupe, est_un_groupe, description, id_utilisateur, visible, read_only)
VALUES( 'exportation', 'journal_apporteur', 'AP', 'AP', 'string', 'journal de l''exportation des apporteurs', 0, NULL, 11, NULL, NULL, NULL, NULL, false, NULL, NULL, true, false);

INSERT INTO ta_preferences
( groupe, cle, valeur, valeur_defaut, type_donnees, libelle, version_obj, valeurs_possibles, id_categorie_preference, id_groupe_preference, fichier_vue, "position", position_dans_groupe, est_un_groupe, description, id_utilisateur, visible, read_only)
VALUES( 'exportation', 'journal_reglement', 'RG', 'RG', 'string', 'journal de l''exportation des réglements', 0, NULL, 11, NULL, NULL, NULL, NULL, false, NULL, NULL, true, false);

INSERT INTO ta_preferences
( groupe, cle, valeur, valeur_defaut, type_donnees, libelle, version_obj, valeurs_possibles, id_categorie_preference, id_groupe_preference, fichier_vue, "position", position_dans_groupe, est_un_groupe, description, id_utilisateur, visible, read_only)
VALUES( 'exportation', 'journal_remise', 'RM', 'RM', 'string', 'journal de l''exportation des remises', 0, NULL, 11, NULL, NULL, NULL, NULL, false, NULL, NULL, true, false);

INSERT INTO ta_preferences
( groupe, cle, valeur, valeur_defaut, type_donnees, libelle, version_obj, valeurs_possibles, id_categorie_preference, id_groupe_preference, fichier_vue, "position", position_dans_groupe, est_un_groupe, description, id_utilisateur, visible, read_only)
VALUES( 'exportation', 'export_compta', 'true', 'true', 'boolean', 'exportation en compta activée', 0, NULL, 11, NULL, NULL, NULL, NULL, false, NULL, NULL, true, false);

