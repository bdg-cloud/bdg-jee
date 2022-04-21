--set schema 'demo';


-------------Debut branche gandiWsStartStopServer---------------

--ajout de catégorie de preference
INSERT INTO ta_categorie_preference (id_categorie_preference, id_parent_categorie_preference, libelle_categorie_preference, position) VALUES (32, null, 'Gwi-Hosting', 14);
INSERT INTO ta_categorie_preference (id_categorie_preference, id_parent_categorie_preference, libelle_categorie_preference, position) VALUES (33, 32, 'Gandi', 15);
INSERT INTO ta_categorie_preference (id_categorie_preference, id_parent_categorie_preference, libelle_categorie_preference, position) VALUES (34, 32, 'OVH', 16);
--ajout de preference        
INSERT INTO ta_preferences (groupe, cle, valeur, valeur_defaut, type_donnees, libelle, id_categorie_preference) VALUES ('Gwi-Hosting', 'cle_api_gandi_prod', '', '', 'string','Clé de production API Gandi',33);
INSERT INTO ta_preferences (groupe, cle, valeur, valeur_defaut, type_donnees, libelle, id_categorie_preference) VALUES ('Gwi-Hosting', 'cle_api_gandi_test', '', '', 'string','Clé de testAPI Gandi',33);
INSERT INTO ta_preferences (groupe, cle, valeur, valeur_defaut, type_donnees, libelle, id_categorie_preference) VALUES ('Gwi-Hosting', 'url_api_gandi_prod', '', '', 'string','Url de production API Gandi',33);
INSERT INTO ta_preferences (groupe, cle, valeur, valeur_defaut, type_donnees, libelle, id_categorie_preference) VALUES ('Gwi-Hosting', 'url_api_gandi_test', '', '', 'string','Url de test API Gandi',33);
INSERT INTO ta_preferences (groupe, cle, valeur, valeur_defaut, type_donnees, libelle, id_categorie_preference) VALUES ('Gwi-Hosting', 'cle_securite_api_gandi', '', '', 'string','Clé de sécurité API Gandi',33);
INSERT INTO ta_preferences (groupe, cle, valeur, valeur_defaut, type_donnees, libelle, id_categorie_preference) VALUES ('Gwi-Hosting', 'reseller_gandi', '', '', 'string','Revendeur Gandi',33);
INSERT INTO ta_preferences (groupe, cle, valeur, valeur_defaut, type_donnees, libelle, id_categorie_preference) VALUES ('Gwi-Hosting', 'affichage_menu_gandi', '', '', 'boolean','Afficher le menu Gandi',33);
INSERT INTO ta_preferences (groupe, cle, valeur, valeur_defaut, type_donnees, libelle, id_categorie_preference) VALUES ('Gwi-Hosting', 'cle_api_ovh_prod', '', '', 'string','Clé de production API Gandi',34);

------------fin maj branche gandiWsStartStopServer

