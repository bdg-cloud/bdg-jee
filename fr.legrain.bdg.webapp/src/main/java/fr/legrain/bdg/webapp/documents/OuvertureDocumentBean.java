package fr.legrain.bdg.webapp.documents;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.enterprise.context.Dependent;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;

import fr.legrain.article.model.TaArticle;
import fr.legrain.article.model.TaFabrication;
import fr.legrain.article.model.TaLot;
import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaFabricationServiceRemote;
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
import fr.legrain.bdg.documents.service.remote.ITaPanierServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaPrelevementServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaPreparationServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaProformaServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaReglementServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaRemiseServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaTicketDeCaisseServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.bdg.webapp.LgrTabEvent;
import fr.legrain.bdg.webapp.app.LgrTab;
import fr.legrain.bdg.webapp.articles.ArticleController;
import fr.legrain.bdg.webapp.articles.GestionLotController;
import fr.legrain.bdg.webapp.reglementMultiple.ReglementController;
import fr.legrain.bdg.webapp.reglementMultiple.ReglementMultipleFacController;
import fr.legrain.bdg.webapp.remise.RemiseController;
import fr.legrain.bdg.webapp.solstyce.FabricationController;
import fr.legrain.bdg.webapp.tiers.TiersController;
import fr.legrain.document.dto.IDocumentTiers;
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
import fr.legrain.document.model.TaPanier;
import fr.legrain.document.model.TaPrelevement;
import fr.legrain.document.model.TaPreparation;
import fr.legrain.document.model.TaProforma;
import fr.legrain.document.model.TaReglement;
import fr.legrain.document.model.TaRemise;
import fr.legrain.document.model.TaTicketDeCaisse;
import fr.legrain.tiers.model.TaTiers;

@Named
@Dependent
public class OuvertureDocumentBean implements Serializable {

	private static final long serialVersionUID = 12814265716218582L;
	
//
//	@Inject @Named(value="fabricationController}")
//	private FabricationController fabricationController;

	@Inject @Named(value="articleController")
	private ArticleController articleController;	
	
	@Inject @Named(value="tiersController")
	private TiersController tiersController;	
	
	@Inject @Named(value="factureController")
	private FactureController factureController;	
	
	@Inject @Named(value="devisController")
	private DevisController devisController;
	
	@Inject @Named(value="avisEcheanceController")
	private AvisEcheanceController avisEcheanceController;
	
	
	@Inject @Named(value="avoirController")
	private AvoirController avoirController;	
	
	
	@Inject @Named(value="bonLivraisonController")
	private BonLivraisonController bonLivraisonController;	
	
	
	@Inject @Named(value="bonCdeController")
	private BonCdeController bonCdeController;	
	
	
	@Inject @Named(value="bonCdeAchatController")
	private BonCdeAchatController bonCdeAchatController;	
	
	@Inject @Named(value="apporteurController")
	private ApporteurController apporteurController;	
	
	
	@Inject @Named(value="prelevementController")
	private PrelevementController prelevementController;	
	
	
	@Inject @Named(value="proformaController")
	private ProformaController proformaController;	
	
	
	@Inject @Named(value="acompteController")
	private AcompteController acompteController;	

	
	@Inject @Named(value="gestionLotController")
	private GestionLotController gestionLotController;
	
	@Inject @Named(value="remiseController")
	private RemiseController remiseController;
	
	@Inject @Named(value="reglementController")
	private ReglementController reglementController;
	
	@Inject @Named(value="reglementMultipleFacController")
	private ReglementMultipleFacController reglementMultipleFacController;
	
	@Inject @Named(value="bonReceptionController")
	private BonReceptionController bonReceptionController;	
	
	@Inject @Named(value="fabricationController")
	private FabricationController fabricationController;	
	
	@Inject @Named(value="abonnementController")
	private AbonnementController abonnementController;	
	
	@Inject @Named(value="bonPreparationController")
	private BonPreparationController bonPreparationController;	
	
	@Inject @Named(value="panierController")
	private PanierController panierController;
	
