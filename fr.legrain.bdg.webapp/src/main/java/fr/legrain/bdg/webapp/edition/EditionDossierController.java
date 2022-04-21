package fr.legrain.bdg.webapp.edition;

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
import fr.legrain.bdg.edition.service.remote.ITaEditionServiceRemote;
import fr.legrain.bdg.edition.service.remote.ITaTEditionServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaTAuthentificationServiceRemote;
import fr.legrain.bdg.tache.service.remote.ITaEvenementServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTTvaDocServiceRemote;
import fr.legrain.bdg.webapp.ConstWeb;
import fr.legrain.bdg.webapp.app.AbstractController;
import fr.legrain.bdg.webapp.app.LgrTab;
import fr.legrain.bdg.webapp.app.TabListModelBean;
import fr.legrain.bdg.webapp.app.TabViewsBean;
import fr.legrain.birt.AnalyseFileReport;
import fr.legrain.droits.service.SessionInfo;
import fr.legrain.edition.dto.TaEditionDTO;
import fr.legrain.edition.dto.TaTEditionDTO;
import fr.legrain.edition.model.TaEdition;
import fr.legrain.edition.model.TaTEdition;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.ModeObjetEcranServeur;

@Named
@ViewScoped  
public class EditionDossierController extends AbstractController implements Serializable {  

	@Inject @Named(value="tabListModelCenterBean")
	private TabListModelBean tabsCenter;

	@Inject @Named(value="tabViewsBean")
	private TabViewsBean tabViews;

	private String paramId;

	private TabView tabViewTaEdition; //Le Binding sur les onglets "secondaire" semble ne pas fonctionné, les onglets ne sont pas générés au bon moment avec le c:forEach

	private List<TaEditionDTO> values; 

	private StreamedContent logo;

	private @Inject SessionInfo sessionInfo;

	protected ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();

	private @EJB ITaEditionServiceRemote taEditionService;
	private @EJB ITaTEditionServiceRemote taTaTEditionService;

	private @EJB ITaInfoEntrepriseServiceRemote taInfoEntrepriseService;

	private @EJB ITaTAuthentificationServiceRemote taTCivlicteService;
	private @EJB ITaTTvaDocServiceRemote taTTvaDocService;


	private @EJB ITaEvenementServiceRemote taEvenementService;
	private @EJB ITaUtilisateurServiceRemote taUtilisateurService;

	private TaEditionDTO[] selectedTaEditionDTOs; 
	private TaEdition taEdition = new TaEdition();
	private TaEditionDTO newTaEditionDTO = new TaEditionDTO();
	private TaEditionDTO selectedTaEditionDTO = new TaEditionDTO();

	private TaTEdition taTEdition;


	private LgrDozerMapper<TaEditionDTO,TaEdition> mapperUIToModel  = new LgrDozerMapper<TaEditionDTO,TaEdition>();
	private LgrDozerMapper<TaEdition,TaEditionDTO> mapperModelToUI  = new LgrDozerMapper<TaEdition,TaEditionDTO>();

	// Dima - Début
	private TaTEditionDTO taTEditionDTO;

	// Dima -  Fin

	public EditionDossierController() {  
		//values = getValues();
	}  

	@PostConstruct
	public void postConstruct(){

		refresh();

		//		FaceletContext faceletContext = (FaceletContext) FacesContext.getCurrentInstance().getAttributes().get(FaceletContext.FACELET_CONTEXT_KEY);
		//		String formId = (String) faceletContext.getAttribute("paramId");
		//		if(formId!=null && !formId.equals("")) {
		//			try {
		//				selectedTaEditionDTO = TaEditionDTOService.findById(LibConversion.stringToInteger(paramId));
		//			} catch (FinderException e) {
		//				// TODO Auto-generated catch block
		//				e.printStackTrace();
		//			}
		//		}
	}

	public void refresh(){
		values = taEditionService.findAllLight();
		//values = taEditionService.findByCodeLight(null);
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}

	public List<TaEditionDTO> getValues(){  
		return values;
	}

