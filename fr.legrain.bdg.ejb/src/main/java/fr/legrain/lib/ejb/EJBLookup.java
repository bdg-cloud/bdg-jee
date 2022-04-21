//package fr.legrain.lib.ejb;
//
//import javax.naming.Context;
//import javax.naming.NamingException;
//
//public class EJBLookup<I> extends EJBLookupName {
//	
//	public static final String EJB_NAME_TA_TIERS_SERVICE      = "TaTiersService";
//	public static final String EJB_NAME_TA_TIERS_SERVICE_STATELESS  = "TaTiersServiceStateless";
//	public static final String EJB_NAME_TA_TIERS_SERVICE_STATEFUL  = "TaTiersServiceStatefull";
//	
//	public static final String EJB_NAME_TA_CONTROLE  = "TaControleService";
//	
//	
//	
//	public static final String EJB_NAME_TA_LIENS_SERVICE      = "TaLiensService";
//	public static final String EJB_NAME_TA_T_TIERS_SERVICE    = "TaTTiersService";
//	public static final String EJB_NAME_TA_T_ENTITE_SERVICE   = "TaTEntiteService";
//	public static final String EJB_NAME_TA_INFO_ENTREPRISE_SERVICE   = "TaInfoEntrepriseService";
//	public static final String EJB_NAME_TA_T_TVA_DOC_SERVICE  = "TaTTvaDocService";
//	public static final String EJB_NAME_TA_T_CIVILITE_SERVICE = "TaTCiviliteService";
//	public static final String EJB_NAME_TA_T_TEL_SERVICE      = "TaTTelService";
//	public static final String EJB_NAME_TA_C_PAIEMENT_SERVICE = "TaCPaiementService";
//	public static final String EJB_NAME_TA_T_C_PAIEMENT_SERVICE = "TaTCPaiementService";
//	public static final String EJB_NAME_TA_T_TARIF_SERVICE    = "TaTTarifService";
//	public static final String EJB_NAME_TA_DOCUMENT_TIERS_SERVICE = "TaDocumentTiersService";
//	
//	public static final String EJB_NAME_TA_PARAM_PUBLIPOSTAGE_SERVICE = "TaParamPublipostageService";
//	
//	public static final String EJB_NAME_TA_EMAIL_SERVICE = "TaEmailService";
//	public static final String EJB_NAME_TA_T_EMAIL_SERVICE = "TaTEmailService";
//	
//	public static final String EJB_NAME_TA_WEB_SERVICE = "TaWebService";
//	public static final String EJB_NAME_TA_T_WEB_SERVICE = "TaTWebService";
//	
//	public static final String EJB_NAME_TA_TELEPHONE_SERVICE = "TaTelephoneService";
//	
//	public static final String EJB_NAME_TA_ADRESSE_SERVICE = "TaAdresseService";
//	public static final String EJB_NAME_TA_T_ADRESSE_SERVICE = "TaTAdrService";
//	
//	public static final String EJB_NAME_TA_ENTREPRISE_SERVICE = "TaEntrepriseService";
//	
//	public static final String EJB_NAME_TA_COMPTE_BANQUE_SERVICE = "TaCompteBanqueService";
//	public static final String EJB_NAME_TA_T_BANQUE_SERVICE = "TaTBanqueService";
//	
//	public static final String EJB_NAME_TA_T_LIENS_SERVICE = "TaTLiensService";
//	public static final String EJB_NAME_TA_NOTE_TIERS_SERVICE = "TaNoteTiersService";
//	public static final String EJB_NAME_TA_T_NOTE_TIERS_SERVICE = "TaTNoteTiersService";
//	
//	public static final String EJB_NAME_TA_FAMILLE_TIERS_SERVICE = "TaFamilleTiersService";
//	
//	public static final String EJB_NAME_TA_INFO_JURIDIQUE_TIERS_SERVICE = "TaInfoJuridiqueService";
//	public static final String EJB_NAME_TA_T_PAIEMENT_SERVICE = "TaTPaiementService";
//	
//	public static final String EJB_NAME_TA_PARAM_CREE_DOC_TIERS_SERVICE = "TaParamCreeDocTiersService";
//	public static final String EJB_NAME_TA_T_DOC_SERVICE = "TaTDocService";
//	
//	
//	public static final String EJB_NAME_TA_ARTICLE_SERVICE = "TaArticleService";
//	public static final String EJB_NAME_TA_ARTICLE_VITI_SERVICE = "TaArticleVitiService";
//	public static final String EJB_NAME_TA_CATALOGUE_WEB_SERVICE = "TaCatalogueWebService";
//	public static final String EJB_NAME_TA_CATEGORIE_ARTICLE_SERVICE = "TaCategorieArticleService";
//	public static final String EJB_NAME_TA_CONDITIONNEMENT_ARTICLE_SERVICE = "TaConditionnementArticleService";
//	public static final String EJB_NAME_TA_FAMILLE_SERVICE = "TaFamilleService";
//	public static final String EJB_NAME_TA_FAMILLE_UNITE_SERVICE = "TaFamilleUniteService";
//	public static final String EJB_NAME_TA_IMAGE_ARTICLE_SERVICE = "TaImageArticleService";
//	public static final String EJB_NAME_TA_LABEL_ARTICLE_SERVICE = "TaLabelArticleService";
//	public static final String EJB_NAME_TA_NOTE_ARTICLE_SERVICE = "TaNoteArticleService";
//	public static final String EJB_NAME_TA_PRIX_SERVICE = "TaPrixService";
//	public static final String EJB_NAME_TA_RAPPORT_UNITE_SERVICE = "TaRapportUniteService";
//	public static final String EJB_NAME_TA_R_TITRE_TRANSPORT_SERVICE = "TaRTitreTransportService";
//	public static final String EJB_NAME_TA_REF_PRIX_SERVICE = "TaRefPrixUniteService";
//	public static final String EJB_NAME_TA_T_ARTICLE_SERVICE = "TaTArticleService";
//	public static final String EJB_NAME_TA_T_CONDITIONNMENT_SERVICE = "TaTConditionnementService";
//	public static final String EJB_NAME_TA_T_NOTE_ARTICLE_SERVICE = "TaTNoteArticleService";
//	public static final String EJB_NAME_TA_T_TVA_SERVICE = "TaTTvaService";
//	public static final String EJB_NAME_TA_TVA_SERVICE = "TaTvaService";
//	public static final String EJB_NAME_TA_UNITE_SERVICE = "TaUniteService";
//	
//	public static final String EJB_NAME_TA_UTILISATEUR_SERVICE = "TaUtilisateurService";
//	
//	public static final String EJB_NAME_TA_STOCK_CAPSULES_SERVICE = "TaStockCapsulesService";
//	public static final String EJB_NAME_TA_TITRE_TRANSPORT_SERVICE = "TaTitreTransportService";
//	
//	public static final String EJB_NAME_TA_DEVIS_SERVICE = "TaDevisService";
//	public static final String EJB_NAME_TA_L_DEVIS_SERVICE = "TaLDevisService";
//	public static final String EJB_NAME_TA_INFOS_DEVIS_SERVICE = "TaInfosDevisService";
//	public static final String EJB_NAME_TA_T_LIGNE_SERVICE = "TaTLigneService";
//	
//	
//	public static final String EJB_NAME_TA_FACTURE_SERVICE = "TaFactureService";
//	public static final String EJB_NAME_TA_L_FACTURE_SERVICE = "TaLFactureService";
//	public static final String EJB_NAME_TA_INFOS_FACTURE_SERVICE = "TaInfosFactureService";
//	
//	
//	public static final String EJB_NAME_TA_COMPL_SERVICE  = "TaComplService";
//	
//	public static final String EJB_NAME_TA_COMPTE_POINT_SERVICE  = "TaComptePointService";
//	
//	public static final String EJB_NAME_TA_ROLE_SERVICE = "TaRoleService";
//	public static final String EJB_NAME_TA_AUTORISATIONS_SERVICE = "TaAutorisationsService";
//	public static final String EJB_NAME_TA_TYPE_PRODUIT_SERVICE = "TaTypeProduitService";
//	
//	
//	public static final String EJB_NAME_TA_TYPE_MOUVEMENT_SERVICE = "TaTypeMouvementService";
//	public static final String EJB_NAME_TA_LOT_SERVICE = "TaLotService";
//	public static final String EJB_NAME_TA_ENTREPOT_SERVICE = "TaEntrepotService";
//	
//	
//	
//	public I doLookup(Context context, String beanName, final String interfaceName) {
//		return doLookup(context, beanName, interfaceName, false);
//	}
//
//
//	/**
//	 * I - interface de l'EJB rechercher : ex : (fr.legrain.bdg.tiers.service.remote.)ITaTiersServiceRemote
//	 * beanName - nom du bean : ex : TaTiersService
//	 * @return
//	 */
//	public I doLookup(Context context, String beanName, final String interfaceName, boolean statefull) {
//		//Context context = null;
//		I bean = null;
//		try {
//			// 1. Obtaining Context
//			//context = JNDILookupClass.getInitialContext();
//			// 2. Generate JNDI Lookup name
//			//String beanName = "TaTiersService";
//			//final String interfaceName = I.class.getName();
//			String lookupName = getLookupName(beanName,interfaceName);
//			
//			if(statefull) {
//				lookupName += "?stateful";
//			}
//			
//			System.err.println(lookupName);
//			
//			// 3. Lookup and cast
//			bean = (I) context.lookup(lookupName);
//
//		} catch (NamingException e) {
//			e.printStackTrace();
//		}
//		return bean;
//	}
//
//}
