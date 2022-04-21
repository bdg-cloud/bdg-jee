CREATE TABLE public.ta_type_partenaire
(
  id_type_partenaire serial NOT NULL,
  code dlib20,
  libelle dlib255,
  ip_acces dlib50,
  quand_cree date_heure_lgr,
  quand_modif date_heure_lgr,
  qui_cree dlib50,
  qui_modif dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_type_partenaire_pkey PRIMARY KEY (id_type_partenaire)
)
WITH (
  OIDS=FALSE
);


INSERT INTO public.ta_type_partenaire (id_type_partenaire, code, libelle, ip_acces, quand_cree, quand_modif, qui_cree, qui_modif, version_obj) VALUES (1, 'PREVPRE', 'Revendeur / Prescripteur', null, '2016-12-07 12:32:14.298139', '2016-12-07 12:32:14.298139', null, null, 0);
INSERT INTO public.ta_type_partenaire (id_type_partenaire, code, libelle, ip_acces, quand_cree, quand_modif, qui_cree, qui_modif, version_obj) VALUES (2, 'PPARRAIN', 'Parrainage', null, '2016-12-07 12:33:03.633962', '2016-12-07 12:33:03.633962', null, null, 0);
INSERT INTO public.ta_type_partenaire (id_type_partenaire, code, libelle, ip_acces, quand_cree, quand_modif, qui_cree, qui_modif, version_obj) VALUES (3, 'PGROUPEMENT', 'Groupement', null, '2016-12-07 12:33:32.233783', '2016-12-07 12:33:32.233783', null, null, 0);
INSERT INTO public.ta_type_partenaire (id_type_partenaire, code, libelle, ip_acces, quand_cree, quand_modif, qui_cree, qui_modif, version_obj) VALUES (4, 'PSALARIE', 'Salarié', null, '2016-12-07 14:14:17.676343', '2016-12-07 14:14:17.676343', null, null, 0);


ALTER TABLE public.ta_client
   ADD COLUMN num_tva public.dlib100;


CREATE TABLE public.ta_cg_partenaire
(
  id serial NOT NULL,
  cg_partenaire text,
  date_cg_partenaire date_heure_lgr,
  actif boolean,
  ip_acces dlib50,
  quand_cree date_heure_lgr,
  quand_modif date_heure_lgr,
  qui_cree dlib50,
  qui_modif dlib50,
  version_obj did_version_obj,
  url dlib255,
  blob_fichier bytea,
  CONSTRAINT ta_cg_partenaire_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);

CREATE TABLE public.ta_partenaire
(
  id serial NOT NULL,
  code_revendeur public.dlib50,
  id_type_partenaire public.did_facultatif,
  date_debut date_heure_lgr,
  actif boolean,
  titulaire_compte_banque dlib100,
  nom_banque dlib100,
  adresse1_banque dlib100,
  adresse2_banque dlib100,
  cp_banque dcodpos,
  ville_banque dlib100,
  iban_banque dlib100,
  bic_swift_banque dlgr_codel,
  id_cg_partenaire did_facultatif,
  ip_acces dlib50,
  quand_cree date_heure_lgr,
  quand_modif date_heure_lgr,
  qui_cree dlib50,
  qui_modif dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_partenaire_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);

ALTER TABLE public.ta_partenaire
  ADD CONSTRAINT fk_ta_partenaire_id_type_partenaire FOREIGN KEY (id_type_partenaire) REFERENCES public.ta_type_partenaire (id_type_partenaire)
   ON UPDATE NO ACTION ON DELETE NO ACTION;
CREATE INDEX fki_ta_partenaire_id_type_partenaire
  ON public.ta_partenaire(id_type_partenaire);

ALTER TABLE public.ta_partenaire
  ADD CONSTRAINT fk_ta_partenaire_id_cg_partenaire FOREIGN KEY (id_cg_partenaire) REFERENCES public.ta_cg_partenaire (id)
   ON UPDATE NO ACTION ON DELETE NO ACTION;
CREATE INDEX fki_ta_partenaire_id_cg_partenaire
  ON public.ta_partenaire(id_cg_partenaire);


ALTER TABLE public.ta_client
   ADD COLUMN id_ta_partenaire public.did_facultatif;


ALTER TABLE public.ta_client
  ADD CONSTRAINT fk_ta_client_id_ta_partenaire FOREIGN KEY (id_ta_partenaire) REFERENCES public.ta_partenaire (id)
   ON UPDATE NO ACTION ON DELETE NO ACTION;
CREATE INDEX fki_ta_client_id_ta_partenaire
  ON public.ta_client(id_ta_partenaire);



ALTER TABLE public.ta_client ADD COLUMN titulaire_compte_banque public.dlib100;
ALTER TABLE public.ta_client ADD COLUMN nom_banque public.dlib100;
ALTER TABLE public.ta_client ADD COLUMN adresse1_banque public.dlib100;
ALTER TABLE public.ta_client ADD COLUMN adresse2_banque public.dlib100;
ALTER TABLE public.ta_client ADD COLUMN cp_banque public.dcodpos;
ALTER TABLE public.ta_client ADD COLUMN ville_banque public.dlib100;
ALTER TABLE public.ta_client ADD COLUMN iban_banque public.dlib100;
ALTER TABLE public.ta_client ADD COLUMN bic_swift_banque public.dlgr_codel;

