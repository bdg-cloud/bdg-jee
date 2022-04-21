---set schema 'demo';

ALTER TABLE ta_status_conformite ADD COLUMN importance integer;

update ta_status_conformite set importance = 200 where code_status_conformite = 'ok';
update ta_status_conformite set importance = 300 where code_status_conformite = 'corrige';
update ta_status_conformite set importance = 500 where code_status_conformite = 'acorriger';
update ta_status_conformite set importance = 400 where code_status_conformite = 'noncorrige';
update ta_status_conformite set importance = 100 where code_status_conformite = 'vide';

ALTER TABLE ta_lot ADD COLUMN id_status_conformite did_facultatif;

ALTER TABLE ta_resultat_conformite
  ADD CONSTRAINT fk_ta_lot_id_status_conformite FOREIGN KEY (id_status_conformite)
      REFERENCES ta_status_conformite (id_status_conformite) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
      

update ta_lot l set id_status_conformite = (select s.id_status_conformite from ta_status_conformite s 
					join ( 
						select f.id_resultat_conformite,f.id_status_conformite 
						from ta_resultat_conformite f 
							join ( select max(quand_cree) as quand, id_conformite
								from ta_resultat_conformite where id_lot=l.id_lot
								group by id_conformite) as f3 
								on (f.quand_cree = f3.quand)
								where f.id_lot= l.id_lot
						) as s1 
						on (s.id_status_conformite=s1.id_status_conformite or s.code_status_conformite='vide') 
						group by s.importance,s.id_status_conformite 
						order by s.importance desc 
						limit 1);
						
						
						
ALTER TABLE ta_adresse
   ADD COLUMN ordre integer NOT NULL DEFAULT 0;	
						
--------------------------------
						
