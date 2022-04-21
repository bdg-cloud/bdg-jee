/*set schema 'demo';*/

--bon liv
ALTER TABLE ta_bonliv ADD COLUMN id_gr_mouv_stock did_facultatif;


--
ALTER TABLE ta_l_bonliv ADD COLUMN id_lot did_facultatif;
ALTER TABLE ta_l_bonliv ALTER COLUMN id_lot SET NOT NULL;
ALTER TABLE ta_l_bonliv ADD COLUMN id_entrepot did_facultatif;
ALTER TABLE ta_l_bonliv ADD COLUMN emplacement_l_document dlib255;
ALTER TABLE ta_l_bonliv ADD COLUMN id_mouvement_stock did_facultatif;
ALTER TABLE ta_l_bonliv ADD COLUMN lib_tva_l_document character varying(255);

--
ALTER TABLE ta_gr_mouv_stock ADD COLUMN id_bonliv did_facultatif;

--
INSERT INTO ta_gen_code_ex (code_gen_code, cle_gen_code, valeur_gen_code, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj) VALUES ('codeDocument', 'TaBonliv', 'BL{@num}', 'postgres', '2017-01-30 15:28:34.903030', 'postgres', '2017-01-30 15:28:34.903030', '2.0.13', '127.0.0.1/32', 0);

  
  
--boncde
ALTER TABLE ta_boncde ADD COLUMN id_gr_mouv_stock did_facultatif;


--
ALTER TABLE ta_l_boncde ADD COLUMN id_lot did_facultatif;
ALTER TABLE ta_l_boncde ADD COLUMN id_entrepot did_facultatif;
ALTER TABLE ta_l_boncde ADD COLUMN emplacement_l_document dlib255;
ALTER TABLE ta_l_boncde ADD COLUMN id_mouvement_stock did_facultatif;
ALTER TABLE ta_l_boncde ADD COLUMN lib_tva_l_document character varying(255);

--
ALTER TABLE ta_gr_mouv_stock ADD COLUMN id_boncde did_facultatif;

--
INSERT INTO ta_gen_code_ex (code_gen_code, cle_gen_code, valeur_gen_code, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj) VALUES ('codeDocument', 'TaBoncde', 'BC{@num}', 'postgres', '2017-01-30 15:28:34.903030', 'postgres', '2017-01-30 15:28:34.903030', '2.0.13', '127.0.0.1/32', 0);


  --avoir
ALTER TABLE ta_avoir ADD COLUMN id_gr_mouv_stock did_facultatif;


--
ALTER TABLE ta_l_avoir ADD COLUMN id_lot did_facultatif;
ALTER TABLE ta_l_avoir ALTER COLUMN id_lot SET NOT NULL;
ALTER TABLE ta_l_avoir ADD COLUMN id_entrepot did_facultatif;
ALTER TABLE ta_l_avoir ADD COLUMN emplacement_l_document dlib255;
ALTER TABLE ta_l_avoir ADD COLUMN id_mouvement_stock did_facultatif;
ALTER TABLE ta_l_avoir ADD COLUMN lib_tva_l_document character varying(255);

--
ALTER TABLE ta_gr_mouv_stock ADD COLUMN id_avoir did_facultatif;

--
INSERT INTO ta_gen_code_ex (code_gen_code, cle_gen_code, valeur_gen_code, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj) 
VALUES ('codeDocument', 'TaAvoir', 'AV{@num}', 'postgres', '2017-01-30 15:28:34.903030', 'postgres', '2017-01-30 15:28:34.903030', '2.0.13', '127.0.0.1/32', 0);


  --acompte
ALTER TABLE ta_acompte ADD COLUMN id_gr_mouv_stock did_facultatif;


--
ALTER TABLE ta_l_acompte ADD COLUMN id_lot did_facultatif;
ALTER TABLE ta_l_acompte ADD COLUMN id_entrepot did_facultatif;
ALTER TABLE ta_l_acompte ADD COLUMN emplacement_l_document dlib255;
ALTER TABLE ta_l_acompte ADD COLUMN id_mouvement_stock did_facultatif;
ALTER TABLE ta_l_acompte ADD COLUMN lib_tva_l_document character varying(255);

