--SET SCHEMA 'demo';

--set schema 'demo';


--POUR EDITION PAR DEFAUT(YANN)

--Creation de la table ta_action_edition
--set schema 'demo';
CREATE TABLE ta_action_edition (
id_action_edition SERIAL PRIMARY KEY,
code_action_edition dlgr_code,
libl_action_edition dlgr_lib,
qui_cree dlib50,
quand_cree date_heure_lgr,
qui_modif dlib50,
quand_modif date_heure_lgr,
version num_version,
ip_acces dlib50,
version_obj did_version_obj,
CONSTRAINT ta_action_edition_code_unique UNIQUE (code_action_edition) 
);
--Création de la table de relation entre les editions et les actions editions
--set schema 'demo';
CREATE TABLE ta_r_edition_action_edition (
id SERIAL PRIMARY KEY,
id_edition did_facultatif REFERENCES ta_edition(id_edition),
id_action_edition did_facultatif REFERENCES ta_action_edition(id_action_edition),
qui_cree dlib50,
quand_cree date_heure_lgr,
qui_modif dlib50,
quand_modif date_heure_lgr,
version num_version,
ip_acces dlib50,
version_obj did_version_obj
);
--Insertion des actions editions :
--set schema 'demo';
INSERT INTO ta_action_edition (code_action_edition, libl_action_edition) VALUES ('impression', 'Impression');
INSERT INTO ta_action_edition (code_action_edition, libl_action_edition) VALUES ('mail', 'Mail');
INSERT INTO ta_action_edition (code_action_edition, libl_action_edition) VALUES ('espacecli', 'Espace client');


-- isa prise en charge d'un coefficient avec 6 chiffres après la virgule

CREATE DOMAIN DLong6 numeric (15,6);
 
alter table ta_r_prix alter column coef type DLong6;
alter table ta_r_prix_tiers alter column coef type DLong6;
ALTER TABLE ta_t_tarif alter COLUMN valeur type DLong6;
alter table ta_coefficient_unite alter column coeff_a_vers_b type qte3;
alter table ta_coefficient_unite alter column coeff_b_vers_a type qte3;



ALTER TABLE ta_facture ADD COLUMN nb_decimales_qte integer;
ALTER TABLE ta_facture ADD COLUMN nb_decimales_prix integer;

ALTER TABLE ta_avoir ADD COLUMN nb_decimales_qte integer;
ALTER TABLE ta_avoir ADD COLUMN nb_decimales_prix integer;

ALTER TABLE ta_acompte ADD COLUMN nb_decimales_qte integer;
ALTER TABLE ta_acompte ADD COLUMN nb_decimales_prix integer;

ALTER TABLE ta_apporteur ADD COLUMN nb_decimales_qte integer;
ALTER TABLE ta_apporteur ADD COLUMN nb_decimales_prix integer;

ALTER TABLE ta_devis ADD COLUMN nb_decimales_qte integer;
ALTER TABLE ta_devis ADD COLUMN nb_decimales_prix integer;

ALTER TABLE ta_bonliv ADD COLUMN nb_decimales_qte integer;
ALTER TABLE ta_bonliv ADD COLUMN nb_decimales_prix integer;

ALTER TABLE ta_boncde ADD COLUMN nb_decimales_qte integer;
ALTER TABLE ta_boncde ADD COLUMN nb_decimales_prix integer;

ALTER TABLE ta_boncde_achat ADD COLUMN nb_decimales_qte integer;
ALTER TABLE ta_boncde_achat ADD COLUMN nb_decimales_prix integer;

ALTER TABLE ta_prelevement ADD COLUMN nb_decimales_qte integer;
ALTER TABLE ta_prelevement ADD COLUMN nb_decimales_prix integer;

ALTER TABLE ta_proforma ADD COLUMN nb_decimales_qte integer;
ALTER TABLE ta_proforma ADD COLUMN nb_decimales_prix integer;

ALTER TABLE ta_avis_echeance ADD COLUMN nb_decimales_qte integer;
ALTER TABLE ta_avis_echeance ADD COLUMN nb_decimales_prix integer;

ALTER TABLE ta_ticket_caisse ADD COLUMN nb_decimales_qte integer;
ALTER TABLE ta_ticket_caisse ADD COLUMN nb_decimales_prix integer;

ALTER TABLE ta_reglement ADD COLUMN nb_decimales_qte integer;
ALTER TABLE ta_reglement ADD COLUMN nb_decimales_prix integer;

ALTER TABLE ta_l_echeance ADD COLUMN nb_decimales_qte integer;
ALTER TABLE ta_l_echeance ADD COLUMN nb_decimales_prix integer;

ALTER TABLE ta_bon_reception ADD COLUMN nb_decimales_qte integer;
ALTER TABLE ta_fabrication ADD COLUMN nb_decimales_qte integer;
ALTER TABLE ta_modele_fabrication ADD COLUMN nb_decimales_qte integer;




