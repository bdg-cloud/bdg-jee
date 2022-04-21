--maj_ligne_a_ligne_echeance.sql

--set schema 'demo';

alter table ta_l_echeance 
drop column if exists id_l_facture,
drop column if exists id_l_avis_echeance;

--set schema 'demo';
alter table ta_l_panier
add column abonnement boolean default false;
alter table ta_l_facture
add column abonnement boolean default false;
alter table ta_l_devis
add column abonnement boolean default false;
alter table ta_l_flash
add column abonnement boolean default false;
alter table ta_l_proforma
add column abonnement boolean default false;
alter table ta_l_bon_reception
add column abonnement boolean default false;
alter table ta_l_acompte 
add column abonnement boolean default false;
alter table ta_l_apporteur 
add column abonnement boolean default false;
alter table ta_l_avis_echeance 
add column abonnement boolean default false;
alter table ta_l_avoir 
add column abonnement boolean default false;
alter table ta_l_boncde 
add column abonnement boolean default false;
alter table ta_l_boncde_achat 
add column abonnement boolean default false;
alter table ta_l_bonliv 
add column abonnement boolean default false;
alter table ta_l_prelevement 
add column abonnement boolean default false;

alter table ta_l_ticket_caisse 
add column abonnement boolean default false;


--set schema 'demo';


CREATE TABLE ta_ligne_a_ligne_echeance
(
  id serial NOT NULL,
  id_l_echeance did_facultatif,
  id_l_facture did_facultatif,
  id_l_devis did_facultatif,
  id_l_boncde did_facultatif,
  id_l_bon_reception did_facultatif,
  id_l_bonliv did_facultatif,
  id_l_avoir did_facultatif,
  id_l_apporteur did_facultatif,
  id_l_proforma did_facultatif,
  id_l_acompte did_facultatif,
  id_l_reglement did_facultatif,
  id_l_prelevement did_facultatif,
  id_l_avis_echeance did_facultatif,
  id_l_ticket_caisse did_facultatif,
  id_l_boncde_achat did_facultatif,
  id_l_flash did_facultatif,
  id_l_panier did_facultatif,
  id_l_preparation did_facultatif,
  qte did9facult,
  qte_Recue did9facult,
  unite_recue dlgr_codeL,
  num_lot varchar(50),
  unite dlgr_codeL,
  prix_unite did9facult,
  id_ligne_src did_facultatif,
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  etat bigint DEFAULT 0,
  CONSTRAINT ta_ligne_a_ligne_echeance_pkey PRIMARY KEY (id),
  CONSTRAINT fk_ta_ligne_a_ligne_echeance_1 FOREIGN KEY (id_l_apporteur)
      REFERENCES ta_l_apporteur (id_l_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_ligne_a_ligne_echeance_bon_reception FOREIGN KEY (id_l_bon_reception)
      REFERENCES ta_l_bon_reception (id_l_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,      
  CONSTRAINT fk_ta_ligne_a_ligne_echeance_2 FOREIGN KEY (id_l_proforma)
      REFERENCES ta_l_proforma (id_l_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_ligne_a_ligne_echeance_3 FOREIGN KEY (id_l_prelevement)
      REFERENCES ta_l_prelevement (id_l_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_ligne_a_ligne_echeance_4 FOREIGN KEY (id_l_acompte)
      REFERENCES ta_l_acompte (id_l_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_ligne_a_ligne_echeance_id_avis_echeance_fkey1 FOREIGN KEY (id_l_avis_echeance)
      REFERENCES ta_l_avis_echeance (id_l_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_ligne_a_ligne_echeance_id_avoir_fkey FOREIGN KEY (id_l_avoir)
      REFERENCES ta_l_avoir (id_l_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_ligne_a_ligne_echeance_id_boncde_fkey FOREIGN KEY (id_l_boncde)
      REFERENCES ta_l_boncde (id_l_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_ligne_a_ligne_echeance_id_bonliv_fkey FOREIGN KEY (id_l_bonliv)
      REFERENCES ta_l_bonliv (id_l_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_ligne_a_ligne_echeance_id_devis_fkey FOREIGN KEY (id_l_devis)
      REFERENCES ta_l_devis (id_l_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_ligne_a_ligne_echeance_id_facture_fkey FOREIGN KEY (id_l_facture)
      REFERENCES ta_l_facture (id_l_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_ligne_a_ligne_echeance_id_ticket_caisse_fkey FOREIGN KEY (id_l_ticket_caisse)
      REFERENCES ta_l_ticket_caisse (id_l_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_ligne_a_ligne_echeance_id_boncde_achat_fkey FOREIGN KEY (id_l_boncde_achat)
      REFERENCES ta_l_boncde_achat (id_l_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_ligne_a_ligne_echeance_id_echeance_fkey FOREIGN KEY (id_l_echeance)
      REFERENCES ta_l_echeance (id_l_echeance) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_ligne_a_ligne_echeance_id_flash_fkey FOREIGN KEY (id_l_flash)
      REFERENCES ta_l_flash (id_l_flash) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_ligne_a_ligne_echeance_id_panier_fkey FOREIGN KEY (id_l_panier)
      REFERENCES ta_l_panier(id_l_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_ligne_a_ligne_echeance_id_preparation_fkey FOREIGN KEY (id_l_preparation)
      REFERENCES ta_l_preparation(id_l_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
      
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_ligne_a_ligne_echeance
  OWNER TO bdg;



CREATE TRIGGER tbi_ligne_a_ligne_echeance
  BEFORE INSERT
  ON ta_ligne_a_ligne_echeance
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();



CREATE TRIGGER tbu_ligne_a_ligne_echeance
  BEFORE UPDATE
  ON ta_ligne_a_ligne_echeance
  FOR EACH ROW
  EXECUTE PROCEDURE before_update();