--
ALTER TABLE ta_gr_mouv_stock ADD COLUMN id_acompte did_facultatif;

--
INSERT INTO ta_gen_code_ex (code_gen_code, cle_gen_code, valeur_gen_code, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj) VALUES ('codeDocument', 'TaAcompte', 'AC{@num}', 'postgres', '2017-01-30 15:28:34.903030', 'postgres', '2017-01-30 15:28:34.903030', '2.0.13', '127.0.0.1/32', 0);

  --proforma
ALTER TABLE ta_proforma ADD COLUMN id_gr_mouv_stock did_facultatif;


--
ALTER TABLE ta_l_proforma ADD COLUMN id_lot did_facultatif;
ALTER TABLE ta_l_proforma ADD COLUMN id_entrepot did_facultatif;
ALTER TABLE ta_l_proforma ADD COLUMN emplacement_l_document dlib255;
ALTER TABLE ta_l_proforma ADD COLUMN id_mouvement_stock did_facultatif;
ALTER TABLE ta_l_proforma ADD COLUMN lib_tva_l_document character varying(255);

--
ALTER TABLE ta_gr_mouv_stock ADD COLUMN id_proforma did_facultatif;

--
INSERT INTO ta_gen_code_ex (code_gen_code, cle_gen_code, valeur_gen_code, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj) VALUES ('codeDocument', 'TaProforma', 'PR{@num}', 'postgres', '2017-01-30 15:28:34.903030', 'postgres', '2017-01-30 15:28:34.903030', '2.0.13', '127.0.0.1/32', 0);


--apporteur
ALTER TABLE ta_apporteur ADD COLUMN id_gr_mouv_stock did_facultatif;


--
ALTER TABLE ta_l_apporteur ADD COLUMN id_lot did_facultatif;
ALTER TABLE ta_l_apporteur ALTER COLUMN id_lot SET NOT NULL;
ALTER TABLE ta_l_apporteur ADD COLUMN id_entrepot did_facultatif;
ALTER TABLE ta_l_apporteur ADD COLUMN emplacement_l_document dlib255;
ALTER TABLE ta_l_apporteur ADD COLUMN id_mouvement_stock did_facultatif;
ALTER TABLE ta_l_apporteur ADD COLUMN lib_tva_l_document character varying(255);

--
ALTER TABLE ta_gr_mouv_stock ADD COLUMN id_apporteur did_facultatif;

--
INSERT INTO ta_gen_code_ex (code_gen_code, cle_gen_code, valeur_gen_code, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj) 
VALUES ('codeDocument', 'TaApporteur', 'AP{@num}', 'postgres', '2017-01-30 15:28:34.903030', 'postgres', '2017-01-30 15:28:34.903030', '2.0.13', '127.0.0.1/32', 0);


--prelevement
ALTER TABLE ta_prelevement ADD COLUMN id_gr_mouv_stock did_facultatif;


--
ALTER TABLE ta_l_prelevement ADD COLUMN id_lot did_facultatif;
ALTER TABLE ta_l_prelevement ADD COLUMN id_entrepot did_facultatif;
ALTER TABLE ta_l_prelevement ADD COLUMN emplacement_l_document dlib255;
ALTER TABLE ta_l_prelevement ADD COLUMN id_mouvement_stock did_facultatif;
ALTER TABLE ta_l_prelevement ADD COLUMN lib_tva_l_document character varying(255);

--
ALTER TABLE ta_gr_mouv_stock ADD COLUMN id_prelevement did_facultatif;

--
INSERT INTO ta_gen_code_ex (code_gen_code, cle_gen_code, valeur_gen_code, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj) 
VALUES ('codeDocument', 'TaPrelevement', 'PL{@num}', 'postgres', '2017-01-30 15:28:34.903030', 'postgres', '2017-01-30 15:28:34.903030', '2.0.13', '127.0.0.1/32', 0);



