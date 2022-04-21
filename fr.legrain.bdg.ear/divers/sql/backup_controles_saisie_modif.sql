set schema 'demo';
SELECT pg_catalog.setval('ta_modele_bareme_id_modele_bareme_seq', 1, false);
SELECT pg_catalog.setval('ta_groupe_id_groupe_seq', 1, false);
SELECT pg_catalog.setval('ta_modele_conformite_id_modele_conformite_seq', 1, false);


INSERT INTO ta_groupe (id_groupe, code_groupe, libelle, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj) 
VALUES (1, 'GRP 1', 'Contrôle à la Réception ', 'postgres', '2015-04-24 11:05:42.73825+02', 'postgres', '2015-07-22 16:53:12.661+02', '2.0.13', '127.0.0.1', 3);
INSERT INTO ta_groupe (id_groupe, code_groupe, libelle, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj)
 VALUES (2, 'GRP 2', 'Contrôle Fermeture Conditionnement', 'postgres', '2015-04-24 11:05:56.19112+02', 'postgres', '2015-07-22 16:53:20.172+02', '2.0.13', '127.0.0.1', 3);
INSERT INTO ta_groupe (id_groupe, code_groupe, libelle, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj) 
VALUES (3, 'GRP 3', 'Contrôle Traitement Thermique ', 'postgres', '2015-07-22 16:42:46.502+02', 'postgres', '2015-07-22 16:53:27.664+02', '2.0.13', '127.0.0.1', 1);
INSERT INTO ta_groupe (id_groupe, code_groupe, libelle, qui_cree, quand_cree, qui_modif, quand_modif, version, ip_acces, version_obj)
 VALUES (4, 'GRP 4', 'Contrôle Stabilite ', 'postgres', '2015-07-22 16:44:22.278+02', 'postgres', '2015-07-22 16:53:32.766+02', '2.0.13', '127.0.0.1', 1);


--
-- TOC entry 5160 (class 0 OID 0)
-- Dependencies: 292
-- Name: ta_groupe_id_groupe_seq; Type: SEQUENCE SET; Schema: demo; Owner: postgres
--

SELECT pg_catalog.setval('ta_groupe_id_groupe_seq', 4, true);


--
-- TOC entry 5149 (class 0 OID 229380)
-- Dependencies: 371
-- Data for Name: ta_modele_bareme; Type: TABLE DATA; Schema: demo; Owner: postgres
--

--
-- TOC entry 5151 (class 0 OID 229392)
-- Dependencies: 373
-- Data for Name: ta_modele_conformite; Type: TABLE DATA; Schema: demo; Owner: postgres
--

INSERT INTO ta_modele_conformite (id_modele_conformite, id_type_conformite, id_groupe, valeur_defaut, deuxieme_valeur, libelle_conformite, valeur_calculee, qui_cree, quand_cree, qui_modif, quand_modif, 
	version, ip_acces, version_obj, id_modele_groupe) VALUES (1, 1, 1, '0', '2', 'test', NULL, 'postgres', '2015-05-05 11:01:07.4569', 'postgres', '2015-05-05 11:01:07.4569', '2.0.13', '127.0.0.1', 0, NULL);
INSERT INTO ta_modele_conformite (id_modele_conformite, id_type_conformite, id_groupe, valeur_defaut, deuxieme_valeur, libelle_conformite, valeur_calculee, qui_cree, quand_cree, qui_modif, quand_modif,
	 version, ip_acces, version_obj, id_modele_groupe) VALUES (2, 6, 1, '', '', 'Date d''arrivée', NULL, 'postgres', '2015-07-22 16:46:01.947', 'postgres', '2015-07-22 16:46:01.947', '2.0.13', '127.0.0.1', 0, NULL);
INSERT INTO ta_modele_conformite (id_modele_conformite, id_type_conformite, id_groupe, valeur_defaut, deuxieme_valeur, libelle_conformite, valeur_calculee, qui_cree, quand_cree, qui_modif, quand_modif,
	 version, ip_acces, version_obj, id_modele_groupe) VALUES (3, 4, 1, '', 'Bon;Mauvais', 'Contrôle visuel (état des conditionnements, emballages et dates de péremption)', NULL, 'postgres', '2015-07-22 16:48:48.979', 'postgres', '2015-07-22 16:48:48.979', '2.0.13', '127.0.0.1', 0, NULL);
