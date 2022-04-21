package fr.legrain.bdg.webapp.servicewebexterne;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
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

import org.apache.commons.io.IOUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.component.tabview.TabView;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.bdg.droits.service.remote.ITaUtilisateurServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaCompteServiceWebExterneServiceRemote;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaServiceWebExterneServiceRemote;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaTAuthentificationServiceRemote;
import fr.legrain.bdg.tache.service.remote.ITaEvenementServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTTvaDocServiceRemote;
import fr.legrain.bdg.webapp.ConstWeb;
import fr.legrain.bdg.webapp.app.AbstractController;
import fr.legrain.bdg.webapp.app.AutorisationBean;
import fr.legrain.bdg.webapp.app.GestionOAuthController;
import fr.legrain.bdg.webapp.app.LgrTab;
import fr.legrain.bdg.webapp.app.TabListModelBean;
import fr.legrain.bdg.webapp.app.TabViewsBean;
import fr.legrain.bdg.webapp.tiers.AdresseController;
import fr.legrain.droits.service.SessionInfo;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.ModeObjetEcranServeur;
import fr.legrain.servicewebexterne.dto.TaCompteServiceWebExterneDTO;
import fr.legrain.servicewebexterne.dto.TaServiceWebExterneDTO;
import fr.legrain.servicewebexterne.dto.TaTAuthentificationDTO;
import fr.legrain.servicewebexterne.model.TaCompteServiceWebExterne;
import fr.legrain.servicewebexterne.model.TaServiceWebExterne;
import fr.legrain.servicewebexterne.model.TaTAuthentification;
import fr.legrain.servicewebexterne.model.UtilServiceWebExterne;
import fr.legrain.tiers.dto.TaTiersDTO;

@Named
@ViewScoped  
public class CompteServiceWebExterneController extends AbstractController implements Serializable {  

	@Inject @Named(value="tabListModelCenterBean")
	private TabListModelBean tabsCenter;

	@Inject @Named(value="tabViewsBean")
	private TabViewsBean tabViews;

	private String paramId;

	private TabView tabViewTaCompteServiceWebExterne; //Le Binding sur les onglets "secondaire" semble ne pas fonctionné, les onglets ne sont pas générés au bon moment avec le c:forEach

	private List<TaCompteServiceWebExterneDTO> values; 

	private StreamedContent logo;

	private @Inject SessionInfo sessionInfo;
	
	@Inject private GestionOAuthController gestionOAuthController;

	protected ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();

	private @EJB ITaCompteServiceWebExterneServiceRemote taCompteServiceWebExterneService;
	private @EJB ITaServiceWebExterneServiceRemote taServiceWebExterneService;

	private @EJB ITaInfoEntrepriseServiceRemote taInfoEntrepriseService;

	private @EJB ITaTAuthentificationServiceRemote taTCivlicteService;
	private @EJB ITaTTvaDocServiceRemote taTTvaDocService;


	private @EJB ITaEvenementServiceRemote taEvenementService;
	private @EJB ITaUtilisateurServiceRemote taUtilisateurService;

	private TaCompteServiceWebExterneDTO[] selectedTaCompteServiceWebExterneDTOs; 
	private TaCompteServiceWebExterne taCompteServiceWebExterne = new TaCompteServiceWebExterne();
	private TaCompteServiceWebExterneDTO newTaCompteServiceWebExterneDTO = new TaCompteServiceWebExterneDTO();
	private TaCompteServiceWebExterneDTO selectedTaCompteServiceWebExterneDTO = new TaCompteServiceWebExterneDTO();

	private TaServiceWebExterne taServiceWebExterne;
	private TaTAuthentification taTAuthentification;
	private boolean authenticationOAuth = false;


	private LgrDozerMapper<TaCompteServiceWebExterneDTO,TaCompteServiceWebExterne> mapperUIToModel  = new LgrDozerMapper<TaCompteServiceWebExterneDTO,TaCompteServiceWebExterne>();
	private LgrDozerMapper<TaCompteServiceWebExterne,TaCompteServiceWebExterneDTO> mapperModelToUI  = new LgrDozerMapper<TaCompteServiceWebExterne,TaCompteServiceWebExterneDTO>();
	private UtilServiceWebExterne utilServiceWebExterne = new UtilServiceWebExterne();

	// Dima - Début
	private TaServiceWebExterneDTO taServiceWebExterneDTO;
	private TaTAuthentificationDTO taTAuthentificationDTO;
	
	@Inject protected AutorisationBean autorisationBean;	

	public TaTAuthentificationDTO getTaTAuthentificationDTO() {
		return taTAuthentificationDTO;
	}

	public void setTaTAuthentificationDTO(TaTAuthentificationDTO taTCiviliteDTO) {
		this.taTAuthentificationDTO = taTCiviliteDTO;
	}
	// Dima -  Fin

	public CompteServiceWebExterneController() {  
		//values = getValues();
	}  

	@PostConstruct
	public void postConstruct(){

		refresh();

		//		FaceletContext faceletContext = (FaceletContext) FacesContext.getCurrentInstance().getAttributes().get(FaceletContext.FACELET_CONTEXT_KEY);
		//		String formId = (String) faceletContext.getAttribute("paramId");
		//		if(formId!=null && !formId.equals("")) {
		//			try {
		//				selectedTaCompteServiceWebExterneDTO = TaCompteServiceWebExterneDTOService.findById(LibConversion.stringToInteger(paramId));
		//			} catch (FinderException e) {
		//				// TODO Auto-generated catch block
		//				e.printStackTrace();
		//			}
		//		}
	}

