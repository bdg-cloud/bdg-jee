package fr.legrain.bdg.webapp.articles;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
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
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.apache.commons.beanutils.PropertyUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.tabview.TabView;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import fr.legrain.article.dto.ArticleLotEntrepotDTO;
import fr.legrain.article.dto.TaArticleDTO;
import fr.legrain.article.dto.TaFamilleDTO;
import fr.legrain.article.dto.TaMarqueArticleDTO;
import fr.legrain.article.dto.TaPrixDTO;
import fr.legrain.article.dto.TaRapportUniteDTO;
import fr.legrain.article.dto.TaTTvaDTO;
import fr.legrain.article.dto.TaTvaDTO;
import fr.legrain.article.dto.TaUniteDTO;
import fr.legrain.article.model.TaArticle;
import fr.legrain.article.model.TaArticleCompose;
import fr.legrain.article.model.TaCatalogueWeb;
import fr.legrain.article.model.TaFamille;
import fr.legrain.article.model.TaLRecette;
import fr.legrain.article.model.TaMarqueArticle;
import fr.legrain.article.model.TaRTaTitreTransport;
import fr.legrain.article.model.TaRapportUnite;
import fr.legrain.article.model.TaRecette;
import fr.legrain.article.model.TaTTva;
import fr.legrain.article.model.TaTva;
import fr.legrain.article.model.TaUnite;
import fr.legrain.article.titretransport.dto.TaTitreTransportDTO;
import fr.legrain.article.titretransport.model.TaTitreTransport;
import fr.legrain.bdg.article.service.remote.ITaArticleComposeServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaCatalogueWebServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaEtatStockServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaFamilleServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaMarqueArticleServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaMouvementStockServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaParamEan128ServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaParamEanServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaRTitreTransportServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaTTvaServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaTvaServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaUniteServiceRemote;
import fr.legrain.bdg.article.titretransport.service.remote.ITaTitreTransportServiceRemote;
import fr.legrain.bdg.conformite.service.remote.ITaConformiteServiceRemote;
import fr.legrain.bdg.controle.service.remote.ITaGenCodeExServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaFactureServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaPreferencesServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.preferences.divers.ConstPreferencesArticles;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.bdg.webapp.ConstWeb;
import fr.legrain.bdg.webapp.SessionListener;
import fr.legrain.bdg.webapp.app.AbstractController;
import fr.legrain.bdg.webapp.app.AutorisationBean;
import fr.legrain.bdg.webapp.app.EtiquetteCodeBarreBean;
import fr.legrain.bdg.webapp.app.LgrTab;
import fr.legrain.bdg.webapp.app.TabListModelBean;
import fr.legrain.bdg.webapp.app.TabViewsBean;
import fr.legrain.bdg.webapp.codebarre.CodeBarreParam;
import fr.legrain.bdg.webapp.codebarre.GenerationCodeBarreController;
import fr.legrain.bdg.webapp.dashboard.DashBoardParArticleController;
import fr.legrain.bdg.webapp.solstyce.ListeConformiteController;
import fr.legrain.bdg.webapp.solstyce.TaLRecetteDTOJSF;
import fr.legrain.conformite.model.TaBareme;
import fr.legrain.conformite.model.TaConformite;
import fr.legrain.document.dto.DocumentChiffreAffaireDTO;
import fr.legrain.document.dto.TaLRecetteDTO;
import fr.legrain.dossier.model.TaInfoEntreprise;
import fr.legrain.dossier.model.TaPreferences;
import fr.legrain.droits.model.IModulesProgramme;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.LibChaine;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.ModeObjetEcranServeur;
import fr.legrain.tiers.dto.TaTiersDTO;
import fr.legrain.tiers.model.TaTiers;

@Named("articleController")
@ViewScoped  
public class ArticleController extends AbstractController implements Serializable {  
	
	@Inject @Named(value="tabListModelCenterBean")
	private TabListModelBean tabsCenter;
	
	@Inject @Named(value="tabViewsBean")
	private TabViewsBean tabViews;
	
	@Inject @Named(value="etiquetteCodeBarreBean")
	private EtiquetteCodeBarreBean etiquetteCodeBarreBean;
	
	@Inject @Named(value="listeConformiteController")
	private ListeConformiteController listeConformiteController;
	
	@Inject @Named(value="tarifArticlesController")
	private TarifArticlesController tarifArticlesController;
	
	@Inject @Named(value="refArticleFournisseurController")
	private RefArticleFournisseurController refArticleFournisseurController;
	
	@Inject @Named(value="abonnementArticleController")
	private AbonnementArticleController abonnementArticleController;
	
	@Inject @Named(value="nomenclatureArticleController")
	private NomenclatureArticleController nomenclatureArticleController;
	
	@Inject @Named(value="imageArticleController")
	private ImageArticleController imageArticleController;
	
	@Inject @Named(value="catalogueWebArticleController")
	private CatalogueWebArticleController catalogueWebArticleController;
	
	@Inject protected AutorisationBean autorisationBean;	
	@Inject private DashBoardParArticleController dashBoardParArticleController;
	
	private boolean insertAutoMPRecette = true;
	protected boolean gestionCapsules = true;

	private static char groupSeperator = (char)29;
	
	private List<TaArticleDTO> values; 
	boolean exportComptaActivee;
	
	private List<TaLRecetteDTOJSF> valuesMPRecette; 
	private TaLRecetteDTOJSF selectedTaMPRecette;
	private TaLRecette taLRecette;
	protected ModeObjetEcranServeur modeEcranRecette = new ModeObjetEcranServeur();
	
	protected ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();
	
	private @EJB ITaArticleServiceRemote taArticleService;
	private @EJB ITaFactureServiceRemote taFactureService;
	private @EJB ITaInfoEntrepriseServiceRemote taInfoEntrepriseService;
	private @EJB ITaArticleComposeServiceRemote taArticleComposeService;
	private @EJB ITaCatalogueWebServiceRemote taCatalogueWebService;

	private @EJB ITaFamilleServiceRemote taFamilleService;	
	private @EJB ITaTvaServiceRemote taTvaService;
	private @EJB ITaTTvaServiceRemote taTTvaService;
	private @EJB ITaUniteServiceRemote taUniteService;
	private @EJB ITaRTitreTransportServiceRemote taRTitreTransportService;
	private @EJB ITaTitreTransportServiceRemote taTitreTransportService;
	private @EJB ITaMarqueArticleServiceRemote taMarqueArticleService;
	private @EJB ITaTiersServiceRemote taFournisseurService;
	private @EJB ITaGenCodeExServiceRemote taGenCodeExService;
	private @EJB ITaParamEanServiceRemote taParamEanService;
	private @EJB ITaParamEan128ServiceRemote taParamEan128Service;
	private @EJB ITaPreferencesServiceRemote taPreferencesService;
	private @EJB ITaEtatStockServiceRemote taEtatStockService;
	private @EJB ITaMouvementStockServiceRemote taMouvementStockService;
	private @EJB ITaConformiteServiceRemote taConformiteService;
	
	

	private TaInfoEntreprise infoEntreprise;
	private TaArticleDTO[] selectedTaArticleDTOs; 
    private TaArticleDTO newTaArticleDTO = new TaArticleDTO();
    private TaArticle taArticle = new TaArticle();
    private TaArticleDTO selectedTaArticleDTO = new TaArticleDTO();
//    private TaArticleDTO OldSelectedTaArticleDTO = new TaArticleDTO();
    private TaArticle articleDuplique;
	private TaUnite taUnite1;
	private TaUnite taUnite2;
	private TaUnite taUniteReference;
	private TaUnite taUniteSaisie;
	private TaTitreTransport taTitreTransport;
	private TaRTaTitreTransport taRTaTitreTransport;
	private TaFamille taFamille;
	private List<TaFamille> taFamilles;
	private List<String> listeTaFamille = new ArrayList<String>();
	private List<TaTiers> taFournisseurs;
	private TaTiers taFournisseur;
	private TaUnite taUniteStock;
	private TaTva taTva;
	private TaTTva taTTva;
	private TaMarqueArticle taMarqueArticle;
	
	private Boolean afficheMatierePremiere=false;
	private Boolean afficheProduitFini=false;
	private Boolean afficheLesDeux=false;
	private Boolean afficheUnOuAutre=false;
	private Boolean afficheTous=true;
	private Boolean afficheInactif=false;
	private Boolean afficheSeulementBoutique=false;
	private Boolean parametrageDepuisMenuPrincipal=false;
	private String affiche=C_TOUS;
	
	private Boolean dejaDansNomenclature = false;
	
	private String urlPourEdition;
	
	private String paramId;
	
	private TabView tabViewArticle; //Le Binding sur les onglets "secondaire" semble ne pas fonctionné, les onglets ne sont pas générés au bon moment avec le c:forEach
    
    private LgrDozerMapper<TaArticleDTO,TaArticle> mapperUIToModel  = new LgrDozerMapper<TaArticleDTO,TaArticle>();
	private LgrDozerMapper<TaArticle,TaArticleDTO> mapperModelToUI  = new LgrDozerMapper<TaArticle,TaArticleDTO>();
	private LgrDozerMapper<TaFamille,TaFamilleDTO> mapperModelToUIFamille  = new LgrDozerMapper<TaFamille,TaFamilleDTO>();
	private LgrDozerMapper<TaTiers,TaTiersDTO> mapperModelToUIFournisseur  = new LgrDozerMapper<TaTiers,TaTiersDTO>();
	private LgrDozerMapper<TaUnite,TaUniteDTO> mapperModelToUIUnite  = new LgrDozerMapper<TaUnite,TaUniteDTO>();
	private LgrDozerMapper<TaTva,TaTvaDTO> mapperModelToUITva  = new LgrDozerMapper<TaTva,TaTvaDTO>();
	private LgrDozerMapper<TaTTva,TaTTvaDTO> mapperModelToUITTva  = new LgrDozerMapper<TaTTva,TaTTvaDTO>();
	private LgrDozerMapper<TaMarqueArticle,TaMarqueArticleDTO> mapperModelToUIMarque  = new LgrDozerMapper<TaMarqueArticle,TaMarqueArticleDTO>();
	private LgrDozerMapper<TaTitreTransport,TaTitreTransportDTO> mapperModelToUITitreTransport  = new LgrDozerMapper<TaTitreTransport,TaTitreTransportDTO>();
	
	
	/////TABVIEW CONST WIDGET VAR
	private final String WV_TABVIEW_ARTICLE = "wvtabViewArticle";
	/////TAB CONST CSS CLASS
	private final String CLASS_CSS_TAB_FICHE_ARTICLE = "tab-fiche-article";
	private final String CLASS_CSS_TAB_DASH_ARTICLE = "tab-dash-article";
	
	// Dima - Début
	private TaFamilleDTO taFamilleDTO;
//	private List<TaFamilleDTO> taFamilleDTOs;
	private TaTvaDTO taTvaDTO;
	private TaTTvaDTO taTTvaDTO;
	private TaUniteDTO taUnite1DTO;
	private TaUniteDTO taUnite2DTO;
	private TaUniteDTO taUniteReferenceDTO;
	private TaUniteDTO taUniteSaisieDTO;
	private TaUniteDTO taUniteStockDTO;
	private TaTitreTransportDTO taTitreTransportDTO;
	private TaMarqueArticleDTO taMarqueArticleDTO;
	private TaTiersDTO taFournisseurDTO;
//	private List<TaTiersDTO> taFournisseurDTOs;
	private BigDecimal montantCaHt = BigDecimal.ZERO;
	private Date dateDebut;
	private Date dateFin;
    private List<DocumentChiffreAffaireDTO> listeCaPeriodeFactureArticle;
    
	private BigDecimal quantiteTotaleEnStock = BigDecimal.ZERO;
	private String quantiteTotaleEnStockUnite = "";
	private String cssQuantiteTotaleEnStock = "";

	
	private static final String C_TOUS="Tous";
	private static final String C_MATIERE_PREMIERE="Matière P.";
	private static final String C_PRODUIT_FINI="Produit F.";
	private static final String C_PF_MP="Les deux.";
	private static final String C_PF_OU_MP="L'un ou l'autre.";
	
	private StreamedContent exportation;
	private boolean affiche_filtre_grille=true;
	private boolean affiche_unite_saisie=false;
	private ParamDuplicationArticle param ;
	
	public TaFamilleDTO getTaFamilleDTO() {
		return taFamilleDTO;
	}

	public void setTaFamilleDTO(TaFamilleDTO taFamilleDTO) {
		this.taFamilleDTO = taFamilleDTO;
	}
	
	public TaUniteDTO getTaUnite1DTO() {
		return taUnite1DTO;
	}

	public void setTaUnite1DTO(TaUniteDTO taUnite1DTO) {
		this.taUnite1DTO = taUnite1DTO;
	}

	public TaUniteDTO getTaUnite2DTO() {
		return taUnite2DTO;
	}

	public void setTaUnite2DTO(TaUniteDTO taUnite2DTO) {
		this.taUnite2DTO = taUnite2DTO;
	}
	// Dima -  Fin
	
	public ArticleController() {  
		//values = getValues();
	}  
	
