/*set schema 'demo';*/  
  
ALTER TABLE ta_l_remise
  ADD COLUMN date_export  timestamp(0);
  
  
ALTER TABLE ta_avis_echeance
  ADD COLUMN date_export  timestamp(0);
  
ALTER TABLE ta_facture 
   ALTER COLUMN date_export TYPE timestamp(0);

   ALTER TABLE ta_avoir 
   ALTER COLUMN date_export TYPE timestamp(0);

ALTER TABLE ta_reglement 
   ALTER COLUMN date_export TYPE timestamp(0);

ALTER TABLE ta_remise 
   ALTER COLUMN date_export TYPE timestamp(0);  
   
ALTER TABLE ta_l_remise 
   ALTER COLUMN date_export TYPE timestamp(0);    

ALTER TABLE ta_apporteur 
   ALTER COLUMN date_export TYPE timestamp(0);

   ALTER TABLE ta_r_reglement 
   ALTER COLUMN date_export TYPE timestamp(0);
      
ALTER TABLE ta_r_avoir
   ALTER COLUMN date_export TYPE timestamp(0); 
   
   
  
update ta_facture set date_export= date_trunc('hour',now()) where export=1;

update ta_avoir set date_export= date_trunc('hour',now()) where export=1;
update ta_r_avoir set date_export= date_trunc('hour',now()) where export=1;

update ta_acompte set date_export= date_trunc('hour',now()) where export=1;

update ta_apporteur set date_export= date_trunc('hour',now()) where export=1;

update ta_reglement set date_export= date_trunc('hour',now()) where export=1;
update ta_r_reglement set date_export= date_trunc('hour',now()) where export=1;

update ta_remise set date_export= date_trunc('hour',now()) where export=1;
update ta_l_remise set date_export= date_trunc('hour',now()) where export=1;


ALTER TABLE ta_r_prix   ALTER COLUMN id_tiers DROP NOT NULL;
ALTER TABLE ta_r_prix   ALTER COLUMN id_t_tarif DROP NOT NULL;

