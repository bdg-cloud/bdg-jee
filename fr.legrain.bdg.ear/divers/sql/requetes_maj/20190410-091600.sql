--set schema 'demo';

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
				case 
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
ALTER FUNCTION conversion_unite_ref()
  OWNER TO bdg;