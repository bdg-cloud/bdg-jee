package fr.legrain.bdg.webapp.app;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.UnselectEvent;

import fr.legrain.article.dto.TaFamilleDTO;
import fr.legrain.article.dto.TaTTvaDTO;
import fr.legrain.article.dto.TaTvaDTO;
import fr.legrain.article.dto.TaUniteDTO;
import fr.legrain.article.model.TaMarqueArticle;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.bdg.droits.service.remote.ITaUtilisateurServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaCompteServiceWebExterneServiceRemote;
import fr.legrain.bdg.sms.service.remote.ITaMessageSMSServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTelephoneServiceRemote;
import fr.legrain.bdg.webapp.ConstWeb;
import fr.legrain.document.dto.TaTPaiementDTO;
import fr.legrain.document.model.TaTPaiement;
import fr.legrain.droits.service.SessionInfo;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.ModeObjetEcranServeur;
import fr.legrain.sms.dto.TaContactMessageSMSDTO;
import fr.legrain.sms.dto.TaMessageSMSDTO;
import fr.legrain.sms.model.TaContactMessageSMS;
import fr.legrain.sms.model.TaMessageSMS;
import fr.legrain.sms.service.EnvoiSmsService;
import fr.legrain.tiers.dto.TaCPaiementDTO;
import fr.legrain.tiers.dto.TaTCiviliteDTO;
import fr.legrain.tiers.dto.TaTEntiteDTO;
import fr.legrain.tiers.dto.TaTTelDTO;
import fr.legrain.tiers.dto.TaTelephoneDTO;
import fr.legrain.tiers.model.TaCPaiement;
import fr.legrain.tiers.model.TaFamilleTiers;
import fr.legrain.tiers.model.TaTTel;
import fr.legrain.tiers.model.TaTTvaDoc;
import fr.legrain.tiers.model.TaTelephone;
import fr.legrain.tiers.model.TaTiers;

@Named
@ViewScoped  
public class SmsController extends AbstractController implements Serializable {  

	@Inject @Named(value="tabListModelCenterBean")
	private TabListModelBean tabsCenter;

	@Inject @Named(value="tabViewsBean")
	private TabViewsBean tabViews;

	private String paramId;
	
	private String cssEtiquetteEmail;
	
	private TaTiers masterEntity;

	private TabView tabViewTiers; //Le Binding sur les onglets "secondaire" semble ne pas fonctionné, les onglets ne sont pas générés au bon moment avec le c:forEach

	private List<TaMessageSMSDTO> values; 

	protected ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();

//	private @EJB LgrEmailSMTPService lgrEmail;

//	private @EJB LgrMailjetService lgrMailjetService;
	@EJB private EnvoiSmsService envoiSmsService;
	protected @EJB ITaInfoEntrepriseServiceRemote taInfoEntrepriseService;

	private @EJB ITaMessageSMSServiceRemote taTiersService;
	private @EJB ITaUtilisateurServiceRemote taUtilisateurService;
	private @EJB ITaMessageSMSServiceRemote taMessageSMSService;

	private @EJB ITaCompteServiceWebExterneServiceRemote taCompteServiceWebExterneService;
	private @EJB ITaTelephoneServiceRemote taTelephoneService;

	private @Inject SessionInfo sessionInfo;

	private TaMessageSMSDTO[] selectedTaMessageSMSDTOs; 
	private TaMessageSMS taMessageSMS = new TaMessageSMS();
	private TaMessageSMSDTO newTaMessageSMSDTO = new TaMessageSMSDTO();
	private TaMessageSMSDTO selectedTaMessageSMSDTO = new TaMessageSMSDTO();

	private TaTelephone taTelephone;
	private TaTTel taTTel;

	private LgrDozerMapper<TaMessageSMSDTO,TaMessageSMS> mapperUIToModel  = new LgrDozerMapper<TaMessageSMSDTO,TaMessageSMS>();
	private LgrDozerMapper<TaMessageSMS,TaMessageSMSDTO> mapperModelToUI  = new LgrDozerMapper<TaMessageSMS,TaMessageSMSDTO>();
	
