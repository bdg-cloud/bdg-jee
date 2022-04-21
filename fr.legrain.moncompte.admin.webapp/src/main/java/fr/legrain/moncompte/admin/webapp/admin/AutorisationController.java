package fr.legrain.moncompte.admin.webapp.admin;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.primefaces.component.tabview.TabView;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import fr.legrain.bdg.moncompte.service.remote.ITaAutorisationServiceRemote;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.ModeObjetEcranServeur;
import fr.legrain.moncompte.admin.webapp.ConstWeb;
import fr.legrain.moncompte.admin.webapp.app.LgrTab;
import fr.legrain.moncompte.admin.webapp.app.TabListModelBean;
import fr.legrain.moncompte.admin.webapp.app.TabViewsBean;
import fr.legrain.moncompte.dto.TaAutorisationDTO;
import fr.legrain.moncompte.model.TaAutorisation;
import fr.legrain.moncompte.model.mapping.LgrDozerMapper;


@ManagedBean
@ViewScoped  
public class AutorisationController implements Serializable {  
	
	@ManagedProperty(value="#{tabListModelCenterBean}")
	private TabListModelBean tabsCenter;
	
	@ManagedProperty(value="#{tabViewsBean}")
	private TabViewsBean tabViews;
	
	private List<TaAutorisationDTO> values; 
	
	protected ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();
	protected ModeObjetEcranServeur modeEcranSetup = new ModeObjetEcranServeur();
	

	private @EJB ITaAutorisationServiceRemote taAutorisationService;

	private TaAutorisationDTO[] selectedTaAutorisationDTOs; 
    private TaAutorisationDTO newTaAutorisationDTO = new TaAutorisationDTO();
    private TaAutorisation taAutorisation = new TaAutorisation();
    private TaAutorisationDTO selectedTaAutorisationDTO = new TaAutorisationDTO();
    
	
	private String urlPourEdition;
	
	private TabView tabViewAutorisation; //Le Binding sur les onglets "secondaire" semble ne pas fonctionné, les onglets ne sont pas générés au bon moment avec le c:forEach
    
    private LgrDozerMapper<TaAutorisationDTO,TaAutorisation> mapperUIToModel  = new LgrDozerMapper<TaAutorisationDTO,TaAutorisation>();
	private LgrDozerMapper<TaAutorisation,TaAutorisationDTO> mapperModelToUI  = new LgrDozerMapper<TaAutorisation,TaAutorisationDTO>();
	
	public AutorisationController() {  
		//values = getValues();
	}  
	
	@PostConstruct
	public void postConstruct(){
		refresh();
				
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
		String path = request.getRequestURI().substring(request.getContextPath().length());
		urlPourEdition = request.getRequestURL().substring(0,request.getRequestURL().length()-path.length());
	}

	public void refresh(){
		values = taAutorisationService.selectAllDTO();
		
		

		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
		
		//updateTabs();
	}
	
	public void updateTabs() {
		try {
			taAutorisation = taAutorisationService.findById(selectedTaAutorisationDTO.getId());
			
		} catch (FinderException e) {
			e.printStackTrace();
		}
	}
	
	public void openAutorisation() {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.autorisation");
		b.setTitre("Autorisation");
		b.setTemplate("admin/autorisation.xhtml");
		b.setStyle(LgrTab.CSS_CLASS_TAB_ARTICLE);
		b.setIdTab("idTabAutorisation");
		tabsCenter.ajouterOnglet(b);
		
		if(taAutorisation!=null) {
			mapperModelToUI.map(taAutorisation, selectedTaAutorisationDTO);
			selectedTaAutorisationDTO.setId(taAutorisation.getIdAutorisation());
			//updateTabs();
			onRowSelect(null);
		}
	}
	
	public List<TaAutorisationDTO> getValues(){  
		return values;
	}
	
	public boolean editable() {
		return !modeEcran.dataSetEnModif();
	}
	
