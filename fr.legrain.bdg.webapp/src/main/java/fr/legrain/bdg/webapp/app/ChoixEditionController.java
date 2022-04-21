package fr.legrain.bdg.webapp.app;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
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

import fr.legrain.article.dto.TaFamilleDTO;
import fr.legrain.article.dto.TaTTvaDTO;
import fr.legrain.article.dto.TaTvaDTO;
import fr.legrain.article.dto.TaUniteDTO;
import fr.legrain.article.model.TaMarqueArticle;
import fr.legrain.bdg.edition.service.remote.ITaEditionServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.document.dto.TaTPaiementDTO;
import fr.legrain.document.model.TaTPaiement;
import fr.legrain.droits.service.SessionInfo;
import fr.legrain.droits.service.TenantInfo;
import fr.legrain.edition.dto.TaEditionDTO;
import fr.legrain.edition.model.TaEdition;
import fr.legrain.edition.model.TaTEdition;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.ModeObjetEcranServeur;
import fr.legrain.tiers.dto.TaCPaiementDTO;
import fr.legrain.tiers.dto.TaTCiviliteDTO;
import fr.legrain.tiers.dto.TaTEntiteDTO;
import fr.legrain.tiers.model.TaCPaiement;
import fr.legrain.tiers.model.TaFamilleTiers;
import fr.legrain.tiers.model.TaTTvaDoc;

@Named
@ViewScoped  
public class ChoixEditionController extends AbstractController implements Serializable {  

	@Inject @Named(value="tabListModelCenterBean")
	private TabListModelBean tabsCenter;

	@Inject @Named(value="tabViewsBean")
	private TabViewsBean tabViews;

	private String paramId;	

	protected ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();

	private @EJB ITaEditionServiceRemote taEditionService;

	private @Inject SessionInfo sessionInfo;
	private @Inject TenantInfo tenantInfo;
	
	private TaEditionDTO[] selectedTaEditionDTOs; 
	private TaEdition taEdition = new TaEdition();
	private TaEditionDTO newTaEditionDTO = new TaEditionDTO();
	private TaEditionDTO selectedTaEditionDTO = new TaEditionDTO();

	private TaTEdition taTEdition;
	private List<TaEdition> listeEditionDisponible;
			
	private LgrDozerMapper<TaEditionDTO,TaEdition> mapperUIToModel  = new LgrDozerMapper<TaEditionDTO,TaEdition>();
	private LgrDozerMapper<TaEdition,TaEditionDTO> mapperModelToUI  = new LgrDozerMapper<TaEdition,TaEditionDTO>();
	
//	private LgrDozerMapper<TaEmailDTO,TaEmail> mapperUIToModelTaEmail  = new LgrDozerMapper<TaEmailDTO,TaEmail>();
//	private LgrDozerMapper<TaEmail,TaEmailDTO> mapperModelToUITaEmail  = new LgrDozerMapper<TaEmail,TaEmailDTO>();

	private EditionParam param;
		
	public ChoixEditionController() {  
		
	}  

	@PostConstruct
	public void postConstruct(){
		refresh();
		System.out.println("ChoixEditionController.postConstruct()");
	}

