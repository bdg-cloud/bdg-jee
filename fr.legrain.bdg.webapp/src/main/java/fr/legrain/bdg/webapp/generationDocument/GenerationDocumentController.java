package fr.legrain.bdg.webapp.generationDocument;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIOutput;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

import fr.legrain.abonnement.stripe.model.TaStripePaymentIntent;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripePaymentIntentServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
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
import fr.legrain.bdg.documents.service.remote.ITaLEcheanceServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaPanierServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaPrelevementServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaPreparationServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaProformaServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaRDocumentServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaTicketDeCaisseServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaPreferencesServiceRemote;
import fr.legrain.bdg.dossierIntelligent.service.remote.ITaParamDossierIntelligentServiceRemote;
import fr.legrain.bdg.dossierIntelligent.service.remote.ITaTypeDonneeServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.tiers.service.remote.ITaEtatServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaParamCreeDocTiersServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTPaiementServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.bdg.webapp.LgrTabEvent;
import fr.legrain.bdg.webapp.app.AbstractController;
import fr.legrain.bdg.webapp.app.AutorisationBean;
import fr.legrain.bdg.webapp.app.LgrTab;
import fr.legrain.bdg.webapp.documents.OuvertureDocumentBean;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.TaRDocumentDTO;
import fr.legrain.document.model.RetourGenerationDoc;
import fr.legrain.document.model.TaAbonnement;
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
import fr.legrain.document.model.TaLAvisEcheance;
import fr.legrain.document.model.TaLEcheance;
import fr.legrain.document.model.TaPanier;
import fr.legrain.document.model.TaPrelevement;
import fr.legrain.document.model.TaPreparation;
import fr.legrain.document.model.TaProforma;
import fr.legrain.document.model.TaTicketDeCaisse;
import fr.legrain.document.model.TypeDoc;
import fr.legrain.dossier.model.TaPreferences;
import fr.legrain.generation.service.CreationDocumentMultiple;
import fr.legrain.generationDocument.model.ParamAfficheChoixGenerationDocument;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.LibDate;
import fr.legrain.tiers.model.TaTiers;


@Named
@ViewScoped 
public class GenerationDocumentController extends AbstractController{
	
	@Inject @Named(value="autorisationBean")
	private AutorisationBean autorisation;
	
	@Inject @Named(value="ouvertureDocumentBean")
	private OuvertureDocumentBean ouvertureDocumentBean;
	
	
	@EJB private ITaPreferencesServiceRemote PreferencesService;
	@EJB private CreationDocumentMultiple creation;
	@EJB private ITaInfoEntrepriseServiceRemote taInfoEntrepriseService;
	
	@EJB private ITaTiersServiceRemote daoTiers;
	@EJB private ITaFactureServiceRemote daoFacture;
	@EJB private ITaAvoirServiceRemote daoAvoir;
	@EJB private ITaBonlivServiceRemote daoBonLiv;
	@EJB private ITaBoncdeServiceRemote daoBoncde;
	@EJB private ITaBonReceptionServiceRemote daoBonReception;
	@EJB private ITaBoncdeAchatServiceRemote daoBoncdeAchat;
	@EJB private ITaLBoncdeAchatServiceRemote daoLBoncdeAchat;
	@EJB private ITaApporteurServiceRemote daoApporteur;
	@EJB private ITaProformaServiceRemote daoProforma;
	@EJB private ITaDevisServiceRemote daoDevis;
	@EJB private ITaPrelevementServiceRemote daoPrelevement;
	@EJB private ITaAcompteServiceRemote daoAcompte;
	@EJB private ITaTicketDeCaisseServiceRemote daoTicketDeCaisse;
	@EJB private ITaAvisEcheanceServiceRemote daoAvisEcheance;
	@EJB private ITaInfoEntrepriseServiceRemote daoInfos;
	@EJB private ITaTPaiementServiceRemote daoTPaiement ;
	@EJB private ITaParamCreeDocTiersServiceRemote daoParamCreeDocTiers;
	@EJB private ITaParamDossierIntelligentServiceRemote taParamDao;
	@EJB private ITaTypeDonneeServiceRemote daoType;
	@EJB private ITaArticleServiceRemote taArticleService;
	@EJB private ITaEtatServiceRemote taEtatService;
	@EJB private ITaPanierServiceRemote daoPanier;   
	@EJB private ITaPreparationServiceRemote daoPreparation;   
	
	@EJB private ITaLEcheanceServiceRemote taLEcheanceService;
	@EJB private ITaAbonnementServiceRemote taAbonnementService;
	
	@EJB private ITaRDocumentServiceRemote taRDocumentService;
	
	@EJB private ITaStripePaymentIntentServiceRemote taStripePaymentIntentService;
	
	
	private List<IDocumentTiers> listeDocCrees = new ArrayList<IDocumentTiers>();
	private String typeSrc = "";
	private String typeDest = "";
	private IDocumentTiers documentSrc = null;
	private String idDocumentSrc = null;
	private List<SelectItem> ListeTypeDoc;
    private String[] selectedTypeDoc;
	private String selectedType = "";
	private String nouveauLibelle = null;
	//private Date dateDocument = null;
	private LocalDate dateDocument = null;
	private Date dateLivDocument = null;
	private String codeTiers=null;
	private TaTiers tiers;
	
	private boolean mouvementerCrdAvoir = false;
	
	
	private TaStripePaymentIntent paiementParCarteEnAttente = null;

	private String titreEcran;
	
	protected LocalDate dateMin;
	protected LocalDate dateMax;
	