--
ALTER TABLE ta_gr_mouv_stock
  DROP CONSTRAINT doc_not_nul;
ALTER TABLE ta_gr_mouv_stock
  ADD CONSTRAINT doc_not_nul
  CHECK (id_fabrication is not null or id_bon_reception is not null or id_inventaire is not null or id_facture is not null or id_bonliv is not null 
   or id_boncde is not null  or id_avoir is not null or id_apporteur is not null or id_prelevement is not null or id_acompte is not null or id_proforma is not null or manuel is not null);

  

drop table   ta_preferences;
CREATE TABLE ta_preferences (
    id_preferences integer NOT NULL,
    groupe character varying(100) ,
    cle character varying(100) NOT NULL,
    valeur character varying(100) DEFAULT NULL::character varying,
    valeur_defaut character varying(100) DEFAULT NULL::character varying,
    type_donnees character varying(100) NOT NULL,
    libelle character varying(100) NOT NULL,
    qui_cree dlib50,
    quand_cree date_heure_lgr,
    qui_modif dlib50,
    quand_modif date_heure_lgr,
    version num_version,
    ip_acces dlib50,
    version_obj did_version_obj,
    valeurs_possibles character varying(255)
);
ALTER TABLE ONLY ta_preferences
    ADD CONSTRAINT pk_ta_preferences PRIMARY KEY (id_preferences);
CREATE SEQUENCE ta_preferences_id_preferences_seq    START WITH 1    INCREMENT BY 1    NO MINVALUE    NO MAXVALUE     CACHE 1;
ALTER TABLE ONLY ta_preferences ALTER COLUMN id_preferences SET DEFAULT nextval('ta_preferences_id_preferences_seq'::regclass);

ALTER TABLE ONLY ta_preferences
    ADD CONSTRAINT ta_preferences_cle_valeur_key UNIQUE (cle, valeur,groupe);
CREATE TRIGGER tbi_preferences BEFORE INSERT ON ta_preferences FOR EACH ROW EXECUTE PROCEDURE before_insert();
CREATE TRIGGER tbu_preferences BEFORE UPDATE ON ta_preferences FOR EACH ROW EXECUTE PROCEDURE before_update();

SELECT setval('ta_preferences_id_preferences_seq', 1, true);
   

INSERT INTO ta_preferences ( cle, valeur, valeur_defaut, type_donnees, libelle,   version_obj, valeurs_possibles, groupe) VALUES ( 'code_barre_prefixe_entreprise', 'ae', NULL, 'string', 'Préfixe entreprise code barre',  0, NULL, NULL);
INSERT INTO ta_preferences ( cle, valeur, valeur_defaut, type_donnees, libelle,  version_obj, valeurs_possibles, groupe) VALUES ( 'code_barre_debut_plage', 'rr', NULL, 'string', 'Code barre début plage',  0, NULL, NULL);
INSERT INTO ta_preferences ( cle, valeur, valeur_defaut, type_donnees, libelle,   version_obj, valeurs_possibles, groupe) VALUES ( 'code_barre_fin_plage', '4', NULL, 'numeric', 'Code barre fin plage',  0, NULL, NULL);
INSERT INTO ta_preferences ( cle, valeur, valeur_defaut, type_donnees, libelle,   version_obj, valeurs_possibles, groupe) VALUES ( 'code_t_paiement_defaut', 'C', NULL, 'string', 'Code type paiement defaut',  0, NULL, NULL);

