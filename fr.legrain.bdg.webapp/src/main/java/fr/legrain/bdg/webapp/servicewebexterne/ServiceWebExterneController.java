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
import fr.legrain.bdg.servicewebexterne.service.remote.ITaServiceWebExterneServiceRemote;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaTAuthentificationServiceRemote;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaTServiceWebExterneServiceRemote;
import fr.legrain.bdg.tache.service.remote.ITaEvenementServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTTvaDocServiceRemote;
import fr.legrain.bdg.webapp.ConstWeb;
import fr.legrain.bdg.webapp.app.AbstractController;
import fr.legrain.bdg.webapp.app.LgrTab;
import fr.legrain.bdg.webapp.app.TabListModelBean;
import fr.legrain.bdg.webapp.app.TabViewsBean;
import fr.legrain.droits.service.SessionInfo;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.ModeObjetEcranServeur;
import fr.legrain.servicewebexterne.dto.TaServiceWebExterneDTO;
import fr.legrain.servicewebexterne.dto.TaTAuthentificationDTO;
import fr.legrain.servicewebexterne.dto.TaTServiceWebExterneDTO;
import fr.legrain.servicewebexterne.model.TaServiceWebExterne;
import fr.legrain.servicewebexterne.model.TaTAuthentification;
import fr.legrain.servicewebexterne.model.TaTServiceWebExterne;

@Named
@ViewScoped  
public class ServiceWebExterneController extends AbstractController implements Serializable {  

	@Inject @Named(value="tabListModelCenterBean")
	private TabListModelBean tabsCenter;

	@Inject @Named(value="tabViewsBean")
	private TabViewsBean tabViews;

	private String paramId;

	private TabView tabViewTaServiceWebExterne; //Le Binding sur les onglets "secondaire" semble ne pas fonctionné, les onglets ne sont pas générés au bon moment avec le c:forEach

	private List<TaServiceWebExterneDTO> values; 

	private StreamedContent logo;

	private @Inject SessionInfo sessionInfo;

	protected ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();

	private @EJB ITaServiceWebExterneServiceRemote taTaServiceWebExterneService;
	private @EJB ITaTServiceWebExterneServiceRemote taTServiceWebExterneService;

	private @EJB ITaInfoEntrepriseServiceRemote taInfoEntrepriseService;

	private @EJB ITaTAuthentificationServiceRemote taTCivlicteService;
	private @EJB ITaTTvaDocServiceRemote taTTvaDocService;


	private @EJB ITaEvenementServiceRemote taEvenementService;
	private @EJB ITaUtilisateurServiceRemote taUtilisateurService;

	private TaServiceWebExterneDTO[] selectedTaServiceWebExterneDTOs; 
	private TaServiceWebExterne taServiceWebExterne = new TaServiceWebExterne();
	private TaServiceWebExterneDTO newTaServiceWebExterneDTO = new TaServiceWebExterneDTO();
	private TaServiceWebExterneDTO selectedTaServiceWebExterneDTO = new TaServiceWebExterneDTO();

	private TaTServiceWebExterne taTServiceWebExterne;
	private TaTAuthentification taTAuthentification;


	private LgrDozerMapper<TaServiceWebExterneDTO,TaServiceWebExterne> mapperUIToModel  = new LgrDozerMapper<TaServiceWebExterneDTO,TaServiceWebExterne>();
	private LgrDozerMapper<TaServiceWebExterne,TaServiceWebExterneDTO> mapperModelToUI  = new LgrDozerMapper<TaServiceWebExterne,TaServiceWebExterneDTO>();

	// Dima - Début
	private TaTServiceWebExterneDTO taTServiceWebExterneDTO;
	private TaTAuthentificationDTO taTAuthentificationDTO;

	public TaTAuthentificationDTO getTaTAuthentificationDTO() {
		return taTAuthentificationDTO;
	}

	public void setTaTAuthentificationDTO(TaTAuthentificationDTO taTCiviliteDTO) {
		this.taTAuthentificationDTO = taTCiviliteDTO;
	}
	// Dima -  Fin