update ta_facture set nb_decimales_qte =3 where nb_decimales_qte is null;
update ta_facture set nb_decimales_prix =2 where nb_decimales_prix is null;

update ta_avoir set nb_decimales_qte=3 where nb_decimales_qte is null;
update ta_avoir set nb_decimales_prix =2 where nb_decimales_prix is null;

update ta_acompte set nb_decimales_qte =3 where nb_decimales_qte is null;
update ta_acompte set nb_decimales_prix= 2 where nb_decimales_prix is null;

update ta_apporteur set nb_decimales_qte= 3 where nb_decimales_qte is null;
update ta_apporteur set nb_decimales_prix =2 where nb_decimales_prix is null;

update ta_devis set nb_decimales_qte =3 where nb_decimales_qte is null;
update ta_devis set nb_decimales_prix =2 where nb_decimales_prix is null;

update ta_bonliv set nb_decimales_qte =3 where nb_decimales_qte is null;
update ta_bonliv set nb_decimales_prix= 2 where nb_decimales_prix is null;

update ta_boncde set nb_decimales_qte= 3 where nb_decimales_qte is null;
update ta_boncde set nb_decimales_prix =2 where nb_decimales_prix is null;

update ta_boncde_achat set nb_decimales_qte =3 where nb_decimales_qte is null;
update ta_boncde_achat set nb_decimales_prix= 2 where nb_decimales_prix is null;

update ta_prelevement set nb_decimales_qte =3 where nb_decimales_qte is null;
update ta_prelevement set nb_decimales_prix= 2 where nb_decimales_prix is null;

update ta_proforma set nb_decimales_qte =3 where nb_decimales_qte is null;
update ta_proforma set nb_decimales_prix =2 where nb_decimales_prix is null;

update ta_avis_echeance set nb_decimales_qte= 3 where nb_decimales_qte is null;
update ta_avis_echeance set nb_decimales_prix =2 where nb_decimales_prix is null;

update ta_ticket_caisse set nb_decimales_qte =3 where nb_decimales_qte is null;
update ta_ticket_caisse set nb_decimales_prix =2 where nb_decimales_prix is null;

update ta_reglement set nb_decimales_qte =3 where nb_decimales_qte is null;
update ta_reglement set nb_decimales_prix =2 where nb_decimales_prix is null;

update ta_l_echeance set nb_decimales_qte =3 where nb_decimales_qte is null;
update ta_l_echeance set nb_decimales_prix =2 where nb_decimales_prix is null;


update ta_bon_reception set nb_decimales_qte =3 where nb_decimales_qte is null;
update ta_fabrication set nb_decimales_qte =3 where nb_decimales_qte is null;
update ta_modele_fabrication set nb_decimales_qte =3 where nb_decimales_qte is null;




INSERT INTO ta_categorie_preference
(id_categorie_preference, id_parent_categorie_preference, code_categorie_preference, libelle_categorie_preference, description_categorie_preference, identifiant_module, "position",  version_obj)
VALUES(39, 20, NULL, 'Document Général', NULL, NULL, 21,0);


ALTER TABLE ta_preferences alter COLUMN libelle type character varying(500);

ALTER TABLE ta_preferences   ADD COLUMN visible boolean;
ALTER TABLE ta_preferences   ADD COLUMN read_only boolean;
update ta_preferences set visible=true,read_only=false;


INSERT INTO ta_preferences
( groupe, cle, valeur, valeur_defaut, type_donnees, libelle,  version_obj, valeurs_possibles, id_categorie_preference, id_groupe_preference, fichier_vue, "position", position_dans_groupe, est_un_groupe, description, id_utilisateur,visible,read_only)
VALUES( 'NbDecimales', 'NbDecimalesMessage', '', '', '', 'Attention !!! Avant de modifier ces valeur, veuillez vérifier avec le service de maintenance que votre dossier est bien compatible avec les valeurs que vous souhaitez !!!',  4, NULL, 39, NULL, NULL, NULL, NULL, false, NULL, NULL,true,false);


INSERT INTO ta_preferences
( groupe, cle, valeur, valeur_defaut, type_donnees, libelle,  version_obj, valeurs_possibles, id_categorie_preference, id_groupe_preference, fichier_vue, "position", position_dans_groupe, est_un_groupe, description, id_utilisateur,visible,read_only)
VALUES( 'NbDecimales', 'NbDecimalesPrix', '2', '2', 'numeric', 'Nombre de décimales pour les prix dans tout le programme',  4, NULL, 39, NULL, NULL, NULL, NULL, false, NULL, NULL,true,true);


