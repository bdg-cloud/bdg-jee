  
CREATE TABLE IF NOT EXISTS ta_r_groupe_modele_conformite
(
  id serial NOT NULL,
  id_modele_groupe integer,
  id_modele_conformite integer,
  "position" integer,
  qui_cree character varying(50),
  quand_cree timestamp with time zone DEFAULT now(),
  qui_modif character varying(50),
  quand_modif timestamp with time zone DEFAULT now(),
  version character varying(20),
  ip_acces character varying(50),
  version_obj integer DEFAULT 0,
  CONSTRAINT ta_r_groupe_modele_conformite_pkey PRIMARY KEY (id),
  CONSTRAINT ta_r_groupe_modele_conformite_id_modele_groupe_fkey FOREIGN KEY (id_modele_groupe)
      REFERENCES ta_modele_groupe (id_modele_groupe) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_r_groupe_modele_conformite_id_modele_conformite_fkey FOREIGN KEY (id_modele_conformite)
      REFERENCES ta_modele_conformite (id_modele_conformite) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);

-- Trigger: tbi_ta_r_groupe_modele_conformite on demo.ta_r_groupe_modele_conformite

DROP TRIGGER IF EXISTS tbi_ta_r_groupe_modele_conformite ON ta_r_groupe_modele_conformite;

CREATE TRIGGER tbi_ta_r_groupe_modele_conformite
  BEFORE INSERT
  ON ta_r_groupe_modele_conformite
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

-- Trigger: tbu_ta_r_groupe_modele_conformite on demo.ta_r_groupe_modele_conformite

DROP TRIGGER IF EXISTS tbu_ta_r_groupe_modele_conformite ON ta_r_groupe_modele_conformite;

CREATE TRIGGER tbu_ta_r_groupe_modele_conformite
  BEFORE UPDATE
  ON ta_r_groupe_modele_conformite
  FOR EACH ROW
  EXECUTE PROCEDURE before_update();
  
  
  
  
  
--ALTER TABLE ta_conformite
--  ADD COLUMN id_modele_conformite_origine integer;
--ALTER TABLE ta_conformite
--  ADD COLUMN id_modele_groupe_origine integer;
--ALTER TABLE ta_conformite
--  ADD COLUMN "position" integer;
--ALTER TABLE ta_conformite
--  ADD CONSTRAINT fk_id_modele_conformite_origine FOREIGN KEY (id_modele_conformite_origine) REFERENCES ta_modele_conformite (id_modele_conformite) ON UPDATE NO ACTION ON DELETE NO ACTION;
--ALTER TABLE ta_conformite
--  ADD CONSTRAINT fk_id_modele_groupe_origine FOREIGN KEY (id_modele_groupe_origine) REFERENCES ta_modele_groupe (id_modele_groupe) ON UPDATE NO ACTION ON DELETE NO ACTION;
  
SELECT f_add_col('ta_conformite', 'id_modele_conformite_origine', 'integer','NULL','ALTER TABLE ta_conformite ADD CONSTRAINT fk_id_modele_conformite_origine FOREIGN KEY (id_modele_conformite_origine) REFERENCES ta_modele_conformite (id_modele_conformite) ON UPDATE NO ACTION ON DELETE NO ACTION;');
SELECT f_add_col('ta_conformite', 'id_modele_groupe_origine', 'integer','NULL','ALTER TABLE ta_conformite ADD CONSTRAINT fk_id_modele_groupe_origine FOREIGN KEY (id_modele_groupe_origine) REFERENCES ta_modele_groupe (id_modele_groupe) ON UPDATE NO ACTION ON DELETE NO ACTION;');
SELECT f_add_col('ta_conformite', 'position', 'integer','0', null);
  




