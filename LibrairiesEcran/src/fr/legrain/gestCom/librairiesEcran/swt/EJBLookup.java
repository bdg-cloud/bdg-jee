package fr.legrain.gestCom.librairiesEcran.swt;

import javax.naming.Context;
import javax.naming.NamingException;

import fr.legrain.bdg.tiers.service.remote.ITaParamCreeDocTiersServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaParamPublipostageServiceRemote;


public class EJBLookup<I> extends EJBLookupName {
	
	public static final String EJB_NAME_TA_TIERS_SERVICE      = "TaTiersService";
	public static final String EJB_NAME_TA_TIERS_SERVICE_STATELESS  = "TaTiersServiceStateless";
	public static final String EJB_NAME_TA_TIERS_SERVICE_STATEFUL  = "TaTiersServiceStatefull";
	
	public static final String EJB_NAME_TA_LIENS_SERVICE      = "TaLiensService";
	public static final String EJB_NAME_TA_T_TIERS_SERVICE    = "TaTTiersService";
	public static final String EJB_NAME_TA_T_ENTITE_SERVICE   = "TaTEntiteService";
	public static final String EJB_NAME_TA_INFO_ENTREPRISE_SERVICE   = "TaInfoEntrepriseService";
	public static final String EJB_NAME_TA_T_TVA_DOC_SERVICE  = "TaTTvaDocService";
	public static final String EJB_NAME_TA_T_CIVILITE_SERVICE = "TaTCiviliteService";
	public static final String EJB_NAME_TA_T_TEL_SERVICE      = "TaTTelService";
	public static final String EJB_NAME_TA_C_PAIEMENT_SERVICE = "TaCPaiementService";
	public static final String EJB_NAME_TA_T_C_PAIEMENT_SERVICE = "TaTCPaiementService";
	public static final String EJB_NAME_TA_T_TARIF_SERVICE    = "TaTTarifService";
	public static final String EJB_NAME_TA_DOCUMENT_TIERS_SERVICE = "TaDocumentTiersService";
	
	public static final String EJB_NAME_TA_PARAM_PUBLIPOSTAGE_SERVICE = "TaParamPublipostageService";
	
	public static final String EJB_NAME_TA_EMAIL_SERVICE = "TaEmailService";
	public static final String EJB_NAME_TA_T_EMAIL_SERVICE = "TaTEmailService";
	
	public static final String EJB_NAME_TA_WEB_SERVICE = "TaWebService";
	public static final String EJB_NAME_TA_T_WEB_SERVICE = "TaTWebService";
	
	public static final String EJB_NAME_TA_TELEPHONE_SERVICE = "TaTelephoneService";
	
	public static final String EJB_NAME_TA_ADRESSE_SERVICE = "TaAdresseService";
	public static final String EJB_NAME_TA_T_ADRESSE_SERVICE = "TaTAdrService";
	
	public static final String EJB_NAME_TA_ENTREPRISE_SERVICE = "TaEntrepriseService";
	
	public static final String EJB_NAME_TA_COMPTE_BANQUE_SERVICE = "TaCompteBanqueService";
	public static final String EJB_NAME_TA_T_BANQUE_SERVICE = "TaTBanqueService";
	
	public static final String EJB_NAME_TA_T_LIENS_SERVICE = "TaTLiensService";
	public static final String EJB_NAME_TA_NOTE_TIERS_SERVICE = "TaNoteTiersService";
	public static final String EJB_NAME_TA_T_NOTE_TIERS_SERVICE = "TaTNoteTiersService";
	
	public static final String EJB_NAME_TA_FAMILLE_TIERS_SERVICE = "TaFamilleTiersService";
	
	public static final String EJB_NAME_TA_INFO_JURIDIQUE_TIERS_SERVICE = "TaInfoJuridiqueService";
	public static final String EJB_NAME_TA_T_PAIEMENT_SERVICE = "TaTPaiementService";
	
	public static final String EJB_NAME_TA_PARAM_CREE_DOC_TIERS_SERVICE = "TaParamCreeDocTiersService";
	public static final String EJB_NAME_TA_T_DOC_SERVICE = "TaTDocService";
	
	
	
	
	
	public I doLookup(Context context, String beanName, final String interfaceName) {
		return doLookup(context, beanName, interfaceName, false);
	}


	/**
	 * I - interface de l'EJB rechercher : ex : (fr.legrain.bdg.tiers.service.remote.)ITaTiersServiceRemote
	 * beanName - nom du bean : ex : TaTiersService
	 * @return
	 */
	public I doLookup(Context context, String beanName, final String interfaceName, boolean statefull) {
		//Context context = null;
		I bean = null;
		try {
			// 1. Obtaining Context
			//context = JNDILookupClass.getInitialContext();
			// 2. Generate JNDI Lookup name
			//String beanName = "TaTiersService";
			//final String interfaceName = I.class.getName();
			String lookupName = getLookupName(beanName,interfaceName);
			
			if(statefull) {
				lookupName += "?stateful";
			}
			
			System.err.println(lookupName);
			
			// 3. Lookup and cast
			bean = (I) context.lookup(lookupName);

		} catch (NamingException e) {
			e.printStackTrace();
		}
		return bean;
	}

}
