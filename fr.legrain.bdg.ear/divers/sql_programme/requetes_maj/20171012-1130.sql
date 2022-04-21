CREATE DOMAIN public.date_heure_lgr
  AS timestamp with time zone
  DEFAULT now();

CREATE DOMAIN public.date_lgr
  AS date
  DEFAULT now();

CREATE DOMAIN public.dbool
  AS smallint
  DEFAULT 0
  NOT NULL
  CONSTRAINT dbool_check CHECK (VALUE >= 0 AND VALUE <= 1);

CREATE DOMAIN public.dcodpos
  AS character varying(25)
  COLLATE pg_catalog."default";

CREATE DOMAIN public.did3
  AS integer
  NOT NULL;

CREATE DOMAIN public.did4
  AS integer;

CREATE DOMAIN public.did9
  AS numeric(15,2)
  NOT NULL;

CREATE DOMAIN public.did9facult
  AS numeric(15,2);

CREATE DOMAIN public.did_facultatif
  AS integer;

CREATE DOMAIN public.did_version_obj
  AS integer
  DEFAULT 0
  NOT NULL;

CREATE DOMAIN public.dlgr_2
  AS character varying(2)
  COLLATE pg_catalog."default"
  NOT NULL;

CREATE DOMAIN public.dlgr_2l
  AS character varying(2)
  COLLATE pg_catalog."default";

CREATE DOMAIN public.dlgr_code
  AS character varying(20)
  COLLATE pg_catalog."default"
  NOT NULL;

CREATE DOMAIN public.dlgr_codel
  AS character varying(20)
  COLLATE pg_catalog."default";

CREATE DOMAIN public.dlgr_lib
  AS character varying(100)
  COLLATE pg_catalog."default"
  NOT NULL;

CREATE DOMAIN public.dlgr_libcode
  AS character varying(100)
  COLLATE pg_catalog."default"
  NOT NULL;

CREATE DOMAIN public.dlib1
  AS character varying(1)
  COLLATE pg_catalog."default"
  NOT NULL;

CREATE DOMAIN public.dlib10
  AS character varying(10)
  COLLATE pg_catalog."default";

CREATE DOMAIN public.dlib100
  AS character varying(100)
  COLLATE pg_catalog."default";

CREATE DOMAIN public.dlib1l
  AS character varying(1)
  COLLATE pg_catalog."default";

CREATE DOMAIN public.dlib20
  AS character varying(20)
  COLLATE pg_catalog."default";

CREATE DOMAIN public.dlib255
  AS character varying(255)
  COLLATE pg_catalog."default";

CREATE DOMAIN public.dlib255nn
  AS character varying(255)
  COLLATE pg_catalog."default"
  NOT NULL;

CREATE DOMAIN public.dlib50
  AS character varying(50)
  COLLATE pg_catalog."default";

CREATE DOMAIN public.dlib50nn
  AS character varying(50)
  COLLATE pg_catalog."default"
  NOT NULL;

CREATE DOMAIN public.dlib60
  AS character varying(60)
  COLLATE pg_catalog."default";

CREATE DOMAIN public.dlib7
  AS character varying(7)
  COLLATE pg_catalog."default";

CREATE DOMAIN public.dlib8
  AS character varying(8)
  COLLATE pg_catalog."default";

CREATE DOMAIN public.dlib8nn
  AS character varying(8)
  COLLATE pg_catalog."default"
  NOT NULL;

CREATE DOMAIN public.dlib_commentaire
  AS character varying(2000)
  COLLATE pg_catalog."default";

CREATE DOMAIN public.dlib_mouv
  AS character varying(1)
  COLLATE pg_catalog."default"
  NOT NULL
  CONSTRAINT dlib_mouv_check CHECK (VALUE::text = 'E'::text OR VALUE::text = 'S'::text);

CREATE DOMAIN public.dlong
  AS numeric(15,4);

CREATE DOMAIN public.dtel
  AS character varying(14)
  COLLATE pg_catalog."default";

CREATE DOMAIN public.num_version
  AS character varying(20)
  COLLATE pg_catalog."default";

CREATE DOMAIN public.qte3
  AS numeric(15,3);
  
----------------------------------------------------------------------------------------------------------
CREATE OR REPLACE FUNCTION public.before_insert()
  RETURNS trigger AS
$BODY$
begin
 
   NEW.qui_cree := user;
   NEW.quand_cree := now();
   NEW.qui_modif := user;
   NEW.quand_modif := now();
   NEW.ip_acces := inet_client_addr();
   select num_version from ta_version into NEW.version;  
   RETURN NEW;
end $BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION public.before_insert()
  OWNER TO postgres;

CREATE OR REPLACE FUNCTION public.before_update()
  RETURNS trigger AS
$BODY$
begin
 
 
   NEW.qui_modif := user;
   NEW.quand_modif := now();
   NEW.ip_acces := inet_client_addr();
   select num_version from ta_version into NEW.version;  
   
   RETURN NEW;
