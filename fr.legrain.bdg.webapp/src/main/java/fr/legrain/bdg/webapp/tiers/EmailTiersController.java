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
import fr.legrain.bdg.tiers.service.remote.ITaEmailServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTAdrServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTEmailServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.bdg.webapp.ConstWeb;
import fr.legrain.bdg.webapp.app.AbstractController;
import fr.legrain.bdg.webapp.app.EmailParam;
import fr.legrain.bdg.webapp.app.LgrTab;
import fr.legrain.bdg.webapp.app.TabListModelBean;
import fr.legrain.bdg.webapp.app.TabViewsBean;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.ModeObjetEcranServeur;
import fr.legrain.tiers.dto.TaEmailDTO;
import fr.legrain.tiers.dto.TaTEmailDTO;
import fr.legrain.tiers.model.TaEmail;
import fr.legrain.tiers.model.TaTEmail;
import fr.legrain.tiers.model.TaTiers;

@Named
@ViewScoped  
public class EmailTiersController extends AbstractController implements Serializable { 
	

	@Inject @Named(value="tabListModelCenterBean")
	private TabListModelBean tabsCenter;
	
	@Inject @Named(value="tabViewsBean")
	private TabViewsBean tabViews;
	
	
	private String paramId;
	
	private TabView tabViewTiers; //Le Binding sur les onglets "secondaire" semble ne pas fonctionné, les onglets ne sont pas générés au bon moment avec le c:forEach

	private List<TaEmailDTO> values; 
	private List<TaEmailDTO> valuesFiltered; 
	
	protected ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();

	private @EJB ITaTiersServiceRemote taTiersService;
	private @EJB ITaTAdrServiceRemote taTTiersService;

	private @EJB ITaEmailServiceRemote TaEmailService;
	private @EJB ITaTAdrServiceRemote taTAdresseService;
	private @EJB ITaTEmailServiceRemote taTEmailService;
	
	private TaEmailDTO[] selectedTaEmailDTOs; 
	private TaEmail taEmail = new TaEmail();
	private TaEmailDTO newTaEmailDTO = new TaEmailDTO();
	private TaEmailDTO selectedTaEmailDTO = new TaEmailDTO();
//	private TaEmailDTO oldSelectedTaEmailDTO = new TaEmailDTO();
	
	private TaTEmail taTEmail;
	
	private LgrDozerMapper<TaEmailDTO,TaEmail> mapperUIToModel  = new LgrDozerMapper<TaEmailDTO,TaEmail>();
	private LgrDozerMapper<TaEmail,TaEmailDTO> mapperModelToUI  = new LgrDozerMapper<TaEmail,TaEmailDTO>();
	
	private TaTiers masterEntity;

	private TaTEmailDTO taTEmailDTO;
	
	public EmailTiersController() {  
		//values = getValues();
	}  

	@PostConstruct
	public void postConstruct(){

		refresh(null);
	}

	public void refresh(ActionEvent ev){
		selectedTaEmailDTO=null;
		 
   	    if(masterEntity!=null) {
   	    	try {
				if(masterEntity.getIdTiers()!=0) masterEntity = taTiersService.findById(masterEntity.getIdTiers());
			} catch (FinderException e) {
				
			}
			values = new ArrayList<>();
			for (TaEmail taEmail : masterEntity.getTaEmails()) {
				TaEmailDTO dto=new TaEmailDTO();
				if(masterEntity.getTaEmail().equals(taEmail)){
					dto.setDefaut(true);
				}
				mapperModelToUI.map(taEmail, dto);
				values.add(dto);
			}

			if(!values.isEmpty()&&selectedTaEmailDTO==null)
				selectedTaEmailDTO=values.get(0);
		}
		autoCompleteMapDTOtoUI();
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}

	public List<TaEmailDTO> getValues(){  
		return values;
	}
	


