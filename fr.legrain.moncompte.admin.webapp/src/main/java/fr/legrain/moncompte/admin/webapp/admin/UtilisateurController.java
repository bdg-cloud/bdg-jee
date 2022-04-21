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

import fr.legrain.bdg.moncompte.service.remote.ITaAdresseServiceRemote;
import fr.legrain.bdg.moncompte.service.remote.ITaClientServiceRemote;
import fr.legrain.bdg.moncompte.service.remote.ITaProduitServiceRemote;
import fr.legrain.bdg.moncompte.service.remote.ITaUtilisateurServiceRemote;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.ModeObjetEcranServeur;
import fr.legrain.moncompte.admin.webapp.ConstWeb;
import fr.legrain.moncompte.admin.webapp.app.LgrTab;
import fr.legrain.moncompte.admin.webapp.app.TabListModelBean;
import fr.legrain.moncompte.admin.webapp.app.TabViewsBean;
import fr.legrain.moncompte.dto.TaAdresseDTO;
import fr.legrain.moncompte.dto.TaUtilisateurDTO;
import fr.legrain.moncompte.model.TaAdresse;
import fr.legrain.moncompte.model.TaUtilisateur;
import fr.legrain.moncompte.model.mapping.LgrDozerMapper;


@ManagedBean
@ViewScoped  
public class UtilisateurController implements Serializable {  
	
	@ManagedProperty(value="#{tabListModelCenterBean}")
	private TabListModelBean tabsCenter;
	
	@ManagedProperty(value="#{tabViewsBean}")
	private TabViewsBean tabViews;
	
	private List<TaUtilisateurDTO> values; 
	
	protected ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();
	
	private @EJB ITaUtilisateurServiceRemote taUtilisateurService;
	private @EJB ITaClientServiceRemote taClientService;
	private @EJB ITaAdresseServiceRemote taAdresseService;
	private @EJB ITaProduitServiceRemote taProduitService;	
//	private @EJB ITaTvaServiceRemote taTvaService;
//	private @EJB ITaTTvaServiceRemote taTTvaService;
//	private @EJB ITaUniteServiceRemote taUniteService;
//	private @EJB ITaRTitreTransportServiceRemote taRTitreTransportService;
//	private @EJB ITaTitreTransportServiceRemote taTitreTransportService;

	private TaUtilisateurDTO[] selectedTaUtilisateurDTOs; 
    private TaUtilisateurDTO newTaUtilisateurDTO = new TaUtilisateurDTO();
    private TaUtilisateur taUtilisateur = new TaUtilisateur();
    private TaAdresse taAdresse1 = new TaAdresse();
    private TaAdresse taAdresse2 = new TaAdresse();
    private TaAdresse taAdresse3 = new TaAdresse();
    private TaUtilisateurDTO selectedTaUtilisateurDTO = new TaUtilisateurDTO();
//    private TaUtilisateurDTO OldSelectedTaUtilisateurDTO = new TaUtilisateurDTO();
    
//	private TaUnite taUnite1;
//	private TaUnite taUnite2;
//	private TaFamille taFamille;
	
//	private Boolean afficheMatierePremiere=false;
//	private Boolean afficheUtilisateurFini=false;
////	private Boolean afficheTous=true;
	
	private String urlPourEdition;
	
	private TabView tabViewUtilisateur; //Le Binding sur les onglets "secondaire" semble ne pas fonctionné, les onglets ne sont pas générés au bon moment avec le c:forEach
    
    private LgrDozerMapper<TaUtilisateurDTO,TaUtilisateur> mapperUIToModel  = new LgrDozerMapper<TaUtilisateurDTO,TaUtilisateur>();
	private LgrDozerMapper<TaUtilisateur,TaUtilisateurDTO> mapperModelToUI  = new LgrDozerMapper<TaUtilisateur,TaUtilisateurDTO>();
	
    private LgrDozerMapper<TaAdresseDTO,TaAdresse> mapperUIToModelAdresse  = new LgrDozerMapper<TaAdresseDTO,TaAdresse>();
	private LgrDozerMapper<TaAdresse,TaAdresseDTO> mapperModelToUIAdresse   = new LgrDozerMapper<TaAdresse,TaAdresseDTO>();
	
	public UtilisateurController() {  
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
		values = taUtilisateurService.selectAllDTO();
		

		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
		
		//updateTabs();
	}
	
