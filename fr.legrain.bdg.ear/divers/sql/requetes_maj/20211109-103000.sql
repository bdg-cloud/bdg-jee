
--set schema 'demo';

ALTER TABLE ta_avoir ADD COLUMN mouvementer_crd boolean;
ALTER TABLE ta_avoir ALTER COLUMN mouvementer_crd SET DEFAULT true;  

ALTER TABLE ta_l_avoir ADD COLUMN mouvementer_crd boolean;
ALTER TABLE ta_l_avoir ALTER COLUMN mouvementer_crd SET DEFAULT true;   


--ajout de catégorie de preference
INSERT INTO ta_categorie_preference (id_categorie_preference, id_parent_categorie_preference, libelle_categorie_preference, position) VALUES (47, null, 'CRD', 22);


--ajout de preference
INSERT INTO ta_preferences (groupe, cle, valeur, valeur_defaut, type_donnees, libelle, id_categorie_preference,visible,read_only) VALUES ('CRD', 'mouvementerCRDAvoir', '', true, 'boolean','Mouvementer les CRD dans les avoirs',47,true,false);
     