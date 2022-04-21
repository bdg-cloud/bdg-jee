package fr.legrain.bdg.webapp.tiers;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
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
import javax.faces.validator.ValidatorException;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.primefaces.PrimeFaces;
import org.primefaces.component.tabview.TabView;
import org.primefaces.event.SelectEvent;

import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.tiers.service.remote.ITaInfoJuridiqueServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.bdg.webapp.ConstWeb;
import fr.legrain.bdg.webapp.app.AbstractController;
import fr.legrain.bdg.webapp.app.LgrTab;
import fr.legrain.bdg.webapp.app.SmsParam;
import fr.legrain.bdg.webapp.app.TabListModelBean;
import fr.legrain.bdg.webapp.app.TabViewsBean;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.ModeObjetEcranServeur;
import fr.legrain.tiers.dto.TaInfoJuridiqueDTO;
import fr.legrain.tiers.model.TaInfoJuridique;
import fr.legrain.tiers.model.TaTiers;

@Named
@ViewScoped  
public class InfosJuridiqueController extends AbstractController implements Serializable { 
	

	@Inject @Named(value="tabListModelCenterBean")
	private TabListModelBean tabsCenter;
	
	@Inject @Named(value="tabViewsBean")
	private TabViewsBean tabViews;
	
	
	private String paramId;
	
	private TabView tabViewTiers; //Le Binding sur les onglets "secondaire" semble ne pas fonctionné, les onglets ne sont pas générés au bon moment avec le c:forEach

	private List<TaInfoJuridiqueDTO> values; 
	private List<TaInfoJuridiqueDTO> valuesFiltered; 
	
	protected ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();

	private @EJB ITaTiersServiceRemote taTiersService;

	private @EJB ITaInfoJuridiqueServiceRemote TaInfoJuridiqueService;
	
	private TaInfoJuridiqueDTO[] selectedTaInfoJuridiqueDTOs; 
	private TaInfoJuridique taInfoJuridique = new TaInfoJuridique();
	private TaInfoJuridiqueDTO newTaInfoJuridiqueDTO = new TaInfoJuridiqueDTO();
	private TaInfoJuridiqueDTO selectedTaInfoJuridiqueDTO = new TaInfoJuridiqueDTO();
//	private TaInfoJuridiqueDTO oldSelectedTaInfoJuridiqueDTO = new TaInfoJuridiqueDTO();
	
	
	private LgrDozerMapper<TaInfoJuridiqueDTO,TaInfoJuridique> mapperUIToModel  = new LgrDozerMapper<TaInfoJuridiqueDTO,TaInfoJuridique>();
	private LgrDozerMapper<TaInfoJuridique,TaInfoJuridiqueDTO> mapperModelToUI  = new LgrDozerMapper<TaInfoJuridique,TaInfoJuridiqueDTO>();
	
	private TaTiers masterEntity;

	
	public InfosJuridiqueController() {  
		//values = getValues();
	}  

	@PostConstruct
	public void postConstruct(){

		refresh(null);
	}

	public void refresh(ActionEvent ev){
		selectedTaInfoJuridiqueDTO=null;
		 
   	    if(masterEntity!=null) {
   	    	try {
				if(masterEntity.getIdTiers()!=0) masterEntity = taTiersService.findById(masterEntity.getIdTiers());
			} catch (FinderException e) {
				
			}
			values = new ArrayList<>();
			
			if(masterEntity.getTaInfoJuridique()!=null) {
			TaInfoJuridiqueDTO dto=new TaInfoJuridiqueDTO();
			mapperModelToUI.map(masterEntity.getTaInfoJuridique(), dto);
			values.add(dto);

			if(!values.isEmpty()&&selectedTaInfoJuridiqueDTO==null)
				selectedTaInfoJuridiqueDTO=values.get(0);
			}
		}
		autoCompleteMapDTOtoUI();
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}

	public List<TaInfoJuridiqueDTO> getValues(){  
		return values;
	}
	