	public void refresh(){
		//values = taTaCompteServiceWebExterneService.findAllLight();
		authenticationOAuth = false;
		values = taCompteServiceWebExterneService.selectAllDTO();
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}

	public List<TaCompteServiceWebExterneDTO> getValues(){  
		return values;
	}

	public void actAnnuler(ActionEvent actionEvent) {
		try {
			switch (modeEcran.getMode()) {
			case C_MO_INSERTION:
				//				if(selectedTaCompteServiceWebExterneDTO.getCodeServiceWebExterne()!=null) {
				//					taTaCompteServiceWebExterneService.annuleCode(selectedTaCompteServiceWebExterneDTO.getCodeServiceWebExterne());
				//				}
				taCompteServiceWebExterne = new TaCompteServiceWebExterne();
				mapperModelToUI.map(taCompteServiceWebExterne,selectedTaCompteServiceWebExterneDTO );
				selectedTaCompteServiceWebExterneDTO=new TaCompteServiceWebExterneDTO();

				if(!values.isEmpty()) selectedTaCompteServiceWebExterneDTO = values.get(0);
				onRowSelect(null);
				break;
			case C_MO_EDITION:
				if(selectedTaCompteServiceWebExterneDTO.getId()!=null && selectedTaCompteServiceWebExterneDTO.getId()!=0){
					taCompteServiceWebExterne = taCompteServiceWebExterneService.findById(selectedTaCompteServiceWebExterneDTO.getId());
					selectedTaCompteServiceWebExterneDTO=taCompteServiceWebExterneService.findByIdDTO(selectedTaCompteServiceWebExterneDTO.getId());
				}				
				break;

			default:
				break;
			}
			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
			mapperModelToUI.map(taCompteServiceWebExterne, selectedTaCompteServiceWebExterneDTO);

			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("TaCompteServiceWebExterne", "actAnnuler"));
			}
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	public void autoCompleteMapUIToDTO() {
		if(taServiceWebExterneDTO!=null) {
			validateUIField(Const.C_CODE_SERVICE_WEB_EXTERNE,taServiceWebExterneDTO.getCodeTServiceWebExterne());
			selectedTaCompteServiceWebExterneDTO.setCodeServiceWebExterne(taServiceWebExterneDTO.getCodeTServiceWebExterne());
		}else selectedTaCompteServiceWebExterneDTO.setCodeServiceWebExterne(null);
		if(taTAuthentificationDTO!=null) {
			validateUIField(Const.C_CODE_T_AUTHENTIFICATION,taTAuthentificationDTO.getCodeTAuthentification());
			selectedTaCompteServiceWebExterneDTO.setCodeTAuthentification(taTAuthentificationDTO.getCodeTAuthentification());
		}else selectedTaCompteServiceWebExterneDTO.setCodeTAuthentification(null);

	}

	public void autoCompleteMapDTOtoUI() {
		try {
			taServiceWebExterne = null;
			taTAuthentification = null;

			taServiceWebExterneDTO = null;
			taTAuthentificationDTO = null;
			taServiceWebExterneDTO = null;

			if(selectedTaCompteServiceWebExterneDTO.getCodeServiceWebExterne()!=null && !selectedTaCompteServiceWebExterneDTO.getCodeServiceWebExterne().equals("")) {
				taServiceWebExterne = taServiceWebExterneService.findByCode(selectedTaCompteServiceWebExterneDTO.getCodeServiceWebExterne());
				taServiceWebExterneDTO=taServiceWebExterneService.findByCodeDTO(selectedTaCompteServiceWebExterneDTO.getCodeServiceWebExterne());
			}
			if(selectedTaCompteServiceWebExterneDTO.getCodeTAuthentification()!=null && !selectedTaCompteServiceWebExterneDTO.getCodeTAuthentification().equals("")) {
				taTAuthentification = taTCivlicteService.findByCode(selectedTaCompteServiceWebExterneDTO.getCodeTAuthentification());
				taTAuthentificationDTO = taTCivlicteService.findByCodeDTO(selectedTaCompteServiceWebExterneDTO.getCodeTAuthentification());
			}

		} catch (FinderException e) {
			e.printStackTrace();
		}
	}