	@PostConstruct
	public void postConstruct(){
		TaPreferences pref= taPreferencesService.findByGoupeAndCle("liste_article", "affichage_filtre_grille");
		exportComptaActivee= taPreferencesService.retourPreferenceBoolean("exportation", "export_compta");
		if( pref!=null) affiche_filtre_grille=LibConversion.StringToBoolean(pref.getValeur());
		
		TaPreferences us = taPreferencesService.findByGoupeAndCle("articles", "utilisation_unite_saisie");
		if(us!=null)affiche_unite_saisie=LibConversion.StringToBoolean(us.getValeur());

		
		gestionCapsules = autorisationBean.isModeGestionCapsules();
		rechercheAvecCommentaire(true);
		refresh();
				
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
		String path = request.getRequestURI().substring(request.getContextPath().length());
		urlPourEdition = request.getRequestURL().substring(0,request.getRequestURL().length()-path.length());

	}

	
	public void refreshResponsive(){
		if(paramId!=null) {
			try {
				if(paramId.equals("-1")){
					actInserer(null);
				}else{
				taArticle = taArticleService.findById(LibConversion.stringToInteger(paramId));
				mapperModelToUI.map(taArticle, selectedTaArticleDTO);
				autoCompleteMapDTOtoUI();
				modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
				valuesMPRecette = IHMmodelRecette();
				}
				paramId=null;
				
			} catch (FinderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("ArticleController.refresh() "+taArticle.getCodeArticle());
		}
		
	}
	public void actCompose() {
		List<TaArticleCompose> liste = taArticleComposeService.findAllByIdArticleEnfant(selectedTaArticleDTO.getId());
		if(liste == null || liste.isEmpty()) {
			if(selectedTaArticleDTO.getCompose() != true) {
				selectedTaArticleDTO.getNomenclature().clear();
			}
			taArticle = taArticleService.setNomenclature(taArticle);
		}else {
			selectedTaArticleDTO.setCompose(false);
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nomenclature", "Vous ne pouvez pas définir cet article comme ayant une nomenclature car il fait lui-même parti de la nomenclature d'un autre article."));
		}
		
	}
	
	public void switchAfficheSeulementBoutique() {
        refresh();
    }
	
	public void refresh(){
//		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
//		Map<String, Object> sessionMap = externalContext.getSessionMap();
//		if(sessionMap.get("liste_article_boutique")!=null && (Boolean)sessionMap.get("liste_article_boutique")) {
//			afficheSeulementBoutique = true;
//		}
		
		List<TaArticleDTO> temp;
		if (afficheSeulementBoutique) {
			temp = taCatalogueWebService.findListeArticleCatalogue();
		}
		else if(getAfficheTous() || affiche_filtre_grille) {
			temp = taArticleService.findAllLight2();
		} else {
			temp=taArticleService.findByMPremierePFiniDTO(getAfficheMatierePremiere(),getAfficheProduitFini(),getAfficheLesDeux(),getAfficheUnOuAutre());
		}
		if(values==null)values=new ArrayList<TaArticleDTO>();else values.clear();


		for (TaArticleDTO l : temp) {
			if(!affiche_filtre_grille) {
				if(!afficheInactif){
					if(l.getActif()!=null && l.getActif())
						values.add(l);
				}else values.add(l);
			}else values.add(l);
		}
		

		
		listeTaFamille=familleArticle();
		
		valuesMPRecette = IHMmodelRecette();

		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

		infoEntreprise = taInfoEntrepriseService.findInstance();
    	dateDebut = infoEntreprise.getDatedebInfoEntreprise();
    	dateFin = infoEntreprise.getDatefinInfoEntreprise();
		

    	if(parametrageDepuisMenuPrincipal) {
    		parametrageDepuisMenuPrincipal = false;
    		PrimeFaces.current().ajax().update("@widgetVar(wvOverlayListeArticle)");
    	}
	}
	
	public void updateTabs() {
		try {	
			valuesMPRecette.clear();
			modeEcranRecette.setMode(EnumModeObjet.C_MO_CONSULTATION);
			
			if(selectedTaArticleDTOs!=null && selectedTaArticleDTOs.length>0) {
				selectedTaArticleDTO = selectedTaArticleDTOs[0];
			}
			if(selectedTaArticleDTO.getId()!=null && selectedTaArticleDTO.getId()!=0) {
				taArticle = taArticleService.findByIdAvecLazy(selectedTaArticleDTO.getId(),gestionCapsules);
			}
			
			
			taArticle = taArticleService.setNomenclature(taArticle);
			
			if(autorisationBean.autoriseMenu(IModulesProgramme.ID_MODULE_ARTICLE_COMPOSE)) {
				List<TaArticleCompose> liste = taArticleComposeService.findAllByIdArticleEnfant(taArticle.getIdArticle());
				if(liste != null && !liste.isEmpty()) {
					dejaDansNomenclature = true;
				}else {
					dejaDansNomenclature = false;
				}
			}
			
			if(autorisationBean.autoriseMenu(IModulesProgramme.ID_MODULE_SOLSTYCE)) {
				listeConformiteController.setMasterEntity(taArticle);
				listeConformiteController.refresh();
				valuesMPRecette = IHMmodelRecette();
				
				if(valuesMPRecette.size()>0) {
					selectionLigneMP(valuesMPRecette.get(0));
				}
			}
			
			if(taArticle!=null){
				mapperModelToUI.map(taArticle, selectedTaArticleDTO);
			}
			
			tarifArticlesController.setMasterEntity(taArticle);
			tarifArticlesController.refresh(null);
			
			refArticleFournisseurController.setMasterEntity(taArticle);
			refArticleFournisseurController.refresh(null);
			
			if(autorisationBean.autoriseMenu(IModulesProgramme.ID_MODULE_ABONNEMENT)) {
				abonnementArticleController.setMasterEntity(taArticle);
				abonnementArticleController.refresh(null);
			}
					
			if(autorisationBean.autoriseMenu(IModulesProgramme.ID_MODULE_ARTICLE_COMPOSE)) {
				nomenclatureArticleController.setMasterEntity(taArticle);
				nomenclatureArticleController.refresh(null);
			}
			
			imageArticleController.setMasterEntity(taArticle);
			imageArticleController.refresh(null);
			
			catalogueWebArticleController.setMasterEntity(taArticle);
			catalogueWebArticleController.refresh(null);
			

			autoCompleteMapDTOtoUI();
			if(selectedTaArticleDTO.getCodeArticle()!=null) {
				dashBoardParArticleController.setCodeArticleCourant(selectedTaArticleDTO.getCodeArticle()); 
				
				if(!taPreferencesService.grosFichierArticle()) {
					dashBoardParArticleController.refresh(null);
				}
				
			}

			
			montantCaHt = BigDecimal.ZERO;
			if(!taPreferencesService.grosFichierArticle()) {
				if(selectedTaArticleDTO.getCodeArticle()!=null) {
					//				listeCaPeriodeFactureArticle = taArticleService.findChiffreAffaireTotalParCodeArticleDTO(dateDebut, dateFin, selectedTaArticleDTO.getCodeArticle());
					//				listeCaPeriodeFactureArticle = taFactureService.sumChiffreAffaireTotalDTOArticle(dateDebut, dateFin, selectedTaArticleDTO.getCodeArticle());
					listeCaPeriodeFactureArticle = taFactureService.sumChiffreAffaireTotalDTOArticleMoinsAvoir(dateDebut, dateFin, selectedTaArticleDTO.getCodeArticle());
					if (listeCaPeriodeFactureArticle != null && ! listeCaPeriodeFactureArticle.isEmpty()) {
						montantCaHt = listeCaPeriodeFactureArticle.get(0).getMtHtCalc();
						if (montantCaHt == null) {
							montantCaHt = BigDecimal.ZERO;
						}
					}
				}
			}
			
			quantiteTotaleEnStock  = BigDecimal.ZERO;
			quantiteTotaleEnStockUnite = "";
			cssQuantiteTotaleEnStock = "";
			
			if(!taPreferencesService.grosFichierArticle()) {
				if(taArticle.getCodeArticle()!=null) {
					List<ArticleLotEntrepotDTO> l = taMouvementStockService.getQuantiteTotaleEnStockArticle(taArticle.getCodeArticle(),null);
					if(l!=null && !l.isEmpty()) {
						quantiteTotaleEnStock = l.get(0).getQte1();
						quantiteTotaleEnStockUnite = l.get(0).getCodeUnite();

						if(quantiteTotaleEnStock.compareTo(taArticle.getStockMinArticle())<=0) {
							cssQuantiteTotaleEnStock = "fiche-art-stock-total-inferieur-min ";
						}
						if(quantiteTotaleEnStock.compareTo(BigDecimal.ZERO)<=0) {
							cssQuantiteTotaleEnStock = "fiche-art-stock-total-negatif";
						}

					}
				}
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void onTabShow() {
		System.out.println("ArticleController.onTabShow()");
	}
	
	public void onTabChange(TabChangeEvent event) {
//		FacesMessage msg = null;
//		if(event.getTab()!=null) {
//			msg = new FacesMessage("Tab Changed ArticleController", "Active Tab: " + event.getTab().getTitle());
//		} else {
//			msg = new FacesMessage("Tab Changed ArticleController", "Active Tab: ");
//		}
//        FacesContext.getCurrentInstance().addMessage(null, msg);
		
		/*
		 * Si trop lent voir au cas par cas pour :
		 * 	- soit mettre à jour uniquement l'onglet qui va s'afficher, 
		 *  - soit mettre à jour les propriété du masterEntity en fonction de ce qui vient d'être fait sur l'onlget précendent
		 * Par exemple mettre à jour la liste des controle dans l'article courant au lieu de recharger l'article entièrement
		 * 
		 * Sinon chercher ce que l'on peu faire avec un refresh JPA
		 */
		updateTabs(); 
	}
	
	public List<TaArticleDTO> getValues(){  
		return values;
	}
	
	public boolean editable() {
		return !modeEcran.dataSetEnModif();
	}
	
	public boolean articleEstUtilise() {
		return taArticleService.articleEstUtilise(taArticle);
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
				break;
			case "supprimerListe":retour = false;break;	
			case "supprimer":
			case "modifier":
			case "imprimer":
				if(selectedTaArticleDTO!=null && selectedTaArticleDTO.getId()!=null  && selectedTaArticleDTO.getId()!=0 ) retour = false;
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
	
	public boolean etatBoutonRecette(String bouton) {
		boolean retour = true;
		switch (modeEcranRecette.getMode()) {
		case C_MO_INSERTION:
			switch (bouton) {
			case "enregistrerRecette":
			case "annulerRecette":				
				retour = false;
				break;
			default:
				break;
			}
			break;
		case C_MO_EDITION:
			switch (bouton) {
			case "enregistrerRecette":
			case "annulerRecette":				
				retour = false;
				break;
			default:
				break;
			}
			break;
		case C_MO_CONSULTATION:
			switch (bouton) {
			case "insererRecette":
				retour = false;
				break;
			case "nouvelleFab":	
				if(valuesMPRecette!=null && valuesMPRecette.size()>0)retour = false;
				break;
			case "supprimerRecette":
				if(selectedTaMPRecette!=null && selectedTaMPRecette.getDto()!=null  && selectedTaMPRecette.getDto().getIdLDocument()!=null  
				&& selectedTaMPRecette.getDto().getIdLDocument()!=0 ) retour = false;
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
	
	public boolean disabledTab(String nomTab) {
		return modeEcran.dataSetEnModif() ||  selectedTaArticleDTO.getId()==0;
	}
	
	public void actImprimerEtiquetteCB(ActionEvent actionEvent) {
		
		etiquetteCodeBarreBean.videListe();
		try {
			etiquetteCodeBarreBean.getListeArticle().add(taArticleService.findById(selectedTaArticleDTO.getId()));
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
		if(ConstWeb.DEBUG) {
	   	FacesContext context = FacesContext.getCurrentInstance();  
	    context.addMessage(null, new FacesMessage("Articles", "actImprimerEtiquetteCB"));
		}
	}
	
	public void actImprimerEtiquetteCB128(ActionEvent actionEvent) {
		
		CodeBarreParam param = new CodeBarreParam();
		param.setTaArticle(taArticle);
//		param.setTaLot(taLot);
		GenerationCodeBarreController.actDialoguePaiementEcheanceParCarte(param);
		
		
	}
	 
	public void actAnnuler(ActionEvent actionEvent) {
		try {
			switch (modeEcran.getMode()) {
			case C_MO_INSERTION:
				if(selectedTaArticleDTO.getCodeArticle()!=null) {
					taArticleService.annuleCode(selectedTaArticleDTO.getCodeArticle());
					
				}
				
				taArticle = new TaArticle();
//				mapperModelToUI.map(taArticle,selectedTaArticleDTO);  // Ne sert a rien et provoque  la perte de l'id de  selectedTaArticleDTO et donc bloque les onglets
//				selectedTaArticleDTO = new TaArticleDTO(); // ne sert a rien ici non plus
				selectedTaArticleDTO = values.get(0);
				onRowSelect(null);
				
				break;
			case C_MO_EDITION:
				if(selectedTaArticleDTO.getId()!=null && selectedTaArticleDTO.getId()!=0){
					taArticle = taArticleService.findById(selectedTaArticleDTO.getId());
					}				
				break;

			default:
				break;
			}			
				
			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
			mapperModelToUI.map(taArticle,selectedTaArticleDTO );
		
		if(ConstWeb.DEBUG) {
		   	FacesContext context = FacesContext.getCurrentInstance();  
		    context.addMessage(null, new FacesMessage("Articles", "actAnnuler")); 
		}
	    
	}
	catch (Exception e) {
		selectedTaArticleDTO=new TaArticleDTO();
		taArticle=new TaArticle();
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
		mapperModelToUI.map(taArticle,selectedTaArticleDTO );		
		// TODO Auto-generated catch block
		e.printStackTrace();
	}		
}
	
	public void autoCompleteMapUIToDTO() {

//		if(taFamille!=null) {
//			validateUIField(Const.C_CODE_FAMILLE,taFamille.getCodeFamille());
//			selectedTaArticleDTO.setCodeFamille(taFamille.getCodeFamille());
//		}
//		if(taUnite1!=null) {
//			validateUIField(Const.C_CODE_UNITE,taUnite1.getCodeUnite());
//			selectedTaArticleDTO.setCodeUnite(taUnite1.getCodeUnite());
//		}
//		if(taUnite2!=null) {
//			validateUIField(Const.C_CODE_UNITE,taUnite2.getCodeUnite());
//			selectedTaArticleDTO.setCodeUnite2(taUnite2.getCodeUnite());
//		}

		if(taFamilleDTO!=null) {
			validateUIField(Const.C_CODE_FAMILLE,taFamilleDTO.getCodeFamille());
			selectedTaArticleDTO.setCodeFamille(taFamilleDTO.getCodeFamille());
		}
//		if(taFamilleDTOs!=null) {
//			for (TaFamilleDTO f : taFamilleDTOs) {
//				validateUIField(Const.C_CODE_FAMILLE,f.getCodeFamille()); //???
//				//selectedTaArticleDTO.setCodeFamille(f.getCodeFamille());
//				if(selectedTaArticleDTO.getTaFamilleDTOs()==null) {
//					selectedTaArticleDTO.setTaFamilleDTOs(new ArrayList<>());
//				}
//				selectedTaArticleDTO.getTaFamilleDTOs().add(f);
//			}
//		}		

//		if(taFournisseurDTOs!=null) {
//			for (TaTiersDTO f : taFournisseurDTOs) {
//				validateUIField(Const.C_CODE_TIERS,f.getCodeTiers()); //???
//				if(selectedTaArticleDTO.getTaFournisseurDTOs()==null) {
//					selectedTaArticleDTO.setTaFournisseurDTOs(new ArrayList<>());
//				}
//				selectedTaArticleDTO.getTaFournisseurDTOs().add(f);
//			}
//		}
		
		if(taUnite1DTO!=null) {
			valideAutoCompleteVide(Const.C_CODE_UNITE,taUnite1DTO);
			selectedTaArticleDTO.setCodeUnite(taUnite1DTO.getCodeUnite());
			selectedTaArticleDTO.setIdUnite(taUnite1DTO.getId());
		}
		if(taUnite2DTO!=null) {
			valideAutoCompleteVide(Const.C_CODE_UNITE2,taUnite2DTO);
			selectedTaArticleDTO.setCodeUnite2(taUnite2DTO.getCodeUnite());
			selectedTaArticleDTO.setIdUnite2(taUnite2DTO.getId());
		}
		if(taUniteReferenceDTO!=null) {
			valideAutoCompleteVide(Const.C_CODE_UNITE_REFERENCE,taUniteReferenceDTO);
			selectedTaArticleDTO.setCodeUniteReference(taUniteReferenceDTO.getCodeUnite());
			selectedTaArticleDTO.setIdUniteReference(taUniteReferenceDTO.getId());
		}
		if(taUniteSaisieDTO!=null) {
			valideAutoCompleteVide(Const.C_CODE_UNITE_SAISIE,taUniteSaisieDTO);
			selectedTaArticleDTO.setCodeUniteSaisie(taUniteSaisieDTO.getCodeUnite());
			selectedTaArticleDTO.setIdUniteSaisie(taUniteSaisieDTO.getId());
		}
		if(taUniteStockDTO!=null) {
			valideAutoCompleteVide(Const.C_CODE_UNITE_STOCK,taUniteStockDTO);
			selectedTaArticleDTO.setCodeUniteStock(taUniteStockDTO.getCodeUnite());
			selectedTaArticleDTO.setIdUniteStock(taUniteStockDTO.getId());
		}		
		if(taTvaDTO!=null) {
			valideAutoCompleteVide(Const.C_CODE_TVA,taTvaDTO);
			selectedTaArticleDTO.setCodeTva(taTvaDTO.getCodeTva());
			selectedTaArticleDTO.setIdTva(taTvaDTO.getId());
		}
		if(taTTvaDTO!=null) {
			valideAutoCompleteVide(Const.C_CODE_T_TVA,taTTvaDTO);
			selectedTaArticleDTO.setCodeTTva(taTTvaDTO.getCodeTTva());
			selectedTaArticleDTO.setIdTTva(taTTvaDTO.getId());
		}
		if(taMarqueArticleDTO!=null) {
			valideAutoCompleteVide(Const.C_CODE_MARQUE_ARTICLE,taMarqueArticleDTO);
			selectedTaArticleDTO.setCodeMarqueArticle(taMarqueArticleDTO.getCodeMarqueArticle());
			selectedTaArticleDTO.setIdMarqueArticle(taMarqueArticleDTO.getId());
		}
		if(taTitreTransportDTO!=null) {
			valideAutoCompleteVide(Const.C_CODE_TITRE_TRANSPORT,taTitreTransportDTO);
			selectedTaArticleDTO.setCodeTitreTransport(taTitreTransportDTO.getCodeTitreTransport());
			taArticle=taArticleService.initTaRTaTitreTransport(taArticle);
		}		
	}
	
	
//	/**
//	 * Creation d'un objet "TaRTaTitreTransport" pour l'objet "TaArticle" gerer par cet ecran
//	 * dans le cas ou la propriete TaRTaTitreTransport de ce dernier est nulle.
//	 */
//	private void initTaRTaTitreTransport() {
//		try {
//			if(taArticle.getTaRTaTitreTransport()==null ) {
//				//initialisation
//				TaRTaTitreTransport r = null;
//				if(taArticle.getIdArticle()!=0) {
//					r = taRTitreTransportService.findByIdArticle(taArticle.getIdArticle());
//				}
//				if (r==null) 
//					r = new TaRTaTitreTransport();
//				r.setTaArticle(taArticle);
//				taArticle.setTaRTaTitreTransport(r);
//
//				if(taArticle.getTaUnite1()!=null) {
//					taArticle.getTaRTaTitreTransport().setTaUnite(taArticle.getTaUnite1());
//				}
//			}
//		} catch (FinderException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
	public void autoCompleteMapDTOtoUI() {
		try {
			taFamille = null;
			taUnite1 = null;
			taUnite2 = null;
			taUniteReference = null;
			taUniteSaisie = null;
			taTva = null;
			taTTva = null;
			taUniteStock = null;
			taMarqueArticle = null;
			taFournisseur = null;
			taTitreTransport = null;
			
//			if(selectedTaArticleDTO.getCodeFamille()!=null && !selectedTaArticleDTO.getCodeFamille().equals("")) {
//				taFamille = taFamilleService.findByCode(selectedTaArticleDTO.getCodeFamille());
//			}
//			if(selectedTaArticleDTO.getCodeUnite()!=null && !selectedTaArticleDTO.getCodeUnite().equals("")) {
//				taUnite1 = taUniteService.findByCode(selectedTaArticleDTO.getCodeUnite());
//			}
//			if(selectedTaArticleDTO.getCodeUnite2()!=null && !selectedTaArticleDTO.getCodeUnite2().equals("")) {
//				taUnite2 = taUniteService.findByCode(selectedTaArticleDTO.getCodeUnite2());
//			}
			taFamilleDTO = null;
//			taFamilleDTOs = null;
			taUnite1DTO = null;
			taUnite2DTO = null;
			taUniteReferenceDTO = null;
			taUniteStockDTO = null;
			taUniteSaisieDTO = null;
			taTvaDTO = null;
			taTTvaDTO = null;
			taMarqueArticleDTO = null;
			taFournisseurDTO = null;
			taTitreTransportDTO = null;
//			taFournisseurDTOs = null;
			
			//init des DTO multiple (autocomplete multiple)
			if(taArticle!=null && taArticle.getTaFamilles()!=null && !taArticle.getTaFamilles().isEmpty()) {
				selectedTaArticleDTO.setTaFamilleDTOs(new ArrayList<>());
				for (TaFamille f : taArticle.getTaFamilles()) {
					selectedTaArticleDTO.getTaFamilleDTOs().add(mapperModelToUIFamille.map(f, TaFamilleDTO.class));
				}
			}
			if(taArticle!=null && taArticle.getTaFournisseurs()!=null && !taArticle.getTaFournisseurs().isEmpty()) {
				selectedTaArticleDTO.setTaFournisseurDTOs(new ArrayList<>());
				for (TaTiers f : taArticle.getTaFournisseurs()) {
					selectedTaArticleDTO.getTaFournisseurDTOs().add(mapperModelToUIFournisseur.map(f, TaTiersDTO.class));
				}
			}
			
			if(selectedTaArticleDTO.getCodeFamille()!=null && !selectedTaArticleDTO.getCodeFamille().equals("")) {
				taFamille = taFamilleService.findByCode(selectedTaArticleDTO.getCodeFamille());
				taFamilleDTO = mapperModelToUIFamille.map(taFamille, TaFamilleDTO.class);
			}
			if(selectedTaArticleDTO.getTaFamilleDTOs()!=null) {
				if(taFamilles==null) {
					taFamilles = new ArrayList<>();
				}
//				if(taFamilleDTOs==null) {
//					taFamilleDTOs = new ArrayList<>();
//				}
				for (TaFamilleDTO f : selectedTaArticleDTO.getTaFamilleDTOs()) {
					if(f.getCodeFamille()!=null && !f.getCodeFamille().equals("")) {
						taFamille = taFamilleService.findByCode(f.getCodeFamille());
						taFamilles.add(taFamille);
						
//						taFamilleDTO = mapperModelToUIFamille.map(taFamille, TaFamilleDTO.class);
//						taFamilleDTOs.add(taFamilleDTO);
					}
				}
			}
			if(selectedTaArticleDTO.getTaFournisseurDTOs()!=null) {
				if(taFournisseurs==null) {
					taFournisseurs = new ArrayList<>();
				}
				taFournisseurs.clear();
//				if(taFournisseurDTOs==null) {
//					taFournisseurDTOs = new ArrayList<>();
//				}
				for (TaTiersDTO f : selectedTaArticleDTO.getTaFournisseurDTOs()) {
					if(f.getCodeTiers()!=null && !f.getCodeTiers().equals("")) {
						taFournisseur = taFournisseurService.findByCode(f.getCodeTiers());
						taFournisseurs.add(taFournisseur);
						
//						taFournisseurDTO = mapperModelToUIFournisseur.map(taFournisseur, TaTiersDTO.class);
//						taFournisseurDTOs.add(taFournisseurDTO);
					}
				}
			}
			if(selectedTaArticleDTO.getCodeTitreTransport()!=null && !selectedTaArticleDTO.getCodeTitreTransport().equals("")) {
				TaRTaTitreTransport f= taRTitreTransportService.findByIdArticle(selectedTaArticleDTO.getId());
				if(f!=null) {
					taTitreTransport =f.getTaTitreTransport();
					if(f.getTaTitreTransport()!=null)
						taTitreTransportDTO = mapperModelToUITitreTransport.map(f.getTaTitreTransport(),TaTitreTransportDTO.class);
				}
			}
			if(selectedTaArticleDTO.getCodeUnite()!=null && !selectedTaArticleDTO.getCodeUnite().equals("")) {
				taUnite1 = taUniteService.findByCode(selectedTaArticleDTO.getCodeUnite());
				taUnite1DTO = mapperModelToUIUnite.map(taUnite1, TaUniteDTO.class);
			}
			if(selectedTaArticleDTO.getCodeUnite2()!=null && !selectedTaArticleDTO.getCodeUnite2().equals("")) {
				taUnite2 = taUniteService.findByCode(selectedTaArticleDTO.getCodeUnite2());
				taUnite2DTO = mapperModelToUIUnite.map(taUnite2, TaUniteDTO.class);
			}
			if(selectedTaArticleDTO.getCodeUniteReference()!=null && !selectedTaArticleDTO.getCodeUniteReference().equals("")) {
				taUniteReference = taUniteService.findByCode(selectedTaArticleDTO.getCodeUniteReference());
				taUniteReferenceDTO = mapperModelToUIUnite.map(taUniteReference, TaUniteDTO.class);
			}
			if(selectedTaArticleDTO.getCodeUniteSaisie()!=null && !selectedTaArticleDTO.getCodeUniteSaisie().equals("")) {
				taUniteSaisie = taUniteService.findByCode(selectedTaArticleDTO.getCodeUniteSaisie());
				taUniteSaisieDTO = mapperModelToUIUnite.map(taUniteSaisie, TaUniteDTO.class);
			}
			if(selectedTaArticleDTO.getCodeUniteStock()!=null && !selectedTaArticleDTO.getCodeUniteStock().equals("")) {
				taUniteStock = taUniteService.findByCode(selectedTaArticleDTO.getCodeUniteStock());
				taUniteStockDTO = mapperModelToUIUnite.map(taUniteStock, TaUniteDTO.class);
			}			
			if(selectedTaArticleDTO.getCodeTva()!=null && !selectedTaArticleDTO.getCodeTva().equals("")) {
				taTva = taTvaService.findByCode(selectedTaArticleDTO.getCodeTva());
				taTvaDTO = mapperModelToUITva.map(taTva, TaTvaDTO.class);
			}
			if(selectedTaArticleDTO.getCodeTTva()!=null && !selectedTaArticleDTO.getCodeTTva().equals("")) {
				taTTva = taTTvaService.findByCode(selectedTaArticleDTO.getCodeTTva());
				taTTvaDTO = mapperModelToUITTva.map(taTTva, TaTTvaDTO.class);
			}
			if(selectedTaArticleDTO.getCodeMarqueArticle()!=null && !selectedTaArticleDTO.getCodeMarqueArticle().equals("")) {
				taMarqueArticle = taMarqueArticleService.findByCode(selectedTaArticleDTO.getCodeMarqueArticle());
				taMarqueArticleDTO = mapperModelToUIMarque.map(taMarqueArticle, TaMarqueArticleDTO.class);
			}

			
		} catch (FinderException e) {
			e.printStackTrace();
		}
	}
	
	public void initListeMultiple(){
		taFamille = null;
		taFournisseur = null;

		taFamilleDTO = null;
//		taFamilleDTOs = null;

		taFournisseurDTO = null;
//		taFournisseurDTOs = null;
		
		try{
		if(selectedTaArticleDTO.getTaFamilleDTOs()!=null) {
			if(taFamilles==null) {
				taFamilles = new ArrayList<>();
			}
			taFamilles.clear();
			
//			if(taFamilleDTOs==null) {
//				taFamilleDTOs = new ArrayList<>();
//			}
//			taFamilleDTOs.clear();
			
			for (TaFamilleDTO f : selectedTaArticleDTO.getTaFamilleDTOs()) {
				if(f.getCodeFamille()!=null && !f.getCodeFamille().equals("")) {
					taFamille=taFamilleService.findById(f.getId());
					taFamilles.add(taFamille);
					
//					taFamilleDTO = mapperModelToUIFamille.map(taFamille, TaFamilleDTO.class);
//					taFamilleDTOs.add(taFamilleDTO);
				}
			}
		}
		if(selectedTaArticleDTO.getTaFournisseurDTOs()!=null) {
			if(taFournisseurs==null) {
				taFournisseurs = new ArrayList<>();
			}
//			if(taFournisseurDTOs==null) {
//				taFournisseurDTOs = new ArrayList<>();
//			}
			for (TaTiersDTO f : selectedTaArticleDTO.getTaFournisseurDTOs()) {
				if(f.getCodeTiers()!=null && !f.getCodeTiers().equals("")) {
					taFournisseur = taFournisseurService.findByCode(f.getCodeTiers());
					taFournisseurs.add(taFournisseur);
					
//					taFournisseurDTO = mapperModelToUIFournisseur.map(taFournisseur, TaTiersDTO.class);
//					taFournisseurDTOs.add(taFournisseurDTO);
				}
			}
		}
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void actEnregistrer(ActionEvent actionEvent) {
		
//		TaArticle taArticle = new TaArticle();
		
		//si l'article a au moins une famille mais pas de famille par defaut
//		if((selectedTaArticleDTO.getTaFamilleDTOs()!= null && !selectedTaArticleDTO.getTaFamilleDTOs().isEmpty()) && selectedTaArticleDTO.getTaFamilleDTO() == null ) {
//			// On attribut la premiere famille de la lsite a la famille par defaut
//			selectedTaArticleDTO.setTaFamilleDTO(selectedTaArticleDTO.getTaFamilleDTOs().get(0));
//		}
		autoCompleteMapUIToDTO();
		initListeMultiple();
		
		mapperUIToModel.map(selectedTaArticleDTO, taArticle);
		
		if(taArticle.getTaRapportUnite()!=null){
			if(taArticle.getTaRapportUnite().getRapport()!=null){
				taArticle.getTaRapportUnite().setTaArticle(taArticle);
				taArticle.getTaRapportUnites().add(taArticle.getTaRapportUnite());
			}else {
				taArticle.getTaRapportUnites().remove(taArticle.getTaRapportUnite());
				taArticle.setTaRapportUnite(null);
			}
		}
		
		if(taArticle.getTaPrix()!=null){
			taArticle.getTaPrix().setTaArticle(taArticle);
			if(!taArticle.getTaPrixes().contains(taArticle.getTaPrix()))
				taArticle.getTaPrixes().add(taArticle.getTaPrix());
		}
		//suppression de la nomenclature si article non compose
		if(taArticle.getCompose() == false) {
			taArticle = taArticleService.setNomenclature(taArticle);
			
			if(taArticle.getNomenclature() != null && !taArticle.getNomenclature().isEmpty()) {
				taArticleComposeService.deleteSet(taArticle.getNomenclature());
				taArticle.getNomenclature().clear();
			}
		}
		
		try {	
			validateNumCptArticle(taArticle.getNumcptArticle());
			taArticle = taArticleService.merge(taArticle,ITaArticleServiceRemote.validationContext);
			taArticle = taArticleService.findByIdAvecLazy(taArticle.getIdArticle(), gestionCapsules);

			if(selectedTaArticleDTO.getCodeArticle()!=null) {
				taArticleService.annuleCode(selectedTaArticleDTO.getCodeArticle());
			}
			
			mapperModelToUI.map(taArticle, selectedTaArticleDTO);
			autoCompleteMapDTOtoUI();
			
			if ((modeEcran.getMode().compareTo(EnumModeObjet.C_MO_INSERTION)==0)) {
				values.add(selectedTaArticleDTO);
				selectedTaArticleDTOs = null;
			}
			
//			listeConformiteController.setMasterEntity(taArticle);
//			listeConformiteController.refresh();
			
			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

		} catch(Exception e) {
			FacesContext context = FacesContext.getCurrentInstance();  
			//	    context.addMessage(null, new FacesMessage("Articles", "actEnregistrer"));
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Articles", e.getMessage()));
		}
	}
	
	public void actInserer(ActionEvent actionEvent) {
		selectedTaArticleDTOs = new TaArticleDTO[]{};
		selectedTaArticleDTO = new TaArticleDTO();
		taArticle = new TaArticle();
		
		valuesMPRecette.clear();
		
		selectedTaArticleDTO.setPrixPrix(new BigDecimal(0));
		selectedTaArticleDTO.setPrixttcPrix(new BigDecimal(0));
		
		taGenCodeExService.libereVerrouTout(new ArrayList<String>(SessionListener.getSessions().keySet()));
		selectedTaArticleDTO.setCodeArticle(taArticleService.genereCode(null));
		
		selectedTaArticleDTO.setActif(true);
		//validateUIField(Const.C_CODE_ARTICLE,selectedTaArticleDTO.getCodeArticle());//permet de verrouiller le code genere
		selectedTaArticleDTO.setStockMinArticle(new BigDecimal(-1));
		
		TaPreferences p = taPreferencesService.findByGoupeAndCle(ITaPreferencesServiceRemote.GROUPE_PREFERENCES_LOTS, ITaPreferencesServiceRemote.LOTS_GESTION_DES_LOTS);
		if(p!=null && LibConversion.StringToBoolean(p.getValeur()) != null) {
			selectedTaArticleDTO.setGestionLot(LibConversion.StringToBoolean(p.getValeur()));
			taArticle.setGestionLot(LibConversion.StringToBoolean(p.getValeur()));
		}
		
		autoCompleteMapDTOtoUI();
		
		modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
		scrollToTop();
		if(tabViewArticle!=null) {
			tabViewArticle.setActiveIndex(1);
		}
		if(ConstWeb.DEBUG) {
		   	FacesContext context = FacesContext.getCurrentInstance();  
		    context.addMessage(null, new FacesMessage("Articles", "actInserer"));
		}
	}
	
	public void actModifier() {
		actModifier(null);
	}
	
	public void actModifier(ActionEvent actionEvent) {
		
		try {
			taArticle = taArticleService.findById(selectedTaArticleDTO.getId());
			
			modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
			
//			if(tabViewArticle!=null) {
//				tabViewArticle.setActiveIndex(0);
//			}
			
			if(ConstWeb.DEBUG) {
			   	FacesContext context = FacesContext.getCurrentInstance();  
			    context.addMessage(null, new FacesMessage("Articles", "actModifier"));
			}
		    
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void actSupprimer() {
		actSupprimer(null);
	}
	
	public void actSupprimer(ActionEvent actionEvent) {
		String msg = "";
		TaArticle taArticle = new TaArticle();
		try {
			if(selectedTaArticleDTO!=null && selectedTaArticleDTO.getId()!=null){
				taArticle = taArticleService.findById(selectedTaArticleDTO.getId());
			}

			taArticleService.remove(taArticle);
			values.remove(selectedTaArticleDTO);
			

			
			if(!values.isEmpty()) {
				selectedTaArticleDTO = values.get(0);
			}else {
				selectedTaArticleDTO=new TaArticleDTO();
			}
			selectedTaArticleDTOs=new TaArticleDTO[]{};
			
			updateTabs();
			
			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Article", "actSupprimer"));
			}
		} catch(Exception e) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Articles", e.getMessage()));
		}
//		} catch (RemoveException e) {
//			e.printStackTrace();
//		} catch (FinderException e) {
//			e.printStackTrace();
//		}	    
	}
	
	public void actFermer(ActionEvent actionEvent) {
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
		
		selectedTaArticleDTO=new TaArticleDTO();
		selectedTaArticleDTOs=new TaArticleDTO[]{selectedTaArticleDTO};
		
//		//gestion du filtre de la liste
//        RequestContext requestContext = RequestContext.getCurrentInstance();
//        requestContext.execute("PF('wvDataTableListeArticle').filter()");
		
//		LgrTab typeOngletDejaOuvert = null;
//		for (LgrTab o : tabsCenter.getOnglets()) {
//			if(LgrTab.TYPE_TAB_ARTICLE.equals(o.getTypeOnglet())) {
//				typeOngletDejaOuvert = o;
//			}
//		}
//		
//		if(typeOngletDejaOuvert!=null) {
////			if(!values.isEmpty()) {
////				selectedTaTiersDTO = values.get(0);
////			}
//			tabsCenter.supprimerOnglet(typeOngletDejaOuvert);
//		}
//		
//		if(ConstWeb.DEBUG) {
//		FacesContext context = FacesContext.getCurrentInstance();  
//		context.addMessage(null, new FacesMessage("Articles", "actFermer"));
//		}
	}
	
	public void actImprimer(ActionEvent actionEvent) {
		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage("Articles", "actImprimer")); 
		try {
			
//		FacesContext facesContext = FacesContext.getCurrentInstance();
//		HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
			
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		sessionMap.put("article", taArticleService.findById(selectedTaArticleDTO.getId()));
		
//			session.setAttribute("tiers", taTiersService.findById(selectedTaTiersDTO.getId()));
			System.out.println("ArticleController.actImprimer()");
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
    
	public void actImprimerListeDesArticles(ActionEvent actionEvent) {
		
		try {
			
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			
			sessionMap.put("listeDesArticles", values);

			} catch (Exception e) {
				e.printStackTrace();
			}
	}    

    public void nouveau(ActionEvent actionEvent) {  
    	LgrTab b = new LgrTab();
    	b.setTypeOnglet(LgrTab.TYPE_TAB_ARTICLE);
		b.setTitre("Article");
		b.setStyle(LgrTab.CSS_CLASS_TAB_ARTICLE);
		b.setTemplate("articles/articles.xhtml");
		b.setIdTab(LgrTab.ID_TAB_ARTICLE);
		b.setParamId("0");
		
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
		actInserer(actionEvent);
		
		if(ConstWeb.DEBUG) {
	    	FacesContext context = FacesContext.getCurrentInstance();  
		    context.addMessage(null, new FacesMessage("Article", 
	    		"Nouveau"
	    		)); 
		}
    } 
    
    public void supprimer(ActionEvent actionEvent) {
    	actSupprimer(actionEvent);
    }
    
    public void detail() {
    	detail(null);
    }
    
    public void detail(ActionEvent actionEvent) {
    	onRowSelect(null);
    }
    
    public String detailResponsive() {
    	if(selectedTaArticleDTO!=null)
    		return "/m/articles.xhtml?idArticle="+selectedTaArticleDTO.getId()+"&faces-redirect=true";
    	else 
    		return "";
    }
   
    public String nouveauResponsive() {
		return "/m/articles.xhtml?idArticle=-1"+"&faces-redirect=true";
    }
    
	public void repondreMessage(ActionEvent event) {
		Integer idMessage = (Integer) event.getComponent().getAttributes().get("idArticle");
	    
	    ExternalContext context2 = FacesContext.getCurrentInstance().getExternalContext();
	    try {
			context2.redirect(context2.getRequestContextPath() + "/m/articles.xhtml?idArticle="+idMessage);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
    
	public boolean pasDejaOuvert() {
    	LgrTab b = new LgrTab();
    	b.setTypeOnglet(LgrTab.TYPE_TAB_ARTICLE);
		return tabsCenter.ongletDejaOuvert(b)==null;
	} 
	
	public void onRowSimpleSelect(SelectEvent event) {
		
		if(pasDejaOuvert()==false){
			
			onRowSelect(event);
			
			autoCompleteMapDTOtoUI();
			
			updateTabs();

			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Article", 
					"Chargement de l'article N°"+selectedTaArticleDTO.getCodeArticle()
					)); 
			}
		} else {
			tabViews.selectionneOngletCentre(LgrTab.TYPE_TAB_ARTICLE);
		}
			
	} 
	
    public void onRowSelect(SelectEvent event) {  
    	LgrTab b = new LgrTab();
    	b.setTypeOnglet(LgrTab.TYPE_TAB_ARTICLE);
		//b.setTitre("Article "+selectedTaArticleDTO.getCodeArticle());
    	b.setTitre("Article");
		b.setTemplate("articles/articles.xhtml");
		b.setIdTab(LgrTab.ID_TAB_ARTICLE);
		b.setStyle(LgrTab.CSS_CLASS_TAB_ARTICLE);
		b.setParamId(LibConversion.integerToString(selectedTaArticleDTO.getId()));
		
//		int pos = 0;
		tabsCenter.ajouterOnglet(b);
//		pos = tabViewArticle!=null?tabViewArticle.getActiveIndex():0;
		tabViews.selectionneOngletCentre(b);
		
//		if(tabViewArticle!=null) {
////			tabViewArticle.setActiveIndex(0);
//			//tabViewArticle.setActiveIndex(pos);
//		}
		
		updateTabs();
		
		scrollToTop();

		
		if(ConstWeb.DEBUG) {
	    	FacesContext context = FacesContext.getCurrentInstance();  
		    context.addMessage(null, new FacesMessage("Article", 
	    		"Chargement de l'article N°"+selectedTaArticleDTO.getCodeArticle()
	    		)); 
		}
    } 
    
    public String onRowSelectResponsive(SelectEvent event) {  
    String url="";
    	if(paramId!=null){
    		try {
    			taArticle = taArticleService.findById(LibConversion.stringToInteger(paramId));
    			//				mapperModelToUI.map(taArticle, selectedTaArticleDTO);
    			url= "m/tiers.xhtml?aa=#{paramId}";
    		} catch (FinderException e) {

    		}
    	}
		return url;	

    } 
	public TaArticleDTO[] getSelectedTaArticleDTOs() {
		return selectedTaArticleDTOs;
	}

	public void setSelectedTaArticleDTOs(TaArticleDTO[] selectedTaArticleDTOs) {
		this.selectedTaArticleDTOs = selectedTaArticleDTOs;
	}

	public TaArticleDTO getNewTaArticleDTO() {
		return newTaArticleDTO;
	}

	public void setNewTaArticleDTO(TaArticleDTO newTaArticleDTO) {
		this.newTaArticleDTO = newTaArticleDTO;
	}

	public TaArticleDTO getSelectedTaArticleDTO() {
		return selectedTaArticleDTO;
	}

	public void setSelectedTaArticleDTO(TaArticleDTO selectedTaArticleDTO) {
		this.selectedTaArticleDTO = selectedTaArticleDTO;
	}

	public TabListModelBean getTabsCenter() {
		return tabsCenter;
	}

	public void setTabsCenter(TabListModelBean tabsCenter) {
		this.tabsCenter = tabsCenter;
	}

	public void validateNumCptArticle(Object value) throws ValidatorException {
		if(exportComptaActivee && (value==null ||value.equals(""))) {
			String messageComplet="Erreur de validation : L'option exportation de vos factures (et avoirs) en comptabilité est activée, le N° de compte de cet article  doit être renseigné (en bas à droite de la fiche article).\n" + 
					"Si vous ne souhaitez pas exporter vos factures (et avoirs) vers un programme de comptabilité, vous pouvez désactiver cette option dans le menu \"Administration / Préférences / Exportation en comptabilité\"";
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, messageComplet, messageComplet));
		}
	}
	public void validateArticle(FacesContext context, UIComponent component, Object value) throws ValidatorException {

		//		String msg = "";
		//		
		//		try {
		//		  String nomChamp =  (String) component.getAttributes().get("nomChamp");
		//		  
		//		  validateUIField(nomChamp,value);
		//		 TaArticleDTO dtoTemp = new TaArticleDTO();
		//		 PropertyUtils.setSimpleProperty(dtoTemp, nomChamp, value);
		//		  taArticleService.validateDTOProperty(dtoTemp, nomChamp, ITaArticleServiceRemote.validationContext );
		//
		//		} catch(Exception e) {
		//			msg += e.getMessage();
		//			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
		//		}

		String messageComplet = "";

		try {

			String nomChamp =  (String) component.getAttributes().get("nomChamp");

			//validation automatique via la JSR bean validation
			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
			Set<ConstraintViolation<TaArticleDTO>> violations = factory.getValidator().validateValue(TaArticleDTO.class,nomChamp,value);
			if (violations.size() > 0) {
				messageComplet = "Erreur de validation : ";
				for (ConstraintViolation<TaArticleDTO> cv : violations) {
					messageComplet+=" "+cv.getMessage()+"\n";
				}
				throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, messageComplet, messageComplet));

			} else {
				if(nomChamp.equals(Const.C_NUMCPT_ARTICLE)) { 
					validateNumCptArticle(value);
				}
				validateUIField(nomChamp,value);

			}


		} catch(Exception e) {
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, messageComplet, messageComplet));
		}
	}

	public void autcompleteSelection(SelectEvent event) {
		Object value = event.getObject();
		
		String nomChamp =  (String) event.getComponent().getAttributes().get("nomChamp");

		valideAutoCompleteVide(nomChamp,value);
	}
	
	public void valideAutoCompleteVide(String nomChamp,Object value ){
		if(value!=null) {
			if(value instanceof String && value.equals(Const.C_AUCUN))value="";	
			if(value instanceof TaFamilleDTO && ((TaFamilleDTO) value).getCodeFamille()!=null && ((TaFamilleDTO) value).getCodeFamille().equals(Const.C_AUCUN))value=null;	
			if(value instanceof TaUniteDTO && ((TaUniteDTO) value).getCodeUnite()!=null && ((TaUniteDTO) value).getCodeUnite().equals(Const.C_AUCUN))value=null;
			if(value instanceof TaTvaDTO && ((TaTvaDTO) value).getCodeTva()!=null && ((TaTvaDTO) value).getCodeTva().equals(Const.C_AUCUN))value=null;
			if(value instanceof TaTTvaDTO && ((TaTTvaDTO) value).getCodeTTva()!=null && ((TaTTvaDTO) value).getCodeTTva().equals(Const.C_AUCUN))value=null;	
			if(value instanceof TaMarqueArticle && ((TaMarqueArticle) value).getCodeMarqueArticle()!=null && ((TaMarqueArticle) value).getCodeMarqueArticle().equals(Const.C_AUCUN))value=null;	
		}
		
		validateUIField(nomChamp,value);
	}
	
	public void autcompleteUnSelect(UnselectEvent event) {
		Object value = event.getObject();
		
		String nomChamp =  (String) event.getComponent().getAttributes().get("nomChamp");

		if(value!=null) {
			if(value instanceof String && value.equals(Const.C_AUCUN))value="";	
			if(value instanceof TaFamilleDTO && ((TaFamilleDTO) value).getCodeFamille()!=null && ((TaFamilleDTO) value).getCodeFamille().equals(Const.C_AUCUN))value=null;	
			if(value instanceof TaUniteDTO && ((TaUniteDTO) value).getCodeUnite()!=null && ((TaUniteDTO) value).getCodeUnite().equals(Const.C_AUCUN))value=null;
			if(value instanceof TaTvaDTO && ((TaTvaDTO) value).getCodeTva()!=null && ((TaTvaDTO) value).getCodeTva().equals(Const.C_AUCUN))value=null;
			if(value instanceof TaTTvaDTO && ((TaTTvaDTO) value).getCodeTTva()!=null && ((TaTTvaDTO) value).getCodeTTva().equals(Const.C_AUCUN))value=null;	
			if(value instanceof TaMarqueArticle && ((TaMarqueArticle) value).getCodeMarqueArticle()!=null && ((TaMarqueArticle) value).getCodeMarqueArticle().equals(Const.C_AUCUN))value=null;	
		}
		
		//validateUIField(nomChamp,value);
		try {
			if(nomChamp.equals(Const.C_CODE_FAMILLE)) {
				TaFamille entity =null;
				if(value!=null){
					if(value instanceof TaFamilleDTO){
	//				entity=(TaFamille) value;
						entity = taFamilleService.findByCode(((TaFamilleDTO)value).getCodeFamille());
					}else{
						entity = taFamilleService.findByCode((String)value);
					}
				}else{
					selectedTaArticleDTO.setCodeFamille("");
				}
				//taArticle.setTaFamille(entity);
				for (TaFamille f : taArticle.getTaFamilles()) {
					if(f.getIdFamille()==((TaFamilleDTO)value).getId())
						entity = f;
				}
				taArticle.getTaFamilles().remove(entity);
				if(taArticle.getTaFamille()!=null && taArticle.getTaFamille().getCodeFamille().equals(entity.getCodeFamille())) {
					taArticle.setTaFamille(null);
					taFamilleDTO = null;
					if(!taArticle.getTaFamilles().isEmpty()) {
						taArticle.setTaFamille(taArticle.getTaFamilles().iterator().next());
						//RAJOUT YANN
						taFamilleDTO = taFamilleService.findByIdDTO(taArticle.getTaFamilles().iterator().next().getIdFamille());
					}
				}
			}
			
			if(nomChamp.equals(Const.C_CODE_TIERS)) {
				TaTiers entity =null;
				if(value!=null){
					if(value instanceof TaTiersDTO){
						entity = taFournisseurService.findByCode(((TaTiersDTO)value).getCodeTiers());
					}else{
						entity = taFournisseurService.findByCode((String)value);
					}
				}else{
					selectedTaArticleDTO.setCodeFamille("");
				}
				for (TaTiers f : taArticle.getTaFournisseurs()) {
					if(f.getIdTiers()==((TaTiersDTO)value).getId())
						entity = f;
				}
				taArticle.getTaFournisseurs().remove(entity);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	
	}
	
	public void autcompleteSelectionRecette(SelectEvent event) {
		Object value = event.getObject();
		
		String nomChamp =  (String) event.getComponent().getAttributes().get("nomChamp");

		validateUIFieldRecette(nomChamp,value);
	}

	public boolean validateUIFieldRecette(String nomChamp,Object value) {
		try {
			
			if(nomChamp.equals(Const.C_CODE_ARTICLE)) {
				TaArticle entity = selectedTaMPRecette.getTaArticle();
				
				if(value!=null){
					if(value instanceof TaArticleDTO){
						entity = taArticleService.findByCode(((TaArticleDTO) value).getCodeArticle());
					}else{
						entity = taArticleService.findByCode((String)value);
					}
				}
				selectedTaMPRecette.setTaArticle(entity);
				selectedTaMPRecette.getDto().setLibLDocument(selectedTaMPRecette.getTaArticle().getLibellecArticle());
				selectedTaMPRecette.setTaUnite1(null);
				selectedTaMPRecette.setTaUnite2(null);
				TaRapportUnite rapport=entity.getTaRapportUnite();
				if(rapport!=null){
					selectedTaMPRecette.setTaRapport(rapport);
					if(rapport.getTaUnite1()!=null) {
						selectedTaMPRecette.getDto().setU1LDocument(rapport.getTaUnite1().getCodeUnite());
						selectedTaMPRecette.setTaUnite1(taUniteService.findByCode(rapport.getTaUnite1().getCodeUnite()));
					}
					if(rapport.getTaUnite2()!=null){
						selectedTaMPRecette.getDto().setU2LDocument(rapport.getTaUnite2().getCodeUnite());
						selectedTaMPRecette.setTaUnite2(taUniteService.findByCode(rapport.getTaUnite2().getCodeUnite()));
					}
				}else
				if(entity.getTaPrix()!=null){
					if(entity.getTaUnite1()!=null) {
						selectedTaMPRecette.getDto().setU1LDocument(entity.getTaUnite1().getCodeUnite());
						selectedTaMPRecette.setTaUnite1(taUniteService.findByCode(entity.getTaUnite1().getCodeUnite()));
					}
					for (TaRapportUnite obj : entity.getTaRapportUnites()) {
						if(obj.getTaUnite1()!=null && obj.getTaUnite1().getCodeUnite().equals(selectedTaMPRecette.getDto().getU1LDocument())){
							selectedTaMPRecette.setTaRapport(obj);
							selectedTaMPRecette.getDto().setU2LDocument(obj.getTaUnite2().getCodeUnite());	
							selectedTaMPRecette.setTaUnite2(taUniteService.findByCode(obj.getTaUnite2().getCodeUnite()));
						}							
					}
				}
			} else if(nomChamp.equals(Const.C_CODE_UNITE)) {				
//				TaUnite entity = null;
//				if(value!=null){
//					if(taArticle.getTaRapportUnite()==null) taArticle.setTaRapportUnite(new TaRapportUnite());
//					if(value instanceof TaUnite){
//					entity=(TaUnite) value;
//					}else{
//						entity = taUniteService.findByCode((String)value);
//					}
//				}
//				
//				if(entity!=null){
//				taArticle.getTaRapportUnite().setTaUnite1(taArticle.getTaPrix().getTaUnite());
//				taArticle.getTaRapportUnite().setTaUnite2(entity);
//				}else
//				if(taArticle.getTaRapportUnite()!=null){
//					taArticle.removeRapportUnite(taArticle.getTaRapportUnite());
//				}
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	

//	public boolean crdEstRempli(){
//		return taTitreTransportDTO!=null && !taTitreTransportDTO.getCodeTitreTransport().equals("");
//		
//	}
	public boolean validateUIField(String nomChamp,Object value) {
		try {
			
//			
			if(nomChamp.equals(Const.C_CODE_MARQUE_ARTICLE)) {
				TaMarqueArticle entity =null;
				if(value!=null){
					if(value instanceof TaMarqueArticleDTO){
					entity = taMarqueArticleService.findByCode(((TaMarqueArticleDTO)value).getCodeMarqueArticle());
					}else{
						entity = taMarqueArticleService.findByCode((String)value);
					}
				}else{
					selectedTaArticleDTO.setCodeMarqueArticle("");
					taMarqueArticleDTO.setCodeMarqueArticle(null);
				}
				taArticle.setTaMarqueArticle(entity);
		
		} else if(nomChamp.equals(Const.C_CODE_UNITE_STOCK)) {
				TaUnite entity =null;
				if(value!=null){
					if(value instanceof TaUniteDTO){
					entity = taUniteService.findByCode(((TaUniteDTO)value).getCodeUnite());
					}else{
						entity = taUniteService.findByCode((String)value);
					}
				}else{
					selectedTaArticleDTO.setCodeUniteStock("");
					taUniteStockDTO.setCodeUnite(null);
				}
				taArticle.setTaUniteStock(entity);
		} else if(nomChamp.equals(Const.C_CODE_UNITE_REFERENCE)) {
			TaUnite entity =null;
			if(value!=null){
				if(value instanceof TaUniteDTO){
				entity = taUniteService.findByCode(((TaUniteDTO)value).getCodeUnite());
				}else{
					entity = taUniteService.findByCode((String)value);
				}
			}else{
				selectedTaArticleDTO.setCodeUniteReference("");
				taUniteReferenceDTO.setCodeUnite(null);
			}
			taArticle.setTaUniteReference(entity);
			
			
			
		} else if(nomChamp.equals(Const.C_CODE_UNITE_SAISIE)) {
			TaUnite entity =null;
			if(value!=null){
				if(value instanceof TaUniteDTO){
				entity = taUniteService.findByCode(((TaUniteDTO)value).getCodeUnite());
				}else{
					entity = taUniteService.findByCode((String)value);
				}
			}else{
				selectedTaArticleDTO.setCodeUniteSaisie("");
				taUniteSaisieDTO.setCodeUnite(null);
			}
			taArticle.setTaUniteSaisie(entity);
			
			
			
		}else if(nomChamp.equals(Const.C_QTE_TITRE_TRANSPORT_L_DOCUMENT)) {
		
			TaRTaTitreTransport entity =null;
			
				if((value==null || value.equals("") || ((BigDecimal)value).doubleValue()<=0) && taArticleService.crdEstRempli(selectedTaArticleDTO))
					value="1";
					
//				value=LibConversion.stringToBigDecimal(value.toString());

			
			if(taArticleService.crdEstRempli(selectedTaArticleDTO) ){
				taArticle=taArticleService.initTaRTaTitreTransport(taArticle);
				taArticle.getTaRTaTitreTransport().setQteTitreTransport( (BigDecimal)value );
			} else {
				if(taArticle.getTaRTaTitreTransport()!=null) {
					taArticle.setTaRTaTitreTransport(null);
				}
			}
			
		} else if(nomChamp.equals(Const.C_CODE_TITRE_TRANSPORT)) {

			TaTitreTransport entity =null;
			if(value!=null){
				if(value instanceof TaTitreTransportDTO){
				entity = taTitreTransportService.findByCode(((TaTitreTransportDTO)value).getCodeTitreTransport());
				}else{
					entity = taTitreTransportService.findByCode((String)value);
				}
				taArticle=taArticleService.initTaRTaTitreTransport(taArticle);
				
				taArticle.getTaRTaTitreTransport().setTaTitreTransport(entity);
				selectedTaArticleDTO.setCodeTitreTransport(entity.getCodeTitreTransport());
				}
			else{
				selectedTaArticleDTO.setCodeTitreTransport("");
				taTitreTransportDTO.setCodeTitreTransport(null);
				if(taArticle.getTaRTaTitreTransport()!=null) {
					taArticle.setTaRTaTitreTransport(null);
				}
			}
			
		} 		
		
		else if(nomChamp.equals(Const.C_CODE_FAMILLE) || nomChamp.equals(Const.C_CODE_FAMILLE+"_DEFAUT")) {
					TaFamille entity =null;
					if(value!=null){
						if(value instanceof TaFamilleDTO){
//						entity=(TaFamille) value;
							entity = taFamilleService.findByCode(((TaFamilleDTO)value).getCodeFamille());
						}else{
							entity = taFamilleService.findByCode((String)value);
						}
					}else{
						selectedTaArticleDTO.setCodeFamille("");
					}
					taArticle.getTaFamilles().add(entity);
					if(nomChamp.equals(Const.C_CODE_FAMILLE+"_DEFAUT")) {
						taArticle.setTaFamille(entity);
						taFamilleDTO = (TaFamilleDTO)value;
						selectedTaArticleDTO.getTaFamilleDTOs().add((TaFamilleDTO)value);
					} else if(taArticle.getTaFamilles().size()==1 && taArticle.getTaFamille()==null) {
						taArticle.setTaFamille(entity);
						taFamilleDTO = (TaFamilleDTO)value;
					}
			
		}  else if(nomChamp.equals(Const.C_CODE_TIERS)) {
				TaTiers entity =null;
				if(value!=null){
					if(value instanceof TaTiersDTO){
						entity = taFournisseurService.findByCode(((TaTiersDTO)value).getCodeTiers());
					}else{
						entity = taFournisseurService.findByCode((String)value);
					}
				}
				taArticle.getTaFournisseurs().add(entity);
		
		} else if(nomChamp.equals(Const.C_CODE_TVA)) {
				TaTva entity = null;
				if(value!=null){
					if(value instanceof TaTvaDTO){
//							entity=(TaTva) value;
						entity = taTvaService.findByCode(((TaTvaDTO)value).getCodeTva());
					}else{
						entity = taTvaService.findByCode((String)value);
					}
				}else{
					selectedTaArticleDTO.setCodeTva("");
					taTvaDTO.setCodeTva("");
				}					
				taArticle.setTaTva(entity);
				
//					taArticle.initCodeTTva();
				//
				if (taArticle.getTaTva()!=null && !LibChaine.empty(taArticle.getTaTva().getCodeTva())){
					if( taArticle.getTaTTva()==null) {
						taArticle.setTaTTva(taTTvaService.findByCode("D")); 
					}  
				} else if(taArticle.getTaTva()==null || LibChaine.empty(taArticle.getTaTva().getCodeTva())) {
						taArticle.setTaTTva(null);	
				}
				if(taArticle.getTaTTva()!=null) {
					selectedTaArticleDTO.setCodeTTva(taArticle.getTaTTva().getCodeTTva());
				} else {
					selectedTaArticleDTO.setCodeTTva(null);	
				}
					
				taArticle.getTaPrix().majPrix();
				selectedTaArticleDTO.setPrixPrix(taArticle.getTaPrix().getPrixPrix());				
				selectedTaArticleDTO.setPrixttcPrix(taArticle.getTaPrix().getPrixttcPrix());	
			
			} else if(nomChamp.equals(Const.C_CODE_T_TVA)) {
					TaTTva entity = null;
					if(value!=null){
						if(value instanceof TaTTvaDTO){
//						entity=(TaTTva) value;
						entity = taTTvaService.findByCode(((TaTTvaDTO)value).getCodeTTva());
						}else{
							entity = taTTvaService.findByCode((String)value);
						}
					}else{
						selectedTaArticleDTO.setCodeTTva("");
						taTTvaDTO.setCodeTTva("");
					}
					taArticle.setTaTTva(entity);
//					taArticle.initCodeTTva();
					//
					if (taArticle.getTaTva()!=null && !LibChaine.empty(taArticle.getTaTva().getCodeTva())){
						if( taArticle.getTaTTva()==null) {
							taArticle.setTaTTva(taTTvaService.findByCode("D"));  
						}
					}
//					else
//						if(taArticle.getTaTva()==null || LibChaine.empty(taArticle.getTaTva().getCodeTva()))
//							taArticle.setTaTTva(null);	
					
					//
					if(taArticle.getTaTTva()!=null)
						selectedTaArticleDTO.setCodeTTva(taArticle.getTaTTva().getCodeTTva());
				else selectedTaArticleDTO.setCodeTTva(null);					
				
			} else if(nomChamp.equals(Const.C_CODE_UNITE2)) {				
					TaUnite entity = null;
					if(value!=null){
						if(taArticle.getTaRapportUnite()==null) {
							taArticle.setTaRapportUnite(new TaRapportUnite());
							taArticle.getTaRapportUnite().setTaArticle(taArticle);
							taArticle.getTaRapportUnite().setRapport(BigDecimal.ONE);
						}
						if(value instanceof TaUniteDTO){
//						entity=(TaUnite) value;
						entity = taUniteService.findByCode(((TaUniteDTO) value).getCodeUnite());
						}else{
							entity = taUniteService.findByCode((String) value);
						}
					}else{
						selectedTaArticleDTO.setCodeUnite2(null);
						taUnite2DTO.setCodeUnite(null);
						taUnite2=null;
					}
					
					if(entity!=null){
					taArticle.getTaRapportUnite().setTaUnite1(taArticle.getTaUnite1());
					taArticle.getTaRapportUnite().setTaUnite2(entity);
					selectedTaArticleDTO.setCodeUnite2(entity.getCodeUnite());
					selectedTaArticleDTO.setIdUnite2(entity.getIdUnite());
					}else
					if(taArticle.getTaRapportUnite()!=null){
						taArticle.removeRapportUnite(taArticle.getTaRapportUnite());
						selectedTaArticleDTO.setRapport(null);
						selectedTaArticleDTO.setIdRapportUnite(null);
						selectedTaArticleDTO.setSens(null);
						selectedTaArticleDTO.setCodeUnite2(null);
						selectedTaArticleDTO.setIdUnite2(null);						
					}
				initEtatRapportUniteUI();
				
			} else if(nomChamp.equals(Const.C_RAPPORT)) {
				TaRapportUniteDTO dto = new TaRapportUniteDTO();
				PropertyUtils.setSimpleProperty(dto, nomChamp, value);
				if (taArticleService.rapportUniteEstRempli(selectedTaArticleDTO)){
					taArticle.getTaRapportUnite().setRapport(dto.getRapport());
				}
				
			}  else if(nomChamp.equals(Const.C_SENS_RAPPORT)) {

				TaRapportUniteDTO dto = new TaRapportUniteDTO();
				PropertyUtils.setSimpleProperty(dto, nomChamp, value);
				if (taArticleService.rapportUniteEstRempli(selectedTaArticleDTO)){
					taArticle.getTaRapportUnite().setSens(LibConversion.booleanToInt(dto.getSens()));
				}
			} else if(nomChamp.equals(Const.C_NB_DECIMAL)) {
				TaRapportUniteDTO dto = new TaRapportUniteDTO();
				PropertyUtils.setSimpleProperty(dto, nomChamp, value);
				if (taArticleService.rapportUniteEstRempli(selectedTaArticleDTO)){
					taArticle.getTaRapportUnite().setNbDecimal(dto.getNbDecimal());
				}

			} else if(nomChamp.equals(Const.C_CODE_UNITE)) {
				
					TaUnite entity = null;
					if(value!=null){
						if(value instanceof TaUniteDTO){
//						entity=(TaUnite) value;
						entity = taUniteService.findByCode(((TaUniteDTO)value).getCodeUnite());
						}else{
							entity = taUniteService.findByCode((String)value);
						}
					} else{
						selectedTaArticleDTO.setCodeUnite(null);
						taUnite1DTO.setCodeUnite(null);
						taUnite1=null;
					}
					if(entity!=null){
						taArticle=taArticleService.initPrixArticle(taArticle,selectedTaArticleDTO);
						taArticle.setTaUnite1(entity);
						}else{
							if(taArticle.getTaRapportUnite()!=null){
								taArticle.removeRapportUnite(taArticle.getTaRapportUnite());
								selectedTaArticleDTO.setRapport(null);
								selectedTaArticleDTO.setIdRapportUnite(null);
								selectedTaArticleDTO.setSens(null);
							}
							if (taArticle.getTaPrix()!=null)
							{
								taArticle.setTaUnite1(null);
								if(taArticle.getTaPrix().getPrixPrix()!=null && 
										taArticle.getTaPrix().getPrixPrix().equals(0)&& 
										taArticle.getTaPrix().getPrixttcPrix()!=null && 
										taArticle.getTaPrix().getPrixttcPrix().equals(0))
									taArticle.removePrix(taArticle.getTaPrix());
							}
						}
						initEtatRapportUniteUI();
				
			} else if(nomChamp.equals(Const.C_PRIX_PRIX)) {

				TaPrixDTO dto = new TaPrixDTO();
//				TaPrix f = new TaPrix();
				int change =0;

						
				if(value!=null && !value.equals("")) {
					taArticle=taArticleService.initPrixArticle(taArticle,selectedTaArticleDTO);
				}
				PropertyUtils.setSimpleProperty(dto, nomChamp, value);
//				PropertyUtils.setSimpleProperty(f, nomChamp, value);
				if(value!=null && selectedTaArticleDTO.getPrixPrix()!=null) {
					change =dto.getPrixPrix().compareTo(selectedTaArticleDTO.getPrixPrix());
				} else {
					change=-1;
				}
				dto.setPrixttcPrix(selectedTaArticleDTO.getPrixttcPrix());							
//				validationClient.validate(dto,nomChamp,ITaArticleServiceRemote.validationContext);
						

				if (change!=0 || dto.getPrixPrix().compareTo(new BigDecimal(0))==0){
					if( ( value==null || value.equals("") || value.equals(0) ) && taArticle.getTaPrix()!=null ){	
						//taArticle.setTaPrix(null);
						taArticle.removePrix(taArticle.getTaPrix());
					} else {
						if((value!=null && !value.equals(""))&& taArticle.getTaPrix()!=null){
							taArticle=taArticleService.initPrixArticle(taArticle,selectedTaArticleDTO);						
							taArticle.getTaPrix().setPrixPrix(dto.getPrixPrix());
							taArticle.getTaPrix().setPrixttcPrix(dto.getPrixttcPrix());
							taArticle.getTaPrix().majPrix();
						}
					}
					selectedTaArticleDTO.setPrixPrix(taArticle.getTaPrix().getPrixPrix());				
					selectedTaArticleDTO.setPrixttcPrix(taArticle.getTaPrix().getPrixttcPrix());	 						
				}
			}else if(nomChamp.equals(Const.C_PRIXTTC_PRIX)) {
				TaPrixDTO dto = new TaPrixDTO();
				int change =0;

				if(value!=null && !value.equals("")) {
					taArticle=taArticleService.initPrixArticle(taArticle,selectedTaArticleDTO);
				}
				PropertyUtils.setSimpleProperty(dto, nomChamp, value);
				if(value!=null && selectedTaArticleDTO.getPrixttcPrix()!=null) {
					change =dto.getPrixttcPrix().compareTo(selectedTaArticleDTO.getPrixttcPrix());
				} else {
					change=-1;
				}
				dto.setPrixPrix(selectedTaArticleDTO.getPrixPrix());						
//				validationClient.validate(dto,nomChamp,ITaArticleServiceRemote.validationContext);


				if(change!=0 || dto.getPrixttcPrix().compareTo(new BigDecimal(0))==0){
					if((value==null||value.equals("")||value.equals(0))&& taArticle.getTaPrix()!=null){
						//taArticle.setTaPrix(null);
						taArticle.removePrix(taArticle.getTaPrix());
					} else {
						if((value!=null && !value.equals(""))&& taArticle.getTaPrix()!=null){
							taArticle=taArticleService.initPrixArticle(taArticle,selectedTaArticleDTO);						
							taArticle.getTaPrix().setPrixPrix(dto.getPrixPrix());
							taArticle.getTaPrix().setPrixttcPrix(dto.getPrixttcPrix());
							taArticle.getTaPrix().majPrixTTC();
						}
					}
					selectedTaArticleDTO.setPrixPrix(taArticle.getTaPrix().getPrixPrix());				
					selectedTaArticleDTO.setPrixttcPrix(taArticle.getTaPrix().getPrixttcPrix()); 						
				}
			} else if(nomChamp.equals(Const.C_QTE_TITRE_TRANSPORT_L_DOCUMENT)) {

				if(!((value==null || value.equals("")) && (!taArticleService.crdEstRempli(selectedTaArticleDTO)))){	
					if((value==null || value.equals("") || ((BigDecimal)value).doubleValue()<=0) && taArticleService.crdEstRempli(selectedTaArticleDTO))
						value="1";							
					value=LibConversion.stringToBigDecimal(value.toString());
				}							


					TaRTaTitreTransport entity = new TaRTaTitreTransport();
					if(value!=null){
						if(value instanceof TaRTaTitreTransport){
						entity=(TaRTaTitreTransport) value;
						}else{
							entity = taRTitreTransportService.findByCode((String)value);
						}
					}
					

					if(taArticleService.crdEstRempli(selectedTaArticleDTO) && value!=null){
						taArticle=taArticleService.initTaRTaTitreTransport(taArticle);
						taArticle.getTaRTaTitreTransport().setQteTitreTransport(entity.getQteTitreTransport());
					} else {
						if(taArticle.getTaRTaTitreTransport()!=null) {
							taArticle.setTaRTaTitreTransport(null);
						}
					}						

			} else if(nomChamp.equals(Const.C_CODE_TITRE_TRANSPORT)) {


					TaTitreTransport entity = new TaTitreTransport();
					if(value!=null){
						if(value instanceof TaTitreTransportDTO){
//						entity=(TaTitreTransport) value;
							entity=taTitreTransportService.findByCode(((TaTitreTransportDTO)value).getCodeTitreTransport());
						}else{
							entity = taTitreTransportService.findByCode((String)value);
						}
					}					
					
					if (!value.equals("")){
						taArticle=taArticleService.initTaRTaTitreTransport(taArticle);
					taArticle.getTaRTaTitreTransport().setTaTitreTransport(entity);
				} else {
					if(taArticle.getTaRTaTitreTransport()!=null) {
						taArticle.setTaRTaTitreTransport(null);
					}
				}						
			
			} else {
					TaArticleDTO dto = new TaArticleDTO();
					TaArticle entity = new TaArticle();
					if(nomChamp.equals(Const.C_ACTIF_ARTICLE)) { //traitement des booleens
						if(value instanceof Integer) 
							if((Integer)value==1) value=new Boolean(true); else new Boolean(false);
					}
					if(nomChamp.equals(Const.C_CODE_ARTICLE)) { 
						boolean changement=false;
						if(selectedTaArticleDTO.getCodeArticle()!=null && value!=null && !selectedTaArticleDTO.getCodeArticle().equals(""))
						{
							if(value instanceof TaArticle)
								changement=((TaArticle) value).getCodeArticle().equals(selectedTaArticleDTO.getCodeArticle());
							else if(value instanceof String)
							changement=!value.equals(selectedTaArticleDTO.getCodeArticle());
						}
						if(changement && modeEcran.dataSetEnModeModification()){
							FacesContext context = FacesContext.getCurrentInstance();  
							context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Articles", Const.C_MESSAGE_CHANGEMENT_CODE));
						}
					}
					if(selectedTaArticleDTO!=null
							&& selectedTaArticleDTO !=null
							&& selectedTaArticleDTO.getId()!=null) {
						entity.setIdArticle(selectedTaArticleDTO.getId());
					}


					if(taArticle!=null) {
						if(taArticle.getTaCatalogueWeb()==null) {
							taArticle.setTaCatalogueWeb(new TaCatalogueWeb());
						}
						if(nomChamp.equals(Const.C_LIBELLEC_ARTICLE)) {
							if(LibChaine.empty(taArticle.getTaCatalogueWeb().getUrlRewritingCatalogueWeb())
									|| ConstPreferencesArticles.REGENERATION_URL_REWRITING_POUR_CATALOGUE_WEB_A_PARTIR_ECRAN_PRINCIPAL) {
								taArticle.getTaCatalogueWeb().setUrlRewritingCatalogueWeb(LibChaine.toUrlRewriting((String)value));
							}
						} else if(nomChamp.equals(Const.C_LIBELLEL_ARTICLE)) {
							if(LibChaine.empty(taArticle.getTaCatalogueWeb().getDescriptionLongueCatWeb())) {
								taArticle.getTaCatalogueWeb().setLibelleCatalogue((String)value);
							}
							if(LibChaine.empty(taArticle.getTaCatalogueWeb().getDescriptionLongueCatWeb())
									|| ConstPreferencesArticles.REGENERATION_DESCRIPTION_POUR_CATALOGUE_WEB_A_PARTIR_ECRAN_PRINCIPAL) {
								taArticle.getTaCatalogueWeb().setDescriptionLongueCatWeb((String)value);
							}
						}
					}
					
			}
			return false;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	
	private void initEtatRapportUniteUI() {
//		boolean enabled = false;
//		enabled=taArticleDTOService.initEtatRapportUnite(modeEcran.dataSetEnModif(),taArticle,selectedTaArticleDTO);
//		vue.getTfCODE_UNITE2().setEditable(enabled);
//		initEtatRapportUniteSuite(taArticle.getTaRapportUnite()!=null && 
//				!taArticle.getTaRapportUnite().getTaUnite2().getCodeUnite().isEmpty());

	}
	
	private void initEtatRapportUniteSuite(boolean enabled) {
//		if(!enabled && modeEcran.dataSetEnModif()){
//			selectedTaArticleDTO.setRapport(null);
//			selectedTaArticleDTO.setNbDecimal(null);
//			vue.getTfDECIMAL().setText("");
//			vue.getTfRAPPORT().setText("");
//		}
//		vue.getTfRAPPORT().setEditable(enabled);
//		vue.getTfDECIMAL().setEditable(enabled);
//		vue.getCbSensRapport().setEnabled(enabled);
//		vue.getLaU1PhraseRapport().setVisible(enabled);
//		vue.getLaU2PhraseRapport().setVisible(enabled);
//		vue.getLaRapportPhraseRapport().setVisible(enabled);
//		vue.getLaEgalePhraseRapport().setVisible(enabled);
//		
//		boolean phraseVisible = false;
//		if(taArticle.getIdArticle()!=0 && !vue.getTfRAPPORT().getText().equals("")) {
//			//article enregistrer
//			phraseVisible = true;
//		}
//		
//		vue.getLaU1PhraseRapport().setVisible(phraseVisible);
//		vue.getLaU2PhraseRapport().setVisible(phraseVisible);
//		vue.getLaRapportPhraseRapport().setVisible(phraseVisible);
//		vue.getLaEgalePhraseRapport().setVisible(phraseVisible);

	}
	
	
	public List<TaFamille> familleArticleAutoComplete(String query) {
		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage("Query", query));
		if(query!=null && query.equals("")){
			taFamille=null;
		}
	        List<TaFamille> allValues = taFamilleService.selectAll();
	        List<TaFamille> filteredValues = new ArrayList<TaFamille>();
	         
	        for (int i = 0; i < allValues.size(); i++) {
	        	TaFamille civ = allValues.get(i);
	            if(query.equals("*") || civ.getCodeFamille().toLowerCase().contains(query.toLowerCase())) {
	                filteredValues.add(civ);
	            }
	        }
	         
	        return filteredValues;
		
    }
	
	public List<String> familleArticle() {

	        List<TaFamille> allValues = taFamilleService.selectAll();
	        List<String> filteredValues = new ArrayList<String>();
	        for (TaFamille l : allValues) {
	        	filteredValues.add(l.getCodeFamille());
			}
	         
	        return filteredValues;
		
    }
	public List<TaTvaDTO> tvaArticleAutoCompleteLight(String query) {
        List<TaTvaDTO> allValues = taTvaService.findByCodeLight(query.toUpperCase());
        List<TaTvaDTO> filteredValues = new ArrayList<TaTvaDTO>();
        TaTvaDTO civ = new TaTvaDTO();
        civ.setCodeTva(Const.C_AUCUN);
        filteredValues.add(civ);
        for (int i = 0; i < allValues.size(); i++) {
        	 civ = allValues.get(i);
            if(query.equals("*") || civ.getCodeTva().toUpperCase().contains(query.toUpperCase())) {
                filteredValues.add(civ);
            }
        }
        return filteredValues;
	}
	
	public List<TaTTvaDTO> ttvaArticleAutoCompleteLight(String query) {
        List<TaTTvaDTO> allValues = taTTvaService.findByCodeLight(query.toUpperCase());
        List<TaTTvaDTO> filteredValues = new ArrayList<TaTTvaDTO>();
        TaTTvaDTO civ = new TaTTvaDTO();
        civ.setCodeTTva(Const.C_AUCUN);
        filteredValues.add(civ);
        for (int i = 0; i < allValues.size(); i++) {
        	 civ = allValues.get(i);
            if(query.equals("*") || civ.getCodeTTva().toUpperCase().contains(query.toUpperCase())) {
                filteredValues.add(civ);
            }
        }
        return filteredValues;
	}
	
	// Dima - Début
	public List<TaFamilleDTO> familleArticleAutoCompleteLight(String query) {
	        List<TaFamilleDTO> allValues = taFamilleService.findByCodeLight(query.toUpperCase());
	        List<TaFamilleDTO> filteredValues = new ArrayList<TaFamilleDTO>();
	        TaFamilleDTO civ = new TaFamilleDTO();
//	        civ.setCodeFamille(Const.C_AUCUN); // il y en a pas besoin car on peut supprimer avec la croix
//	        filteredValues.add(civ);
	        for (int i = 0; i < allValues.size(); i++) {
	        	 civ = allValues.get(i);
	            if(query.equals("*") || civ.getCodeFamille().toUpperCase().contains(query.toUpperCase())) {
	                filteredValues.add(civ);
	            }
	        }
	        return filteredValues;
    }
	
	public List<TaMarqueArticleDTO> marqueAutoCompleteLight(String query) {
        List<TaMarqueArticleDTO> allValues = taMarqueArticleService.findByCodeLight(query.toUpperCase());
        List<TaMarqueArticleDTO> filteredValues = new ArrayList<TaMarqueArticleDTO>();
        TaMarqueArticleDTO  civ = new TaMarqueArticleDTO();
        civ.setCodeMarqueArticle(Const.C_AUCUN);
        filteredValues.add(civ);
        for (int i = 0; i < allValues.size(); i++) {
        	 civ = allValues.get(i);
            if(query.equals("*") || civ.getCodeMarqueArticle().toLowerCase().contains(query.toLowerCase())) {
                filteredValues.add(civ);
            }
        }
        return filteredValues;
    }
	
	public List<TaTiersDTO> founisseursAutoCompleteLight(String query) {
        List<TaTiersDTO> allValues = taFournisseurService.findByCodeLight(query.toUpperCase());
        List<TaTiersDTO> filteredValues = new ArrayList<TaTiersDTO>();
        TaTiersDTO  civ = new TaTiersDTO();
//        civ.setCodeTiers(Const.C_AUCUN);  // il y en a pas besoin car on peut supprimer avec la croix
//        filteredValues.add(civ);
        for (int i = 0; i < allValues.size(); i++) {
        	 civ = allValues.get(i);
            if(query.equals("*") || civ.getCodeTiers().toLowerCase().contains(query.toLowerCase())) {
                filteredValues.add(civ);
            }
        }
        return filteredValues;
    }
	
	
	public List<TaTitreTransportDTO> titreTransportAutoCompleteLight(String query) {
		List<TaTitreTransportDTO> allValues = taTitreTransportService.findByCodeLight(query.toUpperCase());

        List<TaTitreTransportDTO> filteredValues = new ArrayList<TaTitreTransportDTO>();
        TaTitreTransportDTO  civ = new TaTitreTransportDTO();
        civ.setCodeTitreTransport(Const.C_AUCUN);
//        filteredValues.add(civ);
        for (int i = 0; i < allValues.size(); i++) {
        	 civ = allValues.get(i);
            if(query.equals("*") || civ.getCodeTitreTransport().toLowerCase().contains(query.toLowerCase())) {
                filteredValues.add(civ);
            }
        }
        return filteredValues;
    }
	
	
	public List<TaUniteDTO> uniteAutoCompleteLightUniteSaisie(String query) {
		List<TaUniteDTO> allValues = null;
		if(taArticle.getTaUnite1()!=null)
		allValues = taUniteService.findByCodeLightUniteStock(taArticle.getTaUnite1().getCodeUnite(), query.toUpperCase());
		else if(taArticle.getTaUniteReference()!=null)
			allValues = taUniteService.findByCodeLightUniteStock(taArticle.getTaUniteReference().getCodeUnite(), query.toUpperCase());
		else allValues = taUniteService.findByCodeLight(query.toUpperCase());

        List<TaUniteDTO> filteredValues = new ArrayList<TaUniteDTO>();
        TaUniteDTO  civ = new TaUniteDTO();
        civ.setCodeUnite(Const.C_AUCUN);
//        filteredValues.add(civ);
        for (int i = 0; i < allValues.size(); i++) {
        	 civ = allValues.get(i);
            if(query.equals("*") || civ.getCodeUnite().toLowerCase().contains(query.toLowerCase())) {
                filteredValues.add(civ);
            }
        }
        return filteredValues;
    }
	
	public List<TaUniteDTO> uniteAutoCompleteLight(String query) {
		List<TaUniteDTO> allValues = null;
		if(taArticle.getTaUniteReference()==null ||  taArticle.getIdArticle()==0 || !articleEstUtilise()) {
			//l'unité de référence n'a pas encore été définie (cas d'une création d'article)
			allValues = taUniteService.findByCodeLight(query.toUpperCase());
		} else {
			allValues = taUniteService.findByCodeLightUniteStock(taArticle.getTaUniteReference().getCodeUnite(), query.toUpperCase());
		}
        List<TaUniteDTO> filteredValues = new ArrayList<TaUniteDTO>();
        TaUniteDTO  civ = new TaUniteDTO();
        civ.setCodeUnite(Const.C_AUCUN);
//        filteredValues.add(civ);
        for (int i = 0; i < allValues.size(); i++) {
        	 civ = allValues.get(i);
            if(query.equals("*") || civ.getCodeUnite().toLowerCase().contains(query.toLowerCase())) {
                filteredValues.add(civ);
            }
        }
        return filteredValues;
    }
	// Dima -  Fin

	public List<TaUnite> uniteAutoComplete(String query) {
        List<TaUnite> allValues = taUniteService.selectAll();
        List<TaUnite> filteredValues = new ArrayList<TaUnite>();
        for (int i = 0; i < allValues.size(); i++) {
        	TaUnite civ = allValues.get(i);
            if(query.equals("*") || civ.getCodeUnite().toLowerCase().contains(query.toLowerCase())) {
                filteredValues.add(civ);
            }
        }
         
        return filteredValues;
    }
	
	
	public void actDialogModifierUniteRef(ActionEvent actionEvent) {
		//Integer idArticle =  (Integer) actionEvent.getComponent().getAttributes().get("idArticle");
    
        Map<String,Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("draggable", true);
        options.put("resizable", false);
        options.put("contentHeight", 400);
//        options.put("modal", true);
        
        //Map<String,List<String>> params = null;
        Map<String,List<String>> params = new HashMap<String,List<String>>();
        List<String> list = new ArrayList<String>();
        list.add(LibConversion.integerToString(taArticle.getIdArticle()));
        params.put("idArticle", list);
       
//        PrimeFaces.current().dialog().openDynamic("solstyce/dialog_creer_controle_article", options, params);
        PrimeFaces.current().dialog().openDynamic("articles/dialog_modifier_unite_reference_article", options, params);
	}
	
	public void handleReturnDialogModifierUniteRef(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			if(!((TaUnite)event.getObject()).getCodeUnite().equals(taArticle.getTaUniteReference().getCodeUnite())) {
				//l'unité de référence a bien été changée => modifier l'unité dans les mouvement de cet article et recalculé les quantité
				taArticleService.modificationUniteDeReference(taArticle.getIdArticle(), ((TaUnite)event.getObject()).getIdUnite());
				//validateUIField(Const.C_CODE_UNITE_REFERENCE, ((TaUnite)event.getObject()).getCodeUnite());
				//taArticle = taArticleService.findById(taArticle.getIdArticle());
				updateTabs();
			}
		}
	}
	
	///////////////////////////////////////////////////////////
	///////////           ONGLET CONTROLE           ///////////
	///////////////////////////////////////////////////////////
	
	public void actDialogCreerControleArticle(ActionEvent actionEvent) {
		actDialogControle(actionEvent, EnumModeObjet.C_MO_INSERTION);
	}
	
	public void actDialogModifierControleArticle(ActionEvent actionEvent) {
		actDialogControle(actionEvent, EnumModeObjet.C_MO_EDITION);
	}
	
	public void actDialogControle(ActionEvent actionEvent, EnumModeObjet mode) {
		
//		PrimeFaces.current().dialog().openDynamic("aide");
		
		Integer idArticle =  (Integer) actionEvent.getComponent().getAttributes().get("idArticle");
		Integer idConformite =  (Integer) actionEvent.getComponent().getAttributes().get("idConformite");
    
        Map<String,Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("draggable", true);
        options.put("resizable", false);
        options.put("contentHeight", 400);
//        options.put("modal", true);
        
        //Map<String,List<String>> params = null;
        Map<String,List<String>> params = new HashMap<String,List<String>>();
        List<String> list = new ArrayList<String>();
        list.add(modeEcran.modeString(mode));
        params.put("modeEcranDefaut", list);
        List<String> list2 = new ArrayList<String>();
//        list2.add(LibConversion.integerToString(selectedTaArticleDTO.getId()));
        list2.add(LibConversion.integerToString(idArticle));
        params.put("idEntityArticle", list2);
        List<String> list3 = new ArrayList<String>();
        list3.add("type_controle");
        params.put("type", list3);
//        if(listeConformiteController.getSelection()!=null) {
	        List<String> list4 = new ArrayList<String>();
//	        list4.add(LibConversion.integerToString(listeConformiteController.getSelection().getIdConformite()));
	        list4.add(LibConversion.integerToString(idConformite));
	        params.put("idEntityConformite", list4);
//        }

        
        PrimeFaces.current().dialog().openDynamic("solstyce/dialog_creer_controle_article", options, params);
		
//		FacesContext context = FacesContext.getCurrentInstance();  
//		context.addMessage(null, new FacesMessage("Tiers", "actAbout"));
	}
	
	public void actDialogCreerBaremeControle(ActionEvent actionEvent) {
		actDialogBareme(actionEvent, EnumModeObjet.C_MO_INSERTION);
	}
	
	public void actDialogModifierBaremeControle(ActionEvent actionEvent) {
		actDialogBareme(actionEvent, EnumModeObjet.C_MO_EDITION);
	}
	
	public void actDialogBareme(ActionEvent actionEvent, EnumModeObjet mode) {
		
//		PrimeFaces.current().dialog().openDynamic("aide");
		
		Integer idBareme =  (Integer) actionEvent.getComponent().getAttributes().get("idBareme");
		Integer idConformite =  (Integer) actionEvent.getComponent().getAttributes().get("idConformite");
    
        Map<String,Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("draggable", true);
        options.put("resizable", false);
        options.put("contentHeight", 400);
        
        //Map<String,List<String>> params = null;
        Map<String,List<String>> params = new HashMap<String,List<String>>();
        List<String> list = new ArrayList<String>();
        list.add(modeEcran.modeString(mode));
        params.put("modeEcranDefaut", list);
        List<String> list2 = new ArrayList<String>();
        //list2.add(LibConversion.integerToString(listeConformiteController.getSelection().getIdConformite()));
        list2.add(LibConversion.integerToString(idConformite));
        params.put("idEntityConformite", list2);
        List<String> list3 = new ArrayList<String>();
        list3.add("type_bareme");
        params.put("type", list3);
//        if(listeConformiteController.getSelectionBareme()!=null) {
	        List<String> list4 = new ArrayList<String>();
	        //list4.add(LibConversion.integerToString(listeConformiteController.getSelectionBareme().getIdBareme()));
	        list4.add(LibConversion.integerToString(idBareme));
	        params.put("idEntityBareme", list4);
//        }
        
        PrimeFaces.current().dialog().openDynamic("solstyce/dialog_creer_bareme_article", options, params);
		
//		FacesContext context = FacesContext.getCurrentInstance();  
//		context.addMessage(null, new FacesMessage("Tiers", "actAbout")); 
	}
	
	public void handleReturnDialogCreerControleArticle(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			try {
//				taArticle = ;
				listeConformiteController.setMasterEntity(taArticleService.findById( ((TaConformite)event.getObject()).getTaArticle().getIdArticle()));
				listeConformiteController.refresh();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	public void handleReturnDialogCreerBaremeControle(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			try {
				listeConformiteController.setMasterEntity(taArticleService.findById( ((TaBareme)event.getObject()).getTaConformite().getTaArticle().getIdArticle()));
				listeConformiteController.refresh();
			} catch (FinderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	//////////////////////////////////////////////////////////
	///////////           ONGLET RECETTE           ///////////
	//////////////////////////////////////////////////////////
	
	public void actInsererMPRecette(ActionEvent actionEvent) {
		TaLRecetteDTOJSF nouvelleLigne = new TaLRecetteDTOJSF();
		nouvelleLigne.setDto(new TaLRecetteDTO());
		nouvelleLigne.getDto().setNumLigneLDocument(valuesMPRecette.size()+1);
		
		if(taArticle.getTaRecetteArticle()==null) {
			taArticle.setTaRecetteArticle(new TaRecette());
			taArticle.getTaRecetteArticle().setTaArticle(taArticle);
		}
		taLRecette = new TaLRecette(); 				
		
		taArticle.getTaRecetteArticle().getLignes().add(taLRecette);
		taLRecette.setTaRecette(taArticle.getTaRecetteArticle());

//		selectedTaFabricationDTO.setNumLigneLDocument(masterEntityLigneMP.getNumLigneLDocument());
		
//		masterEntityLigneMP.initialiseLigne(true);
		
//		initOrigineMouvement(masterEntityLigneMP);

		valuesMPRecette.add(nouvelleLigne);
		
		selectedTaMPRecette = nouvelleLigne;

		modeEcranRecette.setMode(EnumModeObjet.C_MO_INSERTION);

		if(ConstWeb.DEBUG) {
		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage("Article", "actInsererMPRecette"));
		}
	}
	
	public void actSupprimerMPRecette(ActionEvent actionEvent) {
		//		List<TaLRecette> l = taArticle.getTaRecetteArticle().getLignes();
		//		Boolean trouve = l.remove(selectedTaMPRecette);
		//		if(trouve){
		//			taArticle.getTaRecetteArticle().setLignes(l);
		//		}
		if(valuesMPRecette.size()>0 && selectedTaMPRecette!=null && valuesMPRecette.contains(selectedTaMPRecette)){
			TaLRecette l = null;
			Boolean trouve = valuesMPRecette.remove(selectedTaMPRecette);
			if(trouve){
				for (TaLRecette var : taArticle.getTaRecetteArticle().getLignes()) {
					if(selectedTaMPRecette.getDto().getIdLDocument()!=null && (var.getIdLRecette()==selectedTaMPRecette.getDto().getIdLDocument())) {
						l = var;
					}
				}
				if(l!=null) {
					taArticle.getTaRecetteArticle().getLignes().remove(l);
				}

			}
			modeEcranRecette.setMode(EnumModeObjet.C_MO_EDITION);
		}
	}
	
	public void onRowEditMP(RowEditEvent event) {
		try {
			selectedTaMPRecette.updateDTO();
			if(selectedTaMPRecette.getDto().getIdLDocument()!=null &&  selectedTaMPRecette.getDto().getIdLDocument()!=0) {
				taLRecette=rechercheLigneMP(selectedTaMPRecette.getDto().getIdLDocument());
			}
//			validateLigneFabricationAvantEnregistrement(selectedTaMPRecette);
			actEnregistrerMatierePremiere();
			setInsertAutoMPRecette(modeEcranRecette.getMode()==EnumModeObjet.C_MO_INSERTION);
			if(modeEcranRecette.getMode()==EnumModeObjet.C_MO_CONSULTATION)//si en consultation,veut dire que n'est pas passer par insertion donc
				// on passe en modif pour la gestion des boutons
				modeEcranRecette.setMode(EnumModeObjet.C_MO_EDITION);

		} catch (Exception e) {
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Article", e.getMessage()));	
			context.validationFailed();
			setInsertAutoMPRecette(false);
		}
	}
	
	public void actEnregistrerMatierePremiere() {

		try {
			selectedTaMPRecette.updateDTO();
			
			taLRecette.setTaArticle(selectedTaMPRecette.getTaArticle());
//			taLRecette.setTaEntrepot(selectedTaMPRecette.getTaEntrepot());
//			taLRecette.setTaLot(selectedTaMPRecette.getTaLot());
			
			LgrDozerMapper<TaLRecetteDTO,TaLRecette> mapper = new LgrDozerMapper<TaLRecetteDTO,TaLRecette>();
			mapper.map((TaLRecetteDTO) selectedTaMPRecette.getDto(),taLRecette);

			
//			taArticle.getTaRecetteArticle().enregistreLigne(taLRecette);
			if(!taArticle.getTaRecetteArticle().getLignes().contains(taLRecette)) {
				taArticle.getTaRecetteArticle().getLignes().add(taLRecette);
			}
			if(!taArticle.getTaRecetteArticle().getLignes().contains(taLRecette)) {
				taArticle.getTaRecetteArticle().getLignes().add(taLRecette);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Fabrication", "actEnregisterMatierePremiere"));
		}

	}
	
	public void actEnregistrerRecette(ActionEvent actionEvent) {
		//supression des lignes vides
		List<TaLRecette> listeLigneVide = new ArrayList<TaLRecette>(); 
		for (TaLRecette ligne : taArticle.getTaRecetteArticle().getLignes()) {
			//ligne.setLegrain(false);
			if(ligne.ligneEstVide()) {
				listeLigneVide.add(ligne);
			}
		}
		for (TaLRecette taLBonReception : listeLigneVide) {
			taArticle.getTaRecetteArticle().getLignes().remove(taLBonReception);
		}
		actEnregistrer(null);
		valuesMPRecette = IHMmodelRecette();
		if(valuesMPRecette!=null && valuesMPRecette.size()>0)
			selectionLigneMP(valuesMPRecette.get(0));
		modeEcranRecette.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}
	
	public void actAnnulerRecette(ActionEvent actionEvent) {
		//supression des lignes vides
		try {
			taArticle=taArticleService.findById(taArticle.getIdArticle());
		} catch (FinderException e) {
			
		}
		List<TaLRecette> listeLigneVide = new ArrayList<TaLRecette>(); 
		for (TaLRecette ligne : taArticle.getTaRecetteArticle().getLignes()) {
			//ligne.setLegrain(false);
			if(ligne.ligneEstVide()) {
				listeLigneVide.add(ligne);
			}
		}
		for (TaLRecette taLBonReception : listeLigneVide) {
			taArticle.getTaRecetteArticle().getLignes().remove(taLBonReception);
		}		
		valuesMPRecette = IHMmodelRecette();
		if(valuesMPRecette.size()>0)
			selectionLigneMP(valuesMPRecette.get(0));
		
		modeEcranRecette.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}

	public void onRowCancelMP(RowEditEvent event) {
		List<TaLRecette> lmp = taArticle.getTaRecetteArticle().getLignes();
		if(taLRecette == null ){
			lmp.add(taLRecette);
		}
		if(taLRecette.ligneEstVide()||taLRecette.getIdLRecette()==0)
			lmp.remove(taLRecette);
		
		taArticle.getTaRecetteArticle().setLignes(lmp);
		
		valuesMPRecette = IHMmodelRecette();
		if(valuesMPRecette.size()>0)
			selectionLigneMP(valuesMPRecette.get(0));

		setInsertAutoMPRecette(false);
//		modeEcranRecette.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}

	public void onRowEditInitMP(RowEditEvent event) {
		AjaxBehaviorEvent evt = (AjaxBehaviorEvent)event;
		DataTable table = (DataTable) evt.getSource();
		int activeRow = table.getRowIndex()+1;
		if(table.getRowCount() == activeRow) {
			//derniere ligne
			setInsertAutoMPRecette(modeEcranRecette.getMode()==EnumModeObjet.C_MO_INSERTION);
		} else {
			setInsertAutoMPRecette(false);
		}
	}
	
	public void onRowSelectLigneMP(SelectEvent event) {  
		selectionLigneMP((TaLRecetteDTOJSF) event.getObject());		
	}
	
	public void selectionLigneMP(TaLRecetteDTOJSF ligne){
		selectedTaMPRecette=ligne;
		if(taArticle.getTaRecetteArticle().getIdRecette()!=0 && selectedTaMPRecette.getDto().getIdLDocument()!=null)
			taLRecette=rechercheLigneMP(selectedTaMPRecette.getDto().getIdLDocument());
	}
	
	public TaLRecette rechercheLigneMP(int id){
		TaLRecette obj = taLRecette;
		for (TaLRecette ligne : taArticle.getTaRecetteArticle().getLignes()) {
			if(ligne.getIdLRecette()==id)
				obj=ligne;
		}
		return obj;
	}
	
	public List<TaLRecetteDTOJSF> IHMmodelRecette() {
		LinkedList<TaLRecette> ldao = new LinkedList<TaLRecette>();
		LinkedList<TaLRecetteDTOJSF> lswt = new LinkedList<TaLRecetteDTOJSF>();

		if(taArticle!=null && taArticle.getTaRecetteArticle()!=null && taArticle.getTaRecetteArticle().getLignes() !=null && !taArticle.getTaRecetteArticle().getLignes().isEmpty()) {

			ldao.addAll(taArticle.getTaRecetteArticle().getLignes());

			LgrDozerMapper<TaLRecette,TaLRecetteDTO> mapper = new LgrDozerMapper<TaLRecette,TaLRecetteDTO>();
			TaLRecetteDTO dto = null;
			for (TaLRecette ligne : ldao) {
				TaLRecetteDTOJSF t = new TaLRecetteDTOJSF();
//				try {
//					TaLot lot = taLotService.findByCode(o.getNumLot());
//					o.setTaLot(lot);
				dto = (TaLRecetteDTO) mapper.map(ligne, TaLRecetteDTO.class);
				t.setDto(dto);
//				t.setTaLot(ligne.getTaLot());
//				t.getDto().setLibelleLot(ligne.getLibLDocument());
				t.getDto().setIdLDocument(ligne.getIdLRecette());

//				if(ligne.getTaEntrepot()!=null) {
//					t.getDto().setCodeEntrepot(ligne.getTaEntrepot().getCodeEntrepot());
//				}
//				t.setTaLot(lot);
				t.setTaArticle(ligne.getTaArticle());
				if(ligne.getTaArticle()!=null) {
					t.setTaRapport(ligne.getTaArticle().getTaRapportUnite());
				}
				if(ligne.getU1LDocument()!=null) {
					t.setTaUnite1(new TaUnite());
					t.getTaUnite1().setCodeUnite(ligne.getU1LDocument());
				}
				if(ligne.getU2LDocument()!=null) {
					t.setTaUnite2(new TaUnite());
					t.getTaUnite2().setCodeUnite(ligne.getU2LDocument());
				}
//				t.setTaEntrepot(ligne.getTaEntrepot());
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
				t.updateDTO();
				lswt.add(t);
			}

		}
		return lswt;
	}
	
	public void actAutoInsererLigneMP(ActionEvent actionEvent) /*throws Exception*/ {

		if(insertAutoMPRecette) {
			actInsererMPRecette(actionEvent);
			
			String oncomplete="jQuery('.myclassMP .ui-datatable-data tr').last().find('span.ui-icon-pencil').each(function(){jQuery(this).click()});";
			PrimeFaces.current().executeScript(oncomplete);
		} else {
//			throw new Exception();
		}
	}
	
	public void actDialogParamRecetteFab(ActionEvent actionEvent) {
		
        Map<String,Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("draggable", true);
        options.put("resizable", false);
        options.put("contentHeight", 300);
        options.put("modal", true);
        
        Map<String,List<String>> params = new HashMap<String,List<String>>();
        List<String> list = new ArrayList<String>();
        list.add(LibConversion.integerToString(taArticle.getTaRecetteArticle().getIdRecette()));
        params.put("idRecetteFabrication", list);
        
        PrimeFaces.current().dialog().openDynamic("solstyce/dialog_param_modele_fabrication", options, params);

	}

	public TaUnite getTaUnite1() {
		return taUnite1;
	}

	public void setTaUnite1(TaUnite taUnite1) {
		this.taUnite1 = taUnite1;
	}

	public TaUnite getTaUnite2() {
		return taUnite2;
	}

	public void setTaUnite2(TaUnite taUnite2) {
		this.taUnite2 = taUnite2;
	}

	public TaFamille getTaFamille() {
		return taFamille;
	}

	public void setTaFamille(TaFamille taFamille) {
		this.taFamille = taFamille;
	}

	public Boolean getAfficheMatierePremiere() {
		return afficheMatierePremiere;
	}

	public void setAfficheMatierePremiere(Boolean afficheMatierePremiere) {
		this.afficheMatierePremiere = afficheMatierePremiere;
	}

	public Boolean getAfficheProduitFini() {
		return afficheProduitFini;
	}

	public void setAfficheProduitFini(Boolean afficheProduitFini) {
		this.afficheProduitFini = afficheProduitFini;
	}

	public EtiquetteCodeBarreBean getEtiquetteCodeBarreBean() {
		return etiquetteCodeBarreBean;
	}

	public void setEtiquetteCodeBarreBean(
			EtiquetteCodeBarreBean etiquetteCodeBarreBean) {
		this.etiquetteCodeBarreBean = etiquetteCodeBarreBean;
	}

	public String getUrlPourEdition() {
		return urlPourEdition;
	}

	public void setUrlPourEdition(String urlPourEdition) {
		this.urlPourEdition = urlPourEdition;
	}

	public ListeConformiteController getListeConformiteController() {
		return listeConformiteController;
	}

	public void setListeConformiteController(
			ListeConformiteController listeConformiteController) {
		this.listeConformiteController = listeConformiteController;
	}

	public TabViewsBean getTabViews() {
		return tabViews;
	}

	public void setTabViews(TabViewsBean tabViews) {
		this.tabViews = tabViews;
	}

	public TabView getTabViewArticle() {
		return tabViewArticle;
	}

	public void setTabViewArticle(TabView tabViewArticle) {
		this.tabViewArticle = tabViewArticle;
	}

	public boolean isInsertAutoMPRecette() {
		return insertAutoMPRecette;
	}

	public void setInsertAutoMPRecette(boolean insertAutoMPRecette) {
		this.insertAutoMPRecette = insertAutoMPRecette;
	}

	public List<TaLRecetteDTOJSF> getValuesMPRecette() {
		return valuesMPRecette;
	}

	public void setValuesMPRecette(List<TaLRecetteDTOJSF> valuesMPRecette) {
		this.valuesMPRecette = valuesMPRecette;
	}

	public TaLRecetteDTOJSF getSelectedTaMPRecette() {
		return selectedTaMPRecette;
	}

	public void setSelectedTaMPRecette(TaLRecetteDTOJSF selectedTaMPRecette) {
		this.selectedTaMPRecette = selectedTaMPRecette;
	}

	public TaLRecette getTaLRecette() {
		return taLRecette;
	}

	public void setTaLRecette(TaLRecette taLRecette) {
		this.taLRecette = taLRecette;
	}

//	public Boolean getAfficheTous() {
//		return afficheTous;
//	}
//
//	public void setAfficheTous(Boolean afficheTous) {
//		this.afficheTous = afficheTous;
//	}
	
	public void actDialogCodeTVA(ActionEvent actionEvent) {
		
//		PrimeFaces.current().dialog().openDynamic("aide");
    
        Map<String,Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("draggable", true);
        options.put("resizable", false);
        options.put("contentHeight", 540);
        options.put("modal", true);
        
        //Map<String,List<String>> params = null;
        Map<String,List<String>> params = new HashMap<String,List<String>>();
        List<String> list = new ArrayList<String>();
        list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
        params.put("modeEcranDefaut", list);
        
        PrimeFaces.current().dialog().openDynamic("articles/dialog_type_code_tva", options, params);
			    
	}
	
	public void handleReturnDialogCodeTVA(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			taTva = (TaTva) event.getObject();
			try {
				taTvaDTO = taTvaService.findByCodeDTO(taTva.getCodeTva());
			} catch (FinderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void actDialogFamille(ActionEvent actionEvent) {
		
//		PrimeFaces.current().dialog().openDynamic("aide");
    
        Map<String,Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("draggable", true);
        options.put("resizable", false);
        options.put("contentHeight", 540);
        options.put("modal", true);
        
        //Map<String,List<String>> params = null;
        Map<String,List<String>> params = new HashMap<String,List<String>>();
        List<String> list = new ArrayList<String>();
        list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
        params.put("modeEcranDefaut", list);
        
        PrimeFaces.current().dialog().openDynamic("articles/dialog_type_famille_article", options, params);
			    
	}
	
	public void handleReturnDialogFamille(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			taFamille = (TaFamille) event.getObject();
			try {
				taFamilleDTO = taFamilleService.findByCodeDTO(taFamille.getCodeFamille());
			} catch (FinderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	


	
	
	public void actDialogUnite(ActionEvent actionEvent) {
		
//		PrimeFaces.current().dialog().openDynamic("aide");
    
        Map<String,Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("draggable", true);
        options.put("resizable", false);
        options.put("contentHeight", 540);
        options.put("modal", true);
        
        //Map<String,List<String>> params = null;
        Map<String,List<String>> params = new HashMap<String,List<String>>();
        List<String> list = new ArrayList<String>();
        list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
        params.put("modeEcranDefaut", list);
        
        PrimeFaces.current().dialog().openDynamic("articles/dialog_type_unite", options, params);
			    
	}
	
	public void actDialogMarque(ActionEvent actionEvent) {
		
    
        Map<String,Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("draggable", true);
        options.put("resizable", false);
        options.put("contentHeight", 540);
        options.put("modal", true);
        
        Map<String,List<String>> params = new HashMap<String,List<String>>();
        List<String> list = new ArrayList<String>();
        list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
        params.put("modeEcranDefaut", list);
        
        PrimeFaces.current().dialog().openDynamic("articles/dialog_marque", options, params);
			    
	}
	
	public void handleReturnDialogUnite(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			taUnite1 = (TaUnite) event.getObject();
			try {
				taUnite1DTO = taUniteService.findByCodeDTO(taUnite1.getCodeUnite());
			} catch (FinderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void handleReturnDialogUniteReference(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			taUniteReference = (TaUnite) event.getObject();
			try {
				taUniteReferenceDTO = taUniteService.findByCodeDTO(taUniteReference.getCodeUnite());
			} catch (FinderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	public void handleReturnDialogUniteSaisie(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			taUniteSaisie = (TaUnite) event.getObject();
			try {
				taUniteSaisieDTO = taUniteService.findByCodeDTO(taUniteSaisie.getCodeUnite());
			} catch (FinderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void handleReturnDialogMarque(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			taMarqueArticle = (TaMarqueArticle) event.getObject();
			try {
				taMarqueArticleDTO = taMarqueArticleService.findByCodeDTO(taMarqueArticle.getCodeMarqueArticle());
			} catch (FinderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void handleReturnDialogUnite2(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			taUnite2 = (TaUnite) event.getObject();
			try {
				taUnite2DTO = taUniteService.findByCodeDTO(taUnite2.getCodeUnite());
			} catch (FinderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public Boolean getAfficheTous() {
		return afficheTous;
	}

	public void setAfficheTous(Boolean afficheTous) {
		this.afficheTous = afficheTous;
	}

	public String getAffiche() {
		return affiche;
	}

	public void setAffiche(String affiche) {
		this.affiche = affiche;
		setAfficheMatierePremiere(false);
		setAfficheProduitFini(false);
		setAfficheLesDeux(false);
		setAfficheTous(false);
		setAfficheUnOuAutre(false);
		if(affiche.equals(C_MATIERE_PREMIERE))setAfficheMatierePremiere(true);
		if(affiche.equals(C_PRODUIT_FINI))setAfficheProduitFini(true);
		if(affiche.equals(C_PF_MP))setAfficheLesDeux(true);
		if(affiche.equals(C_TOUS))setAfficheTous(true);
		if(affiche.equals(C_PF_OU_MP))setAfficheUnOuAutre(true);
	}

	public ModeObjetEcranServeur getModeEcran() {
		return modeEcran;
	}

	public void setModeEcran(ModeObjetEcranServeur modeEcran) {
		this.modeEcran = modeEcran;
	}

	public String getParamId() {
		return paramId;
	}

	public void setParamId(String paramId) {
		this.paramId = paramId;
	}

	public TaTvaDTO getTaTvaDTO() {
		return taTvaDTO;
	}

	public void setTaTvaDTO(TaTvaDTO taTvaDTO) {
		this.taTvaDTO = taTvaDTO;
	}

	public TaTTvaDTO getTaTTvaDTO() {
		return taTTvaDTO;
	}

	public void setTaTTvaDTO(TaTTvaDTO taTTvaDTO) {
		this.taTTvaDTO = taTTvaDTO;
	}

	public Boolean getAfficheLesDeux() {
		return afficheLesDeux;
	}

	public void setAfficheLesDeux(Boolean afficheLesDeux) {
		this.afficheLesDeux = afficheLesDeux;
	}

	public TaUnite getTaUniteStock() {
		return taUniteStock;
	}

	public void setTaUniteStock(TaUnite taUniteStock) {
		this.taUniteStock = taUniteStock;
	}

	public TaUniteDTO getTaUniteStockDTO() {
		return taUniteStockDTO;
	}

	public void setTaUniteStockDTO(TaUniteDTO taUniteStockDTO) {
		this.taUniteStockDTO = taUniteStockDTO;
	}

	public TaMarqueArticle getTaMarqueArticle() {
		return taMarqueArticle;
	}

	public void setTaMarqueArticle(TaMarqueArticle taMarqueArticle) {
		this.taMarqueArticle = taMarqueArticle;
	}

	public TaMarqueArticleDTO getTaMarqueArticleDTO() {
		return taMarqueArticleDTO;
	}

	public void setTaMarqueArticleDTO(TaMarqueArticleDTO taMarqueArticleDTO) {
		this.taMarqueArticleDTO = taMarqueArticleDTO;
	}

//	public List<TaFamilleDTO> getTaFamilleDTOs() {
//		return taFamilleDTOs;
//	}
//
//	public void setTaFamilleDTOs(List<TaFamilleDTO> taFamilleDTOs) {
//		this.taFamilleDTOs = taFamilleDTOs;
//	}

	public List<TaFamille> getTaFamilles() {
		return taFamilles;
	}

	public void setTaFamilles(List<TaFamille> taFamilles) {
		this.taFamilles = taFamilles;
	}

	public TaUnite getTaUniteReference() {
		return taUniteReference;
	}

	public void setTaUniteReference(TaUnite taUniteReference) {
		this.taUniteReference = taUniteReference;
	}

	public TaUniteDTO getTaUniteReferenceDTO() {
		return taUniteReferenceDTO;
	}

	public void setTaUniteReferenceDTO(TaUniteDTO taUniteReferenceDTO) {
		this.taUniteReferenceDTO = taUniteReferenceDTO;
	}

	public TaArticle getTaArticle() {
		return taArticle;
	}

	public void setTaArticle(TaArticle taArticle) {
		this.taArticle = taArticle;
	}
	

	public TaArticleDTO rempliDTO(){
		if(taArticle!=null){			
			try {
				taArticle=taArticleService.findByCode(taArticle.getCodeArticle());
				mapperModelToUI.map(taArticle, selectedTaArticleDTO);
				if(values.contains(selectedTaArticleDTO))values.add(selectedTaArticleDTO);
			} catch (FinderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return selectedTaArticleDTO;
	}

	public TaTiersDTO getTaFournisseurDTO() {
		return taFournisseurDTO;
	}

	public void setTaFournisseurDTO(TaTiersDTO taFournisseurDTO) {
		this.taFournisseurDTO = taFournisseurDTO;
	}

	public List<TaTiers> getTaFournisseurs() {
		return taFournisseurs;
	}

	public void setTaFournisseurs(List<TaTiers> taFournisseurs) {
		this.taFournisseurs = taFournisseurs;
	}

//	public List<TaTiersDTO> getTaFournisseurDTOs() {
//		return taFournisseurDTOs;
//	}
//
//	public void setTaFournisseurDTOs(List<TaTiersDTO> taFournisseurDTOs) {
//		this.taFournisseurDTOs = taFournisseurDTOs;
//	}

	public TaTiers getTaFournisseur() {
		return taFournisseur;
	}

	public void setTaFournisseur(TaTiers taFournisseur) {
		this.taFournisseur = taFournisseur;
	}

	public Boolean getAfficheUnOuAutre() {
		return afficheUnOuAutre;
	}

	public void setAfficheUnOuAutre(Boolean afficheUnOuAutre) {
		this.afficheUnOuAutre = afficheUnOuAutre;
	}

	public Boolean getAfficheInactif() {
		return afficheInactif;
	}

	public void setAfficheInactif(Boolean afficheInactif) {
		this.afficheInactif = afficheInactif;
	}

	public List<String> getListeTaFamille() {
		return listeTaFamille;
	}

	public void setListeTaFamille(List<String> listeTaFamille) {
		this.listeTaFamille = listeTaFamille;
	}
	
	public StreamedContent getExportationArticles() {
		File f = null;
		InputStream stream = null;
		try {	
			f = taArticleService.exportToCSV(values);
			stream = new FileInputStream(f); 
		} catch(Exception e) {
			e.printStackTrace();
		}
		exportation = new DefaultStreamedContent(stream,null,"articles.csv");
		
		return exportation;
	}

	public BigDecimal getMontantCaHt() {
		return montantCaHt;
	}

	public void setMontantCaHt(BigDecimal montantCaHt) {
		this.montantCaHt = montantCaHt;
	}

	public TarifArticlesController getTarifArticlesController() {
		return tarifArticlesController;
	}

	public void setTarifArticlesController(TarifArticlesController tarifArticlesController) {
		this.tarifArticlesController = tarifArticlesController;
	}

	public RefArticleFournisseurController getRefArticleFournisseurController() {
		return refArticleFournisseurController;
	}

	public void setRefArticleFournisseurController(RefArticleFournisseurController refArticleFournisseurController) {
		this.refArticleFournisseurController = refArticleFournisseurController;
	}

	private  void decodeEan(String codeBarre){
		Map<String, String> retour;
		try {
			retour = taParamEanService.decodeEan(codeBarre);

		
		retour.values();
		String messageRetour = "";
		for (String key : retour.keySet()) {
			messageRetour+=key+"@"+retour.get(key)+"---";
		}


		PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage("code barre", messageRetour));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage(FacesMessage.SEVERITY_ERROR,"code barre invalide", e.getMessage()));			
		}
	}
	
	private  void encodeEan(Map<String, Object> codeBarre){
		String retour;
		try {
			retour = taParamEanService.encodeEan(codeBarre);

		String messageRetour = retour;

		PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage("code barre", messageRetour));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage(FacesMessage.SEVERITY_ERROR,"code barre invalide", e.getMessage()));			
		}
	}
	
	public void actDecoderEAN128(ActionEvent actionEvent) {
		decodeEan(selectedTaArticleDTO.getCodeBarre());
	}
	
	public void actEncoderEAN128(ActionEvent actionEvent) {
////		Map<String, Object> liste = new LinkedHashMap<String, Object>();
////		liste.put("02","1"+selectedTaArticleDTO.getCodeBarre());
////		liste.put("37",new Date());
////		liste.put("10","Lot10%¨(df");
////		encodeEan(liste);
		
//		ou exemple de récupération d'un ean13Article à partir d'un 128
		
//		try {
//			Map<String, String> liste =taParamEan128Service.decodeEan128(selectedTaArticleDTO.getCodeBarre());
//			if(liste!=null && !liste.isEmpty())
//			String retour=taParamEan128Service.recupEan13SurGtin14(liste.get("02"));
		
//		RequestContext context = RequestContext.getCurrentInstance();
//		PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage("code barre", retour));
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	public DashBoardParArticleController getDashBoardParArticleController() {
		return dashBoardParArticleController;
	}

	public void setDashBoardParArticleController(DashBoardParArticleController dashBoardParArticleController) {
		this.dashBoardParArticleController = dashBoardParArticleController;
	}

	public String getWV_TABVIEW_ARTICLE() {
		return WV_TABVIEW_ARTICLE;
	}

	public String getCLASS_CSS_TAB_FICHE_ARTICLE() {
		return CLASS_CSS_TAB_FICHE_ARTICLE;
	}

	public String getCLASS_CSS_TAB_DASH_ARTICLE() {
		return CLASS_CSS_TAB_DASH_ARTICLE;
	}

	public boolean getAffiche_filtre_grille() {
		return affiche_filtre_grille;
	}

	public void setAffiche_filtre_grille(boolean affiche_filtre_grille) {
		this.affiche_filtre_grille = affiche_filtre_grille;
	}

	public BigDecimal getQuantiteTotaleEnStock() {
		return quantiteTotaleEnStock;
	}

	public void setQuantiteTotaleEnStock(BigDecimal quantiteTotaleEnStock) {
		this.quantiteTotaleEnStock = quantiteTotaleEnStock;
	}

	public String getQuantiteTotaleEnStockUnite() {
		return quantiteTotaleEnStockUnite;
	}

	public void setQuantiteTotaleEnStockUnite(String quantiteTotaleEnStockUnite) {
		this.quantiteTotaleEnStockUnite = quantiteTotaleEnStockUnite;
	}

	public String getCssQuantiteTotaleEnStock() {
		return cssQuantiteTotaleEnStock;
	}

	public void setCssQuantiteTotaleEnStock(String cssQuantiteTotaleEnStock) {
		this.cssQuantiteTotaleEnStock = cssQuantiteTotaleEnStock;
	}


	
	
	public void actDuplicationArticledialog(ActionEvent actionEvent) {
		
//		PrimeFaces.current().dialog().openDynamic("aide");
    
        Map<String,Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("draggable", true);
        options.put("resizable", false);
        options.put("contentHeight", 540);
        options.put("modal", true);
        
        //Map<String,List<String>> params = null;
        Map<String,List<String>> params = new HashMap<String,List<String>>();
        List<String> list = new ArrayList<String>();
        list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
        List<String> listArticle = new ArrayList<String>();
        listArticle.add(selectedTaArticleDTO.getCodeArticle());
        params.put("modeEcranDefaut", list);
        params.put("codeArticle", listArticle);
        
        PrimeFaces.current().dialog().openDynamic("articles/duplication_article", options, params);
			    
	}
	
	public void handleReturnDialogDuplicationArticle(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			param = (ParamDuplicationArticle) event.getObject();
			try {
				if(selectedTaArticleDTO!=null) {
					taArticle=taArticleService.findByCodeAvecConformite(selectedTaArticleDTO.getCodeArticle());
					if(taArticle!=null) {
						articleDuplique=(TaArticle) taArticle.clone();
						
						
						for (TaConformite l : articleDuplique.getTaConformites()) {
							l.setCode(taConformiteService.genereCode(null));
						}
						if(!param.getReprisePrix()) {
							articleDuplique.getTaPrixes().clear();
							articleDuplique.setTaPrix(null);
						}
						if(!param.getRepriseControls()) {
							articleDuplique.getTaConformites().clear();
						}
						if(!param.getRepriseRecettes()) {
							articleDuplique.setTaRecetteArticle(null);
						}
						if(!param.getRepriseFournisseurs()) {
							articleDuplique.getTaFournisseurs().clear();
						}
						if(!param.getRepriseFamilles()) {
							articleDuplique.getTaFamilles().clear();
						}
						actInserer(null);
						selectedTaArticleDTOs=null;
						taArticle=articleDuplique;
						if(taArticle!=null){
							mapperModelToUI.map(taArticle, selectedTaArticleDTO);
						}
						updateTabs();
						if(taArticle.getTaRTaTitreTransport()!=null) {
							taTitreTransportDTO = mapperModelToUITitreTransport.map(taArticle.getTaRTaTitreTransport().getTaTitreTransport(),TaTitreTransportDTO.class);
						}
//						autoCompleteMapDTOtoUI();
						
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public TaTitreTransport getTaTitreTransport() {
		return taTitreTransport;
	}

	public void setTaTitreTransport(TaTitreTransport taTitreTransport) {
		this.taTitreTransport = taTitreTransport;
	}

	public TaRTaTitreTransport getTaRTaTitreTransport() {
		return taRTaTitreTransport;
	}

	public void setTaRTaTitreTransport(TaRTaTitreTransport taRTaTitreTransport) {
		this.taRTaTitreTransport = taRTaTitreTransport;
	}

	public TaTitreTransportDTO getTaTitreTransportDTO() {
		return taTitreTransportDTO;
	}

	public void setTaTitreTransportDTO(TaTitreTransportDTO taTitreTransportDTO) {
		this.taTitreTransportDTO = taTitreTransportDTO;
	}

	public boolean isGestionCapsules() {
		return gestionCapsules;
	}

	public void setGestionCapsules(boolean gestionCapsules) {
		this.gestionCapsules = gestionCapsules;
	}


	public void onClearTaTitreTransport(AjaxBehaviorEvent event){
		selectedTaArticleDTO.setCodeTitreTransport(null);
		selectedTaArticleDTO.setQteTitreTransport(null);
		if(taArticle.getTaRTaTitreTransport()!=null) {
			taArticle.getTaRTaTitreTransport().setTaArticle(null);
			taArticle.getTaRTaTitreTransport().setTaTitreTransport(null);
		}
		taArticle.setTaRTaTitreTransport(null);
		setTaTitreTransport(null);
		setTaRTaTitreTransport(null);
	}



	public Boolean getDejaDansNomenclature() {
		return dejaDansNomenclature;
	}

	public void setDejaDansNomenclature(Boolean dejaDansNomenclature) {
		this.dejaDansNomenclature = dejaDansNomenclature;
	}

	public ImageArticleController getImageArticleController() {
		return imageArticleController;
	}

	public void setImageArticleController(ImageArticleController imageArticleController) {
		this.imageArticleController = imageArticleController;
	}

	public CatalogueWebArticleController getCatalogueWebArticleController() {
		return catalogueWebArticleController;
	}

	public void setCatalogueWebArticleController(CatalogueWebArticleController catalogueWebArticleController) {
		this.catalogueWebArticleController = catalogueWebArticleController;
	}
	
	public boolean articleModifiable() {
		return selectedTaArticleDTO.getId()==0 && modeEcran.dataSetEnInsertion();
	}

	public Boolean getAfficheSeulementBoutique() {
		return afficheSeulementBoutique;
	}

	public void setAfficheSeulementBoutique(Boolean afficheSeulementBoutique) {
		this.afficheSeulementBoutique = afficheSeulementBoutique;
	}

	public Boolean getParametrageDepuisMenuPrincipal() {
		return parametrageDepuisMenuPrincipal;
	}

	public void setParametrageDepuisMenuPrincipal(Boolean parametrageDepuisMenuPrincipal) {
		this.parametrageDepuisMenuPrincipal = parametrageDepuisMenuPrincipal;
	}

	public boolean afficheUniteSaisie() {
		return affiche_unite_saisie;
	}

	public TaUniteDTO getTaUniteSaisieDTO() {
		return taUniteSaisieDTO;
	}

	public void setTaUniteSaisieDTO(TaUniteDTO taUniteSaisieDTO) {
		this.taUniteSaisieDTO = taUniteSaisieDTO;
	}

	public TaUnite getTaUniteSaisie() {
		return taUniteSaisie;
	}

	public void setTaUniteSaisie(TaUnite taUniteSaisie) {
		this.taUniteSaisie = taUniteSaisie;
	}
}  
