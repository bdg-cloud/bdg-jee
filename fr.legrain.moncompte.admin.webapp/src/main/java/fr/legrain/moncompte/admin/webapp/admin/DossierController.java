package fr.legrain.moncompte.admin.webapp.admin;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;

import fr.legrain.bdg.lib.server.osgi.BdgProperties;
import fr.legrain.bdg.moncompte.service.remote.ITaDossierServiceRemote;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.ModeObjetEcranServeur;
import fr.legrain.moncompte.admin.webapp.ConstWeb;
import fr.legrain.moncompte.admin.webapp.app.LgrTab;
import fr.legrain.moncompte.admin.webapp.app.TabListModelBean;
import fr.legrain.moncompte.admin.webapp.app.TabViewsBean;
import fr.legrain.moncompte.dto.TaAdresseDTO;
import fr.legrain.moncompte.dto.TaDossierDTO;
import fr.legrain.moncompte.model.TaDossier;
import fr.legrain.moncompte.model.mapping.LgrDozerMapper;


@ManagedBean
@ViewScoped  
public class DossierController implements Serializable {  
	
	@ManagedProperty(value="#{tabListModelCenterBean}")
	private TabListModelBean tabsCenter;
	
	@ManagedProperty(value="#{tabViewsBean}")
	private TabViewsBean tabViews;
	
	private List<TaDossierDTO> values; 
	
	protected ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();
	
	private @EJB ITaDossierServiceRemote taDossierService;
	
	private BdgProperties bdgProperties;
	
	private String urlDossier;
	
	private boolean supprimerClient = false;
	private boolean supprimerUtilisateur = false;
	
	private TaDossierDTO[] selectedTaDossierDTOs; 
    private TaDossierDTO newTaDossierDTO = new TaDossierDTO();
    private TaDossierDTO selectedTaDossierDTO = new TaDossierDTO();
    private TaDossier taDossier = new TaDossier();
    
    private List<TaDossier> listeDossier;
    
    private LgrDozerMapper<TaDossierDTO,TaDossier> mapperUIToModel  = new LgrDozerMapper<TaDossierDTO,TaDossier>();
	private LgrDozerMapper<TaDossier,TaDossierDTO> mapperModelToUI  = new LgrDozerMapper<TaDossier,TaDossierDTO>();
	
	public DossierController() {  
		//values = getValues();
	}  
	
	@PostConstruct
	public void postConstruct(){
		bdgProperties = new BdgProperties();
		refresh();
	}

	public void refresh(){
		values = taDossierService.selectAllLight();
		

		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
		
//		updateTabs();
	}
	
