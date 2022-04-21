CREATE OR REPLACE FUNCTION tbdid_devis_etranger()
    RETURNS trigger
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE NOT LEAKPROOF
AS $BODY$
begin
  delete from ta_l_devis where id_DOCUMENT = old.id_DOCUMENT;
  delete from TA_COM_DOC where ID_DOCUMENT = old.ID_DOCUMENT;
  delete from ta_infos_devis where id_DOCUMENT = old.id_DOCUMENT;
  delete from TA_R_DOCUMENT where id_DEVIS = old.id_DOCUMENT;
  return OLD;
end
$BODY$;

ALTER FUNCTION tbdid_devis_etranger()
    OWNER TO bdg;
-------------------------------------------------------------------------
CREATE OR REPLACE FUNCTION tbdid_acompte_etranger()
    RETURNS trigger
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE NOT LEAKPROOF
AS $BODY$
begin
  delete from ta_l_ACOMPTE where id_DOCUMENT = old.id_DOCUMENT;
  delete from TA_COM_DOC where ID_DOCUMENT = old.ID_DOCUMENT;
  delete from ta_infos_ACOMPTE where id_DOCUMENT = old.id_DOCUMENT;
  delete from ta_r_document where id_ACOMPTE = old.id_DOCUMENT;
  return OLD;  
end
$BODY$;

ALTER FUNCTION tbdid_acompte_etranger()
    OWNER TO bdg;
-------------------------------------------------------------------------
CREATE OR REPLACE FUNCTION tbdid_apporteur_etranger()
    RETURNS trigger
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE NOT LEAKPROOF
AS $BODY$
begin
  delete from ta_l_APPORTEUR where id_DOCUMENT = old.id_DOCUMENT;
  delete from TA_COM_DOC where ID_DOCUMENT = old.ID_DOCUMENT;
  delete from ta_infos_APPORTEUR where id_DOCUMENT = old.id_DOCUMENT;
  delete from ta_r_document where id_APPORTEUR = old.id_DOCUMENT;
  return OLD;
end
$BODY$;

ALTER FUNCTION tbdid_apporteur_etranger()
    OWNER TO bdg;
-------------------------------------------------------------------------
CREATE OR REPLACE FUNCTION tbdid_avis_echeance_etranger()
    RETURNS trigger
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE NOT LEAKPROOF
AS $BODY$
begin
  delete from ta_l_AVIS_ECHEANCE where id_DOCUMENT = old.id_DOCUMENT;
  delete from TA_COM_DOC where ID_DOCUMENT = old.ID_DOCUMENT;
  delete from ta_infos_AVIS_ECHEANCE where id_DOCUMENT = old.id_DOCUMENT;
  delete from ta_r_document where id_AVIS_ECHEANCE = old.id_DOCUMENT;
  return OLD;
end
$BODY$;

ALTER FUNCTION tbdid_avis_echeance_etranger()
    OWNER TO bdg;
-------------------------------------------------------------------------
CREATE OR REPLACE FUNCTION tbdid_avoir_etranger()
    RETURNS trigger
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE NOT LEAKPROOF
AS $BODY$
begin
  delete from ta_l_AVOIR where id_DOCUMENT = old.id_DOCUMENT;
  delete from TA_COM_DOC where ID_DOCUMENT = old.ID_DOCUMENT;
  delete from ta_infos_AVOIR where id_DOCUMENT = old.id_DOCUMENT;
  delete from ta_r_document where id_AVOIR = old.id_DOCUMENT;
  return OLD;
end
$BODY$;

ALTER FUNCTION tbdid_avoir_etranger()
    OWNER TO bdg;
-------------------------------------------------------------------------
CREATE OR REPLACE FUNCTION tbdid_bon_reception_etranger()
    RETURNS trigger
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE NOT LEAKPROOF
AS $BODY$
begin
  delete from ta_l_bon_reception where id_DOCUMENT = old.id_DOCUMENT;
