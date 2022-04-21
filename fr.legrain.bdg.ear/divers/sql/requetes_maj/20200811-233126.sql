--set schema 'demo';

ALTER TABLE ta_r_document DROP CONSTRAINT fk_ta_r_doc_genere;
ALTER TABLE ta_r_document RENAME id_doc_genere  TO id_src;

ALTER TABLE ta_image_article ADD COLUMN image_origine bytea;
ALTER TABLE ta_image_article ADD COLUMN image_grand bytea;
ALTER TABLE ta_image_article ADD COLUMN image_moyen bytea;
ALTER TABLE ta_image_article ADD COLUMN image_petit bytea;

ALTER TABLE ta_image_article ADD COLUMN url_image dlib255;
ALTER TABLE ta_image_article ADD COLUMN mime dlib255;
ALTER TABLE ta_image_article ADD COLUMN alt_text dlib255;
ALTER TABLE ta_image_article ADD COLUMN aria_text dlib255;

ALTER TABLE ta_image_article ADD COLUMN description dlib255;
ALTER TABLE ta_image_article ADD COLUMN position integer;

ALTER TABLE ta_image_article ALTER COLUMN chemin_image_article TYPE dlib255;

ALTER TABLE ta_adresse   ADD COLUMN livraison dlib_commentaire;
ALTER TABLE ta_infos_bonliv   ADD COLUMN livraison dlib_commentaire;
ALTER TABLE ta_infos_boncde   ADD COLUMN livraison dlib_commentaire;

ALTER TABLE ta_adresse   ADD COLUMN latitude_dec varchar(50);
ALTER TABLE ta_infos_bonliv   ADD COLUMN latitude_dec  varchar(50);
ALTER TABLE ta_infos_boncde   ADD COLUMN latitude_dec  varchar(50);
  
ALTER TABLE ta_adresse   ADD COLUMN longitude_dec varchar(50);
ALTER TABLE ta_infos_bonliv   ADD COLUMN longitude_dec  varchar(50);
ALTER TABLE ta_infos_boncde   ADD COLUMN longitude_dec  varchar(50);

INSERT INTO ta_preferences ( groupe, cle, valeur, valeur_defaut, type_donnees, libelle,  version_obj, valeurs_possibles, id_categorie_preference, id_groupe_preference, fichier_vue, "position", position_dans_groupe, est_un_groupe, description, id_utilisateur, visible, read_only) 
VALUES( 'boncde', 'impression_livraison', 'true', 'true', 'boolean', 'Impression infos. livraison', 0, '', 7, NULL, NULL, NULL, NULL, false, NULL, NULL, true, false);

INSERT INTO ta_preferences ( groupe, cle, valeur, valeur_defaut, type_donnees, libelle,  version_obj, valeurs_possibles, id_categorie_preference, id_groupe_preference, fichier_vue, "position", position_dans_groupe, est_un_groupe, description, id_utilisateur, visible, read_only) 
VALUES( 'bonliv', 'impression_livraison', 'true', 'true', 'boolean', 'Impression infos. livraison', 0, '', 3, NULL, NULL, NULL, NULL, false, NULL, NULL, true, false);

