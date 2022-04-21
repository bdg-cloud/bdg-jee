package fr.legrain.generation.service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.DeclareRoles;
import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import fr.legrain.article.model.TaArticle;
import fr.legrain.article.model.TaFabrication;
import fr.legrain.article.model.TaLot;
import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaLotServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaTypeMouvementServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaBoncdeAchatServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaDevisServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaFactureServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaRDocumentServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaReglementServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaTLigneServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaPreferencesServiceRemote;
import fr.legrain.bdg.general.service.remote.IGenereDocAcompteVersBonLivraisonServiceRemote;
import fr.legrain.bdg.generation.service.remote.IAbstractGenereDocFlashServiceRemote;
import fr.legrain.bdg.generation.service.remote.IAbstractGenereDocLigneServiceWebExterneServiceRemote;
import fr.legrain.bdg.generation.service.remote.IAbstractGenereDocServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocAcompteVersAcompteServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocAcompteVersApporteurServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocAcompteVersAvoirServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocAcompteVersBonCommandeServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocAcompteVersDevisServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocAcompteVersFactureServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocAcompteVersPrelevementServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocAcompteVersProformaServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocApporteurVersApporteurServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocAvisEcheanceVersAvisEcheanceServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocAvisEcheanceVersFactureServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocAvisEcheanceVersPrelevementServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocAvoirVersAvoirServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocBonCommandeAchatVersBonReceptionServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocBonCommandeVersApporteurServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocBonCommandeVersAvoirServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocBonCommandeVersBonCommandeServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocBonCommandeVersBonLivraisonServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocBonCommandeVersDevisServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocBonCommandeVersFactureServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocBonCommandeVersPrelevementServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocBonCommandeVersProformaServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocBonLivraisonVersApporteurServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocBonLivraisonVersAvoirServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocBonLivraisonVersBonLivraisonServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocBonLivraisonVersCommandeServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocBonLivraisonVersDevisServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocBonLivraisonVersFactureServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocBonLivraisonVersPrelevementServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocBonLivraisonVersProformaServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocDevisVersApporteurServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocDevisVersAvoirServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocDevisVersBonCommandeServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocDevisVersBonLivraisonServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocDevisVersDevisServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocDevisVersFactureServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocDevisVersPrelevementServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocDevisVersProformaServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocFLashVersPreparationServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocFactureVersApporteurServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocFactureVersAvisEcheanceServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocFactureVersAvoirServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocFactureVersBonLivraisonServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocFactureVersCommandeServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocFactureVersDevisServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocFactureVersFactureServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocFactureVersPrelevementServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocFactureVersProformaServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocLigneServiceWebExterneVersDevisServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocLigneServiceWebExterneVersfactureServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocPrelevementVersApporteurServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocPrelevementVersAvoirServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocPrelevementVersBonCommandeServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocPrelevementVersBonLivraisonServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocPrelevementVersDevisServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocPrelevementVersFactureServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocPrelevementVersPrelevementServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocPrelevementVersProformaServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocProformaVersApporteurServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocProformaVersAvoirServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocProformaVersBonCommandeServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocProformaVersBonLivraisonServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocProformaVersDevisServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocProformaVersFactureServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocProformaVersPrelevementServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocProformaVersProformaServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocTicketDeCaisseVersFactureServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaLigneServiceWebExterneServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaCPaiementServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTAdrServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTCPaiementServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTPaiementServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.IInfosDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.model.RetourGenerationDoc;
import fr.legrain.document.model.TaAcompte;
import fr.legrain.document.model.TaApporteur;
import fr.legrain.document.model.TaAvisEcheance;
import fr.legrain.document.model.TaAvoir;
import fr.legrain.document.model.TaBonReception;
import fr.legrain.document.model.TaBoncde;
import fr.legrain.document.model.TaBoncdeAchat;
import fr.legrain.document.model.TaBonliv;
import fr.legrain.document.model.TaDevis;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaFlash;
import fr.legrain.document.model.TaInfosDevis;
import fr.legrain.document.model.TaInfosFacture;
import fr.legrain.document.model.TaInfosPreparation;
import fr.legrain.document.model.TaLDevis;
import fr.legrain.document.model.TaLFacture;
import fr.legrain.document.model.TaLFlash;
import fr.legrain.document.model.TaPrelevement;
import fr.legrain.document.model.TaPreparation;
import fr.legrain.document.model.TaProforma;
import fr.legrain.document.model.TaRReglement;
import fr.legrain.document.model.TaReglement;
import fr.legrain.document.model.TaTLigne;
import fr.legrain.document.model.TaTPaiement;
import fr.legrain.document.model.TaTicketDeCaisse;
import fr.legrain.document.model.TypeDoc;
import fr.legrain.documents.service.TaTLigneService;
import fr.legrain.dossier.model.TaPreferences;
import fr.legrain.generationDocument.model.ParamAfficheChoixGenerationDocument;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.lib.data.IHMEtat;
import fr.legrain.lib.data.LibChaine;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.LibDate;
import fr.legrain.servicewebexterne.model.TaLigneServiceWebExterne;
import fr.legrain.servicewebexterne.model.TaServiceWebExterne;
import fr.legrain.stock.model.TaGrMouvStock;
import fr.legrain.stock.model.TaMouvementStock;
import fr.legrain.tiers.model.TaAdresse;
import fr.legrain.tiers.model.TaCPaiement;
import fr.legrain.tiers.model.TaTCPaiement;
import fr.legrain.tiers.model.TaTiers;


