--
-- PostgreSQL database dump
--

-- Dumped from database version 9.4.0
-- Dumped by pg_dump version 9.4.0
-- Started on 2015-07-27 09:22:00

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

SET search_path = demo, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 289 (class 1259 OID 228052)
-- Name: ta_groupe; Type: TABLE; Schema: demo; Owner: postgres; Tablespace: 
--

CREATE TABLE ta_groupe (
    id_groupe integer NOT NULL,
    code_groupe character varying(20),
    libelle character varying(100),
    qui_cree character varying(50),
    quand_cree timestamp with time zone DEFAULT now(),
    qui_modif character varying(50),
    quand_modif timestamp with time zone DEFAULT now(),
    version character varying(20),
    ip_acces character varying(50),
    version_obj integer DEFAULT 0
);


ALTER TABLE ta_groupe OWNER TO postgres;

--
-- TOC entry 292 (class 1259 OID 228068)
-- Name: ta_groupe_id_groupe_seq; Type: SEQUENCE; Schema: demo; Owner: postgres
--

CREATE SEQUENCE ta_groupe_id_groupe_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE ta_groupe_id_groupe_seq OWNER TO postgres;

--
-- TOC entry 5157 (class 0 OID 0)
-- Dependencies: 292
-- Name: ta_groupe_id_groupe_seq; Type: SEQUENCE OWNED BY; Schema: demo; Owner: postgres
--

ALTER SEQUENCE ta_groupe_id_groupe_seq OWNED BY ta_groupe.id_groupe;


--
-- TOC entry 371 (class 1259 OID 229380)
-- Name: ta_modele_bareme; Type: TABLE; Schema: demo; Owner: postgres; Tablespace: 
--

CREATE TABLE ta_modele_bareme (
    id_modele_bareme integer NOT NULL,
    id_modele_conformite integer,
    expression_verifiee character varying(100),
    forcage boolean DEFAULT false,
    chemin_doc character varying(255),
    action_corrective character varying(255),
    qui_cree character varying(50),
    quand_cree timestamp without time zone DEFAULT now(),
    qui_modif character varying(50) NOT NULL,
    quand_modif timestamp without time zone DEFAULT now(),
    version character varying(20),
    ip_acces character varying(50) DEFAULT 0,
    version_obj integer
);


ALTER TABLE ta_modele_bareme OWNER TO postgres;

--
-- TOC entry 372 (class 1259 OID 229390)
-- Name: ta_modele_bareme_id_modele_bareme_seq; Type: SEQUENCE; Schema: demo; Owner: postgres
--

CREATE SEQUENCE ta_modele_bareme_id_modele_bareme_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE ta_modele_bareme_id_modele_bareme_seq OWNER TO postgres;

--
-- TOC entry 5158 (class 0 OID 0)
-- Dependencies: 372
-- Name: ta_modele_bareme_id_modele_bareme_seq; Type: SEQUENCE OWNED BY; Schema: demo; Owner: postgres
--

ALTER SEQUENCE ta_modele_bareme_id_modele_bareme_seq OWNED BY ta_modele_bareme.id_modele_bareme;


--
-- TOC entry 373 (class 1259 OID 229392)
-- Name: ta_modele_conformite; Type: TABLE; Schema: demo; Owner: postgres; Tablespace: 
--

CREATE TABLE ta_modele_conformite (
    id_modele_conformite integer NOT NULL,
    id_type_conformite integer,
    id_groupe integer,
    valeur_defaut character varying(100),
    deuxieme_valeur character varying(100),
    libelle_conformite character varying(100),
    valeur_calculee character varying(255),
    qui_cree character varying(50),
    quand_cree timestamp without time zone DEFAULT now(),
    qui_modif character varying(50) NOT NULL,
    quand_modif timestamp without time zone DEFAULT now(),
    version character varying(20),
    ip_acces character varying(50),
    version_obj integer DEFAULT 0,
    id_modele_groupe integer
);


ALTER TABLE ta_modele_conformite OWNER TO postgres;

--
-- TOC entry 374 (class 1259 OID 229401)
-- Name: ta_modele_conformite_id_modele_conformite_seq; Type: SEQUENCE; Schema: demo; Owner: postgres
--

CREATE SEQUENCE ta_modele_conformite_id_modele_conformite_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE ta_modele_conformite_id_modele_conformite_seq OWNER TO postgres;

--
-- TOC entry 5159 (class 0 OID 0)
-- Dependencies: 374
-- Name: ta_modele_conformite_id_modele_conformite_seq; Type: SEQUENCE OWNED BY; Schema: demo; Owner: postgres
--

