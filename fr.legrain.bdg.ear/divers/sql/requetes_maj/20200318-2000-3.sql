    



INSERT INTO ta_categorie_preference
(id_categorie_preference, id_parent_categorie_preference, code_categorie_preference, libelle_categorie_preference, description_categorie_preference, identifiant_module, "position", version_obj)
VALUES(41, 1, NULL, 'Bon de preparation de commande', NULL, NULL, 14,  0);

INSERT INTO ta_categorie_preference
(id_categorie_preference, id_parent_categorie_preference, code_categorie_preference, libelle_categorie_preference, description_categorie_preference, identifiant_module, "position", version_obj)
VALUES(42, 1, NULL, 'Lignes flashées', NULL, NULL, 14,  0);



INSERT INTO ta_preferences
( groupe, cle, valeur, valeur_defaut, type_donnees, libelle,  id_categorie_preference, id_groupe_preference, fichier_vue, "position", position_dans_groupe, est_un_groupe, description, id_utilisateur, visible, read_only)
VALUES( 'preparation', 'CoupureLigne', '54', '', 'numeric', 'Coupure de ligne', 41, NULL, NULL, NULL, NULL, false, NULL, NULL, true, false);
INSERT INTO ta_preferences
( groupe, cle, valeur, valeur_defaut, type_donnees, libelle,  id_categorie_preference, id_groupe_preference, fichier_vue, "position", position_dans_groupe, est_un_groupe, description, id_utilisateur, visible, read_only)
VALUES( 'preparation', 'PageBreakMaxi', '36', '', 'numeric', 'Page break Maxi',  41, NULL, NULL, NULL, NULL, false, NULL, NULL, true, false);
INSERT INTO ta_preferences
( groupe, cle, valeur, valeur_defaut, type_donnees, libelle,  id_categorie_preference, id_groupe_preference, fichier_vue, "position", position_dans_groupe, est_un_groupe, description, id_utilisateur, visible, read_only)
VALUES( 'preparation', 'PageBreakTotaux', '23', '', 'numeric', 'Page break Totaux', 41, NULL, NULL, NULL, NULL, false, NULL, NULL, true, false);
INSERT INTO ta_preferences
( groupe, cle, valeur, valeur_defaut, type_donnees, libelle,  id_categorie_preference, id_groupe_preference, fichier_vue, "position", position_dans_groupe, est_un_groupe, description, id_utilisateur, visible, read_only)
VALUES( 'preparation', 'ParamChoix', 'choix 3', '', 'string', 'listeChoix',  41, NULL, NULL, NULL, NULL, false, NULL, NULL, true, false);
INSERT INTO ta_preferences
( groupe, cle, valeur, valeur_defaut, type_donnees, libelle,  id_categorie_preference, id_groupe_preference, fichier_vue, "position", position_dans_groupe, est_un_groupe, description, id_utilisateur, visible, read_only)
VALUES( 'preparation', 'ImpressionDirect', 'false', '', 'boolean', 'Impression direct', 41, NULL, NULL, NULL, NULL, false, NULL, NULL, true, false);
INSERT INTO ta_preferences
( groupe, cle, valeur, valeur_defaut, type_donnees, libelle,  id_categorie_preference, id_groupe_preference, fichier_vue, "position", position_dans_groupe, est_un_groupe, description, id_utilisateur, visible, read_only)
VALUES( 'preparation', 'Traite', 'true', '', 'boolean', 'Traite',  41, NULL, NULL, NULL, NULL, false, NULL, NULL, true, false);


INSERT INTO ta_preferences
( groupe, cle, valeur, valeur_defaut, type_donnees, libelle,  id_categorie_preference, id_groupe_preference, fichier_vue, "position", position_dans_groupe, est_un_groupe, description, id_utilisateur, visible, read_only)
VALUES( 'flashage', 'CoupureLigne', '54', '', 'numeric', 'Coupure de ligne', 42, NULL, NULL, NULL, NULL, false, NULL, NULL, true, false);
INSERT INTO ta_preferences
( groupe, cle, valeur, valeur_defaut, type_donnees, libelle,  id_categorie_preference, id_groupe_preference, fichier_vue, "position", position_dans_groupe, est_un_groupe, description, id_utilisateur, visible, read_only)
VALUES( 'flashage', 'PageBreakMaxi', '36', '', 'numeric', 'Page break Maxi',  42, NULL, NULL, NULL, NULL, false, NULL, NULL, true, false);
INSERT INTO ta_preferences
( groupe, cle, valeur, valeur_defaut, type_donnees, libelle,  id_categorie_preference, id_groupe_preference, fichier_vue, "position", position_dans_groupe, est_un_groupe, description, id_utilisateur, visible, read_only)
VALUES( 'flashage', 'PageBreakTotaux', '23', '', 'numeric', 'Page break Totaux', 42, NULL, NULL, NULL, NULL, false, NULL, NULL, true, false);
INSERT INTO ta_preferences
( groupe, cle, valeur, valeur_defaut, type_donnees, libelle,  id_categorie_preference, id_groupe_preference, fichier_vue, "position", position_dans_groupe, est_un_groupe, description, id_utilisateur, visible, read_only)
VALUES( 'flashage', 'ParamChoix', 'choix 3', '', 'string', 'listeChoix',  42, NULL, NULL, NULL, NULL, false, NULL, NULL, true, false);
INSERT INTO ta_preferences
( groupe, cle, valeur, valeur_defaut, type_donnees, libelle,  id_categorie_preference, id_groupe_preference, fichier_vue, "position", position_dans_groupe, est_un_groupe, description, id_utilisateur, visible, read_only)
VALUES( 'flashage', 'ImpressionDirect', 'false', '', 'boolean', 'Impression direct', 42, NULL, NULL, NULL, NULL, false, NULL, NULL, true, false);
INSERT INTO ta_preferences
( groupe, cle, valeur, valeur_defaut, type_donnees, libelle,  id_categorie_preference, id_groupe_preference, fichier_vue, "position", position_dans_groupe, est_un_groupe, description, id_utilisateur, visible, read_only)
VALUES( 'flashage', 'Traite', 'true', '', 'boolean', 'Traite',  42, NULL, NULL, NULL, NULL, false, NULL, NULL, true, false);

------------------------------
ALTER TABLE ta_utilisateur ADD COLUMN android_registration_token text;
ALTER TABLE ta_utilisateur ADD COLUMN dernier_acces_mobile timestamp with time zone;
-------------------------------