INSERT INTO ta_modele_conformite (id_modele_conformite, id_type_conformite, id_groupe, valeur_defaut, deuxieme_valeur, libelle_conformite, valeur_calculee, qui_cree, quand_cree, qui_modif, quand_modif, 
	version, ip_acces, version_obj, id_modele_groupe) VALUES (4, 1, 1, '', '', 'Température réception', NULL, 'postgres', '2015-07-22 16:50:25.002', 'postgres', '2015-07-22 16:50:25.002', '2.0.13', '127.0.0.1', 0, NULL);
INSERT INTO ta_modele_conformite (id_modele_conformite, id_type_conformite, id_groupe, valeur_defaut, deuxieme_valeur, libelle_conformite, valeur_calculee, qui_cree, quand_cree, qui_modif, quand_modif,
	 version, ip_acces, version_obj, id_modele_groupe) VALUES (5, 4, 2, '', 'Bon;Mauvais', 'Contrôle pression', NULL, 'postgres', '2015-07-22 16:52:43.906', 'postgres', '2015-07-22 16:52:43.906', '2.0.13', '127.0.0.1', 0, NULL);
INSERT INTO ta_modele_conformite (id_modele_conformite, id_type_conformite, id_groupe, valeur_defaut, deuxieme_valeur, libelle_conformite, valeur_calculee, qui_cree, quand_cree, qui_modif, quand_modif, 
	version, ip_acces, version_obj, id_modele_groupe) VALUES (6, 1, 2, '', '', 'Pourcentage d''ondulation (%)', NULL, 'postgres', '2015-07-22 16:54:44.675', 'postgres', '2015-07-22 16:54:44.675', '2.0.13', '127.0.0.1', 0, NULL);
INSERT INTO ta_modele_conformite (id_modele_conformite, id_type_conformite, id_groupe, valeur_defaut, deuxieme_valeur, libelle_conformite, valeur_calculee, qui_cree, quand_cree, qui_modif, quand_modif, 
	version, ip_acces, version_obj, id_modele_groupe) VALUES (7, 9, 2, '(b+c+d)-a', NULL, 'Calcul de la croisure (mm)', NULL, 'postgres', '2015-07-22 16:59:14.96', 'postgres', '2015-07-22 16:59:14.96', '2.0.13', '127.0.0.1', 0, NULL);
INSERT INTO ta_modele_conformite (id_modele_conformite, id_type_conformite, id_groupe, valeur_defaut, deuxieme_valeur, libelle_conformite, valeur_calculee, qui_cree, quand_cree, qui_modif, quand_modif,
	 version, ip_acces, version_obj, id_modele_groupe) VALUES (8, 1, 2, '', '2', 'a) Hauteur serti (mm)', NULL, 'postgres', '2015-07-22 17:01:42.454', 'postgres', '2015-07-22 17:01:42.454', '2.0.13', '127.0.0.1', 0, NULL);
INSERT INTO ta_modele_conformite (id_modele_conformite, id_type_conformite, id_groupe, valeur_defaut, deuxieme_valeur, libelle_conformite, valeur_calculee, qui_cree, quand_cree, qui_modif, quand_modif, 
	version, ip_acces, version_obj, id_modele_groupe) VALUES (9, 1, 2, '', '2', 'b) Crochet de corps (mm)', NULL, 'postgres', '2015-07-22 17:02:27.65', 'postgres', '2015-07-22 17:02:27.65', '2.0.13', '127.0.0.1', 0, NULL);
INSERT INTO ta_modele_conformite (id_modele_conformite, id_type_conformite, id_groupe, valeur_defaut, deuxieme_valeur, libelle_conformite, valeur_calculee, qui_cree, quand_cree, qui_modif, quand_modif, 
	version, ip_acces, version_obj, id_modele_groupe) VALUES (10, 1, 2, '', '2', 'c) Crochet de fond (mm)', NULL, 'postgres', '2015-07-22 17:03:02.277', 'postgres', '2015-07-22 17:03:02.277', '2.0.13', '127.0.0.1', 0, NULL);
