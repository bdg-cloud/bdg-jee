--maj_abo.sql

--set schema 'demo';
CREATE TABLE ta_jour_relance
(
  id_jour_relance serial NOT NULL,
  id_article integer,
  nb_jour integer,
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  ip_acces dlib50,
  version num_version,
  version_obj did_version_obj,
  UNIQUE(id_article, nb_jour),
  CONSTRAINT ta_jour_relance_pkey PRIMARY KEY (id_jour_relance),
  CONSTRAINT ta_jour_relance_id_article_fkey FOREIGN KEY (id_article)
      REFERENCES ta_article (id_article) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_jour_relance
  OWNER TO bdg;
  
CREATE TRIGGER tbi_ta_jour_relance
	BEFORE INSERT
	ON ta_jour_relance
	FOR EACH ROW
	EXECUTE PROCEDURE before_insert();


CREATE TRIGGER tbu_ta_jour_relance
  BEFORE UPDATE
  ON ta_jour_relance
  FOR EACH ROW
  EXECUTE PROCEDURE before_update();
  





--set schema 'demo';
ALTER TABLE ta_l_echeance
ADD COLUMN id_l_avis_echeance integer;
ALTER TABLE ta_l_echeance
ADD COLUMN id_l_facture integer;
ALTER TABLE ta_l_echeance
ADD CONSTRAINT ta_l_echeance_id_l_avis_echeance_fkey FOREIGN KEY (id_l_avis_echeance)
      REFERENCES ta_l_avis_echeance (id_l_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
      ALTER TABLE ta_l_echeance
ADD CONSTRAINT ta_l_echeance_id_l_facture_fkey FOREIGN KEY (id_l_facture)
      REFERENCES ta_l_facture (id_l_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

--set schema 'demo';
ALTER TABLE ta_l_abonnement 
ADD COLUMN commission did9facult;
ALTER TABLE ta_l_abonnement 
ADD COLUMN nom_dossier character varying (255);

--set schema 'demo';
ALTER TABLE ta_l_abonnement 
RENAME COLUMN commission TO commission_bancaire;

--set schema 'demo';
ALTER TABLE ta_l_abonnement 
ADD COLUMN commission_panier did9facult;


--set schema 'demo';
ALTER TABLE ta_cron
ADD COLUMN date_derniere_execution date_heure_lgr default NULL;

--set schema 'demo';
ALTER TABLE ta_l_echeance
ADD COLUMN coef_multiplicateur numeric(15,4) default null;
--set schema 'demo';
ALTER TABLE ta_l_abonnement
ADD COLUMN coef_multiplicateur numeric(15,4) default 1;

--set schema 'demo';
ALTER TABLE ta_article
ADD COLUMN coef_multiplicateur boolean default false,
ADD COLUMN libl_coef_multiplicateur character varying (255);

--set schema 'demo';
ALTER TABLE ta_article
ADD COLUMN delai_survie integer default 15;


--ajout des colonnes nécéssaires pour utiliser les états sur les lignes d'abonnements

--set schema 'demo';
ALTER TABLE ta_l_abonnement
ADD COLUMN date_debut_abonnement date_lgr DEFAULT NULL;

ALTER TABLE ta_ligne_a_ligne
ADD COLUMN id_l_abonnement integer;
ALTER TABLE ta_ligne_a_ligne
ADD CONSTRAINT ta_ligne_a_ligne_id_l_abonnement_fkey FOREIGN KEY (id_L_abonnement)
      REFERENCES ta_l_abonnement (id_l_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
      
ALTER TABLE ta_hist_r_etat_ligne_doc
ADD COLUMN id_L_abonnement integer;
ALTER TABLE ta_hist_r_etat_ligne_doc
ADD CONSTRAINT fk_ta_hist_r_etat_ligne_doc_l_abonnement FOREIGN KEY (id_L_abonnement)
      REFERENCES ta_l_abonnement (id_l_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;




--set schema 'demo';
INSERT INTO ta_etat (code_etat, libelle_etat, systeme, visible, identifiant, id_t_etat, listable, accepte, position) 
VALUES ('doc_suspendu', 'document suspendu', false, true, 'bdg.etat.document.suspendu', 4, true, true, 4 );


---ABONNEMENTS
--set schema 'demo';
ALTER TABLE ta_abonnement
ADD COLUMN type_abonnement character varying(20);

ALTER TABLE ta_abonnement
ADD COLUMN date_debut_periode_active date_heure_lgr default null;

ALTER TABLE ta_abonnement
ADD COLUMN date_fin_periode_active date_heure_lgr default null;


--ajout des colonnes nécéssaires pour utiliser les états sur les lignes d'échéances
ALTER TABLE ta_r_etat_ligne_doc
ADD COLUMN id_L_echeance integer;
ALTER TABLE ta_r_etat_ligne_doc
ADD CONSTRAINT fk_ta_r_etat_ligne_doc_l_echeance FOREIGN KEY (id_L_echeance)
      REFERENCES ta_l_echeance (id_l_echeance) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
      
ALTER TABLE ta_hist_r_etat_ligne_doc
ADD COLUMN id_L_echeance integer;
ALTER TABLE ta_hist_r_etat_ligne_doc
ADD CONSTRAINT fk_ta_hist_r_etat_ligne_doc_l_echeance FOREIGN KEY (id_L_echeance)
      REFERENCES ta_l_echeance (id_l_echeance) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
      
ALTER TABLE ta_ligne_a_ligne
ADD COLUMN id_l_echeance integer;
ALTER TABLE ta_ligne_a_ligne
ADD CONSTRAINT ta_ligne_a_ligne_id_l_echeance_fkey FOREIGN KEY (id_l_echeance)
      REFERENCES ta_l_echeance (id_l_echeance) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
      
--ajout du nombre de mois sur les plans
ALTER TABLE ta_stripe_plan
ADD COLUMN nb_mois integer;

--ajout de durée en mois sur la table abonnement
ALTER TABLE ta_abonnement
ADD COLUMN nb_mois_duree_engagement integer;
ALTER TABLE ta_abonnement
ADD COLUMN nb_mois_frequence_facturation integer;
ALTER TABLE ta_abonnement
ADD COLUMN nb_mois_frequence_paiement integer;

