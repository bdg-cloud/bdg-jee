package fr.legrain.bdg.webapp.tiers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response;

import org.primefaces.PrimeFaces;
import org.primefaces.component.tabview.TabView;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.ToggleSelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.TreeNode;

import fr.legrain.abonnement.dto.TaAbonnementFullDTO;
import fr.legrain.abonnement.stripe.dto.TaStripeSourceDTO;
import fr.legrain.abonnement.stripe.model.TaStripeCustomer;
import fr.legrain.abonnement.stripe.model.TaStripeSource;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeCustomerServiceRemote;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeSourceServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.cron.service.remote.ITaCronServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaAbonnementServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaAvisEcheanceServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLAbonnementServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLEcheanceServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLPanierServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaPanierServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaPrelevementServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaReglementServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaTLigneServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocLigneEcheanceAbonnementVersPanierServiceRemote;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.paiement.service.remote.IPaiementEnLigneDossierService;
import fr.legrain.bdg.tiers.service.remote.ITaEtatServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTPaiementServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.bdg.webapp.ConstWeb;
import fr.legrain.bdg.webapp.LgrTabEvent;
import fr.legrain.bdg.webapp.app.AbstractController;
import fr.legrain.bdg.webapp.app.LgrTab;
import fr.legrain.bdg.webapp.app.TabListModelBean;
import fr.legrain.bdg.webapp.app.TabViewsBean;
import fr.legrain.bdg.webapp.documents.OuvertureDocumentBean;
import fr.legrain.bdg.webapp.documents.TaLigneServiceWebExterneDTOJSF;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.TaAvisEcheanceDTO;
import fr.legrain.document.dto.TaLPanierDTO;
import fr.legrain.document.dto.TaPanierDTO;
import fr.legrain.document.model.ImageLgr;
import fr.legrain.document.model.RetourGenerationDoc;
import fr.legrain.document.model.TaAvisEcheance;
import fr.legrain.document.model.TaInfosPanier;
import fr.legrain.document.model.TaLAbonnement;
import fr.legrain.document.model.TaLEcheance;
import fr.legrain.document.model.TaLPanier;
import fr.legrain.document.model.TaLigneALigneEcheance;
import fr.legrain.document.model.TaPanier;
import fr.legrain.document.model.TaTPaiement;
import fr.legrain.generation.service.CreationDocumentLigneEcheanceAbonnementMultiple;
import fr.legrain.generation.service.CreationDocumentLigneServiceWebExterneMultiple;
import fr.legrain.generationDocument.model.ParamAfficheChoixGenerationDocument;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.ModeObjetEcranServeur;
import fr.legrain.paiement.service.PaiementLgr;
import fr.legrain.servicewebexterne.dto.TaLigneServiceWebExterneDTO;
import fr.legrain.servicewebexterne.model.EnteteDocExterne;
import fr.legrain.servicewebexterne.model.TaLigneServiceWebExterne;
import fr.legrain.servicewebexterne.model.TaSynchroServiceExterne;
import fr.legrain.tiers.dto.TaTiersDTO;
import fr.legrain.tiers.model.TaTiers;

@Named
@ViewScoped  
public class ServiceTiersController extends AbstractController implements Serializable { 
	


	/**
	 * 
	 */
	private static final long serialVersionUID = -7185392082333360732L;

	@Inject @Named(value="tabListModelCenterBean")
	private TabListModelBean tabsCenter;
	
	@Inject @Named(value="tabViewsBean")
	private TabViewsBean tabViews;
	
	@Inject @Named(value="ouvertureDocumentBean")
	private OuvertureDocumentBean ouvertureDocumentBean;
	
	@EJB private CreationDocumentLigneEcheanceAbonnementMultiple creation;
	
	private String css;
	
	private String paramId;
	
	private TabView tabViewArticle; //Le Binding sur les onglets "secondaire" semble ne pas fonctionné, les onglets ne sont pas générés au bon moment avec le c:forEach

	protected ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();

	
	private @EJB ITaArticleServiceRemote taArticleService;
	private @EJB ITaTLigneServiceRemote taTLigneService;
	private @EJB ITaPrelevementServiceRemote taPrelevementService;
	private @EJB ITaReglementServiceRemote taReglementService;

	private @EJB ITaTiersServiceRemote taTiersService;
	
	private @EJB ITaPanierServiceRemote taPanierService;
	private @EJB ITaLPanierServiceRemote taLPanierService;