INSERT INTO ta_modele_conformite (id_modele_conformite, id_type_conformite, id_groupe, valeur_defaut, deuxieme_valeur, libelle_conformite, valeur_calculee, qui_cree, quand_cree, qui_modif, quand_modif, 
	version, ip_acces, version_obj, id_modele_groupe) VALUES (11, 1, 2, '0.22', '2', 'd) Epaisseur métal fond (mm)', NULL, 'postgres', '2015-07-22 17:03:57.608', 'postgres', '2015-07-22 17:03:57.608', '2.0.13', '127.0.0.1', 0, NULL);
INSERT INTO ta_modele_conformite (id_modele_conformite, id_type_conformite, id_groupe, valeur_defaut, deuxieme_valeur, libelle_conformite, valeur_calculee, qui_cree, quand_cree, qui_modif, quand_modif, 
	version, ip_acces, version_obj, id_modele_groupe) VALUES (12, 1, 2, '0.23', '2', 'e) Epaisseur métal corps boite (mm)', NULL, 'postgres', '2015-07-22 17:05:14.07', 'postgres', '2015-07-22 17:05:14.07', '2.0.13', '127.0.0.1', 0, NULL);
INSERT INTO ta_modele_conformite (id_modele_conformite, id_type_conformite, id_groupe, valeur_defaut, deuxieme_valeur, libelle_conformite, valeur_calculee, qui_cree, quand_cree, qui_modif, quand_modif, 
	version, ip_acces, version_obj, id_modele_groupe) VALUES (13, 9, 2, 'abs(b-c)', NULL, 'Calcul équilibre crochet (mm)', NULL, 'postgres', '2015-07-22 17:06:35.719', 'postgres', '2015-07-22 17:06:35.719', '2.0.13', '127.0.0.1', 0, NULL);
INSERT INTO ta_modele_conformite (id_modele_conformite, id_type_conformite, id_groupe, valeur_defaut, deuxieme_valeur, libelle_conformite, valeur_calculee, qui_cree, quand_cree, qui_modif, quand_modif, 
	version, ip_acces, version_obj, id_modele_groupe) VALUES (14, 4, 3, '', 'Bon;Mauvais', 'Temps d''attente', NULL, 'postgres', '2015-07-22 17:09:08.81', 'postgres', '2015-07-22 17:09:08.81', '2.0.13', '127.0.0.1', 0, NULL);
INSERT INTO ta_modele_conformite (id_modele_conformite, id_type_conformite, id_groupe, valeur_defaut, deuxieme_valeur, libelle_conformite, valeur_calculee, qui_cree, quand_cree, qui_modif, quand_modif,
	 version, ip_acces, version_obj, id_modele_groupe) VALUES (15, 4, 3, '', 'Bon;Mauvais', 'Contrôle visuel fermeture du conditionnement', NULL, 'postgres', '2015-07-22 17:10:33.389', 'postgres', '2015-07-22 17:10:33.389', '2.0.13', '127.0.0.1', 0, NULL);
INSERT INTO ta_modele_conformite (id_modele_conformite, id_type_conformite, id_groupe, valeur_defaut, deuxieme_valeur, libelle_conformite, valeur_calculee, qui_cree, quand_cree, qui_modif, quand_modif,
	 version, ip_acces, version_obj, id_modele_groupe) VALUES (16, 1, 3, '', '', 'N° de l''autoclave', NULL, 'postgres', '2015-07-22 17:11:01.404', 'postgres', '2015-07-22 17:11:01.404', '2.0.13', '127.0.0.1', 0, NULL);
INSERT INTO ta_modele_conformite (id_modele_conformite, id_type_conformite, id_groupe, valeur_defaut, deuxieme_valeur, libelle_conformite, valeur_calculee, qui_cree, quand_cree, qui_modif, quand_modif, 
	version, ip_acces, version_obj, id_modele_groupe) VALUES (17, 1, 3, 'X', '2', 'N° de programme', NULL, 'postgres', '2015-07-22 17:11:30.998', 'postgres', '2015-07-22 17:11:30.998', '2.0.13', '127.0.0.1', 0, NULL);
