
CREATE OR REPLACE function f_add_col(_tbl regclass, _col  text, _type text, _default  text, _sql_constraint text, OUT success bool)
    LANGUAGE plpgsql AS
$func$
BEGIN
	-- SELECT demo1.f_add_col('demo1.ta_tiers', 'nouveau_champ', 'int',null,null);
	-- SELECT demo1.f_add_col('demo1.ta_tiers', 'nouveau_champ', 'int','NULL',null);
	-- SELECT demo1.f_add_col('demo1.ta_tiers', 'nouveau_champ', 'int','NULL','ALTER TABLE demo1.ta_conformite ADD CONSTRAINT fk_id_modele_conformite_origine FOREIGN KEY (id_modele_conformite_origine) REFERENCES ta_modele_conformite (id_modele_conformite) ON UPDATE NO ACTION ON DELETE NO ACTION;');
	-- ou faire "set schema 'demo1';" et après on n'est pas obligé de précisé le schéma : SELECT f_add_col('ta_tiers', 'nouveau_champ', 'int','NULL');
raise notice '% == Ajout du champ % dans la table % ',current_schema (), _col, _tbl;
IF EXISTS (
   SELECT 1 FROM pg_attribute
   WHERE  attrelid = _tbl
   AND    attname = _col
   AND    NOT attisdropped) THEN
   success := FALSE;
   raise notice 'Le champ % existe deja dans %', _col, _tbl;

ELSE
	if _default is distinct from null then
		raise notice 'Ajout du champ % dans la table % avec le type % et la valeur par defaut %', _col, _tbl, _type, _default;
	  EXECUTE '
   		ALTER TABLE ' || _tbl || ' ADD COLUMN ' || quote_ident(_col) || ' ' || _type || ' DEFAULT ' || _default;
   		if _sql_constraint is distinct from null then
   			raise notice 'Ajout de la contrainte %', _sql_constraint;
   			EXECUTE _sql_constraint;
   		end if;
   	else
		raise notice 'Ajout du champ % dans la table % avec le type %', _col, _tbl, _type;
   	 	EXECUTE '
   		ALTER TABLE ' || _tbl || ' ADD COLUMN ' || quote_ident(_col) || ' ' || _type;
    end if;
   
   success := TRUE;
END IF;

END
$func$;

-- -----------------------------------------------------------------------------------------------------------------------------
-- -----------------------------------------------------------------------------------------------------------------------------
-- -----------------------------------------------------------------------------------------------------------------------------
-- -----------------------------------------------------------------------------------------------------------------------------


CREATE OR REPLACE function f_insert(_tbl regclass, _where text, _sql_insert text, _sql_update text, OUT success bool)
    LANGUAGE plpgsql AS
$func$
DECLARE
 v int;
BEGIN
	--SELECT demo1.f_insert('demo1.ta_controle', 'champ =''TaLFabrication.numLigneLDocument'' and contexte =''L_FABRICATION''',
	--'INSERT INTO demo1.ta_controle(champ, contexte, controle_defaut, controle_utilisateur) VALUES (''TaLFabrication.numLigneLDocument'', ''L_FABRICATION'', 100, 100)',
	--null);

	--SELECT demo1.f_insert('demo1.ta_controle', 'champ =''TaLFabrication.numLigneLDocument'' and contexte =''L_FABRICATION''',
	--'INSERT INTO demo1.ta_controle(champ, contexte, controle_defaut, controle_utilisateur) VALUES (''TaLFabrication.numLigneLDocument'', ''L_FABRICATION'', 100, 100)',
	--'UPDATE demo1.ta_controle SET controle_defaut=''100'', controle_utilisateur=''100'' where champ =''TaLFabrication.numLigneLDocument'' and contexte =''L_FABRICATION'' ');

raise notice '% == Insertion dans la table % pour la condition % ',current_schema (), _tbl, _where;

EXECUTE format('SELECT (EXISTS (SELECT 1 FROM %s WHERE %s))::int', _tbl, _where)
INTO v;

raise notice 'Exist % ', v;

IF v=1 THEN

   success := FALSE;
   raise notice 'Pas d insertion, cette ligne existe deja dans la table %', _tbl;
	raise notice 'Mise à jour : %', _sql_update;
	EXECUTE _sql_update;

ELSE
	raise notice 'Insertion : %', _sql_insert;
   	 EXECUTE _sql_insert;
   
   success := TRUE;
END IF;

END
$func$;


-- -----------------------------------------------------------------------------------------------------------------------------
-- -----------------------------------------------------------------------------------------------------------------------------
-- -----------------------------------------------------------------------------------------------------------------------------
-- -----------------------------------------------------------------------------------------------------------------------------


CREATE OR REPLACE function f_update_seq(OUT success bool) 
    LANGUAGE plpgsql AS
$func$
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
$func$;