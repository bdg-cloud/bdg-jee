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
import fr.legrain.bdg.tiers.service.remote.ITaTAdrServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTTelServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTelephoneServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.bdg.webapp.ConstWeb;
import fr.legrain.bdg.webapp.app.AbstractController;
import fr.legrain.bdg.webapp.app.LgrTab;
import fr.legrain.bdg.webapp.app.SmsParam;
import fr.legrain.bdg.webapp.app.TabListModelBean;
import fr.legrain.bdg.webapp.app.TabViewsBean;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.ModeObjetEcranServeur;
import fr.legrain.tiers.dto.TaTTelDTO;
import fr.legrain.tiers.dto.TaTelephoneDTO;
import fr.legrain.tiers.model.TaTTel;
import fr.legrain.tiers.model.TaTelephone;
import fr.legrain.tiers.model.TaTiers;

@Named
@ViewScoped  
public class TelephoneController extends AbstractController implements Serializable { 
	

	@Inject @Named(value="tabListModelCenterBean")
	private TabListModelBean tabsCenter;
	
	@Inject @Named(value="tabViewsBean")
	private TabViewsBean tabViews;
	
	
	private String paramId;
	
	private TabView tabViewTiers; //Le Binding sur les onglets "secondaire" semble ne pas fonctionné, les onglets ne sont pas générés au bon moment avec le c:forEach

	private List<TaTelephoneDTO> values; 
	private List<TaTelephoneDTO> valuesFiltered; 
	
	protected ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();

	private @EJB ITaTiersServiceRemote taTiersService;
	private @EJB ITaTAdrServiceRemote taTTiersService;

	private @EJB ITaTelephoneServiceRemote TaTelephoneService;
	private @EJB ITaTAdrServiceRemote taTAdresseService;
	private @EJB ITaTTelServiceRemote taTTelService;
	
	private TaTelephoneDTO[] selectedTaTelephoneDTOs; 
	private TaTelephone taTelephone = new TaTelephone();
	private TaTelephoneDTO newTaTelephoneDTO = new TaTelephoneDTO();
	private TaTelephoneDTO selectedTaTelephoneDTO = new TaTelephoneDTO();
//	private TaTelephoneDTO oldSelectedTaTelephoneDTO = new TaTelephoneDTO();
	
	private TaTTel taTTel;
	
	private LgrDozerMapper<TaTelephoneDTO,TaTelephone> mapperUIToModel  = new LgrDozerMapper<TaTelephoneDTO,TaTelephone>();
	private LgrDozerMapper<TaTelephone,TaTelephoneDTO> mapperModelToUI  = new LgrDozerMapper<TaTelephone,TaTelephoneDTO>();
	
	private TaTiers masterEntity;

	private TaTTelDTO taTTelDTO;
	
	public TelephoneController() {  
		//values = getValues();
	}  

	@PostConstruct
	public void postConstruct(){

		refresh(null);
	}

	public void refresh(ActionEvent ev){
		selectedTaTelephoneDTO=null;
		 
   	    if(masterEntity!=null) {
   	    	try {
				if(masterEntity.getIdTiers()!=0) masterEntity = taTiersService.findById(masterEntity.getIdTiers());
			} catch (FinderException e) {
				
			}
			values = new ArrayList<>();
			for (TaTelephone taTelephone : masterEntity.getTaTelephones()) {
				TaTelephoneDTO dto=new TaTelephoneDTO();
				if(masterEntity.getTaTelephone()!=null) {
					if(masterEntity.getTaTelephone().equals(taTelephone)){
						dto.setDefaut(true);
					}
				}
				
				mapperModelToUI.map(taTelephone, dto);
				values.add(dto);
			}

			if(!values.isEmpty()&&selectedTaTelephoneDTO==null)
				selectedTaTelephoneDTO=values.get(0);
		}
		autoCompleteMapDTOtoUI();
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}

	public List<TaTelephoneDTO> getValues(){  
		return values;
	}
	