	public void actAnnuler(ActionEvent actionEvent) {
		try {
			switch (modeEcran.getMode()) {
			case C_MO_INSERTION:
				//				if(selectedTaEditionDTO.getCodeServiceWebExterne()!=null) {
				//					taEditionService.annuleCode(selectedTaEditionDTO.getCodeServiceWebExterne());
				//				}
				taEdition = new TaEdition();
				mapperModelToUI.map(taEdition,selectedTaEditionDTO );
				selectedTaEditionDTO.setFichierBlob(null);
				selectedTaEditionDTO.setFichierBlob(taEdition.getFichierBlob());
				selectedTaEditionDTO=new TaEditionDTO();

				if(!values.isEmpty()) selectedTaEditionDTO = values.get(0);
				onRowSelect(null);
				break;
			case C_MO_EDITION:
				if(selectedTaEditionDTO.getId()!=null && selectedTaEditionDTO.getId()!=0){
					taEdition = taEditionService.findById(selectedTaEditionDTO.getId());
					selectedTaEditionDTO=taEditionService.findByIdDTO(selectedTaEditionDTO.getId());
				}				
				break;

			default:
				break;
			}
			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
			mapperModelToUI.map(taEdition, selectedTaEditionDTO);
			selectedTaEditionDTO.setFichierBlob(null);
			selectedTaEditionDTO.setFichierBlob(taEdition.getFichierBlob());
			refreshLogo();

			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("TaEdition", "actAnnuler"));
			}
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	public void autoCompleteMapUIToDTO() {
		if(taTEditionDTO!=null) {
			validateUIField(Const.C_CODE_T_SERVICE_WEB_EXTERNE,taTEditionDTO.getCodeTEdition());
			selectedTaEditionDTO.setCodeTEdition(taTEditionDTO.getCodeTEdition());
		}else selectedTaEditionDTO.setCodeTEdition(null);
		

	}

	public void autoCompleteMapDTOtoUI() {
		try {
			taTEdition = null;

			taTEditionDTO = null;

			if(selectedTaEditionDTO.getCodeTEdition()!=null && !selectedTaEditionDTO.getCodeTEdition().equals("")) {
				taTEdition = taTaTEditionService.findByCode(selectedTaEditionDTO.getCodeTEdition());
				taTEditionDTO=taTaTEditionService.findByCodeDTO(selectedTaEditionDTO.getCodeTEdition());
			}

		} catch (FinderException e) {
			e.printStackTrace();
		}
	}

