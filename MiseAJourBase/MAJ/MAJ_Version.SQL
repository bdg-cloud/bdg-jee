CREATE or ALTER  PROCEDURE GESTION_CREATION_1
as    
declare variable nb integer;
BEGIN
nb=0;
select count(*) from ta_version into :nb;
if(nb>0)then
begin
  update ta_version  set num_version = '2.0.13';
end
else
begin
  insert into ta_version  (ID_VERSION,NUM_VERSION,old_VERSION,date_version) values(1,'2.0.13','0','2014/06/24');
end 
END
^

EXECUTE PROCEDURE GESTION_CREATION_1 
^
drop procedure GESTION_CREATION_1
^