@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class CreationDocumentLigneServiceWebExterneMultiple implements java.io.Serializable {


	private static final long serialVersionUID = -2836730580231914189L;

	private String nouveauLibelle="";
//	private List<IDocumentTiers> listeDocument = null;
//	private List<ILigneDocumentTiers> listeLigneDocument = null;
	
	private String typeAdresseFacturation="";
	private String typeAdresseLivraison="";
	
	private List<TaLigneServiceWebExterne> listeLigneServiceWeb = null;
	
	private String selectedType="";
	private Date dateDocument = null;
	private Date dateLivDocument = null;
	private TaFlash documentFlashSrc = null;
	private ParamAfficheChoixGenerationDocument param;
	private String codeTiers=null;
	private String idDocumentExterne=null;
	private boolean relation=false;
	private boolean changeTiers=false;
	private boolean generationModele=false;
	private boolean generationLigne=false;
	private boolean conserveNbDecimalesDoc=false;
	
	private String codeServiceWeb = "";
	
	private boolean regleDoc = false;
	private IAbstractGenereDocLigneServiceWebExterneServiceRemote genereDocument;
	
	private @EJB ITaPreferencesServiceRemote taPreferencesService;
	@EJB private ITaBoncdeAchatServiceRemote taBoncdeAchatService;
	@EJB private ITaReglementServiceRemote taReglementService;
	@EJB private ITaTPaiementServiceRemote taTPaiementService;
	@EJB private ITaPreferencesServiceRemote taPreferenceService;

	private @EJB IGenereDocLigneServiceWebExterneVersfactureServiceRemote genereDocLigneServiceVersFactureService;
	private @EJB IGenereDocLigneServiceWebExterneVersDevisServiceRemote genereDocLigneServiceVersDevisService;

	


	private @EJB ITaTiersServiceRemote taTiersService;
	private @EJB ITaArticleServiceRemote taArticleService;
	private @EJB ITaLotServiceRemote taLotService;
	private @EJB ITaCPaiementServiceRemote taCPaiementService;
	private @EJB ITaTCPaiementServiceRemote taTCPaiementService;
	private @EJB ITaInfoEntrepriseServiceRemote taInfoEntrepriseService;	
	
	private @EJB ITaFactureServiceRemote taFactureService;	
	private @EJB ITaDevisServiceRemote taDevisService;	
	
	private @EJB ITaTAdrServiceRemote taTAdrService;
	private @EJB ITaTLigneServiceRemote taTLigneService;
	private @EJB ITaRDocumentServiceRemote taRDocumentService;
	private @EJB  ITaTypeMouvementServiceRemote taTypeMouvementService;
	
	private @EJB ITaLigneServiceWebExterneServiceRemote taLigneServiceWebExterneService;

	public CreationDocumentLigneServiceWebExterneMultiple() {

	}

	

	public RetourGenerationDoc creationDocument(boolean controleExistance) {
		RetourGenerationDoc retour=new RetourGenerationDoc();

		try {
	
			IDocumentTiers docGenere = null;
			final TypeDoc typeDocPresent = TypeDoc.getInstance();
			String typeCPaiement="";
			TaCPaiement taCPaiement1=null;
			TaCPaiement taCPaiement2=null;
			TaTiers tiers = null;
			String commentaireDefaut="";
			String libelleDocument="";
			String libelleComplementaire="";
			
			TaPreferences gestionCrd= taPreferencesService.findByCode("mouvementerCRDAvoir");
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																
			if(selectedType.equals(TypeDoc.TYPE_AVOIR)) {
				typeCPaiement=TaTCPaiement.C_CODE_TYPE_AVOIR;
				docGenere = new TaAvoir();
				libelleDocument="Avoir N°: ";
			} else if(selectedType.equals(TypeDoc.TYPE_PREPARATION)) {
				typeCPaiement=TaTCPaiement.C_CODE_TYPE_DEVIS;
				docGenere = new TaPreparation();
				libelleDocument="Preparation N°: ";
				
				
			}else if(selectedType.equals(TypeDoc.TYPE_DEVIS)) {
				typeCPaiement=TaTCPaiement.C_CODE_TYPE_DEVIS;
				docGenere = new TaDevis();
				libelleDocument="Devis N°: ";
				if(conserveNbDecimalesDoc==false) {
					TaPreferences nb;
					nb = taPreferencesService.findByGoupeAndCle(ITaPreferencesServiceRemote.GROUPE_PREFERENCES_NB_DECIMALES, ITaPreferencesServiceRemote.NB_DECIMALES_PRIX);
					if(nb!=null && nb.getValeur() != null) {
						docGenere.setNbDecimalesPrix(LibConversion.stringToInteger(nb.getValeur()));
					}

				}
				else {
					docGenere.setNbDecimalesQte(3);
				}
				commentaireDefaut=taPreferencesService.retourPreferenceString(selectedType.toLowerCase(), ITaPreferencesServiceRemote.P_COMMENTAIRE);
				if(commentaireDefaut==null)commentaireDefaut="";
		
				TaTCPaiement tcp=taTCPaiementService.findByCode(typeCPaiement);
				if(tcp!=null){
					List<TaCPaiement> liste=taCPaiementService.rechercheParType(tcp.getCodeTCPaiement());
					if(liste!=null && !liste.isEmpty()) {
					taCPaiement1=liste.get(0);
					taCPaiement2=liste.get(0);
					}
				}
				
				Boolean continuer=!controleExistance;

				if(listeLigneServiceWeb.size()>0){
					//genereDocument est null ici
					genereDocument = create(listeLigneServiceWeb.get(0), docGenere);
				}
				if(selectedType.equals(TypeDoc.TYPE_AVOIR)) {
					genereDocument.setDocumentGereLesCrd(LibConversion.StringToBoolean(gestionCrd.getValeur()));
				}
				genereDocument.setRecupLibelleSeparateurDoc(!getParam().getRepriseAucun());
				genereDocument.setLigneSeparatrice(genereDocument.getRecupLibelleSeparateurDoc());
				int nbListeDoc=0;
				boolean genererCode=false;
				if(controleExistance)controleExistance=!getParam().isGenerationLigne();
				
				
				//rajout yann
				tiers = taTiersService.findByCode(codeTiers);
				Map<String, String> paramsCode = new LinkedHashMap<String, String>();
				paramsCode.put(Const.C_NOMFOURNISSEUR, codeTiers);
				paramsCode.put(Const.C_DATEDOCUMENT, LibDate.dateToString(((TaDevis)docGenere).getDateDocument()));
				((TaDevis)docGenere).setTaTiers(tiers);
				((TaDevis)docGenere).setCodeDocument(taDevisService.genereCode(paramsCode));
				
				TaInfosDevis infos = new TaInfosDevis();
				infos.setTaDocument(((TaDevis)docGenere));
				infos=(TaInfosDevis)copieInfosDocument(infos);
				((TaDevis)docGenere).setTaInfosDocument(infos);
				((TaDevis)docGenere).setTtc(1);
				if(!LibChaine.empty(nouveauLibelle)) {
					((TaDevis)docGenere).setLibelleDocument(nouveauLibelle);
				} else {
					((TaDevis)docGenere).setLibelleDocument(libelleDocument+" "+docGenere.getCodeDocument()+" pour "+param.getTypeEntiteExterne()+" "+param.getCodeServiceExterne()+" N° "+idDocumentExterne);
				}
				//((TaFacture)docGenere).setLibelleDocument("Facture ShippingBo N°: "+docGenere.getCodeDocument());
				
				for (TaLigneServiceWebExterne document : listeLigneServiceWeb) {
					ILigneDocumentTiers lf = new TaLDevis();
					((TaLDevis) lf).initialiseLigne(false);
					TaArticle art = taArticleService.findById(document.getTaArticle().getIdArticle());
					if(document.getTaLot() != null) {
						TaLot lot = taLotService.findById(document.getTaLot().getIdLot());
						((TaLDevis) lf).setTaLot(lot);
					}
					
					((TaLDevis) lf).setTaDocument((TaDevis) docGenere);
					((TaLDevis) lf).setTaTLigne(taTLigneService.findByCode("H"));
					((TaLDevis) lf).setTaArticle(art);
					((TaLDevis) lf).setLibLDocument(art.getLibellecArticle());
					((TaLDevis) lf).setQteLDocument(BigDecimal.valueOf(document.getQteArticle()));
					((TaLDevis) lf).setCodeTvaLDocument(art.getTaTva().getCodeTva());
					((TaLDevis) lf).setCodeTTvaLDocument("F");
					
					if(art.getTaUnite1() != null) {
						((TaLDevis) lf).setU1LDocument(art.getTaUnite1().getCodeUnite());
					}
					
					((TaLDevis) lf).setCompteLDocument(art.getNumcptArticle());
					
					if(document.getMontantTtc() != null) {
						((TaLDevis) lf).setMtTtcLDocument(new BigDecimal(document.getMontantTtc()));
						BigDecimal montantHt = lf.getMtTtcLDocument().divide(BigDecimal.valueOf(1).add(
								 (art.getTaTva().getTauxTva().divide(new BigDecimal(100),MathContext.DECIMAL128).setScale(3,BigDecimal.ROUND_HALF_UP)
							)),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP);
						((TaLDevis) lf).setMtHtLDocument(montantHt);
						//BigDecimal prixULDocument = montantHt.divide(((TaLDevis) lf).getQteLDocument(), 2,BigDecimal.ROUND_HALF_UP);
						BigDecimal prixULDocument = ((TaLDevis) lf).getMtTtcLDocument().divide(((TaLDevis) lf).getQteLDocument(), 2,BigDecimal.ROUND_HALF_UP);
						((TaLDevis) lf).setPrixULDocument(prixULDocument);
						
					}else if(document.getMontantHt() != null) {
						BigDecimal montantHt = new BigDecimal(document.getMontantHt());
						((TaLDevis) lf).setMtHtLDocument(montantHt);
						BigDecimal montantTtc = lf.getMtHtLDocument().multiply(BigDecimal.valueOf(1).add(
								 (art.getTaTva().getTauxTva().divide(new BigDecimal(100),MathContext.DECIMAL128).setScale(3,BigDecimal.ROUND_HALF_UP)
							)),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP);;
						((TaLDevis) lf).setMtTtcLDocument(montantTtc);
						//BigDecimal prixULDocument = montantHt.divide(((TaLDevis) lf).getQteLDocument(), 2,BigDecimal.ROUND_HALF_UP);
						BigDecimal prixULDocument = ((TaLDevis) lf).getMtTtcLDocument().divide(((TaLDevis) lf).getQteLDocument(), 2,BigDecimal.ROUND_HALF_UP);
						((TaLDevis) lf).setPrixULDocument(prixULDocument);
					}
					

					
					
					
					

					((TaLDevis) lf).setTauxTvaLDocument(art.getTaTva().getTauxTva());
				
					((TaDevis)docGenere).addLigne((TaLDevis)lf);
					
					document.setTermine(true);
					document = taLigneServiceWebExterneService.merge(document);
				}
				
				if(getParam().getRepriseLibelleSrc() == true) {
					ILigneDocumentTiers lf = new TaLDevis();
					((TaLDevis) lf).initialiseLigne(false);
					((TaLDevis) lf).setTaDocument((TaDevis) docGenere);
					((TaLDevis) lf).setTaTLigne(taTLigneService.findByCode("C"));
					((TaLDevis) lf).setLibLDocument(((TaDevis)docGenere).getLibelleDocument());
					((TaDevis)docGenere).addLigne((TaLDevis)lf);
					
				}else if(getParam().getRepriseReferenceSrc() == true) {
					ILigneDocumentTiers lf = new TaLDevis();
					((TaLDevis) lf).initialiseLigne(false);
					((TaLDevis) lf).setTaDocument((TaDevis) docGenere);
					((TaLDevis) lf).setTaTLigne(taTLigneService.findByCode("C"));
					((TaLDevis) lf).setLibLDocument(((TaDevis)docGenere).getCodeDocument());
					((TaDevis)docGenere).addLigne((TaLDevis)lf);
					
				}
				
				
				if(continuer){
					if(dateDocument!=null){
						docGenere.setDateDocument(dateDocument);
					}
					if(dateLivDocument!=null){
						docGenere.setDateLivDocument(dateLivDocument);
					}

//					TaPreferences gestionLot= taPreferencesService.findByCode("gestion_des_lots");
					
//					if(selectedType.equals(TypeDoc.TYPE_AVOIR)) {
//						((TaAvoir)docGenere).setGestionLot(LibConversion.StringToBoolean(gestionLot.getValeur()));
//					} else if(selectedType.equals(TypeDoc.TYPE_FACTURE)) {
//						((TaFacture)docGenere).setGestionLot(LibConversion.StringToBoolean(gestionLot.getValeur()));
//					} else if(selectedType.equals(TypeDoc.TYPE_BON_LIVRAISON)) {
//						((TaBonliv)docGenere).setGestionLot(LibConversion.StringToBoolean(gestionLot.getValeur()));
//					}  else if(selectedType.equals(TypeDoc.TYPE_BON_LIVRAISON)) {
//						((TaBonReception)docGenere).setGestionLot(LibConversion.StringToBoolean(gestionLot.getValeur()));
//					} else if(selectedType.equals(TypeDoc.TYPE_TICKET_DE_CAISSE)) {
//						((TaTicketDeCaisse)docGenere).setGestionLot(LibConversion.StringToBoolean(gestionLot.getValeur()));
//					}	
					
					if(generationModele) {
						//général
						docGenere.setDateLivDocument(dateDocument);


						if(((TaDevis)docGenere).getDateEchDocument()==null)
							((TaDevis)docGenere).setDateEchDocument(dateDocument);
								
							
						
					}
					
					if(docGenere.getCommentaire() == null || docGenere.getCommentaire().trim().equals("") )
						docGenere.setCommentaire(commentaireDefaut);
					docGenere.setDateExport(null);
					
					//creation reglement document yann
					if(regleDoc) {
					}
					
					//genereDocLigneServiceVersFactureService.creationMouvement()
					genereDocument.enregistreDocument(docGenere);
					
					
					 
					
					

					retour.setRetour(true);
					retour.setDoc(docGenere);
					retour.setMessage("Le document '"+docGenere.getCodeDocument()+"' à été généré correctement.");
				}
				
				
				
				
			} else if(selectedType.equals(TypeDoc.TYPE_FACTURE)) {
				typeCPaiement=TaTCPaiement.C_CODE_TYPE_FACTURE;
				docGenere = new TaFacture();
				libelleDocument="Facture N°: ";
				TaTPaiement taTPaiement= null;
				if(conserveNbDecimalesDoc==false) {
					TaPreferences nb;
					nb = taPreferencesService.findByGoupeAndCle(ITaPreferencesServiceRemote.GROUPE_PREFERENCES_NB_DECIMALES, ITaPreferencesServiceRemote.NB_DECIMALES_PRIX);
					if(nb!=null && nb.getValeur() != null) {
						docGenere.setNbDecimalesPrix(LibConversion.stringToInteger(nb.getValeur()));
					}

				}
				else {
					docGenere.setNbDecimalesQte(3);
				}
				commentaireDefaut=taPreferencesService.retourPreferenceString(selectedType.toLowerCase(), ITaPreferencesServiceRemote.P_COMMENTAIRE);
				if(commentaireDefaut==null)commentaireDefaut="";
				
				
		
				TaTCPaiement tcp=taTCPaiementService.findByCode(typeCPaiement);
				if(tcp!=null){
					List<TaCPaiement> liste=taCPaiementService.rechercheParType(tcp.getCodeTCPaiement());
					if(liste!=null && !liste.isEmpty()) {
					taCPaiement1=liste.get(0);
					taCPaiement2=liste.get(0);
					}
				}
				
				Boolean continuer=!controleExistance;

				if(listeLigneServiceWeb.size()>0){
					genereDocument = create(listeLigneServiceWeb.get(0), docGenere);
				}
				genereDocument.setRecupLibelleSeparateurDoc(!getParam().getRepriseAucun());
				genereDocument.setLigneSeparatrice(genereDocument.getRecupLibelleSeparateurDoc());
				int nbListeDoc=0;
				boolean genererCode=false;
				if(controleExistance)controleExistance=!getParam().isGenerationLigne();
				
				TaPreferences gestionLot= taPreferencesService.findByCode("gestion_des_lots");
				((TaFacture)docGenere).setGestionLot(LibConversion.StringToBoolean(gestionLot.getValeur()));
				//rajout yann
				tiers = listeLigneServiceWeb.get(0).getTaTiers();
				taTPaiement = listeLigneServiceWeb.get(0).getTaTPaiement();
				Map<String, String> paramsCode = new LinkedHashMap<String, String>();
				paramsCode.put(Const.C_NOMFOURNISSEUR, codeTiers);
				paramsCode.put(Const.C_DATEDOCUMENT, LibDate.dateToString(((TaFacture)docGenere).getDateDocument()));
				((TaFacture)docGenere).setTaTiers(tiers);
				((TaFacture)docGenere).setCodeDocument(taFactureService.genereCode(paramsCode));
				
				TaInfosFacture infos = new TaInfosFacture();
				infos.setTaDocument(((TaFacture)docGenere));
				infos=(TaInfosFacture)copieInfosDocument(infos);
				((TaFacture)docGenere).setTaInfosDocument(infos);
				((TaFacture)docGenere).setTtc(1);
				if(!LibChaine.empty(nouveauLibelle)) {
					((TaFacture)docGenere).setLibelleDocument(nouveauLibelle);
				} else {
					((TaFacture)docGenere).setLibelleDocument(libelleDocument+" "+docGenere.getCodeDocument()+" pour "+param.getTypeEntiteExterne()+" "+param.getCodeServiceExterne()+ " N° "+idDocumentExterne);
				}
				
				
				//((TaFacture)docGenere).setLibelleDocument("Facture ShippingBo N°: "+docGenere.getCodeDocument());
				
				if(getParam().getRepriseLibelleSrc() == true) {
					ILigneDocumentTiers lf = new TaLFacture();
					((TaLFacture) lf).initialiseLigne(false);
					((TaLFacture) lf).setTaDocument((TaFacture) docGenere);
					((TaLFacture) lf).setTaTLigne(taTLigneService.findByCode("C"));
					((TaLFacture) lf).setLibLDocument(((TaFacture)docGenere).getLibelleDocument());
					((TaFacture)docGenere).addLigne((TaLFacture)lf);
					
				}else if(getParam().getRepriseReferenceSrc() == true) {
					ILigneDocumentTiers lf = new TaLFacture();
					((TaLFacture) lf).initialiseLigne(false);
					((TaLFacture) lf).setTaDocument((TaFacture) docGenere);
					((TaLFacture) lf).setTaTLigne(taTLigneService.findByCode("C"));
					((TaLFacture) lf).setLibLDocument(param.getTypeEntiteExterne()+" "+param.getCodeServiceExterne()+ " N° "+idDocumentExterne+" du "+listeLigneServiceWeb.get(0).getDateCreationExterne());
					((TaFacture)docGenere).addLigne((TaLFacture)lf);
					
				}
				
				for (TaLigneServiceWebExterne document : listeLigneServiceWeb) {
					ILigneDocumentTiers lf = new TaLFacture();
					((TaLFacture) lf).initialiseLigne(false);
					TaArticle art = taArticleService.findById(document.getTaArticle().getIdArticle());
					if(document.getTaLot() != null) {
						TaLot lot = taLotService.findById(document.getTaLot().getIdLot());
						((TaLFacture) lf).setTaLot(lot);
					}else {
						//lot
//						if((((TaFacture)docGenere).getGestionLot()!=null &&!((TaFacture)docGenere).getGestionLot()) 
//								|| (art!=null && (art.getGestionLot()!=null &&!art.getGestionLot()))) {
							//utiliser un lot virtuel
							if(document.getTaLot()==null && art!=null){
								((TaLFacture) lf).setTaLot(taLotService.findOrCreateLotVirtuelArticle(art.getIdArticle()));
							}
						//}
					}
					
					
					
					((TaLFacture) lf).setTaDocument((TaFacture) docGenere);
					((TaLFacture) lf).setTaTLigne(taTLigneService.findByCode("H"));
					((TaLFacture) lf).setTaArticle(art);
					((TaLFacture) lf).setLibLDocument(document.getNomArticle());
					((TaLFacture) lf).setQteLDocument(BigDecimal.valueOf(document.getQteArticle()));
					((TaLFacture) lf).setCodeTvaLDocument(art.getTaTva().getCodeTva());
					((TaLFacture) lf).setCodeTTvaLDocument("F");
					
					if(art.getTaUnite1() != null) {
						((TaLFacture) lf).setU1LDocument(art.getTaUnite1().getCodeUnite());
					}
					
					((TaLFacture) lf).setCompteLDocument(art.getNumcptArticle());
					
					
					((TaLFacture) lf).setMtTtcLDocument(new BigDecimal(document.getMontantTtc()));
					BigDecimal montantHt = lf.getMtTtcLDocument().divide(BigDecimal.valueOf(1).add(
							 (art.getTaTva().getTauxTva().divide(new BigDecimal(100),MathContext.DECIMAL128).setScale(3,BigDecimal.ROUND_HALF_UP)
						)),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP);
					((TaLFacture) lf).setMtHtLDocument(montantHt);
					//BigDecimal prixULDocument = montantHt.divide(((TaLFacture) lf).getQteLDocument(), 2,BigDecimal.ROUND_HALF_UP);
					BigDecimal prixULDocument = ((TaLFacture) lf).getMtTtcLDocument().divide(((TaLFacture) lf).getQteLDocument(), 2,BigDecimal.ROUND_HALF_UP);
					
					

					
					
					
					((TaLFacture) lf).setPrixULDocument(prixULDocument);
					((TaLFacture) lf).setTauxTvaLDocument(art.getTaTva().getTauxTva());
				
					((TaFacture)docGenere).addLigne((TaLFacture)lf);
					
					
					
					document.setTermine(true);
					document = taLigneServiceWebExterneService.merge(document);
					
				}
				
				
				
				
				if(continuer){
					if(dateDocument!=null){
						docGenere.setDateDocument(dateDocument);
					}
					if(dateLivDocument!=null){
						docGenere.setDateLivDocument(dateLivDocument);
					}

					
					
					if(selectedType.equals(TypeDoc.TYPE_AVOIR)) {
						((TaAvoir)docGenere).setGestionLot(LibConversion.StringToBoolean(gestionLot.getValeur()));
					} else if(selectedType.equals(TypeDoc.TYPE_FACTURE)) {
						//((TaFacture)docGenere).setGestionLot(LibConversion.StringToBoolean(gestionLot.getValeur()));
					} else if(selectedType.equals(TypeDoc.TYPE_BON_LIVRAISON)) {
						((TaBonliv)docGenere).setGestionLot(LibConversion.StringToBoolean(gestionLot.getValeur()));
					}  else if(selectedType.equals(TypeDoc.TYPE_BON_LIVRAISON)) {
						((TaBonReception)docGenere).setGestionLot(LibConversion.StringToBoolean(gestionLot.getValeur()));
					} else if(selectedType.equals(TypeDoc.TYPE_TICKET_DE_CAISSE)) {
						((TaTicketDeCaisse)docGenere).setGestionLot(LibConversion.StringToBoolean(gestionLot.getValeur()));
					}	
					
					if(generationModele) {
						//général
						docGenere.setDateLivDocument(dateDocument);

						if(selectedType.equals(TypeDoc.TYPE_AVOIR)) {
							if(((TaAvoir)docGenere).getDateEchDocument()==null)
								((TaAvoir)docGenere).setDateEchDocument(dateDocument);
						} else if(selectedType.equals(TypeDoc.TYPE_DEVIS)) {
							if(((TaDevis)docGenere).getDateEchDocument()==null)
								((TaDevis)docGenere).setDateEchDocument(dateDocument);
						} else if(selectedType.equals(TypeDoc.TYPE_FACTURE)) {
							if(((TaFacture)docGenere).getDateEchDocument()==null)
								((TaFacture)docGenere).setDateEchDocument(dateDocument);
						} else if(selectedType.equals(TypeDoc.TYPE_PROFORMA)) {
							if(((TaProforma)docGenere).getDateEchDocument()==null)
								((TaProforma)docGenere).setDateEchDocument(dateDocument);
						} else if(selectedType.equals(TypeDoc.TYPE_APPORTEUR)) {
							if(((TaApporteur)docGenere).getDateEchDocument()==null)
								((TaApporteur)docGenere).setDateEchDocument(dateDocument);
						} else if(selectedType.equals(TypeDoc.TYPE_BON_COMMANDE)) {
							if(((TaBoncde)docGenere).getDateEchDocument()==null)
								((TaBoncde)docGenere).setDateEchDocument(dateDocument);
						} else if(selectedType.equals(TypeDoc.TYPE_BON_COMMANDE_ACHAT)) {
							if(((TaBoncdeAchat)docGenere).getDateEchDocument()==null)
								((TaBoncdeAchat)docGenere).setDateEchDocument(dateDocument);
						} else if(selectedType.equals(TypeDoc.TYPE_BON_LIVRAISON)) {
						}else if(selectedType.equals(TypeDoc.TYPE_ACOMPTE)) {
							if(((TaAcompte)docGenere).getDateEchDocument()==null)
								((TaAcompte)docGenere).setDateEchDocument(dateDocument);
						}else if(selectedType.equals(TypeDoc.TYPE_PRELEVEMENT)) {
							if(((TaPrelevement)docGenere).getDateEchDocument()==null)
								((TaPrelevement)docGenere).setDateEchDocument(dateDocument);
						}else if(selectedType.equals(TypeDoc.TYPE_AVIS_ECHEANCE)) {
							if(((TaAvisEcheance)docGenere).getDateEchDocument()==null)
								((TaAvisEcheance)docGenere).setDateEchDocument(dateDocument);
						}else if(selectedType.equals(TypeDoc.TYPE_TICKET_DE_CAISSE)) {
							if(((TaTicketDeCaisse)docGenere).getDateEchDocument()==null)
								((TaTicketDeCaisse)docGenere).setDateEchDocument(dateDocument);
						}else if(selectedType.equals(TypeDoc.TYPE_PREPARATION)) {

						}						
							
						
					}
					
					if(docGenere.getCommentaire() == null || docGenere.getCommentaire().trim().equals("") )
						docGenere.setCommentaire(commentaireDefaut);
					docGenere.setDateExport(null);
					
					//creation reglement facture yann
					if(regleDoc) {
						if(selectedType.equals(TypeDoc.TYPE_FACTURE)) {
							 Date now = new Date();
							 TaRReglement taRReglement = new TaRReglement();
							 TaReglement reglement = new TaReglement();
							 taRReglement.setAffectation(docGenere.getNetTtcCalc());
							 taRReglement.setEtat(IHMEtat.integre);
							 TaPreferences prefTypePaiement = null;
							 //CB par défaut
							 if(taTPaiement != null) {
								 reglement.setTaTPaiement(taTPaiement);
							 }else {
								 String codeTypePaiement = "CB";
								 if(codeServiceWeb != null && !codeServiceWeb.isEmpty()) {
									 switch (codeServiceWeb) {
										case TaServiceWebExterne.CODE_SERVICE_SHIPPINGBO:
											prefTypePaiement =  taPreferenceService.findByGoupeAndCle(ITaPreferencesServiceRemote.IMPORTATION, ITaPreferencesServiceRemote.TYPE_PAIEMENT_SHIPPINGBO);
											break;
										case TaServiceWebExterne.CODE_SERVICE_MENSURA:
											prefTypePaiement = taPreferenceService.findByGoupeAndCle(ITaPreferencesServiceRemote.IMPORTATION, ITaPreferencesServiceRemote.TYPE_PAIEMENT_MENSURA);
											break;
										case TaServiceWebExterne.CODE_SERVICE_WOOCOMMERCE:
											prefTypePaiement = taPreferenceService.findByGoupeAndCle(ITaPreferencesServiceRemote.IMPORTATION, ITaPreferencesServiceRemote.TYPE_PAIEMENT_WOOCOMMERCE);
											break;
										case TaServiceWebExterne.CODE_SERVICE_PRESTASHOP:
											prefTypePaiement = taPreferenceService.findByGoupeAndCle(ITaPreferencesServiceRemote.IMPORTATION, ITaPreferencesServiceRemote.TYPE_PAIEMENT_PRESTASHOP);
											break;
			
										default:
											break;
										}
									 
									 if(prefTypePaiement != null && prefTypePaiement.getValeur() != null) {
										  codeTypePaiement = prefTypePaiement.getValeur();
									 }
								 }
								 
								 reglement.setTaTPaiement(taTPaiementService.findByCode(codeTypePaiement));
							 }
							 
							
							
							
							 reglement.setTaTiers(docGenere.getTaTiers());
							 reglement.setLibelleDocument(docGenere.getLibelleDocument());
							 reglement.getTaFactures().add((TaFacture) docGenere);
							 reglement.setDateDocument(now);
							 reglement.setDateLivDocument(now);
							 reglement.setCodeDocument(taReglementService.genereCode(null));
							 reglement.setNetTtcCalc(docGenere.getNetTtcCalc());
							 
							 ((TaFacture)docGenere).setRegleDocument(docGenere.getNetTtcCalc());
								((TaFacture)docGenere).setRegleCompletDocument(docGenere.getNetTtcCalc());
								((TaFacture)docGenere).setNetAPayer(BigDecimal.valueOf(0));
								
							 taRReglement.setTaReglement(reglement);
							 taRReglement.setTaFacture((TaFacture)docGenere);
							 ((TaFacture)docGenere).addRReglement(taRReglement);
								
							reglement=taReglementService.merge(reglement);
							taReglementService.annuleCode(reglement.getCodeDocument());
							
							 taRReglement.setTaReglement(reglement);
							 taRReglement.setTaFacture((TaFacture)docGenere);
							 ((TaFacture)docGenere).addRReglement(taRReglement);
						 }
					}
					
					docGenere = creationMouvement((TaFacture)docGenere);
					genereDocument.enregistreDocument(docGenere);
					
					
					 
					
					

					retour.setRetour(true);
					retour.setDoc(docGenere);
					retour.setMessage("Le document '"+docGenere.getCodeDocument()+"' à été généré correctement.");
				}
				
			} else if(selectedType.equals(TypeDoc.TYPE_PROFORMA)) {
				typeCPaiement=TaTCPaiement.C_CODE_TYPE_PROFORMA;
				docGenere = new TaProforma();
				libelleDocument="Proforma N°: ";
			} else if(selectedType.equals(TypeDoc.TYPE_APPORTEUR)) {
				typeCPaiement=TaTCPaiement.C_CODE_TYPE_APORTEUR;
				docGenere = new TaApporteur();
				libelleDocument="Apporteur N°: ";
			} else if(selectedType.equals(TypeDoc.TYPE_BON_COMMANDE_ACHAT)) {
				typeCPaiement=TaTCPaiement.C_CODE_TYPE_BON_COMMANDE_ACHAT;
				docGenere = new TaBoncdeAchat();
				libelleDocument="Bon cde N°: ";
			} else if(selectedType.equals(TypeDoc.TYPE_BON_RECEPTION)) {
				typeCPaiement=TaTCPaiement.C_CODE_TYPE_BON_RECEPTION;
				docGenere = new TaBonReception();
				libelleDocument="Bon réception N°: ";
			} else if(selectedType.equals(TypeDoc.TYPE_BON_COMMANDE)) {
				typeCPaiement=TaTCPaiement.C_CODE_TYPE_BON_COMMANDE;
				docGenere = new TaBoncde();
				libelleDocument="Bon cde N°: ";
			} else if(selectedType.equals(TypeDoc.TYPE_BON_LIVRAISON)) {
				typeCPaiement=TaTCPaiement.C_CODE_TYPE_BON_LIVRAISON;
				docGenere = new TaBonliv();
				libelleDocument="Bon liv. N°: ";
			}else if(selectedType.equals(TypeDoc.TYPE_ACOMPTE)) {
				typeCPaiement=TaTCPaiement.C_CODE_TYPE_ACOMPTE;
				docGenere = new TaAcompte();
				libelleDocument="Acompte N°: ";
			}else if(selectedType.equals(TypeDoc.TYPE_PRELEVEMENT)) {
				typeCPaiement=TaTCPaiement.C_CODE_TYPE_PRELEVEMENT;
				docGenere = new TaPrelevement();
				libelleDocument="Prélèvement N°: ";
			}else if(selectedType.equals(TypeDoc.TYPE_AVIS_ECHEANCE)) {
				typeCPaiement=TaTCPaiement.C_CODE_TYPE_AVIS_ECHEANCE;
				docGenere = new TaAvisEcheance();
				libelleDocument="Avis N°: ";
			}else if(selectedType.equals(TypeDoc.TYPE_TICKET_DE_CAISSE)) {
				typeCPaiement=TaTCPaiement.C_CODE_TYPE_TICKET_DE_CAISSE;
				docGenere = new TaTicketDeCaisse();
				libelleDocument="Ticket N°: ";
			}

			
		} catch (Exception e) {
			//System.out.println(e+" "+ e.getStackTrace()+" " +e.getCause());
			e.printStackTrace();
			retour=null;
		}
			return retour;

	}
	
	private IDocumentTiers creationMouvement(TaFacture docGenere) {
		/*
		 * Gérer les mouvements corrrespondant aux lignes 
		 */
		//Création du groupe de mouvement
		TaTLigne typeLigneCommentaire;
		try {
			typeLigneCommentaire = taTLigneService.findByCode(Const.C_CODE_T_LIGNE_C);

		
		TaGrMouvStock grpMouvStock = new TaGrMouvStock();
		if(docGenere.getTaGrMouvStock()!=null) {
			grpMouvStock=docGenere.getTaGrMouvStock();
		}
		grpMouvStock.setCodeGrMouvStock(docGenere.getCodeDocument());
		grpMouvStock.setDateGrMouvStock(docGenere.getDateDocument());
		grpMouvStock.setLibelleGrMouvStock(docGenere.getLibelleDocument()!=null?docGenere.getLibelleDocument():"Facture "+docGenere.getCodeDocument());
		grpMouvStock.setTaTypeMouvStock(taTypeMouvementService.findByCode("T")); // type mouvement Facture
		docGenere.setTaGrMouvStock(grpMouvStock);
		grpMouvStock.setTaFacture(docGenere);

		for (TaLFacture l : docGenere.getLignes()) {
			if(!l.getTaTLigne().equals(typeLigneCommentaire)&& l.getTaArticle()!=null){
				l.setTaTLigne(taTLigneService.findByCode(Const.C_CODE_T_LIGNE_H));
				if(l.getTaMouvementStock()!=null)
					l.getTaMouvementStock().setTaGrMouvStock(docGenere.getTaGrMouvStock());
			}
		}
		
		//
		grpMouvStock.getTaMouvementStockes().clear();
		
		//Création des lignes de mouvement
		for (TaLFacture ligne : docGenere.getLignes()) {
			if(!ligne.getTaTLigne().equals(typeLigneCommentaire)&&ligne.getTaArticle()!=null){
				//				if(ligne.getTaMouvementStock()==null){
				TaMouvementStock mouv = new TaMouvementStock();
				if(ligne.getTaMouvementStock()!=null) {
					mouv=ligne.getTaMouvementStock();
				}
				if(ligne.getLibLDocument()!=null) {
					mouv.setLibelleStock(ligne.getLibLDocument());
				} else if (ligne.getTaArticle().getLibellecArticle()!=null){
					mouv.setLibelleStock(ligne.getTaArticle().getLibellecArticle());
				} else {
					mouv.setLibelleStock("");
				}
				mouv.setDateStock(docGenere.getDateDocument());
				mouv.setEmplacement(ligne.getEmplacementLDocument());
				mouv.setTaEntrepot(ligne.getTaEntrepot());
				if(ligne.getTaLot()!=null){
					//					mouv.setNumLot(ligne.getTaLot().getNumLot());
					mouv.setTaLot(ligne.getTaLot());
				}
				if(ligne.getQteLDocument()!=null)mouv.setQte1Stock(ligne.getQteLDocument().multiply(BigDecimal.valueOf(-1)));
				if(ligne.getQte2LDocument()!=null)mouv.setQte2Stock(ligne.getQte2LDocument().multiply(BigDecimal.valueOf(-1)));
				mouv.setUn1Stock(ligne.getU1LDocument());
				mouv.setUn2Stock(ligne.getU2LDocument());
				mouv.setTaGrMouvStock(grpMouvStock);
				//				ligne.setTaLot(null);
				ligne.setTaMouvementStock(mouv);

				grpMouvStock.getTaMouvementStockes().add(mouv);
				//				}
			}
		}
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return docGenere;
	}
	
	
	/**n'a rien a faire la mais je l'ai mis pour y acceder directement depuis ici**/
	public IInfosDocumentTiers copieInfosDocument( IInfosDocumentTiers id) {
		copieInfosDocumentGeneral(id);
		//copieInfosDocumentSpecifique(id);
		return id;
	}
	public IInfosDocumentTiers copieInfosDocumentGeneral( IInfosDocumentTiers id) {
		try {		
				if(id.getTaDocument()!=null && id.getTaDocument().getTaTiers()!=null) {
					if(id.getTaDocument().getTaTiers().aDesAdressesDuType(typeAdresseFacturation)) {
						//ajout des adresse de facturation au modele
						for (TaAdresse taAdresse : id.getTaDocument().getTaTiers().getTaAdresses()) {
							if(taAdresse.getTaTAdr()!=null && taAdresse.getTaTAdr().getCodeTAdr().equals(typeAdresseFacturation)){
								id.setAdresse1(taAdresse.getAdresse1Adresse());
								id.setAdresse2(taAdresse.getAdresse2Adresse());
								id.setAdresse3(taAdresse.getAdresse3Adresse());
								id.setCodepostal(taAdresse.getCodepostalAdresse());
								id.setVille(taAdresse.getVilleAdresse());
								id.setPays(taAdresse.getPaysAdresse());
							}
						}
					}else {
						if(id.getTaDocument().getTaTiers().getTaAdresse()!=null){
							id.setAdresse1(id.getTaDocument().getTaTiers().getTaAdresse().getAdresse1Adresse());
							id.setAdresse2(id.getTaDocument().getTaTiers().getTaAdresse().getAdresse2Adresse());
							id.setAdresse3(id.getTaDocument().getTaTiers().getTaAdresse().getAdresse3Adresse());
							id.setCodepostal(id.getTaDocument().getTaTiers().getTaAdresse().getCodepostalAdresse());
							id.setVille(id.getTaDocument().getTaTiers().getTaAdresse().getVilleAdresse());
							id.setPays(id.getTaDocument().getTaTiers().getTaAdresse().getPaysAdresse());
						}
					}
					if(id.getTaDocument().getTaTiers().aDesAdressesDuType(typeAdresseLivraison)) {
						//ajout des adresse de livraison au modele
						for (TaAdresse taAdresse : id.getTaDocument().getTaTiers().getTaAdresses()) {
							if(taAdresse.getTaTAdr()!=null && taAdresse.getTaTAdr().getCodeTAdr().equals(typeAdresseLivraison)){
								id.setAdresse1Liv(taAdresse.getAdresse1Adresse());
								id.setAdresse2Liv(taAdresse.getAdresse2Adresse());
								id.setAdresse3Liv(taAdresse.getAdresse3Adresse());
								id.setCodepostalLiv(taAdresse.getCodepostalAdresse());
								id.setVilleLiv(taAdresse.getVilleAdresse());
								id.setPaysLiv(taAdresse.getPaysAdresse());
							}
						}
					}else {
						if(id.getTaDocument().getTaTiers().getTaAdresse()!=null){
							id.setAdresse1Liv(id.getTaDocument().getTaTiers().getTaAdresse().getAdresse1Adresse());
							id.setAdresse2Liv(id.getTaDocument().getTaTiers().getTaAdresse().getAdresse2Adresse());
							id.setAdresse3Liv(id.getTaDocument().getTaTiers().getTaAdresse().getAdresse3Adresse());
							id.setCodepostalLiv(id.getTaDocument().getTaTiers().getTaAdresse().getCodepostalAdresse());
							id.setVilleLiv(id.getTaDocument().getTaTiers().getTaAdresse().getVilleAdresse());
							id.setPaysLiv(id.getTaDocument().getTaTiers().getTaAdresse().getPaysAdresse());
						}
					}
					id.setCodeCompta(id.getTaDocument().getTaTiers().getCodeCompta());
					id.setCompte(id.getTaDocument().getTaTiers().getCompte());
					id.setNomTiers(id.getTaDocument().getTaTiers().getNomTiers());
					id.setPrenomTiers(id.getTaDocument().getTaTiers().getPrenomTiers());
					id.setSurnomTiers(id.getTaDocument().getTaTiers().getSurnomTiers());
					if(id.getTaDocument().getTaTiers().getTaTCivilite()!=null)
						id.setCodeTCivilite(id.getTaDocument().getTaTiers().getTaTCivilite().getCodeTCivilite());
					if(id.getTaDocument().getTaTiers().getTaTEntite()!=null)
						id.setCodeTEntite(id.getTaDocument().getTaTiers().getTaTEntite().getCodeTEntite());
					if(id.getTaDocument().getTaTiers().getTaCompl()!=null)
						id.setTvaIComCompl(id.getTaDocument().getTaTiers().getTaCompl().getTvaIComCompl());
//					id.setCodeCPaiement("888");
					if(id.getTaDocument().getTaTiers().getTaCPaiement()!=null) {
						id.setCodeCPaiement(id.getTaDocument().getTaTiers().getTaCPaiement().getCodeCPaiement());
						id.setLibCPaiement(id.getTaDocument().getTaTiers().getTaCPaiement().getLibCPaiement());
						id.setReportCPaiement(id.getTaDocument().getTaTiers().getTaCPaiement().getReportCPaiement());
						id.setFinMoisCPaiement(id.getTaDocument().getTaTiers().getTaCPaiement().getFinMoisCPaiement());
					}
					if(id.getTaDocument().getTaTiers().getTaEntreprise()!=null)
					id.setNomEntreprise(id.getTaDocument().getTaTiers().getTaEntreprise().getNomEntreprise());
					if(id.getTaDocument().getTaTiers().getTaTTvaDoc()!=null)
						id.setCodeTTvaDoc(id.getTaDocument().getTaTiers().getTaTTvaDoc().getCodeTTvaDoc());
				}
		} catch(Exception cnse) {
			//logger.error("",cnse);
		}
		return id;
	}

	
	public String getNouveauLibelle() {
		return nouveauLibelle;
	}


	public void setNouveauLibelle(String nouveauLibelle) {
		this.nouveauLibelle = nouveauLibelle;
	}




	public String getSelectedType() {
		return selectedType;
	}


	public void setSelectedType(String selectedType) {
		this.selectedType = selectedType;
	}


	public Date getDateDocument() {
		return dateDocument;
	}


	public void setDateDocument(Date dateDocument) {
		this.dateDocument = dateDocument;
	}


	public Date getDateLivDocument() {
		return dateLivDocument;
	}


	public void setDateLivDocument(Date dateLivDocument) {
		this.dateLivDocument = dateLivDocument;
	}



	public ParamAfficheChoixGenerationDocument getParam() {
		return param;
	}

	public void setParam(ParamAfficheChoixGenerationDocument param) {
		//typeSrc = ((ParamAfficheChoixGenerationDocument)param).getTypeSrc();
		selectedType =((ParamAfficheChoixGenerationDocument)param).getTypeDest();
//		documentFlashSrc = ((ParamAfficheChoixGenerationDocument)param).getDocumentFlashSrc();

//		listeDocumentFlash=((ParamAfficheChoixGenerationDocument)param).getListeFlashSrc();
//		listeLigneDocumentFlash=((ParamAfficheChoixGenerationDocument)param).getListeLigneFlashSrc();
		
		//liste de lignes service web d'un même tiers par defaut
		listeLigneServiceWeb = ((ParamAfficheChoixGenerationDocument)param).getListeLigneServiceWebExterneSrc();
		
		
		setNouveauLibelle(((ParamAfficheChoixGenerationDocument)param).getLibelle());
		dateDocument=((ParamAfficheChoixGenerationDocument)param).getDateDocument();
		dateLivDocument=((ParamAfficheChoixGenerationDocument)param).getDateLivraison();
		if(((ParamAfficheChoixGenerationDocument)param).isTiersModifiable())
			codeTiers=((ParamAfficheChoixGenerationDocument)param).getCodeTiers();
		relation=((ParamAfficheChoixGenerationDocument)param).isRelation();
		generationModele=((ParamAfficheChoixGenerationDocument)param).isGenerationModele();
		generationLigne=((ParamAfficheChoixGenerationDocument)param).isGenerationLigne();
		this.param=param;
		conserveNbDecimalesDoc=((ParamAfficheChoixGenerationDocument)param).isConserveNbDecimalesDoc();
			
	}

	public String getCodeTiers() {
		return codeTiers;
	}

	public void setCodeTiers(String codeTiers) {
		this.codeTiers = codeTiers;
	}

	

	public boolean isGenerationModele() {
		return generationModele;
	}

	public void setGenerationModele(boolean generationModele) {
		this.generationModele = generationModele;
	}

	public IAbstractGenereDocLigneServiceWebExterneServiceRemote create(TaLigneServiceWebExterne src, IDocumentTiers dest) {
		IAbstractGenereDocLigneServiceWebExterneServiceRemote genereDoc = null;	
			if(dest instanceof TaPreparation) {
				//genereDoc = genereDocFLashVersPreparationService;
			} else if(dest instanceof TaBonliv) {
//				genereDoc = genereDocAcompteVersBonLivraisonService;
			} else if(dest instanceof TaBonReception) {
				
			}else if(dest instanceof TaFabrication) {
				
			}else if(dest instanceof TaFacture) {
				genereDoc = genereDocLigneServiceVersFactureService;
			}else if(dest instanceof TaDevis) {
				genereDoc = genereDocLigneServiceVersDevisService;
			}
			
//		genereDoc.setRelationDocument(src.getRelationDocument());
		return genereDoc;
	}


	public boolean isGenerationLigne() {
		return generationLigne;
	}



	public void setGenerationLigne(boolean generationLigne) {
		this.generationLigne = generationLigne;
	}



	public List<TaLigneServiceWebExterne> getListeLigneServiceWeb() {
		return listeLigneServiceWeb;
	}



	public void setListeLigneServiceWeb(List<TaLigneServiceWebExterne> listeLigneServiceWeb) {
		this.listeLigneServiceWeb = listeLigneServiceWeb;
	}



	public boolean isRegleDoc() {
		return regleDoc;
	}



	public void setRegleDoc(boolean regleDoc) {
		this.regleDoc = regleDoc;
	}



	public String getCodeServiceWeb() {
		return codeServiceWeb;
	}



	public void setCodeServiceWeb(String codeServiceWeb) {
		this.codeServiceWeb = codeServiceWeb;
	}



	public String getIdDocumentExterne() {
		return idDocumentExterne;
	}



	public void setIdDocumentExterne(String idDocumentExterne) {
		this.idDocumentExterne = idDocumentExterne;
	}
	

}
