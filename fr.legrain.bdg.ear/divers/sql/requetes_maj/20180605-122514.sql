
--numéro de caisse sur le ticket / Vendeur

--table de création de caisse ? => utilisateur avec une autorisation caisse

-------------------------------------------------------------------------------------------------------

INSERT INTO ta_gen_code_ex(code_gen_code, cle_gen_code, valeur_gen_code,version_obj)values('codeSessionCaisse','TaSessionCaisse','CAISSE{@num}',0) ON CONFLICT DO NOTHING;

-------------------------------------------------------------------------------------------------------

CREATE TABLE ta_t_fond_de_caisse
(
  id_t_fond_de_caisse serial NOT NULL,
  code_t_fond_de_caisse dlgr_code,
  libelle_t_fond_de_caisse dlgr_libcode,
  systeme boolean DEFAULT false,
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_t_fond_de_caisse_pkey PRIMARY KEY (id_t_fond_de_caisse),
  CONSTRAINT ta_t_fond_de_caisse_code_unique UNIQUE (code_t_fond_de_caisse)
);

INSERT INTO ta_t_fond_de_caisse (code_t_fond_de_caisse, libelle_t_fond_de_caisse, version_obj, systeme) VALUES ('O', 'Ouverture', 0, true);
INSERT INTO ta_t_fond_de_caisse (code_t_fond_de_caisse, libelle_t_fond_de_caisse, version_obj, systeme) VALUES ('C', 'Cloture', 0, true);
INSERT INTO ta_t_fond_de_caisse (code_t_fond_de_caisse, libelle_t_fond_de_caisse, version_obj, systeme) VALUES ('E', 'Ecart', 0, true);

CREATE TABLE ta_fond_de_caisse
(
  id_fond_de_caisse serial NOT NULL,
  
  numero_caisse dlib255,
  id_vendeur did_facultatif,
  date date_heure_lgr,

  id_t_fond_de_caisse did_facultatif,
  libelle dlib255,
  montant did9facult,

  id_session_caisse did_facultatif,

  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  ip_acces dlib50,
  version num_version,
  version_obj did_version_obj,
  CONSTRAINT ta_fond_de_caisse_pkey PRIMARY KEY (id_fond_de_caisse)
);

ALTER TABLE ta_fond_de_caisse
  ADD CONSTRAINT fk_ta_fond_de_caisse_id_t_fond_de_caisse FOREIGN KEY (id_t_fond_de_caisse)
      REFERENCES ta_t_fond_de_caisse (id_t_fond_de_caisse) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;


-------------------------------------------------------------------------------------------------------

CREATE TABLE ta_t_depot_retrait_caisse
(
  id_t_depot_retrait_caisse serial NOT NULL,
  code_t_depot_retrait_caisse dlgr_code,
  libelle_t_depot_retrait_caisse dlgr_libcode,
  systeme boolean DEFAULT false,
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_t_depot_retrait_caisse_pkey PRIMARY KEY (id_t_depot_retrait_caisse),
  CONSTRAINT ta_t_depot_retrait_caisse_code_unique UNIQUE (code_t_depot_retrait_caisse)
);

INSERT INTO ta_t_depot_retrait_caisse (code_t_depot_retrait_caisse, libelle_t_depot_retrait_caisse, version_obj, systeme) VALUES ('D', 'Dépot caisse', 0, true);
INSERT INTO ta_t_depot_retrait_caisse (code_t_depot_retrait_caisse, libelle_t_depot_retrait_caisse, version_obj, systeme) VALUES ('R', 'Retrait caisse', 0, true);


CREATE TABLE ta_depot_retrait_caisse
(
  id_depot_retrait_caisse serial NOT NULL,
  
  numero_caisse dlib255,
  id_vendeur did_facultatif,
  date date_heure_lgr,

  id_t_depot_retrait_caisse did_facultatif,
  libelle dlib255,
  montant did9facult,

  id_session_caisse did_facultatif,

  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  ip_acces dlib50,
  version num_version,
  version_obj did_version_obj,
  CONSTRAINT ta_depot_retrait_caisse_pkey PRIMARY KEY (id_depot_retrait_caisse)
);

ALTER TABLE ta_depot_retrait_caisse
  ADD CONSTRAINT fk_ta_depot_retrait_caisse_id_t_depot_retrait_caisse FOREIGN KEY (id_t_depot_retrait_caisse)
      REFERENCES ta_t_depot_retrait_caisse (id_t_depot_retrait_caisse) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;


------------------------------------------------------------------------------------------------------------

CREATE TABLE ta_t_l_session_caisse
(
  id_t_l_session_caisse serial NOT NULL,
  code_t_l_session_caisse dlgr_code,
  libelle_t_l_session_caisse dlgr_libcode,
  systeme boolean DEFAULT false,
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_t_l_session_caisse_pkey PRIMARY KEY (id_t_l_session_caisse),
  CONSTRAINT ta_t_l_session_caisse_code_unique UNIQUE (code_t_l_session_caisse)
);

