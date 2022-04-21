package fr.legrain.bdg.webapp.documents;

import java.io.File;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;
import org.primefaces.PrimeFaces.Ajax;
import org.primefaces.component.autocomplete.AutoComplete;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.remotecommand.RemoteCommand;
import org.primefaces.event.FlowEvent;
import org.primefaces.event.ReorderEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;

import fr.legrain.abonnement.stripe.model.TaStripePaymentIntent;
//import fr.legrain.abonnement.stripe.dto.TaStripeSubscriptionItemDTO;
//import fr.legrain.abonnement.stripe.model.TaStripeSubscription;
//import fr.legrain.abonnement.stripe.model.TaStripeSubscriptionItem;
import fr.legrain.article.dto.ArticleLotEntrepotDTO;
import fr.legrain.article.dto.TaArticleDTO;
import fr.legrain.article.dto.TaTransporteurDTO;
import fr.legrain.article.dto.TaUniteDTO;
import fr.legrain.article.model.TaArticle;
import fr.legrain.article.model.TaCoefficientUnite;
import fr.legrain.article.model.TaEntrepot;
import fr.legrain.article.model.TaLot;
import fr.legrain.article.model.TaTransporteur;
import fr.legrain.article.model.TaUnite;
import fr.legrain.article.titretransport.model.TaTitreTransport;
import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaCoefficientUniteServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaEntrepotServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaEtatStockServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaGrMouvStockServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaLabelArticleServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaLotServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaMouvementStockServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaParamEan128ServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaTransporteurServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaTvaServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaTypeMouvementServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaUniteServiceRemote;
import fr.legrain.bdg.article.titretransport.service.remote.ITaTitreTransportServiceRemote;
import fr.legrain.bdg.conformite.service.remote.ITaResultatConformiteServiceRemote;
import fr.legrain.bdg.conformite.service.remote.ITaStatusConformiteServiceRemote;
import fr.legrain.bdg.controle.service.remote.ITaGenCodeExServiceRemote;
import fr.legrain.bdg.documents.service.remote.IDocumentCodeGenere;
import fr.legrain.bdg.documents.service.remote.ITaFactureServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLigneALigneEcheanceServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaRDocumentServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaReglementServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaTLigneServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaPreferencesServiceRemote;
import fr.legrain.bdg.edition.service.remote.ITaEditionServiceRemote;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceDocumentRemote;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.lib.server.osgi.BdgProperties;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.tiers.service.remote.ITaAdresseServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaCPaiementServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaCompteBanqueServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaEtatServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTAdrServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTCPaiementServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTDocServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTEtatServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTPaiementServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTTiersServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTTvaDocServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.bdg.webapp.ConstWeb;
import fr.legrain.bdg.webapp.LgrTabEvent;
import fr.legrain.bdg.webapp.SessionListener;
import fr.legrain.bdg.webapp.agenda.EvenementParam;
import fr.legrain.bdg.webapp.app.AbstractController;
import fr.legrain.bdg.webapp.app.AutorisationBean;
import fr.legrain.bdg.webapp.app.EmailParam;
import fr.legrain.bdg.webapp.app.EmailPieceJointeParam;
import fr.legrain.bdg.webapp.app.EtiquetteCodeBarreBean;
import fr.legrain.bdg.webapp.app.LgrTab;
import fr.legrain.bdg.webapp.app.PushParam;
import fr.legrain.bdg.webapp.app.TabListModelBean;
import fr.legrain.bdg.webapp.app.TabViewsBean;
import fr.legrain.bdg.webapp.reglementMultiple.TaReglementDTOJSF;
import fr.legrain.document.dto.IDocumentCalcul;
import fr.legrain.document.dto.IDocumentDTO;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.IInfosDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentDTO;
import fr.legrain.document.dto.ILigneDocumentLotDTO;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.dto.TaLigneALigneDTO;
import fr.legrain.document.dto.TaLigneALigneEcheanceDTO;
import fr.legrain.document.dto.TaRDocumentDTO;
import fr.legrain.document.model.RetourGenerationDoc;
import fr.legrain.document.model.SWTDocument;
import fr.legrain.document.model.SWTInfosDocument;
import fr.legrain.document.model.SWTLigneDocument;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaLFacture;
import fr.legrain.document.model.TaLigneALigneEcheance;
import fr.legrain.document.model.TaReglement;
import fr.legrain.document.model.TaTPaiement;
import fr.legrain.document.model.TaTicketDeCaisse;
import fr.legrain.dossier.model.TaInfoEntreprise;
import fr.legrain.dossier.model.TaPreferences;
import fr.legrain.droits.service.SessionInfo;
import fr.legrain.droits.service.TenantInfo;
import fr.legrain.edition.dto.TaEditionDTO;
import fr.legrain.edition.model.TaEdition;
import fr.legrain.generationDocument.model.ParamAfficheChoixGenerationDocument;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.LibDate;
import fr.legrain.lib.data.ModeObjetEcranServeur;
import fr.legrain.lib.data.VerrouInterface;
import fr.legrain.tiers.dto.AdresseInfosFacturationDTO;
import fr.legrain.tiers.dto.AdresseInfosLivraisonDTO;
import fr.legrain.tiers.dto.IdentiteTiersDTO;
import fr.legrain.tiers.dto.TaCPaiementDTO;
import fr.legrain.tiers.dto.TaTiersDTO;
import fr.legrain.tiers.model.TaAdresse;
import fr.legrain.tiers.model.TaCPaiement;
import fr.legrain.tiers.model.TaEmail;
import fr.legrain.tiers.model.TaTTvaDoc;
import fr.legrain.tiers.model.TaTiers;


public abstract class AbstractDocumentController
	<
	Document extends SWTDocument & IDocumentTiers & IDocumentCalcul, 
	DocumentDTO extends IDocumentDTO, 
	Ligne extends SWTLigneDocument & ILigneDocumentTiers, 
	LigneDTO extends ILigneDocumentDTO, 