	public void updateTabs() {
		try {
			taDossier = taDossierService.findById(selectedTaDossierDTO.getId());
			urlDossier = "http://"+bdgProperties.getProperty(BdgProperties.KEY_PREFIXE_SOUS_DOMAINE)+taDossier.getCode()+"."+bdgProperties.getProperty(BdgProperties.KEY_NOM_DOMAINE);
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void openDossier() {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.dossiers");
		b.setTitre("Dossiers");
		b.setTemplate("admin/dossiers.xhtml");
		b.setStyle(LgrTab.CSS_CLASS_TAB_DOSSIERS);
		b.setIdTab("idTabDossiers");
		tabsCenter.ajouterOnglet(b);
		
		if(taDossier!=null) {
			mapperModelToUI.map(taDossier, selectedTaDossierDTO);
			//updateTabs();
			onRowSelect(null);
		}
	}
	
	public void actAfficheDialogSupprimer() { 
		//l'appel d'une action permet d'utiliser "<f:setPropertyActionListener ... >" sur la commande et d'initialisé l'objet sur le quel travaillera de dialogue
		supprimerClient = false;
		supprimerUtilisateur = false;
		//System.out.println(selectedCommission.toString());
	}
	
	public List<TaDossierDTO> getValues(){  
		return values;
	}
	
	public boolean editable() {
		return !modeEcran.dataSetEnModif();
	}
	
	public boolean editableLigne() {
		return modeEcran.dataSetEnModif();
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
			case "inserer":	
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
				retour = false;
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
	 
	public void actAnnuler(ActionEvent actionEvent) {
		
		try {
			switch (modeEcran.getMode()) {
			case C_MO_INSERTION:
				taDossier=null;
				
				break;
			case C_MO_EDITION:
				if(selectedTaDossierDTO.getId()!=null && selectedTaDossierDTO.getId()!=0){
					taDossier = taDossierService.findById(selectedTaDossierDTO.getId());
					}				
				break;

			default:
				break;
			}			
				
			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
		mapperModelToUI.map(taDossier,selectedTaDossierDTO );
		
		if(ConstWeb.DEBUG) {
	   	FacesContext context = FacesContext.getCurrentInstance();  
	    context.addMessage(null, new FacesMessage("Dossier", "actAnnuler")); 
		}
	    
	} catch (FinderException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
	
	public void autoCompleteMapUIToDTO() {
//		if(taAdresse1!=null) {
//			mapperModelToUIAdresse.map(taAdresse1, selectedTaDossierDTO.getAdresse1());
//			selectedTaDossierDTO.setAdresse1(selectedTaDossierDTO.getAdresse1());
//			taDossier.setAdresse1(taAdresse1);
//		}
//		if(taAdresse2!=null) {
//			mapperModelToUIAdresse.map(taAdresse2, selectedTaDossierDTO.getAdresse2());
//			selectedTaDossierDTO.setAdresse2(selectedTaDossierDTO.getAdresse2());
//			taDossier.setAdresse2(taAdresse2);
//		}
//		if(taAdresse3!=null) {
//			mapperModelToUIAdresse.map(taAdresse3, selectedTaDossierDTO.getAdresse3());
//			selectedTaDossierDTO.setAdresse3(selectedTaDossierDTO.getAdresse3());
//			taDossier.setAdresse3(taAdresse3);
//		}
	}
	
	public void autoCompleteMapDTOtoUI() {
//		try {
//			taAdresse1 = null;
//			if(selectedTaDossierDTO.getAdresse1()!=null && selectedTaDossierDTO.getAdresse1().getId()!=0) {
//				taAdresse1 = taAdresseService.findById(selectedTaDossierDTO.getAdresse1().getId());
//			}
//			taAdresse2 = null;
//			if(selectedTaDossierDTO.getAdresse2()!=null && selectedTaDossierDTO.getAdresse2().getId()!=0) {
//				taAdresse2 = taAdresseService.findById(selectedTaDossierDTO.getAdresse2().getId());
//			}
//			taAdresse3 = null;
//			if(selectedTaDossierDTO.getAdresse3()!=null && selectedTaDossierDTO.getAdresse3().getId()!=0) {
//				taAdresse3 = taAdresseService.findById(selectedTaDossierDTO.getAdresse3().getId());
//			}			
//		} catch (FinderException e) {
//			e.printStackTrace();
//		}
	}
	
	public boolean adresseEstVide(TaAdresseDTO adresseDTO){
		if(adresseDTO!=null){
		if(adresseDTO.getAdresse1()!=null  && !adresseDTO.getAdresse1().equals(""))return false;
		if(adresseDTO.getAdresse2()!=null  && !adresseDTO.getAdresse2().equals(""))return false;
		if(adresseDTO.getAdresse3()!=null  && !adresseDTO.getAdresse3().equals(""))return false;
		if(adresseDTO.getCodePostal()!=null  && !adresseDTO.getCodePostal().equals(""))return false;
		if(adresseDTO.getVille()!=null  && !adresseDTO.getVille().equals(""))return false;
		if(adresseDTO.getPays()!=null  && !adresseDTO.getPays().equals(""))return false;
		if(adresseDTO.getEmail()!=null  && !adresseDTO.getEmail().equals(""))return false;
		if(adresseDTO.getNomEntreprise()!=null  && !adresseDTO.getNomEntreprise().equals(""))return false;
		if(adresseDTO.getNumFax()!=null  && !adresseDTO.getNumFax().equals(""))return false;
		if(adresseDTO.getWeb()!=null  && !adresseDTO.getWeb().equals(""))return false;
		if(adresseDTO.getNumPort()!=null  && !adresseDTO.getNumPort().equals(""))return false;
		if(adresseDTO.getNumTel1()!=null  && !adresseDTO.getNumTel1().equals(""))return false;
		if(adresseDTO.getNumTel2()!=null  && !adresseDTO.getNumTel2().equals(""))return false;
		}
		return true;
	}
	
    public void onTabChange(TabChangeEvent event) {
        if(event.getData()!=null && event.getData() instanceof TaDossier)
        	taDossier=(TaDossier) event.getData();
    }
    
	public void actEnregistrer(ActionEvent actionEvent) {
		
//		autoCompleteMapUIToDTO();
//		mapperUIToModel.map(selectedTaDossierDTO, taDossier);
//		
//		try {		
//			
//			taDossier = taDossierService.merge(taDossier,ITaDossierServiceRemote.validationContext);
//			
//			
//			taDossier= taDossierService.findById(taDossier.getId());
//			mapperModelToUI.map(taDossier, selectedTaDossierDTO);
//			selectedTaDossierDTO.setId(taDossier.getId());		
//
//			
//			updateTabs();
//			
//			autoCompleteMapDTOtoUI();
//			
//			if ((modeEcran.getMode().compareTo(EnumModeObjet.C_MO_INSERTION)==0)) {
//				values.add(selectedTaDossierDTO);
//			}
//			
//			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
//		} catch(Exception e) {
//			FacesContext context = FacesContext.getCurrentInstance();  
//			//	    context.addMessage(null, new FacesMessage("Dossier", "actEnregistrer"));
//			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Dossiers", e.getMessage()));
//		}
	}
	
	public void actInserer(ActionEvent actionEvent) {
//		selectedTaDossierDTO = new TaDossierDTO();
//		taDossier = new TaDossier();
//		
//		autoCompleteMapDTOtoUI();
//		
//		modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
//		
//		if(ConstWeb.DEBUG) {
//			FacesContext context = FacesContext.getCurrentInstance();  
//	   		context.addMessage(null, new FacesMessage("Dossier", "actInserer")); 
//		}
	}
	
	public void actModifier(ActionEvent actionEvent) {
		
//		try {
//			taDossier = taDossierService.findById(selectedTaDossierDTO.getId());
//			
//			
//		
//			modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
//			
//			if(ConstWeb.DEBUG) {
//		   	FacesContext context = FacesContext.getCurrentInstance();  
//		    context.addMessage(null, new FacesMessage("Dossier", "actModifier"));
//			}
//		    
//		} catch (FinderException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	public void actSupprimer(ActionEvent actionEvent) {
		
		
		TaDossier taDossier = new TaDossier();
		try {
			if(selectedTaDossierDTO!=null && selectedTaDossierDTO.getId()!=null){
				taDossier = taDossierService.findById(selectedTaDossierDTO.getId());
			}

//			taDossierService.remove(taDossier);
//			values.remove(selectedTaDossierDTO);
//			
//			if(!values.isEmpty()) {
//				selectedTaDossierDTO = values.get(0);
//			}else selectedTaDossierDTO=new TaDossierDTO();
			
			taDossierService.supprimeDossier(taDossier, supprimerClient, supprimerUtilisateur);

			
			supprimerClient = false;
			supprimerUtilisateur = false;
			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Dossier", "actSupprimer"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void actFermer(ActionEvent actionEvent) {
//		LgrTab typeOngletDejaOuvert = null;
//		for (LgrTab o : tabsCenter.getOnglets()) {
//			if(LgrTab.TYPE_TAB_ARTICLE.equals(o.getTypeOnglet())) {
//				typeOngletDejaOuvert = o;
//			}
//		}
//		
//		if(typeOngletDejaOuvert!=null) {
//
//			tabsCenter.supprimerOnglet(typeOngletDejaOuvert);
//		}
//		
//		if(ConstWeb.DEBUG) {
//		FacesContext context = FacesContext.getCurrentInstance();  
//		context.addMessage(null, new FacesMessage("Dossier", "actFermer"));
//		}
	}
	
	public void actImprimer(ActionEvent actionEvent) {
//		if(ConstWeb.DEBUG) {
//		FacesContext context = FacesContext.getCurrentInstance();  
//		context.addMessage(null, new FacesMessage("Dossier", "actImprimer"));
//		}
//		try {
//			
////		FacesContext facesContext = FacesContext.getCurrentInstance();
////		HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
//			
//		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
//		Map<String, Object> sessionMap = externalContext.getSessionMap();
//		sessionMap.put("client", taUtilisateurService.findById(selectedTaDossierDTO.getId()));
//		
////			session.setAttribute("tiers", taTiersService.findById(selectedTaTiersDTO.getId()));
//			System.out.println("DossierController.actImprimer()");
//		} catch (FinderException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
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
			
//			autoCompleteMapDTOtoUI();
//			
//			updateTabs();

			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Dossier", 
						""
					)); 
			}
		} else {
			tabViews.selectionneOngletCentre(LgrTab.TYPE_TAB_ARTICLE);
		}
			
	} 
	
    public void onRowSelect(SelectEvent event) {  
    	LgrTab b = new LgrTab();
    	b.setTypeOnglet("fr.legrain.onglet.dossier");
		b.setTitre("Dossier");
		b.setTemplate("admin/dossier.xhtml");
		b.setStyle(LgrTab.CSS_CLASS_TAB_DOSSIER);
		b.setIdTab("idTabDossier");
		tabsCenter.ajouterOnglet(b);
//		b.setParamId(LibConversion.integerToString(selectedTaDossierDTO.getId()));
		
		
		autoCompleteMapDTOtoUI();
		
		updateTabs();
		
		if(ConstWeb.DEBUG) {
    	FacesContext context = FacesContext.getCurrentInstance();  
	    context.addMessage(null, new FacesMessage("Dossier", 
	    		"Chargement "
	    		)); 
		}
    } 
    
	public TaDossierDTO[] getSelectedTaDossierDTOs() {
		return selectedTaDossierDTOs;
	}

	public void setSelectedTaDossierDTOs(TaDossierDTO[] selectedTaDossierDTOs) {
		this.selectedTaDossierDTOs = selectedTaDossierDTOs;
	}

	public TaDossierDTO getNewTaDossierDTO() {
		return newTaDossierDTO;
	}

	public void setNewTaDossierDTO(TaDossierDTO newTaDossierDTO) {
		this.newTaDossierDTO = newTaDossierDTO;
	}

	public TabListModelBean getTabsCenter() {
		return tabsCenter;
	}

	public void setTabsCenter(TabListModelBean tabsCenter) {
		this.tabsCenter = tabsCenter;
	}
	
	public boolean validateUIField(String nomChamp,Object value) {
		return false;
	}

	public TabViewsBean getTabViews() {
		return tabViews;
	}

	public void setTabViews(TabViewsBean tabViews) {
		this.tabViews = tabViews;
	}

	public TaDossier getTaDossier() {
		return taDossier;
	}

	public void setTaDossier(TaDossier taDossier) {
		this.taDossier = taDossier;
	}

	public TaDossierDTO getSelectedTaDossierDTO() {
		return selectedTaDossierDTO;
	}

	public void setSelectedTaDossierDTO(TaDossierDTO selectedTaDossierDTO) {
		this.selectedTaDossierDTO = selectedTaDossierDTO;
	}

	public List<TaDossier> getListeDossier() {
		return listeDossier;
	}

	public void setListeDossier(List<TaDossier> listeDossier) {
		this.listeDossier = listeDossier;
	}

	public String getUrlDossier() {
		return urlDossier;
	}

	public void setUrlDossier(String urlDossier) {
		this.urlDossier = urlDossier;
	}

	public boolean isSupprimerUtilisateur() {
		return supprimerUtilisateur;
	}

	public void setSupprimerUtilisateur(boolean supprimerUtilisateur) {
		this.supprimerUtilisateur = supprimerUtilisateur;
	}

	public boolean isSupprimerClient() {
		return supprimerClient;
	}

	public void setSupprimerClient(boolean supprimerClient) {
		this.supprimerClient = supprimerClient;
	}

}  
