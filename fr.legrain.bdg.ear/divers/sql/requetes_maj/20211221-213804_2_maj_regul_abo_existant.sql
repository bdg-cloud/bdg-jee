--maj_regul_abo_existant.sql

--set schema 'demo';
update ta_abonnement 
set nb_mois_frequence_facturation = 12
where id_frequence = 4;

--set schema 'demo';
update ta_abonnement 
set nb_mois_frequence_paiement = 12;

--set schema 'demo';
update ta_abonnement 
set type_abonnement = 'sans_engagement'
where date_fin_engagement is null 
and type_abonnement is null;

--set schema 'demo';
update ta_stripe_plan 
set nb_mois = 12
where id_frequence = 4 and nb_mois is null;

--set schema 'demo';
update ta_l_abonnement as l
set date_debut_abonnement = abo.date_debut
from ta_abonnement as abo where abo.id_document = l.id_document;