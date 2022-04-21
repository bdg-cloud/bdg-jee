-- Table: public.ta_alias_espace_client

-- DROP TABLE public.ta_alias_espace_client;

CREATE TABLE public.ta_alias_espace_client
(
  id_alias_espace_client serial NOT NULL ,
  tenant dlib255nn,
  alias dlib255nn,
  actif boolean default true,
 
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,

  CONSTRAINT ta_alias_espace_client_pkey PRIMARY KEY (id_alias_espace_client),
  CONSTRAINT ta_alias_espace_client_unique UNIQUE (tenant,alias)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.ta_alias_espace_client
  OWNER TO postgres;

-- Index: public.ta_alias_espace_client_id

-- DROP INDEX public.ta_alias_espace_client_id;

CREATE INDEX ta_alias_espace_client_id
  ON public.ta_alias_espace_client
  USING btree
  (id_alias_espace_client);


-- Trigger: tbi_alias_espace_client on public.ta_alias_espace_client

-- DROP TRIGGER tbi_alias_espace_client ON public.ta_alias_espace_client;

CREATE TRIGGER tbi_alias_espace_client
  BEFORE INSERT
  ON public.ta_alias_espace_client
  FOR EACH ROW
  EXECUTE PROCEDURE public.before_insert();

-- Trigger: tbu_alias_espace_client on public.ta_alias_espace_client

-- DROP TRIGGER tbu_alias_espace_client ON public.ta_alias_espace_client;

CREATE TRIGGER tbu_alias_espace_client
  BEFORE UPDATE
  ON public.ta_alias_espace_client
  FOR EACH ROW
  EXECUTE PROCEDURE public.before_insert();


