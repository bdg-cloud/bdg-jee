--set schema 'demo';

--changement de la façon de récupérer le numlot d'un lot virtuel à partir des mouvements de stocks
-- voir ligne en commentaire !!! isa
DROP FUNCTION obtenirtouslesmouvements();
CREATE OR REPLACE FUNCTION obtenirtouslesmouvements()
  RETURNS TABLE(num_lot character varying, id_article integer, libelle character varying, id_entrepot integer, emplacement character varying, qte1_stock numeric, un1_stock character varying, qte2_stock numeric, un2_stock character varying, qte_ref numeric, unite_ref character varying) AS
$BODY$
DECLARE
    
BEGIN

    RETURN QUERY EXECUTE 'SELECT l.numlot as num_lot ,cast( a.id_article as integer) , cast(a.libellec_article as character varying),cast( id_entrepot as integer)
,COALESCE(emplacement,'''')
 ,COALESCE(qte1_stock,0)
 ,COALESCE(un1_stock,'''')
 ,COALESCE(qte2_stock,0)
 ,COALESCE(un2_stock,'''')
 ,COALESCE(qte_ref,0)
 ,COALESCE(unite_ref,'''')

 FROM ta_mouvement_stock ms
 join ta_lot l on (l.id_lot= ms.id_lot) and (l.virtuel=false)
 join ta_article a on l.id_article=a.id_article

union all
  (
SELECT 
--(''VIRT_''||cast(a.code_article as character varying)) as num_lot
l.numlot as num_lot 
,cast( a.id_article as integer) , 
  cast(a.libellec_article as character varying),cast( id_entrepot as integer)
,COALESCE(emplacement,'''')
 ,sum(COALESCE(qte1_stock,0))
 ,COALESCE(un1_stock,'''')
 ,sum(COALESCE(qte2_stock,0))
 ,COALESCE(un2_stock,'''')
 ,sum(COALESCE(qte_ref,0))
 ,COALESCE(unite_ref,'''')
 
 FROM ta_mouvement_stock ms
 join ta_lot l on (l.id_lot= ms.id_lot) and (l.virtuel=true)
 join ta_article a on l.id_article=a.id_article
group by l.numlot,a.id_article,a.libellec_article,id_entrepot,emplacement,unite_ref,un1_stock,un2_stock
)';
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION obtenirtouslesmouvements()
  OWNER TO bdg;

  
--unicité de tous les infos documents  
ALTER TABLE ta_infos_acompte
  ADD UNIQUE (id_document);
ALTER TABLE ta_infos_avoir
  ADD UNIQUE (id_document);
ALTER TABLE ta_infos_apporteur
  ADD UNIQUE (id_document);
ALTER TABLE ta_infos_devis
  ADD UNIQUE (id_document);
ALTER TABLE ta_infos_facture
  ADD UNIQUE (id_document);
ALTER TABLE ta_infos_bonliv
  ADD UNIQUE (id_document);
ALTER TABLE ta_infos_boncde
  ADD UNIQUE (id_document);
ALTER TABLE ta_infos_boncde_achat
  ADD UNIQUE (id_document);
ALTER TABLE ta_infos_proforma
  ADD UNIQUE (id_document);
ALTER TABLE ta_infos_prelevement
  ADD UNIQUE (id_document);
ALTER TABLE ta_infos_avis_echeance
  ADD UNIQUE (id_document);
ALTER TABLE ta_infos_ticket_caisse
  ADD UNIQUE (id_document);
ALTER TABLE ta_infos_bon_reception
  ADD UNIQUE (id_document);
ALTER TABLE ta_infos_fabrication
  ADD UNIQUE (id_document);
ALTER TABLE ta_infos_modele_fabrication
  ADD UNIQUE (id_document);

    
INSERT INTO ta_categorie_preference
(id_categorie_preference, id_parent_categorie_preference, code_categorie_preference, libelle_categorie_preference, description_categorie_preference, identifiant_module, "position", qui_cree, quand_cree, qui_modif, quand_modif, "version", ip_acces, version_obj)
VALUES(38, NULL, NULL, 'DLC', NULL, NULL, 20, NULL, null, NULL, null, NULL, NULL, 0);

INSERT INTO ta_preferences
( groupe, cle, valeur, valeur_defaut, type_donnees, libelle,  version_obj, valeurs_possibles, id_categorie_preference, id_groupe_preference, fichier_vue, "position", position_dans_groupe, est_un_groupe, description, id_utilisateur)
VALUES( 'DLC', 'maj_dlc_fabrication', 'true', 'true', 'boolean', 'mise à jour de la DLC / DDM dans fabrication',  0, NULL, 38, NULL, NULL, NULL, NULL, false, NULL, NULL);

  
ALTER TABLE ta_facture ADD COLUMN mouvementer_stock boolean default true;
ALTER TABLE ta_l_facture ADD COLUMN mouvementer_stock boolean default true;

ALTER TABLE ta_apporteur ADD COLUMN mouvementer_stock boolean default true;
ALTER TABLE ta_l_apporteur ADD COLUMN mouvementer_stock boolean default true;

ALTER TABLE ta_l_inventaire ADD COLUMN ligne_controlee boolean default false;

CREATE OR REPLACE FUNCTION recalcul_etat_stock()
  RETURNS boolean AS
$BODY$
DECLARE

BEGIN

delete from ta_etat_stock;

 insert into ta_etat_stock ( date_etat_stock, id_article, libelle_etat_stock,num_lot,id_entrepot,emplacement,
 qte_ref,unite_ref,
 version_obj)
 select now(),ms.id_article,ms.libelle,ms.num_lot,ms.id_entrepot
 ,ms.emplacement
  , sum( ms.qte_ref)
 ,ms.unite_ref
 ,'0'
 from obtenirtouslesmouvements() ms
 group by ms.num_lot,ms.id_article,ms.libelle,ms.id_entrepot,ms.emplacement,ms.unite_ref
 order by ms.num_lot,ms.id_entrepot,ms.emplacement ;


 RETURN TRUE;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION recalcul_etat_stock()
  OWNER TO bdg;