ALTER SEQUENCE ta_modele_conformite_id_modele_conformite_seq OWNED BY ta_modele_conformite.id_modele_conformite;


--
-- TOC entry 5006 (class 2604 OID 238822)
-- Name: id_groupe; Type: DEFAULT; Schema: demo; Owner: postgres
--

ALTER TABLE ONLY ta_groupe ALTER COLUMN id_groupe SET DEFAULT nextval('ta_groupe_id_groupe_seq'::regclass);


--
-- TOC entry 5010 (class 2604 OID 238859)
-- Name: id_modele_bareme; Type: DEFAULT; Schema: demo; Owner: postgres
--

ALTER TABLE ONLY ta_modele_bareme ALTER COLUMN id_modele_bareme SET DEFAULT nextval('ta_modele_bareme_id_modele_bareme_seq'::regclass);


--
-- TOC entry 5015 (class 2604 OID 238860)
-- Name: id_modele_conformite; Type: DEFAULT; Schema: demo; Owner: postgres
--

ALTER TABLE ONLY ta_modele_conformite ALTER COLUMN id_modele_conformite SET DEFAULT nextval('ta_modele_conformite_id_modele_conformite_seq'::regclass);


--
-- TOC entry 5147 (class 0 OID 228052)
-- Dependencies: 289
-- Data for Name: ta_groupe; Type: TABLE DATA; Schema: demo; Owner: postgres
--

INSERT INTO ta_groupe (id_groupe, code_groupe, libelle, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj) VALUES (1, 'GRP 1', 'Contrôle à la Réception ', 'postgres', '2015-04-24 11:05:42.73825+02', 'postgres', '2015-07-22 16:53:12.661+02', '2.0.13', '127.0.0.1', 3);
INSERT INTO ta_groupe (id_groupe, code_groupe, libelle, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj) VALUES (2, 'GRP 2', 'Contrôle Fermeture Conditionnement', 'postgres', '2015-04-24 11:05:56.19112+02', 'postgres', '2015-07-22 16:53:20.172+02', '2.0.13', '127.0.0.1', 3);
INSERT INTO ta_groupe (id_groupe, code_groupe, libelle, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj) VALUES (19, 'GRP 3', 'Contrôle Traitement Thermique ', 'postgres', '2015-07-22 16:42:46.502+02', 'postgres', '2015-07-22 16:53:27.664+02', '2.0.13', '127.0.0.1', 1);
INSERT INTO ta_groupe (id_groupe, code_groupe, libelle, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj) VALUES (20, 'GRP 4', 'Contrôle Stabilite ', 'postgres', '2015-07-22 16:44:22.278+02', 'postgres', '2015-07-22 16:53:32.766+02', '2.0.13', '127.0.0.1', 1);


--
-- TOC entry 5160 (class 0 OID 0)
-- Dependencies: 292
-- Name: ta_groupe_id_groupe_seq; Type: SEQUENCE SET; Schema: demo; Owner: postgres
--

SELECT pg_catalog.setval('ta_groupe_id_groupe_seq', 20, true);


--
-- TOC entry 5149 (class 0 OID 229380)
-- Dependencies: 371
-- Data for Name: ta_modele_bareme; Type: TABLE DATA; Schema: demo; Owner: postgres
--

