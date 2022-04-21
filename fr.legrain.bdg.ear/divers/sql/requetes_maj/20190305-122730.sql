--set schema 'demo';

-------------------------------------------------------
-- DEBUT INTEGRATION MODELE STRIPE POUR ABONNEMENT
-------------------------------------------------------

CREATE TABLE ta_carte_bancaire (
  id_carte_bancaire serial NOT NULL,
  
  id_t_banque did_facultatif,
  id_tiers did_facultatif,
  
  nom_banque dlib255,
  nom_proprietaire dlib255,
  numero_carte dlib255,
  empreinte dlib255,
  mois_expiration integer,
  annee_expiration integer,
  type_carte dlib255,
  pays_origine dlib255, 
  last4 dlib255,
  cvc dlib255,
  adresse1 dlib255,
  adresse2 dlib255,
  code_postal dlib255,
  ville dlib255,
  pays dlib255,
  cptcomptable dlib255,
  nom_compte dlib255,
  
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_carte_bancaire_pkey PRIMARY KEY (id_carte_bancaire),
  CONSTRAINT fk_ta_carte_bancaire_id_t_banque FOREIGN KEY (id_t_banque)
      REFERENCES ta_t_banque (id_t_banque) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_carte_bancaire_id_tiers FOREIGN KEY (id_tiers)
      REFERENCES ta_tiers (id_tiers) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE ta_stripe_product (
  id_stripe_product serial NOT NULL,
  id_externe dlib255,
  product_type dlib100,
  active boolean,
  caption dlib255,
  description dlib255,
  id_article did_facultatif,
  
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_stripe_product_pkey PRIMARY KEY (id_stripe_product),
  CONSTRAINT fk_ta_stripe_product_id_article FOREIGN KEY (id_article)
      REFERENCES ta_article (id_article) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE ta_stripe_plan (
  id_stripe_plan serial NOT NULL,
  id_externe dlib255,
  nickname dlib255,
  amount did9facult,
  interval dlib255,
  interval_count integer,
  currency dlib100,
  active boolean,
  trial_prediod_days integer,
  id_article did_facultatif,
  id_stripe_product did_facultatif,
  
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_stripe_plan_pkey PRIMARY KEY (id_stripe_plan),
  CONSTRAINT fk_ta_stripe_plan_id_article FOREIGN KEY (id_article)
      REFERENCES ta_article (id_article) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_stripe_plan_id_stripe_product FOREIGN KEY (id_stripe_product)
      REFERENCES ta_stripe_product (id_stripe_product) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE ta_stripe_coupon (
	  id_stripe_coupon serial NOT NULL,
	  id_externe dlib255,
	  name dlib255,
	  currency dlib100,
	  duration dlib255,
	  duration_in_months integer,
	  redeem_by date_heure_lgr,
	  amount_off did9facult,
	  percent_off did9facult,
	  valid boolean,
	  
	  qui_cree dlib50,
	  quand_cree date_heure_lgr,
	  qui_modif dlib50,
	  quand_modif date_heure_lgr,
	  version num_version,
	  ip_acces dlib50,
	  version_obj did_version_obj,
	  CONSTRAINT ta_stripe_coupon_pkey PRIMARY KEY (id_stripe_coupon)
);



CREATE TABLE ta_stripe_customer (
  id_stripe_customer serial NOT NULL,
  id_externe dlib255,
  description dlib255,
  email dlib255,
  
  id_tiers did_facultatif,
  id_stripe_discount did_facultatif,
  id_stripe_source did_facultatif,
  
  currency dlib50,
  
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_stripe_customer_pkey PRIMARY KEY (id_stripe_customer),
  CONSTRAINT fk_ta_stripe_customer_id_tiers FOREIGN KEY (id_tiers)
      REFERENCES ta_tiers (id_tiers) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
  --CONSTRAINT fk_ta_stripe_customer_id_stripe_discount FOREIGN KEY (id_stripe_discount)
  --    REFERENCES ta_stripe_discount (id_stripe_discount) MATCH SIMPLE
  --    ON UPDATE NO ACTION ON DELETE NO ACTION,
  --CONSTRAINT fk_ta_stripe_customer_id_stripe_source FOREIGN KEY (id_stripe_source)
  --    REFERENCES ta_stripe_source (id_stripe_source) MATCH SIMPLE
  --    ON UPDATE NO ACTION ON DELETE NO ACTION
);



CREATE TABLE ta_stripe_subscription (
  id_stripe_subscription serial NOT NULL,
  id_externe dlib255,
  status dlib100,
  quantity integer,
  
  id_stripe_customer did_facultatif,
  
  date_debut date_heure_lgr,
  date_fin date_heure_lgr,
  billing_cycle_anchor date_heure_lgr,
  prorata boolean default 'true',
  id_source did_facultatif,

  taxe did9facult,
  trial integer,
  id_coupon did_facultatif,
  nb_echeance integer,
  billing dlib255,
  days_until_due integer,
  timer_handle bytea,
  
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_stripe_subscription_pkey PRIMARY KEY (id_stripe_subscription),
  CONSTRAINT fk_ta_stripe_discount_id_stripe_customer FOREIGN KEY (id_stripe_customer)
      REFERENCES ta_stripe_customer (id_stripe_customer) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

	
CREATE TABLE ta_stripe_subscription_item (
  id_stripe_subscription_item serial NOT NULL,
  id_externe dlib255,
  quantity integer,
  id_stripe_plan did_facultatif,
  id_stripe_subscription did_facultatif,
  
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_stripe_subscription_item_pkey PRIMARY KEY (id_stripe_subscription_item),
  CONSTRAINT fk_ta_stripe_subscription_item_id_stripe_subscription FOREIGN KEY (id_stripe_subscription)
      REFERENCES ta_stripe_subscription (id_stripe_subscription) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE ta_stripe_discount (
  id_stripe_discount serial NOT NULL,
  id_externe dlib255,
  discount_end date_heure_lgr,
  discount_start date_heure_lgr,
  
  id_stripe_customer did_facultatif,
  id_stripe_coupon did_facultatif,
  id_stripe_subscription did_facultatif,
  
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_stripe_discount_pkey PRIMARY KEY (id_stripe_discount),
  CONSTRAINT fk_ta_stripe_discount_id_stripe_customer FOREIGN KEY (id_stripe_customer)
      REFERENCES ta_stripe_customer (id_stripe_customer) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
   CONSTRAINT fk_ta_stripe_discount_id_stripe_coupon FOREIGN KEY (id_stripe_coupon)
      REFERENCES ta_stripe_coupon (id_stripe_coupon) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
      CONSTRAINT fk_ta_stripe_discount_id_stripe_subscription FOREIGN KEY (id_stripe_subscription)
      REFERENCES ta_stripe_subscription (id_stripe_subscription) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE ta_stripe_source (
  id_stripe_source serial NOT NULL,
  id_externe dlib255,
  
  id_stripe_customer did_facultatif,
  id_carte_bancaire did_facultatif,
  id_compte_banque did_facultatif,
 
  
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_stripe_source_pkey PRIMARY KEY (id_stripe_source),
  CONSTRAINT fk_ta_stripe_source_id_stripe_customer FOREIGN KEY (id_stripe_customer)
      REFERENCES ta_stripe_customer (id_stripe_customer) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE ta_stripe_invoice (
  id_stripe_invoice serial NOT NULL,
  id_externe dlib255,
  
  id_stripe_subscription did_facultatif,
  id_stripe_customer did_facultatif,
  
  amount_due did9facult,
  number dlib255,
  due_date date_heure_lgr,
  created date_heure_lgr,
  id_stripe_charge did_facultatif,
  paid boolean default 'false',
  period_start date_heure_lgr,
  period_end date_heure_lgr,
  id_stripe_coupon did_facultatif,
  taxe did9facult,
  id_avis_echeance did_facultatif,
  id_reglement did_facultatif,
  status dlib255,

  webhooks_delivered_at date_heure_lgr,
  nex_payment_attempt date_heure_lgr,
  description dlib255,
  billing_reason dlib255,
  billing dlib255,
  attempted boolean default 'false',
  attempt_count integer,
  
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_stripe_invoice_pkey PRIMARY KEY (id_stripe_invoice),
  CONSTRAINT fk_ta_stripe_invoice_id_stripe_customer FOREIGN KEY (id_stripe_customer)
      REFERENCES ta_stripe_customer (id_stripe_customer) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_stripe_invoice_id_stripe_subscription FOREIGN KEY (id_stripe_subscription)
      REFERENCES ta_stripe_subscription (id_stripe_subscription) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE ta_stripe_invoice_item (
  id_stripe_invoice_item serial NOT NULL,
  id_externe dlib255,
 
  id_stripe_invoice did_facultatif,
  
   id_stripe_plan did_facultatif,
   quantity integer,
  
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_stripe_invoice_item_pkey PRIMARY KEY (id_stripe_invoice_item),
  CONSTRAINT fk_ta_stripe_invoice_item_id_stripe_invoice FOREIGN KEY (id_stripe_invoice)
      REFERENCES ta_stripe_invoice (id_stripe_invoice) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE ta_stripe_bank_account (
  id_stripe_bank_account serial NOT NULL,
  id_externe dlib255,
 
  id_compte_banque did_facultatif,
  
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_stripe_bank_account_pkey PRIMARY KEY (id_stripe_bank_account),
  CONSTRAINT fk_ta_stripe_bank_account_id_compte_banque FOREIGN KEY (id_compte_banque)
      REFERENCES ta_compte_banque (id_compte_banque) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE ta_stripe_charge (
  id_stripe_charge serial NOT NULL,
  id_externe dlib255,
  
  id_reglement did_facultatif,
  id_stripe_source did_facultatif,
  id_stripe_card did_facultatif,
  id_stripe_bank_account did_facultatif,
  id_stripe_customer did_facultatif,
  id_stripe_invoice did_facultatif,
  
  amount did9facult,
  amount_refunded did9facult,
  description dlib255,
  status dlib255,
  
  paid boolean default 'false',
  captured boolean default 'false',
  refunded boolean default 'false',
  
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_stripe_charge_pkey PRIMARY KEY (id_stripe_charge),
  CONSTRAINT fk_ta_stripe_charge_id_reglement FOREIGN KEY (id_reglement)
      REFERENCES ta_reglement (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE ta_stripe_card (
  id_stripe_card serial NOT NULL,
  id_externe dlib255,
  
  id_carte_bancaire did_facultatif,
  
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_stripe_card_pkey PRIMARY KEY (id_stripe_card),
  CONSTRAINT fk_ta_stripe_card_id_carte_bancaire FOREIGN KEY (id_carte_bancaire)
      REFERENCES ta_carte_bancaire (id_carte_bancaire) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);
--------------------------------
ALTER TABLE ta_compte_banque ADD COLUMN bank_code dlib255;
ALTER TABLE ta_compte_banque ADD COLUMN branch_code dlib255;
ALTER TABLE ta_compte_banque ADD COLUMN country dlib255;
ALTER TABLE ta_compte_banque ADD COLUMN last4 dlib255;

CREATE TABLE ta_stripe_refund (
  id_stripe_refund serial NOT NULL,
  id_externe dlib255,
  
  id_stripe_charge did_facultatif,
  amount did9facult,
  status dlib255,
  reason dlib255,
  failure_reason dlib255,
  
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_stripe_refund_pkey PRIMARY KEY (id_stripe_refund)
);
