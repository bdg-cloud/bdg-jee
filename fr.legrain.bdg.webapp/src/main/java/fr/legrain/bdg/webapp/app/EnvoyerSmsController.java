package fr.legrain.bdg.webapp.app;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
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
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import fr.legrain.article.dto.TaFamilleDTO;
import fr.legrain.article.dto.TaTTvaDTO;
import fr.legrain.article.dto.TaTvaDTO;
import fr.legrain.article.dto.TaUniteDTO;
import fr.legrain.article.model.TaMarqueArticle;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaPreferencesServiceRemote;
import fr.legrain.bdg.droits.service.remote.ITaUtilisateurServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaCompteServiceWebExterneServiceRemote;
import fr.legrain.bdg.sms.service.remote.ITaMessageSMSServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTelephoneServiceRemote;
import fr.legrain.document.dto.TaTPaiementDTO;
import fr.legrain.document.model.TaTPaiement;
import fr.legrain.droits.service.SessionInfo;
import fr.legrain.email.dto.TaPieceJointeEmailDTO;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.ModeObjetEcranServeur;
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

@Named
@ViewScoped  
public class EnvoyerSmsController extends AbstractController implements Serializable {  

	@Inject @Named(value="tabListModelCenterBean")
	private TabListModelBean tabsCenter;

	@Inject @Named(value="tabViewsBean")
	private TabViewsBean tabViews;

	private String paramId;	

	protected ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();

//	private @EJB LgrEmailSMTPService lgrEmail;

//	private @EJB LgrMailjetService lgrMailjetService;
	protected @EJB ITaInfoEntrepriseServiceRemote taInfoEntrepriseService;

	private @EJB ITaUtilisateurServiceRemote taUtilisateurService;
	private @EJB ITaMessageSMSServiceRemote taMessageSmsService;
	private @EJB ITaCompteServiceWebExterneServiceRemote taCompteServiceWebExterneService;
	private @EJB ITaTelephoneServiceRemote taTelephoneService;
	private @EJB ITaPreferencesServiceRemote taPreferencesService;
	
	private @EJB EnvoiSmsService smsServiceFinder;

	private @Inject SessionInfo sessionInfo;

	private StreamedContent streamedPieceJointe;
	private TaPieceJointeEmailDTO selectedTaPieceJointeEmailDTO;

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
	private List<TaTelephoneDTO> taEmailDTODestinataires;
	
	private List<String> adressesDestinataire;
	
	private boolean showPlusDestinataire = false;

	
	private SmsParam param;

	
	public EnvoyerSmsController() {  
		
	}  

	@PostConstruct
	public void postConstruct(){
		refresh();
	}

	public void refresh(){

		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
		
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		
		try {
			if(sessionMap.get(SmsParam.NOM_OBJET_EN_SESSION)!=null) {
				param = (SmsParam) sessionMap.get(SmsParam.NOM_OBJET_EN_SESSION);
				sessionMap.remove(SmsParam.NOM_OBJET_EN_SESSION);
			}
			
			if(param!=null) {
				taMessageSMS = new TaMessageSMS();
				selectedTaMessageSMSDTO = new TaMessageSMSDTO();
				
				taMessageSMS.setBodyPlain(param.getBodyPlain());
				taMessageSMS.setBodyHtml(param.getBodyHtml());
//				taMessageSMS.setSubject(param.getSubject());
				
				if(param.getDestinataires()!=null) {
					for (String dest : param.getDestinataires()) {
						
					}
				}
				

				
				if(param.getTelephoneDestinataires()!=null) {
					for (TaTelephone dest : param.getTelephoneDestinataires()) {
						if(taMessageSMS.getDestinataires()==null) {
							taMessageSMS.setDestinataires(new HashSet<>());
						}
						if(taEmailDTODestinataires==null) {
							taEmailDTODestinataires = new ArrayList<>();
						}
						
						
						TaContactMessageSMS taContactMessageSMS = new TaContactMessageSMS();
						taContactMessageSMS.setNumeroTelephone(dest.getNumeroTelephone());
						taContactMessageSMS.setTaTiers(dest.getTaTiers());
						taContactMessageSMS.setTaMessageSms(taMessageSMS);
						taMessageSMS.getDestinataires().add(taContactMessageSMS);
						
						TaTelephoneDTO taTelephoneDTO = new TaTelephoneDTO();
						mapperModelToUITaTelephone.map(dest, taTelephoneDTO);
						
						taEmailDTODestinataires.add(taTelephoneDTO);
					}
					
					
				}
				
				mapperModelToUI.map(taMessageSMS, selectedTaMessageSMSDTO);
				
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
	

	public void actEnvoyerSms(ActionEvent actionEvent) {
		try {
			
			List<String> numeroDestinataireLoc = new ArrayList<>();
			List<TaTelephone> numeroTiersDestinataire = null;
			List<TaContactMessageSMS> contactMessageSmsDestinataire = new ArrayList<>();
	
			
			//DEST
			if(taMessageSMS.getDestinataires()!=null) {
				for (TaContactMessageSMS c : taMessageSMS.getDestinataires()) {
					contactMessageSmsDestinataire.add(c);
				}
			}
			if(adressesDestinataire!=null) {
				for (String c : adressesDestinataire) {
					numeroDestinataireLoc.add(c);
				}
			}
			
			
			String message = selectedTaMessageSMSDTO.getBodyPlain();
			message = message.replace("<p>", "");
			message = message.replace("</p>", "");
			message = message.replace("<br>", "");
			
			//String adresseExpediteur = "nicolas@legrain.fr";
			String numeroExpediteur = "";
			////////////////////////////////////////////////////////////////////////////////////
			//IEnvoiEmailViaServiceExterneDossierService service = smsServiceFinder.findEmailServiceDossier();
			//service.sendAndLog(
			//smsServiceFinder.sendAndLog(
			smsServiceFinder.sendAndLogSMSDossier(
					null,
					numeroExpediteur,//null,
					"BDG",
//					selectedTaMessageSMSDTO.getSubject(),
					message, 
//					selectedTaMessageSMSDTO.getBodyPlain(), 
					adressesDestinataire, 
					numeroTiersDestinataire,
					contactMessageSmsDestinataire,
		
					sessionInfo.getUtilisateur()
					);
			
//			smsServiceFinder.sendAndLogSMSProgramme(
//					numeroExpediteur,//null,
//					"BDG",
////					selectedTaMessageSMSDTO.getSubject(),
//					selectedTaMessageSMSDTO.getBodyPlain(), 
////					selectedTaMessageSMSDTO.getBodyPlain(), 
//					adressesDestinataire, 
//					numeroTiersDestinataire,
//					contactMessageSmsDestinataire,
//		
//					sessionInfo.getUtilisateur()
//					);
			///////////////////////////////////////////////////////////////////////////////////

			
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("SMS", 
					"SMS envoyé "
					)); 
			PrimeFaces.current().dialog().closeDynamic(taMessageSMS);

		} catch(Exception e) {
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", 
					"SMS non envoyé "
					)); 
		}
	}
	