INSERT INTO ta_modele_bareme (id_modele_bareme, id_modele_conformite, expression_verifiee, forcage, chemin_doc, action_corrective, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj) VALUES (4, 3, 'val>10', false, '', '', 'postgres', '2015-05-05 11:01:07.4569', 'postgres', '2015-05-05 11:01:07.4569', '2.0.13', '127.0.0.1', 0);
INSERT INTO ta_modele_bareme (id_modele_bareme, id_modele_conformite, expression_verifiee, forcage, chemin_doc, action_corrective, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj) VALUES (5, 5, 'val=''Bon''', false, '', 'Refus du lot matières premières, appel fournisseurs', 'postgres', '2015-07-22 16:48:48.994', 'postgres', '2015-07-22 16:48:48.994', '2.0.13', '127.0.0.1', 0);
INSERT INTO ta_modele_bareme (id_modele_bareme, id_modele_conformite, expression_verifiee, forcage, chemin_doc, action_corrective, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj) VALUES (6, 6, 'val<4', false, '', 'Refus du lot matières premières, appel fournisseurs', 'postgres', '2015-07-22 16:50:25.002', 'postgres', '2015-07-22 16:50:25.002', '2.0.13', '127.0.0.1', 0);
INSERT INTO ta_modele_bareme (id_modele_bareme, id_modele_conformite, expression_verifiee, forcage, chemin_doc, action_corrective, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj) VALUES (7, 7, 'val=''Bon''', false, '', 'Refaire le réglage de la sertisseuse. Cf. PDF', 'postgres', '2015-07-22 16:52:43.906', 'postgres', '2015-07-22 16:52:43.906', '2.0.13', '127.0.0.1', 0);
INSERT INTO ta_modele_bareme (id_modele_bareme, id_modele_conformite, expression_verifiee, forcage, chemin_doc, action_corrective, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj) VALUES (8, 8, 'val<=30', false, '', 'Refaire le réglage de la sertisseuse. Cf. PDF', 'postgres', '2015-07-22 16:54:44.721', 'postgres', '2015-07-22 16:54:44.721', '2.0.13', '127.0.0.1', 0);
INSERT INTO ta_modele_bareme (id_modele_bareme, id_modele_conformite, expression_verifiee, forcage, chemin_doc, action_corrective, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj) VALUES (9, 9, 'val>=1.1', false, '', 'Refaire le réglage de la sertisseuse. Cf. PDF', 'postgres', '2015-07-22 16:59:14.976', 'postgres', '2015-07-22 16:59:14.976', '2.0.13', '127.0.0.1', 0);
INSERT INTO ta_modele_bareme (id_modele_bareme, id_modele_conformite, expression_verifiee, forcage, chemin_doc, action_corrective, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj) VALUES (10, 15, 'val<=0.2', false, '', 'Refaire le réglage de la sertisseuse. Cf. PDF', 'postgres', '2015-07-22 17:06:35.753', 'postgres', '2015-07-22 17:06:35.753', '2.0.13', '127.0.0.1', 0);
INSERT INTO ta_modele_bareme (id_modele_bareme, id_modele_conformite, expression_verifiee, forcage, chemin_doc, action_corrective, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj) VALUES (11, 16, 'val=''Bon''', false, '', 'Lot non conforme, réalisation d''une analyse de stabilité selon la norme AFNOR XP V08 408 dans un laboratoire.', 'postgres', '2015-07-22 17:09:08.81', 'postgres', '2015-07-22 17:09:08.81', '2.0.13', '127.0.0.1', 0);
INSERT INTO ta_modele_bareme (id_modele_bareme, id_modele_conformite, expression_verifiee, forcage, chemin_doc, action_corrective, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj) VALUES (12, 17, 'val=''Bon''', false, '', 'Contrôle de la fermeture sur le lot', 'postgres', '2015-07-22 17:10:33.389', 'postgres', '2015-07-22 17:10:33.389', '2.0.13', '127.0.0.1', 0);
INSERT INTO ta_modele_bareme (id_modele_bareme, id_modele_conformite, expression_verifiee, forcage, chemin_doc, action_corrective, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj) VALUES (13, 21, 'val>=75', false, '', 'Blocage du lot avec analyse microbiologique libératoire', 'postgres', '2015-07-22 17:13:54.532', 'postgres', '2015-07-22 17:13:54.532', '2.0.13', '127.0.0.1', 0);
INSERT INTO ta_modele_bareme (id_modele_bareme, id_modele_conformite, expression_verifiee, forcage, chemin_doc, action_corrective, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj) VALUES (14, 25, 'val<=0.5', false, '', 'Faire étalonner les organes de mesure', 'postgres', '2015-07-22 17:28:16.763', 'postgres', '2015-07-22 17:28:16.763', '2.0.13', '127.0.0.1', 0);
INSERT INTO ta_modele_bareme (id_modele_bareme, id_modele_conformite, expression_verifiee, forcage, chemin_doc, action_corrective, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj) VALUES (15, 26, 'val>=0.7', false, '', '', 'postgres', '2015-07-22 17:29:18.589', 'postgres', '2015-07-22 17:29:18.589', '2.0.13', '127.0.0.1', 0);
INSERT INTO ta_modele_bareme (id_modele_bareme, id_modele_conformite, expression_verifiee, forcage, chemin_doc, action_corrective, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj) VALUES (16, 29, 'val>=7', false, '', 'Poursuivre l''étuvage', 'postgres', '2015-07-22 17:33:06.698', 'postgres', '2015-07-22 17:33:06.698', '2.0.13', '127.0.0.1', 0);
INSERT INTO ta_modele_bareme (id_modele_bareme, id_modele_conformite, expression_verifiee, forcage, chemin_doc, action_corrective, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj) VALUES (17, 30, 'val=''Bon''', false, '', 'Blocage du lot en chambre froide analyse complémentaire, destruction du lot ou recyclage avec maîtrise du risque sanitaire, ENREGISTREMENT DE L''ACTION CORRECTION', 'postgres', '2015-07-22 17:35:40.062', 'postgres', '2015-07-22 17:35:40.062', '2.0.13', '127.0.0.1', 0);
INSERT INTO ta_modele_bareme (id_modele_bareme, id_modele_conformite, expression_verifiee, forcage, chemin_doc, action_corrective, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj) VALUES (18, 33, 'val<0.5', false, '', 'Le lot doit être détruit.', 'postgres', '2015-07-22 17:39:17.458', 'postgres', '2015-07-22 17:39:17.458', '2.0.13', '127.0.0.1', 0);