CREATE TABLE ta_r_label_lot
(
  id serial NOT NULL,
  id_lot did_facultatif NOT NULL,
  id_label_article did_facultatif NOT NULL,
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_r_label_lot_pkey PRIMARY KEY (id),
  CONSTRAINT fk_ta_r_label_lot_1 FOREIGN KEY (id_lot)
      REFERENCES ta_lot (id_lot) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_r_label_lot_2 FOREIGN KEY (id_label_article)
      REFERENCES ta_label_article (id_label_article) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_r_label_lot
  OWNER TO postgres;


CREATE INDEX fk_ta_r_label_lot_1
  ON ta_r_label_lot
  USING btree
  (id_lot);


CREATE INDEX fk_ta_r_label_lot_2
  ON ta_r_label_lot
  USING btree
  (id_label_article);

CREATE INDEX ta_r_label_lot_id
  ON ta_r_label_lot
  USING btree
  (id);

CREATE TRIGGER tbi_ta_r_label_lot
  BEFORE INSERT
  ON ta_r_label_lot
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

CREATE TRIGGER tbu_ta_r_label_lot
  BEFORE UPDATE
  ON ta_r_label_lot
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();
  
------------------------------------------------
ALTER TABLE ta_label_article ADD COLUMN logo bytea;
ALTER TABLE ta_label_article DROP COLUMN id_image_label_article;

---------------------------------------------------


						
CREATE TABLE ta_r_label_fabrication
(
  id serial NOT NULL,
  id_fabrication did_facultatif NOT NULL,
  id_label_article did_facultatif NOT NULL,
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_r_label_fabrication_pkey PRIMARY KEY (id),
  CONSTRAINT fk_ta_r_label_fabrication_1 FOREIGN KEY (id_fabrication)
      REFERENCES ta_fabrication (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_r_label_fabrication_2 FOREIGN KEY (id_label_article)
      REFERENCES ta_label_article (id_label_article) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_r_label_fabrication
  OWNER TO postgres;


CREATE INDEX fk_ta_r_label_fabrication_1
  ON ta_r_label_fabrication
  USING btree
  (id_fabrication);


CREATE INDEX fk_ta_r_label_fabrication_2
  ON ta_r_label_fabrication
  USING btree
  (id_label_article);

CREATE INDEX ta_r_label_fabrication_id
  ON ta_r_label_fabrication
  USING btree
  (id);

CREATE TRIGGER tbi_ta_r_label_fabrication
  BEFORE INSERT
  ON ta_r_label_fabrication
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

CREATE TRIGGER tbu_ta_r_label_fabrication
  BEFORE UPDATE
  ON ta_r_label_fabrication
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();
  
CREATE TABLE ta_r_label_reception
(
  id serial NOT NULL,
  id_reception did_facultatif NOT NULL,
  id_label_article did_facultatif NOT NULL,
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_r_label_reception_pkey PRIMARY KEY (id),
  CONSTRAINT fk_ta_r_label_reception_1 FOREIGN KEY (id_reception)
      REFERENCES ta_bon_reception (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_r_label_reception_2 FOREIGN KEY (id_label_article)
      REFERENCES ta_label_article (id_label_article) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_r_label_reception
  OWNER TO postgres;


CREATE INDEX fk_ta_r_label_reception_1
  ON ta_r_label_reception
  USING btree
  (id_reception);


CREATE INDEX fk_ta_r_label_reception_2
  ON ta_r_label_reception
  USING btree
  (id_label_article);

CREATE INDEX ta_r_label_reception_id
  ON ta_r_label_reception
  USING btree
  (id);

CREATE TRIGGER tbi_ta_r_label_reception
  BEFORE INSERT
  ON ta_r_label_reception
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

CREATE TRIGGER tbu_ta_r_label_reception
  BEFORE UPDATE
  ON ta_r_label_reception
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();
---------------------------------------------------------------------------
ALTER TABLE ta_lot ALTER COLUMN qui_cree DROP NOT NULL;


  --------------------------------------------------
  
  ALTER TABLE ta_mouvement_stock ADD COLUMN unite_ref character varying(20);
  ALTER TABLE ta_mouvement_stock ADD COLUMN qte_ref qte3;
  
  ALTER TABLE ta_etat_stock ADD COLUMN unite_ref character varying(20);
  ALTER TABLE ta_etat_stock ADD COLUMN qte_ref qte3;
  
drop FUNCTION conversion_unite_ref() cascade;

CREATE OR REPLACE FUNCTION conversion_unite_ref()
  RETURNS trigger AS
$BODY$
  DECLARE
    id_unite1 integer;
    id_u_ref integer;
    u_ref varchar;
    qte_ref numeric(15,3);
     coef did9facult;
    nb_decimale integer;
    operateur_divise boolean;
    rentre boolean;   
    _id_coef integer;
    _coeff_a_vers_b did9facult;
    _coeff_b_vers_a did9facult;
    _operateur_a_vers_b_divise boolean;
    _operateur_b_vers_a_divise boolean;
    _nb_decimale_a_vers_b integer;
    _nb_decimale_b_vers_a integer;
  BEGIN
        
      IF (TG_OP = 'INSERT' or TG_OP = 'UPDATE') THEN
        rentre:=false;
        new.qte_ref := new.qte1_stock ; /*on traite le cas où il n'y a pas d'unité de référence ou pas de coefficient, */
        new.unite_ref :=new.un1_stock; /*alors on écrit quand même car l'état des stocks tient compte de ces champs uniquement*/
        qte_ref:= new.qte1_stock ;
	    u_ref:=new.un1_stock;
       if (new.un1_stock is not null and new.un1_stock <>'' )THEN
	select u.code_unite into u_ref from ta_article a join ta_lot l on l.id_article=a.id_article join ta_unite u on u.id_unite=a.id_unite_reference where l.id_lot=new.id_lot ;
	if(u_ref is not null)then  	 
          select u.id_unite from ta_unite u where u.code_Unite like new.un1_stock into id_unite1;
          select u.id_unite from ta_unite u where u.code_Unite like u_ref into id_u_ref;
  		
          select cu.id ,cu.coeff_a_vers_b,cu.operateur_a_vers_b_divise,cu.nb_decimale_a_vers_b
           from ta_coefficient_unite cu where cu.id_unite_a = id_unite1 and cu.id_unite_b = id_u_ref into _id_coef,_coeff_a_vers_b,_operateur_a_vers_b_divise, _nb_decimale_a_vers_b;
           if(_id_coef is not null)then
                if(_nb_decimale_a_vers_b is null)then _nb_decimale_a_vers_b :=3;end if;
                nb_decimale := _nb_decimale_a_vers_b;
                coef := _coeff_a_vers_b;
                operateur_divise := _operateur_a_vers_b_divise;
                rentre:=true;
           ELSE
            select cu.id ,cu.coeff_b_vers_a,cu.operateur_b_vers_a_divise,cu.nb_decimale_b_vers_a
           from ta_coefficient_unite cu where cu.id_unite_a = id_u_ref and cu.id_unite_b = id_unite1 into _id_coef,_coeff_b_vers_a,_operateur_b_vers_a_divise, _nb_decimale_b_vers_a;
	  if(_id_coef is not null)then 
		if(_nb_decimale_b_vers_a is null)then _nb_decimale_b_vers_a :=3;end if;
                nb_decimale := _nb_decimale_b_vers_a;
                coef := _coeff_b_vers_a;
                operateur_divise := _operateur_b_vers_a_divise;
                rentre:=true;
	   end if;/*if(_id_coef is not null) - 2*/
	   /*new.unite_ref :=6;*/
	  end if;/*if(_id_coef is not null) - 1*/
		if(rentre)then	
			if(operateur_divise)then
				case 
				when (nb_decimale<=1) then qte_ref := cast(new.qte1_stock / coef as numeric(15,1));             
				when (nb_decimale=2) then qte_ref := cast(new.qte1_stock / coef as numeric(15,2));  
				when (nb_decimale>=3) then qte_ref := cast(new.qte1_stock / coef as numeric(15,3));
				else  qte_ref := cast(new.qte1_stock / coef as numeric(15,3)); 
				end case;	
			ELSE
				case (nb_decimale)
				when (nb_decimale<=1) then qte_ref := cast(new.qte1_stock * coef as numeric(15,1));             
				when (nb_decimale=2) then qte_ref := cast(new.qte1_stock * coef as numeric(15,2));  
				when (nb_decimale>=3) then qte_ref := cast(new.qte1_stock * coef as numeric(15,3));
				else qte_ref := cast(new.qte1_stock * coef as numeric(15,3));  
				end case;	
			end if;/*if(operateur_divise)*/
			new.qte_ref := qte_ref;
			new.unite_ref := u_ref;
		end if;/*(if(rentre)*/


	end if; /*if(u_ref is not null)  	 */
      end if;/*if (new.un1_stock is not null )*/
     end if;/*IF (TG_OP = 'INSERT') */
	return NEW;
  END;/*fin*/
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

CREATE TRIGGER conversion_u_ref BEFORE INSERT OR UPDATE 
ON ta_mouvement_stock FOR EACH row 
EXECUTE PROCEDURE conversion_unite_ref();



drop FUNCTION obtenirtouslesmouvements();

CREATE OR REPLACE FUNCTION obtenirtouslesmouvements()
  RETURNS TABLE(num_lot character varying, id_article integer, libelle dlgr_libcode, id_entrepot did_facultatif, emplacement character varying, 
  qte1_stock numeric, un1_stock character varying, qte2_stock numeric, un2_stock character varying, qte_ref numeric, unite_ref character varying) AS
$BODY$
DECLARE
    
BEGIN

    RETURN QUERY EXECUTE 'SELECT l.numlot as num_lot , a.id_article , a.libellec_article,id_entrepot
,CASE WHEN emplacement is NULL THEN '''' ELSE emplacement END
 ,CASE WHEN qte1_stock is NULL THEN 0 ELSE qte1_stock END
 ,CASE WHEN un1_stock is NULL THEN '''' ELSE un1_stock END
 ,CASE WHEN qte2_stock is NULL THEN 0 ELSE qte2_stock END
 ,CASE WHEN un2_stock is NULL THEN '''' ELSE un2_stock END
 ,CASE WHEN qte_ref is NULL THEN 0 ELSE qte_ref END
 ,CASE WHEN unite_ref is NULL THEN '''' ELSE unite_ref END

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
 ,CASE WHEN sum(qte_ref) is NULL THEN 0 ELSE sum(qte_ref) END
 ,CASE WHEN (unite_ref is NULL or unite_ref='''') THEN '''' ELSE unite_ref END
 
 FROM ta_mouvement_stock ms
 join ta_lot l on (l.id_lot= ms.id_lot) and (l.virtuel=true)
 join ta_article a on l.id_article=a.id_article
group by a.id_article,a.libellec_article,id_entrepot,emplacement,unite_ref,un1_stock,un2_stock';
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
  
 drop FUNCTION recalcul_etat_stock();

CREATE OR REPLACE FUNCTION recalcul_etat_stock()
  RETURNS boolean AS
$BODY$
DECLARE

BEGIN

delete from ta_etat_stock;

 insert into ta_etat_stock ( date_etat_stock, id_article, libelle_etat_stock,num_lot,id_entrepot,emplacement, qte1_etat_stock, un1_etat_stock,
 qte2_etat_stock, un2_etat_stock,qte_ref,unite_ref,
 version_obj)
 select now(),ms.id_article,ms.libelle,ms.num_lot,ms.id_entrepot
 ,ms.emplacement
 , sum(ms.qte1_stock)
 , ms.un1_stock
 , sum( ms.qte2_stock)
 ,ms.un2_stock
  , sum( ms.qte_ref)
 ,ms.unite_ref
 ,'0'
 from obtenirtouslesmouvements() ms
 group by ms.num_lot,ms.id_article,ms.libelle,ms.id_entrepot,ms.emplacement,ms.unite_ref,ms.un1_stock,ms.un2_stock
 order by ms.num_lot,ms.id_entrepot,ms.emplacement ;


 RETURN TRUE;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

 -- simule l'enregistrement de tous les mouvements pour prendre en compte l'unité de référence
update ta_mouvement_stock set libelle_stock=libelle_stock;  
