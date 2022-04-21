--set schema 'demo';


-----------------------------------------
ALTER TABLE ta_edition ADD COLUMN resources_path dlib255;
ALTER TABLE ta_edition ADD COLUMN theme dlib255;
ALTER TABLE ta_edition ADD COLUMN librairie dlib255;
-----------------------------------------

-- Début maj pour branch developp après release candidate
delete from ta_categorie_preference;

INSERT INTO ta_categorie_preference ( id_categorie_preference,id_parent_categorie_preference, code_categorie_preference, libelle_categorie_preference, description_categorie_preference, identifiant_module, position, version_obj) 
VALUES (1, 17, null, 'Documents', null, null, 3, 0);
INSERT INTO ta_categorie_preference (  id_categorie_preference,id_parent_categorie_preference, code_categorie_preference, libelle_categorie_preference, description_categorie_preference, identifiant_module, position, version_obj) 
VALUES (2, 1, null, 'Facture', null, null, 3, 0);
INSERT INTO ta_categorie_preference (  id_categorie_preference,id_parent_categorie_preference, code_categorie_preference, libelle_categorie_preference, description_categorie_preference, identifiant_module, position, version_obj) 
VALUES (3, 1, null, 'Bon de livraison', null, null, 5, 0);
INSERT INTO ta_categorie_preference (  id_categorie_preference,id_parent_categorie_preference, code_categorie_preference, libelle_categorie_preference, description_categorie_preference, identifiant_module, position, version_obj) 
VALUES (4, 1, null, 'Acompte', null, null, 6, 0);
INSERT INTO ta_categorie_preference (  id_categorie_preference,id_parent_categorie_preference, code_categorie_preference, libelle_categorie_preference, description_categorie_preference, identifiant_module, position, version_obj) 
VALUES (5, 1, null, 'Avoir', null, null, 7, 0);
INSERT INTO ta_categorie_preference (  id_categorie_preference,id_parent_categorie_preference, code_categorie_preference, libelle_categorie_preference, description_categorie_preference, identifiant_module, position, version_obj) 
VALUES (6, 1, null, 'Devis', null, null, 8, 0);
INSERT INTO ta_categorie_preference (  id_categorie_preference,id_parent_categorie_preference, code_categorie_preference, libelle_categorie_preference, description_categorie_preference, identifiant_module, position, version_obj) 
VALUES (7, 1, null, 'Bon de commande', null, null, 4, 0);
INSERT INTO ta_categorie_preference (  id_categorie_preference,id_parent_categorie_preference, code_categorie_preference, libelle_categorie_preference, description_categorie_preference, identifiant_module, position, version_obj) 
VALUES (8, 1, null, 'Proforma', null, null, 9, 0);
INSERT INTO ta_categorie_preference (  id_categorie_preference,id_parent_categorie_preference, code_categorie_preference, libelle_categorie_preference, description_categorie_preference, identifiant_module, position, version_obj) 
VALUES (9, 1, null, 'Prelevement', null, null, 10, 0);
INSERT INTO ta_categorie_preference (  id_categorie_preference,id_parent_categorie_preference, code_categorie_preference, libelle_categorie_preference, description_categorie_preference, identifiant_module, position, version_obj) 
VALUES (10, 1, null, 'Apporteur', null, null, 11, 0);
INSERT INTO ta_categorie_preference (  id_categorie_preference,id_parent_categorie_preference, code_categorie_preference, libelle_categorie_preference, description_categorie_preference, identifiant_module, position, version_obj) 
VALUES (11, null, null, 'Exportation', null, null, 12, 0);
INSERT INTO ta_categorie_preference (  id_categorie_preference,id_parent_categorie_preference, code_categorie_preference, libelle_categorie_preference, description_categorie_preference, identifiant_module, position, version_obj) 
VALUES (12, null, null, 'Caisse enregistreuse', null, null, 13, 0);
INSERT INTO ta_categorie_preference (  id_categorie_preference,id_parent_categorie_preference, code_categorie_preference, libelle_categorie_preference, description_categorie_preference, identifiant_module, position, version_obj) 
VALUES (13, null, null, 'Général', null, null, 1, 0);
INSERT INTO ta_categorie_preference ( id_categorie_preference, id_parent_categorie_preference, code_categorie_preference, libelle_categorie_preference, description_categorie_preference, identifiant_module, position, version_obj) 
VALUES (14, 13, null, 'Code barre', null, null, null, 0);
INSERT INTO ta_categorie_preference (  id_categorie_preference,id_parent_categorie_preference, code_categorie_preference, libelle_categorie_preference, description_categorie_preference, identifiant_module, position, version_obj) 
VALUES (15, 13, null, 'Serveur Mail SMTP', null, null, null, 0);
INSERT INTO ta_categorie_preference (  id_categorie_preference,id_parent_categorie_preference, code_categorie_preference, libelle_categorie_preference, description_categorie_preference, identifiant_module, position, version_obj) 
VALUES (16, 13, null, 'SMS', null, null, null, 0);
INSERT INTO ta_categorie_preference (  id_categorie_preference,id_parent_categorie_preference, code_categorie_preference, libelle_categorie_preference, description_categorie_preference, identifiant_module, position, version_obj) 
VALUES (17, null, null, 'Editions', null, null, 2, 0);
INSERT INTO ta_categorie_preference (  id_categorie_preference,id_parent_categorie_preference, code_categorie_preference, libelle_categorie_preference, description_categorie_preference, identifiant_module, position, version_obj) 
VALUES (18, 20, null, 'Articles', null, null, 4, 0);
INSERT INTO ta_categorie_preference (  id_categorie_preference,id_parent_categorie_preference, code_categorie_preference, libelle_categorie_preference, description_categorie_preference, identifiant_module, position, version_obj) 
VALUES (19, 20, null, 'Tiers', null, null, 4, 0);
INSERT INTO ta_categorie_preference (  id_categorie_preference,id_parent_categorie_preference, code_categorie_preference, libelle_categorie_preference, description_categorie_preference, identifiant_module, position, version_obj) 
VALUES (20, null, null, 'Ecrans', null, null, 3, 0);
INSERT INTO ta_categorie_preference (  id_categorie_preference,id_parent_categorie_preference, code_categorie_preference, libelle_categorie_preference, description_categorie_preference, identifiant_module, position, version_obj) 
VALUES (21, 18, null, 'Liste articles', null, null, 4, 0);

INSERT INTO ta_preferences (groupe, cle, valeur, valeur_defaut, type_donnees, libelle, version_obj, valeurs_possibles, id_categorie_preference, id_groupe_preference, fichier_vue, position, position_dans_groupe, est_un_groupe, description, id_utilisateur) VALUES ('liste_article', 'affichage_filtre_grille', true, null, 'boolean', 'Filtre entête colonne (convient aux petits catalogues articles)', 0, null, 21, null, null, null, null, false, null, null);

-- fin maj pour branch developp après release candidate