	@EJB private ITaArticleServiceRemote taArticleService;
	@EJB private ITaTiersServiceRemote taTiersService;
	@EJB private ITaFactureServiceRemote taFactureService;
	@EJB private ITaApporteurServiceRemote taApporteurService;
	@EJB private ITaTicketDeCaisseServiceRemote taTicketDeCaisseService;
	@EJB private ITaAvoirServiceRemote taAvoirService;
	@EJB private ITaDevisServiceRemote taDevisService;
	@EJB private ITaBonlivServiceRemote taBonlivService;
	@EJB private ITaBoncdeServiceRemote taBoncdeService;
	@EJB private ITaBoncdeAchatServiceRemote taBoncdeAchatService;
	@EJB private ITaAcompteServiceRemote taAcompteService;
	@EJB private ITaProformaServiceRemote taProformaService;
	@EJB private ITaAvisEcheanceServiceRemote taAvisEcheanceService;
	@EJB private ITaPrelevementServiceRemote taPrelevementService;	
	@EJB private ITaReglementServiceRemote taReglementService;	
	@EJB private ITaRemiseServiceRemote taRemiseService;	
	
	@EJB private ITaBonReceptionServiceRemote taBonReceptionService;
	@EJB private ITaFabricationServiceRemote taFabricationService;
	@EJB private ITaAbonnementServiceRemote taAbonnementService;
	@EJB private ITaPreparationServiceRemote taPreparationService;
	@EJB private ITaPanierServiceRemote taPanierService;
	
	private LgrTabEvent event = new LgrTabEvent();
	
	@PostConstruct
	public void init() {
		
	}

	
	
//	public void modelCalendar(Date start, Date end) {
//		if(autorisation.autoriseMenu(AutorisationBean.ID_MODULE_TRACABILITE)) {
//			List<TaFabricationDTO> listeFab = taFabricationService.selectAllDTO();
//			LgrTabScheduleEvent evt = null;
//			for (TaFabricationDTO taFabricationDTO : listeFab) {
//				evt = new LgrTabScheduleEvent(taFabricationDTO.getCodeDocument(), taFabricationDTO.getDateDebR(), taFabricationDTO.getDateDebR());
//				evt.setCssLgrTab(LgrTab.CSS_CLASS_TAB_FABRICATION);
//				evt.setIdObjet(taFabricationDTO.getId());
//				evt.setCodeObjet(taFabricationDTO.getCodeDocument());
//				evt.setData(taFabricationDTO);
////				evt.setStyleClass("");
//				eventModel.addEvent(evt);
//			}
//		}
//	} 
	public void onEventSelect(SelectEvent selectEvent) {
        event = (LgrTabEvent) selectEvent.getObject();
    }

