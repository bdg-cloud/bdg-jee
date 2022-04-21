
/*set schema 'demo';*/


drop TABLE ta_traca_document cascade ;
CREATE TABLE ta_traca_document
(
  id_document_traca serial NOT NULL,
  id_document did_facultatif ,
  code_document character varying(20),
  date_document date_lgr,
  libelle_document dlib255,
  id_tiers did_facultatif NOT NULL,
  code_t_paiement character varying(20),
  export smallint DEFAULT 0,
  net_ttc_calc did9facult,
  etat bigint DEFAULT 0,
  date_export timestamp with time zone,
  code_etat character varying(20),
  id_acompte did_facultatif,
  code_acompte character varying(20),
  date_traca date_heure_lgr,
  affectation did9facult,
  date_export_affectation timestamp with time zone,
  code_etat_affectation character varying(20),
  id_affectation did_facultatif,
  id_facture_affectation did_facultatif,
  code_facture_affectation character varying(20),
  date_facture_affectation date_lgr,
  ht_facture_affectation did9facult,
  tva_facture_affectation did9facult,
  ttc_facture_affectation did9facult,
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  type_document  dlib50,
  type_modif dlib50,
  CONSTRAINT ta_traca_document_pkey PRIMARY KEY (id_document_traca),
  CONSTRAINT ta_traca_document_id_tiers_fkey FOREIGN KEY (id_tiers)
      REFERENCES ta_tiers (id_tiers) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_traca_document
  OWNER TO bdg;



CREATE TRIGGER tbi_reglement_traca
  BEFORE INSERT
  ON ta_traca_document
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();


CREATE TRIGGER tbu_reglement_traca
  BEFORE UPDATE
  ON ta_traca_document
  FOR EACH ROW
  EXECUTE PROCEDURE before_update();

  
  ALTER TABLE ta_r_reglement
  ADD COLUMN date_export timestamp with time zone;
    ALTER TABLE ta_r_avoir
  ADD COLUMN date_export timestamp with time zone;

    ALTER TABLE ta_r_reglement
  ADD COLUMN date_verrouillage timestamp with time zone;
    ALTER TABLE ta_r_avoir
  ADD COLUMN date_verrouillage timestamp with time zone;
  
 DROP FUNCTION traca_reglement() cascade;

CREATE OR REPLACE FUNCTION traca_reglement()
  RETURNS trigger AS
$BODY$DECLARE
  r record;
BEGIN
    IF (TG_OP = 'INSERT') THEN
	INSERT INTO ta_traca_document(id_document,code_document, date_document, libelle_document, 
            id_tiers, code_t_paiement, export, net_ttc_calc, etat, date_export, 
            code_etat, code_acompte, date_traca,type_document,type_modif)
	VALUES (new.id_document, new.code_document, new.date_document, new.libelle_document, 
            new.id_tiers,(select c.code_t_paiement from ta_t_paiement c where c.id_t_paiement = new.id_t_paiement), new.export, new.net_ttc_calc, new.etat, new.date_export, 
            (select e.code_etat from ta_etat e where e.id_etat = new.id_etat) , (select a.code_document from ta_acompte a where a.id_document = new.id_acompte) , now(),
            'Reglement','INSERT_REGLEMENT');
        RETURN NEW;
    END IF;   
    IF (TG_OP = 'UPDATE') THEN
	 INSERT INTO ta_traca_document(id_document,code_document, date_document, libelle_document, 
            id_tiers, code_t_paiement, export, net_ttc_calc, etat, date_export, 
            code_etat, code_acompte, date_traca,type_document,type_modif)
	VALUES (new.id_document, new.code_document, new.date_document, new.libelle_document, 
            new.id_tiers,(select c.code_t_paiement from ta_t_paiement c where c.id_t_paiement = new.id_t_paiement), new.export, new.net_ttc_calc, new.etat, new.date_export, 
            (select e.code_etat from ta_etat e where e.id_etat = new.id_etat) , (select a.code_document from ta_acompte a where a.id_document = new.id_acompte) , now(),
            'Reglement','UPDATE_REGLEMENT');

        RETURN NEW;
    END IF;
    IF (TG_OP = 'DELETE') THEN
	 INSERT INTO ta_traca_document(id_document,code_document, date_document, libelle_document, 
            id_tiers, code_t_paiement, export, net_ttc_calc, etat, date_export, 
            code_etat, code_acompte, date_traca,type_document,type_modif)
	VALUES (old.id_document, old.code_document, old.date_document, old.libelle_document, 
            old.id_tiers,(select c.code_t_paiement from ta_t_paiement c where c.id_t_paiement = old.id_t_paiement), old.export, old.net_ttc_calc, old.etat, old.date_export, 
            (select e.code_etat from ta_etat e where e.id_etat = old.id_etat) , (select a.code_document from ta_acompte a where a.id_document = old.id_acompte) , now(),
            'Reglement','DELETE_REGLEMENT');
        RETURN NEW;
    END IF;

    RETURN NULL; -- le résultat est ignoré car il s'agit d'un trigger AFTER
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION traca_reglement()
  OWNER TO bdg;
  
 


 DROP FUNCTION traca_affectation_reglement() cascade;

CREATE OR REPLACE FUNCTION traca_affectation_reglement()
  RETURNS trigger AS
$BODY$DECLARE
 r record;
 f record;
BEGIN
    IF (TG_OP = 'INSERT') THEN
	select reg.* from ta_reglement reg where reg.id_document = new.id_reglement into r;	
	select fac.* from ta_facture fac where fac.id_document = new.id_facture into f;
	if(r.id_tiers is not null)then INSERT INTO ta_traca_document( id_document,code_document, date_document, libelle_document, 
            id_tiers, code_t_paiement, export, net_ttc_calc, etat, date_export, 
            code_etat, code_acompte, date_traca, affectation, date_export_affectation,id_affectation, 
             code_facture_affectation, date_facture_affectation, 
            ht_facture_affectation, tva_facture_affectation, ttc_facture_affectation,type_document, 
            type_modif)
	VALUES ( r.id_document, r.code_document, r.date_document, r.libelle_document, 
            r.id_tiers,(select c.code_t_paiement from ta_t_paiement c where c.id_t_paiement = r.id_t_paiement), r.export, r.net_ttc_calc, r.etat, r.date_export, 
            (select e.code_etat from ta_etat e where e.id_etat = r.id_etat) , (select a.code_document from ta_acompte a where a.id_document = r.id_acompte) , now()
            , new.affectation, new.date_export,new.id, 
            f.code_document, f.date_document, 
            f.net_ht_calc, f.net_tva_calc, f.net_ttc_calc,
            'Reglement','INSERT_AFFECTATION_REGLEMENT');
            end if;
        RETURN NEW;
    END IF;   
    IF (TG_OP = 'UPDATE') THEN
	select reg.* from ta_reglement reg where reg.id_document = new.id_reglement into r;	
	select fac.* from ta_facture fac where fac.id_document = new.id_facture into f;
	if(r.id_tiers is not null)then INSERT INTO ta_traca_document( id_document,code_document, date_document, libelle_document, 
            id_tiers, code_t_paiement, export, net_ttc_calc, etat, date_export, 
            code_etat, code_acompte, date_traca, affectation, date_export_affectation,id_affectation, 
             code_facture_affectation, date_facture_affectation, 
            ht_facture_affectation, tva_facture_affectation, ttc_facture_affectation,type_document, 
            type_modif)
	VALUES ( r.id_document, r.code_document, r.date_document, r.libelle_document, 
            r.id_tiers,(select c.code_t_paiement from ta_t_paiement c where c.id_t_paiement = r.id_t_paiement), r.export, r.net_ttc_calc, r.etat, r.date_export, 
            (select e.code_etat from ta_etat e where e.id_etat = r.id_etat) , (select a.code_document from ta_acompte a where a.id_document = r.id_acompte) , now()
            , new.affectation, new.date_export,new.id, 
            f.code_document, f.date_document, 
            f.net_ht_calc, f.net_tva_calc, f.net_ttc_calc,
            'Reglement','UPDATE_AFFECTATION_REGLEMENT');
            end if;
        RETURN NEW;
    END IF;
    IF (TG_OP = 'DELETE') THEN
	select reg.* from ta_reglement reg where reg.id_document = old.id_reglement into r;	
	select fac.* from ta_facture fac where fac.id_document = old.id_facture into f;
	if(r.id_tiers is not null)then INSERT INTO ta_traca_document( id_document,code_document, date_document, libelle_document, 
            id_tiers, code_t_paiement, export, net_ttc_calc, etat, date_export, 
            code_etat, code_acompte, date_traca, affectation, date_export_affectation,id_affectation,  
             code_facture_affectation, date_facture_affectation, 
            ht_facture_affectation, tva_facture_affectation, ttc_facture_affectation,type_document, 
            type_modif)
	VALUES ( r.id_document,r.code_document, r.date_document, r.libelle_document, 
            r.id_tiers,(select c.code_t_paiement from ta_t_paiement c where c.id_t_paiement = r.id_t_paiement), r.export, r.net_ttc_calc, r.etat, r.date_export, 
            (select e.code_etat from ta_etat e where e.id_etat = r.id_etat) , (select a.code_document from ta_acompte a where a.id_document = r.id_acompte) , now()
            , old.affectation, old.date_export,old.id,  
            f.code_document, f.date_document, 
            f.net_ht_calc, f.net_tva_calc, f.net_ttc_calc,
            'Reglement','DELETE_AFFECTATION_REGLEMENT');
            end if;
        RETURN NEW;
    END IF;

    RETURN NULL; -- le résultat est ignoré car il s'agit d'un trigger AFTER
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION traca_affectation_reglement()
  OWNER TO bdg;

  
CREATE TRIGGER traca_reglement AFTER INSERT OR UPDATE OR DELETE
   ON ta_reglement FOR EACH ROW
   EXECUTE PROCEDURE traca_reglement();  

CREATE TRIGGER traca_affectation_reglement AFTER INSERT OR UPDATE OR DELETE
   ON ta_r_reglement FOR EACH ROW
   EXECUTE PROCEDURE traca_affectation_reglement();
   
   
DROP FUNCTION traca_avoir() cascade;

CREATE OR REPLACE FUNCTION traca_avoir()
  RETURNS trigger AS
$BODY$DECLARE
  r record;
BEGIN
    IF (TG_OP = 'INSERT') THEN
	 INSERT INTO ta_traca_document(id_document,code_document, date_document, libelle_document, 
            id_tiers, code_t_paiement, export, net_ttc_calc, date_export, 
            code_etat,  date_traca,type_document,type_modif)
	VALUES (new.id_document, new.code_document, new.date_document, new.libelle_document, 
            new.id_tiers,(select c.code_t_paiement from ta_t_paiement c where c.id_t_paiement = new.id_t_paiement), new.export, new.net_ttc_calc,  new.date_export, 
            (select e.code_etat from ta_etat e where e.id_etat = new.id_etat), now(),
            'Avoir','INSERT_AVOIR');
        RETURN NEW;
    END IF;   
    IF (TG_OP = 'UPDATE') THEN
	 INSERT INTO ta_traca_document(id_document,code_document, date_document, libelle_document, 
            id_tiers, code_t_paiement, export, net_ttc_calc, date_export, 
            code_etat,  date_traca,type_document,type_modif)
	VALUES (new.id_document, new.code_document, new.date_document, new.libelle_document, 
            new.id_tiers,(select c.code_t_paiement from ta_t_paiement c where c.id_t_paiement = new.id_t_paiement), new.export, new.net_ttc_calc,  new.date_export, 
            (select e.code_etat from ta_etat e where e.id_etat = new.id_etat), now(),
            'Avoir','UPDATE_AVOIR');
        RETURN NEW;
    END IF;
    IF (TG_OP = 'DELETE') THEN
	 INSERT INTO ta_traca_document(id_document,code_document, date_document, libelle_document, 
            id_tiers, code_t_paiement, export, net_ttc_calc,  date_export, 
            code_etat, date_traca,type_document,type_modif)
	VALUES (old.id_document, old.code_document, old.date_document, old.libelle_document, 
            old.id_tiers,(select c.code_t_paiement from ta_t_paiement c where c.id_t_paiement = old.id_t_paiement), old.export, old.net_ttc_calc, old.date_export, 
            (select e.code_etat from ta_etat e where e.id_etat = old.id_etat) , now(),
            'Avoir','DELETE_AVOIR');
        RETURN NEW;
    END IF;

    RETURN NULL; -- le résultat est ignoré car il s'agit d'un trigger AFTER
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION traca_avoir()
  OWNER TO bdg;
   
 
   DROP FUNCTION traca_affectation_avoir() cascade;

CREATE OR REPLACE FUNCTION traca_affectation_avoir()
  RETURNS trigger AS
$BODY$DECLARE
 r record;
 f record;
BEGIN
     IF (TG_OP = 'INSERT') THEN
	select reg.* from ta_avoir reg where reg.id_document = new.id_avoir into r;	
	select fac.* from ta_facture fac where fac.id_document = new.id_facture into f;
	if(r.id_tiers is not null)then INSERT INTO ta_traca_document( id_document,code_document, date_document, libelle_document, 
            id_tiers, code_t_paiement, export, net_ttc_calc, date_export, 
            code_etat,  date_traca, affectation, date_export_affectation,id_affectation, 
             code_facture_affectation, date_facture_affectation, 
            ht_facture_affectation, tva_facture_affectation, ttc_facture_affectation,type_document, 
            type_modif)
	VALUES ( r.id_document, r.code_document, r.date_document, r.libelle_document, 
            r.id_tiers,(select c.code_t_paiement from ta_t_paiement c where c.id_t_paiement = r.id_t_paiement), r.export, r.net_ttc_calc,  r.date_export, 
            (select e.code_etat from ta_etat e where e.id_etat = r.id_etat)  , now()
            , new.affectation, new.date_export,new.id, 
            f.code_document, f.date_document, 
            f.net_ht_calc, f.net_tva_calc, f.net_ttc_calc,
            'Avoir','INSERT_AFFECTATION_AVOIR');
            end if;
        RETURN NEW;
    END IF;     
    IF (TG_OP = 'UPDATE') THEN
	select reg.* from ta_avoir reg where reg.id_document = new.id_avoir into r;	
	select fac.* from ta_facture fac where fac.id_document = new.id_facture into f;
	if(r.id_tiers is not null)then INSERT INTO ta_traca_document( id_document,code_document, date_document, libelle_document, 
            id_tiers, code_t_paiement, export, net_ttc_calc, date_export, 
            code_etat,  date_traca, affectation, date_export_affectation,id_affectation, 
             code_facture_affectation, date_facture_affectation, 
            ht_facture_affectation, tva_facture_affectation, ttc_facture_affectation,type_document, 
            type_modif)
	VALUES ( r.id_document, r.code_document, r.date_document, r.libelle_document, 
            r.id_tiers,(select c.code_t_paiement from ta_t_paiement c where c.id_t_paiement = r.id_t_paiement), r.export, r.net_ttc_calc,  r.date_export, 
            (select e.code_etat from ta_etat e where e.id_etat = r.id_etat)  , now()
            , new.affectation, new.date_export,new.id, 
            f.code_document, f.date_document, 
            f.net_ht_calc, f.net_tva_calc, f.net_ttc_calc,
            'Avoir','UPDATE_AFFECTATION_AVOIR');
            end if;
        RETURN NEW;
    END IF;
    IF (TG_OP = 'DELETE') THEN
	select reg.* from ta_avoir reg where reg.id_document = old.id_avoir into r;	
	select fac.* from ta_facture fac where fac.id_document = old.id_facture into f;
	if(r.id_tiers is not null)then INSERT INTO ta_traca_document( id_document,code_document, date_document, libelle_document, 
            id_tiers, code_t_paiement, export, net_ttc_calc,  date_export, 
            code_etat,  date_traca, affectation, date_export_affectation,id_affectation,  
             code_facture_affectation, date_facture_affectation, 
            ht_facture_affectation, tva_facture_affectation, ttc_facture_affectation,type_document, 
            type_modif)
	VALUES ( r.id_document,r.code_document, r.date_document, r.libelle_document, 
            r.id_tiers,(select c.code_t_paiement from ta_t_paiement c where c.id_t_paiement = r.id_t_paiement), r.export, r.net_ttc_calc, r.date_export, 
            (select e.code_etat from ta_etat e where e.id_etat = r.id_etat)  , now()
            , old.affectation, old.date_export,old.id,  
            f.code_document, f.date_document, 
            f.net_ht_calc, f.net_tva_calc, f.net_ttc_calc,
            'Avoir','DELETE_AFFECTATION_AVOIR');
            end if;
        RETURN NEW;
    END IF;

    RETURN NULL; -- le résultat est ignoré car il s'agit d'un trigger AFTER
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION traca_affectation_avoir()
  OWNER TO bdg;
  
  CREATE TRIGGER traca_avoir AFTER INSERT OR UPDATE OR DELETE
   ON ta_avoir FOR EACH ROW
   EXECUTE PROCEDURE traca_avoir();  

CREATE TRIGGER traca_affectation_avoir AFTER INSERT OR UPDATE OR DELETE
   ON ta_r_avoir FOR EACH ROW
   EXECUTE PROCEDURE traca_affectation_avoir();
   
   
DROP FUNCTION traca_facture() cascade;
   
CREATE OR REPLACE FUNCTION traca_facture()
  RETURNS trigger AS
$BODY$DECLARE
  r record;
BEGIN
    IF (TG_OP = 'INSERT') THEN
	 INSERT INTO ta_traca_document(id_document,code_document, date_document, libelle_document, 
            id_tiers, code_t_paiement, export, net_ttc_calc, date_export, 
            code_etat,  date_traca,type_document,type_modif)
	VALUES (new.id_document, new.code_document, new.date_document, new.libelle_document, 
            new.id_tiers,(select c.code_t_paiement from ta_t_paiement c where c.id_t_paiement = new.id_t_paiement), new.export, new.net_ttc_calc,  new.date_export, 
            (select e.code_etat from ta_etat e where e.id_etat = new.id_etat), now(),
            'Facture','INSERT_FACTURE');
        RETURN NEW;
    END IF;  
    IF (TG_OP = 'UPDATE') THEN
	 INSERT INTO ta_traca_document(id_document,code_document, date_document, libelle_document, 
            id_tiers, code_t_paiement, export, net_ttc_calc, date_export, 
            code_etat,  date_traca,type_document,type_modif)
	VALUES (new.id_document, new.code_document, new.date_document, new.libelle_document, 
            new.id_tiers,(select c.code_t_paiement from ta_t_paiement c where c.id_t_paiement = new.id_t_paiement), new.export, new.net_ttc_calc,  new.date_export, 
            (select e.code_etat from ta_etat e where e.id_etat = new.id_etat), now(),
            'Facture','UPDATE_FACTURE');
        RETURN NEW;
    END IF;
    IF (TG_OP = 'DELETE') THEN
	 INSERT INTO ta_traca_document(id_document,code_document, date_document, libelle_document, 
            id_tiers, code_t_paiement, export, net_ttc_calc,  date_export, 
            code_etat, date_traca,type_document,type_modif)
	VALUES (old.id_document, old.code_document, old.date_document, old.libelle_document, 
            old.id_tiers,(select c.code_t_paiement from ta_t_paiement c where c.id_t_paiement = old.id_t_paiement), old.export, old.net_ttc_calc, old.date_export, 
            (select e.code_etat from ta_etat e where e.id_etat = old.id_etat) , now(),
            'Facture','DELETE_FACTURE');
        RETURN NEW;
    END IF;

    RETURN NULL; -- le résultat est ignoré car il s'agit d'un trigger AFTER
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION traca_facture()
  OWNER TO bdg;
   
 



  
  CREATE TRIGGER traca_facture AFTER INSERT OR UPDATE OR DELETE
   ON ta_facture FOR EACH ROW
   EXECUTE PROCEDURE traca_facture(); 
   
   
drop TABLE ta_traca_ligne cascade ;
CREATE TABLE ta_traca_ligne
(
  id_ligne_traca serial NOT NULL,
  id_document did_facultatif ,
  id_l_document did_facultatif ,
  code_article character varying(20),
  id_article did_facultatif not null,
  lib_l_document dlib255,
  qte_l_document qte3,
  qte2_l_document qte3,
  u1_l_document character varying(20),
  u2_l_document character varying(20),
  prix_u_l_document did9facult,
  taux_tva_l_document numeric(15,4) DEFAULT 0,
  compte_l_document dlib8,
  code_tva_l_document character varying(20),
  code_t_tva_l_document character varying(1),
  mt_ht_l_document did9facult,
  mt_ttc_l_document did9facult,
  rem_tx_l_document did9facult,
  rem_ht_l_document did9facult,
  mt_ht_apr_rem_globale did9facult,
  mt_ttc_apr_rem_globale did9facult,
  qte_titre_transport_l_document integer DEFAULT 0,
  titre_transport_l_document dlib50,
  lib_tva_l_document character varying(255),
  id_lot did_facultatif,
  id_entrepot did_facultatif,
  emplacement_l_document dlib255,
  id_mouvement_stock did_facultatif,
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  type_document  dlib50,
  type_modif dlib50,
  CONSTRAINT ta_traca_ligne_pkey PRIMARY KEY (id_ligne_traca)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_traca_ligne
  OWNER TO bdg;



CREATE TRIGGER tbi_traca_ligne
  BEFORE INSERT
  ON ta_traca_ligne
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();


CREATE TRIGGER tbu_traca_ligne
  BEFORE UPDATE
  ON ta_traca_ligne
  FOR EACH ROW
  EXECUTE PROCEDURE before_update();


DROP FUNCTION traca_ligne_facture() cascade;
   
CREATE OR REPLACE FUNCTION traca_ligne_facture()
  RETURNS trigger AS
$BODY$DECLARE
  r record;
BEGIN
	
    IF (TG_OP = 'INSERT') THEN
	INSERT INTO ta_traca_ligne( id_document, id_l_document, code_article, id_article, 
            lib_l_document, qte_l_document, qte2_l_document, u1_l_document, 
            u2_l_document, prix_u_l_document, taux_tva_l_document, compte_l_document, 
            code_tva_l_document, code_t_tva_l_document, mt_ht_l_document, 
            mt_ttc_l_document, rem_tx_l_document, rem_ht_l_document, mt_ht_apr_rem_globale, 
            mt_ttc_apr_rem_globale, 
            lib_tva_l_document,  type_document, type_modif)
	VALUES ( new.id_document, new.id_l_document,(select art.code_article from ta_article art where art.id_article=new.id_article) ,new.id_article,new.lib_l_document, 
            new.qte_l_document, new.qte2_l_document, new.u1_l_document, 
            new.u2_l_document, new.prix_u_l_document, new.taux_tva_l_document, new.compte_l_document, 
            new.code_tva_l_document, new.code_t_tva_l_document, new.mt_ht_l_document, 
            new.mt_ttc_l_document, new.rem_tx_l_document, new.rem_ht_l_document, new.mt_ht_apr_rem_globale, 
            new.mt_ttc_apr_rem_globale, new.lib_tva_l_document,  'ligne_facture', 'INSERT_LIGNE_FACTURE'); 
        RETURN NEW;
    END IF;   
    IF (TG_OP = 'UPDATE') THEN
	INSERT INTO ta_traca_ligne( id_document, id_l_document, code_article, id_article, 
            lib_l_document, qte_l_document, qte2_l_document, u1_l_document, 
            u2_l_document, prix_u_l_document, taux_tva_l_document, compte_l_document, 
            code_tva_l_document, code_t_tva_l_document, mt_ht_l_document, 
            mt_ttc_l_document, rem_tx_l_document, rem_ht_l_document, mt_ht_apr_rem_globale, 
            mt_ttc_apr_rem_globale, 
            lib_tva_l_document,  type_document, type_modif)
	VALUES ( new.id_document, new.id_l_document,(select art.code_article from ta_article art where art.id_article=new.id_article) ,new.id_article,new.lib_l_document, 
            new.qte_l_document, new.qte2_l_document, new.u1_l_document, 
            new.u2_l_document, new.prix_u_l_document, new.taux_tva_l_document, new.compte_l_document, 
            new.code_tva_l_document, new.code_t_tva_l_document, new.mt_ht_l_document, 
            new.mt_ttc_l_document, new.rem_tx_l_document, new.rem_ht_l_document, new.mt_ht_apr_rem_globale, 
            new.mt_ttc_apr_rem_globale, new.lib_tva_l_document,  'ligne_facture', 'UPDATE_LIGNE_FACTURE'); 
        RETURN NEW;
    END IF;
    IF (TG_OP = 'DELETE') THEN
	INSERT INTO ta_traca_ligne( id_document, id_l_document, code_article, id_article, 
            lib_l_document, qte_l_document, qte2_l_document, u1_l_document, 
            u2_l_document, prix_u_l_document, taux_tva_l_document, compte_l_document, 
            code_tva_l_document, code_t_tva_l_document, mt_ht_l_document, 
            mt_ttc_l_document, rem_tx_l_document, rem_ht_l_document, mt_ht_apr_rem_globale, 
            mt_ttc_apr_rem_globale, 
            lib_tva_l_document,  type_document, type_modif)
	VALUES ( old.id_document, old.id_l_document,(select art.code_article from ta_article art where art.id_article=old.id_article) ,old.id_article,old.lib_l_document, 
            old.qte_l_document, old.qte2_l_document, old.u1_l_document, 
            old.u2_l_document, old.prix_u_l_document, old.taux_tva_l_document, old.compte_l_document, 
            old.code_tva_l_document, old.code_t_tva_l_document, old.mt_ht_l_document, 
            old.mt_ttc_l_document, old.rem_tx_l_document, old.rem_ht_l_document, old.mt_ht_apr_rem_globale, 
            old.mt_ttc_apr_rem_globale, old.lib_tva_l_document,  'ligne_facture', 'DELETE_LIGNE_FACTURE'); 
        RETURN NEW;
    END IF;

    RETURN NULL; -- le résultat est ignoré car il s'agit d'un trigger AFTER
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION traca_ligne_facture()
  OWNER TO bdg;
   
 


  ALTER TABLE ta_avoir
  DROP COLUMN date_verrouillage; 

ALTER TABLE ta_facture
  DROP COLUMN date_verrouillage; 
  
ALTER TABLE ta_apporteur
  DROP COLUMN date_verrouillage; 
  
ALTER TABLE ta_devis
  DROP COLUMN date_verrouillage; 

ALTER TABLE ta_bonliv
  DROP COLUMN date_verrouillage; 

ALTER TABLE ta_boncde
  DROP COLUMN date_verrouillage; 

ALTER TABLE ta_bon_reception
  DROP COLUMN date_verrouillage;  
  
ALTER TABLE ta_fabrication
  DROP COLUMN date_verrouillage; 
  
ALTER TABLE ta_avis_echeance
  DROP COLUMN date_verrouillage; 
  
ALTER TABLE ta_proforma
  DROP COLUMN date_verrouillage; 
  
ALTER TABLE ta_prelevement
  DROP COLUMN date_verrouillage; 
  
ALTER TABLE ta_acompte
  DROP COLUMN date_verrouillage; 

ALTER TABLE ta_remise
  DROP COLUMN date_verrouillage; 

ALTER TABLE ta_reglement
  DROP COLUMN date_verrouillage; 

ALTER TABLE ta_modele_fabrication
  DROP COLUMN date_verrouillage;  


 
  ALTER TABLE ta_avoir
  ADD COLUMN date_verrouillage timestamp with time zone;

ALTER TABLE ta_facture
  ADD COLUMN date_verrouillage timestamp with time zone;
  
ALTER TABLE ta_apporteur
  ADD COLUMN date_verrouillage timestamp with time zone; 
  
ALTER TABLE ta_devis
  ADD COLUMN date_verrouillage timestamp with time zone; 

ALTER TABLE ta_bonliv
  ADD COLUMN date_verrouillage timestamp with time zone; 

ALTER TABLE ta_boncde
  ADD COLUMN date_verrouillage timestamp with time zone; 

ALTER TABLE ta_bon_reception
  ADD COLUMN date_verrouillage timestamp with time zone; 
  
ALTER TABLE ta_fabrication
  ADD COLUMN date_verrouillage timestamp with time zone; 
  
ALTER TABLE ta_avis_echeance
  ADD COLUMN date_verrouillage timestamp with time zone; 
  
ALTER TABLE ta_proforma
  ADD COLUMN date_verrouillage timestamp with time zone; 
  
ALTER TABLE ta_prelevement
  ADD COLUMN date_verrouillage timestamp with time zone;
  
ALTER TABLE ta_acompte
  ADD COLUMN date_verrouillage timestamp with time zone; 

ALTER TABLE ta_remise
  ADD COLUMN date_verrouillage timestamp with time zone; 

ALTER TABLE ta_reglement
  ADD COLUMN date_verrouillage timestamp with time zone;

ALTER TABLE ta_modele_fabrication
  ADD COLUMN date_verrouillage timestamp with time zone;  


ALTER TABLE ta_r_reglement
  ADD COLUMN id_mise_a_disposition did_facultatif;

ALTER TABLE ta_r_avoir
  ADD COLUMN id_mise_a_disposition did_facultatif;    

  
ALTER TABLE ta_avis_echeance
  ADD COLUMN id_mise_a_disposition did_facultatif;
  
  DROP FUNCTION traca_ligne_avoir() cascade;
   
CREATE OR REPLACE FUNCTION traca_ligne_avoir()
  RETURNS trigger AS
$BODY$DECLARE
  r record;
BEGIN
	
    IF (TG_OP = 'INSERT') THEN
	INSERT INTO ta_traca_ligne( id_document, id_l_document, code_article, id_article, 
            lib_l_document, qte_l_document, qte2_l_document, u1_l_document, 
            u2_l_document, prix_u_l_document, taux_tva_l_document, compte_l_document, 
            code_tva_l_document, code_t_tva_l_document, mt_ht_l_document, 
            mt_ttc_l_document, rem_tx_l_document, rem_ht_l_document, mt_ht_apr_rem_globale, 
            mt_ttc_apr_rem_globale, 
            lib_tva_l_document,  type_document, type_modif)
	VALUES ( new.id_document, new.id_l_document,(select art.code_article from ta_article art where art.id_article=new.id_article) ,new.id_article,new.lib_l_document, 
            new.qte_l_document, new.qte2_l_document, new.u1_l_document, 
            new.u2_l_document, new.prix_u_l_document, new.taux_tva_l_document, new.compte_l_document, 
            new.code_tva_l_document, new.code_t_tva_l_document, new.mt_ht_l_document, 
            new.mt_ttc_l_document, new.rem_tx_l_document, new.rem_ht_l_document, new.mt_ht_apr_rem_globale, 
            new.mt_ttc_apr_rem_globale, new.lib_tva_l_document,  'ligne_avoir', 'INSERT_LIGNE_AVOIR'); 
        RETURN NEW;
    END IF;   
    IF (TG_OP = 'UPDATE') THEN
	INSERT INTO ta_traca_ligne( id_document, id_l_document, code_article, id_article, 
            lib_l_document, qte_l_document, qte2_l_document, u1_l_document, 
            u2_l_document, prix_u_l_document, taux_tva_l_document, compte_l_document, 
            code_tva_l_document, code_t_tva_l_document, mt_ht_l_document, 
            mt_ttc_l_document, rem_tx_l_document, rem_ht_l_document, mt_ht_apr_rem_globale, 
            mt_ttc_apr_rem_globale, 
            lib_tva_l_document,  type_document, type_modif)
	VALUES ( new.id_document, new.id_l_document,(select art.code_article from ta_article art where art.id_article=new.id_article) ,new.id_article,new.lib_l_document, 
            new.qte_l_document, new.qte2_l_document, new.u1_l_document, 
            new.u2_l_document, new.prix_u_l_document, new.taux_tva_l_document, new.compte_l_document, 
            new.code_tva_l_document, new.code_t_tva_l_document, new.mt_ht_l_document, 
            new.mt_ttc_l_document, new.rem_tx_l_document, new.rem_ht_l_document, new.mt_ht_apr_rem_globale, 
            new.mt_ttc_apr_rem_globale, new.lib_tva_l_document,  'ligne_avoir', 'UPDATE_LIGNE_AVOIR'); 
        RETURN NEW;
    END IF;
    IF (TG_OP = 'DELETE') THEN
	INSERT INTO ta_traca_ligne( id_document, id_l_document, code_article, id_article, 
            lib_l_document, qte_l_document, qte2_l_document, u1_l_document, 
            u2_l_document, prix_u_l_document, taux_tva_l_document, compte_l_document, 
            code_tva_l_document, code_t_tva_l_document, mt_ht_l_document, 
            mt_ttc_l_document, rem_tx_l_document, rem_ht_l_document, mt_ht_apr_rem_globale, 
            mt_ttc_apr_rem_globale, 
            lib_tva_l_document,  type_document, type_modif)
	VALUES ( old.id_document, old.id_l_document,(select art.code_article from ta_article art where art.id_article=old.id_article) ,old.id_article,old.lib_l_document, 
            old.qte_l_document, old.qte2_l_document, old.u1_l_document, 
            old.u2_l_document, old.prix_u_l_document, old.taux_tva_l_document, old.compte_l_document, 
            old.code_tva_l_document, old.code_t_tva_l_document, old.mt_ht_l_document, 
            old.mt_ttc_l_document, old.rem_tx_l_document, old.rem_ht_l_document, old.mt_ht_apr_rem_globale, 
            old.mt_ttc_apr_rem_globale, old.lib_tva_l_document,  'ligne_avoir', 'DELETE_LIGNE_AVOIR'); 
        RETURN NEW;
    END IF;

    RETURN NULL; -- le résultat est ignoré car il s'agit d'un trigger AFTER
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION traca_ligne_avoir()
  OWNER TO bdg;
   
ALTER TABLE ta_r_reglement
  DROP COLUMN id_bonliv;
 ALTER TABLE ta_r_reglement
  DROP COLUMN id_devis;
ALTER TABLE ta_r_reglement
  DROP COLUMN id_boncde;
ALTER TABLE ta_r_reglement
  DROP COLUMN id_proforma;
ALTER TABLE ta_r_reglement
  DROP COLUMN id_apporteur;
ALTER TABLE ta_r_reglement
  DROP COLUMN id_avoir; 