---------------------------------------------------
CREATE TABLE ta_t_transport
(
  id_t_transport serial NOT NULL,
  code_t_transport dlgr_code,
  libl_t_transport dlib100,
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_t_transport_pkey PRIMARY KEY (id_t_transport),
  CONSTRAINT ta_t_transport_code_unique UNIQUE (code_t_transport)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_t_transport
  OWNER TO bdg;


CREATE INDEX ta_t_transport_code
  ON ta_t_transport
  USING btree
  (code_t_transport COLLATE pg_catalog."default");


CREATE INDEX unq1_ta_t_transport
  ON ta_t_transport
  USING btree
  (code_t_transport COLLATE pg_catalog."default");


CREATE TRIGGER tbi_ta_t_transport
  BEFORE INSERT
  ON ta_t_transport
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

CREATE TRIGGER tbu_ta_t_transport
  BEFORE UPDATE
  ON ta_t_transport
  FOR EACH ROW
  EXECUTE PROCEDURE before_update();


  
CREATE TABLE ta_transporteur (
  id_transporteur serial NOT NULL,
  code_transporteur dlgr_code,
  libl_transporteur dlib100,
  id_t_transport did_facultatif,
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_transporteur_pkey PRIMARY KEY (id_transporteur),
  CONSTRAINT fk_ta_transporteur_id_t_transport FOREIGN KEY (id_t_transport) REFERENCES ta_t_transport (id_t_transport) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_transporteur_code_unique UNIQUE (code_transporteur)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_transporteur
  OWNER TO bdg;


CREATE INDEX ta_transporteur_code
  ON ta_transporteur
  USING btree
  (code_transporteur COLLATE pg_catalog."default");


CREATE INDEX unq1_ta_transporteur
  ON ta_transporteur
  USING btree
  (code_transporteur COLLATE pg_catalog."default");


CREATE TRIGGER tbi_ta_transporteur
  BEFORE INSERT
  ON ta_transporteur
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

CREATE TRIGGER tbu_ta_transporteur
  BEFORE UPDATE
  ON ta_transporteur
  FOR EACH ROW
  EXECUTE PROCEDURE before_update();

  
ALTER TABLE ta_facture ADD COLUMN numero_commande_fournisseur dlib20;
ALTER TABLE ta_bonliv ADD COLUMN numero_commande_fournisseur dlib20;
ALTER TABLE ta_boncde ADD COLUMN numero_commande_fournisseur dlib20;

ALTER TABLE ta_boncde ADD COLUMN id_transporteur did_facultatif;
ALTER TABLE ta_bonliv ADD COLUMN id_transporteur did_facultatif;


ALTER TABLE ta_facture ADD COLUMN gestion_lot boolean default false;
ALTER TABLE ta_bonliv ADD COLUMN gestion_lot boolean default false;
ALTER TABLE ta_boncde ADD COLUMN gestion_lot boolean default false;
ALTER TABLE ta_avoir ADD COLUMN gestion_lot boolean default false;
ALTER TABLE ta_bon_reception ADD COLUMN gestion_lot boolean default false;

ALTER TABLE ta_ticket_caisse ADD COLUMN gestion_lot boolean default false;
ALTER TABLE ta_apporteur ADD COLUMN gestion_lot boolean default false;

ALTER TABLE ta_avoir ADD COLUMN mouvementer_stock boolean default true;
ALTER TABLE ta_l_avoir ADD COLUMN mouvementer_stock boolean default true;


--ajout de catégorie de preference
INSERT INTO ta_categorie_preference (id_categorie_preference, id_parent_categorie_preference, libelle_categorie_preference, position) VALUES (35, null, 'Stocks', 17);
INSERT INTO ta_categorie_preference (id_categorie_preference, id_parent_categorie_preference, libelle_categorie_preference, position) VALUES (36, null, 'Lots', 18);

--ajout de preference
INSERT INTO ta_preferences (groupe, cle, valeur, valeur_defaut, type_donnees, libelle, id_categorie_preference) VALUES ('Stocks', 'autoriser_stocks_negatif', '', true, 'boolean','Autoriser les stocks négatifs',35);
INSERT INTO ta_preferences (groupe, cle, valeur, valeur_defaut, type_donnees, libelle, id_categorie_preference) VALUES ('Stocks', 'alerte_stock_inferieur_au_stock_minimum', '', true, 'boolean','Alerte quand le stock est inférieur au stock minimum',35);
INSERT INTO ta_preferences (groupe, cle, valeur, valeur_defaut, type_donnees, libelle, id_categorie_preference) VALUES ('Stocks', 'mouvementer_stock_pour_ligne_avoir', '', true, 'boolean','Mouvementer les stocks des lignes provenant d un avoir',35);

INSERT INTO ta_preferences (groupe, cle, valeur, valeur_defaut, type_donnees, libelle, id_categorie_preference) VALUES ('Lots', 'gestion_des_lots', '', true, 'boolean','Gestion des lots',36);
INSERT INTO ta_preferences (groupe, cle, valeur, valeur_defaut, type_donnees, libelle, id_categorie_preference) VALUES ('Lots', 'autoriser_utilisation_lots_non_conforme', '', false, 'boolean','Autoriser l utilisation des lots non conformes',36);

----------------------------------------------------------------------
----------------------------------------------------------------------
----------------------------------------------------------------------

CREATE OR REPLACE FUNCTION modification_unite_de_reference(
    IN _id_article integer,
    IN id_nouvelle_unite_ref integer
)
  RETURNS INT AS
$BODY$
DECLARE
  u_ref varchar;
  --t_row ta_mouvement_stock%rowtype;
  t_row RECORD;
  id_unite1 integer;
  coef did9facult;
  nb_decimale integer;
  operateur_divise boolean;
  _qte_ref numeric(15,3);
  _id_coef integer;
  _coeff_a_vers_b did9facult;
  _coeff_b_vers_a did9facult;
  _operateur_a_vers_b_divise boolean;
  _operateur_b_vers_a_divise boolean;
  _nb_decimale_a_vers_b integer;
  _nb_decimale_b_vers_a integer;
BEGIN

  select u.code_unite into u_ref from ta_unite u where u.id_unite = id_nouvelle_unite_ref;
  

  if(u_ref is not null)then --on a le code correspondant a la nouvelle unite de reference  	 
      select u.id_unite from ta_unite u join ta_article a on a.id_unite_reference=u.id_unite where a.id_article=_id_article into id_unite1; --recuperation de l'unite de reference actuelle
      --select u.id_unite from ta_unite u where u.code_Unite like u_ref into id_u_ref;
  		
      select cu.id ,cu.coeff_a_vers_b,cu.operateur_a_vers_b_divise,cu.nb_decimale_a_vers_b
      from ta_coefficient_unite cu where cu.id_unite_a = id_unite1 and cu.id_unite_b = id_nouvelle_unite_ref 
      into _id_coef,_coeff_a_vers_b,_operateur_a_vers_b_divise, _nb_decimale_a_vers_b;
      if(_id_coef is not null)then
        if(_nb_decimale_a_vers_b is null)then _nb_decimale_a_vers_b :=3;end if;
        nb_decimale := _nb_decimale_a_vers_b;
        coef := _coeff_a_vers_b;
        operateur_divise := _operateur_a_vers_b_divise;
        --rentre:=true;
      else
        select cu.id ,cu.coeff_b_vers_a,cu.operateur_b_vers_a_divise,cu.nb_decimale_b_vers_a
        from ta_coefficient_unite cu where cu.id_unite_a = id_nouvelle_unite_ref and cu.id_unite_b = id_unite1 
        into _id_coef,_coeff_b_vers_a,_operateur_b_vers_a_divise, _nb_decimale_b_vers_a;
	      if(_id_coef is not null)then 
		      if(_nb_decimale_b_vers_a is null)then _nb_decimale_b_vers_a :=3;end if;
          nb_decimale := _nb_decimale_b_vers_a;
          coef := _coeff_b_vers_a;
          operateur_divise := _operateur_b_vers_a_divise;
          --rentre:=true;
	      end if;/*if(_id_coef is not null) - 2*/
	    end if;/*if(_id_coef is not null) - 1*/

      alter table ta_mouvement_stock disable trigger conversion_u_ref;

      FOR t_row in SELECT * FROM ta_mouvement_stock m join ta_lot l on l.id_lot=m.id_lot join ta_article a on l.id_article=a.id_article where a.id_article=_id_article LOOP
          if(operateur_divise)then
            case 
            when (nb_decimale<=1) then _qte_ref := cast(t_row.qte_ref / coef as numeric(15,1));   --qte1_stock          
            when (nb_decimale=2) then _qte_ref := cast(t_row.qte_ref / coef as numeric(15,2));  
            when (nb_decimale>=3) then _qte_ref := cast(t_row.qte_ref / coef as numeric(15,3));
            else  _qte_ref := cast(t_row.qte_ref / coef as numeric(15,3)); 
            end case;	
          ELSE
            case /*(nb_decimale)*/
            when (nb_decimale<=1) then _qte_ref := cast(t_row.qte_ref * coef as numeric(15,1));             
            when (nb_decimale=2) then _qte_ref := cast(t_row.qte_ref * coef as numeric(15,2));  
            when (nb_decimale>=3) then _qte_ref := cast(t_row.qte_ref * coef as numeric(15,3));
            else _qte_ref := cast(t_row.qte_ref * coef as numeric(15,3));  
            end case;	
          end if;/*if(operateur_divise)*/

        update ta_mouvement_stock mouv
            set qte_ref = _qte_ref, unite_ref = u_ref
        where mouv.id_mouvement_stock = t_row.id_mouvement_stock; --<<< !!! important !!!

      END LOOP;

      alter table ta_mouvement_stock enable trigger conversion_u_ref;
  END if;

  RETURN 1;

END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
