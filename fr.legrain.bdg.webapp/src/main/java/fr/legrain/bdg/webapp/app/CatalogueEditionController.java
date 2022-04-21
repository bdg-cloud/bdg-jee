package fr.legrain.bdg.webapp.app;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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

import fr.legrain.article.dto.TaFamilleDTO;
import fr.legrain.article.dto.TaTTvaDTO;
import fr.legrain.article.dto.TaTvaDTO;
import fr.legrain.article.dto.TaUniteDTO;
import fr.legrain.article.model.TaMarqueArticle;
import fr.legrain.bdg.edition.service.remote.ITaActionInterneProgServiceRemote;
import fr.legrain.bdg.edition.service.remote.ITaActionInterneServiceRemote;
import fr.legrain.bdg.edition.service.remote.ITaEditionCatalogueServiceRemote;
import fr.legrain.bdg.edition.service.remote.ITaEditionServiceRemote;
import fr.legrain.bdg.edition.service.remote.ITaTEditionCatalogueServiceRemote;
import fr.legrain.bdg.edition.service.remote.ITaTEditionServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.document.dto.TaTPaiementDTO;
import fr.legrain.document.model.TaTPaiement;
import fr.legrain.droits.service.SessionInfo;
import fr.legrain.droits.service.TenantInfo;
import fr.legrain.edition.dto.TaEditionCatalogueDTO;
import fr.legrain.edition.dto.TaEditionDTO;
import fr.legrain.edition.model.TaActionInterne;
import fr.legrain.edition.model.TaActionInterneProg;
import fr.legrain.edition.model.TaEdition;
import fr.legrain.edition.model.TaEditionCatalogue;
import fr.legrain.edition.model.TaTEdition;
import fr.legrain.edition.model.TaTEditionCatalogue;
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
public class CatalogueEditionController extends AbstractController implements Serializable {  

	@Inject @Named(value="tabListModelCenterBean")
	private TabListModelBean tabsCenter;

	@Inject @Named(value="tabViewsBean")
	private TabViewsBean tabViews;

	private String paramId;	

	protected ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();

	private @EJB ITaEditionCatalogueServiceRemote taEditionCatalogueService;
	private @EJB ITaTEditionCatalogueServiceRemote taTEditionCatalogueService;
	private @EJB ITaActionInterneProgServiceRemote taActionInterneProgService;
	
	private @EJB ITaEditionServiceRemote taEditionService;
	private @EJB ITaTEditionServiceRemote taTEditionService;
	private @EJB ITaActionInterneServiceRemote taActionInterneService;

	private @Inject SessionInfo sessionInfo;
	private @Inject TenantInfo tenantInfo;
	
	private TaEditionCatalogueDTO[] selectedTaEditionCatalogueDTOs; 
	private TaEditionCatalogue taEditionCatalogue = new TaEditionCatalogue();
	private TaEditionCatalogueDTO newTaEditionCatalogueDTO = new TaEditionCatalogueDTO();
	private TaEditionCatalogueDTO selectedTaEditionCatalogueDTO = new TaEditionCatalogueDTO();

	private TaTEditionCatalogue taTEditionCatalogue;
	private List<TaEditionCatalogueDTO> listeEditionCatalogueDisponible;
	private List<TaEditionDTO> listeEditionDossier;
			
	private LgrDozerMapper<TaEditionCatalogueDTO,TaEditionCatalogue> mapperUIToModel  = new LgrDozerMapper<TaEditionCatalogueDTO,TaEditionCatalogue>();
	private LgrDozerMapper<TaEditionCatalogue,TaEditionCatalogueDTO> mapperModelToUI  = new LgrDozerMapper<TaEditionCatalogue,TaEditionCatalogueDTO>();
	
//	private LgrDozerMapper<TaEmailDTO,TaEmail> mapperUIToModelTaEmail  = new LgrDozerMapper<TaEmailDTO,TaEmail>();
//	private LgrDozerMapper<TaEmail,TaEmailDTO> mapperModelToUITaEmail  = new LgrDozerMapper<TaEmail,TaEmailDTO>();

	private EmailParam param;
		
	public CatalogueEditionController() {  
		
	}  

	@PostConstruct
	public void postConstruct(){
		refresh();

		
		System.out.println("ChoixEditionCatalogueController.postConstruct()");
	}