	public void actAnnuler(ActionEvent actionEvent) {
		try {
			switch (modeEcran.getMode()) {
			case C_MO_INSERTION:
				
				taEmail = new TaEmail();
				mapperModelToUI.map(taEmail,selectedTaEmailDTO );
				selectedTaEmailDTO=new TaEmailDTO();
				
				if(!values.isEmpty()) selectedTaEmailDTO = values.get(0);
				onRowSelect(null);
				break;
			case C_MO_EDITION:
				if(selectedTaEmailDTO.getId()!=null && selectedTaEmailDTO.getId()!=0){
					taEmail = TaEmailService.findById(selectedTaEmailDTO.getId());
					selectedTaEmailDTO=TaEmailService.findByIdDTO(selectedTaEmailDTO.getId());
				}				
				break;

			default:
				break;
			}
			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
			mapperModelToUI.map(taEmail, selectedTaEmailDTO);

			if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Tiers", "actAnnuler"));
			}
		} catch (FinderException e) {
			e.printStackTrace();
		}		
	}
	
	public void autoCompleteMapUIToDTO() {
		if(taTEmailDTO!=null) {
			validateUIField(Const.C_CODE_T_EMAIL,taTEmailDTO.getCodeTEmail());
			selectedTaEmailDTO.setCodeTEmail(taTEmailDTO.getCodeTEmail());
		} else {
			selectedTaEmailDTO.setCodeTEmail(null);
		}
		
	}
	
	public void autoCompleteMapDTOtoUI() {
		try {
			taTEmailDTO = null;
			if(selectedTaEmailDTO!=null){
				if(selectedTaEmailDTO.getCodeTEmail()!=null && !selectedTaEmailDTO.getCodeTEmail().equals("")) {
					taTEmailDTO=taTEmailService.findByCodeDTO(selectedTaEmailDTO.getCodeTEmail());
				}	
			}

		} catch (FinderException e) {
			e.printStackTrace();
		}
	}

	public TaEmail rechercheEmailDansTiers(TaEmailDTO dto){
		for (TaEmail mail : masterEntity.getTaEmails()) {
			if(mail.getIdEmail()==dto.getId()) {
				return mail;
			}
		}
		return null;
	}
	

	

	
	public void actEnregistrer(ActionEvent actionEvent) {		
		//TaEmail TaEmail = new TaEmail();

		autoCompleteMapUIToDTO();
//		initAdresseNull();
				
		mapperUIToModel.map(selectedTaEmailDTO, taEmail);
		
		/*
		 * A faire, mapper les objets dans TaEmail avant le merge :
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
			if(selectedTaEmailDTO.isDefaut())masterEntity.setTaEmail(taEmail);
			taEmail.setTaTiers(masterEntity);
			masterEntity.addEmail(taEmail);	
			
			//si null on met par défaut Bureau
			if(taEmail.getTaTEmail()==null){
				TaTEmail taTEmail= taTEmailService.findByCode(Const.C_TYPEEMAILBUREAU);
				taEmail.setTaTEmail(taTEmail);
			}
			
			if(taEmail.getCommAdministratifEmail()==null) {
				taEmail.setCommAdministratifEmail(1);
			}
			if(taEmail.getCommCommercialEmail()==null) {
				taEmail.setCommCommercialEmail(1);
			}
		
			
			taEmail = TaEmailService.merge(taEmail,ITaTiersServiceRemote.validationContext);
			
			mapperModelToUI.map(taEmail, selectedTaEmailDTO);
			autoCompleteMapDTOtoUI();
			
			if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_INSERTION)==0) {
				values.add(selectedTaEmailDTO);
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

				selectedTaEmailDTO = new TaEmailDTO();
				taEmail = new TaEmail();
				String codeEmailDefaut = "B";
		
		
					TaTEmail taTEmail;
					
					taTEmail = taTEmailService.findByCode(codeEmailDefaut);
					

					taEmail.setCommAdministratifEmail(0);
					taEmail.setCommCommercialEmail(0);
					
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
			//TaEmail = TaEmailService.findById(selectedTaEmailDTO.getId());
			for (TaEmail mail : masterEntity.getTaEmails()) {
				if(mail.getIdEmail()==selectedTaEmailDTO.getId()) {
					taEmail = mail;
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
		TaEmail taEmail = new TaEmail();
		try {
			if(selectedTaEmailDTO!=null && selectedTaEmailDTO.getId()!=null){
				taEmail = TaEmailService.findById(selectedTaEmailDTO.getId());
			}

			TaEmailService.remove(taEmail);
			values.remove(selectedTaEmailDTO);
			
			if(!values.isEmpty()) {
				selectedTaEmailDTO = values.get(0);
			}else {
				selectedTaEmailDTO=new TaEmailDTO();
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
		selectedTaEmailDTO=new TaEmailDTO();
		selectedTaEmailDTOs=new TaEmailDTO[]{selectedTaEmailDTO};
	
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
		sessionMap.put("tiers", taTiersService.findById(selectedTaEmailDTO.getId()));
		
		////////////////////////////////////////////////////////////////////////////////////////
		//Test génération de fichier PDF
		
//		HashMap<String,Object> hm = new HashMap<>();
//		hm.put( "tiers", taTiersService.findById(selectedTaEmailDTO.getId()));
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
//		taTiersService.generePDF(selectedTaEmailDTO.getId());
		////////////////////////////////////////////////////////////////////////////////////////
		
//			session.setAttribute("tiers", taTiersService.findById(selectedTaEmailDTO.getId()));
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

//			if(selectedTaEmailDTOs!=null && selectedTaEmailDTOs.length>0) {
//				selectedTaEmailDTO = selectedTaEmailDTOs[0];
//			}
//			if(selectedTaEmailDTO.getId()!=null && selectedTaEmailDTO.getId()!=0) {
//				TaEmail = TaEmailService.findById(selectedTaEmailDTO.getId());
//			}
//			mapperModelToUI.map(TaEmail, selectedTaEmailDTO);			
			autoCompleteMapDTOtoUI();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void onRowSelect(SelectEvent event) {  
//		LgrTab b = new LgrTab();
//		b.setTypeOnglet(LgrTab.TYPE_TAB_TIERS);
//		//b.setTitre("Tiers "+selectedTaEmailDTO.getCodeTiers());
//		b.setTitre("Tiers");
//		b.setTemplate("tiers/tiers.xhtml");
//		b.setIdTab(LgrTab.ID_TAB_TIERS);
//		b.setStyle(LgrTab.CSS_CLASS_TAB_TIERS);
//		b.setParamId(LibConversion.integerToString(selectedTaEmailDTO.getId()));
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
	
	
public void actDialogEmail(ActionEvent actionEvent) {
	    
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
		
		EmailParam email = new EmailParam();
		email.setEmailExpediteur(null);
		
//		selectedTaEmailDTO.getAdresseEmail();
//		selectedTaEmailDTO;
		TaEmail emailFromDTO;
		try {
			emailFromDTO = TaEmailService.findById(selectedTaEmailDTO.getId());
			email.setDestinataires(new String[]{emailFromDTO.getAdresseEmail()});
			email.setEmailDestinataires(new TaEmail[]{emailFromDTO});
			
			
			sessionMap.put(EmailParam.NOM_OBJET_EN_SESSION, email);
	        
	        PrimeFaces.current().dialog().openDynamic("/dialog_email", options, params);
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			    
	}

public void actDialogEmail(TaEmailDTO mail) {
    
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
	
	EmailParam email = new EmailParam();
	email.setEmailExpediteur(null);
	
//	selectedTaEmailDTO.getAdresseEmail();
//	selectedTaEmailDTO;
	TaEmail emailFromDTO;
	try {
		emailFromDTO = TaEmailService.findById(mail.getId());
		email.setDestinataires(new String[]{emailFromDTO.getAdresseEmail()});
		email.setEmailDestinataires(new TaEmail[]{emailFromDTO});
		
		
		sessionMap.put(EmailParam.NOM_OBJET_EN_SESSION, email);
        
        PrimeFaces.current().dialog().openDynamic("/dialog_email", options, params);
	} catch (FinderException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		    
}
	
	
	
	public void actDialogTypeEmail(ActionEvent actionEvent) {
		
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
        
        PrimeFaces.current().dialog().openDynamic("tiers/dialog_type_email", options, params);
		
//		FacesContext context = FacesContext.getCurrentInstance();  
//		context.addMessage(null, new FacesMessage("Tiers", "actAbout")); 	    
	}
	
	
	public void handleReturnDialogTypeEmail(SelectEvent event) {
		
		if(event!=null && event.getObject()!=null) {
			taTEmail = (TaTEmail) event.getObject();
			
			try {
				taTEmailDTO = taTEmailService.findByCodeDTO(taTEmail.getCodeTEmail());
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
				if(selectedTaEmailDTO!=null && selectedTaEmailDTO.getId()!=null  && selectedTaEmailDTO.getId()!=0 ) retour = false;
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

	public void validateEmail(FacesContext context, UIComponent component, Object value) throws ValidatorException {
	
		String messageComplet = "";
		
		try {

		  String nomChamp =  (String) component.getAttributes().get("nomChamp");
		  		  
//			//validation automatique via la JSR bean validation
		  ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		  Set<ConstraintViolation<TaEmailDTO>> violations = factory.getValidator().validateValue(TaEmailDTO.class,nomChamp,value);
			if (violations.size() > 0) {
				messageComplet = "Erreur de validation : ";

				for (ConstraintViolation<TaEmailDTO> cv : violations) {
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
			if(value instanceof TaTEmailDTO && ((TaTEmailDTO) value).getCodeTEmail()!=null && ((TaTEmailDTO) value).getCodeTEmail().equals(Const.C_AUCUN))value=null;

		}
		
		validateUIField(nomChamp,value);
	}
	
	public boolean validateUIField(String nomChamp,Object value) {
		
		boolean changement=false;
		
		try {				
			if(nomChamp.equals(Const.C_CODE_T_EMAIL)) {
				TaTEmail entity = null;
				if(value!=null){
					if(value instanceof TaTEmail){
						entity=(TaTEmail) value;
	//								entity = taTTiersService.findByCode(entity.getCodeTTiers());
					} else if(value instanceof TaTEmailDTO){
							entity = taTEmailService.findByCode(((TaTEmailDTO) value).getCodeTEmail());
					}else{
						entity = taTEmailService.findByCode((String)value);
					}
				} else {
					selectedTaEmailDTO.setCodeTEmail("");
					taTEmailDTO.setCodeTEmail("");
					taTEmail=null;
				}						
				taEmail.setTaTEmail(entity);
			} 
			return false;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private boolean emailEstRempli() {
		boolean retour=false;
		if(selectedTaEmailDTO.getAdresseEmail()!=null && !selectedTaEmailDTO.getAdresseEmail().equals(""))return true;
//		if(selectedTaEmailDTO.getCommAdministratifEmail()!=null || selectedTaEmailDTO.getCommCommercialEmail() != null)return true;
		return retour;
	}
	
	public List<TaTEmailDTO> typeEmailAutoCompleteLight(String query) {
        List<TaTEmailDTO> allValues = taTEmailService.selectAllDTO();
        List<TaTEmailDTO> filteredValues = new ArrayList<TaTEmailDTO>();
         
        for (int i = 0; i < allValues.size(); i++) {
        	TaTEmailDTO civ = allValues.get(i);
            if(query.equals("*") || civ.getCodeTEmail().toLowerCase().contains(query.toLowerCase())) {
                filteredValues.add(civ);
            }
        }
         
        return filteredValues;
    }
	
	public TaEmailDTO[] getSelectedTaEmailDTOs() {
		return selectedTaEmailDTOs;
	}

	public void setSelectedTaEmailDTOs(TaEmailDTO[] selectedTaEmailDTOs) {
		this.selectedTaEmailDTOs = selectedTaEmailDTOs;
	}

	public TaEmailDTO getNewTaEmailDTO() {
		return newTaEmailDTO;
	}

	public void setNewTaEmailDTO(TaEmailDTO newTaEmailDTO) {
		this.newTaEmailDTO = newTaEmailDTO;
	}

	public TaEmailDTO getSelectedTaEmailDTO() {
		return selectedTaEmailDTO;
	}

	public void setSelectedTaEmailDTO(TaEmailDTO selectedTaEmailDTO) {
		this.selectedTaEmailDTO = selectedTaEmailDTO;
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
	
	public TaTEmailDTO getTaTEmailDTO() {
		return taTEmailDTO;
	}

	public void setTaTEmailDTO(TaTEmailDTO taTEmailDTO) {
		this.taTEmailDTO = taTEmailDTO;
	}

	public TaEmail getTaTiers() {
		return taEmail;
	}

	public void setTaTiers(TaEmail TaEmail) {
		this.taEmail = TaEmail;
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


	public List<TaEmailDTO> getValuesFiltered() {
		return valuesFiltered;
	}

	public void setValuesFiltered(List<TaEmailDTO> valuesFiltered) {
		this.valuesFiltered = valuesFiltered;
	}

	public TaTEmail getTaTEmail() {
		return taTEmail;
	}

	public void setTaTEmail(TaTEmail taTEmail) {
		this.taTEmail = taTEmail;
	}



}  
