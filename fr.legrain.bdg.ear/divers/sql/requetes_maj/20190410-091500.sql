--set schema 'demo';

ALTER TABLE ta_l_bonliv
  ADD CONSTRAINT fk_ta_l_bonliv_id_lot FOREIGN KEY (id_lot)
      REFERENCES ta_lot (id_lot) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
    
ALTER TABLE ta_l_avoir
  ADD CONSTRAINT fk_ta_l_avoir_id_lot FOREIGN KEY (id_lot)
      REFERENCES ta_lot (id_lot) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
      
INSERT INTO ta_status_conformite ( code_status_conformite, libelle_status_conformite, version_obj, importance, libelle_status_conformite_lot) VALUES ('aucun_controle', 'pas de controle à faire', 0, 700, 'aucun controle définit sur ce lot');