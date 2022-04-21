
--set schema 'demo';


update ta_apporteur set id_etat=null where id_etat in (select et.id_etat from ta_etat et where et.id_t_etat is not null);
update ta_avoir set id_etat=null where id_etat in (select et.id_etat from ta_etat et where et.id_t_etat is not null);
update ta_acompte set id_etat=null where id_etat in (select et.id_etat from ta_etat et where et.id_t_etat is not null);
update ta_abonnement set id_etat=null where id_etat in (select et.id_etat from ta_etat et where et.id_t_etat is not null);
update ta_avis_echeance set id_etat=null where id_etat in (select et.id_etat from ta_etat et where et.id_t_etat is not null);
update ta_boncde set id_etat=null where id_etat in (select et.id_etat from ta_etat et where et.id_t_etat is not null);
update ta_bonliv set id_etat=null where id_etat in (select et.id_etat from ta_etat et where et.id_t_etat is not null);
update ta_devis set id_etat=null where id_etat in (select et.id_etat from ta_etat et where et.id_t_etat is not null);
update ta_fabrication set id_etat=null where id_etat in (select et.id_etat from ta_etat et where et.id_t_etat is not null);
update ta_facture set id_etat=null where id_etat in (select et.id_etat from ta_etat et where et.id_t_etat is not null);
update ta_panier set id_etat=null where id_etat in (select et.id_etat from ta_etat et where et.id_t_etat is not null);
update ta_prelevement set id_etat=null where id_etat in (select et.id_etat from ta_etat et where et.id_t_etat is not null);
update ta_preparation set id_etat=null where id_etat in (select et.id_etat from ta_etat et where et.id_t_etat is not null);
update ta_proforma set id_etat=null where id_etat in (select et.id_etat from ta_etat et where et.id_t_etat is not null);
update ta_ticket_caisse set id_etat=null where id_etat in (select et.id_etat from ta_etat et where et.id_t_etat is not null);




 