	public void refresh(){

		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
		
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		
		try {
			if(sessionMap.get(EmailParam.NOM_OBJET_EN_SESSION)!=null) {
				param = (EmailParam) sessionMap.get(EmailParam.NOM_OBJET_EN_SESSION);
				sessionMap.remove(EmailParam.NOM_OBJET_EN_SESSION);
			}
			
			if(param!=null) {

				mapperModelToUI.map(taEditionCatalogue, selectedTaEditionCatalogueDTO);
			}
			
//			rechercheEditionDisponible(String codeDossier, String codeTypeEdition, String idAction, List<String> listeCodeEditionDejaImportees);
//			rechercheEditionDisponible(String codeDossier, String codeTypeEdition, String idAction, Map<String,String> mapCodeEditionDejaImporteesVersion);

			listeEditionCatalogueDisponible = taEditionCatalogueService.rechercheEditionDisponible(tenantInfo.getTenantId(), null, null, new ArrayList<String>());
			listeEditionDossier = taEditionService.findAllLight();
			Map<String,TaEditionDTO> mapEditionDossier = new HashMap<String,TaEditionDTO>();
			for (TaEditionDTO taEditionDTO : listeEditionDossier) {
				mapEditionDossier.put(taEditionDTO.getCodeEdition(), taEditionDTO);
			}
			
			for (TaEditionCatalogueDTO taEditionCatalogueDTO : listeEditionCatalogueDisponible) {
				if(mapEditionDossier.keySet().contains(taEditionCatalogueDTO.getCodeEdition())) {
					//deja telecharge
					if(taEditionCatalogueDTO.getVersionEdition()!=null
							&& mapEditionDossier.get(taEditionCatalogueDTO.getCodeEdition()).getVersionEdition()!=null
							&& !taEditionCatalogueDTO.getVersionEdition().equals(mapEditionDossier.get(taEditionCatalogueDTO.getCodeEdition()).getVersionEdition())) {
						//version diff
						taEditionCatalogueDTO.setEtatTelechargement("maj");
					} else {
						taEditionCatalogueDTO.setEtatTelechargement("ok");
					}
				} else {
					taEditionCatalogueDTO.setEtatTelechargement("");
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public void autoCompleteMapUIToDTO() {
//		if(taTEmailDTO!=null) {
//			validateUIField(Const.C_CODE_T_EMAIL,taTEmailDTO.getCodeTEmail());
//			selectedTaEditionCatalogueDTO.setCodeTTiers(taTEmailDTO.getCodeTEmail());
//		}else {
//			selectedTaEditionCatalogueDTO.setCodeTTiers(null);
//		}
//		
//		if(taEmailDTO!=null) {
//			validateUIField(Const.C_CODE_eT_ENTITE,taEmailDTO.getCodeTEntite());
//			selectedTaEditionCatalogueDTO.setCodeTEntite(taEmailDTO.getCodeTEntite());
//		}else {
//			selectedTaEditionCatalogueDTO.setCodeTEntite(null);
//		}
	}

	public void autoCompleteMapDTOtoUI() {
		try {
			taTEditionCatalogue = null;

//			taTEmailDTO = null;
//			taEmailDTODestinataire = null;

//			if(selectedTaEditionCatalogueDTO.getCodeTTiers()!=null && !selectedTaEditionCatalogueDTO.getCodeTTiers().equals("")) {
//				taEmail = taTTiersService.findByCode(selectedTaEditionCatalogueDTO.getCodeTTiers());
//				taTEmailDTO=taTTiersService.findByCodeDTO(selectedTaEditionCatalogueDTO.getCodeTTiers());
//			}
//			if(selectedTaEditionCatalogueDTO.getCodeTCivilite()!=null && !selectedTaEditionCatalogueDTO.getCodeTCivilite().equals("")) {
//				taTEmail = taTCivlicteService.findByCode(selectedTaEditionCatalogueDTO.getCodeTCivilite());
//				taTCiviliteDTO = taTCivlicteService.findByCodeDTO(selectedTaEditionCatalogueDTO.getCodeTCivilite());
//			}
				
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void actImprimer(ActionEvent actionEvent) {
		try {
			
			PrimeFaces.current().dialog().closeDynamic(taEditionCatalogue);
//			System.out.println("ChoixEditionCatalogueController.actImprimer() "+taEditionCatalogue.getLibelleEditionCatalogue());
//			BdgProperties bdgProperties = new BdgProperties();
//			String localPath = bdgProperties.osTempDirDossier(tenantInfo.getTenantId())+"/"+bdgProperties.tmpFileName(taEditionCatalogue.getFichierNom());
//			
//			FileOutputStream fileOuputStream = null;
//			try { 
//			    fileOuputStream = new FileOutputStream(localPath); 
//			    fileOuputStream.write(taEditionCatalogue.getFichierBlob());
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
		selectedTaEditionCatalogueDTO=new TaEditionCatalogueDTO();
		selectedTaEditionCatalogueDTOs=new TaEditionCatalogueDTO[]{selectedTaEditionCatalogueDTO};
	}
	
	public void actTelechargerEdition(TaEditionCatalogueDTO ecDto) {
		System.out.println("CatalogueEditionController.actTelechargerEdition() --qs- " + ecDto.getCodeEdition());
		try {
			TaEditionCatalogue ec = taEditionCatalogueService.findById(ecDto.getId());
			TaEdition e = mapTaEditionCatalogue2TaEdition(ec, false);
			
			taEditionService.enregistrerMerge(e);
			
			refresh();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void actTelechargerEdition(TaEditionCatalogue ec) {
		System.out.println("CatalogueEditionController.actTelechargerEdition() --- " + ec.getCodeEdition());
		try {
			TaEdition e = mapTaEditionCatalogue2TaEdition(ec, false);
			
			taEditionService.enregistrerMerge(e);
			
			refresh();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void actMajEdition(TaEditionCatalogueDTO ecDto) {
		System.out.println("CatalogueEditionController.actMajEdition() --qs- " + ecDto.getCodeEdition());
		try {
			TaEditionCatalogue ec = taEditionCatalogueService.findById(ecDto.getId());
			TaEdition e = mapTaEditionCatalogue2TaEdition(ec, true);
			
			taEditionService.enregistrerMerge(e);
			
			refresh();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void actMajEdition(TaEditionCatalogue ec) {
		System.out.println("CatalogueEditionController.actMajEdition() --- " + ec.getCodeEdition());
		try {
			TaEdition e = mapTaEditionCatalogue2TaEdition(ec, true);
			
			taEditionService.enregistrerMerge(e);
			
			refresh();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public TaEdition mapTaEditionCatalogue2TaEdition(TaEditionCatalogue ec, boolean miseAJour) throws Exception {
		
		TaEdition e = null;
		if(miseAJour) {
			e = taEditionService.findByCode(ec.getCodeEdition());
		} else {
			e = new TaEdition();
			e.setCodeEdition(ec.getCodeEdition());
		}
		e.setLibelleEdition(ec.getLibelleEdition());
		e.setDescriptionEdition(ec.getDescriptionEdition());
		e.setDateImportation(new Date());
		e.setImportationManuelle(false);
		e.setFichierBlob(ec.getFichierBlob());
		e.setVersionEdition(ec.getVersionEdition());
		e.setLibrairie(ec.getLibrairie());
		e.setTheme(ec.getTheme());
		e.setResourcesPath(ec.getResourcesPath());
		
		if(ec.getTaTEdition()!=null) { //le modèle a un type
			TaTEdition te = taTEditionService.findByCode(ec.getTaTEdition().getCodeTEdition());

			if(te==null) { //ce type n'existe pas dans le dossier, on le créé
				te = new TaTEdition();
			}
			te.setCodeTEdition(ec.getTaTEdition().getCodeTEdition());
			te.setLibelle(ec.getTaTEdition().getLibelle());
			te.setDescription(ec.getTaTEdition().getDescription());
			te.setSysteme(ec.getTaTEdition().isSysteme());
			
			te = taTEditionService.merge(te);
			e.setTaTEdition(te);
		} else {
			e.setTaTEdition(null);
		}
		
		
		if(e.getActionInterne()!=null){
			e.getActionInterne().clear();
		} else {
			e.setActionInterne(new HashSet<>());
		}
		if(ec.getActionInterne()!=null && !ec.getActionInterne().isEmpty()) { //le modèle a une action interne
			
			for (TaActionInterneProg actIntProg : ec.getActionInterne()) {
				TaActionInterne ac = taActionInterneService.findByCode(actIntProg.getIdAction());
				if(ac==null) { //cette action n'existe pas dans le dossier, on la créée
					ac = new TaActionInterne();
				}
				ac.setidAction(actIntProg.getIdAction());
				ac.setLibelle(actIntProg.getLibelle());
				ac.setDescription(actIntProg.getDescription());
				ac.setSysteme(actIntProg.getSysteme());
				
				ac = taActionInterneService.merge(ac);
				e.getActionInterne().add(ac);
			}
		}
		return e;
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
				if(selectedTaEditionCatalogueDTO!=null && selectedTaEditionCatalogueDTO.getId()!=null  && selectedTaEditionCatalogueDTO.getId()!=0 ) retour = false;
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

	public TaEditionCatalogueDTO[] getSelectedTaEditionCatalogueDTOs() {
		return selectedTaEditionCatalogueDTOs;
	}

	public void setSelectedTaEditionCatalogueDTOs(TaEditionCatalogueDTO[] selectedTaEditionCatalogueDTOs) {
		this.selectedTaEditionCatalogueDTOs = selectedTaEditionCatalogueDTOs;
	}

	public TaEditionCatalogueDTO getNewTaEditionCatalogueDTO() {
		return newTaEditionCatalogueDTO;
	}

	public void setNewTaEditionCatalogueDTO(TaEditionCatalogueDTO newTaEditionCatalogueDTO) {
		this.newTaEditionCatalogueDTO = newTaEditionCatalogueDTO;
	}

	public TaEditionCatalogueDTO getSelectedTaEditionCatalogueDTO() {
		return selectedTaEditionCatalogueDTO;
	}

	public void setSelectedTaEditionCatalogueDTO(TaEditionCatalogueDTO selectedTaEditionCatalogueDTO) {
		this.selectedTaEditionCatalogueDTO = selectedTaEditionCatalogueDTO;
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
			//			  TaEditionCatalogueDTO dtoTemp=new TaEditionCatalogueDTO();
			//			  PropertyUtils.setSimpleProperty(dtoTemp, nomChamp, value);
			//			  taTiersService.validateDTOProperty(dtoTemp, nomChamp, ITaEditionCatalogueServiceRemote.validationContext );

			//				//validation automatique via la JSR bean validation
			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
			Set<ConstraintViolation<TaEditionCatalogueDTO>> violations = factory.getValidator().validateValue(TaEditionCatalogueDTO.class,nomChamp,value);
			if (violations.size() > 0) {
				messageComplet = "Erreur de validation : ";
				//					List<IStatus> statusList = new ArrayList<IStatus>();
				for (ConstraintViolation<TaEditionCatalogueDTO> cv : violations) {
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

			//selectedTaEditionCatalogueDTO.setAdresse1Adresse("abcd");

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
//					//selectedTaEditionCatalogueDTO.setCodeFamille("");
//				}
//				TaContactMessageEmail taContactMessageEmail = new TaContactMessageEmail();
//				taContactMessageEmail.setAdresseEmail(entity.getAdresseEmail());
//				taContactMessageEmail.setTaTiers(entity.getTaTiers());
//				
//				if(nomChamp.equals(Const.C_ADRESSE_EMAIL+"_DESTINATAIRE")) {
//					taContactMessageEmail.setTaEditionCatalogue(taMessageEmail);
//					if(taMessageEmail.getDestinataires()==null) {
//						taMessageEmail.setDestinataires(new HashSet<>());
//					}
//					taMessageEmail.getDestinataires().add(taContactMessageEmail);
//				} else if(nomChamp.equals(Const.C_ADRESSE_EMAIL+"_CC")) {
//					taContactMessageEmail.setTaEditionCatalogueCc(taMessageEmail);
//					if(taMessageEmail.getCc()==null) {
//						taMessageEmail.setCc(new HashSet<>());
//					}
//					taMessageEmail.getCc().add(taContactMessageEmail);
//				} else if(nomChamp.equals(Const.C_ADRESSE_EMAIL+"_BCC")) {
//					taContactMessageEmail.setTaEditionCatalogueBcc(taMessageEmail);
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

	public TaEditionCatalogue getTaEditionCatalogue() {
		return taEditionCatalogue;
	}

	public void setTaEditionCatalogue(TaEditionCatalogue taTiers) {
		this.taEditionCatalogue = taTiers;
	}

	public TaTEditionCatalogue getTaTEditionCatalogue() {
		return taTEditionCatalogue;
	}

	public void setTaTEditionCatalogue(TaTEditionCatalogue taTEmail) {
		this.taTEditionCatalogue = taTEmail;
	}

	public List<TaEditionCatalogueDTO> getListeEditionCatalogueDisponible() {
		return listeEditionCatalogueDisponible;
	}

	public void setListeEditionCatalogueDisponible(List<TaEditionCatalogueDTO> listeEditionCatalogueDisponible) {
		this.listeEditionCatalogueDisponible = listeEditionCatalogueDisponible;
	}

}  
  