CREATE TABLE ta_commissions
(
	id_commission serial NOT NULL,
	id_partenaire did_facultatif,
	id_panier did_facultatif,
	montant_reference did9facult,
	pourcentage_commission did9facult,
	montant_commission did9facult,
	commission_payee boolean default 'false',
	code_document_associe dlib50,
	date_paiement_commission date_heure_lgr,
	commentaire_legrain dlib_commentaire,
	commentaire_partenaire dlib_commentaire,
	ip_acces dlib50,
	quand_cree date_heure_lgr,
	quand_modif date_heure_lgr,
	qui_cree dlib50,
	qui_modif dlib50,
	version_obj did_version_obj,
	CONSTRAINT ta_commission_pkey PRIMARY KEY (id_commission)
)
WITH (
  OIDS=FALSE
);


CREATE TABLE ta_l_commission
(
	id_l_commission serial NOT NULL,
	id_commission did_facultatif NOT NULL,
	id_l_panier did_facultatif,
	id_produit_achete did_facultatif,
	montant_reference did9facult,
	pourcentage_commission did9facult,
	montant_commission did9facult,
	commentaire_legrain dlib_commentaire,
	commentaire_partenaire dlib_commentaire,
	ip_acces dlib50,
	quand_cree date_heure_lgr,
	quand_modif date_heure_lgr,
	qui_cree dlib50,
	qui_modif dlib50,
	version_obj did_version_obj,
	CONSTRAINT ta_l_commission_pkey PRIMARY KEY (id_l_commission)
)
WITH (
  OIDS=FALSE
);

CREATE TRIGGER tbi_panier
  BEFORE INSERT
  ON public.ta_panier
  FOR EACH ROW
  EXECUTE PROCEDURE public.before_insert();

CREATE TRIGGER tbu_panier
  BEFORE INSERT
  ON public.ta_panier
  FOR EACH ROW
  EXECUTE PROCEDURE public.before_update();

CREATE TRIGGER tbi_dossier
  BEFORE INSERT
  ON public.ta_dossier
  FOR EACH ROW
  EXECUTE PROCEDURE public.before_insert();

CREATE TRIGGER tbu_dossier
  BEFORE INSERT
  ON public.ta_dossier
  FOR EACH ROW
  EXECUTE PROCEDURE public.before_update();

  CREATE TRIGGER tbi_partenaire
  BEFORE INSERT
  ON public.ta_partenaire
  FOR EACH ROW
  EXECUTE PROCEDURE public.before_insert();

CREATE TRIGGER tbu_partenaire
  BEFORE INSERT
  ON public.ta_partenaire
  FOR EACH ROW
  EXECUTE PROCEDURE public.before_update();

    CREATE TRIGGER tbi_commissions
  BEFORE INSERT
  ON public.ta_commissions
  FOR EACH ROW
  EXECUTE PROCEDURE public.before_insert();

CREATE TRIGGER tbu_commissions
  BEFORE INSERT
  ON public.ta_commissions
  FOR EACH ROW
  EXECUTE PROCEDURE public.before_update();

CREATE TRIGGER tbi_produit
  BEFORE INSERT
  ON public.ta_produit
  FOR EACH ROW
  EXECUTE PROCEDURE public.before_insert();

CREATE TRIGGER tbu_produit
  BEFORE INSERT
  ON public.ta_produit
  FOR EACH ROW
  EXECUTE PROCEDURE public.before_update();
  
CREATE TABLE public.ta_type_paiement
(
  id_type_paiement serial NOT NULL,
  code dlib20,
  libelle dlib255,
  ip_acces dlib50,
  quand_cree date_heure_lgr,
  quand_modif date_heure_lgr,
  qui_cree dlib50,
  qui_modif dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_type_paiement_pkey PRIMARY KEY (id_type_paiement)
)
WITH (
  OIDS=FALSE
);

ALTER TABLE ta_panier DROP COLUMN type_paiement;

ALTER TABLE public.ta_panier
   ADD COLUMN id_type_paiement public.did_facultatif;
   
INSERT INTO public.ta_type_paiement (id_type_paiement, code, libelle, ip_acces, quand_cree, quand_modif, qui_cree, qui_modif, version_obj) VALUES (1, 'CB', 'Carte bancaire', null, '2016-12-15 15:14:45.777132', '2016-12-15 15:14:45.777132', null, null, 0);
INSERT INTO public.ta_type_paiement (id_type_paiement, code, libelle, ip_acces, quand_cree, quand_modif, qui_cree, qui_modif, version_obj) VALUES (2, 'C', 'Chèque', null, '2016-12-15 15:15:03.573729', '2016-12-15 15:15:03.573729', null, null, 0);
INSERT INTO public.ta_type_paiement (id_type_paiement, code, libelle, ip_acces, quand_cree, quand_modif, qui_cree, qui_modif, version_obj) VALUES (3, 'PREL', 'Prélèvement', null, '2016-12-15 15:15:41.706480', '2016-12-15 15:15:41.706480', null, null, 0);
INSERT INTO public.ta_type_paiement (id_type_paiement, code, libelle, ip_acces, quand_cree, quand_modif, qui_cree, qui_modif, version_obj) VALUES (4, 'CB_PREL', 'Abonnement Carte bancaire', null, '2016-12-15 15:16:20.431064', '2016-12-15 15:16:20.431064', null, null, 0);
INSERT INTO public.ta_type_paiement (id_type_paiement, code, libelle, ip_acces, quand_cree, quand_modif, qui_cree, qui_modif, version_obj) VALUES (5, 'VIR', 'Virement', null, '2016-12-15 17:09:57.703206', '2016-12-15 17:09:57.703206', null, null, 0);

ALTER TABLE public.ta_panier
   ADD COLUMN commentaire_legrain public.dlib_commentaire;

ALTER TABLE public.ta_panier
   ADD COLUMN commentaire_client public.dlib_commentaire;
   
ALTER TABLE public.ta_produit
   ADD COLUMN eligible_commission boolean DEFAULT true;
   
ALTER TABLE public.ta_panier
   ADD COLUMN nb_mois_recur_paiement public.did4;