	public void actAnnuler(ActionEvent actionEvent) {
		try {
			switch (modeEcran.getMode()) {
			case C_MO_INSERTION:
				
				taTelephone = new TaTelephone();
				mapperModelToUI.map(taTelephone,selectedTaTelephoneDTO );
				selectedTaTelephoneDTO=new TaTelephoneDTO();
				
				if(!values.isEmpty()) selectedTaTelephoneDTO = values.get(0);
				onRowSelect(null);
				break;
			case C_MO_EDITION:
				if(selectedTaTelephoneDTO.getId()!=null && selectedTaTelephoneDTO.getId()!=0){
					taTelephone = TaTelephoneService.findById(selectedTaTelephoneDTO.getId());
					selectedTaTelephoneDTO=TaTelephoneService.findByIdDTO(selectedTaTelephoneDTO.getId());
				}				
				break;

			default:
				break;
			}
			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
			mapperModelToUI.map(taTelephone, selectedTaTelephoneDTO);

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
		if(taTTelDTO!=null) {
			validateUIField(Const.C_CODE_T_TEL,taTTelDTO.getCodeTTel());
			selectedTaTelephoneDTO.setCodeTTel(taTTelDTO.getCodeTTel());
		} else {
			selectedTaTelephoneDTO.setCodeTTel(null);
		}
		
	}
	
	public void autoCompleteMapDTOtoUI() {
		try {
			taTTelDTO = null;
			if(selectedTaTelephoneDTO!=null){
				if(selectedTaTelephoneDTO.getCodeTTel()!=null && !selectedTaTelephoneDTO.getCodeTTel().equals("")) {
					taTTelDTO=taTTelService.findByCodeDTO(selectedTaTelephoneDTO.getCodeTTel());
				}	
			}

		} catch (FinderException e) {
			e.printStackTrace();
		}
	}

	public TaTelephone rechercheTelephoneDansTiers(TaTelephoneDTO dto){
		for (TaTelephone tel : masterEntity.getTaTelephones()) {
			if(tel.getIdTelephone()==dto.getId()) {
				return tel;
			}
		}
		return null;
	}
	

	

	
	public void actEnregistrer(ActionEvent actionEvent) {		
		//TaTelephone TaTelephone = new TaTelephone();

		autoCompleteMapUIToDTO();
//		initAdresseNull();
				
		mapperUIToModel.map(selectedTaTelephoneDTO, taTelephone);
		
		/*
		 * A faire, mapper les objets dans TaTelephone avant le merge :
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
			if(selectedTaTelephoneDTO.isDefaut())masterEntity.setTaTelephone(taTelephone);
			taTelephone.setTaTiers(masterEntity);
			masterEntity.addTelephone(taTelephone);	
			
			//si null on met par défaut Fact
			if(taTelephone.getTaTTel()==null){
				TaTTel taTTel= taTTelService.findByCode(Const.C_TYPETELEPHONEPORTABLE);
				taTelephone.setTaTTel(taTTel);
			}
			
			if(taTelephone.getCommAdministratifTelephone()==null) {
				taTelephone.setCommAdministratifTelephone(1);
			}
			if(taTelephone.getCommCommercialTelephone()==null) {
				taTelephone.setCommCommercialTelephone(1);
			}
		
			
			taTelephone = TaTelephoneService.merge(taTelephone,ITaTiersServiceRemote.validationContext);
			
			mapperModelToUI.map(taTelephone, selectedTaTelephoneDTO);
			autoCompleteMapDTOtoUI();
			
			if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_INSERTION)==0) {
				values.add(selectedTaTelephoneDTO);
			}

			masterEntity = taTiersService.findById(masterEntity.getIdTiers());
			
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

				selectedTaTelephoneDTO = new TaTelephoneDTO();
				taTelephone = new TaTelephone();
				String codeTelDefaut = "P";
		
		
					TaTTel taTTel;
					
					taTTel = taTTelService.findByCode(codeTelDefaut);
					

					taTelephone.setCommAdministratifTelephone(0);
					taTelephone.setCommCommercialTelephone(0);
					
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
			//TaTelephone = TaTelephoneService.findById(selectedTaTelephoneDTO.getId());
			for (TaTelephone tel : masterEntity.getTaTelephones()) {
				if(tel.getIdTelephone()==selectedTaTelephoneDTO.getId()) {
					taTelephone = tel;
				}
			}
		
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
		TaTelephone taTelephone = new TaTelephone();
		try {
			if(selectedTaTelephoneDTO!=null && selectedTaTelephoneDTO.getId()!=null){
				taTelephone = TaTelephoneService.findById(selectedTaTelephoneDTO.getId());
			}

			TaTelephoneService.remove(taTelephone);
			values.remove(selectedTaTelephoneDTO);
			
			if(!values.isEmpty()) {
				selectedTaTelephoneDTO = values.get(0);
			}else {
				selectedTaTelephoneDTO=new TaTelephoneDTO();
			}
			updateTab();
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
		selectedTaTelephoneDTO=new TaTelephoneDTO();
		selectedTaTelephoneDTOs=new TaTelephoneDTO[]{selectedTaTelephoneDTO};
	
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
		sessionMap.put("tiers", taTiersService.findById(selectedTaTelephoneDTO.getId()));
		
		////////////////////////////////////////////////////////////////////////////////////////
		//Test génération de fichier PDF
		
//		HashMap<String,Object> hm = new HashMap<>();
//		hm.put( "tiers", taTiersService.findById(selectedTaTelephoneDTO.getId()));
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
//		taTiersService.generePDF(selectedTaTelephoneDTO.getId());
		////////////////////////////////////////////////////////////////////////////////////////
		
//			session.setAttribute("tiers", taTiersService.findById(selectedTaTelephoneDTO.getId()));
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

//			if(selectedTaTelephoneDTOs!=null && selectedTaTelephoneDTOs.length>0) {
//				selectedTaTelephoneDTO = selectedTaTelephoneDTOs[0];
//			}
//			if(selectedTaTelephoneDTO.getId()!=null && selectedTaTelephoneDTO.getId()!=0) {
//				TaTelephone = TaTelephoneService.findById(selectedTaTelephoneDTO.getId());
//			}
//			mapperModelToUI.map(TaTelephone, selectedTaTelephoneDTO);			
			autoCompleteMapDTOtoUI();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void onRowSelect(SelectEvent event) {  
//		LgrTab b = new LgrTab();
//		b.setTypeOnglet(LgrTab.TYPE_TAB_TIERS);
//		//b.setTitre("Tiers "+selectedTaTelephoneDTO.getCodeTiers());
//		b.setTitre("Tiers");
//		b.setTemplate("tiers/tiers.xhtml");
//		b.setIdTab(LgrTab.ID_TAB_TIERS);
//		b.setStyle(LgrTab.CSS_CLASS_TAB_TIERS);
//		b.setParamId(LibConversion.integerToString(selectedTaTelephoneDTO.getId()));
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
	
	public void actDialogSms(ActionEvent actionEvent) {
	    
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
		
		SmsParam email = new SmsParam();
		email.setNumeroExpediteur(null);


		
		TaTelephone smsFromDTO;
		try {
			smsFromDTO = TaTelephoneService.findById(selectedTaTelephoneDTO.getId());
			email.setDestinataires(new String[]{smsFromDTO.getNumeroTelephone()});
			email.setTelephoneDestinataires(new TaTelephone[]{smsFromDTO});
			
			
			sessionMap.put(SmsParam.NOM_OBJET_EN_SESSION, email);
	        
	        PrimeFaces.current().dialog().openDynamic("/dialog_sms", options, params);
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 
	}
	
	
public void actDialogSms(TaTelephoneDTO tel) {
	    
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
		
		SmsParam smsParam = new SmsParam();
		smsParam.setNumeroExpediteur(null);


		
		TaTelephone smsFromDTO;
		try {
			smsFromDTO = TaTelephoneService.findById(tel.getId());
			smsParam.setDestinataires(new String[]{smsFromDTO.getNumeroTelephone()});
			smsParam.setTelephoneDestinataires(new TaTelephone[]{smsFromDTO});
			
			
			sessionMap.put(SmsParam.NOM_OBJET_EN_SESSION, smsParam);
	        
	        PrimeFaces.current().dialog().openDynamic("/dialog_sms", options, params);
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 
	}
	
	
	
	public void actDialogTypeTel(ActionEvent actionEvent) {
		
//		PrimeFaces.current().dialog().openDynamic("aide");
    
        Map<String,Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("draggable", false);
        options.put("resizable", false);
        options.put("contentHeight", 640);
        options.put("modal", true);
        
        //Map<String,List<String>> params = null;
        Map<String,List<String>> params = new HashMap<String,List<String>>();
        List<String> list = new ArrayList<String>();
        list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
        params.put("modeEcranDefaut", list);
        
        PrimeFaces.current().dialog().openDynamic("tiers/dialog_type_telephone", options, params);
		
//		FacesContext context = FacesContext.getCurrentInstance();  
//		context.addMessage(null, new FacesMessage("Tiers", "actAbout")); 	    
	}
	
	
	public void handleReturnDialogTypeTel(SelectEvent event) {
		
		if(event!=null && event.getObject()!=null) {
			taTTel = (TaTTel) event.getObject();
			
			try {
				taTTelDTO = taTTelService.findByCodeDTO(taTTel.getCodeTTel());
			} catch (FinderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
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
				if(selectedTaTelephoneDTO!=null && selectedTaTelephoneDTO.getId()!=null  && selectedTaTelephoneDTO.getId()!=0 ) retour = false;
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

	public void validateTel(FacesContext context, UIComponent component, Object value) throws ValidatorException {
	
		String messageComplet = "";
		
		try {

		  String nomChamp =  (String) component.getAttributes().get("nomChamp");
		  		  
//			//validation automatique via la JSR bean validation
		  ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		  Set<ConstraintViolation<TaTelephoneDTO>> violations = factory.getValidator().validateValue(TaTelephoneDTO.class,nomChamp,value);
			if (violations.size() > 0) {
				messageComplet = "Erreur de validation : ";

				for (ConstraintViolation<TaTelephoneDTO> cv : violations) {
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
			if(value instanceof TaTTelDTO && ((TaTTelDTO) value).getCodeTTel()!=null && ((TaTTelDTO) value).getCodeTTel().equals(Const.C_AUCUN))value=null;

		}
		
		validateUIField(nomChamp,value);
	}
	
	public boolean validateUIField(String nomChamp,Object value) {
		
		boolean changement=false;
		
		try {				
			if(nomChamp.equals(Const.C_CODE_T_TEL)) {
				TaTTel entity = null;
				if(value!=null){
					if(value instanceof TaTTel){
						entity=(TaTTel) value;
	//								entity = taTTiersService.findByCode(entity.getCodeTTiers());
					} else if(value instanceof TaTTelDTO){
							entity = taTTelService.findByCode(((TaTTelDTO) value).getCodeTTel());
					}else{
						entity = taTTelService.findByCode((String)value);
					}
				} else {
					selectedTaTelephoneDTO.setCodeTTel("");
					taTTelDTO.setCodeTTel("");
					taTTel=null;
				}						
				taTelephone.setTaTTel(entity);
			} 
			return false;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private boolean telEstRempli() {
		boolean retour=false;
		if(selectedTaTelephoneDTO.getNumeroTelephone()!=null && !selectedTaTelephoneDTO.getNumeroTelephone().equals(""))return true;
//		if(selectedTaTelephoneDTO.getCommAdministratifTelephone()!=null || selectedTaTelephoneDTO.getCommCommercialTelephone() != null)return true;
		return retour;
	}
	
	public List<TaTTelDTO> typeTelAutoCompleteLight(String query) {
        List<TaTTelDTO> allValues = taTTelService.selectAllDTO();
        List<TaTTelDTO> filteredValues = new ArrayList<TaTTelDTO>();
         
        for (int i = 0; i < allValues.size(); i++) {
        	TaTTelDTO civ = allValues.get(i);
            if(query.equals("*") || civ.getCodeTTel().toLowerCase().contains(query.toLowerCase())) {
                filteredValues.add(civ);
            }
        }
         
        return filteredValues;
    }
	
	public TaTelephoneDTO[] getSelectedTaTelephoneDTOs() {
		return selectedTaTelephoneDTOs;
	}

	public void setSelectedTaTelephoneDTOs(TaTelephoneDTO[] selectedTaTelephoneDTOs) {
		this.selectedTaTelephoneDTOs = selectedTaTelephoneDTOs;
	}

	public TaTelephoneDTO getNewTaTelephoneDTO() {
		return newTaTelephoneDTO;
	}

	public void setNewTaTelephoneDTO(TaTelephoneDTO newTaTelephoneDTO) {
		this.newTaTelephoneDTO = newTaTelephoneDTO;
	}

	public TaTelephoneDTO getSelectedTaTelephoneDTO() {
		return selectedTaTelephoneDTO;
	}

	public void setSelectedTaTelephoneDTO(TaTelephoneDTO selectedTaTelephoneDTO) {
		this.selectedTaTelephoneDTO = selectedTaTelephoneDTO;
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
	
	public TaTTelDTO getTaTTelDTO() {
		return taTTelDTO;
	}

	public void setTaTTelDTO(TaTTelDTO taTTelDTO) {
		this.taTTelDTO = taTTelDTO;
	}

	public TaTelephone getTaTiers() {
		return taTelephone;
	}

	public void setTaTiers(TaTelephone TaTelephone) {
		this.taTelephone = TaTelephone;
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


	public List<TaTelephoneDTO> getValuesFiltered() {
		return valuesFiltered;
	}

	public void setValuesFiltered(List<TaTelephoneDTO> valuesFiltered) {
		this.valuesFiltered = valuesFiltered;
	}

	public TaTTel getTaTTel() {
		return taTTel;
	}

	public void setTaTTel(TaTTel taTTel) {
		this.taTTel = taTTel;
	}



}  