/*facture*/
INSERT INTO ta_preferences ( cle, valeur, valeur_defaut, type_donnees, libelle,   version_obj, valeurs_possibles, groupe) VALUES ( 'Commentaire', 'Commentaire facture par défaut', NULL, 'text', 'Commentaire par défaut Facture', 0, NULL, 'facture');
INSERT INTO ta_preferences ( cle, valeur, valeur_defaut, type_donnees, libelle,   version_obj, valeurs_possibles, groupe) VALUES ( 'PageBreakMaxi', '30', NULL, 'numeric', 'Page break Maxi',   0, NULL, 'facture');
INSERT INTO ta_preferences ( cle, valeur, valeur_defaut, type_donnees, libelle,  version_obj, valeurs_possibles, groupe) VALUES ( 'PageBreakTotaux', '23', NULL, 'numeric', 'Page break Totaux',   0, NULL, 'facture');
INSERT INTO ta_preferences ( cle, valeur, valeur_defaut, type_donnees, libelle,  version_obj, valeurs_possibles, groupe) VALUES ( 'CoupureLigne', '54', NULL, 'numeric', 'Coupure de ligne',   0, NULL, 'facture');
INSERT INTO ta_preferences ( cle, valeur, valeur_defaut, type_donnees, libelle,  version_obj, valeurs_possibles, groupe) VALUES ( 'ListeChoix', 'choix 1', NULL, 'string', 'listeChoix',   0, NULL, 'facture');
INSERT INTO ta_preferences ( cle, valeur, valeur_defaut, type_donnees, libelle,  version_obj, valeurs_possibles, groupe) VALUES ( 'Traite', 'true', NULL, 'boolean', 'Traite', 0, NULL, 'facture');
INSERT INTO ta_preferences ( cle, valeur, valeur_defaut, type_donnees, libelle,  version_obj, valeurs_possibles, groupe) VALUES ( 'ImpressionDirect', 'false', NULL, 'boolean', 'Impression direct',  0, NULL, 'facture');

/*bl*/
INSERT INTO ta_preferences ( cle, valeur, valeur_defaut, type_donnees, libelle,   version_obj, valeurs_possibles, groupe) VALUES ( 'Commentaire', 'Commentaire facture par défaut', NULL, 'text', 'Commentaire par défaut Facture', 0, NULL, 'bonliv');
INSERT INTO ta_preferences ( cle, valeur, valeur_defaut, type_donnees, libelle,   version_obj, valeurs_possibles, groupe) VALUES ( 'PageBreakMaxi', '36', NULL, 'numeric', 'Page break Maxi',   0, NULL, 'bonliv');
INSERT INTO ta_preferences ( cle, valeur, valeur_defaut, type_donnees, libelle,  version_obj, valeurs_possibles, groupe) VALUES ( 'PageBreakTotaux', '23', NULL, 'numeric', 'Page break Totaux',   0, NULL, 'bonliv');
INSERT INTO ta_preferences ( cle, valeur, valeur_defaut, type_donnees, libelle,  version_obj, valeurs_possibles, groupe) VALUES ( 'CoupureLigne', '54', NULL, 'numeric', 'Coupure de ligne',   0, NULL, 'bonliv');
INSERT INTO ta_preferences ( cle, valeur, valeur_defaut, type_donnees, libelle,  version_obj, valeurs_possibles, groupe) VALUES ( 'ListeChoix', 'choix 1', NULL, 'string', 'listeChoix',   0, NULL, 'bonliv');
INSERT INTO ta_preferences ( cle, valeur, valeur_defaut, type_donnees, libelle,  version_obj, valeurs_possibles, groupe) VALUES ( 'Traite', 'true', NULL, 'boolean', 'Traite', 0, NULL, 'bonliv');
INSERT INTO ta_preferences ( cle, valeur, valeur_defaut, type_donnees, libelle,  version_obj, valeurs_possibles, groupe) VALUES ( 'ImpressionDirect', 'false', NULL, 'boolean', 'Impression direct',  0, NULL, 'bonliv');