--INSERT INTO ta_preferences
--( groupe, cle, valeur, valeur_defaut, type_donnees, libelle,  version_obj, valeurs_possibles, id_categorie_preference, id_groupe_preference, fichier_vue, "position", position_dans_groupe, est_un_groupe, description, id_utilisateur,visible,read_only)
--VALUES( 'NbDecimales', 'NbDecimalesQte', '3', '2', 'numeric', 'Nombre de décimales pour les Quantités dans tout le programme',  4, NULL, 39, NULL, NULL, NULL, NULL, false, NULL, NULL,true,true);



CREATE OR REPLACE FUNCTION conversion_unite_ref()
  RETURNS trigger AS
$BODY$
  DECLARE
    id_unite1 integer;
    id_u_ref integer;
    u_ref varchar;
    qte_ref numeric(15,3);
     coef DLong6;
    nb_decimale integer;
    operateur_divise boolean;
    rentre boolean;   
    _id_coef integer;
    _coeff_a_vers_b  DLong6;
    _coeff_b_vers_a  DLong6;
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
  END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

CREATE TABLE ta_stripe_payment_intent
(
  id_stripe_payment_intent serial NOT NULL,
  id_externe dlib255,
  id_externe_payment_method dlib255,
  id_reglement did_facultatif,
  id_stripe_source did_facultatif,
  id_stripe_card did_facultatif,
  id_stripe_bank_account did_facultatif,
  id_stripe_customer did_facultatif,
  id_stripe_invoice did_facultatif,
  amount did9facult,
  amount_refunded did9facult,
  amount_capturable did9facult,
  amount_received did9facult,
  description dlib255,
  status dlib255,
  methode_validation dlib255,
  paid boolean DEFAULT false,
  captured boolean DEFAULT false,
  refunded boolean DEFAULT false,
  id_facture did_facultatif,
  id_ticket_caisse did_facultatif,
  id_tiers did_facultatif /*NOT NULL*/,
  id_t_paiement did_facultatif,
  export smallint DEFAULT 0,
  net_ttc_calc did9facult,
  compte_client boolean,
  date_intent date_heure_lgr,
  email_ticket dlib255,
  conserver_carte boolean DEFAULT false,
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  date_payment_intent date_heure_lgr,
  CONSTRAINT ta_stripe_payment_intent_pkey PRIMARY KEY (id_stripe_payment_intent),
  CONSTRAINT fk_ta_stripe_payment_intent_id_reglement FOREIGN KEY (id_reglement)
      REFERENCES ta_reglement (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_stripe_payment_intent_id_stripe_bank_account FOREIGN KEY (id_stripe_bank_account)
      REFERENCES ta_stripe_bank_account (id_stripe_bank_account) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_stripe_payment_intent_id_stripe_card FOREIGN KEY (id_stripe_card)
      REFERENCES ta_stripe_card (id_stripe_card) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_stripe_payment_intent_id_stripe_customer FOREIGN KEY (id_stripe_customer)
      REFERENCES ta_stripe_customer (id_stripe_customer) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_stripe_payment_intent_id_stripe_invoice FOREIGN KEY (id_stripe_invoice)
      REFERENCES ta_stripe_invoice (id_stripe_invoice) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_stripe_payment_intent_id_stripe_source FOREIGN KEY (id_stripe_source)
      REFERENCES ta_stripe_source (id_stripe_source) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_stripe_payment_intent_id_fact FOREIGN KEY (id_facture)
      REFERENCES ta_facture (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_stripe_payment_intent_id_ticket FOREIGN KEY (id_ticket_caisse)
      REFERENCES ta_ticket_caisse (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_stripe_payment_intent
  OWNER TO postgres;

-- Index: ta_stripe_payment_intent_id

-- DROP INDEX ta_stripe_payment_intent_id;

CREATE INDEX ta_stripe_payment_intent_id
  ON ta_stripe_payment_intent
  USING btree
  (id_stripe_payment_intent);

-- Index: ta_stripe_payment_intent_id_externe

-- DROP INDEX ta_stripe_payment_intent_id_externe;

CREATE INDEX ta_stripe_payment_intent_id_externe
  ON ta_stripe_payment_intent
  USING btree
  (id_externe COLLATE pg_catalog."default");


-- Trigger: tbi_stripe_payment_intent on ta_stripe_payment_intent

-- DROP TRIGGER tbi_stripe_payment_intent ON ta_stripe_payment_intent;

CREATE TRIGGER tbi_stripe_payment_intent
  BEFORE INSERT
  ON ta_stripe_payment_intent
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

-- Trigger: tbu_stripe_payment_intent on ta_stripe_payment_intent

-- DROP TRIGGER tbu_stripe_payment_intent ON ta_stripe_payment_intent;

CREATE TRIGGER tbu_stripe_payment_intent
  BEFORE UPDATE
  ON ta_stripe_payment_intent
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();
  
 ALTER TABLE ta_stripe_charge ADD COLUMN id_stripe_payment_intent did_facultatif;
 


    
