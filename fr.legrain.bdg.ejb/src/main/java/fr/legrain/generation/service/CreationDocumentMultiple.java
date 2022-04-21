package fr.legrain.generation.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.security.DeclareRoles;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import org.apache.log4j.Logger;

import fr.legrain.bdg.article.service.remote.ITaFabricationServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaTypeMouvementServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaAbonnementServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaAcompteServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaApporteurServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaAvisEcheanceServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaAvoirServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaBonReceptionServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaBoncdeAchatServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaBoncdeServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaBonlivServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaDevisServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaFactureServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLBoncdeAchatServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaPrelevementServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaPreparationServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaProformaServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaRDocumentServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaReglementServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaRemiseServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaTLigneServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaTicketDeCaisseServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaPreferencesServiceRemote;
import fr.legrain.bdg.general.service.remote.IGenereDocAcompteVersBonLivraisonServiceRemote;
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
import fr.legrain.bdg.generation.service.remote.IGenereDocBonCommandeAchatVersBonCommandeAchatServiceRemote;
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
import fr.legrain.bdg.generation.service.remote.IGenereDocBonPreparationVersBonLivraisonServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocDevisVersApporteurServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocDevisVersAvoirServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocDevisVersBonCommandeServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocDevisVersBonLivraisonServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocDevisVersDevisServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocDevisVersFactureServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocDevisVersPrelevementServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocDevisVersProformaServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocFactureVersApporteurServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocFactureVersAvisEcheanceServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocFactureVersAvoirServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocFactureVersBonLivraisonServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocFactureVersCommandeServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocFactureVersDevisServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocFactureVersFactureServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocFactureVersPrelevementServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocFactureVersProformaServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocPanierVersBoncdeServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocPanierVersFactureServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocPanierVersPanierServiceRemote;
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
import fr.legrain.bdg.tiers.service.remote.ITaCPaiementServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTAdrServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTCPaiementServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.document.dto.IDocumentTiers;
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
import fr.legrain.document.model.TaLFacture;
import fr.legrain.document.model.TaPanier;
import fr.legrain.document.model.TaPrelevement;
import fr.legrain.document.model.TaPreparation;
import fr.legrain.document.model.TaProforma;
import fr.legrain.document.model.TaTicketDeCaisse;
import fr.legrain.document.model.TypeDoc;
import fr.legrain.dossier.model.TaPreferences;
import fr.legrain.generationDocument.model.ParamAfficheChoixGenerationDocument;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.lib.data.LibChaine;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.LibDate;
import fr.legrain.stock.model.TaGrMouvStock;
import fr.legrain.stock.model.TaMouvementStock;
import fr.legrain.tiers.model.TaCPaiement;
import fr.legrain.tiers.model.TaTCPaiement;
import fr.legrain.tiers.model.TaTiers;


