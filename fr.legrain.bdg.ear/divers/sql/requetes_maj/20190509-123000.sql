--set schema 'demo';
ALTER TABLE ta_mouvement_stock ALTER COLUMN lettrage_deplacement TYPE dlib255;
INSERT INTO ta_type_mouvement (libelle, code, version_obj, systeme, sens) VALUES ('Déplacement', 'D', 0, false, false);


CREATE OR REPLACE FUNCTION maj_etat_stock_delete()
  RETURNS trigger AS
$BODY$
DECLARE
  id_etat integer;
  numLot varchar;
BEGIN
select l.numlot into numlot from ta_lot l where l.id_lot=OLD.id_lot;
select es.id_etat_stock into id_etat from ta_etat_stock es where es.num_lot=numLot and ((es.id_entrepot=old.id_entrepot) or (es.id_entrepot is null))
and ((es.emplacement=old.emplacement) or (es.emplacement is null)) and  (es.unite_ref is null or es.unite_ref=old.unite_ref) /*and es.un2_etat_stock=old.un2_stock*/ ;
if (id_etat is not null )THEN
        update ta_etat_stock  set qte1_etat_stock=qte1_etat_stock-OLD.qte1_stock,qte2_etat_stock=qte2_etat_stock-OLD.qte2_stock,qte_ref=qte_ref-OLD.qte_ref
        where id_etat_stock=id_etat;
        RETURN NULL;
    END IF;

    RETURN NULL; -- le résultat est ignoré car il s'agit d'un trigger AFTER
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION maj_etat_stock_delete()
  OWNER TO bdg;

  
  -- Function: maj_etat_stock()

-- DROP FUNCTION maj_etat_stock();

CREATE OR REPLACE FUNCTION maj_etat_stock()
  RETURNS trigger AS
$BODY$DECLARE
  id_etat integer;
  numLot varchar;
BEGIN
select l.numlot into numlot from ta_lot l where l.id_lot=new.id_lot;
select es.id_etat_stock into id_etat from ta_etat_stock es where es.num_lot=numLot and ((es.id_entrepot is null)or(es.id_entrepot=new.id_entrepot)  )
and ((es.emplacement is null)or (es.emplacement=new.emplacement) ) and (es.unite_ref is null or es.unite_ref=new.unite_ref) /*and es.un2_etat_stock=new.un2_stock */;
if (id_etat is not null )THEN
  BEGIN
    IF (TG_OP = 'UPDATE') THEN
         update ta_etat_stock set qte1_etat_stock=qte1_etat_stock-old.qte1_stock+new.qte1_stock,qte2_etat_stock=qte2_etat_stock-old.qte2_stock+new.qte2_stock
         ,qte_ref=qte_ref-old.qte_ref+new.qte_ref
         where id_etat_stock=id_etat;
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
         update ta_etat_stock set qte1_etat_stock=qte1_etat_stock+new.qte1_stock,qte2_etat_stock=qte2_etat_stock+new.qte2_stock,qte_ref=qte_ref+new.qte_ref
         where id_etat_stock=id_etat;
        RETURN NEW;
    END IF;
  end;
  ELSE
    begin

      IF  (TG_OP = 'INSERT') THEN
          insert INTO ta_etat_stock ( date_etat_stock , libelle_etat_stock, qte1_etat_stock , un1_etat_stock , qte2_etat_stock,un2_etat_stock , id_entrepot ,
  description, num_lot, unite_ref,qte_ref, version_obj , emplacement)VALUES('now',new.libelle_stock,new.qte1_stock , new.un1_stock , new.qte2_stock,new.un2_stock , new.id_entrepot ,
  new.description, numLot, new.unite_ref, new.qte_ref, 0 , new.emplacement) ;

          RETURN NEW;
      END IF;
    end;

  end if;
    RETURN NULL; -- le résultat est ignoré car il s'agit d'un trigger AFTER
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION maj_etat_stock()
  OWNER TO bdg;
  
  
  
  
  
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
SELECT (''VIRT_''||cast(a.code_article as character varying)) as num_lot ,cast( a.id_article as integer) , 
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
group by a.id_article,a.libellec_article,id_entrepot,emplacement,unite_ref,un1_stock,un2_stock
)';
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION obtenirtouslesmouvements()
  OWNER TO bdg;

