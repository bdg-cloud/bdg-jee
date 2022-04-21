--maj_gestionlot_et_stock
--set schema 'demo';

ALTER TABLE ta_r_reglement
  ADD FOREIGN KEY (id_mise_a_disposition) REFERENCES ta_mise_a_disposition (id_mise_a_disposition) ON UPDATE NO ACTION ON DELETE NO ACTION;

       ALTER TABLE ta_r_reglement_liaison
  ADD FOREIGN KEY (id_mise_a_disposition) REFERENCES ta_mise_a_disposition (id_mise_a_disposition) ON UPDATE NO ACTION ON DELETE NO ACTION; 
  