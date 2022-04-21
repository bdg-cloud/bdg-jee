package fr.legrain.moncompte.admin.webapp.admin;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
import org.primefaces.event.TransferEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.DualListModel;

import fr.legrain.bdg.moncompte.service.remote.ITaProduitServiceRemote;
import fr.legrain.bdg.moncompte.service.remote.ITaTNiveauServiceRemote;
import fr.legrain.bdg.moncompte.service.remote.ITaTypeProduitServiceRemote;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.ModeObjetEcranServeur;
import fr.legrain.moncompte.admin.webapp.ConstWeb;
import fr.legrain.moncompte.admin.webapp.app.LgrTab;
import fr.legrain.moncompte.admin.webapp.app.TabListModelBean;
import fr.legrain.moncompte.admin.webapp.app.TabViewsBean;
import fr.legrain.moncompte.dto.TaProduitDTO;
import fr.legrain.moncompte.model.TaProduit;
import fr.legrain.moncompte.model.TaSetup;
import fr.legrain.moncompte.model.TaTNiveau;
import fr.legrain.moncompte.model.TaTypeProduit;
import fr.legrain.moncompte.model.mapping.LgrDozerMapper;


@ManagedBean
@ViewScoped  
public class ProduitController implements Serializable {  
	
	@ManagedProperty(value="#{tabListModelCenterBean}")
	private TabListModelBean tabsCenter;
	
	@ManagedProperty(value="#{tabViewsBean}")
	private TabViewsBean tabViews;
	
	private List<TaProduitDTO> values; 
	
	protected ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();
	protected ModeObjetEcranServeur modeEcranSetup = new ModeObjetEcranServeur();
	
	private DualListModel<TaProduit> listeProduitCompose;
	private DualListModel<TaProduit> listeProduitDependant;
	
	private @EJB ITaProduitServiceRemote taProduitService;
	private @EJB ITaTypeProduitServiceRemote taTypeProduitService;
	private @EJB ITaTNiveauServiceRemote taTNiveauService;

	private TaProduitDTO[] selectedTaProduitDTOs; 
    private TaProduitDTO newTaProduitDTO = new TaProduitDTO();
    private TaProduit taProduit = new TaProduit();
    private TaProduitDTO selectedTaProduitDTO = new TaProduitDTO();
    
    private TaSetup taSetup = new TaSetup();
    private TaSetup selectedTaSetup = new TaSetup();

    private TaTypeProduit taTypeProduit;
    private TaTNiveau taTNiveau;
	
	private String urlPourEdition;
	
	private TabView tabViewProduit; //Le Binding sur les onglets "secondaire" semble ne pas fonctionné, les onglets ne sont pas générés au bon moment avec le c:forEach
    
    private LgrDozerMapper<TaProduitDTO,TaProduit> mapperUIToModel  = new LgrDozerMapper<TaProduitDTO,TaProduit>();
	private LgrDozerMapper<TaProduit,TaProduitDTO> mapperModelToUI  = new LgrDozerMapper<TaProduit,TaProduitDTO>();
	
	public ProduitController() {  
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
		values = taProduitService.selectAllDTO();
		
		List<TaProduit> sourceCompose = taProduitService.selectAll();
		List<TaProduit> targetCompose = new ArrayList<TaProduit>();
	    List<TaProduit> sourceDep = taProduitService.selectAll();
	    List<TaProduit> targetDep = new ArrayList<TaProduit>();
	    listeProduitCompose = new DualListModel<TaProduit>(sourceCompose, targetCompose);
	    listeProduitDependant = new DualListModel<TaProduit>(sourceDep, targetDep);
		

		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
		
		//updateTabs();
	}
	
	public void updateTabs() {
		try {
			taProduit = taProduitService.findById(selectedTaProduitDTO.getId());
			
		} catch (FinderException e) {
			e.printStackTrace();
		}
	}
	
	public void openProduit() {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.produit");
		b.setTitre("Produit");
		b.setTemplate("admin/produit.xhtml");
		b.setStyle(LgrTab.CSS_CLASS_TAB_ARTICLE);
		b.setIdTab("idTabProduit");
		tabsCenter.ajouterOnglet(b);
		
		if(taProduit!=null) {
			mapperModelToUI.map(taProduit, selectedTaProduitDTO);
			selectedTaProduitDTO.setId(taProduit.getIdProduit());
			//updateTabs();
			onRowSelect(null);
		}
	}
	