	private @EJB ITaAbonnementServiceRemote taAbonnementService;
	private @EJB ITaLAbonnementServiceRemote taLAbonnementService;
	private @EJB ITaLEcheanceServiceRemote taLEcheanceService;
	private @EJB ITaEtatServiceRemote taEtatService;
	private @EJB ITaTPaiementServiceRemote taTPaiementService;
	private @EJB ITaStripeSourceServiceRemote taStripeSourceService;
	private @EJB ITaStripeCustomerServiceRemote taStripeCustomerService;
	private @EJB ITaAvisEcheanceServiceRemote taAvisEcheanceService;
	private @EJB ITaCronServiceRemote taCronService;
	
	protected ImageLgr selectedTypeCreation;
	

	protected TaLEcheance ligneDocument = null;
	
	@EJB private IGenereDocLigneEcheanceAbonnementVersPanierServiceRemote genereEcheanceVersPanier;

	private @Inject @PaiementLgr(PaiementLgr.TYPE_STRIPE) IPaiementEnLigneDossierService paiementEnLigneDossierService;

	private List<TaAbonnementFullDTO> listeAllAbonnement = new ArrayList<>();

	private List<TaLEcheance> listeEcheanceNonEmiseAbonnement = new ArrayList<>();
	private List<TaLEcheance> listeLEcheance = new ArrayList<>();
	
	private List<TaLEcheance> selectedLignesEcheances = new ArrayList<>();
	
	private List<TaAbonnementFullDTO> listeLAbonnement = new ArrayList<>();

	private TaTiers masterEntity;
	private TaTiersDTO taTiersDTO;
	private boolean modeDialogue;
	
	private MoyenPaiementAbonnementDTOJSF selectedMoyenPaiementAbonnementDTOJSF;
	
	private boolean regleDoc = true;
	
	private boolean cronValide = true;
	
	




	public ServiceTiersController() {  
	}  

	@PostConstruct
	public void postConstruct(){
	}
	

	public void refresh(){
		refresh(null);
	}
	public void refresh(ActionEvent ev){
		verifPassageCRON();
		
		
		
			if(selectedLignesEcheances != null) {
				selectedLignesEcheances.clear();
			}
		
	   	    if(masterEntity!=null) {
	   	    	
					if(masterEntity.getIdTiers()!=0)
						try {
							masterEntity = taTiersService.findById(masterEntity.getIdTiers());
						} catch (FinderException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}					
					
					
					initLigneAbo();
					
					
						
			}
	}
	
	public void initLigneAbo() {
		initLigneAbo(null);
	}
	public void initLigneAbo(ActionEvent ev) {
		listeLAbonnement = taAbonnementService.findAllFullDTOByIdTiers(masterEntity.getIdTiers());
	}
	
	
	/**
	 * Cette méthode vérifie si le CRON responsable de la génération des prochaines échéances, de la suspension et suppression des échéances,
	 *  et suspension ou annulation des lignes d'abo est bien passé il y a moins de 24 heures
	 */
	public void verifPassageCRON() {
		cronValide = taCronService.verifPassageCRONGenerationEcheance();				
			
	}
	
	

    public void onRowSelectLigneAboAll(SelectEvent event){
    	System.out.println("onRowSelectLigneAboAll a etait cliquéééé");
    }
    public void onRowUnSelectLigneAboAll(UnselectEvent event){
    	System.out.println("onRowUnSelectLigneAboAll a etait cliquéééé");
    }
    public void onToggleSelectLigneAboAll(ToggleSelectEvent event){
    	System.out.println("onToggleSelectLigneAboAll a etait cliquéééé");
    }
    
    public void actionGroupee1() {    	
		refresh();    	
    }
    
    
    public void actionGroupee2() {
    }
    
    
	public void actionGroupee3() {
	}
	
	public void actionGroupee4() {
		
		
	}
	