end $BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION public.before_update()
  OWNER TO postgres;

----------------------------------------------------------------------------------------------------------

CREATE TABLE ta_t_edition_catalogue
(
  id_t_edition_catalogue serial NOT NULL,
  code_t_edition_catalogue dlgr_libcode,
  libelle dlib255,
  description dlib255,
  systeme boolean,
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_t_edition_catalogue_pkey PRIMARY KEY (id_t_edition_catalogue),
  CONSTRAINT ta_t_edition_catalogue_code_unique UNIQUE (code_t_edition_catalogue)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_t_edition_catalogue
  OWNER TO postgres;

CREATE INDEX ta_t_edition_catalogue_code
  ON ta_t_edition_catalogue
  USING btree
  (code_t_edition_catalogue COLLATE pg_catalog."default");

CREATE INDEX unq1_ta_t_edition_catalogue
  ON ta_t_edition_catalogue
  USING btree
  (code_t_edition_catalogue COLLATE pg_catalog."default");

CREATE TRIGGER tbi_ta_t_edition_catalogue
  BEFORE INSERT
  ON ta_t_edition_catalogue
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

CREATE TRIGGER tbu_ta_t_edition_catalogue
  BEFORE UPDATE
  ON ta_t_edition_catalogue
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

CREATE TABLE ta_edition_catalogue
(
  id_edition_catalogue serial NOT NULL,
  code_edition_catalogue dlib255,
  libelle_edition_catalogue dlib255,
  description_edition_catalogue text,
  id_t_edition_catalogue did_facultatif,
  miniature bytea,
  fichier_blob bytea,
  fichier_chemin dlib255,
  fichier_nom dlib255,
  defaut boolean,
  actif boolean,
  systeme boolean,
  importation_manuelle boolean,
  date_importation date_heure_lgr,
  version_edition_catalogue dlib50,
  fichier_exemple bytea,
  code_dossier_edition_personalisee dlib255,
  payant boolean,
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_edition_catalogue_pkey PRIMARY KEY (id_edition_catalogue),
  CONSTRAINT fk_ta_edition_catalogue_id_t_edition_catalogue FOREIGN KEY (id_t_edition_catalogue)
      REFERENCES ta_t_edition_catalogue (id_t_edition_catalogue) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ta_edition_catalogue_code_unique UNIQUE (code_edition_catalogue)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_edition_catalogue
  OWNER TO postgres;

CREATE INDEX ta_edition_catalogue_id
  ON ta_edition_catalogue
  USING btree
  (id_edition_catalogue);

CREATE INDEX unq1_ta_edition_catalogue
  ON ta_edition_catalogue
  USING btree
  (code_edition_catalogue COLLATE pg_catalog."default");

CREATE TRIGGER tbi_edition_catalogue
  BEFORE INSERT
  ON ta_edition_catalogue
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

CREATE TRIGGER tbu_edition_catalogue
  BEFORE UPDATE
  ON ta_edition_catalogue
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();
  
----------------------------------------------------------------------------------------------------------
CREATE OR REPLACE FUNCTION public.before_insert_version()
  RETURNS trigger AS
$BODY$
begin
 
   New.QUI_CREE = USER;
   New.QUAND_CREE = 'NOW';
   New.QUI_MODIF = USER;
   New.QUAND_MODIF = 'NOW';
   new.IP_ACCES = current_connection;
   
   RETURN NEW;
end $BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION public.before_insert_version()
  OWNER TO postgres;

  CREATE OR REPLACE FUNCTION public.before_update_version()
  RETURNS trigger AS
$BODY$
begin
 IF (NEW($1) IS NULL) THEN
   New.QUI_MODIF = USER;
   New.QUAND_MODIF = 'NOW';
   new.IP_ACCES = current_connection;
    END IF;
   RETURN NEW;
end $BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION public.before_update_version()
  OWNER TO postgres;

  
  CREATE TABLE public.ta_version
(
  id_version serial not null,
  num_version dlib255nn,
  old_version dlib255,
  date_version timestamp with time zone,
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  ip_acces dlib50,
  CONSTRAINT ta_version_pkey PRIMARY KEY (id_version)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.ta_version
  OWNER TO postgres;

-- Trigger: tbi_ta_version on public.ta_version

-- DROP TRIGGER tbi_ta_version ON public.ta_version;

CREATE TRIGGER tbi_ta_version
  BEFORE INSERT
  ON public.ta_version
  FOR EACH ROW
  EXECUTE PROCEDURE public.before_insert_version();

-- Trigger: tbu_ta_version on public.ta_version

-- DROP TRIGGER tbu_ta_version ON public.ta_version;

CREATE TRIGGER tbu_ta_version
  BEFORE UPDATE
  ON public.ta_version
  FOR EACH ROW
  EXECUTE PROCEDURE public.before_update_version();
  
  