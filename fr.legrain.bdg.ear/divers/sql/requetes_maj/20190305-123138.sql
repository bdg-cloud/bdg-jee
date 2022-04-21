

CREATE INDEX ta_carte_bancaire_id
  ON ta_carte_bancaire
  USING btree
  (id_carte_bancaire);

CREATE TRIGGER tbi_carte_bancaire
  BEFORE INSERT
  ON ta_carte_bancaire
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

CREATE TRIGGER tbu_carte_bancaire
  BEFORE UPDATE
  ON ta_carte_bancaire
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

------------------------------------------------------------------

CREATE INDEX ta_stripe_product_id
  ON ta_stripe_product
  USING btree
  (id_stripe_product);

CREATE INDEX ta_stripe_product_id_externe
  ON ta_stripe_product
  USING btree
  (id_externe);

CREATE TRIGGER tbi_stripe_product
  BEFORE INSERT
  ON ta_stripe_product
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

CREATE TRIGGER tbu_stripe_product
  BEFORE UPDATE
  ON ta_stripe_product
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();
------------------------------------------------------------------

  CREATE INDEX ta_stripe_plan_id
  ON ta_stripe_plan
  USING btree
  (id_stripe_plan);

CREATE INDEX ta_stripe_plan_id_externe
  ON ta_stripe_plan
  USING btree
  (id_externe);

CREATE TRIGGER tbi_stripe_plan
  BEFORE INSERT
  ON ta_stripe_plan
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

CREATE TRIGGER tbu_stripe_plan
  BEFORE UPDATE
  ON ta_stripe_plan
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();
------------------------------------------------------------------

CREATE INDEX ta_stripe_coupon_id
  ON ta_stripe_coupon
  USING btree
  (id_stripe_coupon);

CREATE INDEX ta_stripe_coupon_id_externe
  ON ta_stripe_coupon
  USING btree
  (id_externe);

CREATE TRIGGER tbi_stripe_coupon
  BEFORE INSERT
  ON ta_stripe_coupon
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

CREATE TRIGGER tbu_stripe_coupon
  BEFORE UPDATE
  ON ta_stripe_coupon
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();
------------------------------------------------------------------

CREATE INDEX ta_stripe_customer_id
  ON ta_stripe_customer
  USING btree
  (id_stripe_customer);

CREATE INDEX ta_stripe_customer_id_externe
  ON ta_stripe_customer
  USING btree
  (id_externe);

CREATE TRIGGER tbi_stripe_customer
  BEFORE INSERT
  ON ta_stripe_customer
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

CREATE TRIGGER tbu_stripe_customer
  BEFORE UPDATE
  ON ta_stripe_customer
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();
------------------------------------------------------------------

CREATE INDEX ta_stripe_subscription_id
  ON ta_stripe_subscription
  USING btree
  (id_stripe_subscription);

CREATE INDEX ta_stripe_subscription_id_externe
  ON ta_stripe_subscription
  USING btree
  (id_externe);

CREATE TRIGGER tbi_stripe_subscription
  BEFORE INSERT
  ON ta_stripe_subscription
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

CREATE TRIGGER tbu_stripe_subscription
  BEFORE UPDATE
  ON ta_stripe_subscription
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();
------------------------------------------------------------------

CREATE INDEX ta_stripe_subscription_item_id
  ON ta_stripe_subscription_item
  USING btree
  (id_stripe_subscription_item);

CREATE INDEX ta_stripe_subscription_item_id_externe
  ON ta_stripe_subscription_item
  USING btree
  (id_externe);

CREATE TRIGGER tbi_stripe_subscription_item
  BEFORE INSERT
  ON ta_stripe_subscription_item
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

CREATE TRIGGER tbu_stripe_subscription_item
  BEFORE UPDATE
  ON ta_stripe_subscription_item
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();
------------------------------------------------------------------

CREATE INDEX stripe_discount_id
  ON ta_stripe_discount
  USING btree
  (id_stripe_discount);

CREATE INDEX stripe_discount_id_externe
  ON ta_stripe_discount
  USING btree
  (id_externe);

CREATE TRIGGER tbi_stripe_discount
  BEFORE INSERT
  ON ta_stripe_discount
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

CREATE TRIGGER tbu_stripe_discount
  BEFORE UPDATE
  ON ta_stripe_discount
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();
------------------------------------------------------------------

CREATE INDEX ta_stripe_source_id
  ON ta_stripe_source
  USING btree
  (id_stripe_source);

CREATE INDEX ta_stripe_source_id_externe
  ON ta_stripe_source
  USING btree
  (id_externe);

CREATE TRIGGER tbi_stripe_source
  BEFORE INSERT
  ON ta_stripe_source
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

CREATE TRIGGER tbu_stripe_source
  BEFORE UPDATE
  ON ta_stripe_source
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();
------------------------------------------------------------------

CREATE INDEX ta_stripe_invoice_id
  ON ta_stripe_invoice
  USING btree
  (id_stripe_invoice);

CREATE INDEX ta_stripe_invoice_id_externe
  ON ta_stripe_invoice
  USING btree
  (id_externe);

CREATE TRIGGER tbi_stripe_invoice
  BEFORE INSERT
  ON ta_stripe_invoice
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

CREATE TRIGGER tbu_stripe_invoice
  BEFORE UPDATE
  ON ta_stripe_invoice
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();
------------------------------------------------------------------

CREATE INDEX ta_stripe_invoice_item_id
  ON ta_stripe_invoice_item
  USING btree
  (id_stripe_invoice_item);

CREATE INDEX ta_stripe_invoice_item_id_externe
  ON ta_stripe_invoice_item
  USING btree
  (id_externe);