	public void openDocument(ActionEvent e) {
		if(event.getCssLgrTab().equals(LgrTab.CSS_CLASS_TAB_ARTICLE)) {
			articleController.setTaArticle(((TaArticle) event.getData()));
			articleController.rempliDTO();
			if(event.isAfficheDoc())articleController.onRowSelect(null);
		}
		if(event.getCssLgrTab().equals(LgrTab.CSS_CLASS_TAB_TIERS)) {
			tiersController.setTaTiers(((TaTiers) event.getData()));
			tiersController.rempliDTO();
			if(event.isAfficheDoc())tiersController.onRowSelect(null);
		}
		if(event.getCssLgrTab().equals(LgrTab.CSS_CLASS_TAB_FACTURE)) {
			factureController.setMasterEntity((TaFacture) event.getData());
			factureController.rempliDTOOuvertureDocumentExterne();
			if(event.isAfficheDoc())factureController.onRowSelect(null);

		}		
		if(event.getCssLgrTab().equals(LgrTab.CSS_CLASS_TAB_DEVIS)) {
			devisController.setMasterEntity((TaDevis) event.getData());
			devisController.rempliDTOOuvertureDocumentExterne();
			if(event.isAfficheDoc())devisController.onRowSelect(null);
		}		
		if(event.getCssLgrTab().equals(LgrTab.CSS_CLASS_TAB_LIVRAISON)) {
			bonLivraisonController.setMasterEntity((TaBonliv) event.getData());
			bonLivraisonController.rempliDTOOuvertureDocumentExterne();
			if(event.isAfficheDoc())bonLivraisonController.onRowSelect(null);
		}
		if(event.getCssLgrTab().equals(LgrTab.CSS_CLASS_TAB_BONCDE)) {
			bonCdeController.setMasterEntity((TaBoncde) event.getData());
			bonCdeController.rempliDTOOuvertureDocumentExterne();
			if(event.isAfficheDoc())bonCdeController.onRowSelect(null);
		}
		if(event.getCssLgrTab().equals(LgrTab.CSS_CLASS_TAB_BONCDE_ACHAT)) {
			bonCdeAchatController.setMasterEntity((TaBoncdeAchat) event.getData());
			bonCdeAchatController.rempliDTOOuvertureDocumentExterne();
			if(event.isAfficheDoc())bonCdeAchatController.onRowSelect(null);
		}		
		if(event.getCssLgrTab().equals(LgrTab.CSS_CLASS_TAB_AVOIR)) {
			avoirController.setMasterEntity((TaAvoir) event.getData());
			avoirController.rempliDTOOuvertureDocumentExterne();
			if(event.isAfficheDoc())avoirController.onRowSelect(null);
		}
		if(event.getCssLgrTab().equals(LgrTab.CSS_CLASS_TAB_APPORTEUR)) {
			apporteurController.setMasterEntity((TaApporteur) event.getData());
			apporteurController.rempliDTOOuvertureDocumentExterne();
			if(event.isAfficheDoc())apporteurController.onRowSelect(null);
		}
		if(event.getCssLgrTab().equals(LgrTab.CSS_CLASS_TAB_PRELEVEMENT)) {
			prelevementController.setMasterEntity((TaPrelevement) event.getData());
			prelevementController.rempliDTOOuvertureDocumentExterne();
			if(event.isAfficheDoc())prelevementController.onRowSelect(null);
		}
		if(event.getCssLgrTab().equals(LgrTab.CSS_CLASS_TAB_PROFORMA)) {
			proformaController.setMasterEntity((TaProforma) event.getData());
			proformaController.rempliDTOOuvertureDocumentExterne();
			if(event.isAfficheDoc())proformaController.onRowSelect(null);
		}
		if(event.getCssLgrTab().equals(LgrTab.CSS_CLASS_TAB_ACOMPTE)) {
			acompteController.setMasterEntity((TaAcompte) event.getData());
			acompteController.rempliDTOOuvertureDocumentExterne();
			if(event.isAfficheDoc())acompteController.onRowSelect(null);
		}
		if(event.getCssLgrTab().equals(LgrTab.CSS_CLASS_TAB_LOTS)) {
			gestionLotController.rempliDTO((TaLot) event.getData());
			if(event.isAfficheDoc())gestionLotController.onRowSelect(null);
		}
		if(event.getCssLgrTab().equals(LgrTab.CSS_CLASS_TAB_AVIS_ECHEANCE)) {
			avisEcheanceController.setMasterEntity((TaAvisEcheance) event.getData());
			avisEcheanceController.rempliDTOOuvertureDocumentExterne();
			if(event.isAfficheDoc())avisEcheanceController.onRowSelect(null);
		}
		if(event.getCssLgrTab().equals(LgrTab.CSS_CLASS_TAB_REMISE)) {
			remiseController.setTaRemise((TaRemise) event.getData());
			remiseController.rempliDTO();
			if(event.isAfficheDoc())remiseController.onRowSelect(null);
		}
		if(event.getCssLgrTab().equals(LgrTab.CSS_CLASS_TAB_REGLEMENT)) {
			reglementController.setTaReglement((TaReglement) event.getData());
			reglementController.rempliDTO();
			if(event.isAfficheDoc())reglementController.onRowSelect(null);
		}	
		if(event.getCssLgrTab().equals(LgrTab.CSS_CLASS_TAB_GESTION_REGLEMENT)) {
			reglementMultipleFacController.setMasterEntity((TaFacture) event.getData());
			reglementMultipleFacController.rempliDTO();
			
			if(event.isAfficheDoc())reglementMultipleFacController.refresh();
		}
		if(event.getCssLgrTab().equals(LgrTab.CSS_CLASS_TAB_RECEPTION)) {
			bonReceptionController.setMasterEntity((TaBonReception) event.getData());
			bonReceptionController.rempliDTOOuvertureDocumentExterne();
			if(event.isAfficheDoc())bonReceptionController.onRowSelect(null);
		}
		if(event.getCssLgrTab().equals(LgrTab.CSS_CLASS_TAB_PREPARATION)) {
			bonPreparationController.setMasterEntity((TaPreparation) event.getData());
			bonPreparationController.rempliDTOOuvertureDocumentExterne();
			if(event.isAfficheDoc())bonPreparationController.onRowSelect(null);
		}
		if(event.getCssLgrTab().equals(LgrTab.CSS_CLASS_TAB_FABRICATION)) {
			fabricationController.setMasterEntity((TaFabrication) event.getData());
			fabricationController.rempliDTO();
			if(event.isAfficheDoc())fabricationController.onRowSelect(null);
		}
		if(event.getCssLgrTab().equals(LgrTab.CSS_CLASS_TAB_ABONNEMENT)) {
			abonnementController.setMasterEntity((TaAbonnement) event.getData());
			abonnementController.rempliDTOOuvertureDocumentExterne();
			if(event.isAfficheDoc())abonnementController.onRowSelect(null);
		}
		if(event.getCssLgrTab().equals(LgrTab.CSS_CLASS_TAB_PANIER)) {
			panierController.setMasterEntity((TaPanier) event.getData());
			panierController.rempliDTOOuvertureDocumentExterne();
			if(event.isAfficheDoc())panierController.onRowSelect(null);
		}		
	}


