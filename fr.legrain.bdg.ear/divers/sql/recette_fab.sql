  
CREATE TABLE IF NOT EXISTS ta_recette
(
  id_recette serial NOT NULL,
  id_article did_facultatif,
  qte did9facult,
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  version_obj did_version_obj,
  ip_acces dlib50,
  CONSTRAINT ta_recette_pkey PRIMARY KEY (id_recette)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_recette
  OWNER TO postgres;
 
DROP INDEX IF EXISTS "idx_id_article";

CREATE INDEX "idx_id_article"
  ON ta_recette
  USING btree
  (id_article);


DROP TRIGGER IF EXISTS tbi_fabrication ON ta_recette;

CREATE TRIGGER tbi_recette
  BEFORE INSERT
  ON ta_recette
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

DROP TRIGGER IF EXISTS tbu_recette ON ta_recette;

CREATE TRIGGER tbu_recette
  BEFORE UPDATE
  ON ta_recette
  FOR EACH ROW
  EXECUTE PROCEDURE before_update();

  -------------------------------------------------------------------------------------------------------------------------------------------------------------
  -------------------------------------------------------------------------------------------------------------------------------------------------------------
  -------------------------------------------------------------------------------------------------------------------------------------------------------------
  -------------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS ta_l_recette
(
  id_l_recette serial NOT NULL,
  id_recette did_facultatif,
  id_article did_facultatif,
  id_lot did_facultatif,
  id_entrepot did_facultatif,
  num_ligne_l_document integer DEFAULT 0,
  lib_l_document dlib255,
  qte_l_document did9facult,
  qte2_l_document did9facult,
  u1_l_document character varying(20),
  u2_l_document character varying(20),
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  emplacement_l_document dlib255,
  CONSTRAINT ta_l_recette_pkey PRIMARY KEY (id_l_recette),
  CONSTRAINT fk_ta_l_recette_1 FOREIGN KEY (id_recette)
      REFERENCES ta_recette (id_recette) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_l_recette_2 FOREIGN KEY (id_article)
      REFERENCES ta_article (id_article) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_l_recette_3 FOREIGN KEY (id_lot)
      REFERENCES ta_lot (id_lot) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_l_recette_4 FOREIGN KEY (id_entrepot)
      REFERENCES ta_entrepot (id_entrepot) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_l_recette
  OWNER TO postgres;

DROP INDEX IF EXISTS fk_ta_l_recette;

CREATE INDEX fk_ta_l_recette
  ON ta_l_recette
  USING btree
  (id_recette);


DROP INDEX IF EXISTS fk_ta_l_recette;

CREATE INDEX fk_ta_l_recette
  ON ta_l_recette
  USING btree
  (id_article);

DROP TRIGGER IF EXISTS tbi_l_recette ON ta_l_recette;

CREATE TRIGGER tbi_l_recette
  BEFORE INSERT
  ON ta_l_recette
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();


DROP TRIGGER IF EXISTS tbu_l_recette_art ON ta_l_recette;

CREATE TRIGGER tbu_l_recette_art
  BEFORE UPDATE
  ON ta_l_recette
  FOR EACH ROW
  EXECUTE PROCEDURE before_update();


SELECT f_add_col('ta_article', 'id_recette', 'did_facultatif','NULL','ALTER TABLE ta_article ADD CONSTRAINT fk_id_recette FOREIGN KEY (id_recette) REFERENCES ta_recette (id_recette) ON UPDATE NO ACTION ON DELETE NO ACTION;');