INSERT INTO ta_t_l_session_caisse (code_t_l_session_caisse, libelle_t_l_session_caisse, version_obj, systeme) VALUES ('TAUX_TVA', 'Taux TVA', 0, true);
INSERT INTO ta_t_l_session_caisse (code_t_l_session_caisse, libelle_t_l_session_caisse, version_obj, systeme) VALUES ('TYPE_PAIEMENT', 'Type de paiement', 0, true);
INSERT INTO ta_t_l_session_caisse (code_t_l_session_caisse, libelle_t_l_session_caisse, version_obj, systeme) VALUES ('ARTICLE', 'Article', 0, true);
INSERT INTO ta_t_l_session_caisse (code_t_l_session_caisse, libelle_t_l_session_caisse, version_obj, systeme) VALUES ('FAMILLE_ARTICLE', 'Famille article', 0, true);
INSERT INTO ta_t_l_session_caisse (code_t_l_session_caisse, libelle_t_l_session_caisse, version_obj, systeme) VALUES ('VENDEUR', 'Vendeur', 0, true);


CREATE TABLE ta_session_caisse
(
  id_session_caisse serial NOT NULL,
  
  code_session_caisse dlgr_code,
  numero_caisse dlib255,
  id_vendeur did_facultatif,
  date_session date_heure_lgr,
  date_debut_session date_heure_lgr,
  date_fin_session date_heure_lgr,
  libelle dlib255,
  automatique boolean DEFAULT false,
  verrouillage_ticket_z boolean DEFAULT false,
  
  montant_fond_de_caisse_ouverture did9facult,
  id_fond_de_caisse_ouverture did_facultatif,

  montant_ht_session did9facult,
  montant_ht_cumul_mensuel did9facult,
  montant_ht_cumul_annuel did9facult,
  montant_ht_cumul_exercice did9facult,

  montant_ttc_session did9facult,
  montant_ttc_cumul_mensuel did9facult,
  montant_ttc_cumul_annuel did9facult,
  montant_ttc_cumul_exercice did9facult,

  montant_tva_session did9facult,
  montant_tva_cumul_mensuel did9facult,
  montant_tva_cumul_annuel did9facult,
  montant_tva_cumul_exercice did9facult,

  montant_remise_session did9facult,

  montant_fond_de_caisse_cloture did9facult,
  id_fond_de_caisse_cloture did_facultatif,
  
  fichier_ticket_z bytea,

  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  ip_acces dlib50,
  version num_version,
  version_obj did_version_obj,
  CONSTRAINT ta_session_caisse_pkey PRIMARY KEY (id_session_caisse)
);