	public ServiceWebExterneController() {  
		//values = getValues();
	}  

	@PostConstruct
	public void postConstruct(){

		refresh();

		//		FaceletContext faceletContext = (FaceletContext) FacesContext.getCurrentInstance().getAttributes().get(FaceletContext.FACELET_CONTEXT_KEY);
		//		String formId = (String) faceletContext.getAttribute("paramId");
		//		if(formId!=null && !formId.equals("")) {
		//			try {
		//				selectedTaServiceWebExterneDTO = TaServiceWebExterneDTOService.findById(LibConversion.stringToInteger(paramId));
		//			} catch (FinderException e) {
		//				// TODO Auto-generated catch block
		//				e.printStackTrace();
		//			}
		//		}
	}



	public void refreshResponsive(){		
		if(paramId!=null) {
			try {
				if(paramId.equals("-1")){
					actInserer(null);
				}else{
					taServiceWebExterne = taTaServiceWebExterneService.findById(LibConversion.stringToInteger(paramId));
					mapperModelToUI.map(taServiceWebExterne, selectedTaServiceWebExterneDTO);
					autoCompleteMapDTOtoUI();
					modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
				}
				paramId=null;
			} catch (FinderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("TaServiceWebExterneController.refresh() "+taServiceWebExterne.getCodeServiceWebExterne());
		}
	}

	public void refresh(){
		//values = taTaServiceWebExterneService.findAllLight();
		values = taTaServiceWebExterneService.findByCodeLight(null);
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}

	public List<TaServiceWebExterneDTO> getValues(){  
		return values;
	}

	public void actAnnuler(ActionEvent actionEvent) {
		try {
			switch (modeEcran.getMode()) {
			case C_MO_INSERTION:
				//				if(selectedTaServiceWebExterneDTO.getCodeServiceWebExterne()!=null) {
				//					taTaServiceWebExterneService.annuleCode(selectedTaServiceWebExterneDTO.getCodeServiceWebExterne());
				//				}
				taServiceWebExterne = new TaServiceWebExterne();
				mapperModelToUI.map(taServiceWebExterne,selectedTaServiceWebExterneDTO );
				selectedTaServiceWebExterneDTO=new TaServiceWebExterneDTO();

				if(!values.isEmpty()) selectedTaServiceWebExterneDTO = values.get(0);
				onRowSelect(null);
				break;
			case C_MO_EDITION:
				if(selectedTaServiceWebExterneDTO.getId()!=null && selectedTaServiceWebExterneDTO.getId()!=0){
					taServiceWebExterne = taTaServiceWebExterneService.findById(selectedTaServiceWebExterneDTO.getId());
					selectedTaServiceWebExterneDTO=taTaServiceWebExterneService.findByIdDTO(selectedTaServiceWebExterneDTO.getId());
				}				
				break;

			default:
				break;
			}
			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
			mapperModelToUI.map(taServiceWebExterne, selectedTaServiceWebExterneDTO);
			refreshLogo();

			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("TaServiceWebExterne", "actAnnuler"));
			}
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	public void autoCompleteMapUIToDTO() {
		if(taTServiceWebExterneDTO!=null) {
			validateUIField(Const.C_CODE_T_SERVICE_WEB_EXTERNE,taTServiceWebExterneDTO.getCodeTServiceWebExterne());
			selectedTaServiceWebExterneDTO.setCodeTServiceWebExterne(taTServiceWebExterneDTO.getCodeTServiceWebExterne());
		}else selectedTaServiceWebExterneDTO.setCodeTServiceWebExterne(null);
		if(taTAuthentificationDTO!=null) {
			validateUIField(Const.C_CODE_T_AUTHENTIFICATION,taTAuthentificationDTO.getCodeTAuthentification());
			selectedTaServiceWebExterneDTO.setCodeTAuthentification(taTAuthentificationDTO.getCodeTAuthentification());
		}else selectedTaServiceWebExterneDTO.setCodeTAuthentification(null);

	}

	public void autoCompleteMapDTOtoUI() {
		try {
			taTServiceWebExterne = null;
			taTAuthentification = null;

			taTServiceWebExterneDTO = null;
			taTAuthentificationDTO = null;
			taTServiceWebExterneDTO = null;

			if(selectedTaServiceWebExterneDTO.getCodeTServiceWebExterne()!=null && !selectedTaServiceWebExterneDTO.getCodeTServiceWebExterne().equals("")) {
				taTServiceWebExterne = taTServiceWebExterneService.findByCode(selectedTaServiceWebExterneDTO.getCodeTServiceWebExterne());
				taTServiceWebExterneDTO=taTServiceWebExterneService.findByCodeDTO(selectedTaServiceWebExterneDTO.getCodeTServiceWebExterne());
			}
			if(selectedTaServiceWebExterneDTO.getCodeTAuthentification()!=null && !selectedTaServiceWebExterneDTO.getCodeTAuthentification().equals("")) {
				taTAuthentification = taTCivlicteService.findByCode(selectedTaServiceWebExterneDTO.getCodeTAuthentification());
				taTAuthentificationDTO = taTCivlicteService.findByCodeDTO(selectedTaServiceWebExterneDTO.getCodeTAuthentification());
			}

		} catch (FinderException e) {
			e.printStackTrace();
		}
	}

	public void actEnregistrer(ActionEvent actionEvent) {		

		mapperUIToModel.map(selectedTaServiceWebExterneDTO, taServiceWebExterne);

		try {
			taServiceWebExterne = taTaServiceWebExterneService.merge(taServiceWebExterne,ITaServiceWebExterneServiceRemote.validationContext);

			//			if(selectedTaServiceWebExterneDTO.getCodeServiceWebExterne()!=null) {
			//				taTaServiceWebExterneService.annuleCode(selectedTaServiceWebExterneDTO.getCodeServiceWebExterne());
			//			}

			mapperModelToUI.map(taServiceWebExterne, selectedTaServiceWebExterneDTO);
			autoCompleteMapDTOtoUI();

			if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_INSERTION)==0) {
				values.add(selectedTaServiceWebExterneDTO);
			}

			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

		} catch(Exception e) {
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			//context.addMessage(null, new FacesMessage("TaServiceWebExterne", "actEnregistrer")); 
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "TaServiceWebExterne", e.getMessage()));
		}

		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("TaServiceWebExterne", "actEnregistrer"));
		}
	}

	public void actInserer(ActionEvent actionEvent) {
		try {
			selectedTaServiceWebExterneDTOs= new TaServiceWebExterneDTO[]{};
			selectedTaServiceWebExterneDTO = new TaServiceWebExterneDTO();
			taServiceWebExterne = new TaServiceWebExterne();

			//			String codeTaServiceWebExterneDefaut = "C";
			String codeTaServiceWebExterneDefaut = "";

			//			taGenCodeExService.libereVerrouTout(new ArrayList<String>(SessionListener.getSessions().keySet()));
			//			selectedTaServiceWebExterneDTO.setCodeTaServiceWebExterne(taTaServiceWebExterneService.genereCode(null)); //ejb
			//			validateUIField(Const.C_CODE_TIERS,selectedTaServiceWebExterneDTO.getCodeServiceWebExterne());//permet de verrouiller le code genere

			selectedTaServiceWebExterneDTO.setCodeTServiceWebExterne(codeTaServiceWebExterneDefaut);
			
			selectedTaServiceWebExterneDTO.setSysteme(true);
			taServiceWebExterne.setSysteme(true);
			
			selectedTaServiceWebExterneDTO.setActif(true);
			taServiceWebExterne.setActif(true);

			//			selectedTaServiceWebExterneDTO.setCodeCompta(selectedTaServiceWebExterneDTO.getCodeServiceWebExterne()); //ejb
			//			validateUIField(Const.C_CODE_COMPTA,selectedTaServiceWebExterneDTO.getCodeCompta()); //ejb
			//			selectedTaServiceWebExterneDTO.setCompte("411"); //ejb
			//			validateUIField(Const.C_COMPTE,selectedTaServiceWebExterneDTO.getCompte()); //ejb

			//TaTServiceWebExterneDAO daoTTaServiceWebExterne = new TaTServiceWebExterneDAO(getEm());
			TaTServiceWebExterne taTServiceWebExterne;

			taTServiceWebExterne = taTServiceWebExterneService.findByCode(codeTaServiceWebExterneDefaut);

			//			if(taTServiceWebExterne!=null && taTServiceWebExterne.getCompteTTaServiceWebExterne()!=null) {
			//				selectedTaServiceWebExterneDTO.setCompte(taTServiceWebExterne.getCompteTTaServiceWebExterne());
			//				this.taTServiceWebExterne = taTServiceWebExterne;
			//			} else {
			//				//selectedTaServiceWebExterneDTO.setCompte(TaServiceWebExternePlugin.getDefault().getPreferenceStore().
			//				//getString(PreferenceConstants.TIERS_COMPTE_COMPTALE_DEFAUT));
			//			}
			//			selectedTaServiceWebExterneDTO.setActifTaServiceWebExterne(true);
			//			//swtTaServiceWebExterne.setTtcTaServiceWebExterne(TaServiceWebExternePlugin.getDefault().getPreferenceStore().
			//			//getBoolean(PreferenceConstants.TIERS_SAISIE_TTC_DEFAUT));
			//			selectedTaServiceWebExterneDTO.setCodeTTvaDoc("F");

			autoCompleteMapDTOtoUI();

			modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
			scrollToTop();
			if(tabViewTaServiceWebExterne!=null) {
				tabViewTaServiceWebExterne.setActiveIndex(0);
				updateTab();
				//				adresseController.setMasterEntity(taTaServiceWebExterne);
				//				adresseController.refresh(null);
			}
			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("TaServiceWebExterne", "actInserer"));
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
		refreshLogo();

		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("TaServiceWebExterne", "actModifier"));
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
			context.addMessage(null, new FacesMessage("TaServiceWebExterne", "actAide"));
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
			refreshLogo();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void refreshLogo() {
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

	public void supprimerLogo(ActionEvent e) {
		taServiceWebExterne.setLogo(null);
		actEnregistrer(null);
		refreshLogo();
	}

	public void nouveau(ActionEvent actionEvent) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_SERVICE_WEB_EXTERNE);
		b.setTitre("Service Web Externe");
		b.setTemplate("admin/service_web_externe.xhtml");
		b.setIdTab(LgrTab.ID_TAB_SERVICE_WEB_EXTERNE);
		b.setStyle(LgrTab.CSS_CLASS_TAB_SERVICE_WEB_EXTERNE);
		b.setParamId("0");

		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
		actInserer(actionEvent);

		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("TaServiceWebExterne", 
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
		//selectedTaServiceWebExterneDTO = (TaServiceWebExterneDTO) actionEvent.getComponent().getAttributes().get("selectionTaServiceWebExterne");
		//    	onRowSelectResponsive(null);
		if(selectedTaServiceWebExterneDTO!=null)   	return "/m/tiers.xhtml?idTaServiceWebExterne="+selectedTaServiceWebExterneDTO.getId()+"&faces-redirect=true";
		else return "";
	}


	public void repondreMessage(ActionEvent event) {
		Integer idMessage = (Integer) event.getComponent().getAttributes().get("idTaServiceWebExterne");

		ExternalContext context2 = FacesContext.getCurrentInstance().getExternalContext();
		try {
			context2.redirect(context2.getRequestContextPath() + "/m/tiers.xhtml?idTaServiceWebExterne="+idMessage);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void actSupprimer() {
		actSupprimer(null);
	}

	public void actSupprimer(ActionEvent actionEvent) {
		TaServiceWebExterne taTaServiceWebExterne = new TaServiceWebExterne();
		try {
			if(selectedTaServiceWebExterneDTO!=null && selectedTaServiceWebExterneDTO.getId()!=null){
				taTaServiceWebExterne = taTaServiceWebExterneService.findById(selectedTaServiceWebExterneDTO.getId());
			}

			taTaServiceWebExterneService.remove(taTaServiceWebExterne);
			values.remove(selectedTaServiceWebExterneDTO);

			if(!values.isEmpty()) {
				selectedTaServiceWebExterneDTO = values.get(0);
			}else {
				selectedTaServiceWebExterneDTO=new TaServiceWebExterneDTO();
			}
			updateTab();
			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("TaServiceWebExterne", "actSupprimer"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "TaServiceWebExterne", e.getCause().getCause().getCause().getCause().getMessage()));
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
		selectedTaServiceWebExterneDTO=new TaServiceWebExterneDTO();
		selectedTaServiceWebExterneDTOs=new TaServiceWebExterneDTO[]{selectedTaServiceWebExterneDTO};

		//		//gestion du filtre de la liste
		//        RequestContext requestContext = RequestContext.getCurrentInstance();
		//        requestContext.execute("PF('wvDataTableListeTaServiceWebExterne').filter()");


		//		LgrTab typeOngletDejaOuvert = null;
		//		for (LgrTab o : tabsCenter.getOnglets()) {
		//			if(LgrTab.TYPE_TAB_TIERS.equals(o.getTypeOnglet())) {
		//				typeOngletDejaOuvert = o;
		//			}
		//		}
		//		
		//		if(typeOngletDejaOuvert!=null) {
		////			if(!values.isEmpty()) {
		////				selectedTaServiceWebExterneDTO = values.get(0);
		////			}
		//			tabsCenter.supprimerOnglet(typeOngletDejaOuvert);
		//		}
		//		if(ConstWeb.DEBUG) {
		//		FacesContext context = FacesContext.getCurrentInstance();  
		//		context.addMessage(null, new FacesMessage("TaServiceWebExterne", "actFermer"));
		//		}
	}

	public void actImprimer(ActionEvent actionEvent) {
		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("TaServiceWebExterne", "actImprimer"));
		}
		try {

			//		FacesContext facesContext = FacesContext.getCurrentInstance();
			//		HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);

			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			sessionMap.put("tiers", taTaServiceWebExterneService.findById(selectedTaServiceWebExterneDTO.getId()));

			////////////////////////////////////////////////////////////////////////////////////////
			//Test génération de fichier PDF

			//		HashMap<String,Object> hm = new HashMap<>();
			//		hm.put( "tiers", taTaServiceWebExterneService.findById(selectedTaServiceWebExterneDTO.getId()));
			//		BirtUtil.setAppContextEdition(hm);
			//		
			//		BirtUtil.startReportEngine();
			//		
			////		BirtUtil.renderReportToFile(
			////				"/donnees/Projet/Java/Eclipse/GestionCommerciale_branche_2_0_13_JEE_E46/fr.legrain.bdg.webapp/src/main/webapp/reports/tiers/FicheTaServiceWebExterne.rptdesign", 
			////				"/tmp/tiers.pdf", 
			////				new HashMap<>(), 
			////				BirtUtil.PDF_FORMAT);
			//		
			//		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("/reports/tiers/FicheTaServiceWebExterne.rptdesign");
			//		BirtUtil.renderReportToFile(
			//				inputStream, 
			//				"/tmp/tiers.pdf", 
			//				new HashMap<>(), 
			//				BirtUtil.PDF_FORMAT);
			////////////////////////////////////////////////////////////////////////////////////////
			//java.net.URL.setURLStreamHandlerFactory();
			//		taTaServiceWebExterneService.generePDF(selectedTaServiceWebExterneDTO.getId());
			////////////////////////////////////////////////////////////////////////////////////////

			//			session.setAttribute("tiers", taTaServiceWebExterneService.findById(selectedTaServiceWebExterneDTO.getId()));
			System.out.println("TaServiceWebExterneController.actImprimer()");
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
				context.addMessage(null, new FacesMessage("TaServiceWebExterne", 
						"Chargement du tiers N°"+selectedTaServiceWebExterneDTO.getCodeServiceWebExterne()
						)); }
		} else {
			tabViews.selectionneOngletCentre(LgrTab.TYPE_TAB_TIERS);
		}
	} 
	public void updateTab(){
		try {

			if(selectedTaServiceWebExterneDTOs!=null && selectedTaServiceWebExterneDTOs.length>0) {
				selectedTaServiceWebExterneDTO = selectedTaServiceWebExterneDTOs[0];
			}
			if(selectedTaServiceWebExterneDTO.getId()!=null && selectedTaServiceWebExterneDTO.getId()!=0) {
				taServiceWebExterne = taTaServiceWebExterneService.findById(selectedTaServiceWebExterneDTO.getId());
			}
			mapperModelToUI.map(taServiceWebExterne, selectedTaServiceWebExterneDTO);	

			autoCompleteMapDTOtoUI();
			refreshLogo();

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
		return modeEcran.dataSetEnModif() ||  selectedTaServiceWebExterneDTO.getId()==0;
	}

	public void onRowSelect(SelectEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_SERVICE_WEB_EXTERNE);
		//b.setTitre("TaServiceWebExterne "+selectedTaServiceWebExterneDTO.getCodeServiceWebExterne());
		b.setTitre("Service Web Externe");
		b.setTemplate("admin/service_web_externe.xhtml");
		b.setIdTab(LgrTab.ID_TAB_SERVICE_WEB_EXTERNE);
		b.setStyle(LgrTab.CSS_CLASS_TAB_SERVICE_WEB_EXTERNE);
		b.setParamId(LibConversion.integerToString(selectedTaServiceWebExterneDTO.getId()));

		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);


		updateTab();
		scrollToTop();

		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("TaServiceWebExterne", 
					"Chargement du tiers N°"+selectedTaServiceWebExterneDTO.getCodeServiceWebExterne()
					)); 
		}
	} 

	public void onRowSelectResponsive(SelectEvent event) {		
		autoCompleteMapDTOtoUI();

		try {
			taServiceWebExterne = taTaServiceWebExterneService.findById(selectedTaServiceWebExterneDTO.getId());
			mapperModelToUI.map(taServiceWebExterne, selectedTaServiceWebExterneDTO);
		} catch (FinderException e) {
			e.printStackTrace();
		}

		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("TaServiceWebExterne", 
					"Chargement du tiers N°"+selectedTaServiceWebExterneDTO.getCodeServiceWebExterne()
					)); }
	} 

	public boolean editable() {
		return !modeEcran.dataSetEnModif();
	}

	public void actDialogTypeTaServiceWebExterne(ActionEvent actionEvent) {

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
		//		context.addMessage(null, new FacesMessage("TaServiceWebExterne", "actAbout")); 	    
	}

	public void handleReturnDialogTypeTaServiceWebExterne(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			taTServiceWebExterne = (TaTServiceWebExterne) event.getObject();
			try {
				taTServiceWebExterneDTO = taTServiceWebExterneService.findByCodeDTO(taTServiceWebExterne.getCodeTServiceWebExterne());
			} catch (FinderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void actDialogFamilleTaServiceWebExterne(ActionEvent actionEvent) {

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
		//		context.addMessage(null, new FacesMessage("TaServiceWebExterne", "actAbout")); 	    
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
		//		context.addMessage(null, new FacesMessage("TaServiceWebExterne", "actAbout")); 	    
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
		//		context.addMessage(null, new FacesMessage("TaServiceWebExterne", "actAbout")); 	    
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
				if(selectedTaServiceWebExterneDTO!=null && selectedTaServiceWebExterneDTO.getId()!=null  && selectedTaServiceWebExterneDTO.getId()!=0 ) retour = false;
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

	public TaServiceWebExterneDTO[] getSelectedTaServiceWebExterneDTOs() {
		return selectedTaServiceWebExterneDTOs;
	}

	public void setSelectedTaServiceWebExterneDTOs(TaServiceWebExterneDTO[] selectedTaServiceWebExterneDTOs) {
		this.selectedTaServiceWebExterneDTOs = selectedTaServiceWebExterneDTOs;
	}

	public TaServiceWebExterneDTO getNewTaServiceWebExterneDTO() {
		return newTaServiceWebExterneDTO;
	}

	public void setNewTaServiceWebExterneDTO(TaServiceWebExterneDTO newTaServiceWebExterneDTO) {
		this.newTaServiceWebExterneDTO = newTaServiceWebExterneDTO;
	}

	public TaServiceWebExterneDTO getSelectedTaServiceWebExterneDTO() {
		return selectedTaServiceWebExterneDTO;
	}

	public void setSelectedTaServiceWebExterneDTO(TaServiceWebExterneDTO selectedTaServiceWebExterneDTO) {
		this.selectedTaServiceWebExterneDTO = selectedTaServiceWebExterneDTO;
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

	public void validateTaServiceWebExterne(FacesContext context, UIComponent component, Object value) throws ValidatorException {

		String messageComplet = "";

		try {
			//String selectedRadio = (String) value;

			String nomChamp =  (String) component.getAttributes().get("nomChamp");

			//msg = "Mon message d'erreur pour : "+nomChamp;

			//		  validateUIField(nomChamp,value);
			//		  TaServiceWebExterneDTO dtoTemp=new TaServiceWebExterneDTO();
			//		  PropertyUtils.setSimpleProperty(dtoTemp, nomChamp, value);
			//		  taTaServiceWebExterneService.validateDTOProperty(dtoTemp, nomChamp, ITaServiceWebExterneServiceRemote.validationContext );

			//			//validation automatique via la JSR bean validation
			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
			Set<ConstraintViolation<TaServiceWebExterneDTO>> violations = factory.getValidator().validateValue(TaServiceWebExterneDTO.class,nomChamp,value);
			if (violations.size() > 0) {
				messageComplet = "Erreur de validation : ";
				//				List<IStatus> statusList = new ArrayList<IStatus>();
				for (ConstraintViolation<TaServiceWebExterneDTO> cv : violations) {
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

			//selectedTaServiceWebExterneDTO.setAdresse1Adresse("abcd");

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
			if(value instanceof TaTServiceWebExterneDTO && ((TaTServiceWebExterneDTO) value).getCodeTServiceWebExterne()!=null && ((TaTServiceWebExterneDTO) value).getCodeTServiceWebExterne().equals(Const.C_AUCUN))value=null;	
			if(value instanceof TaTAuthentificationDTO && ((TaTAuthentificationDTO) value).getCodeTAuthentification()!=null && ((TaTAuthentificationDTO) value).getCodeTAuthentification().equals(Const.C_AUCUN))value=null;

		}

		validateUIField(nomChamp,value);
	}

	public boolean validateUIField(String nomChamp,Object value) {

		boolean changement=false;


		try {				
			if(nomChamp.equals(Const.C_CODE_T_SERVICE_WEB_EXTERNE)) {
				TaTServiceWebExterne entity = null;
				if(value!=null){
					if(value instanceof TaTServiceWebExterne){
						entity=(TaTServiceWebExterne) value;
						//								entity = taTServiceWebExterneService.findByCode(entity.getCodeTServiceWebExterne());
					}else if(value instanceof TaTServiceWebExterneDTO){
						entity = taTServiceWebExterneService.findByCode(((TaTServiceWebExterneDTO)value).getCodeTServiceWebExterne());
					}else{
						entity = taTServiceWebExterneService.findByCode((String)value);
					}
				}else{
					selectedTaServiceWebExterneDTO.setCodeTServiceWebExterne("");
					taTServiceWebExterneDTO.setCodeTServiceWebExterne("");
					taTServiceWebExterne=null;
				}						
				taServiceWebExterne.setTaTServiceWebExterne(entity);

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
					selectedTaServiceWebExterneDTO.setCodeTAuthentification(null);
					taTAuthentificationDTO.setCodeTAuthentification(null);
					taTAuthentification=null;
				}

				taServiceWebExterne.setTaTAuthentification(entity);


			}else  if(nomChamp.equals(Const.C_CODE_T_TVA_DOC)) {

//				TaTTvaDoc entity = null;
//				if(value!=null){
//					if(value instanceof TaTTvaDoc){
//						entity=(TaTTvaDoc) value;
//						//							entity = taTTvaDocService.findByCode(entity.getCodeTTvaDoc());
//					}else{
//						entity = taTTvaDocService.findByCode((String)value);
//					}
//				}else{
//					selectedTaServiceWebExterneDTO.setCodeTTvaDoc(null);
//				}
//
//				taServiceWebExterne.setTaTTvaDoc(entity);

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
	public List<TaTServiceWebExterne> typeTaServiceWebExterneAutoComplete(String query) {
		List<TaTServiceWebExterne> allValues = taTServiceWebExterneService.selectAll();
		List<TaTServiceWebExterne> filteredValues = new ArrayList<TaTServiceWebExterne>();

		for (int i = 0; i < allValues.size(); i++) {
			TaTServiceWebExterne civ = allValues.get(i);
			if(query.equals("*") || civ.getCodeTServiceWebExterne().toLowerCase().contains(query.toLowerCase())) {
				filteredValues.add(civ);
			}
		}

		return filteredValues;
	}
	public List<TaTServiceWebExterneDTO> typeTaServiceWebExterneAutoCompleteLight(String query) {
		List<TaTServiceWebExterneDTO> allValues = taTServiceWebExterneService.selectAllDTO();
		List<TaTServiceWebExterneDTO> filteredValues = new ArrayList<TaTServiceWebExterneDTO>();

		for (int i = 0; i < allValues.size(); i++) {
			TaTServiceWebExterneDTO civ = allValues.get(i);
			if(query.equals("*") || civ.getCodeTServiceWebExterne().toLowerCase().contains(query.toLowerCase())) {
				filteredValues.add(civ);
			}
		}

		return filteredValues;
	}

	public TaTServiceWebExterne getTaTServiceWebExterne() {
		return taTServiceWebExterne;
	}

	public void setTaTServiceWebExterne(TaTServiceWebExterne taTServiceWebExterne) {
		this.taTServiceWebExterne = taTServiceWebExterne;
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

	public TaServiceWebExterne getTaServiceWebExterne() {
		return taServiceWebExterne;
	}

	public void setTaServiceWebExterne(TaServiceWebExterne taTaServiceWebExterne) {
		this.taServiceWebExterne = taTaServiceWebExterne;
	}

	public TaServiceWebExterneDTO rempliDTO(){
		if(taServiceWebExterne!=null){			
			try {
				taServiceWebExterne=taTaServiceWebExterneService.findByCode(taServiceWebExterne.getCodeServiceWebExterne());
				mapperModelToUI.map(taServiceWebExterne, selectedTaServiceWebExterneDTO);
				if(values.contains(selectedTaServiceWebExterneDTO))values.add(selectedTaServiceWebExterneDTO);
			} catch (FinderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return selectedTaServiceWebExterneDTO;
	}

	public StreamedContent getLogo() {
		return logo;
	}

	public void setLogo(StreamedContent logo) {
		this.logo = logo;
	}

	public TaTServiceWebExterneDTO getTaTServiceWebExterneDTO() {
		return taTServiceWebExterneDTO;
	}

	public void setTaTServiceWebExterneDTO(TaTServiceWebExterneDTO taTServiceWebExterneDTO) {
		this.taTServiceWebExterneDTO = taTServiceWebExterneDTO;
	}

}  
