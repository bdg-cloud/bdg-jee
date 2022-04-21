
--set schema 'demo';

ALTER TABLE ta_article  ADD COLUMN gestion_stock boolean default true;

update ta_article set gestion_stock=true where gestion_stock is null;


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
 where a.gestion_stock = true

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
 where a.gestion_stock = true
group by l.numlot,a.id_article,a.libellec_article,id_entrepot,emplacement,unite_ref,un1_stock,un2_stock
)';
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION obtenirtouslesmouvements()
  OWNER TO bdg;
  
  
  
  
----- suppression des cascades sur les lots dans fab et BR -------;

CREATE OR REPLACE FUNCTION lot_a_supprimer()
  RETURNS trigger AS
$BODY$
DECLARE  
  idlot integer;
  idMouvement integer;
begin
  select l.id_lot from ta_lot l where id_lot=old.id_lot and (l.virtuel=false or l.virtuel_unique=true ) into idlot;
  if(idlot is not null)then
	IF (TG_OP = 'UPDATE') THEN
	   update ta_mouvement_stock set id_lot=new.id_lot where id_mouvement_stock = old.id_mouvement_stock;
	   if(old.id_lot<>new.id_lot)then delete from Ta_lot where id_lot = idlot; end if;
	   RETURN NEW;
	end if;
	IF (TG_OP = 'DELETE') THEN
	   update ta_mouvement_stock set id_lot=null where id_mouvement_stock = old.id_mouvement_stock;
	   delete from Ta_lot where id_lot = idlot; 
	   RETURN NULL;
	end if;	
    
  end if;
	return null;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION lot_a_supprimer()
  OWNER TO bdg;

CREATE OR REPLACE FUNCTION supprime_lot_non_utilise( OUT success boolean)
  RETURNS boolean AS
$BODY$
DECLARE

num integer;
r record;
 idLot integer;
 requete text;
boutRequete text;
 retour boolean;
BEGIN

 requete='delete  from ta_lot lot where lot.numlot is not null ';
retour=false;
num=0;
 for r in SELECT
    tc.table_schema, 
    tc.constraint_name, 
    tc.table_name, 
    kcu.column_name, 
    ccu.table_schema AS foreign_table_schema,
    ccu.table_name AS foreign_table_name,
    ccu.column_name AS foreign_column_name 
FROM 
    information_schema.table_constraints AS tc 
    JOIN information_schema.key_column_usage AS kcu
      ON tc.constraint_name = kcu.constraint_name
      AND tc.table_schema = kcu.table_schema
    JOIN information_schema.constraint_column_usage AS ccu
      ON ccu.constraint_name = tc.constraint_name
      AND ccu.table_schema = tc.table_schema
WHERE tc.constraint_type = 'FOREIGN KEY' AND ccu.table_name='ta_lot' and ccu.table_schema=current_schema
  loop
  	num=num+1;
	boutRequete=format('and not exists(select * from  '||r.table_name||' l'||num||'  where  l'||num||'.'||r.column_name||' = lot.id_lot )');
	requete=requete ||boutRequete;
  end loop;

    if(num >= 1 )then  
	EXECUTE format(requete);
        retour := TRUE;
     end if; 
success := retour;
 

END
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION supprime_lot_non_utilise()
  OWNER TO bdg;
  
  
  
  
  CREATE TRIGGER tbdelete_l_lot_suppr_lbr
  AFTER DELETE
  ON ta_l_bon_reception
  FOR EACH ROW
  EXECUTE PROCEDURE lot_a_supprimer();


  CREATE TRIGGER tbdelete_l_lot_suppr_lfabpf
  AFTER DELETE
  ON ta_l_fabrication_pf
  FOR EACH ROW
  EXECUTE PROCEDURE lot_a_supprimer();
  
  
  CREATE TRIGGER tbupd_l_lot_suppr_br
  AFTER update
  ON ta_l_bon_reception
  FOR EACH ROW
  EXECUTE PROCEDURE lot_a_supprimer();


  CREATE TRIGGER tbupd_l_lot_suppr_fabpf
  AFTER update
  ON ta_l_fabrication_pf
  FOR EACH ROW
  EXECUTE PROCEDURE lot_a_supprimer();
  
 
 
-- rajout d'une option pour prendre en compte les commentaires dans certaines recherches -----


INSERT INTO ta_categorie_preference
(id_categorie_preference, id_parent_categorie_preference, code_categorie_preference, libelle_categorie_preference, description_categorie_preference, identifiant_module, "position", version_obj)
VALUES(46, null, NULL, 'Recherche', NULL, NULL, 1,  0);

INSERT INTO ta_preferences
( groupe, cle, valeur, valeur_defaut, type_donnees, libelle,  id_categorie_preference, id_groupe_preference, fichier_vue, "position", position_dans_groupe, est_un_groupe, description, id_utilisateur, visible, read_only)
VALUES( 'recherche', 'commentaire_dans_recherche_article', 'false', '', 'boolean', 'Commentaire article dans recherche', 46, NULL, NULL, NULL, NULL, false, NULL, NULL, true, false);
 
INSERT INTO ta_preferences 
( groupe, cle, valeur, valeur_defaut, type_donnees, libelle,  id_categorie_preference, id_groupe_preference, fichier_vue, "position", position_dans_groupe, est_un_groupe, description, id_utilisateur, visible, read_only)
VALUES( 'recherche', 'commentaire_dans_recherche_tiers', 'false', '', 'boolean', 'Commentaire tiers dans recherche', 46, NULL, NULL, NULL, NULL, false, NULL, NULL, true, false);