/*acompte*/
INSERT INTO ta_preferences ( cle, valeur, valeur_defaut, type_donnees, libelle,   version_obj, valeurs_possibles, groupe) VALUES ( 'Commentaire', 'Commentaire facture par défaut', NULL, 'text', 'Commentaire par défaut Facture', 0, NULL, 'acompte');
INSERT INTO ta_preferences ( cle, valeur, valeur_defaut, type_donnees, libelle,   version_obj, valeurs_possibles, groupe) VALUES ( 'PageBreakMaxi', '36', NULL, 'numeric', 'Page break Maxi',   0, NULL, 'acompte');
INSERT INTO ta_preferences ( cle, valeur, valeur_defaut, type_donnees, libelle,  version_obj, valeurs_possibles, groupe) VALUES ( 'PageBreakTotaux', '23', NULL, 'numeric', 'Page break Totaux',   0, NULL, 'acompte');
INSERT INTO ta_preferences ( cle, valeur, valeur_defaut, type_donnees, libelle,  version_obj, valeurs_possibles, groupe) VALUES ( 'CoupureLigne', '54', NULL, 'numeric', 'Coupure de ligne',   0, NULL, 'acompte');
INSERT INTO ta_preferences ( cle, valeur, valeur_defaut, type_donnees, libelle,  version_obj, valeurs_possibles, groupe) VALUES ( 'ListeChoix', 'choix 1', NULL, 'string', 'listeChoix',   0, NULL, 'acompte');
INSERT INTO ta_preferences ( cle, valeur, valeur_defaut, type_donnees, libelle,  version_obj, valeurs_possibles, groupe) VALUES ( 'Traite', 'true', NULL, 'boolean', 'Traite', 0, NULL, 'acompte');
INSERT INTO ta_preferences ( cle, valeur, valeur_defaut, type_donnees, libelle,  version_obj, valeurs_possibles, groupe) VALUES ( 'ImpressionDirect', 'false', NULL, 'boolean', 'Impression direct',  0, NULL, 'acompte');
  
/*avoir*/
INSERT INTO ta_preferences ( cle, valeur, valeur_defaut, type_donnees, libelle,   version_obj, valeurs_possibles, groupe) VALUES ( 'Commentaire', 'Commentaire facture par défaut', NULL, 'text', 'Commentaire par défaut Facture', 0, NULL, 'avoir');
INSERT INTO ta_preferences ( cle, valeur, valeur_defaut, type_donnees, libelle,   version_obj, valeurs_possibles, groupe) VALUES ( 'PageBreakMaxi', '36', NULL, 'numeric', 'Page break Maxi',   0, NULL, 'avoir');
INSERT INTO ta_preferences ( cle, valeur, valeur_defaut, type_donnees, libelle,  version_obj, valeurs_possibles, groupe) VALUES ( 'PageBreakTotaux', '23', NULL, 'numeric', 'Page break Totaux',   0, NULL, 'avoir');
INSERT INTO ta_preferences ( cle, valeur, valeur_defaut, type_donnees, libelle,  version_obj, valeurs_possibles, groupe) VALUES ( 'CoupureLigne', '54', NULL, 'numeric', 'Coupure de ligne',   0, NULL, 'avoir');
INSERT INTO ta_preferences ( cle, valeur, valeur_defaut, type_donnees, libelle,  version_obj, valeurs_possibles, groupe) VALUES ( 'ListeChoix', 'choix 1', NULL, 'string', 'listeChoix',   0, NULL, 'avoir');
INSERT INTO ta_preferences ( cle, valeur, valeur_defaut, type_donnees, libelle,  version_obj, valeurs_possibles, groupe) VALUES ( 'Traite', 'true', NULL, 'boolean', 'Traite', 0, NULL, 'avoir');
INSERT INTO ta_preferences ( cle, valeur, valeur_defaut, type_donnees, libelle,  version_obj, valeurs_possibles, groupe) VALUES ( 'ImpressionDirect', 'false', NULL, 'boolean', 'Impression direct',  0, NULL, 'avoir');
  