	public void actAnnuler(ActionEvent actionEvent) {
		try {
			switch (modeEcran.getMode()) {
			case C_MO_INSERTION:
				
				refresh();
				break;
			case C_MO_EDITION:
			
				break;

			default:
				break;
			}
			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

			if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Paiement abonnement", "actAnnuler"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	public void autoCompleteMapUIToDTO() {

	}
	public void autoCompleteMapDTOtoUI() {
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
	
	}
	public void updateTab(){
		try {

		autoCompleteMapDTOtoUI();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public boolean editable() {
		return !modeEcran.dataSetEnModif();
	}

	
	public List<TaTiersDTO> articleAutoCompleteDTOLight(String query) {
		List<TaTiersDTO> allValues = taTiersService.findByCodeLight(query.toUpperCase());
		List<TaTiersDTO> filteredValues = new ArrayList<TaTiersDTO>();

		for (int i = 0; i < allValues.size(); i++) {
			TaTiersDTO civ = allValues.get(i);
			if(query.equals("*") || civ.getCodeTiers().toUpperCase().contains(query.toUpperCase())) {
				filteredValues.add(civ);
			}
		}

		return filteredValues;
	}
	public TabListModelBean getTabsCenter() {
		return tabsCenter;
	}
	public void setTabsCenter(TabListModelBean tabsCenter) {
		this.tabsCenter = tabsCenter;
	}
	public TabViewsBean getTabViews() {
		return tabViews;
	}
	public void setTabViews(TabViewsBean tabViews) {
		this.tabViews = tabViews;
	}
	public String getParamId() {
		return paramId;
	}
	public void setParamId(String paramId) {
		this.paramId = paramId;
	}
	public TabView getTabViewArticle() {
		return tabViewArticle;
	}
	public void setTabViewArticle(TabView tabViewArticle) {
		this.tabViewArticle = tabViewArticle;
	}
	public ModeObjetEcranServeur getModeEcran() {
		return modeEcran;
	}
	public void setModeEcran(ModeObjetEcranServeur modeEcran) {
		this.modeEcran = modeEcran;
	}
	public List<TaAbonnementFullDTO> getListeAllAbonnement() {
		return listeAllAbonnement;
	}
	public void setListeAllAbonnement(List<TaAbonnementFullDTO> listeAllAbonnement) {
		this.listeAllAbonnement = listeAllAbonnement;
	}
	public List<TaLEcheance> getListeEcheanceNonEmiseAbonnement() {
		return listeEcheanceNonEmiseAbonnement;
	}
	public void setListeEcheanceNonEmiseAbonnement(List<TaLEcheance> listeEcheanceNonEmiseAbonnement) {
		this.listeEcheanceNonEmiseAbonnement = listeEcheanceNonEmiseAbonnement;
	}
	public List<TaLEcheance> getListeLEcheance() {
		return listeLEcheance;
	}
	public void setListeLEcheance(List<TaLEcheance> listeLEcheance) {
		this.listeLEcheance = listeLEcheance;
	}
	public TaTiers getMasterEntity() {
		return masterEntity;
	}
	public void setMasterEntity(TaTiers masterEntity) {
		this.masterEntity = masterEntity;
	}
	public TaTiersDTO getTaTiersDTO() {
		return taTiersDTO;
	}
	public void setTaTiersDTO(TaTiersDTO taTiersDTO) {
		this.taTiersDTO = taTiersDTO;
	}
	public boolean isModeDialogue() {
		return modeDialogue;
	}
	public void setModeDialogue(boolean modeDialogue) {
		this.modeDialogue = modeDialogue;
	}
	public MoyenPaiementAbonnementDTOJSF getSelectedMoyenPaiementAbonnementDTOJSF() {
		return selectedMoyenPaiementAbonnementDTOJSF;
	}
	public void setSelectedMoyenPaiementAbonnementDTOJSF(
			MoyenPaiementAbonnementDTOJSF selectedMoyenPaiementAbonnementDTOJSF) {
		this.selectedMoyenPaiementAbonnementDTOJSF = selectedMoyenPaiementAbonnementDTOJSF;
	}
	public List<TaLEcheance> getSelectedLignesEcheances() {
		return selectedLignesEcheances;
	}
	public void setSelectedLignesEcheances(List<TaLEcheance> selectedLignesEcheances) {
		this.selectedLignesEcheances = selectedLignesEcheances;
	}

	public ImageLgr getSelectedTypeCreation() {
		return selectedTypeCreation;
	}
	public void setSelectedTypeCreation(ImageLgr selectedTypeCreation) {
		this.selectedTypeCreation = selectedTypeCreation;
	}
	public TaLEcheance getLigneDocument() {
		return ligneDocument;
	}
	public void setLigneDocument(TaLEcheance ligneDocument) {
		this.ligneDocument = ligneDocument;
	}
	public boolean isRegleDoc() {
		return regleDoc;
	}

	public void setRegleDoc(boolean regleDoc) {
		this.regleDoc = regleDoc;
	}

	
	public boolean isCronValide() {
		return cronValide;
	}

	public void setCronValide(boolean cronValide) {
		cronValide = cronValide;
	}


	public String getCss() {
		return css;
	}

	public void setCss(String css) {
		this.css = css;
	}

	public List<TaAbonnementFullDTO> getListeLAbonnement() {
		return listeLAbonnement;
	}

	public void setListeLAbonnement(List<TaAbonnementFullDTO> listeLAbonnement) {
		this.listeLAbonnement = listeLAbonnement;
	}

}  
