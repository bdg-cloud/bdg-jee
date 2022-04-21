--set schema 'demo';
DROP FUNCTION f_update_seq();

CREATE OR REPLACE FUNCTION f_update_seq(OUT success boolean)
  RETURNS boolean AS
$BODY$
DECLARE
    r record;
    s integer;
    table_schema varchar(50);
    table_name varchar(50);
    column_name varchar(50);
BEGIN
    FOR r IN select tc.table_schema sc, tc.table_name ta, kc.column_name co	
		from  
		    information_schema.table_constraints tc,  
		    information_schema.key_column_usage kc  
		where 
		    tc.constraint_type = 'PRIMARY KEY' 
		    and kc.table_name = tc.table_name and kc.table_schema = tc.table_schema
		    and kc.constraint_name = tc.constraint_name
		    and kc.table_name <> 'ta_abonnement' --la cle primaire de cette table pose probleme
		order by 1, 2
	loop
      table_schema := r.sc;
      table_name := r.ta;
      column_name := r.co;
--IF EXISTS (SELECT 0 FROM pg_class where relname = '''' ||table_name||'_'||column_name||'_seq''' )
--THEN
--      EXECUTE 'SELECT setval('''||table_name||'_'||column_name||'_seq'', COALESCE((SELECT MAX('||column_name||')+1 FROM '||table_name||'), 1), false)';
--	RAISE NOTICE 'Mise à jour de la sequence %', table_name||'_'||column_name||'_seq';
--      ELSE
--	RAISE NOTICE '==== La sequence % n existe pas.', table_name||'_'||column_name||'_seq';
--END IF;

	EXECUTE format('select COALESCE((SELECT MAX(%s)::int+1 FROM %s),1)::int',column_name,table_name) into s;
	EXECUTE format('ALTER SEQUENCE IF EXISTS %s_%s_seq RESTART WITH %s',table_name,column_name,s);
    end loop;
    success := TRUE;

END
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION f_update_seq()
  OWNER TO bdg;
