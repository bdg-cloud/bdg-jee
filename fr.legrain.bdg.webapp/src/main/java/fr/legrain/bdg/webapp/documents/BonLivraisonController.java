package fr.legrain.bdg.webapp.documents;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.validator.ValidatorException;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.apache.commons.beanutils.PropertyUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

import fr.legrain.article.dto.ArticleLotEntrepotDTO;
import fr.legrain.article.dto.TaArticleDTO;
import fr.legrain.article.dto.TaTransporteurDTO;
import fr.legrain.article.model.TaArticle;
import fr.legrain.article.model.TaCoefficientUnite;
import fr.legrain.article.model.TaEntrepot;
import fr.legrain.article.model.TaLot;
import fr.legrain.article.model.TaPrix;
import fr.legrain.article.model.TaRapportUnite;
import fr.legrain.article.model.TaTransporteur;
import fr.legrain.article.model.TaTva;
import fr.legrain.article.model.TaUnite;
import fr.legrain.article.titretransport.model.TaTitreTransport;
import fr.legrain.bdg.documents.service.remote.ITaBonlivServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaInfosBonlivServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLBonlivServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaPreferencesServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.lib.osgi.ConstActionInterne;
import fr.legrain.bdg.lib.server.osgi.BdgProperties;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.webapp.ConstWeb;
import fr.legrain.bdg.webapp.SessionListener;
import fr.legrain.bdg.webapp.app.EditionParam;
import fr.legrain.bdg.webapp.app.LgrTab;
import fr.legrain.birt.AnalyseFileReport;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.IDocumentTiersDTO;
import fr.legrain.document.dto.TaBonlivDTO;
import fr.legrain.document.dto.TaLBonlivDTO;
import fr.legrain.document.dto.TaLigneALigneDTO;
import fr.legrain.document.dto.TaLigneALigneEcheanceDTO;
import fr.legrain.document.dto.TaRDocumentDTO;
import fr.legrain.document.model.TaAcompte;
import fr.legrain.document.model.TaApporteur;
import fr.legrain.document.model.TaBonliv;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaInfosBonliv;
import fr.legrain.document.model.TaLBonliv;
import fr.legrain.document.model.TaLigneALigneEcheance;
import fr.legrain.document.model.TaMiseADisposition;
import fr.legrain.document.model.TaTLigne;
import fr.legrain.document.model.TaTPaiement;
import fr.legrain.dossier.model.TaPreferences;
import fr.legrain.droits.model.IModulesProgramme;
import fr.legrain.edition.dto.TaEditionDTO;
import fr.legrain.edition.model.TaActionEdition;
import fr.legrain.edition.model.TaEdition;
import fr.legrain.email.model.TaMessageEmail;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.LibDate;
import fr.legrain.stock.model.TaGrMouvStock;
import fr.legrain.stock.model.TaMouvementStock;
import fr.legrain.tiers.dto.AdresseInfosFacturationDTO;
import fr.legrain.tiers.dto.AdresseInfosLivraisonDTO;
import fr.legrain.tiers.dto.TaCPaiementDTO;
import fr.legrain.tiers.dto.TaTiersDTO;
import fr.legrain.tiers.model.TaCPaiement;
import fr.legrain.tiers.model.TaTCPaiement;
import fr.legrain.tiers.model.TaTTvaDoc;
import fr.legrain.tiers.model.TaTiers;

@Named
@ViewScoped  
public class BonLivraisonController extends AbstractDocumentController<TaBonliv,TaBonlivDTO,TaLBonliv,TaLBonlivDTO,TaLBonLivraisonDTOJSF,TaInfosBonliv> {  

	@Inject @Named(value="ouvertureTiersBean")
	private OuvertureTiersBean ouvertureTiersBean;
	
	@Inject @Named(value="ouvertureArticleBean")
	private OuvertureArticleBean ouvertureArticleBean;
	
	
//	//A gérer ailleurs, ou de façon plus globale, mis en place juste pour tester les lots dans les factures
//	private boolean gestionLot = false;
//	
//	private String stepEntete = "idEnteteBonLivraison";
//	private String stepLignes = "idLignesBonLivraison";
//	private String stepTotaux = "idTotauxFormBonLivraison";
//	private String stepValidation = "idValidationFormBonliv";
//	
//	private String currentStepId;
//	//list permettant de faire la correspondance entre les lots / articles , les entrepot et les emplacement
//	private List<ArticleLotEntrepotDTO> listArticleLotEntrepot;
//	
//	
//	@Inject @Named(value="tabListModelCenterBean")
//	private TabListModelBean tabsCenter;
//	
//	@Inject @Named(value="tabViewsBean")
//	private TabViewsBean tabViews;
//	
//	@Inject @Named(value="etiquetteCodeBarreBean")
//	private EtiquetteCodeBarreBean etiquetteCodeBarreBean;
//	
//	@Inject private AutorisationBean autorisationBean;
//
//	private String paramId;
//
//	private List<TaBonlivDTO> values; 
//	private List<TaLBonLivraisonDTOJSF> valuesLigne;
//	private org.primefaces.component.wizard.Wizard wizardDocument;
//	private RemoteCommand rc;
//
//	protected ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();
//	protected ModeObjetEcranServeur modeEcranLigne = new ModeObjetEcranServeur();

	private @EJB ITaBonlivServiceRemote taBonLivraisonService;
	private @EJB ITaInfosBonlivServiceRemote taInfosBonlivService;
	private @EJB ITaLBonlivServiceRemote taLBonlivService;
//	private @EJB ITaTLigneServiceRemote taTLigneService;
//	private @EJB ITaTiersServiceRemote taTiersService;
//	private @EJB ITaArticleServiceRemote taArticleService;
//	private @EJB ITaEntrepotServiceRemote taEntrepotService;
//	private @EJB ITaEtatStockServiceRemote taEtatStockService;
//	private @EJB ITaLotServiceRemote taLotService;
//	private @EJB ITaTypeMouvementServiceRemote taTypeMouvementService;
//	private @EJB ITaMouvementStockServiceRemote taMouvementStockService;
//	private @EJB ITaGrMouvStockServiceRemote taGrMouvStockService;
//	private @EJB ITaUniteServiceRemote taUniteService;
//	private @EJB ITaTTvaDocServiceRemote taTTvaDocService;
//	private @EJB ITaGenCodeExServiceRemote taGenCodeExService;
//	private @EJB ITaInfoEntrepriseServiceRemote taInfoEntrepriseService;
//	private @EJB ITaPreferencesServiceRemote taPreferencesService;
//	
//	private @EJB ITaTAdrServiceRemote taTAdrService;
//	private @EJB ITaTCPaiementServiceRemote taTCPaiementService;
//	private @EJB ITaTPaiementServiceRemote taTPaiementService;
//	private @EJB ITaReglementServiceRemote taReglementService;
//	private @EJB ITaCompteBanqueServiceRemote taCompteBanqueService;
//	private @EJB ITaCPaiementServiceRemote taCPaiementService;
//	
//
//	private String typeAdresseFacturation="FAC";
//	private String typeAdresseLivraison="LIV";
//	
//	private TaBonlivDTO[] selectedDocumentDTOs; 
//	private TaBonliv masterEntity = new TaBonliv();
//	private TaBonlivDTO newDocumentDTO = new TaBonlivDTO();
//	private TaBonlivDTO selectedDocumentDTO = new TaBonlivDTO();
//
//	private TaInfosBonliv taInfosDocument = new TaInfosBonliv();
//	
//	private TaLBonLivraisonDTOJSF[] selectedLigneJSFs; 
//	private TaLBonliv masterEntityLigne = new TaLBonliv();
//	private TaLBonLivraisonDTOJSF oldTaLBonLivraisonDTOJSF = new TaLBonLivraisonDTOJSF();
//	private TaLBonLivraisonDTOJSF selectedLigneJSF = new TaLBonLivraisonDTOJSF();
//	private TaLBonLivraisonDTOJSF detailLigneOverLayPanel = null;
//	
//
//	
//	private AdresseInfosFacturationDTO selectedAdresseFact = new AdresseInfosFacturationDTO();
//	private Class classModelAdresseFact = AdresseInfosFacturationDTO.class;
//	
//	private AdresseInfosLivraisonDTO selectedAdresseLiv = new AdresseInfosLivraisonDTO();
//	private Class classModelAdresseLiv = AdresseInfosLivraisonDTO.class;
//	
//	private TaCPaiementDTO selectedCPaiement = new TaCPaiementDTO();
//	private Class classModelCPaiement = TaCPaiementDTO.class;
//	
//	private TaTiers taTiers;
//	private TaTiersDTO taTiersDTO;
//	private TaArticle taArticleLigne;
//	private TaEntrepot taEntrepotLigne;
//	private TaTPaiement taTPaiement=new TaTPaiement();
//	private TaReglement taReglement;
//	private TaTTvaDoc taTTvaDoc;
//	private TaCPaiement taCPaiementDoc = null;
//	private boolean differenceReglementResteARegle=false;
//
//	private LgrDozerMapper<TaBonlivDTO,TaBonliv> mapperUIToModel  = new LgrDozerMapper<TaBonlivDTO,TaBonliv>();
//	private LgrDozerMapper<TaBonliv,TaBonlivDTO> mapperModelToUI  = new LgrDozerMapper<TaBonliv,TaBonlivDTO>();
//	private LgrDozerMapper<TaTiers,TaTiersDTO> mapperModelToUITiers  = new LgrDozerMapper<TaTiers,TaTiersDTO>();
//
//	private LgrDozerMapper<TaAdresse,AdresseInfosFacturationDTO> mapperModelToUIAdresseInfosDocument = new LgrDozerMapper<TaAdresse,AdresseInfosFacturationDTO>();
//	private LgrDozerMapper<TaAdresse,AdresseInfosLivraisonDTO> mapperModelToUIAdresseLivInfosDocument = new LgrDozerMapper<TaAdresse,AdresseInfosLivraisonDTO>();
//	private LgrDozerMapper<TaCPaiement,TaCPaiementDTO> mapperModelToUICPaiementInfosDocument = new LgrDozerMapper<TaCPaiement,TaCPaiementDTO>();
	
//	private LgrDozerMapper<TaInfosBonliv,TaCPaiementDTO> mapperModelToUIInfosDocVersCPaiement = new LgrDozerMapper<TaInfosBonliv,TaCPaiementDTO>();
//	private LgrDozerMapper<TaInfosBonliv,AdresseInfosFacturationDTO> mapperModelToUIInfosDocVersAdresseFact = new LgrDozerMapper<TaInfosBonliv,AdresseInfosFacturationDTO>();
//	private LgrDozerMapper<TaInfosBonliv,AdresseInfosLivraisonDTO> mapperModelToUIInfosDocVersAdresseLiv = new LgrDozerMapper<TaInfosBonliv,AdresseInfosLivraisonDTO>();
//		
//	private LgrDozerMapper<TaCPaiementDTO,TaInfosBonliv> mapperUIToModelCPaiementVersInfosDoc = new LgrDozerMapper<TaCPaiementDTO, TaInfosBonliv>();
//	private LgrDozerMapper<AdresseInfosFacturationDTO,TaInfosBonliv> mapperUIToModelAdresseFactVersInfosDoc = new LgrDozerMapper<AdresseInfosFacturationDTO, TaInfosBonliv>();
//	private LgrDozerMapper<AdresseInfosLivraisonDTO,TaInfosBonliv> mapperUIToModelAdresseLivVersInfosDoc = new LgrDozerMapper<AdresseInfosLivraisonDTO, TaInfosBonliv>();
//	
//	private LgrDozerMapper<TaBonliv,TaInfosBonliv> mapperUIToModelDocumentVersInfosDoc = new LgrDozerMapper<TaBonliv, TaInfosBonliv>();	
	
//
//	private boolean factureReglee =false;
//	private boolean insertAuto = true;
//	private String typePaiementDefaut=null;
//	private String libellePaiement;
//	private boolean impressionDirect = false;

	public BonLivraisonController() {  
		//values = getValues();
	}  

	@PostConstruct
	public void postConstruct(){
		super.postConstruct();
		listePreferences= taPreferencesService.findByGroupe("bonliv");
		nomDocument="Bon de livraison";
		setTaDocumentService(taBonLivraisonService);
		setTaLDocumentService(taLBonlivService);
		setTaInfosDocumentService(taInfosBonlivService);
		
		initListeEdition();
		
		stepSynthese = "idSyntheseBonLivraison";
		stepEntete = "idEnteteBonLivraison";
		stepLignes = "idLignesBonLivraison";
		stepTotaux = "idTotauxFormBonLivraison";
		stepValidation = "idValidationFormBonLivraison";
		idMenuBouton = "form:tabView:idMenuBoutonBonLivraison";
		classeCssDataTableLigne = "myclassBonliv";
		variableEcran="Bonliv";
		refresh();
		initChoixEdition();
//		gestionLot = autorisationBean.isModeGestionLot();
	}
	public void initChoixEdition() {
		choixEdition = false;
		List<TaEdition> listeEditionDisponible = taEditionService.rechercheEditionDisponible("", ConstActionInterne.EDITION_BONLIV_DEFAUT,null);
		
		if(listeEditionDisponible!=null && !listeEditionDisponible.isEmpty() && listeEditionDisponible.size()>1) {
			choixEdition = true;
		}
	}

	public void refresh(){
//		values = taBonLivraisonService.selectAllDTO();
		values = taBonLivraisonService.findAllLight();
		valuesLigne = IHMmodel();
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}

	public void IHMmodel(TaLBonLivraisonDTOJSF dtoJSF, TaLBonliv ligne) {

			LgrDozerMapper<TaLBonliv,TaLBonlivDTO> mapper = new LgrDozerMapper<TaLBonliv,TaLBonlivDTO>();
			TaLBonlivDTO dto = dtoJSF.getDto();

				dto = (TaLBonlivDTO) mapper.map(ligne, TaLBonlivDTO.class);
				dto.setEmplacement(ligne.getEmplacementLDocument());
				dtoJSF.setDto(dto);
				dtoJSF.setTaLot(ligne.getTaLot());
				if(dtoJSF.getArticleLotEntrepotDTO()==null) {
					dtoJSF.setArticleLotEntrepotDTO(new ArticleLotEntrepotDTO());
				}
				if(ligne.getTaLot()!=null) {
					dtoJSF.getArticleLotEntrepotDTO().setNumLot(ligne.getTaLot().getNumLot());
				}
				dtoJSF.setTaArticle(ligne.getTaArticle());
				if(ligne.getTaArticle()!=null){
					dtoJSF.setTaRapport(ligne.getTaArticle().getTaRapportUnite());
				}
				dtoJSF.setTaEntrepot(ligne.getTaEntrepot());
				if(ligne.getU1LDocument()!=null) {
					dtoJSF.setTaUnite1(new TaUnite());
					dtoJSF.getTaUnite1().setCodeUnite(ligne.getU1LDocument());
				}
				if(ligne.getU2LDocument()!=null) {
					dtoJSF.setTaUnite2(new TaUnite());
					dtoJSF.getTaUnite2().setCodeUnite(ligne.getU2LDocument());
				}
				if(ligne.getCodeTitreTransport()!=null) {
					dtoJSF.setTaTitreTransport(new TaTitreTransport());
					dtoJSF.getTaTitreTransport().setCodeTitreTransport(ligne.getCodeTitreTransport());
				}

				dtoJSF.updateDTO(false);
				List<TaLigneALigneDTO>  l=rechercheSiLigneDocLie(dtoJSF);
				if(l!=null) {
					for (TaLigneALigneDTO ll : l) {
						if(ll.getIdLigneSrc().equals(ll.getIdLDocumentSrc())) {
							if(dtoJSF.getLigneLieeFils()==null)dtoJSF.setLigneLieeFils(new LinkedList<>());
							dtoJSF.getLigneLieeFils().add(ll);
						}
						else { 
							if(dtoJSF.getLigneLieeMere()==null)dtoJSF.setLigneLieeMere(new LinkedList<>());
							dtoJSF.getLigneLieeMere().add(ll);
						}							
					}
				}
	}
	
