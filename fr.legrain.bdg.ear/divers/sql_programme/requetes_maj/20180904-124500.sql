ALTER TABLE ta_edition_catalogue ADD COLUMN resources_path dlib255;
ALTER TABLE ta_edition_catalogue ADD COLUMN theme dlib255;
ALTER TABLE ta_edition_catalogue ADD COLUMN librairie dlib255;

--------------------------------------------------------
-- INSERTION de type d'edition BONLIV
---------------------------------------------------------
    INSERT INTO demo.ta_t_edition
(code_t_edition, libelle, description, systeme)
VALUES('BONLIV', 'Bon livraison', NULL, false);
--------------------------------------------------------
-- INSERTION d' action interne pour l'edition BONLIV
---------------------------------------------------------
INSERT INTO demo.ta_action_interne
(id_action, libelle, description, systeme)
VALUES('fr.legrain.action.edition.bonliv.defaut', 'Edition d''un bon de livraison', NULL, false);
--------------------------------------------------------
-- INSERTION de la relation entre une action et une edition pour BONLIV
-- A FAIRE A LA MAIN AVEC LES ID OBTENUS !!!!!!!!!!------
---------------------------------------------------------
--INSERT INTO demo.ta_r_action_edition
--( id_action, id_edition)
--VALUES( X, X);
--------------------------------------------------------
-- INSERTION de type d'edtion catalogue pour l'edition BONLIV
-------------------------------------------------------
INSERT INTO public.ta_t_edition_catalogue
(code_t_edition_catalogue, libelle, description, systeme)
VALUES('BONLIV', 'bon livraison', NULL, false);