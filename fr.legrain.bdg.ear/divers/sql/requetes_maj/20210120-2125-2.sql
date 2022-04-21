
CREATE OR REPLACE FUNCTION update_etat_tout_doc_lie(OUT success boolean)
  RETURNS boolean AS
$BODY$
DECLARE

 ligne record;
 r record;
 totaux record;
 doc_encours integer;
 doc_partiellement_transforme integer;
 doc_termine integer;
 totalTransforme float;
id_etat_encours integer;
id_etat_termine integer;
id_etat_partiellement integer;
id_doc integer;
id_l_doc integer;
id integer;
nom_table varchar(50);

BEGIN

select id_etat from ta_etat where code_etat like ('doc_encours') into id_etat_encours;
select id_etat from ta_etat where code_etat like ('doc_part_Transforme') into id_etat_partiellement;
select id_etat from ta_etat where code_etat like ('doc_tot_Transforme') into id_etat_termine;


for r in select * from (
  values
    ('avoir'::varchar(50)),
    ('acompte'::varchar(50)),
    ('apporteur'::varchar(50)),
    ('avis_echeance'::varchar(50)),
    ('abonnement'::varchar(50)),
    ('bonliv'::varchar(50)),
    ('boncde'::varchar(50)),
    ('boncde_achat'::varchar(50)),
    ('devis'::varchar(50)),
    ('facture'::varchar(50)),
    ('bon_reception'::varchar(50)),
    ('preparation'::varchar(50)),
    ('ticket_caisse'::varchar(50)),
    ('proforma'::varchar(50)),
    ('prelevement'::varchar(50))) 
    as t(nom)
    loop--loop selection nom de toutes les tables des documents   	 	
	--facture
	nom_table=r.nom;
		-- travail sur ligne des documents pour mettre un état sur chaque ligne
	FOR ligne IN EXECUTE 'select f.*  from  ta_l_'||nom_table||' f where f.id_article is not null and f.qte_l_document is not null and f.qte_l_document<>0' 
	loop --loop selection ligne document
		if(nom_table='facture')then select coalesce(sum(fac.net_ttc_calc),0)as ttc,(select coalesce(sum(rr.affectation),0) from ta_r_reglement rr where rr.id_facture=fac.id_document)
		+(select coalesce(sum(rav.affectation),0)from ta_r_avoir rav where rav.id_facture=fac.id_document)as regle from ta_facture fac where fac.id_document = ligne.id_document group by fac.id_document into totaux;
		else select sum(l.qte_recue) from ta_ligne_a_ligne l where l.id_ligne_src=ligne.id_l_document and l.qte_recue is not null into totalTransforme;
		end if;
	--       	for totalTransforme in select sum(l.qte_recue) from ta_ligne_a_ligne l where l.id_ligne_src=ligne.id_l_document and l.qte_recue is not null
	--         loop --loop totalTransforme
		id_l_doc=ligne.id_l_document;
	            if(nom_table='facture')then-- si nom_table facture		
			    if(totaux.ttc<=totaux.regle)then -- si reglement >= au net ttc , facture réglée donc terminée
				Execute format('insert into ta_r_etat_ligne_doc (id_etat,date_etat,commentaire,id_l_%s) values(%s,now(),''Initialisation par défaut par Legrain'',%s )RETURNING ta_r_etat_ligne_doc.id_r_etat_ligne_doc ',nom_table,id_etat_termine,id_l_doc) into id;  
				if(id is not null)then EXECUTE format('update ta_l_'||nom_table||' set id_r_etat = %s where id_l_document = %s',id,id_l_doc);end if; 
			    else
				if(totaux.regle=0)then -- si facture pas réglée, donc enCours
					Execute format('insert into ta_r_etat_ligne_doc (id_etat,date_etat,commentaire,id_l_%s) values(%s,now(),''Initialisation par défaut par Legrain'',%s )RETURNING ta_r_etat_ligne_doc.id_r_etat_ligne_doc ',nom_table,id_etat_encours,id_l_doc) into id;  
					if(id is not null)then EXECUTE format('update ta_l_'||nom_table||' set id_r_etat = %s where id_l_document = %s',id,id_l_doc);end if; 
			        else --sinon partiellement réglée donc facture partiellement transformée
					Execute format('insert into ta_r_etat_ligne_doc (id_etat,date_etat,commentaire,id_l_%s) values(%s,now(),''Initialisation par défaut par Legrain'',%s )RETURNING ta_r_etat_ligne_doc.id_r_etat_ligne_doc ',nom_table,id_etat_partiellement,id_l_doc) into id;  
					if(id is not null)then EXECUTE format('update ta_l_'||nom_table||' set id_r_etat = %s where id_l_document = %s',id,id_l_doc);end if; 
			        end if;	-- end if si facture pas réglée ou autres
			    end if;-- end if -- si reglement >= au net ttc ou pas			 

		    else
		
			    if(totalTransforme=0 or totalTransforme is null)then -- si pas de transformation
						 Execute format('insert into ta_r_etat_ligne_doc (id_etat,date_etat,commentaire,id_l_%s) values(%s,now(),''Initialisation par défaut par Legrain'',%s )RETURNING ta_r_etat_ligne_doc.id_r_etat_ligne_doc ',nom_table,id_etat_encours,id_l_doc) into id;  
						 if(id is not null)then EXECUTE format('update ta_l_'||nom_table||' set id_r_etat = %s where id_l_document = %s',id,id_l_doc);end if;      		
			    else  -- si transformation		        
					if(ligne.qte_l_document >=0)then -- si qté positive
						if(totalTransforme>=ligne.qte_l_document)then -- si tout transforme
							Execute format('insert into ta_r_etat_ligne_doc (id_etat,date_etat,commentaire,id_l_%s) values(%s,now(),''Initialisation par défaut par Legrain'',%s )RETURNING ta_r_etat_ligne_doc.id_r_etat_ligne_doc ',nom_table,id_etat_termine,id_l_doc) into id;  
							if(id is not null)then EXECUTE format('update ta_l_'||nom_table||' set id_r_etat = %s where id_l_document = %s',id,id_l_doc);	end if; 
						
						else -- si partiellement transforme
							Execute format('insert into ta_r_etat_ligne_doc (id_etat,date_etat,commentaire,id_l_%s) values(%s,now(),''Initialisation par défaut par Legrain'',%s )RETURNING ta_r_etat_ligne_doc.id_r_etat_ligne_doc ',nom_table,id_etat_partiellement,id_l_doc) into id;  
							if(id is not null)then EXECUTE format('update ta_l_'||nom_table||' set id_r_etat = %s where id_l_document = %s',id,id_l_doc);	end if; 
								
						end if; -- si tout transforme ou partiellement sur qté positive
					else -- si qté négative
						if(totalTransforme<ligne.qte_l_document)then -- si tout transforme
							Execute format('insert into ta_r_etat_ligne_doc (id_etat,date_etat,commentaire,id_l_%s) values(%s,now(),''Initialisation par défaut par Legrain'',%s )RETURNING ta_r_etat_ligne_doc.id_r_etat_ligne_doc ',nom_table,id_etat_termine,id_l_doc) into id;  
							if(id is not null)then EXECUTE format('update ta_l_'||nom_table||' set id_r_etat = %s where id_l_document = %s',id,id_l_doc); 	end if;      		
						else -- si partiellement transforme
							Execute format('insert into ta_r_etat_ligne_doc (id_etat,date_etat,commentaire,id_l_%s) values(%s,now(),''Initialisation par défaut par Legrain'',%s )RETURNING ta_r_etat_ligne_doc.id_r_etat_ligne_doc ',nom_table,id_etat_partiellement,id_l_doc) into id;  
							if(id is not null)then EXECUTE format('update ta_l_'||nom_table||' set id_r_etat = %s where id_l_document = %s',id,id_l_doc);	end if; 
							end if; -- si tout transforme ou partiellement sur qté négative			
					end if;-- si qté positive ou négative		
			    end if;-- end si transformation ou pas
		    end if; --end if si nom_table = facture	
	--       	end loop; --end loop totalTransforme
	end loop; --end loop selection ligne document


	-- travail sur table des documents (entête)
	for id_doc IN EXECUTE 'select f.*  from  ta_'||nom_table||' f ' 
  loop --loop selection document
        	-- récup etatPartielle ou encours = document pas termine
        
        	EXECUTE format('select count(f1.*) from  ta_l_'||nom_table||' f1 join ta_r_etat_ligne_doc rl on rl.id_r_etat_ligne_doc=f1.id_r_etat where f1.id_document=%s and rl.id_etat = %s ',id_doc,id_etat_encours) into doc_encours;
        	EXECUTE format('select count(f2.*) from  ta_l_'||nom_table||' f2 join ta_r_etat_ligne_doc rl on rl.id_r_etat_ligne_doc=f2.id_r_etat where f2.id_document=%s and rl.id_etat = %s ',id_doc,id_etat_partiellement) into doc_partiellement_transforme;
        	EXECUTE format('select count(f3.*) from  ta_l_'||nom_table||' f3 join ta_r_etat_ligne_doc rl on rl.id_r_etat_ligne_doc=f3.id_r_etat where f3.id_document=%s and rl.id_etat = %s ',id_doc,id_etat_termine) into doc_termine;
        	-- si etat trouve dans les lignes mais qu'il y a au moins un etat # de termine alors document non termine
        	if(doc_encours>0 and (doc_partiellement_transforme=0 and doc_termine=0))then -- s'il y a que des encours alors le document est enCours
              		Execute format('insert into ta_r_etat (id_etat,date_etat,commentaire,id_%s) values(%s,now(),''Initialisation par défaut par Legrain'',%s )RETURNING ta_r_etat.id_r_etat ',nom_table,id_etat_encours,id_doc) into id;	     
              		if(id is not null)then EXECUTE format('update ta_'||nom_table||' set id_r_etat = %s where id_document = %s',id,id_doc);        		end if; 
      
          else 
                  if(doc_termine>0 and (doc_partiellement_transforme=0 and doc_encours=0))then -- s'il y a que des termines alors le document est termine
              		Execute format('insert into ta_r_etat (id_etat,date_etat,commentaire,id_%s) values(%s,now(),''Initialisation par défaut par Legrain'',%s )RETURNING ta_r_etat.id_r_etat ',nom_table,id_etat_termine,id_doc) into id;	     
              		if(id is not null)then EXECUTE format('update ta_'||nom_table||' set id_r_etat = %s where id_document = %s',id,id_doc);        		end if; 
      
              	  else 
                      if(doc_partiellement_transforme+doc_termine+doc_encours>0 )then -- s'il y a au moins un etat et qu'il n'est pas rentré dans les autres cas alors le document est partiellement transforme
              		    Execute format('insert into ta_r_etat (id_etat,date_etat,commentaire,id_%s) values(%s,now(),''Initialisation par défaut par Legrain'',%s )RETURNING ta_r_etat.id_r_etat ',nom_table,id_etat_partiellement,id_doc) into id;	     
              		    if(id is not null)then EXECUTE format('update ta_'||nom_table||' set id_r_etat = %s where id_document = %s',id,id_doc);        		end if;
                      end if; -- end if s'il y a au moins un etat et qu'il n'est pas rentré dans les autres cas alors le document est partiellement transforme
                  end if; -- end if s'il y a que des termines alors le document est termine
        	end if;-- end if s'il y a que des encours alors le document est enCours
	end loop;--end loop selection document
