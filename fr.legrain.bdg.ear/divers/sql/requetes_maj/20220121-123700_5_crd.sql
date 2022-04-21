
--set schema 'demo';

ALTER TABLE ta_type_mouvement ADD COLUMN crd boolean;
ALTER TABLE ta_type_mouvement ALTER COLUMN crd SET DEFAULT false;
update ta_type_mouvement set crd=false where crd is null;
ALTER TABLE ta_type_mouvement ALTER COLUMN crd SET NOT NULL;

ALTER TABLE ta_mouvement_stock  ADD COLUMN qte_titre_transport integer; 
ALTER TABLE ta_mouvement_stock ADD COLUMN code_titre_transport character varying(50) ;  

ALTER TABLE  ta_l_acompte  ADD COLUMN titre_transport_l_document dlib50 ;
ALTER TABLE ta_l_acompte ADD COLUMN qte_titre_transport_l_document integer DEFAULT 0;

ALTER TABLE  ta_l_apporteur  ADD COLUMN titre_transport_l_document dlib50 ;
ALTER TABLE ta_l_apporteur ADD COLUMN qte_titre_transport_l_document integer DEFAULT 0;

ALTER TABLE  ta_l_abonnement  ADD COLUMN titre_transport_l_document dlib50 ;
ALTER TABLE ta_l_abonnement ADD COLUMN qte_titre_transport_l_document integer DEFAULT 0;

ALTER TABLE  ta_l_boncde  ADD COLUMN titre_transport_l_document dlib50 ;
ALTER TABLE ta_l_boncde ADD COLUMN qte_titre_transport_l_document integer DEFAULT 0;

ALTER TABLE  ta_l_boncde_achat  ADD COLUMN titre_transport_l_document dlib50 ;
ALTER TABLE ta_l_boncde_achat ADD COLUMN qte_titre_transport_l_document integer DEFAULT 0;

ALTER TABLE  ta_l_bonliv  ADD COLUMN titre_transport_l_document dlib50 ;
ALTER TABLE ta_l_bonliv ADD COLUMN qte_titre_transport_l_document integer DEFAULT 0;

ALTER TABLE  ta_l_bon_reception  ADD COLUMN titre_transport_l_document dlib50 ;
ALTER TABLE ta_l_bon_reception ADD COLUMN qte_titre_transport_l_document integer DEFAULT 0;

ALTER TABLE  ta_l_devis  ADD COLUMN titre_transport_l_document dlib50 ;
ALTER TABLE ta_l_devis ADD COLUMN qte_titre_transport_l_document integer DEFAULT 0;

ALTER TABLE  ta_l_panier  ADD COLUMN titre_transport_l_document dlib50 ;
ALTER TABLE ta_l_panier ADD COLUMN qte_titre_transport_l_document integer DEFAULT 0;

ALTER TABLE  ta_l_prelevement  ADD COLUMN titre_transport_l_document dlib50 ;
ALTER TABLE ta_l_prelevement ADD COLUMN qte_titre_transport_l_document integer DEFAULT 0;

ALTER TABLE  ta_l_preparation  ADD COLUMN titre_transport_l_document dlib50 ;
ALTER TABLE ta_l_preparation ADD COLUMN qte_titre_transport_l_document integer DEFAULT 0;

ALTER TABLE  ta_l_proforma  ADD COLUMN titre_transport_l_document dlib50 ;
ALTER TABLE ta_l_proforma ADD COLUMN qte_titre_transport_l_document integer DEFAULT 0;

 
     