	public LgrTabEvent getEvent() {
		return event;
	}


	public void setEvent(LgrTabEvent event) {
		this.event = event;
	}

//
//	public FabricationController getFabricationController() {
//		return fabricationController;
//	}
//
//
//	public void setFabricationController(FabricationController fabricationController) {
//		this.fabricationController = fabricationController;
//	}


	public FactureController getFactureController() {
		return factureController;
	}


	public void setFactureController(FactureController factureController) {
		this.factureController = factureController;
	}


	public DevisController getDevisController() {
		return devisController;
	}


	public void setDevisController(DevisController devisController) {
		this.devisController = devisController;
	}


	public AvoirController getAvoirController() {
		return avoirController;
	}


	public void setAvoirController(AvoirController avoirController) {
		this.avoirController = avoirController;
	}


	public BonLivraisonController getBonLivraisonController() {
		return bonLivraisonController;
	}


	public void setBonLivraisonController(BonLivraisonController bonLivraisonController) {
		this.bonLivraisonController = bonLivraisonController;
	}


	public BonCdeController getBonCdeController() {
		return bonCdeController;
	}


	public void setBonCdeController(BonCdeController bonCdeController) {
		this.bonCdeController = bonCdeController;
	}


	public ApporteurController getApporteurController() {
		return apporteurController;
	}


	public void setApporteurController(ApporteurController apporteurController) {
		this.apporteurController = apporteurController;
	}


	public PrelevementController getPrelevementController() {
		return prelevementController;
	}


	public void setPrelevementController(PrelevementController prelevementController) {
		this.prelevementController = prelevementController;
	}


	public ProformaController getProformaController() {
		return proformaController;
	}


	public void setProformaController(ProformaController proformaController) {
		this.proformaController = proformaController;
	}


	public AcompteController getAcompteController() {
		return acompteController;
	}


	public void setAcompteController(AcompteController acompteController) {
		this.acompteController = acompteController;
	}


	public ArticleController getArticleController() {
		return articleController;
	}


	public void setArticleController(ArticleController articleController) {
		this.articleController = articleController;
	}


	public TiersController getTiersController() {
		return tiersController;
	}