end loop;--end loop selection nom de toutes les tables des documents  
      	
   success := TRUE;





END
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_etat_tout_doc_lie()
  OWNER TO bdg;




CREATE OR REPLACE FUNCTION recherche_ligne_correspondante(
    OUT success integer,
    IN id_article_src integer,
    IN u1_l_document_src character varying,
    IN qte_l_document_src qte3,
    IN id_dest integer,
    IN type_dest character varying)
  RETURNS integer AS
$BODY$
DECLARE

 ligne record;
 r record;
 nom_table varchar(50);
 requete text;
BEGIN
nom_table=type_dest;
if(u1_l_document_src is null)then u1_l_document_src=''; end if;
	for ligne IN EXECUTE format('select lf.*  from  ta_l_'||nom_table||' lf where id_document = %s and lf.id_article is not null and lf.id_article=%s 
	 and coalesce(lf.u1_l_document,'''') like ''%s'' and  lf.id_l_document not in (select * from ligneTraite) order by lf.id_l_document',id_dest,id_article_src,u1_l_document_src)
	loop--loop selection de toutes les lignes du document dest	
		success = ligne.id_l_document; -- si même article, même unité,  correspondance partielle
	end loop;--end loop selection de toutes les lignes du document dest

END
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;


CREATE OR REPLACE FUNCTION recherche_ligne_correspondante_complete(
    OUT success integer,
    IN id_article_src integer,
    IN u1_l_document_src character varying,
    IN qte_l_document_src qte3,
    IN id_dest integer,
    IN type_dest character varying)
  RETURNS integer AS
$BODY$
DECLARE

 ligne record;
 r record;
 nom_table varchar(50);
 requete text;
BEGIN
nom_table=type_dest;
if(u1_l_document_src is null)then u1_l_document_src='';end if;
	for ligne IN EXECUTE format('select lf.*  from  ta_l_'||nom_table||' lf where id_document = %s and lf.id_article is not null and lf.id_article=%s and 
		coalesce(lf.u1_l_document,'''') like ''%s'' and coalesce(lf.qte_l_document,0)=%s and lf.id_l_document not in (select * from ligneTraite) order by lf.id_l_document',id_dest,id_article_src,u1_l_document_src,qte_l_document_src)
	loop--loop selection de toutes les lignes du document dest
		success = ligne.id_l_document; -- si même article, même unité, même qté correspondance complete
	end loop;--end loop selection de toutes les lignes du document dest


END
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;




CREATE OR REPLACE FUNCTION maj_ligne_a_ligne(OUT success boolean)
  RETURNS boolean AS
$BODY$
DECLARE

 ligne record;
 ligne_dest record;
 r record;
id_l_complet integer;
id_l_incomplet integer;

id_l_document integer;
num_lot text;
qte_l_document_src float ;
qte_l_document_dest float ;
prix_u_l_document_src float;
u1_l_document_src varchar;
u1_l_document_dest varchar;

retour_id_traite integer;

BEGIN

CREATE TABLE IF NOT EXISTS docTraite (idsrc,iddest) as values(-1::integer,-1::integer);
CREATE TABLE IF NOT EXISTS ligneTraite (id) as values(-1::integer);
CREATE TABLE IF NOT EXISTS ligneSrc (id,type_doc,id_document,id_article,u1_l_document,qte_l_document,iddest,typeDest) as values(null::integer,null::text,null::integer,null::integer,null::text,null::float,null::integer,null::text);
CREATE TABLE IF NOT EXISTS ligneTraiteNonTraite (id,type_doc) as values(-1::integer,''::text);

delete from docTraite;
delete from ligneTraite;
delete from ligneSrc;
delete from ligneTraiteNonTraite;
delete from ta_ligne_a_ligne where indic is not null and indic <>'';

for r in select   
(case
when id_bonliv is not null and id_bonliv=id_src then id_bonliv
when id_acompte is not null and id_acompte=id_src then id_acompte
when id_avoir is not null and id_avoir=id_src then id_avoir
when id_apporteur is not null and id_apporteur=id_src then id_apporteur
when id_avis_echeance is not null and id_avis_echeance=id_src then id_avis_echeance
when id_boncde is not null and id_boncde=id_src then id_boncde
when id_devis is not null and id_devis=id_src then id_devis
when id_facture is not null and id_facture=id_src then id_facture
when id_proforma is not null and id_proforma=id_src then id_proforma
when id_prelevement is not null and id_prelevement=id_src then id_prelevement
when id_ticket_caisse is not null and id_ticket_caisse=id_src then id_ticket_caisse
end) as src, 
(case
when id_bonliv is not null and id_bonliv=id_src then 'bonliv'
when id_acompte is not null and id_acompte=id_src then 'acompte'
when id_avoir is not null and id_avoir=id_src then 'avoir'
when id_apporteur is not null and id_apporteur=id_src then 'apporteur'
when id_avis_echeance is not null and id_avis_echeance=id_src then 'avis_echeance'
when id_boncde is not null and id_boncde=id_src then 'boncde'
when id_devis is not null and id_devis=id_src then 'devis'
when id_facture is not null and id_facture=id_src then 'facture'
when id_proforma is not null and id_proforma=id_src then 'proforma'
when id_prelevement is not null and id_prelevement=id_src then 'prelevement'
when id_ticket_caisse is not null and id_ticket_caisse=id_src then 'ticket_caisse'
end) as type_src,
(case
when id_bonliv is not null and id_bonliv<>id_src then id_bonliv
when id_acompte is not null and id_acompte<>id_src then id_acompte
when id_avoir is not null and id_avoir<>id_src then id_avoir
when id_apporteur is not null and id_apporteur<>id_src then id_apporteur
when id_avis_echeance is not null and id_avis_echeance<>id_src then id_avis_echeance
when id_boncde is not null and id_boncde<>id_src then id_boncde
when id_devis is not null and id_devis<>id_src then id_devis
when id_facture is not null and id_facture<>id_src then id_facture
when id_proforma is not null and id_proforma<>id_src then id_proforma
when id_prelevement is not null and id_prelevement<>id_src then id_prelevement
when id_ticket_caisse is not null and id_ticket_caisse<>id_src then id_ticket_caisse
end) as dest,
(case
when id_bonliv is not null and id_bonliv<>id_src then 'bonliv'
when id_acompte is not null and id_acompte<>id_src then 'acompte'
when id_avoir is not null and id_avoir<>id_src then 'avoir'
when id_apporteur is not null and id_apporteur<>id_src then 'apporteur'
when id_avis_echeance is not null and id_avis_echeance<>id_src then 'avis_echeance'
when id_boncde is not null and id_boncde<>id_src then 'boncde'
when id_devis is not null and id_devis<>id_src then 'devis'
when id_facture is not null and id_facture<>id_src then 'facture'
when id_proforma is not null and id_proforma<>id_src then 'proforma'
when id_prelevement is not null and id_prelevement<>id_src then 'prelevement'
when id_ticket_caisse is not null and id_ticket_caisse<>id_src then 'ticket_caisse'                                                                                                                                                                                                                                  
end) as type_dest,id_src
from ta_r_document rd where rd.id_preparation is null and rd.id_boncde_achat is null and rd.id_src is not null
loop--loop selection nom de toutes les lignes de rdocument	

-- if(r.id_src=68 or r.id_src=64 or r.id_src=66)then
insert into docTraite values(r.src,r.dest);

	--je récupère toutes les lignes du document src qui ont un article
 for ligne IN EXECUTE format('select lf.* from  ta_l_'||r.type_src||' lf where lf.id_document = %s and lf.id_article is not null order by id_l_document',r.src)
	 loop -- loop selection de toutes les lignes du document source de Rdocument
          insert into ligneSrc values(ligne.id_l_document,r.type_src,ligne.id_document,ligne.id_article,ligne.u1_l_document,ligne.qte_l_document,r.dest,r.type_dest);
	 	--si on n'est sur un devis ou avis echeance alors le champ numlot existe dans la table et on peut le rechercher	
	 if(not r.type_src='devis' and not r.type_src='avis_echeance' )then --if suivant la table document on n'a pas les mêmes colonnes (ex : id_lot)
		EXECUTE format('select l.numlot from  ta_l_'||r.type_src||' lf left join ta_lot l on l.id_lot=lf.id_lot where lf.id_document = %s and lf.id_article is not null',r.src)into num_lot;
	 end if;--end if suivant la table document on n'a pas les mêmes colonnes (ex : id_lot)
		id_l_complet=null;	  
	   -- récupération d'une ligne du document dest qui correspond totalement à la ligne en cours (même article, unité, qté)
	   select * from recherche_ligne_correspondante_complete(ligne.id_article,ligne.u1_l_document,ligne.qte_l_document,r.dest,r.type_dest)into id_l_complet;
	   if(id_l_complet is not null)then -- si même et complet
			-- remplir un enregistrement de ligne a ligne
			execute format('select lf.*  from  ta_l_'||r.type_dest||' lf where lf.id_l_document = %s',id_l_complet)into ligne_dest;  
			
				execute format('insert into ta_ligne_a_ligne (id_ligne_src,num_lot,qte,qte_recue,prix_unite,unite,unite_recue,id_l_%s,id_l_%s,indic)
				values(%s,''%s'',%s,%s,%s,''%s'',''%s'',%s,%s,%s)',r.type_src,r.type_dest,ligne.id_l_document,num_lot,ligne.qte_l_document,ligne_dest.qte_l_document,ligne.prix_u_l_document,ligne.u1_l_document,ligne_dest.u1_l_document,ligne.id_l_document,ligne_dest.id_l_document,'''majProc''') ;		  
		insert into ligneTraite values(id_l_complet);	

	   else
		id_l_incomplet=null;	
		select * from recherche_ligne_correspondante(ligne.id_article,ligne.u1_l_document,ligne.qte_l_document,r.dest,r.type_dest)into id_l_incomplet;
		if(id_l_incomplet is not null)then -- si même et complet

				-- remplir un enregistrement de ligne a ligne 
				execute format('select lf.*  from  ta_l_'||r.type_dest||' lf where lf.id_l_document = %s',id_l_incomplet)into ligne_dest;
				execute format('insert into ta_ligne_a_ligne (id_ligne_src,num_lot,qte,qte_recue,prix_unite,unite,unite_recue,id_l_%s,id_l_%s,indic)
				values(%s,''%s'',%s,%s,%s,''%s'',''%s'',%s,%s,%s)',r.type_src,r.type_dest,ligne.id_l_document,num_lot,ligne.qte_l_document,ligne_dest.qte_l_document,ligne.prix_u_l_document,ligne.u1_l_document,ligne_dest.u1_l_document,ligne.id_l_document,ligne_dest.id_l_document,'''majProc''') ;		  
				insert into ligneTraite values(id_l_incomplet);
		else
		  insert into ligneTraiteNonTraite values(ligne.id_l_document,r.type_src);	
		end if;-- si trouvé un presque identique (même article, même unité)
	   end if; -- fin si même et complet ou pas
	 end loop;-- end loop selection de toutes les lignes du document source de Rdocument	
-- end if; 
end loop;--end loop selection nom de toutes les lignes de rdocument	

 drop table ligneSrc;
 drop table lignetraite; 
 drop table ligneTraiteNonTraite;
drop table docTraite;
    	
success := TRUE;





END
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION maj_ligne_a_ligne()
  OWNER TO bdg;



---- fin rajout pour les états -----

------ debut paiement des paniers -----------
ALTER TABLE ta_stripe_payment_intent ADD COLUMN id_panier did_facultatif;

ALTER TABLE ta_stripe_payment_intent
  ADD CONSTRAINT fk_ta_stripe_payment_intent_id_panier FOREIGN KEY (id_panier)
      REFERENCES ta_panier (id_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
------- fin paiement des paniers --------
      
      INSERT INTO ta_gen_code_ex
( code_gen_code, cle_gen_code, valeur_gen_code )
VALUES( 'taLot.numLot', 'TaLBonReception', 'L{@coded}_{@date:aa/MM/jj}_{@num:3}' );
INSERT INTO ta_gen_code_ex
( code_gen_code, cle_gen_code, valeur_gen_code )
VALUES( 'taLot.numLot', 'TaLFabricationPF', 'L{@coded}_{@date:aa/MM/jj}_{@num:3}' );



INSERT INTO ta_preferences
( groupe, cle, valeur, valeur_defaut, type_donnees, libelle,  id_categorie_preference, id_groupe_preference, fichier_vue, "position", position_dans_groupe, est_un_groupe, description, id_utilisateur, visible, read_only)
VALUES( 'Lots', 'Autoriser_generation_code_lot_Fab_PF', 'false', '', 'boolean', 'Autoriser l''utilisation de la génération du code lot Fab. PF', 36, NULL, NULL, NULL, NULL, false, NULL, NULL, true, false);

INSERT INTO ta_preferences
( groupe, cle, valeur, valeur_defaut, type_donnees, libelle,  id_categorie_preference, id_groupe_preference, fichier_vue, "position", position_dans_groupe, est_un_groupe, description, id_utilisateur, visible, read_only)
VALUES( 'Lots', 'Autoriser_generation_code_lot_Bon_reception', 'false', '', 'boolean', 'Autoriser l''utilisation de la génération du code lot bon réception', 36, NULL, NULL, NULL, NULL, false, NULL, NULL, true, false);