--INSERT INTO ta_controle(champ, contexte, controle_defaut, controle_utilisateur) VALUES ( 'TaLFabrication.numLigneLDocument', 'L_FABRICATION', 100, 100);
--INSERT INTO ta_controle(champ, contexte, controle_defaut, controle_utilisateur) VALUES ( 'TaLFabrication.libLDocument', 'L_FABRICATION', 100, 100);
--INSERT INTO ta_controle(champ, contexte, controle_defaut, controle_utilisateur) VALUES ( 'TaLFabrication.u1LDocument', 'L_FABRICATION', 100, 100);
--INSERT INTO ta_controle(champ, contexte, controle_defaut, controle_utilisateur) VALUES ( 'TaLFabrication.u2LDocument', 'L_FABRICATION', 100, 100);
--INSERT INTO ta_controle(champ, contexte, controle_defaut, controle_utilisateur) VALUES ( 'TaLFabrication.qteLDocument', 'L_FABRICATION', 100, 100);
--INSERT INTO ta_controle(champ, contexte, controle_defaut, controle_utilisateur) VALUES ( 'TaLFabrication.qte2LDocument', 'L_FABRICATION', 100, 100);

SELECT f_insert('ta_controle', 'champ =''TaLFabrication.numLigneLDocument'' and contexte =''L_FABRICATION''',
'INSERT INTO ta_controle(champ, contexte, controle_defaut, controle_utilisateur) VALUES (''TaLFabrication.numLigneLDocument'', ''L_FABRICATION'', 100, 100)',null);

SELECT f_insert('ta_controle', 'champ =''TaLFabrication.libLDocument'' and contexte =''L_FABRICATION''',
'INSERT INTO ta_controle(champ, contexte, controle_defaut, controle_utilisateur) VALUES ( ''TaLFabrication.libLDocument'', ''L_FABRICATION'', 100, 100)',null);

SELECT f_insert('ta_controle', 'champ =''TaLFabrication.u1LDocument'' and contexte =''L_FABRICATION''',
'INSERT INTO ta_controle(champ, contexte, controle_defaut, controle_utilisateur) VALUES ( ''TaLFabrication.u1LDocument'', ''L_FABRICATION'', 100, 100)',null);

SELECT f_insert('ta_controle', 'champ =''TaLFabrication.u2LDocument'' and contexte =''L_FABRICATION''',
'INSERT INTO ta_controle(champ, contexte, controle_defaut, controle_utilisateur) VALUES ( ''TaLFabrication.u2LDocument'', ''L_FABRICATION'', 100, 100)',null);

SELECT f_insert('ta_controle', 'champ =''TaLFabrication.qteLDocument'' and contexte =''L_FABRICATION''',
'INSERT INTO ta_controle(champ, contexte, controle_defaut, controle_utilisateur) VALUES ( ''TaLFabrication.qteLDocument'', ''L_FABRICATION'', 100, 100)',null);

SELECT f_insert('ta_controle', 'champ =''TaLFabrication.qte2LDocument'' and contexte =''L_FABRICATION''',
'INSERT INTO ta_controle(champ, contexte, controle_defaut, controle_utilisateur) VALUES ( ''TaLFabrication.qte2LDocument'', ''L_FABRICATION'', 100, 100)',null);



