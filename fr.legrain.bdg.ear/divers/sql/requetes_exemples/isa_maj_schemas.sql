DROP FUNCTION IF EXISTS count_em_all ();
DROP TYPE IF EXISTS table_count;
CREATE TYPE table_count AS (table_name TEXT, num_rows INTEGER);
 
   CREATE OR REPLACE FUNCTION count_em_all () RETURNS SETOF table_count  AS
   $$
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
               FOR the_count IN EXECUTE 'SELECT COUNT(*) AS "count" FROM "'||t_name.schema_name||'".ta_facture where id_reglement is not null'
               LOOP
               END LOOP;
 
               r.table_name := t_name.schema_name;
               r.num_rows := the_count.count;
               RETURN NEXT r;
           END LOOP;
           RETURN;
   END;
   $$ LANGUAGE plpgsql;
  
  
  
   insert into ta_r_reglement (id_reglement,id_facture,affectation,export,etat)select f.id_reglement,f.id_document,r.net_ttc_calc,r.export,64 from ta_facture f join ta_reglement r on r.id_document=f.id_reglement
  
  
   update ta_facture set id_reglement = null where id_reglement is not null