	public List<TaProduitDTO> getValues(){  
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
				taProduit=null;

				break;
			case C_MO_EDITION:
				if(selectedTaProduitDTO.getId()!=null && selectedTaProduitDTO.getId()!=0){
					taProduit = taProduitService.findById(selectedTaProduitDTO.getId());
				}				
				break;

			default:
				break;
			}			

			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
			mapperModelToUI.map(taProduit,selectedTaProduitDTO );

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
				taSetup=null;

				break;
			case C_MO_EDITION:
				if(selectedTaProduitDTO.getId()!=null && selectedTaProduitDTO.getId()!=0){
					taProduit = taProduitService.findById(selectedTaProduitDTO.getId());
				}				
				break;

			default:
				break;
			}			

			modeEcranSetup.setMode(EnumModeObjet.C_MO_CONSULTATION);
			mapperModelToUI.map(taProduit,selectedTaProduitDTO );

			if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Setup", "actAnnulerSetup")); 
			}

		} catch (FinderException e) {
			e.printStackTrace();
		}
	}
	
	public void autoCompleteMapUIToDTO() {
		if(taTypeProduit!=null) {
//			validateUIField(Const.C_CODE_T_ENTITE,taTypeProduit.getCode());
			selectedTaProduitDTO.setCodeTypeProduit(taTypeProduit.getCode());
			taProduit.setTaTypeProduit(taTypeProduit);
		}
		if(taTNiveau!=null) {
//			validateUIField(Const.C_CODE_T_ENTITE,taTypeProduit.getCode());
			selectedTaProduitDTO.setCodeTNiveau(taTNiveau.getCode());
			taProduit.setTaTNiveau(taTNiveau);
		}		
	}
	
	public void autoCompleteMapDTOtoUI() {
		try {
			taTypeProduit = null;
			taTNiveau=null;
			if(selectedTaProduitDTO.getCodeTypeProduit()!=null && !selectedTaProduitDTO.getCodeTypeProduit().equals("")) {
				taTypeProduit = taTypeProduitService.findByCode(selectedTaProduitDTO.getCodeTypeProduit());
			}
			if(selectedTaProduitDTO.getCodeTNiveau()!=null && !selectedTaProduitDTO.getCodeTNiveau().equals("")) {
				taTNiveau = taTNiveauService.findByCode(selectedTaProduitDTO.getCodeTNiveau());
			}			
		} catch (FinderException e) {
			e.printStackTrace();
		}
	}
	
	public void actEnregistrer(ActionEvent actionEvent) {
		
//		TaProduit taArticle = new TaProduit();
		autoCompleteMapUIToDTO();
		mapperUIToModel.map(selectedTaProduitDTO, taProduit);
		try {		
			
			updateListeSousProduit();
			updateListeSousDependance();
			
			taProduit = taProduitService.merge(taProduit,ITaProduitServiceRemote.validationContext);

			mapperModelToUI.map(taProduit, selectedTaProduitDTO);
			selectedTaProduitDTO.setId(taProduit.getIdProduit());
			
			autoCompleteMapDTOtoUI();
			
			if ((modeEcran.getMode().compareTo(EnumModeObjet.C_MO_INSERTION)==0)) {
				values.add(selectedTaProduitDTO);
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
		if(taProduit.getListeSetup()==null) {
			taProduit.setListeSetup(new ArrayList<TaSetup>());
		}
		if(selectedTaSetup.getIdSetup()!=null && selectedTaSetup.getIdSetup()!=0) { //modification
			int position = -1;
			for (TaSetup s : taProduit.getListeSetup()) {
				if(s.getIdSetup()==selectedTaSetup.getIdSetup()) {
					position = taProduit.getListeSetup().indexOf(s);
				}
			}
			if(position!=-1) {
				selectedTaSetup.setTaProduit(taProduit);
				taProduit.getListeSetup().set(position, selectedTaSetup);
			}
		} else { //nouveau
			selectedTaSetup.setTaProduit(taProduit);
			taProduit.getListeSetup().add(selectedTaSetup);
		}
		actEnregistrer(null);
	}
	
	public void actInserer(ActionEvent actionEvent) {
		selectedTaProduitDTO = new TaProduitDTO();
		taProduit = new TaProduit();
		
		selectedTaProduitDTO.setPrixHT(new BigDecimal(0));
		selectedTaProduitDTO.setPrixTTC(new BigDecimal(0));
//		selectedTaProduitDTO.setCodeArticle(taArticleService.genereCode());
//		selectedTaProduitDTO.setActif(true);
//		selectedTaProduitDTO.setStockMinArticle(new BigDecimal(-1));
		
		autoCompleteMapDTOtoUI();
		
		modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
		if(ConstWeb.DEBUG) {
	   	FacesContext context = FacesContext.getCurrentInstance();  
	    context.addMessage(null, new FacesMessage("Articles", "actInserer")); 
		}
	}
	
	public void actInsererSetup(ActionEvent actionEvent) {
		actModifier(actionEvent);
		//selectedTaProduitDTO = new TaProduitDTO();
		taSetup = new TaSetup();
		
		selectedTaSetup  = new TaSetup();
		selectedTaSetup.setMaj(true);
		selectedTaSetup.setActif(true);
		selectedTaSetup.setDateSetup(new Date());
		
//		selectedTaProduitDTO.setPrixHT(new BigDecimal(0));
//		selectedTaProduitDTO.setPrixTTC(new BigDecimal(0));

//		autoCompleteMapDTOtoUI();
		
		modeEcranSetup.setMode(EnumModeObjet.C_MO_INSERTION);
		if(ConstWeb.DEBUG) {
	   	FacesContext context = FacesContext.getCurrentInstance();  
	    context.addMessage(null, new FacesMessage("Setup", "actInsererSetup")); 
		}
	}
	
	public void actModifier(ActionEvent actionEvent) {
		
		try {
			taProduit = taProduitService.findById(selectedTaProduitDTO.getId());
			
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
			taProduit = taProduitService.findById(selectedTaProduitDTO.getId());
			
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
		TaProduit taArticle = new TaProduit();
		try {
			if(selectedTaProduitDTO!=null && selectedTaProduitDTO.getId()!=null){
				taArticle = taProduitService.findById(selectedTaProduitDTO.getId());
			}

			taProduitService.remove(taArticle);
			values.remove(selectedTaProduitDTO);
			
			if(!values.isEmpty()) {
				selectedTaProduitDTO = values.get(0);
			}else selectedTaProduitDTO=new TaProduitDTO();

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
		TaProduit taArticle = new TaProduit();
		try {
			if(selectedTaProduitDTO!=null && selectedTaProduitDTO.getId()!=null){
				taArticle = taProduitService.findById(selectedTaProduitDTO.getId());
			}

			taProduitService.remove(taArticle);
			values.remove(selectedTaProduitDTO);
			
			if(!values.isEmpty()) {
				selectedTaProduitDTO = values.get(0);
			}else selectedTaProduitDTO=new TaProduitDTO();

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
		sessionMap.put("article", taProduitService.findById(selectedTaProduitDTO.getId()));
		
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
					"Chargement de l'article N°"+selectedTaProduitDTO.getCode()
					)); }
		} else {
			tabViews.selectionneOngletCentre(LgrTab.TYPE_TAB_ARTICLE);
		}
			
	} 
	
    public void onRowSelect(SelectEvent event) { 
    	if(pasDejaOuvert()){
	    	LgrTab b = new LgrTab();
	    	b.setTypeOnglet(LgrTab.TYPE_TAB_ARTICLE);
			b.setTitre("Article "+selectedTaProduitDTO.getCode());
			b.setTemplate("admin/produit.xhtml");
			b.setStyle(LgrTab.CSS_CLASS_TAB_ARTICLE);
			b.setParamId(LibConversion.integerToString(selectedTaProduitDTO.getId()));
			
			int pos = 0;
			tabsCenter.ajouterOnglet(b);
			pos = tabViewProduit!=null?tabViewProduit.getActiveIndex():0;
			tabViews.selectionneOngletCentre(b);
			if(tabViewProduit!=null) {
				tabViewProduit.setActiveIndex(pos);
			}
    	}
		
		autoCompleteMapDTOtoUI();
		
		updatePickListCompose();
		updatePickListDependant();
		
		updateTabs();
		if(ConstWeb.DEBUG) {
    	FacesContext context = FacesContext.getCurrentInstance();  
	    context.addMessage(null, new FacesMessage("Article", 
	    		"Chargement de l'article N°"+selectedTaProduitDTO.getCode()
	    		)); }
    } 
    
	public TaProduitDTO[] getSelectedTaProduitDTOs() {
		return selectedTaProduitDTOs;
	}

	public void setSelectedTaProduitDTOs(TaProduitDTO[] selectedTaProduitDTOs) {
		this.selectedTaProduitDTOs = selectedTaProduitDTOs;
	}

	public TaProduitDTO getNewTaProduitDTO() {
		return newTaProduitDTO;
	}

	public void setNewTaProduitDTO(TaProduitDTO newTaProduitDTO) {
		this.newTaProduitDTO = newTaProduitDTO;
	}

	public TaProduitDTO getSelectedTaProduitDTO() {
		return selectedTaProduitDTO;
	}

	public void setSelectedTaProduitDTO(TaProduitDTO selectedTaProduitDTO) {
		this.selectedTaProduitDTO = selectedTaProduitDTO;
	}

	public TabListModelBean getTabsCenter() {
		return tabsCenter;
	}

	public void setTabsCenter(TabListModelBean tabsCenter) {
		this.tabsCenter = tabsCenter;
	}

	public void validateProduit(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		
		String msg = "";
		
		try {
		  String nomChamp =  (String) component.getAttributes().get("nomChamp");
		  
		  validateUIField(nomChamp,value);
		 TaProduitDTO dtoTemp = new TaProduitDTO();
		 PropertyUtils.setSimpleProperty(dtoTemp, nomChamp, value);
		  taProduitService.validateDTOProperty(dtoTemp, nomChamp, ITaProduitServiceRemote.validationContext );

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

	public List<TaTypeProduit> typeProduitAutoComplete(String query) {
		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage("Query", query));
		if(query!=null && query.equals("")){
			taTypeProduit=null;
		}
	        List<TaTypeProduit> allValues = taTypeProduitService.selectAll();
	        List<TaTypeProduit> filteredValues = new ArrayList<TaTypeProduit>();
	         
	        for (int i = 0; i < allValues.size(); i++) {
	        	TaTypeProduit civ = allValues.get(i);
	            if(query.equals("*") || civ.getCode().toLowerCase().contains(query.toLowerCase())) {
	                filteredValues.add(civ);
	            }
	        }
	         
	        return filteredValues;
		
    }

	public List<TaTNiveau> tNiveauAutoComplete(String query) {
		if(ConstWeb.DEBUG) {
		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage("Query", query));
		}
		if(query!=null && query.equals("")){
			taTNiveau=null;
		}
	        List<TaTNiveau> allValues = taTNiveauService.selectAll();
	        List<TaTNiveau> filteredValues = new ArrayList<TaTNiveau>();
	         
	        for (int i = 0; i < allValues.size(); i++) {
	        	TaTNiveau civ = allValues.get(i);
	            if(query.equals("*") || civ.getCode().toLowerCase().contains(query.toLowerCase())) {
	                filteredValues.add(civ);
	            }
	        }
	         
	        return filteredValues;
		
    }
	
	public void onTransfer(TransferEvent event) {
        StringBuilder builder = new StringBuilder();
        for(Object item : event.getItems()) {
            builder.append(((TaProduit) item).getCode()).append("<br />");
        }
         
        FacesMessage msg = new FacesMessage();
        msg.setSeverity(FacesMessage.SEVERITY_INFO);
        msg.setSummary("Items Transferred");
        msg.setDetail(builder.toString());
         
        FacesContext.getCurrentInstance().addMessage(null, msg);
    } 
 
    public void onSelect(SelectEvent event) {
    	if(ConstWeb.DEBUG) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Item Selected", event.getObject().toString()));
    	}
    	//detailConformite = (TaProduit) event.getObject();
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
    
    
    public void updatePickListCompose() {
    	try {
	    	taProduit = (TaProduit) taProduitService.findById(selectedTaProduitDTO.getId());
	    	//ajoute dans la cible, les produits selectionné (deja affecte a l'article)
	    	List<TaProduit> l = new ArrayList<TaProduit>();
	    	if(taProduit.getListeSousProduit()!=null) {
				for (TaProduit c : taProduit.getListeSousProduit()) {
					l.add(c);
				}
				listeProduitCompose.setTarget(l);
	    	}
			
			//supprime de la source les produits deja affecte a la cible
			listeProduitCompose.setSource(taProduitService.selectAll());
			List<TaProduit> lsuppr = new ArrayList<TaProduit>();
			for(TaProduit s : listeProduitCompose.getSource()) {
				for(TaProduit t : l) {
					if(t.getIdProduit()==s.getIdProduit()) {
						lsuppr.add(s);
					}
				}
				if(s.getIdProduit()==taProduit.getIdProduit()) {
					//suppression du produit lui meme de la liste sources
					lsuppr.add(s);
				}
			}
			
			listeProduitCompose.getSource().removeAll(lsuppr);

    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    
    public void updatePickListDependant() {
    	try {
	    	taProduit = (TaProduit) taProduitService.findById(selectedTaProduitDTO.getId());
	    	//ajoute dans la cible, les produits selectionné (deja affecte a l'article)
	    	List<TaProduit> l = new ArrayList<TaProduit>();
	    	if(taProduit.getListeProduitDependant()!=null) {
				for (TaProduit c : taProduit.getListeProduitDependant()) {
					l.add(c);
				}
				listeProduitDependant.setTarget(l);
	    	}
			
			//supprime de la source les produits deja affecte a la cible
	    	listeProduitDependant.setSource(taProduitService.selectAll());
			List<TaProduit> lsuppr = new ArrayList<TaProduit>();
			for(TaProduit s : listeProduitDependant.getSource()) {
				for(TaProduit t : l) {
					if(t.getIdProduit()==s.getIdProduit()) {
						lsuppr.add(s);
					}
				}
				if(s.getIdProduit()==taProduit.getIdProduit()) {
					//suppression du produit lui meme de la liste sources
					lsuppr.add(s);
				}
			}
			
			listeProduitDependant.getSource().removeAll(lsuppr);

    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    
    public void updateListeSousProduit() {
    	//supprimer les controles non sélectionné
		List<TaProduit> lasupprimer = new ArrayList<TaProduit>();
		if(taProduit.getListeSousProduit()!=null) {
			for (TaProduit c : taProduit.getListeSousProduit()) {
				if(!listeProduitCompose.getTarget().contains(c)) {
					lasupprimer.add(c);
				}
			}
		
		for (TaProduit taModeleConformite : lasupprimer) {
			taProduit.getListeSousProduit().remove(taModeleConformite);
		}
		
		//ajouter les nouveaux
		for (TaProduit ctrl : listeProduitCompose.getTarget()) {
			taProduit.getListeSousProduit().add(ctrl);
		}
		}
    }
    
    public void updateListeSousDependance() {
    	//supprimer les controles non sélectionné
		List<TaProduit> lasupprimer = new ArrayList<TaProduit>();
		
		if(taProduit.getListeProduitDependant()!=null) {
			for (TaProduit c : taProduit.getListeProduitDependant()) {
				if(!listeProduitDependant.getTarget().contains(c)) {
					lasupprimer.add(c);
				}
			}
		
		for (TaProduit taModeleConformite : lasupprimer) {
			taProduit.getListeProduitDependant().remove(taModeleConformite);
		}
		
		//ajouter les nouveaux
		for (TaProduit ctrl : listeProduitDependant.getTarget()) {
			taProduit.getListeProduitDependant().add(ctrl);
		}
		}
    }
	

	public boolean validateUIField(String nomChamp,Object value) {
//		try {
//			
//			if(nomChamp.equals(Const.C_CODE_FAMILLE)) {
//					TaFamille entity =null;
//					if(value!=null){
//						if(value instanceof TaFamille){
//						entity=(TaFamille) value;
////						entity = taFamilleService.findByCode(entity.getCodeFamille());
//						}else{
//							entity = taFamilleService.findByCode((String)value);
//						}
//					}
//					taArticle.setTaFamille(entity);
//			
//			} else if(nomChamp.equals(Const.C_CODE_TVA)) {
//					TaTva entity = null;
//					if(value!=null){
//						if(value instanceof TaTva){
//						entity=(TaTva) value;
////						entity = taTvaService.findByCode(entity.getCodeTva());
//						}else{
//							entity = taTvaService.findByCode((String)value);
//						}
//					}					
//					taArticle.setTaTva(entity);
//					taArticle.initCodeTTva();
//					if(taArticle.getTaTTva()!=null)
//						selectedTaProduitDTO.setCodeTTva(taArticle.getTaTTva().getCodeTTva());
//				else selectedTaProduitDTO.setCodeTTva(null);					
//			
//			} else if(nomChamp.equals(Const.C_CODE_T_TVA)) {
//					TaTTva entity = null;
//					if(value!=null){
//						if(value instanceof TaTTva){
//						entity=(TaTTva) value;
////						entity = taTTvaService.findByCode(entity.getCodeTTva());
//						}else{
//							entity = taTTvaService.findByCode((String)value);
//						}
//					}					
//					taArticle.setTaTTva(entity);
//					taArticle.initCodeTTva();
//					if(taArticle.getTaTTva()!=null)
//						selectedTaProduitDTO.setCodeTTva(taArticle.getTaTTva().getCodeTTva());
//				else selectedTaProduitDTO.setCodeTTva(null);					
//				
//			} else if(nomChamp.equals(Const.C_CODE_UNITE2)) {				
//					TaUnite entity = null;
//					if(value!=null){
//						if(taArticle.getTaRapportUnite()==null) taArticle.setTaRapportUnite(new TaRapportUnite());
//						if(value instanceof TaUnite){
//						entity=(TaUnite) value;
////						entity = taUniteService.findByCode(entity.getCodeUnite());
//						}else{
//							entity = taUniteService.findByCode((String)value);
//						}
//					}
//					
//					if(entity!=null){
//					taArticle.getTaRapportUnite().setTaUnite1(taArticle.getTaPrix().getTaUnite());
//					taArticle.getTaRapportUnite().setTaUnite2(entity);
//					}else
//					if(taArticle.getTaRapportUnite()!=null){
//						taArticle.removeRapportUnite(taArticle.getTaRapportUnite());
//					}
//				initEtatRapportUniteUI();
//				
//			} else if(nomChamp.equals(Const.C_RAPPORT)) {
//				TaRapportUniteDTO dto = new TaRapportUniteDTO();
//				PropertyUtils.setSimpleProperty(dto, nomChamp, value);
//				if (taArticleService.rapportUniteEstRempli(selectedTaProduitDTO)){
//					taArticle.getTaRapportUnite().setRapport(dto.getRapport());
//				}
//				
//			}  else if(nomChamp.equals(Const.C_SENS_RAPPORT)) {
//
//				TaRapportUniteDTO dto = new TaRapportUniteDTO();
//				PropertyUtils.setSimpleProperty(dto, nomChamp, value);
//				if (taArticleService.rapportUniteEstRempli(selectedTaProduitDTO)){
//					taArticle.getTaRapportUnite().setSens(LibConversion.booleanToInt(dto.getSens()));
//				}
//			} else if(nomChamp.equals(Const.C_NB_DECIMAL)) {
//				TaRapportUniteDTO dto = new TaRapportUniteDTO();
//				PropertyUtils.setSimpleProperty(dto, nomChamp, value);
//				if (taArticleService.rapportUniteEstRempli(selectedTaProduitDTO)){
//					taArticle.getTaRapportUnite().setNbDecimal(dto.getNbDecimal());
//				}
//
//			} else if(nomChamp.equals(Const.C_CODE_UNITE)) {
//				
//					TaUnite entity = null;
//					if(value!=null){
//						if(value instanceof TaUnite){
//						entity=(TaUnite) value;
////						entity = taUniteService.findByCode(entity.getCodeUnite());
//						}else{
//							entity = taUniteService.findByCode((String)value);
//						}
//					} 
//					if(entity!=null){
//						taArticle=taArticleService.initPrixArticle(taArticle,selectedTaProduitDTO);
//						taArticle.getTaPrix().setTaUnite(entity);
//						}else{
//							if (taArticle.getTaPrix()!=null)
//							{
//								taArticle.getTaPrix().setTaUnite(null);
//								if(taArticle.getTaPrix().getPrixPrix()!=null && 
//										taArticle.getTaPrix().getPrixPrix().equals(0)&& 
//										taArticle.getTaPrix().getPrixttcPrix()!=null && 
//										taArticle.getTaPrix().getPrixttcPrix().equals(0))
//									taArticle.removePrix(taArticle.getTaPrix());
//							}
//						}
//						initEtatRapportUniteUI();
//				
//			} else if(nomChamp.equals(Const.C_PRIX_PRIX)) {
//
//				TaPrixDTO dto = new TaPrixDTO();
//				TaPrix f = new TaPrix();
//				int change =0;
//
//						
//						if(value!=null && !value.equals(""))taArticleService.initPrixArticle(taArticle,selectedTaProduitDTO);
//						PropertyUtils.setSimpleProperty(dto, nomChamp, value);
//						PropertyUtils.setSimpleProperty(f, nomChamp, value);
//						if(value!=null && selectedTaProduitDTO.getPrixPrix()!=null)
//							change =dto.getPrixPrix().compareTo(selectedTaProduitDTO.getPrixPrix());
//						else change=-1;
//						dto.setPrixttcPrix(selectedTaProduitDTO.getPrixttcPrix());							
////						validationClient.validate(dto,nomChamp,ITaProduitServiceRemote.validationContext);
//						
//
//				if (change!=0||dto.getPrixPrix().compareTo(new BigDecimal(0))==0){
//					if((value==null||value.equals("")||value.equals(0))&& taArticle.getTaPrix()!=null){
//					//taArticle.setTaPrix(null);
//					taArticle.removePrix(taArticle.getTaPrix());
//				}else{
//					if((value!=null && !value.equals(""))&& taArticle.getTaPrix()!=null){
//						taArticleService.initPrixArticle(taArticle,selectedTaProduitDTO);						
//						taArticle.getTaPrix().setPrixPrix(dto.getPrixPrix());
//						taArticle.getTaPrix().setPrixttcPrix(dto.getPrixttcPrix());
//						taArticle.getTaPrix().majPrix();
//					}
//				}
//					selectedTaProduitDTO.setPrixPrix(taArticle.getTaPrix().getPrixPrix());				
//					selectedTaProduitDTO.setPrixttcPrix(taArticle.getTaPrix().getPrixttcPrix());	 						
//				}
//			}else if(nomChamp.equals(Const.C_PRIXTTC_PRIX)) {
//				TaPrixDTO dto = new TaPrixDTO();
//				int change =0;
//
//						if(value!=null && !value.equals(""))taArticleService.initPrixArticle(taArticle,selectedTaProduitDTO);
//						PropertyUtils.setSimpleProperty(dto, nomChamp, value);
//						if(value!=null && selectedTaProduitDTO.getPrixttcPrix()!=null)
//						change =dto.getPrixttcPrix().compareTo(selectedTaProduitDTO.getPrixttcPrix());
//					else change=-1;
//						dto.setPrixPrix(selectedTaProduitDTO.getPrixPrix());						
////						validationClient.validate(dto,nomChamp,ITaProduitServiceRemote.validationContext);
//
//
//				if(change!=0||dto.getPrixttcPrix().compareTo(new BigDecimal(0))==0){
//					if((value==null||value.equals("")||value.equals(0))&& taArticle.getTaPrix()!=null){
//					//taArticle.setTaPrix(null);
//					taArticle.removePrix(taArticle.getTaPrix());
//				}else{
//					if((value!=null && !value.equals(""))&& taArticle.getTaPrix()!=null){
//						taArticleService.initPrixArticle(taArticle,selectedTaProduitDTO);						
//						taArticle.getTaPrix().setPrixPrix(dto.getPrixPrix());
//						taArticle.getTaPrix().setPrixttcPrix(dto.getPrixttcPrix());
//						taArticle.getTaPrix().majPrixTTC();
//					}
//				}
//					selectedTaProduitDTO.setPrixPrix(taArticle.getTaPrix().getPrixPrix());				
//					selectedTaProduitDTO.setPrixttcPrix(taArticle.getTaPrix().getPrixttcPrix()); 						
//				}
//			} else if(nomChamp.equals(Const.C_QTE_TITRE_TRANSPORT_L_DOCUMENT)) {
//
//				if(!((value==null || value.equals("")) && (!taArticleService.crdEstRempli(selectedTaProduitDTO)))){	
//					if((value==null || value.equals("") || ((BigDecimal)value).doubleValue()<=0) && taArticleService.crdEstRempli(selectedTaProduitDTO))
//						value="1";							
//					value=LibConversion.stringToBigDecimal(value.toString());
//				}							
//
//
//					TaRTaTitreTransport entity = new TaRTaTitreTransport();
//					if(value!=null){
//						if(value instanceof TaRTaTitreTransport){
//						entity=(TaRTaTitreTransport) value;
//						}else{
//							entity = taRTitreTransportService.findByCode((String)value);
//						}
//					}
//					
//
//					if(taArticleService.crdEstRempli(selectedTaProduitDTO) && value!=null){
//						taArticleService.initTaRTaTitreTransport(taArticle);
//						taArticle.getTaRTaTitreTransport().setQteTitreTransport(entity.getQteTitreTransport());
//					} else {
//						if(taArticle.getTaRTaTitreTransport()!=null) {
//							taArticle.setTaRTaTitreTransport(null);
//						}
//					}						
//
//			} else if(nomChamp.equals(Const.C_CODE_TITRE_TRANSPORT)) {
//
//
//					TaTitreTransport entity = new TaTitreTransport();
//					if(value!=null){
//						if(value instanceof TaTitreTransport){
//						entity=(TaTitreTransport) value;
//						}else{
//							entity = taTitreTransportService.findByCode((String)value);
//						}
//					}					
//					
//					if (!value.equals("")){
//						taArticleService.initTaRTaTitreTransport(taArticle);
//					taArticle.getTaRTaTitreTransport().setTaTitreTransport(entity);
//				} else {
//					if(taArticle.getTaRTaTitreTransport()!=null) {
//						taArticle.setTaRTaTitreTransport(null);
//					}
//				}						
//			
//			} else {
//					TaProduitDTO dto = new TaProduitDTO();
//					TaProduit entity = new TaProduit();
//					if(nomChamp.equals(Const.C_ACTIF_ARTICLE)) { //traitement des booleens
//						if(value instanceof Integer) 
//							if((Integer)value==1) value=new Boolean(true); else new Boolean(false);
//					}
//					if(selectedTaProduitDTO!=null
//							&& selectedTaProduitDTO !=null
//							&& selectedTaProduitDTO.getId()!=null) {
//						entity.setIdArticle(selectedTaProduitDTO.getId());
//					}
//
//
//					if(taArticle!=null) {
//						if(taArticle.getTaCatalogueWeb()==null) {
//							taArticle.setTaCatalogueWeb(new TaCatalogueWeb());
//						}
//						if(nomChamp.equals(Const.C_LIBELLEC_ARTICLE)) {
//							if(LibChaine.empty(taArticle.getTaCatalogueWeb().getUrlRewritingCatalogueWeb())
//									|| ConstPreferencesArticles.REGENERATION_URL_REWRITING_POUR_CATALOGUE_WEB_A_PARTIR_ECRAN_PRINCIPAL) {
//								taArticle.getTaCatalogueWeb().setUrlRewritingCatalogueWeb(LibChaine.toUrlRewriting((String)value));
//							}
//						} else if(nomChamp.equals(Const.C_LIBELLEL_ARTICLE)) {
//							if(LibChaine.empty(taArticle.getTaCatalogueWeb().getDescriptionLongueCatWeb())
//									|| ConstPreferencesArticles.REGENERATION_DESCRIPTION_POUR_CATALOGUE_WEB_A_PARTIR_ECRAN_PRINCIPAL) {
//								taArticle.getTaCatalogueWeb().setDescriptionLongueCatWeb((String)value);
//							}
//						}
//					}
//					
//			}
//			return false;
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
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

	public TabView getTabViewProduit() {
		return tabViewProduit;
	}

	public void setTabViewProduit(TabView tabViewArticle) {
		this.tabViewProduit = tabViewArticle;
	}

	public TaTypeProduit getTaTypeProduit() {
		return taTypeProduit;
	}

	public void setTaTypeProduit(TaTypeProduit taTypeProduit) {
		this.taTypeProduit = taTypeProduit;
	}

	public DualListModel<TaProduit> getListeProduitCompose() {
		return listeProduitCompose;
	}

	public void setListeProduitCompose(DualListModel<TaProduit> listeProduitCompose) {
		this.listeProduitCompose = listeProduitCompose;
	}

	public DualListModel<TaProduit> getListeProduitDependant() {
		return listeProduitDependant;
	}

	public void setListeProduitDependant(
			DualListModel<TaProduit> listeProduitDependant) {
		this.listeProduitDependant = listeProduitDependant;
	}

	public TaProduit getTaProduit() {
		return taProduit;
	}

	public void setTaProduit(TaProduit taProduit) {
		this.taProduit = taProduit;
	}

	public TaSetup getTaSetup() {
		return taSetup;
	}

	public void setTaSetup(TaSetup taSetup) {
		this.taSetup = taSetup;
	}

	public TaSetup getSelectedTaSetup() {
		return selectedTaSetup;
	}

	public void setSelectedTaSetup(TaSetup selectedTaSetup) {
		this.selectedTaSetup = selectedTaSetup;
	}

	public TaTNiveau getTaTNiveau() {
		return taTNiveau;
	}

	public void setTaTNiveau(TaTNiveau taTNiveau) {
		this.taTNiveau = taTNiveau;
	}

}  