CREATE TABLE IF NOT EXISTS  ta_gen_code_ex
(
  id_gen_code_ex serial NOT NULL,
  code_gen_code dlib255nn,
  cle_gen_code dlib255nn,
  valeur_gen_code dlib255,
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT pk_id_gen_code_ex PRIMARY KEY (id_gen_code_ex)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_gen_code_ex
  OWNER TO postgres;


DROP TRIGGER IF EXISTS tbi_ta_gen_code_ex ON ta_gen_code_ex;
CREATE TRIGGER tbi_ta_gen_code_ex
  BEFORE INSERT
  ON ta_gen_code_ex
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();


DROP TRIGGER  IF EXISTS tbu_ta_gen_code_ex ON ta_gen_code_ex;
CREATE TRIGGER tbu_ta_gen_code_ex
  BEFORE UPDATE
  ON ta_gen_code_ex
  FOR EACH ROW
  EXECUTE PROCEDURE before_update();

  
SELECT f_insert('ta_gen_code_ex', 'code_gen_code =''codeTiers'' and cle_gen_code =''TaTiers''',
'INSERT INTO ta_gen_code_ex( code_gen_code, cle_gen_code)VALUES (''codeTiers'',''TaTiers'')',null);  
SELECT f_insert('ta_gen_code_ex', 'code_gen_code =''codeDocument'' and cle_gen_code =''TaDevis''',
'INSERT INTO ta_gen_code_ex( code_gen_code, cle_gen_code)VALUES (''codeDocument'',''TaDevis'')',null); 
SELECT f_insert('ta_gen_code_ex', 'code_gen_code =''codeDocument'' and cle_gen_code =''TaFabrication''',
'INSERT INTO ta_gen_code_ex( code_gen_code, cle_gen_code)VALUES (''codeDocument'',''TaFabrication'')',null); 
SELECT f_insert('ta_gen_code_ex', 'code_gen_code =''codeDocument'' and cle_gen_code =''TaBonReception''',
'INSERT INTO ta_gen_code_ex( code_gen_code, cle_gen_code)VALUES (''codeDocument'',''TaBonReception'')',null); 
SELECT f_insert('ta_gen_code_ex', 'code_gen_code =''codeArticle'' and cle_gen_code =''TaArticle''',
'INSERT INTO ta_gen_code_ex( code_gen_code, cle_gen_code)VALUES (''codeArticle'',''TaArticle'')',null); 

-----------------------------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------------------------
SELECT f_insert('ta_gen_code_ex', 'code_gen_code =''numLot'' and cle_gen_code =''TaLot''',
'INSERT INTO ta_gen_code_ex( code_gen_code, cle_gen_code)VALUES (''numLot'',''TaLot'')',null); 
-----------------------------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------------------------
SELECT f_insert('ta_gen_code_ex', 'code_gen_code =''codeInventaire'' and cle_gen_code =''TaInventaire''',
'INSERT INTO ta_gen_code_ex( code_gen_code, cle_gen_code)VALUES (''codeInventaire'',''TaInventaire'')',null);  
-----------------------------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------------------------
SELECT f_insert('ta_gen_code_ex', 'code_gen_code =''codeGrMouvStock'' and cle_gen_code =''TaGrMouvStock''',
'INSERT INTO ta_gen_code_ex( code_gen_code, cle_gen_code)VALUES (''codeGrMouvStock'',''TaGrMouvStock'')',null);  


ALTER TABLE ta_lot
  ADD CONSTRAINT ta_lot_numlot_key UNIQUE(numlot);
  
ALTER TABLE ta_lot ADD COLUMN libelle dlib255;





-----------------------------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------------------------

CREATE TABLE ta_t_fabrication
(
  id_t_fabrication serial NOT NULL,
  code_t_fabrication dlgr_code,
  libl_t_fabrication dlib100,
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_t_fabrication_pkey PRIMARY KEY (id_t_fabrication)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_t_fabrication
  OWNER TO postgres;

CREATE INDEX ta_t_fabrication_code
  ON ta_t_fabrication
  USING btree
  (code_t_fabrication COLLATE pg_catalog."default");

CREATE INDEX unq1_ta_t_fabrication
  ON ta_t_fabrication
  USING btree
  (code_t_fabrication COLLATE pg_catalog."default");

CREATE TRIGGER tbi_ta_t_fabrication
  BEFORE INSERT
  ON ta_t_fabrication
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

CREATE TRIGGER tbu_ta_t_fabrication
  BEFORE UPDATE
  ON ta_t_fabrication
  FOR EACH ROW
  EXECUTE PROCEDURE before_update();
  
SELECT f_add_col('ta_fabrication', 'id_t_fabrication', 'integer','NULL','ALTER TABLE ta_fabrication ADD CONSTRAINT fk_id_t_fabrication FOREIGN KEY (id_t_fabrication) REFERENCES ta_t_fabrication (id_t_fabrication) ON UPDATE NO ACTION ON DELETE NO ACTION;');

SELECT f_insert('ta_controle', 'champ =''TaTFabrication.idTFabrication'' and contexte =''T_FABRICATION''',
'INSERT INTO ta_controle(champ, contexte, controle_defaut, controle_utilisateur) VALUES ( ''TaTFabrication.idTFabrication'', ''T_FABRICATION'', 100, 100)',null);

SELECT f_insert('ta_controle', 'champ =''TaTFabrication.codeTFabrication'' and contexte =''T_FABRICATION''',
'INSERT INTO ta_controle(champ, contexte, controle_defaut, controle_utilisateur) VALUES ( ''TaTFabrication.codeTFabrication'', ''T_FABRICATION'', 100, 100)',null);

SELECT f_insert('ta_controle', 'champ =''TaTFabrication.liblTFabrication'' and contexte =''T_FABRICATION''',
'INSERT INTO ta_controle(champ, contexte, controle_defaut, controle_utilisateur) VALUES ( ''TaTFabrication.liblTFabrication'', ''T_FABRICATION'', 100, 100)',null);

-----------------------------------------------------------------------------------------------------------------------------
-------------------------------------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------------------------------

CREATE TABLE ta_t_reception
(
  id_t_reception serial NOT NULL,
  code_t_reception dlgr_code,
  libl_t_reception dlib100,
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_t_reception_pkey PRIMARY KEY (id_t_reception)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_t_reception
  OWNER TO postgres;

CREATE INDEX ta_t_reception_code
  ON ta_t_reception
  USING btree
  (code_t_reception COLLATE pg_catalog."default");

CREATE INDEX unq1_ta_t_reception
  ON ta_t_reception
  USING btree
  (code_t_reception COLLATE pg_catalog."default");

CREATE TRIGGER tbi_ta_t_reception
  BEFORE INSERT
  ON ta_t_reception
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

CREATE TRIGGER tbu_ta_t_reception
  BEFORE UPDATE
  ON ta_t_reception
  FOR EACH ROW
  EXECUTE PROCEDURE before_update();

SELECT f_add_col('ta_bon_reception', 'id_t_reception', 'integer','NULL','ALTER TABLE ta_bon_reception ADD CONSTRAINT fk_id_t_reception FOREIGN KEY (id_t_reception) REFERENCES ta_t_reception (id_t_reception) ON UPDATE NO ACTION ON DELETE NO ACTION;');

SELECT f_insert('ta_controle', 'champ =''TaTReception.idTReception'' and contexte =''T_RECEPTION''',
'INSERT INTO ta_controle(champ, contexte, controle_defaut, controle_utilisateur) VALUES ( ''TaTReception.idTReception'', ''T_RECEPTION'', 100, 100)',null);

SELECT f_insert('ta_controle', 'champ =''TaTReception.liblTReception'' and contexte =''T_RECEPTION''',
'INSERT INTO ta_controle(champ, contexte, controle_defaut, controle_utilisateur) VALUES ( ''TaTReception.liblTReception'', ''T_RECEPTION'', 100, 100)',null);

SELECT f_insert('ta_controle', 'champ =''TaTReception.codeTReception'' and contexte =''T_RECEPTION''',
'INSERT INTO ta_controle(champ, contexte, controle_defaut, controle_utilisateur) VALUES ( ''TaTReception.codeTReception'', ''T_RECEPTION'', 100, 100)',null);



-----------------------------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------------------------
CREATE TABLE ta_verrou_code_genere
(
  id_verrou_code_genere serial NOT NULL,
  entite dlib100,
  champ dlib100,
  valeur dlib100,
  session_id dlib255,
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_verrou_code_genere_pkey PRIMARY KEY (id_verrou_code_genere)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_verrou_code_genere
  OWNER TO postgres;

CREATE TRIGGER tbi_ta_verrou_code_genere
  BEFORE INSERT
  ON ta_verrou_code_genere
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

CREATE TRIGGER tbu_ta_verrou_code_genere
  BEFORE UPDATE
  ON ta_verrou_code_genere
  FOR EACH ROW
  EXECUTE PROCEDURE before_update();
 
SELECT f_insert('ta_controle', 'champ =''TaVerrouCodeGenere.idVerrouCodeGenere'' and contexte =''VERROU_CODE_GENERE''',
'INSERT INTO ta_controle(champ, contexte, controle_defaut, controle_utilisateur) VALUES ( ''TaVerrouCodeGenere.idVerrouCodeGenere'', ''VERROU_CODE_GENERE'', 100, 100)',null);

SELECT f_insert('ta_controle', 'champ =''TaVerrouCodeGenere.entite'' and contexte =''VERROU_CODE_GENERE''',
'INSERT INTO ta_controle(champ, contexte, controle_defaut, controle_utilisateur) VALUES ( ''TaVerrouCodeGenere.entite'', ''VERROU_CODE_GENERE'', 100, 100)',null);

SELECT f_insert('ta_controle', 'champ =''TaVerrouCodeGenere.champ'' and contexte =''VERROU_CODE_GENERE''',
'INSERT INTO ta_controle(champ, contexte, controle_defaut, controle_utilisateur) VALUES ( ''TaVerrouCodeGenere.champ'', ''VERROU_CODE_GENERE'', 100, 100)',null);

SELECT f_insert('ta_controle', 'champ =''TaVerrouCodeGenere.valeur'' and contexte =''VERROU_CODE_GENERE''',
'INSERT INTO ta_controle(champ, contexte, controle_defaut, controle_utilisateur) VALUES ( ''TaVerrouCodeGenere.valeur'', ''VERROU_CODE_GENERE'', 100, 100)',null);

---------------------------------------------------------------------------------------------------------------------------------

CREATE TABLE ta_verrou_modification
(
  id_verrou_modification serial NOT NULL,
  entite dlib100,
  champ dlib100,
  valeur dlib100,
  session_id dlib255,
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_verrou_modification_pkey PRIMARY KEY (id_verrou_modification)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_verrou_modification
  OWNER TO postgres;

CREATE TRIGGER tbi_ta_verrou_modification
  BEFORE INSERT
  ON ta_verrou_modification
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

CREATE TRIGGER tbu_ta_verrou_modification
  BEFORE UPDATE
  ON ta_verrou_modification
  FOR EACH ROW
  EXECUTE PROCEDURE before_update();
 
SELECT f_insert('ta_controle', 'champ =''TaVerrouModification.idVerrouModification'' and contexte =''VERROU_MODIFICATION''',
'INSERT INTO ta_controle(champ, contexte, controle_defaut, controle_utilisateur) VALUES ( ''TaVerrouModification.idVerrouModification'', ''VERROU_MODIFICATION'', 100, 100)',null);

SELECT f_insert('ta_controle', 'champ =''TaVerrouModification.entite'' and contexte =''VERROU_MODIFICATION''',
'INSERT INTO ta_controle(champ, contexte, controle_defaut, controle_utilisateur) VALUES ( ''TaVerrouModification.entite'', ''VERROU_MODIFICATION'', 100, 100)',null);

SELECT f_insert('ta_controle', 'champ =''TaVerrouModification.champ'' and contexte =''VERROU_MODIFICATION''',
'INSERT INTO ta_controle(champ, contexte, controle_defaut, controle_utilisateur) VALUES ( ''TaVerrouModification.champ'', ''VERROU_MODIFICATION'', 100, 100)',null);

SELECT f_insert('ta_controle', 'champ =''TaVerrouModification.valeur'' and contexte =''VERROU_MODIFICATION''',
'INSERT INTO ta_controle(champ, contexte, controle_defaut, controle_utilisateur) VALUES ( ''TaVerrouModification.valeur'', ''VERROU_MODIFICATION'', 100, 100)',null);

-----------------------------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------------------------

