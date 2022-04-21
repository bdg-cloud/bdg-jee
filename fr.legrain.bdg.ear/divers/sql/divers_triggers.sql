CREATE OR REPLACE FUNCTION public.maj_etat_stock (
)
RETURNS trigger AS
$body$
DECLARE
  id_etat integer;
BEGIN
select es.id_etat_stock into id_etat from ta_etat_stock es where es.id_lot=new.id_lot and ((es.id_entrepot=new.id_entrepot) or (es.id_entrepot is null)) 
and ((es.emplacement=new.emplacement) or (es.emplacement is null)) and es.un1_etat_stock=new.un1_stock and es.un2_etat_stock=new.un2_stock ;
if (id_etat is not null )THEN
  BEGIN
    IF (TG_OP = 'UPDATE') THEN
         update ta_etat_stock set qte1_etat_stock=qte1_etat_stock-old.qte1_stock+new.qte1_stock,qte2_etat_stock=qte2_etat_stock-old.qte2_stock+OLD.qte2_stock;
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
         update ta_etat_stock set qte1_etat_stock=qte1_etat_stock+new.qte1_stock,qte2_etat_stock=qte2_etat_stock+OLD.qte2_stock;
        RETURN NEW;
    END IF;
  end;
  ELSE
    begin
    
      IF  (TG_OP = 'INSERT') THEN
          insert INTO ta_etat_stock ( date_etat_stock , libelle_etat_stock, qte1_etat_stock , un1_etat_stock , qte2_etat_stock,un2_etat_stock , id_entrepot ,
  description, id_lot, version_obj , emplacement)VALUES('now',new.libelle_stock,new.qte1_stock , new.un1_stock , new.qte2_stock,new.un2_stock , new.id_entrepot ,
  new.description, new.id_lot, 0 , new.emplacement) ;

          RETURN NEW;
      END IF;  
    end;
  
  end if;
    RETURN NULL; -- le résultat est ignoré car il s'agit d'un trigger AFTER
END;
$body$
LANGUAGE 'plpgsql'
VOLATILE
CALLED ON NULL INPUT
SECURITY INVOKER
COST 100;

CREATE OR REPLACE FUNCTION public.maj_etat_stock_delete (
)
RETURNS trigger AS
$body$
DECLARE
  id_etat integer;
BEGIN
select es.id_etat_stock into id_etat from ta_etat_stock es where es.id_lot=OLD.id_lot and ((es.id_entrepot=old.id_entrepot) or (es.id_entrepot is null)) 
and ((es.emplacement=old.emplacement) or (es.emplacement is null)) and es.un1_etat_stock=old.un1_stock and es.un2_etat_stock=old.un2_stock ;
if (id_etat is not null )THEN
        update ta_etat_stock  set qte1_etat_stock=qte1_etat_stock-OLD.qte1_stock,qte2_etat_stock=qte2_etat_stock-OLD.qte2_stock;
        RETURN old;
    END IF;
  
    RETURN NULL; -- le résultat est ignoré car il s'agit d'un trigger AFTER
END;
$body$
LANGUAGE 'plpgsql'
VOLATILE
CALLED ON NULL INPUT
SECURITY INVOKER
COST 100;

CREATE TRIGGER ta_mouvement_stock_tr AFTER INSERT OR UPDATE 
ON public.ta_mouvement_stock FOR EACH ROW 
EXECUTE PROCEDURE public.maj_etat_stock();

CREATE TRIGGER ta_mouvement_stock_tr_delete BEFORE DELETE 
ON public.ta_mouvement_stock FOR EACH ROW 
EXECUTE PROCEDURE public.maj_etat_stock_delete();

