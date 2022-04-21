--maj_unite_saisie
--set schema 'demo';

ALTER TABLE ta_article   ADD COLUMN id_unite_saisie did_facultatif;
   
ALTER TABLE ta_article
  ADD CONSTRAINT ta_article_id_unite_saisie_fkey FOREIGN KEY (id_unite_saisie)
      REFERENCES ta_unite (id_unite) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
      
      
INSERT INTO ta_preferences
( groupe, cle, valeur, valeur_defaut, type_donnees, libelle,  id_categorie_preference, id_groupe_preference, fichier_vue, "position", position_dans_groupe, est_un_groupe, description, id_utilisateur, visible, read_only)
VALUES( 'articles', 'utilisation_unite_saisie', 'false', '', 'boolean', 'Utilisation d''une unité de saisie dans les documents', 18, NULL, NULL, NULL, NULL, false, NULL, NULL, true, false);
    
--devis      
ALTER TABLE ta_l_devis  ADD COLUMN qte_saisie_l_document qte3;
ALTER TABLE ta_l_devis  ADD COLUMN u_saisie_l_document character varying(20);

ALTER TABLE ta_devis   ADD COLUMN utilise_unite_saisie boolean;
update ta_devis set  utilise_unite_saisie=false;       
    
--bonliv      
ALTER TABLE ta_l_bonliv  ADD COLUMN qte_saisie_l_document qte3;
ALTER TABLE ta_l_bonliv  ADD COLUMN u_saisie_l_document character varying(20);

ALTER TABLE ta_bonliv   ADD COLUMN utilise_unite_saisie boolean;
update ta_bonliv set  utilise_unite_saisie=false;

--boncde      
ALTER TABLE ta_l_boncde  ADD COLUMN qte_saisie_l_document qte3;
ALTER TABLE ta_l_boncde  ADD COLUMN u_saisie_l_document character varying(20);

ALTER TABLE ta_boncde   ADD COLUMN utilise_unite_saisie boolean;
update ta_boncde set  utilise_unite_saisie=false;     

--boncdeAchat      
ALTER TABLE ta_l_boncde_achat  ADD COLUMN qte_saisie_l_document qte3;
ALTER TABLE ta_l_boncde_achat  ADD COLUMN u_saisie_l_document character varying(20);

ALTER TABLE ta_boncde_achat   ADD COLUMN utilise_unite_saisie boolean;
update ta_boncde_achat set  utilise_unite_saisie=false;

--acompte      
ALTER TABLE ta_l_acompte  ADD COLUMN qte_saisie_l_document qte3;
ALTER TABLE ta_l_acompte  ADD COLUMN u_saisie_l_document character varying(20);

ALTER TABLE ta_acompte   ADD COLUMN utilise_unite_saisie boolean;
update ta_acompte set  utilise_unite_saisie=false;

--avis_echeance
ALTER TABLE ta_l_avis_echeance  ADD COLUMN qte_saisie_l_document qte3;
ALTER TABLE ta_l_avis_echeance  ADD COLUMN u_saisie_l_document character varying(20);

ALTER TABLE ta_avis_echeance   ADD COLUMN utilise_unite_saisie boolean;
update ta_avis_echeance set  utilise_unite_saisie=false;

--avoir      
ALTER TABLE ta_l_avoir  ADD COLUMN qte_saisie_l_document qte3;
ALTER TABLE ta_l_avoir  ADD COLUMN u_saisie_l_document character varying(20);

ALTER TABLE ta_avoir   ADD COLUMN utilise_unite_saisie boolean;
update ta_avoir set  utilise_unite_saisie=false;