INSERT INTO ta_modele_conformite (id_modele_conformite, id_type_conformite, id_groupe, valeur_defaut, deuxieme_valeur, libelle_conformite, valeur_calculee, qui_cree, quand_cree, qui_modif, quand_modif, 
	version, ip_acces, version_obj, id_modele_groupe) VALUES (18, 1, 3, 'X', '2', 'Durée du barème', NULL, 'postgres', '2015-07-22 17:12:00.27', 'postgres', '2015-07-22 17:12:00.27', '2.0.13', '127.0.0.1', 0, NULL);
INSERT INTO ta_modele_conformite (id_modele_conformite, id_type_conformite, id_groupe, valeur_defaut, deuxieme_valeur, libelle_conformite, valeur_calculee, qui_cree, quand_cree, qui_modif, quand_modif,
	 version, ip_acces, version_obj, id_modele_groupe) VALUES (19, 1, 3, '116', '2', 'Température barème', NULL, 'postgres', '2015-07-22 17:13:54.532', 'postgres', '2015-07-22 17:13:54.532', '2.0.13', '127.0.0.1', 0, NULL);
INSERT INTO ta_modele_conformite (id_modele_conformite, id_type_conformite, id_groupe, valeur_defaut, deuxieme_valeur, libelle_conformite, valeur_calculee, qui_cree, quand_cree, qui_modif, quand_modif, 
	version, ip_acces, version_obj, id_modele_groupe) VALUES (20, 7, 3, '', NULL, 'Heure du relevé dans le palier', NULL, 'postgres', '2015-07-22 17:14:33.441', 'postgres', '2015-07-22 17:14:33.441', '2.0.13', '127.0.0.1', 0, NULL);
INSERT INTO ta_modele_conformite (id_modele_conformite, id_type_conformite, id_groupe, valeur_defaut, deuxieme_valeur, libelle_conformite, valeur_calculee, qui_cree, quand_cree, qui_modif, quand_modif, 
	version, ip_acces, version_obj, id_modele_groupe) VALUES (21, 1, 3, '116', '2', 'T°C de la régulation', NULL, 'postgres', '2015-07-22 17:21:33.463', 'postgres', '2015-07-22 17:21:33.463', '2.0.13', '127.0.0.1', 0, NULL);
INSERT INTO ta_modele_conformite (id_modele_conformite, id_type_conformite, id_groupe, valeur_defaut, deuxieme_valeur, libelle_conformite, valeur_calculee, qui_cree, quand_cree, qui_modif, quand_modif, 
	version, ip_acces, version_obj, id_modele_groupe) VALUES (22, 1, 3, '116', '2', 'T°C thermo à lecture direct', NULL, 'postgres', '2015-07-22 17:25:40.11', 'postgres', '2015-07-22 17:28:26.614', '2.0.13', '127.0.0.1', 1, NULL);
INSERT INTO ta_modele_conformite (id_modele_conformite, id_type_conformite, id_groupe, valeur_defaut, deuxieme_valeur, libelle_conformite, valeur_calculee, qui_cree, quand_cree, qui_modif, quand_modif, 
	version, ip_acces, version_obj, id_modele_groupe) VALUES (23, 9, 3, 'abs(T°C de la régulation - T°C thermo à lecture direct)', NULL, 'Ecart entre la températures des différents organes ', NULL, 'postgres', '2015-07-22 17:28:16.763', 'postgres', '2015-07-22 17:28:39.693', '2.0.13', '127.0.0.1', 1, NULL);
INSERT INTO ta_modele_conformite (id_modele_conformite, id_type_conformite, id_groupe, valeur_defaut, deuxieme_valeur, libelle_conformite, valeur_calculee, qui_cree, quand_cree, qui_modif, quand_modif,
	 version, ip_acces, version_obj, id_modele_groupe) VALUES (24, 1, 3, '', '2', 'Pression', NULL, 'postgres', '2015-07-22 17:29:18.559', 'postgres', '2015-07-22 17:29:18.559', '2.0.13', '127.0.0.1', 0, NULL);
INSERT INTO ta_modele_conformite (id_modele_conformite, id_type_conformite, id_groupe, valeur_defaut, deuxieme_valeur, libelle_conformite, valeur_calculee, qui_cree, quand_cree, qui_modif, quand_modif, 
	version, ip_acces, version_obj, id_modele_groupe) VALUES (25, 6, 4, '', NULL, 'Date de mise en étuve à 37°C', NULL, 'postgres', '2015-07-22 17:30:14.706', 'postgres', '2015-07-22 17:30:14.706', '2.0.13', '127.0.0.1', 0, NULL);