--
-- TOC entry 5161 (class 0 OID 0)
-- Dependencies: 372
-- Name: ta_modele_bareme_id_modele_bareme_seq; Type: SEQUENCE SET; Schema: demo; Owner: postgres
--

SELECT pg_catalog.setval('ta_modele_bareme_id_modele_bareme_seq', 18, true);


--
-- TOC entry 5151 (class 0 OID 229392)
-- Dependencies: 373
-- Data for Name: ta_modele_conformite; Type: TABLE DATA; Schema: demo; Owner: postgres
--

INSERT INTO ta_modele_conformite (id_modele_conformite, id_type_conformite, id_groupe, valeur_defaut, deuxieme_valeur, libelle_conformite, valeur_calculee, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj, id_modele_groupe) VALUES (3, 1, 1, '0', '2', 'test', NULL, 'postgres', '2015-05-05 11:01:07.4569', 'postgres', '2015-05-05 11:01:07.4569', '2.0.13', '127.0.0.1', 0, 1);
INSERT INTO ta_modele_conformite (id_modele_conformite, id_type_conformite, id_groupe, valeur_defaut, deuxieme_valeur, libelle_conformite, valeur_calculee, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj, id_modele_groupe) VALUES (4, 6, 1, '', '', 'Date d''arrivée', NULL, 'postgres', '2015-07-22 16:46:01.947', 'postgres', '2015-07-22 16:46:01.947', '2.0.13', '127.0.0.1', 0, NULL);
INSERT INTO ta_modele_conformite (id_modele_conformite, id_type_conformite, id_groupe, valeur_defaut, deuxieme_valeur, libelle_conformite, valeur_calculee, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj, id_modele_groupe) VALUES (5, 4, 1, '', 'Bon;Mauvais', 'Contrôle visuel (état des conditionnements, emballages et dates de péremption)', NULL, 'postgres', '2015-07-22 16:48:48.979', 'postgres', '2015-07-22 16:48:48.979', '2.0.13', '127.0.0.1', 0, NULL);
INSERT INTO ta_modele_conformite (id_modele_conformite, id_type_conformite, id_groupe, valeur_defaut, deuxieme_valeur, libelle_conformite, valeur_calculee, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj, id_modele_groupe) VALUES (6, 1, 1, '', '', 'Température réception', NULL, 'postgres', '2015-07-22 16:50:25.002', 'postgres', '2015-07-22 16:50:25.002', '2.0.13', '127.0.0.1', 0, NULL);
INSERT INTO ta_modele_conformite (id_modele_conformite, id_type_conformite, id_groupe, valeur_defaut, deuxieme_valeur, libelle_conformite, valeur_calculee, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj, id_modele_groupe) VALUES (7, 4, 2, '', 'Bon;Mauvais', 'Contrôle pression', NULL, 'postgres', '2015-07-22 16:52:43.906', 'postgres', '2015-07-22 16:52:43.906', '2.0.13', '127.0.0.1', 0, NULL);
INSERT INTO ta_modele_conformite (id_modele_conformite, id_type_conformite, id_groupe, valeur_defaut, deuxieme_valeur, libelle_conformite, valeur_calculee, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj, id_modele_groupe) VALUES (8, 1, 2, '', '', 'Pourcentage d''ondulation (%)', NULL, 'postgres', '2015-07-22 16:54:44.675', 'postgres', '2015-07-22 16:54:44.675', '2.0.13', '127.0.0.1', 0, NULL);
INSERT INTO ta_modele_conformite (id_modele_conformite, id_type_conformite, id_groupe, valeur_defaut, deuxieme_valeur, libelle_conformite, valeur_calculee, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj, id_modele_groupe) VALUES (9, 9, 2, '(b+c+d)-a', NULL, 'Calcul de la croisure (mm)', NULL, 'postgres', '2015-07-22 16:59:14.96', 'postgres', '2015-07-22 16:59:14.96', '2.0.13', '127.0.0.1', 0, NULL);
INSERT INTO ta_modele_conformite (id_modele_conformite, id_type_conformite, id_groupe, valeur_defaut, deuxieme_valeur, libelle_conformite, valeur_calculee, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj, id_modele_groupe) VALUES (10, 1, 2, '', '2', 'a) Hauteur serti (mm)', NULL, 'postgres', '2015-07-22 17:01:42.454', 'postgres', '2015-07-22 17:01:42.454', '2.0.13', '127.0.0.1', 0, NULL);
INSERT INTO ta_modele_conformite (id_modele_conformite, id_type_conformite, id_groupe, valeur_defaut, deuxieme_valeur, libelle_conformite, valeur_calculee, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj, id_modele_groupe) VALUES (11, 1, 2, '', '2', 'b) Crochet de corps (mm)', NULL, 'postgres', '2015-07-22 17:02:27.65', 'postgres', '2015-07-22 17:02:27.65', '2.0.13', '127.0.0.1', 0, NULL);
INSERT INTO ta_modele_conformite (id_modele_conformite, id_type_conformite, id_groupe, valeur_defaut, deuxieme_valeur, libelle_conformite, valeur_calculee, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj, id_modele_groupe) VALUES (12, 1, 2, '', '2', 'c) Crochet de fond (mm)', NULL, 'postgres', '2015-07-22 17:03:02.277', 'postgres', '2015-07-22 17:03:02.277', '2.0.13', '127.0.0.1', 0, NULL);
INSERT INTO ta_modele_conformite (id_modele_conformite, id_type_conformite, id_groupe, valeur_defaut, deuxieme_valeur, libelle_conformite, valeur_calculee, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj, id_modele_groupe) VALUES (13, 1, 2, '0.22', '2', 'd) Epaisseur métal fond (mm)', NULL, 'postgres', '2015-07-22 17:03:57.608', 'postgres', '2015-07-22 17:03:57.608', '2.0.13', '127.0.0.1', 0, NULL);
INSERT INTO ta_modele_conformite (id_modele_conformite, id_type_conformite, id_groupe, valeur_defaut, deuxieme_valeur, libelle_conformite, valeur_calculee, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj, id_modele_groupe) VALUES (14, 1, 2, '0.23', '2', 'e) Epaisseur métal corps boite (mm)', NULL, 'postgres', '2015-07-22 17:05:14.07', 'postgres', '2015-07-22 17:05:14.07', '2.0.13', '127.0.0.1', 0, NULL);
INSERT INTO ta_modele_conformite (id_modele_conformite, id_type_conformite, id_groupe, valeur_defaut, deuxieme_valeur, libelle_conformite, valeur_calculee, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj, id_modele_groupe) VALUES (15, 9, 2, 'abs(b-c)', NULL, 'Calcul équilibre crochet (mm)', NULL, 'postgres', '2015-07-22 17:06:35.719', 'postgres', '2015-07-22 17:06:35.719', '2.0.13', '127.0.0.1', 0, NULL);
INSERT INTO ta_modele_conformite (id_modele_conformite, id_type_conformite, id_groupe, valeur_defaut, deuxieme_valeur, libelle_conformite, valeur_calculee, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj, id_modele_groupe) VALUES (16, 4, 19, '', 'Bon;Mauvais', 'Temps d''attente', NULL, 'postgres', '2015-07-22 17:09:08.81', 'postgres', '2015-07-22 17:09:08.81', '2.0.13', '127.0.0.1', 0, NULL);
INSERT INTO ta_modele_conformite (id_modele_conformite, id_type_conformite, id_groupe, valeur_defaut, deuxieme_valeur, libelle_conformite, valeur_calculee, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj, id_modele_groupe) VALUES (17, 4, 19, '', 'Bon;Mauvais', 'Contrôle visuel fermeture du conditionnement', NULL, 'postgres', '2015-07-22 17:10:33.389', 'postgres', '2015-07-22 17:10:33.389', '2.0.13', '127.0.0.1', 0, NULL);
INSERT INTO ta_modele_conformite (id_modele_conformite, id_type_conformite, id_groupe, valeur_defaut, deuxieme_valeur, libelle_conformite, valeur_calculee, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj, id_modele_groupe) VALUES (18, 1, 19, '', '', 'N° de l''autoclave', NULL, 'postgres', '2015-07-22 17:11:01.404', 'postgres', '2015-07-22 17:11:01.404', '2.0.13', '127.0.0.1', 0, NULL);
INSERT INTO ta_modele_conformite (id_modele_conformite, id_type_conformite, id_groupe, valeur_defaut, deuxieme_valeur, libelle_conformite, valeur_calculee, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj, id_modele_groupe) VALUES (19, 1, 19, 'X', '2', 'N° de programme', NULL, 'postgres', '2015-07-22 17:11:30.998', 'postgres', '2015-07-22 17:11:30.998', '2.0.13', '127.0.0.1', 0, NULL);
INSERT INTO ta_modele_conformite (id_modele_conformite, id_type_conformite, id_groupe, valeur_defaut, deuxieme_valeur, libelle_conformite, valeur_calculee, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj, id_modele_groupe) VALUES (20, 1, 19, 'X', '2', 'Durée du barème', NULL, 'postgres', '2015-07-22 17:12:00.27', 'postgres', '2015-07-22 17:12:00.27', '2.0.13', '127.0.0.1', 0, NULL);
INSERT INTO ta_modele_conformite (id_modele_conformite, id_type_conformite, id_groupe, valeur_defaut, deuxieme_valeur, libelle_conformite, valeur_calculee, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj, id_modele_groupe) VALUES (21, 1, 19, '116', '2', 'Température barème', NULL, 'postgres', '2015-07-22 17:13:54.532', 'postgres', '2015-07-22 17:13:54.532', '2.0.13', '127.0.0.1', 0, NULL);
INSERT INTO ta_modele_conformite (id_modele_conformite, id_type_conformite, id_groupe, valeur_defaut, deuxieme_valeur, libelle_conformite, valeur_calculee, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj, id_modele_groupe) VALUES (22, 7, 19, '', NULL, 'Heure du relevé dans le palier', NULL, 'postgres', '2015-07-22 17:14:33.441', 'postgres', '2015-07-22 17:14:33.441', '2.0.13', '127.0.0.1', 0, NULL);
INSERT INTO ta_modele_conformite (id_modele_conformite, id_type_conformite, id_groupe, valeur_defaut, deuxieme_valeur, libelle_conformite, valeur_calculee, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj, id_modele_groupe) VALUES (23, 1, 19, '116', '2', 'T°C de la régulation', NULL, 'postgres', '2015-07-22 17:21:33.463', 'postgres', '2015-07-22 17:21:33.463', '2.0.13', '127.0.0.1', 0, NULL);
INSERT INTO ta_modele_conformite (id_modele_conformite, id_type_conformite, id_groupe, valeur_defaut, deuxieme_valeur, libelle_conformite, valeur_calculee, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj, id_modele_groupe) VALUES (24, 1, 19, '116', '2', 'T°C thermo à lecture direct', NULL, 'postgres', '2015-07-22 17:25:40.11', 'postgres', '2015-07-22 17:28:26.614', '2.0.13', '127.0.0.1', 1, NULL);
INSERT INTO ta_modele_conformite (id_modele_conformite, id_type_conformite, id_groupe, valeur_defaut, deuxieme_valeur, libelle_conformite, valeur_calculee, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj, id_modele_groupe) VALUES (25, 9, 19, 'abs(T°C de la régulation - T°C thermo à lecture direct)', NULL, 'Ecart entre la températures des différents organes ', NULL, 'postgres', '2015-07-22 17:28:16.763', 'postgres', '2015-07-22 17:28:39.693', '2.0.13', '127.0.0.1', 1, NULL);
INSERT INTO ta_modele_conformite (id_modele_conformite, id_type_conformite, id_groupe, valeur_defaut, deuxieme_valeur, libelle_conformite, valeur_calculee, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj, id_modele_groupe) VALUES (26, 1, 19, '', '2', 'Pression', NULL, 'postgres', '2015-07-22 17:29:18.559', 'postgres', '2015-07-22 17:29:18.559', '2.0.13', '127.0.0.1', 0, NULL);
INSERT INTO ta_modele_conformite (id_modele_conformite, id_type_conformite, id_groupe, valeur_defaut, deuxieme_valeur, libelle_conformite, valeur_calculee, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj, id_modele_groupe) VALUES (27, 6, 20, '', NULL, 'Date de mise en étuve à 37°C', NULL, 'postgres', '2015-07-22 17:30:14.706', 'postgres', '2015-07-22 17:30:14.706', '2.0.13', '127.0.0.1', 0, NULL);
INSERT INTO ta_modele_conformite (id_modele_conformite, id_type_conformite, id_groupe, valeur_defaut, deuxieme_valeur, libelle_conformite, valeur_calculee, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj, id_modele_groupe) VALUES (28, 6, 20, '', NULL, 'Date de sortie en étuve à 37°C', NULL, 'postgres', '2015-07-22 17:30:51.5', 'postgres', '2015-07-22 17:30:51.5', '2.0.13', '127.0.0.1', 0, NULL);
INSERT INTO ta_modele_conformite (id_modele_conformite, id_type_conformite, id_groupe, valeur_defaut, deuxieme_valeur, libelle_conformite, valeur_calculee, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj, id_modele_groupe) VALUES (29, 9, 20, '(Date de sortie - Date de mise)/1440', NULL, 'Durée d''étuvage à 37° (j)', NULL, 'postgres', '2015-07-22 17:33:06.698', 'postgres', '2015-07-22 17:33:06.698', '2.0.13', '127.0.0.1', 0, NULL);
INSERT INTO ta_modele_conformite (id_modele_conformite, id_type_conformite, id_groupe, valeur_defaut, deuxieme_valeur, libelle_conformite, valeur_calculee, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj, id_modele_groupe) VALUES (30, 4, 20, '', 'Bon;Mauvais', 'Contrôle visuel', NULL, 'postgres', '2015-07-22 17:35:40.028', 'postgres', '2015-07-22 17:35:40.028', '2.0.13', '127.0.0.1', 0, NULL);
INSERT INTO ta_modele_conformite (id_modele_conformite, id_type_conformite, id_groupe, valeur_defaut, deuxieme_valeur, libelle_conformite, valeur_calculee, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj, id_modele_groupe) VALUES (31, 1, 20, '', '2', 'Mesure pH boite étuvé 37°C', NULL, 'postgres', '2015-07-22 17:36:27.272', 'postgres', '2015-07-22 17:36:27.272', '2.0.13', '127.0.0.1', 0, NULL);
INSERT INTO ta_modele_conformite (id_modele_conformite, id_type_conformite, id_groupe, valeur_defaut, deuxieme_valeur, libelle_conformite, valeur_calculee, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj, id_modele_groupe) VALUES (32, 1, 20, '', '2', 'Mesure pH témoin 20°C', NULL, 'postgres', '2015-07-22 17:37:03.983', 'postgres', '2015-07-22 17:37:03.983', '2.0.13', '127.0.0.1', 0, NULL);
INSERT INTO ta_modele_conformite (id_modele_conformite, id_type_conformite, id_groupe, valeur_defaut, deuxieme_valeur, libelle_conformite, valeur_calculee, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj, id_modele_groupe) VALUES (33, 9, 20, 'Mesure pH témoin - Mesure pH boite étuvé', NULL, 'Calcul variation', NULL, 'postgres', '2015-07-22 17:39:17.442', 'postgres', '2015-07-22 17:39:17.442', '2.0.13', '127.0.0.1', 0, NULL);
INSERT INTO ta_modele_conformite (id_modele_conformite, id_type_conformite, id_groupe, valeur_defaut, deuxieme_valeur, libelle_conformite, valeur_calculee, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj, id_modele_groupe) VALUES (34, 4, 20, 'NON', 'OUI;NON', 'Test pH effectué', NULL, 'postgres', '2015-07-22 17:40:03.553', 'postgres', '2015-07-22 17:40:03.553', '2.0.13', '127.0.0.1', 0, NULL);