/*devis*/
INSERT INTO ta_preferences ( cle, valeur, valeur_defaut, type_donnees, libelle,   version_obj, valeurs_possibles, groupe) VALUES ( 'Commentaire', 'Commentaire facture par défaut', NULL, 'text', 'Commentaire par défaut Facture', 0, NULL, 'devis');
INSERT INTO ta_preferences ( cle, valeur, valeur_defaut, type_donnees, libelle,   version_obj, valeurs_possibles, groupe) VALUES ( 'PageBreakMaxi', '36', NULL, 'numeric', 'Page break Maxi',   0, NULL, 'devis');
INSERT INTO ta_preferences ( cle, valeur, valeur_defaut, type_donnees, libelle,  version_obj, valeurs_possibles, groupe) VALUES ( 'PageBreakTotaux', '23', NULL, 'numeric', 'Page break Totaux',   0, NULL, 'devis');
INSERT INTO ta_preferences ( cle, valeur, valeur_defaut, type_donnees, libelle,  version_obj, valeurs_possibles, groupe) VALUES ( 'CoupureLigne', '54', NULL, 'numeric', 'Coupure de ligne',   0, NULL, 'devis');
INSERT INTO ta_preferences ( cle, valeur, valeur_defaut, type_donnees, libelle,  version_obj, valeurs_possibles, groupe) VALUES ( 'ListeChoix', 'choix 1', NULL, 'string', 'listeChoix',   0, NULL, 'devis');
INSERT INTO ta_preferences ( cle, valeur, valeur_defaut, type_donnees, libelle,  version_obj, valeurs_possibles, groupe) VALUES ( 'Traite', 'true', NULL, 'boolean', 'Traite', 0, NULL, 'devis');
INSERT INTO ta_preferences ( cle, valeur, valeur_defaut, type_donnees, libelle,  version_obj, valeurs_possibles, groupe) VALUES ( 'ImpressionDirect', 'false', NULL, 'boolean', 'Impression direct',  0, NULL, 'devis');
  
/*boncde*/
INSERT INTO ta_preferences ( cle, valeur, valeur_defaut, type_donnees, libelle,   version_obj, valeurs_possibles, groupe) VALUES ( 'Commentaire', 'Commentaire facture par défaut', NULL, 'text', 'Commentaire par défaut Facture', 0, NULL, 'boncde');
INSERT INTO ta_preferences ( cle, valeur, valeur_defaut, type_donnees, libelle,   version_obj, valeurs_possibles, groupe) VALUES ( 'PageBreakMaxi', '36', NULL, 'numeric', 'Page break Maxi',   0, NULL, 'boncde');
INSERT INTO ta_preferences ( cle, valeur, valeur_defaut, type_donnees, libelle,  version_obj, valeurs_possibles, groupe) VALUES ( 'PageBreakTotaux', '23', NULL, 'numeric', 'Page break Totaux',   0, NULL, 'boncde');
INSERT INTO ta_preferences ( cle, valeur, valeur_defaut, type_donnees, libelle,  version_obj, valeurs_possibles, groupe) VALUES ( 'CoupureLigne', '54', NULL, 'numeric', 'Coupure de ligne',   0, NULL, 'boncde');
INSERT INTO ta_preferences ( cle, valeur, valeur_defaut, type_donnees, libelle,  version_obj, valeurs_possibles, groupe) VALUES ( 'ListeChoix', 'choix 1', NULL, 'string', 'listeChoix',   0, NULL, 'boncde');
INSERT INTO ta_preferences ( cle, valeur, valeur_defaut, type_donnees, libelle,  version_obj, valeurs_possibles, groupe) VALUES ( 'Traite', 'true', NULL, 'boolean', 'Traite', 0, NULL, 'boncde');
INSERT INTO ta_preferences ( cle, valeur, valeur_defaut, type_donnees, libelle,  version_obj, valeurs_possibles, groupe) VALUES ( 'ImpressionDirect', 'false', NULL, 'boolean', 'Impression direct',  0, NULL, 'boncde');
  