INSERT INTO ta_modele_conformite (id_modele_conformite, id_type_conformite, id_groupe, valeur_defaut, deuxieme_valeur, libelle_conformite, valeur_calculee, qui_cree, quand_cree, qui_modif, quand_modif, 
	version, ip_acces, version_obj, id_modele_groupe) VALUES (26, 6, 4, '', NULL, 'Date de sortie en étuve à 37°C', NULL, 'postgres', '2015-07-22 17:30:51.5', 'postgres', '2015-07-22 17:30:51.5', '2.0.13', '127.0.0.1', 0, NULL);
INSERT INTO ta_modele_conformite (id_modele_conformite, id_type_conformite, id_groupe, valeur_defaut, deuxieme_valeur, libelle_conformite, valeur_calculee, qui_cree, quand_cree, qui_modif, quand_modif,
	 version, ip_acces, version_obj, id_modele_groupe) VALUES (27, 9, 4, '(Date de sortie - Date de mise)/1440', NULL, 'Durée d''étuvage à 37° (j)', NULL, 'postgres', '2015-07-22 17:33:06.698', 'postgres', '2015-07-22 17:33:06.698', '2.0.13', '127.0.0.1', 0, NULL);
INSERT INTO ta_modele_conformite (id_modele_conformite, id_type_conformite, id_groupe, valeur_defaut, deuxieme_valeur, libelle_conformite, valeur_calculee, qui_cree, quand_cree, qui_modif, quand_modif, 
	version, ip_acces, version_obj, id_modele_groupe) VALUES (28, 4, 4, '', 'Bon;Mauvais', 'Contrôle visuel', NULL, 'postgres', '2015-07-22 17:35:40.028', 'postgres', '2015-07-22 17:35:40.028', '2.0.13', '127.0.0.1', 0, NULL);
INSERT INTO ta_modele_conformite (id_modele_conformite, id_type_conformite, id_groupe, valeur_defaut, deuxieme_valeur, libelle_conformite, valeur_calculee, qui_cree, quand_cree, qui_modif, quand_modif,
	 version, ip_acces, version_obj, id_modele_groupe) VALUES (29, 1, 4, '', '2', 'Mesure pH boite étuvé 37°C', NULL, 'postgres', '2015-07-22 17:36:27.272', 'postgres', '2015-07-22 17:36:27.272', '2.0.13', '127.0.0.1', 0, NULL);
INSERT INTO ta_modele_conformite (id_modele_conformite, id_type_conformite, id_groupe, valeur_defaut, deuxieme_valeur, libelle_conformite, valeur_calculee, qui_cree, quand_cree, qui_modif, quand_modif, 
	version, ip_acces, version_obj, id_modele_groupe) VALUES (30, 1, 4, '', '2', 'Mesure pH témoin 20°C', NULL, 'postgres', '2015-07-22 17:37:03.983', 'postgres', '2015-07-22 17:37:03.983', '2.0.13', '127.0.0.1', 0, NULL);
INSERT INTO ta_modele_conformite (id_modele_conformite, id_type_conformite, id_groupe, valeur_defaut, deuxieme_valeur, libelle_conformite, valeur_calculee, qui_cree, quand_cree, qui_modif, quand_modif, 
	version, ip_acces, version_obj, id_modele_groupe) VALUES (31, 9, 4, 'Mesure pH témoin - Mesure pH boite étuvé', NULL, 'Calcul variation', NULL, 'postgres', '2015-07-22 17:39:17.442', 'postgres', '2015-07-22 17:39:17.442', '2.0.13', '127.0.0.1', 0, NULL);
INSERT INTO ta_modele_conformite (id_modele_conformite, id_type_conformite, id_groupe, valeur_defaut, deuxieme_valeur, libelle_conformite, valeur_calculee, qui_cree, quand_cree, qui_modif, quand_modif, 
	version, ip_acces, version_obj, id_modele_groupe) VALUES (32, 4, 4, 'NON', 'OUI;NON', 'Test pH effectué', NULL, 'postgres', '2015-07-22 17:40:03.553', 'postgres', '2015-07-22 17:40:03.553', '2.0.13', '127.0.0.1', 0, NULL);