--
-- TOC entry 5162 (class 0 OID 0)
-- Dependencies: 374
-- Name: ta_modele_conformite_id_modele_conformite_seq; Type: SEQUENCE SET; Schema: demo; Owner: postgres
--

SELECT pg_catalog.setval('ta_modele_conformite_id_modele_conformite_seq', 34, true);


--
-- TOC entry 5021 (class 2606 OID 239411)
-- Name: ta_groupe_pkey; Type: CONSTRAINT; Schema: demo; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ta_groupe
    ADD CONSTRAINT ta_groupe_pkey PRIMARY KEY (id_groupe);


--
-- TOC entry 5024 (class 2606 OID 239495)
-- Name: ta_modele_bareme_pkey; Type: CONSTRAINT; Schema: demo; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ta_modele_bareme
    ADD CONSTRAINT ta_modele_bareme_pkey PRIMARY KEY (id_modele_bareme);


--
-- TOC entry 5027 (class 2606 OID 239497)
-- Name: ta_modele_conformite_pkey; Type: CONSTRAINT; Schema: demo; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ta_modele_conformite
    ADD CONSTRAINT ta_modele_conformite_pkey PRIMARY KEY (id_modele_conformite);


--
-- TOC entry 5025 (class 1259 OID 240614)
-- Name: fki_modele_groupe; Type: INDEX; Schema: demo; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_modele_groupe ON ta_modele_conformite USING btree (id_modele_groupe);


