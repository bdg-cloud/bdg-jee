

SELECT f_insert('ta_controle', 'champ =''TaLFabrication.qte2LDocument'' and contexte =''L_FABRICATION''',
'INSERT INTO ta_controle(champ, contexte, controle_defaut, controle_utilisateur) VALUES ( ''TaLFabrication.qte2LDocument'', ''L_FABRICATION'', 100, 100)',
null);



-- Mise a jour du numero de version (ACTIVER CETTE LIGNE AVANT LE DEPLOIEMENT DE LA NOUVELLE VERSION)
--update ta_version set old_version = '3.0.0', num_version = '3.1.0', date_version='2016/02/15'