----
---
SELECT pg_catalog.setval('ta_modele_conformite_id_modele_conformite_seq', 32, true);





INSERT INTO ta_modele_bareme (id_modele_bareme, id_modele_conformite, expression_verifiee, forcage, chemin_doc, action_corrective, qui_cree, quand_cree, qui_modif, quand_modif, version, 
	ip_acces, version_obj) VALUES (1, 1, 'val>10', false, '', '', 'postgres', '2015-05-05 11:01:07.4569', 'postgres', '2015-05-05 11:01:07.4569', '2.0.13', '127.0.0.1', 0);
INSERT INTO ta_modele_bareme (id_modele_bareme, id_modele_conformite, expression_verifiee, forcage, chemin_doc, action_corrective, qui_cree, quand_cree, qui_modif, quand_modif, version, 
	ip_acces, version_obj) VALUES (2, 3, 'val=''Bon''', false, '', 'Refus du lot matières premières, appel fournisseurs', 'postgres', '2015-07-22 16:48:48.994', 'postgres', '2015-07-22 16:48:48.994', '2.0.13', '127.0.0.1', 0);
INSERT INTO ta_modele_bareme (id_modele_bareme, id_modele_conformite, expression_verifiee, forcage, chemin_doc, action_corrective, qui_cree, quand_cree, qui_modif, quand_modif, version, 
	ip_acces, version_obj) VALUES (3, 4, 'val<4', false, '', 'Refus du lot matières premières, appel fournisseurs', 'postgres', '2015-07-22 16:50:25.002', 'postgres', '2015-07-22 16:50:25.002', '2.0.13', '127.0.0.1', 0);
INSERT INTO ta_modele_bareme (id_modele_bareme, id_modele_conformite, expression_verifiee, forcage, chemin_doc, action_corrective, qui_cree, quand_cree, qui_modif, quand_modif, version, 
	ip_acces, version_obj) VALUES (4, 5, 'val=''Bon''', false, '', 'Refaire le réglage de la sertisseuse. Cf. PDF', 'postgres', '2015-07-22 16:52:43.906', 'postgres', '2015-07-22 16:52:43.906', '2.0.13', '127.0.0.1', 0);
INSERT INTO ta_modele_bareme (id_modele_bareme, id_modele_conformite, expression_verifiee, forcage, chemin_doc, action_corrective, qui_cree, quand_cree, qui_modif, quand_modif, version, 
	ip_acces, version_obj) VALUES (5, 6, 'val<=30', false, '', 'Refaire le réglage de la sertisseuse. Cf. PDF', 'postgres', '2015-07-22 16:54:44.721', 'postgres', '2015-07-22 16:54:44.721', '2.0.13', '127.0.0.1', 0);
INSERT INTO ta_modele_bareme (id_modele_bareme, id_modele_conformite, expression_verifiee, forcage, chemin_doc, action_corrective, qui_cree, quand_cree, qui_modif, quand_modif, version, 
	ip_acces, version_obj) VALUES (6, 7, 'val>=1.1', false, '', 'Refaire le réglage de la sertisseuse. Cf. PDF', 'postgres', '2015-07-22 16:59:14.976', 'postgres', '2015-07-22 16:59:14.976', '2.0.13', '127.0.0.1', 0);
INSERT INTO ta_modele_bareme (id_modele_bareme, id_modele_conformite, expression_verifiee, forcage, chemin_doc, action_corrective, qui_cree, quand_cree, qui_modif, quand_modif, version, 
	ip_acces, version_obj) VALUES (7, 13, 'val<=0.2', false, '', 'Refaire le réglage de la sertisseuse. Cf. PDF', 'postgres', '2015-07-22 17:06:35.753', 'postgres', '2015-07-22 17:06:35.753', '2.0.13', '127.0.0.1', 0);
INSERT INTO ta_modele_bareme (id_modele_bareme, id_modele_conformite, expression_verifiee, forcage, chemin_doc, action_corrective, qui_cree, quand_cree, qui_modif, quand_modif, version, 
	ip_acces, version_obj) VALUES (8, 14, 'val=''Bon''', false, '', 'Lot non conforme, réalisation d''une analyse de stabilité selon la norme AFNOR XP V08 408 dans un laboratoire.', 'postgres', '2015-07-22 17:09:08.81', 'postgres', '2015-07-22 17:09:08.81', '2.0.13', '127.0.0.1', 0);