--
-- TOC entry 5019 (class 1259 OID 240733)
-- Name: ta_groupe_code; Type: INDEX; Schema: demo; Owner: postgres; Tablespace: 
--

CREATE INDEX ta_groupe_code ON ta_groupe USING btree (code_groupe);


--
-- TOC entry 5022 (class 1259 OID 240829)
-- Name: unq1_ta_groupe; Type: INDEX; Schema: demo; Owner: postgres; Tablespace: 
--

CREATE INDEX unq1_ta_groupe ON ta_groupe USING btree (code_groupe);


--
-- TOC entry 5032 (class 2620 OID 241830)
-- Name: tbi_ta_groupe; Type: TRIGGER; Schema: demo; Owner: postgres
--

CREATE TRIGGER tbi_ta_groupe BEFORE INSERT ON ta_groupe FOR EACH ROW EXECUTE PROCEDURE before_insert();


--
-- TOC entry 5034 (class 2620 OID 241849)
-- Name: tbi_ta_modele_bareme; Type: TRIGGER; Schema: demo; Owner: postgres
--

CREATE TRIGGER tbi_ta_modele_bareme BEFORE INSERT ON ta_modele_bareme FOR EACH ROW EXECUTE PROCEDURE before_insert();


--
-- TOC entry 5036 (class 2620 OID 241850)
-- Name: tbi_ta_modele_conformite; Type: TRIGGER; Schema: demo; Owner: postgres
--