	public List<TaLBonLivraisonDTOJSF> IHMmodel() {
		LinkedList<TaLBonliv> ldao = new LinkedList<TaLBonliv>();
		LinkedList<TaLBonLivraisonDTOJSF> lswt = new LinkedList<TaLBonLivraisonDTOJSF>();

		if(masterEntity!=null && !masterEntity.getLignes().isEmpty()) {

			ldao.addAll(masterEntity.getLignes());

			LgrDozerMapper<TaLBonliv,TaLBonlivDTO> mapper = new LgrDozerMapper<TaLBonliv,TaLBonlivDTO>();
			TaLBonlivDTO dto = null;
			for (TaLBonliv o : ldao) {
				TaLBonLivraisonDTOJSF t = new TaLBonLivraisonDTOJSF();
				dto = (TaLBonlivDTO) mapper.map(o, TaLBonlivDTO.class);
				dto.setEmplacement(o.getEmplacementLDocument());
				t.setDto(dto);
				t.setTaLot(o.getTaLot());
				if(t.getArticleLotEntrepotDTO()==null) {
					t.setArticleLotEntrepotDTO(new ArticleLotEntrepotDTO());
				}
				if(o.getTaLot()!=null) {
					t.getArticleLotEntrepotDTO().setNumLot(o.getTaLot().getNumLot());
				}
				t.setTaArticle(o.getTaArticle());
				if(o.getTaArticle()!=null){
					t.setTaRapport(o.getTaArticle().getTaRapportUnite());
				}
				t.setTaEntrepot(o.getTaEntrepot());
				if(o.getU1LDocument()!=null) {
					t.setTaUnite1(new TaUnite());
					t.getTaUnite1().setCodeUnite(o.getU1LDocument());
				}
				if(o.getU2LDocument()!=null) {
					t.setTaUnite2(new TaUnite());
					t.getTaUnite2().setCodeUnite(o.getU2LDocument());
					t.setTaCoefficientUnite(recupCoefficientUnite(t.getDto().getU1LDocument(),t.getDto().getU2LDocument()));
				}
				if(o.getUSaisieLDocument()!=null) {
					t.setTaUniteSaisie(new TaUnite());
					t.getTaUniteSaisie().setCodeUnite(o.getUSaisieLDocument());
					t.setTaCoefficientUniteSaisie(recupCoefficientUnite(t.getDto().getUSaisieLDocument(),t.getDto().getU1LDocument()));
				}
				if(o.getCodeTitreTransport()!=null) {
					try {
						t.setTaTitreTransport(taTitreTransportService.findByCode(o.getCodeTitreTransport()));
						if(t.getTaArticle()!=null && t.getTaArticle().getTaRTaTitreTransport()!=null)
							t.getTaArticle().getTaRTaTitreTransport().setTaTitreTransport(t.getTaTitreTransport());
					} catch (FinderException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}				
				t.setTaREtatLigneDocument(o.getTaREtatLigneDocument());
				if(o.getTaREtatLigneDocument()!=null && o.getTaREtatLigneDocument().getTaEtat()!=null)
					t.setCodeEtat(o.getTaREtatLigneDocument().getTaEtat().getCodeEtat());
				t.updateDTO(false);
				List<TaRDocumentDTO>  r=rechercheSiDocLie();
				List<TaLigneALigneDTO>  l=rechercheSiLigneDocLie(t);
				List<TaLigneALigneEcheanceDTO>  le=rechercheSiLigneEcheanceDocLie(t);
				if(le!=null) {
					for (TaLigneALigneEcheanceDTO ligne : le) {
							if(t.getLigneAbonnement()==null)t.setLigneAbonnement(new LinkedList<>());
							t.getLigneAbonnement().add(ligne);
						}
				}
				if(l!=null) {
					for (TaLigneALigneDTO ligne : l) {
						if(ligne.getIdLigneSrc().equals(ligne.getIdLDocumentSrc())) {
							if(t.getLigneLieeFils()==null)t.setLigneLieeFils(new LinkedList<>());
							t.getLigneLieeFils().add(ligne);
						}
						else { 
							if(t.getLigneLieeMere()==null)t.setLigneLieeMere(new LinkedList<>());
							t.getLigneLieeMere().add(ligne);
						}	
						if(ligne.getEtat()!=null) {
//							t.setCodeEtat(ligne.getEtat());
							t.setLibelleEtat(" - Liée au document "+ligne.getCodeDocumentDest());
						}
					}
				}else
					if(r!=null) {
						for (TaRDocumentDTO ligne : r) {
//							t.setCodeEtat("*****");
							t.setLibelleEtat("Liée au document "+ligne.getCodeDocumentDest());
						}
					}
				
				lswt.add(t);
			}

		}
		return lswt;
	}
	
//	public boolean stepEnCours(String step ){		
//		return step.equals(currentStepId);
//	}
//	
//	public String handleFlow(FlowEvent event) {
//
//		try{
//		currentStepId = event.getOldStep();
//		String stepToGo = event.getNewStep();
//
//		if(modeEcranLigne.dataSetEnModif())
//			actEnregistrerLigne(null);
//		
//		if(currentStepId.equals(stepEntete) && stepToGo.equals(stepLignes)) {
//			mapperUIToModel.map(selectedDocumentDTO, masterEntity);
//		}
//		
//		currentStepId=event.getNewStep();
//		PrimeFaces.current().ajax().update("form:tabView:idMenuBoutonBonliv");
//		return currentStepId;
//
//		} catch (Exception e) {
//			FacesContext context = FacesContext.getCurrentInstance();  
//			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Bonliv", e.getMessage()));
//			return event.getOldStep();
//		}
//	}
//
//	public List<TaBonlivDTO> getValues(){  
//		return values;
//	}
//	
//	public void actAutoInsererLigne(ActionEvent actionEvent) /*throws Exception*/ {
//		boolean existeDeja=false;
//		if(insertAuto) {
//			for (TaLBonLivraisonDTOJSF ligne : valuesLigne) {
//				if(ligne.ligneEstVide())existeDeja=true;
//			}
//			if(!existeDeja){	
//			actInsererLigne(actionEvent);
//			
//			String oncomplete="jQuery('.myclass .ui-datatable-data tr').last().find('span.ui-icon-pencil').each(function(){jQuery(this).click()});";
//			PrimeFaces.current().executeScript(oncomplete);
//			}
//		} else {
////			throw new Exception();
//		}
//	}
	
	public void initListeEdition() {
		listeEdition = taEditionService.findByCodeTypeDTOAvecActionsEdition(TaBonliv.TYPE_DOC.toUpperCase());
		TaEditionDTO editionDefautNulle = new TaEditionDTO();
		editionDefautNulle.setLibelleEdition("Bon de livraison modèle par défaut (V0220)");
		listeEdition.add(editionDefautNulle);
	}
	
	public void envoiMailEdition(TaEditionDTO edition, String modeEdition, String pourClient) {
		 Map<String,Object> options = new HashMap<String, Object>();
	       
	        
	        Map<String,List<String>> params = new HashMap<String,List<String>>();
	        List<String> list = new ArrayList<String>();
	        list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
	        params.put("modeEcranDefaut", list);
	        
	        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			Map<String, Object> mapParametreEdition = null;
			
	
				
			mapParametreEdition = new HashMap<>();
			boolean editionClient = false;	
//			String modeEdition = (String)actionEvent.getComponent().getAttributes().get("mode_edition");
//			String pourClient = (String)actionEvent.getComponent().getAttributes().get("pour_client");
			editionClient = LibConversion.StringToBoolean(pourClient,false);
			mapParametreEdition.put("editionClient", editionClient);
			
			List<TaPreferences> liste= taPreferencesService.findByGroupe("bonliv");
			mapParametreEdition.put("preferences", liste);
			mapParametreEdition.put("gestionLot", masterEntity.getGestionLot());
			  

//			if(!editionClient) {
//				switch (modeEdition) {
//				case "BROUILLON":
//					if(masterEntity.getResteAReglerComplet().compareTo(BigDecimal.ZERO)==0) {
//						mapParametreEdition.put("mode", "BROUILLONAPAYER");
//					} else {
//						mapParametreEdition.put("mode", "BROUILLON");
//					}
//					break;
//				case "DUPLICATA":
//					if(masterEntity.getResteAReglerComplet().compareTo(BigDecimal.ZERO)==0) {
//						mapParametreEdition.put("mode", "DUPLICATAPAYER");
//					} else {
//						mapParametreEdition.put("mode", "DUPLICATA");
//					}
//					break;
//				case "PAYER":
//					if(masterEntity.getResteAReglerComplet().compareTo(BigDecimal.ZERO)==0) {
//						mapParametreEdition.put("mode", "PAYER");
//					} else {
//						mapParametreEdition.put("mode", "PAYER");
//					}
//					break;
//	
//				default:
//					break;
//				}
//			} else {
//				if(masterEntity.getResteAReglerComplet().compareTo(BigDecimal.ZERO)==0) {
//					mapParametreEdition.put("mode", "CLIENTAPAYER");
//				} else {
//					mapParametreEdition.put("mode", "CLIENT");
//				}
//			}
			
			
			EditionParam editionParam = new EditionParam();
			editionParam.setIdActionInterne(ConstActionInterne.EDITION_BONLIV_DEFAUT);
			editionParam.setMapParametreEdition(mapParametreEdition);
			
			sessionMap.put(EditionParam.NOM_OBJET_EN_SESSION, editionParam);
	        
			
	        //PrimeFaces.current().dialog().openDynamic("/dialog_choix_edition", options, params);
			
			//ICI s'arrete la partie actDialogImprimer
			
			//ICI COMMENCE LA PARTIE handleReturnDialogImprimer
			String pdf="";
			TaEdition taEdition = null;
			if(edition == null) {//si aucune edition n'est choisie
				TaActionEdition action = new TaActionEdition();
				action.setCodeAction(TaActionEdition.code_mail);
				
				taEdition = taEditionService.rechercheEditionActionDefaut(null,action, TaBonliv.TYPE_DOC);
				
			}else {//si une édition est choisie
				
				 if(edition.getId() != null) {//si ce n'est pas l'édition defaut programme
					 try {
							taEdition = taEditionService.findById(edition.getId());
						} catch (FinderException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
				 } 
				
				if(taEdition == null) {
					taEdition = taEditionService.rechercheEditionDisponibleProgrammeDefaut("", ConstActionInterne.EDITION_BONLIV_DEFAUT).get(0);
				}
				
				
			}
			//On rempli selectedEdition qui va être utiliser dans AbstractDocumentController.actDialogEmailSelectedEdition
			if(taEdition!=null) {
				
				taEdition.setMapParametreEdition(mapParametreEdition);
				selectedEdition = taEdition;

				
			}
	}
	
	public void imprimeEdition(TaEditionDTO edition, String modeEdition, String pourClient) {
		//ICI COMMENCE LA PARTIE actDialogImprimer
		 Map<String,Object> options = new HashMap<String, Object>();
	       
	        
	        Map<String,List<String>> params = new HashMap<String,List<String>>();
	        List<String> list = new ArrayList<String>();
	        list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
	        params.put("modeEcranDefaut", list);
	        
	        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			Map<String, Object> mapParametreEdition = null;
			
	
				
			mapParametreEdition = new HashMap<>();
			boolean editionClient = false;	
			editionClient = LibConversion.StringToBoolean(pourClient,false);
			mapParametreEdition.put("editionClient", editionClient);
			
			List<TaPreferences> liste= taPreferencesService.findByGroupe("bonliv");
			mapParametreEdition.put("preferences", liste);
			mapParametreEdition.put("gestionLot", masterEntity.getGestionLot());
			  
		    if(!editionClient) {

		    }else {
		        //Mise à jour de la mise à dispostion
		        if(masterEntity.getTaMiseADisposition()==null) {
		            masterEntity.setTaMiseADisposition(new TaMiseADisposition());
		        }
		        Date dateMiseADispositionClient = new Date();
		        masterEntity.getTaMiseADisposition().setImprimePourClient(dateMiseADispositionClient);
		        masterEntity = taBonLivraisonService.mergeAndFindById(masterEntity,ITaBonlivServiceRemote.validationContext);
		    }
//			if(!editionClient) {
//				switch (modeEdition) {
//				case "BROUILLON":
//					if(masterEntity.getResteAReglerComplet().compareTo(BigDecimal.ZERO)==0) {
//						mapParametreEdition.put("mode", "BROUILLONAPAYER");
//					} else {
//						mapParametreEdition.put("mode", "BROUILLON");
//					}
//					break;
//				case "DUPLICATA":
//					if(masterEntity.getResteAReglerComplet().compareTo(BigDecimal.ZERO)==0) {
//						mapParametreEdition.put("mode", "DUPLICATAPAYER");
//					} else {
//						mapParametreEdition.put("mode", "DUPLICATA");
//					}
//					break;
//				case "PAYER":
//					if(masterEntity.getResteAReglerComplet().compareTo(BigDecimal.ZERO)==0) {
//						mapParametreEdition.put("mode", "PAYER");
//					} else {
//						mapParametreEdition.put("mode", "PAYER");
//					}
//					break;
//	
//				default:
//					break;
//				}
//			} else {
//				if(masterEntity.getResteAReglerComplet().compareTo(BigDecimal.ZERO)==0) {
//					mapParametreEdition.put("mode", "CLIENTAPAYER");
//				} else {
//					mapParametreEdition.put("mode", "CLIENT");
//				}
//			}
			
			
			EditionParam editionParam = new EditionParam();
			editionParam.setIdActionInterne(ConstActionInterne.EDITION_BONLIV_DEFAUT);
			editionParam.setMapParametreEdition(mapParametreEdition);
			
			sessionMap.put(EditionParam.NOM_OBJET_EN_SESSION, editionParam);
	        
			
			
			//ICI s'arrete la partie actDialogImprimer
			
			//ICI COMMENCE LA PARTIE handleReturnDialogImprimer
			String pdf="";
			if(edition == null) {//si aucune edition n'est choisie
				
				TaActionEdition actionImprimer = new TaActionEdition();
				actionImprimer.setCodeAction(TaActionEdition.code_impression);

				//on appelle une methode qui va aller chercher (et elle crée un fichier xml (writing)) elle même l'edition par defaut choisie en fonction de l'action si il y a 
				pdf = taBonLivraisonService.generePDF(selectedDocumentDTO.getId(),mapParametreEdition,null,null,actionImprimer);
				
			}else {//si une edition est choisie
				
				  TaEdition taEdition = null;
				  
				  if( edition.getId() != null) {//si l'édition choisie n'est pas l'edition defaut programme
					  try {
							//on récupère l'objet édition
							taEdition = taEditionService.findById(edition.getId());
						} catch (FinderException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
				  }
				
				if(taEdition == null) {
					//si cette edition n'existe pas, on prend celle programme
					taEdition = taEditionService.rechercheEditionDisponibleProgrammeDefaut("", ConstActionInterne.EDITION_BONLIV_DEFAUT).get(0);
				}
					if(taEdition!=null) {
						taEdition.setMapParametreEdition(mapParametreEdition);
						List<String> listeResourcesPath = null;
						if(taEdition.getResourcesPath()!=null) {
							listeResourcesPath = new ArrayList<>();
							listeResourcesPath.add(taEdition.getResourcesPath());
						}

						try { 
							//on créer le fichier birt xml de cette édition dans /tmp/bdg/demo
							String localPath  = taBonLivraisonService.writingFileEdition(taEdition);
							//on genere le pdf avec le fichier xml crée ci-dessus
							 pdf = taBonLivraisonService.generePDF(selectedDocumentDTO.getId(),localPath,taEdition.getMapParametreEdition(),listeResourcesPath,taEdition.getTheme());
							

						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				
			}
			//Enfin on ouvre un onglet pour afficher le PDF
			try {
				String urlEncoded = URLEncoder.encode(pdf, "UTF-8");
				PrimeFaces.current().executeScript("window.open('/edition?fichier="+urlEncoded+"')");
				masterEntity = taBonLivraisonService.findById(masterEntity.getIdDocument());
			} catch (FinderException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			updateTab();
		  
			
			
			//ICI FINI LA PARTIE handleReturnDialogImprimer
	}	
	
	public void handleReturnDialogEmail(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			TaMessageEmail j = (TaMessageEmail) event.getObject();
			
			//Mise à jour de la mise à dispostion
			if(masterEntity.getTaMiseADisposition()==null) {
				masterEntity.setTaMiseADisposition(new TaMiseADisposition());
			}
			Date dateMiseADispositionClient = new Date();
			
			masterEntity.getTaMiseADisposition().setEnvoyeParEmail(dateMiseADispositionClient);
			masterEntity = taBonLivraisonService.mergeAndFindById(masterEntity,ITaBonlivServiceRemote.validationContext);
			updateTab();
			
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Email", 
					"Email envoyée "
					));  
		}
	}
	
	public void handleReturnDialogImprimer(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			//List<ControleConformiteJSF> listeControle = (List<ControleConformiteJSF>) event.getObject();
			TaEdition taEdition = (TaEdition) event.getObject();

			if(taEdition!=null) {
				System.out.println("ChoixEditionController.actImprimer() "+taEdition.getLibelleEdition());
				List<String> listeResourcesPath = null;
				if(taEdition.getResourcesPath()!=null) {
					listeResourcesPath = new ArrayList<>();
					listeResourcesPath.add(taEdition.getResourcesPath());
				}
				BdgProperties bdgProperties = new BdgProperties();
				String localPath = bdgProperties.osTempDirDossier(tenantInfo.getTenantId())+"/"+bdgProperties.tmpFileName(taEdition.getFichierNom());

				try { 
					Files.write(Paths.get(localPath), taEdition.getFichierBlob());
					
					AnalyseFileReport afr = new AnalyseFileReport();
					afr.initializeBuildDesignReportConfig(localPath);
					afr.ajouteLogo();
					afr.closeDesignReportConfig();
					

					String pdf = taBonLivraisonService.generePDF(selectedDocumentDTO.getId(),localPath,taEdition.getMapParametreEdition(),listeResourcesPath,taEdition.getTheme());
					PrimeFaces.current().executeScript("window.open('/edition?fichier="+URLEncoder.encode(pdf, "UTF-8")+"')");
					masterEntity = taBonLivraisonService.findById(masterEntity.getIdDocument());
					updateTab();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}


		}
	}

	public void actEnregistrerLigne(ActionEvent actionEvent) {

		try {
			selectedLigneJSF.updateDTO(false);
//			if(selectedLigneJSF.getDto().getIdLDocument()!=null &&  selectedLigneJSF.getDto().getIdLDocument()!=0) {
//				masterEntityLigne=rechercheLigne(selectedLigneJSF.getDto().getIdLDocument());
//			}
			masterEntityLigne.setTaArticle(selectedLigneJSF.getTaArticle());
			if(masterEntityLigne.getTaArticle()==null && masterEntityLigne.getTaMouvementStock()!=null) {
				masterEntityLigne.setTaMouvementStock(null);
			}
			if(masterEntityLigne.getTaArticle()!=null && selectedLigneJSF.getDto().getPrixULDocument()==null) {
				masterEntityLigne.setPrixULDocument(BigDecimal.ZERO);
				selectedLigneJSF.getDto().setPrixULDocument(BigDecimal.ZERO);
			}
			
			
			masterEntityLigne.setTaEntrepot(selectedLigneJSF.getTaEntrepot());
			masterEntityLigne.setTaLot(selectedLigneJSF.getTaLot());
			masterEntityLigne.setEmplacementLDocument(selectedLigneJSF.getDto().getEmplacement());
			
			
			LgrDozerMapper<TaLBonlivDTO,TaLBonliv> mapper = new LgrDozerMapper<TaLBonlivDTO,TaLBonliv>();
			mapper.map((TaLBonlivDTO) selectedLigneJSF.getDto(),masterEntityLigne);
			IHMmodel(selectedLigneJSF,masterEntityLigne);
			//masterEntityLigne.setTaArticle(selectedLigneJSF.getTaArticle());

			
			if(masterEntityLigne.getCodeTvaLDocument()!=null && !masterEntityLigne.getCodeTvaLDocument().isEmpty()) {
				TaTva tva=taTvaService.findByCode(masterEntityLigne.getCodeTvaLDocument());
				if(tva!=null)
					masterEntityLigne.setLibTvaLDocument(tva.getLibelleTva());
			}


			masterEntity.enregistreLigne(masterEntityLigne);
			if(!masterEntity.getLignes().contains(masterEntityLigne))
				masterEntity.addLigne(masterEntityLigne);	
			
//			taBonReceptionService.calculeTvaEtTotaux(masterEntity);
			masterEntity.calculeTvaEtTotaux();
			mapperModelToUI.map(masterEntity, selectedDocumentDTO);
			
			
//			verifSiDifferenceReglement();
			
			modeEcranLigne.setMode(EnumModeObjet.C_MO_INSERTION);

		} catch (Exception e) {
			e.printStackTrace();
		}

		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Bonliv", "actEnregisterLigne")); 
		}
	}
	
//	
//	public TaLBonliv rechercheLigne(int id){
//		TaLBonliv obj=masterEntityLigne;
//		for (TaLBonliv ligne : masterEntity.getLignes()) {
//			if(ligne.getIdLDocument()==id)
//				obj=ligne;
//		}
//		return obj;
//	}
//	
//	public TaLBonliv rechercheLigneNumLigne(int numLigne){
//		TaLBonliv obj=masterEntityLigne;
//		for (TaLBonliv ligne : masterEntity.getLignes()) {
//			if(ligne.getNumLigneLDocument()==numLigne)
//				obj=ligne;
//		}
//		return obj;
//	}
//	
//	public void actSupprimerLigne() {
//		actSupprimerLigne(null);
//	}
//	
//	public void actSupprimerLigne(ActionEvent actionEvent) {
//		try {
//			valuesLigne.remove(selectedLigneJSF);
//			
//			if(selectedLigneJSF.getDto().getIdLDocument()!=null) {
//				masterEntityLigne =rechercheLigne(selectedLigneJSF.getDto().getIdLDocument());
//			} else {
//				masterEntityLigne =rechercheLigneNumLigne(selectedLigneJSF.getDto().getNumLigneLDocument());
//			}
//			
//			masterEntityLigne.setTaDocument(null);	
//			masterEntity.removeLigne(masterEntityLigne);
//
//			masterEntity.calculeTvaEtTotaux();
//			valuesLigne=IHMmodel();
//			mapperModelToUI.map(masterEntity, selectedDocumentDTO);
//			
//			modeEcranLigne.setMode(EnumModeObjet.C_MO_CONSULTATION);
//	
//			if(ConstWeb.DEBUG) {
//				FacesContext context = FacesContext.getCurrentInstance();  
//				context.addMessage(null, new FacesMessage("Bonliv", "actSupprimerLigne")); 
//			}
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//	}

//	public void onRowEdit(RowEditEvent event) {
//			try {
//				selectedLigneJSF.updateDTO(true);
//				if(selectedLigneJSF.getDto().getIdLDocument()!=null &&  selectedLigneJSF.getDto().getIdLDocument()!=0) {
//					masterEntityLigne=rechercheLigne(selectedLigneJSF.getDto().getIdLDocument());
//				}else if(selectedLigneJSF.getDto().getNumLigneLDocument()!=null/* &&  selectedTaLBonReceptionDTOJSF.getDto().getNumLigneLDocument()!=0*/) {
//					masterEntityLigne = rechercheLigneNumLigne(selectedLigneJSF.getDto().getNumLigneLDocument());
//				}
//				validateLigneDocumentAvantEnregistrement(selectedLigneJSF);
//				actEnregistrerLigne(null);
//				setInsertAuto(modeEcran.getMode()==EnumModeObjet.C_MO_INSERTION);
//			} catch (Exception e) {
//				e.printStackTrace();
//				FacesContext context = FacesContext.getCurrentInstance();  
//				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Bonliv", e.getMessage()));	
//				context.validationFailed();
//				//RequestContext.getCurrentInstance().addCallbackParam("notValid", true);
//				setInsertAuto(false);
////				throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage()));
//			}
//	}
//	
//	public void onRowCancel(RowEditEvent event) {
//		//((TaLBonLivraisonDTOJSF) event.getObject()).setAutoInsert(false);
////        FacesMessage msg = new FacesMessage("Edit Cancelled", ((Car) event.getObject()).getId());
////        FacesContext.getCurrentInstance().addMessage(null, msg);
//		selectionLigne((TaLBonLivraisonDTOJSF) event.getObject());
//		actAnnulerLigne(null);
//    }
//	
//	public void onRowEditInit(RowEditEvent event) {
//		AjaxBehaviorEvent evt = (AjaxBehaviorEvent)event;
//		DataTable table = (DataTable) evt.getSource();
//		int activeRow = table.getRowIndex()+1;
//		if(table.getRowCount() == activeRow) {
//			//derniere ligne
//			setInsertAuto(modeEcran.dataSetEnInsertion());
//		} else {
//			setInsertAuto(false);
//		}
//		if(event.getObject()!=null && !event.getObject().equals(selectedLigneJSF))
//			selectedLigneJSF=(TaLBonLivraisonDTOJSF) event.getObject();
//		actModifierLigne(null);
//}
//	
//	public void onRowSelectLigne(SelectEvent event) { 
//		if(event.getObject()!=null)
//			selectionLigne((TaLBonLivraisonDTOJSF) event.getObject());		
//	}
//	
//	public void selectionLigne(TaLBonLivraisonDTOJSF ligne){
//		selectedLigneJSF=ligne;
//		if(masterEntity.getIdDocument()!=0 && selectedLigneJSF!=null && selectedLigneJSF.getDto()!=null
//				&& selectedLigneJSF.getDto().getIdLDocument()!=null)
//			masterEntityLigne=rechercheLigne(selectedLigneJSF.getDto().getIdLDocument());
//		else if(selectedLigneJSF!=null && selectedLigneJSF.getDto()!=null)
//			masterEntityLigne=rechercheLigneNumLigne(selectedLigneJSF.getDto().getNumLigneLDocument());
//	}

	public void actAnnuler(ActionEvent actionEvent) {
		try {
			switch (modeEcran.getMode()) {
			case C_MO_INSERTION:
				if(selectedDocumentDTO.getCodeDocument()!=null) {
					taBonLivraisonService.annuleCode(selectedDocumentDTO.getCodeDocument());
				}				
				masterEntity=new TaBonliv();
				selectedDocumentDTO=new TaBonlivDTO();
				
				if(values.size()>0)	selectedDocumentDTO = values.get(0);

				onRowSelect(null);
				
				valuesLigne=IHMmodel();
				initInfosDocument();
				break;
			case C_MO_EDITION:
				if(selectedDocumentDTO.getId()!=null && selectedDocumentDTO.getId()!=0){
					masterEntity = taBonLivraisonService.findByIDFetch(selectedDocumentDTO.getId());
					selectedDocumentDTO=taBonLivraisonService.findByIdDTO(selectedDocumentDTO.getId());
				}				
				break;

			default:
				break;
			}			
				
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
		modeEcranLigne.setMode(EnumModeObjet.C_MO_CONSULTATION);
		updateTab();
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Bonliv", "actAnnuler")); 
		}
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void autoCompleteMapUIToDTO() {
		if(taTiersDTO!=null) {
			validateUIField(Const.C_CODE_TIERS,taTiersDTO);
			selectedDocumentDTO.setCodeTiers(taTiersDTO.getCodeTiers());
		}
		if(taTPaiement!=null) {
			validateUIField(Const.C_CODE_T_PAIEMENT,taTPaiement.getCodeTPaiement());
			selectedDocumentDTO.setCodeTPaiement(taTPaiement.getCodeTPaiement());
		}

		if(taTTvaDoc!=null) {
			validateUIField(Const.C_CODE_T_TVA_DOC,taTTvaDoc.getCodeTTvaDoc());
			selectedDocumentDTO.setCodeTTvaDoc(taTTvaDoc.getCodeTTvaDoc());
		}
		
		if(taCPaiementDoc!=null) {
			validateUIField(Const.C_CODE_C_PAIEMENT,taCPaiementDoc.getCodeCPaiement());
			selectedCPaiement.setCodeCPaiement(taCPaiementDoc.getCodeCPaiement());
		}
//		if(selectedEtatDocument!=null) {
//			validateUIField(Const.C_CODE_ETAT,selectedEtatDocument.getCodeEtat());
//			selectedDocumentDTO.setCodeEtat(selectedEtatDocument.getCodeEtat());
//			masterEntity.setTaEtat(selectedEtatDocument);
//		}
		if(taTransporteurDTO!=null) {
			validateUIField(Const.C_CODE_TRANSPORTEUR,taTransporteurDTO);
			selectedDocumentDTO.setCodeTransporteur(taTransporteurDTO.getCodeTransporteur());
		}
//		if(masterEntity.getTaReglement()!=null){
//			selectedDocumentDTO.setMontantReglement(masterEntity.getTaReglement().getNetTtcCalc());
//			selectedDocumentDTO.setLibelleReglement(masterEntity.getTaReglement().getLibelleDocument());
//			if(masterEntity.getTaReglement().getTaTPaiement()!=null)
//				selectedDocumentDTO.setCodeTPaiement(masterEntity.getTaReglement().getTaTPaiement().getCodeTPaiement());
//		}
	}

	public void autoCompleteMapDTOtoUI() {
		try {
			taTiers = null;
			taTiersDTO = null;
			taTPaiement = null;
			taTTvaDoc = null;
			taReglement = null;
			taCPaiementDoc = null;
			taTransporteur = null;
			taTransporteurDTO = null;
			
			//			taTEntite = null;
			if(selectedDocumentDTO.getCodeTiers()!=null && !selectedDocumentDTO.getCodeTiers().equals("")) {
				taTiers = taTiersService.findByCode(selectedDocumentDTO.getCodeTiers());
				taTiersDTO = mapperModelToUITiers.map(taTiers, TaTiersDTO.class);
			}
			if(selectedDocumentDTO.getCodeTPaiement()!=null && !selectedDocumentDTO.getCodeTPaiement().equals("")) {
				taTPaiement = taTPaiementService.findByCode(selectedDocumentDTO.getCodeTPaiement());
			}
			
			
			if(selectedDocumentDTO.getCodeTTvaDoc()!=null && !selectedDocumentDTO.getCodeTTvaDoc().equals("")) {
				taTTvaDoc = taTTvaDocService.findByCode(selectedDocumentDTO.getCodeTTvaDoc());
			}
			
			if(selectedCPaiement.getCodeCPaiement()!=null && !selectedCPaiement.getCodeCPaiement().equals("")) {
				taCPaiementDoc = taCPaiementService.findByCode(selectedCPaiement.getCodeCPaiement());
			}	
			if(selectedDocumentDTO.getCodeTransporteur()!=null && !selectedDocumentDTO.getCodeTransporteur().equals("")) {
				taTransporteurDTO = taTransporteurService.findByCodeDTO(selectedDocumentDTO.getCodeTransporteur());
//				taTransporteurDTO = mapperModelToUI.map(taTransporteur, TaTransporteurDTO.class);
			}
			
			//rajouté temporairement pour aller vite, sera remplacé par la gestion des réglements multiples
			factureReglee=(masterEntity.getTaRReglementLiaison()!=null && masterEntity.getTaRReglementLiaison().getTaReglement()!=null
					&& masterEntity.getTaRReglementLiaison().getTaReglement().getIdDocument()!=0);
			if(masterEntity.getTaRReglementLiaison()!=null && masterEntity.getTaRReglementLiaison().getTaReglement()!=null){
				taReglement=masterEntity.getTaRReglementLiaison().getTaReglement();

				if(taReglement.getTaTPaiement()!=null && taReglement.getTaTPaiement().getIdTPaiement()!=0)
					taTPaiement=taTPaiementService.findById(taReglement.getTaTPaiement().getIdTPaiement());
				if(taTPaiement!=null){
					selectedDocumentDTO.setCodeTPaiement(taTPaiement.getCodeTPaiement());
				}
				selectedDocumentDTO.setLibelleReglement(taReglement.getLibelleDocument());
				selectedDocumentDTO.setMontantReglement(masterEntity.getTaRReglementLiaison().getAffectation());
			}else{
				selectedDocumentDTO.setLibelleReglement("");
				selectedDocumentDTO.setMontantReglement(BigDecimal.ZERO);
			}
			masterEntity.calculRegleDocument();
			factureARegler=masterEntity.getResteAReglerComplet().compareTo(BigDecimal.ZERO)!=0;					
		} catch (FinderException e) {
			e.printStackTrace();
		}
	}


//	public void onRowReorder(ReorderEvent event) {
//		int i=1;
//		
//		for (TaLBonLivraisonDTOJSF l : valuesLigne) {
//			l.getDto().setNumLigneLDocument(i);
//			if(l.getDto().getIdLDocument()!=null &&  l.getDto().getIdLDocument()!=0) {
//				masterEntityLigne=rechercheLigne(l.getDto().getIdLDocument());
//			}else if(l.getDto().getNumLigneLDocument()!=null) {
//				masterEntityLigne = rechercheLigneNumLigne(l.getDto().getNumLigneLDocument());
//			}
//			//change le num ligne et remplace l'odre des lignes dans la liste des lignes du document
//			masterEntityLigne.setNumLigneLDocument(i);
//			masterEntity.getLignes().remove(masterEntityLigne);
//			masterEntity.getLignes().add(masterEntityLigne.getNumLigneLDocument()-1, masterEntityLigne);
//			i++;
//		}
//		actModifier();
//		if(valuesLigne!=null && !valuesLigne.isEmpty())
//			selectionLigne(valuesLigne.get(0));
//	}
	/**
	 * @author yann
	 * On surcharge la méthode actModifier pour éviter le vérouillage quand mis a dispo
	 */
	public void actModifier(ActionEvent actionEvent) {
//		super.actModifier(actionEvent);
		if(!modeEcran.getMode().equals(EnumModeObjet.C_MO_EDITION) && !modeEcran.getMode().equals(EnumModeObjet.C_MO_INSERTION)) {
			try {
				masterEntity=taBonLivraisonService.findByIDFetch(masterEntity.getIdDocument());
				modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
			} catch (FinderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
//		super.actModifier(actionEvent);
			docEnregistre=false;
			masterEntity.setLegrain(true);
			initialisePositionBoutonWizard();
			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Facture", "actModifier")); 	 
			}
			ecranSynthese=modeEcran.getMode().equals(EnumModeObjet.C_MO_CONSULTATION);
			changementStepWizard(true);
//		}
	}
	public void actEnregistrer(ActionEvent actionEvent) {		
		try {
			masterEntity.calculeTvaEtTotaux();
			//verifSiDifferenceReglement();
			
//			if(differenceReglementResteARegle){
//				
//			}else{
			autoCompleteMapUIToDTO();
			validateDocumentAvantEnregistrement(selectedDocumentDTO);
			mapperUIToModel.map(selectedDocumentDTO, masterEntity);

			
			initInfosDocument();			
			mapperUIToModelDocumentVersInfosDoc.map(masterEntity, taInfosDocument);
			taInfosDocument.setNomTiers(selectedDocumentDTO.getNomTiers());
			taInfosDocument.setCodeTTvaDoc(selectedDocumentDTO.getCodeTTvaDoc());
			
			mapperUIToModelAdresseFactVersInfosDoc.map((AdresseInfosFacturationDTO) selectedAdresseFact, taInfosDocument);
			mapperUIToModelAdresseLivVersInfosDoc.map((AdresseInfosLivraisonDTO) selectedAdresseLiv, taInfosDocument);						
			mapperUIToModelCPaiementVersInfosDoc.map((TaCPaiementDTO) selectedCPaiement, taInfosDocument);

			
//			//vérifier remplissage du codeTTva
//			if(!((IStructuredSelection)viewerComboLocalisationTVA.getSelection()).isEmpty()) {
//				String codeTTvaDoc = ((TaTTvaDoc)((IStructuredSelection)viewerComboLocalisationTVA.getSelection()).getFirstElement()).getCodeTTvaDoc();
//				taInfosDocument.setCodeTTvaDoc(codeTTvaDoc);
//			}			
			TaTLigne typeLigneCommentaire =  taTLigneService.findByCode(Const.C_CODE_T_LIGNE_C);
			masterEntity.setLegrain(false);
			List<TaLBonliv> listeLigneVide = new ArrayList<TaLBonliv>(); 
			for (TaLBonliv ligne : masterEntity.getLignes()) {
				ligne.setLegrain(false);
				if(ligne.ligneEstVide() && ligne.getNumLigneLDocument().compareTo(masterEntity.getLignes().size())==0) {
					listeLigneVide.add(ligne);
				} else if(ligne.getTaArticle()==null) {
					ligne.setTaTLigne(typeLigneCommentaire);
				} else if(masterEntity.getGestionLot() && ligne.getTaArticle()!=null && ligne.getTaArticle().getGestionLot() && ligne.getTaLot()==null){
					throw new Exception("Le numéro du lot doit être rempli");
				} else {
					if(!masterEntity.getGestionLot() || (ligne.getTaArticle()!=null && !ligne.getTaArticle().getGestionLot())) {
						//utiliser un lot virtuel
						if(ligne.getTaLot()==null ){								
							listArticleLotEntrepot = new ArrayList<ArticleLotEntrepotDTO>();
							listArticleLotEntrepot = taMouvementStockService.getEtatStockByArticle(ligne.getTaArticle().getCodeArticle(),false);
							if(listArticleLotEntrepot!=null && listArticleLotEntrepot.size()>0) {
								ArticleLotEntrepotDTO lot=listArticleLotEntrepot.get(0);
								ligne.setTaLot(taLotService.findByCode(lot.getNumLot()));
								if(lot.getIdEntrepot()!=null)ligne.setTaEntrepot(taEntrepotService.findById(lot.getIdEntrepot()));
								if(lot.getEmplacement()!=null && !lot.getEmplacement().isEmpty())ligne.setEmplacementLDocument(lot.getEmplacement());
							}else {
								ligne.setTaLot(taLotService.findOrCreateLotVirtuelArticle(ligne.getTaArticle().getIdArticle()));
							}
						}
					}
				}
			}
			
			//supression des lignes vides
			for (TaLBonliv taLBonReception : listeLigneVide) {
				masterEntity.getLignes().remove(taLBonReception);
			}
			
			//suppression des liaisons entre ligne doc et ligne d'échéance
			for (TaLigneALigneEcheance ligneALigne : listeLigneALigneEcheanceASupprimer) {
				taLigneALigneEcheanceService.remove(ligneALigne);
			}
			
			//isa le 08/11/2016
			//j'ai rajouté cette réinitialisation tant que l'on enlève les lignes vides, sinon génère des trous dans la numérotation des lignes
			masterEntity.reinitialiseNumLignes(0, 1);
			
			/*
			 * Gérer les mouvements corrrespondant aux lignes
			 * si le document n'est pas déjà lié à un document qui en contient ou s'il en a déjà un 
			 */

			dejaLie=rechercheSiDocLie()!=null;
			if(masterEntity.getTaGrMouvStock()!=null || !dejaLie) {
				//Création du groupe de mouvement
				TaGrMouvStock grpMouvStock = new TaGrMouvStock();
				if(masterEntity.getTaGrMouvStock()!=null) {
					grpMouvStock=masterEntity.getTaGrMouvStock();
				}
				grpMouvStock.setCodeGrMouvStock(masterEntity.getCodeDocument());
				grpMouvStock.setDateGrMouvStock(masterEntity.getDateDocument());
				grpMouvStock.setLibelleGrMouvStock(masterEntity.getLibelleDocument()!=null?masterEntity.getLibelleDocument():"Bonliv "+masterEntity.getCodeDocument());
				grpMouvStock.setTaTypeMouvStock(taTypeMouvementService.findByCode("T")); // type mouvement Bonliv
				masterEntity.setTaGrMouvStock(grpMouvStock);
				grpMouvStock.setTaBonliv(masterEntity);

				for (TaLBonliv l : masterEntity.getLignes()) {
					if(!l.getTaTLigne().equals(typeLigneCommentaire)){
						l.setTaTLigne(taTLigneService.findByCode(Const.C_CODE_T_LIGNE_H));
						if(l.getTaMouvementStock()!=null)
							l.getTaMouvementStock().setTaGrMouvStock(masterEntity.getTaGrMouvStock());
					}
				}

				grpMouvStock.getTaMouvementStockes().clear();

				//Création des lignes de mouvement
				for (TaLBonliv ligne : masterEntity.getLignes()) {
					if(!ligne.getTaTLigne().equals(typeLigneCommentaire)){
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
						mouv.setDateStock(masterEntity.getDateDocument());
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
			}
			
			
			masterEntity = taBonLivraisonService.mergeAndFindById(masterEntity,ITaBonlivServiceRemote.validationContext);
			taBonLivraisonService.annuleCode(selectedDocumentDTO.getCodeDocument());

			
			mapperModelToUI.map(masterEntity, selectedDocumentDTO);
			autoCompleteMapDTOtoUI();
			selectedEtatDocument=null;
			if(masterEntity.getTaREtat()!=null)selectedEtatDocument=masterEntity.getTaREtat().getTaEtat();
			selectedDocumentDTOs=new TaBonlivDTO[]{selectedDocumentDTO};

			
			if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_INSERTION)==0) {
				values.add(selectedDocumentDTO);
			}

			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
			modeEcranLigne.setMode(EnumModeObjet.C_MO_CONSULTATION);
			if(impressionDirect) {
				if(impressionDirectClient) {
					imprimeEdition(null, "CLIENT", "true"); 
				}else {
					imprimeEdition(null, "BROUILLON", "false");
				}
				
			}
			
			if(envoyeParEmail) {
				envoiMailEdition(null, "CLIENT", "true");
			}
			
			updateTab();
			
			ecranSynthese=modeEcran.getMode().equals(EnumModeObjet.C_MO_CONSULTATION);
			if(wizardDocument!=null)wizardDocument.setStep(stepEntete);
			changementStepWizard(false);
			

//			}
			

			
		} catch(Exception e) {
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			//context.addMessage(null, new FacesMessage("Bonliv", "actEnregistrer")); 
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Bonliv", e.getMessage()));
		}


		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Bonliv", "actEnregistrer")); 
		}
	}

	public void actInsererLigne(ActionEvent actionEvent) {
		super.actInsererLigne(actionEvent);

		TaEtat etat = taBonLivraisonService.etatLigneInsertion(masterEntity);
		masterEntityLigne.addREtatLigneDoc(etat);
		
		selectedLigneJSF.setTaREtatLigneDocument(masterEntityLigne.getTaREtatLigneDocument());
		if(etat!=null)selectedLigneJSF.getDto().setCodeEtat(etat.getCodeEtat());
	}
	
	public void actInserer(ActionEvent actionEvent) {
		try {
			impressionDirectClient = false;
			miseADispositionCompteClient = false;
			envoyeParEmail = false;
			
			listePreferences= taPreferencesService.findByGroupe("bonliv");
			selectedDocumentDTO = new TaBonlivDTO();
			selectedDocumentDTO.setCommentaire(rechercheCommentaireDefautDocument());
			masterEntity = new TaBonliv();
			masterEntity.setTaGrMouvStock(new TaGrMouvStock());
			masterEntity.setLegrain(true);
			masterEntity.setUtiliseUniteSaisie(afficheUniteSaisie);//récupéré à partir d'une préférence
			selectedDocumentDTO.setUtiliseUniteSaisie(afficheUniteSaisie);

			//Tous les documents ont cet état par défaut vue avec philippe le 06/08/2019
//			TaEtat etat = taEtatService.findByCode(TaEtat.ETAT_ENCOURS_NON_TRANSFORME);
			TaEtat etat = taBonLivraisonService.rechercheEtatInitialDocument();
			masterEntity.addREtat(etat);
			
			valuesLigne = IHMmodel();
			selectedDocumentDTO.setCodeTTvaDoc("F");
			factureReglee=false;
			selectedEtatDocument=null;
			autoCompleteMapDTOtoUI();
			if(masterEntity.getTaREtat()!=null)selectedEtatDocument=masterEntity.getTaREtat().getTaEtat();
			initInfosDocument();


			//			selectedDocumentDTO.setCodeDocument(taBonReceptionService.genereCode()); //ejb
			//			validateUIField(Const.C_CODE_DOCUMENT,selectedDocumentDTO.getCodeDocument());//permet de verrouiller le code genere

//			selectedDocumentDTO.setCodeDocument(LibDate.dateToStringTimeStamp(new Date(), "", "", ""));
			taGenCodeExService.libereVerrouTout(new ArrayList<String>(SessionListener.getSessions().keySet()));
			Map<String, String> paramsCode = new LinkedHashMap<>();
			paramsCode.put(Const.C_DATEDOCUMENT, LibDate.dateToString(selectedDocumentDTO.getDateDocument()));
//			paramsCode.put(Const.C_NOMFOURNISSEUR, selectedDocumentDTO.getNomTiers());
			
			if(selectedDocumentDTO.getCodeDocument()!=null) {
				taBonLivraisonService.annuleCode(selectedDocumentDTO.getCodeDocument());
			}			

			selectedDocumentDTO.setCodeDocument(taBonLivraisonService.genereCode(paramsCode));
			changementTiers(true);
			
			modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
			
			ecranSynthese=modeEcran.getMode().equals(EnumModeObjet.C_MO_CONSULTATION);
			if(wizardDocument!=null)wizardDocument.setStep(stepEntete);
			changementStepWizard(false);
			
			TaPreferences p = taPreferencesService.findByGoupeAndCle(ITaPreferencesServiceRemote.GROUPE_PREFERENCES_LOTS, ITaPreferencesServiceRemote.LOTS_GESTION_DES_LOTS);
			if(p!=null && LibConversion.StringToBoolean(p.getValeur()) != null) {
				selectedDocumentDTO.setGestionLot(LibConversion.StringToBoolean(p.getValeur()));
				masterEntity.setGestionLot(LibConversion.StringToBoolean(p.getValeur()));
			}
			
			
			TaPreferences nb;
			nb = taPreferencesService.findByGoupeAndCle(ITaPreferencesServiceRemote.GROUPE_PREFERENCES_NB_DECIMALES, ITaPreferencesServiceRemote.NB_DECIMALES_PRIX);
			if(nb!=null && nb.getValeur() != null) {
				masterEntity.setNbDecimalesPrix(LibConversion.stringToInteger(nb.getValeur()));
			}

//			nb = taPreferencesService.findByGoupeAndCle(ITaPreferencesServiceRemote.GROUPE_PREFERENCES_NB_DECIMALES, ITaPreferencesServiceRemote.NB_DECIMALES_QTE);
//			if(nb!=null && nb.getValeur() != null) {
//				masterEntity.setNbDecimalesQte(LibConversion.stringToInteger(nb.getValeur()));
//			}
			
			scrollToTop();
			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Bonliv", "actInserer"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	public void actModifier() {
//		actModifier(null);
//	}
//
//	public void actModifier(ActionEvent actionEvent) {
//		if(!modeEcran.getMode().equals(EnumModeObjet.C_MO_EDITION) && !modeEcran.getMode().equals(EnumModeObjet.C_MO_INSERTION))
//			modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
//		masterEntity.setLegrain(true);
//		if(ConstWeb.DEBUG) {
//			FacesContext context = FacesContext.getCurrentInstance();  
//			context.addMessage(null, new FacesMessage("Bonliv", "actModifier")); 	 
//		}
//	}
//
//	public void actAide(ActionEvent actionEvent) {
//
//		//		PrimeFaces.current().dialog().openDynamic("aide");
//
//		Map<String,Object> options = new HashMap<String, Object>();
//		options.put("modal", true);
//		options.put("draggable", false);
//		options.put("resizable", false);
//		options.put("contentHeight", 320);
//		Map<String,List<String>> params = null;
//		PrimeFaces.current().dialog().openDynamic("aide", options, params);
//
//		if(ConstWeb.DEBUG) {
//			FacesContext context = FacesContext.getCurrentInstance();  
//			context.addMessage(null, new FacesMessage("Bonliv", "actAide")); 	
//		}
//	}

	public void nouveau(ActionEvent actionEvent) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_BON_LIVRAISON);
		b.setTitre("Bon de livraison");
		b.setTemplate("documents/bon_livraison.xhtml");
		b.setIdTab(LgrTab.ID_TAB_BON_LIVRAISON);
		b.setStyle(LgrTab.CSS_CLASS_TAB_LIVRAISON);
		tabsCenter.ajouterOnglet(b);
		b.setParamId("0");

//		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
		actInserer(actionEvent);

		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Bonliv", 
					"Nouveau"
					)); 
		}
	} 

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
//	public void actSupprimer() {
//		actSupprimer(null);
//	}
	public void actValiderCodeBarre(ActionEvent actionEvent) {
		try {
			if(taTiersDTO!=null && taTiersDTO.getCodeTiers()!=null && selectedLigneJSF.getDto().getCodeBarre()!=null && !selectedLigneJSF.getDto().getCodeBarre().equals("")) {
				//récupérer l'article qui correspond au codeBarre fournisseur (TaRefArticleFournisseur)
				
				Map<String, String>  listeRetour = taParamEan128Service.decodeEan128(selectedLigneJSF.getDto().getCodeBarre());
	    		String codeArticle = taParamEan128Service.decodeCodeArticle(listeRetour);
	    		String ean = taParamEan128Service.decodeEanArticle(listeRetour);
	    		String numLot = taParamEan128Service.decodeLotFabrication(listeRetour);
				
				TaArticle obj = taArticleService.findByCodeBarre(selectedLigneJSF.getDto().getCodeBarre());
//				TaRefArticleFournisseurDTO obj= trouveCodeBarre(selectedLigneJSF.getDto().getCodeBarre(), taTiersDTO.getCodeTiers());
				if(obj!=null) {
					selectedLigneJSF.getDto().setCodeArticle(obj.getCodeArticle());
					selectedLigneJSF.setTaArticleDTO(taArticleService.findByCodeDTO(obj.getCodeArticle()));
					validateUIField(Const.C_CODE_ARTICLE, selectedLigneJSF.getDto().getCodeArticle());
					selectedLigneJSF.getDto().setQteLDocument(BigDecimal.ONE);
					validateUIField(Const.C_QTE_L_DOCUMENT, selectedLigneJSF.getDto().getQteLDocument());
				} else {
					//actDialogRefArticleFournisseur(taTiersDTO.getCodeTiers(),selectedLigneJSF.getDto().getCodeBarre());
				}
			}
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	
	public RetourAutorisationLiaison autoriseSuppression() {
		return autoriseSuppression(false);
	}

//	public RetourAutorisationLiaison autoriseSuppression(boolean silencieux) {
//		RetourAutorisationLiaison retour = new RetourAutorisationLiaison();
//		TaRDocumentDTO suppressionDocInterdit=null;
//		messageSuppression=Const.C_MESSAGE_SUPPRESSION_DOCUMENT;
//	    messageModification=Const.C_MESSAGE_MODIFIFCATION_DOCUMENT;
//		if(selectedDocumentDTO!=null && selectedDocumentDTO.getCodeDocument()!=null && !selectedDocumentDTO.getCodeDocument().equals("")) {
//			messageSuppression=Const.C_MESSAGE_SUPPRESSION_DOCUMENT+" "+selectedDocumentDTO.getCodeDocument();
//	        messageModification=Const.C_MESSAGE_MODIFIFCATION_DOCUMENT+" "+selectedDocumentDTO.getCodeDocument();
//		}
//		List<TaRDocumentDTO> docLie=rechercheSiDocLie();
//		String documents = "";
//		if(docLie!=null) {
//			PrimeFaces.current().ajax().addCallbackParam("Autorise la suppression", docLie!=null);
//			if(docLie!=null)
//				for (TaRDocumentDTO doc : docLie) {
//					if(!documents.equals("") && !doc.getCodeDocumentDest().equals(selectedDocumentDTO.getCodeDocument()))
//						documents=documents+";"+doc.getCodeDocumentDest();
//					else if(!doc.getCodeDocumentDest().equals(selectedDocumentDTO.getCodeDocument())) documents=doc.getCodeDocumentDest();
//					if(suppressionDocInterdit==null && doc.getTypeDocumentDest().equals(TaFacture.TYPE_DOC))
//						suppressionDocInterdit=doc;
//					retour.lie=true;
//				}
//			if(!documents.equals("")) {
//				if(suppressionDocInterdit==null) {
//					messageSuppression="Suppression d'un document lié à un autre : Le document "+selectedDocumentDTO.getCodeDocument()+" est lié au(x) document(s) n° "+documents+"."
//							+ "\r\nEtes-vous sur de vouloir le supprimer ?";
//		            messageModification="Modification d'un document lié à un autre : Le document "+selectedDocumentDTO.getCodeDocument()+" est lié au(x) document(s) n° "+documents+"."
//		                    + "\r\nEtes-vous sur de vouloir le modifier ?";
//				}
//				else {
//					messageSuppression="Le document "+selectedDocumentDTO.getCodeDocument()+" est lié au document n° "+suppressionDocInterdit.getCodeDocumentDest()+"."
//							+ "\r\nSi vous souhaitez le supprimer, vous devez au préalable supprimer ce document.";
//		            messageModification="Le document "+selectedDocumentDTO.getCodeDocument()+" est lié au(x) document(s) n° "+suppressionDocInterdit.getCodeDocumentDest()+"."
//		            		 + "\r\nEtes-vous sur de vouloir le modifier ?";
//					if(!silencieux)PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage("Suppression d'un document lié à une autre",messageSuppression ));
//				}
//			}
//		}
//		retour.autorise=suppressionDocInterdit==null;
//		return retour;
//	}
	
//	public boolean autoriseSuppression() {
//		IDocumentTiers docLie=rechercheSiDocLie();
//		if(docLie!=null) {
//			RequestContext context = RequestContext.getCurrentInstance();
//			PrimeFaces.current().ajax().addCallbackParam("Autorise la suppression", docLie!=null);
//			if(docLie!=null)
//				PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage("Suppression d'un document lié à une autre", "Ce bon de livraison est lié à la facture n° "+docLie.getCodeDocument()+"."
//						+ "\r\nSi vous souhaitez le supprimer, vous devez au préalable supprimer cette facture."));
//		}
//		return docLie==null;
//	}
	
	public void actSupprimer(ActionEvent actionEvent) {
		TaBonliv obj = new TaBonliv();
		try {
			if(autorisationLiaisonComplete(false)) {
				if(selectedDocumentDTO!=null && selectedDocumentDTO.getId()!=null){
					obj = taBonLivraisonService.findByIDFetch(selectedDocumentDTO.getId());
				}

				taBonLivraisonService.remove(obj);
				values.remove(selectedDocumentDTO);

				if(!values.isEmpty()) {
					selectedDocumentDTO = values.get(0);
					selectedDocumentDTOs = new TaBonlivDTO[]{selectedDocumentDTO};
					updateTab();
				}else{
					selectedDocumentDTO = new TaBonlivDTO();
					selectedDocumentDTOs = new TaBonlivDTO[]{};
				}

				modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

				if(ConstWeb.DEBUG) {
					FacesContext context = FacesContext.getCurrentInstance();  
					context.addMessage(null, new FacesMessage("Bonliv", "actSupprimer"));
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Bonliv", e.getMessage()));
		}	    
	}

	public void actFermer(ActionEvent actionEvent) {
		//fermeture de l'onglet en JavaScript
		switch (modeEcran.getMode()) {
		case C_MO_INSERTION:
			actAnnuler(actionEvent);
			break;
		case C_MO_EDITION:
			actAnnuler(actionEvent);							
			break;

		default:
			break;
		}
	
		selectedDocumentDTO=new TaBonlivDTO();
		selectedDocumentDTOs=new TaBonlivDTO[]{selectedDocumentDTO};
		updateTab();

		
//		//gestion du filtre de la liste
//        RequestContext requestContext = RequestContext.getCurrentInstance();
//        requestContext.execute("PF('wvDataTableListeBL').filter()");
        
		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Bonliv", "actFermer")); 
		}
	}

	public void actImprimer(ActionEvent actionEvent) {
		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Bonliv", "actImprimer")); 
		}
		try {
			
			//Fiche_facture.rptdesign&=7332&typeTraite=null&PageBreakTotaux=27&ape=&ParamChoix=choix 1
			// &PageBreakMaxi=36&ParamCorr=null&CoupureLigne=54
			//		&nomEntreprise=EARL DE GRINORD&capital=&rcs=&siret=&__document=doc1469798570882&__format=pdf

			//		FacesContext facesContext = FacesContext.getCurrentInstance();
			//		HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);

			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			
			Map<String,Object> mapEdition = new HashMap<String,Object>();
			
			TaBonliv doc =taBonLivraisonService.findById(selectedDocumentDTO.getId());
			doc.calculeTvaEtTotaux();
			
			mapEdition.put("doc",doc );
			
			mapEdition.put("taInfoEntreprise", taInfoEntrepriseService.findInstance());
			//mapEdition.put("LibelleJournalTva", taTTvaDoc.getLibelleEdition());
			
			mapEdition.put("lignes", masterEntity.getLignes());
			
			//"select r from TaRReglement r join r.taBonliv f where f.idDocument  = "+idDocument;
//			mapEdition.put("taRReglement", masterEntity.getTaRReglements());
			
			//sessionMap.put("doc", taBonlivService.findById(selectedDocumentDTO.getId()));
			
			
			Map<String,Object> mapParametreEdition = new HashMap<String,Object>();
			List<TaPreferences> liste= taPreferencesService.findByGroupe("bonliv");
			mapParametreEdition.put("preferences", liste);
			mapParametreEdition.put("gestionLot", masterEntity.getGestionLot());
			mapEdition.put("param", mapParametreEdition);
			
			sessionMap.put("edition", mapEdition);

			//			session.setAttribute("tiers", taTiersService.findById(selectedDocumentDTO.getId()));
			System.out.println("BonlivController.actImprimer()");
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}    
	
	public void actImprimerListeDesLivraisons(ActionEvent actionEvent) {
		
		try {
			
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			
			sessionMap.put("listeDesLivraisons", values);

			} catch (Exception e) {
				e.printStackTrace();
			}
	}    

	public void actDialogImprimer(ActionEvent actionEvent) {
	    
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
		Map<String, Object> mapParametreEdition = null;
		
		if(actionEvent!=null){
			
			mapParametreEdition = new HashMap<>();
			boolean editionClient = false;	
			String modeEdition = (String)actionEvent.getComponent().getAttributes().get("mode_edition");
			String pourClient = (String)actionEvent.getComponent().getAttributes().get("pour_client");
			editionClient = LibConversion.StringToBoolean(pourClient,false);
			mapParametreEdition.put("editionClient", editionClient);
			
			List<TaPreferences> liste= taPreferencesService.findByGroupe("bonliv");
			mapParametreEdition.put("preferences", liste);
			mapParametreEdition.put("gestionLot", masterEntity.getGestionLot());
			
//			public void actImprimer(ActionEvent actionEvent) {
//				actImprimerGlobal(actionEvent,"CLIENT","CLIENTAPAYER");
//			}    


		}
		
		EditionParam edition = new EditionParam();
		edition.setIdActionInterne(ConstActionInterne.EDITION_BONLIV_DEFAUT);
		edition.setMapParametreEdition(mapParametreEdition);
		
		sessionMap.put(EditionParam.NOM_OBJET_EN_SESSION, edition);
        
        PrimeFaces.current().dialog().openDynamic("/dialog_choix_edition", options, params);
		
//		FacesContext context = FacesContext.getCurrentInstance();  
//		context.addMessage(null, new FacesMessage("Tiers", "actAbout")); 	    
	}
	
//	public void actImprimerEtiquetteCB(ActionEvent actionEvent) {
//		
//		etiquetteCodeBarreBean.videListe();
//		try {
//			for (TaLBonliv l : masterEntity.getLignes()) {
//				etiquetteCodeBarreBean.getListeArticle().add(l.getTaArticle());
//			}
//			
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
//		
//		if(ConstWeb.DEBUG) {
//		   	FacesContext context = FacesContext.getCurrentInstance();  
//		    context.addMessage(null, new FacesMessage("Bonliv", "actImprimerEtiquetteCB")); 
//		}
//	}

	public boolean pasDejaOuvert() {
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_FACTURE);
		return tabsCenter.ongletDejaOuvert(b)==null;
	} 

//	public void onRowSimpleSelect(SelectEvent event) {
//
//		try {
//			if(pasDejaOuvert()==false){
//				onRowSelect(event);
//	
//				autoCompleteMapDTOtoUI();
//				
//				masterEntity = taBonLivraisonService.findById(selectedDocumentDTO.getId());
//				masterEntity.calculeTvaEtTotaux();
//				
//				valuesLigne = IHMmodel();
//				
//				if(ConstWeb.DEBUG) {
//					FacesContext context = FacesContext.getCurrentInstance();  
//					context.addMessage(null, new FacesMessage("Bonliv", 
//							"Chargement du Bonliv N°"+selectedDocumentDTO.getCodeTiers()
//							)); 
//				}
//			} else {
//				tabViews.selectionneOngletCentre(LgrTab.TYPE_TAB_FACTURE);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
//	public void updateTab(){
//		try {		
//			modeEcranLigne.setMode(EnumModeObjet.C_MO_CONSULTATION);
//			
//			if(selectedDocumentDTOs!=null && selectedDocumentDTOs.length>0) {
//				selectedDocumentDTO = selectedDocumentDTOs[0];
//			}
//			if(selectedDocumentDTO.getId()!=null && selectedDocumentDTO.getId()!=0) {
//				masterEntity = taBonLivraisonService.findById(selectedDocumentDTO.getId());
//			}
//			
//			valuesLigne = IHMmodel();
//			
//			masterEntity.calculeTvaEtTotaux();
//			changementTiers(false);
//			
//			mapperModelToUI.map(masterEntity, selectedDocumentDTO);
//			
//			autoCompleteMapDTOtoUI();
//			
//			etatBouton("supprimer");
//			
//			if(ConstWeb.DEBUG) {
//				FacesContext context = FacesContext.getCurrentInstance();  
//				context.addMessage(null, new FacesMessage("Bonliv", 
//						"Chargement du Bonliv N°"+selectedDocumentDTO.getCodeTiers()
//						)); 
//			}
//		
//		} catch (FinderException e) {
//			e.printStackTrace();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

	public void onRowSelect(SelectEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_BON_LIVRAISON);
		b.setTitre("Bon de livraison");
		b.setTemplate("documents/bon_livraison.xhtml");
		b.setIdTab(LgrTab.ID_TAB_BON_LIVRAISON);
		b.setStyle(LgrTab.CSS_CLASS_TAB_LIVRAISON);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);

		updateTab();
		scrollToTop();
	} 

//	public boolean editable() {
//		return !modeEcran.dataSetEnModif();
//	}
//	public boolean editableEnInsertionUniquement() {
//		return !modeEcran.dataSetEnInsertion();
//	}
//	
//	public void actDialogTiers(ActionEvent actionEvent) {
//
//		//		PrimeFaces.current().dialog().openDynamic("aide");
//
//		Map<String,Object> options = new HashMap<String, Object>();
//		options.put("modal", true);
//		options.put("draggable", false);
//		options.put("resizable", false);
//		options.put("contentHeight", 640);
//		options.put("modal", true);
//
//		//Map<String,List<String>> params = null;
//		Map<String,List<String>> params = new HashMap<String,List<String>>();
//		List<String> list = new ArrayList<String>();
//		list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
//		params.put("modeEcranDefaut", list);
//
//		PrimeFaces.current().dialog().openDynamic("tiers/dialog_tiers", options, params);
//
//		//		FacesContext context = FacesContext.getCurrentInstance();  
//		//		context.addMessage(null, new FacesMessage("Bonliv", "actAbout")); 	    
//	}

//	public void handleReturnDialogTiers(SelectEvent event) {
//		if(event!=null && event.getObject()!=null) {
//			taTiers = (TaTiers) event.getObject();
//		}
//	}
//
//	public void actDialogTypeCivilite(ActionEvent actionEvent) {
//
//		//		PrimeFaces.current().dialog().openDynamic("aide");
//
//		Map<String,Object> options = new HashMap<String, Object>();
//		options.put("modal", true);
//		options.put("draggable", false);
//		options.put("resizable", false);
//		options.put("contentHeight", 640);
//		options.put("modal", true);
//
//		//Map<String,List<String>> params = null;
//		Map<String,List<String>> params = new HashMap<String,List<String>>();
//		List<String> list = new ArrayList<String>();
//		list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
//		params.put("modeEcranDefaut", list);
//
//		PrimeFaces.current().dialog().openDynamic("tiers/dialog_type_civilite", options, params);
//
//		//		FacesContext context = FacesContext.getCurrentInstance();  
//		//		context.addMessage(null, new FacesMessage("Bonliv", "actAbout")); 	    
//	}
//
//	public void handleReturnDialogTypeCivilite(SelectEvent event) {
//		//		if(event!=null && event.getObject()!=null) {
//		//			taTCivilite = (TaTCivilite) event.getObject();
//		//		}
//	}
//
//
//
//	public void actDialogTypeEntite(ActionEvent actionEvent) {
//
//		//		PrimeFaces.current().dialog().openDynamic("aide");
//
//		Map<String,Object> options = new HashMap<String, Object>();
//		options.put("modal", true);
//		options.put("draggable", false);
//		options.put("resizable", false);
//		options.put("contentHeight", 640);
//		options.put("modal", true);
//
//		//Map<String,List<String>> params = null;
//		Map<String,List<String>> params = new HashMap<String,List<String>>();
//		List<String> list = new ArrayList<String>();
//		list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
//		params.put("modeEcranDefaut", list);
//
//		PrimeFaces.current().dialog().openDynamic("tiers/dialog_type_entite", options, params);
//
//		//		FacesContext context = FacesContext.getCurrentInstance();  
//		//		context.addMessage(null, new FacesMessage("Bonliv", "actAbout")); 	    
//	}

//	public void handleReturnDialogTypeEntite(SelectEvent event) {
//		//		if(event!=null && event.getObject()!=null) {
//		//			taTEntite = (TaTEntite) event.getObject();
//		//		}
//	}
//
//	public boolean etatBouton(String bouton) {
//		boolean retour = true;
//		switch (modeEcran.getMode()) {
//		case C_MO_INSERTION:
//			switch (bouton) {
//				case "annuler":
//				case "enregistrer":
//				case "fermer":
//					retour = false;
//					break;
//				default:
//					break;
//			}
//			break;
//		case C_MO_EDITION:
//			switch (bouton) {
//			case "annuler":
//			case "enregistrer":
//			case "fermer":
//				retour = false;
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
//			case "supprimerListe":retour = false;break;	
//			case "modifier":
//			case "supprimer":
//			case "imprimer":
//				if(selectedDocumentDTO!=null && selectedDocumentDTO.getId()!=null  && selectedDocumentDTO.getId()!=0 ) retour = false;
//				break;				
//			default:
//				break;
//		}
//			break;
//		default:
//			break;
//		}
//		
//		return retour;
//
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
//				retour = false;
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
	
