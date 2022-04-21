--maj_unite_saisie_2
--set schema 'demo';

--apporteur      
ALTER TABLE ta_l_apporteur  ADD COLUMN qte_saisie_l_document qte3;
ALTER TABLE ta_l_apporteur  ADD COLUMN u_saisie_l_document character varying(20);

ALTER TABLE ta_apporteur   ADD COLUMN utilise_unite_saisie boolean;
update ta_apporteur set  utilise_unite_saisie=false;

--facture      
ALTER TABLE ta_l_facture  ADD COLUMN qte_saisie_l_document qte3;
ALTER TABLE ta_l_facture  ADD COLUMN u_saisie_l_document character varying(20);

ALTER TABLE ta_facture   ADD COLUMN utilise_unite_saisie boolean;
update ta_facture set  utilise_unite_saisie=false;

--bon_reception      
ALTER TABLE ta_l_bon_reception  ADD COLUMN qte_saisie_l_document qte3;
ALTER TABLE ta_l_bon_reception  ADD COLUMN u_saisie_l_document character varying(20);

ALTER TABLE ta_bon_reception   ADD COLUMN utilise_unite_saisie boolean;
update ta_bon_reception set  utilise_unite_saisie=false;

--abonnement      
ALTER TABLE ta_l_abonnement  ADD COLUMN qte_saisie_l_document qte3;
ALTER TABLE ta_l_abonnement  ADD COLUMN u_saisie_l_document character varying(20);

ALTER TABLE ta_abonnement   ADD COLUMN utilise_unite_saisie boolean;
update ta_abonnement set  utilise_unite_saisie=false;

--panier      
ALTER TABLE ta_l_panier  ADD COLUMN qte_saisie_l_document qte3;
ALTER TABLE ta_l_panier  ADD COLUMN u_saisie_l_document character varying(20);

ALTER TABLE ta_panier   ADD COLUMN utilise_unite_saisie boolean;
update ta_panier set  utilise_unite_saisie=false;

--prelevement      
ALTER TABLE ta_l_prelevement  ADD COLUMN qte_saisie_l_document qte3;
ALTER TABLE ta_l_prelevement  ADD COLUMN u_saisie_l_document character varying(20);

ALTER TABLE ta_prelevement   ADD COLUMN utilise_unite_saisie boolean;
update ta_prelevement set  utilise_unite_saisie=false;

--preparation      
ALTER TABLE ta_l_preparation  ADD COLUMN qte_saisie_l_document qte3;
ALTER TABLE ta_l_preparation  ADD COLUMN u_saisie_l_document character varying(20);

ALTER TABLE ta_preparation   ADD COLUMN utilise_unite_saisie boolean;
update ta_preparation set  utilise_unite_saisie=false;

--proforma      
ALTER TABLE ta_l_proforma  ADD COLUMN qte_saisie_l_document qte3;
ALTER TABLE ta_l_proforma  ADD COLUMN u_saisie_l_document character varying(20);

ALTER TABLE ta_proforma   ADD COLUMN utilise_unite_saisie boolean;
update ta_proforma set  utilise_unite_saisie=false;

--ticket_caisse      
ALTER TABLE ta_l_ticket_caisse  ADD COLUMN qte_saisie_l_document qte3;
ALTER TABLE ta_l_ticket_caisse  ADD COLUMN u_saisie_l_document character varying(20);

ALTER TABLE ta_ticket_caisse   ADD COLUMN utilise_unite_saisie boolean;
update ta_ticket_caisse set  utilise_unite_saisie=false;

--fabrication      
ALTER TABLE ta_l_fabrication_mp  ADD COLUMN qte_saisie_l_document qte3;
ALTER TABLE ta_l_fabrication_mp  ADD COLUMN u_saisie_l_document character varying(20);

ALTER TABLE ta_l_fabrication_pf  ADD COLUMN qte_saisie_l_document qte3;
ALTER TABLE ta_l_fabrication_pf  ADD COLUMN u_saisie_l_document character varying(20);

ALTER TABLE ta_fabrication   ADD COLUMN utilise_unite_saisie boolean;
update ta_fabrication set  utilise_unite_saisie=false;