ALTER TABLE ta_session_caisse
  ADD CONSTRAINT fk_ta_session_caisse_id_vendeur FOREIGN KEY (id_vendeur)
      REFERENCES ta_utilisateur (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
      
ALTER TABLE ta_session_caisse
  ADD CONSTRAINT fk_ta_session_caisse_id_fond_de_caisse_ouverture FOREIGN KEY (id_fond_de_caisse_ouverture)
      REFERENCES ta_fond_de_caisse (id_fond_de_caisse) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
      
ALTER TABLE ta_session_caisse
  ADD CONSTRAINT fk_ta_session_caisse_id_fond_de_caisse_session FOREIGN KEY (id_fond_de_caisse_cloture)
      REFERENCES ta_fond_de_caisse (id_fond_de_caisse) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

CREATE TABLE ta_l_session_caisse
(
  id_l_session_caisse serial NOT NULL,
  id_session_caisse did_facultatif,

  id_t_l_session_caisse did_facultatif,
  libelle dlib255,
  code dlgr_code,
  taux did9facult,
  id_ext did_facultatif, --id tx tva ou id article ou id TaTPaiement ou ...

  montant_ht_session did9facult,
  montant_ht_cumul_mensuel did9facult,
  montant_ht_cumul_annuel did9facult,
  montant_ht_cumul_exercice did9facult,

  montant_ttc_session did9facult,
  montant_ttc_cumul_mensuel did9facult,
  montant_ttc_cumul_annuel did9facult,
  montant_ttc_cumul_exercice did9facult,

  montant_tva_session did9facult,
  montant_tva_cumul_mensuel did9facult,
  montant_tva_cumul_annuel did9facult,
  montant_tva_cumul_exercice did9facult,

  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  ip_acces dlib50,
  version num_version,
  version_obj did_version_obj,
  CONSTRAINT ta_l_session_caisse_pkey PRIMARY KEY (id_l_session_caisse)
);

ALTER TABLE ta_l_session_caisse
  ADD CONSTRAINT fk_ta_l_session_caisse_id_t_l_session_caisse FOREIGN KEY (id_t_l_session_caisse)
      REFERENCES ta_t_l_session_caisse (id_t_l_session_caisse) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
      
-------------------------------------------------------------------------------------------------------

ALTER TABLE ta_ticket_caisse ADD COLUMN id_session_caisse did_facultatif;

ALTER TABLE ta_ticket_caisse
  ADD CONSTRAINT fk_ta_ticket_caisse_id_session_caisse FOREIGN KEY (id_session_caisse)
      REFERENCES ta_session_caisse (id_session_caisse) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE ta_ticket_caisse ADD COLUMN vendeur dlib255;
ALTER TABLE ta_ticket_caisse ADD COLUMN id_vendeur did_facultatif;

ALTER TABLE ta_ticket_caisse
  ADD CONSTRAINT fk_ta_ticket_caisse_id_vendeur FOREIGN KEY (id_vendeur)
      REFERENCES ta_utilisateur (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
      
-----------------------------------------------------------------------------------------------------------
CREATE OR REPLACE FUNCTION traca_ticket_de_caisse()
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
            'TicketDeCaisse','INSERT_TICKET_DE_CAISSE');
        RETURN NEW;
    END IF;  
    IF (TG_OP = 'UPDATE') THEN
	 INSERT INTO ta_traca_document(id_document,code_document, date_document, libelle_document, 
            id_tiers, code_t_paiement, export, net_ttc_calc, date_export, 
            code_etat,  date_traca,type_document,type_modif)
	VALUES (new.id_document, new.code_document, new.date_document, new.libelle_document, 
            new.id_tiers,(select c.code_t_paiement from ta_t_paiement c where c.id_t_paiement = new.id_t_paiement), new.export, new.net_ttc_calc,  new.date_export, 
            (select e.code_etat from ta_etat e where e.id_etat = new.id_etat), now(),
            'TicketDeCaisse','UPDATE_TICKET_DE_CAISSE');
        RETURN NEW;
    END IF;
    IF (TG_OP = 'DELETE') THEN
	 INSERT INTO ta_traca_document(id_document,code_document, date_document, libelle_document, 
            id_tiers, code_t_paiement, export, net_ttc_calc,  date_export, 
            code_etat, date_traca,type_document,type_modif)
	VALUES (old.id_document, old.code_document, old.date_document, old.libelle_document, 
            old.id_tiers,(select c.code_t_paiement from ta_t_paiement c where c.id_t_paiement = old.id_t_paiement), old.export, old.net_ttc_calc, old.date_export, 
            (select e.code_etat from ta_etat e where e.id_etat = old.id_etat) , now(),
            'TicketDeCaisse','DELETE_TICKET_DE_CAISSE');
        RETURN NEW;
    END IF;

    RETURN NULL; -- le résultat est ignoré car il s'agit d'un trigger AFTER
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
-------------------------------------------------------------------------------------------------------------
  

CREATE OR REPLACE FUNCTION traca_ligne_ticket_de_caisse()
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
            new.mt_ttc_apr_rem_globale, new.lib_tva_l_document,  'ligne_ticket_de_caisse', 'INSERT_LIGNE_TICKET_DE_CAISSE'); 
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
            new.mt_ttc_apr_rem_globale, new.lib_tva_l_document,  'ligne_ticket_de_caisse', 'UPDATE_LIGNE_TICKET_DE_CAISSE'); 
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
            old.mt_ttc_apr_rem_globale, old.lib_tva_l_document,  'ligne_ticket_de_caisse', 'DELETE_LIGNE_TICKET_DE_CAISSE'); 
        RETURN NEW;
    END IF;

    RETURN NULL; -- le résultat est ignoré car il s'agit d'un trigger AFTER
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION traca_ligne_ticket_de_caisse()
  OWNER TO bdg;

---------------------------------------------------------------------------------------------------------------------------------

 CREATE TRIGGER traca_ticket_de_caisse
  AFTER INSERT OR UPDATE OR DELETE
  ON ta_ticket_caisse
  FOR EACH ROW
  EXECUTE PROCEDURE traca_ticket_de_caisse();
  
 ---------------------------------------------------------------------------------------------------------------------------------


 CREATE OR REPLACE FUNCTION traca_affectation_reglement()
  RETURNS trigger AS
$BODY$DECLARE
 r record;
 f record;
BEGIN
    IF (TG_OP = 'INSERT') THEN
	select reg.* from ta_reglement reg where reg.id_document = new.id_reglement into r;	
    if(new.id_facture is not null) then
	    select fac.* from ta_facture fac where fac.id_document = new.id_facture into f;
    elseif (new.id_ticket_caisse is not null) then
        select tc.* from ta_ticket_caisse tc where tc.id_document = new.id_ticket_caisse into f;
    end if;
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
	if(new.id_facture is not null) then
	    select fac.* from ta_facture fac where fac.id_document = new.id_facture into f;
    elseif (new.id_ticket_caisse is not null) then
        select tc.* from ta_ticket_caisse tc where tc.id_document = new.id_ticket_caisse into f;
    end if;
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
	if(new.id_facture is not null) then
	    select fac.* from ta_facture fac where fac.id_document = new.id_facture into f;
    elseif (new.id_ticket_caisse is not null) then
        select tc.* from ta_ticket_caisse tc where tc.id_document = new.id_ticket_caisse into f;
    end if;
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
---------------------------------------------------------------------------------------------------------------------------------

  