CREATE TRIGGER tbi_ta_modele_conformite BEFORE INSERT ON ta_modele_conformite FOR EACH ROW EXECUTE PROCEDURE before_insert();


--
-- TOC entry 5033 (class 2620 OID 242012)
-- Name: tbu_ta_groupe; Type: TRIGGER; Schema: demo; Owner: postgres
--

CREATE TRIGGER tbu_ta_groupe BEFORE UPDATE ON ta_groupe FOR EACH ROW EXECUTE PROCEDURE before_update();


--
-- TOC entry 5035 (class 2620 OID 242031)
-- Name: tbu_ta_modele_bareme; Type: TRIGGER; Schema: demo; Owner: postgres
--

CREATE TRIGGER tbu_ta_modele_bareme BEFORE UPDATE ON ta_modele_bareme FOR EACH ROW EXECUTE PROCEDURE before_update();


--
-- TOC entry 5037 (class 2620 OID 242032)
-- Name: tbu_ta_modele_conformite; Type: TRIGGER; Schema: demo; Owner: postgres
--

CREATE TRIGGER tbu_ta_modele_conformite BEFORE UPDATE ON ta_modele_conformite FOR EACH ROW EXECUTE PROCEDURE before_update();


--
-- TOC entry 5029 (class 2606 OID 242819)
-- Name: fk_modele_groupe; Type: FK CONSTRAINT; Schema: demo; Owner: postgres
--