	public void setTiersController(TiersController tiersController) {
		this.tiersController = tiersController;
	}


	public GestionLotController getGestionLotController() {
		return gestionLotController;
	}


	public void setGestionLotController(GestionLotController gestionLotController) {
		this.gestionLotController = gestionLotController;
	}


	public void detailDocument(IDocumentTiers detailDocument){
		String tabEcran="";
		LgrTab lgrTab=LgrTab.getInstance();
		if(detailDocument!=null){
			tabEcran=lgrTab.getTabDocCorrespondance().get(detailDocument.getTypeDocument());
			if(tabEcran!=null && !tabEcran.isEmpty()){
				setEvent(new LgrTabEvent());
				getEvent().setCodeObjet(detailDocument.getCodeDocument());
				getEvent().setData(detailDocument);
				getEvent().setCssLgrTab(tabEcran);
				getEvent().setAfficheDoc(true);
				openDocument(null);
			}
		}
	}
	
	public void detailAbonnement(TaAbonnement detailAbonnement){
		String tabEcran="";
		LgrTab lgrTab=LgrTab.getInstance();
		if(detailAbonnement!=null){
			tabEcran=LgrTab.CSS_CLASS_TAB_ABONNEMENT;
			if(tabEcran!=null && !tabEcran.isEmpty()){
				setEvent(new LgrTabEvent());
				getEvent().setCodeObjet(detailAbonnement.getCodeDocument());
				getEvent().setData(detailAbonnement);
				getEvent().setCssLgrTab(tabEcran);
				getEvent().setAfficheDoc(true);
				openDocument(null);
			}
		}
	}

		public void detailTiers(TaTiers detailTiers){
			String tabEcran="";
			if(detailTiers!=null){
				tabEcran=LgrTab.CSS_CLASS_TAB_TIERS;
				if(tabEcran!=null && !tabEcran.isEmpty()){
					setEvent(new LgrTabEvent());
					getEvent().setCodeObjet(detailTiers.getCodeTiers());
					getEvent().setData(detailTiers);
					getEvent().setCssLgrTab(tabEcran);
					getEvent().setAfficheDoc(true);
					openDocument(null);
				}
			}
		}

		public void detailArticle(TaArticle detailArticle){
			String tabEcran="";
			if(detailArticle!=null){
				tabEcran=LgrTab.CSS_CLASS_TAB_ARTICLE;
				if(tabEcran!=null && !tabEcran.isEmpty()){
					setEvent(new LgrTabEvent());
					getEvent().setCodeObjet(detailArticle.getCodeArticle());
					getEvent().setData(detailArticle);
					getEvent().setCssLgrTab(tabEcran);
					getEvent().setAfficheDoc(true);
					openDocument(null);
				}
			}
		}
		
		public void detailLot(TaLot detailLot){
			String tabEcran="";
			if(detailLot!=null){
				tabEcran=LgrTab.CSS_CLASS_TAB_LOTS;
				if(tabEcran!=null && !tabEcran.isEmpty()){
					setEvent(new LgrTabEvent());
					getEvent().setCodeObjet(detailLot.getNumLot());
					getEvent().setData(detailLot);
					getEvent().setCssLgrTab(tabEcran);
					getEvent().setAfficheDoc(true);
					openDocument(null);
				}
			}
		}
		
		
		public void detailReglement(TaReglement detailReglement){
			String tabEcran="";
			if(detailReglement!=null){
				tabEcran=LgrTab.CSS_CLASS_TAB_REGLEMENT;
				if(tabEcran!=null && !tabEcran.isEmpty()){
					setEvent(new LgrTabEvent());
					getEvent().setCodeObjet(detailReglement.getCodeDocument());
					getEvent().setData(detailReglement);
					getEvent().setCssLgrTab(tabEcran);
					getEvent().setAfficheDoc(true);
					openDocument(null);
				}
			}
		}
		