	public void actEnregistrer(ActionEvent actionEvent) {		

		mapperUIToModel.map(selectedTaCompteServiceWebExterneDTO, taCompteServiceWebExterne);
		
		
		taCompteServiceWebExterne = utilServiceWebExterne.crypter(taCompteServiceWebExterne);

		try {
			taCompteServiceWebExterne = taCompteServiceWebExterneService.merge(taCompteServiceWebExterne,ITaCompteServiceWebExterneServiceRemote.validationContext);

			//			if(selectedTaCompteServiceWebExterneDTO.getCodeServiceWebExterne()!=null) {
			//				taTaCompteServiceWebExterneService.annuleCode(selectedTaCompteServiceWebExterneDTO.getCodeServiceWebExterne());
			//			}
			if(taCompteServiceWebExterne.isDefaut()) {
				taCompteServiceWebExterneService.changeDefaut(taCompteServiceWebExterne);
			}
			
			taCompteServiceWebExterne = utilServiceWebExterne.decrypter(taCompteServiceWebExterne);
			mapperModelToUI.map(taCompteServiceWebExterne, selectedTaCompteServiceWebExterneDTO);
			autoCompleteMapDTOtoUI();

			if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_INSERTION)==0) {
				values.add(selectedTaCompteServiceWebExterneDTO);
			}

			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

		} catch(Exception e) {
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			//context.addMessage(null, new FacesMessage("TaCompteServiceWebExterne", "actEnregistrer")); 
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "TaCompteServiceWebExterne", e.getMessage()));
		}

		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("TaCompteServiceWebExterne", "actEnregistrer"));
		}
	}

	public void actInserer(ActionEvent actionEvent) {
		try {
			selectedTaCompteServiceWebExterneDTOs= new TaCompteServiceWebExterneDTO[]{};
			selectedTaCompteServiceWebExterneDTO = new TaCompteServiceWebExterneDTO();
			taCompteServiceWebExterne = new TaCompteServiceWebExterne();
			
			selectedTaCompteServiceWebExterneDTO.setActif(true);
			selectedTaCompteServiceWebExterneDTO.setDefaut(true);

			//			String codeTaCompteServiceWebExterneDefaut = "C";
			String codeTaCompteServiceWebExterneDefaut = "";

			//			taGenCodeExService.libereVerrouTout(new ArrayList<String>(SessionListener.getSessions().keySet()));
			//			selectedTaCompteServiceWebExterneDTO.setCodeTaCompteServiceWebExterne(taTaCompteServiceWebExterneService.genereCode(null)); //ejb
			//			validateUIField(Const.C_CODE_TIERS,selectedTaCompteServiceWebExterneDTO.getCodeServiceWebExterne());//permet de verrouiller le code genere

			selectedTaCompteServiceWebExterneDTO.setCodeServiceWebExterne(codeTaCompteServiceWebExterneDefaut);

			//			selectedTaCompteServiceWebExterneDTO.setCodeCompta(selectedTaCompteServiceWebExterneDTO.getCodeServiceWebExterne()); //ejb
			//			validateUIField(Const.C_CODE_COMPTA,selectedTaCompteServiceWebExterneDTO.getCodeCompta()); //ejb
			//			selectedTaCompteServiceWebExterneDTO.setCompte("411"); //ejb
			//			validateUIField(Const.C_COMPTE,selectedTaCompteServiceWebExterneDTO.getCompte()); //ejb

			//TaTServiceWebExterneDAO daoTTaCompteServiceWebExterne = new TaTServiceWebExterneDAO(getEm());
			TaServiceWebExterne taTServiceWebExterne;

			taServiceWebExterne = taServiceWebExterneService.findByCode(codeTaCompteServiceWebExterneDefaut);

			//			if(taTServiceWebExterne!=null && taTServiceWebExterne.getCompteTTaCompteServiceWebExterne()!=null) {
			//				selectedTaCompteServiceWebExterneDTO.setCompte(taTServiceWebExterne.getCompteTTaCompteServiceWebExterne());
			//				this.taTServiceWebExterne = taTServiceWebExterne;
			//			} else {
			//				//selectedTaCompteServiceWebExterneDTO.setCompte(TaCompteServiceWebExternePlugin.getDefault().getPreferenceStore().
			//				//getString(PreferenceConstants.TIERS_COMPTE_COMPTALE_DEFAUT));
			//			}
			//			selectedTaCompteServiceWebExterneDTO.setActifTaCompteServiceWebExterne(true);
			//			//swtTaCompteServiceWebExterne.setTtcTaCompteServiceWebExterne(TaCompteServiceWebExternePlugin.getDefault().getPreferenceStore().
			//			//getBoolean(PreferenceConstants.TIERS_SAISIE_TTC_DEFAUT));
			//			selectedTaCompteServiceWebExterneDTO.setCodeTTvaDoc("F");

			autoCompleteMapDTOtoUI();

			modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
			scrollToTop();
			if(tabViewTaCompteServiceWebExterne!=null) {
				tabViewTaCompteServiceWebExterne.setActiveIndex(0);
				updateTab();
				//				adresseController.setMasterEntity(taTaCompteServiceWebExterne);
				//				adresseController.refresh(null);
			}
			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("TaCompteServiceWebExterne", "actInserer"));
			}

		} catch (FinderException e) {
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
			context.addMessage(null, new FacesMessage("TaCompteServiceWebExterne", "actModifier"));
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
			context.addMessage(null, new FacesMessage("TaCompteServiceWebExterne", "actAide"));
		}
	}

	//	public void actAideRetour(ActionEvent actionEvent) {
	//		
	//	}

	public void handleFileUpload(FileUploadEvent event) {
		FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
		FacesContext.getCurrentInstance().addMessage(null, message);

		try {

			//nouveauBareme.setCheminDoc(event.getFile().getFileName());
			taServiceWebExterne.setLogo(IOUtils.toByteArray(event.getFile().getInputStream()));
			//			fichier.setBlobFichierMiniature(fichier.resizeImage(IOUtils.toByteArray(event.getFile().getInputstream()), event.getFile().getFileName(), 640, 480));

			actEnregistrer(null);
			refresLogo();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void refresLogo() {
		try {
			logo = null;
			if(taServiceWebExterne!=null && taServiceWebExterne.getLogo()!=null) {
				InputStream stream = new ByteArrayInputStream(taServiceWebExterne.getLogo()); 
				//logo = new DefaultStreamedContent(stream, "image/png");
				logo = new DefaultStreamedContent(stream);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean afficher(String nomChamp) {
		if(taServiceWebExterneDTO!=null) {
			return utilServiceWebExterne.afficher(taServiceWebExterneDTO.getCodeServiceWebExterne(), nomChamp);
		} else {
			return false;
		}
	}
	
	public String label(String nomChamp) {
		if(taServiceWebExterneDTO!=null) {
			return utilServiceWebExterne.label(taServiceWebExterneDTO.getCodeServiceWebExterne(), nomChamp);
		} else {
			return "";
		}
	}
	
	public boolean afficherPanelOAuth(String codeService) {
		if(taServiceWebExterneDTO!=null) {
			return taServiceWebExterneDTO.getCodeServiceWebExterne().equals(codeService);
		} else {
			return false;
		}
	}

	public void supprimerLogo(ActionEvent e) {
		taServiceWebExterne.setLogo(null);
		actEnregistrer(null);
		refresLogo();
	}

	public void nouveau(ActionEvent actionEvent) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_COMPTE_SERVICE_WEB_EXTERNE);
		b.setTitre("Compte Service Web Externe");
		b.setTemplate("admin/compte_service_web_externe.xhtml");
		b.setIdTab(LgrTab.ID_TAB_COMPTE_SERVICE_WEB_EXTERNE);
		b.setStyle(LgrTab.CSS_CLASS_TAB_COMPTE_SERVICE_WEB_EXTERNE);
		b.setParamId("0");

		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
		actInserer(actionEvent);

		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("TaCompteServiceWebExterne", 
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


	public String detailResponsive() {
		//selectedTaCompteServiceWebExterneDTO = (TaCompteServiceWebExterneDTO) actionEvent.getComponent().getAttributes().get("selectionTaCompteServiceWebExterne");
		//    	onRowSelectResponsive(null);
		if(selectedTaCompteServiceWebExterneDTO!=null)   	return "/m/tiers.xhtml?idTaCompteServiceWebExterne="+selectedTaCompteServiceWebExterneDTO.getId()+"&faces-redirect=true";
		else return "";
	}


	public void repondreMessage(ActionEvent event) {
		Integer idMessage = (Integer) event.getComponent().getAttributes().get("idTaCompteServiceWebExterne");

		ExternalContext context2 = FacesContext.getCurrentInstance().getExternalContext();
		try {
			context2.redirect(context2.getRequestContextPath() + "/m/tiers.xhtml?idTaCompteServiceWebExterne="+idMessage);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void actSupprimer() {
		actSupprimer(null);
	}

	public void actSupprimer(ActionEvent actionEvent) {
		TaCompteServiceWebExterne taTaCompteServiceWebExterne = new TaCompteServiceWebExterne();
		try {

			if(selectedTaCompteServiceWebExterneDTO!=null && selectedTaCompteServiceWebExterneDTO.getId()!=null){
				taTaCompteServiceWebExterne = taCompteServiceWebExterneService.findById(selectedTaCompteServiceWebExterneDTO.getId());
			}
			
			if(authenticationOAuth) {
				//TODO vérifier que le compte OAuth est déconnecter ou au moins afficher un message
				if(taTaCompteServiceWebExterne.getTaServiceWebExterne().getCodeServiceWebExterne().equals(UtilServiceWebExterne.CODE_SWE_STRIPE_CONNECT)) {
					gestionOAuthController.getStripeBean().revokeToken();
				}
			}

			taCompteServiceWebExterneService.remove(taTaCompteServiceWebExterne);
			values.remove(selectedTaCompteServiceWebExterneDTO);

			if(!values.isEmpty()) {
				selectedTaCompteServiceWebExterneDTO = values.get(0);
			}else {
				selectedTaCompteServiceWebExterneDTO=new TaCompteServiceWebExterneDTO();
			}
			selectedTaCompteServiceWebExterneDTOs = null;
			updateTab();
			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("TaCompteServiceWebExterne", "actSupprimer"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "TaCompteServiceWebExterne", e.getCause().getCause().getCause().getCause().getMessage()));
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
		selectedTaCompteServiceWebExterneDTO=new TaCompteServiceWebExterneDTO();
		selectedTaCompteServiceWebExterneDTOs=new TaCompteServiceWebExterneDTO[]{selectedTaCompteServiceWebExterneDTO};

		//		//gestion du filtre de la liste
		//        RequestContext requestContext = RequestContext.getCurrentInstance();
		//        requestContext.execute("PF('wvDataTableListeTaCompteServiceWebExterne').filter()");


		//		LgrTab typeOngletDejaOuvert = null;
		//		for (LgrTab o : tabsCenter.getOnglets()) {
		//			if(LgrTab.TYPE_TAB_TIERS.equals(o.getTypeOnglet())) {
		//				typeOngletDejaOuvert = o;
		//			}
		//		}
		//		
		//		if(typeOngletDejaOuvert!=null) {
		////			if(!values.isEmpty()) {
		////				selectedTaCompteServiceWebExterneDTO = values.get(0);
		////			}
		//			tabsCenter.supprimerOnglet(typeOngletDejaOuvert);
		//		}
		//		if(ConstWeb.DEBUG) {
		//		FacesContext context = FacesContext.getCurrentInstance();  
		//		context.addMessage(null, new FacesMessage("TaCompteServiceWebExterne", "actFermer"));
		//		}
	}

	public void actImprimer(ActionEvent actionEvent) {
		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("TaCompteServiceWebExterne", "actImprimer"));
		}
		try {

			//		FacesContext facesContext = FacesContext.getCurrentInstance();
			//		HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);

			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			sessionMap.put("tiers", taCompteServiceWebExterneService.findById(selectedTaCompteServiceWebExterneDTO.getId()));

			////////////////////////////////////////////////////////////////////////////////////////
			//Test génération de fichier PDF

			//		HashMap<String,Object> hm = new HashMap<>();
			//		hm.put( "tiers", taTaCompteServiceWebExterneService.findById(selectedTaCompteServiceWebExterneDTO.getId()));
			//		BirtUtil.setAppContextEdition(hm);
			//		
			//		BirtUtil.startReportEngine();
			//		
			////		BirtUtil.renderReportToFile(
			////				"/donnees/Projet/Java/Eclipse/GestionCommerciale_branche_2_0_13_JEE_E46/fr.legrain.bdg.webapp/src/main/webapp/reports/tiers/FicheTaCompteServiceWebExterne.rptdesign", 
			////				"/tmp/tiers.pdf", 
			////				new HashMap<>(), 
			////				BirtUtil.PDF_FORMAT);
			//		
			//		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("/reports/tiers/FicheTaCompteServiceWebExterne.rptdesign");
			//		BirtUtil.renderReportToFile(
			//				inputStream, 
			//				"/tmp/tiers.pdf", 
			//				new HashMap<>(), 
			//				BirtUtil.PDF_FORMAT);
			////////////////////////////////////////////////////////////////////////////////////////
			//java.net.URL.setURLStreamHandlerFactory();
			//		taTaCompteServiceWebExterneService.generePDF(selectedTaCompteServiceWebExterneDTO.getId());
			////////////////////////////////////////////////////////////////////////////////////////

			//			session.setAttribute("tiers", taTaCompteServiceWebExterneService.findById(selectedTaCompteServiceWebExterneDTO.getId()));
			System.out.println("TaCompteServiceWebExterneController.actImprimer()");
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
				context.addMessage(null, new FacesMessage("TaCompteServiceWebExterne", 
						"Chargement du tiers N°"+selectedTaCompteServiceWebExterneDTO.getCodeServiceWebExterne()
						)); }
		} else {
			tabViews.selectionneOngletCentre(LgrTab.TYPE_TAB_TIERS);
		}
	} 
	public void updateTab(){
		try {

			if(selectedTaCompteServiceWebExterneDTOs!=null && selectedTaCompteServiceWebExterneDTOs.length>0) {
				selectedTaCompteServiceWebExterneDTO = selectedTaCompteServiceWebExterneDTOs[0];
			}
			if(selectedTaCompteServiceWebExterneDTO.getId()!=null && selectedTaCompteServiceWebExterneDTO.getId()!=0) {
				taCompteServiceWebExterne = taCompteServiceWebExterneService.findById(selectedTaCompteServiceWebExterneDTO.getId());
			}
			taCompteServiceWebExterne = utilServiceWebExterne.decrypter(taCompteServiceWebExterne);
			mapperModelToUI.map(taCompteServiceWebExterne, selectedTaCompteServiceWebExterneDTO);	

			autoCompleteMapDTOtoUI();
			
			if(taCompteServiceWebExterne.getTaTAuthentification()!=null) {
				validateUIField(Const.C_CODE_T_AUTHENTIFICATION, taCompteServiceWebExterne.getTaTAuthentification().getCodeTAuthentification());
			}

		} catch (FinderException e) {
			e.printStackTrace();
		}
	}

	public void onTabShow() {
		System.out.println("ArticleController.onTabShow()");
	}

	public void onTabChange(TabChangeEvent event) {
		//		FacesMessage msg = null;
		//		if(event.getTab()!=null) {
		//			msg = new FacesMessage("Tab Changed ArticleController", "Active Tab: " + event.getTab().getTitle());
		//		} else {
		//			msg = new FacesMessage("Tab Changed ArticleController", "Active Tab: ");
		//		}
		//        FacesContext.getCurrentInstance().addMessage(null, msg);

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
		return modeEcran.dataSetEnModif() ||  selectedTaCompteServiceWebExterneDTO.getId()==0;
	}

	public void onRowSelect(SelectEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_COMPTE_SERVICE_WEB_EXTERNE);
		//b.setTitre("TaCompteServiceWebExterne "+selectedTaCompteServiceWebExterneDTO.getCodeServiceWebExterne());
		b.setTitre("Compte Service Web Externe");
		b.setTemplate("admin/compte_service_web_externe.xhtml");
		b.setIdTab(LgrTab.ID_TAB_COMPTE_SERVICE_WEB_EXTERNE);
		b.setStyle(LgrTab.CSS_CLASS_TAB_COMPTE_SERVICE_WEB_EXTERNE);
		b.setParamId(LibConversion.integerToString(selectedTaCompteServiceWebExterneDTO.getId()));

		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);


		updateTab();
		scrollToTop();

		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("TaCompteServiceWebExterne", 
					"Chargement du tiers N°"+selectedTaCompteServiceWebExterneDTO.getCodeServiceWebExterne()
					)); 
		}
	} 

	public void onRowSelectResponsive(SelectEvent event) {		
		autoCompleteMapDTOtoUI();

		try {
			taCompteServiceWebExterne = taCompteServiceWebExterneService.findById(selectedTaCompteServiceWebExterneDTO.getId());
			mapperModelToUI.map(taCompteServiceWebExterne, selectedTaCompteServiceWebExterneDTO);
		} catch (FinderException e) {
			e.printStackTrace();
		}

		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("TaCompteServiceWebExterne", 
					"Chargement du tiers N°"+selectedTaCompteServiceWebExterneDTO.getCodeServiceWebExterne()
					)); }
	} 

	public boolean editable() {
		return !modeEcran.dataSetEnModif();
	}

	public void actDialogTypeTaCompteServiceWebExterne(ActionEvent actionEvent) {

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

		PrimeFaces.current().dialog().openDynamic("tiers/dialog_type_tiers", options, params);

		//		FacesContext context = FacesContext.getCurrentInstance();  
		//		context.addMessage(null, new FacesMessage("TaCompteServiceWebExterne", "actAbout")); 	    
	}

	public void handleReturnDialogTypeTaCompteServiceWebExterne(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			taServiceWebExterne = (TaServiceWebExterne) event.getObject();
			try {
				taServiceWebExterneDTO = taServiceWebExterneService.findByCodeDTO(taServiceWebExterne.getCodeServiceWebExterne());
			} catch (FinderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void actDialogFamilleTaCompteServiceWebExterne(ActionEvent actionEvent) {

		Map<String,Object> options = new HashMap<String, Object>();
		options.put("modal", true);
		options.put("draggable", false);
		options.put("resizable", false);
		options.put("contentHeight", 640);
		options.put("modal", true);

		Map<String,List<String>> params = new HashMap<String,List<String>>();
		List<String> list = new ArrayList<String>();
		list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
		params.put("modeEcranDefaut", list);

		PrimeFaces.current().dialog().openDynamic("tiers/dialog_type_famille_tiers", options, params);

	}

	public void actDialogTypeCivilite(ActionEvent actionEvent) {

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

		PrimeFaces.current().dialog().openDynamic("tiers/dialog_type_civilite", options, params);

		//		FacesContext context = FacesContext.getCurrentInstance();  
		//		context.addMessage(null, new FacesMessage("TaCompteServiceWebExterne", "actAbout")); 	    
	}

	public void handleReturnDialogTypeCivilite(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			taTAuthentification = (TaTAuthentification) event.getObject();
			try {
				taTAuthentificationDTO = taTCivlicteService.findByCodeDTO(taTAuthentification.getCodeTAuthentification());
			} catch (FinderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void actDialogTypeEntite(ActionEvent actionEvent) {

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

		PrimeFaces.current().dialog().openDynamic("tiers/dialog_type_entite", options, params);

		//		FacesContext context = FacesContext.getCurrentInstance();  
		//		context.addMessage(null, new FacesMessage("TaCompteServiceWebExterne", "actAbout")); 	    
	}

	public void actDialogConditionPaiement(ActionEvent actionEvent) {

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

		PrimeFaces.current().dialog().openDynamic("tiers/dialog_condition_paiement_tiers", options, params);

		//		FacesContext context = FacesContext.getCurrentInstance();  
		//		context.addMessage(null, new FacesMessage("TaCompteServiceWebExterne", "actAbout")); 	    
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
				if(selectedTaCompteServiceWebExterneDTO!=null && selectedTaCompteServiceWebExterneDTO.getId()!=null  && selectedTaCompteServiceWebExterneDTO.getId()!=0 ) retour = false;
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

	public TaCompteServiceWebExterneDTO[] getSelectedTaCompteServiceWebExterneDTOs() {
		return selectedTaCompteServiceWebExterneDTOs;
	}

	public void setSelectedTaCompteServiceWebExterneDTOs(TaCompteServiceWebExterneDTO[] selectedTaCompteServiceWebExterneDTOs) {
		this.selectedTaCompteServiceWebExterneDTOs = selectedTaCompteServiceWebExterneDTOs;
	}

	public TaCompteServiceWebExterneDTO getNewTaCompteServiceWebExterneDTO() {
		return newTaCompteServiceWebExterneDTO;
	}

	public void setNewTaCompteServiceWebExterneDTO(TaCompteServiceWebExterneDTO newTaCompteServiceWebExterneDTO) {
		this.newTaCompteServiceWebExterneDTO = newTaCompteServiceWebExterneDTO;
	}

	public TaCompteServiceWebExterneDTO getSelectedTaCompteServiceWebExterneDTO() {
		return selectedTaCompteServiceWebExterneDTO;
	}

	public void setSelectedTaCompteServiceWebExterneDTO(TaCompteServiceWebExterneDTO selectedTaCompteServiceWebExterneDTO) {
		this.selectedTaCompteServiceWebExterneDTO = selectedTaCompteServiceWebExterneDTO;
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

	public void validateTaCompteServiceWebExterne(FacesContext context, UIComponent component, Object value) throws ValidatorException {

		String messageComplet = "";

		try {
			//String selectedRadio = (String) value;

			String nomChamp =  (String) component.getAttributes().get("nomChamp");

			//msg = "Mon message d'erreur pour : "+nomChamp;

			//		  validateUIField(nomChamp,value);
			//		  TaCompteServiceWebExterneDTO dtoTemp=new TaCompteServiceWebExterneDTO();
			//		  PropertyUtils.setSimpleProperty(dtoTemp, nomChamp, value);
			//		  taTaCompteServiceWebExterneService.validateDTOProperty(dtoTemp, nomChamp, ITaCompteServiceWebExterneServiceRemote.validationContext );

			//			//validation automatique via la JSR bean validation
			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
			Set<ConstraintViolation<TaCompteServiceWebExterneDTO>> violations = factory.getValidator().validateValue(TaCompteServiceWebExterneDTO.class,nomChamp,value);
			if (violations.size() > 0) {
				messageComplet = "Erreur de validation : ";
				//				List<IStatus> statusList = new ArrayList<IStatus>();
				for (ConstraintViolation<TaCompteServiceWebExterneDTO> cv : violations) {
					//					statusList.add(ValidationStatus.error(cv.getMessage()));
					messageComplet+=" "+cv.getMessage()+"\n";
				}
				throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, messageComplet, messageComplet));
				//				return new MultiStatus(LibrairiesEcranPlugin.PLUGIN_ID, IStatus.ERROR,
				//						//statusList.toArray(new IStatus[statusList.size()]), "Validation errors", null);
				//						statusList.toArray(new IStatus[statusList.size()]), messageComplet, null);
			} else {
				//aucune erreur de validation "automatique", on déclanche les validations du controller
				//				if(controller!=null) {
				validateUIField(nomChamp,value);
				//					if(!s.isOK()) {
				//						return s;
				//					}
				//				}
			}
			//			return ValidationStatus.ok();

			//selectedTaCompteServiceWebExterneDTO.setAdresse1Adresse("abcd");

			//		  if(selectedRadio.equals("aa")) {
			//			  throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
			//		  }

		} catch(Exception e) {
			//messageComplet += e.getMessage();
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, messageComplet, messageComplet));
		}
	}

	public void autcompleteSelection(SelectEvent event) {
		Object value = event.getObject();
		//		FacesMessage msg = new FacesMessage("Selected", "Item:" + value);

		String nomChamp =  (String) event.getComponent().getAttributes().get("nomChamp");

		if(value!=null) {
			if(value instanceof String && value.equals(Const.C_AUCUN))value="";	
			if(value instanceof TaServiceWebExterneDTO && ((TaServiceWebExterneDTO) value).getCodeTServiceWebExterne()!=null && ((TaServiceWebExterneDTO) value).getCodeTServiceWebExterne().equals(Const.C_AUCUN))value=null;	
			if(value instanceof TaTAuthentificationDTO && ((TaTAuthentificationDTO) value).getCodeTAuthentification()!=null && ((TaTAuthentificationDTO) value).getCodeTAuthentification().equals(Const.C_AUCUN))value=null;

		}

		validateUIField(nomChamp,value);
	}

	public boolean validateUIField(String nomChamp,Object value) {

		boolean changement=false;


		try {				
			if(nomChamp.equals(Const.C_CODE_SERVICE_WEB_EXTERNE)) {
				TaServiceWebExterne entity = null;
				if(value!=null){
					if(value instanceof TaServiceWebExterne){
						entity=(TaServiceWebExterne) value;
						//								entity = taTServiceWebExterneService.findByCode(entity.getCodeTServiceWebExterne());
					}else if(value instanceof TaServiceWebExterneDTO){
						entity = taServiceWebExterneService.findByCode(((TaServiceWebExterneDTO)value).getCodeServiceWebExterne());
					}else{
						entity = taServiceWebExterneService.findByCode((String)value);
					}
				}else{
					selectedTaCompteServiceWebExterneDTO.setCodeServiceWebExterne("");
					taServiceWebExterneDTO.setCodeServiceWebExterne("");
					taServiceWebExterne=null;
				}						
				taCompteServiceWebExterne.setTaServiceWebExterne(entity);
				
				taCompteServiceWebExterne.setUrlService(entity.getUrlService());
				selectedTaCompteServiceWebExterneDTO.setUrlService(entity.getUrlService());
				
				taCompteServiceWebExterne.setCodeCompteServiceWebExterne(entity.getCodeServiceWebExterne());
				selectedTaCompteServiceWebExterneDTO.setCodeCompteServiceWebExterne(entity.getCodeServiceWebExterne());
				
				taCompteServiceWebExterne.setLibelleCompteServiceWebExterne(entity.getLibelleServiceWebExterne());
				selectedTaCompteServiceWebExterneDTO.setLibelleCompteServiceWebExterne(entity.getLibelleServiceWebExterne());
				
				taCompteServiceWebExterne.setDescriptionCompteServiceWebExterne(entity.getDescriptionServiceWebExterne());
				selectedTaCompteServiceWebExterneDTO.setDescriptionCompteServiceWebExterne(entity.getDescriptionServiceWebExterne());
				
				if(entity.getTaTAuthentification()!=null) {
					validateUIField(Const.C_CODE_T_AUTHENTIFICATION, entity.getTaTAuthentification().getCodeTAuthentification());
				}

			}
			else if(nomChamp.equals(Const.C_CODE_T_AUTHENTIFICATION)) {

				TaTAuthentification entity =null;
				if(value!=null){
					if(value instanceof TaTAuthentification){
						entity=(TaTAuthentification) value;
						//							entity = taTCivlicteService.findByCode(entity.getCodeTCivilite());
					}else if(value instanceof TaTAuthentificationDTO){
						entity = taTCivlicteService.findByCode(((TaTAuthentificationDTO)value).getCodeTAuthentification());
					}else{
						entity = taTCivlicteService.findByCode((String)value);								
					}
				}else{
					selectedTaCompteServiceWebExterneDTO.setCodeTAuthentification(null);
					taTAuthentificationDTO.setCodeTAuthentification(null);
					taTAuthentification=null;
				}

				taCompteServiceWebExterne.setTaTAuthentification(entity);
				if( !(value instanceof TaTAuthentificationDTO)){
					taTAuthentificationDTO = taTCivlicteService.findByCodeDTO(entity.getCodeTAuthentification());
				}

				authenticationOAuth = false;
				if(entity!=null && entity.getCodeTAuthentification()!=null
						&& entity.getCodeTAuthentification().equals(TaTAuthentification.OAUTH)) {
					authenticationOAuth = true;
				}

			}

			return false;

		} catch (Exception e) {

		}
		return false;
	}

	public List<TaTAuthentification> typeAuthentificationAutoComplete(String query) {
		List<TaTAuthentification> allValues = taTCivlicteService.selectAll();
		List<TaTAuthentification> filteredValues = new ArrayList<TaTAuthentification>();
		TaTAuthentification civ = new TaTAuthentification();
		civ.setCodeTAuthentification(Const.C_AUCUN);
		filteredValues.add(civ);
		for (int i = 0; i < allValues.size(); i++) {
			civ = allValues.get(i);
			if(query.equals("*") || civ.getCodeTAuthentification().toLowerCase().contains(query.toLowerCase())) {
				filteredValues.add(civ);
			}
		}

		return filteredValues;
	}

	public List<TaTAuthentificationDTO> typeAuthentificationAutoCompleteLight(String query) {
		List<TaTAuthentificationDTO> allValues = taTCivlicteService.selectAllDTO();
		List<TaTAuthentificationDTO> filteredValues = new ArrayList<TaTAuthentificationDTO>();
		TaTAuthentificationDTO civ = new TaTAuthentificationDTO();
		civ.setCodeTAuthentification(Const.C_AUCUN);
		filteredValues.add(civ);
		for (int i = 0; i < allValues.size(); i++) {
			civ = allValues.get(i);
			if(query.equals("*") || civ.getCodeTAuthentification().toLowerCase().contains(query.toLowerCase())) {
				filteredValues.add(civ);
			}
		}

		return filteredValues;
	}
	public List<TaServiceWebExterne> typeTaCompteServiceWebExterneAutoComplete(String query) {
		List<TaServiceWebExterne> allValues = taServiceWebExterneService.selectAll();
		List<TaServiceWebExterne> filteredValues = new ArrayList<TaServiceWebExterne>();

		for (int i = 0; i < allValues.size(); i++) {
			TaServiceWebExterne civ = allValues.get(i);
			if(query.equals("*") || civ.getCodeServiceWebExterne().toLowerCase().contains(query.toLowerCase())) {
				filteredValues.add(civ);
			}
		}

		return filteredValues;
	}
	public List<TaServiceWebExterneDTO> typeTaCompteServiceWebExterneAutoCompleteLight(String query) {
		List<TaServiceWebExterneDTO> allValues = taServiceWebExterneService.findAllLightActif();
		List<TaServiceWebExterneDTO> filteredValues = new ArrayList<TaServiceWebExterneDTO>();

		for (int i = 0; i < allValues.size(); i++) {
			TaServiceWebExterneDTO civ = allValues.get(i);
			boolean moduleAutorise = true;
			if(civ.getCodeTServiceWebExterne().equals("STRIPE")) {
				moduleAutorise = autorisationBean.autoriseMenu(AutorisationBean.ID_MODULE_CONNEXION_DOSSIER_STRIPE);
			}
			if(moduleAutorise && query.equals("*") || civ.getCodeTServiceWebExterne().toLowerCase().contains(query.toLowerCase())) {
				filteredValues.add(civ);
			}
		}
		

		return filteredValues;
	}

	public TaServiceWebExterne getTaServiceWebExterne() {
		return taServiceWebExterne;
	}

	public void setTaServiceWebExterne(TaServiceWebExterne taServiceWebExterne) {
		this.taServiceWebExterne = taServiceWebExterne;
	}

	public TaTAuthentification getTaTAuthentification() {
		return taTAuthentification;
	}

	public void setTaTAuthentification(TaTAuthentification taTCivilite) {
		this.taTAuthentification = taTCivilite;
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

	public TaCompteServiceWebExterne getTaCompteServiceWebExterne() {
		return taCompteServiceWebExterne;
	}

	public void setTaCompteServiceWebExterne(TaCompteServiceWebExterne taTaCompteServiceWebExterne) {
		this.taCompteServiceWebExterne = taTaCompteServiceWebExterne;
	}

	public TaCompteServiceWebExterneDTO rempliDTO(){
		if(taServiceWebExterne!=null){			
			try {
				taCompteServiceWebExterne=taCompteServiceWebExterneService.findByCode(taServiceWebExterne.getCodeServiceWebExterne());
				mapperModelToUI.map(taCompteServiceWebExterne, selectedTaCompteServiceWebExterneDTO);
				if(values.contains(selectedTaCompteServiceWebExterneDTO))values.add(selectedTaCompteServiceWebExterneDTO);
			} catch (FinderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return selectedTaCompteServiceWebExterneDTO;
	}

	public StreamedContent getLogo() {
		return logo;
	}

	public void setLogo(StreamedContent logo) {
		this.logo = logo;
	}

	public TaServiceWebExterneDTO getTaServiceWebExterneDTO() {
		return taServiceWebExterneDTO;
	}

	public void setTaServiceWebExterneDTO(TaServiceWebExterneDTO taServiceWebExterneDTO) {
		this.taServiceWebExterneDTO = taServiceWebExterneDTO;
	}

	public boolean isAuthenticationOAuth() {
		return authenticationOAuth;
	}

	public void setAuthenticationOAuth(boolean authenticationOAuth) {
		this.authenticationOAuth = authenticationOAuth;
	}

	public GestionOAuthController getGestionOAuthController() {
		return gestionOAuthController;
	}

	public void setGestionOAuthController(GestionOAuthController gestionOAuthController) {
		this.gestionOAuthController = gestionOAuthController;
	}

}  
