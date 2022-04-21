--set schema 'demo';

ALTER TABLE ta_article   ADD COLUMN id_unite_1 did_facultatif;

   
update ta_article aa set id_unite_1 =(select u.id_unite from ta_unite u 
 join ta_prix p on p.id_unite=u.id_unite
 join ta_article a on p.id_prix=a.id_prix where a.id_article=aa.id_article);
 
  
  

ALTER TABLE ta_r_prix  ADD COLUMN coef did9facult;
COMMENT ON COLUMN ta_r_prix.coef IS ' coefficient par rapport à la grille de reference';



CREATE TABLE ta_r_prix_tiers
(
  id serial NOT NULL,
  id_prix did_facultatif NOT NULL,
  id_tiers did_facultatif,
  id_t_tarif did_facultatif,
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  coef did9facult, -- coefficient par rapport à la grille de reference
  CONSTRAINT ta_r_prix_tiers_pkey PRIMARY KEY (id),
  CONSTRAINT fk_ta_r_prix_tiers_1 FOREIGN KEY (id_prix)
      REFERENCES ta_prix (id_prix) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ta_r_prix_tiers_2 FOREIGN KEY (id_tiers)
      REFERENCES ta_tiers (id_tiers) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_r_prix_tiers
  OWNER TO bdg;
COMMENT ON COLUMN ta_r_prix_tiers.coef IS ' coefficient par rapport à la grille de reference';


CREATE INDEX fk_ta_r_prix_tiers_1
  ON ta_r_prix_tiers
  USING btree
  (id_prix);


CREATE INDEX fk_ta_r_prix_tiers_2
  ON ta_r_prix_tiers
  USING btree
  (id_tiers, id_t_tarif);


CREATE INDEX fk_ta_r_prix_tiers_3
  ON ta_r_prix_tiers
  USING btree
  (id_tiers);


CREATE TRIGGER tbi_r_prix_tiers
  BEFORE INSERT
  ON ta_r_prix_tiers
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();



CREATE TRIGGER tbu_r_prix_tiers
  BEFORE UPDATE
  ON ta_r_prix_tiers
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

  
DROP INDEX fk_ta_r_prix_2;
ALTER TABLE ta_r_prix DROP column id_tiers;
