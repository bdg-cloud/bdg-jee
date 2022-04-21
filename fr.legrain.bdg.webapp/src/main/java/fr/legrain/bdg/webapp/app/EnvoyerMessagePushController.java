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
import fr.legrain.bdg.compteclient.ws.TaTTiers;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaPreferencesServiceRemote;
import fr.legrain.bdg.droits.service.remote.ITaUtilisateurServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.push.service.remote.ITaMessagePushServiceRemote;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaCompteServiceWebExterneServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.document.dto.TaTPaiementDTO;
import fr.legrain.document.model.TaTPaiement;
import fr.legrain.droits.service.SessionInfo;
import fr.legrain.email.dto.TaPieceJointeEmailDTO;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.ModeObjetEcranServeur;
import fr.legrain.push.dto.TaMessagePushDTO;
import fr.legrain.push.model.TaContactMessagePush;
import fr.legrain.push.model.TaMessagePush;
import fr.legrain.push.service.EnvoiMessagePushService;
import fr.legrain.tiers.dto.TaCPaiementDTO;
import fr.legrain.tiers.dto.TaTCiviliteDTO;
import fr.legrain.tiers.dto.TaTEntiteDTO;
import fr.legrain.tiers.dto.TaTTiersDTO;
import fr.legrain.tiers.dto.TaTiersDTO;
import fr.legrain.tiers.model.TaCPaiement;
import fr.legrain.tiers.model.TaFamilleTiers;
import fr.legrain.tiers.model.TaTTvaDoc;
import fr.legrain.tiers.model.TaTiers;

@Named
@ViewScoped  
public class EnvoyerMessagePushController extends AbstractController implements Serializable {  

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
	private @EJB ITaMessagePushServiceRemote taMessagePushService;
	private @EJB ITaCompteServiceWebExterneServiceRemote taCompteServiceWebExterneService;
	private @EJB ITaTiersServiceRemote taTiersService;
	private @EJB ITaPreferencesServiceRemote taPreferencesService;
	
	private @EJB EnvoiMessagePushService pushServiceFinder;

	private @Inject SessionInfo sessionInfo;

	private StreamedContent streamedPieceJointe;
	private TaPieceJointeEmailDTO selectedTaPieceJointeEmailDTO;

	private TaMessagePushDTO[] selectedTaMessagePushDTOs; 
	private TaMessagePush taMessagePush = new TaMessagePush();
	private TaMessagePushDTO newTaMessagePushDTO = new TaMessagePushDTO();
	private TaMessagePushDTO selectedTaMessagePushDTO = new TaMessagePushDTO();

	private TaTiers taTelephone;
	private TaTTiers taTTiers;

	private LgrDozerMapper<TaMessagePushDTO,TaMessagePush> mapperUIToModel  = new LgrDozerMapper<TaMessagePushDTO,TaMessagePush>();
	private LgrDozerMapper<TaMessagePush,TaMessagePushDTO> mapperModelToUI  = new LgrDozerMapper<TaMessagePush,TaMessagePushDTO>();
	
	private LgrDozerMapper<TaTiersDTO,TaTiers> mapperUIToModelTaTiers  = new LgrDozerMapper<TaTiersDTO,TaTiers>();
	private LgrDozerMapper<TaTiers,TaTiersDTO> mapperModelToUITaTiers  = new LgrDozerMapper<TaTiers,TaTiersDTO>();