	public void validateLignesDocument(FacesContext context, UIComponent component, Object value) throws ValidatorException {

		String msg = "";

		try {
			//String selectedRadio = (String) value;

			String nomChamp =  (String) component.getAttributes().get("nomChamp");

			//msg = "Mon message d'erreur pour : "+nomChamp;

			validateUIField(nomChamp,value);
			TaLBonlivDTO dtoTemp =new TaLBonlivDTO();
			PropertyUtils.setSimpleProperty(dtoTemp, nomChamp, value);
			taLBonlivService.validateDTOProperty(dtoTemp, nomChamp, ITaLBonlivServiceRemote.validationContext );

			//selectedDocumentDTO.setAdresse1Adresse("abcd");

			//		  if(selectedRadio.equals("aa")) {
			//			  throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
			//		  }

		} catch(Exception e) {
			msg += e.getMessage();
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
		}
	}
	
	public void validateDocument(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		
		String messageComplet = "";
		try {
			String nomChamp =  (String) component.getAttributes().get("nomChamp");
			
			if(nomChamp.equals(Const.C_TX_REM_HT_DOCUMENT)||nomChamp.equals(Const.C_TX_REM_TTC_DOCUMENT)){
				if(value==null || value.equals(""))value=BigDecimal.ZERO;
			}
			
			//validation automatique via la JSR bean validation
			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
			Set<ConstraintViolation<TaBonlivDTO>> violations = factory.getValidator().validateValue(TaBonlivDTO.class,nomChamp,value);
			if (violations.size() > 0) {
				messageComplet = "Erreur de validation : ";
				for (ConstraintViolation<TaBonlivDTO> cv : violations) {
					messageComplet+=" "+cv.getMessage()+"\n";
				}
				throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, messageComplet, messageComplet));
			} else {
				//aucune erreur de validation "automatique", on déclanche les validations du controller
				validateUIField(nomChamp,value);
			}
		} catch(Exception e) {
			//messageComplet += e.getMessage();
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, messageComplet, messageComplet));
		}
	}

	public void validateDocumentAvantEnregistrement( Object value) throws ValidatorException {

		String msg = "";

		try {
			//taBonReceptionService.validateDTOProperty(selectedDocumentDTO,Const.C_CODE_TIERS,  ITaBonlivServiceRemote.validationContext );

		} catch(Exception e) {
			msg += e.getMessage();
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
		}
	}
	
	public void validateLigneDocumentAvantEnregistrement( Object value) throws ValidatorException {

		String msg = "";

		try {

			if(afficheUniteSaisie && selectedLigneJSF.getTaCoefficientUniteSaisie()!=null)
				validateUIField(Const.C_QTE_SAISIE_L_DOCUMENT, selectedLigneJSF.getDto().getQteSaisieLDocument());
			else
				if(selectedLigneJSF.getTaCoefficientUnite()!=null) {
					if(!calculCoherenceAffectationCoefficientQte2(selectedLigneJSF.getDto().getQte2LDocument())) {
						setLigneAReenregistrer(selectedLigneJSF);
						PrimeFaces.current().executeScript("PF('wVdialogQte2BonLivraison').show()");
					}

				}

		} catch(Exception e) {
			msg += e.getMessage();
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
		}
	}
	