/*proforma*/
INSERT INTO ta_preferences ( cle, valeur, valeur_defaut, type_donnees, libelle,   version_obj, valeurs_possibles, groupe) VALUES ( 'Commentaire', 'Commentaire facture par défaut', NULL, 'text', 'Commentaire par défaut Facture', 0, NULL, 'proforma');
INSERT INTO ta_preferences ( cle, valeur, valeur_defaut, type_donnees, libelle,   version_obj, valeurs_possibles, groupe) VALUES ( 'PageBreakMaxi', '36', NULL, 'numeric', 'Page break Maxi',   0, NULL, 'proforma');
INSERT INTO ta_preferences ( cle, valeur, valeur_defaut, type_donnees, libelle,  version_obj, valeurs_possibles, groupe) VALUES ( 'PageBreakTotaux', '23', NULL, 'numeric', 'Page break Totaux',   0, NULL, 'proforma');
INSERT INTO ta_preferences ( cle, valeur, valeur_defaut, type_donnees, libelle,  version_obj, valeurs_possibles, groupe) VALUES ( 'CoupureLigne', '54', NULL, 'numeric', 'Coupure de ligne',   0, NULL, 'proforma');
INSERT INTO ta_preferences ( cle, valeur, valeur_defaut, type_donnees, libelle,  version_obj, valeurs_possibles, groupe) VALUES ( 'ListeChoix', 'choix 1', NULL, 'string', 'listeChoix',   0, NULL, 'proforma');
INSERT INTO ta_preferences ( cle, valeur, valeur_defaut, type_donnees, libelle,  version_obj, valeurs_possibles, groupe) VALUES ( 'Traite', 'true', NULL, 'boolean', 'Traite', 0, NULL, 'proforma');
INSERT INTO ta_preferences ( cle, valeur, valeur_defaut, type_donnees, libelle,  version_obj, valeurs_possibles, groupe) VALUES ( 'ImpressionDirect', 'false', NULL, 'boolean', 'Impression direct',  0, NULL, 'proforma');
  
/*prelevement*/
INSERT INTO ta_preferences ( cle, valeur, valeur_defaut, type_donnees, libelle,   version_obj, valeurs_possibles, groupe) VALUES ( 'Commentaire', 'Commentaire facture par défaut', NULL, 'text', 'Commentaire par défaut Facture', 0, NULL, 'prelevement');
INSERT INTO ta_preferences ( cle, valeur, valeur_defaut, type_donnees, libelle,   version_obj, valeurs_possibles, groupe) VALUES ( 'PageBreakMaxi', '36', NULL, 'numeric', 'Page break Maxi',   0, NULL, 'prelevement');
INSERT INTO ta_preferences ( cle, valeur, valeur_defaut, type_donnees, libelle,  version_obj, valeurs_possibles, groupe) VALUES ( 'PageBreakTotaux', '23', NULL, 'numeric', 'Page break Totaux',   0, NULL, 'prelevement');
INSERT INTO ta_preferences ( cle, valeur, valeur_defaut, type_donnees, libelle,  version_obj, valeurs_possibles, groupe) VALUES ( 'CoupureLigne', '54', NULL, 'numeric', 'Coupure de ligne',   0, NULL, 'prelevement');
INSERT INTO ta_preferences ( cle, valeur, valeur_defaut, type_donnees, libelle,  version_obj, valeurs_possibles, groupe) VALUES ( 'ListeChoix', 'choix 1', NULL, 'string', 'listeChoix',   0, NULL, 'prelevement');
INSERT INTO ta_preferences ( cle, valeur, valeur_defaut, type_donnees, libelle,  version_obj, valeurs_possibles, groupe) VALUES ( 'Traite', 'true', NULL, 'boolean', 'Traite', 0, NULL, 'prelevement');
INSERT INTO ta_preferences ( cle, valeur, valeur_defaut, type_donnees, libelle,  version_obj, valeurs_possibles, groupe) VALUES ( 'ImpressionDirect', 'false', NULL, 'boolean', 'Impression direct',  0, NULL, 'prelevement');
  
