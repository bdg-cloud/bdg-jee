--set schema 'demo';

CREATE OR REPLACE FUNCTION traca_affectation_reglement()
 RETURNS trigger
 LANGUAGE plpgsql
AS $function$DECLARE
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
	if(old.id_facture is not null) then
	    select fac.* from ta_facture fac where fac.id_document = old.id_facture into f;
    elseif (old.id_ticket_caisse is not null) then
        select tc.* from ta_ticket_caisse tc where tc.id_document = old.id_ticket_caisse into f;
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
        RETURN old;
    END IF;

    RETURN NULL; -- le résultat est ignoré car il s'agit d'un trigger AFTER
END;$function$
;