INSERT INTO ta_modele_bareme (id_modele_bareme, id_modele_conformite, expression_verifiee, forcage, chemin_doc, action_corrective, qui_cree, quand_cree, qui_modif, quand_modif, version, 
	ip_acces, version_obj) VALUES (9, 15, 'val=''Bon''', false, '', 'Contrôle de la fermeture sur le lot', 'postgres', '2015-07-22 17:10:33.389', 'postgres', '2015-07-22 17:10:33.389', '2.0.13', '127.0.0.1', 0);
INSERT INTO ta_modele_bareme (id_modele_bareme, id_modele_conformite, expression_verifiee, forcage, chemin_doc, action_corrective, qui_cree, quand_cree, qui_modif, quand_modif, version, 
	ip_acces, version_obj) VALUES (10, 19, 'val>=75', false, '', 'Blocage du lot avec analyse microbiologique libératoire', 'postgres', '2015-07-22 17:13:54.532', 'postgres', '2015-07-22 17:13:54.532', '2.0.13', '127.0.0.1', 0);
INSERT INTO ta_modele_bareme (id_modele_bareme, id_modele_conformite, expression_verifiee, forcage, chemin_doc, action_corrective, qui_cree, quand_cree, qui_modif, quand_modif, version, 
	ip_acces, version_obj) VALUES (11, 23, 'val<=0.5', false, '', 'Faire étalonner les organes de mesure', 'postgres', '2015-07-22 17:28:16.763', 'postgres', '2015-07-22 17:28:16.763', '2.0.13', '127.0.0.1', 0);
INSERT INTO ta_modele_bareme (id_modele_bareme, id_modele_conformite, expression_verifiee, forcage, chemin_doc, action_corrective, qui_cree, quand_cree, qui_modif, quand_modif, version, 
	ip_acces, version_obj) VALUES (12, 24, 'val>=0.7', false, '', '', 'postgres', '2015-07-22 17:29:18.589', 'postgres', '2015-07-22 17:29:18.589', '2.0.13', '127.0.0.1', 0);
INSERT INTO ta_modele_bareme (id_modele_bareme, id_modele_conformite, expression_verifiee, forcage, chemin_doc, action_corrective, qui_cree, quand_cree, qui_modif, quand_modif, version, 
	ip_acces, version_obj) VALUES (13, 27, 'val>=7', false, '', 'Poursuivre l''étuvage', 'postgres', '2015-07-22 17:33:06.698', 'postgres', '2015-07-22 17:33:06.698', '2.0.13', '127.0.0.1', 0);
INSERT INTO ta_modele_bareme (id_modele_bareme, id_modele_conformite, expression_verifiee, forcage, chemin_doc, action_corrective, qui_cree, quand_cree, qui_modif, quand_modif, version, 
	ip_acces, version_obj) VALUES (14, 28, 'val=''Bon''', false, '', 'Blocage du lot en chambre froide analyse complémentaire, destruction du lot ou recyclage avec maîtrise du risque sanitaire, ENREGISTREMENT DE L''ACTION CORRECTION', 'postgres', '2015-07-22 17:35:40.062', 'postgres', '2015-07-22 17:35:40.062', '2.0.13', '127.0.0.1', 0);
INSERT INTO ta_modele_bareme (id_modele_bareme, id_modele_conformite, expression_verifiee, forcage, chemin_doc, action_corrective, qui_cree, quand_cree, qui_modif, quand_modif, version, 
	ip_acces, version_obj) VALUES (15, 31, 'val<0.5', false, '', 'Le lot doit être détruit.', 'postgres', '2015-07-22 17:39:17.458', 'postgres', '2015-07-22 17:39:17.458', '2.0.13', '127.0.0.1', 0);


--
-- TOC entry 5161 (class 0 OID 0)
-- Dependencies: 372
-- Name: ta_modele_bareme_id_modele_bareme_seq; Type: SEQUENCE SET; Schema: demo; Owner: postgres
--

SELECT pg_catalog.setval('ta_modele_bareme_id_modele_bareme_seq', 15, true);