/*apporteur*/
INSERT INTO ta_preferences ( cle, valeur, valeur_defaut, type_donnees, libelle,   version_obj, valeurs_possibles, groupe) VALUES ( 'Commentaire', 'Commentaire facture par défaut', NULL, 'text', 'Commentaire par défaut Facture', 0, NULL, 'apporteur');
INSERT INTO ta_preferences ( cle, valeur, valeur_defaut, type_donnees, libelle,   version_obj, valeurs_possibles, groupe) VALUES ( 'PageBreakMaxi', '36', NULL, 'numeric', 'Page break Maxi',   0, NULL, 'apporteur');
INSERT INTO ta_preferences ( cle, valeur, valeur_defaut, type_donnees, libelle,  version_obj, valeurs_possibles, groupe) VALUES ( 'PageBreakTotaux', '23', NULL, 'numeric', 'Page break Totaux',   0, NULL, 'apporteur');
INSERT INTO ta_preferences ( cle, valeur, valeur_defaut, type_donnees, libelle,  version_obj, valeurs_possibles, groupe) VALUES ( 'CoupureLigne', '54', NULL, 'numeric', 'Coupure de ligne',   0, NULL, 'apporteur');
INSERT INTO ta_preferences ( cle, valeur, valeur_defaut, type_donnees, libelle,  version_obj, valeurs_possibles, groupe) VALUES ( 'ListeChoix', 'choix 1', NULL, 'string', 'listeChoix',   0, NULL, 'apporteur');
INSERT INTO ta_preferences ( cle, valeur, valeur_defaut, type_donnees, libelle,  version_obj, valeurs_possibles, groupe) VALUES ( 'Traite', 'true', NULL, 'boolean', 'Traite', 0, NULL, 'apporteur');
INSERT INTO ta_preferences ( cle, valeur, valeur_defaut, type_donnees, libelle,  version_obj, valeurs_possibles, groupe) VALUES ( 'ImpressionDirect', 'false', NULL, 'boolean', 'Impression direct',  0, NULL, 'apporteur');
  
/*exportation*/
INSERT INTO ta_preferences ( cle, valeur, valeur_defaut, type_donnees, libelle,  version_obj, valeurs_possibles, groupe) VALUES ( 'Repertoire_exportation', 'tmp/', NULL, 'string', 'répertoire d''exportation',  0, NULL, 'exportation');
INSERT INTO ta_preferences ( cle, valeur, valeur_defaut, type_donnees, libelle,  version_obj, valeurs_possibles, groupe) VALUES ( 'Acomptes', 'false', NULL, 'boolean', 'exportation des acomptes',  0, NULL, 'exportation');
INSERT INTO ta_preferences ( cle, valeur, valeur_defaut, type_donnees, libelle,  version_obj, valeurs_possibles, groupe) VALUES ( 'Remise', 'false', NULL, 'boolean', 'Exportation des remises',  0, NULL, 'exportation');
INSERT INTO ta_preferences ( cle, valeur, valeur_defaut, type_donnees, libelle,  version_obj, valeurs_possibles, groupe) VALUES ( 'Reglement_simple', 'false', NULL, 'boolean', 'Exportation Réglement simple',  0, NULL, 'exportation');
INSERT INTO ta_preferences ( cle, valeur, valeur_defaut, type_donnees, libelle,  version_obj, valeurs_possibles, groupe) VALUES ( 'Transfert_reglements_lies_au_document', 'false', NULL, 'boolean', 'Transfert des règlement liés au document',  0, NULL, 'exportation');
INSERT INTO ta_preferences ( cle, valeur, valeur_defaut, type_donnees, libelle,  version_obj, valeurs_possibles, groupe) VALUES ( 'Transfert_pointages', 'false', NULL, 'boolean', 'Transfert des pointages',  0, NULL, 'exportation');
INSERT INTO ta_preferences ( cle, valeur, valeur_defaut, type_donnees, libelle,  version_obj, valeurs_possibles, groupe) VALUES ( 'Transfert_documents_lies_au_reglement', 'false', NULL, 'boolean', 'Transfert des documents liés au réglement',  0, NULL, 'exportation');