	public void actAnnuler(ActionEvent actionEvent) {
		try {
			switch (modeEcran.getMode()) {
			case C_MO_INSERTION:
				
				taInfoJuridique = new TaInfoJuridique();
				mapperModelToUI.map(taInfoJuridique,selectedTaInfoJuridiqueDTO );
				selectedTaInfoJuridiqueDTO=new TaInfoJuridiqueDTO();
				
				if(!values.isEmpty()) selectedTaInfoJuridiqueDTO = values.get(0);
				onRowSelect(null);
				break;
			case C_MO_EDITION:
				if(selectedTaInfoJuridiqueDTO.getId()!=null && selectedTaInfoJuridiqueDTO.getId()!=0){
					taInfoJuridique = TaInfoJuridiqueService.findById(selectedTaInfoJuridiqueDTO.getId());
					selectedTaInfoJuridiqueDTO=TaInfoJuridiqueService.findByIdDTO(selectedTaInfoJuridiqueDTO.getId());
				}				
				break;

			default:
				break;
			}
			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
			mapperModelToUI.map(taInfoJuridique, selectedTaInfoJuridiqueDTO);

			if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Tiers", "actAnnuler"));
			}
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public void autoCompleteMapUIToDTO() {
//		if(taTTelDTO!=null) {
//			validateUIField(Const.C_CODE_T_TEL,taTTelDTO.getCodeTTel());
//			selectedTaInfoJuridiqueDTO.setCodeTTel(taTTelDTO.getCodeTTel());
//		} else {
//			selectedTaInfoJuridiqueDTO.setCodeTTel(null);
//		}
		
	}
	
	public void autoCompleteMapDTOtoUI() {
//		try {
//			taTTelDTO = null;
//			if(selectedTaInfoJuridiqueDTO!=null){
//				if(selectedTaInfoJuridiqueDTO.getCodeTTel()!=null && !selectedTaInfoJuridiqueDTO.getCodeTTel().equals("")) {
//					taTTelDTO=taTTelService.findByCodeDTO(selectedTaInfoJuridiqueDTO.getCodeTTel());
//				}	
//			}
//
//		} catch (FinderException e) {
//			e.printStackTrace();
//		}
	}

	public TaInfoJuridique rechercheInfoJuridiqueDansTiers(TaInfoJuridiqueDTO dto){
//		for (TaInfoJuridique tel : masterEntity.getTaInfoJuridiques()) {
//			if(tel.getIdInfoJuridique()==dto.getId()) {
//				return tel;
//			}
//		}
		return masterEntity.getTaInfoJuridique();
	}
	

	

	
	public void actEnregistrer(ActionEvent actionEvent) {		
		//TaInfoJuridique TaInfoJuridique = new TaInfoJuridique();

		autoCompleteMapUIToDTO();
//		initAdresseNull();
				
		mapperUIToModel.map(selectedTaInfoJuridiqueDTO, taInfoJuridique);
		
		/*
		 * A faire, mapper les objets dans TaInfoJuridique avant le merge :
		 * mapping de tous les objets avec des "codes", typeTiers, typeCvilite, ...
		 * tous les objets imbriqués par défaut, adresse défaut, email défaut, ...
		 * 
		 * Pour les converter, essayé de faire fonctionner les injections via omniface
		 * ou, récupérer dans le client RCP le système pour générer les chaines JNDI
		 * 
		 * Ma questions sur les forums
		 * http://forum.primefaces.org/viewtopic.php?f=3&t=40677&sid=fc4d0075c1e57d0433bd8a8d3bdf0393
		 * http://stackoverflow.com/questions/27551615/primefaces-autocomplete-using-a-list-pojo-and-a-string-as-default-value
		 * 
		 */
		
		try {
			//si défaut coché rentre dans tatiers
//			if(selectedTaInfoJuridiqueDTO.isDefaut())
				masterEntity.setTaInfoJuridique(taInfoJuridique);
//			taInfoJuridique.setTaTiers(masterEntity);
//			masterEntity.addInfoJuridique(taInfoJuridique);	
			
			
		
			masterEntity = taTiersService.merge(masterEntity,ITaTiersServiceRemote.validationContext);
			refresh(null);
////			taInfoJuridique = TaInfoJuridiqueService.merge(taInfoJuridique,ITaTiersServiceRemote.validationContext);
//			
//			mapperModelToUI.map(taInfoJuridique, selectedTaInfoJuridiqueDTO);
//			autoCompleteMapDTOtoUI();
//			
//			if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_INSERTION)==0) {
//				values.add(selectedTaInfoJuridiqueDTO);
//			}
//
//			masterEntity = taTiersService.findById(masterEntity.getIdTiers());
			
			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

		} catch(Exception e) {
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			//context.addMessage(null, new FacesMessage("Tiers", "actEnregistrer")); 
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Tiers", e.getMessage()));
		}

		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Tiers", "actEnregistrer"));
		}
	}

