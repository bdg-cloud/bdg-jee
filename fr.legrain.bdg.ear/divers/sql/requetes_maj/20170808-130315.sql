CREATE TABLE ta_t_service_web_externe
(
  id_t_service_web_externe serial NOT NULL,
  code_t_service_web_externe dlgr_libcode,
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
  CONSTRAINT ta_t_service_web_externe_pkey PRIMARY KEY (id_t_service_web_externe),
  CONSTRAINT ta_t_service_web_externe_code_unique UNIQUE (code_t_service_web_externe)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_t_service_web_externe
  OWNER TO postgres;

-- Index: ta_t_service_web_externe_code

-- DROP INDEX ta_t_service_web_externe_code;

CREATE INDEX ta_t_service_web_externe_code
  ON ta_t_service_web_externe
  USING btree
  (code_t_service_web_externe COLLATE pg_catalog."default");

-- Index: unq1_ta_t_service_web_externe

-- DROP INDEX unq1_ta_t_service_web_externe;

CREATE INDEX unq1_ta_t_service_web_externe
  ON ta_t_service_web_externe
  USING btree
  (code_t_service_web_externe COLLATE pg_catalog."default");


-- Trigger: tbi_ta_t_service_web_externe on ta_t_service_web_externe

-- DROP TRIGGER tbi_ta_t_service_web_externe ON ta_t_service_web_externe;

CREATE TRIGGER tbi_ta_t_service_web_externe
  BEFORE INSERT
  ON ta_t_service_web_externe
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

-- Trigger: tbu_ta_t_service_web_externe on ta_t_service_web_externe

-- DROP TRIGGER tbu_ta_t_service_web_externe ON ta_t_service_web_externe;

CREATE TRIGGER tbu_ta_t_service_web_externe
  BEFORE UPDATE
  ON ta_t_service_web_externe
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();
  
  CREATE TABLE ta_t_authentification
(
    --api key, oauth, login/password, ldap, autre
  id_t_authentification serial NOT NULL,
  code_t_authentification dlgr_libcode,
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
  CONSTRAINT ta_t_authentification_pkey PRIMARY KEY (id_t_authentification),
  CONSTRAINT ta_t_authentification_code_unique UNIQUE (code_t_authentification)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_t_authentification
  OWNER TO postgres;

-- Index: ta_t_authentification_code

-- DROP INDEX ta_t_authentification_code;

CREATE INDEX ta_t_authentification_code
  ON ta_t_authentification
  USING btree
  (code_t_authentification COLLATE pg_catalog."default");

-- Index: unq1_ta_t_authentification

-- DROP INDEX unq1_ta_t_authentification;

CREATE INDEX unq1_ta_t_authentification
  ON ta_t_authentification
  USING btree
  (code_t_authentification COLLATE pg_catalog."default");


-- Trigger: tbi_ta_t_authentification on ta_t_authentification

-- DROP TRIGGER tbi_ta_t_authentification ON ta_t_authentification;

CREATE TRIGGER tbi_ta_t_authentification
  BEFORE INSERT
  ON ta_t_authentification
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

-- Trigger: tbu_ta_t_authentification on ta_t_authentification

-- DROP TRIGGER tbu_ta_t_authentification ON ta_t_authentification;

CREATE TRIGGER tbu_ta_t_authentification
  BEFORE UPDATE
  ON ta_t_authentification
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();
  
  CREATE TABLE ta_service_web_externe
(
  id_service_web_externe serial NOT NULL,
  code_service_web_externe dlib255,
  libelle_service_web_externe dlib255,
  description_service_web_externe text,
  id_t_authentification_defaut did_facultatif,
  id_t_service_web_externe did_facultatif,
  url_editeur dlib255,
  url_service dlib255,
  defaut boolean,
  verson_api dlib255,
  type_api dlib255,
  actif boolean,
  api_multicompte boolean,
  systeme boolean,
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  logo bytea,
  CONSTRAINT ta_service_web_externe_pkey PRIMARY KEY (id_service_web_externe),
  CONSTRAINT ta_service_web_externe_code_unique UNIQUE (code_service_web_externe),
    CONSTRAINT fk_ta_compte_service_web_externe_id_t_authentification_defaut FOREIGN KEY (id_t_authentification_defaut)
      REFERENCES ta_t_authentification (id_t_authentification) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
        CONSTRAINT fk_ta_compte_service_web_externe_id_t_service_web_externe FOREIGN KEY (id_t_service_web_externe)
      REFERENCES ta_t_service_web_externe (id_t_service_web_externe) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_service_web_externe
  OWNER TO postgres;

-- Index: ta_service_web_externe_id

-- DROP INDEX ta_service_web_externe_id;

CREATE INDEX ta_service_web_externe_id
  ON ta_service_web_externe
  USING btree
  (id_service_web_externe);

-- Index: unq1_ta_service_web_externe

-- DROP INDEX unq1_ta_service_web_externe;

CREATE INDEX unq1_ta_service_web_externe
  ON ta_service_web_externe
  USING btree
  (code_service_web_externe COLLATE pg_catalog."default");


-- Trigger: tbi_service_web_externe on ta_service_web_externe

-- DROP TRIGGER tbi_service_web_externe ON ta_service_web_externe;

CREATE TRIGGER tbi_service_web_externe
  BEFORE INSERT
  ON ta_service_web_externe
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

-- Trigger: tbu_service_web_externe on ta_service_web_externe

-- DROP TRIGGER tbu_service_web_externe ON ta_service_web_externe;
  
CREATE TRIGGER tbu_service_web_externe
  BEFORE UPDATE
  ON ta_service_web_externe
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();
  
  CREATE TABLE ta_compte_service_web_externe
(
  id_compte_service_web_externe serial NOT NULL,
  code_compte_service_web_externe dlib255,
  libelle_compte_service_web_externe dlib255,
  description_compte_service_web_externe text,
  id_t_authentification did_facultatif,
  id_service_web_externe did_facultatif,
  id_utilisateur did_facultatif,
  url_service dlib255,
  login dlib255,
  password dlib255,
  api_key_1 dlib255,
  api_key_2 dlib255,
  valeur_1 dlib255,
  valeur_2 dlib255,
  valeur_3 dlib255,
  valeur_4 dlib255,
  valeur_5 dlib255,
  valeur_6 dlib255,
  valeur_7 dlib255,
  valeur_8 dlib255,
  valeur_9 dlib255,
  valeur_10 dlib255,
  actif boolean,
  defaut boolean,
  identifiant_de_test boolean,
  debut date_heure_lgr,
  fin date_heure_lgr,
  derniere_utilisation date_heure_lgr,
  systeme boolean,
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_compte_service_web_externe_pkey PRIMARY KEY (id_compte_service_web_externe),
  CONSTRAINT ta_compte_service_web_externe_code_unique UNIQUE (code_compte_service_web_externe),
  CONSTRAINT fk_ta_compte_service_web_externe_id_t_authentification FOREIGN KEY (id_t_authentification)
      REFERENCES ta_t_authentification (id_t_authentification) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_compte_service_web_externe_id_service_web_externe FOREIGN KEY (id_service_web_externe)
      REFERENCES ta_service_web_externe (id_service_web_externe) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_compte_service_web_externe_id_utilisateur FOREIGN KEY (id_utilisateur)
      REFERENCES ta_utilisateur (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_compte_service_web_externe
  OWNER TO postgres;

-- Index: ta_compte_service_web_externe_id

-- DROP INDEX ta_compte_service_web_externe_id;

CREATE INDEX ta_compte_service_web_externe_id
  ON ta_compte_service_web_externe
  USING btree
  (id_compte_service_web_externe);

-- Index: unq1_ta_compte_service_web_externe

-- DROP INDEX unq1_ta_compte_service_web_externe;

CREATE INDEX unq1_ta_compte_service_web_externe
  ON ta_compte_service_web_externe
  USING btree
  (code_compte_service_web_externe COLLATE pg_catalog."default");


-- Trigger: tbi_compte_service_web_externe on ta_compte_service_web_externe

-- DROP TRIGGER tbi_compte_service_web_externe ON ta_compte_service_web_externe;

CREATE TRIGGER tbi_compte_service_web_externe
  BEFORE INSERT
  ON ta_compte_service_web_externe
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

-- Trigger: tbu_compte_service_web_externe on ta_compte_service_web_externe

-- DROP TRIGGER tbu_compte_service_web_externe ON ta_compte_service_web_externe;

CREATE TRIGGER tbu_compte_service_web_externe
  BEFORE UPDATE
  ON ta_compte_service_web_externe
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();
  
----------------------------------------------------------------------------------------------------
----------------------------------------------------------------------------------------------------

--ALTER TABLE ta_compte_service_web_externe ADD COLUMN id_utilisateur did_facultatif;

--ALTER TABLE ta_compte_service_web_externe ADD CONSTRAINT fk_ta_compte_service_web_externe_id_utilisateur FOREIGN KEY (id_utilisateur)
--      REFERENCES ta_utilisateur (id) MATCH SIMPLE
--      ON UPDATE NO ACTION ON DELETE NO ACTION; 
----------------------------------------------------------------------------------------------------
----------------------------------------------------------------------------------------------------
DROP FUNCTION conversion_unite_ref();

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
	select u.code_Unite from ta_article a join ta_lot l on l.id_article=a.id_article join ta_unite u on u.id_unite=a.id_unite_reference  into u_ref;
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
  
  
------=======================================================================================================  
ALTER TABLE ta_reglement  ADD COLUMN compte_client boolean;
COMMENT ON COLUMN ta_reglement.compte_client IS 'indique si règlement vient du compte client';

ALTER TABLE ta_reglement ADD COLUMN ref_paiement dlib255;
COMMENT ON COLUMN ta_reglement.ref_paiement  IS 'référence du paiement stype ou paypal ou autre';

ALTER TABLE ta_reglement ADD COLUMN service dlib100;
COMMENT ON COLUMN ta_reglement.service  IS 'si service est stype ou paypal ou autre';

ALTER TABLE ta_reglement ADD COLUMN id_etat did_facultatif;
COMMENT ON COLUMN ta_reglement.id_etat  IS 'indique etat du règlement';

COMMENT ON COLUMN ta_reglement.etat  IS 'indique si le reglement modifie la facture ou non';

ALTER TABLE ta_reglement ADD COLUMN id_acompte did_facultatif;
ALTER TABLE ta_reglement ADD COLUMN id_prelevement did_facultatif;   
ALTER TABLE ta_reglement ADD COLUMN id_echeancier did_facultatif;

------=======================================================================================================  

  CREATE TABLE ta_log_paiement_internet
(
  id_log_paiement_internet serial NOT NULL,
  date_paiement date_heure_lgr,
  ref_paiement_service dlib255,
  montant_paiement did9facult,
  devise dlib50,
  id_reglement did_facultatif,
  code_reglement dlib255,
  id_tiers did_facultatif,
  code_tiers dlib255,
  nom_tiers dlib255,
  id_document did_facultatif,
  code_document dlib255,
  type_document dlib255,
  origine_paiement dlib255,
  service_paiement dlib255,
  commentaire dlib255,

  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_log_paiement_internet_pkey PRIMARY KEY (id_log_paiement_internet)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_log_paiement_internet
  OWNER TO postgres;

-- Index: ta_log_paiement_internet_id

-- DROP INDEX ta_log_paiement_internet_id;

CREATE INDEX ta_log_paiement_internet_id
  ON ta_log_paiement_internet
  USING btree
  (id_log_paiement_internet);

-- Index: unq1_ta_log_paiement_internet

-- DROP INDEX unq1_ta_log_paiement_internet;

-- Trigger: tbi_log_paiement_internet on ta_log_paiement_internet

-- DROP TRIGGER tbi_log_paiement_internet ON ta_log_paiement_internet;

CREATE TRIGGER tbi_log_paiement_internet
  BEFORE INSERT
  ON ta_log_paiement_internet
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

-- Trigger: tbu_log_paiement_internet on ta_log_paiement_internet

-- DROP TRIGGER tbu_log_paiement_internet ON ta_log_paiement_internet;

CREATE TRIGGER tbu_log_paiement_internet
  BEFORE UPDATE
  ON ta_log_paiement_internet
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();
   
-------------------------------------------------------------------------------------------------------------
ALTER TABLE ta_service_web_externe ADD COLUMN url_gestion_service_web_externe dlib255;

-----------------------------------------------------------------------------------------------------------------

CREATE TABLE ta_message_email
(
  id_email serial NOT NULL,

  id_utilisateur did_facultatif,
  subject dlib255,
  adresse_expediteur dlib255,
  nom_expediteur dlib255,
  body_plain text,
  body_html text,
  encodage dlib50,
  date_creation date_heure_lgr,
  date_envoi date_heure_lgr,
  suivi boolean,
  spam boolean,
  lu boolean,
  accuse_reception_lu boolean,
  recu boolean,
  id_mail_precedent did_facultatif,
  id_mail_suivant did_facultatif,
  smtp dlib255,
  message_id dlib255,
  infos_service text,

  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_message_email_pkey PRIMARY KEY (id_email),
  CONSTRAINT fk_ta_message_email_id_utilisateur FOREIGN KEY (id_utilisateur)
      REFERENCES ta_utilisateur (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_message_email_id_mail_precedent FOREIGN KEY (id_mail_precedent)
      REFERENCES ta_message_email (id_email) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_message_email_id_mail_suivant FOREIGN KEY (id_mail_suivant)
      REFERENCES ta_message_email (id_email) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_message_email
  OWNER TO postgres;

-- Index: ta_message_email_id

-- DROP INDEX ta_message_email_id;

CREATE INDEX ta_message_email_id
  ON ta_message_email
  USING btree
  (id_email);


-- Trigger: tbi_email on ta_message_email

-- DROP TRIGGER tbi_email ON ta_message_email;

CREATE TRIGGER tbi_email
  BEFORE INSERT
  ON ta_message_email
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

-- Trigger: tbu_email on ta_message_email

-- DROP TRIGGER tbu_email ON ta_message_email;

CREATE TRIGGER tbu_email
  BEFORE UPDATE
  ON ta_message_email
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

  ----------------------------------------------------------------------------------------------------------------
  
  CREATE TABLE ta_piece_jointe_email
(
  id_piece_jointe_email serial NOT NULL,

  id_email did_facultatif,
  nom_fichier dlib255,
  taille integer,
  type_mime dlib50,
  fichier bytea,
  fichier_miniature bytea,

  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_piece_jointe_email_pkey PRIMARY KEY (id_piece_jointe_email),
  CONSTRAINT ta_piece_jointe_email_id_email FOREIGN KEY (id_email)
      REFERENCES ta_message_email (id_email) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_piece_jointe_email
  OWNER TO postgres;

-- Index: ta_piece_jointe_email_id

-- DROP INDEX ta_piece_jointe_email_id;

CREATE INDEX ta_piece_jointe_email_id
  ON ta_piece_jointe_email
  USING btree
  (id_piece_jointe_email);


-- Trigger: tbi_piece_jointe_email on ta_piece_jointe_email

-- DROP TRIGGER tbi_piece_jointe_email ON ta_piece_jointe_email;

CREATE TRIGGER tbi_piece_jointe_email
  BEFORE INSERT
  ON ta_piece_jointe_email
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

-- Trigger: tbu_piece_jointe_email on ta_piece_jointe_email

-- DROP TRIGGER tbu_piece_jointe_email ON ta_piece_jointe_email;

CREATE TRIGGER tbu_piece_jointe_email
  BEFORE UPDATE
  ON ta_piece_jointe_email
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();
  --------------------------------------------------------------------------------------------------------------
  CREATE TABLE ta_etiquette_email
(
  id_etiquette_email serial NOT NULL,

  code dlgr_libcode,
  libelle dlib255,
  description dlib255,
  couleur dlib255,
  systeme boolean,
  visible boolean,

  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_etiquette_email_pkey PRIMARY KEY (id_etiquette_email)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_etiquette_email
  OWNER TO postgres;

-- Index: ta_etiquette_email_id

-- DROP INDEX ta_etiquette_email_id;

CREATE INDEX ta_etiquette_email_id
  ON ta_etiquette_email
  USING btree
  (id_etiquette_email);


-- Trigger: tbi_etiquette_email on ta_etiquette_email

-- DROP TRIGGER tbi_etiquette_email ON ta_etiquette_email;

CREATE TRIGGER tbi_etiquette_email
  BEFORE INSERT
  ON ta_etiquette_email
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

-- Trigger: tbu_etiquette_email on ta_etiquette_email

-- DROP TRIGGER tbu_etiquette_email ON ta_etiquette_email;

CREATE TRIGGER tbu_etiquette_email
  BEFORE UPDATE
  ON ta_etiquette_email
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();
  -----------------------------------------------------------------------------------------------------
  CREATE TABLE ta_r_etiquette_email
(
  id serial NOT NULL,
  id_etiquette_email did_facultatif NOT NULL,
  id_email did_facultatif NOT NULL,
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_r_etiquette_email_pkey PRIMARY KEY (id),
  CONSTRAINT ta_r_etiquette_email_id_email_fkey FOREIGN KEY (id_email)
      REFERENCES ta_message_email (id_email) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_r_etiquette_email_id_etiquette_email_fkey FOREIGN KEY (id_etiquette_email)
      REFERENCES ta_etiquette_email (id_etiquette_email) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_r_etiquette_email
  OWNER TO postgres;

-- Index: idx_ta_r_etiquette_email_id_etiquette_email

-- DROP INDEX idx_ta_r_etiquette_email_id_etiquette_email;

CREATE INDEX idx_ta_r_etiquette_email_id_etiquette_email
  ON ta_r_etiquette_email
  USING btree
  (id_etiquette_email);

-- Index: idx_ta_r_etiquette_email_id_email

-- DROP INDEX idx_ta_r_etiquette_email_id_email;

CREATE INDEX idx_ta_r_etiquette_email_id_email
  ON ta_r_etiquette_email
  USING btree
  (id_email);


-- Trigger: tbi_ta_r_etiquette_email on ta_r_etiquette_email

-- DROP TRIGGER tbi_ta_r_etiquette_email ON ta_r_etiquette_email;

CREATE TRIGGER tbi_ta_r_etiquette_email
  BEFORE INSERT
  ON ta_r_etiquette_email
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

-- Trigger: tbu_ta_r_etiquette_email on ta_r_etiquette_email

-- DROP TRIGGER tbu_ta_r_etiquette_email ON ta_r_etiquette_email;

CREATE TRIGGER tbu_ta_r_etiquette_email
  BEFORE UPDATE
  ON ta_r_etiquette_email
  FOR EACH ROW
  EXECUTE PROCEDURE before_update();
  -----------------------------------------------------------------------------------
  CREATE TABLE ta_contact_message_email
(
  id_contact_message_email serial NOT NULL,
  adresseEmail dlib50,
  id_email did_facultatif NOT NULL,
  id_tiers did_facultatif,
  id_email_cc did_facultatif,
  id_email_bcc did_facultatif,
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_contact_message_email_pkey PRIMARY KEY (id_contact_message_email),
  CONSTRAINT ta_contact_message_email_id_tiers_fkey FOREIGN KEY (id_tiers)
      REFERENCES ta_tiers (id_tiers) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_contact_message_email_id_email_fkey FOREIGN KEY (id_email)
      REFERENCES ta_message_email (id_email) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_contact_message_email
  OWNER TO postgres;

-- Index: idx_ta_contact_message_email_id_email

-- DROP INDEX idx_ta_contact_message_email_id_email;

CREATE INDEX idx_ta_contact_message_email_id_email
  ON ta_contact_message_email
  USING btree
  (id_email);

-- Index: idx_ta_contact_message_email_id_tiers

-- DROP INDEX idx_ta_contact_message_email_id_tiers;

CREATE INDEX idx_ta_contact_message_email_id_tiers
  ON ta_contact_message_email
  USING btree
  (id_tiers);


-- Trigger: tbi_ta_contact_message_email on ta_contact_message_email

-- DROP TRIGGER tbi_ta_contact_message_email ON ta_contact_message_email;

CREATE TRIGGER tbi_ta_contact_message_email
  BEFORE INSERT
  ON ta_contact_message_email
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

-- Trigger: tbu_ta_contact_message_email on ta_contact_message_email

-- DROP TRIGGER tbu_ta_contact_message_email ON ta_contact_message_email;

CREATE TRIGGER tbu_ta_contact_message_email
  BEFORE UPDATE
  ON ta_contact_message_email
  FOR EACH ROW
  EXECUTE PROCEDURE before_update();

  --------------------------------------------------------------------------------------------------------------------
  
--------------------------------------------------------------------------------------------------------------
--ALTER TABLE ta_r_reglement   ALTER COLUMN id_reglement DROP NOT NULL;
drop  FUNCTION ta_r_reglement_bd0() CASCADE;