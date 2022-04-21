
--set schema 'demo';
ALTER TABLE ta_article
ADD COLUMN delai_grace integer;

ALTER TABLE ta_l_echeance
ADD COLUMN grace boolean;

 
     