//	public void autcompleteSelection(SelectEvent event) {
//		Object value = event.getObject();
//		//		FacesMessage msg = new FacesMessage("Selected", "Item:" + value);
//
//		String nomChamp =  (String) event.getComponent().getAttributes().get("nomChamp");
//		System.out.println("BonlivController.autcompleteSelection() : "+nomChamp);
//
//		if(value!=null) {
//			if(value instanceof String && value.equals(Const.C_AUCUN))value="";	
//			if(value instanceof TaUniteDTO && ((TaUniteDTO) value).getCodeUnite()!=null && ((TaUniteDTO) value).getCodeUnite().equals(Const.C_AUCUN))value=null;	
//		}
//		validateUIField(nomChamp,value);
//	}
	
	public boolean validateUIField(String nomChamp,Object value) {

		boolean changement=false;

		try {				
			if(nomChamp.equals(Const.C_CODE_TIERS)) {
				TaTiers entity = null;
				if(value!=null){
					if(value instanceof TaTiersDTO){
//						entity=(TaTiers) value;
						entity = taTiersService.findByCode(((TaTiersDTO)value).getCodeTiers());
					}else{
						entity = taTiersService.findByCode((String)value);
					}
					
					changement=!entity.equalsCode(masterEntity.getTaTiers());


				}
				masterEntity.setTaTiers(entity);
				if(changement){
					String nomTiers=masterEntity.getTaTiers().getNomTiers();
					((TaBonlivDTO)selectedDocumentDTO).setLibelleDocument("Bonliv N°"+selectedDocumentDTO.getCodeDocument()+" - "+nomTiers);										
					if(!disableTtc() && !disableTtcSiDocSansTVA()){
						masterEntity.setTtc(entity.getTtcTiers());
						((TaBonlivDTO)selectedDocumentDTO).setTtc(LibConversion.intToBoolean(masterEntity.getTtc()));
					}									
					changementTiers(true);
				}
			} else if(nomChamp.equals(Const.C_NUM_LOT)) {
				selectedLigneJSF.getDto().setEmplacement(null);
				if( selectedLigneJSF.getArticleLotEntrepotDTO().getEmplacement()!=null 
						&& !selectedLigneJSF.getArticleLotEntrepotDTO().getEmplacement().equals("")) {
					selectedLigneJSF.getDto().setEmplacement(selectedLigneJSF.getArticleLotEntrepotDTO().getEmplacement());
				}
				selectedLigneJSF.getDto().setCodeEntrepot(selectedLigneJSF.getArticleLotEntrepotDTO().getCodeEntrepot());
				
				TaEntrepot entrepot =null;
				entrepot = taEntrepotService.findByCode(selectedLigneJSF.getArticleLotEntrepotDTO().getCodeEntrepot());
				selectedLigneJSF.setTaEntrepot(entrepot);
				
				TaLot lot =null;
				lot = taLotService.findByCode(selectedLigneJSF.getArticleLotEntrepotDTO().getNumLot());
				selectedLigneJSF.setTaLot(lot);
				if(lot!=null) {
					selectedLigneJSF.getDto().setLibLDocument(lot.getLibelle());
					masterEntityLigne.setLibLDocument(lot.getLibelle());
				}
			
			}else if(nomChamp.equals(Const.C_DATE_DOCUMENT)) {
				dateDansPeriode((Date)value,nomChamp);

			}else if(nomChamp.equals(Const.C_DATE_ECH_DOCUMENT)) {				
				dateDansPeriode((Date)value,nomChamp);
			}else if(nomChamp.equals(Const.C_CODE_ARTICLE)) {
				TaArticle entity = null;
				TaPrix prix =null;
				boolean changementArticleLigne = false;
				if(value!=null){
					if(value instanceof TaArticleDTO){
						entity = taArticleService.findByCode(((TaArticleDTO) value).getCodeArticle());
					}else{
						entity = taArticleService.findByCode((String)value);
					}
				}
				if(entity!=null) {
					 prix = entity.chercheTarif(masterEntity.getTaTiers());
					 if(prix==null)prix=new TaPrix(BigDecimal.ZERO,BigDecimal.ZERO);
				}
				if(selectedLigneJSF.getTaArticle()== null || selectedLigneJSF.getTaArticle().getIdArticle()!=entity.getIdArticle()) {
					changementArticleLigne = true;
				}
				selectedLigneJSF.setTaArticle(entity);
				masterEntityLigne.setTaArticle(entity);
				if(changementArticleLigne) {
					if(entity!=null)selectedLigneJSF.getDto().setLibLDocument(entity.getLibellecArticle());
//					selectedLigneJSF.getDto().setDluo(LibDate.incrementDate(selectedLigneJSF.getDateDocument(), LibConversion.stringToInteger(entity.getParamDluo(), 0)+1  , 0, 0));
//					selectedLigneJSF.getDto().setLibelleLot(entity.getLibellecArticle());
					selectedLigneJSF.setTaUniteSaisie(null);
					selectedLigneJSF.getDto().setUSaisieLDocument(null);
					selectedLigneJSF.getDto().setQteSaisieLDocument(BigDecimal.ZERO);
					selectedLigneJSF.setTaCoefficientUniteSaisie(null);

					selectedLigneJSF.setTaLot(null);
					selectedLigneJSF.setTaEntrepot(null);
					selectedLigneJSF.getDto().setCodeEntrepot(null);
					selectedLigneJSF.getDto().setNumLot(null);
					if(!masterEntity.getGestionLot() || (selectedLigneJSF.getTaArticle()!=null && !selectedLigneJSF.getTaArticle().getGestionLot())) {
						//utiliser un lot virtuel
						if(selectedLigneJSF.getTaLot()==null ){								
							listArticleLotEntrepot = new ArrayList<ArticleLotEntrepotDTO>();
							listArticleLotEntrepot = taMouvementStockService.getEtatStockByArticle(selectedLigneJSF.getTaArticle().getCodeArticle(),false);
							if(listArticleLotEntrepot!=null && listArticleLotEntrepot.size()>0) {
								ArticleLotEntrepotDTO lot=listArticleLotEntrepot.get(0);
								selectedLigneJSF.setTaLot(taLotService.findByCode(lot.getNumLot()));
								if(lot.getIdEntrepot()!=null)selectedLigneJSF.setTaEntrepot(taEntrepotService.findById(lot.getIdEntrepot()));
//								if(lot.getEmplacement()!=null && !lot.getEmplacement().isEmpty())selectedLigneJSF.getDto().setEmplacementLDocument(lot.getEmplacement());
							}else {
								selectedLigneJSF.setTaLot(taLotService.findOrCreateLotVirtuelArticle(selectedLigneJSF.getTaArticle().getIdArticle()));
							}
						}
					}					
				}
				selectedLigneJSF.setTaUnite1(null);
				selectedLigneJSF.setTaUnite2(null);
				selectedLigneJSF.getDto().setU2LDocument(null);
				selectedLigneJSF.getDto().setQte2LDocument(BigDecimal.ZERO);
				TaRapportUnite rapport = null;
				if(entity!=null) rapport=entity.getTaRapportUnite();
				TaCoefficientUnite coef = null;
				if(rapport!=null){
					if(rapport.getTaUnite2()!=null) {
						coef=recupCoefficientUnite(rapport.getTaUnite1().getCodeUnite(),rapport.getTaUnite2().getCodeUnite());
						selectedLigneJSF.setTaCoefficientUnite(coef);
					}
				}
				if(entity!=null && entity.getTaUniteSaisie()!=null && selectedDocumentDTO.getUtiliseUniteSaisie()) {
					selectedLigneJSF.getDto().setUSaisieLDocument(entity.getTaUniteSaisie().getCodeUnite());
					selectedLigneJSF.setTaUniteSaisie(taUniteService.findByCode(entity.getTaUniteSaisie().getCodeUnite()));
					selectedLigneJSF.getDto().setQteSaisieLDocument(BigDecimal.ONE);
				}

				if(coef!=null){
					selectedLigneJSF.setTaCoefficientUnite(coef);
					if(entity.getTaUnite1()!=null) {
						selectedLigneJSF.getDto().setU1LDocument(entity.getTaUnite1().getCodeUnite());
						selectedLigneJSF.setTaUnite1(taUniteService.findByCode(entity.getTaUnite1().getCodeUnite()));
					}
					if(coef.getUniteB()!=null && coef.getUniteB().getCodeUnite().equals(rapport.getTaUnite2().getCodeUnite())){
						selectedLigneJSF.getDto().setU2LDocument(coef.getUniteB().getCodeUnite());
						selectedLigneJSF.setTaUnite2(taUniteService.findByCode(coef.getUniteB().getCodeUnite()));
					}else if(coef.getUniteA()!=null && coef.getUniteA().getCodeUnite().equals(rapport.getTaUnite2().getCodeUnite())){
						selectedLigneJSF.getDto().setU2LDocument(coef.getUniteA().getCodeUnite());
						selectedLigneJSF.setTaUnite2(taUniteService.findByCode(coef.getUniteA().getCodeUnite()));
					}
				}else
					if(entity!=null ){
						if(entity.getTaUnite1()!=null) {
							selectedLigneJSF.getDto().setU1LDocument(entity.getTaUnite1().getCodeUnite());
							selectedLigneJSF.setTaUnite1(taUniteService.findByCode(entity.getTaUnite1().getCodeUnite()));
						}
						for (TaRapportUnite obj : entity.getTaRapportUnites()) {
							if(obj.getTaUnite1()!=null && obj.getTaUnite1().getCodeUnite().equals(selectedLigneJSF.getDto().getU1LDocument())){
								if(obj!=null){
									if(obj.getTaUnite2()!=null) {										
										coef=recupCoefficientUnite(obj.getTaUnite1().getCodeUnite(),obj.getTaUnite2().getCodeUnite());
									}
								}
								selectedLigneJSF.setTaCoefficientUnite(coef);
								if(coef!=null) {
									if(coef.getUniteB()!=null && coef.getUniteB().getCodeUnite().equals(obj.getTaUnite2().getCodeUnite())){
									selectedLigneJSF.getDto().setU2LDocument(coef.getUniteB().getCodeUnite());
									selectedLigneJSF.setTaUnite2(taUniteService.findByCode(coef.getUniteB().getCodeUnite()));
									}else if(coef.getUniteA()!=null && coef.getUniteA().getCodeUnite().equals(obj.getTaUnite2().getCodeUnite())){
										selectedLigneJSF.getDto().setU2LDocument(coef.getUniteA().getCodeUnite());
										selectedLigneJSF.setTaUnite2(taUniteService.findByCode(coef.getUniteA().getCodeUnite()));
									}
								}else if(obj.getTaUnite2()!=null){
									selectedLigneJSF.getDto().setU2LDocument(obj.getTaUnite2().getCodeUnite());
									selectedLigneJSF.setTaUnite2(taUniteService.findByCode(obj.getTaUnite2().getCodeUnite()));
								}
							}							
						}
					}
				if(entity!=null && entity.getTaTva()!=null && masterEntity.isGestionTVA()){
					selectedLigneJSF.getDto().setCodeTvaLDocument(entity.getTaTva().getCodeTva());
					selectedLigneJSF.getDto().setTauxTvaLDocument(entity.getTaTva().getTauxTva());
				}else {
					selectedLigneJSF.getDto().setCodeTvaLDocument(null);
					selectedLigneJSF.getDto().setTauxTvaLDocument(null);
					
				}
				if(prix!=null){
					selectedLigneJSF.getDto().setQteLDocument(new BigDecimal(1));
					validateUIField(Const.C_QTE_L_DOCUMENT, selectedLigneJSF.getDto().getQteLDocument());
					if(masterEntity.getTtc()==1) {
						selectedLigneJSF.getDto().setPrixULDocument(prix.getPrixttcPrix());
					} else {
						selectedLigneJSF.getDto().setPrixULDocument(prix.getPrixPrix());
					}

					selectedLigneJSF.getDto().setMtHtLDocument(masterEntityLigne.getMtHtLDocument());
					selectedLigneJSF.getDto().setMtTtcLDocument(masterEntityLigne.getMtTtcLDocument());
				}

				if(entity!=null && entity.getTaRTaTitreTransport()!=null) {
					BigDecimal qteCrd = BigDecimal.ZERO;	
					selectedLigneJSF.setTaTitreTransport(entity.getTaRTaTitreTransport().getTaTitreTransport());
						if(entity.getTaRTaTitreTransport().getQteTitreTransport()!=null )
							qteCrd=entity.getTaRTaTitreTransport().getQteTitreTransport();
						selectedLigneJSF.getDto().setQteTitreTransport(selectedLigneJSF.getDto().getQteLDocument().multiply(qteCrd));
				} else {
					selectedLigneJSF.setTaTitreTransport(null);
					selectedLigneJSF.getDto().setQteTitreTransport(BigDecimal.ZERO);
				}


				masterEntityLigne.setTauxTvaLDocument(selectedLigneJSF.getDto().getTauxTvaLDocument());
				masterEntityLigne.setPrixULDocument(selectedLigneJSF.getDto().getPrixULDocument());
				masterEntityLigne.calculMontant();
				selectedLigneJSF.getDto().setMtHtLDocument(masterEntityLigne.getMtHtLDocument());
				selectedLigneJSF.getDto().setMtTtcLDocument(masterEntityLigne.getMtTtcLDocument());
				if(entity!=null && entity.getTaUniteSaisie()!=null && selectedDocumentDTO.getUtiliseUniteSaisie()) {
					validateUIField(Const.C_QTE_SAISIE_L_DOCUMENT, selectedLigneJSF.getDto().getQteSaisieLDocument());	
				}

			}else if(nomChamp.equals(Const.C_PRIX_U_L_DOCUMENT)) {
				BigDecimal prix=BigDecimal.ZERO;
				if(value!=null){
					if(!value.equals("")){
						prix=(BigDecimal)value;
					}}				
				masterEntityLigne.setPrixULDocument(prix);
				masterEntityLigne.calculMontant();
				selectedLigneJSF.getDto().setMtHtLDocument(masterEntityLigne.getMtHtLDocument());
				selectedLigneJSF.getDto().setMtTtcLDocument(masterEntityLigne.getMtTtcLDocument());
		   } else if(nomChamp.equals(Const.C_QTE_SAISIE_L_DOCUMENT)) {
				BigDecimal qte=BigDecimal.ZERO;
				if(value!=null){
					if(!value.equals("")){
						qte=(BigDecimal)value;
					}
					selectedLigneJSF.setTaCoefficientUniteSaisie(recupCoefficientUnite(selectedLigneJSF.getDto().getUSaisieLDocument(),selectedLigneJSF.getDto().getU1LDocument()));
					if(selectedLigneJSF.getTaCoefficientUniteSaisie()!=null) {
						if(selectedLigneJSF.getTaCoefficientUniteSaisie().getOperateurDivise()) 
							selectedLigneJSF.getDto().setQteLDocument((qte).divide(selectedLigneJSF.getTaCoefficientUniteSaisie().getCoeff()
									,MathContext.DECIMAL128).setScale(selectedLigneJSF.getTaCoefficientUniteSaisie().getNbDecimale(),BigDecimal.ROUND_HALF_UP));
						else selectedLigneJSF.getDto().setQteLDocument((qte).multiply(selectedLigneJSF.getTaCoefficientUniteSaisie().getCoeff()));
					}else  {
						selectedLigneJSF.getDto().setQteLDocument(BigDecimal.ZERO);
						masterEntityLigne.setQteLDocument(null);
					}
				} else {
					masterEntityLigne.setQteLDocument(null);
				}
				validateUIField(Const.C_QTE_L_DOCUMENT, selectedLigneJSF.getDto().getQteLDocument());
//				masterEntityLigne.setQteLDocument(qte);
//				selectedLigneJSF.getDto().setMtHtLDocument(masterEntityLigne.getMtHtLDocument());
//				selectedLigneJSF.getDto().setMtTtcLDocument(masterEntityLigne.getMtTtcLDocument());
			
		} else if(nomChamp.equals(Const.C_U_SAISIE_L_DOCUMENT)) {
			TaUnite entity =null;
			if(value!=null){
				if(value instanceof TaUnite){
					entity=(TaUnite) value;
					entity = taUniteService.findByCode(entity.getCodeUnite());
				}else{
					entity = taUniteService.findByCode((String)value);
				}
				masterEntityLigne.setUSaisieLDocument(entity.getCodeUnite());
				selectedLigneJSF.getDto().setUSaisieLDocument(entity.getCodeUnite());
				selectedLigneJSF.getDto().setQteSaisieLDocument(BigDecimal.ONE);	
			}else {
				selectedLigneJSF.getDto().setUSaisieLDocument("");
				masterEntityLigne.setUSaisieLDocument(null);
				selectedLigneJSF.getDto().setQteSaisieLDocument(null);	
			}
			selectedLigneJSF.setTaCoefficientUniteSaisie(recupCoefficientUnite(selectedLigneJSF.getDto().getUSaisieLDocument(),selectedLigneJSF.getDto().getU1LDocument()));
			validateUIField(Const.C_QTE_SAISIE_L_DOCUMENT, selectedLigneJSF.getDto().getQteSaisieLDocument());	
			validateUIField(Const.C_QTE_L_DOCUMENT, selectedLigneJSF.getDto().getQteLDocument());
			validateUIField(Const.C_QTE2_L_DOCUMENT, selectedLigneJSF.getDto().getQte2LDocument());
		}else if(nomChamp.equals(Const.C_QTE_L_DOCUMENT)) {
				BigDecimal qte=BigDecimal.ZERO;
				if(value!=null){
					if(!value.equals("")){
						qte=(BigDecimal)value;
					}
					selectedLigneJSF.setTaCoefficientUnite(recupCoefficientUnite(selectedLigneJSF.getDto().getU1LDocument(),selectedLigneJSF.getDto().getU2LDocument()));
					if(selectedLigneJSF.getTaCoefficientUnite()!=null) {
						if(selectedLigneJSF.getTaCoefficientUnite().getOperateurDivise()) 
							selectedLigneJSF.getDto().setQte2LDocument((qte).divide(selectedLigneJSF.getTaCoefficientUnite().getCoeff()
									,MathContext.DECIMAL128).setScale(selectedLigneJSF.getTaCoefficientUnite().getNbDecimale(),BigDecimal.ROUND_HALF_UP));
						else selectedLigneJSF.getDto().setQte2LDocument((qte).multiply(selectedLigneJSF.getTaCoefficientUnite().getCoeff()));
					}else  {
						selectedLigneJSF.getDto().setQte2LDocument(BigDecimal.ZERO);
						masterEntityLigne.setQte2LDocument(null);
					}
				} else {
					masterEntityLigne.setQte2LDocument(null);
				}
				
				if(selectedLigneJSF.getTaTitreTransport()!=null) {
					BigDecimal qteCrd =BigDecimal.ZERO;
					if(selectedLigneJSF.getTaArticle().getTaRTaTitreTransport().getQteTitreTransport()!=null )
						qteCrd=selectedLigneJSF.getTaArticle().getTaRTaTitreTransport().getQteTitreTransport();
					selectedLigneJSF.getDto().setQteTitreTransport(qte.multiply(qteCrd));
				} else {
					selectedLigneJSF.getDto().setQteTitreTransport(BigDecimal.ZERO);
				}

				
				masterEntityLigne.setQteLDocument(qte);
				selectedLigneJSF.getDto().setMtHtLDocument(masterEntityLigne.getMtHtLDocument());
				selectedLigneJSF.getDto().setMtTtcLDocument(masterEntityLigne.getMtTtcLDocument());
			
		} else if(nomChamp.equals(Const.C_U1_L_DOCUMENT)) {
			TaUnite entity =null;
			if(value!=null){
				if(value instanceof TaUnite){
					entity=(TaUnite) value;
					entity = taUniteService.findByCode(entity.getCodeUnite());
				}else{
					entity = taUniteService.findByCode((String)value);
				}
				masterEntityLigne.setU1LDocument(entity.getCodeUnite());
				selectedLigneJSF.getDto().setU1LDocument(entity.getCodeUnite());
			}else {
				selectedLigneJSF.getDto().setU1LDocument("");
				masterEntityLigne.setU1LDocument(null);
			}
			selectedLigneJSF.setTaCoefficientUnite(recupCoefficientUnite(selectedLigneJSF.getDto().getU1LDocument(),selectedLigneJSF.getDto().getU2LDocument()));
			validateUIField(Const.C_QTE_L_DOCUMENT, selectedLigneJSF.getDto().getQteLDocument());
		} else if(nomChamp.equals(Const.C_QTE2_L_DOCUMENT)) {
			BigDecimal qte=BigDecimal.ZERO;
			if(value==null) {
				masterEntityLigne.setQte2LDocument(null);
			}else if(!value.equals("")){
				qte=(BigDecimal)value;
			}
			selectedLigneJSF.getDto().setQte2LDocument(qte);
			
		}else if(nomChamp.equals(Const.C_U2_L_DOCUMENT)) {
			TaUnite entity =null;
			if(value!=null){
				if(value instanceof TaUnite){
					entity=(TaUnite) value;
					entity = taUniteService.findByCode(entity.getCodeUnite());
				}else{
					entity = taUniteService.findByCode((String)value);
				}
				masterEntityLigne.setU2LDocument(entity.getCodeUnite());
				selectedLigneJSF.getDto().setU2LDocument(entity.getCodeUnite());
			}else {
				selectedLigneJSF.getDto().setU2LDocument("");
				masterEntityLigne.setU2LDocument(null);
			}
			selectedLigneJSF.setTaCoefficientUnite(recupCoefficientUnite(selectedLigneJSF.getDto().getU1LDocument(),selectedLigneJSF.getDto().getU2LDocument()));
			validateUIField(Const.C_QTE_L_DOCUMENT, selectedLigneJSF.getDto().getQteLDocument());
		} else if(nomChamp.equals(Const.C_CODE_TITRE_TRANSPORT)) {
			TaTitreTransport entity = null;
			if(value!=null){
				entity = (TaTitreTransport) value;
			}
			if(entity!=null) {
				masterEntityLigne.setCodeTitreTransport(entity.getCodeTitreTransport());
				selectedLigneJSF.setTaTitreTransport(entity);
				selectedLigneJSF.setTaTitreTransport(entity);
			}else {
				masterEntityLigne.setCodeTitreTransport(null);
				masterEntityLigne.setQteTitreTransport(null);
				selectedLigneJSF.setTaTitreTransport(entity);
			}			
		} else if(nomChamp.equals(Const.C_CODE_T_PAIEMENT)) {
			TaTPaiement entity = null;
			if(value!=null){
				if(value instanceof TaTPaiement){
					entity = (TaTPaiement) value;
				}else{
					entity = taTPaiementService.findByCode((String)value);
				}
			}
			if(entity!=null && masterEntity.getTaTPaiement()!=null){
				if(entity.getCodeTPaiement()!=null) {
					changement=!entity.getCodeTPaiement().equals(masterEntity.getTaTPaiement().getCodeTPaiement());
				}
			}
			masterEntity.setTaTPaiement(entity);
			taTPaiement=entity;
//			if(changement || masterEntity.getTaReglement()==null) {
//				actInitReglement();
//			}
		}else if(nomChamp.equals(Const.C_CODE_C_PAIEMENT)) {
			TaCPaiement entity = null;
			if(value!=null){
				entity = (TaCPaiement) value;
			}
			setTaCPaiementDoc(entity);
			if(entity!=null) {
				selectedCPaiement.setCodeCPaiement(entity.getCodeCPaiement());
				selectedCPaiement.setFinMoisCPaiement(entity.getFinMoisCPaiement());
				selectedCPaiement.setReportCPaiement(entity.getReportCPaiement());
				selectedCPaiement.setLibCPaiement(entity.getLibCPaiement());
			}			
		} 
		else	if(nomChamp.equals(Const.C_TX_REM_HT_DOCUMENT)) {
			BigDecimal tx=BigDecimal.ZERO;
			if(value!=null){
				if(!value.equals("")){
					tx=(BigDecimal)value;
				}
				masterEntity.setTxRemHtDocument(tx);
				mapperModelToUI.map(masterEntity, selectedDocumentDTO);
			}
		} else	if(nomChamp.equals(Const.C_TX_REM_TTC_DOCUMENT)) {
			BigDecimal tx=BigDecimal.ZERO;
			if(value!=null){
				if(!value.equals("")){
					tx=(BigDecimal)value;
				}
				masterEntity.setTxRemTtcDocument(tx);
				mapperModelToUI.map(masterEntity, selectedDocumentDTO);
			}
		} else	if(nomChamp.equals(Const.C_REM_TX_L_DOCUMENT)) {
			BigDecimal tx=BigDecimal.ZERO;
			if(value!=null){
				if(!value.equals("")){
					tx=(BigDecimal)value;
				}
			}
			masterEntityLigne.setRemTxLDocument(tx);
			selectedLigneJSF.getDto().setMtHtLDocument(masterEntityLigne.getMtHtLDocument());
			selectedLigneJSF.getDto().setMtTtcLDocument(masterEntityLigne.getMtTtcLDocument());
		}else if(nomChamp.equals(Const.C_EXPORT)) {
		} else if(nomChamp.equals(Const.C_CODE_T_TVA_DOC)) {
			if(value!=null){
				if(value instanceof TaTTvaDoc){
					selectedDocumentDTO.setCodeTTvaDoc(((TaTTvaDoc) value).getCodeTTvaDoc());
				}else if(value instanceof String){
					selectedDocumentDTO.setCodeTTvaDoc((String) value);
				}
			}
			initLocalisationTVA();
			//si TTC est vrai et que codeTvaDoc différent de France alors on remets TTC à faux
			//car la saisie dans ce cas là doit se faire sur le HT
			if(selectedDocumentDTO.getTtc() && disableTtcSiDocSansTVA()) {
				selectedDocumentDTO.setTtc(false);
				validateUIField(Const.C_TTC, selectedDocumentDTO.getTtc());
			}
		} else if(nomChamp.equals(Const.C_TTC)) {
			masterEntity.setTtc(LibConversion.booleanToInt(selectedDocumentDTO.getTtc()));

		}else if(nomChamp.equals(Const.C_CODE_TRANSPORTEUR)) {
			TaTransporteur entity = null;
			if(value!=null){
				if(value instanceof TaTransporteurDTO){
					entity = taTransporteurService.findByCode(((TaTransporteurDTO)value).getCodeTransporteur());
				}else{
					entity = taTransporteurService.findByCode((String)value);
				}
				
				changement=!entity.equalsCode(masterEntity.getTaTiers());


			}
			masterEntity.setTaTransporteur(entity);
		}			
		return false;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	

//	public List<TaTiers> tiersAutoComplete(String query) {
//		List<TaTiers> allValues = taTiersService.selectAll();
//		List<TaTiers> filteredValues = new ArrayList<TaTiers>();
//
//		for (int i = 0; i < allValues.size(); i++) {
//			TaTiers civ = allValues.get(i);
//			if(query.equals("*") || civ.getCodeTiers().toLowerCase().contains(query.toLowerCase())) {
//				filteredValues.add(civ);
//			}
//		}
//
//		return filteredValues;
//	}
//
//	public List<TaTiersDTO> tiersAutoCompleteDTOLight(String query) {
//		List<TaTiersDTO> allValues = taTiersService.findByCodeLight("*");
//		List<TaTiersDTO> filteredValues = new ArrayList<TaTiersDTO>();
//
//		for (int i = 0; i < allValues.size(); i++) {
//			TaTiersDTO civ = allValues.get(i);
//			if(query.equals("*") || civ.getCodeTiers().toLowerCase().contains(query.toLowerCase())
//					|| civ.getNomTiers().toLowerCase().contains(query.toLowerCase())) {
//				filteredValues.add(civ);
//			}
//		}
//
//		return filteredValues;
//	}
//	
//	public List<TaArticle> articleAutoComplete(String query) {
//		List<TaArticle> allValues = taArticleService.selectAll();
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
//	public List<TaArticleDTO> articleAutoCompleteDTOLight(String query) {
//		List<TaArticleDTO> allValues = taArticleService.findByCodeLight("*");
//		List<TaArticleDTO> filteredValues = new ArrayList<TaArticleDTO>();
//
//		for (int i = 0; i < allValues.size(); i++) {
//			TaArticleDTO civ = allValues.get(i);
//			if(query.equals("*") || civ.getCodeArticle().toLowerCase().contains(query.toLowerCase())) {
//				filteredValues.add(civ);
//			}
//		}
//
//		return filteredValues;
//	}
//	
//	public List<TaTPaiement> typePaiementAutoComplete(String query) {
//		List<TaTPaiement> allValues = taTPaiementService.selectAll();
//		List<TaTPaiement> filteredValues = new ArrayList<TaTPaiement>();
//		TaTPaiement cp = new TaTPaiement();
//		cp.setCodeTPaiement(Const.C_AUCUN);
//		filteredValues.add(cp);
//		for (int i = 0; i < allValues.size(); i++) {
//			cp = allValues.get(i);
//			if(query.equals("*") || cp.getCodeTPaiement().toLowerCase().contains(query.toLowerCase())) {
//				filteredValues.add(cp);
//			}
//		}
//
//		return filteredValues;
//	}
	
//	public List<TaTTvaDoc> typeTvaDocAutoComplete(String query) {
//		List<TaTTvaDoc> allValues = taTTvaDocService.selectAll();
//		List<TaTTvaDoc> filteredValues = new ArrayList<TaTTvaDoc>();
//		TaTTvaDoc cp = new TaTTvaDoc();
//		cp.setCodeTTvaDoc(Const.C_AUCUN);
//		filteredValues.add(cp);
//		for (int i = 0; i < allValues.size(); i++) {
//			cp = allValues.get(i);
//			if(query.equals("*") || cp.getCodeTTvaDoc().toLowerCase().contains(query.toLowerCase())) {
//				filteredValues.add(cp);
//			}
//		}
//
//		return filteredValues;
//	}
//	
//	public List<TaUnite> uniteAutoComplete(String query) {
//		List<TaUnite> allValues = taUniteService.selectAll();
//		List<TaUnite> filteredValues = new ArrayList<TaUnite>();
//		TaUnite civ=new TaUnite();
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
//	public List<TaEntrepot> entrepotAutoComplete(String query) {
//		List<TaEntrepot> allValues = taEntrepotService.selectAll();
//		List<TaEntrepot> filteredValues = new ArrayList<TaEntrepot>();
//
//		for (int i = 0; i < allValues.size(); i++) {
//			TaEntrepot civ = allValues.get(i);
//			if(query.equals("*") || civ.getCodeEntrepot().toLowerCase().contains(query.toLowerCase())) {
//				filteredValues.add(civ);
//			}
//		}
//
//		return filteredValues;
//	}
//
//	public List<String> emplacementAutoComplete(String query) {
//		List<String> filteredValues = new ArrayList<String>();
//		if(selectedLigneJSF!=null && selectedLigneJSF.getTaEntrepot()!=null) {
//			List<String> allValues = taEtatStockService.emplacementEntrepot(selectedLigneJSF.getTaEntrepot().getCodeEntrepot(),null);
//			
//			for (int i = 0; i < allValues.size(); i++) {
//				String civ = allValues.get(i);
//				if(civ !=null && (query.equals("*") || civ.toLowerCase().contains(query.toLowerCase()))) {
//					filteredValues.add(civ);
//				}
//			}
//		}
//		return filteredValues;
//	}
//	
//	public void actDialogControleLot(ActionEvent actionEvent) {
//		
////		PrimeFaces.current().dialog().openDynamic("aide");
//    
//        Map<String,Object> options = new HashMap<String, Object>();
//        options.put("modal", true);
//        options.put("draggable", false);
//        options.put("resizable", false);
//        options.put("contentHeight", 640);
//        options.put("modal", true);
//        
//        //Map<String,List<String>> params = null;
//        Map<String,List<String>> params = new HashMap<String,List<String>>();
//        List<String> list = new ArrayList<String>();
//        list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
//        params.put("modeEcranDefaut", list);
//        
//        PrimeFaces.current().dialog().openDynamic("solstyce/dialog_controle_article", options, params);
//		
////		FacesContext context = FacesContext.getCurrentInstance();  
////		context.addMessage(null, new FacesMessage("Tiers", "actAbout")); 	    
//	}
//	
//	public void handleReturnDialogControleLot(SelectEvent event) {
//		if(event!=null && event.getObject()!=null) {
//			//taTTiers = (TaTTiers) event.getObject();
//		}
//	}
//
//	public void setInsertAuto(boolean insertAuto) {
////		String oncomplete="jQuery('.myclass .ui-datatable-data tr').last().find('span.ui-icon-pencil').each(function(){jQuery(this).click()});";
////		if(insertAuto) {
////			rc.setOncomplete(oncomplete);
////			System.out.println("Ajoutera une nouvelle ligne automatiquement");
////		} else {
////			rc.setOncomplete(null);
////			System.out.println("N'ajoutera pas de nouvelle ligne automatiquement");
////		}
//		this.insertAuto = insertAuto;
//	}



	public void initialisationDesInfosComplementaires(Boolean recharger,String typeARecharger){
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
		
		try {
			if(((TaBonlivDTO)selectedDocumentDTO).getCodeDocument()!=null) {
				taInfosDocument = taInfosBonlivService.findByCodeBonliv(((TaBonlivDTO)selectedDocumentDTO).getCodeDocument());
			} else {
				taInfosDocument = new TaInfosBonliv();
			}
			
			if(taInfosDocument!=null && taInfosDocument.getNomTiers()!=null)selectedDocumentDTO.setNomTiers(taInfosDocument.getNomTiers());
			if(taInfosDocument!=null && taInfosDocument.getCodeTTvaDoc()!=null)selectedDocumentDTO.setCodeTTvaDoc(taInfosDocument.getCodeTTvaDoc());
			if(recharger) {				
				if(masterEntity.getTaTiers()!=null){
					masterEntity.setTaTiers(taTiersService.findById(masterEntity.getTaTiers().getIdTiers()));
					selectedDocumentDTO.setCodeTiers(masterEntity.getTaTiers().getCodeTiers());
					selectedDocumentDTO.setNomTiers(masterEntity.getTaTiers().getNomTiers());
					selectedDocumentDTO.setPrenomTiers(masterEntity.getTaTiers().getPrenomTiers());
					selectedDocumentDTO.setNomEntreprise("");
					if(masterEntity.getTaTiers().getTaEntreprise()!=null)
						selectedDocumentDTO.setNomEntreprise(masterEntity.getTaTiers().getTaEntreprise().getNomEntreprise());					
					
					if (masterEntity.getTaTiers().getTaTTvaDoc()!=null){
						selectedDocumentDTO.setCodeTTvaDoc(masterEntity.getTaTiers().getTaTTvaDoc().getCodeTTvaDoc());
					}
				}
			}
	
			if(typeARecharger==Const.RECHARGE_ADR_FACT||typeARecharger=="")
				initialisationDesAdrFact(recharger);
			if(typeARecharger==Const.RECHARGE_ADR_LIV||typeARecharger=="")
				initialisationDesAdrLiv(recharger);
			if(typeARecharger==Const.RECHARGE_C_PAIEMENT||typeARecharger=="")
				initialisationDesCPaiement(recharger);
			if(typeARecharger==Const.RECHARGE_INFOS_TIERS||typeARecharger=="")
				initialisationDesInfosTiers(recharger);
		}  catch (FinderException e) {
			e.printStackTrace();
		}
	}
	
//	public void initialisationDesAdrFact(Boolean recharger){
//		try {
//
//			LinkedList<AdresseInfosFacturationDTO> liste = new LinkedList<AdresseInfosFacturationDTO>();
//	
//			boolean leTiersADesAdresseFact = false;
//			if(masterEntity.getTaTiers()!=null){
//				if(typeAdresseFacturation!=null && taTAdrService.findByCode(typeAdresseFacturation)!=null) { //le type d'adresse existe bien
//					leTiersADesAdresseFact = masterEntity.getTaTiers().aDesAdressesDuType(typeAdresseFacturation); //le tiers a des adresse de ce type
//				}	
//			
//			if(leTiersADesAdresseFact) { 
//				//ajout des adresse de facturation au modele
//				for (TaAdresse taAdresse : masterEntity.getTaTiers().getTaAdresses()) {
//					if(taAdresse.getTaTAdr()!=null && taAdresse.getTaTAdr().getCodeTAdr().equals(typeAdresseFacturation)){
//						liste.add(mapperModelToUIAdresseInfosDocument.map(taAdresse, classModelAdresseFact));
//					}
//				}
//			}else{
//				
//			}
//			
//			//ajout des autres types d'adresse
//			for (TaAdresse taAdresse : masterEntity.getTaTiers().getTaAdresses()) {
//				liste.add(mapperModelToUIAdresseInfosDocument.map(taAdresse, classModelAdresseFact));
//			}
//			if(liste.isEmpty()) 
//				liste.add(mapperModelToUIAdresseInfosDocument.map(new TaAdresse(), classModelAdresseFact));
//			}
//			//ajout de l'adresse de livraison inscrite dans l'infos facture
//			if(taInfosDocument!=null) {
//				if(recharger )
//					liste.add(mapperModelToUIInfosDocVersAdresseFact.map(taInfosDocument, classModelAdresseFact));
//				else
//					liste.addFirst(mapperModelToUIInfosDocVersAdresseFact.map(taInfosDocument, classModelAdresseFact));
//			}
//			
//			if (!liste.isEmpty())
//				selectedAdresseFact.setIHMAdresse(liste.getFirst());
//			else
//				selectedAdresseFact.setIHMAdresse(new AdresseInfosFacturationDTO());
//			
//			System.out.println("BonlivController.initialisationDesAdrFact() ** "+selectedAdresseFact.getAdresse1Adresse());
//		
//		}catch (FinderException e) {
//			e.printStackTrace();
//		}
//				
//	}
//
//	public void initialisationDesAdrLiv(Boolean recharger){
//		try {
//		
//			LinkedList<AdresseInfosLivraisonDTO> liste = new LinkedList<AdresseInfosLivraisonDTO>();
//
//		boolean leTiersADesAdresseLiv = false;
//		if(masterEntity.getTaTiers()!=null){
//			if(typeAdresseLivraison!=null && taTAdrService.findByCode(typeAdresseLivraison)!=null) { //le type d'adresse existe bien
//				leTiersADesAdresseLiv = masterEntity.getTaTiers().aDesAdressesDuType(typeAdresseLivraison); //le tiers a des adresse de ce type
//			}
//		
//		
//		if(leTiersADesAdresseLiv) { 
//			//ajout des adresse de livraison au modele
//			for (TaAdresse taAdresse : masterEntity.getTaTiers().getTaAdresses()) {
//				if(taAdresse.getTaTAdr()!=null && taAdresse.getTaTAdr().getCodeTAdr().equals(typeAdresseLivraison)){
//					liste.add(mapperModelToUIAdresseLivInfosDocument.map(taAdresse, classModelAdresseLiv));
//				}
//			}
//		}
//		//ajout des autres types d'adresse
//		for (TaAdresse taAdresse : masterEntity.getTaTiers().getTaAdresses()) {
//			if(leTiersADesAdresseLiv && taAdresse.getTaTAdr()!=null && !taAdresse.getTaTAdr().getCodeTAdr().equals(typeAdresseLivraison)){
//				liste.add(mapperModelToUIAdresseLivInfosDocument.map(taAdresse, classModelAdresseLiv));
//			} else {
//				liste.add(mapperModelToUIAdresseLivInfosDocument.map(taAdresse, classModelAdresseLiv));
//			}
//		}
//		}
//		//ajout de l'adresse de livraison inscrite dans l'infos facture
//		if(taInfosDocument!=null) {
//			
//			if(recharger )
//				liste.add(mapperModelToUIInfosDocVersAdresseLiv.map(taInfosDocument, classModelAdresseLiv));
//			else
//				liste.addFirst(mapperModelToUIInfosDocVersAdresseLiv.map(taInfosDocument, classModelAdresseLiv));
//		}		
//		if (!liste.isEmpty())
//			((AdresseInfosLivraisonDTO) selectedAdresseLiv).setIHMAdresse(liste.getFirst());
//		else
//			((AdresseInfosLivraisonDTO) selectedAdresseLiv).setIHMAdresse(new AdresseInfosLivraisonDTO());
//		
//		}catch (FinderException e) {
//			e.printStackTrace();
//		}
//				
//	}
	
	public void initialisationDesCPaiement(Boolean recharger) {
		LinkedList<TaCPaiementDTO> liste = new LinkedList<TaCPaiementDTO>();
		try {

			TaInfosBonliv taInfosDocument = null;
			if (masterEntity != null) {
				if (masterEntity.getCodeDocument()!=null&& masterEntity.getCodeDocument() != "")
					taInfosDocument = taInfosBonlivService.findByCodeBonliv(masterEntity.getCodeDocument());
				else
					taInfosDocument = new TaInfosBonliv();


				taCPaiementDoc = null;
				List<TaCPaiement> listeCPaiement=taCPaiementService.rechercheParType(TaBonliv.TYPE_DOC);
				if(listeCPaiement!=null && !listeCPaiement.isEmpty())taCPaiementDoc=listeCPaiement.get(0);
				

				if (taTCPaiementService.findByCode(TaTCPaiement.C_CODE_TYPE_BON_LIVRAISON) != null
						&& taTCPaiementService.findByCode(TaTCPaiement.C_CODE_TYPE_BON_LIVRAISON).getTaCPaiement() != null) {
					taCPaiementDoc = taTCPaiementService.findByCode(TaTCPaiement.C_CODE_TYPE_BON_LIVRAISON).getTaCPaiement();
				}
				int report = 0;
				int finDeMois = 0;

				TaTPaiement taTPaiementReglement = new TaTPaiement();
//				if (masterEntity.reglementExiste()&& masterEntity.getTaRReglement().getTaReglement()
//						.getTaTPaiement() != null) {
//					taTPaiementReglement = masterEntity.getTaRReglement().getTaReglement().getTaTPaiement();
//				}

				if (taTPaiementReglement != null
						&& ((taTPaiementReglement.getReportTPaiement() != null && taTPaiementReglement.getReportTPaiement() != 0) || 
								(taTPaiementReglement.getFinMoisTPaiement() != null && taTPaiementReglement.getFinMoisTPaiement() != 0))) {
					TaCPaiementDTO ihm = new TaCPaiementDTO();
					ihm.setReportCPaiement(taTPaiementReglement.getReportTPaiement());
					ihm.setFinMoisCPaiement(taTPaiementReglement.getFinMoisTPaiement());
					liste.add(ihm);
				} else {


					if (masterEntity.getTaTiers() != null) {
						if (masterEntity.getTaTiers().getTaTPaiement() != null) {
							if (masterEntity.getTaTiers().getTaTPaiement().getReportTPaiement() != null)
								report = masterEntity.getTaTiers().getTaTPaiement().getReportTPaiement();
							if (masterEntity.getTaTiers().getTaTPaiement().getFinMoisTPaiement() != null)
								finDeMois = masterEntity.getTaTiers().getTaTPaiement().getFinMoisTPaiement();
						}

						if (masterEntity.getTaTiers().getTaCPaiement() == null
								|| (report != 0 || finDeMois != 0)) {
							if (taCPaiementDoc == null
									|| (report != 0 || finDeMois != 0)) {// alors on
								// met
								// au
								// moins
								// une
								// condition
								// vide
								TaCPaiementDTO ihm = new TaCPaiementDTO();
								ihm.setReportCPaiement(report);
								ihm.setFinMoisCPaiement(finDeMois);
								liste.add(ihm);
							}
						} else
							liste.add(
									mapperModelToUICPaiementInfosDocument.map(
											masterEntity.getTaTiers()
											.getTaCPaiement(),
											classModelCPaiement));
					}
				}

					if (taCPaiementDoc != null  )
						liste.add(mapperModelToUICPaiementInfosDocument.map(taCPaiementDoc,classModelCPaiement));
					masterEntity.setTaInfosDocument(taInfosDocument);
					// ajout de l'adresse de livraison inscrite dans l'infos facture
					if (taInfosDocument != null) {
						if (recharger)
							liste.add(
									mapperModelToUIInfosDocVersCPaiement.map(masterEntity.getTaInfosDocument(),classModelCPaiement));
						else
							liste.addFirst(
									mapperModelToUIInfosDocVersCPaiement.map(masterEntity.getTaInfosDocument(),classModelCPaiement));
					}
				}
				if (!liste.isEmpty()) {
					((TaCPaiementDTO) selectedCPaiement)
					.setSWTCPaiement(liste.getFirst());
				} else {
					((TaCPaiementDTO) selectedCPaiement)
					.setSWTCPaiement(new TaCPaiementDTO());
				}
			
			}catch(Exception e) {
				e.printStackTrace();
			}
		}

	public void calculDateEcheance() {
		calculDateEcheance(false);
	}
	public void calculDateEcheance(Boolean appliquer) {
		if(selectedDocumentDTO!=null){
		if(((TaBonlivDTO)selectedDocumentDTO).getId()==0|| appliquer) {

			Integer report = null;
			Integer finDeMois = null;
			if(((TaCPaiementDTO)selectedCPaiement)!=null /*&& ((TaCPaiementDTO)selectedCPaiement).getCodeCPaiement()!=null*/) { 
				if(((TaCPaiementDTO)selectedCPaiement).getReportCPaiement()!=null)
					report = ((TaCPaiementDTO)selectedCPaiement).getReportCPaiement();
				if(((TaCPaiementDTO)selectedCPaiement).getFinMoisCPaiement()!=null)
					finDeMois = ((TaCPaiementDTO)selectedCPaiement).getFinMoisCPaiement();
			}
			masterEntity.setDateDocument(selectedDocumentDTO.getDateDocument());
			((TaBonlivDTO)selectedDocumentDTO).setDateEchDocument(taBonLivraisonService.calculDateEcheance(masterEntity,report, finDeMois,taTPaiement));
		}
		}
	}
	
//	public void initialisationDesInfosComplementaires(){
//		initialisationDesInfosComplementaires(false,"");
//	}
	

//	public void actReinitAdrFact() throws Exception {
//		initialisationDesInfosComplementaires(true,Const.RECHARGE_ADR_FACT);
//		actModifier();
//	}
//	
//	public void actReinitAdrLiv() throws Exception {
//		initialisationDesInfosComplementaires(true,Const.RECHARGE_ADR_LIV);
//		actModifier();
//	}
	
//	public void actReinitCPaiement() throws Exception {
//		initialisationDesInfosComplementaires(true,Const.RECHARGE_C_PAIEMENT);
//		calculDateEcheance(true);
//		actModifier();
//	}
	
	public void actAppliquerCPaiement() throws Exception {
		calculDateEcheance(true);
		//taBonLivraisonService.mettreAJourDateEcheanceReglements(masterEntity);
		actModifier();
	}
	
//	public void actReinitInfosTiers() throws Exception {
//		initialisationDesInfosComplementaires(true,Const.RECHARGE_INFOS_TIERS);
//		actModifier();
//	}
//	
//	public void modifMode(EnumModeObjet mode){
//		if (!VerrouInterface.isVerrouille() ){
//			try {
//				if(!modeEcran.dataSetEnModif()) {
//					if(mode.equals(EnumModeObjet.C_MO_EDITION)) {
//						actModifier();
//					} 
//					if(mode.equals(EnumModeObjet.C_MO_INSERTION)) {
//						actInserer(null);
//					} 					
//				}
//			} catch (Exception e1) {
//				e1.printStackTrace();  
//			}
//		}
//	}
	
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

	@Override
	public void calculTypePaiement(boolean recharger) {
		// TODO Auto-generated method stub
		
	}

	public OuvertureTiersBean getOuvertureTiersBean() {
		return ouvertureTiersBean;
	}

	public void setOuvertureTiersBean(OuvertureTiersBean ouvertureTiersBean) {
		this.ouvertureTiersBean = ouvertureTiersBean;
	}

	public OuvertureArticleBean getOuvertureArticleBean() {
		return ouvertureArticleBean;
	}

	public void setOuvertureArticleBean(OuvertureArticleBean ouvertureArticleBean) {
		this.ouvertureArticleBean = ouvertureArticleBean;
	}
	

	public void onClearArticle(AjaxBehaviorEvent event){
		super.onClearArticle(event);
		selectedLigneJSF.setTaLot(null);
		selectedLigneJSF.getDto().setNumLot(null);
		if(selectedLigneJSF.getArticleLotEntrepotDTO()!=null)selectedLigneJSF.getArticleLotEntrepotDTO().setNumLot(null);
		
		masterEntityLigne.calculMontant();
		masterEntity.calculeTvaEtTotaux();
		validateUIField(Const.C_CODE_ARTICLE, null);
	}

	
	public void updateTab(){

		try {	
			super.updateTab();
			autorisationLiaisonComplete(true);
			selectedEtatDocument=null;
			if(masterEntity!=null && masterEntity.getTaREtat()!=null)
				selectedEtatDocument=masterEntity.getTaREtat().getTaEtat();
			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Livraison", 
						"Chargement du Bon de livraison N°"+selectedDocumentDTO.getCodeTiers()
						)); 
			}
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void actSupprimerLigne(ActionEvent actionEvent) {
		try {
			if(autorisationLiaisonComplete(false)) {
				if(autorisationBean.autoriseMenu(IModulesProgramme.ID_MODULE_ABONNEMENT)) {
					  if(selectedLigneJSF.getDto().getIdLDocument() != null) {
//							//List<TaLEcheance> liste  = taLEcheanceService.findAllByIdLFacture(selectedLigneJSF.getDto().getIdLDocument());
//							List<TaLigneALigneEcheance> liste  = taLigneALigneEcheanceService.findAllByIdLDocumentAndTypeDoc(selectedLigneJSF.getDto().getIdLDocument(), TaBonliv.TYPE_DOC);
//							if(liste != null && !liste.isEmpty()) {
//								for (TaLigneALigneEcheance li : liste) {
//									if(!listeLigneALigneEcheanceASupprimer.contains(li)) {
//										listeLigneALigneEcheanceASupprimer.add(li);
//									}
//									
//								}
//
//							}
					  }
					}
				super.actSupprimerLigne(actionEvent);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void onClearTaTitreTransport(AjaxBehaviorEvent event){
		selectedLigneJSF.getDto().setCodeTitreTransport(null);
		selectedLigneJSF.getDto().setQteTitreTransport(null);
		selectedLigneJSF.setTaTitreTransport(null);
		masterEntityLigne.setCodeTitreTransport(null);
		masterEntityLigne.setQteTitreTransport(null);
	}

}
