CREATE TABLE ta_t_edition
(
  id_t_edition serial NOT NULL,
  code_t_edition dlgr_libcode,
  libelle dlib255,
  description dlib255,
  systeme boolean,
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_t_edition_pkey PRIMARY KEY (id_t_edition),
  CONSTRAINT ta_t_edition_code_unique UNIQUE (code_t_edition)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_t_edition
  OWNER TO postgres;

CREATE INDEX ta_t_edition_code
  ON ta_t_edition
  USING btree
  (code_t_edition COLLATE pg_catalog."default");

CREATE INDEX unq1_ta_t_edition
  ON ta_t_edition
  USING btree
  (code_t_edition COLLATE pg_catalog."default");

CREATE TRIGGER tbi_ta_t_edition
  BEFORE INSERT
  ON ta_t_edition
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

CREATE TRIGGER tbu_ta_t_edition
  BEFORE UPDATE
  ON ta_t_edition
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

CREATE TABLE ta_edition
(
  id_edition serial NOT NULL,
  code_edition dlib255,
  libelle_edition dlib255,
  description_edition text,
  id_t_edition did_facultatif,
  miniature bytea,
  fichier_blob bytea,
  fichier_chemin dlib255,
  fichier_nom dlib255,
  defaut boolean,
  actif boolean,
  systeme boolean,
  importation_manuelle boolean,
  date_importation date_heure_lgr,
  version_edition dlib50,
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_edition_pkey PRIMARY KEY (id_edition),
  CONSTRAINT fk_ta_edition_id_t_edition FOREIGN KEY (id_t_edition)
      REFERENCES ta_t_edition (id_t_edition) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_edition_code_unique UNIQUE (code_edition)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_edition
  OWNER TO postgres;

CREATE INDEX ta_edition_id
  ON ta_edition
  USING btree
  (id_edition);

CREATE INDEX unq1_ta_edition
  ON ta_edition
  USING btree
  (code_edition COLLATE pg_catalog."default");

CREATE TRIGGER tbi_edition
  BEFORE INSERT
  ON ta_edition
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

CREATE TRIGGER tbu_edition
  BEFORE UPDATE
  ON ta_edition
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();
  
  -----------------------------------------------------------------------------------------------------
  ------------------------------------------------------------------------------------------------------
  
  CREATE TABLE ta_action_interne
(
  id serial NOT NULL,
  id_action dlib255,
  libelle dlib255,
  description dlib255,
  systeme boolean,
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_action_interne_pkey PRIMARY KEY (id),
  CONSTRAINT ta_action_interne_id_action UNIQUE (id_action)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_action_interne
  OWNER TO postgres;

CREATE TRIGGER tbi_ta_action_interne
  BEFORE INSERT
  ON ta_action_interne
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

CREATE TRIGGER tbu_ta_action_interne
  BEFORE UPDATE
  ON ta_action_interne
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

CREATE TABLE ta_r_action_edition
(
  id serial NOT NULL,
  id_action did_facultatif NOT NULL,
  id_edition did_facultatif NOT NULL,
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_r_action_edition_pkey PRIMARY KEY (id),
  CONSTRAINT fk_ta_r_action_edition_1 FOREIGN KEY (id_action)
      REFERENCES ta_action_interne (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_r_action_edition_2 FOREIGN KEY (id_edition)
      REFERENCES ta_edition (id_edition) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_r_action_edition
  OWNER TO postgres;

CREATE INDEX fk_ta_r_action_edition_1
  ON ta_r_action_edition
  USING btree
  (id_action);

CREATE INDEX fk_ta_r_action_edition_2
  ON ta_r_action_edition
  USING btree
  (id_edition);

CREATE INDEX ta_r_action_edition_id
  ON ta_r_action_edition
  USING btree
  (id);

CREATE TRIGGER tbi_ta_r_action_edition
  BEFORE INSERT
  ON ta_r_action_edition
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

CREATE TRIGGER tbu_ta_r_action_edition
  BEFORE UPDATE
  ON ta_r_action_edition
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();
  
  
  
  /*  rajout pour les évenements*/
  
  ALTER TABLE ta_r_document_evenement  ADD COLUMN id_fabrication did_facultatif;
ALTER TABLE ta_r_document_evenement  ADD COLUMN id_bon_reception did_facultatif;
  

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
  
ALTER FUNCTION conversion_unite_ref()
  OWNER TO postgres; 

CREATE TRIGGER conversion_u_ref BEFORE INSERT OR UPDATE 
ON ta_mouvement_stock FOR EACH row 
EXECUTE PROCEDURE conversion_unite_ref();

  -----------------------------------------------------------------------------------------------------
  ------------------------------------------------------------------------------------------------------

ALTER TABLE ta_acompte  ADD COLUMN id_etat did_facultatif;
ALTER TABLE ta_apporteur  ADD COLUMN id_etat did_facultatif;
ALTER TABLE ta_avoir  ADD COLUMN id_etat did_facultatif;
ALTER TABLE ta_bonliv  ADD COLUMN id_etat did_facultatif;
ALTER TABLE ta_bon_reception  ADD COLUMN id_etat did_facultatif;
ALTER TABLE ta_fabrication  ADD COLUMN id_etat did_facultatif;

  -----------------------------------------------------------------------------------------------------
  ------------------------------------------------------------------------------------------------------