	public void actInserer(ActionEvent actionEvent) {
		try {

				selectedTaInfoJuridiqueDTO = new TaInfoJuridiqueDTO();
				taInfoJuridique = new TaInfoJuridique();

					

					
				autoCompleteMapDTOtoUI();
				
				modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
				scrollToTop();
				if(ConstWeb.DEBUG) {
					FacesContext context = FacesContext.getCurrentInstance();  
					context.addMessage(null, new FacesMessage("Tel", "actInserer"));
				}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void actModifier() {
		actModifier(null);
	}

	public void actModifier(ActionEvent actionEvent) {
		
		try {
//			//TaInfoJuridique = TaInfoJuridiqueService.findById(selectedTaInfoJuridiqueDTO.getId());
//			for (TaInfoJuridique tel : masterEntity.getTaInfoJuridiques()) {
//				if(tel.getIdInfoJuridique()==selectedTaInfoJuridiqueDTO.getId()) {
//					taInfoJuridique = tel;
//				}
//			}
		
			taInfoJuridique = masterEntity.getTaInfoJuridique();
			modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
	
			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Tiers", "actModifier"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void actAide(ActionEvent actionEvent) {
		
//		PrimeFaces.current().dialog().openDynamic("aide");
    
        Map<String,Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("draggable", false);
        options.put("resizable", false);
        options.put("contentHeight", 320);
        Map<String,List<String>> params = null;
        PrimeFaces.current().dialog().openDynamic("aide", options, params);
		
		if(ConstWeb.DEBUG) {
		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage("Tiers", "actAide"));
		}
	}
	
//	public void actAideRetour(ActionEvent actionEvent) {
//		
//	}
	   
    public void nouveau(ActionEvent actionEvent) {  
    	LgrTab b = new LgrTab();
    	b.setTypeOnglet(LgrTab.TYPE_TAB_TIERS);
		b.setTitre("Tiers");
		b.setStyle(LgrTab.CSS_CLASS_TAB_TIERS);
		b.setTemplate("tiers/tiers.xhtml");
		b.setIdTab(LgrTab.ID_TAB_TIERS);
		b.setParamId("0");
		
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
		actInserer(actionEvent);
		
		if(ConstWeb.DEBUG) {
    	FacesContext context = FacesContext.getCurrentInstance();  
	    context.addMessage(null, new FacesMessage("Tiers", 
	    		"Nouveau"
	    		)); }
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
	
	public void actSupprimer() {
		actSupprimer(null);
	}
	
	public void actSupprimer(ActionEvent actionEvent) {
		try {

			masterEntity.setTaInfoJuridique(null);
			masterEntity = taTiersService.merge(masterEntity,ITaTiersServiceRemote.validationContext);
			selectedTaInfoJuridiqueDTO=null;
			refresh(null);
			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Tiers", "actSupprimer"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Tiers", e.getCause().getCause().getCause().getCause().getMessage()));
		} 	    
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
		selectedTaInfoJuridiqueDTO=new TaInfoJuridiqueDTO();
		selectedTaInfoJuridiqueDTOs=new TaInfoJuridiqueDTO[]{selectedTaInfoJuridiqueDTO};
	
	}

	public void actImprimer(ActionEvent actionEvent) {
		if(ConstWeb.DEBUG) {
		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage("Tiers", "actImprimer"));
		}
		try {
			
//		FacesContext facesContext = FacesContext.getCurrentInstance();
//		HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
		
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		sessionMap.put("tiers", taTiersService.findById(selectedTaInfoJuridiqueDTO.getId()));
		
		////////////////////////////////////////////////////////////////////////////////////////
		//Test génération de fichier PDF
		
//		HashMap<String,Object> hm = new HashMap<>();
//		hm.put( "tiers", taTiersService.findById(selectedTaInfoJuridiqueDTO.getId()));
//		BirtUtil.setAppContextEdition(hm);
//		
//		BirtUtil.startReportEngine();
//		
////		BirtUtil.renderReportToFile(
////				"/donnees/Projet/Java/Eclipse/GestionCommerciale_branche_2_0_13_JEE_E46/fr.legrain.bdg.webapp/src/main/webapp/reports/tiers/FicheTiers.rptdesign", 
////				"/tmp/tiers.pdf", 
////				new HashMap<>(), 
////				BirtUtil.PDF_FORMAT);
//		
//		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("/reports/tiers/FicheTiers.rptdesign");
//		BirtUtil.renderReportToFile(
//				inputStream, 
//				"/tmp/tiers.pdf", 
//				new HashMap<>(), 
//				BirtUtil.PDF_FORMAT);
		////////////////////////////////////////////////////////////////////////////////////////
		//java.net.URL.setURLStreamHandlerFactory();
//		taTiersService.generePDF(selectedTaInfoJuridiqueDTO.getId());
		////////////////////////////////////////////////////////////////////////////////////////
		
//			session.setAttribute("tiers", taTiersService.findById(selectedTaInfoJuridiqueDTO.getId()));
			System.out.println("TiersController.actImprimer()");
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}    

	public boolean pasDejaOuvert() {
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_TIERS);
		return tabsCenter.ongletDejaOuvert(b)==null;
	} 
	