	public boolean editableSetup() {
		return !modeEcranSetup.dataSetEnModif();
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
			case "supprimer":
			case "modifier":
			case "inserer":
			case "imprimer":
			case "fermer":
				retour = false;
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
	
	public boolean etatBoutonSetup(String bouton) {
		boolean retour = true;
		switch (modeEcranSetup.getMode()) {
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
			case "supprimer":
			case "modifier":
			case "inserer":
			case "imprimer":
			case "fermer":
				retour = false;
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

	public void actAnnuler(ActionEvent actionEvent) {

		try {
			switch (modeEcran.getMode()) {
			case C_MO_INSERTION:
				taAutorisation=null;

				break;
			case C_MO_EDITION:
				if(selectedTaAutorisationDTO.getId()!=null && selectedTaAutorisationDTO.getId()!=0){
					taAutorisation = taAutorisationService.findById(selectedTaAutorisationDTO.getId());
				}				
				break;

			default:
				break;
			}			

			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
			mapperModelToUI.map(taAutorisation,selectedTaAutorisationDTO );

			if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Articles", "actAnnuler")); 
			}
		} catch (FinderException e) {
			e.printStackTrace();
		}
	}
	
	public void actAnnulerSetup(ActionEvent actionEvent) {

		try {
			switch (modeEcranSetup.getMode()) {
			case C_MO_INSERTION:
//				taSetup=null;

				break;
			case C_MO_EDITION:
				if(selectedTaAutorisationDTO.getId()!=null && selectedTaAutorisationDTO.getId()!=0){
					taAutorisation = taAutorisationService.findById(selectedTaAutorisationDTO.getId());
				}				
				break;

			default:
				break;
			}			

			modeEcranSetup.setMode(EnumModeObjet.C_MO_CONSULTATION);
			mapperModelToUI.map(taAutorisation,selectedTaAutorisationDTO );

			if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Setup", "actAnnulerSetup")); 
			}

		} catch (FinderException e) {
			e.printStackTrace();
		}
	}
	
	public void autoCompleteMapUIToDTO() {
//		if(taTypeAutorisation!=null) {
////			validateUIField(Const.C_CODE_T_ENTITE,taTypeAutorisation.getCode());
//			selectedTaAutorisationDTO.setCodeTypeAutorisation(taTypeAutorisation.getCode());
//			taAutorisation.setTaTypeAutorisation(taTypeAutorisation);
//		}
//		if(taTNiveau!=null) {
////			validateUIField(Const.C_CODE_T_ENTITE,taTypeAutorisation.getCode());
//			selectedTaAutorisationDTO.setCodeTNiveau(taTNiveau.getCode());
//			taAutorisation.setTaTNiveau(taTNiveau);
//		}		
	}
	
	public void autoCompleteMapDTOtoUI() {
//		try {
////			
////			if(selectedTaAutorisationDTO.getCodeTypeAutorisation()!=null && !selectedTaAutorisationDTO.getCodeTypeAutorisation().equals("")) {
//////				taTypeAutorisation = taTypeAutorisationService.findByCode(selectedTaAutorisationDTO.getCodeTypeAutorisation());
////			}
////			if(selectedTaAutorisationDTO.getCodeTNiveau()!=null && !selectedTaAutorisationDTO.getCodeTNiveau().equals("")) {
//////				taTNiveau = taTNiveauService.findByCode(selectedTaAutorisationDTO.getCodeTNiveau());
////			}			
//		} catch (FinderException e) {
//			e.printStackTrace();
//		}
	}
	
	public void actEnregistrer(ActionEvent actionEvent) {
		
//		TaAutorisation taArticle = new TaAutorisation();
		autoCompleteMapUIToDTO();
		mapperUIToModel.map(selectedTaAutorisationDTO, taAutorisation);
		try {		
			
			taAutorisation = taAutorisationService.merge(taAutorisation,ITaAutorisationServiceRemote.validationContext);

			mapperModelToUI.map(taAutorisation, selectedTaAutorisationDTO);
			selectedTaAutorisationDTO.setId(taAutorisation.getIdAutorisation());
			
			autoCompleteMapDTOtoUI();
			
			if ((modeEcran.getMode().compareTo(EnumModeObjet.C_MO_INSERTION)==0)) {
				values.add(selectedTaAutorisationDTO);
			}
			
			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
			modeEcranSetup.setMode(EnumModeObjet.C_MO_CONSULTATION);
		} catch(Exception e) {
			FacesContext context = FacesContext.getCurrentInstance();  
			//	    context.addMessage(null, new FacesMessage("Articles", "actEnregistrer"));
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Articles", e.getMessage()));
		}
	}
	
	public void actEnregistrerSetup(ActionEvent actionEvent) {
//		if(taAutorisation.getListeSetup()==null) {
//			taAutorisation.setListeSetup(new ArrayList<TaSetup>());
//		}
//		if(selectedTaSetup.getIdSetup()!=null && selectedTaSetup.getIdSetup()!=0) { //modification
//			int position = -1;
//			for (TaSetup s : taAutorisation.getListeSetup()) {
//				if(s.getIdSetup()==selectedTaSetup.getIdSetup()) {
//					position = taAutorisation.getListeSetup().indexOf(s);
//				}
//			}
//			if(position!=-1) {
//				selectedTaSetup.setTaAutorisation(taAutorisation);
//				taAutorisation.getListeSetup().set(position, selectedTaSetup);
//			}
//		} else { //nouveau
//			selectedTaSetup.setTaAutorisation(taAutorisation);
//			taAutorisation.getListeSetup().add(selectedTaSetup);
//		}
//		actEnregistrer(null);
	}
	
	public void actInserer(ActionEvent actionEvent) {
//		selectedTaAutorisationDTO = new TaAutorisationDTO();
//		taAutorisation = new TaAutorisation();
//		
//		selectedTaAutorisationDTO.setPrixHT(new BigDecimal(0));
//		selectedTaAutorisationDTO.setPrixTTC(new BigDecimal(0));
////		selectedTaAutorisationDTO.setCodeArticle(taArticleService.genereCode());
////		selectedTaAutorisationDTO.setActif(true);
////		selectedTaAutorisationDTO.setStockMinArticle(new BigDecimal(-1));
//		
//		autoCompleteMapDTOtoUI();
//		
//		modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
//		if(ConstWeb.DEBUG) {
//	   	FacesContext context = FacesContext.getCurrentInstance();  
//	    context.addMessage(null, new FacesMessage("Articles", "actInserer")); 
//		}
	}
	
	public void actInsererSetup(ActionEvent actionEvent) {
		actModifier(actionEvent);
		//selectedTaAutorisationDTO = new TaAutorisationDTO();
//		taSetup = new TaSetup();
//		
//		selectedTaSetup  = new TaSetup();
//		selectedTaSetup.setMaj(true);
//		selectedTaSetup.setActif(true);
//		selectedTaSetup.setDateSetup(new Date());
//		
//		selectedTaAutorisationDTO.setPrixHT(new BigDecimal(0));
//		selectedTaAutorisationDTO.setPrixTTC(new BigDecimal(0));

//		autoCompleteMapDTOtoUI();
		
		modeEcranSetup.setMode(EnumModeObjet.C_MO_INSERTION);
		if(ConstWeb.DEBUG) {
	   	FacesContext context = FacesContext.getCurrentInstance();  
	    context.addMessage(null, new FacesMessage("Setup", "actInsererSetup")); 
		}
	}
	
	public void actModifier(ActionEvent actionEvent) {
		
		try {
			taAutorisation = taAutorisationService.findById(selectedTaAutorisationDTO.getId());
			
			modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
			if(ConstWeb.DEBUG) {
		   	FacesContext context = FacesContext.getCurrentInstance();  
		    context.addMessage(null, new FacesMessage("Articles", "actModifier")); 
			}
		    
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void actModifierSetup(ActionEvent actionEvent) {
		
		try {
			taAutorisation = taAutorisationService.findById(selectedTaAutorisationDTO.getId());
			
			modeEcranSetup.setMode(EnumModeObjet.C_MO_EDITION);
			if(ConstWeb.DEBUG) {
		   	FacesContext context = FacesContext.getCurrentInstance();  
		    context.addMessage(null, new FacesMessage("Setup", "actModifierSetup")); 
			}
		    
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void actSupprimer(ActionEvent actionEvent) {
		TaAutorisation taArticle = new TaAutorisation();
		try {
			if(selectedTaAutorisationDTO!=null && selectedTaAutorisationDTO.getId()!=null){
				taArticle = taAutorisationService.findById(selectedTaAutorisationDTO.getId());
			}

			taAutorisationService.remove(taArticle);
			values.remove(selectedTaAutorisationDTO);
			
			if(!values.isEmpty()) {
				selectedTaAutorisationDTO = values.get(0);
			}else selectedTaAutorisationDTO=new TaAutorisationDTO();

			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
			if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Article", "actSupprimer"));
			}
		} catch (RemoveException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}	    
	}
	
	public void actSupprimerSetup(ActionEvent actionEvent) {
		TaAutorisation taArticle = new TaAutorisation();
		try {
			if(selectedTaAutorisationDTO!=null && selectedTaAutorisationDTO.getId()!=null){
				taArticle = taAutorisationService.findById(selectedTaAutorisationDTO.getId());
			}

			taAutorisationService.remove(taArticle);
			values.remove(selectedTaAutorisationDTO);
			
			if(!values.isEmpty()) {
				selectedTaAutorisationDTO = values.get(0);
			}else selectedTaAutorisationDTO=new TaAutorisationDTO();

			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
			if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Article", "actSupprimer"));
			}
		} catch (RemoveException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}	    
	}
	
	public void actFermer(ActionEvent actionEvent) {
		LgrTab typeOngletDejaOuvert = null;
		for (LgrTab o : tabsCenter.getOnglets()) {
			if(LgrTab.TYPE_TAB_ARTICLE.equals(o.getTypeOnglet())) {
				typeOngletDejaOuvert = o;
			}
		}
		
		if(typeOngletDejaOuvert!=null) {
//			if(!values.isEmpty()) {
//				selectedTaTiersDTO = values.get(0);
//			}
			tabsCenter.supprimerOnglet(typeOngletDejaOuvert);
		}
		if(ConstWeb.DEBUG) {
		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage("Articles", "actFermer")); 
		}
	}
	
	public void actImprimer(ActionEvent actionEvent) {
		if(ConstWeb.DEBUG) {
		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage("Articles", "actImprimer"));
		}
		try {
			
//		FacesContext facesContext = FacesContext.getCurrentInstance();
//		HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
			
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		sessionMap.put("article", taAutorisationService.findById(selectedTaAutorisationDTO.getId()));
		
//			session.setAttribute("tiers", taTiersService.findById(selectedTaTiersDTO.getId()));
			System.out.println("ArticleController.actImprimer()");
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
    
    public void nouveau(ActionEvent actionEvent) {  
    	LgrTab b = new LgrTab();
    	b.setTypeOnglet(LgrTab.TYPE_TAB_ARTICLE);
		b.setTitre("Nouveau");
		b.setStyle(LgrTab.CSS_CLASS_TAB_ARTICLE);
		b.setTemplate("admin/produit.xhtml");
		b.setParamId("0");
		
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
		actInserer(actionEvent);
		if(ConstWeb.DEBUG) {
    	FacesContext context = FacesContext.getCurrentInstance();  
	    context.addMessage(null, new FacesMessage("Article", 
	    		"Nouveau"
	    		)); }
    } 
    
    public void supprimer(ActionEvent actionEvent) {
    	actSupprimer(actionEvent);
    }
    
    public void detail(ActionEvent actionEvent) {
    	onRowSelect(null);
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
					"Chargement de l'article N°"+selectedTaAutorisationDTO.getCode()
					)); }
		} else {
			tabViews.selectionneOngletCentre(LgrTab.TYPE_TAB_ARTICLE);
		}
			
	} 
	
    public void onRowSelect(SelectEvent event) { 
    	if(pasDejaOuvert()){
	    	LgrTab b = new LgrTab();
	    	b.setTypeOnglet(LgrTab.TYPE_TAB_ARTICLE);
			b.setTitre("Article "+selectedTaAutorisationDTO.getCode());
			b.setTemplate("admin/produit.xhtml");
			b.setStyle(LgrTab.CSS_CLASS_TAB_ARTICLE);
			b.setParamId(LibConversion.integerToString(selectedTaAutorisationDTO.getId()));
			
			int pos = 0;
			tabsCenter.ajouterOnglet(b);
			pos = tabViewAutorisation!=null?tabViewAutorisation.getActiveIndex():0;
			tabViews.selectionneOngletCentre(b);
			if(tabViewAutorisation!=null) {
				tabViewAutorisation.setActiveIndex(pos);
			}
    	}
		
		autoCompleteMapDTOtoUI();
		
		updateTabs();
		if(ConstWeb.DEBUG) {
    	FacesContext context = FacesContext.getCurrentInstance();  
	    context.addMessage(null, new FacesMessage("Article", 
	    		"Chargement de l'article N°"+selectedTaAutorisationDTO.getCode()
	    		)); }
    } 
    
	public TaAutorisationDTO[] getSelectedTaAutorisationDTOs() {
		return selectedTaAutorisationDTOs;
	}

	public void setSelectedTaAutorisationDTOs(TaAutorisationDTO[] selectedTaAutorisationDTOs) {
		this.selectedTaAutorisationDTOs = selectedTaAutorisationDTOs;
	}

	public TaAutorisationDTO getNewTaAutorisationDTO() {
		return newTaAutorisationDTO;
	}

	public void setNewTaAutorisationDTO(TaAutorisationDTO newTaAutorisationDTO) {
		this.newTaAutorisationDTO = newTaAutorisationDTO;
	}

	public TaAutorisationDTO getSelectedTaAutorisationDTO() {
		return selectedTaAutorisationDTO;
	}

	public void setSelectedTaAutorisationDTO(TaAutorisationDTO selectedTaAutorisationDTO) {
		this.selectedTaAutorisationDTO = selectedTaAutorisationDTO;
	}

	public TabListModelBean getTabsCenter() {
		return tabsCenter;
	}

	public void setTabsCenter(TabListModelBean tabsCenter) {
		this.tabsCenter = tabsCenter;
	}

	public void validateAutorisation(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		
		String msg = "";
		
		try {
		  String nomChamp =  (String) component.getAttributes().get("nomChamp");
		  
		  validateUIField(nomChamp,value);
		 TaAutorisationDTO dtoTemp = new TaAutorisationDTO();
		 PropertyUtils.setSimpleProperty(dtoTemp, nomChamp, value);
		  taAutorisationService.validateDTOProperty(dtoTemp, nomChamp, ITaAutorisationServiceRemote.validationContext );

		} catch(Exception e) {
			msg += e.getMessage();
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
		}
	}
	
	public void autcompleteSelection(SelectEvent event) {
		Object value = event.getObject();
		
		String nomChamp =  (String) event.getComponent().getAttributes().get("nomChamp");

		validateUIField(nomChamp,value);
	}

	

	
	
	
 
    public void onSelect(SelectEvent event) {
    	if(ConstWeb.DEBUG) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Item Selected", event.getObject().toString()));
    	}
    	//detailConformite = (TaAutorisation) event.getObject();
    }
     
    public void onUnselect(UnselectEvent event) {
    	if(ConstWeb.DEBUG) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Item Unselected", event.getObject().toString()));
    	}
    }
     
    public void onReorder() {
    	if(ConstWeb.DEBUG) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "List Reordered", null));
    	}
    } 
    
    
   
	

	public boolean validateUIField(String nomChamp,Object value) {
		return false;
	}

	public String getUrlPourEdition() {
		return urlPourEdition;
	}

	public void setUrlPourEdition(String urlPourEdition) {
		this.urlPourEdition = urlPourEdition;
	}

	public TabViewsBean getTabViews() {
		return tabViews;
	}

	public void setTabViews(TabViewsBean tabViews) {
		this.tabViews = tabViews;
	}

	public TabView getTabViewAutorisation() {
		return tabViewAutorisation;
	}

	public void setTabViewAutorisation(TabView tabViewArticle) {
		this.tabViewAutorisation = tabViewArticle;
	}

	
	public TaAutorisation getTaAutorisation() {
		return taAutorisation;
	}

	public void setTaAutorisation(TaAutorisation taAutorisation) {
		this.taAutorisation = taAutorisation;
	}


}  