	public void actPlusDeDestinataire(ActionEvent actionEvent) {
		showPlusDestinataire = !showPlusDestinataire;
	}
	
	public void actSupprimerPieceJointe(ActionEvent actionEvent) {
		
	}
	
	public void actFermerDialog(ActionEvent actionEvent) {
		//PrimeFaces.current().dialog().closeDynamic(null);
		
		//PrimeFaces.current().dialog().closeDynamic(listeControle);
		PrimeFaces.current().dialog().closeDynamic(null);
	}

	public void actFermer(ActionEvent actionEvent) {

		switch (modeEcran.getMode()) {
		case C_MO_INSERTION:
			//actAnnuler(actionEvent);
			break;
		case C_MO_EDITION:
			//actAnnuler(actionEvent);							
			break;

		default:
			break;
		}
		selectedTaMessageSMSDTO=new TaMessageSMSDTO();
		selectedTaMessageSMSDTOs=new TaMessageSMSDTO[]{selectedTaMessageSMSDTO};

		
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
//				TaEmail entity =null;
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
//				for (TaEmail f : taMessageSMS.getDestinataires()) {
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


	public List<TaTelephone> numeroTelephoneAutoComplete(String query) {
		List<TaTelephone> allValues = taTelephoneService.selectAll();
		List<TaTelephone> filteredValues = new ArrayList<TaTelephone>();
		TaTelephone n = new TaTelephone();
		n.setNumeroTelephone(Const.C_AUCUN);
		filteredValues.add(n);
		for (int i = 0; i < allValues.size(); i++) {
			n = allValues.get(i);
			if(query.equals("*") || n.getNumeroTelephone().toLowerCase().contains(query.toLowerCase())) {
				filteredValues.add(n);
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
		return taEmailDTODestinataires;
	}

	public void setTaTelephoneDTODestinataires(List<TaTelephoneDTO> taEmailDTODestinataires) {
		this.taEmailDTODestinataires = taEmailDTODestinataires;
	}

	public List<String> getAdressesDestinataire() {
		return adressesDestinataire;
	}

	public void setAdressesDestinataire(List<String> adressesDestinataire) {
		this.adressesDestinataire = adressesDestinataire;
	}

	public boolean isShowPlusDestinataire() {
		return showPlusDestinataire;
	}

	public void setShowPlusDestinataire(boolean showPlusDestinataire) {
		this.showPlusDestinataire = showPlusDestinataire;
	}

	public StreamedContent getStreamedPieceJointe() {
//		InputStream stream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/demo/images/optimus.jpg");
//        file = new DefaultStreamedContent(stream, "image/jpg", "downloaded_optimus.jpg");
		InputStream stream = new ByteArrayInputStream(selectedTaPieceJointeEmailDTO.getFichier()); 
		streamedPieceJointe = new DefaultStreamedContent(stream,null,selectedTaPieceJointeEmailDTO.getNomFichier());
		return streamedPieceJointe;
	}

	public TaPieceJointeEmailDTO getSelectedTaPieceJointeEmailDTO() {
		return selectedTaPieceJointeEmailDTO;
	}

	public void setSelectedTaPieceJointeEmailDTO(TaPieceJointeEmailDTO selectedTaPieceJointeEmailDTO) {
		this.selectedTaPieceJointeEmailDTO = selectedTaPieceJointeEmailDTO;
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
//		email.setDestinataires(new String[]{masterEntity.getTaTiers().getTaEmail().getAdresseEmail()});
//		email.setEmailDestinataires(new TaEmail[]{masterEntity.getTaTiers().getTaEmail()});
		
//		EmailPieceJointeParam pj1 = new EmailPieceJointeParam();
//		pj1.setFichier(new File(((ITaFactureServiceRemote)taDocumentService).generePDF(masterEntity.getIdDocument())));
//		pj1.setTypeMime("pdf");
//		pj1.setNomFichier(pj1.getFichier().getName());
//		email.setPiecesJointes(
//				new EmailPieceJointeParam[]{pj1}
//				);
		
//		sessionMap.put("email", email);
        
        PrimeFaces.current().dialog().openDynamic("/dialog_email", options, params);
    
	}
	
	public void handleReturnDialogEmail(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			
			TaMessageSMS j = (TaMessageSMS) event.getObject();
			
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Email", 
					"Email envoyée "
					)); 
		}
	}

}  
  