	public void onRowSimpleSelect(SelectEvent event) {
		
		if(pasDejaOuvert()==false){
			onRowSelect(event);
			
			autoCompleteMapDTOtoUI();
			if(ConstWeb.DEBUG) {
		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage("Tiers", 
				"Chargement du tiers N°"
				)); }
		} else {
			tabViews.selectionneOngletCentre(LgrTab.TYPE_TAB_TIERS);
		}
	} 
	public void updateTab(){
		try {

//			if(selectedTaInfoJuridiqueDTOs!=null && selectedTaInfoJuridiqueDTOs.length>0) {
//				selectedTaInfoJuridiqueDTO = selectedTaInfoJuridiqueDTOs[0];
//			}
//			if(selectedTaInfoJuridiqueDTO.getId()!=null && selectedTaInfoJuridiqueDTO.getId()!=0) {
//				TaInfoJuridique = TaInfoJuridiqueService.findById(selectedTaInfoJuridiqueDTO.getId());
//			}
//			mapperModelToUI.map(TaInfoJuridique, selectedTaInfoJuridiqueDTO);			
			autoCompleteMapDTOtoUI();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void onRowSelect(SelectEvent event) {  
//		LgrTab b = new LgrTab();
//		b.setTypeOnglet(LgrTab.TYPE_TAB_TIERS);
//		//b.setTitre("Tiers "+selectedTaInfoJuridiqueDTO.getCodeTiers());
//		b.setTitre("Tiers");
//		b.setTemplate("tiers/tiers.xhtml");
//		b.setIdTab(LgrTab.ID_TAB_TIERS);
//		b.setStyle(LgrTab.CSS_CLASS_TAB_TIERS);
//		b.setParamId(LibConversion.integerToString(selectedTaInfoJuridiqueDTO.getId()));
//
//		tabsCenter.ajouterOnglet(b);
//		tabViews.selectionneOngletCentre(b);
//		
//		
		updateTab();
//		scrollToTop();
		
		if(ConstWeb.DEBUG) {
		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage("Tiers", 
				"Chargement du tiers N°"
				)); 
		}
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
			case "inserer":
			case "fermer":
				retour = false;
				break;
			case "supprimerListe":retour = false;break;	
			case "supprimer":
			case "modifier":
			case "imprimer":
				if(selectedTaInfoJuridiqueDTO!=null && selectedTaInfoJuridiqueDTO.getId()!=null  && selectedTaInfoJuridiqueDTO.getId()!=0 ) retour = false;
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

	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
	
		String messageComplet = "";
		
		try {

		  String nomChamp =  (String) component.getAttributes().get("nomChamp");
		  		  
//			//validation automatique via la JSR bean validation
		  ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		  Set<ConstraintViolation<TaInfoJuridiqueDTO>> violations = factory.getValidator().validateValue(TaInfoJuridiqueDTO.class,nomChamp,value);
			if (violations.size() > 0) {
				messageComplet = "Erreur de validation : ";

				for (ConstraintViolation<TaInfoJuridiqueDTO> cv : violations) {
					messageComplet+=" "+cv.getMessage()+"\n";
				}
				throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, messageComplet, messageComplet));

			} else {
				//aucune erreur de validation "automatique", on déclanche les validations du controller
					 validateUIField(nomChamp,value);
			}

		} catch(Exception e) {
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, messageComplet, messageComplet));
		}
	}
	
	
	public void autcompleteSelection(SelectEvent event) {
		Object value = event.getObject();
//		FacesMessage msg = new FacesMessage("Selected", "Item:" + value);
		
		String nomChamp =  (String) event.getComponent().getAttributes().get("nomChamp");

		if(value!=null) {
			if(value instanceof String && value.equals(Const.C_AUCUN))value="";	

		}
		
		validateUIField(nomChamp,value);
	}
	
	public boolean validateUIField(String nomChamp,Object value) {
		
		boolean changement=false;
		
		try {				
 
			return false;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private boolean infoJuridiqueEstRempli() {
		boolean retour=false;
		if((selectedTaInfoJuridiqueDTO.getApeInfoJuridique()!=null && !selectedTaInfoJuridiqueDTO.getApeInfoJuridique().equals(""))|| 
				(selectedTaInfoJuridiqueDTO.getCapitalInfoJuridique()!=null && !selectedTaInfoJuridiqueDTO.getCapitalInfoJuridique().equals(""))||
				(selectedTaInfoJuridiqueDTO.getRcsInfoJuridique()!=null && !selectedTaInfoJuridiqueDTO.getRcsInfoJuridique().equals(""))||
				(selectedTaInfoJuridiqueDTO.getSiretInfoJuridique()!=null && !selectedTaInfoJuridiqueDTO.getSiretInfoJuridique().equals("")))return true;
		return retour;
	}
	

	
	public TaInfoJuridiqueDTO[] getSelectedTaInfoJuridiqueDTOs() {
		return selectedTaInfoJuridiqueDTOs;
	}

	public void setSelectedTaInfoJuridiqueDTOs(TaInfoJuridiqueDTO[] selectedTaInfoJuridiqueDTOs) {
		this.selectedTaInfoJuridiqueDTOs = selectedTaInfoJuridiqueDTOs;
	}

	public TaInfoJuridiqueDTO getNewTaInfoJuridiqueDTO() {
		return newTaInfoJuridiqueDTO;
	}

	public void setNewTaInfoJuridiqueDTO(TaInfoJuridiqueDTO newTaInfoJuridiqueDTO) {
		this.newTaInfoJuridiqueDTO = newTaInfoJuridiqueDTO;
	}

	public TaInfoJuridiqueDTO getSelectedTaInfoJuridiqueDTO() {
		return selectedTaInfoJuridiqueDTO;
	}

	public void setSelectedTaInfoJuridiqueDTO(TaInfoJuridiqueDTO selectedTaInfoJuridiqueDTO) {
		this.selectedTaInfoJuridiqueDTO = selectedTaInfoJuridiqueDTO;
	}

	public TabListModelBean getTabsCenter() {
		return tabsCenter;
	}

	public void setTabsCenter(TabListModelBean tabsCenter) {
		this.tabsCenter = tabsCenter;
	}

	public String getParamId() {
		return paramId;
	}

	public void setParamId(String paramId) {
		this.paramId = paramId;
	} 


	public TabViewsBean getTabViews() {
		return tabViews;
	}

	public void setTabViews(TabViewsBean tabViews) {
		this.tabViews = tabViews;
	}

	public ModeObjetEcranServeur getModeEcran() {
		return modeEcran;
	}

	public void setModeEcran(ModeObjetEcranServeur modeEcran) {
		this.modeEcran = modeEcran;
	}
	

	public TaInfoJuridique getTaTiers() {
		return taInfoJuridique;
	}

	public void setTaTiers(TaInfoJuridique TaInfoJuridique) {
		this.taInfoJuridique = TaInfoJuridique;
	}

	public TabView getTabViewTiers() {
		return tabViewTiers;
	}

	public void setTabViewTiers(TabView tabViewTiers) {
		this.tabViewTiers = tabViewTiers;
	}

	public TaTiers getMasterEntity() {
		return masterEntity;
	}

	public void setMasterEntity(TaTiers masterEntity) {
		this.masterEntity = masterEntity;
	}


	public List<TaInfoJuridiqueDTO> getValuesFiltered() {
		return valuesFiltered;
	}

	public void setValuesFiltered(List<TaInfoJuridiqueDTO> valuesFiltered) {
		this.valuesFiltered = valuesFiltered;
	}


	public boolean insertionValide() {
		return values==null || values.size()==0;
	}


}  
