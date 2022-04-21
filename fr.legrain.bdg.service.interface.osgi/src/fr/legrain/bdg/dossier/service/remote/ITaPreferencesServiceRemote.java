package fr.legrain.bdg.dossier.service.remote;

import java.util.List;

import javax.ejb.FinderException;
import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.dossier.dto.TaPreferencesDTO;
import fr.legrain.dossier.model.TaPreferences;

@Remote
public interface ITaPreferencesServiceRemote extends IGenericCRUDServiceRemote<TaPreferences,TaPreferencesDTO>,
														IAbstractLgrDAOServer<TaPreferences>,IAbstractLgrDAOServerDTO<TaPreferencesDTO>{
	public static final String validationContext = "PREFERENCES";
	
	//consante qui doivent correspondre au champ "cle" dans la table ta_preferences
	public static final String P_CODE_BARRE_PREFIXE_ENTREPRISE = "code_barre_prefixe_entreprise";
	public static final String P_CODE_BARRE_DEBUT_PLAGE = "code_barre_debut_plage";
	public static final String P_CODE_BARRE_FIN_PLAGE = "code_barre_fin_plage";
	
	public static final String P_LISTE_CHOIX = "liste_choix";
	public static final String P_COUPURE_LIGNE = "coupureLigne";
	public static final String P_PAGE_BREAK_MAXI = "pageBreakMaxi";
	public static final String P_PAGE_BREAK_TOTAUX = "pageBreakTotaux";
	public static final String P_IMPRESSION_DIRECT = "ImpressionDirect";
	public static final String P_COMMENTAIRE = "commentaire";
	
	public static final String REPERTOIRE_EXPORTATION = "Repertoire_exportation";
//	public static final String TYPE_REGLEMENT = "Tous les types de réglements";
	public static final String ACOMPTES = "Acomptes";
	public static final String APPORTEUR = "Apporteur";
	public static final String REGLEMENT_SIMPLE = "Reglement_simple";
	public static final String REMISE = "Remise";
	public static final String REGLEMENTS_LIES = "Transfert_reglements_lies_au_document";
	public static final String DOCUMENTS_LIES = "Transfert_documents_lies_au_reglement";
	public static final String POINTAGES = "Transfert_pointages";
	public static final String CENTRALISER_ECRITURES = "Centraliser_Les_Ecritures";
	public static final String JOURNAL_FACTURE = "journal_facture";
	public static final String JOURNAL_AVOIR = "journal_avoir";
	public static final String JOURNAL_APPORTEUR = "journal_apporteur";
	public static final String JOURNAL_REGLEMENT = "journal_reglement";
	public static final String JOURNAL_REMISE = "journal_remise";
	
	public static final String EXPORTATION = "exportation";
	public static final String IMPORTATION = "importation";
	public static final String TYPE_PAIEMENT_SHIPPINGBO = "type_paiement_shippingbo";
	public static final String TYPE_PAIEMENT_MENSURA = "type_paiement_mensura";
	public static final String TYPE_PAIEMENT_PRESTASHOP = "type_paiement_prestashop";
	public static final String TYPE_PAIEMENT_WOOCOMMERCE = "type_paiement_woocommerce";
	
	
	public static final String CAISSE_CODE_TIERS_DEFAUT = "tiers_defaut_caisse";
	public static final String GROUPE_PREFERENCES_CAISSE = "caisse";
	
	public static final String GROUPE_PREFERENCES_ENVOI_EMAIL = "envoi_email";
	public static final String UTILISE_SERVICE_WEB_EXTERNE_POUR_EMAIL = "utilise_service_web_externe";
	public static final String EMAIL_SMTP_HOST = "email_smtp_host";
	public static final String EMAIL_SMTP_PORT = "email_smtp_port";
	public static final String EMAIL_SMTP_SSL = "email_smtp_ssl";
	public static final String EMAIL_SMTP_LOGIN = "email_smtp_login";
	public static final String EMAIL_SMTP_PASSWORD = "email_smtp_password";
	
	public static final String EMAIL_ADRESSE_EXPEDITEUR_DEFAUT = "email_adresse_expediteur_defaut";
	public static final String EMAIL_NOM_EXPEDITEUR_DEFAUT = "email_nom_expediteur_defaut";
	public static final String EMAIL_REPONDRE_A_DEFAUT = "email_repondre_a_defaut";
	public static final String EMAIL_ADRESSE_COMPTABLE_DEFAUT = "email_adresse_comptable_defaut";
	
	public static final String GROUPE_PREFERENCES_ENVOI_SMS = "envoi_sms";
	public static final String SMS_NOM_EXPEDITEUR_DEFAUT = "sms_nom_expediteur_defaut";
	public static final String SMS_NUMERO_EXPEDITEUR_DEFAUT = "email_numero_expediteur_defaut";
	
	public static final String CLE_API_GANDI_PROD ="cle_api_gandi_prod";
	public static final String CLE_API_GANDI_TEST ="cle_api_gandi_test";
	public static final String URL_API_GANDI_PROD ="url_api_gandi_prod";
	public static final String URL_API_GANDI_TEST ="url_api_gandi_test";
	public static final String CLE_SECURITE_API_GANDI ="cle_securite_api_gandi";
	public static final String RESELLER_GANDI ="reseller_gandi";
	public static final String AFFICHAGE_MENU_GANDI ="affichage_menu_gandi";
	public static final String CLE_API_OVH_PROD ="cle_api_ovh_prod";
	
	public static final String GROUPE_PREFERENCES_CRD = "CRD";
	
	public static final String GROUPE_PREFERENCES_STOCKS = "Stocks";
	public static final String STOCK_AUTORISER_STOCK_NEGATIF   = "autoriser_stocks_negatif";
	public static final String STOCK_ALERTE_STOCK_INFERIEUR_STOCK_MINI = "alerte_stock_inferieur_au_stock_minimum";
	public static final String STOCK_MOUVEMENTER_STOCK_POUR_LIGNE_AVOIR = "mouvementer_stock_pour_ligne_avoir";
	
	public static final String STOCK_MOUVEMENTER_CRD_POUR_LIGNE_AVOIR = "mouvementerCRDAvoir";
	
	public static final String GROUPE_PREFERENCES_LOTS = "Lots";
	public static final String LOTS_GESTION_DES_LOTS = "gestion_des_lots";
	public static final String LOTS_AUTORISER_UTILISATION_LOTS_NON_CONFORME = "autoriser_utilisation_lots_non_conforme";
	public static final String LOTS_AUTORISER_UTILISATION_CODE_LOT_FAB_PF = "Autoriser_generation_code_lot_Fab_PF";
	public static final String LOTS_AUTORISER_UTILISATION_CODE_LOT_BON_RECEPTION = "Autoriser_generation_code_lot_Bon_reception";
	
	public static final String GROUPE_PREFERENCES_DDM_DLC = "DLC";
	public static final String MAJ_DLC_FAB = "maj_dlc_fabrication";
	
	public static final String GROUPE_PREFERENCES_NB_DECIMALES = "NbDecimales";
	public static final String NB_DECIMALES_PRIX = "NbDecimalesPrix";
	public static final String NB_DECIMALES_QTE = "NbDecimalesQte";	

	
	public static final String GROUPE_PREFERENCES_OPTIMISATION = "optimisation";
	public static final String NB_CHARGE_MAX_LISTE_ARTICLE = "nbMaxChargeListeArticle";	
	public static final String NB_CHARGE_MAX_LISTE_TIERS = "nbMaxChargeListeTiers";	
	public static final String GROS_DOSSIER = "grosDossier";	
	public static final String GROS_FICHIER_ARTICLE = "grosFichierArticle";	
	public static final String GROS_FICHIER_TIERS = "grosFichierTiers";	

	public static final String GROUPE_PREFERENCES_RECHERCHE = "recherche";
	public static final String COMMENTAIRE_RECHERCHE_ARTICLE = "commentaire_dans_recherche_article";	
	public static final String COMMENTAIRE_RECHERCHE_TIERS = "commentaire_dans_recherche_tiers";
	
	public static final String OPTION_REPRISE_INFOS_DOC_GENERATION = "option_reprise_infos_doc_generation";
	

	public List<TaPreferences> findByGroupe(String groupe);
	public TaPreferences findByGoupeAndCle(String groupe,String cle); 
	
	public List<TaPreferences> findByCategorie(int idCategorie);
	
	public String retourPreferenceString(String groupe,String cle);
	public Boolean retourPreferenceBoolean(String groupe,String cle);
	public Integer retourPreferenceInteger(String groupe,String cle);
	
	public void  mergePreferenceBoolean(String groupe,String cle,Boolean valeur);
	public void  mergePreferenceInteger(String groupe,String cle,Integer valeur);
	public void  mergePreferenceString(String groupe,String cle,String valeur);
	
	public void chargePrefGeneral();
	public Integer nbMaxChargeListeArticle();
	public Integer nbMaxChargeListeTiers();
	public Boolean grosDossier();
	public Boolean grosFichierArticle();
	public Boolean grosFichierTiers();
	public void razPrefGeneral();
}