	public void refresh(){

		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
		
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		
		try {
			if(sessionMap.get(EditionParam.NOM_OBJET_EN_SESSION)!=null) {
				param = (EditionParam) sessionMap.get(EditionParam.NOM_OBJET_EN_SESSION);
				sessionMap.remove(EditionParam.NOM_OBJET_EN_SESSION);
			}
			
			if(param!=null) {

				String codeTypeEdition = param.getCodeTypeEdition();
				String idActionInterne = param.getIdActionInterne();
				
				listeEditionDisponible = taEditionService.rechercheEditionDisponible(codeTypeEdition, idActionInterne, null);
				
				mapperModelToUI.map(taEdition, selectedTaEditionDTO);
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public void autoCompleteMapUIToDTO() {
//		if(taTEmailDTO!=null) {
//			validateUIField(Const.C_CODE_T_EMAIL,taTEmailDTO.getCodeTEmail());
//			selectedTaEditionDTO.setCodeTTiers(taTEmailDTO.getCodeTEmail());
//		}else {
//			selectedTaEditionDTO.setCodeTTiers(null);
//		}
//		
//		if(taEmailDTO!=null) {
//			validateUIField(Const.C_CODE_eT_ENTITE,taEmailDTO.getCodeTEntite());
//			selectedTaEditionDTO.setCodeTEntite(taEmailDTO.getCodeTEntite());
//		}else {
//			selectedTaEditionDTO.setCodeTEntite(null);
//		}
	}

	public void autoCompleteMapDTOtoUI() {
		try {
			taTEdition = null;

//			taTEmailDTO = null;
//			taEmailDTODestinataire = null;

//			if(selectedTaEditionDTO.getCodeTTiers()!=null && !selectedTaEditionDTO.getCodeTTiers().equals("")) {
//				taEmail = taTTiersService.findByCode(selectedTaEditionDTO.getCodeTTiers());
//				taTEmailDTO=taTTiersService.findByCodeDTO(selectedTaEditionDTO.getCodeTTiers());
//			}
//			if(selectedTaEditionDTO.getCodeTCivilite()!=null && !selectedTaEditionDTO.getCodeTCivilite().equals("")) {
//				taTEmail = taTCivlicteService.findByCode(selectedTaEditionDTO.getCodeTCivilite());
//				taTCiviliteDTO = taTCivlicteService.findByCodeDTO(selectedTaEditionDTO.getCodeTCivilite());
//			}
				
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void actImprimer(ActionEvent actionEvent) {
		try {
			taEdition.setMapParametreEdition(param.getMapParametreEdition());
			PrimeFaces.current().dialog().closeDynamic(taEdition);
//			System.out.println("ChoixEditionController.actImprimer() "+taEdition.getLibelleEdition());
//			BdgProperties bdgProperties = new BdgProperties();
//			String localPath = bdgProperties.osTempDirDossier(tenantInfo.getTenantId())+"/"+bdgProperties.tmpFileName(taEdition.getFichierNom());
//			
//			FileOutputStream fileOuputStream = null;
//			try { 
//			    fileOuputStream = new FileOutputStream(localPath); 
//			    fileOuputStream.write(taEdition.getFichierBlob());
//			 } finally {
//			    fileOuputStream.close();
//			 }
//			//Files.write(Paths.get("filename"), bytes);

		} catch(Exception e) {
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", 
					"Email non envoyée "
					)); 
		}
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
		selectedTaEditionDTO=new TaEditionDTO();
		selectedTaEditionDTOs=new TaEditionDTO[]{selectedTaEditionDTO};
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
				if(selectedTaEditionDTO!=null && selectedTaEditionDTO.getId()!=null  && selectedTaEditionDTO.getId()!=0 ) retour = false;
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

	public TaEditionDTO[] getSelectedTaEditionDTOs() {
		return selectedTaEditionDTOs;
	}

	public void setSelectedTaEditionDTOs(TaEditionDTO[] selectedTaEditionDTOs) {
		this.selectedTaEditionDTOs = selectedTaEditionDTOs;
	}

	public TaEditionDTO getNewTaEditionDTO() {
		return newTaEditionDTO;
	}

	public void setNewTaEditionDTO(TaEditionDTO newTaEditionDTO) {
		this.newTaEditionDTO = newTaEditionDTO;
	}

	public TaEditionDTO getSelectedTaEditionDTO() {
		return selectedTaEditionDTO;
	}

	public void setSelectedTaEditionDTO(TaEditionDTO selectedTaEditionDTO) {
		this.selectedTaEditionDTO = selectedTaEditionDTO;
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
			//			  TaEditionDTO dtoTemp=new TaEditionDTO();
			//			  PropertyUtils.setSimpleProperty(dtoTemp, nomChamp, value);
			//			  taTiersService.validateDTOProperty(dtoTemp, nomChamp, ITaEditionServiceRemote.validationContext );

			//				//validation automatique via la JSR bean validation
			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
			Set<ConstraintViolation<TaEditionDTO>> violations = factory.getValidator().validateValue(TaEditionDTO.class,nomChamp,value);
			if (violations.size() > 0) {
				messageComplet = "Erreur de validation : ";
				//					List<IStatus> statusList = new ArrayList<IStatus>();
				for (ConstraintViolation<TaEditionDTO> cv : violations) {
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

			//selectedTaEditionDTO.setAdresse1Adresse("abcd");

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
//					if(value instanceof TaEmailDTO){
//	//				entity=(TaFamille) value;
//						entity = taEmailService.findByCode(((TaFamilleDTO)value).getCodeFamille());
//					}else{
//						entity = taEmailService.findByCode((String)value);
//					}
//				}else{
//					selectedTaArticleDTO.setCodeFamille("");
//				}
//				//taArticle.setTaFamille(entity);
//				for (TaEmail f : taMessageEmail.getDestinataires()) {
//					if(f.getIdEmail()==((TaEmailDTO)value).getId())
//						entity = f;
//				}
//				taMessageEmail.getDestinataires().remove(entity);
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
//			if(nomChamp.equals(Const.C_ADRESSE_EMAIL+"_DESTINATAIRE") 
//					|| nomChamp.equals(Const.C_ADRESSE_EMAIL+"_CC")
//					|| nomChamp.equals(Const.C_ADRESSE_EMAIL+"_BCC")) {
//				
//				TaEmail entity =null;
//				if(value!=null){
//					if(value instanceof TaEmailDTO){
////					entity=(TaFamille) value;
//						entity = taEmailService.findByCode(((TaEmailDTO)value).getAdresseEmail());
//					}else{
//						entity = taEmailService.findByCode((String)value);
//					}
//				}else{
//					//selectedTaEditionDTO.setCodeFamille("");
//				}
//				TaContactMessageEmail taContactMessageEmail = new TaContactMessageEmail();
//				taContactMessageEmail.setAdresseEmail(entity.getAdresseEmail());
//				taContactMessageEmail.setTaTiers(entity.getTaTiers());
//				
//				if(nomChamp.equals(Const.C_ADRESSE_EMAIL+"_DESTINATAIRE")) {
//					taContactMessageEmail.setTaEdition(taMessageEmail);
//					if(taMessageEmail.getDestinataires()==null) {
//						taMessageEmail.setDestinataires(new HashSet<>());
//					}
//					taMessageEmail.getDestinataires().add(taContactMessageEmail);
//				} else if(nomChamp.equals(Const.C_ADRESSE_EMAIL+"_CC")) {
//					taContactMessageEmail.setTaEditionCc(taMessageEmail);
//					if(taMessageEmail.getCc()==null) {
//						taMessageEmail.setCc(new HashSet<>());
//					}
//					taMessageEmail.getCc().add(taContactMessageEmail);
//				} else if(nomChamp.equals(Const.C_ADRESSE_EMAIL+"_BCC")) {
//					taContactMessageEmail.setTaEditionBcc(taMessageEmail);
//					if(taMessageEmail.getBcc()==null) {
//						taMessageEmail.setBcc(new HashSet<>());
//					}
//					taMessageEmail.getBcc().add(taContactMessageEmail);
//				} 
//
//			} else if(nomChamp.equals(Const.C_CODE_TIERS)) {
////		
//			}
//			

			return false;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
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

	public TaEdition getTaEdition() {
		return taEdition;
	}

	public void setTaEdition(TaEdition taTiers) {
		this.taEdition = taTiers;
	}

	public TaTEdition getTaTEdition() {
		return taTEdition;
	}

	public void setTaTEdition(TaTEdition taTEmail) {
		this.taTEdition = taTEmail;
	}

	public List<TaEdition> getListeEditionDisponible() {
		return listeEditionDisponible;
	}

	public void setListeEditionDisponible(List<TaEdition> listeEditionDisponible) {
		this.listeEditionDisponible = listeEditionDisponible;
	}
	
	public void actDialogCatalogueEdition(ActionEvent actionEvent) {
	    
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
		
//		creerLot();
//		sessionMap.put("lotBR", selectedTaLBonReceptionDTOJSF.getTaLot());
//		String numLot =  (String) actionEvent.getComponent().getAttributes().get("lot");
		//sessionMap.put("numLot", selectedTaLBonReceptionDTOJSF.getDto().getNumLot());
//		sessionMap.put("numLot", numLot);
		
//		EditionParam email = new EditionParam();
//		email.setEmailExpediteur(null);
//		email.setNomExpediteur(taInfoEntrepriseService.findInstance().getNomInfoEntreprise()); 
//		email.setSubject(taInfoEntrepriseService.findInstance().getNomInfoEntreprise()+" Facture "+masterEntity.getCodeDocument()); 
//		email.setBodyPlain("Votre facture "+masterEntity.getCodeDocument());
//		email.setBodyHtml("Votre facture "+masterEntity.getCodeDocument());
//		email.setDestinataires(new String[]{masterEntity.getTaTiers().getTaEmail().getAdresseEmail()});
//		email.setEmailDestinataires(new TaEmail[]{masterEntity.getTaTiers().getTaEmail()});
//		
//		EmailPieceJointeParam pj1 = new EmailPieceJointeParam();
//		pj1.setFichier(new File(((ITaFactureServiceRemote)taDocumentService).generePDF(masterEntity.getIdDocument())));
//		pj1.setTypeMime("pdf");
//		pj1.setNomFichier(pj1.getFichier().getName());
//		email.setPiecesJointes(
//				new EmailPieceJointeParam[]{pj1}
//				);
		
//		sessionMap.put(EditionParam.NOM_OBJET_EN_SESSION, email);
        
        PrimeFaces.current().dialog().openDynamic("/dialog_catalogue_edition", options, params);
		
//		FacesContext context = FacesContext.getCurrentInstance();  
//		context.addMessage(null, new FacesMessage("Tiers", "actAbout")); 	    
	}
	
	public void handleReturnDialogCatalogueEdition(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			//List<ControleConformiteJSF> listeControle = (List<ControleConformiteJSF>) event.getObject();
//			TaEdition taEdition = (TaEdition) event.getObject();
//			
//			if(taEdition!=null) {
//				System.out.println("ChoixEditionController.actImprimer() "+taEdition.getLibelleEdition());
//				BdgProperties bdgProperties = new BdgProperties();
//				String localPath = bdgProperties.osTempDirDossier(tenantInfo.getTenantId())+"/"+bdgProperties.tmpFileName(taEdition.getFichierNom());
//				
//				try { 
//					Files.write(Paths.get(localPath), taEdition.getFichierBlob());
//				 } catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				String pdf = taFactureService.generePDF(selectedDocumentDTO.getId(),localPath);
//				
////				 if (validate(url)) {
//					 PrimeFaces.current().executeScript("window.open('/edition?fichier="+pdf+"')");
////				 }
//			}
			

		}
	}

}  
  