		public void detailReglementMultiple(TaFacture detailReglementFacture){
			String tabEcran="";
			if(detailReglementFacture!=null){
				tabEcran=LgrTab.CSS_CLASS_TAB_GESTION_REGLEMENT;
				if(tabEcran!=null && !tabEcran.isEmpty()){
					setEvent(new LgrTabEvent());
					getEvent().setCodeObjet(detailReglementFacture.getCodeDocument());
					getEvent().setData(detailReglementFacture);
					getEvent().setCssLgrTab(tabEcran);
					getEvent().setAfficheDoc(true);
					openDocument(null);
				}
			}
		}
		
		public void detailRemise(TaRemise detailRemise){
			String tabEcran="";
			if(detailRemise!=null){
				tabEcran=LgrTab.CSS_CLASS_TAB_REMISE;
				if(tabEcran!=null && !tabEcran.isEmpty()){
					setEvent(new LgrTabEvent());
					getEvent().setCodeObjet(detailRemise.getCodeDocument());
					getEvent().setData(detailRemise);
					getEvent().setCssLgrTab(tabEcran);
					getEvent().setAfficheDoc(true);
					openDocument(null);
				}
			}
		}

		public RemiseController getRemiseController() {
			return remiseController;
		}


		public void setRemiseController(RemiseController remiseController) {
			this.remiseController = remiseController;
		}


		public ReglementController getReglementController() {
			return reglementController;
		}


		public void setReglementController(ReglementController reglementController) {
			this.reglementController = reglementController;
		}


		public ReglementMultipleFacController getReglementMultipleFacController() {
			return reglementMultipleFacController;
		}


		public void setReglementMultipleFacController(ReglementMultipleFacController reglementMultipleFacController) {
			this.reglementMultipleFacController = reglementMultipleFacController;
		}
		
		
		
		public IDocumentTiers recupCodeDocument(String code,String type){
			FacesContext context = FacesContext.getCurrentInstance();
			if(code!=null && !code.isEmpty())
				return documentValide(code,type);
			return null;
		}
		
		public IDocumentTiers documentValide(String code,String type){
			try {	
				IDocumentTiers document = null;
				if(type.equals(TaFacture.TYPE_DOC)) {
					document=taFactureService.findByCodeFetch(code);
				}
				if(type.equals(TaApporteur.TYPE_DOC)) {
					document=taApporteurService.findByCodeFetch(code);
				}
				if(type.equals(TaAvoir.TYPE_DOC)) {
					document=taAvoirService.findByCodeFetch(code);
				}
				if(type.equals(TaAvisEcheance.TYPE_DOC)) {
					document=taAvisEcheanceService.findByCodeFetch(code);
				}

				if(type.equals(TaDevis.TYPE_DOC)) {
					document=taDevisService.findByCodeFetch(code);
				}
				if(type.equals(TaBonliv.TYPE_DOC)) {
					document=taBonlivService.findByCodeFetch(code);
				}
				if(type.equals(TaBoncde.TYPE_DOC)) {
					document=taBoncdeService.findByCodeFetch(code);
				}
				if(type.equals(TaBoncdeAchat.TYPE_DOC)) {
					document=taBoncdeAchatService.findByCodeFetch(code);
				}
				if(type.equals(TaAcompte.TYPE_DOC)) {
					document=taAcompteService.findByCodeFetch(code);
				}				
				if(type.equals(TaProforma.TYPE_DOC)) {
					document=taProformaService.findByCodeFetch(code);
				}
				if(type.equals(TaPrelevement.TYPE_DOC)) {
					document=taPrelevementService.findByCodeFetch(code);
				}
				if(type.equals(TaReglement.TYPE_DOC)) {
					document=taReglementService.findByCode(code);
				}
				if(type.equals(TaRemise.TYPE_DOC)) {
					document=taRemiseService.findByCode(code);
				}
				if(type.equals(TaBonReception.TYPE_DOC)) {
					document=taBonReceptionService.findByCodeFetch(code);
				}
				if(type.equals(TaFabrication.TYPE_DOC)) {
					document=taFabricationService.findByCodeFetch(code);
				}
				if(type.equals(TaPreparation.TYPE_DOC)) {
					document=taPreparationService.findByCodeFetch(code);
				}
				if(type.equals(TaTicketDeCaisse.TYPE_DOC)) {
					document=taTicketDeCaisseService.findByCodeFetch(code);
				}
				if(type.equals(TaAbonnement.TYPE_DOC)) {
					document=taAbonnementService.findByCodeFetch(code);
				}	
				if(type.equals(TaPanier.TYPE_DOC)) {
					document=taPanierService.findByCodeFetch(code);
				}				
				return document;
			} catch (FinderException e) {
				return null;
			}
		}
		
