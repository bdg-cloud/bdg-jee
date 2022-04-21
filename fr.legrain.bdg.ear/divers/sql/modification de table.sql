ALTER TABLE TA_L_DEVIS ADD LIB_TVA_L_DOCUMENT DLIB255;

-- ------------------------------------------------------------------------------------

ALTER TABLE ta_fabrication
   ADD COLUMN id_ta_gr_mouv_stock integer;

ALTER TABLE ta_fabrication
  ADD CONSTRAINT fk_ta_gr_mouv_stock FOREIGN KEY (id_ta_gr_mouv_stock) REFERENCES ta_gr_mouv_stock (id_gr_mouv_stock)
   ON UPDATE NO ACTION ON DELETE NO ACTION;
CREATE INDEX fki_ta_gr_mouv_stock
  ON ta_fabrication(id_ta_gr_mouv_stock);
  
  ALTER TABLE ta_reception
   ADD COLUMN id_ta_gr_mouv_stock integer;

ALTER TABLE ta_reception
  ADD CONSTRAINT fk_ta_reception_ta_gr_mouv_stock FOREIGN KEY (id_ta_gr_mouv_stock) REFERENCES ta_gr_mouv_stock (id_gr_mouv_stock)
   ON UPDATE NO ACTION ON DELETE NO ACTION;
CREATE INDEX fki_ta_reception_ta_gr_mouv_stock
  ON ta_reception(id_ta_gr_mouv_stock);
  
-- ------------------------------------------------------------------------------------