	public void actEnregistrer(ActionEvent actionEvent) {		
		mapperUIToModel.map(selectedTaEditionDTO, taEdition);
		taEdition.setFichierBlob(null);
		taEdition.setFichierBlob(selectedTaEditionDTO.getFichierBlob()); 
		try {
			taEdition = taEditionService.merge(taEdition,ITaEditionServiceRemote.validationContext);

			//			if(selectedTaEditionDTO.getCodeServiceWebExterne()!=null) {
			//				taEditionService.annuleCode(selectedTaEditionDTO.getCodeServiceWebExterne());
			//			}

			mapperModelToUI.map(taEdition, selectedTaEditionDTO);
			selectedTaEditionDTO.setFichierBlob(null);
			selectedTaEditionDTO.setFichierBlob(taEdition.getFichierBlob());
			autoCompleteMapDTOtoUI();
			

			if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_INSERTION)==0) {
				values.add(selectedTaEditionDTO);
			}

			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

		} catch(Exception e) {
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			//context.addMessage(null, new FacesMessage("TaEdition", "actEnregistrer")); 
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "TaEdition", e.getMessage()));
		}

		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("TaEdition", "actEnregistrer"));
		}
	}

	public void actInserer(ActionEvent actionEvent) {
		try {
			selectedTaEditionDTOs= new TaEditionDTO[]{};
			selectedTaEditionDTO = new TaEditionDTO();
			taEdition = new TaEdition();

			//			String codeTaEditionDefaut = "C";
			String codeTaEditionDefaut = "";

			//			taGenCodeExService.libereVerrouTout(new ArrayList<String>(SessionListener.getSessions().keySet()));
			//			selectedTaEditionDTO.setCodeTaEdition(taEditionService.genereCode(null)); //ejb
			//			validateUIField(Const.C_CODE_TIERS,selectedTaEditionDTO.getCodeServiceWebExterne());//permet de verrouiller le code genere

			selectedTaEditionDTO.setCodeEdition(codeTaEditionDefaut);
			
			selectedTaEditionDTO.setSysteme(true);
			taEdition.setSysteme(true);
			
			selectedTaEditionDTO.setActif(true);
			taEdition.setActif(true);

			//			selectedTaEditionDTO.setCodeCompta(selectedTaEditionDTO.getCodeServiceWebExterne()); //ejb
			//			validateUIField(Const.C_CODE_COMPTA,selectedTaEditionDTO.getCodeCompta()); //ejb
			//			selectedTaEditionDTO.setCompte("411"); //ejb
			//			validateUIField(Const.C_COMPTE,selectedTaEditionDTO.getCompte()); //ejb

			//TaTEditionDAO daoTTaEdition = new TaTEditionDAO(getEm());
			TaTEdition taTEdition;

			taTEdition = taTaTEditionService.findByCode(codeTaEditionDefaut);

			//			if(taTServiceWebExterne!=null && taTServiceWebExterne.getCompteTTaEdition()!=null) {
			//				selectedTaEditionDTO.setCompte(taTServiceWebExterne.getCompteTTaEdition());
			//				this.taTServiceWebExterne = taTServiceWebExterne;
			//			} else {
			//				//selectedTaEditionDTO.setCompte(TaEditionPlugin.getDefault().getPreferenceStore().
			//				//getString(PreferenceConstants.TIERS_COMPTE_COMPTALE_DEFAUT));
			//			}
			//			selectedTaEditionDTO.setActifTaEdition(true);
			//			//swtTaEdition.setTtcTaEdition(TaEditionPlugin.getDefault().getPreferenceStore().
			//			//getBoolean(PreferenceConstants.TIERS_SAISIE_TTC_DEFAUT));
			//			selectedTaEditionDTO.setCodeTTvaDoc("F");

			autoCompleteMapDTOtoUI();

			modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
			scrollToTop();
			if(tabViewTaEdition!=null) {
				tabViewTaEdition.setActiveIndex(0);
				updateTab();
				//				adresseController.setMasterEntity(taTaEdition);
				//				adresseController.refresh(null);
			}
			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("TaEdition", "actInserer"));
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
		//refreshLogo();

		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("TaEdition", "actModifier"));
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
			context.addMessage(null, new FacesMessage("TaEdition", "actAide"));
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
//			taEdition.setFichierBlob(IOUtils.toByteArray(event.getFile().getInputstream()));
//			taEdition.setFichierNom(event.getFile().getFileName());
			//			fichier.setBlobFichierMiniature(fichier.resizeImage(IOUtils.toByteArray(event.getFile().getInputstream()), event.getFile().getFileName(), 640, 480));
			
			selectedTaEditionDTO.setFichierBlob(IOUtils.toByteArray(event.getFile().getInputStream()));
			selectedTaEditionDTO.setFichierNom(event.getFile().getFileName());
			
			AnalyseFileReport a = new AnalyseFileReport();
			a.initializeBuildDesignReportConfig(event.getFile().getFileName(),event.getFile().getInputStream());
//			System.out.println("EditionDossierController.handleFileUpload()" + a.findTitleReport());
			
//			File sourceFile = new File(sourceDir);
//	        File destinationFile = new File(destinationDir);
//	        if (!destinationFile.exists()) {
//	            destinationFile.mkdir();
//	            System.out.println("Folder Created -> "+ destinationFile.getAbsolutePath());
//	        }
//	        if (sourceFile.exists()) {
//	            System.out.println("Images copied to Folder: "+ destinationFile.getName());             
//	            PDDocument document = PDDocument.load(sourceDir);
//	            List<PDPage> list = document.getDocumentCatalog().getAllPages();
//	            System.out.println("Total files to be converted -> "+ list.size());
//
//	            String fileName = sourceFile.getName().replace(".pdf", "");             
//	            int pageNumber = 1;
//	            for (PDPage page : list) {
//	                BufferedImage image = page.convertToImage();
//	                File outputfile = new File(destinationDir + fileName +"_"+ pageNumber +".png");
//	                System.out.println("Image Created -> "+ outputfile.getName());
//	                ImageIO.write(image, "png", outputfile);
//	                pageNumber++;
//	            }
//	            document.close();
//	            System.out.println("Converted Images are saved at -> "+ destinationFile.getAbsolutePath());
//	        } else {
//	            System.err.println(sourceFile.getName() +" File not exists");
//	        }

//			actEnregistrer(null);
			//refreshLogo();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void refreshLogo() {
