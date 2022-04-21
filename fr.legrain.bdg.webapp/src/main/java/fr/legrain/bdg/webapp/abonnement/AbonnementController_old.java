//package fr.legrain.bdg.webapp.abonnement;
//
//import java.io.File;
//import java.io.Serializable;
//import java.math.BigDecimal;
//import java.math.MathContext;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.LinkedHashMap;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//import javax.annotation.PostConstruct;
//import javax.ejb.EJB;
//import javax.ejb.FinderException;
//import javax.faces.application.FacesMessage;
//import javax.faces.component.UIComponent;
//import javax.faces.context.ExternalContext;
//import javax.faces.context.FacesContext;
//import javax.faces.event.ActionEvent;
//import javax.faces.event.AjaxBehaviorEvent;
//import javax.faces.validator.ValidatorException;
//import javax.faces.view.ViewScoped;
//import javax.inject.Inject;
//import javax.inject.Named;
//import javax.validation.ConstraintViolation;
//import javax.validation.Validation;
//import javax.validation.ValidatorFactory;
//
//import org.primefaces.component.datatable.DataTable;
//import org.primefaces.component.remotecommand.RemoteCommand;
//import org.primefaces.context.RequestContext;
//import org.primefaces.event.FlowEvent;
//import org.primefaces.event.RowEditEvent;
//import org.primefaces.event.SelectEvent;
//import org.primefaces.event.UnselectEvent;
//
//import fr.legrain.abonnement.stripe.dto.TaStripeSubscriptionDTO;
//import fr.legrain.abonnement.stripe.dto.TaStripeSubscriptionItemDTO;
//import fr.legrain.abonnement.stripe.model.TaStripeSubscription;
//import fr.legrain.abonnement.stripe.model.TaStripeSubscriptionItem;
//import fr.legrain.article.dto.ArticleLotEntrepotDTO;
//import fr.legrain.article.dto.TaArticleDTO;
//import fr.legrain.article.dto.TaEntrepotDTO;
//import fr.legrain.article.dto.TaLabelArticleDTO;
//import fr.legrain.article.dto.TaTFabricationDTO;
//import fr.legrain.article.dto.TaUniteDTO;
//import fr.legrain.article.model.TaArticle;
//import fr.legrain.article.model.TaCoefficientUnite;
//import fr.legrain.article.model.TaEntrepot;
//import fr.legrain.article.model.TaLFabricationMP;
//import fr.legrain.article.model.TaLFabricationPF;
//import fr.legrain.article.model.TaLModeleFabricationMP;
//import fr.legrain.article.model.TaLModeleFabricationPF;
//import fr.legrain.article.model.TaLRecette;
//import fr.legrain.article.model.TaLabelArticle;
//import fr.legrain.article.model.TaLot;
//import fr.legrain.article.model.TaModeleFabrication;
//import fr.legrain.article.model.TaRapportUnite;
//import fr.legrain.article.model.TaRecette;
//import fr.legrain.article.model.TaTFabrication;
//import fr.legrain.article.model.TaUnite;
//import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripePlanServiceRemote;
//import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeSubscriptionItemServiceRemote;
//import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeSubscriptionServiceRemote;
//import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
//import fr.legrain.bdg.article.service.remote.ITaCoefficientUniteServiceRemote;
//import fr.legrain.bdg.article.service.remote.ITaEntrepotServiceRemote;
//import fr.legrain.bdg.article.service.remote.ITaEtatStockServiceRemote;
//import fr.legrain.bdg.article.service.remote.ITaLabelArticleServiceRemote;
//import fr.legrain.bdg.article.service.remote.ITaLotServiceRemote;
//import fr.legrain.bdg.article.service.remote.ITaModeleFabricationServiceRemote;
//import fr.legrain.bdg.article.service.remote.ITaMouvementStockServiceRemote;
//import fr.legrain.bdg.article.service.remote.ITaRecetteServiceRemote;
//import fr.legrain.bdg.article.service.remote.ITaTFabricationServiceRemote;
//import fr.legrain.bdg.article.service.remote.ITaTypeMouvementServiceRemote;
//import fr.legrain.bdg.article.service.remote.ITaUniteServiceRemote;
//import fr.legrain.bdg.conformite.service.remote.ITaConformiteServiceRemote;
//import fr.legrain.bdg.conformite.service.remote.ITaResultatConformiteServiceRemote;
//import fr.legrain.bdg.conformite.service.remote.ITaStatusConformiteServiceRemote;
//import fr.legrain.bdg.controle.service.remote.ITaGenCodeExServiceRemote;
//import fr.legrain.bdg.documents.service.remote.ITaTLigneServiceRemote;
//import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
//import fr.legrain.bdg.dossier.service.remote.ITaPreferencesServiceRemote;
//import fr.legrain.bdg.droits.service.remote.ITaUtilisateurServiceRemote;
//import fr.legrain.bdg.lib.osgi.Const;
//import fr.legrain.bdg.model.mapping.LgrDozerMapper;
//import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
//import fr.legrain.bdg.webapp.ConstWeb;
//import fr.legrain.bdg.webapp.SessionListener;
//import fr.legrain.bdg.webapp.agenda.EvenementParam;
//import fr.legrain.bdg.webapp.app.AbstractController;
//import fr.legrain.bdg.webapp.app.EmailParam;
//import fr.legrain.bdg.webapp.app.EmailPieceJointeParam;
//import fr.legrain.bdg.webapp.app.EtiquetteCodeBarreBean;
//import fr.legrain.bdg.webapp.app.LeftBean;
//import fr.legrain.bdg.webapp.app.LgrTab;
//import fr.legrain.bdg.webapp.app.TabListModelBean;
//import fr.legrain.bdg.webapp.app.TabViewsBean;
//import fr.legrain.bdg.webapp.documents.OuvertureArticleBean;
//import fr.legrain.bdg.webapp.documents.OuvertureTiersBean;
//import fr.legrain.conformite.model.TaStatusConformite;
//import fr.legrain.document.dto.IDocumentDTO;
//import fr.legrain.document.dto.ILigneDocumentLotDTO;
//import fr.legrain.document.dto.ITaLFabrication;
//import fr.legrain.document.dto.TaLFabricationDTO;
//import fr.legrain.document.model.SWTLigneDocument;
//import fr.legrain.dossier.model.TaPreferences;
//import fr.legrain.droits.dto.TaUtilisateurDTO;
//import fr.legrain.droits.model.TaUtilisateur;
//import fr.legrain.lib.data.EnumModeObjet;
//import fr.legrain.lib.data.ExceptLgr;
//import fr.legrain.lib.data.LibConversion;
//import fr.legrain.lib.data.LibDate;
//import fr.legrain.lib.data.ModeObjetEcranServeur;
//import fr.legrain.stock.model.TaGrMouvStock;
//import fr.legrain.stock.model.TaMouvementStock;
//import fr.legrain.tiers.dto.TaTiersDTO;
//import fr.legrain.tiers.model.TaEmail;
//import fr.legrain.tiers.model.TaTiers;
//
//
//@Named
//@ViewScoped  
//public class AbonnementController_old extends AbstractController implements Serializable { 
//	
//	@Inject @Named(value="tabViewsBean")
//	private TabViewsBean tabViews;
//	
//	@Inject @Named(value="etiquetteCodeBarreBean")
//	private EtiquetteCodeBarreBean etiquetteCodeBarreBean;
//	
//	@Inject @Named(value="tabListModelCenterBean")
//	private TabListModelBean tabsCenter;
//	
//	@Inject @Named(value="leftBean")
//	private LeftBean leftBean;
//	
//	@Inject @Named(value="ouvertureTiersBean")
//	private OuvertureTiersBean ouvertureTiersBean;
//	
//	@Inject @Named(value="ouvertureArticleBean")
//	private OuvertureArticleBean ouvertureArticleBean;
//	
//	protected org.primefaces.component.wizard.Wizard wizardDocument;
//	
//	private TaStripeSubscription masterEntity;
//	private TaLFabricationMP masterEntityLigneMP;
//	
//	private TaLAbonnementDTOJSF selectedLigneJSF;
//	private TaStripeSubscriptionDTO selectedDocumentDTO;
//	private TaStripeSubscriptionDTO[] selectedDocumentDTOs; 
//
//	private List<TaLAbonnementDTOJSF> valuesLigne;
//	
//	private TaTiers taTiers;
//	private TaTiersDTO taTiersDTO;
//	
//	private boolean insertAutoMP = true;
//	private boolean insertAutoEnabledMP = true;
//	
//	
//	private List<TaStripeSubscriptionDTO> listeAbonnement;
//	
//	protected RemoteCommand rc;
//	protected String stepSynthese = "idSyntheseXXXXXXX";
//	protected String stepEntete = "idEnteteXXXXXXX";
//	protected String stepLignes = "idLignesXXXXXXX";
//	protected String stepTotaux = "idTotauxFormXXXXXXX";
//	protected String stepValidation = "idValidationFormXXXXXXX";
//	protected String idMenuBouton = "form:tabView:idMenuBoutonXXXXXXX";
//	protected String idBtnWizardButtonPrecedent = "form:tabView:idBtnWizardButtonPrecedentXXXXXXX";
//	protected String idBtnWizardButtonSuivant = "form:tabView:idBtnWizardButtonSuivantXXXXXXX";
//	protected String currentStepId;
//	protected String wvEcran = "XXXXXXX";
//	protected String JS_FONCTION_INITIALISE_POSITION_BOUTON_WIZARD = "initialisePositionBoutonWizard('')";
//	protected boolean btnPrecedentVisible = false;
//	protected boolean btnSuivantVisible = true;
//	protected boolean ecranSynthese=false;
//	protected boolean gestionBoutonWizardDynamique = false;
//	
//	protected String messageSuppression=Const.C_MESSAGE_SUPPRESSION_DOCUMENT;
//	protected String titreSuppression="Confirmation de la suppression de l'enregistrement";
//	
//	protected String messageModification=Const.C_MESSAGE_MODIFIFCATION_DOCUMENT;
//	protected String titreModification="Confirmation de la modification de l'enregistrement";
//
//	//list permettant de faire la correspondance entre les lots / articles , les entrepot et les emplacement
//	private List<ArticleLotEntrepotDTO> listArticleLotEntrepot;
//
//	protected ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();
//	protected ModeObjetEcranServeur modeEcranLigne = new ModeObjetEcranServeur();
//	protected ModeObjetEcranServeur modeEcranProduit = new ModeObjetEcranServeur();
//
//	private @EJB ITaStripeSubscriptionServiceRemote taStripeSubscriptionService;
//	private @EJB ITaStripePlanServiceRemote taStripePlanService;
//	private @EJB ITaTiersServiceRemote tiersService;
//	private @EJB ITaPreferencesServiceRemote taPreferencesService;
//	private @EJB ITaArticleServiceRemote articleService;
//	private @EJB ITaTypeMouvementServiceRemote typeMouvementService;
//	private @EJB ITaUniteServiceRemote uniteService;
//	private @EJB ITaTLigneServiceRemote taTLigneService;
//	private @EJB ITaUniteServiceRemote taUniteService;
//	private @EJB ITaGenCodeExServiceRemote taGenCodeExService;
//	protected @EJB ITaInfoEntrepriseServiceRemote taInfoEntrepriseService;
//	
//	private LgrDozerMapper<TaStripeSubscriptionDTO,TaStripeSubscription> mapperUIToModel  = new LgrDozerMapper<TaStripeSubscriptionDTO,TaStripeSubscription>();
//	private LgrDozerMapper<TaStripeSubscription,TaStripeSubscriptionDTO> mapperModelToUI  = new LgrDozerMapper<TaStripeSubscription,TaStripeSubscriptionDTO>();
//	private LgrDozerMapper<TaTiers,TaTiersDTO> mapperModelToUITiers  = new LgrDozerMapper<TaTiers,TaTiersDTO>();
//
//	public AbonnementController_old() {  
//
//	}  
//
//	@PostConstruct
//	public void postConstruct(){
//		nomDocument="Abonnement";
//		
//		stepSynthese = "idSyntheseAbonnement";
//		stepEntete = "idEnteteAbonnement";
//		stepLignes = "idLignesAbonnement";
//		stepTotaux = "idTotauxFormAbonnement";
//		stepValidation = "idValidationFormAbonnement";
//		idMenuBouton = "form:tabView:idMenuBoutonAbonnement";
//		wvEcran = LgrTab.WV_TAB_DEVIS;
//		idBtnWizardButtonPrecedent = "form:tabView:idBtnWizardButtonPrecedentAbonnement";
//		idBtnWizardButtonSuivant = "form:tabView:idBtnWizardButtonSuivantAbonnement";
//		
//		JS_FONCTION_INITIALISE_POSITION_BOUTON_WIZARD = "initialisePositionBoutonWizard('.wizard-abonnement')";
//		gestionBoutonWizardDynamique = true;
//		
//		refresh();
//		
//		
//		//actInserer();
//	}
//
//	public void refresh(){
//		listeAbonnement = taStripeSubscriptionService.selectAllDTO();
////		listeAbonnement = taStripeSubscriptionService.findAllLight();
//		valuesLigne = IHMmodelMP();
//		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
//		
//	}
//	
//	public List<TaLAbonnementDTOJSF> IHMmodelMP() {
//		LinkedList<TaStripeSubscriptionItem> ldao = new LinkedList<TaStripeSubscriptionItem>();
//		LinkedList<TaLAbonnementDTOJSF> lswt = new LinkedList<TaLAbonnementDTOJSF>();
//		
//		if(masterEntity!=null && masterEntity.getItems() !=null && !masterEntity.getItems().isEmpty()) {
//			masterEntity.reinitialiseNumLignesMP(0, 1);
//			ldao.addAll(masterEntity.getItems());
//
//			LgrDozerMapper<TaStripeSubscriptionItem,TaStripeSubscriptionItemDTO> mapper = new LgrDozerMapper<TaStripeSubscriptionItem,TaStripeSubscriptionItemDTO>();
//			TaStripeSubscriptionItemDTO dto = null;
//			TaArticleDTO artDTO = null;
//			for (TaStripeSubscriptionItem ligne : ldao) {
//				if(!ligne.ligneEstVide()){
//				TaLAbonnementDTOJSF t = new TaLAbonnementDTOJSF();
////				try {
////					TaLot lot = taLotService.findByCode(o.getNumLot());
////					o.setTaLot(lot);
//				dto = (TaStripeSubscriptionItemDTO) mapper.map(ligne, TaStripeSubscriptionItemDTO.class);
//				dto.setId(ligne.getIdStripeSubscriptionItem());
//				
////				dto.setEmplacement(ligne.getEmplacementLDocument());
//				
//				t.setDto(dto);
////				t.setTaLot(ligne.getTaLot());
////				if(t.getArticleLotEntrepotDTO()==null) {
////					t.setArticleLotEntrepotDTO(new ArticleLotEntrepotDTO());
////				}
////				if(ligne.getTaLot()!=null) {
////					t.getArticleLotEntrepotDTO().setNumLot(ligne.getTaLot().getNumLot());
////				}
////				//t.getDto().setNumLot(ligne.getTaLot().getNumLot());
////				t.getDto().setLibelleLot(ligne.getLibLDocument());
////				if(ligne.getTaMouvementStock()!=null) {
////					t.getDto().setEmplacement(ligne.getTaMouvementStock().getEmplacement());
////					t.getDto().setU1LDocument(ligne.getTaMouvementStock().getUn1Stock());
////					t.getDto().setU2LDocument(ligne.getTaMouvementStock().getUn2Stock());
////				}
////				if(ligne.getTaEntrepot()!=null) {
////					t.getDto().setCodeEntrepot(ligne.getTaEntrepot().getCodeEntrepot());
////				}
////				t.setTaLot(lot);
//				
//				t.setTaArticle(taStripePlanService.findByStripeSubscriptionItem(ligne).getTaArticle());
//				artDTO = new TaArticleDTO();
//				artDTO.setCodeArticle(t.getTaArticle().getCodeArticle());
//				artDTO.setLibellecArticle(t.getTaArticle().getLibellecArticle());
//				t.setTaArticleDTO(artDTO);
//				
////				if(ligne.getTaArticle()!=null) {
////					t.setTaRapport(ligne.getTaArticle().getTaRapportUnite());
////				}
////				if(ligne.getU1LDocument()!=null) {
////					t.setTaUnite1(new TaUnite());
////					t.getTaUnite1().setCodeUnite(ligne.getU1LDocument());
////				}
////				if(ligne.getU2LDocument()!=null) {
////					t.setTaUnite2(new TaUnite());
////					t.getTaUnite2().setCodeUnite(ligne.getU2LDocument());
////				}
////				t.setTaEntrepot(ligne.getTaEntrepot());
//				
////				} catch (Exception e) {
////					e.printStackTrace();
////				}
////				t.getDto().setCodeStatusConformite(lotService.validationLot(t.getTaLot()));
//				lswt.add(t);
//				}
//			}
//
//		}
//		return lswt;
//	}
//
//	public boolean pasDejaOuvert() {
//		LgrTab b = new LgrTab();
//		b.setTypeOnglet(LgrTab.TYPE_TAB_ABONNEMENT);
//		return tabsCenter.ongletDejaOuvert(b)==null;
//	} 
//
//	public void nouveau(ActionEvent actionEvent) {  
//		LgrTab b = new LgrTab();
//		b.setTypeOnglet(LgrTab.TYPE_TAB_ABONNEMENT);
//		b.setTitre("Fabrication");
//		//b.setStyle(LgrTab.CSS_CLASS_TAB_ARTICLE);
//		b.setTemplate("abonnement/abonnement.xhtml");
//		b.setIdTab(LgrTab.ID_TAB_ABONNEMENT);
//		b.setStyle(LgrTab.CSS_CLASS_TAB_ABONNEMENT);
//		b.setParamId("0");
//		leftBean.setExpandedForce(false);
//
//		tabsCenter.ajouterOnglet(b);
//		tabViews.selectionneOngletCentre(b);
//		//actInserer(actionEvent);
//		actInserer();
//
//		if(ConstWeb.DEBUG) {
//			FacesContext context = FacesContext.getCurrentInstance();  
//			context.addMessage(null, new FacesMessage("Abonnement", 
//					"Nouveau"
//					)); 
//		}
//	} 
//	
//	public boolean stepEnCours(String step ){		
//		return step.equals(currentStepId);
//	}
//	
//	public String handleFlow(FlowEvent event) {
//
//		try{
//			currentStepId = event.getOldStep();
//			String stepToGo = event.getNewStep();
//	
//			if(modeEcranLigne.dataSetEnModif())
//				actEnregistrerLigne(null);
//			
//			btnPrecedentVisible = true;
//			btnSuivantVisible = true;
//			
//			if(currentStepId.equals(stepEntete) && stepToGo.equals(stepLignes)) {
//				mapperUIToModel.map(selectedDocumentDTO, masterEntity);
//			} else if(currentStepId.equals(stepLignes) && stepToGo.equals(stepTotaux)) {
//				btnSuivantVisible = false;
//			} else {
//				if(stepSynthese.equals("idSyntheseXXXXXXX")) {//pas implémenter
//					if(currentStepId.equals(stepLignes) && stepToGo.equals(stepEntete))btnPrecedentVisible = false;
//				}else {
//					if(currentStepId.equals(stepEntete) && stepToGo.equals(stepSynthese))btnPrecedentVisible = false;
//				}
//				
//			}
//			
//			RequestContext.getCurrentInstance().update(idBtnWizardButtonPrecedent);
//			RequestContext.getCurrentInstance().update(idBtnWizardButtonSuivant);
//			
//			scrollToTop();
//			//executer ici car pas d'evenement de type oncomplete sur le handlerFlow mais doit pourtant etre executer à la fin après toute les mise jour de la vue (y compris le scroll)
//			initialisePositionBoutonWizard();
//			
//			currentStepId=event.getNewStep();
//			RequestContext.getCurrentInstance().update(idMenuBouton);
//			
//			
//			return currentStepId;
//
//		} catch (Exception e) {
//			FacesContext context = FacesContext.getCurrentInstance();  
//			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Facture", e.getMessage()));
//			return event.getOldStep();
//		}
//	}
//	
//	public void initialisePositionBoutonWizard() {
//		if(gestionBoutonWizardDynamique) {
//			RequestContext.getCurrentInstance().execute(JS_FONCTION_INITIALISE_POSITION_BOUTON_WIZARD);
//		}
//	}
//	
//	public String stepCourant() {
//		ecranSynthese=modeEcran.getMode().equals(EnumModeObjet.C_MO_CONSULTATION);
//		if(ecranSynthese)currentStepId=	stepSynthese;
//		else currentStepId=	stepEntete;
//		return currentStepId;
//	}
//
//	public void changementStepWizard(boolean actModifier) {
//		if(wizardDocument!=null) {
//			if(ecranSynthese && !this.stepSynthese.equals("idSyntheseXXXXXXX"))wizardDocument.setStep(this.stepSynthese);
//			else	if(actModifier && (wizardDocument.getStep().equals(this.stepSynthese)))
//			{
//				wizardDocument.setStep(this.stepEntete);
//			}
//			currentStepId=wizardDocument.getStep();
//		}	
//	}
//	
//	public boolean etatBouton(String bouton) {
//		return etatBouton(bouton,modeEcran);
//	}
//	
//	public boolean etatBoutonLigne(String bouton) {
//		boolean retour = true;
//		if(modeEcran.dataSetEnModif()) {
//			retour = false;
//		}
//		switch (modeEcranLigne.getMode()) {
//		case C_MO_INSERTION:
//			switch (bouton) {
//			case "annuler":
//			case "enregistrer":
//			case "fermer":
//				retour = false;
//				break;
//			case "rowEditor":
//				retour = modeEcran.dataSetEnModif()?true:false;
//				break;
//			default:
//				break;
//			}
//			break;
//		case C_MO_EDITION:
//			switch (bouton) {
//			case "annuler":
//			case "enregistrer":
//			case "fermer":
//				retour = false;
//				break;
//			case "rowEditor":
//				retour = modeEcran.dataSetEnModif()?true:false;
//				break;
//			default:
//				break;
//			}
//			break;
//		case C_MO_CONSULTATION:
//			switch (bouton) {
//			case "supprimer":
//			case "modifier":
//			case "inserer":
//			case "imprimer":
//			case "fermer":
//				retour = modeEcran.dataSetEnModif()?false:true;
//				break;
//			case "rowEditor":
//				retour = modeEcran.dataSetEnModif()?true:false;
//				break;
//			default:
//				break;
//			}
//			break;
//		default:
//			break;
//		}
//
//		return retour;
//
//	}
//	
//	public boolean etatBouton(String bouton, ModeObjetEcranServeur mode) {
//		boolean retour = true;
//		switch (mode.getMode()) {
//		case C_MO_INSERTION:
//			switch (bouton) {
//			case "annuler":
//			case "enregistrer":
//			case "fermer":
//				retour = false;
//				break;
//			case "rowEditor":
//				retour = modeEcran.dataSetEnModif()?true:false;
//				break;
//			case "majNumLot":
//				retour = false;
//				break;
//			default:
//				break;
//			}
//			break;
//		case C_MO_EDITION:
//			switch (bouton) {
//			case "annuler":
//			case "enregistrer":
//			case "fermer":
//				retour = false;
//				break;
//			case "rowEditor":
//				retour = modeEcran.dataSetEnModif()?true:false;
//				break;
//			default:
//				break;
//			}
//			break;
//		case C_MO_CONSULTATION:
//			switch (bouton) {
//			case "inserer":
//			case "fermer":
//				retour = false;
//				break;
//			case "rowEditor":
//				retour = modeEcran.dataSetEnModif()?true:false;
//				break;
//			case "supprimer":
//			case "modifier":
//			case "imprimer":
//				if(selectedDocumentDTO!=null && selectedDocumentDTO.getId()!=null  && selectedDocumentDTO.getId()!=0 ) retour = false;
//				break;				
//			default:
//				break;
//			}
//			break;
//		default:
//			break;
//		}
//
//		return retour;
//
//	}
//	
//	public void actEnregistrer(){  
//		try {
//			//Mapping vers les entités
//			autoCompleteMapUIToDTO();
//			
//			validateDocumentAvantEnregistrement(selectedDocumentDTO);
//			mapperUIToModel.map(selectedDocumentDTO, masterEntity);
//			
//			masterEntity.setLegrain(false);
//			
//			
//			//supression des lignes vides
//			List<TaStripeSubscriptionItem> listeLigneVide = new ArrayList<TaStripeSubscriptionItem>(); 
//			for (TaStripeSubscriptionItem ligne : masterEntity.getItems()) {
//				ligne.setLegrain(false);
//				if(ligne.ligneEstVide()) {
//					listeLigneVide.add(ligne);
//				}
//			}
//			
//			for (TaStripeSubscriptionItem ligne : masterEntity.getItems()) {
//				ligne.setLegrain(false);
//				if(gestionLot && ligne.getTaArticle()!=null && ligne.getTaArticle().getGestionLot() && ligne.getTaLot()==null){
//					throw new Exception("Le numéro du lot doit être rempli");
//				} else {
//					if(!gestionLot || (ligne.getTaArticle()!=null && !ligne.getTaArticle().getGestionLot())) {
//						//utiliser un lot virtuel
//						if(ligne.getTaLot()==null ){
//							ligne.setTaLot(lotService.findOrCreateLotVirtuelArticle(ligne.getTaArticle().getIdArticle()));
//						}
//					}
//				}
//			}
//			
//			for (TaStripeSubscriptionItem taLBonReception : listeLigneVide) {
//				masterEntity.getItems().remove(taLBonReception);
//			}
//			
////			/*
////			 * Gérer les mouvements corrrespondant aux lignes 
////			 */
////			//Création du groupe de mouvement
////			TaGrMouvStock grpMouvStock = new TaGrMouvStock();
////			if(masterEntity.getTaGrMouvStock()!=null) {
////				grpMouvStock=masterEntity.getTaGrMouvStock();
////			}
////			grpMouvStock.setCodeGrMouvStock(masterEntity.getCodeDocument());
////			grpMouvStock.setDateGrMouvStock(masterEntity.getDateDebR());
////			grpMouvStock.setLibelleGrMouvStock(masterEntity.getLibelleDocument()!=null?masterEntity.getLibelleDocument():"Fabrication "+masterEntity.getCodeDocument());
////			grpMouvStock.setTaTypeMouvStock(taTypeMouvementService.findByCode("F"));
////			masterEntity.setTaGrMouvStock(grpMouvStock);
////			grpMouvStock.setTaStripeSubscription(masterEntity);
////			
////			for (TaStripeSubscriptionItem l : masterEntity.getItems()) {
////				l.setTaTLigne(taTLigneService.findByCode(Const.C_CODE_T_LIGNE_H));
////				if(l.getTaMouvementStock()!=null)
////					l.getTaMouvementStock().setTaGrMouvStock(masterEntity.getTaGrMouvStock());
////			}
////			
////			//Création des lignes de mouvement
////			for (TaStripeSubscriptionItem ligne : masterEntity.getItems()) {
//////				if(ligne.getTaMouvementStock()==null){
////				TaMouvementStock mouv = new TaMouvementStock();
////				if(ligne.getTaMouvementStock()!=null) {
////					mouv=ligne.getTaMouvementStock();
////				}
////				if(ligne.getLibLDocument()!=null) {
////					mouv.setLibelleStock(ligne.getLibLDocument());
////				} else if (ligne.getTaArticle().getLibellecArticle()!=null){
////					mouv.setLibelleStock(ligne.getTaArticle().getLibellecArticle());
////				} else {
////					mouv.setLibelleStock("");
////				}
////				mouv.setDateStock(masterEntity.getDateDocument());
////				mouv.setEmplacement(ligne.getEmplacementLDocument());
////				mouv.setTaEntrepot(ligne.getTaEntrepot());
////				if(ligne.getTaLot()!=null){
//////					mouv.setNumLot(ligne.getTaLot().getNumLot());
////					mouv.setTaLot(ligne.getTaLot());
////				}
////				//mouv.setQte1Stock(ligne.getQteLDocument());
////				//mouv.setQte2Stock(ligne.getQte2LDocument());
////				mouv.setUn1Stock(ligne.getU1LDocument());
////				mouv.setUn2Stock(ligne.getU2LDocument());
////				mouv.setTaGrMouvStock(grpMouvStock);
//////				ligne.setTaLot(null);
////				ligne.setTaMouvementStock(mouv);
////				
////				grpMouvStock.getTaMouvementStockes().add(mouv);
//////				}
////			}
////			
////			
////			for (TaStripeSubscriptionItem ligneMP : masterEntity.getItems()) {
////				if(ligneMP.getQteLDocument()!=null) {
////					ligneMP.getTaMouvementStock().setQte1Stock(ligneMP.getQteLDocument().abs().negate());
////				}
////				if(ligneMP.getQte2LDocument()!=null) {
////					ligneMP.getTaMouvementStock().setQte2Stock(ligneMP.getQte2LDocument().abs().negate());
////				}
//////				ligneMP.getTaMouvementStock().setTaLot(null);
////
////			}
//			
//			masterEntity = taStripeSubscriptionService.mergeAndFindById(masterEntity,ITaStripeSubscriptionServiceRemote.validationContext);
//			masterEntity = taStripeSubscriptionService.findById(masterEntity.getIdDocument());
//			taStripeSubscriptionService.annuleCode(selectedDocumentDTO.getCodeDocument());
////			listeLFabASupprimer.clear();
////			fabricationService.remplirListeLigneASupprimer(listeLFabASupprimer);
//			masterEntity.findProduit();
//			//mapping vers l'IHM
//			mapperModelToUI.map(masterEntity, selectedDocumentDTO);
//			selectedDocumentDTOs=new TaStripeSubscriptionDTO[]{selectedDocumentDTO};
//			
//			valuesLigne = IHMmodelMP();
//			
//			
//			autoCompleteMapDTOtoUI();
//
//			if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_INSERTION)==0) {
////				listeFabrication.add(new TaStripeSubscriptionMapper().mapEntityToDto(masterEntity, null));
//				listeAbonnement.add(selectedDocumentDTO);
//			}
//
//			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
//			modeEcranLigne.setMode(EnumModeObjet.C_MO_CONSULTATION);
//			modeEcranProduit.setMode(EnumModeObjet.C_MO_CONSULTATION);
//			
//			//réinitialise l'écran
//			//onRowSelect(null);
//
//		} catch(Exception e) {
//			e.printStackTrace();
//			FacesContext context = FacesContext.getCurrentInstance();  
//			//context.addMessage(null, new FacesMessage("Fabrication", "actEnregistrer")); 
//			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fabrication", e.getMessage()));
//		}
//	}
//	
//	public void actAnnuler() {
//		try {
//			switch (modeEcran.getMode()) {
//			case C_MO_INSERTION:
//				if(selectedDocumentDTO.getCodeDocument()!=null) {
//					taStripeSubscriptionService.annuleCode(selectedDocumentDTO.getCodeDocument());
//				}
//				
//				
//				masterEntity=new TaStripeSubscription();
//				
//				if(listeAbonnement.size()>0)
//				selectedDocumentDTO = listeAbonnement.get(0);
//				else selectedDocumentDTO=new TaStripeSubscriptionDTO();
//				onRowSelect(null);
//				
//				mapperModelToUI.map(masterEntity,selectedDocumentDTO );
//				valuesLigne = IHMmodelMP();
//				break;
//			case C_MO_EDITION:
//				if(selectedDocumentDTO.getId()!=null && selectedDocumentDTO.getId()!=0){
//					
//					masterEntity = taStripeSubscriptionService.findById(selectedDocumentDTO.getId());
//					selectedDocumentDTO=taStripeSubscriptionService.findByIdDTO(selectedDocumentDTO.getId());
//					//isa le 14/01/2016
//					mapperModelToUI.map(masterEntity,selectedDocumentDTO );
//					masterEntity.findProduit();
//					valuesLigne = IHMmodelMP();
//					
//				}				
//				break;
//
//			default:
//				break;
//			}			
//				
//			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
//			modeEcranLigne.setMode(EnumModeObjet.C_MO_CONSULTATION);
//			modeEcranProduit.setMode(EnumModeObjet.C_MO_CONSULTATION);
//			masterEntity.findProduit();
//			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
//	
//			if(ConstWeb.DEBUG) {
//				FacesContext context = FacesContext.getCurrentInstance();  
//				context.addMessage(null, new FacesMessage("Fabrication", "actAnnuler")); 
//			}
//		} catch (FinderException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	public void actFermer() {
//		switch (modeEcran.getMode()) {
//		case C_MO_INSERTION:
//			actAnnuler();
//			break;
//		case C_MO_EDITION:
//			actAnnuler();							
//			break;
//
//		default:
//			break;
//		}
//		
//		selectedDocumentDTO=new TaStripeSubscriptionDTO();
//		selectedDocumentDTOs=new TaStripeSubscriptionDTO[]{selectedDocumentDTO};
//		updateTab();
//		
////		//gestion du filtre de la liste
////        RequestContext requestContext = RequestContext.getCurrentInstance();
////        requestContext.execute("PF('wvDataTableListeFabrication').filter()");
//        
//	}
//
//	public void actInserer() {
//		
//		insertAutoEnabledMP = true;
//		
//		selectedDocumentDTO = new TaStripeSubscriptionDTO();
//		
//		masterEntity = new TaStripeSubscription();
//		masterEntity.findProduit();
//		
//		valuesLigne = IHMmodelMP();
//		autoCompleteMapDTOtoUI();
//		
//		selectedDocumentDTO.setDateDebR(new Date());
//
//		taGenCodeExService.libereVerrouTout(new ArrayList<String>(SessionListener.getSessions().keySet()));
//		Map<String, String> params = new LinkedHashMap<String, String>();
//		params.put(Const.C_CODETYPE, selectedDocumentDTO.getCodeTFabrication());
//		
//		if(selectedDocumentDTO.getCodeDocument()!=null) {
//			taStripeSubscriptionService.annuleCode(selectedDocumentDTO.getCodeDocument());
//		}
//		String newCode=taStripeSubscriptionService.genereCode(params);
//		if(newCode!=null && !newCode.equals("")){
//			selectedDocumentDTO.setCodeDocument(newCode);
//			if(selectedDocumentDTO.getLibelleDocument().isEmpty()){
//				selectedDocumentDTO.setLibelleDocument("Fabrication n°: "+newCode);
//			}
//		}
//		
//		
//		masterEntity.setItems(new ArrayList<TaStripeSubscriptionItem>());
//		
//		modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
//		scrollToTop();
//	}
//	
//	public void actModifier() {
//		modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
//	}
//	
//	public void supprimer() {
//		actSupprimer(null);
//	}
//	
//	public void actSupprimer() {
//		actSupprimer(null);
//	}
//	
//	public void actSupprimer(ActionEvent actionEvent) {
//		try {
//			if(selectedDocumentDTO!=null && selectedDocumentDTO.getId()!=null){
//				masterEntity = taStripeSubscriptionService.findById(selectedDocumentDTO.getId());
//			}
//			masterEntity.findProduit();
////			for (TaLFabrication ligne : masterEntity.getListMatierePremieres()) {
////				ligne.setTaLot(null);
////			}
//			
//			taStripeSubscriptionService.remove(masterEntity);
//			listeAbonnement.remove(selectedDocumentDTO);
//			
//
//			if(!listeAbonnement.isEmpty()) {
//				selectedDocumentDTO = listeAbonnement.get(0);
//				selectedDocumentDTOs=new TaStripeSubscriptionDTO[]{selectedDocumentDTO};
//				updateTab();
//			}else {
//				selectedDocumentDTO=new TaStripeSubscriptionDTO();
//				selectedDocumentDTOs=new TaStripeSubscriptionDTO[]{};
//			}
//
//			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
//			scrollToTop();
//
//			if(ConstWeb.DEBUG) {
//				FacesContext context = FacesContext.getCurrentInstance();  
//				context.addMessage(null, new FacesMessage("Fabrication", "fabrication supprimée."));
//			}
//		} catch(Exception e) {
//			e.printStackTrace();
//			FacesContext context = FacesContext.getCurrentInstance();  
//			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fabrication", e.getMessage()));
//		}
//	}
//	
//	public void actImprimer(ActionEvent actionEvent) {
//		if(ConstWeb.DEBUG) {
//			FacesContext context = FacesContext.getCurrentInstance();  
//			context.addMessage(null, new FacesMessage("Fabrication", "actImprimer")); 
//		}
//		
//	}    
//	
//	public List<TaUnite> uniteAutoComplete(String query) {
//		List<TaUnite> allValues = taUniteService.selectAll();
//		List<TaUnite> filteredValues = new ArrayList<TaUnite>();
//		TaUnite civ = new TaUnite();
//		civ.setCodeUnite(Const.C_AUCUN);
//		filteredValues.add(civ);
//		for (int i = 0; i < allValues.size(); i++) {
//			 civ = allValues.get(i);
//			if(query.equals("*") || civ.getCodeUnite().toLowerCase().contains(query.toLowerCase())) {
//				filteredValues.add(civ);
//			}
//		}
//
//		return filteredValues;
//	}
//
//	//************************************************************************************************************//
//	//	Matière Premiere
//	//************************************************************************************************************//
//
//	public void actEnregistrerLigne() {
//		actEnregistrerLigne(null);
//	}
//
//	public void actEnregistrerLigne(ActionEvent ae) {
//
//		try {
//			
//			selectedLigneJSF.updateDTO(true);
////			masterEntityLigneMP.setPf(false);
//			masterEntityLigneMP.setTaArticle(selectedLigneJSF.getTaArticle());
////			masterEntityLigneMP.setTaEntrepot(selectedTaMatierePremiere.getTaEntrepot());
////			masterEntityLigneMP.setTaLot(selectedTaMatierePremiere.getTaLot());
////			masterEntityLigneMP.setEmplacementLDocument(selectedTaMatierePremiere.getDto().getEmplacement());
//			
//			LgrDozerMapper<TaLFabricationDTO,TaLFabricationMP> mapper = new LgrDozerMapper<TaLFabricationDTO,TaLFabricationMP>();
//			mapper.map((TaLFabricationDTO) selectedLigneJSF.getDto(),masterEntityLigneMP);
//			
//			//rajouter par isa car perte du numlot si modif ligne existante
//			if(selectedLigneJSF.getArticleLotEntrepotDTO()==null) {
//				selectedLigneJSF.setArticleLotEntrepotDTO(new ArticleLotEntrepotDTO());
//			}
//			if(masterEntityLigneMP.getTaLot()!=null) {
//				selectedLigneJSF.getArticleLotEntrepotDTO().setNumLot(masterEntityLigneMP.getTaLot().getNumLot());
//			}
//			/////
//			
//			masterEntity.enregistreLigne(masterEntityLigneMP);
//			//if(masterEntityLigneMP.getIdLDocument()==0) { //** if pour empecher de doubler les lignes pendant une modification
//			//if(modeEcranLigne.getMode().compareTo(EnumModeObjet.C_MO_INSERTION)==0){
//			if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_INSERTION)==0){
////				if(!masterEntity.getLignes().contains(masterEntityLigneMP)) {
////					masterEntity.addLigne(masterEntityLigneMP);
////				}
//				if(!masterEntity.getLignesMatierePremieres().contains(masterEntityLigneMP)) {
//					masterEntity.getLignesMatierePremieres().add(masterEntityLigneMP);
//				}
//			}
//			modeEcranLigne.setMode(EnumModeObjet.C_MO_CONSULTATION);
//			
//			//((TaLBonReceptionDTOJSF) event.getObject()).setAutoInsert(false);
//			//		actInsererLigne(null);
//	
//			//        FacesMessage msg = new FacesMessage("Ligne Edited", ((TaLBonReceptionDTOJSF) event.getObject()).getIdLDocument().toString());
//			//        FacesContext.getCurrentInstance().addMessage(null, msg);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		if(ConstWeb.DEBUG) {
//			FacesContext context = FacesContext.getCurrentInstance();  
//			context.addMessage(null, new FacesMessage("Fabrication", "actEnregisterMatierePremiere")); 
//		}
//
//	}
//
//	public void actSupprimerMatierePremiere() {
//		try {
//		TaLFabricationMP l = null;
//		Boolean trouve = valuesLigne.remove(selectedLigneJSF);
//		if(trouve){
//			for (TaLFabricationMP var : masterEntity.getLignesMatierePremieres()) {
//				if(selectedLigneJSF.getDto().getNumLigneLDocument()!=null && (var.getNumLigneLDocument()==selectedLigneJSF.getDto().getNumLigneLDocument())) {
//					l = var;
//				}
//			}
//			if(l!=null) {
////				l.setTaLot(null);
////				l.getTaMouvementStock().setTaLot(null);
////				masterEntity.getLignesMatierePremieres().remove(l);
//				removeLigneMP(l);
//				valuesLigne=IHMmodelMP();
//				//s'assurer que la ligne de mouvement correspondant est bien supprimée du groupe de mouvement aussi
//				TaGrMouvStock grpMouvStock = new TaGrMouvStock();
//				if(masterEntity.getTaGrMouvStock()!=null) {
//					grpMouvStock=masterEntity.getTaGrMouvStock();
//					grpMouvStock.getTaMouvementStockes().remove(l.getTaMouvementStock());
//				}
//			}
//			
//		}
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//
//	public void actInsererMatierePremiere(ActionEvent actionEvent) {
////		masterEntityLigneMP = new TaLFabrication();
////		masterEntityLigneMP.setTaMouvementStock(new TaMouvementStock());
////
////		initOrigineMouvement(masterEntityLigneMP);
//		
////		modeEcranLigne.setMode(EnumModeObjet.C_MO_INSERTION);
//		/***********************************************************************************************************************************************************/
//		
//		//		if(selectedTaLBonReceptionDTOJSF==null || !selectedTaLBonReceptionDTOJSF.isAutoInsert()) {
//		TaLAbonnementDTOJSF nouvelleLigne = new TaLAbonnementDTOJSF();
//		nouvelleLigne.setDto(new TaLFabricationDTO());
//		//nouvelleLigne.getDto().setNumLigneLDocument(valuesLigneMP.size()+valuesLignePF.size()+1);
////		nouvelleLigne.getDto().setNumLigneLDocument(calcNextNumLigneDoc());
//		nouvelleLigne.getDto().setNumLigneLDocument(valuesLigne.size()+1);
////		nouvelleLigne.getDto().setNumLot(selectedTaBonReceptionDTO.getCodeDocument()+"_"+valuesLigne.size()+1);
//		if(selectedDocumentDTO.getCodeDocument()!=null) {
//			nouvelleLigne.getDto().setNumLot(selectedDocumentDTO.getCodeDocument()+"_"+nouvelleLigne.getDto().getNumLigneLDocument());
//		} else {
//			nouvelleLigne.getDto().setNumLot(""+nouvelleLigne.getDto().getNumLigneLDocument());
//		}
//		
//		//			nouvelleLigne.setAutoInsert(true);
//
//		masterEntityLigneMP = new TaLFabricationMP(true); 
//		try {
//			//H = HT => pas nécessaire, mais il ne faut pas de ligne de commentaire  pour passer trigger "ligne_vide"
//			masterEntityLigneMP.setTaTLigne(taTLigneService.findByCode(Const.C_CODE_T_LIGNE_H));
//		
//			//List<TaLBonReceptionDTOJSF> modelListeLigneDevis = IHMmodel();
//
//			//masterEntity.getLignesMatierePremieres().add(masterEntityLigneMP);
////			masterEntity.addLigne(masterEntityLigneMP);
//			masterEntity.insertLigneMP(masterEntityLigneMP,nouvelleLigne.getDto().getNumLigneLDocument());
//		} catch (FinderException e) {
//			e.printStackTrace();
//		}
//		catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		selectedDocumentDTO.setNumLigneLDocument(masterEntityLigneMP.getNumLigneLDocument());
//		masterEntityLigneMP.setTaDocument(masterEntity);
//		masterEntityLigneMP.initialiseLigne(true);
//		
//		initOrigineMouvement(masterEntityLigneMP);
//
//		valuesLigne.add(nouvelleLigne);
//		
////		l'affectation du selected avec nouvelleLigne est importante pour
////		la création sur modèle -> donc je l'ai remis
//		selectedLigneJSF = nouvelleLigne;
//
//		modeEcranLigne.setMode(EnumModeObjet.C_MO_INSERTION);
//		//		}
//
//		if(ConstWeb.DEBUG) {
//			FacesContext context = FacesContext.getCurrentInstance();  
//			context.addMessage(null, new FacesMessage("Fabrication", "actInsererMatierePremiere")); 
//		}
//	}
//
//	public void initOrigineMouvement(ITaLFabrication l) {
//		if(l.getTaMouvementStock()==null) {
//			l.setTaMouvementStock(new TaMouvementStock());
//		}
//		l.getTaMouvementStock().setTaGrMouvStock(masterEntity.getTaGrMouvStock());
//	}
//
//	public void actModifierMatierePremiere () {
//		
//		modeEcranLigne.setMode(EnumModeObjet.C_MO_EDITION);
//		
//		if(ConstWeb.DEBUG) {
//			FacesContext context = FacesContext.getCurrentInstance();  
//			context.addMessage(null, new FacesMessage("Fabrication", "actModifierMatierePremiere")); 
//		}
//		
//	}
//
//	public void actAnnulerMatierePremiere(){
//		List<TaLFabricationMP> lmp = masterEntity.getLignesMatierePremieres();
////		if(masterEntityLigneMP == null 
////				|| masterEntityLigneMP.getTaMouvementStock().getIdMouvementStock() != 0){
////			lmp.add(masterEntityLigneMP);
////		}
////		if(masterEntityLigneMP == null 
////				|| (!lmp.contains(masterEntityLigneMP) && masterEntityLigneMP.getTaMouvementStock()!=null && masterEntityLigneMP.getTaMouvementStock().getIdMouvementStock() != 0)){
////			lmp.add(masterEntityLigneMP);
////		}
//		masterEntity.setLignesMatierePremieres(lmp);
//		masterEntityLigneMP = new TaLFabricationMP();
//		
//		valuesLigne = IHMmodelMP();
//		if(!valuesLigne.isEmpty())
//			selectionLigneMP(valuesLigne.get(0));	
//		setInsertAutoMP(false);
//		modeEcranLigne.setMode(EnumModeObjet.C_MO_CONSULTATION);
//	}
//
//	
//	
//	public TaStripeSubscriptionDTO rempliDTO(){
//		if(masterEntity!=null){			
//			try {
//				refresh();
//				selectedDocumentDTO = rechercheDansListeValues(masterEntity.getCodeDocument());
//				masterEntity = taStripeSubscriptionService.findByCode(masterEntity.getCodeDocument());
////				mapperModelToUI.map(masterEntity, selectedDocumentDTO);
//				selectedDocumentDTOs=null ;
//			} catch (FinderException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		return selectedDocumentDTO;
//	}
//	
//	public TaStripeSubscriptionDTO rechercheDansListeValues(String codeDocument){
//		for (TaStripeSubscriptionDTO doc : listeAbonnement) {
//			if(doc.getCodeDocument().equals(codeDocument))
//				return doc;
//		}
//		return null;
//	}
//
//	public void actModifierProduit () {
//		
//		modeEcranProduit.setMode(EnumModeObjet.C_MO_EDITION);
//		
//		if(ConstWeb.DEBUG) {
//			FacesContext context = FacesContext.getCurrentInstance();  
//			context.addMessage(null, new FacesMessage("Fabrication", "actModifierProduit")); 
//		}
//		
//	}
//
//	
//	
//	public int calcNextNumLigneDoc() {
//		int num = -1;
//		List<Integer> l = new ArrayList<>();
//		for (TaLAbonnementDTOJSF lf : valuesLigne) {
//			l.add(lf.getDto().getNumLigneLDocument());
//		}
//		for (TaLAbonnementDTOJSF lf : valuesLignePF) {
//			l.add(lf.getDto().getNumLigneLDocument());
//		}
//		if(!l.isEmpty()) {
//			num = Collections.max(l);
////			Collections.sort(l);
////			num = l.get(l.size() - 1);
//		} else {
//			return 1;
//		}
//		return num+1;
//	}
//
//	public void supprimer(ActionEvent actionEvent) {
//		actSupprimer(actionEvent);
//	}
//	
//	public void detail() {
//		detail(null);
//	}
//	
//	public void detail(ActionEvent actionEvent) {
//		onRowSelect(null);
//	}
//
//	public void onRowSimpleSelect(SelectEvent event) {
//
//		try {
//			if(pasDejaOuvert()==false){
//				onRowSelect(event);
//	
//				autoCompleteMapDTOtoUI();
//				valuesLigne = IHMmodelMP();
//	
//				FacesContext context = FacesContext.getCurrentInstance();  
//				context.addMessage(null, new FacesMessage("Fabrication", 
//						"Chargement de fabrication N°"+selectedDocumentDTO.getCodeDocument()
//						)); 
//			} else {
//				tabViews.selectionneOngletCentre(LgrTab.TYPE_TAB_FABRICATION);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	} 
//
//	public void updateTab(){
//		try {
//			
//			if(selectedDocumentDTOs!=null && selectedDocumentDTOs.length>0) {
//				selectedDocumentDTO = selectedDocumentDTOs[0];
//			}
//			if(selectedDocumentDTO.getId()!=null && selectedDocumentDTO.getId()!=0) {
//				masterEntity = taStripeSubscriptionService.findById(selectedDocumentDTO.getId());
//			}
//			//séparer en 2 listes dans l'entité les lignes de fabrication
//			//masterEntity.findProduit();
//			
////			masterEntity = taBonReceptionService.findById(selectedTaBonReceptionDTO.getId());
//			valuesLigne = IHMmodelMP();
//			
//			mapperModelToUI.map(masterEntity, selectedDocumentDTO);
//			
//			autoCompleteMapDTOtoUI();
//	
//			if(ConstWeb.DEBUG) {
//				FacesContext context = FacesContext.getCurrentInstance();  
//				context.addMessage(null, new FacesMessage("Fabrication", 
//						"Chargement de fabrication N°"+selectedDocumentDTO.getCodeDocument()
//						)); 
//			}
//		
//		} catch (FinderException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	public void onRowSelect(SelectEvent event) {  
//		LgrTab b = new LgrTab();
//		b.setTypeOnglet(LgrTab.TYPE_TAB_ABONNEMENT);
//		b.setTitre("Abonnement");
//		b.setTemplate("abonnement/abonnement.xhtml");
//		b.setIdTab(LgrTab.ID_TAB_ABONNEMENT);
//		b.setStyle(LgrTab.CSS_CLASS_TAB_ABONNEMENT);
//		tabsCenter.ajouterOnglet(b);
//		tabViews.selectionneOngletCentre(b);
//		updateTab();
//		scrollToTop();
//
//	} 
//	
//	public boolean editable() {
//		return !modeEcran.dataSetEnModif();
//	}
//	public boolean editableEnInsertionUniquement() {
//		return !modeEcran.dataSetEnInsertion();
//	}
//	
//	public void onRowSelectLigneMP(SelectEvent event) {  
//		selectionLigneMP((TaLAbonnementDTOJSF) event.getObject());		
//	}
//	
//	public void selectionLigneMP(TaLAbonnementDTOJSF ligne){
//		selectedLigneJSF=ligne;
//		if(masterEntity.getIdDocument()!=0 && selectedLigneJSF!=null && selectedLigneJSF.getDto().getIdLDocument()!=null)
//			masterEntityLigneMP=rechercheLigneMP(selectedLigneJSF.getDto().getIdLDocument());
//		else if(selectedLigneJSF!=null && selectedLigneJSF.getDto().getNumLigneLDocument()!=null /*&&  selectedTaProduit.getDto().getNumLigneLDocument()!=0*/) {
//			masterEntityLigneMP = rechercheLigneMPNumLigne(selectedLigneJSF.getDto().getNumLigneLDocument());
//		}
//	}
//
//	public void onRowEditMP(RowEditEvent event) {
//		try {
//			selectedLigneJSF.updateDTO();
//			if(selectedLigneJSF.getDto().getIdLDocument()!=null &&  selectedLigneJSF.getDto().getIdLDocument()!=0) {
//				masterEntityLigneMP = rechercheLigneMP(selectedLigneJSF.getDto().getIdLDocument());
//			} else if(selectedLigneJSF.getDto().getNumLigneLDocument()!=null /*&&  selectedTaMatierePremiere.getDto().getNumLigneLDocument()!=0*/) {
//				masterEntityLigneMP = rechercheLigneMPNumLigne(selectedLigneJSF.getDto().getNumLigneLDocument());
//			}
//			validateLigneFabricationAvantEnregistrement(selectedLigneJSF);
//			actEnregistrerLigne();
//			setInsertAutoPF(modeEcran.dataSetEnInsertion());
//		} catch (Exception e) {
//			e.printStackTrace();
//			FacesContext context = FacesContext.getCurrentInstance();  
//			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fabrication", e.getMessage()));	
//			context.validationFailed();
//			//RequestContext.getCurrentInstance().addCallbackParam("notValid", true);
//			setInsertAutoMP(false);
//			//			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage()));
//		}
//	}
//
//	public void onRowCancelMP(RowEditEvent event) {
//		//((TaLBonReceptionDTOJSF) event.getObject()).setAutoInsert(false);
//		//    FacesMessage msg = new FacesMessage("Edit Cancelled", ((Car) event.getObject()).getId());
//		//    FacesContext.getCurrentInstance().addMessage(null, msg);
//
//		actAnnulerMatierePremiere();
//	}
//
//	public void onRowEditInitMP(RowEditEvent event) {
//		AjaxBehaviorEvent evt = (AjaxBehaviorEvent)event;
//		DataTable table = (DataTable) evt.getSource();
//		int activeRow = table.getRowIndex()+1;
//		if(table.getRowCount() == activeRow) {
//			//derniere ligne
//			setInsertAutoMP(modeEcran.dataSetEnInsertion());
//		} else {
//			setInsertAutoMP(false);
//		}
//		if(event.getObject()!=null && !event.getObject().equals(selectedLigneJSF))
//			selectedLigneJSF=(TaLAbonnementDTOJSF) event.getObject();	
//		actModifierMatierePremiere();
//	}
//	
//	public TaLFabricationMP rechercheLigneMP(int id){
//		TaLFabricationMP obj = masterEntityLigneMP;
//		for (TaLFabricationMP ligne : masterEntity.getLignesMatierePremieres()) {
//			if(ligne.getIdLDocument()==id)
//				obj=ligne;
//		}
//		return obj;
//	}
//	
//	public TaLFabricationMP rechercheLigneMPNumLigne(int numLigne){
//		TaLFabricationMP obj=masterEntityLigneMP;
//		for (TaStripeSubscriptionItem ligne : masterEntity.getItems()) {
//			if(ligne.getNumLigneLDocument()==numLigne)
//				obj=ligne;
//		}
//		return obj;
//	}
//	
//	public void validateLigneFabricationAvantEnregistrement(Object value) throws ValidatorException {
//
//		String msg = "";
//
//		try {
//			if(((TaLAbonnementDTOJSF)value).getDto().getNumLot()==null ||((TaLAbonnementDTOJSF)value).getDto().getNumLot().equals("")){
////				throw new Exception("Le lot doit être renseigné");
//				FacesContext context = FacesContext.getCurrentInstance();  
//				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Lot", "Le lot doit être renseigné"));
//			}
////			taLFabricationService.validateDTOProperty(((TaLAbonnementDTOJSF)value).getDto(),Const.C_CODE_ARTICLE,  ITaLFabricationServiceRemote.validationContext );
////			taLFabricationService.validateDTOProperty(((TaLAbonnementDTOJSF)value).getDto(),Const.C_NUM_LOT,  ITaLFabricationServiceRemote.validationContext );
//		} catch(Exception e) {
//			msg += e.getMessage();
//			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
//		}
//	}
//	
//	public void actAutoInsererLigneMP(ActionEvent actionEvent) /*throws Exception*/ {
//		boolean existeDeja=false;
//		if(insertAutoMP && insertAutoEnabledMP) {
//			for (TaLAbonnementDTOJSF ligne : valuesLigne) {
//				if(ligne.ligneEstVide())existeDeja=true;
//			}
//			if(!existeDeja){
//				actInsererMatierePremiere(actionEvent);
//			
//			String oncomplete="jQuery('.myclassMP .ui-datatable-data tr').last().find('span.ui-icon-pencil').each(function(){jQuery(this).click()});";
//			RequestContext.getCurrentInstance().execute(oncomplete);
//			}
//		} else {
////			throw new Exception();
//		}
//	}
//	//************************************************************************************************************//
//	//	Commun
//	//************************************************************************************************************//
//
//
//	public void setMasterEntity(TaStripeSubscription fabrication) {
//		this.masterEntity = fabrication;
//	}
//
//	public void setSelectedLigneJSF(
//			TaLAbonnementDTOJSF selectedTaMatierePremiere) {
//		this.selectedLigneJSF = selectedTaMatierePremiere;
//	}
//
//	
//	public void setMasterEntityLigneMP(TaLFabricationMP newTaMatierePremiere) {
//		this.masterEntityLigneMP = newTaMatierePremiere;
//	}
//
//	
//
//	public TaStripeSubscription getMasterEntity() {
//		return masterEntity;
//	}
//
//	public TaLAbonnementDTOJSF getSelectedLigneJSF() {
//		return selectedLigneJSF;
//	}
//
//	
//
//	public TaLFabricationMP getMasterEntityLigneMP() {
//		return masterEntityLigneMP;
//	}
//
//
//	public List<ArticleLotEntrepotDTO> getListArticleLotEntrepot() {
//		return listArticleLotEntrepot;
//	}
//
//	public void setListArticleLotEntrepot(List<ArticleLotEntrepotDTO> listArticleLotEntrepot) {
//		this.listArticleLotEntrepot = listArticleLotEntrepot;
//	}
//
//	public List<TaStripeSubscriptionDTO> getListeAbonnement() {
//		return listeAbonnement;
//	}
//
//	public TaStripeSubscriptionDTO getSelectedDocumentDTO() {
//		return selectedDocumentDTO;
//	}
//
//	public void setSelectedDocumentDTO(
//			TaStripeSubscriptionDTO selectedTaStripeSubscriptionDTO) {
//		this.selectedDocumentDTO = selectedTaStripeSubscriptionDTO;
//	}
//
//	public TabListModelBean getTabsCenter() {
//		return tabsCenter;
//	}
//
//	public void setTabsCenter(TabListModelBean tabsCenter) {
//		this.tabsCenter = tabsCenter;
//	}
//
//	public ModeObjetEcranServeur getModeEcranMatierePremiere() {
//		return modeEcranLigne;
//	}
//
//	public ModeObjetEcranServeur getModeEcranProduit() {
//		return modeEcranProduit;
//	}
//
//	public ModeObjetEcranServeur getModeEcran() {
//		return modeEcran;
//	}
//
//	public void setModeEcran(ModeObjetEcranServeur modeEcran) {
//		this.modeEcran = modeEcran;
//	}
//
//	public EtiquetteCodeBarreBean getEtiquetteCodeBarreBean() {
//		return etiquetteCodeBarreBean;
//	}
//
//	public void setEtiquetteCodeBarreBean(
//			EtiquetteCodeBarreBean etiquetteCodeBarreBean) {
//		this.etiquetteCodeBarreBean = etiquetteCodeBarreBean;
//	}
//
//	public TabViewsBean getTabViews() {
//		return tabViews;
//	}
//
//	public void setTabViews(TabViewsBean tabViews) {
//		this.tabViews = tabViews;
//	}
//	
//	public List<TaArticle> articleAutoComplete(String query) {
//		List<TaArticle> allValues = articleService.selectAll();
//		List<TaArticle> filteredValues = new ArrayList<TaArticle>();
//
//		for (int i = 0; i < allValues.size(); i++) {
//			TaArticle civ = allValues.get(i);
//			if(query.equals("*") || civ.getCodeArticle().toLowerCase().contains(query.toLowerCase())) {
//				filteredValues.add(civ);
//			}
//		}
//
//		return filteredValues;
//	}
//	
//	public List<TaArticleDTO> articleAutoCompleteDTOLight(String query) {
//		List<TaArticleDTO> allValues = articleService.findByCodeLight(query.toUpperCase());
//		List<TaArticleDTO> filteredValues = new ArrayList<TaArticleDTO>();
//
//		for (int i = 0; i < allValues.size(); i++) {
//			TaArticleDTO civ = allValues.get(i);
//			if(query.equals("*") || civ.getCodeArticle().toUpperCase().contains(query.toUpperCase())) {
//				filteredValues.add(civ);
//			}
//		}
//
//		return filteredValues;
//	}
//	
//	public List<TaArticleDTO> articleAutoCompleteDTOLightMP(String query) {
//		List<TaArticleDTO> allValues = articleService.findByCodeLightMP(query.toUpperCase());
//		List<TaArticleDTO> filteredValues = new ArrayList<TaArticleDTO>();
//
//		for (int i = 0; i < allValues.size(); i++) {
//			TaArticleDTO civ = allValues.get(i);
//			if(query.equals("*") || civ.getCodeArticle().toUpperCase().contains(query.toUpperCase())) {
//				filteredValues.add(civ);
//			}
//		}
//
//		return filteredValues;
//	}
//
//	
//	public void autcompleteSelection(SelectEvent event) {
//		Object value = event.getObject();
//		//		FacesMessage msg = new FacesMessage("Selected", "Item:" + value);
//
//		String nomChamp =  (String) event.getComponent().getAttributes().get("nomChamp");
//		System.out.println("FabricationController.autcompleteSelection() : "+nomChamp);
//		
//		if(value!=null) {
//			if(value instanceof String && value.equals(Const.C_AUCUN))value="";	
//			if(value instanceof TaTFabricationDTO && ((TaTFabricationDTO) value).getCodeTFabrication()!=null && ((TaTFabricationDTO) value).getCodeTFabrication().equals(Const.C_AUCUN))value=null;	
//			if(value instanceof TaEntrepotDTO && ((TaEntrepotDTO) value).getCodeEntrepot()!=null && ((TaEntrepotDTO) value).getCodeEntrepot().equals(Const.C_AUCUN))value=null;	
//			if(value instanceof TaUniteDTO && ((TaUniteDTO) value).getCodeUnite()!=null && ((TaUniteDTO) value).getCodeUnite().equals(Const.C_AUCUN))value=null;	
//		}
//
//		validateUIField(nomChamp,value);
//	}
//	
//	public void autcompleteUnSelect(UnselectEvent event) {
//		Object value = event.getObject();
//		
//		String nomChamp =  (String) event.getComponent().getAttributes().get("nomChamp");
//
////		if(value!=null) {
////			if(value instanceof String && value.equals(Const.C_AUCUN))value="";	
////			if(value instanceof TaFamilleDTO && ((TaFamilleDTO) value).getCodeFamille()!=null && ((TaFamilleDTO) value).getCodeFamille().equals(Const.C_AUCUN))value=null;	
////			if(value instanceof TaUniteDTO && ((TaUniteDTO) value).getCodeUnite()!=null && ((TaUniteDTO) value).getCodeUnite().equals(Const.C_AUCUN))value=null;
////			if(value instanceof TaTvaDTO && ((TaTvaDTO) value).getCodeTva()!=null && ((TaTvaDTO) value).getCodeTva().equals(Const.C_AUCUN))value=null;
////			if(value instanceof TaTTvaDTO && ((TaTTvaDTO) value).getCodeTTva()!=null && ((TaTTvaDTO) value).getCodeTTva().equals(Const.C_AUCUN))value=null;	
////			if(value instanceof TaMarqueArticle && ((TaMarqueArticle) value).getCodeMarqueArticle()!=null && ((TaMarqueArticle) value).getCodeMarqueArticle().equals(Const.C_AUCUN))value=null;	
////		}
////		
////		//validateUIField(nomChamp,value);
//		try {
//			if(nomChamp.equals(Const.C_CODE_LABEL_ARTICLE)) {
////				TaLabelArticle entity =null;
////				if(value!=null){
////					if(value instanceof TaLabelArticleDTO){
////	//				entity=(TaFamille) value;
////						entity = taLabelArticleService.findByCode(((TaLabelArticleDTO)value).getCodeLabelArticle());
////					}else{
////						entity = taLabelArticleService.findByCode((String)value);
////					}
////				}else{
//////					selectedTaStripeSubscriptionDTO.setCodeFamille("");
////				}
////				//taArticle.setTaFamille(entity);
////				for (TaLabelArticle f : masterEntity.getTaLabelArticles()) {
////					if(f.getIdLabelArticle()==((TaLabelArticleDTO)value).getId())
////						entity = f;
////				}
////				masterEntity.getTaLabelArticles().remove(entity);
//////				if(masterEntity.getTaFamille()!=null && masterEntity.getTaFamille().getCodeFamille().equals(entity.getCodeFamille())) {
//////					masterEntity.setTaFamille(null);
//////					taFamilleDTO = null;
//////					if(!masterEntity.getTaLabelArticles().isEmpty()) {
//////						masterEntity.setTaFamille(masterEntity.getTaLabelArticles().iterator().next());
//////						//taFamilleDTO = null;
//////					}
//////				}
//			}
//			
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//	
//	}
//	
//	public List<TaTiersDTO> tiersAutoCompleteDTOLight(String query) {
//		List<TaTiersDTO> allValues = tiersService.findByCodeLight(query);
//		List<TaTiersDTO> filteredValues = new ArrayList<TaTiersDTO>();
//
//		for (int i = 0; i < allValues.size(); i++) {
//			TaTiersDTO civ = allValues.get(i);
//			if(query.equals("*") || civ.getCodeTiers().toLowerCase().contains(query.toLowerCase())) {
//				filteredValues.add(civ);
//			}
//		}
//
//		return filteredValues;
//	}
//	
//	
//	
//	public void autcompleteSelectionMP(SelectEvent event) {
//		Object value = event.getObject();
//		//		FacesMessage msg = new FacesMessage("Selected", "Item:" + value);
//
//		String nomChamp =  (String) event.getComponent().getAttributes().get("nomChamp");
//		System.out.println("FabricationController.autcompleteSelectionMP() : "+nomChamp);
//
//		validateUIFieldMP(nomChamp,value);
//	}
//
//	public void autoCompleteMapUIToDTO() {
////		if(taTFabricationDTO!=null) {
////			validateUIField(Const.C_CODE_T_FABRICATION,taTFabricationDTO);
////			selectedTaStripeSubscriptionDTO.setCodeTFabrication(taTFabricationDTO.getCodeTFabrication());
////		}
//		if(taTiersDTO!=null) {
//			validateUIField(Const.C_CODE_TIERS_PRESTATION,taTiersDTO);
//			selectedDocumentDTO.setCodeTiers(taTiersDTO.getCodeTiers());
//		}
////		if(redacteurDTO!=null) {
////			validateUIField(Const.C_NOM_REDACTEUR,redacteurDTO);
////			selectedTaStripeSubscriptionDTO.setNomRedacteur(redacteurDTO.getEmail());
////		}
////		if(controleurDTO!=null) {
////			validateUIField(Const.C_NOM_CONTROLEUR,controleurDTO);
////			selectedTaStripeSubscriptionDTO.setNomControleur(controleurDTO.getEmail());
////		}
//	}
//
//	public void autoCompleteMapDTOtoUI() {
//		try {
////			taTFabrication = null;
////			taTFabricationDTO = null;
//			
//			taTiers = null;
//			taTiersDTO = null;
//			
//			
//			
//			//init des DTO multiple (autocomplete multiple)
////			if(masterEntity!=null && masterEntity.getTaLabelArticles()!=null && !masterEntity.getTaLabelArticles().isEmpty()) {
////				selectedTaStripeSubscriptionDTO.setTaLabelArticleDTOs(new ArrayList<>());
////				for (TaLabelArticle f : masterEntity.getTaLabelArticles()) {
////					selectedTaStripeSubscriptionDTO.getTaLabelArticleDTOs().add(mapperCertification.map(f, TaLabelArticleDTO.class));
////				}
////			}
////
////			if(selectedTaStripeSubscriptionDTO.getCodeTFabrication()!=null && !selectedTaStripeSubscriptionDTO.getCodeTFabrication().equals("")) {
////				taTFabrication = taTFabricationService.findByCode(selectedTaStripeSubscriptionDTO.getCodeTFabrication());
////				taTFabricationDTO = mapperModelToUITFabrication.map(taTFabrication, TaTFabricationDTO.class);
////			}
//			
//			if(selectedDocumentDTO.getCodeTiers()!=null && !selectedDocumentDTO.getCodeTiers().equals("")) {
//				taTiers = tiersService.findByCode(selectedDocumentDTO.getCodeTiers());
//				taTiersDTO = mapperModelToUITiers.map(taTiers, TaTiersDTO.class);
//			}
////			
////			if(selectedTaStripeSubscriptionDTO.getNomRedacteur()!=null && !selectedTaStripeSubscriptionDTO.getNomRedacteur().equals("")) {
////				redacteur = utilisateurService.findByCode(selectedTaStripeSubscriptionDTO.getNomRedacteur());
////				redacteurDTO = mapperModelToUIUtilisateur.map(redacteur, TaUtilisateurDTO.class);
////			}
////			
////			if(selectedTaStripeSubscriptionDTO.getNomControleur()!=null && !selectedTaStripeSubscriptionDTO.getNomControleur().equals("")) {
////				controleur = utilisateurService.findByCode(selectedTaStripeSubscriptionDTO.getNomControleur());
////				controleurDTO = mapperModelToUIUtilisateur.map(controleur, TaUtilisateurDTO.class);
////			}
////			if(selectedTaStripeSubscriptionDTO.getTaLabelArticleDTOs()!=null) {
////				if(taLabelArticles==null) {
////					taLabelArticles = new ArrayList<>();
////				}
////				if(selectedTaStripeSubscriptionDTO.getTaLabelArticleDTOs()==null) {
////					selectedTaStripeSubscriptionDTO.setTaLabelArticleDTOs(new ArrayList<>());
////				}
////				for (TaLabelArticleDTO f : selectedTaStripeSubscriptionDTO.getTaLabelArticleDTOs()) {
////					if(f.getCodeLabelArticle()!=null && !f.getCodeLabelArticle().equals("")) {
////						taLabelArticle = taLabelArticleService.findByCode(f.getCodeLabelArticle());
////						taLabelArticles.add(taLabelArticle);
////						
////					}
////				}
////			}
//		
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//
//	public boolean validateUIField(String nomChamp,Object value) {
//
//		boolean changement=false;
//
//		try {				
//			if(nomChamp.equals(Const.C_CODE_TIERS_PRESTATION)) {
//				TaTiers entity = null;
//				if(value!=null){
//					if(value instanceof TaTiersDTO){
////						entity=(TaTiers) value;
//						entity = tiersService.findByCode(((TaTiersDTO)value).getCodeTiers());
//					}else{
//						entity = tiersService.findByCode((String)value);
//					}
//					
//				}
//				changement=masterEntity.getTaTiersPrestationService()==null || !entity.equalsCode(masterEntity.getTaTiersPrestationService());
//				masterEntity.setTaTiersPrestationService(entity);
//
//				if(changement){
//					String nomTiers=entity.getNomTiers();
//					((TaStripeSubscriptionDTO)selectedDocumentDTO).setLibelleDocument("Fabrication N°"+selectedDocumentDTO.getCodeDocument()+" - pour "+nomTiers);																			
//				}					
//
//			} else if(nomChamp.equals(Const.C_CODE_T_FABRICATION)) {
//				TaTFabrication entity = null;
//				if(value!=null){
//					if(value instanceof TaTFabricationDTO){
////						entity=(TaTiers) value;
//						entity = taTFabricationService.findByCode(((TaTFabricationDTO)value).getCodeTFabrication());
//					}else{
//						entity = taTFabricationService.findByCode((String)value);
//					}
//					changement=!entity.equalsCode(masterEntity.getTaTFabrication());
//					masterEntity.setTaTFabrication(entity);
//				}else {
//					changement=masterEntity.getTaTFabrication()!=null;
//					masterEntity.setTaTFabrication(null);
//					selectedDocumentDTO.setCodeTFabrication("");
//					selectedDocumentDTO.setLiblTFabrication("");
//					taTFabricationDTO=null;
//					
//				}
//				
//				if(changement){
//					Map<String, String> params = new LinkedHashMap<String, String>();
//					if(masterEntity!=null && masterEntity.getTaTFabrication()!=null)
//						params.put(Const.C_CODETYPE, masterEntity.getTaTFabrication().getCodeTFabrication());						
//					if(selectedDocumentDTO.getCodeDocument()!=null) {
//						taStripeSubscriptionService.annuleCode(selectedDocumentDTO.getCodeDocument());
//					}
//					String newCode=taStripeSubscriptionService.genereCode(params);
//					if(!newCode.equals(""))selectedDocumentDTO.setCodeDocument(newCode);					
//				}
//			}else if(nomChamp.equals(Const.C_NUM_LOT)) {
//			
//			}else if(nomChamp.equals(Const.C_NOM_REDACTEUR)) {
//				TaUtilisateur entity = null;
//				if(value!=null){
//					if(value instanceof TaUtilisateurDTO){
////						entity=(TaUtilisateur) value;
//						entity = utilisateurService.findByCode(((TaUtilisateurDTO)value).getEmail());
//					}else{
//						entity = utilisateurService.findByCode((String)value);
//					}
//					
//				}
//				masterEntity.setTaUtilisateurRedacteur(entity);
//			}else if(nomChamp.equals(Const.C_NOM_CONTROLEUR)) {
//				TaUtilisateur entity = null;
//				if(value!=null){
//					if(value instanceof TaUtilisateurDTO){
////						entity=(TaUtilisateur) value;
//						entity = utilisateurService.findByCode(((TaUtilisateurDTO)value).getEmail());
//					}else{
//						entity = utilisateurService.findByCode((String)value);
//					}
//					
//				}
//				masterEntity.setTaUtilisateurControleur(entity);
//			}else if(nomChamp.equals(Const.C_DATE_DOCUMENT)) {
//				
//			}else if(nomChamp.equals(Const.C_CODE_LABEL_ARTICLE)) {
//				TaLabelArticle entity =null;
//				if(value!=null){
//					if(value instanceof TaLabelArticleDTO){
//						entity = taLabelArticleService.findByCode(((TaLabelArticleDTO)value).getCodeLabelArticle());
//						masterEntity.getTaLabelArticles().add(entity);
//						//selectedTaStripeSubscriptionDTO.getTaLabelArticleDTOs().add((TaLabelArticleDTO)value);
//					}else{
//						entity = taLabelArticleService.findByCode((String)value);
//					}
//				}
//			} else if(nomChamp.equals(Const.C_CODE_ARTICLE)) {
//				boolean changementArticleLigne = false;
////				TaArticle entity = null;
////				if(value!=null){
////					if(value instanceof TaArticle){
////						entity=(TaArticle) value;
////						entity = articleService.findByCode(entity.getCodeArticle());
////					}else{
////						entity = articleService.findByCode((String)value);
////					}
////				}
//				TaArticle entity = null;
//				if(value!=null){
//					if(value instanceof TaArticleDTO){
//						entity = articleService.findByCode(((TaArticleDTO) value).getCodeArticle());
//					}else{
//						entity = articleService.findByCode((String)value);
//					}
//				}
//				if(selectedTaProduit.getTaArticle()== null || selectedTaProduit.getTaArticle().getIdArticle()!=entity.getIdArticle()) {
//					changementArticleLigne = true;
//				}
//				selectedTaProduit.setTaArticle(entity);
//
//				if(changementArticleLigne) {
//					selectedTaProduit.getDto().setLibLDocument(entity.getLibellecArticle());
//					selectedTaProduit.getDto().setDluo(LibDate.incrementDate(selectedDocumentDTO.getDateDocument(), LibConversion.stringToInteger(entity.getParamDluo(), 0) , 0, 0));
//					selectedTaProduit.getDto().setLibelleLot(entity.getLibellecArticle());
//
//					if(selectedTaProduit.getDto().getNumLot()!=null)lotService.annuleCode(selectedTaProduit.getDto().getNumLot());
//
//					selectedTaProduit.getDto().setNumLot(selectedDocumentDTO.getCodeDocument()+"_"+selectedTaProduit.getDto().getNumLigneLDocument());					
//					taGenCodeExService.libereVerrouTout(new ArrayList<String>(SessionListener.getSessions().keySet()));
//					Map<String, String> params = new LinkedHashMap<String, String>();
//					if(masterEntity!=null && masterEntity.getTaTiers()!=null)params.put(Const.C_NOMFOURNISSEUR, masterEntity.getTaTiers().getNomTiers());
//					if(masterEntity!=null && masterEntity.getTaTiers()!=null)params.put(Const.C_CODEFOURNISSEUR, masterEntity.getTaTiers().getCodeTiers());
//					params.put(Const.C_CODEDOCUMENT, selectedDocumentDTO.getCodeDocument());
//					if(!entity.getGestionLot()){
//						params.put(Const.C_VIRTUEL, "true");
//						params.put(Const.C_IDARTICLEVIRTUEL, LibConversion.integerToString(entity.getIdArticle()));
//					}
//					selectedTaProduit.getDto().setNumLot(lotService.genereCode(params));
//				}
//				
//				selectedTaProduit.setTaUnite1(null);
//				selectedTaProduit.setTaUnite2(null);
//				TaRapportUnite rapport=entity.getTaRapportUnite();
//				TaCoefficientUnite coef = null;
//				if(rapport!=null){
//					if(rapport.getTaUnite2()!=null)
//						coef=taCoefficientUniteService.findByCode(rapport.getTaUnite1().getCodeUnite(),rapport.getTaUnite2().getCodeUnite());
//				}
//				if(coef!=null){
//					selectedTaProduit.setTaCoefficientUnite(coef);
//					if(coef.getUniteA()!=null) {
//						selectedTaProduit.getDto().setU1LDocument(coef.getUniteA().getCodeUnite());
//						selectedTaProduit.setTaUnite1(taUniteService.findByCode(coef.getUniteA().getCodeUnite()));
//					}
//					if(coef.getUniteB()!=null) {
//						selectedTaProduit.getDto().setU2LDocument(coef.getUniteB().getCodeUnite());
//						selectedTaProduit.setTaUnite2(taUniteService.findByCode(coef.getUniteB().getCodeUnite()));
//					}
//				}else if(entity!=null){
//					if(entity.getTaUnite1()!=null) {
//						selectedTaProduit.getDto().setU1LDocument(entity.getTaUnite1().getCodeUnite());
//						selectedTaProduit.setTaUnite1(taUniteService.findByCode(entity.getTaUnite1().getCodeUnite()));
//					}
//					for (TaRapportUnite obj : entity.getTaRapportUnites()) {
//						if(obj.getTaUnite1()!=null && obj.getTaUnite1().getCodeUnite().equals(selectedTaProduit.getDto().getU1LDocument())){
//							if(obj!=null){
//								if(obj.getTaUnite2()!=null) {										
//									coef=recupCoefficientUnite(obj.getTaUnite1().getCodeUnite(),obj.getTaUnite2().getCodeUnite());
//								}
//							}
//							selectedTaProduit.setTaCoefficientUnite(coef);
//							System.out.println("coef :"+coef);
//							if(coef!=null) {
//								selectedTaProduit.getDto().setU2LDocument(coef.getUniteB().getCodeUnite());
//								selectedTaProduit.setTaUnite2(taUniteService.findByCode(coef.getUniteB().getCodeUnite()));
//							}
//						}							
//					}
//				}
//				if(entity.getTaPrix()!=null){
//					if(entity.getTaUnite1()!=null) {
//						selectedTaProduit.getDto().setU1LDocument(entity.getTaUnite1().getCodeUnite());
//						selectedTaProduit.setTaUnite1(taUniteService.findByCode(entity.getTaUnite1().getCodeUnite()));
//					}
//					for (TaRapportUnite obj : entity.getTaRapportUnites()) {
//						if(obj.getTaUnite1()!=null && obj.getTaUnite1().getCodeUnite().equals(selectedTaProduit.getDto().getU1LDocument())){
//							if(obj!=null){
//								if(obj.getTaUnite2()!=null)
//									coef=taCoefficientUniteService.findByCode(obj.getTaUnite1().getCodeUnite(),obj.getTaUnite2().getCodeUnite());
//							}
//							selectedTaProduit.setTaCoefficientUnite(coef);
//							if(coef!=null) {
//								selectedTaProduit.getDto().setU2LDocument(coef.getUniteB().getCodeUnite());
//								selectedTaProduit.setTaUnite2(taUniteService.findByCode(coef.getUniteB().getCodeUnite()));
//							}
//							if(selectedTaProduit.getTaCoefficientUnite()!=null)
//								selectedTaProduit.getTaCoefficientUnite().recupRapportEtSens(selectedTaProduit.getDto().getU1LDocument(),selectedTaProduit.getDto().getU2LDocument());
//						}							
//					}
//				}
//				
//			}else if(nomChamp.equals(Const.C_CODE_ENTREPOT)) {
//				TaEntrepot entity =null;
//				if(value!=null){
//					if(value instanceof TaEntrepot){
//						entity=(TaEntrepot) value;
//						entity = entrepotService.findByCode(entity.getCodeEntrepot());
//					}else{
//						entity = entrepotService.findByCode((String)value);
//					}
//					masterEntityLignePF.setTaEntrepot(entity);
//				}else{
//					masterEntityLignePF.setTaEntrepot(null);
//					selectedTaProduit.getDto().setCodeEntrepot("");
//				}
//				
//		
//			}
//			 else if(nomChamp.equals(Const.C_QTE_L_DOCUMENT)) {
////				if(value!=null && selectedTaProduit.getTaRapport()!=null && selectedTaProduit.getTaRapport().getRapport()!=null){
////					switch (selectedTaProduit.getTaRapport().getSens()) {
////					case 1:
////						selectedTaProduit.getDto().setQte2LDocument(((BigDecimal)value).multiply(selectedTaProduit.getTaRapport().getRapport()));
////						break;
////					case 0:
////						selectedTaProduit.getDto().setQte2LDocument(((BigDecimal)value).divide(selectedTaProduit.getTaRapport().getRapport()
////								,MathContext.DECIMAL128).setScale(selectedTaProduit.getTaRapport().getNbDecimal(),BigDecimal.ROUND_HALF_UP));
////						break;
////					default:
////						break;
////					} 
//					BigDecimal qte=BigDecimal.ZERO;
//					if(value!=null){
//						if(!value.equals("")){
//							qte=(BigDecimal)value;
//						}
//						selectedTaProduit.setTaCoefficientUnite(recupCoefficientUnite(selectedTaProduit.getDto().getU1LDocument(),selectedTaProduit.getDto().getU2LDocument()));
//						if(selectedTaProduit.getTaCoefficientUnite()!=null) {
//							if(selectedTaProduit.getTaCoefficientUnite().getOperateurDivise()) 
//								selectedTaProduit.getDto().setQte2LDocument((qte).divide(selectedTaProduit.getTaCoefficientUnite().getCoeff()
//										,MathContext.DECIMAL128).setScale(selectedTaProduit.getTaCoefficientUnite().getNbDecimale(),BigDecimal.ROUND_HALF_UP));
//							else selectedTaProduit.getDto().setQte2LDocument((qte).multiply(selectedTaProduit.getTaCoefficientUnite().getCoeff()));
//						}
//				} else {
//					masterEntityLignePF.setQte2LDocument(null);
//				}
//				
//			} else if(nomChamp.equals(Const.C_U1_L_DOCUMENT)) {
//				TaUnite entity =null;
//				if(value!=null){
//					if(value instanceof TaUnite){
//						entity=(TaUnite) value;
//						entity = uniteService.findByCode(entity.getCodeUnite());
//					}else{
//						entity = uniteService.findByCode((String)value);
//					}
//					masterEntityLignePF.setU1LDocument(entity.getCodeUnite());
//					selectedTaProduit.getDto().setU1LDocument(entity.getCodeUnite());
//				}else{
//					masterEntityLignePF.setU1LDocument(null);
//					selectedTaProduit.getDto().setU1LDocument("");
//				}
//				selectedTaProduit.setTaCoefficientUnite(recupCoefficientUnite(selectedTaProduit.getDto().getU1LDocument(),selectedTaProduit.getDto().getU2LDocument()));
//				validateUIField(Const.C_QTE_L_DOCUMENT, selectedTaProduit.getDto().getQteLDocument());
//			} 		
//			 else if(nomChamp.equals(Const.C_QTE2_L_DOCUMENT)) {
//					BigDecimal qte=BigDecimal.ZERO;
//					if(value==null) {
//						masterEntityLignePF.setQte2LDocument(null);
//					}else if(!value.equals("")){
//						qte=(BigDecimal)value;
//					}
//					selectedTaProduit.getDto().setQte2LDocument(qte);
//				
//			} else if(nomChamp.equals(Const.C_U2_L_DOCUMENT)) {
//				TaUnite entity =null;
//				if(value!=null){
//					if(value instanceof TaUnite){
//						entity=(TaUnite) value;
//						entity = uniteService.findByCode(entity.getCodeUnite());
//					}else{
//						entity = uniteService.findByCode((String)value);
//					}
//					masterEntityLignePF.setU2LDocument(entity.getCodeUnite());
//					selectedTaProduit.getDto().setU2LDocument(entity.getCodeUnite());
//				}else{
//					masterEntityLignePF.setU2LDocument(null);
//					selectedTaProduit.getDto().setU2LDocument("");
//				}
//				selectedTaProduit.setTaCoefficientUnite(recupCoefficientUnite(selectedTaProduit.getDto().getU1LDocument(),selectedTaProduit.getDto().getU2LDocument()));
//				validateUIField(Const.C_QTE_L_DOCUMENT, selectedTaProduit.getDto().getQteLDocument());
//			}			
//			return false;
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return false;
//	}
//	
//	public boolean validateUIFieldMP(String nomChamp,Object value) {
//
//		boolean changement=false;
//
//		try {				
//			if(nomChamp.equals(Const.C_CODE_TIERS)) {
////				TaTiers entity = null;
////				if(value!=null){
////					entity=(TaTiers) value;
////					entity = tiersService.findByCode(entity.getCodeTiers());
////				}
////				masterEntity.setTaTiers(entity);
//
//			}if(nomChamp.equals(Const.C_CODE_T_FABRICATION)) {
////				TaTFabrication entity = null;
////				if(value!=null){
////					if(value instanceof TaTFabricationDTO){
//////						entity=(TaTiers) value;
////						entity = taTFabricationService.findByCode(((TaTFabricationDTO)value).getCodeTFabrication());
////					}else{
////						entity = taTFabricationService.findByCode((String)value);
////					}
////				}
////				masterEntity.setTaTFabrication(entity);
////				Map<String, String> params = new LinkedHashMap<String, String>();
////				if(masterEntity!=null && masterEntity.getTaTFabrication()!=null)
////					params.put(Const.C_CODETYPE, masterEntity.getTaTFabrication().getCodeTFabrication());
////				if(selectedTaStripeSubscriptionDTO.getCodeDocument()!=null) {
////					taStripeSubscriptionService.annuleCode(selectedTaStripeSubscriptionDTO.getCodeDocument());
////				}				
////				String newCode=taStripeSubscriptionService.genereCode(params);
////				if(!newCode.equals(""))selectedTaStripeSubscriptionDTO.setCodeDocument(newCode);
//				
//		}else if(nomChamp.equals(Const.C_NUM_LOT)) {
////				selectedTaMatierePremiere.getDto().setEmplacement(null);
////				if( selectedTaMatierePremiere.getArticleLotEntrepotDTO().getEmplacement()!=null 
////						&& !selectedTaMatierePremiere.getArticleLotEntrepotDTO().getEmplacement().equals("")) {
////					selectedTaMatierePremiere.getDto().setEmplacement(selectedTaMatierePremiere.getArticleLotEntrepotDTO().getEmplacement());
////				}
////				selectedTaMatierePremiere.getDto().setCodeEntrepot(selectedTaMatierePremiere.getArticleLotEntrepotDTO().getCodeEntrepot());
////				
////				TaEntrepot entity =null;
////				entity = entrepotService.findByCode(selectedTaMatierePremiere.getArticleLotEntrepotDTO().getCodeEntrepot());
////				selectedTaMatierePremiere.setTaEntrepot(entity);
////				
////				TaLot lot =null;
////				lot = lotService.findByCode(selectedTaMatierePremiere.getArticleLotEntrepotDTO().getNumLot());
////				selectedTaMatierePremiere.setTaLot(lot);
////				selectedTaMatierePremiere.getDto().setLibLDocument(lot.getLibelle());
////				if(selectedTaMatierePremiere.getDto().getLibLDocument()==null || selectedTaMatierePremiere.getDto().getLibLDocument().isEmpty())
////					selectedTaMatierePremiere.getDto().setLibLDocument("lot : "+lot.getNumLot());
//			}else if(nomChamp.equals(Const.C_DATE_DOCUMENT)) {
//				
//			}
//			else if(nomChamp.equals(Const.C_CODE_ARTICLE)) {
//				TaArticle entity = null;
//				if(value!=null){
//					if(value instanceof TaArticleDTO){
//						entity = articleService.findByCode(((TaArticleDTO) value).getCodeArticle());
//					}else{
//						entity = articleService.findByCode((String)value);
//					}
//				}
//				changement=selectedLigneJSF.getTaArticle()==null || !selectedLigneJSF.getTaArticle().equalsCodeArticle(entity);
//				selectedLigneJSF.setTaArticle(entity);
//				if(changement){
//					if(selectedLigneJSF.getArticleLotEntrepotDTO()!=null)
//						selectedLigneJSF.getArticleLotEntrepotDTO().setNumLot(null);
//					selectedLigneJSF.setTaLot(null);
//					selectedLigneJSF.getDto().setNumLot(null);
//					if(!entity.getGestionLot()){
//						ArticleLotEntrepotDTO articleLotEntrepotDTO;
//						List<ArticleLotEntrepotDTO> liste= lotAutoComplete("");
//						if(liste!=null && !liste.isEmpty()){
//							articleLotEntrepotDTO=liste.get(0);
//							articleLotEntrepotDTO.setGestionLot(false);
//							TaLot lot=lotService.findByCode(articleLotEntrepotDTO.getNumLot());
//							selectedLigneJSF.setArticleLotEntrepotDTO(articleLotEntrepotDTO);
//							selectedLigneJSF.setTaLot(lot);
//							selectedLigneJSF.getDto().setNumLot(articleLotEntrepotDTO.getNumLot());
//							selectedLigneJSF.getDto().setCodeEntrepot(articleLotEntrepotDTO.getCodeEntrepot());
//							selectedLigneJSF.getDto().setEmplacement(articleLotEntrepotDTO.getEmplacement());
//						}
//					}else{
//						if(selectedLigneJSF.getArticleLotEntrepotDTO()!=null)
//							selectedLigneJSF.setArticleLotEntrepotDTO(new ArticleLotEntrepotDTO());
//					}
//				}
//				//masterEntityLigne.setTaArticle(entity);
//				selectedLigneJSF.getDto().setLibLDocument(entity.getLibellecArticle());
//				selectedLigneJSF.setTaUnite1(null);
//				selectedLigneJSF.setTaUnite2(null);
//				TaRapportUnite rapport=entity.getTaRapportUnite();
//				TaCoefficientUnite coef = null;
//				if(rapport!=null){
//					if(rapport.getTaUnite1()!=null && rapport.getTaUnite2()!=null) {
//						coef=recupCoefficientUnite(rapport.getTaUnite1().getCodeUnite(),rapport.getTaUnite2().getCodeUnite());
//						selectedLigneJSF.setTaCoefficientUnite(coef);
//					}
//				}
//				if(coef!=null){
//					selectedLigneJSF.setTaCoefficientUnite(coef);
//					if(coef.getUniteA()!=null) {
//						selectedLigneJSF.getDto().setU1LDocument(coef.getUniteA().getCodeUnite());
//						selectedLigneJSF.setTaUnite1(taUniteService.findByCode(coef.getUniteA().getCodeUnite()));
//					}
//					if(coef.getUniteB()!=null) {
//						selectedLigneJSF.getDto().setU2LDocument(coef.getUniteB().getCodeUnite());
//						selectedLigneJSF.setTaUnite2(taUniteService.findByCode(coef.getUniteB().getCodeUnite()));
//					}
//				}else if(entity!=null){
//					if(entity.getTaUnite1()!=null) {
//						selectedLigneJSF.getDto().setU1LDocument(entity.getTaUnite1().getCodeUnite());
//						selectedLigneJSF.setTaUnite1(taUniteService.findByCode(entity.getTaUnite1().getCodeUnite()));
//					}
//					for (TaRapportUnite obj : entity.getTaRapportUnites()) {
//						if(obj.getTaUnite1()!=null && obj.getTaUnite1().getCodeUnite().equals(selectedLigneJSF.getDto().getU1LDocument())){
//							if(obj!=null){
//								if(obj.getTaUnite2()!=null) {										
//									coef=recupCoefficientUnite(obj.getTaUnite1().getCodeUnite(),obj.getTaUnite2().getCodeUnite());
//								}
//							}
//							selectedLigneJSF.setTaCoefficientUnite(coef);
//							System.out.println("coef :"+coef);
//							if(coef!=null) {
//								selectedLigneJSF.getDto().setU2LDocument(coef.getUniteB().getCodeUnite());
//								selectedLigneJSF.setTaUnite2(taUniteService.findByCode(coef.getUniteB().getCodeUnite()));
//							}
//						}							
//					}
//				}
//				if(entity.getTaPrix()!=null){
//					if(entity.getTaUnite1()!=null) {
//						selectedLigneJSF.getDto().setU1LDocument(entity.getTaUnite1().getCodeUnite());
//						selectedLigneJSF.setTaUnite1(taUniteService.findByCode(entity.getTaUnite1().getCodeUnite()));
//					}
//					for (TaRapportUnite obj : entity.getTaRapportUnites()) {
//						if(obj.getTaUnite1()!=null && obj.getTaUnite1().getCodeUnite().equals(selectedLigneJSF.getDto().getU1LDocument())){
//							if(obj!=null){
//								if(obj.getTaUnite2()!=null) {
//									coef=recupCoefficientUnite(obj.getTaUnite1().getCodeUnite(),obj.getTaUnite2().getCodeUnite());
//								}
//							}
//							selectedLigneJSF.setTaCoefficientUnite(coef);
//							if(coef!=null) {
//								selectedLigneJSF.getDto().setU2LDocument(coef.getUniteB().getCodeUnite());
//								selectedLigneJSF.setTaUnite2(taUniteService.findByCode(coef.getUniteB().getCodeUnite()));
//							}
//						}							
//					}
////					for (TaRapportUnite obj : entity.getTaRapportUnites()) {
////						if(obj.getTaUnite1()!=null && obj.getTaUnite1().getCodeUnite().equals(selectedTaMatierePremiere.getDto().getU1LDocument())){
////							selectedTaMatierePremiere.setTaRapport(obj);
////							selectedTaMatierePremiere.getDto().setU2LDocument(obj.getTaUnite2().getCodeUnite());
////							selectedTaMatierePremiere.setTaUnite2(taUniteService.findByCode(obj.getTaUnite2().getCodeUnite()));
////						}							
////					}
//				}
//				
//			}else if(nomChamp.equals(Const.C_CODE_ENTREPOT)) {
////				TaEntrepot entity =null;
////				if(value!=null){
////					entity=(TaEntrepot) value;
////					entity = entrepotService.findByCode(entity.getCodeEntrepot());
////				}
////				masterEntityLigneMP.setTaEntrepot(entity);
//		
//			}
//			 else if(nomChamp.equals(Const.C_QTE_L_DOCUMENT)) {
//					BigDecimal qte=BigDecimal.ZERO;
//					if(value!=null){
//						if(!value.equals("")){
//							qte=(BigDecimal)value;
//						}
//						selectedLigneJSF.setTaCoefficientUnite(recupCoefficientUnite(selectedLigneJSF.getDto().getU1LDocument(),selectedLigneJSF.getDto().getU2LDocument()));
//						if(selectedLigneJSF.getTaCoefficientUnite()!=null) {
//							if(selectedLigneJSF.getTaCoefficientUnite().getOperateurDivise()) 
//								selectedLigneJSF.getDto().setQte2LDocument((qte).divide(selectedLigneJSF.getTaCoefficientUnite().getCoeff()
//										,MathContext.DECIMAL128).setScale(selectedLigneJSF.getTaCoefficientUnite().getNbDecimale(),BigDecimal.ROUND_HALF_UP));
//							else selectedLigneJSF.getDto().setQte2LDocument((qte).multiply(selectedLigneJSF.getTaCoefficientUnite().getCoeff()));
//						}
//
//				} else {
//					masterEntityLigneMP.setQte2LDocument(null);
//				}
//					masterEntityLigneMP.setQteLDocument(qte);
////				if(value!=null && selectedTaMatierePremiere.getTaRapport()!=null && selectedTaMatierePremiere.getTaRapport().getRapport()!=null && selectedTaMatierePremiere.getTaRapport().getSens()!=null){
////					switch (selectedTaMatierePremiere.getTaRapport().getSens()) {
////					case 1:
////						selectedTaMatierePremiere.getDto().setQte2LDocument(((BigDecimal)value).multiply(selectedTaMatierePremiere.getTaRapport().getRapport()));
////						break;
////					case 0:
////						selectedTaMatierePremiere.getDto().setQte2LDocument(((BigDecimal)value).divide(selectedTaMatierePremiere.getTaRapport().getRapport()
////								,MathContext.DECIMAL128).setScale(selectedTaMatierePremiere.getTaRapport().getNbDecimal(),BigDecimal.ROUND_HALF_UP));
////						break;
////					default:
////						break;
////					} 
////				} else {
////					masterEntityLigneMP.setQte2LDocument(null);
////				}
//				
//			} else if(nomChamp.equals(Const.C_U1_L_DOCUMENT)) {
//				TaUnite entity =null;
//				if(value!=null){
//					entity=(TaUnite) value;
//					entity = uniteService.findByCode(entity.getCodeUnite());
//				}
//				masterEntityLigneMP.setU1LDocument(entity.getCodeUnite());
//				selectedLigneJSF.getDto().setU1LDocument(entity.getCodeUnite());
//				selectedLigneJSF.setTaCoefficientUnite(recupCoefficientUnite(selectedLigneJSF.getDto().getU1LDocument(),selectedLigneJSF.getDto().getU2LDocument()));
//				validateUIField(Const.C_QTE_L_DOCUMENT, selectedLigneJSF.getDto().getQteLDocument());
//			} 			
//			return false;
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return false;
//	}
//
//	
//	public void validateLignesMP(FacesContext context, UIComponent component, Object value) throws ValidatorException {
//
//
//		String messageComplet = "";
//		try {
//			String nomChamp =  (String) component.getAttributes().get("nomChamp");
//			//validation automatique via la JSR bean validation
//			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//			Set<ConstraintViolation<TaLFabricationDTO>> violations = factory.getValidator().validateValue(TaLFabricationDTO.class,nomChamp,value);
//			if (violations.size() > 0) {
//				messageComplet = "Erreur de validation : ";
//				for (ConstraintViolation<TaLFabricationDTO> cv : violations) {
//					messageComplet+=" "+cv.getMessage()+"\n";
//				}
//				throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, messageComplet, messageComplet));
//			} else {
//				//aucune erreur de validation "automatique", on déclanche les validations du controller
//				validateUIFieldMP(nomChamp,value);
//			}
//		} catch(Exception e) {
//			//messageComplet += e.getMessage();
//			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, messageComplet, messageComplet));
//		}
//	}
//	
//	public void validateDocument(FacesContext context, UIComponent component, Object value) throws ValidatorException {
//
//		
//		String messageComplet = "";
//		try {
//			String nomChamp =  (String) component.getAttributes().get("nomChamp");
//			//validation automatique via la JSR bean validation
//			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//			Set<ConstraintViolation<TaStripeSubscriptionDTO>> violations = factory.getValidator().validateValue(TaStripeSubscriptionDTO.class,nomChamp,value);
//			if (violations.size() > 0) {
//				messageComplet = "Erreur de validation : ";
//				for (ConstraintViolation<TaStripeSubscriptionDTO> cv : violations) {
//					messageComplet+=" "+cv.getMessage()+"\n";
//				}
//				throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, messageComplet, messageComplet));
//			} else {
//				//aucune erreur de validation "automatique", on déclanche les validations du controller
//				validateUIField(nomChamp,value);
//			}
//		} catch(Exception e) {
//			//messageComplet += e.getMessage();
//			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, messageComplet, messageComplet));
//		}
//	}
//
//	public void validateDocumentAvantEnregistrement( Object value) throws ValidatorException {
//
//		String msg = "";
//
//		try {
////			fabricationService.validateDTOProperty(selectedTaStripeSubscriptionDTO,Const.C_CODE_TIERS,  ITaStripeSubscriptionServiceRemote.validationContext );
//
//		} catch(Exception e) {
//			msg += e.getMessage();
//			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
//		}
//	}
//
//	
//	public List<TaLAbonnementDTOJSF> getValuesLigne() {
//		return valuesLigne;
//	}
//
//	public void setValuesLigne(List<TaLAbonnementDTOJSF> valuesLigneMP) {
//		this.valuesLigne = valuesLigneMP;
//	}
//
//	public boolean isInsertAutoMP() {
//		return insertAutoMP;
//	}
//
//	public void setInsertAutoMP(boolean insertAutoMP) {
//		this.insertAutoMP = insertAutoMP;
//	}
//
//
//	public LeftBean getLeftBean() {
//		return leftBean;
//	}
//
//	public void setLeftBean(LeftBean leftBean) {
//		this.leftBean = leftBean;
//	}
//
//	public TaModeleFabrication getModelFab() {
//		return modelFab;
//	}
//
//	public void setModelFab(TaModeleFabrication modelFab) {
//		this.modelFab = modelFab;
//	}
//
//	public BigDecimal getQteModeleFabrication() {
//		return qteModeleFabrication;
//	}
//
//	public void setQteModeleFabrication(BigDecimal qteModele) {
//		this.qteModeleFabrication = qteModele;
//	}
//
//	public TaRecette getRecetteFab() {
//		return recetteFab;
//	}
//
//	public void setRecetteFab(TaRecette recetteFab) {
//		this.recetteFab = recetteFab;
//	}
//
//	public TaTFabrication getTaTFabrication() {
//		return taTFabrication;
//	}
//
//	public void setTaTFabrication(TaTFabrication taTFabrication) {
//		this.taTFabrication = taTFabrication;
//	}
//
//	public TaTFabricationDTO getTaTFabricationDTO() {
//		return taTFabricationDTO;
//	}
//
//	public void setTaTFabricationDTO(TaTFabricationDTO taTFabricationDTO) {
//		this.taTFabricationDTO = taTFabricationDTO;
//	}
//
//	public TaStripeSubscriptionDTO[] getSelectedDocumentDTOs() {
//		return selectedDocumentDTOs;
//	}
//
//	public void setSelectedDocumentDTOs(
//			TaStripeSubscriptionDTO[] selectedTaStripeSubscriptionDTOs) {
//		this.selectedDocumentDTOs = selectedTaStripeSubscriptionDTOs;
//	}
//
//	public TaTiers getTaTiers() {
//		return taTiers;
//	}
//
//	public void setTaTiers(TaTiers taTiers) {
//		this.taTiers = taTiers;
//	}
//
//	public TaTiersDTO getTaTiersDTO() {
//		return taTiersDTO;
//	}
//
//	public void setTaTiersDTO(TaTiersDTO taTiersDTO) {
//		this.taTiersDTO = taTiersDTO;
//	}
//
//	
//	public Integer renvoiPremierIdSelection(){
//		if(selectedDocumentDTOs!=null && selectedDocumentDTOs.length>0){
//			if(selectedDocumentDTOs.length>1){
//				RequestContext.getCurrentInstance().showMessageInDialog(new FacesMessage("Sélection multiple","Vous ne devez sélectionner qu'une seule fabrication pour créer le modèle !"));
//				
//			}else
//				return selectedDocumentDTOs[0].getId();
//		}
//		return null;
//	}
//	
//	public void removeLigneMP(SWTLigneDocument ligne) throws Exception {
////		if(beforeRemoveLigne(ligne)) {
//			if( masterEntity.getLignesMatierePremieres().size()-1>=0 ){
//				//passage ejb
//				int rang = masterEntity.getLignesMatierePremieres().indexOf(ligne);
//				 masterEntity.getLignesMatierePremieres().remove(ligne);
//				 masterEntity.reinitialiseNumLignesMP(0, 1);
////			}
//		} else {
//			throw new ExceptLgr();
//		}
//	}
//
//	public org.primefaces.component.wizard.Wizard getWizardDocument() {
//		return wizardDocument;
//	}
//
//	public void setWizardDocument(org.primefaces.component.wizard.Wizard wizardDocument) {
//		this.wizardDocument = wizardDocument;
//	}
//
//	public RemoteCommand getRc() {
//		return rc;
//	}
//
//	public void setRc(RemoteCommand rc) {
//		this.rc = rc;
//	}
//
//	public String getStepSynthese() {
//		return stepSynthese;
//	}
//
//	public void setStepSynthese(String stepSynthese) {
//		this.stepSynthese = stepSynthese;
//	}
//
//	public String getStepEntete() {
//		return stepEntete;
//	}
//
//	public void setStepEntete(String stepEntete) {
//		this.stepEntete = stepEntete;
//	}
//
//	public String getStepLignes() {
//		return stepLignes;
//	}
//
//	public void setStepLignes(String stepLignes) {
//		this.stepLignes = stepLignes;
//	}
//
//	public String getStepTotaux() {
//		return stepTotaux;
//	}
//
//	public void setStepTotaux(String stepTotaux) {
//		this.stepTotaux = stepTotaux;
//	}
//
//	public String getStepValidation() {
//		return stepValidation;
//	}
//
//	public void setStepValidation(String stepValidation) {
//		this.stepValidation = stepValidation;
//	}
//
//	public String getIdMenuBouton() {
//		return idMenuBouton;
//	}
//
//	public void setIdMenuBouton(String idMenuBouton) {
//		this.idMenuBouton = idMenuBouton;
//	}
//
//	public String getIdBtnWizardButtonPrecedent() {
//		return idBtnWizardButtonPrecedent;
//	}
//
//	public void setIdBtnWizardButtonPrecedent(String idBtnWizardButtonPrecedent) {
//		this.idBtnWizardButtonPrecedent = idBtnWizardButtonPrecedent;
//	}
//
//	public String getIdBtnWizardButtonSuivant() {
//		return idBtnWizardButtonSuivant;
//	}
//
//	public void setIdBtnWizardButtonSuivant(String idBtnWizardButtonSuivant) {
//		this.idBtnWizardButtonSuivant = idBtnWizardButtonSuivant;
//	}
//
//	public String getCurrentStepId() {
//		return currentStepId;
//	}
//
//	public void setCurrentStepId(String currentStepId) {
//		this.currentStepId = currentStepId;
//	}
//
//	public String getJS_FONCTION_INITIALISE_POSITION_BOUTON_WIZARD() {
//		return JS_FONCTION_INITIALISE_POSITION_BOUTON_WIZARD;
//	}
//
//	public void setJS_FONCTION_INITIALISE_POSITION_BOUTON_WIZARD(String jS_FONCTION_INITIALISE_POSITION_BOUTON_WIZARD) {
//		JS_FONCTION_INITIALISE_POSITION_BOUTON_WIZARD = jS_FONCTION_INITIALISE_POSITION_BOUTON_WIZARD;
//	}
//
//	public boolean isBtnPrecedentVisible() {
//		return btnPrecedentVisible;
//	}
//
//	public void setBtnPrecedentVisible(boolean btnPrecedentVisible) {
//		this.btnPrecedentVisible = btnPrecedentVisible;
//	}
//
//	public boolean isBtnSuivantVisible() {
//		return btnSuivantVisible;
//	}
//
//	public void setBtnSuivantVisible(boolean btnSuivantVisible) {
//		this.btnSuivantVisible = btnSuivantVisible;
//	}
//
//	public ModeObjetEcranServeur getModeEcranLigne() {
//		return modeEcranLigne;
//	}
//
//	public void setModeEcranLigne(ModeObjetEcranServeur modeEcranLigne) {
//		this.modeEcranLigne = modeEcranLigne;
//	}
//
//	public OuvertureTiersBean getOuvertureTiersBean() {
//		return ouvertureTiersBean;
//	}
//
//	public void setOuvertureTiersBean(OuvertureTiersBean ouvertureTiersBean) {
//		this.ouvertureTiersBean = ouvertureTiersBean;
//	}
//
//	public OuvertureArticleBean getOuvertureArticleBean() {
//		return ouvertureArticleBean;
//	}
//
//	public void setOuvertureArticleBean(OuvertureArticleBean ouvertureArticleBean) {
//		this.ouvertureArticleBean = ouvertureArticleBean;
//	}
//
//	public boolean isEcranSynthese() {
//		return ecranSynthese;
//	}
//
//	public void setEcranSynthese(boolean ecranSynthese) {
//		this.ecranSynthese = ecranSynthese;
//	}
//
//	public String getWvEcran() {
//		return wvEcran;
//	}
//
//	public void setWvEcran(String wvEcran) {
//		this.wvEcran = wvEcran;
//	}
//
//	public String getMessageSuppression() {
//		return messageSuppression;
//	}
//
//	public void setMessageSuppression(String messageSuppression) {
//		this.messageSuppression = messageSuppression;
//	}
//
//	public String getTitreSuppression() {
//		return titreSuppression;
//	}
//
//	public void setTitreSuppression(String titreSuppression) {
//		this.titreSuppression = titreSuppression;
//	}
//
//	public String getMessageModification() {
//		return messageModification;
//	}
//
//	public void setMessageModification(String messageModification) {
//		this.messageModification = messageModification;
//	}
//
//	public String getTitreModification() {
//		return titreModification;
//	}
//
//	public void setTitreModification(String titreModification) {
//		this.titreModification = titreModification;
//	}
//
//	public boolean isGestionBoutonWizardDynamique() {
//		return gestionBoutonWizardDynamique;
//	}
//
//	public void setGestionBoutonWizardDynamique(boolean gestionBoutonWizardDynamique) {
//		this.gestionBoutonWizardDynamique = gestionBoutonWizardDynamique;
//	}
//	
//	
//}  