ALTER TABLE ONLY ta_modele_conformite
    ADD CONSTRAINT fk_modele_groupe FOREIGN KEY (id_modele_groupe) REFERENCES ta_modele_groupe(id_modele_groupe);


--
-- TOC entry 5028 (class 2606 OID 243974)
-- Name: ta_modele_bareme_fk; Type: FK CONSTRAINT; Schema: demo; Owner: postgres
--

ALTER TABLE ONLY ta_modele_bareme
    ADD CONSTRAINT ta_modele_bareme_fk FOREIGN KEY (id_modele_conformite) REFERENCES ta_modele_conformite(id_modele_conformite);


--
-- TOC entry 5030 (class 2606 OID 243979)
-- Name: ta_modele_conformite_fk; Type: FK CONSTRAINT; Schema: demo; Owner: postgres
--

ALTER TABLE ONLY ta_modele_conformite
    ADD CONSTRAINT ta_modele_conformite_fk FOREIGN KEY (id_type_conformite) REFERENCES ta_type_conformite(id_type_conformite);


--
-- TOC entry 5031 (class 2606 OID 243984)
-- Name: ta_modele_conformite_fk1; Type: FK CONSTRAINT; Schema: demo; Owner: postgres
--

ALTER TABLE ONLY ta_modele_conformite
    ADD CONSTRAINT ta_modele_conformite_fk1 FOREIGN KEY (id_groupe) REFERENCES ta_groupe(id_groupe);


-- Completed on 2015-07-27 09:22:01

--
-- PostgreSQL database dump complete
--

