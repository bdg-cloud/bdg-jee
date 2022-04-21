--set schema 'demo';
ALTER TABLE ta_ligne_service_web_externe
ALTER COLUMN date_creation_externe TYPE date_heure_lgr USING date_creation_externe::date_heure_lgr,
ALTER COLUMN date_update_externe TYPE date_heure_lgr USING date_update_externe::date_heure_lgr;


--set schema 'demo';
ALTER TABLE ta_ligne_service_web_externe
ADD COLUMN ref_type_paiement character varying (255);

--set schema 'demo';
ALTER TABLE ta_ligne_service_web_externe
ADD COLUMN id_t_paiement integer;

--set schema 'demo';
ALTER TABLE ta_ligne_service_web_externe
ADD COLUMN etat_ligne_externe character varying (255);


--set schema 'demo';
ALTER TABLE ta_ligne_service_web_externe
ADD COLUMN montant_ht_total_doc character varying (255),
ADD COLUMN montant_ttc_total_doc character varying (255),
ADD COLUMN prix_unitaire character varying (255),
ADD COLUMN montant_total_livraison_doc character varying (255),
ADD COLUMN montant_total_discount_doc character varying (255);


--------- DEBUT MISE A JOUR API ARTICLE ------------------------------
--set schema 'demo';
ALTER TABLE ta_categorie_article ADD COLUMN image_origine bytea;
ALTER TABLE ta_categorie_article ADD COLUMN image_grand bytea;
ALTER TABLE ta_categorie_article ADD COLUMN image_moyen bytea;
ALTER TABLE ta_categorie_article ADD COLUMN image_petit bytea;

ALTER TABLE ta_categorie_article ADD COLUMN url_image dlib255;
ALTER TABLE ta_categorie_article ADD COLUMN mime dlib255;
ALTER TABLE ta_categorie_article ADD COLUMN alt_text dlib255;
ALTER TABLE ta_categorie_article ADD COLUMN aria_text dlib255;

ALTER TABLE ta_categorie_article ADD COLUMN checksum_image_article dlib255;

INSERT INTO ta_gen_code_ex
( code_gen_code, cle_gen_code, valeur_gen_code )
VALUES( 'codeCategorieArticle', 'TaCategorieArticle', 'CAT{@num}' );


ALTER TABLE ta_param_espace_client ADD COLUMN affiche_boutique boolean;
ALTER TABLE ta_param_espace_client ALTER COLUMN affiche_boutique SET DEFAULT false;

ALTER TABLE ta_param_espace_client ADD COLUMN affiche_catalogue boolean;
ALTER TABLE ta_param_espace_client ALTER COLUMN affiche_catalogue SET DEFAULT false;

ALTER TABLE ta_param_espace_client ADD COLUMN active_panier boolean;
ALTER TABLE ta_param_espace_client ALTER COLUMN active_panier SET DEFAULT false;
------------
ALTER TABLE ta_catalogue_web ADD COLUMN id_prix_catalogue_defaut did_facultatif;
ALTER TABLE ta_catalogue_web ADD COLUMN libelle_catalogue dlib255;
ALTER TABLE ta_param_espace_client ADD COLUMN active_email_renseignement_produit boolean;
ALTER TABLE ta_param_espace_client ALTER COLUMN active_email_renseignement_produit SET DEFAULT false;
ALTER TABLE ta_param_espace_client ADD COLUMN active_commande_email_simple boolean;
ALTER TABLE ta_param_espace_client ALTER COLUMN active_commande_email_simple SET DEFAULT false;
ALTER TABLE ta_catalogue_web ALTER COLUMN description_longue_cat_web TYPE text;
ALTER TABLE ta_param_espace_client ADD COLUMN texte_accueil_catalogue text;
alter table ta_catalogue_web add CONSTRAINT ta_catalogue_web_id_prix_catalogue_defaut_fkey FOREIGN KEY (id_prix_catalogue_defaut)
      REFERENCES ta_prix (id_prix) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
------------------------------------------------
ALTER TABLE ta_catalogue_web ADD COLUMN non_disponible_catalogue_web dbool;
ALTER TABLE ta_catalogue_web ADD COLUMN frais_port_additionnel did9facult;
ALTER TABLE ta_catalogue_web ADD COLUMN resume character varying(800);

ALTER TABLE ta_param_espace_client ADD COLUMN frais_port_fixe did9facult;
ALTER TABLE ta_param_espace_client ADD COLUMN frais_port_limite_offert did9facult;
ALTER TABLE ta_param_espace_client ADD COLUMN cgv_cat_web text;
ALTER TABLE ta_param_espace_client ADD COLUMN cgv_file_cat_web bytea;

ALTER TABLE ta_param_espace_client ADD COLUMN afficher_prix_catalogue boolean default false;

ALTER TABLE ta_param_espace_client ADD COLUMN id_article_fdp did_facultatif;
alter table ta_param_espace_client add CONSTRAINT ta_param_espace_client_id_article_fdp_fkey FOREIGN KEY (id_article_fdp)
      REFERENCES ta_article (id_article) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE ta_catalogue_web
DROP COLUMN nouveaute_catalogue_web,
DROP COLUMN exportation_catalogue_web,
DROP COLUMN expediable_catalogue_web,
DROP COLUMN special_catalogue_web,
DROP COLUMN non_disponible_catalogue_web;

ALTER TABLE ta_catalogue_web
ADD COLUMN nouveaute_catalogue_web boolean default false,
ADD COLUMN exportation_catalogue_web boolean default false,
ADD COLUMN expediable_catalogue_web boolean default false,
ADD COLUMN special_catalogue_web boolean default false,
ADD COLUMN non_disponible_catalogue_web boolean default false;
--------- FIN MISE A JOUR API ARTICLE ------------------------------
--set schema 'demo';

INSERT INTO ta_preferences
( groupe, cle, valeur, valeur_defaut, type_donnees, libelle,  id_categorie_preference, id_groupe_preference, fichier_vue, "position", position_dans_groupe, est_un_groupe, description, id_utilisateur, visible, read_only)
VALUES( 'preparation', 'Commentaire', '', '', 'text', 'Commentaire par défaut', 41, NULL, NULL, NULL, NULL, false, NULL, NULL, true, false);
