     
CREATE OR REPLACE FUNCTION public.maj_owner()
  RETURNS boolean AS
$BODY$
DECLARE 
  r record;
  i int;
  v_schema varchar; 
  v_new_owner varchar := 'bdg';
BEGIN

    /*for v_schema in SELECT schema_name FROM information_schema.schemata where schema_name<>'public' and schema_name<>'information_schema' and schema_name not like 'pg_%' loop*/
      
    FOR r IN 
        select 'ALTER TABLE "' ||  current_schema() || '"."' || table_name || '" OWNER TO ' || v_new_owner || ';' as a from information_schema.tables where table_schema =  current_schema()
        union all
        select 'ALTER SEQUENCE "' ||  current_schema() || '"."' || sequence_name || '" OWNER TO ' || v_new_owner || ';' as a from information_schema.sequences where sequence_schema =  current_schema()
        union all
        select 'ALTER DOMAIN "' ||  current_schema() || '"."' || domain_name || '" OWNER TO ' || v_new_owner || ';' as a from information_schema.domains where domain_schema =  current_schema()
        union all
        select 'ALTER FUNCTION "'|| current_schema()||'"."'||p.proname||'"('||pg_get_function_identity_arguments(p.oid)||') OWNER TO ' || v_new_owner || ';' as a from pg_proc p join pg_namespace nsp ON p.pronamespace = nsp.oid where nsp.nspname =  current_schema()
        union all
        select 'ALTER DATABASE "' || current_database() || '" OWNER TO ' || v_new_owner 

    LOOP
        EXECUTE r.a;
    /*END LOOP;*/
    END LOOP;
    /*FOR i IN array_lower(v_schema,1) .. array_upper(v_schema,1)
    LOOP
        EXECUTE 'ALTER SCHEMA "' || v_schema[i] || '" OWNER TO ' || v_new_owner ;
    END LOOP;*/
    return true;
END
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;  
  
  
SELECT public.maj_owner();  


-- Function: demo.count_em_all()

-- DROP FUNCTION demo.count_em_all();

CREATE OR REPLACE FUNCTION demo.count_em_all()
  RETURNS SETOF demo.table_count AS
$BODY$
   DECLARE
       the_count RECORD;
       t_name RECORD;
       r table_count%ROWTYPE;
 
   BEGIN
       -- list the schemas
       FOR t_name IN
         SELECT schema_name FROM information_schema.schemata where schema_name<>'public' and schema_name<>'information_schema' and schema_name not like 'pg_%'   
           LOOP
               -- Run your query on each schema
               FOR the_count IN EXECUTE 'SELECT COUNT(*) AS "count" FROM "'||t_name.schema_name||'".ta_article where id_article is not null'
               LOOP
               END LOOP;
 
               r.table_name := t_name.schema_name;
               r.num_rows := the_count.count;
               RETURN NEXT r;
           END LOOP;
           RETURN;
   END;
   $BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION demo.count_em_all()
  OWNER TO bdg;