	private TaTiersDTO taTiersDTODestinataire;
	private TaTTiersDTO taTTiersDTO;
	private List<TaTiersDTO> taTiersDTODestinataires;
	
//	private List<String> adressesDestinataire;
//	
//	private boolean showPlusDestinataire = false;

	
	private PushParam param;

	
	public EnvoyerMessagePushController() {  
		
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
			if(sessionMap.get(PushParam.NOM_OBJET_EN_SESSION)!=null) {
				param = (PushParam) sessionMap.get(PushParam.NOM_OBJET_EN_SESSION);
				sessionMap.remove(PushParam.NOM_OBJET_EN_SESSION);
			}
			
			if(param!=null) {
				taMessagePush = new TaMessagePush();
				selectedTaMessagePushDTO = new TaMessagePushDTO();
				
				taMessagePush.setSujet(param.getSujet());
				taMessagePush.setResume(param.getResume());
				taMessagePush.setContenu(param.getContenu());
				taMessagePush.setUrl(param.getUrl());
				taMessagePush.setUrlImage(param.getUrlImage());
				taMessagePush.setStyle(param.getStyle());
				
				if(param.getDestinataires()!=null) {
					for (TaTiers dest : param.getDestinataires()) {
						if(taTiersDTODestinataires==null) {
							taTiersDTODestinataires = new ArrayList<>();
						}
						taTiersDTODestinataires.add(taTiersService.findByIdDTO(dest.getIdTiers()));
						TaContactMessagePush taContactMessagePush = new TaContactMessagePush();
//						taContactMessagePush.setNumeroTelephone(entity.getNumeroTelephone());
						taContactMessagePush.setTaTiers(dest);
						
//						if(nomChamp.equals(Const.C_NUMERO_TELEPHONE)) {
							taContactMessagePush.setTaMessagePush(taMessagePush);
							if(taMessagePush.getDestinataires()==null) {
								taMessagePush.setDestinataires(new HashSet<>());
							}
							taMessagePush.getDestinataires().add(taContactMessagePush);
//						} 
					}
				}
				

				
//				if(param.getTelephoneDestinataires()!=null) {
//					for (TaTiers dest : param.getTelephoneDestinataires()) {
//						if(taMessagePush.getDestinataires()==null) {
//							taMessagePush.setDestinataires(new HashSet<>());
//						}
//						if(taEmailDTODestinataires==null) {
//							taEmailDTODestinataires = new ArrayList<>();
//						}
//						
//						
//						TaContactMessagePush taContactMessagePush = new TaContactMessagePush();
////						taContactMessagePush.setNumeroTelephone(dest.getNumeroTelephone());
//						taContactMessagePush.setTaTiers(dest.getTaTiers());
//						taContactMessagePush.setTaMessagePush(taMessagePush);
//						taMessagePush.getDestinataires().add(taContactMessagePush);
//						
//						TaTiersDTO taTelephoneDTO = new TaTiersDTO();
//						mapperModelToUITaTiers.map(dest, taTelephoneDTO);
//						
//						taEmailDTODestinataires.add(taTelephoneDTO);
//					}
//					
//					
//				}
				
				mapperModelToUI.map(taMessagePush, selectedTaMessagePushDTO);
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public void autoCompleteMapUIToDTO() {
//		if(taTTelDTO!=null) {
//			validateUIField(Const.C_CODE_T_EMAIL,taTTelDTO.getCodeTEmail());
//			selectedTaMessagePushDTO.setCodeTTiers(taTTelDTO.getCodeTEmail());
//		}else {
//			selectedTaMessagePushDTO.setCodeTTiers(null);
//		}
//		
//		if(taEmailDTO!=null) {
//			validateUIField(Const.C_CODE_eT_ENTITE,taEmailDTO.getCodeTEntite());
//			selectedTaMessagePushDTO.setCodeTEntite(taEmailDTO.getCodeTEntite());
//		}else {
//			selectedTaMessagePushDTO.setCodeTEntite(null);
//		}
		
	}

	public void autoCompleteMapDTOtoUI() {
		try {
			taTelephone = null;
			taTTiers = null;

			taTTiersDTO = null;
			taTiersDTODestinataire = null;

//			if(selectedTaMessagePushDTO.getCodeTTiers()!=null && !selectedTaMessagePushDTO.getCodeTTiers().equals("")) {
//				taTelephone = taTTiersService.findByCode(selectedTaMessagePushDTO.getCodeTTiers());
//				taTTelDTO=taTTiersService.findByCodeDTO(selectedTaMessagePushDTO.getCodeTTiers());
//			}
//			if(selectedTaMessagePushDTO.getCodeTCivilite()!=null && !selectedTaMessagePushDTO.getCodeTCivilite().equals("")) {
//				taTTel = taTCivlicteService.findByCode(selectedTaMessagePushDTO.getCodeTCivilite());
//				taTCiviliteDTO = taTCivlicteService.findByCodeDTO(selectedTaMessagePushDTO.getCodeTCivilite());
//			}
				
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	public void actEnvoyerMessagePush(ActionEvent actionEvent) {
		try {
			
			List<String> numeroDestinataireLoc = new ArrayList<>();
			List<TaTiers> numeroTiersDestinataire = null;
			List<TaContactMessagePush> contactMessagePushDestinataire= new ArrayList<>();
	
			
			//DEST
			if(taMessagePush.getDestinataires()!=null) {
				for (TaContactMessagePush c : taMessagePush.getDestinataires()) {
					contactMessagePushDestinataire.add(c);
				}
			}
//			if(adressesDestinataire!=null) {
//				for (String c : adressesDestinataire) {
//					numeroDestinataireLoc.add(c);
//				}
//			}
			
			
			String message = selectedTaMessagePushDTO.getContenu();
			message = message.replace("<p>", "");
			message = message.replace("</p>", "");
			message = message.replace("<br>", "");
			
			//String adresseExpediteur = "nicolas@legrain.fr";
			String numeroExpediteur = "";
			////////////////////////////////////////////////////////////////////////////////////
			//IEnvoiEmailViaServiceExterneDossierService service = smsServiceFinder.findEmailServiceDossier();
			//service.sendAndLog(
			//smsServiceFinder.sendAndLog(
			pushServiceFinder.sendAndLogMessagePushDossier(
					null,
					selectedTaMessagePushDTO.getSujet(),
					selectedTaMessagePushDTO.getResume(),
					message,
					selectedTaMessagePushDTO.getUrl(),
					selectedTaMessagePushDTO.getUrlImage(),
					selectedTaMessagePushDTO.getStyle(),
					contactMessagePushDestinataire,	
					sessionInfo.getUtilisateur()
					);
			
//			smsServiceFinder.sendAndLogSMSProgramme(
//					numeroExpediteur,//null,
//					"BDG",
////					selectedTaMessagePushDTO.getSubject(),
//					selectedTaMessagePushDTO.getBodyPlain(), 
////					selectedTaMessagePushDTO.getBodyPlain(), 
//					adressesDestinataire, 
//					numeroTiersDestinataire,
//					contactMessageSmsDestinataire,
//		
//					sessionInfo.getUtilisateur()
//					);
			///////////////////////////////////////////////////////////////////////////////////

			
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Push", 
					"Push envoyé "
					)); 
			PrimeFaces.current().dialog().closeDynamic(taMessagePush);

		} catch(Exception e) {
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", 
					"Push non envoyé "
					)); 
		}
	}
	
//	public void actPlusDeDestinataire(ActionEvent actionEvent) {
//		showPlusDestinataire = !showPlusDestinataire;
//	}
	
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
		selectedTaMessagePushDTO=new TaMessagePushDTO();
		selectedTaMessagePushDTOs=new TaMessagePushDTO[]{selectedTaMessagePushDTO};

		
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
				if(selectedTaMessagePushDTO!=null && selectedTaMessagePushDTO.getId()!=null  && selectedTaMessagePushDTO.getId()!=0 ) retour = false;
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

