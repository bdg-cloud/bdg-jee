--maj_option_generation
--set schema 'demo';

update ta_preferences set id_categorie_preference=40 where groupe ='boncdeAchat'; 

-- INSERT INTO ta_etat ( code_etat, libelle_etat, version_obj, systeme, visible, identifiant, id_t_etat, listable, accepte, "position", conditions, commentaire) 
-- VALUES( 'doc_encours_part_paiement', 'document en cours de paiement partiel',  0, true, true, 'bdg.etat.document.encours.partiellement.paiement', 4, true, true, 4, NULL, NULL);
-- 
-- INSERT INTO ta_etat ( code_etat, libelle_etat, version_obj, systeme, visible, identifiant, id_t_etat, listable, accepte, "position", conditions, commentaire) 
-- VALUES( 'doc_encours_tot_paiement', 'document en cours de paiement total',  0, true, true, 'bdg.etat.document.encours.totalement.paiement', 4, true, true, 4, NULL, NULL);


INSERT INTO ta_preferences
( groupe, cle, valeur, valeur_defaut, type_donnees, libelle,  id_categorie_preference, id_groupe_preference, fichier_vue, "position", position_dans_groupe, est_un_groupe, description, id_utilisateur, visible, read_only)
VALUES( 'facture', 'option_reprise_infos_doc_generation', '3', '', 'string', 'Option de reprise des infos document dans la génération (1 à 3)', 2, NULL, NULL, NULL, NULL, false, NULL, NULL, true, false);

INSERT INTO ta_preferences
( groupe, cle, valeur, valeur_defaut, type_donnees, libelle,  id_categorie_preference, id_groupe_preference, fichier_vue, "position", position_dans_groupe, est_un_groupe, description, id_utilisateur, visible, read_only)
VALUES( 'acompte', 'option_reprise_infos_doc_generation', '3', '', 'string', 'Option de reprise des infos document dans la génération (1 à 3)',4, NULL, NULL, NULL, NULL, false, NULL, NULL, true, false);

INSERT INTO ta_preferences
( groupe, cle, valeur, valeur_defaut, type_donnees, libelle,  id_categorie_preference, id_groupe_preference, fichier_vue, "position", position_dans_groupe, est_un_groupe, description, id_utilisateur, visible, read_only)
VALUES( 'apporteur', 'option_reprise_infos_doc_generation', '3', '', 'string', 'Option de reprise des infos document dans la génération (1 à 3)',10, NULL, NULL, NULL, NULL, false, NULL, NULL, true, false);

INSERT INTO ta_preferences
( groupe, cle, valeur, valeur_defaut, type_donnees, libelle,  id_categorie_preference, id_groupe_preference, fichier_vue, "position", position_dans_groupe, est_un_groupe, description, id_utilisateur, visible, read_only)
VALUES( 'avoir', 'option_reprise_infos_doc_generation', '3', '', 'string', 'Option de reprise des infos document dans la génération (1 à 3)',5, NULL, NULL, NULL, NULL, false, NULL, NULL, true, false);

INSERT INTO ta_preferences
( groupe, cle, valeur, valeur_defaut, type_donnees, libelle,  id_categorie_preference, id_groupe_preference, fichier_vue, "position", position_dans_groupe, est_un_groupe, description, id_utilisateur, visible, read_only)
VALUES( 'boncde', 'option_reprise_infos_doc_generation', '3', '', 'string', 'Option de reprise des infos document dans la génération (1 à 3)', 7, NULL, NULL, NULL, NULL, false, NULL, NULL, true, false);

INSERT INTO ta_preferences
( groupe, cle, valeur, valeur_defaut, type_donnees, libelle,  id_categorie_preference, id_groupe_preference, fichier_vue, "position", position_dans_groupe, est_un_groupe, description, id_utilisateur, visible, read_only)
VALUES( 'boncdeAchat', 'option_reprise_infos_doc_generation', '3', '', 'string', 'Option de reprise des infos document dans la génération (1 à 3)', 40, NULL, NULL, NULL, NULL, false, NULL, NULL, true, false);

INSERT INTO ta_preferences
( groupe, cle, valeur, valeur_defaut, type_donnees, libelle,  id_categorie_preference, id_groupe_preference, fichier_vue, "position", position_dans_groupe, est_un_groupe, description, id_utilisateur, visible, read_only)
VALUES( 'bonliv', 'option_reprise_infos_doc_generation', '3', '', 'string', 'Option de reprise des infos document dans la génération (1 à 3)', 3, NULL, NULL, NULL, NULL, false, NULL, NULL, true, false);

INSERT INTO ta_preferences
( groupe, cle, valeur, valeur_defaut, type_donnees, libelle,  id_categorie_preference, id_groupe_preference, fichier_vue, "position", position_dans_groupe, est_un_groupe, description, id_utilisateur, visible, read_only)
VALUES( 'panier', 'option_reprise_infos_doc_generation', '3', '', 'string', 'Option de reprise des infos document dans la génération (1 à 3)',44, NULL, NULL, NULL, NULL, false, NULL, NULL, true, false);

INSERT INTO ta_preferences
( groupe, cle, valeur, valeur_defaut, type_donnees, libelle,  id_categorie_preference, id_groupe_preference, fichier_vue, "position", position_dans_groupe, est_un_groupe, description, id_utilisateur, visible, read_only)
VALUES( 'prelevement', 'option_reprise_infos_doc_generation', '3', '', 'string', 'Option de reprise des infos document dans la génération (1 à 3)', 9, NULL, NULL, NULL, NULL, false, NULL, NULL, true, false);

INSERT INTO ta_preferences
( groupe, cle, valeur, valeur_defaut, type_donnees, libelle,  id_categorie_preference, id_groupe_preference, fichier_vue, "position", position_dans_groupe, est_un_groupe, description, id_utilisateur, visible, read_only)
VALUES( 'preparation', 'option_reprise_infos_doc_generation', '3', '', 'string', 'Option de reprise des infos document dans la génération (1 à 3)', 41, NULL, NULL, NULL, NULL, false, NULL, NULL, true, false);

INSERT INTO ta_preferences
( groupe, cle, valeur, valeur_defaut, type_donnees, libelle,  id_categorie_preference, id_groupe_preference, fichier_vue, "position", position_dans_groupe, est_un_groupe, description, id_utilisateur, visible, read_only)
VALUES( 'proforma', 'option_reprise_infos_doc_generation', '3', '', 'string', 'Option de reprise des infos document dans la génération (1 à 3)', 8, NULL, NULL, NULL, NULL, false, NULL, NULL, true, false);

INSERT INTO ta_preferences
( groupe, cle, valeur, valeur_defaut, type_donnees, libelle,  id_categorie_preference, id_groupe_preference, fichier_vue, "position", position_dans_groupe, est_un_groupe, description, id_utilisateur, visible, read_only)
VALUES( 'devis', 'option_reprise_infos_doc_generation', '3', '', 'string', 'Option de reprise des infos document dans la génération (1 à 3)', 6, NULL, NULL, NULL, NULL, false, NULL, NULL, true, false);

INSERT INTO ta_preferences
( groupe, cle, valeur, valeur_defaut, type_donnees, libelle,  id_categorie_preference, id_groupe_preference, fichier_vue, "position", position_dans_groupe, est_un_groupe, description, id_utilisateur, visible, read_only)
VALUES( 'flashage', 'option_reprise_infos_doc_generation', '3', '', 'string', 'Option de reprise des infos document dans la génération (1 à 3)', 42, NULL, NULL, NULL, NULL, false, NULL, NULL, true, false);