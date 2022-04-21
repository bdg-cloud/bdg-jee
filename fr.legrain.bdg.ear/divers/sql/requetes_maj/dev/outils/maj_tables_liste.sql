 
 
/*update  ta_article a set id_prix = (select p.id_prix from ta_prix p where p.id_article=a.id_article limit 1) where id_prix is null ;*/
 update  ta_article a set id_rapport_unite = (select p.id from ta_rapport_unite p where p.id_article=a.id_article limit 1) where id_rapport_unite is null ;
 /*update  ta_tiers a set id_email = (select p.id_email from ta_email p where p.id_tiers=a.id_tiers limit 1) where a.id_email is null ;
 update  ta_tiers a set id_web = (select p.id_web from ta_web p where p.id_tiers=a.id_tiers limit 1) where a.id_web is null ;
 update  ta_tiers a set id_telephone = (select p.id_telephone from ta_telephone p where p.id_tiers=a.id_tiers limit 1) where a.id_telephone is null;*/