	public TaMessagePushDTO[] getSelectedTaMessagePushDTOs() {
		return selectedTaMessagePushDTOs;
	}

	public void setSelectedTaMessagePushDTOs(TaMessagePushDTO[] selectedTaMessagePushDTOs) {
		this.selectedTaMessagePushDTOs = selectedTaMessagePushDTOs;
	}

	public TaMessagePushDTO getNewTaMessagePushDTO() {
		return newTaMessagePushDTO;
	}

	public void setNewTaMessagePushDTO(TaMessagePushDTO newTaMessagePushDTO) {
		this.newTaMessagePushDTO = newTaMessagePushDTO;
	}

	public TaMessagePushDTO getSelectedTaMessagePushDTO() {
		return selectedTaMessagePushDTO;
	}

	public void setSelectedTaMessagePushDTO(TaMessagePushDTO selectedTaMessagePushDTO) {
		this.selectedTaMessagePushDTO = selectedTaMessagePushDTO;
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
			//			  TaMessagePushDTO dtoTemp=new TaMessagePushDTO();
			//			  PropertyUtils.setSimpleProperty(dtoTemp, nomChamp, value);
			//			  taTiersService.validateDTOProperty(dtoTemp, nomChamp, ITaMessagePushServiceRemote.validationContext );

			//				//validation automatique via la JSR bean validation
			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
			Set<ConstraintViolation<TaMessagePushDTO>> violations = factory.getValidator().validateValue(TaMessagePushDTO.class,nomChamp,value);
			if (violations.size() > 0) {
				messageComplet = "Erreur de validation : ";
				//					List<IStatus> statusList = new ArrayList<IStatus>();
				for (ConstraintViolation<TaMessagePushDTO> cv : violations) {
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

			//selectedTaMessagePushDTO.setAdresse1Adresse("abcd");

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
//					if(value instanceof TaTiersDTO){
//	//				entity=(TaFamille) value;
//						entity = taTelephoneService.findByCode(((TaFamilleDTO)value).getCodeFamille());
//					}else{
//						entity = taTelephoneService.findByCode((String)value);
//					}
//				}else{
//					selectedTaArticleDTO.setCodeFamille("");
//				}
//				//taArticle.setTaFamille(entity);
//				for (TaEmail f : taMessagePush.getDestinataires()) {
//					if(f.getIdEmail()==((TaTiersDTO)value).getId())
//						entity = f;
//				}
//				taMessagePush.getDestinataires().remove(entity);
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
			if(nomChamp.equals(Const.C_CODE_TIERS)) {
				
				TaTiers entity =null;
				if(value!=null){
					if(value instanceof TaTiersDTO){
//					entity=(TaFamille) value;
						entity = taTiersService.findByCode(((TaTiersDTO)value).getCodeTiers());
					}else{
						entity = taTiersService.findByCode((String)value);
					}
				}else{
					//selectedTaMessagePushDTO.setCodeFamille("");
				}
				TaContactMessagePush taContactMessagePush = new TaContactMessagePush();
//				taContactMessagePush.setNumeroTelephone(entity.getNumeroTelephone());
				taContactMessagePush.setTaTiers(entity/*.getTaTiers()*/);
				
//				if(nomChamp.equals(Const.C_NUMERO_TELEPHONE)) {
					taContactMessagePush.setTaMessagePush(taMessagePush);
					if(taMessagePush.getDestinataires()==null) {
						taMessagePush.setDestinataires(new HashSet<>());
					}
					taMessagePush.getDestinataires().add(taContactMessagePush);
//				} 

			} 
//			else 
//				if(nomChamp.equals(Const.C_CODE_TIERS)) {
////		
//			}
			

			return false;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}


	public List<TaTiers> tiersAutoComplete(String query) {
		List<TaTiers> allValues = taTiersService.selectAll();
		List<TaTiers> filteredValues = new ArrayList<TaTiers>();
		TaTiers n = new TaTiers();
		n.setNomTiers(Const.C_AUCUN);
		filteredValues.add(n);
		for (int i = 0; i < allValues.size(); i++) {
			n = allValues.get(i);
			if(query.equals("*") || n.getNomTiers().toLowerCase().contains(query.toLowerCase())) {
				filteredValues.add(n);
			}
		}

		return filteredValues;
	}

	public List<TaTiersDTO> tiersAutoCompleteLight(String query) {
		List<TaTiersDTO> allValues = taTiersService.findAllLight();
		List<TaTiersDTO> filteredValues = new ArrayList<TaTiersDTO>();
		TaTiersDTO civ = new TaTiersDTO();
//		civ.setAdresseEmail(Const.C_AUCUN);
//		filteredValues.add(civ);
		for (int i = 0; i < allValues.size(); i++) {
			civ = allValues.get(i);
			if(query.equals("*") || civ.getNomTiers().toLowerCase().contains(query.toLowerCase())) {
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

	public TaMessagePush getTaMessagePush() {
		return taMessagePush;
	}

	public void setTaMessagePush(TaMessagePush taTiers) {
		this.taMessagePush = taTiers;
	}

	public TaTiers getTaTiers() {
		return taTelephone;
	}

	public void setTaTiers(TaTiers taEmail) {
		this.taTelephone = taEmail;
	}

	public TaTTiers getTaTTiers() {
		return taTTiers;
	}

	public void setTaTTiers(TaTTiers taTEmail) {
		this.taTTiers = taTEmail;
	}

	public TaTiersDTO getTaTiersDTODestinataire() {
		return taTiersDTODestinataire;
	}

	public void setTaTiersDTODestinataire(TaTiersDTO taEmailDTO) {
		this.taTiersDTODestinataire = taEmailDTO;
	}

	public TaTTiersDTO getTaTTiersDTO() {
		return taTTiersDTO;
	}

	public void setTaTTiersDTO(TaTTiersDTO taTEmailDTO) {
		this.taTTiersDTO = taTEmailDTO;
	}

	public List<TaTiersDTO> getTaTiersDTODestinataires() {
		return taTiersDTODestinataires;
	}

	public void setTaTiersDTODestinataires(List<TaTiersDTO> taTiersDTODestinataires) {
		this.taTiersDTODestinataires = taTiersDTODestinataires;
	}

//	public List<String> getAdressesDestinataire() {
//		return adressesDestinataire;
//	}
//
//	public void setAdressesDestinataire(List<String> adressesDestinataire) {
//		this.adressesDestinataire = adressesDestinataire;
//	}
//
//	public boolean isShowPlusDestinataire() {
//		return showPlusDestinataire;
//	}
//
//	public void setShowPlusDestinataire(boolean showPlusDestinataire) {
//		this.showPlusDestinataire = showPlusDestinataire;
//	}

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
	
	public void handleReturnDialogPush(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			//List<ControleConformiteJSF> listeControle = (List<ControleConformiteJSF>) event.getObject();
			TaMessagePush j = (TaMessagePush) event.getObject();
			
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Message Push", 
					"Message push envoyé "
					)); 
		}
	}

}  
  