@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class CreationDocumentMultiple implements java.io.Serializable {

	static Logger logger = Logger.getLogger(CreationDocumentMultiple.class.getName());
	private static final long serialVersionUID = -2836730580231914189L;

	private String nouveauLibelle="";
	private List<IDocumentTiers> listeDocument = null;
	private List<ILigneDocumentTiers> listeLigneDocument = null;
	private String selectedType="";
	private Date dateDocument = null;
	private Date dateLivDocument = null;
	private IDocumentTiers documentSrc = null;
	private ParamAfficheChoixGenerationDocument param;
	private String codeTiers=null;
//	private Map<String,Object> listeGestionnaireExtension = new LinkedHashMap<String,Object>();
	private boolean relation=false;
	private boolean changeTiers=false;
	private boolean generationModele=false;
	private boolean generationLigne=false;
	private boolean conserveNbDecimalesDoc=false;
//	public  AbstractGenereDoc genereDocument;
	
	private boolean mouvementerCrdAvoir = false;
	
	
	private IAbstractGenereDocServiceRemote genereDocument;
	
	private @EJB ITaPreferencesServiceRemote taPreferencesService;
	@EJB private ITaBoncdeAchatServiceRemote taBoncdeAchatService;
	
	private @EJB IGenereDocFactureVersFactureServiceRemote genereDocFactureVersFactureService;
	private @EJB IGenereDocFactureVersDevisServiceRemote genereDocFactureVersDevisService;
	private @EJB IGenereDocFactureVersApporteurServiceRemote genereDocFactureVersApporteurService;
	private @EJB IGenereDocFactureVersAvisEcheanceServiceRemote genereDocFactureVersAvisEcheanceService;
	private @EJB IGenereDocFactureVersAvoirServiceRemote genereDocFactureVersAvoirService;
	private @EJB IGenereDocFactureVersBonLivraisonServiceRemote genereDocFactureVersBonLivraisonService;
	private @EJB IGenereDocFactureVersCommandeServiceRemote genereDocFactureVersCommandeService;
	private @EJB IGenereDocFactureVersPrelevementServiceRemote genereDocFactureVersPrelevementService;
	private @EJB IGenereDocFactureVersProformaServiceRemote genereDocFactureVersProformaService;

	private @EJB IGenereDocAcompteVersFactureServiceRemote genereDocAcompteVersFactureService;
	private @EJB IGenereDocAcompteVersDevisServiceRemote genereDocAcompteVersDevisService;
	private @EJB IGenereDocAcompteVersApporteurServiceRemote genereDocAcompteVersApporteurService;
	private @EJB IGenereDocAcompteVersAcompteServiceRemote genereDocAcompteVersAcompteService;
	private @EJB IGenereDocAcompteVersAvoirServiceRemote genereDocAcompteVersAvoirService;
	private @EJB IGenereDocAcompteVersBonLivraisonServiceRemote genereDocAcompteVersBonLivraisonService;
	private @EJB IGenereDocAcompteVersBonCommandeServiceRemote genereDocAcompteVersCommandeService;
	private @EJB IGenereDocAcompteVersPrelevementServiceRemote genereDocAcompteVersPrelevementService;
	private @EJB IGenereDocAcompteVersProformaServiceRemote genereDocAcompteVersProformaService;
	
	private @EJB IGenereDocAvoirVersAvoirServiceRemote genereDocAvoirVersAvoirService;

	
	private @EJB IGenereDocAvisEcheanceVersAvisEcheanceServiceRemote genereDocAvisEcheanceVersAvisEcheanceService;
	private @EJB IGenereDocAvisEcheanceVersFactureServiceRemote genereDocAvisEcheanceVersFactureService;
	private @EJB IGenereDocAvisEcheanceVersPrelevementServiceRemote genereDocAvisEcheanceVersPrelevementService;
	
	private @EJB IGenereDocTicketDeCaisseVersFactureServiceRemote genereDocTicketDeCaisseVersFactureService;
	
	private @EJB IGenereDocApporteurVersApporteurServiceRemote genereDocApporteurVersApporteurService;
	
	private @EJB IGenereDocBonCommandeAchatVersBonReceptionServiceRemote genereDocBonCommandeAchatVersBonReceptionService;
	private @EJB IGenereDocBonCommandeAchatVersBonCommandeAchatServiceRemote genereDocBonCommandeAchatVersBonCommandeAchatService;
	
	private @EJB IGenereDocBonPreparationVersBonLivraisonServiceRemote genereDocBonPreparationVersBonLivraisonService;
	
	private @EJB IGenereDocPanierVersBoncdeServiceRemote genereDocPanierVersBoncdeService;
	private @EJB IGenereDocPanierVersFactureServiceRemote genereDocPanierVersFactureService;
	private @EJB IGenereDocPanierVersPanierServiceRemote genereDocPanierVersPanierService;
	
	private @EJB IGenereDocBonCommandeVersDevisServiceRemote genereDocBonCommandeVersDevisService;
	private @EJB IGenereDocBonCommandeVersApporteurServiceRemote genereDocBonCommandeVersApporteurService;
	private @EJB IGenereDocBonCommandeVersAvoirServiceRemote genereDocBonCommandeVersAvoirService;
	private @EJB IGenereDocBonCommandeVersBonCommandeServiceRemote genereDocBonCommandeVersBonCommandeService;
	private @EJB IGenereDocBonCommandeVersBonLivraisonServiceRemote genereDocBonCommandeVersBonLivraisonService;	
	private @EJB IGenereDocBonCommandeVersPrelevementServiceRemote genereDocBonCommandeVersPrelevementService;
	private @EJB IGenereDocBonCommandeVersProformaServiceRemote genereDocBonCommandeVersProformaService;
	private @EJB IGenereDocBonCommandeVersFactureServiceRemote genereDocBonCommandeVersFactureService;
	
	private @EJB IGenereDocBonLivraisonVersApporteurServiceRemote genereDocBonLivraisonVersApporteurService;
	private @EJB IGenereDocBonLivraisonVersAvoirServiceRemote genereDocBonLivraisonVersAvoirService;
	private @EJB IGenereDocBonLivraisonVersBonLivraisonServiceRemote genereDocBonLivraisonVersBonLivraisonService;
	private @EJB IGenereDocBonLivraisonVersCommandeServiceRemote genereDocBonLivraisonVersCommandeService;
	private @EJB IGenereDocBonLivraisonVersPrelevementServiceRemote genereDocBonLivraisonVersPrelevementService;
	private @EJB IGenereDocBonLivraisonVersProformaServiceRemote genereDocBonLivraisonVersProformaService;	
	private @EJB IGenereDocBonLivraisonVersFactureServiceRemote genereDocBonLivraisonVersFactureService;
	private @EJB IGenereDocBonLivraisonVersDevisServiceRemote genereDocBonLivraisonVersDevisService;
	
	private @EJB IGenereDocDevisVersApporteurServiceRemote genereDocDevisVersApporteurService;
	private @EJB IGenereDocDevisVersFactureServiceRemote genereDocDevisVersFactureService;
	private @EJB IGenereDocDevisVersAvoirServiceRemote genereDocDevisVersAvoirService;
	private @EJB IGenereDocDevisVersBonLivraisonServiceRemote genereDocDevisVersBonLivraisonService;
	private @EJB IGenereDocDevisVersBonCommandeServiceRemote genereDocDevisVersCommandeService;
	private @EJB IGenereDocDevisVersPrelevementServiceRemote genereDocDevisVersPrelevementService;
	private @EJB IGenereDocDevisVersProformaServiceRemote genereDocDevisVersProformaService;
	private @EJB IGenereDocDevisVersDevisServiceRemote genereDocDevisVersDevisService;
	
	private @EJB IGenereDocProformaVersApporteurServiceRemote genereDocProformaVersApporteurService;
	private @EJB IGenereDocProformaVersFactureServiceRemote genereDocProformaVersFactureService;
	private @EJB IGenereDocProformaVersAvoirServiceRemote genereDocProformaVersAvoirService;
	private @EJB IGenereDocProformaVersBonLivraisonServiceRemote genereDocProformaVersBonLivraisonService;
	private @EJB IGenereDocProformaVersBonCommandeServiceRemote genereDocProformaVersCommandeService;
	private @EJB IGenereDocProformaVersPrelevementServiceRemote genereDocProformaVersPrelevementService;
	private @EJB IGenereDocProformaVersProformaServiceRemote genereDocProformaVersProformaService;
	private @EJB IGenereDocProformaVersDevisServiceRemote genereDocProformaVersDevisService;
	
	private @EJB IGenereDocPrelevementVersApporteurServiceRemote genereDocPrelevementVersApporteurService;
	private @EJB IGenereDocPrelevementVersFactureServiceRemote genereDocPrelevementVersFactureService;
	private @EJB IGenereDocPrelevementVersAvoirServiceRemote genereDocPrelevementVersAvoirService;
	private @EJB IGenereDocPrelevementVersBonLivraisonServiceRemote genereDocPrelevementVersBonLivraisonService;
	private @EJB IGenereDocPrelevementVersBonCommandeServiceRemote genereDocPrelevementVersCommandeService;
	private @EJB IGenereDocPrelevementVersPrelevementServiceRemote genereDocPrelevementVersPrelevementService;
	private @EJB IGenereDocPrelevementVersProformaServiceRemote genereDocPrelevementVersProformaService;
	private @EJB IGenereDocPrelevementVersDevisServiceRemote genereDocPrelevementVersDevisService;
	


	private @EJB ITaTiersServiceRemote taTiersService;
	private @EJB ITaCPaiementServiceRemote taCPaiementService;
	private @EJB ITaTCPaiementServiceRemote taTCPaiementService;
	private @EJB ITaInfoEntrepriseServiceRemote taInfoEntrepriseService;	
	private @EJB ITaFactureServiceRemote taFactureService;	
	private @EJB ITaTAdrServiceRemote taTAdrService;
	private @EJB ITaTLigneServiceRemote taTLigneService;
	private @EJB ITaRDocumentServiceRemote taRDocumentService;
	private @EJB  ITaTypeMouvementServiceRemote taTypeMouvementService;
	
	@EJB private ITaApporteurServiceRemote taApporteurService;
	@EJB private ITaTicketDeCaisseServiceRemote taTicketDeCaisseService;
	@EJB private ITaAvoirServiceRemote taAvoirService;
	@EJB private ITaDevisServiceRemote taDevisService;
	@EJB private ITaBonlivServiceRemote taBonlivService;
	@EJB private ITaBoncdeServiceRemote taBoncdeService;
	@EJB private ITaAcompteServiceRemote taAcompteService;
	@EJB private ITaProformaServiceRemote taProformaService;
	@EJB private ITaAvisEcheanceServiceRemote taAvisEcheanceService;
	@EJB private ITaPrelevementServiceRemote taPrelevementService;	
	
	@EJB private ITaBonReceptionServiceRemote taBonReceptionService;
	@EJB private ITaFabricationServiceRemote taFabricationService;
	@EJB private ITaAbonnementServiceRemote taAbonnementService;
	@EJB private ITaPreparationServiceRemote taPreparationService;
	

	public CreationDocumentMultiple() {

	}

	

	public RetourGenerationDoc creationDocument(boolean controleExistance) {
		RetourGenerationDoc retour=new RetourGenerationDoc();

		try {
	
			IDocumentTiers docGenere = null;
			final TypeDoc typeDocPresent = TypeDoc.getInstance();
//			String idEditor ="";
			String typeCPaiement="";
			TaCPaiement taCPaiement1=null;
			TaCPaiement taCPaiement2=null;
			TaTiers tiers = null;
			String commentaireDefaut="";
			String libelleDocument="";
			String libelleComplementaire="";
			String libelleNumeroExterne="";
			
			TaPreferences gestionLot= taPreferencesService.findByCode("gestion_des_lots");
//			TaPreferences gestionCrd= taPreferencesService.findByCode(ITaPreferencesServiceRemote.STOCK_MOUVEMENTER_CRD_POUR_LIGNE_AVOIR);
			
			if(selectedType.equals(TypeDoc.TYPE_AVOIR)) {
				typeCPaiement=TaTCPaiement.C_CODE_TYPE_AVOIR;
				docGenere = new TaAvoir();
				libelleDocument="Avoir N°: ";				
			} else if(selectedType.equals(TypeDoc.TYPE_DEVIS)) {
				typeCPaiement=TaTCPaiement.C_CODE_TYPE_DEVIS;
				docGenere = new TaDevis();
				libelleDocument="Devis N°: ";
			} else if(selectedType.equals(TypeDoc.TYPE_FACTURE)) {
				typeCPaiement=TaTCPaiement.C_CODE_TYPE_FACTURE;
				docGenere = new TaFacture();
				libelleDocument="Facture N°: ";
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
			}else if(selectedType.equals(TypeDoc.TYPE_PANIER)) {
				typeCPaiement=TaTCPaiement.C_CODE_TYPE_PANIER;
				docGenere = new TaPanier();
				libelleDocument="Panier N°: ";
			}

			if(conserveNbDecimalesDoc==false) {
				TaPreferences nb;
				nb = taPreferencesService.findByGoupeAndCle(ITaPreferencesServiceRemote.GROUPE_PREFERENCES_NB_DECIMALES, ITaPreferencesServiceRemote.NB_DECIMALES_PRIX);
				if(nb!=null && nb.getValeur() != null) {
					docGenere.setNbDecimalesPrix(LibConversion.stringToInteger(nb.getValeur()));
				}

//				nb = taPreferencesService.findByGoupeAndCle(ITaPreferencesServiceRemote.GROUPE_PREFERENCES_NB_DECIMALES, ITaPreferencesServiceRemote.NB_DECIMALES_QTE);
//				if(nb!=null && nb.getValeur() != null) {
//					docGenere.setNbDecimalesQte(LibConversion.stringToInteger(nb.getValeur()));
//				}
			}
			else {
				docGenere.setNbDecimalesPrix(documentSrc.getNbDecimalesPrix());
				docGenere.setNbDecimalesQte(documentSrc.getNbDecimalesQte());
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

			if(listeDocument.size()>0){
				listeDocument.get(0).setRelationDocument(true);
				genereDocument = create(listeDocument.get(0), docGenere);
			}
			
			if(selectedType.equals(TypeDoc.TYPE_AVOIR)) {
				genereDocument.setDocumentGereLesCrd(getMouvementerCrdAvoir());
			}
//			genereDocument.setLigneSeparatrice(getParam().getLigneSeparatrice());
			genereDocument.setRecupLibelleSeparateurDoc(!getParam().getRepriseAucun());
			genereDocument.setLigneSeparatrice(genereDocument.getRecupLibelleSeparateurDoc());
			
			genereDocument.setCapturerReglement(getParam().isCapturerReglement());
			
			int nbListeDoc=0;
			boolean genererCode=false;
//			if(controleExistance)controleExistance=!getParam().isGenerationLigne();
			for (IDocumentTiers document : listeDocument) {
				document.setRelationDocument(controleExistance);
				if(controleExistance && (!genereDocument.dejaGenere(document,getParam().isAccepterMultiType())||getParam().isGenerationLigne())) {
					continuer=true;
				}
				if(continuer){
					nbListeDoc++;
					if(param.getRepriseReferenceSrc()) {						
						if(document.getTypeDocument().equals(TaBonliv.TYPE_DOC) )libelleComplementaire=" livré le "+LibDate.dateToString(document.getDateLivDocument());
						genereDocument.setLibelleSeparateurDoc(document.getCodeDocument()+" du "+
								LibDate.dateToString(document.getDateDocument())+libelleComplementaire);
						if(document.getNumeroCommandeFournisseur()!=null && !document.getNumeroCommandeFournisseur().isEmpty()) {
							genereDocument.setLibelleSeparateurDoc(genereDocument.getLibelleSeparateurDoc()+"- Numéro externe : "+document.getNumeroCommandeFournisseur());
						}

					}
					else
						genereDocument.setLibelleSeparateurDoc(document.getLibelleDocument());
					
					
					
					if(genereDocument.getRecupLibelleSeparateurDoc()==false){
						genereDocument.setLibelleSeparateurDoc("");
						genereDocument.setLigneSeparatrice(false);
					}
					
					if(!LibChaine.empty(nouveauLibelle)) {
						genereDocument.setLibelleDoc(nouveauLibelle);
					}
					if(document.getTaTiers().getTaCPaiement()!=null && selectedType.equals( TypeDoc.TYPE_FACTURE)){
						taCPaiement2=document.getTaTiers().getTaCPaiement();
					}
					if(taCPaiement2!=null){
						genereDocument.setTaCPaiement(taCPaiement2);
					}
					
					Date date = param.getDateDocument();
					if (date.before(document.getDateDocument()))
						date=document.getDateDocument();
					genereDocument.setDateDoc(taInfoEntrepriseService.dateDansExercice(date));
					
					if((param.isTiersModifiable() && codeTiers!=null && !codeTiers.trim().equals("") && 
					taTiersService.findByCode(codeTiers)!=null)){
						changeTiers=(!codeTiers.equals(document.getTaTiers().getCodeTiers()));
						genereDocument.setCodeTiers(codeTiers);
					}else codeTiers=null;

					genererCode=nbListeDoc==1;
//					genererCode=nbListeDoc==listeDocument.size();
					
					docGenere.setGestionLot(LibConversion.StringToBoolean(gestionLot.getValeur()));
					genereDocument.setDocumentGereLesLots(LibConversion.StringToBoolean(gestionLot.getValeur()));
					
					
					docGenere = genereDocument.genereDocument(document,docGenere,codeTiers,false,generationModele,genererCode,param.isMultiple());
					

					

					
					if(!LibChaine.empty(nouveauLibelle)) {
						docGenere.setLibelleDocument(nouveauLibelle);
					} else {
						docGenere.setLibelleDocument(libelleDocument+" "+docGenere.getCodeDocument());
					}

				} else {
					continuer=false;
					retour.setMessage("Ce document a déjà servi à générer un document." +Const.finDeLigne+ "La génération du document a échoué.");
					retour.setRetour(false);

				}
			}
			if(continuer){
				if(dateDocument!=null){
					docGenere.setDateDocument(dateDocument);
				}
				if(dateLivDocument!=null){
					docGenere.setDateLivDocument(dateLivDocument);
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
					}else if(selectedType.equals(TypeDoc.TYPE_PANIER)) {
						if(((TaPanier)docGenere).getDateEchDocument()==null)
							((TaPanier)docGenere).setDateEchDocument(dateDocument);
					}						
						
					
				}
				if(docGenere.getCommentaire().trim().equals("") )
					docGenere.setCommentaire(commentaireDefaut);
				docGenere.setDateExport(null);
				
				docGenere=genereDocument.enregistreDocument(docGenere);

				

				retour.setRetour(true);
				retour.setDoc(docGenere);
				retour.setIdDoc(docGenere.getIdDocument());
				retour.setMessage("Le document '"+docGenere.getCodeDocument()+"' à été généré correctement.");
			}
//		}
			
		} catch (Exception e) {
			logger.error("", e);
			retour=null;
		}
			return retour;

	}

	
	public String getNouveauLibelle() {
		return nouveauLibelle;
	}


	public void setNouveauLibelle(String nouveauLibelle) {
		this.nouveauLibelle = nouveauLibelle;
	}


	public List<IDocumentTiers> getListeDocument() {
		return listeDocument;
	}


	public void setListeDocument(List<IDocumentTiers> listeDocument) {
		this.listeDocument = listeDocument;
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



	public IDocumentTiers getDocumentSrc() {
		return documentSrc;
	}

	public void setDocumentSrc(IDocumentTiers documentSrc) {
		this.documentSrc = documentSrc;
	}

	public ParamAfficheChoixGenerationDocument getParam() {
		return param;
	}

	public void setParam(ParamAfficheChoixGenerationDocument param) {
		//typeSrc = ((ParamAfficheChoixGenerationDocument)param).getTypeSrc();
		selectedType =((ParamAfficheChoixGenerationDocument)param).getTypeDest();
		documentSrc = ((ParamAfficheChoixGenerationDocument)param).getDocumentSrc();
		listeDocument=((ParamAfficheChoixGenerationDocument)param).getListeDocumentSrc();
		listeLigneDocument=((ParamAfficheChoixGenerationDocument)param).getListeLigneDocumentSrc();
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


	public IAbstractGenereDocServiceRemote create(IDocumentTiers src, IDocumentTiers dest) {
		IAbstractGenereDocServiceRemote genereDoc = null;
		
		if(src instanceof TaFacture) {
			if(dest instanceof TaDevis) {
				genereDoc = genereDocFactureVersDevisService;
			}
			if(dest instanceof TaFacture) {
				genereDoc =  genereDocFactureVersFactureService;
			}
		}
		if(src instanceof TaAcompte) {
			if(dest instanceof TaFacture) {
				genereDoc = genereDocAcompteVersFactureService;
			} else if(dest instanceof TaBonliv) {
				genereDoc = genereDocAcompteVersBonLivraisonService;
			} else if(dest instanceof TaProforma) {
				genereDoc = genereDocAcompteVersProformaService;
			}else if(dest instanceof TaBoncde) {
				genereDoc = genereDocAcompteVersCommandeService;
			}else if(dest instanceof TaDevis) {
				genereDoc = genereDocAcompteVersDevisService;
			}else if(dest instanceof TaAvoir) {
				genereDoc = genereDocAcompteVersAvoirService;
			}else if(dest instanceof TaApporteur) {
				genereDoc = genereDocAcompteVersApporteurService;
			}else if(dest instanceof TaPrelevement) {
				genereDoc = genereDocAcompteVersPrelevementService;
			}
		}		
		if(src instanceof TaDevis) {
			if(dest instanceof TaFacture) {
				genereDoc = genereDocDevisVersFactureService;
			} else if(dest instanceof TaBonliv) {
				genereDoc =genereDocDevisVersBonLivraisonService;
			} else if(dest instanceof TaProforma) {
				genereDoc = genereDocDevisVersProformaService;
			}else if(dest instanceof TaBoncde) {
				genereDoc = genereDocDevisVersCommandeService;
			}else if(dest instanceof TaDevis) {
				genereDoc = genereDocDevisVersDevisService;
			}else if(dest instanceof TaAvoir) {
				genereDoc = genereDocDevisVersAvoirService;
			}else if(dest instanceof TaApporteur) {
				genereDoc = genereDocDevisVersApporteurService;
			}else if(dest instanceof TaPrelevement) {
				genereDoc = genereDocDevisVersPrelevementService;
			}
		} else if(src instanceof TaFacture) {
			if(dest instanceof TaBonliv) {
				genereDoc = genereDocFactureVersBonLivraisonService;
			} else if(dest instanceof TaAvoir) {
				genereDoc = genereDocFactureVersAvoirService;
			}else if(dest instanceof TaFacture) {
				genereDoc =  genereDocFactureVersFactureService;
			}else if(dest instanceof TaDevis) {
				genereDoc = genereDocFactureVersDevisService;
			}else if(dest instanceof TaProforma) {
				genereDoc = genereDocFactureVersProformaService;
			}else if(dest instanceof TaApporteur) {
				genereDoc = genereDocFactureVersApporteurService;
			}else if(dest instanceof TaBoncde) {
				genereDoc = genereDocFactureVersCommandeService;
			}else if(dest instanceof TaPrelevement) {
				genereDoc = genereDocFactureVersPrelevementService;
			}
			
		} else if(src instanceof TaBoncde) {
			if(dest instanceof TaFacture) {
				genereDoc = genereDocBonCommandeVersFactureService;
			} else if(dest instanceof TaBonliv) {
				genereDoc = genereDocBonCommandeVersBonLivraisonService;
			}else if(dest instanceof TaBoncde) {
				genereDoc = genereDocBonCommandeVersBonCommandeService;
			}else if(dest instanceof TaApporteur) {
				genereDoc =genereDocBonCommandeVersApporteurService;
			}else if(dest instanceof TaAvoir) {
				genereDoc = genereDocBonCommandeVersAvoirService;
			}else if(dest instanceof TaDevis) {
				genereDoc = genereDocBonCommandeVersDevisService;
			}else if(dest instanceof TaProforma) {
				genereDoc = genereDocBonCommandeVersProformaService;
			}else if(dest instanceof TaPrelevement) {
				genereDoc = genereDocBonCommandeVersPrelevementService;
			}
			
		} 
		 else if(src instanceof TaBoncdeAchat) {
				if(dest instanceof TaBonReception) {
					genereDoc = genereDocBonCommandeAchatVersBonReceptionService;
				}
				if(dest instanceof TaBoncdeAchat) {
					genereDoc = genereDocBonCommandeAchatVersBonCommandeAchatService;
				}				
			}
		 else if(src instanceof TaPreparation) {
				if(dest instanceof TaBonliv) {
					genereDoc = genereDocBonPreparationVersBonLivraisonService;
				}
				
			}
		 else if(src instanceof TaPanier) {
				if(dest instanceof TaBoncde) {
					genereDoc = genereDocPanierVersBoncdeService;
				}
				if(dest instanceof TaFacture) {
					genereDoc = genereDocPanierVersFactureService;
				}	
				if(dest instanceof TaPanier) {
					genereDoc = genereDocPanierVersPanierService;
				}				
			}		
	 else if(src instanceof TaBoncde) {
		if(dest instanceof TaFacture) {
			genereDoc = genereDocBonCommandeVersFactureService;
		} else if(dest instanceof TaBonliv) {
			genereDoc = genereDocBonCommandeVersBonLivraisonService;
		}else if(dest instanceof TaBoncde) {
			genereDoc = genereDocBonCommandeVersBonCommandeService;
		}else if(dest instanceof TaApporteur) {
			genereDoc =genereDocBonCommandeVersApporteurService;
		}else if(dest instanceof TaAvoir) {
			genereDoc = genereDocBonCommandeVersAvoirService;
		}else if(dest instanceof TaDevis) {
			genereDoc = genereDocBonCommandeVersDevisService;
		}else if(dest instanceof TaProforma) {
			genereDoc = genereDocBonCommandeVersProformaService;
		}else if(dest instanceof TaPrelevement) {
			genereDoc = genereDocBonCommandeVersPrelevementService;
		}
		
	}
		else if(src instanceof TaAvoir) {
			if(dest instanceof TaAvoir) {
				genereDoc = genereDocAvoirVersAvoirService;
			}
		} else if(src instanceof TaApporteur) {
			if(dest instanceof TaApporteur) {
				genereDoc = genereDocApporteurVersApporteurService;
			}
	
		} else if(src instanceof TaProforma) {
			if(dest instanceof TaFacture) {
				genereDoc = genereDocProformaVersFactureService;
			} else if(dest instanceof TaBonliv) {
				genereDoc = genereDocProformaVersBonLivraisonService;
			} else if(dest instanceof TaBoncde) {
				genereDoc = genereDocProformaVersCommandeService;
			}else if(dest instanceof TaProforma) {
				genereDoc = genereDocProformaVersProformaService;
			}else if(dest instanceof TaApporteur) {
				genereDoc = genereDocProformaVersApporteurService;
			}else if(dest instanceof TaAvoir) {
				genereDoc = genereDocProformaVersAvoirService;
			}else if(dest instanceof TaDevis) {
				genereDoc = genereDocProformaVersDevisService;
			}else if(dest instanceof TaPrelevement) {
				genereDoc = genereDocProformaVersPrelevementService;
			}
			
		}  else if(src instanceof TaPrelevement) {
			if(dest instanceof TaFacture) {
				genereDoc = genereDocPrelevementVersFactureService;
			} else if(dest instanceof TaBonliv) {
				genereDoc = genereDocPrelevementVersBonLivraisonService;
			} else if(dest instanceof TaBoncde) {
				genereDoc = genereDocPrelevementVersCommandeService;
			}else if(dest instanceof TaProforma) {
				genereDoc = genereDocPrelevementVersProformaService;
			}else if(dest instanceof TaApporteur) {
				genereDoc = genereDocPrelevementVersApporteurService;
			}else if(dest instanceof TaAvoir) {
				genereDoc = genereDocPrelevementVersAvoirService;
			}else if(dest instanceof TaDevis) {
				genereDoc = genereDocPrelevementVersDevisService;
			}else if(dest instanceof TaPrelevement) {
				genereDoc = genereDocPrelevementVersPrelevementService;
			}
			
		}else if(src instanceof TaBonliv) {
			if(dest instanceof TaFacture) {
				genereDoc = genereDocBonLivraisonVersFactureService;
			}else if(dest instanceof TaBonliv) {
				genereDoc = genereDocBonLivraisonVersBonLivraisonService;
			}else if(dest instanceof TaApporteur) {
				genereDoc = genereDocBonLivraisonVersApporteurService;
			}else if(dest instanceof TaAvoir) {
				genereDoc = genereDocBonLivraisonVersAvoirService;
			}else if(dest instanceof TaBoncde) {
				genereDoc = genereDocBonLivraisonVersCommandeService;
			}else if(dest instanceof TaDevis) {
				genereDoc = genereDocBonLivraisonVersDevisService;
			}else if(dest instanceof TaProforma) {
				genereDoc = genereDocBonLivraisonVersProformaService;
			}else if(dest instanceof TaPrelevement) {
				genereDoc = genereDocBonLivraisonVersPrelevementService;
			}
		}else if(src instanceof TaAvisEcheance) {
			if(dest instanceof TaFacture) {
				genereDoc = genereDocAvisEcheanceVersFactureService;
			}else if(dest instanceof TaAvisEcheance) {
				genereDoc = genereDocAvisEcheanceVersAvisEcheanceService;
			}else if(dest instanceof TaPrelevement) {
				genereDoc = genereDocAvisEcheanceVersPrelevementService;
			}
		}else if(src instanceof TaTicketDeCaisse) {
			if(dest instanceof TaFacture) {
				genereDoc = genereDocTicketDeCaisseVersFactureService;
			}
		}
//		genereDoc.setTaRDocumentService(taRDocumentService);
//		genereDoc.setTaTLigneService(taTLigneService);
//		genereDoc.setTaTAdrService(taTAdrService);
//		genereDoc.setTaTiersService(taTiersService);
		
		genereDoc.setRelationDocument(src.getRelationDocument());
		return genereDoc;
	}



	public boolean isGenerationLigne() {
		return generationLigne;
	}



	public void setGenerationLigne(boolean generationLigne) {
		this.generationLigne = generationLigne;
	}

public boolean getMouvementerCrdAvoir() {
	return mouvementerCrdAvoir;
}

public void setMouvementerCrdAvoir(boolean mouvementerCrdAvoir) {
	this.mouvementerCrdAvoir = mouvementerCrdAvoir;
}	

}