//	LigneJSF extends ILigneDocumentJSF<ILigneDocumentDTO>
	LigneJSF extends ILigneDocumentJSF,
	InfosDocument extends SWTInfosDocument & IInfosDocumentTiers
	> 
	extends AbstractController
	implements ICRUDController, ICRUDLigneDocumentController {  
	
	//A gérer ailleurs, ou de façon plus globale, mis en place juste pour tester les lots dans les factures
//	protected boolean gestionLot = true;
	protected boolean ouvertureDocumentIndirect = false;
//	protected boolean gestionCapsules = true;

	protected List<AdresseInfosFacturationDTO> listeAdresseInfosFacturationDTO=new LinkedList<>();
	protected List<AdresseInfosLivraisonDTO> listeAdresseInfosLivraisonDTO=new LinkedList<>();
	protected String stepSynthese = "idSyntheseXXXXXXX";
	protected String stepEntete = "idEnteteXXXXXXX";
	protected String stepLignes = "idLignesXXXXXXX";
	protected String stepTotaux = "idTotauxFormXXXXXXX";
	protected String stepValidation = "idValidationFormXXXXXXX";
	protected String idMenuBouton = "form:tabView:idMenuBoutonXXXXXXX";
	protected String idBtnWizardButtonPrecedent = "form:tabView:idBtnWizardButtonPrecedentXXXXXXX";
	protected String idBtnWizardButtonSuivant = "form:tabView:idBtnWizardButtonSuivantXXXXXXX";
	protected String wvEcran = "XXXXXXX";
	protected String classeCssDataTableLigne = "myclass";
	protected TaLigneALigneDTO selectedLigneALigneDTO; 
	protected String variableEcran ="ZZZZZZZ";
	
	protected String currentStepId;
	//list permettant de faire la correspondance entre les lots / articles , les entrepot et les emplacement
	protected List<ArticleLotEntrepotDTO> listArticleLotEntrepot;
	
	protected boolean ecranSynthese=false;
	@Inject @Named(value="tabListModelCenterBean")
	protected TabListModelBean tabsCenter;
	
	@Inject @Named(value="tabViewsBean")
	protected TabViewsBean tabViews;
	
	@Inject @Named(value="etiquetteCodeBarreBean")
	protected EtiquetteCodeBarreBean etiquetteCodeBarreBean;
	
	@Inject @Named(value="duplicationDocumentBean")
	protected DuplicationDocumentBean duplicationDocumentBean;
	
//	@Inject protected AutorisationBean autorisationBean;
	@Inject protected TenantInfo tenantInfo;
	@Inject protected SessionInfo sessionInfo;

	protected String paramId;

	protected List<DocumentDTO> values; 
	protected List<LigneJSF> valuesLigne;
	protected transient org.primefaces.component.wizard.Wizard wizardDocument;
	protected transient RemoteCommand rc;
	
	protected String messageTerminerLigne=Const.C_MESSAGE_TERMINER_LIGNE_DOCUMENT;
	protected String messageSuppression=Const.C_MESSAGE_SUPPRESSION_DOCUMENT;
	protected String titreSuppression="Confirmation de la suppression de l'enregistrement";
	
	protected String messageModification=Const.C_MESSAGE_MODIFIFCATION_DOCUMENT;
	protected String titreModification="Confirmation de la modification de l'enregistrement";
	

//	protected ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();
	protected ModeObjetEcranServeur modeEcranLigne = new ModeObjetEcranServeur();

	protected IGenericCRUDServiceDocumentRemote<Document,DocumentDTO> taDocumentService;
	protected IGenericCRUDServiceRemote taInfosDocumentService;
	protected IGenericCRUDServiceRemote<Ligne,LigneDTO> taLDocumentService;
	
	protected @EJB ITaTLigneServiceRemote taTLigneService;
	protected @EJB ITaTiersServiceRemote taTiersService;
	protected @EJB ITaTransporteurServiceRemote taTransporteurService;
	protected @EJB ITaTTiersServiceRemote taTTiersService;
//	protected @EJB ITaRapportUniteServiceRemote taRapportUniteService;
//	protected @EJB ITaCoefficientUniteServiceRemote taCoefficientUniteService;
	protected @EJB ITaArticleServiceRemote taArticleService;
	protected @EJB ITaEntrepotServiceRemote taEntrepotService;
	protected @EJB ITaEtatStockServiceRemote taEtatStockService;
	protected @EJB ITaLotServiceRemote taLotService;
	protected @EJB ITaLabelArticleServiceRemote taLabelArticleService;
	protected @EJB ITaResultatConformiteServiceRemote taResultatConformiteService;
	protected @EJB ITaStatusConformiteServiceRemote taStatusConformiteService;
	protected @EJB ITaTypeMouvementServiceRemote taTypeMouvementService;
//	protected @EJB ITaMouvementStockServiceRemote taMouvementStockService;
	protected @EJB ITaGrMouvStockServiceRemote taGrMouvStockService;
//	protected @EJB ITaUniteServiceRemote taUniteService;
	protected @EJB ITaTTvaDocServiceRemote taTTvaDocService;
	protected @EJB ITaGenCodeExServiceRemote taGenCodeExService;

//	protected @EJB ITaPreferencesServiceRemote taPreferencesService;
	protected @EJB ITaTitreTransportServiceRemote taTitreTransportService;
	protected @EJB ITaTvaServiceRemote taTvaService;
	protected @EJB ITaParamEan128ServiceRemote taParamEan128Service;
	
	protected @EJB ITaAdresseServiceRemote taAdresseService;
	protected @EJB ITaTAdrServiceRemote taTAdrService;
	protected @EJB ITaTCPaiementServiceRemote taTCPaiementService;
	protected @EJB ITaTPaiementServiceRemote taTPaiementService;
	protected @EJB ITaReglementServiceRemote taReglementService;
	protected @EJB ITaCompteBanqueServiceRemote taCompteBanqueService;
	protected @EJB ITaCPaiementServiceRemote taCPaiementService;
	protected @EJB ITaTDocServiceRemote taTDocService;
	protected @EJB ITaTEtatServiceRemote taTEtatService;
	
	protected @EJB ITaEditionServiceRemote taEditionService;
	protected @EJB ITaEtatServiceRemote taEtatService;
//	protected  @EJB ITaRDocumentServiceRemote taRDocumentService;
	
	protected @EJB ITaFactureServiceRemote taFactureService;
	
	protected String typeAdresseFacturation="FACT";
	protected String typeAdresseLivraison="LIV";
	
	protected DocumentDTO[] selectedDocumentDTOs; 
	protected Document masterEntity = newDocument();
	protected DocumentDTO newDocumentDTO = newDocumentDTO();
	protected DocumentDTO selectedDocumentDTO = newDocumentDTO();

	protected InfosDocument taInfosDocument = newInfosDocument();
	
	protected LigneJSF[] selectedLigneJSFs; 
	protected Ligne masterEntityLigne = newLigne();
	protected LigneJSF oldLigneJSF = newLigneJSF();
	protected LigneJSF selectedLigneJSF = newLigneJSF();
	protected LigneJSF detailLigneOverLayPanel = null;
	

	protected AdresseInfosFacturationDTO taAdresseFact = new AdresseInfosFacturationDTO();
	protected AdresseInfosFacturationDTO selectedAdresseFact = new AdresseInfosFacturationDTO();
	protected Class classModelAdresseFact = AdresseInfosFacturationDTO.class;
	
	protected AdresseInfosLivraisonDTO taAdresseLiv = new AdresseInfosLivraisonDTO();
	protected AdresseInfosLivraisonDTO selectedAdresseLiv = new AdresseInfosLivraisonDTO();
	protected Class classModelAdresseLiv = AdresseInfosLivraisonDTO.class;
	
	protected TaCPaiementDTO selectedCPaiement = new TaCPaiementDTO();
	protected Class classModelCPaiement = TaCPaiementDTO.class;
	
	protected IdentiteTiersDTO selectedInfosTiers = new IdentiteTiersDTO();
	
	protected TaTiers taTiers;
	protected TaTiersDTO taTiersDTO;
	protected TaArticle taArticleLigne;
	protected TaEntrepot taEntrepotLigne;
	protected TaTPaiement taTPaiement=new TaTPaiement();
	protected TaReglement taReglement;
	protected TaTTvaDoc taTTvaDoc;
	protected TaTransporteurDTO taTransporteurDTO;
	protected TaTransporteur taTransporteur;

	protected TaCPaiement taCPaiementDoc = null;
	protected boolean differenceReglementResteARegle=false;
	protected boolean differenceReglementResteARegleForPrint=false;
	
	protected List<TaRDocumentDTO> docLie;
	protected List<TaLigneALigneDTO> ligneLie;

	protected LgrDozerMapper<DocumentDTO,Document> mapperUIToModel  = new LgrDozerMapper<DocumentDTO,Document>();
	protected LgrDozerMapper<Document,DocumentDTO> mapperModelToUI  = new LgrDozerMapper<Document,DocumentDTO>();
	protected LgrDozerMapper<TaTiers,TaTiersDTO> mapperModelToUITiers  = new LgrDozerMapper<TaTiers,TaTiersDTO>();
	protected LgrDozerMapper<TaTiersDTO,TaTiers> mapperUIToModelTiers  = new LgrDozerMapper<TaTiersDTO,TaTiers>();

	protected LgrDozerMapper<TaAdresse,AdresseInfosFacturationDTO> mapperModelToUIAdresseInfosDocument = new LgrDozerMapper<TaAdresse,AdresseInfosFacturationDTO>();
	protected LgrDozerMapper<TaAdresse,AdresseInfosLivraisonDTO> mapperModelToUIAdresseLivInfosDocument = new LgrDozerMapper<TaAdresse,AdresseInfosLivraisonDTO>();
	protected LgrDozerMapper<TaCPaiement,TaCPaiementDTO> mapperModelToUICPaiementInfosDocument = new LgrDozerMapper<TaCPaiement,TaCPaiementDTO>();
	
	protected LgrDozerMapper<InfosDocument,TaCPaiementDTO> mapperModelToUIInfosDocVersCPaiement = new LgrDozerMapper<InfosDocument,TaCPaiementDTO>();
	protected LgrDozerMapper<InfosDocument,AdresseInfosFacturationDTO> mapperModelToUIInfosDocVersAdresseFact = new LgrDozerMapper<InfosDocument,AdresseInfosFacturationDTO>();
	protected LgrDozerMapper<InfosDocument,AdresseInfosLivraisonDTO> mapperModelToUIInfosDocVersAdresseLiv = new LgrDozerMapper<InfosDocument,AdresseInfosLivraisonDTO>();
		
	protected LgrDozerMapper<TaCPaiementDTO,InfosDocument> mapperUIToModelCPaiementVersInfosDoc = new LgrDozerMapper<TaCPaiementDTO, InfosDocument>();
	protected LgrDozerMapper<AdresseInfosFacturationDTO,InfosDocument> mapperUIToModelAdresseFactVersInfosDoc = new LgrDozerMapper<AdresseInfosFacturationDTO, InfosDocument>();
	protected LgrDozerMapper<AdresseInfosLivraisonDTO,InfosDocument> mapperUIToModelAdresseLivVersInfosDoc = new LgrDozerMapper<AdresseInfosLivraisonDTO, InfosDocument>();
	
	protected LgrDozerMapper<Document,InfosDocument> mapperUIToModelDocumentVersInfosDoc = new LgrDozerMapper<Document, InfosDocument>();
	
	//private static final String JS_FONCTION_INITIALISE_POSITION_BOUTON_WIZARD = "initialisePositionBoutonWizard()";
	protected String JS_FONCTION_INITIALISE_POSITION_BOUTON_WIZARD = "initialisePositionBoutonWizard('')";
	protected boolean btnPrecedentVisible = false;
	protected boolean btnSuivantVisible = true;
	
	protected TaStripePaymentIntent paiementParCarteEnAttente = null;
	
	protected String libelleReglementEnAttente ="";
	protected String libelleMultipleReglement ="";
	protected boolean factureReglee =false;
	protected boolean factureARegler =true;
	protected boolean insertAuto = true;
	protected String typePaiementDefaut=null;
	protected String libellePaiement;
	protected boolean impressionDirect = false;
	protected String css;
	
	protected boolean impressionDirectClient = false;
	protected boolean miseADispositionCompteClient = false;
	protected boolean envoyeParEmail = false;
	
	protected boolean choixEdition = false;
	protected LocalDate dateMin;
	protected LocalDate dateMax;
	
	protected boolean gestionBoutonWizardDynamique = false;
	
	protected List<TaReglementDTOJSF> listeTaRReglementDTOJSF=new LinkedList<>();
	protected boolean docEnregistre=true;
	protected String nomDocument;
	
	protected TaEtat selectedEtatDocument;
	protected List<TaEtat> listeEtatDocument;
	protected List<TaPreferences> listePreferences;
	
	protected boolean modeScanCodeBarre = false;
	protected boolean dejaLie=false;
	protected TaReglementDTOJSF detailLigneReglement;
	
	protected List<TaEditionDTO> listeEdition = new ArrayList<TaEditionDTO>();

	protected TaEdition selectedEdition = null;
	protected Integer prefNbDecimalesPrix;
	protected Integer prefNbDecimalesQte;
	
	protected boolean afficheUniteSaisie=false;
	protected LigneJSF  ligneAReenregistrer;
	
	
	public static final String MESSAGE_MULTIPLE_REGLEMENT   = "Document réglé en plusieurs fois";
	public static final String MESSAGE_SIMPLE_REGLEMENT   = "";
	
	protected @EJB ITaLigneALigneEcheanceServiceRemote taLigneALigneEcheanceService;
	
	List<TaLigneALigneEcheance> listeLigneALigneEcheanceASupprimer = new ArrayList<TaLigneALigneEcheance>();

	
	public TaEtat getSelectedEtatDocument() {
		return selectedEtatDocument;
	}

	public void setSelectedEtatDocument(TaEtat selectedEtatDocument) {
		this.selectedEtatDocument = selectedEtatDocument;
	}

	public List<TaEtat> getListeEtatDocument() {
		return listeEtatDocument;
	}

	public void setListeEtatDocument(List<TaEtat> listeEtatDocument) {
		this.listeEtatDocument = listeEtatDocument;
	}

	public AbstractDocumentController() {  

	}  

	@PostConstruct
	public void postConstruct(){
		listeLigneALigneEcheanceASupprimer.clear();
		dateMin = LibDate.dateToLocalDateTime(new Date(taInfoEntrepriseService.findInstance().getDatedebInfoEntreprise().getTime())).toLocalDate();
		dateMax = LibDate.dateToLocalDateTime(new Date(taInfoEntrepriseService.findInstance().getDatefinInfoEntreprise().getTime())).toLocalDate();
		TaPreferences nb;
		nb = taPreferencesService.findByGoupeAndCle(ITaPreferencesServiceRemote.GROUPE_PREFERENCES_NB_DECIMALES, ITaPreferencesServiceRemote.NB_DECIMALES_PRIX);
		if(nb!=null && nb.getValeur() != null) {
			prefNbDecimalesPrix=LibConversion.stringToInteger(nb.getValeur());
		}
		
		TaPreferences us = taPreferencesService.findByGoupeAndCle("articles", "utilisation_unite_saisie");
		if(us!=null)afficheUniteSaisie=LibConversion.StringToBoolean(us.getValeur());
		
		rechercheAvecCommentaire(true);
//		nb = taPreferencesService.findByGoupeAndCle(ITaPreferencesServiceRemote.GROUPE_PREFERENCES_NB_DECIMALES, ITaPreferencesServiceRemote.NB_DECIMALES_QTE);
//		if(nb!=null && nb.getValeur() != null) {
//			prefNbDecimalesQte=LibConversion.stringToInteger(nb.getValeur());
//		}


		
	}


	public abstract void refresh();
//	{
//		values = taDocumentService.selectAllDTO();
//		valuesLigne = IHMmodel();
//		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
//	}
	
	
	/**
	 * Méthode qui est déclenchée au clic sur envoi de mail
	 * Elle prépare l'email, et créer un fichier XML qui contient l'édition séléctionné selectedEdition dans un dossier /tmp/bdg,
	 * si l'édition séléctionné selectedEdition est vide, c'est l'édition par defaut qui est utilisé,
	 * puis génère le pdf et le passe en pièce jointe du mail,
	 * enfin, elle appelle la méthode actDialogEmail qui ouvre le dialog
	 * @author yann
	 * @param actionEvent
	 */
	public void actDialogEmailSelectedEdition(ActionEvent actionEvent) {	
		EmailParam email = new EmailParam();
		email.setEmailExpediteur(null);
		email.setNomExpediteur(taInfoEntrepriseService.findInstance().getNomInfoEntreprise()); 
		email.setSubject(taInfoEntrepriseService.findInstance().getNomInfoEntreprise()+" "+nomDocument+" "+masterEntity.getCodeDocument()); 
		email.setBodyPlain("Votre "+nomDocument+" "+ masterEntity.getCodeDocument());
		email.setBodyHtml("Votre "+nomDocument+" "+ masterEntity.getCodeDocument());
		email.setDestinataires(new String[]{masterEntity.getTaTiers().getTaEmail().getAdresseEmail()});
		email.setEmailDestinataires(new TaEmail[]{masterEntity.getTaTiers().getTaEmail()});
		
		BdgProperties bdgProperties = new BdgProperties();
		String localPath = bdgProperties.osTempDirDossier(tenantInfo.getTenantId())+"/"+bdgProperties.tmpFileName(selectedEdition.getFichierNom());
		List<String> listeResourcesPath = null;
		/**
		 * Ecriture du xml BIRT dans /tmp/bdg
		 * on utilise ici la méthode writingFile présente dans AbstracApplicationDAOServer commune a tous les services des documents
		 * J'utilise ici le factureService mais je pourrait tout aussi bien utiliser n'importe quel autre service implémentant AbstracApplicationDAOServer
		 * C'est pas optimal au niveau de la lecture de code mais c'est validé par Nicolas. Voir éventuellement à créer un service général qui ne fait rien d'autre que d'implémenté AbstracApplicationDAOServer
		 * ou éventuellement modifier AbstracApplicationDAOServer pour qu'elle ne soit plus abstraite mais attention !
		 * @author yann
		 */
		taFactureService.writingFileEdition(selectedEdition);
//		
//		Files.write(Paths.get(localPath), selectedEdition.getFichierBlob());
//		AnalyseFileReport afr = new AnalyseFileReport();
//		afr.initializeBuildDesignReportConfig(localPath);
//		afr.ajouteLogo();
//		afr.closeDesignReportConfig();
		
		if(selectedEdition.getResourcesPath()!=null) {
			listeResourcesPath = new ArrayList<>();
			listeResourcesPath.add(selectedEdition.getResourcesPath());
		}
		
		if(selectedEdition != null) {
			EmailPieceJointeParam pj1 = new EmailPieceJointeParam();
			pj1.setFichier(new File((taDocumentService).generePDF(masterEntity.getIdDocument(),localPath,selectedEdition.getMapParametreEdition(),listeResourcesPath,selectedEdition.getTheme())));
			pj1.setTypeMime("pdf");
			pj1.setNomFichier(pj1.getFichier().getName());
			email.setPiecesJointes(
					new EmailPieceJointeParam[]{pj1}
					);
		}else {
			EmailPieceJointeParam pj1 = new EmailPieceJointeParam();
			pj1.setFichier(new File((taDocumentService).generePDF(masterEntity.getIdDocument(),null, null, null)));
			pj1.setTypeMime("pdf");
			pj1.setNomFichier(pj1.getFichier().getName());
			email.setPiecesJointes(
					new EmailPieceJointeParam[]{pj1}
					);
		}
		
		actDialogEmail(email);
	}
	
	
	
	
	
	private Object newGenerics(int postion) {
		try {
			if(this.getClass().getGenericSuperclass() instanceof java.lang.reflect.ParameterizedType) {
//				System.out.println("OK ****** AbstractDocumentController.newGenerics() "+this.getClass().getName());
				return ((Class)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[postion]).newInstance();
			} else {
//				System.out.println("PAS OK ****** AbstractDocumentController.newGenerics() "+this.getClass().getName()+" ** >"+this.getClass().getSuperclass().getName());
				return ((Class)((ParameterizedType)this.getClass().getSuperclass().getGenericSuperclass()).getActualTypeArguments()[postion]).newInstance();
			}
			
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private Document newDocument() {
		return (Document) newGenerics(0);
	}
	
	private DocumentDTO newDocumentDTO() {
		return (DocumentDTO) newGenerics(1);
	}
	
	private Ligne newLigne() {
		return (Ligne) newGenerics(2);
	}
	
	private LigneDTO newLigneDTO() {
		return (LigneDTO) newGenerics(3);
	}
	
	private LigneJSF newLigneJSF() {
		return (LigneJSF) newGenerics(4);
	}
	
	private InfosDocument newInfosDocument() {
		return (InfosDocument) newGenerics(5);
	}


	public abstract void IHMmodel(LigneJSF t, Ligne ligne);
	
	public abstract List<LigneJSF> IHMmodel();
	
	public boolean stepEnCours(String step ){		
		return step.equals(currentStepId);
	}
	
	public String handleFlow(FlowEvent event) {

		try{
			currentStepId = event.getOldStep();
			String stepToGo = event.getNewStep();
	
			if(modeEcranLigne.dataSetEnModif())
				actEnregistrerLigne(null);
			
			btnPrecedentVisible = true;
			btnSuivantVisible = true;
			
			if(currentStepId.equals(stepEntete) && stepToGo.equals(stepLignes)) {
				mapperUIToModel.map(selectedDocumentDTO, masterEntity);
			} else if(currentStepId.equals(stepLignes) && stepToGo.equals(stepTotaux)) {
				btnSuivantVisible = false;
			} else {
				if(stepSynthese.equals("idSyntheseXXXXXXX")) {//pas implémenter
					if(currentStepId.equals(stepLignes) && stepToGo.equals(stepEntete))btnPrecedentVisible = false;
				}else {
					if(currentStepId.equals(stepEntete) && stepToGo.equals(stepSynthese))btnPrecedentVisible = false;
				}
				
			}
			
			PrimeFaces.current().ajax().update(idBtnWizardButtonPrecedent);
			PrimeFaces.current().ajax().update(idBtnWizardButtonSuivant);
			
			scrollToTop();
			//executer ici car pas d'evenement de type oncomplete sur le handlerFlow mais doit pourtant etre executer à la fin après toute les mise jour de la vue (y compris le scroll)
			initialisePositionBoutonWizard();
			
			currentStepId=event.getNewStep();
			PrimeFaces.current().ajax().update(idMenuBouton);
			
			return currentStepId;

		} catch (Exception e) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Facture", e.getMessage()));
			return event.getOldStep();
		}
	}
	
	public void changeModeSaisieCodeBarre() {
		changeModeSaisieCodeBarre(null);
    }
	
	public void changeModeSaisieCodeBarre(ActionEvent actionEvent) {
		String activeScan ="activeScan('.zone-scan-codebarre')";
		String desactiveScan ="desactiveScan('.zone-scan-codebarre')";

		PrimeFaces requestContext = PrimeFaces.current();  
		if(modeScanCodeBarre)
			requestContext.executeScript(activeScan);
		else
			requestContext.executeScript(desactiveScan);
    }

	public List<DocumentDTO> getValues(){  
		return values;
	}
	
	/* (non-Javadoc)
	 * @see fr.legrain.bdg.webapp.documents.ICRUDLigneDocumentcontroller#actAutoInsererLigne(javax.faces.event.ActionEvent)
	 */
	@Override
	public void actAutoInsererLigne(ActionEvent actionEvent) /*throws Exception*/ {
		boolean existeDeja=false;
		if(insertAuto) {
			for (LigneJSF ligne : valuesLigne) {
				if(ligne.ligneEstVide())existeDeja=true;
			}
			if(!existeDeja){	
			actInsererLigne(actionEvent);
			
			String oncomplete="jQuery('."+classeCssDataTableLigne+" .ui-datatable-data tr').last().find('span.ui-icon-pencil').each(function(){jQuery(this).click()});";
			PrimeFaces.current().executeScript(oncomplete);
			}
		} else {
//			throw new Exception();
		}
	}
	
	/* (non-Javadoc)
	 * @see fr.legrain.bdg.webapp.documents.ICRUDLigneDocumentcontroller#actInsererLigne(javax.faces.event.ActionEvent)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void actInsererLigne(ActionEvent actionEvent) {
	
		
				
		LigneJSF nouvelleLigne = newLigneJSF();
		nouvelleLigne.setDto(newLigneDTO());
		nouvelleLigne.getDto().setNumLigneLDocument(valuesLigne.size()+1);
//		nouvelleLigne.getDto().setNumLot(selectedDocumentDTO.getCodeDocument()+"_"+nouvelleLigne.getDto().getNumLigneLDocument());//@@ champs à ajouter aux facture
		
		//Rajout Yann
		//nouvelleLigne.setDto2(newLigneDTO());
		

		masterEntityLigne = newLigne(); 
		masterEntityLigne.setLegrain(true);
		try {
			//H = HT => pas nécessaire, mais il ne faut pas de ligne de commentaire  pour passer trigger "ligne_vide"
			masterEntityLigne.setTaTLigne(taTLigneService.findByCode(Const.C_CODE_T_LIGNE_H));
		
			//List<LigneJSF> modelListeLigneFacture = IHMmodel();

			masterEntity.insertLigne(masterEntityLigne,nouvelleLigne.getDto().getNumLigneLDocument());
//			masterEntity.addLigne(masterEntityLigne);
		} catch (FinderException e) {
			e.printStackTrace();
		}
		
		catch (Exception e) {
			e.printStackTrace();
		}

//		selectedDocumentDTO.setNumLigneLDocument(masterEntityLigne.getNumLigneLDocument()); //@@ champs à ajouter aux facture
		masterEntityLigne.setTaDocumentGeneral(masterEntity);
		masterEntityLigne.initialiseLigne(true);

		valuesLigne.add(nouvelleLigne);
		//isa le 14/08/2019
		selectedLigneJSF=nouvelleLigne;
		//pour gérér l'annulation
		oldLigneJSF=(LigneJSF) nouvelleLigne.copy(nouvelleLigne);
		
		modeEcranLigne.setMode(EnumModeObjet.C_MO_INSERTION);
		//		}

		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Facture", "actInsererLigne")); 
		}
	}
	
	
	public void actImprimerPourClient() {
		if(!impressionDirect) {
			impressionDirectClient = false;
		}
	}

	/* (non-Javadoc)
	 * @see fr.legrain.bdg.webapp.documents.ICRUDLigneDocumentcontroller#actEnregistrerLigne(javax.faces.event.ActionEvent)
	 */
	@Override
	public abstract void actEnregistrerLigne(ActionEvent actionEvent);

	
	/* (non-Javadoc)
	 * @see fr.legrain.bdg.webapp.documents.ICRUDLigneDocumentcontroller#actAnnulerLigne(javax.faces.event.ActionEvent)
	 */
	@Override
	public void actAnnulerLigne(ActionEvent actionEvent) {
		if(selectedLigneJSF.getDto().getIdLDocument()!=null) {
			masterEntityLigne =rechercheLigne(selectedLigneJSF.getDto().getIdLDocument());
			//pour gérér l'annulation
			selectedLigneJSF=(LigneJSF) oldLigneJSF.copy(oldLigneJSF);
			actEnregistrerLigne(null);		
		} else {
			masterEntityLigne =rechercheLigneNumLigne(selectedLigneJSF.getDto().getNumLigneLDocument());
			try {
				masterEntity.removeLigne(masterEntityLigne);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		valuesLigne= IHMmodel();
		if(!valuesLigne.isEmpty())
			selectionLigne(valuesLigne.get(0));		
		setInsertAuto(false);
		modeEcranLigne.setMode(EnumModeObjet.C_MO_CONSULTATION);

		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Facture", "actAnnulerLigne")); 
		}
	}

	/* (non-Javadoc)
	 * @see fr.legrain.bdg.webapp.documents.ICRUDLigneDocumentcontroller#actModifierLigne(javax.faces.event.ActionEvent)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void actModifierLigne(ActionEvent actionEvent) {
		if(oldLigneJSF == null) {
			oldLigneJSF = newLigneJSF();
		}
		//pour gérér l'annulation
		oldLigneJSF=(LigneJSF) oldLigneJSF.copy(selectedLigneJSF);
		modeEcranLigne.setMode(EnumModeObjet.C_MO_EDITION);

		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Facture", "actModifierLigne")); 
		}
	}

	public Ligne rechercheLigne(int id){
		Ligne obj=masterEntityLigne;
		for (ILigneDocumentTiers ligne : masterEntity.getLignesGeneral()) {
			if(ligne.getIdLDocument()==id)
				obj=(Ligne) ligne;
		}
		return obj;
	}
	
	public Ligne rechercheLigneNumLigne(int numLigne){
		Ligne obj=masterEntityLigne;
		for (ILigneDocumentTiers ligne : masterEntity.getLignesGeneral()) {
			if(ligne.getNumLigneLDocument()==numLigne)
				obj=(Ligne) ligne;
		}
		return obj;
	}
	
	public DocumentDTO rechercheDansListeValues(String codeDocument){
		for (DocumentDTO doc : values) {
			if(doc.getCodeDocument().equals(codeDocument))
				return doc;
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see fr.legrain.bdg.webapp.documents.ICRUDLigneDocumentcontroller#actSupprimerLigne()
	 */
	@Override
	public void actSupprimerLigne() {
		actSupprimerLigne(null);
	}
	
	/* (non-Javadoc)
	 * @see fr.legrain.bdg.webapp.documents.ICRUDLigneDocumentcontroller#actSupprimerLigne(javax.faces.event.ActionEvent)
	 */
	@Override
	public void actSupprimerLigne(ActionEvent actionEvent) {
		try {
			valuesLigne.remove(selectedLigneJSF);
			
//			for (Ligne lbr : masterEntity.getLignes()) {
//				if(selectedLigneJSF.getDto().getIdLDocument()==lbr.getIdLDocument()) {
//					masterEntityLigne = lbr;
//				}
//			}
//			
//			masterEntityLigne =rechercheLigne(selectedLigneJSF.getDto().getIdLDocument());

			if(selectedLigneJSF.getDto().getIdLDocument()!=null && selectedLigneJSF.getDto().getIdLDocument()!=0) {
				masterEntityLigne =rechercheLigne(selectedLigneJSF.getDto().getIdLDocument());
			} else {
				masterEntityLigne =rechercheLigneNumLigne(selectedLigneJSF.getDto().getNumLigneLDocument());
			}
			
			masterEntityLigne.setTaDocumentGeneral(null);	
			masterEntity.removeLigne(masterEntityLigne);
			
			

			masterEntity.calculeTvaEtTotaux();
			valuesLigne=IHMmodel();
			mapperModelToUI.map(masterEntity, selectedDocumentDTO);
			
			modeEcranLigne.setMode(EnumModeObjet.C_MO_CONSULTATION);
	
			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Facture", "actSupprimerLigne")); 
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void onRowEdit(RowEditEvent event) {
			try {
				selectedLigneJSF.updateDTO(true);
				if(selectedLigneJSF.getDto().getIdLDocument()!=null &&  selectedLigneJSF.getDto().getIdLDocument()!=0) {
					masterEntityLigne=rechercheLigne(selectedLigneJSF.getDto().getIdLDocument());
				}else if(selectedLigneJSF.getDto().getNumLigneLDocument()!=null/* &&  selectedTaLBonReceptionDTOJSF.getDto().getNumLigneLDocument()!=0*/) {
					masterEntityLigne = rechercheLigneNumLigne(selectedLigneJSF.getDto().getNumLigneLDocument());
				}
				validateLigneDocumentAvantEnregistrement(selectedLigneJSF);
				actEnregistrerLigne(null);
				setInsertAuto(modeEcran.getMode()==EnumModeObjet.C_MO_INSERTION);
			} catch (Exception e) {
				e.printStackTrace();
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Facture", e.getMessage()));	
				context.validationFailed();
				//RequestContext.getCurrentInstance().addCallbackParam("notValid", true);
				setInsertAuto(false);
//				throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage()));
			}
	}
	
	public void onRowCancel(RowEditEvent event) {
		//((LigneJSF) event.getObject()).setAutoInsert(false);
//        FacesMessage msg = new FacesMessage("Edit Cancelled", ((Car) event.getObject()).getId());
//        FacesContext.getCurrentInstance().addMessage(null, msg);
		selectionLigne((LigneJSF) event.getObject());
		actAnnulerLigne(null);
    }
	
	public void onRowEditInit(RowEditEvent event) {
		AjaxBehaviorEvent evt = (AjaxBehaviorEvent)event;
		DataTable table = (DataTable) evt.getSource();
		int activeRow = table.getRowIndex()+1;
		if(table.getRowCount() == activeRow) {
			//derniere ligne
			setInsertAuto(modeEcran.dataSetEnInsertion());
		} else {
			setInsertAuto(false);
		}
		if(event.getObject()!=null && !event.getObject().equals(selectedLigneJSF))
			selectedLigneJSF=(LigneJSF) event.getObject();
		actModifierLigne(null);
		enabledUniteSaisie();
		//changeModeSaisieCodeBarre();
	}
	
	public void onRowSelectLigne(SelectEvent event) { 
		if(event.getObject()!=null)
			selectionLigne((LigneJSF) event.getObject());		
	}
	
	public void selectionLigne(LigneJSF ligne){
		selectedLigneJSF=ligne;
		if(masterEntity.getIdDocument()!=0 && selectedLigneJSF!=null && selectedLigneJSF.getDto()!=null
				&& selectedLigneJSF.getDto().getIdLDocument()!=null)
			masterEntityLigne=rechercheLigne(selectedLigneJSF.getDto().getIdLDocument());
		else if(selectedLigneJSF!=null && selectedLigneJSF.getDto()!=null)
			masterEntityLigne=rechercheLigneNumLigne(selectedLigneJSF.getDto().getNumLigneLDocument());
	}

	/* (non-Javadoc)
	 * @see fr.legrain.bdg.webapp.documents.ICRUDController#actAnnuler(javax.faces.event.ActionEvent)
	 */
	@Override
	abstract public void actAnnuler(ActionEvent actionEvent);

	abstract public void autoCompleteMapUIToDTO();

	abstract public void autoCompleteMapDTOtoUI();

	public void onRowReorder(ReorderEvent event) {
		int i=1;
		actModifier();
		for (LigneJSF l : valuesLigne) {
			if(l.getDto().getIdLDocument()!=null &&  l.getDto().getIdLDocument()!=0) {
				masterEntityLigne=rechercheLigne(l.getDto().getIdLDocument());
			}else if(l.getDto().getNumLigneLDocument()!=null) {
				masterEntityLigne = rechercheLigneNumLigne(l.getDto().getNumLigneLDocument());
			}
			l.getDto().setNumLigneLDocument(i);
			//change le num ligne et remplace l'odre des lignes dans la liste des lignes du document
			masterEntityLigne.setNumLigneLDocument(i);
			masterEntity.getLignesGeneral().remove(masterEntityLigne);
			masterEntity.getLignesGeneral().add(masterEntityLigne.getNumLigneLDocument()-1, masterEntityLigne);
			i++;
		}

		if(valuesLigne!=null && !valuesLigne.isEmpty())
			selectionLigne(valuesLigne.get(0));
	}
	
	/* (non-Javadoc)
	 * @see fr.legrain.bdg.webapp.documents.ICRUDController#actEnregistrer(javax.faces.event.ActionEvent)
	 */
	@Override
	public abstract void actEnregistrer(ActionEvent actionEvent);

	/* (non-Javadoc)
	 * @see fr.legrain.bdg.webapp.documents.ICRUDController#actInserer(javax.faces.event.ActionEvent)
	 */
	@Override
	public void actInserer(ActionEvent actionEvent) {
		try {
			selectedDocumentDTO = newDocumentDTO();
			masterEntity = newDocument();
			masterEntity.setLegrain(true);
			valuesLigne = IHMmodel();
			selectedDocumentDTO.setCodeTTvaDoc("F");
			factureReglee=false;
			factureARegler=true;
			selectedEtatDocument=null;
			autoCompleteMapDTOtoUI();
			
			initInfosDocument();


			//			selectedDocumentDTO.setCodeDocument(taBonReceptionService.genereCode()); //ejb
			//			validateUIField(Const.C_CODE_DOCUMENT,selectedDocumentDTO.getCodeDocument());//permet de verrouiller le code genere

//			selectedDocumentDTO.setCodeDocument(LibDate.dateToStringTimeStamp(new Date(), "", "", ""));
			taGenCodeExService.libereVerrouTout(new ArrayList<String>(SessionListener.getSessions().keySet()));
			Map<String, String> paramsCode = new LinkedHashMap<>();
			paramsCode.put(Const.C_DATEDOCUMENT, LibDate.dateToString(selectedDocumentDTO.getDateDocument()));
//			paramsCode.put(Const.C_NOMFOURNISSEUR, selectedDocumentDTO.getNomTiers());
			
			if(selectedDocumentDTO.getCodeDocument()!=null) {
				if(taDocumentService instanceof IDocumentCodeGenere) {
					((IDocumentCodeGenere) taDocumentService).annuleCode(selectedDocumentDTO.getCodeDocument());
				}
			}			
//			String newCode = taFactureService.genereCode(paramsCode);
//			if(newCode!=null && !newCode.equals("")){
//				selectedDocumentDTO.setCodeDocument(newCode);
//			}
			if(taDocumentService instanceof IDocumentCodeGenere) {
				selectedDocumentDTO.setCodeDocument(((IDocumentCodeGenere) taDocumentService).genereCode(paramsCode));
			}
			changementTiers(true);
			//			selectedDocumentDTO.setCodeCompta("aa"); //ejb
			//			validateUIField(Const.C_CODE_COMPTA,selectedDocumentDTO.getCodeCompta()); //ejb
			//			selectedDocumentDTO.setCompte("111"); //ejb
			//			validateUIField(Const.C_COMPTE,selectedDocumentDTO.getCompte()); //ejb

			//			TaTTiers taTTiers;
			//
			//			taTTiers = taTTiersService.findByCode(codeTiersDefaut);
			//
			//			if(taTTiers!=null && taTTiers.getCompteTTiers()!=null) {
			//				selectedDocumentDTO.setCompte(taTTiers.getCompteTTiers());
			//				this.taTTiers = taTTiers;
			//			} else {
			//				//selectedDocumentDTO.setCompte(TiersPlugin.getDefault().getPreferenceStore().
			//				//getString(PreferenceConstants.TIERS_COMPTE_COMPTALE_DEFAUT));
			//			}
			//			selectedDocumentDTO.setActifTiers(true);
			//
			//			selectedDocumentDTO.setCodeTTvaDoc("F");
			
			
			TaPreferences nb;
			nb = taPreferencesService.findByGoupeAndCle(ITaPreferencesServiceRemote.GROUPE_PREFERENCES_NB_DECIMALES, ITaPreferencesServiceRemote.NB_DECIMALES_PRIX);
			if(nb!=null && nb.getValeur() != null) {
				masterEntity.setNbDecimalesPrix(LibConversion.stringToInteger(nb.getValeur()));
			}

//			nb = taPreferencesService.findByGoupeAndCle(ITaPreferencesServiceRemote.GROUPE_PREFERENCES_NB_DECIMALES, ITaPreferencesServiceRemote.NB_DECIMALES_QTE);
//			if(nb!=null && nb.getValeur() != null) {
//				masterEntity.setNbDecimalesQte(LibConversion.stringToInteger(nb.getValeur()));
//			}
			
			modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
			if(wizardDocument!=null) {
				wizardDocument.setStep(this.stepEntete);
			}
			
			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Facture", "actInserer"));
			}

		} catch (Exception e) {
			e.printStackTrace();
			//		} catch (FinderException e) {
			//			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see fr.legrain.bdg.webapp.documents.ICRUDController#actModifier()
	 */
	@Override
	public void actModifier() {
		actModifier(null);
	}

	/* (non-Javadoc)
	 * @see fr.legrain.bdg.webapp.documents.ICRUDController#actModifier(javax.faces.event.ActionEvent)
	 */
	@Override
	public void actModifier(ActionEvent actionEvent) {
		if(verifSiEstModifiable(masterEntity) && autorisationLiaisonComplete(false)) {
			if(!modeEcran.getMode().equals(EnumModeObjet.C_MO_EDITION) && !modeEcran.getMode().equals(EnumModeObjet.C_MO_INSERTION))
				modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
			docEnregistre=false;
			masterEntity.setLegrain(true);
			initialisePositionBoutonWizard();
			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Facture", "actModifier")); 	 
			}
			ecranSynthese=modeEcran.getMode().equals(EnumModeObjet.C_MO_CONSULTATION);
			changementStepWizard(true);
		}
	}

	/* (non-Javadoc)
	 * @see fr.legrain.bdg.webapp.documents.ICRUDController#actAide(javax.faces.event.ActionEvent)
	 */
	@Override
	public void actAide(ActionEvent actionEvent) {

		//		PrimeFaces.current().dialog().openDynamic("aide");

		Map<String,Object> options = new HashMap<String, Object>();
		options.put("modal", true);
		options.put("draggable", false);
		options.put("resizable", false);
		options.put("contentHeight", 320);
		Map<String,List<String>> params = null;
		PrimeFaces.current().dialog().openDynamic("aide", options, params);

		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Facture", "actAide")); 	
		}
	}

	//	public void actAideRetour(ActionEvent actionEvent) {
	//		
	//	}

	public abstract void nouveau(ActionEvent actionEvent);

	public void supprimer(ActionEvent actionEvent) {
		actSupprimer(actionEvent);
	}
	
	public void detail() {
    	detail(null);
	}
	
	public void detail(ActionEvent actionEvent) {
		onRowSelect(null);
	}
	
	/* (non-Javadoc)
	 * @see fr.legrain.bdg.webapp.documents.ICRUDController#actSupprimer()
	 */
	@Override
	public void actSupprimer() {
		actSupprimer(null);
	}
	
	public void actBeforeSupprimer(DocumentDTO doc) {
		selectedDocumentDTO=doc;
		autorisationLiaisonComplete();
	}

	/* (non-Javadoc)
	 * @see fr.legrain.bdg.webapp.documents.ICRUDController#actSupprimer(javax.faces.event.ActionEvent)
	 */
	@Override
	public abstract void actSupprimer(ActionEvent actionEvent);

	/* (non-Javadoc)
	 * @see fr.legrain.bdg.webapp.documents.ICRUDController#actFermer(javax.faces.event.ActionEvent)
	 */
	@Override
	public abstract void actFermer(ActionEvent actionEvent);

	/* (non-Javadoc)
	 * @see fr.legrain.bdg.webapp.documents.ICRUDController#actImprimer(javax.faces.event.ActionEvent)
	 */
	@Override
	public abstract void actImprimer(ActionEvent actionEvent);
	
	public void actImprimerEtiquetteCB(ActionEvent actionEvent) {
		
		etiquetteCodeBarreBean.videListe();
		try {
			for (ILigneDocumentTiers l : masterEntity.getLignesGeneral()) {
				etiquetteCodeBarreBean.getListeArticle().add(l.getTaArticle());
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
		
		if(ConstWeb.DEBUG) {
		   	FacesContext context = FacesContext.getCurrentInstance();  
		    context.addMessage(null, new FacesMessage("Facture", "actImprimerEtiquetteCB")); 
		}
	}

	public boolean pasDejaOuvert() {
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_FACTURE);
		return tabsCenter.ongletDejaOuvert(b)==null;
	} 

	public void onRowSimpleSelect(SelectEvent event) {

		try {
			if(pasDejaOuvert()==false){
				onRowSelect(event);
	
				autoCompleteMapDTOtoUI();
				
				masterEntity = taDocumentService.findByIDFetch(selectedDocumentDTO.getId());
				masterEntity.calculeTvaEtTotaux();
				
				valuesLigne = IHMmodel();
				
				if(ConstWeb.DEBUG) {
					FacesContext context = FacesContext.getCurrentInstance();  
					context.addMessage(null, new FacesMessage("Facture", 
							"Chargement du Facture N°"+selectedDocumentDTO.getCodeTiers()
							)); 
				}
			} else {
				tabViews.selectionneOngletCentre(LgrTab.TYPE_TAB_FACTURE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	} 
	
	public void updateTab(){
		try {		
			modeEcranLigne.setMode(EnumModeObjet.C_MO_CONSULTATION);
			ecranSynthese=modeEcran.getMode().equals(EnumModeObjet.C_MO_CONSULTATION);

			if(selectedDocumentDTOs!=null && selectedDocumentDTOs.length>0) {
				selectedDocumentDTO = selectedDocumentDTOs[0];
			}
			if(selectedDocumentDTO.getId()!=null && selectedDocumentDTO.getId()!=0 && !ouvertureDocumentIndirect) {
				masterEntity = taDocumentService.findByIDFetch(selectedDocumentDTO.getId());
			}
			ouvertureDocumentIndirect=false;
			initLocalisationTVA();
			masterEntity.calculeTvaEtTotaux(); // pour que tous les totaux en transient soient remplis aussi
			valuesLigne = IHMmodel();
			
			masterEntity.calculeTvaEtTotaux();
			changementTiers(false);
			
			autorisationLiaisonComplete(true);
			impressionDirectClient = false;
			miseADispositionCompteClient = false;
			envoyeParEmail = false;
			
			//infos sur verrouillage et mise à disposition
			if(masterEntity.getTaMiseADisposition()!=null) {
				impressionDirectClient = masterEntity.getTaMiseADisposition().getImprimePourClient()!=null;
				miseADispositionCompteClient = masterEntity.getTaMiseADisposition().getAccessibleSurCompteClient()!=null;
				envoyeParEmail = masterEntity.getTaMiseADisposition().getEnvoyeParEmail()!=null;
				selectedDocumentDTO.setEstMisADisposition(masterEntity.getTaMiseADisposition().estMisADisposition());
			}
			
			mapperModelToUI.map(masterEntity, selectedDocumentDTO);
			
			
			autoCompleteMapDTOtoUI();
			listeAdresseInfosFacturationDTO=adresseFacAutoCompleteDTO();
			listeAdresseInfosLivraisonDTO=adresseLivAutoCompleteDTO();
			
			btnPrecedentVisible = false;
			btnSuivantVisible = true;
			initialisePositionBoutonWizard();
			
			changementStepWizard(false);
			//TODO si on veut reinitialiser ces variable il faut le faire avec une remoteCommand apres l'excution du js du onsuccess
//			impressionDirect = false;
//			impressionDirectClient = false;
//			miseADispositionCompteClient = false;
//			envoyeParEmail = false;
//			selectedEtatDocument=masterEntity.getTaREtat().getTaEtat();
			etatBouton("supprimer");
			PrimeFaces.current().ajax().update(idMenuBouton);
			
			listeLigneALigneEcheanceASupprimer.clear();
		} catch (FinderException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void initialisePositionBoutonWizard() {
		if(gestionBoutonWizardDynamique) {
			PrimeFaces.current().executeScript(JS_FONCTION_INITIALISE_POSITION_BOUTON_WIZARD);
		}
	}

	public abstract void onRowSelect(SelectEvent event);

	public boolean editable() {
		return !modeEcran.dataSetEnModif();
	}
	public boolean editableEnInsertionUniquement() {
		return !modeEcran.dataSetEnInsertion();
	}
	
	public void actDialogTiers(ActionEvent actionEvent) {

		//		PrimeFaces.current().dialog().openDynamic("aide");

		Map<String,Object> options = new HashMap<String, Object>();
		options.put("modal", true);
		options.put("draggable", false);
		options.put("resizable", false);
		options.put("contentHeight", 640);
		options.put("modal", true);

		//Map<String,List<String>> params = null;
		Map<String,List<String>> params = new HashMap<String,List<String>>();
		List<String> list = new ArrayList<String>();
		list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
		params.put("modeEcranDefaut", list);

		PrimeFaces.current().dialog().openDynamic("tiers/dialog_tiers", options, params);

		//		FacesContext context = FacesContext.getCurrentInstance();  
		//		context.addMessage(null, new FacesMessage("Facture", "actAbout")); 	    
	}

	public void handleReturnDialogTiers(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			taTiers = (TaTiers) event.getObject();
		}
	}

	public void actDialogTypeCivilite(ActionEvent actionEvent) {

		//		PrimeFaces.current().dialog().openDynamic("aide");

		Map<String,Object> options = new HashMap<String, Object>();
		options.put("modal", true);
		options.put("draggable", false);
		options.put("resizable", false);
		options.put("contentHeight", 640);
		options.put("modal", true);

		//Map<String,List<String>> params = null;
		Map<String,List<String>> params = new HashMap<String,List<String>>();
		List<String> list = new ArrayList<String>();
		list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
		params.put("modeEcranDefaut", list);

		PrimeFaces.current().dialog().openDynamic("tiers/dialog_type_civilite", options, params);
		//		FacesContext context = FacesContext.getCurrentInstance();  
		//		context.addMessage(null, new FacesMessage("Facture", "actAbout")); 	    
	}

	public void handleReturnDialogTypeCivilite(SelectEvent event) {
		//		if(event!=null && event.getObject()!=null) {
		//			taTCivilite = (TaTCivilite) event.getObject();
		//		}
	}



	public void actDialogTypeEntite(ActionEvent actionEvent) {

		//		PrimeFaces.current().dialog().openDynamic("aide");

		Map<String,Object> options = new HashMap<String, Object>();
		options.put("modal", true);
		options.put("draggable", false);
		options.put("resizable", false);
		options.put("contentHeight", 640);
		options.put("modal", true);

		//Map<String,List<String>> params = null;
		Map<String,List<String>> params = new HashMap<String,List<String>>();
		List<String> list = new ArrayList<String>();
		list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
		params.put("modeEcranDefaut", list);

		PrimeFaces.current().dialog().openDynamic("tiers/dialog_type_entite", options, params);

		//		FacesContext context = FacesContext.getCurrentInstance();  
		//		context.addMessage(null, new FacesMessage("Facture", "actAbout")); 	    
	}

	public void handleReturnDialogTypeEntite(SelectEvent event) {
		//		if(event!=null && event.getObject()!=null) {
		//			taTEntite = (TaTEntite) event.getObject();
		//		}
	}

	public void actDialogGenerationDocumentLie(ActionEvent actionEvent) {	

		Map<String,Object> options = new HashMap<String, Object>();
		options.put("modal", true);
		options.put("draggable", false);
		options.put("closable", false);
		options.put("resizable", true);
		options.put("contentHeight", 710);
		options.put("contentWidth", "100%");
		options.put("width", 1024);

		Map<String,Object> mapDialogue = new HashMap<String,Object>();

		mapDialogue.put("masterEntityDTO",selectedDocumentDTO );

		Map<String,List<String>> params = new HashMap<String,List<String>>();
		List<String> list = new ArrayList<String>();
		list.add(modeEcran.modeString(EnumModeObjet.C_MO_CONSULTATION));
		params.put("modeEcranDefaut", list);


		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		sessionMap.put("mapDialogue", mapDialogue);

		PrimeFaces.current().dialog().openDynamic("documents/detail_liaison_document.xhtml", options, params);

	}
	
	public void handleReturnDialogDocumentLie(SelectEvent event) {

	}

	public boolean etatBouton(String bouton) {
		boolean retour = true;
		switch (modeEcran.getMode()) {
		case C_MO_INSERTION:
			switch (bouton) {
				case "annuler":
				case "enregistrer":
				case "fermer":
					retour = false;
					break;
				default:
					break;
			}
			break;
		case C_MO_EDITION:
			switch (bouton) {
			case "annuler":
			case "enregistrer":
			case "fermer":
				retour = false;
				break;
			default:
				break;
			}
			break;
		case C_MO_CONSULTATION:
			switch (bouton) {
			case "inserer":
			case "fermer":
				retour = false;
			case "supprimerListe":retour = false;break;	
			case "modifier":
			case "supprimer":
			case "imprimer":
				if(selectedDocumentDTO!=null && selectedDocumentDTO.getId()!=null  && selectedDocumentDTO.getId()!=0 ) retour = false;
				break;				
			default:
				break;
		}
			break;
		default:
			break;
		}
		
		return retour;

	}

	public boolean etatBoutonLigne(String bouton) {
		boolean retour = true;
		if(modeEcran.dataSetEnModif()) {
			retour = false;
		}
		switch (modeEcranLigne.getMode()) {
		case C_MO_INSERTION:
			switch (bouton) {
			case "annuler":
			case "enregistrer":
			case "fermer":
				retour = false;
				break;
			case "rowEditor":
				retour = modeEcran.dataSetEnModif()?true:false;
				break;
			default:
				break;
			}
			break;
		case C_MO_EDITION:
			switch (bouton) {
			case "annuler":
			case "enregistrer":
			case "fermer":
				retour = false;
				break;
			case "rowEditor":
				retour = modeEcran.dataSetEnModif()?true:false;
				break;
			default:
				break;
			}
			break;
		case C_MO_CONSULTATION:
			switch (bouton) {
			case "supprimer":
			case "modifier":
			case "inserer":
			case "imprimer":
			case "fermer":
				retour = modeEcran.dataSetEnModif()?false:true;
				break;
			case "rowEditor":
				retour = modeEcran.dataSetEnModif()?true:false;
				break;
			default:
				break;
			}
			break;
		default:
			break;
		}

		return retour;

	}
	
	public abstract void validateLignesDocument(FacesContext context, UIComponent component, Object value) throws ValidatorException;
	
	public abstract void validateDocument(FacesContext context, UIComponent component, Object value) throws ValidatorException;

	public abstract void validateDocumentAvantEnregistrement( Object value) throws ValidatorException;
	
	public abstract void validateLigneDocumentAvantEnregistrement( Object value) throws ValidatorException ;
	
	public void autcompleteSelection(SelectEvent event) {
		Object value = event.getObject();
		//		FacesMessage msg = new FacesMessage("Selected", "Item:" + value);

		String nomChamp =  (String) event.getComponent().getAttributes().get("nomChamp");
		System.out.println("autcompleteSelection() : "+nomChamp);

		if(value!=null) {
			if(value instanceof String && value.equals(Const.C_AUCUN))value="";	
			if(value instanceof TaUniteDTO && ((TaUniteDTO) value).getCodeUnite()!=null && ((TaUniteDTO) value).getCodeUnite().equals(Const.C_AUCUN))value=null;	
			if(value instanceof TaTitreTransport && ((TaTitreTransport) value).getCodeTitreTransport()!=null && ((TaTitreTransport) value).getCodeTitreTransport().equals(Const.C_AUCUN))value=null;	
		}
		validateUIField(nomChamp,value);
	}
	
	public abstract boolean validateUIField(String nomChamp,Object value);

	protected boolean adresseEstRempli() {
		boolean retour=false;
		//		if(!selectedDocumentDTO.getAdresse1Adresse().equals(""))return true;
		//		if(!selectedDocumentDTO.getAdresse2Adresse().equals(""))return true;
		//		if(!selectedDocumentDTO.getAdresse3Adresse().equals(""))return true;
		//		if(!selectedDocumentDTO.getCodepostalAdresse().equals(""))return true;
		//		if(!selectedDocumentDTO.getVilleAdresse().equals(""))return true;
		//		if(!selectedDocumentDTO.getPaysAdresse().equals(""))return true;
		return retour;
	}

	public List<TaTiers> tiersAutoComplete(String query) {
		List<TaTiers> allValues = taTiersService.selectAll();
		List<TaTiers> filteredValues = new ArrayList<TaTiers>();

		for (int i = 0; i < allValues.size(); i++) {
			TaTiers civ = allValues.get(i);
			if(query.equals("*") || civ.getCodeTiers().toLowerCase().contains(query.toLowerCase())) {
				filteredValues.add(civ);
			}
		}

		return filteredValues;
	}

	public List<TaTiersDTO> tiersAutoCompleteDTOLight(String query) {
		queryTiers = query;
		List<TaTiersDTO> allValues = taTiersService.findByCodeLight("*");
		List<TaTiersDTO> filteredValues = new ArrayList<TaTiersDTO>();

		for (int i = 0; i < allValues.size(); i++) {
			TaTiersDTO civ = allValues.get(i);
			if(query.equals("*") || civ.getCodeTiers().toLowerCase().contains(query.toLowerCase())
					|| civ.getNomTiers().toLowerCase().contains(query.toLowerCase())
					|| (civ.getPrenomTiers() != null && civ.getPrenomTiers().toLowerCase().contains(query.toLowerCase()))
					|| (civ.getNomEntreprise() != null && civ.getNomEntreprise().toLowerCase().contains(query.toLowerCase()))
					) {
				filteredValues.add(civ);
			}
		}

		return filteredValues;
	}
	
	public List<TaTransporteurDTO> transporteurAutoCompleteDTOLight(String query) {
		List<TaTransporteurDTO> allValues = taTransporteurService.findByCodeLight("*");
		List<TaTransporteurDTO> filteredValues = new ArrayList<TaTransporteurDTO>();

		for (int i = 0; i < allValues.size(); i++) {
			TaTransporteurDTO civ = allValues.get(i);
			if(query.equals("*") || civ.getCodeTransporteur().toLowerCase().contains(query.toLowerCase())
					|| civ.getLiblTransporteur().toLowerCase().contains(query.toLowerCase())
					) {
				filteredValues.add(civ);
			}
		}

		return filteredValues;
	}
	
	
	public List<TaEtat> etatAutoComplete(String query) {
		List<TaEtat> allValues = taEtatService.selectAll();
		List<TaEtat> filteredValues = new ArrayList<TaEtat>();

		for (int i = 0; i < allValues.size(); i++) {
			TaEtat civ = allValues.get(i);
			if(query.equals("*") || civ.getCodeEtat().toLowerCase().contains(query.toLowerCase())
					|| civ.getLiblEtat().toLowerCase().contains(query.toLowerCase())) {
				filteredValues.add(civ);
			}
		}

		return filteredValues;
	}
	
	
	public List<TaArticle> articleAutoComplete(String query) {
		List<TaArticle> allValues = taArticleService.selectAll();
		List<TaArticle> filteredValues = new ArrayList<TaArticle>();

		for (int i = 0; i < allValues.size(); i++) {
			TaArticle civ = allValues.get(i);
			if(query.equals("*") || civ.getCodeArticle().toLowerCase().contains(query.toLowerCase())) {
				filteredValues.add(civ);
			}
		}

		return filteredValues;
	}
	

	public List<TaArticleDTO> articleAutoCompleteDTOLight(String query) {
		queryArticle = query;
		List<TaArticleDTO> allValues = taArticleService.findByCodeAndLibelleLight(query.toUpperCase());
		List<TaArticleDTO> filteredValues = new ArrayList<TaArticleDTO>();

		if(query!=null && !query.trim().equals("")) {
//			for (int i = 0; i < allValues.size(); i++) {
//				TaArticleDTO civ = allValues.get(i);
//				if(query.equals("*") || civ.getCodeArticle().toLowerCase().contains(query.toLowerCase())
//						|| (civ.getLibellelArticle()!=null && civ.getLibellelArticle().toLowerCase().contains(query.toLowerCase()))
//						|| (civ.getLibellecArticle()!=null && civ.getLibellecArticle().toLowerCase().contains(query.toLowerCase()))
//						) {
//					filteredValues.add(civ);
//				}
//			}
		} else {
			filteredValues = allValues;
		}
		filteredValues = allValues;
		return filteredValues;
	}
	

	
	public List<AdresseInfosFacturationDTO> adresseFacAutoCompleteDTO() {
		List<AdresseInfosFacturationDTO> allValues = new ArrayList<AdresseInfosFacturationDTO>();

		if(masterEntity!=null && masterEntity.getTaTiers()!=null && masterEntity.getTaTiers().getTaAdresses()!=null){
			for (TaAdresse adresse : masterEntity.getTaTiers().getTaAdresses()) {
				AdresseInfosFacturationDTO dto=new AdresseInfosFacturationDTO();
				new LgrDozerMapper<TaAdresse, AdresseInfosFacturationDTO>().map(adresse,dto );	
				allValues.add(dto);
			}

		}
		return allValues;
	}
	
	public List<AdresseInfosLivraisonDTO> adresseLivAutoCompleteDTO() {
		List<AdresseInfosLivraisonDTO> allValues = new ArrayList<AdresseInfosLivraisonDTO>();

		if(masterEntity!=null && masterEntity.getTaTiers()!=null && masterEntity.getTaTiers().getTaAdresses()!=null){
			for (TaAdresse adresse : masterEntity.getTaTiers().getTaAdresses()) {
				AdresseInfosLivraisonDTO dto=new AdresseInfosLivraisonDTO();
				new LgrDozerMapper<TaAdresse, AdresseInfosLivraisonDTO>().map(adresse,dto );	
				allValues.add(dto);
			}

		}
		return allValues;
	}
	
	public List<TaTPaiement> typePaiementAutoComplete(String query) {
		List<TaTPaiement> allValues = taTPaiementService.selectAll();
		List<TaTPaiement> filteredValues = new ArrayList<TaTPaiement>();
		TaTPaiement cp = new TaTPaiement();
		cp.setCodeTPaiement(Const.C_AUCUN);
		filteredValues.add(cp);
		for (int i = 0; i < allValues.size(); i++) {
			cp = allValues.get(i);
			if(query.equals("*") || cp.getCodeTPaiement().toLowerCase().contains(query.toLowerCase())) {
				filteredValues.add(cp);
			}
		}

		return filteredValues;
	}
	
	public List<TaTTvaDoc> typeTvaDocAutoComplete(String query) {
		List<TaTTvaDoc> allValues = taTTvaDocService.selectAll();
		List<TaTTvaDoc> filteredValues = new ArrayList<TaTTvaDoc>();
		TaTTvaDoc cp = new TaTTvaDoc();
//		cp.setCodeTTvaDoc(Const.C_AUCUN);
//		filteredValues.add(cp);
		for (int i = 0; i < allValues.size(); i++) {
			cp = allValues.get(i);
			if(query.equals("*") || cp.getCodeTTvaDoc().toLowerCase().contains(query.toLowerCase())) {
				filteredValues.add(cp);
			}
		}

		return filteredValues;
	}
	
	public List<TaUnite> uniteAutoComplete(String query) {
		List<TaUnite> allValues = taUniteService.findByCodeUniteStock(selectedLigneJSF.getTaArticle().getTaUniteReference().getCodeUnite(), query.toUpperCase());
		List<TaUnite> filteredValues = new ArrayList<TaUnite>();
		TaUnite civ=new TaUnite();
		civ.setCodeUnite(Const.C_AUCUN);
		filteredValues.add(civ);
		for (int i = 0; i < allValues.size(); i++) {
			 civ = allValues.get(i);
			if(query.equals("*") || civ.getCodeUnite().toLowerCase().contains(query.toLowerCase())) {
				filteredValues.add(civ);
			}
		}

		return filteredValues;
	}
	
	public List<TaUnite> uniteAutoCompleteLightUniteSaisie(String query) {
		List<TaUnite> allValues = null;
		if(selectedLigneJSF.getTaArticle().getTaUnite1()!=null)
		allValues = taUniteService.findByCodeUniteStock(selectedLigneJSF.getTaArticle().getTaUnite1().getCodeUnite(), query.toUpperCase());
		else if(selectedLigneJSF.getTaArticle().getTaUniteReference()!=null)
			allValues = taUniteService.findByCodeUniteStock(selectedLigneJSF.getTaArticle().getTaUniteReference().getCodeUnite(), query.toUpperCase());

		List<TaUnite> filteredValues = new ArrayList<TaUnite>();
		TaUnite civ=new TaUnite();
		civ.setCodeUnite(Const.C_AUCUN);
		filteredValues.add(civ);
		for (int i = 0; i < allValues.size(); i++) {
			 civ = allValues.get(i);
			if(query.equals("*") || civ.getCodeUnite().toLowerCase().contains(query.toLowerCase())) {
				filteredValues.add(civ);
			}
		}

		return filteredValues;
	}
	
	public List<TaUnite> uniteAutoCompleteAll(String query) {
		List<TaUnite> allValues = taUniteService.selectAll();
		List<TaUnite> filteredValues = new ArrayList<TaUnite>();
		TaUnite civ=new TaUnite();
		civ.setCodeUnite(Const.C_AUCUN);
		filteredValues.add(civ);
		for (int i = 0; i < allValues.size(); i++) {
			 civ = allValues.get(i);
			if(query.equals("*") || civ.getCodeUnite().toLowerCase().contains(query.toLowerCase())) {
				filteredValues.add(civ);
			}
		}

		return filteredValues;
	}

	
	public List<TaTitreTransport> titreTransportAutoComplete(String query) {
		List<TaTitreTransport> allValues = taTitreTransportService.selectAll();
		List<TaTitreTransport> filteredValues = new ArrayList<TaTitreTransport>();
		TaTitreTransport civ=new TaTitreTransport();
//		civ.setCodeTitreTransport(Const.C_AUCUN);
//		filteredValues.add(civ);
		for (int i = 0; i < allValues.size(); i++) {
			 civ = allValues.get(i);
			if(query.equals("*") || civ.getCodeTitreTransport().toLowerCase().contains(query.toLowerCase())) {
				filteredValues.add(civ);
			}
		}

		return filteredValues;
	}
	
	public List<TaEntrepot> entrepotAutoComplete(String query) {
		List<TaEntrepot> allValues = taEntrepotService.selectAll();
		List<TaEntrepot> filteredValues = new ArrayList<TaEntrepot>();

		for (int i = 0; i < allValues.size(); i++) {
			TaEntrepot civ = allValues.get(i);
			if(query.equals("*") || civ.getCodeEntrepot().toLowerCase().contains(query.toLowerCase())) {
				filteredValues.add(civ);
			}
		}

		return filteredValues;
	}

	public List<String> emplacementAutoComplete(String query) {
		List<String> filteredValues = new ArrayList<String>();
		if(selectedLigneJSF!=null && selectedLigneJSF.getTaEntrepot()!=null) {
			List<String> allValues = taEtatStockService.emplacementEntrepot(selectedLigneJSF.getTaEntrepot().getCodeEntrepot(),null);
			
			for (int i = 0; i < allValues.size(); i++) {
				String civ = allValues.get(i);
				if(civ !=null && (query.equals("*") || civ.toLowerCase().contains(query.toLowerCase()))) {
					filteredValues.add(civ);
				}
			}
		}
		return filteredValues;
	}
	
	public void actDialogControleLot(ActionEvent actionEvent) {
		
//		PrimeFaces.current().dialog().openDynamic("aide");
    
        Map<String,Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("draggable", false);
        options.put("closable", false);
        options.put("resizable", true);
        options.put("contentHeight", 710);
        options.put("contentWidth", "100%");
        //options.put("contentHeight", "100%");
        options.put("width", 1024);
        
        //Map<String,List<String>> params = null;
        Map<String,List<String>> params = new HashMap<String,List<String>>();
        List<String> list = new ArrayList<String>();
        list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
        params.put("modeEcranDefaut", list);
        
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		TaLot l = new TaLot();
		
//		creerLot();
//		sessionMap.put("lotBR", selectedLigneJSF.getTaLot());
		String numLot =  (String) actionEvent.getComponent().getAttributes().get("lot");
		//sessionMap.put("numLot", selectedLigneJSF.getDto().getNumLot());
		sessionMap.put("numLot", numLot);
        
        PrimeFaces.current().dialog().openDynamic("solstyce/dialog_controle_article", options, params);
		
//		FacesContext context = FacesContext.getCurrentInstance();  
//		context.addMessage(null, new FacesMessage("Tiers", "actAbout")); 	    
	}
	
	public void handleReturnDialogControleLot(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			TaLot j = (TaLot) event.getObject();
			
//			String action = lotService.validationLot(masterEntityLignePF.getTaLot());
//			TaStatusConformite s = taResultatConformiteService.etatLot(masterEntityLignePF.getTaLot().getIdLot());
			masterEntityLigne.setTaLot(j);
			selectedLigneJSF.setTaLot(j);
			
			if(selectedLigneJSF instanceof ILigneDocumentLotDTO) {
				//((ILigneDocumentLotDTO)masterEntityLignePF).setCodeStatusConformite(action);
				((ILigneDocumentLotDTO)masterEntityLigne).setCodeStatusConformite(masterEntityLigne.getTaLot().getTaStatusConformite().getCodeStatusConformite());
			}
			
//			List<ControleConformiteJSF> listeControle = (List<ControleConformiteJSF>) event.getObject();
//			if(listeControle!=null && !listeControle.isEmpty()) {
//				if(masterEntityLigne.getTaLot().getIdLot()==listeControle.get(0).getR().getTaLot().getIdLot()) {
//					masterEntityLigne.getTaLot().getTaResultatConformites().clear();
//					for(ControleConformiteJSF ctrl : listeControle) {
//						if(ctrl.getR()!=null) {
//							masterEntityLigne.getTaLot().getTaResultatConformites().add(ctrl.getR());
//						}
//					}
//				}
//				
//				String action = taLotService.validationLot(masterEntityLigne.getTaLot());
//				TaStatusConformite s = taResultatConformiteService.etatLot(masterEntityLigne.getTaLot().getIdLot());
//				masterEntityLigne.getTaLot().setTaStatusConformite(s);
//				
//				if(selectedLigneJSF.getDto() instanceof ILigneDocumentLotDTO) {
//					((ILigneDocumentLotDTO)selectedLigneJSF.getDto()).setCodeStatusConformite(action);
//				}
//				
//				if(action.equals(TaStatusConformite.C_TYPE_ACTION_A_CORRIGER)
//						|| action.equals(TaStatusConformite.C_TYPE_ACTION_NON_CORRIGE)
//						|| action.equals(TaStatusConformite.C_TYPE_ACTION_CORRIGE)
//						) {
//					masterEntityLigne.getTaLot().setPresenceDeNonConformite(true);
//				} else {
//					masterEntityLigne.getTaLot().setPresenceDeNonConformite(false);
//				} 
//				
//				if(action.equals(TaStatusConformite.C_TYPE_ACTION_OK)
//						|| action.equals(TaStatusConformite.C_TYPE_ACTION_CORRIGE)
//						) {
//					masterEntityLigne.getTaLot().setLotConforme(true);
//				} else {
//					masterEntityLigne.getTaLot().setLotConforme(false);
//				} 
//			}
		}
	}
	

	public TaTiers getTaTiers() {
		return taTiers;
	}

	public void setTaTiers(TaTiers taTiers) {
		this.taTiers = taTiers;
	}

	public List<LigneJSF> getValuesLigne() {
		return valuesLigne;
	}

	public LigneJSF[] getSelectedLigneJSFs() {
		return selectedLigneJSFs;
	}

	public void setSelectedLigneJSFs(
			LigneJSF[] selectedLigneJSFs) {
		this.selectedLigneJSFs = selectedLigneJSFs;
	}

	public LigneJSF getOldLigneJSF() {
		return oldLigneJSF;
	}

	public void setOldLigneJSF(LigneJSF newLigneJSF) {
		this.oldLigneJSF = newLigneJSF;
	}

	public LigneJSF getSelectedLigneJSF() {
		return selectedLigneJSF;
	}

	public void setSelectedLigneJSF(
			LigneJSF selectedLigneJSF) {
		this.selectedLigneJSF = selectedLigneJSF;
	}

	public TaArticle getTaArticleLigne() {
		return taArticleLigne;
	}

	public void setTaArticleLigne(TaArticle taArticleLigne) {
		this.taArticleLigne = taArticleLigne;
	}
	
	public DocumentDTO[] getSelectedDocumentDTOs() {
		return selectedDocumentDTOs;
	}

	public void setSelectedDocumentDTOs(DocumentDTO[] selectedDocumentDTOs) {
		this.selectedDocumentDTOs = selectedDocumentDTOs;
	}

	public DocumentDTO getNewDocumentDTO() {
		return newDocumentDTO;
	}

	public void setNewDocumentDTO(DocumentDTO newDocumentDTO) {
		this.newDocumentDTO = newDocumentDTO;
	}

	public DocumentDTO getSelectedDocumentDTO() {
		return selectedDocumentDTO;
	}

	public void setSelectedDocumentDTO(DocumentDTO selectedDocumentDTO) {
		this.selectedDocumentDTO = selectedDocumentDTO;
	}

	public TabListModelBean getTabsCenter() {
		return tabsCenter;
	}

	public void setTabsCenter(TabListModelBean tabsCenter) {
		this.tabsCenter = tabsCenter;
	}

	public String getParamId() {
		return paramId;
	}

	public void setParamId(String paramId) {
		this.paramId = paramId;
	}

	public TaEntrepot getTaEntrepotLigne() {
		return taEntrepotLigne;
	}

	public void setTaEntrepotLigne(TaEntrepot taEntrepotLigne) {
		this.taEntrepotLigne = taEntrepotLigne;
	}



	public ModeObjetEcranServeur getModeEcranLigne() {
		return modeEcranLigne;
	}

	public boolean isInsertAuto() {
		return insertAuto;
	}

	public EtiquetteCodeBarreBean getEtiquetteCodeBarreBean() {
		return etiquetteCodeBarreBean;
	}

	public void setEtiquetteCodeBarreBean(
			EtiquetteCodeBarreBean etiquetteCodeBarreBean) {
		this.etiquetteCodeBarreBean = etiquetteCodeBarreBean;
	}

	public RemoteCommand getRc() {
		return rc;
	}

	public void setRc(RemoteCommand rc) {
		this.rc = rc;
	}

	public void setInsertAuto(boolean insertAuto) {
//		String oncomplete="jQuery('.myclass .ui-datatable-data tr').last().find('span.ui-icon-pencil').each(function(){jQuery(this).click()});";
//		if(insertAuto) {
//			rc.setOncomplete(oncomplete);
//			System.out.println("Ajoutera une nouvelle ligne automatiquement");
//		} else {
//			rc.setOncomplete(null);
//			System.out.println("N'ajoutera pas de nouvelle ligne automatiquement");
//		}
		this.insertAuto = insertAuto;
	}

	public TabViewsBean getTabViews() {
		return tabViews;
	}

	public void setTabViews(TabViewsBean tabViews) {
		this.tabViews = tabViews;
	}

	public Document getMasterEntity() {
		return masterEntity;
	}

	public void setMasterEntity(Document masterEntity) {
		this.masterEntity = masterEntity;
	}

	public TaTiersDTO getTaTiersDTO() {
		return taTiersDTO;
	}

	public void setTaTiersDTO(TaTiersDTO taTiersDTO) {
		this.taTiersDTO = taTiersDTO;
	}
	
//	public abstract void initInfosDocument();
//	{
//		if(taInfosFacture==null) {
//			taInfosFacture = new InfosDocument();
//		}
//		if(masterEntity!=null) {
//			taInfosFacture.setTaDocument(masterEntity);
//			masterEntity.setTaInfosDocument(taInfosFacture);
//		}
//	}
	
	public void initInfosDocument() {
		if(taInfosDocument==null) {
			taInfosDocument = newInfosDocument();
		}
		if(masterEntity!=null) {
			taInfosDocument.setTaDocumentGeneral(masterEntity);
			masterEntity.setTaInfosDocument(taInfosDocument);
		}
	}
	
	public abstract void initialisationDesInfosComplementaires(Boolean recharger,String typeARecharger);
	
	public void initialisationDesInfosTiers(Boolean recharger){
		IdentiteTiersDTO ihmInfosTiers=new IdentiteTiersDTO();
		if(masterEntity.getTaTiers()!=null) {
			if(taInfosDocument!=null && !recharger)
				new LgrDozerMapper<InfosDocument, IdentiteTiersDTO>().map(taInfosDocument, ihmInfosTiers);					
			else
			{
				new LgrDozerMapper<TaTiers, IdentiteTiersDTO>().map(masterEntity.getTaTiers(), ihmInfosTiers);				
			}
			//rajout des infos non contenues dans taInfosDocument
			if(masterEntity.getTaTiers().getTaTelephone()!=null)
				ihmInfosTiers.setNumeroTelephone(masterEntity.getTaTiers().getTaTelephone().getNumeroTelephone());
			if(masterEntity.getTaTiers().getTaEmail()!=null)
				ihmInfosTiers.setAdresseEmail(masterEntity.getTaTiers().getTaEmail().getAdresseEmail());
			if(masterEntity.getTaTiers().getTaWeb()!=null)
				ihmInfosTiers.setAdresseWeb(masterEntity.getTaTiers().getTaWeb().getAdresseWeb());
			if(masterEntity.getTaTiers().getTaCompl()!=null)
				ihmInfosTiers.setSiretCompl(masterEntity.getTaTiers().getTaCompl().getSiretCompl());
			if(masterEntity.getTaTiers().getTaCommentaire()!=null)
				ihmInfosTiers.setCommentaire(masterEntity.getTaTiers().getTaCommentaire().getCommentaire());

		}
		if (ihmInfosTiers!=null)
			((IdentiteTiersDTO) selectedInfosTiers).setIdentiteTiersDTO(ihmInfosTiers);
		else
			((IdentiteTiersDTO) selectedInfosTiers).setIdentiteTiersDTO(new IdentiteTiersDTO());
		if(taInfosDocument!=null)
			new LgrDozerMapper<IdentiteTiersDTO,InfosDocument >().map( ihmInfosTiers,taInfosDocument);
		
		if(ihmInfosTiers!=null&& ihmInfosTiers.getNomTiers()!=null && selectedDocumentDTO!=null && !ihmInfosTiers.getNomTiers().equals(((DocumentDTO)selectedDocumentDTO).getNomTiers())){
			if(((DocumentDTO)selectedDocumentDTO).getLibelleDocument()!=null && ((DocumentDTO)selectedDocumentDTO).getLibelleDocument().contains("Facture N°"))
				((DocumentDTO)selectedDocumentDTO).setLibelleDocument("Facture N°"+masterEntity.getCodeDocument()+" - "+ihmInfosTiers.getNomTiers());
			masterEntity.setLibelleDocument(((DocumentDTO)selectedDocumentDTO).getLibelleDocument());
			((DocumentDTO)selectedDocumentDTO).setNomTiers(ihmInfosTiers.getNomTiers());
		}
	}
	
	public void initialisationDesAdrFact(Boolean recharger){
		try {
			LinkedList<AdresseInfosFacturationDTO> liste = new LinkedList<AdresseInfosFacturationDTO>();
	
			boolean leTiersADesAdresseFact = false;
			if(masterEntity.getTaTiers()!=null){
				if(typeAdresseFacturation!=null && taTAdrService.findByCode(typeAdresseFacturation)!=null) { //le type d'adresse existe bien
					leTiersADesAdresseFact = masterEntity.getTaTiers().aDesAdressesDuType(typeAdresseFacturation); //le tiers a des adresse de ce type
				}	
			
			if(leTiersADesAdresseFact) { 
				//ajout des adresse de facturation au modele
				for (TaAdresse taAdresse : masterEntity.getTaTiers().getTaAdresses()) {
					if(taAdresse.getTaTAdr()!=null && taAdresse.getTaTAdr().getCodeTAdr().equals(typeAdresseFacturation)){
						liste.add(mapperModelToUIAdresseInfosDocument.map(taAdresse, classModelAdresseFact));
					}
				}
			}else{
				
			}
			
			//ajout des autres types d'adresse
			for (TaAdresse taAdresse : masterEntity.getTaTiers().getTaAdresses()) {
				liste.add(mapperModelToUIAdresseInfosDocument.map(taAdresse, classModelAdresseFact));
			}
			if(liste.isEmpty()) 
				liste.add(mapperModelToUIAdresseInfosDocument.map(new TaAdresse(), classModelAdresseFact));
			}
			//ajout de l'adresse de livraison inscrite dans l'infos facture
			if(taInfosDocument!=null) {
				if(recharger )
					liste.add(mapperModelToUIInfosDocVersAdresseFact.map(taInfosDocument, classModelAdresseFact));
				else
					liste.addFirst(mapperModelToUIInfosDocVersAdresseFact.map(taInfosDocument, classModelAdresseFact));
			}
			
			if (!liste.isEmpty())
				selectedAdresseFact.setIHMAdresse(liste.getFirst());
			else
				selectedAdresseFact.setIHMAdresse(new AdresseInfosFacturationDTO());
			if(selectedAdresseFact!=null && selectedAdresseFact.getAdresse1Adresse()!=null)
				taAdresseFact=selectedAdresseFact;
			System.out.println("FactureController.initialisationDesAdrFact() ** "+selectedAdresseFact.getAdresse1Adresse());
		
		}catch (FinderException e) {
			e.printStackTrace();
		}
				
	}

	public void affecteAdrFactAutoComplete(){
		if(taAdresseFact!=null)
			selectedAdresseFact=taAdresseFact;
	}

	public void affecteAdrLivAutoComplete(){
		if(taAdresseLiv!=null)
			selectedAdresseLiv=taAdresseLiv;
	}
	
	public void initialisationDesAdrLiv(Boolean recharger){
		try {
		
			LinkedList<AdresseInfosLivraisonDTO> liste = new LinkedList<AdresseInfosLivraisonDTO>();

		boolean leTiersADesAdresseLiv = false;
		if(masterEntity.getTaTiers()!=null){
			if(typeAdresseLivraison!=null && taTAdrService.findByCode(typeAdresseLivraison)!=null) { //le type d'adresse existe bien
				leTiersADesAdresseLiv = masterEntity.getTaTiers().aDesAdressesDuType(typeAdresseLivraison); //le tiers a des adresse de ce type
			}
		
		
		if(leTiersADesAdresseLiv) { 
			//ajout des adresse de livraison au modele
			for (TaAdresse taAdresse : masterEntity.getTaTiers().getTaAdresses()) {
				if(taAdresse.getTaTAdr()!=null && taAdresse.getTaTAdr().getCodeTAdr().equals(typeAdresseLivraison)){
					liste.add(mapperModelToUIAdresseLivInfosDocument.map(taAdresse, classModelAdresseLiv));
				}
			}
		}
		//ajout des autres types d'adresse
		for (TaAdresse taAdresse : masterEntity.getTaTiers().getTaAdresses()) {
			if(leTiersADesAdresseLiv && taAdresse.getTaTAdr()!=null && !taAdresse.getTaTAdr().getCodeTAdr().equals(typeAdresseLivraison)){
				liste.add(mapperModelToUIAdresseLivInfosDocument.map(taAdresse, classModelAdresseLiv));
			} else {
				liste.add(mapperModelToUIAdresseLivInfosDocument.map(taAdresse, classModelAdresseLiv));
			}
		}
		}
		//ajout de l'adresse de livraison inscrite dans l'infos facture
		if(taInfosDocument!=null) {
			
			if(recharger )
				liste.add(mapperModelToUIInfosDocVersAdresseLiv.map(taInfosDocument, classModelAdresseLiv));
			else
				liste.addFirst(mapperModelToUIInfosDocVersAdresseLiv.map(taInfosDocument, classModelAdresseLiv));
		}		
		if (!liste.isEmpty())
			((AdresseInfosLivraisonDTO) selectedAdresseLiv).setIHMAdresse(liste.getFirst());
		else
			((AdresseInfosLivraisonDTO) selectedAdresseLiv).setIHMAdresse(new AdresseInfosLivraisonDTO());
		if(selectedAdresseLiv!=null && selectedAdresseLiv.getAdresse1AdresseLiv()!=null)
			taAdresseLiv=selectedAdresseLiv;
		}catch (FinderException e) {
			e.printStackTrace();
		}
				
	}
	
	public abstract void initialisationDesCPaiement(Boolean recharger);

	public AdresseInfosFacturationDTO getSelectedAdresseFact() {
		return selectedAdresseFact;
	}

	public void setSelectedAdresseFact(
			AdresseInfosFacturationDTO selectedAdresseFact) {
		this.selectedAdresseFact = selectedAdresseFact;
	}

	public AdresseInfosLivraisonDTO getSelectedAdresseLiv() {
		return selectedAdresseLiv;
	}

	public void setSelectedAdresseLiv(AdresseInfosLivraisonDTO selectedAdresseLiv) {
		this.selectedAdresseLiv = selectedAdresseLiv;
	}

	public TaCPaiementDTO getSelectedCPaiement() {
		return selectedCPaiement;
	}

	public void setSelectedCPaiement(TaCPaiementDTO selectedCPaiement) {
		this.selectedCPaiement = selectedCPaiement;
	}

	public void calculDateEcheance() {
		calculDateEcheance(false);
	}
	
	public abstract void calculDateEcheance(Boolean appliquer);
//	{
//		if(selectedDocumentDTO!=null){
//		if(((DocumentDTO)selectedDocumentDTO).getId()==0|| appliquer) {
//
//			Integer report = null;
//			Integer finDeMois = null;
//			if(((TaCPaiementDTO)selectedCPaiement)!=null) { 
//				if(((TaCPaiementDTO)selectedCPaiement).getReportCPaiement()!=null)
//					report = ((TaCPaiementDTO)selectedCPaiement).getReportCPaiement();
//				if(((TaCPaiementDTO)selectedCPaiement).getFinMoisCPaiement()!=null)
//					finDeMois = ((TaCPaiementDTO)selectedCPaiement).getFinMoisCPaiement();
//			}
//			masterEntity.setDateDocument(selectedDocumentDTO.getDateDocument());
//			((DocumentDTO)selectedDocumentDTO).setDateEchDocument(taDocumentService.calculDateEcheance(masterEntity,report, finDeMois,taTPaiement));
//		}
//		}
//	}
	
	protected boolean changementTiers() {
		return changementTiers(false);
	}
	
	protected void initLocalisationTVA() {
        if(taTTvaDoc!=null && taTTvaDoc.getCodeTTvaDoc()!=null){
            if(!taTTvaDoc.getCodeTTvaDoc().equals("F")
                    //&& !taTTvaDoc.getCodeTTvaDoc().equals("FRANCHISE")
                    ) {
                masterEntity.setGestionTVA(false);
            } else {
            	masterEntity.setGestionTVA(true);
            }
        }else {
        	masterEntity.setGestionTVA(true);
        }
    }
    
	
	protected boolean changementTiers(boolean refresh) {
		boolean change=refresh;
		boolean res = true;
		try {						
//			initEtatComposant();
//			masterEntity.changementDeTiers();

			if(!disableTtc() && masterEntity.getTaTiers()!=null){//){
				TaTTvaDoc t = null ;
				if(masterEntity.getTaTiers().getTaTTvaDoc()!=null)
					taTTvaDoc=taTTvaDocService.findByCode(masterEntity.getTaTiers().getTaTTvaDoc().getCodeTTvaDoc());
				if(taTTvaDoc!=null)selectedDocumentDTO.setCodeTTvaDoc(taTTvaDoc.getCodeTTvaDoc());
				initLocalisationTVA();
				//forcer la revalidation de la saisie ttc en cas de document sans tva obligation saisie HT !!!
				validateUIField(Const.C_CODE_T_TVA_DOC, selectedDocumentDTO.getCodeTTvaDoc());
			}
			
			Date date = new Date();
			TaInfoEntreprise taInfoEntreprise = taInfoEntrepriseService.findInstance();
			// si date inférieur à dateDeb dossier
			if (LibDate.compareTo(date, taInfoEntreprise.getDatedebInfoEntreprise()) == -1) {
				selectedDocumentDTO.setDateDocument(taInfoEntreprise.getDatedebInfoEntreprise());
				selectedDocumentDTO.setDateLivDocument(taInfoEntreprise.getDatedebInfoEntreprise());
			} else
				// si date supérieur à dateFin dossier
				if (LibDate.compareTo(date, taInfoEntreprise.getDatefinInfoEntreprise()) == 1) {
					 selectedDocumentDTO.setDateDocument(taInfoEntreprise.getDatefinInfoEntreprise());
					 selectedDocumentDTO.setDateLivDocument(taInfoEntreprise.getDatefinInfoEntreprise());
				} else {
					selectedDocumentDTO.setDateDocument(new Date());
					selectedDocumentDTO.setDateLivDocument(new Date());
				}
			
			
			/*
			 * verifier que le type d'adresse existe
			 * verifier que le tiers à des adresses de ce tpe
			 * remplir les maps et changer les clauses where des DAO de modeles
			 * 
			 * remplir les modèles 
			 * avoir dans l'ordre :
			 * - adresse de l'infos facture si elle existe
			 * - adresse du type demandé s'il y en a
			 * - le reste des adresses du tiers
			 */
			calculTypePaiement(change);
			initialisationDesInfosComplementaires(change,"");
			calculDateEcheance();
			listeAdresseInfosFacturationDTO=adresseFacAutoCompleteDTO();
			listeAdresseInfosLivraisonDTO=adresseLivAutoCompleteDTO();
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			res = false;
		} finally {
			return res;
		}
	}
	
	public void controleDate(SelectEvent event) {
		String nomChamp =  (String)event.getComponent().getAttributes().get("nomChamp");
		validateUIField(nomChamp, event.getObject());
		
	}
	public void controleDate(AjaxBehaviorEvent event) {
		String nomChamp =  (String)event.getComponent().getAttributes().get("nomChamp");
		validateUIField(nomChamp,  ((UIOutput) event.getSource()).getValue());
	}

	public Date dateDansPeriode(Date newValue,String champ) throws Exception{
		if(champ.equals(Const.C_DATE_DOCUMENT)){
			newValue=taInfoEntrepriseService.dateDansExercice(newValue);
			if(newValue!=null){
				selectedDocumentDTO.setDateDocument(newValue);
				calculDateEcheance(true);
//				if((selectedDocumentDTO.getDateEchDocument()!=null && 
//						selectedDocumentDTO.getDateEchDocument().before(newValue))||
//						selectedDocumentDTO.getDateEchDocument()==null ){
//					taDocument.setDateEchDocument(newValue);
//					selectedDocumentDTO.setDateEchDocument(newValue);
//					}
				if((selectedDocumentDTO.getDateLivDocument()!=null && 
						selectedDocumentDTO.getDateLivDocument().before(newValue))||
					selectedDocumentDTO.getDateLivDocument()==null ){
					masterEntity.setDateLivDocument(newValue);
					selectedDocumentDTO.setDateLivDocument(newValue);
					}
			}
		}
		if(champ.equals(Const.C_DATE_ECH_DOCUMENT)	){
			if(newValue!=null){
				if(masterEntity.getDateDocument()!=null && 
						masterEntity.getDateDocument().after(newValue))
					newValue=masterEntity.getDateDocument();
			}
		}
		return newValue;
	}
	public void initialisationDesInfosComplementaires(){
		initialisationDesInfosComplementaires(false,"");
	}

	public TaTPaiement getTaTPaiement() {
		return taTPaiement;
	}

	public void setTaTPaiement(TaTPaiement taTPaiement) {
		this.taTPaiement = taTPaiement;
	}

	public TaTTvaDoc getTaTTvaDoc() {
		initLocalisationTVA();//pour être sûr que gestion tva est bien initialisé surtour lors d'une modif document avec rajout de ligne
		return taTTvaDoc;		
	}

	public void setTaTTvaDoc(TaTTvaDoc taTTvaDoc) {
		this.taTTvaDoc = taTTvaDoc;
	}
	
	public void actReinitAdrFact() throws Exception {
		initialisationDesInfosComplementaires(true,Const.RECHARGE_ADR_FACT);
		actModifier();
	}
	
	public void actReinitAdrLiv() throws Exception {
		initialisationDesInfosComplementaires(true,Const.RECHARGE_ADR_LIV);
		actModifier();
	}
	
//	public void actReinitCPaiement() throws Exception {
//		initialisationDesInfosComplementaires(true,Const.RECHARGE_C_PAIEMENT);
//		calculDateEcheance(true);
//		actModifier();
//	}
	
	public abstract void actAppliquerCPaiement() throws Exception;
	
	public void actReinitInfosTiers() throws Exception {
		initialisationDesInfosComplementaires(true,Const.RECHARGE_INFOS_TIERS);
		actModifier();
	}
	
	public void modifMode(EnumModeObjet mode){
		if (!VerrouInterface.isVerrouille() ){
			try {
				if(!modeEcran.dataSetEnModif()) {
					if(mode.equals(EnumModeObjet.C_MO_EDITION)) {
						actModifier();
					} 
					if(mode.equals(EnumModeObjet.C_MO_INSERTION)) {
						actInserer(null);
					} 					
				}
			} catch (Exception e1) {
				e1.printStackTrace();  
			}
		}
	}
	
	public boolean disableTtcSiDocSansTVA(){
		return selectedDocumentDTO.getCodeTTvaDoc().equals("HUE")||
						selectedDocumentDTO.getCodeTTvaDoc().equals("UE")||selectedDocumentDTO.getCodeTTvaDoc().equals("N") ;
	}
	
	public boolean disableTtc(){
		if(masterEntity!=null && masterEntity.getLignesGeneral()!=null)
			return masterEntity.getLignesGeneral().size()>0;
		return false;
	}

	public String getStepEntete() {
		return stepEntete;
	}

	public String getStepLignes() {
		return stepLignes;
	}

	public String getStepTotaux() {
		return stepTotaux;
	}

	public String getStepValidation() {
		return stepValidation;
	}
	
//	public boolean isGestionLot() {
//		return gestionLot;
//	}
//
//	public void setGestionLot(boolean gestionLot) {
//		this.gestionLot = gestionLot;
//	}
	
	public List<ArticleLotEntrepotDTO> lotAutoComplete(String query) {
		
		return onChangeListArticleMP(selectedLigneJSF.getTaArticle().getCodeArticle(),query);
	}
	
	// copier de Fabricationcontroller ==> à homogénéiser si au final les 2 méthodes reste identique (voir aussi webservice rest des articles)
	public List<ArticleLotEntrepotDTO> onChangeListArticleMP(String codeArticle, String numlot) {
		try {
			
			TaPreferences p = taPreferencesService.findByGoupeAndCle(ITaPreferencesServiceRemote.GROUPE_PREFERENCES_LOTS, ITaPreferencesServiceRemote.LOTS_AUTORISER_UTILISATION_LOTS_NON_CONFORME);
			Boolean utiliserLotNonConforme = null;
			if(p!=null && LibConversion.StringToBoolean(p.getValeur()) != null) {
				utiliserLotNonConforme = LibConversion.StringToBoolean(p.getValeur());
			}
			
			listArticleLotEntrepot = new ArrayList<ArticleLotEntrepotDTO>();
			//listArticleLotEntrepot = mouvementService.getEtatStockByArticle((article.split("~"))[0]);
			listArticleLotEntrepot = taMouvementStockService.getEtatStockByArticle(codeArticle,utiliserLotNonConforme);
			List<ArticleLotEntrepotDTO> temp= new ArrayList<ArticleLotEntrepotDTO>();
			List<BigDecimal> qte = new ArrayList<BigDecimal>();
//			for(ArticleLotEntrepotDTO dto: listArticleLotEntrepot){ //liste des lots en stocks par articles
//	
//				for(Ligne mp : masterEntity.getLignes()){ //liste des MP de la fabrication courante
//					TaMouvementStock mouv = mp.getTaMouvementStock();
//	
//					//TaLot lot =lotService.findByCode(mouv.getNumLot());
//					//mouv.setTaLot(lot);
//					
//					//meme emplacement, meme entrepot, meme article, meme lot, meme Unite 1
//					if(mouv.getEmplacement()!=null && mouv.getEmplacement().equals(dto.getEmplacement()) 
//							&& mouv.getTaEntrepot()!=null && mouv.getTaEntrepot().getCodeEntrepot()!=null && mouv.getTaEntrepot().getCodeEntrepot().equals(dto.getCodeEntrepot()) 
//							&& mouv.getTaLot().getTaArticle().getCodeArticle().equals(codeArticle)  
//							&& ((Integer)mouv.getTaLot().getIdLot()).equals(dto.getIdLot())
////							&& (mouv.getNumLot()).equals(dto.getNumLot())
//							&& dto.getCodeUnite()!=null 
//							&& (mouv.getUn1Stock()).equals(dto.getCodeUnite())
//						){
//						temp.add(dto);
//						qte.add(mouv.getQte1Stock()) ;
//					}
//				}
//			}
	
//			int i = 0;
//			for(ArticleLotEntrepotDTO dtoTemp: temp){
//				//soustraction des quantités qui serait déjà utilisé dans d'autre lignes de MP de cette fabrication
//				listArticleLotEntrepot.remove(dtoTemp);
//				dtoTemp.setQte1(dtoTemp.getQte1().subtract(qte.get(i)));
//				i++;
//				listArticleLotEntrepot.add(dtoTemp);
//			}
			
			List<ArticleLotEntrepotDTO> filteredValues = new ArrayList<ArticleLotEntrepotDTO>();

			for (int j = 0; j < listArticleLotEntrepot.size(); j++) {
				//filtre en fonction de la saisie
				ArticleLotEntrepotDTO ale = listArticleLotEntrepot.get(j);
				if(numlot.equals("*") || ale.getNumLot().toLowerCase().contains(numlot.toLowerCase())) {
					filteredValues.add(ale);
				}
			}
			listArticleLotEntrepot = filteredValues;
			return listArticleLotEntrepot;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listArticleLotEntrepot;
	}

	public List<ArticleLotEntrepotDTO> getListArticleLotEntrepot() {
		return listArticleLotEntrepot;
	}

	public void setListArticleLotEntrepot(
			List<ArticleLotEntrepotDTO> listArticleLotEntrepot) {
		this.listArticleLotEntrepot = listArticleLotEntrepot;
	}

	public TaReglement getTaReglement() {
		return taReglement;
	}

	public void setTaReglement(TaReglement taReglement) {
		this.taReglement = taReglement;
	}


	public abstract void calculTypePaiement(boolean recharger);

	public boolean isFactureReglee() {
		return factureReglee;
	}

	public void setFactureReglee(boolean factureReglee) {
		this.factureReglee = factureReglee;
	}

	public String getLibellePaiement() {
		return libellePaiement;
	}

	public void setLibellePaiement(String libellePaiement) {
		this.libellePaiement = libellePaiement;
	}

	public TaCPaiement getTaCPaiementDoc() {
		return taCPaiementDoc;
	}

	public void setTaCPaiementDoc(TaCPaiement taCPaiementDoc) {
		this.taCPaiementDoc = taCPaiementDoc;
	}

	
	public List<TaCPaiement> typeCPaiementAutoComplete(String query) {
		List<TaCPaiement> allValues = taCPaiementService.selectAll();
		List<TaCPaiement> filteredValues = new ArrayList<TaCPaiement>();
		TaCPaiement cp = new TaCPaiement();
		cp.setCodeCPaiement(Const.C_AUCUN);
		filteredValues.add(cp);
		for (int i = 0; i < allValues.size(); i++) {
			cp = allValues.get(i);
			if(query.equals("*") || cp.getCodeCPaiement().toLowerCase().contains(query.toLowerCase())) {
				filteredValues.add(cp);
			}
		}

		return filteredValues;
	}

	public LigneJSF getDetailLigneOverLayPanel() {
		return detailLigneOverLayPanel;
	}

	public void setDetailLigneOverLayPanel(LigneJSF detailLigneOverLayPanel) {
		this.detailLigneOverLayPanel = detailLigneOverLayPanel;
	}

	public boolean isDifferenceReglementResteARegle() {
		return differenceReglementResteARegle;
	}

	public void setDifferenceReglementResteARegle(
			boolean differenceReglementResteARegle) {
		this.differenceReglementResteARegle = differenceReglementResteARegle;
	}

	public boolean isImpressionDirect() {
		return impressionDirect;
	}

	public void setImpressionDirect(boolean impressionDirect) {
		this.impressionDirect = impressionDirect;
	}

	public org.primefaces.component.wizard.Wizard getWizardDocument() {
		return wizardDocument;
	}

	public void setWizardDocument(
			org.primefaces.component.wizard.Wizard wizardFacture) {
		this.wizardDocument = wizardFacture;
	}

	public String getCurrentStepId() {
		return currentStepId;
	}

	public void setCurrentStepId(String currentStepId) {
		this.currentStepId = currentStepId;
	}

	public IGenericCRUDServiceDocumentRemote<Document, DocumentDTO> getTaDocumentService() {
		return taDocumentService;
	}

	public void setTaDocumentService(IGenericCRUDServiceDocumentRemote<Document, DocumentDTO> taDocumentService) {
		this.taDocumentService = taDocumentService;
	}

	public IGenericCRUDServiceRemote getTaInfosDocumentService() {
		return taInfosDocumentService;
	}

	public void setTaInfosDocumentService(IGenericCRUDServiceRemote taInfosDocumentService) {
		this.taInfosDocumentService = taInfosDocumentService;
	}

	public IGenericCRUDServiceRemote<Ligne, LigneDTO> getTaLDocumentService() {
		return taLDocumentService;
	}

	public void setTaLDocumentService(IGenericCRUDServiceRemote<Ligne, LigneDTO> taLDocumentService) {
		this.taLDocumentService = taLDocumentService;
	}

	public String getWvEcran() {
		return wvEcran;
	}

	public void setWvEcran(String wvEcran) {
		this.wvEcran = wvEcran;
	}

	public IDocumentDTO rempliDTOOuvertureDocumentExterne(){
		if(masterEntity!=null){			
			try {
				ouvertureDocumentIndirect=true;
				refresh();
				selectedDocumentDTO=rechercheDansListeValues(masterEntity.getCodeDocument());
//				masterEntity=taDocumentService.findByCodeFetch(masterEntity.getCodeDocument());
//				mapperModelToUI.map(masterEntity, selectedDocumentDTO);
				selectedDocumentDTOs=null ;
//			} catch (FinderException e) {
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return selectedDocumentDTO;
	}

	public String getCss() {
		return css;
	}

	public void setCss(String css) {
		this.css = css;
	}

	public void actDialogGenerationDocument(ActionEvent actionEvent) {

		Map<String,Object> options = new HashMap<String, Object>();
		options.put("modal", true);
		options.put("draggable", true);
		options.put("resizable", false);
		options.put("contentHeight", 600);
		options.put("modal", true);

		Map<String,List<String>> params = new HashMap<String,List<String>>();
		List<String> list = new ArrayList<String>();
		list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
		params.put("modeEcranDefaut", list);
		
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		Map<String,ParamAfficheChoixGenerationDocument> mapEdition = new HashMap<String,ParamAfficheChoixGenerationDocument>();
		ParamAfficheChoixGenerationDocument paramGeneration=new ParamAfficheChoixGenerationDocument();
		paramGeneration.setCodeDocument(masterEntity.getCodeDocument());
		paramGeneration.setTypeSrc(masterEntity.getTypeDocument());
		paramGeneration.setDocumentSrc(masterEntity);
//		paramGeneration.setDateDocument(masterEntity.getDateDocument());
		paramGeneration.setCodeTiers(masterEntity.getTaTiers().getCodeTiers());
		paramGeneration.setGenerationModele(false);
		paramGeneration.setTitreFormulaire("Génération d'un document à partir du document : "+masterEntity.getCodeDocument());
		sessionMap.put("generation", paramGeneration);
		
		PrimeFaces.current().dialog().openDynamic("generation/generation_documents_simple", options, params);
		
	}

	public void actDialogGenerationModeleDocument(ActionEvent actionEvent) {

		Map<String,Object> options = new HashMap<String, Object>();
		options.put("modal", true);
		options.put("draggable", true);
		options.put("resizable", false);
		options.put("contentHeight", 700);
		options.put("contentWidth", 800);
		options.put("modal", true);

		Map<String,List<String>> params = new HashMap<String,List<String>>();
		List<String> list = new ArrayList<String>();
		list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
		params.put("modeEcranDefaut", list);
		
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		ParamAfficheChoixGenerationDocument paramGeneration=new ParamAfficheChoixGenerationDocument();
		paramGeneration.setCodeDocument(masterEntity.getCodeDocument());
		paramGeneration.setTypeSrc(masterEntity.getTypeDocument());
		paramGeneration.setDocumentSrc(masterEntity);
//		paramGeneration.setDateDocument(masterEntity.getDateDocument());
		paramGeneration.setCodeTiers(masterEntity.getTaTiers().getCodeTiers());
		paramGeneration.setGenerationModele(true);
		paramGeneration.setTitreFormulaire("Génération d'un modèle à partir du document : "+masterEntity.getCodeDocument());
		sessionMap.put("generation", paramGeneration);
		
		PrimeFaces.current().dialog().openDynamic("generation/generation_documents_simple", options, params);
		
	}
	public void handleReturnDialogGenerationDocument(SelectEvent event) {
		autorisationLiaisonComplete();
		css = null;
		IDocumentTiers docCree = null; 
		LgrTabEvent retourEvent;
		FacesContext context = FacesContext.getCurrentInstance();
		if(event!=null && event.getObject()!=null) {
			if(event.getObject() instanceof LgrTabEvent){
				retourEvent=(LgrTabEvent) event.getObject();
				css =retourEvent.getCssLgrTab();
				docCree=(IDocumentTiers) retourEvent.getData();
				
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Création document", "Le document '"+docCree.getCodeDocument()+"' à été généré correctement."));
			}else if(event.getObject() instanceof RetourGenerationDoc){
				RetourGenerationDoc retour=(RetourGenerationDoc) event.getObject();
				if(retour.getMessage()!=null && !retour.getMessage().isEmpty())
					context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Création document", retour.getMessage()));
			}
		}
	}
	
	public void actDialogEvenement(ActionEvent actionEvent) {
		    
	//        Map<String,Object> options = new HashMap<String, Object>();
	//        options.put("modal", true);
	//        options.put("draggable", false);
	//        options.put("resizable", false);
	//        options.put("contentHeight", 640);
	//        options.put("modal", true);
	//        
	//        AgendaBean.initAppelDialogue();
	//        
	//        //Map<String,List<String>> params = null;
	//        Map<String,List<String>> params = new HashMap<String,List<String>>();
	//        List<String> list = new ArrayList<String>();
	//        list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
	//        params.put("modeEcranDefaut", list);
	//        
	//        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
	//		Map<String, Object> sessionMap = externalContext.getSessionMap();
	//		
	//		//String numLot =  (String) actionEvent.getComponent().getAttributes().get("idEvenement");
			EvenementParam param = new EvenementParam();
			param.setListeTiers(new ArrayList<>());
			param.getListeTiers().add(masterEntity.getTaTiers());
			param.setListeDocuments(new ArrayList<>());
			param.getListeDocuments().add(masterEntity);
			actDialogEvenement(param);
	//		sessionMap.put(EvenementParam.NOM_OBJET_EN_SESSION, param);
	//
	//        PrimeFaces.current().dialog().openDynamic("agenda/dialog_evenement", options, params);
				    
		}
	
	public List<AdresseInfosFacturationDTO> getListeAdresseInfosFacturationDTO() {
		return listeAdresseInfosFacturationDTO;
	}
	
	public void setListeAdresseInfosFacturationDTO(List<AdresseInfosFacturationDTO> listeAdresseInfosFacturationDTO) {
		this.listeAdresseInfosFacturationDTO = listeAdresseInfosFacturationDTO;
	}
	
	public List<AdresseInfosLivraisonDTO> getListeAdresseInfosLivraisonDTO() {
		return listeAdresseInfosLivraisonDTO;
	}
	
	public void setListeAdresseInfosLivraisonDTO(List<AdresseInfosLivraisonDTO> listeAdresseInfosLivraisonDTO) {
		this.listeAdresseInfosLivraisonDTO = listeAdresseInfosLivraisonDTO;
	}
	
	public IdentiteTiersDTO getSelectedInfosTiers() {
		return selectedInfosTiers;
	}
	
	public void setSelectedInfosTiers(IdentiteTiersDTO selectedInfosTiers) {
		this.selectedInfosTiers = selectedInfosTiers;
	}
	
	public AdresseInfosLivraisonDTO getTaAdresseLiv() {
		return taAdresseLiv;
	}
	
	public void setTaAdresseLiv(AdresseInfosLivraisonDTO taAdresseLiv) {
		this.taAdresseLiv = taAdresseLiv;
	}
	
	public AdresseInfosFacturationDTO getTaAdresseFact() {
		return taAdresseFact;
	}
	
	public void setTaAdresseFact(AdresseInfosFacturationDTO taAdresseFact) {
		this.taAdresseFact = taAdresseFact;
	}
	public boolean isImpressionDirectClient() {
		return impressionDirectClient;
	}
	
	public void setImpressionDirectClient(boolean impressionDirectClient) {
		this.impressionDirectClient = impressionDirectClient;
	}
	
	public boolean isMiseADispositionCompteClient() {
		return miseADispositionCompteClient;
	}
	
	public void setMiseADispositionCompteClient(boolean miseADispositionCompteClient) {
		this.miseADispositionCompteClient = miseADispositionCompteClient;
	}
	public String getLibelleMultipleReglement() {
		return libelleMultipleReglement;
	}
	
	public void setLibelleMultipleReglement(String libelleMultipleReglement) {
		this.libelleMultipleReglement = libelleMultipleReglement;
	}
	
	public boolean isBtnPrecedentVisible() {
		return btnPrecedentVisible;
	}
	
	public void setBtnPrecedentVisible(boolean btnPrecedentVisible) {
		this.btnPrecedentVisible = btnPrecedentVisible;
	}
	
	public boolean isBtnSuivantVisible() {
		return btnSuivantVisible;
	}
	
	public void setBtnSuivantVisible(boolean btnSuivantVisible) {
		this.btnSuivantVisible = btnSuivantVisible;
	}
	
	public boolean isEnvoyeParEmail() {
		return envoyeParEmail;
	}
	
	public void setEnvoyeParEmail(boolean envoyeParEmail) {
		this.envoyeParEmail = envoyeParEmail;
	}
	
	
	public TaReglementDTOJSF remplirIHMReglement(TaReglement reglement,IDocumentTiers taDocument){
		TaReglementDTOJSF ihmReglement = new TaReglementDTOJSF();
		ihmReglement.getDto().setId(reglement.getIdDocument());
		ihmReglement.getDto().setCodeDocument(reglement.getCodeDocument());
		ihmReglement.getDto().setTypeDocument("");
		ihmReglement.getDto().setDateDocument(reglement.getDateDocument());
		ihmReglement.getDto().setDateLivDocument(reglement.getDateLivDocument());
		if(reglement.getTaCompteBanque()!=null){
			ihmReglement.getDto().setIdCompteBanque(reglement.getTaCompteBanque().getIdCompteBanque());
			ihmReglement.getDto().setCodeBanque(reglement.getTaCompteBanque().getCodeBanque());
			ihmReglement.getDto().setCompte(reglement.getTaCompteBanque().getCompte());
			ihmReglement.getDto().setCodeGuichet(reglement.getTaCompteBanque().getCodeGuichet());
			ihmReglement.getDto().setCleRib(reglement.getTaCompteBanque().getCleRib());
		}
		if(reglement.getTaTPaiement()!=null){
			ihmReglement.getDto().setIdTPaiement(reglement.getTaTPaiement().getIdTPaiement());
			ihmReglement.getDto().setCodeTPaiement(reglement.getTaTPaiement().getCodeTPaiement());
			ihmReglement.setTaTPaiement(reglement.getTaTPaiement());
		}
	
		ihmReglement.getDto().setDateExportAffectation(null);
		ihmReglement.getDto().setDateExport(reglement.getDateExport());
		ihmReglement.getDto().setLibelleDocument(reglement.getLibelleDocument());
		if(taDocument!=null && taDocument instanceof TaFacture) {
			ihmReglement.getDto().setAffectation(reglement.calculAffectationEnCoursReel((TaFacture)taDocument));
		}
		if(taDocument!=null && taDocument instanceof TaTicketDeCaisse) {
			ihmReglement.getDto().setAffectation(reglement.calculAffectationEnCoursReel((TaTicketDeCaisse)taDocument));
		}
		ihmReglement.getDto().setNetTtcCalc(reglement.getNetTtcCalc());
		ihmReglement.getDto().setMulti(reglement.getTaRReglements().size()>1);
		ihmReglement.getDto().setResteAAffecter(reglement.getResteAAffecter());
		if(reglement.getTaAcompte()!=null) {
			ihmReglement.getDto().setCodeAcompte(reglement.getTaAcompte().getCodeDocument());
		}
		ihmReglement.setTypeDocument(TaReglement.TYPE_DOC);
		return ihmReglement;		
	
	}
	
	public List<TaReglementDTOJSF> getListeTaRReglementDTOJSF() {
		return listeTaRReglementDTOJSF;
	}
	
	public void setListeTaRReglementDTOJSF(List<TaReglementDTOJSF> listeTaRReglementDTOJSF) {
		this.listeTaRReglementDTOJSF = listeTaRReglementDTOJSF;
	}
	
	public boolean isDocEnregistre() {
		return docEnregistre;
	}
	
	public void setDocEnregistre(boolean docEnregistre) {
		this.docEnregistre = docEnregistre;
	}
	
	public boolean isChoixEdition() {
		return choixEdition;
	}
	
	public void setChoixEdition(boolean choixEdition) {
		this.choixEdition = choixEdition;
	}
	
	public DuplicationDocumentBean getDuplicationDocumentBean() {
		return duplicationDocumentBean;
	}
	
	public void setDuplicationDocumentBean(DuplicationDocumentBean duplicationDocumentBean) {
		this.duplicationDocumentBean = duplicationDocumentBean;
	}
	
	public String getLibelleReglementEnAttente() {
		return libelleReglementEnAttente;
	}
	
	public void setLibelleReglementEnAttente(String libelleReglementEnAttente) {
		this.libelleReglementEnAttente = libelleReglementEnAttente;
	}
	
	public void actDialogEmail(ActionEvent actionEvent) {		
		EmailParam email = new EmailParam();
		email.setEmailExpediteur(null);
		email.setNomExpediteur(taInfoEntrepriseService.findInstance().getNomInfoEntreprise()); 
		email.setSubject(taInfoEntrepriseService.findInstance().getNomInfoEntreprise()+" "+nomDocument+" "+masterEntity.getCodeDocument()); 
		email.setBodyPlain("Votre "+nomDocument+" "+ masterEntity.getCodeDocument());
		email.setBodyHtml("Votre "+nomDocument+" "+ masterEntity.getCodeDocument());
		email.setDestinataires(new String[]{masterEntity.getTaTiers().getTaEmail().getAdresseEmail()});
		email.setEmailDestinataires(new TaEmail[]{masterEntity.getTaTiers().getTaEmail()});
		
		EmailPieceJointeParam pj1 = new EmailPieceJointeParam();
		pj1.setFichier(new File((taDocumentService).generePDF(masterEntity.getIdDocument(),null, null, null)));
		pj1.setTypeMime("pdf");
		pj1.setNomFichier(pj1.getFichier().getName());
		email.setPiecesJointes(
				new EmailPieceJointeParam[]{pj1}
				);
		actDialogEmail(email);
	}
	
	public void actDialogPush(ActionEvent actionEvent) {		
		PushParam push = new PushParam();
//		push.setEmailExpediteur(null);
//		push.setNomExpediteur(taInfoEntrepriseService.findInstance().getNomInfoEntreprise()); 
//		push.setSubject(taInfoEntrepriseService.findInstance().getNomInfoEntreprise()+" "+nomDocument+" "+masterEntity.getCodeDocument()); 
//		push.setBodyPlain("Votre "+nomDocument+" "+ masterEntity.getCodeDocument());
//		push.setBodyHtml("Votre "+nomDocument+" "+ masterEntity.getCodeDocument());
//		push.setDestinataires(new String[]{masterEntity.getTaTiers().getTaEmail().getAdresseEmail()});
		
		List<TaTiers> l = new ArrayList<>();
		l.add(masterEntity.getTaTiers());
		push.setDestinataires(l);
		
//		push.setEmailDestinataires(new TaEmail[]{masterEntity.getTaTiers().getTaEmail()});
		
//		EmailPieceJointeParam pj1 = new EmailPieceJointeParam();
//		pj1.setFichier(new File((taDocumentService).generePDF(masterEntity.getIdDocument(),null, null, null)));
//		pj1.setTypeMime("pdf");
//		pj1.setNomFichier(pj1.getFichier().getName());
//		push.setPiecesJointes(
//				new EmailPieceJointeParam[]{pj1}
//				);
		actDialogPush(push);
	}
	


	
	public void onClearEtat(AjaxBehaviorEvent event){
		selectedEtatDocument=null;
		selectedDocumentDTO.setCodeEtat(null);
		masterEntity.setTaREtat(null);
	}
	
	
	
	
	public Verrouillage documentDTOEstVerrouille() {
		return documentDTOEstVerrouille(selectedDocumentDTO);
	}
	public Verrouillage documentDTOEstVerrouille(DocumentDTO courant) {
		if(courant!=null ) {
			return Verrouillage.determineVerrouillage(courant.getEstMisADisposition(), courant.getDateExport(), courant.getDateVerrouillage());
		}
		return null;
	}
	
	public Verrouillage documentEstVerrouille(Document courant) {
		if(courant!=null) {
			return Verrouillage.determineVerrouillage(courant.getTaMiseADisposition(), courant.getDateExport(), courant.getDateVerrouillage());
		}
		return null;
	}
	
	public Verrouillage documentEstVerrouille() {
		return documentEstVerrouille(masterEntity);	
	}
	
	protected boolean verifSiEstModifiable() {
		return verifSiEstModifiable(masterEntity);
	}
	protected boolean verifSiEstModifiable(Document courant) {
		Verrouillage estVerrouille=documentEstVerrouille(courant);
		if(!estVerrouille.isVerrouille())return true;
		Ajax context = PrimeFaces.current().ajax();
		PrimeFaces.current().ajax().addCallbackParam("verifSiEstModifiable", estVerrouille);
		if(estVerrouille.isVerrouille())
			PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage("Document",estVerrouille.getMessage() ));
		return false;
	}
	
	
	public boolean isEcranSynthese() {
		return ecranSynthese;
	}
	
	public void setEcranSynthese(boolean ecranSynthese) {
		this.ecranSynthese = ecranSynthese;
	}
	
	public String stepCourant() {
		ecranSynthese=modeEcran.getMode().equals(EnumModeObjet.C_MO_CONSULTATION);
		if(ecranSynthese)currentStepId=	stepSynthese;
		else currentStepId=	stepEntete;
		return currentStepId;
	}
	
	public void changementStepWizard(boolean actModifier) {
		if(wizardDocument!=null) {
			if(ecranSynthese && !this.stepSynthese.equals("idSyntheseXXXXXXX"))wizardDocument.setStep(this.stepSynthese);
			else	if(actModifier && (wizardDocument.getStep().equals(this.stepSynthese)))
			{
				wizardDocument.setStep(this.stepEntete);
			}
			currentStepId=wizardDocument.getStep();
		}	
	}
	
	public LocalDate getDateMin() {
		return dateMin;
	}
	
	public LocalDate getDateMax() {
		return dateMax;
	}

	
	
	public String rechercheCommentaireDefautDocument() {
		for (TaPreferences obj : listePreferences) {
			if(obj.getCle().equals("Commentaire")){
				return obj.getValeur();
			}
		}
		return null;
	}
	public boolean isModeScanCodeBarre() {
		return modeScanCodeBarre;
	}
	
	public void setModeScanCodeBarre(boolean modeScanCodeBarre) {
		this.modeScanCodeBarre = modeScanCodeBarre;
	}
	
	public TaTransporteurDTO getTaTransporteurDTO() {
		return taTransporteurDTO;
	}
	
	public void setTaTransporteurDTO(TaTransporteurDTO taTransporteurDTO) {
		this.taTransporteurDTO = taTransporteurDTO;
	}
	
	public TaTransporteur getTaTransporteur() {
		return taTransporteur;
	}
	
	public void setTaTransporteur(TaTransporteur taTransporteur) {
		this.taTransporteur = taTransporteur;
	}
	
	public boolean isFactureARegler() {
		return factureARegler;
	}
	
	public void setFactureARegler(boolean factureARegler) {
		this.factureARegler = factureARegler;
	}
	
	
	
//	public List<TaRDocumentDTO> dejaGenereDocument(IDocumentDTO ds ) {
//	    List<TaRDocumentDTO> dejaGenere = null;
//	    String requeteFixeRDocument = null;
//	    if(ds.getTypeDocument().equals(TaAcompte.TYPE_DOC))requeteFixeRDocument=" from TaRDocument x where x.taAcompte.idDocument="+ds.getId();
//	    if(ds.getTypeDocument().equals(TaAvoir.TYPE_DOC))requeteFixeRDocument=" from TaRDocument x where x.taAvoir.idDocument="+ds.getId();
//	    if(ds.getTypeDocument().equals(TaAvisEcheance.TYPE_DOC))requeteFixeRDocument=" from TaRDocument x where x.taAvisEcheance.idDocument="+ds.getId();
//	    if(ds.getTypeDocument().equals(TaApporteur.TYPE_DOC))requeteFixeRDocument=" from TaRDocument x where x.taApporteur.idDocument="+ds.getId();
//	    if(ds.getTypeDocument().equals(TaBoncde.TYPE_DOC))requeteFixeRDocument=" from TaRDocument x where x.taBoncdeAchat.idDocument="+ds.getId();
//	    if(ds.getTypeDocument().equals(TaBoncdeAchat.TYPE_DOC))requeteFixeRDocument=" from TaRDocument x where x.taBoncde.idDocument="+ds.getId();
//	    if(ds.getTypeDocument().equals(TaBonReception.TYPE_DOC))requeteFixeRDocument=" from TaRDocument x where x.taBonReception.idDocument="+ds.getId();
//	    if(ds.getTypeDocument().equals(TaBonliv.TYPE_DOC))requeteFixeRDocument=" from TaRDocument x where x.taBonliv.idDocument="+ds.getId();
//	    if(ds.getTypeDocument().equals(TaDevis.TYPE_DOC))requeteFixeRDocument=" from TaRDocument x where x.taDevis.idDocument="+ds.getId();
//	    if(ds.getTypeDocument().equals(TaFacture.TYPE_DOC))requeteFixeRDocument=" from TaRDocument x where x.taFacture.idDocument="+ds.getId();
//	    if(ds.getTypeDocument().equals(TaPrelevement.TYPE_DOC))requeteFixeRDocument=" from TaRDocument x where x.taPrelevement.idDocument="+ds.getId();
//	    if(ds.getTypeDocument().equals(TaProforma.TYPE_DOC))requeteFixeRDocument=" from TaRDocument x where x.taProforma.idDocument="+ds.getId();
//	    if(ds.getTypeDocument().equals(TaTicketDeCaisse.TYPE_DOC))requeteFixeRDocument=" from TaRDocument x where x.taTicketDeCaisse.idDocument="+ds.getId();
//	    if(ds.getTypeDocument().equals(TaPreparation.TYPE_DOC))requeteFixeRDocument=" from TaRDocument x where x.taPreparation.idDocument="+ds.getId();
//	    String requeteDoc="";
//	    if(ds!=null && ds.getId()!=0 && requeteFixeRDocument != null) {
//	        String jpql = "select x "+requeteFixeRDocument;
//	        List<TaRDocument> l = taRDocumentService.dejaGenere(jpql);
//	        if(l!=null && l.size()>0)dejaGenere=new LinkedList<>();
//	        if(l== null) {
//	        	l = new LinkedList<>();
//	        }
//	        for (TaRDocument taRDocument : l) {
//	            if(taRDocument.getTaAcompte()!=null && !ds.getTypeDocument().equals(TaAcompte.TYPE_DOC))requeteDoc="select new fr.legrain.document.dto.TaRDocumentDTO(x.taAcompte.idDocument,x.taAcompte.codeDocument,'"+TaAcompte.TYPE_DOC+"') ";
//	            if(taRDocument.getTaAvoir()!=null && !ds.getTypeDocument().equals(TaAvoir.TYPE_DOC))requeteDoc="select new fr.legrain.document.dto.TaRDocumentDTO(x.taAvoir.idDocument,x.taAvoir.codeDocument,'"+TaAvoir.TYPE_DOC+"') ";
//	            if(taRDocument.getTaAvisEcheance()!=null && !ds.getTypeDocument().equals(TaAvisEcheance.TYPE_DOC))requeteDoc="select new fr.legrain.document.dto.TaRDocumentDTO(x.taAvisEcheance.idDocument,x.taAvisEcheance.codeDocument,'"+TaAvisEcheance.TYPE_DOC+"') ";
//	            if(taRDocument.getTaApporteur()!=null && !ds.getTypeDocument().equals(TaApporteur.TYPE_DOC))requeteDoc="select new fr.legrain.document.dto.TaRDocumentDTO(x.taApporteur.idDocument,x.taApporteur.codeDocument,'"+TaApporteur.TYPE_DOC+"') ";
//	            if(taRDocument.getTaBoncde()!=null && !ds.getTypeDocument().equals(TaBoncde.TYPE_DOC))requeteDoc="select new fr.legrain.document.dto.TaRDocumentDTO(x.taBoncde.idDocument,x.taBoncde.codeDocument,'"+TaBoncde.TYPE_DOC+"') ";
//	            if(taRDocument.getTaBoncdeAchat()!=null && !ds.getTypeDocument().equals(TaBoncdeAchat.TYPE_DOC))requeteDoc="select new fr.legrain.document.dto.TaRDocumentDTO(x.taBoncdeAchat.idDocument,x.taBoncdeAchat.codeDocument,'"+TaBoncdeAchat.TYPE_DOC+"') ";
//	            if(taRDocument.getTaBonliv()!=null && !ds.getTypeDocument().equals(TaBonliv.TYPE_DOC))requeteDoc="select new fr.legrain.document.dto.TaRDocumentDTO(x.taBonliv.idDocument,x.taBonliv.codeDocument,'"+TaBonliv.TYPE_DOC+"') ";
//	            if(taRDocument.getTaBonReception()!=null && !ds.getTypeDocument().equals(TaBonReception.TYPE_DOC))requeteDoc="select new fr.legrain.document.dto.TaRDocumentDTO(x.taBonReception.idDocument,x.taBonReception.codeDocument,'"+TaBonReception.TYPE_DOC+"') ";
//	            if(taRDocument.getTaDevis()!=null && !ds.getTypeDocument().equals(TaDevis.TYPE_DOC))requeteDoc="select new fr.legrain.document.dto.TaRDocumentDTO(x.taDevis.idDocument,x.taDevis.codeDocument,'"+TaDevis.TYPE_DOC+"') ";
//	            if(taRDocument.getTaFacture()!=null && !ds.getTypeDocument().equals(TaFacture.TYPE_DOC))requeteDoc="select new fr.legrain.document.dto.TaRDocumentDTO(x.taFacture.idDocument,x.taFacture.codeDocument,'"+TaFacture.TYPE_DOC+"') ";
//	            if(taRDocument.getTaPrelevement()!=null && !ds.getTypeDocument().equals(TaPrelevement.TYPE_DOC))requeteDoc="select new fr.legrain.document.dto.TaRDocumentDTO(x.taPrelevement.idDocument,x.taPrelevement.codeDocument,'"+TaPrelevement.TYPE_DOC+"') ";
//	            if(taRDocument.getTaProforma()!=null && !ds.getTypeDocument().equals(TaProforma.TYPE_DOC))requeteDoc="select new fr.legrain.document.dto.TaRDocumentDTO(x.taProforma.idDocument,x.taProforma.codeDocument,'"+TaProforma.TYPE_DOC+"') ";
//	            if(taRDocument.getTaTicketDeCaisse()!=null && !ds.getTypeDocument().equals(TaTicketDeCaisse.TYPE_DOC))requeteDoc="select new fr.legrain.document.dto.TaRDocumentDTO(x.taTicketDeCaisse.idDocument,x.taTicketDeCaisse.codeDocument,'"+TaTicketDeCaisse.TYPE_DOC+"') ";
//	            if(taRDocument.getTaPreparation()!=null && !ds.getTypeDocument().equals(TaPreparation.TYPE_DOC))requeteDoc="select new fr.legrain.document.dto.TaRDocumentDTO(x.taPreparation.idDocument,x.taPreparation.codeDocument,'"+TaPreparation.TYPE_DOC+"') ";
//	            List<TaRDocumentDTO> l2 = new LinkedList<>();
//	            if(requeteDoc!=null && !requeteDoc.equals(""))
//	                l2 =taRDocumentService.dejaGenereDocument(requeteDoc+requeteFixeRDocument);
//	            for (TaRDocumentDTO iDocumentTiers : l2) {
//	                dejaGenere.add(iDocumentTiers);
//	            }
//	        }
//	        System.err.println(l.size());
//	    }
//	    return dejaGenere;
//	}






//	public List<TaRDocumentDTO> rechercheSiDocLie(IDocumentDTO ds) {
//	    return dejaGenereDocument(ds);
//	}
	public List<TaRDocumentDTO> rechercheSiDocLie() {
	    return dejaGenereDocument(selectedDocumentDTO);
	}
	
	
	
//	public List<IDocumentTiers> dejaGenereDocument(IDocumentDTO ds ) {
//		List<IDocumentTiers> dejaGenere = null;
//		String requeteFixeRDocument = null;
//		if(!ds.getTypeDocument().equals(TaBonReception.TYPE_DOC)) {
//			if(ds.getTypeDocument().equals(TaAcompte.TYPE_DOC))requeteFixeRDocument=" from TaRDocument x where x.taAcompte.idDocument="+ds.getId();
//			if(ds.getTypeDocument().equals(TaAvoir.TYPE_DOC))requeteFixeRDocument=" from TaRDocument x where x.taAvoir.idDocument="+ds.getId();
//			if(ds.getTypeDocument().equals(TaAvisEcheance.TYPE_DOC))requeteFixeRDocument=" from TaRDocument x where x.taAvisEcheance.idDocument="+ds.getId();
//			if(ds.getTypeDocument().equals(TaApporteur.TYPE_DOC))requeteFixeRDocument=" from TaRDocument x where x.taApporteur.idDocument="+ds.getId();
//			if(ds.getTypeDocument().equals(TaBoncde.TYPE_DOC))requeteFixeRDocument=" from TaRDocument x where x.taBoncde.idDocument="+ds.getId();
//			if(ds.getTypeDocument().equals(TaBonliv.TYPE_DOC))requeteFixeRDocument=" from TaRDocument x where x.taBonliv.idDocument="+ds.getId();
//			if(ds.getTypeDocument().equals(TaDevis.TYPE_DOC))requeteFixeRDocument=" from TaRDocument x where x.taDevis.idDocument="+ds.getId();
//			if(ds.getTypeDocument().equals(TaFacture.TYPE_DOC))requeteFixeRDocument=" from TaRDocument x where x.taFacture.idDocument="+ds.getId();
//			if(ds.getTypeDocument().equals(TaPrelevement.TYPE_DOC))requeteFixeRDocument=" from TaRDocument x where x.taPrelevement.idDocument="+ds.getId();
//			if(ds.getTypeDocument().equals(TaProforma.TYPE_DOC))requeteFixeRDocument=" from TaRDocument x where x.taProforma.idDocument="+ds.getId();
//			String requeteDoc="";
//			if(ds!=null && ds.getId()!=0) {
//				String jpql = "select x "+requeteFixeRDocument;
//				List<TaRDocument> l = taRDocumentService.dejaGenere(jpql);
//				if(l!=null && l.size()>0)dejaGenere=new LinkedList<>();
//				for (TaRDocument taRDocument : l) {
//					if(taRDocument.getTaAcompte()!=null && !ds.getTypeDocument().equals(TaAcompte.TYPE_DOC))requeteDoc="select x.taAcompte ";
//					if(taRDocument.getTaAvoir()!=null && !ds.getTypeDocument().equals(TaAvoir.TYPE_DOC))requeteDoc="select x.taAvoir ";
//					if(taRDocument.getTaAvisEcheance()!=null && !ds.getTypeDocument().equals(TaAvisEcheance.TYPE_DOC))requeteDoc="select x.taAvisEcheance ";;
//					if(taRDocument.getTaApporteur()!=null && !ds.getTypeDocument().equals(TaApporteur.TYPE_DOC))requeteDoc="select x.taApporteur ";
//					if(taRDocument.getTaBoncde()!=null && !ds.getTypeDocument().equals(TaBoncde.TYPE_DOC))requeteDoc="select x.taBoncde ";
//					if(taRDocument.getTaBonliv()!=null && !ds.getTypeDocument().equals(TaBonliv.TYPE_DOC))requeteDoc="select x.taBonliv ";
//					if(taRDocument.getTaDevis()!=null && !ds.getTypeDocument().equals(TaDevis.TYPE_DOC))requeteDoc="select x.taDevis ";
//					if(taRDocument.getTaFacture()!=null && !ds.getTypeDocument().equals(TaFacture.TYPE_DOC))requeteDoc="select x.taFacture ";
//					if(taRDocument.getTaPrelevement()!=null && !ds.getTypeDocument().equals(TaPrelevement.TYPE_DOC))requeteDoc="select x.taPrelevement ";
//					if(taRDocument.getTaProforma()!=null && !ds.getTypeDocument().equals(TaProforma.TYPE_DOC))requeteDoc="select x.taProforma ";
//					
//					List<IDocumentTiers> l2 =taRDocumentService.dejaGenereDocument(requeteDoc+requeteFixeRDocument);
//					for (IDocumentTiers iDocumentTiers : l2) {
//						dejaGenere.add(iDocumentTiers);
//					}
//				}
//				System.err.println(l.size());
//			}
//		} else {
//			dejaGenere = new LinkedList<>();
//		}
//		return dejaGenere;
//	}
//	public List<IDocumentTiers> rechercheSiDocLie() {
//		return dejaGenereDocument(selectedDocumentDTO);
//	}
	
	
	
	public RetourAutorisationLiaison autoriseSuppression() {
		return autoriseSuppression(false);
	}
	
//	public boolean autoriseSuppressionMessage() {
//		return autoriseSuppressionMessage(false);
//	}
	
	 public boolean autoriseSuppressionMessage(boolean silencieux) {
		    messageSuppression=Const.C_MESSAGE_SUPPRESSION_DOCUMENT;
		    messageModification=Const.C_MESSAGE_MODIFIFCATION_DOCUMENT;
		    if(selectedDocumentDTO!=null && selectedDocumentDTO.getCodeDocument()!=null && !selectedDocumentDTO.getCodeDocument().equals("")) {
		        messageSuppression=Const.C_MESSAGE_SUPPRESSION_DOCUMENT+" "+selectedDocumentDTO.getCodeDocument();
		        messageModification=Const.C_MESSAGE_MODIFIFCATION_DOCUMENT+" "+selectedDocumentDTO.getCodeDocument();
		    }
		    List<TaRDocumentDTO> docLie=rechercheSiDocLie();
		    String documents = "";
		    if(docLie!=null) {
		        if(docLie!=null)
		            for (TaRDocumentDTO doc : docLie) {
		                if(!documents.equals("") && !doc.getCodeDocumentDest().equals(selectedDocumentDTO.getCodeDocument()))
		                    documents=documents+";"+doc.getCodeDocumentDest();
		                else if(!doc.getCodeDocumentDest().equals(selectedDocumentDTO.getCodeDocument())) documents=doc.getCodeDocumentDest();
		            }
		        if(!documents.equals("")) {
		            messageSuppression="Suppression d'un document lié à un autre : Le document "+selectedDocumentDTO.getCodeDocument()+" est lié au(x) document(s) n° "+documents+"."
		                + "\r\nEtes-vous sur de vouloir le supprimer ?";
		            messageModification="Modification d'un document lié à un autre : Le document "+selectedDocumentDTO.getCodeDocument()+" est lié au(x) document(s) n° "+documents+"."
		                    + "\r\nEtes-vous sur de vouloir le modifier ?";
		        }
		        }
		    return true;
		}
	 
	public RetourAutorisationLiaison autoriseSuppression(boolean silencieux) {
		RetourAutorisationLiaison retour=new RetourAutorisationLiaison();
		TaRDocumentDTO suppressionDocAvecConfirmationPossibleAnomalieStock=null;

		String messageStock="Si vous continuez, vous risquez d'engendrer une anomalie de mouvement de stock";
		messageSuppression=Const.C_MESSAGE_SUPPRESSION_DOCUMENT;
	    messageModification=Const.C_MESSAGE_MODIFIFCATION_DOCUMENT;
		if(selectedDocumentDTO!=null && selectedDocumentDTO.getCodeDocument()!=null && !selectedDocumentDTO.getCodeDocument().equals("")) {
			messageSuppression=Const.C_MESSAGE_SUPPRESSION_DOCUMENT+" "+selectedDocumentDTO.getCodeDocument();
	        messageModification=Const.C_MESSAGE_MODIFIFCATION_DOCUMENT+" "+selectedDocumentDTO.getCodeDocument();
		}
		List<TaRDocumentDTO> docLie=rechercheSiDocLie();
		String documents = "";
		String codeLie="";
		boolean documentCourantEstSrc=false;
		if(docLie!=null) {
			PrimeFaces.current().ajax().addCallbackParam("Autorise la suppression", docLie!=null);
			if(docLie!=null)
				for (TaRDocumentDTO doc : docLie) {//je parcours toutes les relations avec doc en cours
					if(doc.getIdDocumentSrc().compareTo(selectedDocumentDTO.getId())==0) {//si document courant est le src alors on met le code dest dans code lié
						codeLie=doc.getCodeDocumentDest();
						documentCourantEstSrc=true;
					}
					else codeLie=doc.getCodeDocumentSrc();//sinon on récupère le code du document lié avec doc en cours qui dans ce cas est le src
					
					if(!documents.equals(""))documents=documents+";"+codeLie;
					else documents=codeLie;//rajoute codelié à la chaîne des codesLiés
					
//					if(suppressionDocAvecConfirmationPossibleAnomalieStock==null && doc.getTypeDocumentDest().equals(TaFacture.TYPE_DOC)&& doc.getTypeDocumentSrc().equals(TaBonliv.TYPE_DOC) )
					if(suppressionDocAvecConfirmationPossibleAnomalieStock==null && masterEntity.gereStock() && documentCourantEstSrc)
						suppressionDocAvecConfirmationPossibleAnomalieStock=doc;
					retour.lie=true;
					if(!masterEntity.gereStock())messageStock="";
				}
			if(!documents.equals("")) {
				String debutMessage="";
				if(documentCourantEstSrc)debutMessage=" d'un document qui a servi à générer un autre : ";else debutMessage="d'un document lié à un autre : ";
				if(suppressionDocAvecConfirmationPossibleAnomalieStock==null) {
					messageSuppression="Suppression "+debutMessage+" Le document "+selectedDocumentDTO.getCodeDocument()+" est lié au(x) document(s) n° "+documents+"."
							+ "\r\nEtes-vous sur de vouloir le supprimer ?";
		            messageModification="Modification "+debutMessage+" Le document "+selectedDocumentDTO.getCodeDocument()+" est lié au(x) document(s) n° "+documents+"."
		                    + "\r\nEtes-vous sur de vouloir le modifier ?";
				}
				else {//si messageStock est rempli, averti que possible anomalie de stock
					messageSuppression="Suppression "+debutMessage+" \r\nLe document "+selectedDocumentDTO.getCodeDocument()+" est lié au document n° "+documents+"."
							+ "\r\n"+messageStock+ "\r\nEtes-vous sur de vouloir le supprimer ?";
		            messageModification="Modification "+debutMessage+" Le document "+selectedDocumentDTO.getCodeDocument()+" est lié au(x) document(s) n° "+documents+"."
		            		+ "\r\n"+messageStock+ "\r\nEtes-vous sur de vouloir le modifier ?";
//					if(!silencieux)PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage(FacesMessage.SEVERITY_WARN, "Suppression d'un document lié à une autre",messageSuppression ),true);
				}
			}
			
		}
//		retour.autorise=suppressionDocInterdit==null;
		retour.autorise=true;
		return retour;
	}		
	
public String getMessageSuppression() {
	return messageSuppression;
}

public void setMessageSuppression(String messageSuppression) {
	this.messageSuppression = messageSuppression;
}


//public List<TaLigneALigneDTO> dejaGenereLDocument(ILigneDocumentDTO ds ) {
//    List<TaLigneALigneDTO> dejaGenere = null;
//    String requeteFixeRDocument = null;
//    String requeteWhereSupp = null;
//    if(ds.getTypeDocument().equals(TaAcompte.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigne x join x.taLAcompte l join l.taDocument d  left join l.taArticle art  where l.idLDocument="+ds.getIdLDocument();
//    if(ds.getTypeDocument().equals(TaAvoir.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigne x join x.taLAvoir l join l.taDocument d  left join l.taArticle art  where l.idLDocument="+ds.getIdLDocument();
//    if(ds.getTypeDocument().equals(TaAvisEcheance.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigne x join x.taLAvisEcheance l join l.taDocument d  left join l.taArticle art  where l.idLDocument="+ds.getIdLDocument();
//    if(ds.getTypeDocument().equals(TaApporteur.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigne x join x.taLApporteur l join l.taDocument d  left join l.taArticle art  where l.idLDocument="+ds.getIdLDocument();
//    if(ds.getTypeDocument().equals(TaBoncde.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigne x join x.taLBoncde l join l.taDocument d left join l.taArticle art where l.idLDocument="+ds.getIdLDocument();
//    if(ds.getTypeDocument().equals(TaBoncdeAchat.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigne x join x.taLBoncdeAchat l join l.taDocument d left join l.taArticle art  where l.idLDocument="+ds.getIdLDocument();
//    if(ds.getTypeDocument().equals(TaBonReception.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigne x join x.taLBonReception l join l.taDocument d left join l.taArticle art where l.idLDocument="+ds.getIdLDocument();
//    if(ds.getTypeDocument().equals(TaBonliv.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigne x join x.taLBonliv l join l.taDocument d left join l.taArticle art where l.idLDocument="+ds.getIdLDocument();
//    if(ds.getTypeDocument().equals(TaDevis.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigne x join x.taLDevis l join l.taDocument d left join l.taArticle art where l.idLDocument="+ds.getIdLDocument();
//    if(ds.getTypeDocument().equals(TaFacture.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigne x join x.taLFacture l join l.taDocument d left join l.taArticle art where l.idLDocument="+ds.getIdLDocument();
//    if(ds.getTypeDocument().equals(TaPrelevement.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigne x join x.taLPrelevement l join l.taDocument d  where l.idLDocument="+ds.getIdLDocument();
//    if(ds.getTypeDocument().equals(TaProforma.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigne x join x.taLProforma l join l.taDocument d left join l.taArticle art where l.idLDocument="+ds.getIdLDocument();
//    if(ds.getTypeDocument().equals(TaTicketDeCaisse.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigne x join x.taLTicketDeCaisse l join l.taDocument d left join l.taArticle art where l.idLDocument="+ds.getIdLDocument();
//    if(ds.getTypeDocument().equals(TaPreparation.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigne x join x.taLPreparation l join l.taDocument d left join l.taArticle art where l.idLDocument="+ds.getIdLDocument();
//    if(ds.getTypeDocument().equals(TaFlash.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigne x join x.taLFlash l join l.taFlash d left join l.taArticle art where l.idLFlash="+ds.getIdLDocument();
//
//    String requeteDoc="";
//    if(ds!=null && ds.getIdLDocument()!=null && ds.getIdLDocument()!=0) {
//        String jpql = "select x "+requeteFixeRDocument;
//        String nomEntity = null;
//        String typeEntity = null;
//        int idEntity = -1;
//        List<TaLigneALigne> l = taLigneALigneService.dejaGenere(jpql);
//        if(l!=null && l.size()>0)dejaGenere=new LinkedList<>();
//        for (TaLigneALigne ligne : l) {
//        	requeteWhereSupp="";
//        	requeteDoc="";
//            if(ligne.getTaLAcompte()!=null && !ds.getTypeDocument().equals(TaAcompte.TYPE_DOC)) {
//                nomEntity="taLAcompte";
//                typeEntity=TaAcompte.TYPE_DOC;
//                idEntity=ligne.getTaLAcompte().getIdLDocument();
//            }
//            if(ligne.getTaLAvoir()!=null && !ds.getTypeDocument().equals(TaAvoir.TYPE_DOC)) {
//                nomEntity="taLAvoir";
//                typeEntity=TaAvoir.TYPE_DOC;
//                idEntity=ligne.getTaLAvoir().getIdLDocument();
//            }
//            if(ligne.getTaLAvisEcheance()!=null && !ds.getTypeDocument().equals(TaAvisEcheance.TYPE_DOC)) {
//                nomEntity="taLAvisEcheance";
//                typeEntity=TaAvisEcheance.TYPE_DOC;
//                idEntity=ligne.getTaLAvisEcheance().getIdLDocument();
//            }
//            if(ligne.getTaLApporteur()!=null && !ds.getTypeDocument().equals(TaApporteur.TYPE_DOC)) {
//                nomEntity="taLApporteur";
//                typeEntity=TaApporteur.TYPE_DOC;
//                idEntity=ligne.getTaLApporteur().getIdLDocument();
//            }
//            if(ligne.getTaLBoncde()!=null && !ds.getTypeDocument().equals(TaBoncde.TYPE_DOC)) {
//                nomEntity="taLBoncde";
//                idEntity=ligne.getTaLBoncde().getIdLDocument();
//                typeEntity=TaBoncde.TYPE_DOC;
//            }
//            if(ligne.getTaLBoncdeAchat()!=null && !ds.getTypeDocument().equals(TaBoncdeAchat.TYPE_DOC)) {
//                nomEntity="taLBoncdeAchat";
//                typeEntity=TaBoncdeAchat.TYPE_DOC;
//                idEntity=ligne.getTaLBoncdeAchat().getIdLDocument();
//            }
//            if(ligne.getTaLBonliv()!=null && !ds.getTypeDocument().equals(TaBonliv.TYPE_DOC)) {
//                nomEntity="taLBonliv";
//                typeEntity=TaBonliv.TYPE_DOC;
//                idEntity=ligne.getTaLBonliv().getIdLDocument();
//            }
//            if(ligne.getTaLBonReception()!=null && !ds.getTypeDocument().equals(TaBonReception.TYPE_DOC)){
//                nomEntity="taLBonReception";
//                typeEntity=TaBonReception.TYPE_DOC;
//                idEntity=ligne.getTaLBonReception().getIdLDocument();
//            }
//            if(ligne.getTaLDevis()!=null && !ds.getTypeDocument().equals(TaDevis.TYPE_DOC)){
//                nomEntity="taLDevis";
//                typeEntity=TaDevis.TYPE_DOC;
//                idEntity=ligne.getTaLDevis().getIdLDocument();
//            }
//            if(ligne.getTaLFacture()!=null && !ds.getTypeDocument().equals(TaFacture.TYPE_DOC)){
//                nomEntity="taLFacture";
//                typeEntity=TaFacture.TYPE_DOC;
//                idEntity=ligne.getTaLFacture().getIdLDocument();
//            }
//            if(ligne.getTaLPrelevement()!=null && !ds.getTypeDocument().equals(TaPrelevement.TYPE_DOC)){
//                nomEntity="taLPrelevement";
//                typeEntity=TaPrelevement.TYPE_DOC;
//                idEntity=ligne.getTaLPrelevement().getIdLDocument();
//            }
//            if(ligne.getTaLProforma()!=null && !ds.getTypeDocument().equals(TaProforma.TYPE_DOC)){
//                nomEntity="taLProformaAcompte";
//                typeEntity=TaProforma.TYPE_DOC;
//                idEntity=ligne.getTaLProforma().getIdLDocument();
//            }
//            if(ligne.getTaLTicketDeCaisse()!=null && !ds.getTypeDocument().equals(TaTicketDeCaisse.TYPE_DOC)){
//                nomEntity="taLTicketDeCaisse";
//                typeEntity=TaTicketDeCaisse.TYPE_DOC;
//                idEntity=ligne.getTaLTicketDeCaisse().getIdLDocument();
//            }
//            if(ligne.getTaLPreparation()!=null && !ds.getTypeDocument().equals(TaPreparation.TYPE_DOC)){
//                nomEntity="taLPreparation";
//                typeEntity=TaPreparation.TYPE_DOC;
//                idEntity=ligne.getTaLPreparation().getIdLDocument();
//                if(ds.getTypeDocument().equals(TaFlash.TYPE_DOC)) {
//                    requeteWhereSupp=" and x."+nomEntity+".idLDocument="+idEntity;
//                    requeteDoc="select new fr.legrain.document.dto.TaLigneALigneDTO(x.id,x.idLigneSrc,l.idLFlash,x."+nomEntity+".idLDocument,x."+nomEntity+".taDocument.idDocument,x."+nomEntity+".taDocument.codeDocument,art.codeArticle,x.numLot,x."+nomEntity+".libLDocument,x.qteRecue,'"+typeEntity+"') ";
//                 }
//            }
//            if(ligne.getTaLFlash()!=null && !ds.getTypeDocument().equals(TaFlash.TYPE_DOC)){
//                nomEntity="taLFlash";
//                typeEntity=TaFlash.TYPE_DOC;
//                idEntity=ligne.getTaLFlash().getIdLFlash();
//                requeteWhereSupp=" and x."+nomEntity+".idLFlash="+idEntity;
//                requeteDoc="select new fr.legrain.document.dto.TaLigneALigneDTO(x.id,x.idLigneSrc,l.idLDocument,x."+nomEntity+".idLFlash,x."+nomEntity+".taFlash.idFlash,x."+nomEntity+".taFlash.codeFlash,art.codeArticle,x.numLot,x."+nomEntity+".libLFlash,x.qteRecue,'"+typeEntity+"') ";
//            }
//            if(requeteWhereSupp==null || requeteWhereSupp.isEmpty()) {            
//            requeteWhereSupp=" and x."+nomEntity+".idLDocument="+idEntity;
//            requeteDoc="select new fr.legrain.document.dto.TaLigneALigneDTO(x.id,x.idLigneSrc,l.idLDocument,x."+nomEntity+".idLDocument,x."+nomEntity+".taDocument.idDocument,x."+nomEntity+".taDocument.codeDocument,art.codeArticle,x.numLot,x."+nomEntity+".libLDocument,x.qteRecue,'"+typeEntity+"') ";
//            }
//            List<TaLigneALigneDTO> l2 = new LinkedList<>();
//            if(requeteDoc!=null && !requeteDoc.equals(""))
//                l2 =taLigneALigneService.dejaGenereLigneDocument(requeteDoc+requeteFixeRDocument+requeteWhereSupp);
//            for (TaLigneALigneDTO iLDocumentTiers : l2) {
//                dejaGenere.add(iLDocumentTiers);
//            }
//        }
//        System.err.println(l.size());
//    }
//    return dejaGenere;
//}


//public List<TaLigneALigneDTO> dejaGenereDocumentLigneALigne(IDocumentDTO ds ) {
//    List<TaLigneALigneDTO> dejaGenere = null;
//    String requeteFixeRDocument = null;
//    String requeteWhereSupp = null;
//    if(ds.getTypeDocument().equals(TaAcompte.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigne x join x.taLAcompte l join l.taDocument d  left join l.taArticle art  where d.idDocument="+ds.getId();
//    if(ds.getTypeDocument().equals(TaAvoir.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigne x join x.taLAvoir l join l.taDocument d  left join l.taArticle art  where d.idDocument="+ds.getId();
//    if(ds.getTypeDocument().equals(TaAvisEcheance.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigne x join x.taLAvisEcheance l join l.taDocument d  left join l.taArticle art  where d.idDocument="+ds.getId();
//    if(ds.getTypeDocument().equals(TaApporteur.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigne x join x.taLApporteur l join l.taDocument d  left join l.taArticle art  where d.idDocument="+ds.getId();
//    if(ds.getTypeDocument().equals(TaBoncde.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigne x join x.taLBoncde l join l.taDocument d left join l.taArticle art where d.idDocument="+ds.getId();
//    if(ds.getTypeDocument().equals(TaBoncdeAchat.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigne x join x.taLBoncdeAchat l join l.taDocument d left join l.taArticle art  where d.idDocument="+ds.getId();
//    if(ds.getTypeDocument().equals(TaBonReception.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigne x join x.taLBonReception l join l.taDocument d left join l.taArticle art where d.idDocument="+ds.getId();
//    if(ds.getTypeDocument().equals(TaBonliv.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigne x join x.taLBonliv l join l.taDocument d left join l.taArticle art where d.idDocument="+ds.getId();
//    if(ds.getTypeDocument().equals(TaDevis.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigne x join x.taLDevis l join l.taDocument d left join l.taArticle art where d.idDocument="+ds.getId();
//    if(ds.getTypeDocument().equals(TaFacture.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigne x join x.taLFacture l join l.taDocument d left join l.taArticle art where d.idDocument="+ds.getId();
//    if(ds.getTypeDocument().equals(TaPrelevement.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigne x join x.taLPrelevement l join l.taDocument d  where d.idDocument="+ds.getId();
//    if(ds.getTypeDocument().equals(TaProforma.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigne x join x.taLProforma l join l.taDocument d left join l.taArticle art where d.idDocument="+ds.getId();
//    if(ds.getTypeDocument().equals(TaTicketDeCaisse.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigne x join x.taLTicketDeCaisse l join l.taDocument d left join l.taArticle art where d.idDocument="+ds.getId();
//    if(ds.getTypeDocument().equals(TaPreparation.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigne x join x.taLPreparation l join l.taDocument d left join l.taArticle art where d.idDocument="+ds.getId();
//    if(ds.getTypeDocument().equals(TaFlash.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigne x join x.taLFlash l join l.taFlash d left join l.taArticle art where d.idFlash="+ds.getId();
//    String requeteDoc="";
//    if(ds!=null && ds.getId()!=0) {
//        String jpql = "select x "+requeteFixeRDocument;
//        List<TaLigneALigne> l = taLigneALigneService.dejaGenere(jpql);
//        if(l!=null && l.size()>0)dejaGenere=new LinkedList<>();
//        String nomEntity = null;
//        String typeEntity = null;
//        int idEntity = -1;
//        for (TaLigneALigne ligne : l) {
//        	requeteWhereSupp="";
//        	requeteDoc="";
//            if(ligne.getTaLAcompte()!=null && !ds.getTypeDocument().equals(TaAcompte.TYPE_DOC)) {
//                nomEntity="taLAcompte";
//                typeEntity=TaAcompte.TYPE_DOC;
//                idEntity=ligne.getTaLAcompte().getIdLDocument();
//            }
//            if(ligne.getTaLAvoir()!=null && !ds.getTypeDocument().equals(TaAvoir.TYPE_DOC)) {
//                nomEntity="taLAvoir";
//                typeEntity=TaAvoir.TYPE_DOC;
//                idEntity=ligne.getTaLAvoir().getIdLDocument();
//            }
//            if(ligne.getTaLAvisEcheance()!=null && !ds.getTypeDocument().equals(TaAvisEcheance.TYPE_DOC)) {
//                nomEntity="taLAvisEcheance";
//                typeEntity=TaAvisEcheance.TYPE_DOC;
//                idEntity=ligne.getTaLAvisEcheance().getIdLDocument();
//            }
//            if(ligne.getTaLApporteur()!=null && !ds.getTypeDocument().equals(TaApporteur.TYPE_DOC)) {
//                nomEntity="taLApporteur";
//                typeEntity=TaApporteur.TYPE_DOC;
//                idEntity=ligne.getTaLApporteur().getIdLDocument();
//            }
//            if(ligne.getTaLBoncde()!=null && !ds.getTypeDocument().equals(TaBoncde.TYPE_DOC)) {
//                nomEntity="taLBoncde";
//                idEntity=ligne.getTaLBoncde().getIdLDocument();
//                typeEntity=TaBoncde.TYPE_DOC;
//            }
//            if(ligne.getTaLBoncdeAchat()!=null && !ds.getTypeDocument().equals(TaBoncdeAchat.TYPE_DOC)) {
//                nomEntity="taLBoncdeAchat";
//                typeEntity=TaBoncdeAchat.TYPE_DOC;
//                idEntity=ligne.getTaLBoncdeAchat().getIdLDocument();
//            }
//            if(ligne.getTaLBonliv()!=null && !ds.getTypeDocument().equals(TaBonliv.TYPE_DOC)) {
//                nomEntity="taLBonliv";
//                typeEntity=TaBonliv.TYPE_DOC;
//                idEntity=ligne.getTaLBonliv().getIdLDocument();
//            }
//            if(ligne.getTaLBonReception()!=null && !ds.getTypeDocument().equals(TaBonReception.TYPE_DOC)){
//                nomEntity="taLBonReception";
//                typeEntity=TaBonReception.TYPE_DOC;
//                idEntity=ligne.getTaLBonReception().getIdLDocument();
//            }
//            if(ligne.getTaLDevis()!=null && !ds.getTypeDocument().equals(TaDevis.TYPE_DOC)){
//                nomEntity="taLDevis";
//                typeEntity=TaDevis.TYPE_DOC;
//                idEntity=ligne.getTaLDevis().getIdLDocument();
//            }
//            if(ligne.getTaLFacture()!=null && !ds.getTypeDocument().equals(TaFacture.TYPE_DOC)){
//                nomEntity="taLFacture";
//                typeEntity=TaFacture.TYPE_DOC;
//                idEntity=ligne.getTaLFacture().getIdLDocument();
//            }
//            if(ligne.getTaLPrelevement()!=null && !ds.getTypeDocument().equals(TaPrelevement.TYPE_DOC)){
//                nomEntity="taLPrelevement";
//                typeEntity=TaPrelevement.TYPE_DOC;
//                idEntity=ligne.getTaLPrelevement().getIdLDocument();
//            }
//            if(ligne.getTaLProforma()!=null && !ds.getTypeDocument().equals(TaProforma.TYPE_DOC)){
//                nomEntity="taLProformaAcompte";
//                typeEntity=TaProforma.TYPE_DOC;
//                idEntity=ligne.getTaLProforma().getIdLDocument();
//            }
//            if(ligne.getTaLTicketDeCaisse()!=null && !ds.getTypeDocument().equals(TaTicketDeCaisse.TYPE_DOC)){
//                nomEntity="taLTicketDeCaisse";
//                typeEntity=TaTicketDeCaisse.TYPE_DOC;
//                idEntity=ligne.getTaLTicketDeCaisse().getIdLDocument();
//            }
//            if(ligne.getTaLPreparation()!=null && !ds.getTypeDocument().equals(TaPreparation.TYPE_DOC)){
//                nomEntity="taLPreparation";
//                typeEntity=TaPreparation.TYPE_DOC;
//                idEntity=ligne.getTaLPreparation().getIdLDocument();
//                if(ds.getTypeDocument().equals(TaFlash.TYPE_DOC)) {
//                    requeteWhereSupp=" and x."+nomEntity+".idLDocument="+idEntity;
//                    requeteDoc="select new fr.legrain.document.dto.TaLigneALigneDTO(x.id,x.idLigneSrc,l.idLFlash,x."+nomEntity+".idLDocument,x."+nomEntity+".taDocument.idDocument,x."+nomEntity+".taDocument.codeDocument,art.codeArticle,x.numLot,x."+nomEntity+".libLDocument,x.qteRecue,'"+typeEntity+"') ";
//                }
//            }
//            if(ligne.getTaLFlash()!=null && !ds.getTypeDocument().equals(TaFlash.TYPE_DOC)){
//                nomEntity="taLFlash";
//                typeEntity=TaFlash.TYPE_DOC;
//                idEntity=ligne.getTaLFlash().getIdLFlash();
//                requeteWhereSupp=" and x."+nomEntity+".idLFlash="+idEntity;
//                requeteDoc="select new fr.legrain.document.dto.TaLigneALigneDTO(x.id,x.idLigneSrc,l.idLDocument,x."+nomEntity+".idLFlash,x."+nomEntity+".taFlash.idFlash,x."+nomEntity+".taFlash.codeFlash,art.codeArticle,x.numLot,x."+nomEntity+".libLFlash,x.qteRecue,'"+typeEntity+"') ";
//            }
//            if(requeteWhereSupp==null || requeteWhereSupp.isEmpty()) {
//            requeteWhereSupp=" and x."+nomEntity+".idLDocument="+idEntity;
//            requeteDoc="select new fr.legrain.document.dto.TaLigneALigneDTO(x.id,x.idLigneSrc,l.idLDocument,x."+nomEntity+".idLDocument,x."+nomEntity+".taDocument.idDocument,x."+nomEntity+".taDocument.codeDocument,art.codeArticle,x.numLot,x."+nomEntity+".libLDocument,x.qteRecue,'"+typeEntity+"') ";
//            }
//            List<TaLigneALigneDTO> l2 = new LinkedList<>();
//            if(requeteDoc!=null && !requeteDoc.equals(""))
//                l2 =taLigneALigneService.dejaGenereLigneDocument(requeteDoc+requeteFixeRDocument+requeteWhereSupp);
//            for (TaLigneALigneDTO ligneDoc : l2) {
//                dejaGenere.add(ligneDoc);
//            }
//        }
//        System.err.println(l.size());
//    }
//    return dejaGenere;
//}


public List<TaLigneALigneDTO> rechercheSiDocLieLigneALigne() {
	return dejaGenereDocumentLigneALigne(selectedDocumentDTO);
}


public List<TaLigneALigneDTO> rechercheSiLigneDocLie(ILigneDocumentJSF ligne) {
	if(ligne!=null && ligne.getDto()!=null)
		return dejaGenereLDocument(ligne.getDto());
	return null;
}


	public String getTitreSuppression() {
		return titreSuppression;
	}
	
	public void setTitreSuppression(String titreSuppression) {
		this.titreSuppression = titreSuppression;
	}
	
	public String getMessageModification() {
		return messageModification;
	}
	
	public void setMessageModification(String messageModification) {
		this.messageModification = messageModification;
	}
	
	public String getTitreModification() {
		return titreModification;
	}
	
	public void setTitreModification(String titreModification) {
		this.titreModification = titreModification;
	}
	
	public String getClasseCssDataTableLigne() {
		return classeCssDataTableLigne;
	}
	
	public void setClasseCssDataTableLigne(String classeCssDataTableLigne) {
		this.classeCssDataTableLigne = classeCssDataTableLigne;
	}
	
	public TaReglementDTOJSF getDetailLigneReglement() {
		return detailLigneReglement;
	}
	
	public void setDetailLigneReglement(TaReglementDTOJSF detailLigneReglement) {
		this.detailLigneReglement = detailLigneReglement;
	}

	
	
	public void detailDocument(){
		String tabEcran="";
		LgrTab lgrTab=LgrTab.getInstance();
		if(detailLigneReglement!=null){
			tabEcran=lgrTab.getTabDocCorrespondance().get(detailLigneReglement.getTypeDocument());
			IDocumentTiers doc=duplicationDocumentBean.getOuvertureDocumentBean().recupCodeDocument(detailLigneReglement.getDto().getCodeDocument(), detailLigneReglement.getTypeDocument());
			if(tabEcran!=null && !tabEcran.isEmpty()){
				duplicationDocumentBean.getOuvertureDocumentBean().setEvent(new LgrTabEvent());
				duplicationDocumentBean.getOuvertureDocumentBean().getEvent().setCodeObjet(detailLigneReglement.getDto().getCodeDocument());
				duplicationDocumentBean.getOuvertureDocumentBean().getEvent().setData(doc);
				duplicationDocumentBean.getOuvertureDocumentBean().getEvent().setCssLgrTab(tabEcran);
				duplicationDocumentBean.getOuvertureDocumentBean().getEvent().setAfficheDoc(true);
				duplicationDocumentBean.getOuvertureDocumentBean().openDocument(null);
			}
		}
	}
	
	public List<TaEditionDTO> getListeEdition() {
		return listeEdition;
	}
	
	public void setListeEdition(List<TaEditionDTO> listeEdition) {
		this.listeEdition = listeEdition;
	}

	public TaEdition getSelectedEdition() {
		return selectedEdition;
	}

	public void setSelectedEdition(TaEdition selectedEdition) {
		this.selectedEdition = selectedEdition;
	}

	public Integer getPrefNbDecimalesPrix() {
		return prefNbDecimalesPrix;
	}

	public void setPrefNbDecimalesPrix(Integer nbDecimalesPrix) {
		this.prefNbDecimalesPrix = nbDecimalesPrix;
	}
	

	
	//public DefaultSubMenu getSousMenuEdition() {
	//	return sousMenuEdition;
	//}
	//
	//public void setSousMenuEdition(DefaultSubMenu sousMenuEdition) {
	//	this.sousMenuEdition = sousMenuEdition;
	//}
	//
	//public MenuModel getMenuModelEdition() {
	//	return menuModelEdition;
	//}
	//
	//public void setMenuModelEdition(MenuModel menuModelEdition) {
	//	this.menuModelEdition = menuModelEdition;
	//}

	public boolean affichagePrecisionPrixDocument() {
		if(masterEntity==null)return false;
		if(masterEntity.getNbDecimalesPrix()==null) return false;
		if(prefNbDecimalesPrix == null) return false;
		if (!prefNbDecimalesPrix.equals(masterEntity.getNbDecimalesPrix()))	return true;		
		return false;
//		return true;
	}


	public List<TaLigneALigneDTO> rechercheSiDocLieLigneALigne(IDocumentDTO ds) {
	    return dejaGenereDocumentLigneALigne(ds);
	}



public void onClearArticle(AjaxBehaviorEvent event){
    selectedLigneJSF.setTaArticle(null);
    selectedLigneJSF.getDto().setCodeArticle(null);
    masterEntityLigne.setTaArticle(null);
    selectedLigneJSF.getDto().setU1LDocument(null);
    selectedLigneJSF.getDto().setU2LDocument(null);
    selectedLigneJSF.getDto().setQteLDocument(BigDecimal.ONE);
    selectedLigneJSF.getDto().setQte2LDocument(null);
    selectedLigneJSF.getDto().setLibLDocument(null);
    selectedLigneJSF.setTaUnite1(null);
    selectedLigneJSF.setTaUnite2(null);
    selectedLigneJSF.setTaRapport(null);
    selectedLigneJSF.getDto().setPrixULDocument(BigDecimal.ZERO);
    selectedLigneJSF.getDto().setMtHtLDocument(BigDecimal.ZERO);
    selectedLigneJSF.getDto().setMtTtcLDocument(BigDecimal.ZERO);
    selectedLigneJSF.getDto().setMtHtLApresRemiseGlobaleDocument(BigDecimal.ZERO);
    selectedLigneJSF.getDto().setMtTtcLApresRemiseGlobaleDocument(BigDecimal.ZERO);
    selectedLigneJSF.setTaLot(null);
//    selectedLigneJSF.getDto().setNumLot(null);
    
    masterEntity.calculeTvaEtTotaux();
    validateUIField(Const.C_CODE_ARTICLE, null);

}

public TaLigneALigneDTO getSelectedLigneALigneDTO() {
	return selectedLigneALigneDTO;
}

public void setSelectedLigneALigneDTO(TaLigneALigneDTO selectedLigneALigneDTO) {
	this.selectedLigneALigneDTO = selectedLigneALigneDTO;
}

public String getMessageTerminerLigne() {
	return messageTerminerLigne;
}

public void setMessageTerminerLigne(String messageTerminerLigne) {
	this.messageTerminerLigne = messageTerminerLigne;
}

public RetourAutorisationLiaison autoriseSuppressionLigne(boolean silencieux) {
	RetourAutorisationLiaison retour=new RetourAutorisationLiaison();
	TaLigneALigneDTO suppressionDocAvecConfirmationPossibleAnomalieStock=null;
	List<String> listeDocLie = new LinkedList<>();
	String messageStock="Si vous continuez, vous risquez d'engendrer une anomalie de mouvement de stock";
	boolean documentCourantEstSrc=false;
	messageSuppression=Const.C_MESSAGE_SUPPRESSION_DOCUMENT;
    messageModification=Const.C_MESSAGE_MODIFIFCATION_DOCUMENT;
	if(selectedDocumentDTO!=null && selectedDocumentDTO.getCodeDocument()!=null && !selectedDocumentDTO.getCodeDocument().equals("")) {
		messageSuppression=Const.C_MESSAGE_SUPPRESSION_DOCUMENT+" "+selectedDocumentDTO.getCodeDocument();
		messageModification=Const.C_MESSAGE_MODIFIFCATION_DOCUMENT+" "+selectedDocumentDTO.getCodeDocument();
	}
	List<TaLigneALigneDTO> docLie=rechercheSiDocLieLigneALigne();
	String documents = "";
	if(docLie!=null) {
		PrimeFaces.current().ajax().addCallbackParam("Autorise la suppression", docLie!=null);

		if(docLie!=null)
			for (TaLigneALigneDTO doc : docLie) {
				if(doc.getIdLDocumentSrc().compareTo(doc.getIdLigneSrc())==0)documentCourantEstSrc=true;
				if(!documents.equals("") && !doc.getCodeDocumentDest().equals(selectedDocumentDTO.getCodeDocument())&& !listeDocLie.contains(doc.getCodeDocumentDest())) {
					documents=documents+";"+doc.getCodeDocumentDest();
					listeDocLie.add(doc.getCodeDocumentDest());
				}
				else if(!doc.getCodeDocumentDest().equals(selectedDocumentDTO.getCodeDocument())&& !listeDocLie.contains(doc.getCodeDocumentDest())) {
					documents=doc.getCodeDocumentDest();
					listeDocLie.add(doc.getCodeDocumentDest());
				}
				
				if(suppressionDocAvecConfirmationPossibleAnomalieStock==null && documentCourantEstSrc)
					suppressionDocAvecConfirmationPossibleAnomalieStock=doc;
				retour.lie=true;
			}
		if(!documents.equals("")) {
			String debutMessage="";
			if(documentCourantEstSrc)debutMessage=" d'un document qui a servi à générer un autre : ";else debutMessage="d'un document lié à un autre : ";
			if(suppressionDocAvecConfirmationPossibleAnomalieStock==null) {
				messageSuppression="Suppression "+debutMessage+"Le document "+selectedDocumentDTO.getCodeDocument()+" est lié au(x) document(s) n° "+documents+"."
						+ "\r\nSi vous le supprimez, la liaison sera également supprimée."
						+ "\r\nEtes-vous sur de vouloir le supprimer ?";
			messageModification="Modification "+debutMessage+"Le document "+selectedDocumentDTO.getCodeDocument()+" est lié au(x) document(s) n° "+documents+"."
					+ "\r\nEtes-vous sur de vouloir le modifier ?";
			}
			else {
				messageSuppression="Suppression "+debutMessage+"Le document "+selectedDocumentDTO.getCodeDocument()+" est lié au document n° "+suppressionDocAvecConfirmationPossibleAnomalieStock.getCodeDocumentDest()+"."
						+ "\r\n"+messageStock+ "\r\nEtes-vous sur de vouloir le supprimer ?";
	            messageModification="Modification "+debutMessage+"Le document "+selectedDocumentDTO.getCodeDocument()+" est lié au document n° "+suppressionDocAvecConfirmationPossibleAnomalieStock.getCodeDocumentDest()+"."  
	            		+ "\r\n"+messageStock+ "\r\nEtes-vous sur de vouloir le modifier ?";					
//				if(!silencieux) PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage(FacesMessage.SEVERITY_WARN,"Suppression d'un document lié à un autre",messageSuppression ),true);
			}
		}
	}
//	retour.autorise=suppressionDocAvecConfirmationPossibleAnomalieStock==null;
	retour.autorise=true;
	return retour;
}

public boolean autoriseSuppressionLigne(boolean silencieux,ILigneDocumentJSF selectedLigneJSF) {
	TaLigneALigneDTO suppressionDocAvecConfirmationPossibleAnomalieStock=null;
	List<String> listeDocLie = new LinkedList<>();
	messageSuppression=Const.C_MESSAGE_SUPPRESSION_LIGNE_DOCUMENT;
	if(selectedDocumentDTO!=null && selectedDocumentDTO.getCodeDocument()!=null && !selectedDocumentDTO.getCodeDocument().equals(""))
		messageSuppression=Const.C_MESSAGE_SUPPRESSION_LIGNE_DOCUMENT+" "+selectedDocumentDTO.getCodeDocument();
	List<TaLigneALigneDTO> ligneLie=rechercheSiLigneDocLie(selectedLigneJSF);
	String messageStock="Si vous continuez, vous risquez d'engendrer une anomalie de mouvement de stock";
	boolean documentCourantEstSrc=false;
	String lignes = "";
	if(ligneLie!=null) {
		PrimeFaces.current().ajax().addCallbackParam("Autorise la suppression", ligneLie!=null);
		if(ligneLie!=null)
			for (TaLigneALigneDTO doc : ligneLie) {
				if(doc.getIdLDocumentSrc().compareTo(doc.getIdLigneSrc())==0)documentCourantEstSrc=true;
				if(!lignes.equals("") && doc.getIdDocumentDest()!=(selectedLigneJSF.getDto().getIdLDocument()) && !listeDocLie.contains(doc.getCodeDocumentDest())) {
					lignes=lignes+";"+doc.getCodeDocumentDest();
					listeDocLie.add(doc.getCodeDocumentDest());
				}
				else if(doc.getIdDocumentDest()!=(selectedLigneJSF.getDto().getIdLDocument())&& !listeDocLie.contains(doc.getCodeDocumentDest())) {
					lignes=doc.getCodeDocumentDest();
					listeDocLie.add(doc.getCodeDocumentDest());
				}
				if(suppressionDocAvecConfirmationPossibleAnomalieStock==null && doc.getIdLDocumentSrc().compareTo(doc.getIdLigneSrc())==0)
					suppressionDocAvecConfirmationPossibleAnomalieStock=doc;
			}
		if(!lignes.equals("")) {
			String debutMessage="";
			if(documentCourantEstSrc)debutMessage=" d'un document qui a servi à générer un autre : ";else debutMessage="d'un document lié à un autre : ";
			if(suppressionDocAvecConfirmationPossibleAnomalieStock==null) {
				messageSuppression="Suppression d'une ligne"+debutMessage+"Le document "+selectedDocumentDTO.getCodeDocument()+" est lié au(x) document(s) n° "+lignes+"."
						+ "\r\nSi vous la supprimez, la liaison sera également supprimée."
						+ "\r\nEtes-vous sur de vouloir la supprimer ?";
			messageModification="Modification "+debutMessage+"Le document "+selectedDocumentDTO.getCodeDocument()+" est lié au(x) document(s) n° "+lignes+"."
					+ "\r\nEtes-vous sur de vouloir la modifier ?";
			}
			else {
				messageSuppression="Suppression d'une ligne"+debutMessage+"Le document "+selectedDocumentDTO.getCodeDocument()+" est lié au document n° "+suppressionDocAvecConfirmationPossibleAnomalieStock.getCodeDocumentDest()+"."
						+ "\r\n"+messageStock+ "\r\nEtes-vous sur de vouloir la supprimer ?";
	            messageModification="Modification "+debutMessage+"Le document "+selectedDocumentDTO.getCodeDocument()+" est lié au document n° "+suppressionDocAvecConfirmationPossibleAnomalieStock.getCodeDocumentDest()+"."  
	            		+ "\r\n"+messageStock+ "\r\nEtes-vous sur de vouloir la modifier ?";					
//				if(!silencieux) PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage(FacesMessage.SEVERITY_WARN,"Suppression d'un document lié à un autre",messageSuppression),true);
			}
		}
	}
//	return suppressionDocAvecConfirmationPossibleAnomalieStock==null;
	return true;
}

public boolean autorisationLiaisonComplete(boolean silencieux) {
	RetourAutorisationLiaison retour=autoriseSuppression(silencieux);
	if(retour.autorise && !retour.lie)
		retour.autorise=autoriseSuppressionLigne(silencieux).autorise;
	return retour.autorise;
}

public boolean autorisationLiaisonComplete() {
	RetourAutorisationLiaison retour=autoriseSuppression(false);
	if(retour.autorise && !retour.lie)retour.autorise=autoriseSuppressionLigne(false).autorise;
	return retour.autorise;
}

public class RetourAutorisationLiaison {
	boolean autorise;
	boolean lie;
}

public TaStripePaymentIntent getPaiementParCarteEnAttente() {
	return paiementParCarteEnAttente;
}

public void setPaiementParCarteEnAttente(TaStripePaymentIntent paiementParCarteEnAttente) {
	this.paiementParCarteEnAttente = paiementParCarteEnAttente;
}

public List<TaLigneALigneEcheance> getListeEcheanceLiaisonASupprimer() {
	return listeLigneALigneEcheanceASupprimer;
}

public void setListeEcheanceLiaisonASupprimer(List<TaLigneALigneEcheance> listeLigneALigneEcheanceASupprimer) {
	this.listeLigneALigneEcheanceASupprimer = listeLigneALigneEcheanceASupprimer;
}


public void actDialogDetailLigneAbonnement(ActionEvent actionEvent) {	

	TaLigneALigneEcheanceDTO taLigneALigneEcheanceDTO = (TaLigneALigneEcheanceDTO) actionEvent.getComponent().getAttributes().get("taLigneALigneEcheanceDTO");
	
	Map<String,Object> options = new HashMap<String, Object>();
	options.put("modal", true);
	options.put("draggable", false);
	options.put("closable", false);
	options.put("resizable", true);
	options.put("contentHeight", 710);
	options.put("contentWidth", "100%");
	options.put("width", 1024);

	Map<String,Object> mapDialogue = new HashMap<String,Object>();

	mapDialogue.put("taLigneALigneEcheanceDTO",taLigneALigneEcheanceDTO );
	mapDialogue.put("dansDialogue",true );

	Map<String,List<String>> params = new HashMap<String,List<String>>();
	List<String> list = new ArrayList<String>();
	list.add(modeEcran.modeString(EnumModeObjet.C_MO_CONSULTATION));
	params.put("modeEcranDefaut", list);


	ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
	Map<String, Object> sessionMap = externalContext.getSessionMap();
	sessionMap.put("mapDialogue", mapDialogue);

//	PrimeFaces.current().dialog().openDynamic("documents/dialog_detail_abonnement.xhtml", options, params);
	PrimeFaces.current().dialog().openDynamic("documents/detail_ligne_Abonnement.xhtml", options, params);
	

}

public void handleReturnDialogDetailLigneAbonnement(SelectEvent event) {

}



public boolean afficheUniteSaisie() {
	return afficheUniteSaisie;
}



public void setAfficheUniteSaisie(boolean afficheUniteSaisie) {
	this.afficheUniteSaisie = afficheUniteSaisie;
}

public boolean isAfficheUniteSaisie() {
	return afficheUniteSaisie;
}


public boolean enabledUniteSaisie() {
	if(selectedLigneJSF!=null && selectedLigneJSF.getTaArticle() !=null) {
		return selectedLigneJSF.getTaCoefficientUniteSaisie()!=null;
	}
	return false;
}





public boolean calculCoherenceAffectationCoefficientQte2(BigDecimal qte2Actuelle) {
	return calculAffectationCoefficientQte2(selectedLigneJSF).compareTo(qte2Actuelle)==0;	
}

public BigDecimal  calculAffectationCoefficientQte2(LigneJSF  ligne) {
	BigDecimal qteCalculee =ligne.getDto().getQte2LDocument();
	if(ligne==null)return null;
	if(ligne.getTaCoefficientUnite()!=null) {
		qteCalculee =ligne.getDto().getQteLDocument();
		if(ligne.getTaCoefficientUnite().getOperateurDivise()) 
			return (qteCalculee).divide(ligne.getTaCoefficientUnite().getCoeff()
					,MathContext.DECIMAL128).setScale(ligne.getTaCoefficientUnite().getNbDecimale(),BigDecimal.ROUND_HALF_UP);
		else return (qteCalculee).multiply(ligne.getTaCoefficientUnite().getCoeff());
	}else  {
		return qteCalculee;
	}		
}

public void actArreterEnregistrementEtFocusQte2() throws Exception {
	//récupérer la ligne con
	//passer dans le validate de qté2 et ensuite réenregistrer la ligne avec la nouvelle valeurs de qté2
	ILigneDocumentTiers masterEntityLigneAReenregistrer = null;
	if(ligneAReenregistrer!=null) {
		validateUIField(Const.C_QTE_L_DOCUMENT, ligneAReenregistrer.getDto().getQteLDocument());
		ligneAReenregistrer.getDto().setQte2LDocument(calculAffectationCoefficientQte2(ligneAReenregistrer));
		if(ligneAReenregistrer.getDto().getIdLDocument()!=null &&  ligneAReenregistrer.getDto().getIdLDocument()!=0) {
			masterEntityLigneAReenregistrer=rechercheLigne(ligneAReenregistrer.getDto().getIdLDocument());
		}else if(ligneAReenregistrer.getDto().getNumLigneLDocument()!=null/* &&  selectedTaLBonReceptionDTOJSF.getDto().getNumLigneLDocument()!=0*/) {
			masterEntityLigneAReenregistrer = rechercheLigneNumLigne(ligneAReenregistrer.getDto().getNumLigneLDocument());
		}
		if(masterEntityLigneAReenregistrer!=null)
			masterEntityLigneAReenregistrer.setQte2LDocument(ligneAReenregistrer.getDto().getQte2LDocument());
	}

}

public LigneJSF  getLigneAReenregistrer() {
	return ligneAReenregistrer;
}

public void setLigneAReenregistrer(LigneJSF  ligneAReenregistrer) {
	this.ligneAReenregistrer = ligneAReenregistrer;
}


}
