


--table de relation entre document
CREATE TABLE ta_r_etat
(
  id_r_etat serial NOT NULL,
  id_etat did_facultatif not null,
  id_facture did_facultatif,
  id_devis did_facultatif,
  id_boncde did_facultatif,
  id_bonliv did_facultatif,
  id_avoir did_facultatif,
  id_apporteur did_facultatif,
  id_proforma did_facultatif,
  id_acompte did_facultatif,
  id_reglement did_facultatif,
  id_prelevement did_facultatif,
  id_avis_echeance did_facultatif,
  id_ticket_caisse did_facultatif,
  id_boncde_achat did_facultatif,
  id_bon_reception did_facultatif,
  date_etat date_heure_lgr,
  commentaire text, 
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_r_etat_pkey PRIMARY KEY (id_r_etat),
    CONSTRAINT fk_ta_r_etat_etat FOREIGN KEY (id_etat)
      REFERENCES ta_etat (id_etat) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_r_etat_avis FOREIGN KEY (id_avis_echeance)
      REFERENCES ta_avis_echeance (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_r_etat_apporteur FOREIGN KEY (id_apporteur)
      REFERENCES ta_apporteur (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_r_etat_proforma FOREIGN KEY (id_proforma)
      REFERENCES ta_proforma (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_r_etat_prelevement FOREIGN KEY (id_prelevement)
      REFERENCES ta_prelevement (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_r_etat_acompte FOREIGN KEY (id_acompte)
      REFERENCES ta_acompte (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_r_etat_id_avoir FOREIGN KEY (id_avoir)
      REFERENCES ta_avoir (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_r_etat_id_boncde FOREIGN KEY (id_boncde)
      REFERENCES ta_boncde (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_r_etat_id_bonliv FOREIGN KEY (id_bonliv)
      REFERENCES ta_bonliv (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_r_etat_id_devis FOREIGN KEY (id_devis)
      REFERENCES ta_devis (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_r_etat_id_facture FOREIGN KEY (id_facture)
      REFERENCES ta_facture (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_r_etat_id_reglement FOREIGN KEY (id_reglement)
      REFERENCES ta_reglement (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_r_etat_id_ticket_caisse FOREIGN KEY (id_ticket_caisse)
      REFERENCES ta_ticket_caisse (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_r_etat_id_boncde_achat FOREIGN KEY (id_boncde_achat)
      REFERENCES ta_boncde_achat (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,  
  CONSTRAINT ta_r_etat_id_bon_reception FOREIGN KEY (id_bon_reception)
      REFERENCES ta_bon_reception (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION 
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_r_etat
  OWNER TO bdg;


CREATE INDEX fk_ta_r_etat_avis
  ON ta_r_etat
  USING btree
  (id_avis_echeance);


CREATE INDEX fk_ta_r_etat_apporteur
  ON ta_r_etat
  USING btree
  (id_apporteur);


CREATE INDEX fk_ta_r_etat_proforma
  ON ta_r_etat
  USING btree
  (id_proforma);


CREATE INDEX fk_ta_r_etat_prelevement
  ON ta_r_etat
  USING btree
  (id_prelevement);


CREATE INDEX fk_ta_r_etat_acompte
  ON ta_r_etat
  USING btree
  (id_acompte);


CREATE INDEX ta_r_etat_id_facture
  ON ta_r_etat
  USING btree
  (id_facture);


CREATE INDEX ta_r_etat_id_devis
  ON ta_r_etat
  USING btree
  (id_devis);


CREATE INDEX ta_r_etat_id_boncde
  ON ta_r_etat
  USING btree
  (id_boncde);


CREATE INDEX ta_r_etat_id_bonliv
  ON ta_r_etat
  USING btree
  (id_bonliv);


CREATE INDEX ta_r_etat_id_avoir
  ON ta_r_etat
  USING btree
  (id_avoir);


CREATE INDEX ta_r_etat_id_reglement
  ON ta_r_etat
  USING btree
  (id_reglement);


CREATE INDEX ta_r_etat_id_ticket_caisse
  ON ta_r_etat
  USING btree
  (id_ticket_caisse);


CREATE INDEX ta_r_etat_id_boncde_achat
  ON ta_r_etat
  USING btree
  (id_boncde_achat);


CREATE INDEX ta_r_etat_id_bon_reception
  ON ta_r_etat
  USING btree
  (id_bon_reception);
    

CREATE TRIGGER tbi_r_etat
  BEFORE INSERT
  ON ta_r_etat
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();


CREATE TRIGGER tbu_r_etat
  BEFORE UPDATE
  ON ta_r_etat
  FOR EACH ROW
  EXECUTE PROCEDURE before_update();

--table historique relation états
CREATE TABLE ta_hist_r_etat
(
  id_hist_r_etat serial NOT NULL,
    id_etat did_facultatif not null,
  id_facture did_facultatif,
  id_devis did_facultatif,
  id_boncde did_facultatif,
  id_bonliv did_facultatif,
  id_avoir did_facultatif,
  id_apporteur did_facultatif,
  id_proforma did_facultatif,
  id_acompte did_facultatif,
  id_reglement did_facultatif,
  id_prelevement did_facultatif,
  id_avis_echeance did_facultatif,
  id_ticket_caisse did_facultatif,
  id_boncde_achat did_facultatif,
  id_bon_reception did_facultatif,
  date_etat date_heure_lgr,
  commentaire text, 
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_hist_r_etat_pkey PRIMARY KEY (id_hist_r_etat),
  CONSTRAINT fk_ta_hist_r_etat_etat FOREIGN KEY (id_etat)
      REFERENCES ta_etat (id_etat) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,  
  CONSTRAINT fk_ta_hist_r_etat_avis FOREIGN KEY (id_avis_echeance)
      REFERENCES ta_avis_echeance (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_hist_r_etat_apporteur FOREIGN KEY (id_apporteur)
      REFERENCES ta_apporteur (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_hist_r_etat_proforma FOREIGN KEY (id_proforma)
      REFERENCES ta_proforma (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_hist_r_etat_prelevement FOREIGN KEY (id_prelevement)
      REFERENCES ta_prelevement (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_hist_r_etat_acompte FOREIGN KEY (id_acompte)
      REFERENCES ta_acompte (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_hist_r_etat_id_avoir FOREIGN KEY (id_avoir)
      REFERENCES ta_avoir (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_hist_r_etat_id_boncde FOREIGN KEY (id_boncde)
      REFERENCES ta_boncde (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_hist_r_etat_id_bonliv FOREIGN KEY (id_bonliv)
      REFERENCES ta_bonliv (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_hist_r_etat_id_devis FOREIGN KEY (id_devis)
      REFERENCES ta_devis (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_hist_r_etat_id_facture FOREIGN KEY (id_facture)
      REFERENCES ta_facture (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_hist_r_etat_id_reglement FOREIGN KEY (id_reglement)
      REFERENCES ta_reglement (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_hist_r_etat_id_ticket_caisse FOREIGN KEY (id_ticket_caisse)
      REFERENCES ta_ticket_caisse (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_hist_r_etat_id_boncde_achat FOREIGN KEY (id_boncde_achat)
      REFERENCES ta_boncde_achat (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,  
  CONSTRAINT ta_hist_r_etat_id_bon_reception FOREIGN KEY (id_bon_reception)
      REFERENCES ta_bon_reception (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION 
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_hist_r_etat
  OWNER TO bdg;


CREATE INDEX fk_ta_hist_r_etat_avis
  ON ta_hist_r_etat
  USING btree
  (id_avis_echeance);


CREATE INDEX fk_ta_hist_r_etat_apporteur
  ON ta_hist_r_etat
  USING btree
  (id_apporteur);


CREATE INDEX fk_ta_hist_r_etat_proforma
  ON ta_hist_r_etat
  USING btree
  (id_proforma);


CREATE INDEX fk_ta_hist_r_etat_prelevement
  ON ta_hist_r_etat
  USING btree
  (id_prelevement);


CREATE INDEX fk_ta_hist_r_etat_acompte
  ON ta_hist_r_etat
  USING btree
  (id_acompte);


CREATE INDEX ta_hist_r_etat_id_facture
  ON ta_hist_r_etat
  USING btree
  (id_facture);


CREATE INDEX ta_hist_r_etat_id_devis
  ON ta_hist_r_etat
  USING btree
  (id_devis);


CREATE INDEX ta_hist_r_etat_id_boncde
  ON ta_hist_r_etat
  USING btree
  (id_boncde);


CREATE INDEX ta_hist_r_etat_id_bonliv
  ON ta_hist_r_etat
  USING btree
  (id_bonliv);


CREATE INDEX ta_hist_r_etat_id_avoir
  ON ta_hist_r_etat
  USING btree
  (id_avoir);


CREATE INDEX ta_hist_r_etat_id_reglement
  ON ta_hist_r_etat
  USING btree
  (id_reglement);


CREATE INDEX ta_hist_r_etat_id_ticket_caisse
  ON ta_hist_r_etat
  USING btree
  (id_ticket_caisse);


CREATE INDEX ta_hist_r_etat_id_boncde_achat
  ON ta_hist_r_etat
  USING btree
  (id_boncde_achat);


CREATE INDEX ta_hist_r_etat_id_bon_reception
  ON ta_hist_r_etat
  USING btree
  (id_bon_reception);
    

CREATE TRIGGER tbi_hist_r_etat
  BEFORE INSERT
  ON ta_hist_r_etat
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();


CREATE TRIGGER tbu_hist_r_etat
  BEFORE UPDATE
  ON ta_hist_r_etat
  FOR EACH ROW
  EXECUTE PROCEDURE before_update();


ALTER TABLE ta_l_abonnement
  ADD COLUMN id_r_etat did_facultatif;

ALTER TABLE ta_l_facture
  ADD COLUMN id_r_etat did_facultatif;

  ALTER TABLE ta_l_avoir
  ADD COLUMN id_r_etat did_facultatif;

  ALTER TABLE ta_l_devis
  ADD COLUMN id_r_etat did_facultatif;

  ALTER TABLE ta_l_apporteur
  ADD COLUMN id_r_etat did_facultatif;

  ALTER TABLE ta_l_boncde
  ADD COLUMN id_r_etat did_facultatif;

  ALTER TABLE ta_l_boncde_achat
  ADD COLUMN id_r_etat did_facultatif;

  ALTER TABLE ta_l_bonliv
  ADD COLUMN id_r_etat did_facultatif;

  ALTER TABLE ta_l_proforma
  ADD COLUMN id_r_etat did_facultatif;

  ALTER TABLE ta_l_avis_echeance
  ADD COLUMN id_r_etat did_facultatif;

  ALTER TABLE ta_l_prelevement
  ADD COLUMN id_r_etat did_facultatif;

  ALTER TABLE ta_l_acompte
  ADD COLUMN id_r_etat did_facultatif;

  ALTER TABLE ta_l_bon_reception
  ADD COLUMN id_r_etat did_facultatif;

  ALTER TABLE ta_l_fabrication_mp
  ADD COLUMN id_r_etat did_facultatif;

  ALTER TABLE ta_l_fabrication_pf
  ADD COLUMN id_r_etat did_facultatif;
  
  ALTER TABLE ta_l_echeance
  ADD COLUMN id_r_etat did_facultatif;

  ALTER TABLE ta_l_inventaire
  ADD COLUMN id_r_etat did_facultatif;
 
  ALTER TABLE ta_l_modele_fabrication_mp
  ADD COLUMN id_r_etat did_facultatif;
  
  ALTER TABLE ta_l_modele_fabrication_pf
  ADD COLUMN id_r_etat did_facultatif;
  
  ALTER TABLE ta_l_recette
  ADD COLUMN id_r_etat did_facultatif;
  
  ALTER TABLE ta_l_relance
  ADD COLUMN id_r_etat did_facultatif;
  
  ALTER TABLE ta_l_remise
  ADD COLUMN id_r_etat did_facultatif;
  
  ALTER TABLE ta_l_session_caisse
  ADD COLUMN id_r_etat did_facultatif;
  
  ALTER TABLE ta_l_ticket_caisse
  ADD COLUMN id_r_etat did_facultatif;

   
ALTER TABLE ta_l_abonnement
ADD FOREIGN KEY (id_r_etat) REFERENCES ta_r_etat_ligne_doc (id_r_etat_ligne_doc) ON UPDATE NO ACTION ON DELETE NO ACTION;
  
ALTER TABLE ta_l_facture
ADD FOREIGN KEY (id_r_etat) REFERENCES ta_r_etat_ligne_doc (id_r_etat_ligne_doc) ON UPDATE NO ACTION ON DELETE NO ACTION;

  ALTER TABLE ta_l_avoir
ADD FOREIGN KEY (id_r_etat) REFERENCES ta_r_etat_ligne_doc (id_r_etat_ligne_doc) ON UPDATE NO ACTION ON DELETE NO ACTION;

  ALTER TABLE ta_l_devis
ADD FOREIGN KEY (id_r_etat) REFERENCES ta_r_etat_ligne_doc (id_r_etat_ligne_doc) ON UPDATE NO ACTION ON DELETE NO ACTION;

  ALTER TABLE ta_l_apporteur
ADD FOREIGN KEY (id_r_etat) REFERENCES ta_r_etat_ligne_doc (id_r_etat_ligne_doc) ON UPDATE NO ACTION ON DELETE NO ACTION;

  ALTER TABLE ta_l_boncde
ADD FOREIGN KEY (id_r_etat) REFERENCES ta_r_etat_ligne_doc (id_r_etat_ligne_doc) ON UPDATE NO ACTION ON DELETE NO ACTION;

  ALTER TABLE ta_l_boncde_achat
ADD FOREIGN KEY (id_r_etat) REFERENCES ta_r_etat_ligne_doc (id_r_etat_ligne_doc) ON UPDATE NO ACTION ON DELETE NO ACTION;

  ALTER TABLE ta_l_bonliv
ADD FOREIGN KEY (id_r_etat) REFERENCES ta_r_etat_ligne_doc (id_r_etat_ligne_doc) ON UPDATE NO ACTION ON DELETE NO ACTION;

  ALTER TABLE ta_l_proforma
ADD FOREIGN KEY (id_r_etat) REFERENCES ta_r_etat_ligne_doc (id_r_etat_ligne_doc) ON UPDATE NO ACTION ON DELETE NO ACTION;

  ALTER TABLE ta_l_avis_echeance
ADD FOREIGN KEY (id_r_etat) REFERENCES ta_r_etat_ligne_doc (id_r_etat_ligne_doc) ON UPDATE NO ACTION ON DELETE NO ACTION;

  ALTER TABLE ta_l_prelevement
ADD FOREIGN KEY (id_r_etat) REFERENCES ta_r_etat_ligne_doc (id_r_etat_ligne_doc) ON UPDATE NO ACTION ON DELETE NO ACTION;

  ALTER TABLE ta_l_acompte
ADD FOREIGN KEY (id_r_etat) REFERENCES ta_r_etat_ligne_doc (id_r_etat_ligne_doc) ON UPDATE NO ACTION ON DELETE NO ACTION;

  ALTER TABLE ta_l_bon_reception
ADD FOREIGN KEY (id_r_etat) REFERENCES ta_r_etat_ligne_doc (id_r_etat_ligne_doc) ON UPDATE NO ACTION ON DELETE NO ACTION;

  ALTER TABLE ta_l_fabrication_mp
ADD FOREIGN KEY (id_r_etat) REFERENCES ta_r_etat_ligne_doc (id_r_etat_ligne_doc) ON UPDATE NO ACTION ON DELETE NO ACTION;

  ALTER TABLE ta_l_fabrication_pf
ADD FOREIGN KEY (id_r_etat) REFERENCES ta_r_etat_ligne_doc (id_r_etat_ligne_doc) ON UPDATE NO ACTION ON DELETE NO ACTION;
  
  ALTER TABLE ta_l_echeance
ADD FOREIGN KEY (id_r_etat) REFERENCES ta_r_etat_ligne_doc (id_r_etat_ligne_doc) ON UPDATE NO ACTION ON DELETE NO ACTION;

  ALTER TABLE ta_l_inventaire
ADD FOREIGN KEY (id_r_etat) REFERENCES ta_r_etat_ligne_doc (id_r_etat_ligne_doc) ON UPDATE NO ACTION ON DELETE NO ACTION;
 
  ALTER TABLE ta_l_modele_fabrication_mp
ADD FOREIGN KEY (id_r_etat) REFERENCES ta_r_etat_ligne_doc (id_r_etat_ligne_doc) ON UPDATE NO ACTION ON DELETE NO ACTION;
  
  ALTER TABLE ta_l_modele_fabrication_pf
ADD FOREIGN KEY (id_r_etat) REFERENCES ta_r_etat_ligne_doc (id_r_etat_ligne_doc) ON UPDATE NO ACTION ON DELETE NO ACTION;
  
  ALTER TABLE ta_l_recette
ADD FOREIGN KEY (id_r_etat) REFERENCES ta_r_etat_ligne_doc (id_r_etat_ligne_doc) ON UPDATE NO ACTION ON DELETE NO ACTION;
  
  ALTER TABLE ta_l_relance
ADD FOREIGN KEY (id_r_etat) REFERENCES ta_r_etat_ligne_doc (id_r_etat_ligne_doc) ON UPDATE NO ACTION ON DELETE NO ACTION;
  
  ALTER TABLE ta_l_remise
ADD FOREIGN KEY (id_r_etat) REFERENCES ta_r_etat_ligne_doc (id_r_etat_ligne_doc) ON UPDATE NO ACTION ON DELETE NO ACTION;
  
  ALTER TABLE ta_l_session_caisse
ADD FOREIGN KEY (id_r_etat) REFERENCES ta_r_etat_ligne_doc (id_r_etat_ligne_doc) ON UPDATE NO ACTION ON DELETE NO ACTION;
  
  ALTER TABLE ta_l_ticket_caisse
ADD FOREIGN KEY (id_r_etat) REFERENCES ta_r_etat_ligne_doc (id_r_etat_ligne_doc) ON UPDATE NO ACTION ON DELETE NO ACTION; 



ALTER TABLE ta_abonnement
   add id_r_etat  did_facultatif;
   
   
ALTER TABLE ta_boncde_achat 
   add id_r_etat  did_facultatif;

ALTER TABLE ta_facture
  ADD COLUMN id_r_etat did_facultatif;

  ALTER TABLE ta_avoir
  ADD COLUMN id_r_etat did_facultatif;

  ALTER TABLE ta_devis
  ADD COLUMN id_r_etat did_facultatif;

  ALTER TABLE ta_apporteur
  ADD COLUMN id_r_etat did_facultatif;

  ALTER TABLE ta_bon_reception
  ADD COLUMN id_r_etat did_facultatif;
  
  ALTER TABLE ta_boncde
  ADD COLUMN id_r_etat did_facultatif;

  ALTER TABLE ta_bonliv
  ADD COLUMN id_r_etat did_facultatif;

  ALTER TABLE ta_proforma
  ADD COLUMN id_r_etat did_facultatif;

  ALTER TABLE ta_avis_echeance
  ADD COLUMN id_r_etat did_facultatif;

  ALTER TABLE ta_prelevement
  ADD COLUMN id_r_etat did_facultatif;

  ALTER TABLE ta_acompte
  ADD COLUMN id_r_etat did_facultatif;


  ALTER TABLE ta_fabrication
  ADD COLUMN id_r_etat did_facultatif;
  

  ALTER TABLE ta_inventaire
  ADD COLUMN id_r_etat did_facultatif;
 
  ALTER TABLE ta_modele_fabrication
  ADD COLUMN id_r_etat did_facultatif;
  
  ALTER TABLE ta_recette
  ADD COLUMN id_r_etat did_facultatif;
  
  ALTER TABLE ta_relance
  ADD COLUMN id_r_etat did_facultatif;
  
  ALTER TABLE ta_remise
  ADD COLUMN id_r_etat did_facultatif;
  
  ALTER TABLE ta_session_caisse
  ADD COLUMN id_r_etat did_facultatif;
  
  ALTER TABLE ta_ticket_caisse
  ADD COLUMN id_r_etat did_facultatif;
  
  ALTER TABLE ta_reglement
  ADD COLUMN id_r_etat did_facultatif;
  
ALTER TABLE ta_abonnement
ADD FOREIGN KEY (id_r_etat) REFERENCES ta_r_etat (id_r_etat) ON UPDATE NO ACTION ON DELETE NO ACTION;
  
ALTER TABLE ta_reglement
ADD FOREIGN KEY (id_r_etat) REFERENCES ta_r_etat (id_r_etat) ON UPDATE NO ACTION ON DELETE NO ACTION;
  
ALTER TABLE ta_facture
ADD FOREIGN KEY (id_r_etat) REFERENCES ta_r_etat (id_r_etat) ON UPDATE NO ACTION ON DELETE NO ACTION;

  ALTER TABLE ta_avoir
ADD FOREIGN KEY (id_r_etat) REFERENCES ta_r_etat (id_r_etat) ON UPDATE NO ACTION ON DELETE NO ACTION;

  ALTER TABLE ta_devis
ADD FOREIGN KEY (id_r_etat) REFERENCES ta_r_etat (id_r_etat) ON UPDATE NO ACTION ON DELETE NO ACTION;

  ALTER TABLE ta_apporteur
ADD FOREIGN KEY (id_r_etat) REFERENCES ta_r_etat (id_r_etat) ON UPDATE NO ACTION ON DELETE NO ACTION;

  ALTER TABLE ta_boncde
ADD FOREIGN KEY (id_r_etat) REFERENCES ta_r_etat (id_r_etat) ON UPDATE NO ACTION ON DELETE NO ACTION;

  ALTER TABLE ta_bon_reception
ADD FOREIGN KEY (id_r_etat) REFERENCES ta_r_etat (id_r_etat) ON UPDATE NO ACTION ON DELETE NO ACTION;

  ALTER TABLE ta_boncde_achat
ADD FOREIGN KEY (id_r_etat) REFERENCES ta_r_etat (id_r_etat) ON UPDATE NO ACTION ON DELETE NO ACTION;

  ALTER TABLE ta_bonliv
ADD FOREIGN KEY (id_r_etat) REFERENCES ta_r_etat (id_r_etat) ON UPDATE NO ACTION ON DELETE NO ACTION;

  ALTER TABLE ta_proforma
ADD FOREIGN KEY (id_r_etat) REFERENCES ta_r_etat (id_r_etat) ON UPDATE NO ACTION ON DELETE NO ACTION;

  ALTER TABLE ta_avis_echeance
ADD FOREIGN KEY (id_r_etat) REFERENCES ta_r_etat (id_r_etat) ON UPDATE NO ACTION ON DELETE NO ACTION;

  ALTER TABLE ta_prelevement
ADD FOREIGN KEY (id_r_etat) REFERENCES ta_r_etat (id_r_etat) ON UPDATE NO ACTION ON DELETE NO ACTION;

  ALTER TABLE ta_acompte
ADD FOREIGN KEY (id_r_etat) REFERENCES ta_r_etat (id_r_etat) ON UPDATE NO ACTION ON DELETE NO ACTION;


  ALTER TABLE ta_fabrication
ADD FOREIGN KEY (id_r_etat) REFERENCES ta_r_etat (id_r_etat) ON UPDATE NO ACTION ON DELETE NO ACTION;


  ALTER TABLE ta_inventaire
ADD FOREIGN KEY (id_r_etat) REFERENCES ta_r_etat (id_r_etat) ON UPDATE NO ACTION ON DELETE NO ACTION;
 
  ALTER TABLE ta_modele_fabrication
ADD FOREIGN KEY (id_r_etat) REFERENCES ta_r_etat (id_r_etat) ON UPDATE NO ACTION ON DELETE NO ACTION;
  

  ALTER TABLE ta_recette
ADD FOREIGN KEY (id_r_etat) REFERENCES ta_r_etat (id_r_etat) ON UPDATE NO ACTION ON DELETE NO ACTION;
  
  ALTER TABLE ta_relance
ADD FOREIGN KEY (id_r_etat) REFERENCES ta_r_etat (id_r_etat) ON UPDATE NO ACTION ON DELETE NO ACTION;
  
  ALTER TABLE ta_remise
ADD FOREIGN KEY (id_r_etat) REFERENCES ta_r_etat (id_r_etat) ON UPDATE NO ACTION ON DELETE NO ACTION;
  
  ALTER TABLE ta_session_caisse
ADD FOREIGN KEY (id_r_etat) REFERENCES ta_r_etat (id_r_etat) ON UPDATE NO ACTION ON DELETE NO ACTION;
  
  ALTER TABLE ta_ticket_caisse
ADD FOREIGN KEY (id_r_etat) REFERENCES ta_r_etat (id_r_etat) ON UPDATE NO ACTION ON DELETE NO ACTION;  


ALTER TABLE ta_boncde_achat  drop id_etat cascade;
ALTER TABLE ta_bon_reception drop id_etat cascade;
  
  
INSERT INTO ta_etat (code_etat,libelle_etat,qui_cree,quand_cree,qui_modif,quand_modif,"version",ip_acces,version_obj,systeme,visible,identifiant,listable,accepte,"position",conditions,commentaire,id_t_etat) VALUES 
('doc_brouillon','etat initial','bdg','2019-10-14 15:46:45.400','bdg','2019-10-14 15:46:45.400','2.0.13','::1/128',0,false,true,'bdg.etat.document.brouillon',true,true,1,NULL,NULL,1)
,('doc_commercial','document commercial','bdg','2019-10-14 15:46:45.421','bdg','2019-10-14 15:46:45.421','2.0.13','::1/128',0,false,true,'bdg.etat.document.commercial',true,true,2,NULL,NULL,2)
,('doc_preparation','document en préparation','bdg','2019-10-14 15:46:45.442','bdg','2019-10-14 15:46:45.442','2.0.13','::1/128',0,false,true,'bdg.etat.document.preparation',true,true,3,NULL,NULL,3)
,('doc_encours','document en cours transformable','bdg','2019-10-14 15:46:45.464','bdg','2019-10-14 15:46:45.464','2.0.13','::1/128',0,false,true,'bdg.etat.document.encours',true,true,4,NULL,NULL,4)
,('doc_part_Transforme','document partiellement transformé','bdg','2019-10-14 15:46:45.485','bdg','2019-10-14 15:46:45.485','2.0.13','::1/128',0,false,true,'bdg.etat.document.partiellement.transforme',true,true,4,NULL,NULL,4)
,('doc_part_Abandonne','document partiellement abandonné','bdg','2019-10-14 15:46:45.509','bdg','2019-10-14 15:46:45.509','2.0.13','::1/128',0,false,true,'bdg.etat.document.partiellement.abandonne',true,true,4,NULL,NULL,4)
,('doc_tot_Transforme','document totalement transformé','bdg','2019-10-14 15:46:45.552','bdg','2019-10-14 15:46:45.552','2.0.13','::1/128',0,false,true,'bdg.etat.document.termine.totalement.transforme',true,true,5,NULL,NULL,5)
,('doc_part_abandonne','document terminé et partiellement abandonné','bdg','2019-10-14 15:46:45.574','bdg','2019-10-14 15:46:45.574','2.0.13','::1/128',0,false,true,'bdg.etat.document.termine.partiellement.abandonne',true,true,5,NULL,NULL,5)
,('doc_abandonne','document terminé et abandonné','bdg','2019-10-14 15:46:45.574','bdg','2019-10-14 15:46:45.574','2.0.13','::1/128',0,false,true,'bdg.etat.document.termine.abandonne',true,true,5,NULL,NULL,5)
;  


--ALTER TABLE ta_ligne_a_ligne  ADD COLUMN unite dlgr_codeL;
ALTER TABLE ta_ligne_a_ligne alter COLUMN unite type dlgr_codel;

--------------implementation des mouvements prévisionnels-------------
--------------------------------------------------------------------

CREATE TABLE ta_gr_mouv_stock_prev
(
  id_gr_mouv_stock serial NOT NULL,
  code_gr_mouv_stock character varying(20),
  date_gr_mouv_stock date_lgr,
  libelle_gr_mouv_stock dlib255nn,
  id_etat did_facultatif,
  commentaire dlib_commentaire,
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  id_type_mouv did_facultatif,
  id_fabrication did_facultatif,
  id_boncde did_facultatif,
  id_boncde_achat did_facultatif,
  id_devis did_facultatif,
  id_avis_echeance did_facultatif,
  id_proforma did_facultatif,
  manuel boolean NOT NULL DEFAULT false,

  CONSTRAINT ta_gr_mouv_stock_prev_pkey PRIMARY KEY (id_gr_mouv_stock),
  CONSTRAINT ta_gr_mouv_stock_prev_fk2 FOREIGN KEY (id_type_mouv)
      REFERENCES ta_type_mouvement (id_type_mouvement) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_gr_mouv_stock_prev_id_etat_fkey FOREIGN KEY (id_etat)
      REFERENCES ta_etat (id_etat) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
 CONSTRAINT fabrication_foreignkey FOREIGN KEY (id_fabrication)
      REFERENCES ta_fabrication (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT devis_foreignkey FOREIGN KEY (id_devis)
      REFERENCES ta_devis (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT proforma_foreignkey FOREIGN KEY (id_proforma)
      REFERENCES ta_proforma (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT boncde_foreignkey FOREIGN KEY (id_boncde)
      REFERENCES ta_boncde (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT boncde_achat_foreignkey FOREIGN KEY (id_boncde_achat)
      REFERENCES ta_boncde_achat (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT avis_echeance_foreignkey FOREIGN KEY (id_avis_echeance)
      REFERENCES ta_avis_echeance (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
           
  CONSTRAINT ta_grmouv_prev_code_unique UNIQUE (code_gr_mouv_stock),
  CONSTRAINT doc_not_nul_prev CHECK (id_fabrication IS NOT NULL OR id_boncde IS NOT NULL OR id_boncde_achat IS NOT NULL OR id_devis IS NOT NULL OR id_avis_echeance IS NOT NULL OR id_proforma IS NOT NULL OR manuel IS NOT NULL)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_gr_mouv_stock_prev
  OWNER TO bdg;



CREATE INDEX ta_gr_mouv_stock_prev_code
  ON ta_gr_mouv_stock_prev
  USING btree
  (code_gr_mouv_stock COLLATE pg_catalog."default");


CREATE TRIGGER tbi_gr_mouv_stock_prev
  BEFORE INSERT
  ON ta_gr_mouv_stock_prev
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();


CREATE TRIGGER tbu_gr_mouv_stock_prev
  BEFORE UPDATE
  ON ta_gr_mouv_stock_prev
  FOR EACH ROW
  EXECUTE PROCEDURE before_update();



CREATE TABLE ta_mouvement_stock_prev
(
  id_mouvement_stock serial NOT NULL,
  date_stock date,
  libelle_stock dlib255nn,
  qte1_stock qte3,
  un1_stock character varying(20),
  qte2_stock qte3,
  un2_stock character varying(20),
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  description character varying(500),
  id_gr_mouv_stock did_facultatif NOT NULL,
  id_entrepot did_facultatif,
  emplacement dlib255,
  unite_ref character varying(20),
  qte_ref qte3,
  CONSTRAINT ta_stock_prev_pkey PRIMARY KEY (id_mouvement_stock),
  CONSTRAINT ta_mouvement_stock_prev_fk FOREIGN KEY (id_gr_mouv_stock)
      REFERENCES ta_gr_mouv_stock_prev (id_gr_mouv_stock) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_mouvement_stock_prev
  OWNER TO bdg;

ALTER TABLE ta_mouvement_stock_prev 
ADD COLUMN id_lot did_facultatif;

ALTER TABLE ta_mouvement_stock_prev 
ADD COLUMN lettrage_deplacement dlib255;

-- Function: conversion_unite_ref()

-- DROP FUNCTION conversion_unite_ref();

CREATE OR REPLACE FUNCTION conversion_unite_ref_prev()
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
	select u.code_unite into u_ref from ta_article a join ta_lot l on l.id_article=a.id_article join ta_unite u on u.id_unite=a.id_unite_reference where l.id_lot=new.id_lot ;
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
ALTER FUNCTION conversion_unite_ref_prev()
  OWNER TO bdg;



CREATE TRIGGER conversion_u_ref_prev
  BEFORE INSERT OR UPDATE
  ON ta_mouvement_stock_prev
  FOR EACH ROW
  EXECUTE PROCEDURE conversion_unite_ref_prev();



-- CREATE TRIGGER ta_mouvement_stock_prev_tr
--   AFTER INSERT OR UPDATE
--   ON ta_mouvement_stock_prev
--   FOR EACH ROW
--   EXECUTE PROCEDURE maj_etat_stock();
-- 
-- 
-- CREATE TRIGGER ta_mouvement_stock_prev_tr_delete
--   AFTER DELETE
--   ON ta_mouvement_stock_prev
--   FOR EACH ROW
--   EXECUTE PROCEDURE maj_etat_stock_prev_delete();


CREATE TRIGGER tbi_ta_stock_prev
  BEFORE INSERT
  ON ta_mouvement_stock_prev
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();


CREATE TRIGGER tbu_ta_stock_prev
  BEFORE UPDATE
  ON ta_mouvement_stock_prev
  FOR EACH ROW
  EXECUTE PROCEDURE before_update();


ALTER TABLE ta_fabrication  ADD COLUMN id_gr_mouv_stock_prev did_facultatif;
ALTER TABLE ta_boncde  ADD COLUMN id_gr_mouv_stock_prev did_facultatif;
ALTER TABLE ta_boncde_achat  ADD COLUMN id_gr_mouv_stock_prev did_facultatif;
ALTER TABLE ta_devis  ADD COLUMN id_gr_mouv_stock_prev did_facultatif;
ALTER TABLE ta_proforma  ADD COLUMN id_gr_mouv_stock_prev did_facultatif;
ALTER TABLE ta_avis_echeance  ADD COLUMN id_gr_mouv_stock_prev did_facultatif;

ALTER TABLE ta_l_fabrication_pf  ADD COLUMN id_mouvement_stock_prev did_facultatif;
ALTER TABLE ta_l_fabrication_mp  ADD COLUMN id_mouvement_stock_prev did_facultatif;
ALTER TABLE ta_l_boncde  ADD COLUMN id_mouvement_stock_prev did_facultatif;
ALTER TABLE ta_l_boncde_achat  ADD COLUMN id_mouvement_stock_prev did_facultatif;
ALTER TABLE ta_l_devis  ADD COLUMN id_mouvement_stock_prev did_facultatif;
ALTER TABLE ta_l_proforma  ADD COLUMN id_mouvement_stock_prev did_facultatif;
ALTER TABLE ta_l_avis_echeance  ADD COLUMN id_mouvement_stock_prev did_facultatif;


  ALTER TABLE ta_l_boncde
ADD FOREIGN KEY (id_mouvement_stock_prev) REFERENCES ta_etat (id_etat) ON UPDATE NO ACTION ON DELETE NO ACTION;


CREATE TABLE ta_etat_stock_prev
(
  id_etat_stock serial NOT NULL,
  date_etat_stock date,
  libelle_etat_stock dlib255nn,
  qte1_etat_stock qte3,
  un1_etat_stock character varying(20),
  qte2_etat_stock qte3,
  un2_etat_stock character varying(20),
  id_entrepot did_facultatif,
  description character varying(500),
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  emplacement character varying(255),
  id_article did_facultatif NOT NULL DEFAULT 0,
  unite_ref character varying(20),
  qte_ref qte3,
  CONSTRAINT id_etat_stock_prev_cle PRIMARY KEY (id_etat_stock),
  CONSTRAINT ta_etat_stock_prev_id_entrepot_org_fkey FOREIGN KEY (id_entrepot)
      REFERENCES ta_entrepot (id_entrepot) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_etat_stock_prev
  OWNER TO bdg;



CREATE TRIGGER ta_etat_stock_prev_tr
  AFTER UPDATE
  ON ta_etat_stock_prev
  FOR EACH ROW
  EXECUTE PROCEDURE tr_af_update_etat_stock();



CREATE TRIGGER tbi_ta_etat_stock_prev
  BEFORE INSERT
  ON ta_etat_stock_prev
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();


CREATE TRIGGER tbu_ta_etat_stock_prev
  BEFORE UPDATE
  ON ta_etat_stock_prev
  FOR EACH ROW
  EXECUTE PROCEDURE before_update();




------------------- fin implémentation lien ligne à ligne ----------------
-------------------------------------------------------------------------
