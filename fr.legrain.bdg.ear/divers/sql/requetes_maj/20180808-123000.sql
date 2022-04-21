--set schema 'demo';


---Début maj pour branche codeEAN128----------
-----------------------------------------
drop table ta_param_ean128;
CREATE TABLE ta_param_ean128
(
  id_param_ean128 serial NOT NULL,
  code_128 dlib50,
  libelle dlib255,
  long_ai int,
  data_type dlib50,
  long_borne_max int,
  long_variable boolean,
  type_conversion dlib50,
  unite_defaut dlib50,
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_param_ean128_pkey PRIMARY KEY (id_param_ean128))
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_param_ean128
  OWNER TO bdg;



-- Trigger: tbi_ta_param_ean128 on ta_param_ean128

-- DROP TRIGGER tbi_ta_param_ean128 ON ta_param_ean128;

CREATE TRIGGER tbi_ta_param_ean128
  BEFORE INSERT
  ON ta_param_ean128
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

-- Trigger: tbu_ta_param_ean128 on ta_param_ean128

-- DROP TRIGGER tbu_ta_param_ean128 ON ta_param_ean128;

CREATE TRIGGER tbu_ta_param_ean128
  BEFORE UPDATE
  ON ta_param_ean128
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();
  
    	INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('00', 'Num sequentiel de colis <SSCC>', 2, 'numeric', 18, false,'','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('01', 'code EAN13 suremballage', 2, 'numeric', 14, false,'','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('02', 'code EAN article', 2, 'numeric', 14, false,'','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('10', 'num lot fabrication', 2, 'alphanumeric', 20, true,'','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('11', 'Date fabrication AAMMJJ', 2, 'numeric', 6, false,'date','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('12', 'Mois de fabrication_AAMM', 2, 'numeric', 4, false,'mois','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('13', 'Date d''emballage_AAMMJJ', 2, 'numeric', 6, false,'date','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('14', 'Mois d''emballage_AAMM', 2, 'numeric', 4, false,'mois','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('15', 'date de durabilité minimale (DDM) AAMMJJ', 2, 'numeric', 6, false,'date','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('16', 'mois de durabilité minimale (DDM) AAMM', 2, 'numeric', 4, false,'mois','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('17', 'Date limite de consommation (DLC) AAMMJJ', 2, 'numeric', 6, false,'date','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('18', 'Mois limite de consommation (DLC) AAMM', 2, 'numeric', 4, false,'mois','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('20', 'Variante produit «variante promotionnelle»', 2, 'numeric', 2, false,'','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('21', 'Numéro de série', 2, 'alphanumeric', 20, true,'','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('22', 'HIBCC Secteur de la santé', 2, 'alphanumeric', 29, true,'','');
        
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('230', 'Num lot', 3, 'numeric', 19, true,'','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('231', 'Num lot', 3, 'numeric', 19, true,'','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('232', 'Num lot', 3, 'numeric', 19, true,'','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('233', 'Num lot', 3, 'numeric', 19, true,'','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('234', 'Num lot', 3, 'numeric', 19, true,'','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('235', 'Num lot', 3, 'numeric', 19, true,'','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('236', 'Num lot', 3, 'numeric', 19, true,'','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('237', 'Num lot', 3, 'numeric', 19, true,'','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('238', 'Num lot', 3, 'numeric', 19, true,'','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('239', 'Num lot', 3, 'numeric', 19, true,'','');
        
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('240', 'Identification complémentaire de produit', 3, 'alphanumeric', 30, true,'','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('241', 'Code article client', 3, 'alphanumeric', 30, true,'','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('250', 'Numéro de série secondaire', 3, 'alphanumeric', 30, true,'','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('251', 'ReferenceToTheBasisUnit', 3, 'alphanumeric', 30, true,'','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('252', 'GlobalIdentifierSerialisedForTrade', 3, 'numeric', 2, false,'','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('30',  'Quantité unitaire', 2, 'numeric', 8, true,'','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('310d', 'poids en Kg', 4, 'numeric', 6, false,'decimal','Kg');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('311d', 'longueur en mètre', 4, 'numeric', 6, false,'decimal','M');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('312d', 'largeur en mètre', 4, 'numeric', 6, false,'decimal','M');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('313d', 'hauteur en mètre', 4, 'numeric', 6, false,'decimal','M');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('314d', 'Surface_SquareMeter', 4, 'numeric', 6, false,'decimal','M3');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('315d', 'Volume en Litre', 4, 'numeric', 6, false,'decimal','l');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('316d', 'Volume net en mètres cube', 4, 'numeric', 6, false,'decimal','M3');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('320d', 'Poids net en livres', 4, 'numeric', 6, false,'decimal','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('321d', 'Length_Inches', 4, 'numeric', 6, false,'decimal','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('322d', 'Length_Feet', 4, 'numeric', 6, false,'decimal','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('323d', 'Length_Yards', 4, 'numeric', 6, false,'decimal','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('324d', 'Width_Inches', 4, 'numeric', 6, false,'decimal','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('325d', 'Width_Feed', 4, 'numeric', 6, false,'decimal','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('326d', 'Width_Yards', 4, 'numeric', 6, false,'decimal','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('327d', 'Heigth_Inches', 4, 'numeric', 6, false,'','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('328d', 'Heigth_Feed', 4, 'numeric', 6, false,'decimal','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('329d', 'Heigth_Yards', 4, 'numeric', 6, false,'decimal','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('330d', 'Poids brut en kilogrammes', 4, 'numeric', 6, false,'decimal','Kg');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('331d', 'Length_Meter', 4, 'numeric', 6, false,'decimal','M');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('332d', 'Width_Meter', 4, 'numeric', 6, false,'decimal','M');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('333d', 'Heigth_Meter', 4, 'numeric', 6, false,'decimal','M');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('334d', 'Surface_SquareMeter', 4, 'numeric', 6, false,'decimal','M');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('335d', 'Volume brut en litres', 4, 'numeric', 6, false,'decimal','L');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('336d', 'Volume brut en mètres cube', 4, 'numeric', 6, false,'decimal','M3');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('337d', 'Poids en kilogrammes au mètre carré', 4, 'numeric', 6, false,'decimal','Kg');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('340d', 'Poids brut en livres', 4, 'numeric', 6, false,'decimal','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('341d', 'Length_Inches', 4, 'numeric', 6, false,'decimal','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('342d', 'Length_Feet', 4, 'numeric', 6, false,'decimal','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('343d', 'Length_Yards', 4, 'numeric', 6, false,'decimal','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('344d', 'Width_Inches', 4, 'numeric', 6, false,'decimal','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('345d', 'Width_Feed', 4, 'numeric', 6, false,'decimal','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('346d', 'Width_Yards', 4, 'numeric', 6, false,'decimal','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('347d', 'Heigth_Inches', 4, 'numeric', 6, false,'decimal','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('348d', 'Heigth_Feed', 4, 'numeric', 6, false,'decimal','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('349d', 'Heigth_Yards', 4, 'numeric', 6, false,'decimal','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('350d', 'Surface_SquareInches', 4, 'numeric', 6, false,'decimal','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('351d', 'Surface_SquareFeet', 4, 'numeric', 6, false,'decimal','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('352d', 'Surface_SquareYards', 4, 'numeric', 6, false,'decimal','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('353d', 'Surface_SquareInches', 4, 'numeric', 6, false,'decimal','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('354d', 'Surface_SquareFeed', 4, 'numeric', 6, false,'decimal','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('355d', 'Surface_SquareYards', 4, 'numeric', 6, false,'decimal','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('356d', 'NetWeight_TroyOunces', 4, 'numeric', 6, false,'decimal','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('357d', 'NetVolume_Ounces', 4, 'numeric', 6, false,'decimal','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('360d', 'NetVolume_Quarts', 4, 'numeric', 6, false,'decimal','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('361d', 'NetVolume_Gallonen', 4, 'numeric', 6, false,'decimal','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('362d', 'GrossVolume_Quarts', 4, 'numeric', 6, false,'decimal','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('363d', 'GrossVolume_Gallonen', 4, 'numeric', 6, false,'decimal','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('364d', 'NetVolume_CubicInches', 4, 'numeric', 6, false,'decimal','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('365d', 'NetVolume_CubicFeet', 4, 'numeric', 6, false,'decimal','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('366d', 'NetVolume_CubicYards', 4, 'numeric', 6, false,'decimal','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('367d', 'GrossVolume_CubicInches', 4, 'numeric', 6, false,'decimal','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('368d', 'GrossVolume_CubicFeet', 4, 'numeric', 6, false,'decimal','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('369d', 'GrossVolume_CubicYards', 4, 'numeric', 6, false,'decimal','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('37',   'Quantité', 2, 'numeric', 8, true,'','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('390d', 'AmountDue_DefinedValutaBand', 4, 'numeric', 15, true,'decimal','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('391d', 'AmountDue_WithISOValutaCode', 4, 'numeric', 18, true,'decimal','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('392d', 'BePayingAmount_DefinedValutaBand', 4, 'numeric', 15, true,'decimal','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('393d', 'BePayingAmount_WithISOValutaCode', 4, 'numeric', 18, true,'decimal','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('400', 'Numéro de commande de l''acheteur', 3, 'alphanumeric', 30, true,'','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('401', 'Numéro d''expédition', 3, 'alphanumeric', 30, true,'','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('402', 'Numéro d''expédition fournisseur', 3, 'numeric', 17, false,'','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('403', 'Code de routage', 3, 'alphanumeric', 30, true,'','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('410', 'EAN_UCC_GlobalLocationNumber(GLN) Lieu de livraison', 3, 'numeric', 13, false,'','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('411', 'EAN_UCC_GlobalLocationNumber(GLN)  Lieu d''envoi de la facture', 3, 'numeric', 13, false,'','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('412', 'EAN_UCC_GlobalLocationNumber(GLN) Lieu de qui fournit', 3, 'numeric', 13, false,'','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('413', 'EAN_UCC_GlobalLocationNumber(GLN) Lieu final de destination', 3, 'numeric', 13, false,'','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('414', 'EAN_UCC_GlobalLocationNumber(GLN) Lieu fonction EAN', 3, 'numeric', 13, false,'','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('415', 'EAN_UCC_GlobalLocationNumber(GLN)_ToBilligParticipant', 3, 'numeric', 13, false,'','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('420', 'Code postal du lieu de livraison', 3, 'alphanumeric', 20, true,'','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('421', 'Code ISO + postal du lieu de livraison', 3, 'alphanumeric', 12, true,'','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('422', 'Pays d''origine du produit', 3, 'numeric', 3, false,'','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('7001', 'Nato Stock Number', 4, 'numeric', 13, false,'','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('8001', 'Produit en rouleau', 4, 'numeric', 14, false,'','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('8002', 'N° de série de téléphone mobile', 4, 'alphanumeric', 20, true,'','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('8003', 'Code Ean + n° de série d''objet retournable', 4, 'alphanumeric', 34, true,'','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('8004', 'Identification d''un actif individuel', 4, 'numeric', 30, true,'','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('8005', 'Prix à l''unité de mesure', 4, 'numeric', 6, false,'','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('8006', 'Composant d''un article', 4, 'numeric', 18, false,'','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('8007', 'IBAN', 4, 'alphanumeric', 30, true,'','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('8008', 'DataAndTimeOfManufacturing', 4, 'numeric', 12, true,'','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('8018', 'Identification d''une prestation de service', 4, 'numeric', 18, false,'','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('8020', 'NumberBillCoverNumber', 4, 'alphanumeric', 25, false,'','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('8100', 'Code coupon UCC', 4, 'numeric', 6, false,'','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('8101', 'Code coupon UCC (variante 1)', 4, 'numeric', 10, false,'','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('8102', 'Code coupon UCC (variante 2)', 4, 'numeric', 2, false,'','');
        INSERT INTO ta_param_ean128(code_128, libelle,long_ai, data_type, long_borne_max,long_variable,type_conversion,unite_defaut)VALUES ('90',   'Usage interne ou propre à deux entreprises', 2, 'alphanumeric', 30, true,'','');  


--- fin maj pour branche codeEAN128----------
-----------------------------------------
