--set schema 'demo';


--Requêtes : Pour réaliser ces changements, il faut exécuter les requêtes suivantes :
--Ajout des nouvelles colonnes sur ta_abonnement  + contraintes :
ALTER TABLE ta_abonnement 
ADD COLUMN id_externe  dlib255,
ADD COLUMN id_stripe_customer did_facultatif,
ADD COLUMN date_debut  date_heure_lgr,
ADD COLUMN date_fin  date_heure_lgr,
ADD COLUMN prorata  boolean,
ADD COLUMN id_source  did_facultatif,
ADD COLUMN id_coupon  did_facultatif,
ADD COLUMN biling  dlib255,
ADD COLUMN days_until_due  integer,
ADD COLUMN date_annulation  date_heure_lgr,
ADD COLUMN commentaire_annulation  text;

ALTER TABLE ta_abonnement 
ADD CONSTRAINT fk_ta_abonnement_id_stripe_customer FOREIGN KEY (id_stripe_customer)
      REFERENCES ta_stripe_customer (id_stripe_customer) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE ta_abonnement 
ADD CONSTRAINT fk_ta_abonnement_id_coupon FOREIGN KEY (id_coupon)
      REFERENCES ta_stripe_coupon (id_stripe_coupon) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE ta_abonnement 
ADD CONSTRAINT fk_ta_abonnement_id_source FOREIGN KEY (id_source)
      REFERENCES ta_stripe_source (id_stripe_source) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

--Ajout des nouvelles colonnes sur ta_l_abonnement  + contraintes :
ALTER TABLE ta_l_abonnement 
ADD COLUMN id_externe  dlib255,
ADD COLUMN id_stripe_plan  did_facultatif,
ADD COLUMN complement_1  dlib255,
ADD COLUMN complement_2  dlib255,
ADD COLUMN complement_3  dlib255;

ALTER TABLE ta_l_abonnement 
ADD CONSTRAINT fk_ta_l_abonnement_id_stripe_plan FOREIGN KEY (id_stripe_plan)
      REFERENCES ta_stripe_plan (id_stripe_plan) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;


--Report des données de ta_stripe_subscription sur ta_abonnement :
UPDATE ta_abonnement 
SET id_externe = sub.id_externe,
id_stripe_customer = sub.id_stripe_customer,
date_debut  = sub.date_debut,
date_fin  = sub.date_fin,
prorata  = sub.prorata,
id_source  = sub.id_source,
id_coupon  = sub.id_coupon,
biling = sub.billing,
days_until_due = sub.days_until_due,
date_annulation = sub.date_annulation,
commentaire_annulation= sub.commentaire_annulation
FROM ta_stripe_subscription sub
WHERE sub.id_abonnement = ta_abonnement.id_document;


--Report des données de ta_stripe_subscription_item sur ta_l_abonnement :
UPDATE ta_l_abonnement 
SET id_externe = item.id_externe,
id_stripe_plan = item.id_stripe_plan,
complement_1  = item.complement_1,
complement_2  = item.complement_2,
complement_3  = item.complement_3
FROM ta_stripe_subscription_item item
WHERE item.id_l_abonnement = ta_l_abonnement.id_l_document;

--Modification table et données de ta_l_echeance :
ALTER TABLE ta_l_echeance
ADD COLUMN id_l_abonnement did_facultatif;