--  delete from TA_COM_DOC where ID_DOCUMENT = old.ID_DOCUMENT;
  delete from ta_infos_bon_reception where id_DOCUMENT = old.id_DOCUMENT;
 -- delete from TA_R_DOCUMENT where id_DEVIS = old.id_DOCUMENT;
  return OLD;
end 
$BODY$;

ALTER FUNCTION tbdid_bon_reception_etranger()
    OWNER TO bdg;
-------------------------------------------------------------------------
CREATE OR REPLACE FUNCTION tbdid_boncde_etranger()
    RETURNS trigger
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE NOT LEAKPROOF
AS $BODY$
begin
  delete from ta_l_BONCDE where id_DOCUMENT = old.id_DOCUMENT;
  delete from TA_COM_DOC where ID_DOCUMENT = old.ID_DOCUMENT;
  delete from ta_infos_BONCDE where id_DOCUMENT = old.id_DOCUMENT;
  delete from TA_R_DOCUMENT where id_BONCDE = old.id_DOCUMENT;
  return OLD;
end
$BODY$;

ALTER FUNCTION tbdid_boncde_etranger()
    OWNER TO bdg;
-------------------------------------------------------------------------
CREATE OR REPLACE FUNCTION tbdid_bonliv_etranger()
    RETURNS trigger
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE NOT LEAKPROOF
AS $BODY$
begin
  delete from ta_l_BONLIV where id_DOCUMENT = old.id_DOCUMENT;
  delete from TA_COM_DOC where ID_DOCUMENT = old.ID_DOCUMENT;
  delete from ta_infos_BONLIV where id_DOCUMENT = old.id_DOCUMENT;
  delete from TA_R_DOCUMENT where id_BONLIV = old.id_DOCUMENT;
  return OLD;
end
$BODY$;

ALTER FUNCTION tbdid_bonliv_etranger()
    OWNER TO bdg;
-------------------------------------------------------------------------
CREATE OR REPLACE FUNCTION tbdid_fabrication_etranger()
    RETURNS trigger
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE NOT LEAKPROOF
AS $BODY$
begin
  delete from ta_l_fabrication_mp where id_DOCUMENT = old.id_DOCUMENT;
  delete from ta_l_fabrication_pf where id_DOCUMENT = old.id_DOCUMENT;
--  delete from TA_COM_DOC where ID_DOCUMENT = old.ID_DOCUMENT;
  delete from ta_infos_fabrication where id_DOCUMENT = old.id_DOCUMENT;
 -- delete from TA_R_DOCUMENT where id_DEVIS = old.id_DOCUMENT;
  return OLD;
end
$BODY$;

ALTER FUNCTION tbdid_fabrication_etranger()
    OWNER TO bdg;
-------------------------------------------------------------------------
CREATE OR REPLACE FUNCTION tbdid_facture_etranger()
    RETURNS trigger
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE NOT LEAKPROOF
AS $BODY$
begin
  delete from ta_l_facture where id_DOCUMENT = old.id_DOCUMENT;
  delete from TA_COM_DOC where ID_DOCUMENT = old.ID_DOCUMENT;
  delete from ta_infos_facture where id_DOCUMENT = old.id_DOCUMENT;
  delete from ta_r_document where id_facture = old.id_DOCUMENT;
  return OLD;
end
$BODY$;

ALTER FUNCTION tbdid_facture_etranger()
    OWNER TO bdg;
-------------------------------------------------------------------------
CREATE OR REPLACE FUNCTION tbdid_inventaire_etranger()
    RETURNS trigger
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE NOT LEAKPROOF
AS $BODY$
begin
  delete from ta_l_inventaire where id_inventaire = old.id_inventaire;
  /*EXECUTE recalcul_etat_stock();*/
  return OLD;
end
$BODY$;

ALTER FUNCTION tbdid_inventaire_etranger()
    OWNER TO bdg;