CREATE TRIGGER tbi_stripe_invoice_item
  BEFORE INSERT
  ON ta_stripe_invoice_item
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

CREATE TRIGGER tbu_stripe_invoice_item
  BEFORE UPDATE
  ON ta_stripe_invoice_item
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();
------------------------------------------------------------------

CREATE INDEX ta_stripe_bank_account_id
  ON ta_stripe_bank_account
  USING btree
  (id_stripe_bank_account);

CREATE INDEX ta_stripe_bank_account_id_externe
  ON ta_stripe_bank_account
  USING btree
  (id_externe);

CREATE TRIGGER tbi_stripe_bank_account
  BEFORE INSERT
  ON ta_stripe_bank_account
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

CREATE TRIGGER tbu_stripe_bank_account
  BEFORE UPDATE
  ON ta_stripe_bank_account
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();
------------------------------------------------------------------

CREATE INDEX ta_stripe_charge_id
  ON ta_stripe_charge
  USING btree
  (id_stripe_charge);

CREATE INDEX ta_stripe_charge_id_externe
  ON ta_stripe_charge
  USING btree
  (id_externe);

CREATE TRIGGER tbi_stripe_charge
  BEFORE INSERT
  ON ta_stripe_charge
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

CREATE TRIGGER tbu_stripe_charge
  BEFORE UPDATE
  ON ta_stripe_charge
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();
------------------------------------------------------------------

CREATE INDEX ta_stripe_card_id
  ON ta_stripe_card
  USING btree
  (id_stripe_card);

CREATE INDEX ta_stripe_card_id_externe
  ON ta_stripe_card
  USING btree
  (id_externe);

CREATE TRIGGER tbi_stripe_card
  BEFORE INSERT
  ON ta_stripe_card
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

CREATE TRIGGER tbu_stripe_card
  BEFORE UPDATE
  ON ta_stripe_card
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();
------------------------------------------------------------------

CREATE INDEX ta_stripe_refund_id
  ON ta_stripe_refund
  USING btree
  (id_stripe_refund);

CREATE INDEX ta_stripe_refund_id_externe
  ON ta_stripe_refund
  USING btree
  (id_externe);

CREATE TRIGGER tbi_stripe_refund
  BEFORE INSERT
  ON ta_stripe_refund
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

CREATE TRIGGER tbu_stripe_refund
  BEFORE UPDATE
  ON ta_stripe_refund
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();
------------------------------------------------------------------
--------------------------------------------------------------------------------------------------
--------------------------------------------------------------------------------------------------
--------------------------------------------------------------------------------------------------
--------------------------------------------------------------------------------------------------

ALTER TABLE ta_stripe_source
  ADD CONSTRAINT fk_ta_stripe_source_id_carte_bancaire FOREIGN KEY (id_carte_bancaire)
      REFERENCES ta_carte_bancaire (id_carte_bancaire) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

      ALTER TABLE ta_stripe_source
  ADD CONSTRAINT fk_ta_stripe_source_id_compte_banque FOREIGN KEY (id_compte_banque)
      REFERENCES ta_compte_banque (id_compte_banque) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE ta_stripe_subscription
  ADD CONSTRAINT fk_ta_stripe_subscription_id_source FOREIGN KEY (id_source)
      REFERENCES ta_stripe_source (id_stripe_source) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE ta_stripe_subscription
  ADD CONSTRAINT fk_ta_stripe_subscription_id_coupon FOREIGN KEY (id_coupon)
      REFERENCES ta_stripe_coupon (id_stripe_coupon) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE ta_stripe_invoice
  ADD CONSTRAINT fk_ta_stripe_invoice_id_stripe_charge FOREIGN KEY (id_stripe_charge)
      REFERENCES ta_stripe_charge (id_stripe_charge) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE ta_stripe_invoice
  ADD CONSTRAINT fk_ta_stripe_invoice_id_stripe_coupon FOREIGN KEY (id_stripe_coupon)
      REFERENCES ta_stripe_coupon (id_stripe_coupon) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE ta_stripe_invoice
  ADD CONSTRAINT fk_ta_stripe_invoice_id_avis_echeance FOREIGN KEY (id_avis_echeance)
      REFERENCES ta_avis_echeance (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE ta_stripe_invoice
  ADD CONSTRAINT fk_ta_stripe_invoice_id_reglement FOREIGN KEY (id_reglement)
      REFERENCES ta_reglement (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE ta_stripe_charge
  ADD CONSTRAINT fk_ta_stripe_charge_id_stripe_source FOREIGN KEY (id_stripe_source)
      REFERENCES ta_stripe_source (id_stripe_source) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE ta_stripe_charge
  ADD CONSTRAINT fk_ta_stripe_charge_id_stripe_bank_account FOREIGN KEY (id_stripe_bank_account)
      REFERENCES ta_stripe_bank_account (id_stripe_bank_account) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE ta_stripe_charge
  ADD CONSTRAINT fk_ta_stripe_charge_id_stripe_card FOREIGN KEY (id_stripe_card)
      REFERENCES ta_stripe_card (id_stripe_card) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE ta_stripe_charge
  ADD CONSTRAINT fk_ta_stripe_charge_id_stripe_customer FOREIGN KEY (id_stripe_customer)
      REFERENCES ta_stripe_customer (id_stripe_customer) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE ta_stripe_charge
  ADD CONSTRAINT fk_ta_stripe_charge_id_stripe_invoice FOREIGN KEY (id_stripe_invoice)
      REFERENCES ta_stripe_invoice (id_stripe_invoice) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE ta_stripe_invoice_item
  ADD CONSTRAINT fk_ta_stripe_invoice_item_id_stripe_plan FOREIGN KEY (id_stripe_plan)
      REFERENCES ta_stripe_plan (id_stripe_plan) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
      