ALTER TABLE ta_l_echeance 
ADD CONSTRAINT fk_ta_l_echeance_id_l_abonnement FOREIGN KEY (id_l_abonnement)
      REFERENCES ta_l_abonnement (id_l_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE ta_l_echeance 
ADD CONSTRAINT fk_ta_l_echeance_id_abonnement FOREIGN KEY (id_abonnement)
      REFERENCES ta_abonnement (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE ta_l_echeance 
DROP CONSTRAINT fk_ta_l_echeance_1;


UPDATE ta_l_echeance 
SET id_abonnement = abo.id_document,
id_l_abonnement = ligne.id_l_document
FROM ta_stripe_subscription_item item 
JOIN ta_stripe_subscription sub ON sub.id_stripe_subscription = item.id_stripe_subscription_item
JOIN ta_abonnement abo ON abo.id_document = sub.id_abonnement
JOIN ta_l_abonnement ligne ON ligne.id_l_document = item.id_l_abonnement
WHERE item.id_stripe_subscription = ta_l_echeance.id_stripe_subscription_item;





ALTER TABLE ta_l_echeance 
DROP CONSTRAINT fk_ta_l_echeance_id_stripe_subscription;
ALTER TABLE ta_l_echeance 
DROP CONSTRAINT fk_ta_l_echeance_id_stripe_subscription_item;


--Modification table et données de ta_stripe_paiement_prevu :
ALTER TABLE ta_stripe_paiement_prevu
ADD COLUMN id_abonnement did_facultatif;

ALTER TABLE ta_stripe_paiement_prevu 
ADD CONSTRAINT fk_ta_stripe_paiement_prevu_id_abonnement FOREIGN KEY (id_abonnement)
      REFERENCES ta_abonnement (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

UPDATE ta_stripe_paiement_prevu 
SET id_abonnement = abo.id_document
FROM ta_stripe_subscription sub
JOIN ta_abonnement abo ON abo.id_document = sub.id_abonnement
WHERE sub.id_stripe_subscription = ta_stripe_paiement_prevu.id_stripe_subscription;


ALTER TABLE ta_stripe_paiement_prevu 
DROP CONSTRAINT fk_ta_stripe_paiement_prevu_id_stripe_subscription;

--UPDATE ta_stripe_paiement_prevu
--SET id_stripe_subscription = NULL
--FROM ta_stripe_paiement_prevu;
--ALTER TABLE ta_stripe_paiement_prevu
--DROP COLUMN id_stripe_subscription;

--Modification table et données de ta_stripe_invoice :
ALTER TABLE ta_stripe_invoice
ADD COLUMN id_abonnement did_facultatif;

ALTER TABLE ta_stripe_invoice 
ADD CONSTRAINT fk_ta_stripe_invoice_id_abonnement FOREIGN KEY (id_abonnement)
      REFERENCES ta_abonnement (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

UPDATE ta_stripe_invoice 
SET id_abonnement = abo.id_document
FROM ta_stripe_subscription sub
JOIN ta_abonnement abo ON abo.id_document = sub.id_abonnement
WHERE sub.id_stripe_subscription = ta_stripe_invoice.id_stripe_subscription;

ALTER TABLE ta_stripe_invoice 
DROP CONSTRAINT fk_ta_stripe_invoice_id_stripe_subscription;

--Modification table et données de ta_stripe_discount :
ALTER TABLE ta_stripe_discount
ADD COLUMN id_abonnement did_facultatif;

ALTER TABLE ta_stripe_discount 
ADD CONSTRAINT fk_ta_stripe_discount_id_abonnement FOREIGN KEY (id_abonnement)
      REFERENCES ta_abonnement (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

UPDATE ta_stripe_discount 
SET id_abonnement = abo.id_document
FROM ta_stripe_subscription sub
JOIN ta_abonnement abo ON abo.id_document = sub.id_abonnement
WHERE sub.id_stripe_subscription = ta_stripe_discount.id_stripe_subscription;

ALTER TABLE ta_stripe_discount 
DROP CONSTRAINT fk_ta_stripe_discount_id_stripe_subscription;





--Ajout des etats 
--Ajout de la colonne id_abonnement dans la table ta_r_etat :

ALTER TABLE ta_r_etat
ADD COLUMN id_abonnement did_facultatif;

ALTER TABLE ta_r_etat 
ADD CONSTRAINT fk_ta_r_etat_abonnement FOREIGN KEY (id_abonnement)
      REFERENCES ta_abonnement (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

--Ajout de la colonne id_l_abonnement dans la table ta_r_etat_ligne_doc :
ALTER TABLE ta_r_etat_ligne_doc
ADD COLUMN id_l_abonnement did_facultatif;

ALTER TABLE ta_r_etat_ligne_doc 
ADD CONSTRAINT fk_ta_r_etat_ligne_doc_abonnement FOREIGN KEY (id_l_abonnement)
      REFERENCES ta_l_abonnement (id_l_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;