//		try {
//			logo = null;
//			if(taEdition!=null && taEdition.getFichierBlob()!=null) {
//				InputStream stream = new ByteArrayInputStream(taEdition.getFichierBlob()); 
//				//logo = new DefaultStreamedContent(stream, "image/png");
//				logo = new DefaultStreamedContent(stream,null,taEdition.getFichierNom());
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	public void supprimerLogo(ActionEvent e) {
		taEdition.setFichierBlob(null);
		taEdition.setFichierNom(null);
		selectedTaEditionDTO.setFichierBlob(null);
		selectedTaEditionDTO.setFichierNom(null);
		actEnregistrer(null);
		refreshLogo();
	}

	public void nouveau(ActionEvent actionEvent) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.edition.dossier");
		b.setTitre("Edition dossier");
		b.setTemplate("admin/edition_dossier.xhtml");
		b.setIdTab(LgrTab.ID_TAB_EDITION_DOSSIER);
		b.setStyle(LgrTab.CSS_CLASS_TAB_EDITION_DOSSIER);
		b.setParamId("0");

		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
		actInserer(actionEvent);

		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("TaEdition", 
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

	public void repondreMessage(ActionEvent event) {
		Integer idMessage = (Integer) event.getComponent().getAttributes().get("idTaEdition");

		ExternalContext context2 = FacesContext.getCurrentInstance().getExternalContext();
		try {
			context2.redirect(context2.getRequestContextPath() + "/m/tiers.xhtml?idTaEdition="+idMessage);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void actSupprimer() {
		actSupprimer(null);
	}

	public void actSupprimer(ActionEvent actionEvent) {
		TaEdition taTaEdition = new TaEdition();
		try {
			if(selectedTaEditionDTO!=null && selectedTaEditionDTO.getId()!=null){
				taTaEdition = taEditionService.findById(selectedTaEditionDTO.getId());
			}

			taEditionService.remove(taTaEdition);
			values.remove(selectedTaEditionDTO);

			if(!values.isEmpty()) {
				selectedTaEditionDTO = values.get(0);
			}else {
				selectedTaEditionDTO=new TaEditionDTO();
			}
			updateTab();
			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("TaEdition", "actSupprimer"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "TaEdition", e.getCause().getCause().getCause().getCause().getMessage()));
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
		selectedTaEditionDTO=new TaEditionDTO();
		selectedTaEditionDTOs=new TaEditionDTO[]{selectedTaEditionDTO};

		//		//gestion du filtre de la liste
		//        RequestContext requestContext = RequestContext.getCurrentInstance();
		//        requestContext.execute("PF('wvDataTableListeTaEdition').filter()");


		//		LgrTab typeOngletDejaOuvert = null;
		//		for (LgrTab o : tabsCenter.getOnglets()) {
		//			if(LgrTab.TYPE_TAB_TIERS.equals(o.getTypeOnglet())) {
		//				typeOngletDejaOuvert = o;
		//			}
		//		}
		//		
		//		if(typeOngletDejaOuvert!=null) {
		////			if(!values.isEmpty()) {
		////				selectedTaEditionDTO = values.get(0);
		////			}
		//			tabsCenter.supprimerOnglet(typeOngletDejaOuvert);
		//		}
		//		if(ConstWeb.DEBUG) {
		//		FacesContext context = FacesContext.getCurrentInstance();  
		//		context.addMessage(null, new FacesMessage("TaEdition", "actFermer"));
		//		}
	}

	public void actImprimer(ActionEvent actionEvent) {
		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("TaEdition", "actImprimer"));
		}
		try {

			//		FacesContext facesContext = FacesContext.getCurrentInstance();
			//		HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);

			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			sessionMap.put("tiers", taEditionService.findById(selectedTaEditionDTO.getId()));

			////////////////////////////////////////////////////////////////////////////////////////
			//Test génération de fichier PDF

			//		HashMap<String,Object> hm = new HashMap<>();
			//		hm.put( "tiers", taEditionService.findById(selectedTaEditionDTO.getId()));
			//		BirtUtil.setAppContextEdition(hm);
			//		
			//		BirtUtil.startReportEngine();
			//		
			////		BirtUtil.renderReportToFile(
			////				"/donnees/Projet/Java/Eclipse/GestionCommerciale_branche_2_0_13_JEE_E46/fr.legrain.bdg.webapp/src/main/webapp/reports/tiers/FicheTaEdition.rptdesign", 
			////				"/tmp/tiers.pdf", 
			////				new HashMap<>(), 
			////				BirtUtil.PDF_FORMAT);
			//		
			//		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("/reports/tiers/FicheTaEdition.rptdesign");
			//		BirtUtil.renderReportToFile(
			//				inputStream, 
			//				"/tmp/tiers.pdf", 
			//				new HashMap<>(), 
			//				BirtUtil.PDF_FORMAT);
			////////////////////////////////////////////////////////////////////////////////////////
			//java.net.URL.setURLStreamHandlerFactory();
			//		taEditionService.generePDF(selectedTaEditionDTO.getId());
			////////////////////////////////////////////////////////////////////////////////////////

			//			session.setAttribute("tiers", taEditionService.findById(selectedTaEditionDTO.getId()));
			System.out.println("TaEditionController.actImprimer()");
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
				context.addMessage(null, new FacesMessage("TaEdition", 
						"Chargement de l'edition N°"+selectedTaEditionDTO.getCodeEdition()
						)); }
		} else {
			tabViews.selectionneOngletCentre(LgrTab.TYPE_TAB_TIERS);
		}
	} 
	public void updateTab(){
		try {

			if(selectedTaEditionDTOs!=null && selectedTaEditionDTOs.length>0) {
				selectedTaEditionDTO = selectedTaEditionDTOs[0];
			}
			if(selectedTaEditionDTO.getId()!=null && selectedTaEditionDTO.getId()!=0) {
				taEdition = taEditionService.findById(selectedTaEditionDTO.getId());
			}
			mapperModelToUI.map(taEdition, selectedTaEditionDTO);	
			selectedTaEditionDTO.setFichierBlob(null);
			selectedTaEditionDTO.setFichierBlob(taEdition.getFichierBlob());

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
		return modeEcran.dataSetEnModif() ||  selectedTaEditionDTO.getId()==0;
	}

	public void onRowSelect(SelectEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.edition.dossier");
		b.setTitre("Edition dossier");
		b.setTemplate("admin/edition_dossier.xhtml");
		b.setIdTab(LgrTab.ID_TAB_EDITION_DOSSIER);
		b.setStyle(LgrTab.CSS_CLASS_TAB_EDITION_DOSSIER);
		b.setParamId(LibConversion.integerToString(selectedTaEditionDTO.getId()));

		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);


		updateTab();
		scrollToTop();

		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("TaEdition", 
					"Chargement de l'édition N°"+selectedTaEditionDTO.getCodeEdition()
					)); 
		}
	} 

	public boolean editable() {
		return !modeEcran.dataSetEnModif();
	}

	public void actDialogTypeTaEdition(ActionEvent actionEvent) {

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
		//		context.addMessage(null, new FacesMessage("TaEdition", "actAbout")); 	    
	}

	public void handleReturnDialogTypeTaEdition(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			taTEdition = (TaTEdition) event.getObject();
			try {
				taTEditionDTO = taTaTEditionService.findByCodeDTO(taTEdition.getCodeTEdition());
			} catch (FinderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void actDialogFamilleTaEdition(ActionEvent actionEvent) {

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
		//		context.addMessage(null, new FacesMessage("TaEdition", "actAbout")); 	    
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
		//		context.addMessage(null, new FacesMessage("TaEdition", "actAbout")); 	    
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
		//		context.addMessage(null, new FacesMessage("TaEdition", "actAbout")); 	    
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

	public void validateTaEdition(FacesContext context, UIComponent component, Object value) throws ValidatorException {

		String messageComplet = "";

		try {
			//String selectedRadio = (String) value;

			String nomChamp =  (String) component.getAttributes().get("nomChamp");

			//msg = "Mon message d'erreur pour : "+nomChamp;

			//		  validateUIField(nomChamp,value);
			//		  TaEditionDTO dtoTemp=new TaEditionDTO();
			//		  PropertyUtils.setSimpleProperty(dtoTemp, nomChamp, value);
			//		  taEditionService.validateDTOProperty(dtoTemp, nomChamp, ITaEditionServiceRemote.validationContext );

			//			//validation automatique via la JSR bean validation
			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
			Set<ConstraintViolation<TaEditionDTO>> violations = factory.getValidator().validateValue(TaEditionDTO.class,nomChamp,value);
			if (violations.size() > 0) {
				messageComplet = "Erreur de validation : ";
				//				List<IStatus> statusList = new ArrayList<IStatus>();
				for (ConstraintViolation<TaEditionDTO> cv : violations) {
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

			//selectedTaEditionDTO.setAdresse1Adresse("abcd");

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
			if(value instanceof TaTEditionDTO && ((TaTEditionDTO) value).getCodeTEdition()!=null && ((TaTEditionDTO) value).getCodeTEdition().equals(Const.C_AUCUN))value=null;	

		}

		validateUIField(nomChamp,value);
	}

	public boolean validateUIField(String nomChamp,Object value) {

		boolean changement=false;


		try {				
			if(nomChamp.equals(Const.C_CODE_T_SERVICE_WEB_EXTERNE)) {
				TaTEdition entity = null;
				if(value!=null){
					if(value instanceof TaTEdition){
						entity=(TaTEdition) value;
						//								entity = taTServiceWebExterneService.findByCode(entity.getCodeTServiceWebExterne());
					}else if(value instanceof TaTEditionDTO){
						entity = taTaTEditionService.findByCode(((TaTEditionDTO)value).getCodeTEdition());
					}else{
						entity = taTaTEditionService.findByCode((String)value);
					}
				}else{
					selectedTaEditionDTO.setCodeTEdition("");
					taTEditionDTO.setCodeTEdition("");
					taTEdition=null;
				}						
				taEdition.setTaTEdition(entity);

			}
			else if(nomChamp.equals(Const.C_CODE_T_AUTHENTIFICATION)) {
//
//				TaTAuthentification entity =null;
//				if(value!=null){
//					if(value instanceof TaTAuthentification){
//						entity=(TaTAuthentification) value;
//						//							entity = taTCivlicteService.findByCode(entity.getCodeTCivilite());
//					}else if(value instanceof TaTAuthentificationDTO){
//						entity = taTCivlicteService.findByCode(((TaTAuthentificationDTO)value).getCodeTAuthentification());
//					}else{
//						entity = taTCivlicteService.findByCode((String)value);								
//					}
//				}else{
//					selectedTaEditionDTO.setCodeTAuthentification(null);
//					taTAuthentificationDTO.setCodeTAuthentification(null);
//					taTAuthentification=null;
//				}
//
//				taEdition.setTaTAuthentification(entity);


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
//					selectedTaEditionDTO.setCodeTTvaDoc(null);
//				}
//
//				taEdition.setTaTTvaDoc(entity);

			}

			return false;

		} catch (Exception e) {

		}
		return false;
	}

	public List<TaTEdition> typeTaEditionAutoComplete(String query) {
		List<TaTEdition> allValues = taTaTEditionService.selectAll();
		List<TaTEdition> filteredValues = new ArrayList<TaTEdition>();

		for (int i = 0; i < allValues.size(); i++) {
			TaTEdition civ = allValues.get(i);
			if(query.equals("*") || civ.getCodeTEdition().toLowerCase().contains(query.toLowerCase())) {
				filteredValues.add(civ);
			}
		}

		return filteredValues;
	}
	public List<TaTEditionDTO> typeTaEditionAutoCompleteLight(String query) {
		List<TaTEditionDTO> allValues = taTaTEditionService.selectAllDTO();
		List<TaTEditionDTO> filteredValues = new ArrayList<TaTEditionDTO>();

		for (int i = 0; i < allValues.size(); i++) {
			TaTEditionDTO civ = allValues.get(i);
			if(query.equals("*") || civ.getCodeTEdition().toLowerCase().contains(query.toLowerCase())) {
				filteredValues.add(civ);
			}
		}

		return filteredValues;
	}

	public TaTEdition getTaTEdition() {
		return taTEdition;
	}

	public void setTaTEdition(TaTEdition taTServiceWebExterne) {
		this.taTEdition = taTServiceWebExterne;
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

	public void setTaEdition(TaEdition taTaEdition) {
		this.taEdition = taTaEdition;
	}

	public TaEditionDTO rempliDTO(){
		if(taEdition!=null){			
			try {
				taEdition=taEditionService.findByCode(taEdition.getCodeEdition());
				mapperModelToUI.map(taEdition, selectedTaEditionDTO);
				selectedTaEditionDTO.setFichierBlob(null);
				selectedTaEditionDTO.setFichierBlob(taEdition.getFichierBlob());
				if(values.contains(selectedTaEditionDTO))values.add(selectedTaEditionDTO);
			} catch (FinderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return selectedTaEditionDTO;
	}

	public StreamedContent getLogo() {
		if(taEdition!=null && taEdition.getFichierBlob()!=null) {
			InputStream stream = new ByteArrayInputStream(taEdition.getFichierBlob()); 
			//logo = new DefaultStreamedContent(stream, "image/png");
			logo = new DefaultStreamedContent(stream,null,taEdition.getFichierNom());
		}
		return logo;
	}

	public void setLogo(StreamedContent logo) {
		this.logo = logo;
	}

	public TaTEditionDTO getTaTEditionDTO() {
		return taTEditionDTO;
	}

	public void setTaTEditionDTO(TaTEditionDTO taTServiceWebExterneDTO) {
		this.taTEditionDTO = taTServiceWebExterneDTO;
	}

}  