-------------------------------------------------------------------------
CREATE OR REPLACE FUNCTION tbdid_modele_fabrication_etranger()
    RETURNS trigger
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE NOT LEAKPROOF
AS $BODY$
begin
  delete from ta_l_modele_fabrication_mp where id_DOCUMENT = old.id_DOCUMENT;
  delete from ta_l_modele_fabrication_pf where id_DOCUMENT = old.id_DOCUMENT;
  delete from ta_infos_modele_fabrication where id_DOCUMENT = old.id_DOCUMENT;
  return OLD;
end
$BODY$;

ALTER FUNCTION tbdid_modele_fabrication_etranger()
    OWNER TO bdg;
-------------------------------------------------------------------------
CREATE OR REPLACE FUNCTION tbdid_prelevement_etranger()
    RETURNS trigger
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE NOT LEAKPROOF
AS $BODY$
begin
  delete from ta_l_PRELEVEMENT where id_DOCUMENT = old.id_DOCUMENT;
  delete from TA_COM_DOC where ID_DOCUMENT = old.ID_DOCUMENT;
  delete from ta_infos_PRELEVEMENT where id_DOCUMENT = old.id_DOCUMENT;
  return OLD;
end
$BODY$;

ALTER FUNCTION tbdid_prelevement_etranger()
    OWNER TO bdg;
-------------------------------------------------------------------------
CREATE OR REPLACE FUNCTION tbdid_proforma_etranger()
    RETURNS trigger
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE NOT LEAKPROOF
AS $BODY$
begin
  delete from ta_l_PROFORMA where id_DOCUMENT = old.id_DOCUMENT;
  delete from TA_COM_DOC where ID_DOCUMENT = old.ID_DOCUMENT;
  delete from ta_infos_PROFORMA where id_DOCUMENT = old.id_DOCUMENT;
  delete from ta_r_document where id_PROFORMA = old.id_DOCUMENT;
  return OLD;
end
$BODY$;

ALTER FUNCTION tbdid_proforma_etranger()
    OWNER TO bdg;
-------------------------------------------------------------------------
CREATE OR REPLACE FUNCTION tbdid_reception_etranger()
    RETURNS trigger
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE NOT LEAKPROOF
AS $BODY$
begin
  delete from ta_l_reception where id_DOCUMENT = old.id_DOCUMENT;
  delete from TA_COM_DOC where ID_DOCUMENT = old.ID_DOCUMENT;
  delete from ta_infos_reception where id_DOCUMENT = old.id_DOCUMENT;
  delete from TA_R_DOCUMENT where id_BONCDE = old.id_DOCUMENT;
  return OLD;
end 
$BODY$;

ALTER FUNCTION tbdid_reception_etranger()
    OWNER TO bdg;
-------------------------------------------------------------------------
CREATE OR REPLACE FUNCTION tbdid_remise_etranger()
    RETURNS trigger
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE NOT LEAKPROOF
AS $BODY$
begin
  delete from ta_l_REMISE where id_DOCUMENT = old.id_DOCUMENT;
  delete from TA_COM_DOC where ID_DOCUMENT = old.ID_DOCUMENT;
  return OLD;
end
$BODY$;

ALTER FUNCTION tbdid_remise_etranger()
    OWNER TO bdg;
-------------------------------------------------------------------------
CREATE OR REPLACE FUNCTION tbdid_ticket_caisse_etranger()
    RETURNS trigger
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE NOT LEAKPROOF
AS $BODY$
begin
  delete from ta_l_ticket_caisse where id_DOCUMENT = old.id_DOCUMENT;
  delete from TA_COM_DOC where ID_DOCUMENT = old.ID_DOCUMENT;
  delete from ta_infos_ticket_caisse where id_DOCUMENT = old.id_DOCUMENT;
  delete from ta_r_document where id_ticket_caisse = old.id_DOCUMENT;
  return OLD;
end
$BODY$;

ALTER FUNCTION tbdid_ticket_caisse_etranger()
    OWNER TO postgres;
-------------------------------------------------------------------------
