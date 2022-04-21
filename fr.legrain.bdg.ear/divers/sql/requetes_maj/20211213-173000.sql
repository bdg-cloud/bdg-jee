
--set schema 'demo';


ALTER TABLE ta_type_mouvement ADD COLUMN dms boolean;
ALTER TABLE ta_type_mouvement ALTER COLUMN dms SET DEFAULT false;
update ta_type_mouvement set dms=false where dms is null;
ALTER TABLE ta_type_mouvement ALTER COLUMN dms SET NOT NULL;


--INSERT INTO ta_type_mouvement ( libelle, code, version_obj, systeme, sens, dms) VALUES( 'Dégustation', 'G', 0, false, false, true);