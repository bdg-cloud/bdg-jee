--set schema 'demo';

------------------- Debut implémentation lien ligne à ligne ----------------
-------------------------------------------------------------------------


--drop TABLE ta_ligne_a_ligne;

CREATE TABLE ta_ligne_a_ligne
(
  id serial NOT NULL,
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
  CONSTRAINT ta_ligne_a_ligne_pkey PRIMARY KEY (id),
  CONSTRAINT fk_ta_ligne_a_ligne_1 FOREIGN KEY (id_l_apporteur)
      REFERENCES ta_l_apporteur (id_l_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_ligne_a_ligne_bon_reception FOREIGN KEY (id_l_bon_reception)
      REFERENCES ta_l_bon_reception (id_l_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,      
  CONSTRAINT fk_ta_ligne_a_ligne_2 FOREIGN KEY (id_l_proforma)
      REFERENCES ta_l_proforma (id_l_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_ligne_a_ligne_3 FOREIGN KEY (id_l_prelevement)
      REFERENCES ta_l_prelevement (id_l_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_ligne_a_ligne_4 FOREIGN KEY (id_l_acompte)
      REFERENCES ta_l_acompte (id_l_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_ligne_a_ligne_id_avis_echeance_fkey1 FOREIGN KEY (id_l_avis_echeance)
      REFERENCES ta_l_avis_echeance (id_l_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_ligne_a_ligne_id_avoir_fkey FOREIGN KEY (id_l_avoir)
      REFERENCES ta_l_avoir (id_l_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_ligne_a_ligne_id_boncde_fkey FOREIGN KEY (id_l_boncde)
      REFERENCES ta_l_boncde (id_l_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_ligne_a_ligne_id_bonliv_fkey FOREIGN KEY (id_l_bonliv)
      REFERENCES ta_l_bonliv (id_l_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_ligne_a_ligne_id_devis_fkey FOREIGN KEY (id_l_devis)
      REFERENCES ta_l_devis (id_l_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_ligne_a_ligne_id_facture_fkey FOREIGN KEY (id_l_facture)
      REFERENCES ta_l_facture (id_l_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_ligne_a_ligne_id_ticket_caisse_fkey FOREIGN KEY (id_l_ticket_caisse)
      REFERENCES ta_l_ticket_caisse (id_l_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_ligne_a_ligne_id_boncde_achat_fkey FOREIGN KEY (id_l_boncde_achat)
      REFERENCES ta_l_boncde_achat (id_l_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
      
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_ligne_a_ligne
  OWNER TO bdg;



CREATE TRIGGER tbi_ligne_a_ligne
  BEFORE INSERT
  ON ta_ligne_a_ligne
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();



CREATE TRIGGER tbu_ligne_a_ligne
  BEFORE UPDATE
  ON ta_ligne_a_ligne
  FOR EACH ROW
  EXECUTE PROCEDURE before_update();



ALTER TABLE ta_r_document
  ADD COLUMN id_bon_reception did_facultatif;
  
ALTER TABLE ta_l_boncde_achat  ADD COLUMN date_prevue date_heure_lgr;
ALTER TABLE ta_l_boncde  ADD COLUMN date_prevue date_heure_lgr;
ALTER TABLE ta_l_devis  ADD COLUMN date_prevue date_heure_lgr;
ALTER TABLE ta_l_proforma  ADD COLUMN date_prevue date_heure_lgr;
ALTER TABLE ta_l_avis_echeance  ADD COLUMN date_prevue date_heure_lgr;
ALTER TABLE ta_l_bonliv  ADD COLUMN date_prevue date_heure_lgr;  



 

      

ALTER TABLE ta_remise
  ADD COLUMN id_etat did_facultatif;
  
  ALTER TABLE ta_remise
ADD FOREIGN KEY (id_etat) REFERENCES ta_etat (id_etat) ON UPDATE NO ACTION ON DELETE NO ACTION;   



ALTER SEQUENCE ta_t_c_paiement_id_t_c_paiement_seq RESTART WITH 12;

INSERT INTO ta_t_c_paiement
( code_t_c_paiement, libl_t_c_paiement,  version_obj, systeme)
VALUES( 'BoncdeAchat', 'Bon de commande achat', 0, true);
INSERT INTO ta_t_c_paiement
( code_t_c_paiement, libl_t_c_paiement,  version_obj, systeme)
VALUES( 'BonReception', 'Bon de réception', 0, true);
INSERT INTO ta_t_c_paiement
( code_t_c_paiement, libl_t_c_paiement,  version_obj, systeme)
VALUES( 'TicketDeCaisse', 'Ticket de caisse', 0, true);


delete from ta_t_doc;
ALTER SEQUENCE ta_t_doc_id_t_doc_seq RESTART WITH 1;

INSERT INTO ta_t_doc ( code_t_doc, lib_t_doc, qui_cree,  qui_modif,  version_obj, systeme) VALUES( 'Acompte', 'Facture d''acompte', 'bdg',  'bdg',  0, true);
INSERT INTO ta_t_doc ( code_t_doc, lib_t_doc, qui_cree,  qui_modif,  version_obj, systeme) VALUES( 'Apporteur', 'Facture apporteur', 'bdg',  'bdg',  0, true);
INSERT INTO ta_t_doc ( code_t_doc, lib_t_doc, qui_cree,  qui_modif,  version_obj, systeme) VALUES( 'AvisEcheance', 'Avis d''Echéance', 'bdg',  'bdg',  0, true);
INSERT INTO ta_t_doc ( code_t_doc, lib_t_doc, qui_cree,  qui_modif,  version_obj, systeme) VALUES( 'Avoir', 'Facture d''avoir',  'bdg',  'bdg',  0, true);
INSERT INTO ta_t_doc ( code_t_doc, lib_t_doc, qui_cree,  qui_modif,  version_obj, systeme) VALUES( 'Boncde', 'Bon de commande',  'bdg',  'bdg',  0, true);
INSERT INTO ta_t_doc ( code_t_doc, lib_t_doc, qui_cree,  qui_modif,  version_obj, systeme) VALUES( 'BoncdeAchat', 'Bon de commande Achat', 'bdg',  'bdg',  0, true);
INSERT INTO ta_t_doc ( code_t_doc, lib_t_doc, qui_cree,  qui_modif,  version_obj, systeme) VALUES( 'Bonliv', 'Bon de livraison', 'bdg',  'bdg',  0, true);
INSERT INTO ta_t_doc ( code_t_doc, lib_t_doc, qui_cree,  qui_modif,  version_obj, systeme) VALUES( 'BonReception', 'Bon de réception', 'bdg',  'bdg',  0, true);
INSERT INTO ta_t_doc ( code_t_doc, lib_t_doc, qui_cree,  qui_modif,  version_obj, systeme) VALUES( 'Devis', 'Devis', 'bdg',  'bdg',  0, true);
INSERT INTO ta_t_doc ( code_t_doc, lib_t_doc, qui_cree,  qui_modif,  version_obj, systeme) VALUES( 'Facture', 'Facture', 'bdg',  'bdg',  0, true);
INSERT INTO ta_t_doc ( code_t_doc, lib_t_doc, qui_cree,  qui_modif,  version_obj, systeme) VALUES( 'FactureAchat', 'facture d''Achat', 'bdg',  'bdg',  0, true);
INSERT INTO ta_t_doc ( code_t_doc, lib_t_doc, qui_cree,  qui_modif,  version_obj, systeme) VALUES( 'Prelevement', 'Prelevement', 'bdg',  'bdg',  0, true);
INSERT INTO ta_t_doc ( code_t_doc, lib_t_doc, qui_cree,  qui_modif,  version_obj, systeme) VALUES( 'Proforma', 'Proforma', 'bdg',  'bdg',  0, true);
INSERT INTO ta_t_doc ( code_t_doc, lib_t_doc, qui_cree,  qui_modif,  version_obj, systeme) VALUES( 'TicketDeCaisse', 'Ticket de caisse', 'bdg',  'bdg',  0, true);




INSERT INTO ta_categorie_preference
(id_categorie_preference, id_parent_categorie_preference, code_categorie_preference, libelle_categorie_preference, description_categorie_preference, identifiant_module, "position")
VALUES(40, 1, NULL, 'Bon de commande achat', NULL, NULL, 12);







CREATE TABLE ta_t_etat
(
  id_t_etat serial NOT NULL,
  code_t_etat  character varying(50),
  libl_t_etat dlib100,
  visible boolean,
  position integer,
  action text,
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_t_etat_pkey PRIMARY KEY (id_t_etat),
  CONSTRAINT ta_t_etat_code_unique UNIQUE (code_t_etat)
);




CREATE TRIGGER tbi_ta_t_etat  BEFORE INSERT  ON ta_t_etat
  FOR EACH ROW  EXECUTE PROCEDURE before_insert();



CREATE TRIGGER tbu_ta_t_etat  BEFORE UPDATE  ON ta_t_etat
  FOR EACH ROW  EXECUTE PROCEDURE before_update();
  
INSERT INTO ta_t_etat (id_t_etat,code_t_etat,libl_t_etat,visible,"position","action") VALUES 
(1,'BROUILLON','brouillon',true,1,NULL)
,(2,'COMMERCIAL','En cours commercial',true,2,NULL)
,(3,'PREPARATION','En préparation',true,3,NULL)
,(4,'ENCOURS','Peut commencer à être transformer',true,4,NULL)
,(5,'TERMINE','à fini son processus',true,5,NULL);


  

ALTER TABLE ta_etat ADD column id_t_etat did_facultatif; 
ALTER TABLE ta_etat  ADD FOREIGN KEY (id_t_etat) REFERENCES ta_t_etat (id_t_etat) ON UPDATE NO ACTION ON DELETE NO ACTION;   

ALTER TABLE ta_etat ADD COLUMN listable boolean;
ALTER TABLE ta_etat ADD COLUMN accepte boolean;
ALTER TABLE ta_etat ADD COLUMN "position" integer;
ALTER TABLE ta_etat ADD COLUMN conditions text;
ALTER TABLE ta_etat ADD COLUMN commentaire text;


--INSERT INTO ta_etat
--( code_etat, libelle_etat,id_doc_org,id_doc_dest, qui_cree,  qui_modif,   version_obj, systeme, visible, identifiant, id_t_etat, defaut, total, position)
--VALUES( 'brouillon', 'etat initial',6,6, 'bdg',  'bdg',  0, true, true, '', 1, true, true,1);


--transformation des documents possibles
CREATE TABLE ta_transfo_t_doc
(
  id_transfo_t_doc serial NOT NULL,
  id_t_doc_org did_facultatif NOT NULL,
  id_t_doc_dest did_facultatif NOT NULL,
  possible boolean,
  accepte boolean,
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_transfo_t_doc_pkey PRIMARY KEY (id_transfo_t_doc),
  CONSTRAINT fk_ta_transfo_t_doc_1 FOREIGN KEY (id_t_doc_org)
      REFERENCES ta_t_doc (id_t_doc) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_transfo_t_doc_2 FOREIGN KEY (id_t_doc_dest)
      REFERENCES ta_t_doc (id_t_doc) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_transfo_t_doc
  OWNER TO bdg;


CREATE INDEX fk_ta_transfo_t_doc_1
  ON ta_transfo_t_doc
  USING btree
  (id_t_doc_org);


CREATE INDEX fk_ta_transfo_t_doc_2
  ON ta_transfo_t_doc
  USING btree
  (id_t_doc_dest);


CREATE TRIGGER tbi_ta_transfo_t_doc
  BEFORE INSERT
  ON ta_transfo_t_doc
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();


CREATE TRIGGER tbu_ta_transfo_t_doc
  BEFORE UPDATE
  ON ta_transfo_t_doc
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();


--table de relation entre ligne document

CREATE TABLE ta_r_etat_ligne_doc
(
  id_r_etat_ligne_doc serial NOT NULL,
  id_etat did_facultatif not null,
  id_l_facture did_facultatif,
  id_l_devis did_facultatif,
  id_l_boncde did_facultatif,
  id_l_bonliv did_facultatif,
  id_l_avoir did_facultatif,
  id_l_apporteur did_facultatif,
  id_l_proforma did_facultatif,
  id_l_acompte did_facultatif,
  id_r_reglement did_facultatif,
  id_l_prelevement did_facultatif,
  id_l_avis_echeance did_facultatif,
  id_l_ticket_caisse did_facultatif,
  id_l_boncde_achat did_facultatif,
  id_l_bon_reception did_facultatif,
  date_etat date_heure_lgr,
  commentaire text, 
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_r_etat_ligne_doc_pkey PRIMARY KEY (id_r_etat_ligne_doc),
  CONSTRAINT fk_ta_r_etat_ligne_doc_etat FOREIGN KEY (id_etat)
      REFERENCES ta_etat (id_etat) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
      
  CONSTRAINT fk_ta_r_etat_ligne_doc_avis FOREIGN KEY (id_l_avis_echeance)
      REFERENCES ta_l_avis_echeance (id_l_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_r_etat_ligne_doc_apporteur FOREIGN KEY (id_l_apporteur)
      REFERENCES ta_l_apporteur (id_l_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_r_etat_ligne_doc_proforma FOREIGN KEY (id_l_proforma)
      REFERENCES ta_l_proforma (id_l_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_r_etat_ligne_doc_prelevement FOREIGN KEY (id_l_prelevement)
      REFERENCES ta_l_prelevement (id_l_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_r_etat_ligne_doc_acompte FOREIGN KEY (id_l_acompte)
      REFERENCES ta_l_acompte (id_l_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_r_etat_ligne_doc_avoir FOREIGN KEY (id_l_avoir)
      REFERENCES ta_l_avoir (id_l_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_r_etat_ligne_doc_boncde FOREIGN KEY (id_l_boncde)
      REFERENCES ta_l_boncde (id_l_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_r_etat_ligne_doc_bonliv FOREIGN KEY (id_l_bonliv)
      REFERENCES ta_l_bonliv (id_l_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_r_etat_ligne_doc_devis FOREIGN KEY (id_l_devis)
      REFERENCES ta_l_devis (id_l_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_r_etat_ligne_doc_facture FOREIGN KEY (id_l_facture)
      REFERENCES ta_l_facture (id_l_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_r_etat_ligne_doc_reglement FOREIGN KEY (id_r_reglement)
      REFERENCES ta_r_reglement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_r_etat_ligne_doc_ticket_caisse FOREIGN KEY (id_l_ticket_caisse)
      REFERENCES ta_l_ticket_caisse (id_l_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_r_etat_ligne_doc_boncde_achat FOREIGN KEY (id_l_boncde_achat)
      REFERENCES ta_l_boncde_achat (id_l_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,  
  CONSTRAINT ta_r_etat_ligne_doc_bon_reception FOREIGN KEY (id_l_bon_reception)
      REFERENCES ta_l_bon_reception (id_l_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION 
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_r_etat_ligne_doc
  OWNER TO bdg;


CREATE INDEX fk_ta_r_etat_ligne_doc_avis
  ON ta_r_etat_ligne_doc
  USING btree
  (id_l_avis_echeance);


CREATE INDEX fk_ta_r_etat_ligne_doc_apporteur
  ON ta_r_etat_ligne_doc
  USING btree
  (id_l_apporteur);


CREATE INDEX fk_ta_r_etat_ligne_doc_proforma
  ON ta_r_etat_ligne_doc
  USING btree
  (id_l_proforma);


CREATE INDEX fk_ta_r_etat_ligne_doc_prelevement
  ON ta_r_etat_ligne_doc
  USING btree
  (id_l_prelevement);


CREATE INDEX fk_ta_r_etat_ligne_doc_acompte
  ON ta_r_etat_ligne_doc
  USING btree
  (id_l_acompte);


CREATE INDEX ta_r_etat_ligne_doc_id_facture
  ON ta_r_etat_ligne_doc
  USING btree
  (id_l_facture);


CREATE INDEX ta_r_etat_ligne_doc_id_devis
  ON ta_r_etat_ligne_doc
  USING btree
  (id_l_devis);


CREATE INDEX ta_r_etat_ligne_doc_id_boncde
  ON ta_r_etat_ligne_doc
  USING btree
  (id_l_boncde);


CREATE INDEX ta_r_etat_ligne_doc_id_bonliv
  ON ta_r_etat_ligne_doc
  USING btree
  (id_l_bonliv);


CREATE INDEX ta_r_etat_ligne_doc_id_avoir
  ON ta_r_etat_ligne_doc
  USING btree
  (id_l_avoir);


CREATE INDEX ta_r_etat_ligne_doc_id_reglement
  ON ta_r_etat_ligne_doc
  USING btree
  (id_r_reglement);


CREATE INDEX ta_r_etat_ligne_doc_id_ticket_caisse
  ON ta_r_etat_ligne_doc
  USING btree
  (id_l_ticket_caisse);


CREATE INDEX ta_r_etat_ligne_doc_id_boncde_achat
  ON ta_r_etat_ligne_doc
  USING btree
  (id_l_boncde_achat);


CREATE INDEX ta_r_etat_ligne_doc_id_bon_reception
  ON ta_r_etat_ligne_doc
  USING btree
  (id_l_bon_reception);
    

CREATE TRIGGER tbi_r_etat_ligne_doc
  BEFORE INSERT
  ON ta_r_etat_ligne_doc
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();


CREATE TRIGGER tbu_r_etat_ligne_doc
  BEFORE UPDATE
  ON ta_r_etat_ligne_doc
  FOR EACH ROW
  EXECUTE PROCEDURE before_update();



--table historique relation états ligne document
CREATE TABLE ta_hist_r_etat_ligne_doc
(
  id_hist_r_etat_ligne_doc serial NOT NULL,
    id_etat did_facultatif not null,
  id_l_facture did_facultatif,
  id_l_devis did_facultatif,
  id_l_boncde did_facultatif,
  id_l_bonliv did_facultatif,
  id_l_avoir did_facultatif,
  id_l_apporteur did_facultatif,
  id_l_proforma did_facultatif,
  id_l_acompte did_facultatif,
  id_r_reglement did_facultatif,
  id_l_prelevement did_facultatif,
  id_l_avis_echeance did_facultatif,
  id_l_ticket_caisse did_facultatif,
  id_l_boncde_achat did_facultatif,
  id_l_bon_reception did_facultatif,
  date_etat date_heure_lgr,
  commentaire text, 
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_hist_r_etat_ligne_doc_pkey PRIMARY KEY (id_hist_r_etat_ligne_doc),
  CONSTRAINT fk_ta_hist_r_etat_l_doc_etat FOREIGN KEY (id_etat)
      REFERENCES ta_etat (id_etat) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,  
  CONSTRAINT fk_ta_hist_r_etat_l_doc_avis FOREIGN KEY (id_l_avis_echeance)
      REFERENCES ta_l_avis_echeance (id_l_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_hist_r_etat_l_doc_apporteur FOREIGN KEY (id_l_apporteur)
      REFERENCES ta_l_apporteur (id_l_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_hist_r_etat_l_doc_proforma FOREIGN KEY (id_l_proforma)
      REFERENCES ta_l_proforma (id_l_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_hist_r_etat_ligne_doc_prelevement FOREIGN KEY (id_l_prelevement)
      REFERENCES ta_l_prelevement (id_l_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_hist_r_etat_l_doc_acompte FOREIGN KEY (id_l_acompte)
      REFERENCES ta_l_acompte (id_l_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_hist_r_etat_l_doc_id_avoir FOREIGN KEY (id_l_avoir)
      REFERENCES ta_l_avoir (id_l_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_hist_r_etat_l_doc_id_boncde FOREIGN KEY (id_l_boncde)
      REFERENCES ta_l_boncde (id_l_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_hist_r_etat_l_doc_id_bonliv FOREIGN KEY (id_l_bonliv)
      REFERENCES ta_l_bonliv (id_l_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_hist_r_etat_l_doc_id_devis FOREIGN KEY (id_l_devis)
      REFERENCES ta_l_devis (id_l_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_hist_r_etat_l_doc_id_facture FOREIGN KEY (id_l_facture)
      REFERENCES ta_l_facture (id_l_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_hist_r_etat_l_doc_id_reglement FOREIGN KEY (id_r_reglement)
      REFERENCES ta_r_reglement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_hist_r_etat_l_doc_id_ticket_caisse FOREIGN KEY (id_l_ticket_caisse)
      REFERENCES ta_l_ticket_caisse (id_l_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_hist_r_etat_l_doc_id_boncde_achat FOREIGN KEY (id_l_boncde_achat)
      REFERENCES ta_l_boncde_achat (id_l_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,  
  CONSTRAINT ta_hist_r_etat_l_doc_id_bon_reception FOREIGN KEY (id_l_bon_reception)
      REFERENCES ta_l_bon_reception (id_l_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION 
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_hist_r_etat_ligne_doc
  OWNER TO bdg;


CREATE INDEX fk_ta_hist_r_etat_l_doc_avis
  ON ta_hist_r_etat_ligne_doc
  USING btree
  (id_l_avis_echeance);


CREATE INDEX fk_ta_hist_r_etat_l_doc_apporteur
  ON ta_hist_r_etat_ligne_doc
  USING btree
  (id_l_apporteur);


CREATE INDEX fk_ta_hist_r_etat_l_doc_proforma
  ON ta_hist_r_etat_ligne_doc
  USING btree
  (id_l_proforma);


CREATE INDEX fk_ta_hist_r_etat_l_doc_prelevement
  ON ta_hist_r_etat_ligne_doc
  USING btree
  (id_l_prelevement);


CREATE INDEX fk_ta_hist_r_etat_l_doc_acompte
  ON ta_hist_r_etat_ligne_doc
  USING btree
  (id_l_acompte);


CREATE INDEX ta_hist_r_etat_l_doc_id_facture
  ON ta_hist_r_etat_ligne_doc
  USING btree
  (id_l_facture);


CREATE INDEX ta_hist_r_etat_l_doc_id_devis
  ON ta_hist_r_etat_ligne_doc
  USING btree
  (id_l_devis);


CREATE INDEX ta_hist_r_etat_l_doc_id_boncde
  ON ta_hist_r_etat_ligne_doc
  USING btree
  (id_l_boncde);


CREATE INDEX ta_hist_r_etat_l_doc_id_bonliv
  ON ta_hist_r_etat_ligne_doc
  USING btree
  (id_l_bonliv);


CREATE INDEX ta_hist_r_etat_l_doc_id_avoir
  ON ta_hist_r_etat_ligne_doc
  USING btree
  (id_l_avoir);


CREATE INDEX ta_hist_r_etat_l_doc_id_reglement
  ON ta_hist_r_etat_ligne_doc
  USING btree
  (id_r_reglement);


CREATE INDEX ta_hist_r_etat_l_doc_id_ticket_caisse
  ON ta_hist_r_etat_ligne_doc
  USING btree
  (id_l_ticket_caisse);


CREATE INDEX ta_hist_r_etat_l_doc_id_boncde_achat
  ON ta_hist_r_etat_ligne_doc
  USING btree
  (id_l_boncde_achat);


CREATE INDEX ta_hist_r_etat_l_doc_id_bon_reception
  ON ta_hist_r_etat_ligne_doc
  USING btree
  (id_l_bon_reception);
    

CREATE TRIGGER tbi_hist_r_etat_ligne_doc
  BEFORE INSERT
  ON ta_hist_r_etat_ligne_doc
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();


CREATE TRIGGER tbu_hist_r_etat_ligne_doc
  BEFORE UPDATE
  ON ta_hist_r_etat_ligne_doc
  FOR EACH ROW
  EXECUTE PROCEDURE before_update();