	private LgrDozerMapper<TaTelephoneDTO,TaTelephone> mapperUIToModelTaTelephone  = new LgrDozerMapper<TaTelephoneDTO,TaTelephone>();
	private LgrDozerMapper<TaTelephone,TaTelephoneDTO> mapperModelToUITaTelephone  = new LgrDozerMapper<TaTelephone,TaTelephoneDTO>();

	private TaTelephoneDTO taTelephoneDTODestinataire;
	
	private TaTTelDTO taTTelDTO;

	private List<TaTelephoneDTO> taTelephoneDTODestinataires;
	
	private List<String> numerosDestinataire;

	private boolean showPlusDestinataire = false;
	
	private SmsParam param;

	
	public SmsController() {  
		
	}  

	@PostConstruct
	public void postConstruct(){
		refresh();
	}

	public void refresh(){
//		values = taMessageSMSService.findAllLight();
		if(masterEntity==null) {
			values = taMessageSMSService.selectAllDTO();
		} else {
			values = taMessageSMSService.selectAllDTO(masterEntity.getIdTiers());
			//masterEntity=null;
		}
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}

	public List<TaMessageSMSDTO> getValues(){  
		return values;
	}
	

	public void actAnnuler(ActionEvent actionEvent) {
		try {
//			switch (modeEcran.getMode()) {
//			case C_MO_INSERTION:
//				if(selectedTaMessageSMSDTO.getCodeTiers()!=null) {
//					taTiersService.annuleCode(selectedTaMessageSMSDTO.getCodeTiers());
//				}
//				taTiers = new TaMessageSMS();
//				mapperModelToUI.map(taTiers,selectedTaMessageSMSDTO );
//				selectedTaMessageSMSDTO=new TaMessageSMSDTO();
//
//				if(!values.isEmpty()) selectedTaMessageSMSDTO = values.get(0);
//				onRowSelect(null);
//				break;
//			case C_MO_EDITION:
//				if(selectedTaMessageSMSDTO.getId()!=null && selectedTaMessageSMSDTO.getId()!=0){
//					taTiers = taTiersService.findById(selectedTaMessageSMSDTO.getId());
//					selectedTaMessageSMSDTO=taTiersService.findByIdDTO(selectedTaMessageSMSDTO.getId());
//				}				
//				break;
//
//			default:
//				break;
//			}
			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
			mapperModelToUI.map(taMessageSMS, selectedTaMessageSMSDTO);

			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Tiers", "actAnnuler"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	public void autoCompleteMapUIToDTO() {
//		if(taTTelDTO!=null) {
//			validateUIField(Const.C_CODE_T_EMAIL,taTTelDTO.getCodeTEmail());
//			selectedTaMessageSMSDTO.setCodeTTiers(taTTelDTO.getCodeTEmail());
//		}else {
//			selectedTaMessageSMSDTO.setCodeTTiers(null);
//		}
//		
//		if(taEmailDTO!=null) {
//			validateUIField(Const.C_CODE_eT_ENTITE,taEmailDTO.getCodeTEntite());
//			selectedTaMessageSMSDTO.setCodeTEntite(taEmailDTO.getCodeTEntite());
//		}else {
//			selectedTaMessageSMSDTO.setCodeTEntite(null);
//		}
		
	}

	public void autoCompleteMapDTOtoUI() {
		try {
			taTelephone = null;
			taTTel = null;

			taTTelDTO = null;
			taTelephoneDTODestinataire = null;

//			if(selectedTaMessageSMSDTO.getCodeTTiers()!=null && !selectedTaMessageSMSDTO.getCodeTTiers().equals("")) {
//				taTelephone = taTTiersService.findByCode(selectedTaMessageSMSDTO.getCodeTTiers());
//				taTTelDTO=taTTiersService.findByCodeDTO(selectedTaMessageSMSDTO.getCodeTTiers());
//			}
//			if(selectedTaMessageSMSDTO.getCodeTCivilite()!=null && !selectedTaMessageSMSDTO.getCodeTCivilite().equals("")) {
//				taTTel = taTCivlicteService.findByCode(selectedTaMessageSMSDTO.getCodeTCivilite());
//				taTCiviliteDTO = taTCivlicteService.findByCodeDTO(selectedTaMessageSMSDTO.getCodeTCivilite());
//			}
				
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void actEnregistrer(ActionEvent actionEvent) {		
		//TaMessageSMS taTiers = new TaMessageSMS();


//		if(taTiers.getTaAdresse()!=null){
//			try {
//				if(taTiers.getTaAdresse()!=null && taTiers.getTaAdresse().getTaTAdr()==null){
//					TaTAdr tAdr= taTAdresseService.findByCode(ConstPreferencesTiers.TADR_DEFAUT);
//					taTiers.getTaAdresse().setTaTAdr(tAdr);
//				}
//				int ordre=taAdresseService.rechercheOdrePourType(taTiers.getTaAdresse().getTaTAdr().getCodeTAdr(),taTiers.getCodeTiers());
//				if(taTiers.getTaAdresse().getOrdre()==null || taTiers.getTaAdresse().getOrdre()==0)taTiers.getTaAdresse().setOrdre(ordre);
//			} catch (FinderException e) {
//
//			}
//		}
//		autoCompleteMapUIToDTO();
//		initAdresseNull();
//		if(taTiers.getTaTelephone()==null){
//			selectedTaMessageSMSDTO.setAdresseEmail(null);
//		}
//		if(taTiers.getTaTelephone()==null){
//			selectedTaMessageSMSDTO.setNumeroTelephone(null);
//		}
//		if(taTiers.getTaWeb()==null){
//			selectedTaMessageSMSDTO.setAdresseWeb(null);
//		}

		mapperUIToModel.map(selectedTaMessageSMSDTO, taMessageSMS);

		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Tiers", "actEnregistrer"));
		}
	}

	public void actInserer(ActionEvent actionEvent) {
		try {
			selectedTaMessageSMSDTOs= new TaMessageSMSDTO[]{};
			selectedTaMessageSMSDTO = new TaMessageSMSDTO();
			taMessageSMS = new TaMessageSMS();

			String codeTiersDefaut = "C";

			
			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Tiers", "actInserer"));
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void actModifier() {
		actModifier(null);
	}

	public void actModifier(ActionEvent actionEvent) {

		modeEcran.setMode(EnumModeObjet.C_MO_EDITION);

		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Tiers", "actModifier"));
		}
	}

	public void actAide(ActionEvent actionEvent) {

		//			PrimeFaces.current().dialog().openDynamic("aide");

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

	public void nouveau(ActionEvent actionEvent) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_EMAILS_ENVOYES);
		b.setTitre("Gestion des emails envoyés");
		b.setTemplate("/gestion_emails_envoyes.xhtml");
		b.setIdTab(LgrTab.ID_TAB_GESTION_EMAILS_ENVOYES);
		b.setStyle(LgrTab.CSS_CLASS_TAB_EMAILS_ENVOYES);
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
		TaMessageSMS taTiers = new TaMessageSMS();
		try {
			if(selectedTaMessageSMSDTO!=null && selectedTaMessageSMSDTO.getId()!=null){
				taTiers = taTiersService.findById(selectedTaMessageSMSDTO.getId());
			}

			taTiersService.remove(taTiers);
			values.remove(selectedTaMessageSMSDTO);

			if(!values.isEmpty()) {
				selectedTaMessageSMSDTO = values.get(0);
			}else {
				selectedTaMessageSMSDTO=new TaMessageSMSDTO();
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
	
	public void actPlusDeDestinataire(ActionEvent actionEvent) {
		showPlusDestinataire = !showPlusDestinataire;
	}
	
	public void actSupprimerPieceJointe(ActionEvent actionEvent) {
		
	}
	
	public void actSupprimerPieceJointe() {
		//actSupprimerPieceJointe(null);
	}
	
	public void actFermerDialog(ActionEvent actionEvent) {
		//PrimeFaces.current().dialog().closeDynamic(null);
		
		//PrimeFaces.current().dialog().closeDynamic(listeControle);
		PrimeFaces.current().dialog().closeDynamic(null);
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
		selectedTaMessageSMSDTO=new TaMessageSMSDTO();
		selectedTaMessageSMSDTOs=new TaMessageSMSDTO[]{selectedTaMessageSMSDTO};

	}

	public boolean pasDejaOuvert() {
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_EMAILS_ENVOYES);
		return tabsCenter.ongletDejaOuvert(b)==null;
	} 

	public void onRowSimpleSelect(SelectEvent event) {

		if(pasDejaOuvert()==false){
			onRowSelect(event);

			autoCompleteMapDTOtoUI();
//			if(ConstWeb.DEBUG) {
//				FacesContext context = FacesContext.getCurrentInstance();  
//				context.addMessage(null, new FacesMessage("Tiers", 
//						"Chargement du tiers N°"+selectedTaMessageSMSDTO.getCodeTiers()
//						)); }
		} else {
			tabViews.selectionneOngletCentre(LgrTab.TYPE_TAB_EMAILS_ENVOYES);
		}
	} 
	public void updateTab(){
		try {

			if(selectedTaMessageSMSDTOs!=null && selectedTaMessageSMSDTOs.length>0) {
				selectedTaMessageSMSDTO = selectedTaMessageSMSDTOs[0];
			}
			if(selectedTaMessageSMSDTO.getId()!=null && selectedTaMessageSMSDTO.getId()!=0) {
				taMessageSMS = taTiersService.findById(selectedTaMessageSMSDTO.getId());
			}
			mapperModelToUI.map(taMessageSMS, selectedTaMessageSMSDTO);	
			
			/*
			 * 
			 */
			//DEST
			selectedTaMessageSMSDTO.setDestinataires(new ArrayList<>());
			for (TaContactMessageSMS dest : taMessageSMS.getDestinataires()) {
				TaContactMessageSMSDTO dto = new TaContactMessageSMSDTO();
				dto.setNumeroTelephone(dest.getNumeroTelephone());
				dto.setId(dest.getIdContactMessageSms());
				if(dest.getTaTiers()!=null) {
					dto.setIdTiers(dest.getTaTiers().getIdTiers());
					dto.setCodeTiers(dest.getTaTiers().getCodeTiers());
					dto.setNomTiers(dest.getTaTiers().getNomTiers());
					dto.setPrenomTiers(dest.getTaTiers().getPrenomTiers());
				}
				selectedTaMessageSMSDTO.getDestinataires().add(dto);
				
			}
			
			
			//Status du service d'envoi externe (si c'est le cas)
			taMessageSMS = envoiSmsService.updateInfosMessageExterne(taMessageSMS);
			if(taMessageSMS.getStatusServiceExterne()!=null && taMessageSMS.getStatusServiceExterne().equals("")) {
				selectedTaMessageSMSDTO.setStatusServiceExterne(taMessageSMS.getStatusServiceExterne());
			}
			String infoTech = "";
			infoTech += "Envoi : <br/>";
			infoTech += taMessageSMS.getInfosService()!=null ? taMessageSMS.getInfosService() : "";
			infoTech += "<br/> =========== <br/>";
			infoTech += "Détails : <br/>";
			infoTech += taMessageSMS.getEtatMessageServiceExterne()!=null ? taMessageSMS.getEtatMessageServiceExterne() : "";
			selectedTaMessageSMSDTO.setInfosTechniques(infoTech);
				

			autoCompleteMapDTOtoUI();

		} catch (FinderException e) {
			e.printStackTrace();
		}
	}

	public void onTabShow() {
		System.out.println("ArticleController.onTabShow()");
	}

	public void onTabChange(TabChangeEvent event) {
		//			FacesMessage msg = null;
		//			if(event.getTab()!=null) {
		//				msg = new FacesMessage("Tab Changed ArticleController", "Active Tab: " + event.getTab().getTitle());
		//			} else {
		//				msg = new FacesMessage("Tab Changed ArticleController", "Active Tab: ");
		//			}
		//	        FacesContext.getCurrentInstance().addMessage(null, msg);

		/*
		 * Si trop lent voir au cas par cas pour :
		 * 	- soit mettre à jour uniquement l'onglet qui va s'afficher, 
		 *  - soit mettre à jour les propriété du masterEntity en fonction de ce qui vient d'être fait sur l'onlget précendent
		 * Par exemple mettre à jour la liste des controle dans l'article courant au lieu de recharger l'article entièrement
		 * 
		 * Sinon chercher ce que l'on peu faire avec un refresh JPA
		 */
		updateTab(); 
	}

	public boolean disabledTab(String nomTab) {
		return modeEcran.dataSetEnModif() ||  selectedTaMessageSMSDTO.getId()==0;
	}

	public void onRowSelect(SelectEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_EMAILS_ENVOYES);
		b.setTitre("Gestion des emails envoyés");
		b.setTemplate("/gestion_emails_envoyes.xhtml");
		b.setIdTab(LgrTab.ID_TAB_GESTION_EMAILS_ENVOYES);
		b.setStyle(LgrTab.CSS_CLASS_TAB_EMAILS_ENVOYES);
//		b.setParamId(LibConversion.integerToString(selectedTaMessageSMSDTO.getId()));

//		tabsCenter.ajouterOnglet(b);
//		tabViews.selectionneOngletCentre(b);


		updateTab();
		//scrollToTop();

		if(ConstWeb.DEBUG) {
//			FacesContext context = FacesContext.getCurrentInstance();  
//			context.addMessage(null, new FacesMessage("Tiers", 
//					"Chargement du tiers N°"+selectedTaMessageSMSDTO.getCodeTiers()
//					)); 
		}
	} 

	public void onRowSelectResponsive(SelectEvent event) {		
		autoCompleteMapDTOtoUI();

		try {
			taMessageSMS = taTiersService.findById(selectedTaMessageSMSDTO.getId());
			mapperModelToUI.map(taMessageSMS, selectedTaMessageSMSDTO);
		} catch (FinderException e) {
			e.printStackTrace();
		}

		if(ConstWeb.DEBUG) {
//			FacesContext context = FacesContext.getCurrentInstance();  
//			context.addMessage(null, new FacesMessage("Tiers", 
//					"Chargement du tiers N°"+selectedTaMessageSMSDTO.getCodeTiers()
//					)); 
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
				if(selectedTaMessageSMSDTO!=null && selectedTaMessageSMSDTO.getId()!=null  && selectedTaMessageSMSDTO.getId()!=0 ) retour = false;
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

	public TaMessageSMSDTO[] getSelectedTaMessageSMSDTOs() {
		return selectedTaMessageSMSDTOs;
	}

	public void setSelectedTaMessageSMSDTOs(TaMessageSMSDTO[] selectedTaMessageSMSDTOs) {
		this.selectedTaMessageSMSDTOs = selectedTaMessageSMSDTOs;
	}

	public TaMessageSMSDTO getNewTaMessageSMSDTO() {
		return newTaMessageSMSDTO;
	}

	public void setNewTaMessageSMSDTO(TaMessageSMSDTO newTaMessageSMSDTO) {
		this.newTaMessageSMSDTO = newTaMessageSMSDTO;
	}

	public TaMessageSMSDTO getSelectedTaMessageSMSDTO() {
		return selectedTaMessageSMSDTO;
	}

	public void setSelectedTaMessageSMSDTO(TaMessageSMSDTO selectedTaMessageSMSDTO) {
		this.selectedTaMessageSMSDTO = selectedTaMessageSMSDTO;
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

	public void validateTiers(FacesContext context, UIComponent component, Object value) throws ValidatorException {

		String messageComplet = "";

		try {
			//String selectedRadio = (String) value;

			String nomChamp =  (String) component.getAttributes().get("nomChamp");

			//msg = "Mon message d'erreur pour : "+nomChamp;

			//			  validateUIField(nomChamp,value);
			//			  TaMessageSMSDTO dtoTemp=new TaMessageSMSDTO();
			//			  PropertyUtils.setSimpleProperty(dtoTemp, nomChamp, value);
			//			  taTiersService.validateDTOProperty(dtoTemp, nomChamp, ITaMessageSMSServiceRemote.validationContext );

			//				//validation automatique via la JSR bean validation
			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
			Set<ConstraintViolation<TaMessageSMSDTO>> violations = factory.getValidator().validateValue(TaMessageSMSDTO.class,nomChamp,value);
			if (violations.size() > 0) {
				messageComplet = "Erreur de validation : ";
				//					List<IStatus> statusList = new ArrayList<IStatus>();
				for (ConstraintViolation<TaMessageSMSDTO> cv : violations) {
					//						statusList.add(ValidationStatus.error(cv.getMessage()));
					messageComplet+=" "+cv.getMessage()+"\n";
				}
				throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, messageComplet, messageComplet));
				//					return new MultiStatus(LibrairiesEcranPlugin.PLUGIN_ID, IStatus.ERROR,
				//							//statusList.toArray(new IStatus[statusList.size()]), "Validation errors", null);
				//							statusList.toArray(new IStatus[statusList.size()]), messageComplet, null);
			} else {
				//aucune erreur de validation "automatique", on déclanche les validations du controller
				//					if(controller!=null) {
				validateUIField(nomChamp,value);
				//						if(!s.isOK()) {
				//							return s;
				//						}
				//					}
			}
			//				return ValidationStatus.ok();

			//selectedTaMessageSMSDTO.setAdresse1Adresse("abcd");

			//			  if(selectedRadio.equals("aa")) {
			//				  throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
			//			  }

		} catch(Exception e) {
			//messageComplet += e.getMessage();
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, messageComplet, messageComplet));
		}
	}

	public void autcompleteSelection(SelectEvent event) {
		Object value = event.getObject();
		//			FacesMessage msg = new FacesMessage("Selected", "Item:" + value);

		String nomChamp =  (String) event.getComponent().getAttributes().get("nomChamp");

		if(value!=null) {
			if(value instanceof String && value.equals(Const.C_AUCUN))value="";	
			if(value instanceof TaTEntiteDTO && ((TaTEntiteDTO) value).getCodeTEntite()!=null && ((TaTEntiteDTO) value).getCodeTEntite().equals(Const.C_AUCUN))value=null;	
			if(value instanceof TaTCiviliteDTO && ((TaTCiviliteDTO) value).getCodeTCivilite()!=null && ((TaTCiviliteDTO) value).getCodeTCivilite().equals(Const.C_AUCUN))value=null;
			if(value instanceof TaCPaiementDTO && ((TaCPaiementDTO) value).getCodeCPaiement()!=null && ((TaCPaiementDTO) value).getCodeCPaiement().equals(Const.C_AUCUN))value=null;
			if(value instanceof TaTPaiementDTO && ((TaTPaiementDTO) value).getCodeTPaiement()!=null && ((TaTPaiementDTO) value).getCodeTPaiement().equals(Const.C_AUCUN))value=null;
			//if(value instanceof TaTTiersDTO && ((TaTTiersDTO) value).getCodeTTiers()!=null && ((TaTTiersDTO) value).getCodeTTiers().equals(Const.C_AUCUN))value=null;
			if(value instanceof TaCPaiement && ((TaCPaiement) value).getCodeCPaiement()!=null && ((TaCPaiement) value).getCodeCPaiement().equals(Const.C_AUCUN))value=null;
			if(value instanceof TaTPaiement && ((TaTPaiement) value).getCodeTPaiement()!=null && ((TaTPaiement) value).getCodeTPaiement().equals(Const.C_AUCUN))value=null;
			if(value instanceof TaTTvaDoc && ((TaTTvaDoc) value).getCodeTTvaDoc()!=null && ((TaTTvaDoc) value).getCodeTTvaDoc().equals(Const.C_AUCUN))value=null;
			if(value instanceof TaFamilleTiers && ((TaFamilleTiers) value).getCodeFamille()!=null && ((TaFamilleTiers) value).getCodeFamille().equals(Const.C_AUCUN))value=null;
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
		
//		//validateUIField(nomChamp,value);
//		try {
//			if(nomChamp.equals(Const.C_ADRESSE_EMAIL+"_DESTINATAIRE")) {
//				TaTelephone entity =null;
//				if(value!=null){
//					if(value instanceof TaTelephoneDTO){
//	//				entity=(TaFamille) value;
//						entity = taTelephoneService.findByCode(((TaFamilleDTO)value).getCodeFamille());
//					}else{
//						entity = taTelephoneService.findByCode((String)value);
//					}
//				}else{
//					selectedTaArticleDTO.setCodeFamille("");
//				}
//				//taArticle.setTaFamille(entity);
//				for (TaTelephone f : taMessageSMS.getDestinataires()) {
//					if(f.getIdEmail()==((TaTelephoneDTO)value).getId())
//						entity = f;
//				}
//				taMessageSMS.getDestinataires().remove(entity);
//				if(taArticle.getTaFamille()!=null && taArticle.getTaFamille().getCodeFamille().equals(entity.getCodeFamille())) {
//					taArticle.setTaFamille(null);
//					taFamilleDTO = null;
//					if(!taArticle.getTaFamilles().isEmpty()) {
//						taArticle.setTaFamille(taArticle.getTaFamilles().iterator().next());
//						//taFamilleDTO = null;
//					}
//				}
//			}
//
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
	
	}

	public boolean validateUIField(String nomChamp,Object value) {

		boolean changement=false;

		try {				
			if(nomChamp.equals(Const.C_NUMERO_TELEPHONE)) {
				
				TaTelephone entity =null;
				if(value!=null){
					if(value instanceof TaTelephoneDTO){
//					entity=(TaFamille) value;
						entity = taTelephoneService.findByCode(((TaTelephoneDTO)value).getNumeroTelephone());
					}else{
						entity = taTelephoneService.findByCode((String)value);
					}
				}else{
					//selectedTaMessageSMSDTO.setCodeFamille("");
				}
				TaContactMessageSMS taContactMessageSMS = new TaContactMessageSMS();
				taContactMessageSMS.setNumeroTelephone(entity.getNumeroTelephone());
				taContactMessageSMS.setTaTiers(entity.getTaTiers());
				
				if(nomChamp.equals(Const.C_NUMERO_TELEPHONE)) {
					taContactMessageSMS.setTaMessageSms(taMessageSMS);
					if(taMessageSMS.getDestinataires()==null) {
						taMessageSMS.setDestinataires(new HashSet<>());
					}
					taMessageSMS.getDestinataires().add(taContactMessageSMS);
				} 

			} else if(nomChamp.equals(Const.C_CODE_TIERS)) {
//		
			}
			

			return false;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}


	public List<TaTelephone> adresseEmailAutoComplete(String query) {
		List<TaTelephone> allValues = taTelephoneService.selectAll();
		List<TaTelephone> filteredValues = new ArrayList<TaTelephone>();
		TaTelephone civ = new TaTelephone();
		civ.setNumeroTelephone(Const.C_AUCUN);
		filteredValues.add(civ);
		for (int i = 0; i < allValues.size(); i++) {
			civ = allValues.get(i);
			if(query.equals("*") || civ.getNumeroTelephone().toLowerCase().contains(query.toLowerCase())) {
				filteredValues.add(civ);
			}
		}

		return filteredValues;
	}

	public List<TaTelephoneDTO> adresseEmailAutoCompleteLight(String query) {
		List<TaTelephoneDTO> allValues = taTelephoneService.findAllLight();
		List<TaTelephoneDTO> filteredValues = new ArrayList<TaTelephoneDTO>();
		TaTelephoneDTO civ = new TaTelephoneDTO();
//		civ.setAdresseEmail(Const.C_AUCUN);
//		filteredValues.add(civ);
		for (int i = 0; i < allValues.size(); i++) {
			civ = allValues.get(i);
			if(query.equals("*") || civ.getNumeroTelephone().toLowerCase().contains(query.toLowerCase())) {
				filteredValues.add(civ);
			}
		}

		return filteredValues;
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

	public TaMessageSMS getTaMessageSMS() {
		return taMessageSMS;
	}

	public void setTaMessageSMS(TaMessageSMS taTiers) {
		this.taMessageSMS = taTiers;
	}


	public TabView getTabViewTiers() {
		return tabViewTiers;
	}

	public void setTabViewTiers(TabView tabViewTiers) {
		this.tabViewTiers = tabViewTiers;
	}

	public TaTelephone getTaTelephone() {
		return taTelephone;
	}

	public void setTaTelephone(TaTelephone taEmail) {
		this.taTelephone = taEmail;
	}

	public TaTTel getTaTTel() {
		return taTTel;
	}

	public void setTaTTel(TaTTel taTEmail) {
		this.taTTel = taTEmail;
	}

	public TaTelephoneDTO getTaTelephoneDTODestinataire() {
		return taTelephoneDTODestinataire;
	}

	public void setTaTelephoneDTODestinataire(TaTelephoneDTO taEmailDTO) {
		this.taTelephoneDTODestinataire = taEmailDTO;
	}

	public TaTTelDTO getTaTTelDTO() {
		return taTTelDTO;
	}

	public void setTaTTelDTO(TaTTelDTO taTEmailDTO) {
		this.taTTelDTO = taTEmailDTO;
	}

	public List<TaTelephoneDTO> getTaTelephoneDTODestinataires() {
		return taTelephoneDTODestinataires;
	}

	public void setTaTelephoneDTODestinataires(List<TaTelephoneDTO> taEmailDTODestinataires) {
		this.taTelephoneDTODestinataires = taEmailDTODestinataires;
	}

	public List<String> getNumerosDestinataire() {
		return numerosDestinataire;
	}

	public void setNumerosDestinataire(List<String> adressesDestinataire) {
		this.numerosDestinataire = adressesDestinataire;
	}

	public boolean isShowPlusDestinataire() {
		return showPlusDestinataire;
	}

	public void setShowPlusDestinataire(boolean showPlusDestinataire) {
		this.showPlusDestinataire = showPlusDestinataire;
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
//		email.setNomExpediteur(taInfoEntrepriseService.findInstance().getNomInfoEntreprise()); 
//		email.setSubject(taInfoEntrepriseService.findInstance().getNomInfoEntreprise()+" Facture "+masterEntity.getCodeDocument()); 
//		email.setBodyPlain("Votre facture "+masterEntity.getCodeDocument());
//		email.setBodyHtml("Votre facture "+masterEntity.getCodeDocument());
//		email.setDestinataires(new String[]{masterEntity.getTaTiers().getTaTelephone().getAdresseEmail()});
//		email.setEmailDestinataires(new TaTelephone[]{masterEntity.getTaTiers().getTaTelephone()});
		
//		EmailPieceJointeParam pj1 = new EmailPieceJointeParam();
//		pj1.setFichier(new File(((ITaFactureServiceRemote)taDocumentService).generePDF(masterEntity.getIdDocument())));
//		pj1.setTypeMime("pdf");
//		pj1.setNomFichier(pj1.getFichier().getName());
//		email.setPiecesJointes(
//				new EmailPieceJointeParam[]{pj1}
//				);
		
//		sessionMap.put(PaiementParam.NOM_OBJET_EN_SESSION, email);
        
        PrimeFaces.current().dialog().openDynamic("/dialog_sms", options, params);
    
	}
	
	public void handleReturnDialogEmail(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			
			TaMessageSMS j = (TaMessageSMS) event.getObject();
			
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("SMS", 
					"SMS envoyé "
					)); 
		}
	}

	public TaTiers getMasterEntity() {
		return masterEntity;
	}

	public void setMasterEntity(TaTiers masterEntity) {
		this.masterEntity = masterEntity;
	}

	public String getCssEtiquetteEmail() {
		return cssEtiquetteEmail;
	}

	public void setCssEtiquetteEmail(String cssEtiquetteEmail) {
		this.cssEtiquetteEmail = cssEtiquetteEmail;
	}


}  
  
