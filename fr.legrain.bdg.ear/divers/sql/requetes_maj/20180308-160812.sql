
 DROP FUNCTION obtenirtouslesmouvements();

CREATE OR REPLACE FUNCTION obtenirtouslesmouvements()
  RETURNS TABLE(num_lot character varying, id_article integer, libelle character varying, id_entrepot integer, emplacement character varying, qte1_stock numeric, un1_stock character varying, qte2_stock numeric, un2_stock character varying, qte_ref numeric, unite_ref character varying) AS
$BODY$
DECLARE
    
BEGIN

    RETURN QUERY EXECUTE 'SELECT l.numlot as num_lot ,cast( a.id_article as integer) , cast(a.libellec_article as character varying),cast( id_entrepot as integer)
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
  SELECT (''VIRT_''||cast(a.code_article as character varying)) as num_lot ,cast( a.id_article as integer) , cast(a.libellec_article as character varying),cast( id_entrepot as integer)
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
  LANGUAGE plpgsql VOLATILE;
ALTER FUNCTION obtenirtouslesmouvements()
  OWNER TO bdg;
  
  
  ALTER TABLE ta_preferences
   ALTER COLUMN valeur TYPE dlib_commentaire;

   
   
   
   CREATE TABLE ta_ticket_caisse
(
  id_document did3 NOT NULL DEFAULT nextval('num_id_document'::regclass),
  code_document character varying(20),
  date_document date_lgr,
  date_ech_document date_lgr,
  date_liv_document date_lgr,
  libelle_document dlib255nn,
  id_tiers did_facultatif NOT NULL,
  id_t_paiement did_facultatif,
  id_etat did_facultatif,
  regle_document did9 DEFAULT 0,
  rem_ht_document did9 DEFAULT 0,
  tx_rem_ht_document did9 DEFAULT 0,
  rem_ttc_document did9 DEFAULT 0,
  tx_rem_ttc_document did9 DEFAULT 0,
  nb_e_document did_facultatif DEFAULT 0,
  ttc smallint DEFAULT 0,
  export smallint DEFAULT 0,
  commentaire dlib_commentaire,
  mt_ttc_calc did9facult,
  mt_ht_calc did9facult,
  mt_tva_calc did9facult,
  net_ttc_calc did9facult,
  net_ht_calc did9facult,
  net_tva_calc did9facult,
  net_a_payer did9facult,
  mt_ttc_avt_rem_globale_calc did9facult,
  acomptes did9facult,
  reste_a_regler did9facult,
  libelle_paiement dlib255,
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  id_gr_mouv_stock did_facultatif,
  id_reglement did_facultatif,
  date_export timestamp(0) without time zone,
  id_mise_a_disposition did_facultatif,
  date_verrouillage timestamp with time zone,
  CONSTRAINT ta_ticket_caisse_pkey PRIMARY KEY (id_document),
  CONSTRAINT fk_ta_ticket_caisse_id_gr_mouv_stock FOREIGN KEY (id_gr_mouv_stock)
      REFERENCES ta_gr_mouv_stock (id_gr_mouv_stock) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_mise_a_disposition_id_mise_a_disposition FOREIGN KEY (id_mise_a_disposition)
      REFERENCES ta_mise_a_disposition (id_mise_a_disposition) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_ticket_caisse_id_etat_fkey FOREIGN KEY (id_etat)
      REFERENCES ta_etat (id_etat) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_ticket_caisse_id_reglement_fkey FOREIGN KEY (id_reglement)
      REFERENCES ta_reglement (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_ticket_caisse_id_t_paiement_fkey FOREIGN KEY (id_t_paiement)
      REFERENCES ta_t_paiement (id_t_paiement) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_ticket_caisse_id_tiers_fkey FOREIGN KEY (id_tiers)
      REFERENCES ta_tiers (id_tiers) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_ticket_caisse_code_unique UNIQUE (code_document)
)
WITH (
  OIDS=FALSE
);

CREATE OR REPLACE FUNCTION tbdid_ticket_caisse_etranger()
  RETURNS trigger AS
$BODY$
begin
  delete from ta_l_ticket_caisse where id_DOCUMENT = old.id_DOCUMENT;
  delete from TA_COM_DOC where ID_DOCUMENT = old.ID_DOCUMENT;
  delete from ta_infos_ticket_caisse where id_DOCUMENT = old.id_DOCUMENT;
  delete from ta_r_document where id_ticket_caisse = old.id_DOCUMENT;
   return null;
end
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

CREATE INDEX fki_ta_ticket_caisse_id_gr_mouv_stock
  ON ta_ticket_caisse
  USING btree
  (id_gr_mouv_stock);

CREATE INDEX ta_ticket_caisse_id_tiers
  ON ta_ticket_caisse
  USING btree
  (id_tiers);

CREATE INDEX ta_ticket_caisse_t_paiement
  ON ta_ticket_caisse
  USING btree
  (id_t_paiement);

CREATE INDEX ta_ticket_caisse_etat
  ON ta_ticket_caisse
  USING btree
  (id_etat);

CREATE INDEX ta_ticket_caisse_code
  ON ta_ticket_caisse
  USING btree
  (code_document COLLATE pg_catalog."default");

CREATE INDEX unq1_ta_ticket_caisse
  ON ta_ticket_caisse
  USING btree
  (code_document COLLATE pg_catalog."default");

CREATE TRIGGER tbdid_ticket_caisse_etranger
  AFTER DELETE
  ON ta_ticket_caisse
  FOR EACH ROW
  EXECUTE PROCEDURE tbdid_ticket_caisse_etranger();

CREATE TRIGGER tbi_ticket_caisse
  BEFORE INSERT
  ON ta_ticket_caisse
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

CREATE TRIGGER tbu_ticket_caisse
  BEFORE UPDATE
  ON ta_ticket_caisse
  FOR EACH ROW
  EXECUTE PROCEDURE before_update_code_doc();

--CREATE TRIGGER traca_ticket_caisse
--  AFTER INSERT OR UPDATE OR DELETE
--  ON ta_ticket_caisse
--  FOR EACH ROW
--  EXECUTE PROCEDURE traca_ticket_caisse();

--==========================================================================================================

CREATE TABLE ta_infos_ticket_caisse
(
  id_infos_document did3 NOT NULL DEFAULT nextval('num_id_infos_document'::regclass),
  id_document did_facultatif NOT NULL,
  adresse1 dlib100,
  adresse2 dlib100,
  adresse3 dlib100,
  codepostal dcodpos,
  ville dlib100,
  pays dlib100,
  adresse1_liv dlib100,
  adresse2_liv dlib100,
  adresse3_liv dlib100,
  codepostal_liv dcodpos,
  ville_liv dlib100,
  pays_liv dlib100,
  code_compta dlib8nn,
  compte dlib8nn,
  nom_tiers dlib100,
  prenom_tiers dlib100,
  surnom_tiers dlib20,
  code_t_civilite dlgr_codel,
  code_t_entite dlgr_codel,
  tva_i_com_compl dlib50,
  code_c_paiement dlgr_codel,
  lib_c_paiement dlib255,
  report_c_paiement integer DEFAULT 0,
  fin_mois_c_paiement integer DEFAULT 0,
  libl_entreprise dlib100,
  nom_entreprise dlib100,
  code_t_tva_doc dlgr_codel,
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_infos_ticket_caisse_pkey PRIMARY KEY (id_infos_document),
  CONSTRAINT fk_ta_infos_ticket_caisse_1 FOREIGN KEY (id_document)
      REFERENCES ta_ticket_caisse (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);

CREATE INDEX ta_infos_ticket_caisse_id_ticket_caisse
  ON ta_infos_ticket_caisse
  USING btree
  (id_document);

CREATE TRIGGER tbi_ta_infos_ticket_caisse
  BEFORE INSERT
  ON ta_infos_ticket_caisse
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

CREATE TRIGGER tbu_ta_infos_ticket_caisse
  BEFORE UPDATE
  ON ta_infos_ticket_caisse
  FOR EACH ROW
  EXECUTE PROCEDURE before_update();

--==========================================================================================================

CREATE TABLE ta_l_ticket_caisse
(
  id_l_document did3 NOT NULL DEFAULT nextval('num_id_l_document'::regclass),
  id_document did_facultatif NOT NULL,
  id_t_ligne did_facultatif NOT NULL,
  id_article did_facultatif,
  num_ligne_l_document integer DEFAULT 0,
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
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  lib_tva_l_document character varying(255),
  id_lot did_facultatif,
  id_entrepot did_facultatif,
  emplacement_l_document dlib255,
  id_mouvement_stock did_facultatif,
  CONSTRAINT ta_l_ticket_caisse_pkey PRIMARY KEY (id_l_document),
  CONSTRAINT fk_ta_l_ticket_caisse_1 FOREIGN KEY (id_document)
      REFERENCES ta_ticket_caisse (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_l_ticket_caisse_2 FOREIGN KEY (id_article)
      REFERENCES ta_article (id_article) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_l_ticket_caisse_3 FOREIGN KEY (id_lot)
      REFERENCES ta_lot (id_lot) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_l_ticket_caisse_4 FOREIGN KEY (id_entrepot)
      REFERENCES ta_entrepot (id_entrepot) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_l_ticket_caisse_5 FOREIGN KEY (id_mouvement_stock)
      REFERENCES ta_mouvement_stock (id_mouvement_stock) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_l_ticket_caisse_id_t_ligne_fkey FOREIGN KEY (id_t_ligne)
      REFERENCES ta_t_ligne (id_t_ligne) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);

CREATE INDEX fk_ta_l_ticket_caisse_1
  ON ta_l_ticket_caisse
  USING btree
  (id_document);

CREATE INDEX fk_ta_l_ticket_caisse_2
  ON ta_l_ticket_caisse
  USING btree
  (id_article);

CREATE INDEX fki_ta_l_ticket_caisse_3
  ON ta_l_ticket_caisse
  USING btree
  (id_lot);

CREATE INDEX fki_ta_l_ticket_caisse_4
  ON ta_l_ticket_caisse
  USING btree
  (id_entrepot);

CREATE INDEX fki_ta_l_ticket_caisse_5
  ON ta_l_ticket_caisse
  USING btree
  (id_mouvement_stock);

CREATE INDEX ta_l_ticket_caisse_t_ligne
  ON ta_l_ticket_caisse
  USING btree
  (id_t_ligne);

CREATE INDEX ta_l_ticket_caisse_id_ticket_caisse
  ON ta_l_ticket_caisse
  USING btree
  (id_document);

CREATE INDEX ta_l_ticket_caisse_id_t_ligne
  ON ta_l_ticket_caisse
  USING btree
  (id_t_ligne);

CREATE TRIGGER tbi_l_ticket_caisse
  BEFORE INSERT
  ON ta_l_ticket_caisse
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

CREATE TRIGGER tbiid_article_l_ticket_caisse
  BEFORE INSERT
  ON ta_l_ticket_caisse
  FOR EACH ROW
  EXECUTE PROCEDURE ligne_vide();

CREATE TRIGGER tbu_l_ticket_caisse
  BEFORE UPDATE
  ON ta_l_ticket_caisse
  FOR EACH ROW
  EXECUTE PROCEDURE before_update();

CREATE TRIGGER tbuid_article_l_ticket_caisse
  BEFORE UPDATE
  ON ta_l_ticket_caisse
  FOR EACH ROW
  EXECUTE PROCEDURE ligne_vide();

--==========================================================================================================

  ALTER TABLE ta_r_avoir ADD COLUMN id_ticket_caisse did_facultatif;

ALTER TABLE ta_r_avoir
  ADD CONSTRAINT fk_ta_r_avoir_id_ticket_caisse FOREIGN KEY (id_ticket_caisse)
      REFERENCES ta_ticket_caisse (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

-----------------------------------------------------------------------------------------

ALTER TABLE ta_r_reglement ADD COLUMN id_ticket_caisse did_facultatif;

ALTER TABLE ta_r_reglement
  ADD CONSTRAINT fk_ta_r_reglement_id_ticket_caisse FOREIGN KEY (id_ticket_caisse)
      REFERENCES ta_ticket_caisse (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
      
-----------------------------------------------------------------------------------------

ALTER TABLE ta_r_acompte ADD COLUMN id_ticket_caisse did_facultatif;

ALTER TABLE ta_r_acompte
  ADD CONSTRAINT fk_ta_r_acompte_id_ticket_caisse FOREIGN KEY (id_ticket_caisse)
      REFERENCES ta_ticket_caisse (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

-----------------------------------------------------------------------------------------

ALTER TABLE ta_r_document ADD COLUMN id_ticket_caisse did_facultatif;

ALTER TABLE ta_r_document
  ADD CONSTRAINT ta_r_document_id_ticket_caisse_fkey FOREIGN KEY (id_ticket_caisse)
      REFERENCES ta_ticket_caisse (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
      
-----------------------------------------------------------------------------------------

ALTER TABLE ta_gr_mouv_stock ADD COLUMN id_ticket_caisse did_facultatif;

ALTER TABLE ta_gr_mouv_stock
  ADD CONSTRAINT ticket_caisse_foreignkey FOREIGN KEY (id_ticket_caisse)
      REFERENCES ta_ticket_caisse (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
      
ALTER TABLE ta_gr_mouv_stock DROP CONSTRAINT doc_not_nul;

ALTER TABLE ta_gr_mouv_stock
  ADD CONSTRAINT doc_not_nul CHECK (id_fabrication IS NOT NULL OR id_bon_reception IS NOT NULL OR id_inventaire IS NOT NULL OR id_facture IS NOT NULL OR id_ticket_caisse IS NOT NULL OR id_bonliv IS NOT NULL OR id_boncde IS NOT NULL OR id_avoir IS NOT NULL OR id_apporteur IS NOT NULL OR id_prelevement IS NOT NULL OR id_acompte IS NOT NULL OR id_proforma IS NOT NULL OR manuel IS NOT NULL);

  
--==========================================================================================================

 INSERT INTO ta_gen_code_ex (code_gen_code, cle_gen_code, valeur_gen_code, version_obj) VALUES ('codeDocument', 'TaTicketDeCaisse', 'TC{@num}', 0);
 
--==========================================================================================================

      
      
      