	public void updateTabs() {
		try {
			taUtilisateur = taUtilisateurService.findById(selectedTaUtilisateurDTO.getId());
			
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public List<TaUtilisateurDTO> getValues(){  
		return values;
	}
	
	public boolean editable() {
		return !modeEcran.dataSetEnModif();
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
	 
	public void actAnnuler(ActionEvent actionEvent) {
		
		try {
			switch (modeEcran.getMode()) {
			case C_MO_INSERTION:
				taUtilisateur=null;
				
				break;
			case C_MO_EDITION:
				if(selectedTaUtilisateurDTO.getId()!=null && selectedTaUtilisateurDTO.getId()!=0){
					taUtilisateur = taUtilisateurService.findById(selectedTaUtilisateurDTO.getId());
					}				
				break;

			default:
				break;
			}			
				
			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
		mapperModelToUI.map(taUtilisateur,selectedTaUtilisateurDTO );
		
		if(ConstWeb.DEBUG) {
	   	FacesContext context = FacesContext.getCurrentInstance();  
	    context.addMessage(null, new FacesMessage("Utilisateur", "actAnnuler")); 
		}
	    
	} catch (FinderException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
	
	public void autoCompleteMapUIToDTO() {
//		if(taAdresse1!=null) {
//			mapperModelToUIAdresse.map(taAdresse1, selectedTaUtilisateurDTO.getAdresse1());
//			selectedTaUtilisateurDTO.setAdresse1(selectedTaUtilisateurDTO.getAdresse1());
//			taUtilisateur.setAdresse1(taAdresse1);
//		}
//		if(taAdresse2!=null) {
//			mapperModelToUIAdresse.map(taAdresse2, selectedTaUtilisateurDTO.getAdresse2());
//			selectedTaUtilisateurDTO.setAdresse2(selectedTaUtilisateurDTO.getAdresse2());
//			taUtilisateur.setAdresse2(taAdresse2);
//		}
//		if(taAdresse3!=null) {
//			mapperModelToUIAdresse.map(taAdresse3, selectedTaUtilisateurDTO.getAdresse3());
//			selectedTaUtilisateurDTO.setAdresse3(selectedTaUtilisateurDTO.getAdresse3());
//			taUtilisateur.setAdresse3(taAdresse3);
//		}
	}
	
	public void autoCompleteMapDTOtoUI() {
//		try {
//			taAdresse1 = null;
//			if(selectedTaUtilisateurDTO.getAdresse1()!=null && selectedTaUtilisateurDTO.getAdresse1().getId()!=0) {
//				taAdresse1 = taAdresseService.findById(selectedTaUtilisateurDTO.getAdresse1().getId());
//			}
//			taAdresse2 = null;
//			if(selectedTaUtilisateurDTO.getAdresse2()!=null && selectedTaUtilisateurDTO.getAdresse2().getId()!=0) {
//				taAdresse2 = taAdresseService.findById(selectedTaUtilisateurDTO.getAdresse2().getId());
//			}
//			taAdresse3 = null;
//			if(selectedTaUtilisateurDTO.getAdresse3()!=null && selectedTaUtilisateurDTO.getAdresse3().getId()!=0) {
//				taAdresse3 = taAdresseService.findById(selectedTaUtilisateurDTO.getAdresse3().getId());
//			}			
//		} catch (FinderException e) {
//			e.printStackTrace();
//		}
	}
	
	public void actEnregistrer(ActionEvent actionEvent) {
		
//		TaUtilisateur taArticle = new TaUtilisateur();
		autoCompleteMapUIToDTO();
		mapperUIToModel.map(selectedTaUtilisateurDTO, taUtilisateur);
		try {		
			taUtilisateur = taUtilisateurService.merge(taUtilisateur,ITaUtilisateurServiceRemote.validationContext);

			mapperModelToUI.map(taUtilisateur, selectedTaUtilisateurDTO);
			autoCompleteMapDTOtoUI();
			
			if ((modeEcran.getMode().compareTo(EnumModeObjet.C_MO_INSERTION)==0)) {
				values.add(selectedTaUtilisateurDTO);
			}
			
			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
		} catch(Exception e) {
			FacesContext context = FacesContext.getCurrentInstance();  
			//	    context.addMessage(null, new FacesMessage("Articles", "actEnregistrer"));
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Utilisateurs", e.getMessage()));
		}
	}
	
	public void actInserer(ActionEvent actionEvent) {
		selectedTaUtilisateurDTO = new TaUtilisateurDTO();
		taUtilisateur = new TaUtilisateur();
		
//		selectedTaUtilisateurDTO.setPrixHT(new BigDecimal(0));
//		selectedTaUtilisateurDTO.setPrixTTC(new BigDecimal(0));
//		selectedTaUtilisateurDTO.setCodeArticle(taArticleService.genereCode());
//		selectedTaUtilisateurDTO.setActif(true);
//		selectedTaUtilisateurDTO.setStockMinArticle(new BigDecimal(-1));
		
		autoCompleteMapDTOtoUI();
		
		modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
		if(ConstWeb.DEBUG) {
	   	FacesContext context = FacesContext.getCurrentInstance();  
	    context.addMessage(null, new FacesMessage("Utilisateur", "actInserer"));
		}
	}
	
	public void actModifier(ActionEvent actionEvent) {
		
		try {
			taUtilisateur = taUtilisateurService.findById(selectedTaUtilisateurDTO.getId());
			
			modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
			
			if(ConstWeb.DEBUG) {
		   	FacesContext context = FacesContext.getCurrentInstance();  
		    context.addMessage(null, new FacesMessage("Utilisateur", "actModifier")); 
			}
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void actSupprimer(ActionEvent actionEvent) {
		TaUtilisateur taArticle = new TaUtilisateur();
		try {
			if(selectedTaUtilisateurDTO!=null && selectedTaUtilisateurDTO.getId()!=null){
				taArticle = taUtilisateurService.findById(selectedTaUtilisateurDTO.getId());
			}

			taUtilisateurService.remove(taArticle);
			values.remove(selectedTaUtilisateurDTO);
			
			if(!values.isEmpty()) {
				selectedTaUtilisateurDTO = values.get(0);
			}else selectedTaUtilisateurDTO=new TaUtilisateurDTO();

			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
			if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(""
					+ "Utilisateur", "actSupprimer"));
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
		context.addMessage(null, new FacesMessage("Utilisateur", "actFermer"));
		}
	}
	
	public void actImprimer(ActionEvent actionEvent) {
		if(ConstWeb.DEBUG) {
		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage("Utilisateur", "actImprimer"));
		}
		try {
			
//		FacesContext facesContext = FacesContext.getCurrentInstance();
//		HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
			
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		sessionMap.put("article", taUtilisateurService.findById(selectedTaUtilisateurDTO.getId()));
		
//			session.setAttribute("tiers", taTiersService.findById(selectedTaTiersDTO.getId()));
			System.out.println("actImprimer()");
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
    
    public void nouveau(ActionEvent actionEvent) {  
    	LgrTab b = new LgrTab();
    	b.setTypeOnglet("fr.legrain.onglet.utilisateur");
		b.setTitre("Nouveau");
		b.setStyle(LgrTab.CSS_CLASS_TAB_ARTICLE);
		b.setTemplate("admin/utilisateur.xhtml");
		b.setParamId("0");
		
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
		actInserer(actionEvent);
		if(ConstWeb.DEBUG) {
    	FacesContext context = FacesContext.getCurrentInstance();  
	    context.addMessage(null, new FacesMessage("Utilisateur", 
	    		"Nouveau"
	    		)); 
		}
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
			context.addMessage(null, new FacesMessage("Utilisateur", 
					"Chargement de l'utilisateur N°"+selectedTaUtilisateurDTO.getUsername()
					)); 
			}
		} else {
			tabViews.selectionneOngletCentre(LgrTab.TYPE_TAB_ARTICLE);
		}
			
	} 
	
    public void onRowSelect(SelectEvent event) {  
    	LgrTab b = new LgrTab();
    	b.setTypeOnglet("fr.legrain.onglet.utilisateur");
		b.setTitre("Utilisateur "+selectedTaUtilisateurDTO.getUsername());
		b.setTemplate("admin/utilisateur.xhtml");
		b.setStyle(LgrTab.CSS_CLASS_TAB_ARTICLE);
		b.setParamId(LibConversion.integerToString(selectedTaUtilisateurDTO.getId()));
		
		int pos = 0;
		tabsCenter.ajouterOnglet(b);
		pos = tabViewUtilisateur!=null?tabViewUtilisateur.getActiveIndex():0;
		tabViews.selectionneOngletCentre(b);
		if(tabViewUtilisateur!=null) {
			tabViewUtilisateur.setActiveIndex(pos);
		}
		
		autoCompleteMapDTOtoUI();
		
		updateTabs();
		
		if(ConstWeb.DEBUG) {
    	FacesContext context = FacesContext.getCurrentInstance();  
	    context.addMessage(null, new FacesMessage("Utilisateur", 
	    		"Chargement de l'utilisateur N°"+selectedTaUtilisateurDTO.getUsername()
	    		)); 
		}
    } 
    
	public TaUtilisateurDTO[] getSelectedTaUtilisateurDTOs() {
		return selectedTaUtilisateurDTOs;
	}

	public void setSelectedTaUtilisateurDTOs(TaUtilisateurDTO[] selectedTaUtilisateurDTOs) {
		this.selectedTaUtilisateurDTOs = selectedTaUtilisateurDTOs;
	}

	public TaUtilisateurDTO getNewTaUtilisateurDTO() {
		return newTaUtilisateurDTO;
	}

	public void setNewTaUtilisateurDTO(TaUtilisateurDTO newTaUtilisateurDTO) {
		this.newTaUtilisateurDTO = newTaUtilisateurDTO;
	}

	public TaUtilisateurDTO getSelectedTaUtilisateurDTO() {
		return selectedTaUtilisateurDTO;
	}

	public void setSelectedTaUtilisateurDTO(TaUtilisateurDTO selectedTaUtilisateurDTO) {
		this.selectedTaUtilisateurDTO = selectedTaUtilisateurDTO;
	}

	public TabListModelBean getTabsCenter() {
		return tabsCenter;
	}

	public void setTabsCenter(TabListModelBean tabsCenter) {
		this.tabsCenter = tabsCenter;
	}

	public void validateUtilisateur(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		
		String msg = "";
		
		try {
		  String nomChamp =  (String) component.getAttributes().get("nomChamp");
		  
		  validateUIField(nomChamp,value);
		 TaUtilisateurDTO dtoTemp = new TaUtilisateurDTO();
		 PropertyUtils.setSimpleProperty(dtoTemp, nomChamp, value);
		  taUtilisateurService.validateDTOProperty(dtoTemp, nomChamp, ITaUtilisateurServiceRemote.validationContext );

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

//	public List<TaTypeUtilisateur> typeUtilisateurAutoComplete(String query) {
//		FacesContext context = FacesContext.getCurrentInstance();  
//		context.addMessage(null, new FacesMessage("Query", query));
//		if(query!=null && query.equals("")){
//			taTypeUtilisateur=null;
//		}
//	        List<TaTypeUtilisateur> allValues = taTypeUtilisateurService.selectAll();
//	        List<TaTypeUtilisateur> filteredValues = new ArrayList<TaTypeUtilisateur>();
//	         
//	        for (int i = 0; i < allValues.size(); i++) {
//	        	TaTypeUtilisateur civ = allValues.get(i);
//	            if(query.equals("*") || civ.getCode().toLowerCase().contains(query.toLowerCase())) {
//	                filteredValues.add(civ);
//	            }
//	        }
//	         
//	        return filteredValues;
//		
//    }

	

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
//						selectedTaUtilisateurDTO.setCodeTTva(taArticle.getTaTTva().getCodeTTva());
//				else selectedTaUtilisateurDTO.setCodeTTva(null);					
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
//						selectedTaUtilisateurDTO.setCodeTTva(taArticle.getTaTTva().getCodeTTva());
//				else selectedTaUtilisateurDTO.setCodeTTva(null);					
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
//				if (taArticleService.rapportUniteEstRempli(selectedTaUtilisateurDTO)){
//					taArticle.getTaRapportUnite().setRapport(dto.getRapport());
//				}
//				
//			}  else if(nomChamp.equals(Const.C_SENS_RAPPORT)) {
//
//				TaRapportUniteDTO dto = new TaRapportUniteDTO();
//				PropertyUtils.setSimpleProperty(dto, nomChamp, value);
//				if (taArticleService.rapportUniteEstRempli(selectedTaUtilisateurDTO)){
//					taArticle.getTaRapportUnite().setSens(LibConversion.booleanToInt(dto.getSens()));
//				}
//			} else if(nomChamp.equals(Const.C_NB_DECIMAL)) {
//				TaRapportUniteDTO dto = new TaRapportUniteDTO();
//				PropertyUtils.setSimpleProperty(dto, nomChamp, value);
//				if (taArticleService.rapportUniteEstRempli(selectedTaUtilisateurDTO)){
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
//						taArticle=taArticleService.initPrixArticle(taArticle,selectedTaUtilisateurDTO);
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
//						if(value!=null && !value.equals(""))taArticleService.initPrixArticle(taArticle,selectedTaUtilisateurDTO);
//						PropertyUtils.setSimpleProperty(dto, nomChamp, value);
//						PropertyUtils.setSimpleProperty(f, nomChamp, value);
//						if(value!=null && selectedTaUtilisateurDTO.getPrixPrix()!=null)
//							change =dto.getPrixPrix().compareTo(selectedTaUtilisateurDTO.getPrixPrix());
//						else change=-1;
//						dto.setPrixttcPrix(selectedTaUtilisateurDTO.getPrixttcPrix());							
////						validationUtilisateur.validate(dto,nomChamp,ITaUtilisateurServiceRemote.validationContext);
//						
//
//				if (change!=0||dto.getPrixPrix().compareTo(new BigDecimal(0))==0){
//					if((value==null||value.equals("")||value.equals(0))&& taArticle.getTaPrix()!=null){
//					//taArticle.setTaPrix(null);
//					taArticle.removePrix(taArticle.getTaPrix());
//				}else{
//					if((value!=null && !value.equals(""))&& taArticle.getTaPrix()!=null){
//						taArticleService.initPrixArticle(taArticle,selectedTaUtilisateurDTO);						
//						taArticle.getTaPrix().setPrixPrix(dto.getPrixPrix());
//						taArticle.getTaPrix().setPrixttcPrix(dto.getPrixttcPrix());
//						taArticle.getTaPrix().majPrix();
//					}
//				}
//					selectedTaUtilisateurDTO.setPrixPrix(taArticle.getTaPrix().getPrixPrix());				
//					selectedTaUtilisateurDTO.setPrixttcPrix(taArticle.getTaPrix().getPrixttcPrix());	 						
//				}
//			}else if(nomChamp.equals(Const.C_PRIXTTC_PRIX)) {
//				TaPrixDTO dto = new TaPrixDTO();
//				int change =0;
//
//						if(value!=null && !value.equals(""))taArticleService.initPrixArticle(taArticle,selectedTaUtilisateurDTO);
//						PropertyUtils.setSimpleProperty(dto, nomChamp, value);
//						if(value!=null && selectedTaUtilisateurDTO.getPrixttcPrix()!=null)
//						change =dto.getPrixttcPrix().compareTo(selectedTaUtilisateurDTO.getPrixttcPrix());
//					else change=-1;
//						dto.setPrixPrix(selectedTaUtilisateurDTO.getPrixPrix());						
////						validationUtilisateur.validate(dto,nomChamp,ITaUtilisateurServiceRemote.validationContext);
//
//
//				if(change!=0||dto.getPrixttcPrix().compareTo(new BigDecimal(0))==0){
//					if((value==null||value.equals("")||value.equals(0))&& taArticle.getTaPrix()!=null){
//					//taArticle.setTaPrix(null);
//					taArticle.removePrix(taArticle.getTaPrix());
//				}else{
//					if((value!=null && !value.equals(""))&& taArticle.getTaPrix()!=null){
//						taArticleService.initPrixArticle(taArticle,selectedTaUtilisateurDTO);						
//						taArticle.getTaPrix().setPrixPrix(dto.getPrixPrix());
//						taArticle.getTaPrix().setPrixttcPrix(dto.getPrixttcPrix());
//						taArticle.getTaPrix().majPrixTTC();
//					}
//				}
//					selectedTaUtilisateurDTO.setPrixPrix(taArticle.getTaPrix().getPrixPrix());				
//					selectedTaUtilisateurDTO.setPrixttcPrix(taArticle.getTaPrix().getPrixttcPrix()); 						
//				}
//			} else if(nomChamp.equals(Const.C_QTE_TITRE_TRANSPORT_L_DOCUMENT)) {
//
//				if(!((value==null || value.equals("")) && (!taArticleService.crdEstRempli(selectedTaUtilisateurDTO)))){	
//					if((value==null || value.equals("") || ((BigDecimal)value).doubleValue()<=0) && taArticleService.crdEstRempli(selectedTaUtilisateurDTO))
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
//					if(taArticleService.crdEstRempli(selectedTaUtilisateurDTO) && value!=null){
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
//					TaUtilisateurDTO dto = new TaUtilisateurDTO();
//					TaUtilisateur entity = new TaUtilisateur();
//					if(nomChamp.equals(Const.C_ACTIF_ARTICLE)) { //traitement des booleens
//						if(value instanceof Integer) 
//							if((Integer)value==1) value=new Boolean(true); else new Boolean(false);
//					}
//					if(selectedTaUtilisateurDTO!=null
//							&& selectedTaUtilisateurDTO !=null
//							&& selectedTaUtilisateurDTO.getId()!=null) {
//						entity.setIdArticle(selectedTaUtilisateurDTO.getId());
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

	public TabView getTabViewUtilisateur() {
		return tabViewUtilisateur;
	}

	public void setTabViewUtilisateur(TabView tabViewArticle) {
		this.tabViewUtilisateur = tabViewArticle;
	}

	
}  
