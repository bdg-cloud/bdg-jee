ALTER TABLE ta_l_flash  ADD COLUMN qte_saisie_l_document qte3;
ALTER TABLE ta_l_flash  ADD COLUMN u_saisie_l_document character varying(20);

ALTER TABLE ta_flash   ADD COLUMN utilise_unite_saisie boolean;
update ta_flash set  utilise_unite_saisie=false;

alter table ta_l_preparation
add column abonnement boolean default false;