		public TaTiers recupCodetiers(String code){
			FacesContext context = FacesContext.getCurrentInstance();
			if(code!=null && !code.isEmpty())
				try {
					return taTiersService.findByCode(code);
				} catch (FinderException e) {
					context.addMessage(null, new FacesMessage("erreur", "code tiers vide")); 	
				}
			return null;
		}
		
		
		public TaArticle recupCodeArticle(String code){
			FacesContext context = FacesContext.getCurrentInstance();
			if(code!=null && !code.isEmpty())
				try {
					return taArticleService.findByCode(code);
				} catch (FinderException e) {
					context.addMessage(null, new FacesMessage("erreur", "code article vide")); 	
				}
			return null;
		}
		
		public TaAbonnement recupCodeAbonnement(String code){
			FacesContext context = FacesContext.getCurrentInstance();
			if(code!=null && !code.isEmpty())
				try {
					return taAbonnementService.findByCode(code);
				} catch (FinderException e) {
					context.addMessage(null, new FacesMessage("erreur", "code abonnement vide")); 	
				}
			return null;
		}
		
		public String getTypeDocumentAbonnement() {
			return TaAbonnement.TYPE_DOC;
		}
		public String getTypeDocumentFacture() {
			return TaFacture.TYPE_DOC;
		}
		public String getTypeDocumentDevis() {
			return TaDevis.TYPE_DOC;
		}
		public String getTypeDocumentAcompte() {
			return TaAcompte.TYPE_DOC;
		}
		public String getTypeDocumentAvoir() {
			return TaAvoir.TYPE_DOC;
		}
		public String getTypeDocumentApporteur() {
			return TaApporteur.TYPE_DOC;
		}
		public String getTypeDocumentBonLiv() {
			return TaBonliv.TYPE_DOC;
		}
		public String getTypeDocumentBoncde() {
			return TaBoncde.TYPE_DOC;
		}
		public String getTypeDocumentBoncdeAchat() {
			return TaBoncdeAchat.TYPE_DOC;
		}
		public String getTypeDocumentReglement() {
			return TaReglement.TYPE_DOC;
		}
		public String getTypeDocumentRemise() {
			return TaRemise.TYPE_DOC;
		}
		public String getTypeDocumentPrelevement() {
			return TaPrelevement.TYPE_DOC;
		}
		public String getTypeDocumentProforma() {
			return TaProforma.TYPE_DOC;
		}
		public String getTypeDocumentBonReception() {
			return TaBonReception.TYPE_DOC;
		}
		public String getTypeDocumentFabrication() {
			return TaFabrication.TYPE_DOC;
		}
		public String getTypeDocumentAvisEcheance() {
			return TaAvisEcheance.TYPE_DOC;
		}
		public String getTypeDocumentPreparation() {
			return TaPreparation.TYPE_DOC;
		}
		public String getTypeDocumentTicketDeCaisse() {
			return TaTicketDeCaisse.TYPE_DOC;
		}
		public String getTypeDocumentPanier() {
			return TaPanier.TYPE_DOC;
		}

		public AbonnementController getAbonnementController() {
			return abonnementController;
		}



		public void setAbonnementController(AbonnementController abonnementController) {
			this.abonnementController = abonnementController;
		}



		public BonPreparationController getBonPreparationController() {
			return bonPreparationController;
		}



		public void setBonPreparationController(BonPreparationController bonPreparationController) {
			this.bonPreparationController = bonPreparationController;
		}



		public PanierController getPanierController() {
			return panierController;
		}



		public void setPanierController(PanierController panierController) {
			this.panierController = panierController;
		}

		

}
