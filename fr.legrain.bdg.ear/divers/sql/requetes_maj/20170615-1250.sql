ALTER TABLE ta_tiers ADD COLUMN email_cle_compte_client_envoye boolean;

ALTER TABLE ta_tiers ADD COLUMN contact boolean;

ALTER TABLE ta_tiers ADD COLUMN cle_liaison_compte_client dlib20;

ALTER TABLE ta_tiers ADD COLUMN date_derniere_connexion_compte_client date_heure_lgr;

ALTER TABLE ta_tiers ADD COLUMN id_contact did_facultatif;

ALTER TABLE ta_tiers ADD CONSTRAINT fk_ta_tiers_contact FOREIGN KEY (id_contact) REFERENCES ta_tiers (id_tiers) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;

CREATE TABLE ta_r_contact (
  id_r_contact serial NOT NULL,
  id_tiers did_facultatif NOT NULL,
  id_tiers_contact did_facultatif NOT NULL,
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_r_contact_pkey PRIMARY KEY (id_r_contact),
  CONSTRAINT ta_r_contact_id_tiers_contact_fkey FOREIGN KEY (id_tiers_contact)
      REFERENCES ta_tiers (id_tiers) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_r_contact_id_tiers_fkey FOREIGN KEY (id_tiers)
      REFERENCES ta_tiers (id_tiers) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

ALTER TABLE ta_coefficient_unite ADD COLUMN operateur_a_vers_b_divise boolean;
ALTER TABLE ta_coefficient_unite ADD COLUMN operateur_b_vers_a_divise boolean;
------
ALTER TABLE ta_etat_stock  ALTER COLUMN num_lot TYPE character varying(50);
ALTER TABLE ta_lot ADD COLUMN virtuel_unique boolean NOT NULL DEFAULT false;


ALTER TABLE ta_etat_stock  ADD COLUMN id_article did_facultatif NOT NULL DEFAULT 0;

delete from ta_etat_stock;

drop function obtenirtouslesmouvements();
CREATE OR REPLACE FUNCTION obtenirtouslesmouvements()
  RETURNS TABLE(num_lot character varying, id_article integer, libelle dlgr_libcode
                          , id_entrepot  did_facultatif ,emplacement  character varying,qte1_stock numeric ,un1_stock character varying,qte2_stock numeric ,un2_stock character varying ) AS
$$
DECLARE
    
BEGIN

    RETURN QUERY EXECUTE 'SELECT l.numlot as num_lot , a.id_article , a.libellec_article,id_entrepot
,CASE WHEN emplacement is NULL THEN '''' ELSE emplacement END
 ,CASE WHEN qte1_stock is NULL THEN 0 ELSE qte1_stock END
 ,CASE WHEN un1_stock is NULL THEN '''' ELSE un1_stock END
 ,CASE WHEN qte2_stock is NULL THEN 0 ELSE qte2_stock END
 ,CASE WHEN un2_stock is NULL THEN '''' ELSE un2_stock END


 FROM ta_mouvement_stock ms
 join ta_lot l on (l.id_lot= ms.id_lot) and (l.virtuel=false)
 join ta_article a on l.id_article=a.id_article

union
  SELECT (''VIRT_''||cast(a.code_article as character varying)) as num_lot , a.id_article , a.libellec_article,id_entrepot
,CASE WHEN emplacement is NULL THEN '''' ELSE emplacement END
 ,CASE WHEN sum(qte1_stock) is NULL THEN 0 ELSE sum(qte1_stock) END
 ,CASE WHEN (un1_stock is NULL or un1_stock='''') THEN '''' ELSE un1_stock END
 ,CASE WHEN sum(qte2_stock) is NULL THEN 0 ELSE sum(qte2_stock) END
 ,CASE WHEN (un2_stock is NULL or un2_stock='''') THEN '''' ELSE un2_stock END

 FROM ta_mouvement_stock ms
 join ta_lot l on (l.id_lot= ms.id_lot) and (l.virtuel=true)
 join ta_article a on l.id_article=a.id_article
group by a.id_article,a.libellec_article,id_entrepot,emplacement,un1_stock,un2_stock';
END;
$$ LANGUAGE plpgsql;



drop FUNCTION recalcul_etat_stock();
CREATE OR REPLACE FUNCTION recalcul_etat_stock()
  RETURNS boolean AS
$BODY$
DECLARE

BEGIN

delete from ta_etat_stock;

 insert into ta_etat_stock ( date_etat_stock, id_article, libelle_etat_stock,num_lot,id_entrepot,emplacement, qte1_etat_stock, un1_etat_stock,
 qte2_etat_stock, un2_etat_stock,
 version_obj)
 select now(),ms.id_article,ms.libelle,ms.num_lot,ms.id_entrepot
 ,ms.emplacement
 , sum(ms.qte1_stock)
 , ms.un1_stock
 , sum( ms.qte2_stock)
 ,ms.un2_stock
 ,'0'
 from obtenirtouslesmouvements() ms
 group by ms.num_lot,ms.id_article,ms.libelle,ms.id_entrepot,ms.emplacement,ms.un1_stock,ms.un2_stock
 order by ms.num_lot,ms.id_entrepot,ms.emplacement ;


 RETURN TRUE;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE;


select * from recalcul_etat_stock();