	private String typeFacture =TaFacture.TYPE_DOC;
	private String typeAvoir =TaAvoir.TYPE_DOC;
	private String typeAcompte =TaAcompte.TYPE_DOC;
	private String typeApporteur =TaApporteur.TYPE_DOC;
	private String typeDevis =TaDevis.TYPE_DOC;
	private String typeBonLiv =TaBonliv.TYPE_DOC;
	private String typeBoncde =TaBoncde.TYPE_DOC;
	private String typeBoncdeAchat =TaBoncdeAchat.TYPE_DOC;
	private String typeBonReception =TaBonReception.TYPE_DOC;
	private String typeProforma =TaProforma.TYPE_DOC;
	private String typePrelevement =TaPrelevement.TYPE_DOC;
	private String typeAvisEcheance =TaAvisEcheance.TYPE_DOC;
	private String typeTicketDeCaisse =TaTicketDeCaisse.TYPE_DOC;
	private String typePanier =TaPanier.TYPE_DOC;
	private String typeBonPreparation =TaPreparation.TYPE_DOC;
	
//	public static String option1="repriseCodeDocument";
//	public static String option2="repriseLibelle";
//	public static String option3="aucun";
	public static String option1="1";
	public static String option2="2";
	public static String option3="3";	
	private String option=option3;
	
	private boolean ouvrir=false;
	private boolean capturerReglement=false;
	private boolean accepterMultiType=false;
	
	private ParamAfficheChoixGenerationDocument result;
	
	private @EJB ITaTiersServiceRemote taTiersService;
	
	@PostConstruct
	public void init() {
		ListeTypeDoc = new ArrayList<SelectItem>();
		listeDocCrees.clear();
//		Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
//		HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
		
		dateMin=LibDate.dateToLocalDate(taInfoEntrepriseService.findInstance().getDatedebInfoEntreprise()) ;
		dateMax=LibDate.dateToLocalDate(taInfoEntrepriseService.findInstance().getDatefinInfoEntreprise());
		ParamAfficheChoixGenerationDocument param=(ParamAfficheChoixGenerationDocument) externalContext.getSessionMap().get("generation");
		
		if(param instanceof ParamAfficheChoixGenerationDocument) {
			documentSrc = ((ParamAfficheChoixGenerationDocument)param).getDocumentSrc();
			typeSrc = ((ParamAfficheChoixGenerationDocument)param).getTypeSrc();
			typeDest =((ParamAfficheChoixGenerationDocument)param).getTypeDest();
			codeTiers=((ParamAfficheChoixGenerationDocument)param).getCodeTiers();
			List<IDocumentTiers> listeDocument = ((ParamAfficheChoixGenerationDocument)param).getListeDocumentSrc();
			setNouveauLibelle(((ParamAfficheChoixGenerationDocument)param).getLibelle());
			if(((ParamAfficheChoixGenerationDocument)param).getDateDocument()==null)
				dateDocument=LocalDate.now();
			else
				dateDocument= LibDate.dateToLocalDate(((ParamAfficheChoixGenerationDocument)param).getDateDocument());
			try {
				dateDocument=  LibDate.dateToLocalDate(taInfoEntrepriseService.dateDansExercice(LibDate.localDateToDate(dateDocument)));
			} catch (Exception e) {				
			}
			if(((ParamAfficheChoixGenerationDocument)param).getDateLivraison()==null)
				dateLivDocument=new Date();
			else
				dateLivDocument=((ParamAfficheChoixGenerationDocument)param).getDateLivraison();
			
			if(listeDocument!=null && listeDocument.size()>0)
				codeTiers=((IDocumentTiers)listeDocument.get(0)).getTaTiers().getCodeTiers();
			if(((ParamAfficheChoixGenerationDocument) param).getCodeTiers()!=null &&
					!((ParamAfficheChoixGenerationDocument) param).getCodeTiers().equals(""))
				codeTiers=((ParamAfficheChoixGenerationDocument) param).getCodeTiers();
			else 
				if(((ParamAfficheChoixGenerationDocument) param).isMultiple()==true)codeTiers="";
			
			//parametrage d'un libellé reprenant le numéro de chaque document pour séparer chaque groupe de lignes
			//des différents documents composant le nouveau.
			if(!((ParamAfficheChoixGenerationDocument) param).isMultiple()){
				((ParamAfficheChoixGenerationDocument)param).setRepriseAucun(true);
				((ParamAfficheChoixGenerationDocument)param).setRepriseReferenceSrc(false);
				((ParamAfficheChoixGenerationDocument)param).setRepriseLibelleSrc(false);
			}
			else{
				((ParamAfficheChoixGenerationDocument)param).setRepriseReferenceSrc(false);
				((ParamAfficheChoixGenerationDocument)param).setRepriseLibelleSrc(true);
				((ParamAfficheChoixGenerationDocument)param).setRepriseAucun(false);
			}

			
			if(((ParamAfficheChoixGenerationDocument)param).getRepriseReferenceSrc())option=option1;
			if(((ParamAfficheChoixGenerationDocument)param).getRepriseLibelleSrc())option=option2;
			if(((ParamAfficheChoixGenerationDocument)param).getRepriseAucun())option=option3;

			
			setResult((ParamAfficheChoixGenerationDocument)param);
			
			if(documentSrc instanceof TaBoncde) {
				paiementParCarteEnAttente = taStripePaymentIntentService.findPaiementNonCapture((TaBoncde)documentSrc);
			}
		}
		titreEcran=result.getTitreFormulaire();

		if(result.getCodeTiers()!=null){
			try {
				tiers=taTiersService.findByCode(result.getCodeTiers());
			} catch (FinderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(result.isGenerationModele()){
			if(typeSrc.equals(TypeDoc.TYPE_AVOIR)) {
				addBtnAvoir();
			} else if(typeSrc.equals(TypeDoc.TYPE_ACOMPTE)) {
				addBtnAcompte();
			} else if(typeSrc.equals(TypeDoc.TYPE_PANIER)) {
				addBtnPanier();
				addBtnFacture();
				addBtnBonCmd();
//			} else if(typeSrc.equals(TypeDoc.TYPE_FLASH)) {
//				addBtnPreparation();
			} else if(typeSrc.equals(TypeDoc.TYPE_DEVIS)) {
				addBtnDevis();
				addBtnFacture();
				addBtnBonLiv();
				addBtnBonCmd();
				addBtnProforma();
				addBtnAvoir();
				addBtnApporteur();
//				addBtnAcompte();
				addBtnPrelevement();
			} else if(typeSrc.equals(TypeDoc.TYPE_FACTURE)) {
				addBtnDevis();
				addBtnFacture();
				addBtnBonLiv();
				addBtnBonCmd();
				addBtnProforma();
				addBtnAvoir();
				addBtnApporteur();
//				addBtnAcompte();
				addBtnPrelevement();
			} else if(typeSrc.equals(TypeDoc.TYPE_PROFORMA)) {
				addBtnDevis();
				addBtnFacture();
				addBtnBonLiv();
				addBtnBonCmd();
				addBtnProforma();
				addBtnAvoir();
				addBtnApporteur();
//				addBtnAcompte();
				addBtnPrelevement();
			}else if(typeSrc.equals(TypeDoc.TYPE_PRELEVEMENT)) {
				addBtnDevis();
				addBtnFacture();
				addBtnBonLiv();
				addBtnBonCmd();
				addBtnProforma();
				addBtnAvoir();
				addBtnApporteur();
//				addBtnAcompte();
				addBtnPrelevement();
			} else if(typeSrc.equals(TypeDoc.TYPE_APPORTEUR)) {
				addBtnApporteur();
			} else if(typeSrc.equals(TypeDoc.TYPE_BON_COMMANDE_ACHAT)) {
				addBtnBonCmdAchat();
			} else if(typeSrc.equals(TypeDoc.TYPE_BON_COMMANDE)) {
				addBtnDevis();
				addBtnFacture();
				addBtnBonLiv();
				addBtnBonCmd();
				addBtnProforma();
				addBtnAvoir();
				addBtnApporteur();
//				addBtnAcompte();
				addBtnPrelevement();
			} else if(typeSrc.equals(TypeDoc.TYPE_BON_LIVRAISON)) {
				addBtnDevis();
				addBtnFacture();
				addBtnBonLiv();
				addBtnBonCmd();
				addBtnProforma();
				addBtnAvoir();
				addBtnApporteur();
//				addBtnAcompte();
				addBtnPrelevement();
			} else if(typeSrc.equals(TypeDoc.TYPE_TICKET_DE_CAISSE)) {
				addBtnTicketDeCaisse();
				addBtnFacture();
			} else if(typeSrc.equals(TypeDoc.TYPE_FLASH)) {
//				addBtnPreparation();
			}else if(typeSrc.equals(TypeDoc.TYPE_PREPARATION)) {
//				addBtnPreparation();
			}else if(typeSrc.equals(TypeDoc.TYPE_BON_RECEPTION)) {
//				addBtnBonReception();
			}
		}else{
			if(typeDest!=null && typeDest!=""){
				if(typeDest.equals(TypeDoc.TYPE_ACOMPTE))
					addBtnAcompte();
				if(typeDest.equals(TypeDoc.TYPE_APPORTEUR))
					addBtnApporteur();
				if(typeDest.equals(TypeDoc.TYPE_AVOIR))
					addBtnAvoir();
				if(typeDest.equals(TypeDoc.TYPE_BON_COMMANDE))
					addBtnBonCmd();
				if(typeDest.equals(TypeDoc.TYPE_BON_COMMANDE_ACHAT))
					addBtnBonCmdAchat();
				if(typeDest.equals(TypeDoc.TYPE_BON_RECEPTION))
					addBtnBonReception();				
				if(typeDest.equals(TypeDoc.TYPE_BON_LIVRAISON))
					addBtnBonLiv();
				if(typeDest.equals(TypeDoc.TYPE_DEVIS))
					addBtnDevis();
				if(typeDest.equals(TypeDoc.TYPE_FACTURE))
					addBtnFacture();
				if(typeDest.equals(TypeDoc.TYPE_PRELEVEMENT))
					addBtnPrelevement();
				if(typeDest.equals(TypeDoc.TYPE_PROFORMA))
					addBtnProforma();
				if(typeDest.equals(TypeDoc.TYPE_TICKET_DE_CAISSE))
					addBtnTicketDeCaisse();
				if(typeDest.equals(TypeDoc.TYPE_PREPARATION))
					addBtnPreparation();
				if(typeDest.equals(TypeDoc.TYPE_PANIER))
					addBtnPanier();
				if(typeDest.equals(TypeDoc.TYPE_AVIS_ECHEANCE))
					addBtnAvisEcheance();
				//if(typeDest.equals(TypeDoc.TYPE_PANIER))
					//addBtnPanier();

			}else{
				TypeDoc.getInstance();
				for (int i = 0; i < TypeDoc.getInstance().getEditorDocPossibleCreationDocument().size(); i++) {
					if(TypeDoc.getInstance().getEditorDocPossibleCreationDocument().get(i)[0].equals(typeSrc)){
						if(TypeDoc.getInstance().getEditorDocPossibleCreationDocument().get(i)[1].equals(TypeDoc.TYPE_ACOMPTE))
							addBtnAcompte();
						if(TypeDoc.getInstance().getEditorDocPossibleCreationDocument().get(i)[1].equals(TypeDoc.TYPE_APPORTEUR))
							addBtnApporteur();
						if(TypeDoc.getInstance().getEditorDocPossibleCreationDocument().get(i)[1].equals(TypeDoc.TYPE_AVOIR))
							addBtnAvoir();
						if(TypeDoc.getInstance().getEditorDocPossibleCreationDocument().get(i)[1].equals(TypeDoc.TYPE_BON_RECEPTION))
							addBtnBonReception();	
						if(TypeDoc.getInstance().getEditorDocPossibleCreationDocument().get(i)[1].equals(TypeDoc.TYPE_BON_COMMANDE_ACHAT))
							addBtnBonCmdAchat();						
						if(TypeDoc.getInstance().getEditorDocPossibleCreationDocument().get(i)[1].equals(TypeDoc.TYPE_BON_COMMANDE))
							addBtnBonCmd();
						if(TypeDoc.getInstance().getEditorDocPossibleCreationDocument().get(i)[1].equals(TypeDoc.TYPE_BON_LIVRAISON))
							addBtnBonLiv();
						if(TypeDoc.getInstance().getEditorDocPossibleCreationDocument().get(i)[1].equals(TypeDoc.TYPE_DEVIS))
							addBtnDevis();
						if(TypeDoc.getInstance().getEditorDocPossibleCreationDocument().get(i)[1].equals(TypeDoc.TYPE_FACTURE))
							addBtnFacture();
						if(TypeDoc.getInstance().getEditorDocPossibleCreationDocument().get(i)[1].equals(TypeDoc.TYPE_PRELEVEMENT))
							addBtnPrelevement();
						if(TypeDoc.getInstance().getEditorDocPossibleCreationDocument().get(i)[1].equals(TypeDoc.TYPE_PROFORMA))
							addBtnProforma();
						if(TypeDoc.getInstance().getEditorDocPossibleCreationDocument().get(i)[1].equals(TypeDoc.TYPE_TICKET_DE_CAISSE))
							addBtnTicketDeCaisse();
						if(TypeDoc.getInstance().getEditorDocPossibleCreationDocument().get(i)[1].equals(TypeDoc.TYPE_PREPARATION))
							addBtnPreparation();
						if(TypeDoc.getInstance().getEditorDocPossibleCreationDocument().get(i)[1].equals(TypeDoc.TYPE_PANIER))
							addBtnPanier();						
						if(TypeDoc.getInstance().getEditorDocPossibleCreationDocument().get(i)[1].equals(TypeDoc.TYPE_AVIS_ECHEANCE))
							addBtnAvisEcheance();
					}
				}
			}
		}
		if(ListeTypeDoc.size()>0)
			selectedType=(String) ListeTypeDoc.get(0).getValue();
		choixTypeDest();
	}
	
	private void addBtnAvoir() {
		if(autorisation.autoriseMenu(AutorisationBean.ID_MODULE_AVOIR)) {
			ListeTypeDoc.add(new SelectItem(TypeDoc.TYPE_AVOIR, "Avoir"));
		}
	}

	private void addBtnDevis() {
		if(autorisation.autoriseMenu(AutorisationBean.ID_MODULE_DEVIS)) {
			ListeTypeDoc.add(new SelectItem(TypeDoc.TYPE_DEVIS, "Devis"));
		}
	}
	
	private void addBtnTicketDeCaisse() {
		if(autorisation.autoriseMenu(AutorisationBean.ID_MODULE_CAISSE)) {
			ListeTypeDoc.add(new SelectItem(TypeDoc.TYPE_TICKET_DE_CAISSE, "Ticket de caisse"));
		}
	}

	private void addBtnFacture() {
		if(autorisation.autoriseMenu(AutorisationBean.ID_MODULE_FACTURE)) {
			ListeTypeDoc.add(new SelectItem(TypeDoc.TYPE_FACTURE, "Facture"));
		}
	}

	private void addBtnProforma() {
		if(autorisation.autoriseMenu(AutorisationBean.ID_MODULE_PROFORMA)) {
			ListeTypeDoc.add(new SelectItem(TypeDoc.TYPE_PROFORMA, "Proforma"));
		}
	}

	private void addBtnApporteur() {
		if(autorisation.autoriseMenu(AutorisationBean.ID_MODULE_APPORTEUR)) {
			ListeTypeDoc.add(new SelectItem(TypeDoc.TYPE_APPORTEUR, "Facture apporteur"));
		}
	}

	private void addBtnBonCmdAchat() {
		if(autorisation.autoriseMenu(AutorisationBean.ID_MODULE_BON_COMMANDE_ACHAT)) {
			ListeTypeDoc.add(new SelectItem(TypeDoc.TYPE_BON_COMMANDE_ACHAT, "Bon de commande achat"));
		}
	}
	

	private void addBtnBonReception() {
		if(autorisation.autoriseMenu(AutorisationBean.ID_MODULE_BON_RECEPTION)) {
			ListeTypeDoc.add(new SelectItem(TypeDoc.TYPE_BON_RECEPTION, "Bon de réception"));
		}
	}
	
	private void addBtnBonCmd() {
		if(autorisation.autoriseMenu(AutorisationBean.ID_MODULE_BON_COMMANDE)) {
			ListeTypeDoc.add(new SelectItem(TypeDoc.TYPE_BON_COMMANDE, "Bon de commande"));
		}
	}

	private void addBtnBonLiv() {
		if(autorisation.autoriseMenu(AutorisationBean.ID_MODULE_BON_LIVRAISON)) {
			ListeTypeDoc.add(new SelectItem(TypeDoc.TYPE_BON_LIVRAISON, "Bon de livraison"));
		}
	}
	private void addBtnPrelevement() {
		if(autorisation.autoriseMenu(AutorisationBean.ID_MODULE_PRELEVEMENT)) {
			ListeTypeDoc.add(new SelectItem(TypeDoc.TYPE_PRELEVEMENT, "Prélèvement"));
		}
	}
	private void addBtnAcompte() {
		if(autorisation.autoriseMenu(AutorisationBean.ID_MODULE_ACOMPTE)) {
			ListeTypeDoc.add(new SelectItem(TypeDoc.TYPE_ACOMPTE, "Acompte"));
		}
	}
	private void addBtnPreparation() {
		if(autorisation.autoriseMenu(AutorisationBean.ID_MODULE_PREPARATION)) {
			ListeTypeDoc.add(new SelectItem(TypeDoc.TYPE_PREPARATION, "Bon de préparation"));
		}
	}
	private void addBtnPanier() {
		if(autorisation.autoriseMenu(AutorisationBean.ID_MODULE_PANIER)) {
			ListeTypeDoc.add(new SelectItem(TypeDoc.TYPE_PANIER, "Panier"));
		}
	}
	private void addBtnAvisEcheance() {
		if(autorisation.autoriseMenu(AutorisationBean.ID_MODULE_AVOIR)) {
			ListeTypeDoc.add(new SelectItem(TypeDoc.TYPE_AVIS_ECHEANCE, "Avis d'échéance"));
		}		
	}
	
	public void actFermer(Object e) {
		PrimeFaces.current().dialog().closeDynamic(e);
	}
	
	public void actFermer(ActionEvent e) {
		PrimeFaces.current().dialog().closeDynamic(null);
	}
	public void actAnnuler(ActionEvent e) {
			
		}
	
	
	public void actEnregistrer(ActionEvent e) {
		
	}
	
	
	
	public void actImprimer(ActionEvent e)  throws Exception {
		
		listeDocCrees=new ArrayList<IDocumentTiers>();
		result.setDateDocument(LibDate.localDateToDate(dateDocument));
		result.setLibelle(nouveauLibelle);
		result.setTypeDest(selectedType);
		if(option==null)option=option3;
		result.setRepriseReferenceSrc(option.equals(option1));
		result.setRepriseLibelleSrc(option.equals(option2));
		result.setRepriseAucun(option.equals(option3));

		result.setCodeTiers(codeTiers);
		result.setRetour(true);
		result.setOuvrir(ouvrir);
		result.setAccepterMultiType(accepterMultiType);
		
		result.setCapturerReglement(capturerReglement);
//		result.setMouvementerCrdAvoir(mouvementerCrdAvoir);
		
		creation.setMouvementerCrdAvoir(mouvementerCrdAvoir);
		RetourGenerationDoc retourDoc=null; 			
		if(result.getListeDocumentSrc().size()!=0){
			creation.setParam(result);
			retourDoc=(creation.creationDocument(!result.isGenerationModele()));
			if(retourDoc.isRetour()){
				listeDocCrees.add(retourDoc.getDoc());
				for (IDocumentTiers doc : result.getListeDocumentSrc()) {
			
					if(doc instanceof TaAvisEcheance) {
						doc=daoAvisEcheance.findById(doc.getIdDocument());
						List<TaRDocumentDTO> listeRdoc = dejaGenereDocument(doc);
						for (TaRDocumentDTO rdoc : listeRdoc) {
							//regardé type 
							if(rdoc.getTypeDocumentDest() != null && rdoc.getTypeDocumentDest().equals(TaFacture.TYPE_DOC)) {
								TaFacture facture;
								
								facture = daoFacture.findByCode(rdoc.getCodeDocumentDest());
								//daoFacture.rechercheDocument(rdoc.getCodeDocumentDest(), rdoc.getCodeDocumentDest());
								
								if(facture != null) {
									//Condition ci-dessous est equivalente a l'utilisation de contains,
									//mais contains ne marche pas ici(surement un problème avec la redéfinition de equals ou hashcode du taFacture)
									if(listeDocCrees.stream().filter(o -> o.getCodeDocument().equals(facture.getCodeDocument())).findFirst().isPresent()){
										System.out.println("L'avis d'échéance "+doc.getCodeDocument()+" à généré la nouvelle facture "+facture.getCodeDocument()+" ");
										System.out.println("Donc si les abonnements liés a cet avis d'échéance sont désactivé,");
										System.out.println("Il faut aller les réactiver");
										
										//aller chercher toute les lignes d'avis d'échéances
										//pour chacune de ces lignes, aller chercher la ligne d'échéance correspondante
										//aller chercher chaque taSubscription correspondant a ces lignes d'échéances
										//aller chercher chaque taAbonnement correspondant a ces taSubscriptions et,
										//si cet abo n'est pas deja actif, on le réactive
										//A AMELIORER avec l'utilisation des DTO ou par l'utilisation d'une seule requete qui réactive l'abo directement en fonction des lignes d'avis
										for (TaLAvisEcheance ligneAvis : ((TaAvisEcheance) doc).getLignes()) {
											TaLEcheance ech =  taLEcheanceService.findByIdLAvisEcheance(ligneAvis.getIdLDocument());
//											TaStripeSubscription sub = taStripeSubscriptionService.findByIdLEcheance(ech.getIdLEcheance());
//											TaAbonnement abo = taAbonnementService.findByIdSubscription(sub.getIdStripeSubscription());
											TaAbonnement abo = taAbonnementService.findByIdLEcheance(ech.getIdLEcheance());
											//TaAbonnement abo = taAbonnementService.findByIdSubscription(sub.getIdStripeSubscription());
											if(abo.getSuspension()) {
												abo.setSuspension(false);
												taAbonnementService.merge(abo);
												System.out.println("l'abonnement "+abo.getCodeDocument()+" vient d'être ré-activé car il vient d'être réglé par la facture "+facture.getCodeDocument());
											}
										}
									}
									
								}
							}
							
							
						}
						
					}
					
					
					
				}
				if(retourDoc.isRetour() && retourDoc.getDoc()!=null) {
					if(retourDoc.getDoc() instanceof TaBoncdeAchat) {
						retourDoc.setDoc(daoBoncdeAchat.findByIDFetch(retourDoc.getDoc().getIdDocument()));
					}
					if(retourDoc.getDoc() instanceof TaFacture) {
						retourDoc.setDoc(daoFacture.findByIDFetch(retourDoc.getDoc().getIdDocument()));
					}
					if(retourDoc.getDoc() instanceof TaDevis) {
						retourDoc.setDoc(daoDevis.findByIDFetch(retourDoc.getDoc().getIdDocument()));
					}
					if(retourDoc.getDoc() instanceof TaPanier) {
						retourDoc.setDoc(daoPanier.findByIDFetch(retourDoc.getDoc().getIdDocument()));
					}
					if(retourDoc.getDoc() instanceof TaAvisEcheance) {
						retourDoc.setDoc(daoAvisEcheance.findByIDFetch(retourDoc.getDoc().getIdDocument()));
					}
					if(retourDoc.getDoc() instanceof TaPanier) {
						retourDoc.setDoc(daoPanier.findByIDFetch(retourDoc.getDoc().getIdDocument()));
					}
					if(retourDoc.getDoc() instanceof TaAvoir) {
						retourDoc.setDoc(daoAvoir.findByIDFetch(retourDoc.getDoc().getIdDocument()));
					}
					if(retourDoc.getDoc() instanceof TaAcompte) {
						retourDoc.setDoc(daoAcompte.findByIDFetch(retourDoc.getDoc().getIdDocument()));
					}
					if(retourDoc.getDoc() instanceof TaApporteur) {
						retourDoc.setDoc(daoApporteur.findByIDFetch(retourDoc.getDoc().getIdDocument()));
					}
					if(retourDoc.getDoc() instanceof TaAbonnement) {
						retourDoc.setDoc(taAbonnementService.findByIDFetch(retourDoc.getDoc().getIdDocument()));
					}
					if(retourDoc.getDoc() instanceof TaBonReception) {
						retourDoc.setDoc(daoBonReception.findByIDFetch(retourDoc.getDoc().getIdDocument()));
					}
					if(retourDoc.getDoc() instanceof TaBoncde) {
						retourDoc.setDoc(daoBoncde.findByIDFetch(retourDoc.getDoc().getIdDocument()));
					}
					if(retourDoc.getDoc() instanceof TaBonliv) {
						retourDoc.setDoc(daoBonLiv.findByIDFetch(retourDoc.getDoc().getIdDocument()));
					}
					if(retourDoc.getDoc() instanceof TaPrelevement) {
						retourDoc.setDoc(daoPrelevement.findByIDFetch(retourDoc.getDoc().getIdDocument()));
					}
					if(retourDoc.getDoc() instanceof TaPreparation) {
						retourDoc.setDoc(daoPreparation.findByIDFetch(retourDoc.getDoc().getIdDocument()));
					}
					if(retourDoc.getDoc() instanceof TaProforma) {
						retourDoc.setDoc(daoProforma.findByIDFetch(retourDoc.getDoc().getIdDocument()));
					}
					if(retourDoc.getDoc() instanceof TaTicketDeCaisse) {
						retourDoc.setDoc(daoTicketDeCaisse.findByIDFetch(retourDoc.getDoc().getIdDocument()));
					}					
				}
			}else if(retourDoc.getMessage()!=null && !retourDoc.getMessage().isEmpty()){
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Création document", retourDoc.getMessage()));
				
			}
		}
			

			if(listeDocCrees.size()>0){
				LgrTab typeDocPresent = LgrTab.getInstance();
				String tab = typeDocPresent.getTabDocCorrespondance().get(selectedType);
				retourDoc.setTypeTabCSS(tab);
				retourDoc.setOuvrirDoc(ouvrir);
//				FacesContext context = FacesContext.getCurrentInstance();  
//				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Création document", "Le document '"+listeDocCrees.get(0).getCodeDocument()+"' à été généré correctement. Voulez-vous le visualiser ?"));
				{
					String valeurIdentifiant = listeDocCrees.get(0).getCodeDocument();
					actFermer(retourDoc);
				}
//				ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
//				Map<String, Object> sessionMap = externalContext.getSessionMap();
//				
//				
//				sessionMap.put("retourGenerationModele", listeDocCrees);
			}else			
				if(result.isMultiple())
					actFermer(result);
				else actFermer(retourDoc);
		
	}


	public String getSelectedType() {
		return selectedType;
	}


	public void setSelectedType(String selectedType) {
		this.selectedType = selectedType;
	}


	public ITaPreferencesServiceRemote getPreferencesService() {
		return PreferencesService;
	}


	public void setPreferencesService(ITaPreferencesServiceRemote preferencesService) {
		PreferencesService = preferencesService;
	}


	public String getTypeSrc() {
		return typeSrc;
	}


	public void setTypeSrc(String typeSrc) {
		this.typeSrc = typeSrc;
	}


	public String getTypeDest() {
		return typeDest;
	}


	public void setTypeDest(String typeDest) {
		this.typeDest = typeDest;
	}


	public IDocumentTiers getDocumentSrc() {
		return documentSrc;
	}


	public void setDocumentSrc(IDocumentTiers documentSrc) {
		this.documentSrc = documentSrc;
	}


	public List<SelectItem> getListeTypeDoc() {
		return ListeTypeDoc;
	}


	public void setListeTypeDoc(List<SelectItem> listeTypeDoc) {
		ListeTypeDoc = listeTypeDoc;
	}


	public String[] getSelectedTypeDoc() {
		return selectedTypeDoc;
	}


	public void setSelectedTypeDoc(String[] selectedTypeDoc) {
		this.selectedTypeDoc = selectedTypeDoc;
	}


	public String getNouveauLibelle() {
		return nouveauLibelle;
	}


	public void setNouveauLibelle(String nouveauLibelle) {
		this.nouveauLibelle = nouveauLibelle;
	}


	public LocalDate getDateDocument() {
		return dateDocument;
	}


	public void setDateDocument(LocalDate dateDocument) {
		this.dateDocument = dateDocument;
	}


	public Date getDateLivDocument() {
		return dateLivDocument;
	}


	public void setDateLivDocument(Date dateLivDocument) {
		this.dateLivDocument = dateLivDocument;
	}


	public String getCodeTiers() {
		return codeTiers;
	}


	public void setCodeTiers(String codeTiers) {
		this.codeTiers = codeTiers;
	}


	public String getOption() {
		return option;
	}


	public void setOption(String option) {
		this.option = option;
	}

	public String getTypeFacture() {
		return typeFacture;
	}

	public void setTypeFacture(String typeFacture) {
		this.typeFacture = typeFacture;
	}

	public String getTypeAvoir() {
		return typeAvoir;
	}

	public void setTypeAvoir(String typeAvoir) {
		this.typeAvoir = typeAvoir;
	}

	public String getTypeAcompte() {
		return typeAcompte;
	}

	public void setTypeAcompte(String typeAcompte) {
		this.typeAcompte = typeAcompte;
	}

	public String getTypeApporteur() {
		return typeApporteur;
	}

	public void setTypeApporteur(String typeApporteur) {
		this.typeApporteur = typeApporteur;
	}

	public String getTypeDevis() {
		return typeDevis;
	}

	public void setTypeDevis(String typeDevis) {
		this.typeDevis = typeDevis;
	}

	public String getTypeBonLiv() {
		return typeBonLiv;
	}

	public void setTypeBonLiv(String typeBonLiv) {
		this.typeBonLiv = typeBonLiv;
	}

	public String getTypeBoncde() {
		return typeBoncde;
	}

	public void setTypeBoncde(String typeBoncde) {
		this.typeBoncde = typeBoncde;
	}

	public String getTypeProforma() {
		return typeProforma;
	}

	public void setTypeProforma(String typeProforma) {
		this.typeProforma = typeProforma;
	}

	public String getTypePrelevement() {
		return typePrelevement;
	}

	public void setTypePrelevement(String typePrelevement) {
		this.typePrelevement = typePrelevement;
	}

	public String getTypeAvisEcheance() {
		return typeAvisEcheance;
	}

	public void setTypeAvisEcheance(String typeAvisEcheance) {
		this.typeAvisEcheance = typeAvisEcheance;
	}

	public String getIdDocumentSrc() {
		return idDocumentSrc;
	}

	public void setIdDocumentSrc(String idDocumentSrc) {
		this.idDocumentSrc = idDocumentSrc;
	}

	public ParamAfficheChoixGenerationDocument getResult() {
		return result;
	}

	public void setResult(ParamAfficheChoixGenerationDocument result) {
		this.result = result;
	}

	public String getTitreEcran() {
		return titreEcran;
	}

	public void setTitreEcran(String titreEcran) {
		this.titreEcran = titreEcran;
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

	public TaTiers getTiers() {
		return tiers;
	}

	public void setTiers(TaTiers tiers) {
		this.tiers = tiers;
	}
	

	public void autcompleteSelection(SelectEvent event) {
		Object value = event.getObject();
		
		String nomChamp =  (String) event.getComponent().getAttributes().get("nomChamp");

		if(value!=null) {
			if(value instanceof String && value.equals(Const.C_AUCUN))value="";	
			if(value instanceof TaTiers && ((TaTiers) value).getCodeTiers()!=null && ((TaTiers) value).getCodeTiers().equals(Const.C_AUCUN))value=null;			
			}
		
		if(tiers!=null)codeTiers=tiers.getCodeTiers();
		else codeTiers="";
	}
	
	public void ouvreDocument(ActionEvent e)  {
		if(listeDocCrees.size()>0){
			LgrTab typeDocPresent = LgrTab.getInstance();
			String tab = typeDocPresent.getTabDocCorrespondance().get(selectedType);
			{
				String valeurIdentifiant = listeDocCrees.get(0).getCodeDocument();
				ouvreDocument(valeurIdentifiant, tab);
			}
		}
	}
	
	public void ouvreDocument(String valeurIdentifiant,String  tabEcran) {
		if(valeurIdentifiant!=null ) {					
			ouvertureDocumentBean.setEvent(new LgrTabEvent());
			ouvertureDocumentBean.getEvent().setCodeObjet(valeurIdentifiant);
			ouvertureDocumentBean.getEvent().setData(listeDocCrees.get(0));
			ouvertureDocumentBean.getEvent().setCssLgrTab(tabEcran);
			ouvertureDocumentBean.getEvent().setAfficheDoc(false);/* A remettre à true dès que l'on pourra afficher le document à la suite d'un dialogue*/
			ouvertureDocumentBean.openDocument(null);
		}
	}

	public List<IDocumentTiers> getListeDocCrees() {
		return listeDocCrees;
	}

	public void setListeDocCrees(List<IDocumentTiers> listeDocCrees) {
		this.listeDocCrees = listeDocCrees;
	}

	public AutorisationBean getAutorisation() {
		return autorisation;
	}

	public void setAutorisation(AutorisationBean autorisation) {
		this.autorisation = autorisation;
	}

	public OuvertureDocumentBean getOuvertureDocumentBean() {
		return ouvertureDocumentBean;
	}

	public void setOuvertureDocumentBean(OuvertureDocumentBean ouvertureDocumentBean) {
		this.ouvertureDocumentBean = ouvertureDocumentBean;
	}

	public boolean isOuvrir() {
		return ouvrir;
	}

	public void setOuvrir(boolean ouvrir) {
		this.ouvrir = ouvrir;
	}

	public String getTypeTicketDeCaisse() {
		return typeTicketDeCaisse;
	}

	public void setTypeTicketDeCaisse(String typeTicketDeCaisse) {
		this.typeTicketDeCaisse = typeTicketDeCaisse;
	}
	
	
	public void controleDate(SelectEvent event) {
		String nomChamp =  (String)event.getComponent().getAttributes().get("nomChamp");
		validateUIField(nomChamp, event.getObject());
		
	}
	public void controleDate(AjaxBehaviorEvent event) {
		String nomChamp =  (String)event.getComponent().getAttributes().get("nomChamp");
		validateUIField(nomChamp,  ((UIOutput) event.getSource()).getValue());
	}
	
	
	public boolean validateUIField(String nomChamp,Object value) {

		boolean changement=false;

		try {				
			if(nomChamp.equals(Const.C_DATE_DOCUMENT)) {				
				dateDocument= LibDate.dateToLocalDate(dateDansPeriode(LibDate.localDateToDate((LocalDate)value),nomChamp)) ;
			}
			return false;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	public Date dateDansPeriode(Date newValue,String champ) throws Exception{
		if(champ.equals(Const.C_DATE_DOCUMENT)){
			newValue=taInfoEntrepriseService.dateDansExercice(newValue);
			
//			if(newValue!=null){
//				selectedDocumentDTO.setDateDocument(newValue);
//				calculDateEcheance(true);
//
//				if((selectedDocumentDTO.getDateLivDocument()!=null && 
//						selectedDocumentDTO.getDateLivDocument().before(newValue))||
//					selectedDocumentDTO.getDateLivDocument()==null ){
//					masterEntity.setDateLivDocument(newValue);
//					selectedDocumentDTO.setDateLivDocument(newValue);
//					}
//			}
		}

		return newValue;
	}

	public LocalDate getDateMin() {
		return dateMin;
	}

	public LocalDate getDateMax() {
		return dateMax;
	}

	public String getTypeBoncdeAchat() {
		return typeBoncdeAchat;
	}

	public void setTypeBoncdeAchat(String typeBoncdeAchat) {
		this.typeBoncdeAchat = typeBoncdeAchat;
	}

	public String getTypeBonReception() {
		return typeBonReception;
	}

	public void setTypeBonReception(String typeBonReception) {
		this.typeBonReception = typeBonReception;
	}

	public boolean isAccepterMultiType() {
		return accepterMultiType;
	}

	public void setAccepterMultiType(boolean accepterMultiType) {
		this.accepterMultiType = accepterMultiType;
	}

public void choixTypeDest() {
	TaPreferences p=null;
	Boolean b=null;
	
//	if(selectedType.equals(TaAcompte.TYPE_DOC)) {
		p = taPreferencesService.findByGoupeAndCle(selectedType, ITaPreferencesServiceRemote.OPTION_REPRISE_INFOS_DOC_GENERATION);
//	}

		if(p!=null && p.getValeur() != null) {
			option = p.getValeur();
		}

		mouvementerCrdAvoir=false;
		if(selectedType.equals(TaAvoir.TYPE_DOC)) {
			TaPreferences crd = taPreferencesService.findByGoupeAndCle(ITaPreferencesServiceRemote.GROUPE_PREFERENCES_CRD, ITaPreferencesServiceRemote.STOCK_MOUVEMENTER_CRD_POUR_LIGNE_AVOIR);
			if(crd!=null && LibConversion.StringToBoolean(crd.getValeur()) != null) {
				mouvementerCrdAvoir =LibConversion.StringToBoolean(crd.getValeur());
			}
		}
}

public TaStripePaymentIntent getPaiementParCarteEnAttente() {
	return paiementParCarteEnAttente;
}

public void setPaiementParCarteEnAttente(TaStripePaymentIntent paiementParCarteEnAttente) {
	this.paiementParCarteEnAttente = paiementParCarteEnAttente;
}

public boolean isCapturerReglement() {
	return capturerReglement;
}

public void setCapturerReglement(boolean capturerReglement) {
	this.capturerReglement = capturerReglement;
}

public boolean getMouvementerCrdAvoir() {
	return mouvementerCrdAvoir;
}

public void setMouvementerCrdAvoir(boolean mouvementerCrdAvoir) {
	this.mouvementerCrdAvoir = mouvementerCrdAvoir;
}

}
