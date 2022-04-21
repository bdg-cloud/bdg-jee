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
import org.primefaces.event.TransferEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.DualListModel;
import org.primefaces.model.StreamedContent;

import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.bdg.droits.service.remote.ITaUtilisateurServiceRemote;
import fr.legrain.bdg.edition.service.remote.ITaActionInterneProgServiceRemote;
import fr.legrain.bdg.edition.service.remote.ITaEditionCatalogueServiceRemote;
import fr.legrain.bdg.edition.service.remote.ITaTEditionCatalogueServiceRemote;
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
import fr.legrain.droits.service.SessionInfo;
import fr.legrain.edition.dto.TaEditionCatalogueDTO;
import fr.legrain.edition.dto.TaTEditionCatalogueDTO;
import fr.legrain.edition.model.TaActionInterneProg;
import fr.legrain.edition.model.TaEditionCatalogue;
import fr.legrain.edition.model.TaTEditionCatalogue;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.ModeObjetEcranServeur;

@Named
@ViewScoped  
public class EditionCatalogueController extends AbstractController implements Serializable {  

	@Inject @Named(value="tabListModelCenterBean")
	private TabListModelBean tabsCenter;

	@Inject @Named(value="tabViewsBean")
	private TabViewsBean tabViews;

	private String paramId;

	private TabView tabViewTaEditionCatalogue; //Le Binding sur les onglets "secondaire" semble ne pas fonctionné, les onglets ne sont pas générés au bon moment avec le c:forEach

	private List<TaEditionCatalogueDTO> values; 

	private StreamedContent logo;

	private @Inject SessionInfo sessionInfo;

	protected ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();

	private @EJB ITaEditionCatalogueServiceRemote taEditionCatalogueService;
	private @EJB ITaTEditionCatalogueServiceRemote taTaTEditionCatalogueService;
	
	private @EJB ITaActionInterneProgServiceRemote taActionInterneProgService;

	private @EJB ITaInfoEntrepriseServiceRemote taInfoEntrepriseService;

	private @EJB ITaTAuthentificationServiceRemote taTCivlicteService;
	private @EJB ITaTTvaDocServiceRemote taTTvaDocService;


	private @EJB ITaEvenementServiceRemote taEvenementService;
	private @EJB ITaUtilisateurServiceRemote taUtilisateurService;

	private TaEditionCatalogueDTO[] selectedTaEditionCatalogueDTOs; 
	private TaEditionCatalogue taEditionCatalogue = new TaEditionCatalogue();
	private TaEditionCatalogueDTO newTaEditionCatalogueDTO = new TaEditionCatalogueDTO();
	private TaEditionCatalogueDTO selectedTaEditionCatalogueDTO = new TaEditionCatalogueDTO();

	private TaTEditionCatalogue taTEditionCatalogue;
	
	private DualListModel<TaActionInterneProg> dualListModelActionProg;


	private LgrDozerMapper<TaEditionCatalogueDTO,TaEditionCatalogue> mapperUIToModel  = new LgrDozerMapper<TaEditionCatalogueDTO,TaEditionCatalogue>();
	private LgrDozerMapper<TaEditionCatalogue,TaEditionCatalogueDTO> mapperModelToUI  = new LgrDozerMapper<TaEditionCatalogue,TaEditionCatalogueDTO>();

	// Dima - Début
	private TaTEditionCatalogueDTO taTEditionCatalogueDTO;

	// Dima -  Fin

	public EditionCatalogueController() {  
		//values = getValues();
	}  

	@PostConstruct
	public void postConstruct(){

		refresh();

		//		FaceletContext faceletContext = (FaceletContext) FacesContext.getCurrentInstance().getAttributes().get(FaceletContext.FACELET_CONTEXT_KEY);
		//		String formId = (String) faceletContext.getAttribute("paramId");
		//		if(formId!=null && !formId.equals("")) {
		//			try {
		//				selectedTaEditionCatalogueDTO = TaEditionCatalogueDTOService.findById(LibConversion.stringToInteger(paramId));
		//			} catch (FinderException e) {
		//				// TODO Auto-generated catch block
		//				e.printStackTrace();
		//			}
		//		}
	}

	public void refresh(){
		values = taEditionCatalogueService.findAllLight();
		//values = taEditionCatalogueService.findByCodeLight(null);
		
        List<TaActionInterneProg> actionSource = taActionInterneProgService.selectAll();
        List<TaActionInterneProg> actionTarget = new ArrayList<TaActionInterneProg>();
        dualListModelActionProg = new DualListModel<TaActionInterneProg>(actionSource, actionTarget);
		
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}

	public List<TaEditionCatalogueDTO> getValues(){  
		return values;
	}

	public void actAnnuler(ActionEvent actionEvent) {
		try {
			switch (modeEcran.getMode()) {
			case C_MO_INSERTION:
				//				if(selectedTaEditionCatalogueDTO.getCodeServiceWebExterne()!=null) {
				//					taEditionCatalogueService.annuleCode(selectedTaEditionCatalogueDTO.getCodeServiceWebExterne());
				//				}
				taEditionCatalogue = new TaEditionCatalogue();
				mapperModelToUI.map(taEditionCatalogue,selectedTaEditionCatalogueDTO );
				selectedTaEditionCatalogueDTO.setFichierBlob(null);
				selectedTaEditionCatalogueDTO.setFichierBlob(taEditionCatalogue.getFichierBlob());
				selectedTaEditionCatalogueDTO=new TaEditionCatalogueDTO();

				if(!values.isEmpty()) selectedTaEditionCatalogueDTO = values.get(0);
				onRowSelect(null);
				break;
			case C_MO_EDITION:
				if(selectedTaEditionCatalogueDTO.getId()!=null && selectedTaEditionCatalogueDTO.getId()!=0){
					taEditionCatalogue = taEditionCatalogueService.findById(selectedTaEditionCatalogueDTO.getId());
					selectedTaEditionCatalogueDTO=taEditionCatalogueService.findByIdDTO(selectedTaEditionCatalogueDTO.getId());
				}				
				break;

			default:
				break;
			}
			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
			mapperModelToUI.map(taEditionCatalogue, selectedTaEditionCatalogueDTO);
			selectedTaEditionCatalogueDTO.setFichierBlob(null);
			selectedTaEditionCatalogueDTO.setFichierBlob(taEditionCatalogue.getFichierBlob());
			refreshLogo();

			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("TaEditionCatalogue", "actAnnuler"));
			}
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	public void autoCompleteMapUIToDTO() {
		if(taTEditionCatalogueDTO!=null) {
			validateUIField(Const.C_CODE_T_SERVICE_WEB_EXTERNE,taTEditionCatalogueDTO.getCodeTEdition());
			selectedTaEditionCatalogueDTO.setCodeTEdition(taTEditionCatalogueDTO.getCodeTEdition());
		}else selectedTaEditionCatalogueDTO.setCodeTEdition(null);
		

	}

	public void autoCompleteMapDTOtoUI() {
		try {
			taTEditionCatalogue = null;

			taTEditionCatalogueDTO = null;

			if(selectedTaEditionCatalogueDTO.getCodeTEdition()!=null && !selectedTaEditionCatalogueDTO.getCodeTEdition().equals("")) {
				taTEditionCatalogue = taTaTEditionCatalogueService.findByCode(selectedTaEditionCatalogueDTO.getCodeTEdition());
				taTEditionCatalogueDTO=taTaTEditionCatalogueService.findByCodeDTO(selectedTaEditionCatalogueDTO.getCodeTEdition());
			}

		} catch (FinderException e) {
			e.printStackTrace();
		}
	}

	public void actEnregistrer(ActionEvent actionEvent) {		

		mapperUIToModel.map(selectedTaEditionCatalogueDTO, taEditionCatalogue);
		taEditionCatalogue.setFichierBlob(null);
		taEditionCatalogue.setFichierBlob(selectedTaEditionCatalogueDTO.getFichierBlob());
		try {
			
			if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_INSERTION)==0) {
				//ajouter les nouvelles
				int i = 1;
				for (TaActionInterneProg ctrl : dualListModelActionProg.getTarget()) {
					if(taEditionCatalogue.chercheTaActionInterne(ctrl)!=null) {
//						taEditionCatalogue.chercheTaActionInterne(ctrl).setPosition(i);
					} else {
						taEditionCatalogue.getActionInterne().add(ctrl);
					}
					i++;
				}
			} else {
				//supprimer les actions non sélectionnées
				List<TaActionInterneProg> lasupprimer = new ArrayList<TaActionInterneProg>();
				for (TaActionInterneProg c : taEditionCatalogue.getActionInterne()) {
					if(!dualListModelActionProg.getTarget().contains(c)) {
						lasupprimer.add(c);
					}
				}
				for (TaActionInterneProg taModeleConformite : lasupprimer) {
					taEditionCatalogue.getActionInterne().remove(taEditionCatalogue.chercheTaActionInterne(taModeleConformite));
				}
				
				//ajouter les nouvelles
				int i = 1;
				for (TaActionInterneProg ctrl : dualListModelActionProg.getTarget()) {
					if(taEditionCatalogue.chercheTaActionInterne(ctrl)!=null) {
//						taEditionCatalogue.chercheTaActionInterne(ctrl).setPosition(i);
					} else {
						taEditionCatalogue.getActionInterne().add(ctrl);
					}
					i++;
				}
			}
			taEditionCatalogue = taEditionCatalogueService.merge(taEditionCatalogue,ITaEditionCatalogueServiceRemote.validationContext);

			//			if(selectedTaEditionCatalogueDTO.getCodeServiceWebExterne()!=null) {
			//				taEditionCatalogueService.annuleCode(selectedTaEditionCatalogueDTO.getCodeServiceWebExterne());
			//			}

			mapperModelToUI.map(taEditionCatalogue, selectedTaEditionCatalogueDTO);
			selectedTaEditionCatalogueDTO.setFichierBlob(null);
			selectedTaEditionCatalogueDTO.setFichierBlob(taEditionCatalogue.getFichierBlob());
			autoCompleteMapDTOtoUI();

			if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_INSERTION)==0) {
				values.add(selectedTaEditionCatalogueDTO);
			}

			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

		} catch(Exception e) {
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			//context.addMessage(null, new FacesMessage("TaEditionCatalogue", "actEnregistrer")); 
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "TaEditionCatalogue", e.getMessage()));
		}

		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("TaEditionCatalogue", "actEnregistrer"));
		}
	}

	public void actInserer(ActionEvent actionEvent) {
		try {
			selectedTaEditionCatalogueDTOs= new TaEditionCatalogueDTO[]{};
			selectedTaEditionCatalogueDTO = new TaEditionCatalogueDTO();
			taEditionCatalogue = new TaEditionCatalogue();
			
			List<TaActionInterneProg> actionSource = taActionInterneProgService.selectAll();
	        List<TaActionInterneProg> actionTarget = new ArrayList<TaActionInterneProg>();
	        dualListModelActionProg = new DualListModel<TaActionInterneProg>(actionSource, actionTarget);

			//			String codeTaEditionCatalogueDefaut = "C";
			String codeTaEditionCatalogueDefaut = "";

			//			taGenCodeExService.libereVerrouTout(new ArrayList<String>(SessionListener.getSessions().keySet()));
			//			selectedTaEditionCatalogueDTO.setCodeTaEditionCatalogue(taEditionCatalogueService.genereCode(null)); //ejb
			//			validateUIField(Const.C_CODE_TIERS,selectedTaEditionCatalogueDTO.getCodeServiceWebExterne());//permet de verrouiller le code genere

			selectedTaEditionCatalogueDTO.setCodeEdition(codeTaEditionCatalogueDefaut);
			
			selectedTaEditionCatalogueDTO.setSysteme(true);
			taEditionCatalogue.setSysteme(true);
			
			selectedTaEditionCatalogueDTO.setActif(true);
			taEditionCatalogue.setActif(true);

			//			selectedTaEditionCatalogueDTO.setCodeCompta(selectedTaEditionCatalogueDTO.getCodeServiceWebExterne()); //ejb
			//			validateUIField(Const.C_CODE_COMPTA,selectedTaEditionCatalogueDTO.getCodeCompta()); //ejb
			//			selectedTaEditionCatalogueDTO.setCompte("411"); //ejb
			//			validateUIField(Const.C_COMPTE,selectedTaEditionCatalogueDTO.getCompte()); //ejb

			//TaTEditionCatalogueDAO daoTTaEditionCatalogue = new TaTEditionCatalogueDAO(getEm());
			TaTEditionCatalogue taTEditionCatalogue;

			taTEditionCatalogue = taTaTEditionCatalogueService.findByCode(codeTaEditionCatalogueDefaut);

			//			if(taTServiceWebExterne!=null && taTServiceWebExterne.getCompteTTaEditionCatalogue()!=null) {
			//				selectedTaEditionCatalogueDTO.setCompte(taTServiceWebExterne.getCompteTTaEditionCatalogue());
			//				this.taTServiceWebExterne = taTServiceWebExterne;
			//			} else {
			//				//selectedTaEditionCatalogueDTO.setCompte(TaEditionCataloguePlugin.getDefault().getPreferenceStore().
			//				//getString(PreferenceConstants.TIERS_COMPTE_COMPTALE_DEFAUT));
			//			}
			//			selectedTaEditionCatalogueDTO.setActifTaEditionCatalogue(true);
			//			//swtTaEditionCatalogue.setTtcTaEditionCatalogue(TaEditionCataloguePlugin.getDefault().getPreferenceStore().
			//			//getBoolean(PreferenceConstants.TIERS_SAISIE_TTC_DEFAUT));
			//			selectedTaEditionCatalogueDTO.setCodeTTvaDoc("F");

			autoCompleteMapDTOtoUI();

			modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
			scrollToTop();
			if(tabViewTaEditionCatalogue!=null) {
				tabViewTaEditionCatalogue.setActiveIndex(0);
				updateTab();
				//				adresseController.setMasterEntity(taTaEditionCatalogue);
				//				adresseController.refresh(null);
			}
			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("TaEditionCatalogue", "actInserer"));
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
			context.addMessage(null, new FacesMessage("TaEditionCatalogue", "actModifier"));
		}
	}
	
	public void onTransfer(TransferEvent event) {
//        StringBuilder builder = new StringBuilder();
//        for(Object item : event.getItems()) {
//            builder.append(((TaActionInterneProg) item).getIdAction()).append("<br />");
//        }
//         
//        FacesMessage msg = new FacesMessage();
//        msg.setSeverity(FacesMessage.SEVERITY_INFO);
//        msg.setSummary("Items Transferred");
//        msg.setDetail(builder.toString());
//         
//        FacesContext.getCurrentInstance().addMessage(null, msg);
    } 
 
    public void onSelect(SelectEvent event) {
//        FacesContext context = FacesContext.getCurrentInstance();
//        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Item Selected", event.getObject().toString()));
    }
     
    public void onUnselect(UnselectEvent event) {
//        FacesContext context = FacesContext.getCurrentInstance();
//        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Item Unselected", event.getObject().toString()));
    }
     
    public void onReorder() {
//        FacesContext context = FacesContext.getCurrentInstance();
//        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "List Reordered", null));
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
			context.addMessage(null, new FacesMessage("TaEditionCatalogue", "actAide"));
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
//			taEditionCatalogue.setFichierBlob(IOUtils.toByteArray(event.getFile().getInputstream()));
//			taEditionCatalogue.setFichierNom(event.getFile().getFileName());
			//			fichier.setBlobFichierMiniature(fichier.resizeImage(IOUtils.toByteArray(event.getFile().getInputstream()), event.getFile().getFileName(), 640, 480));
			
			selectedTaEditionCatalogueDTO.setFichierBlob(IOUtils.toByteArray(event.getFile().getInputStream()));
			selectedTaEditionCatalogueDTO.setFichierNom(event.getFile().getFileName());

//			actEnregistrer(null);
			//refreshLogo();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void handleFileUploadExemple(FileUploadEvent event) {
		FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
		FacesContext.getCurrentInstance().addMessage(null, message);

		try {

			//nouveauBareme.setCheminDoc(event.getFile().getFileName());
//			taEditionCatalogue.setFichierBlob(IOUtils.toByteArray(event.getFile().getInputstream()));
//			taEditionCatalogue.setFichierNom(event.getFile().getFileName());
			//			fichier.setBlobFichierMiniature(fichier.resizeImage(IOUtils.toByteArray(event.getFile().getInputstream()), event.getFile().getFileName(), 640, 480));
			
			selectedTaEditionCatalogueDTO.setFichierBlob(IOUtils.toByteArray(event.getFile().getInputStream()));
			selectedTaEditionCatalogueDTO.setFichierNom(event.getFile().getFileName());

//			actEnregistrer(null);
			//refreshLogo();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void handleFileUploadMiniature(FileUploadEvent event) {
		FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
		FacesContext.getCurrentInstance().addMessage(null, message);

		try {

			//nouveauBareme.setCheminDoc(event.getFile().getFileName());
//			taEditionCatalogue.setFichierBlob(IOUtils.toByteArray(event.getFile().getInputstream()));
//			taEditionCatalogue.setFichierNom(event.getFile().getFileName());
			//			fichier.setBlobFichierMiniature(fichier.resizeImage(IOUtils.toByteArray(event.getFile().getInputstream()), event.getFile().getFileName(), 640, 480));
			
			selectedTaEditionCatalogueDTO.setFichierBlob(IOUtils.toByteArray(event.getFile().getInputStream()));
			selectedTaEditionCatalogueDTO.setFichierNom(event.getFile().getFileName());

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
//			if(taEditionCatalogue!=null && taEditionCatalogue.getFichierBlob()!=null) {
//				InputStream stream = new ByteArrayInputStream(taEditionCatalogue.getFichierBlob()); 
//				//logo = new DefaultStreamedContent(stream, "image/png");
//				logo = new DefaultStreamedContent(stream,null,taEditionCatalogue.getFichierNom());
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	public void supprimerLogo(ActionEvent e) {
		taEditionCatalogue.setFichierBlob(null);
		taEditionCatalogue.setFichierNom(null);
		selectedTaEditionCatalogueDTO.setFichierBlob(null);
		selectedTaEditionCatalogueDTO.setFichierNom(null);
		actEnregistrer(null);
		refreshLogo();
	}

	public void nouveau(ActionEvent actionEvent) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.edition.catalogue");
		b.setTitre("Gestion catalogue edition");
		b.setTemplate("admin_legrain/edition_catalogue.xhtml");
		b.setIdTab(LgrTab.ID_TAB_EDITION_CATALOGUE);
		b.setStyle(LgrTab.CSS_CLASS_TAB_EDITION_CATALOGUE);
		b.setParamId("0");

		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
		actInserer(actionEvent);

		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("TaEditionCatalogue", 
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
		Integer idMessage = (Integer) event.getComponent().getAttributes().get("idTaEditionCatalogue");

		ExternalContext context2 = FacesContext.getCurrentInstance().getExternalContext();
		try {
			context2.redirect(context2.getRequestContextPath() + "/m/tiers.xhtml?idTaEditionCatalogue="+idMessage);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void actSupprimer() {
		actSupprimer(null);
	}

	public void actSupprimer(ActionEvent actionEvent) {
		TaEditionCatalogue taTaEditionCatalogue = new TaEditionCatalogue();
		try {
			if(selectedTaEditionCatalogueDTO!=null && selectedTaEditionCatalogueDTO.getId()!=null){
				taTaEditionCatalogue = taEditionCatalogueService.findById(selectedTaEditionCatalogueDTO.getId());
			}

			taEditionCatalogueService.remove(taTaEditionCatalogue);
			values.remove(selectedTaEditionCatalogueDTO);

			if(!values.isEmpty()) {
				selectedTaEditionCatalogueDTO = values.get(0);
			}else {
				selectedTaEditionCatalogueDTO=new TaEditionCatalogueDTO();
			}
			updateTab();
			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("TaEditionCatalogue", "actSupprimer"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "TaEditionCatalogue", e.getCause().getCause().getCause().getCause().getMessage()));
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
		selectedTaEditionCatalogueDTO=new TaEditionCatalogueDTO();
		selectedTaEditionCatalogueDTOs=new TaEditionCatalogueDTO[]{selectedTaEditionCatalogueDTO};

		//		//gestion du filtre de la liste
		//        RequestContext requestContext = RequestContext.getCurrentInstance();
		//        requestContext.execute("PF('wvDataTableListeTaEditionCatalogue').filter()");


		//		LgrTab typeOngletDejaOuvert = null;
		//		for (LgrTab o : tabsCenter.getOnglets()) {
		//			if(LgrTab.TYPE_TAB_TIERS.equals(o.getTypeOnglet())) {
		//				typeOngletDejaOuvert = o;
		//			}
		//		}
		//		
		//		if(typeOngletDejaOuvert!=null) {
		////			if(!values.isEmpty()) {
		////				selectedTaEditionCatalogueDTO = values.get(0);
		////			}
		//			tabsCenter.supprimerOnglet(typeOngletDejaOuvert);
		//		}
		//		if(ConstWeb.DEBUG) {
		//		FacesContext context = FacesContext.getCurrentInstance();  
		//		context.addMessage(null, new FacesMessage("TaEditionCatalogue", "actFermer"));
		//		}
	}

	public void actImprimer(ActionEvent actionEvent) {
		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("TaEditionCatalogue", "actImprimer"));
		}
		try {

			//		FacesContext facesContext = FacesContext.getCurrentInstance();
			//		HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);

			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			sessionMap.put("tiers", taEditionCatalogueService.findById(selectedTaEditionCatalogueDTO.getId()));

			////////////////////////////////////////////////////////////////////////////////////////
			//Test génération de fichier PDF

			//		HashMap<String,Object> hm = new HashMap<>();
			//		hm.put( "tiers", taEditionCatalogueService.findById(selectedTaEditionCatalogueDTO.getId()));
			//		BirtUtil.setAppContextEditionCatalogue(hm);
			//		
			//		BirtUtil.startReportEngine();
			//		
			////		BirtUtil.renderReportToFile(
			////				"/donnees/Projet/Java/Eclipse/GestionCommerciale_branche_2_0_13_JEE_E46/fr.legrain.bdg.webapp/src/main/webapp/reports/tiers/FicheTaEditionCatalogue.rptdesign", 
			////				"/tmp/tiers.pdf", 
			////				new HashMap<>(), 
			////				BirtUtil.PDF_FORMAT);
			//		
			//		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("/reports/tiers/FicheTaEditionCatalogue.rptdesign");
			//		BirtUtil.renderReportToFile(
			//				inputStream, 
			//				"/tmp/tiers.pdf", 
			//				new HashMap<>(), 
			//				BirtUtil.PDF_FORMAT);
			////////////////////////////////////////////////////////////////////////////////////////
			//java.net.URL.setURLStreamHandlerFactory();
			//		taEditionCatalogueService.generePDF(selectedTaEditionCatalogueDTO.getId());
			////////////////////////////////////////////////////////////////////////////////////////

			//			session.setAttribute("tiers", taEditionCatalogueService.findById(selectedTaEditionCatalogueDTO.getId()));
			System.out.println("TaEditionCatalogueController.actImprimer()");
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
				context.addMessage(null, new FacesMessage("TaEditionCatalogue", 
						"Chargement de l'edition N°"+selectedTaEditionCatalogueDTO.getCodeEdition()
						)); }
		} else {
			tabViews.selectionneOngletCentre(LgrTab.TYPE_TAB_TIERS);
		}
	} 
	public void updateTab(){
		try {

			if(selectedTaEditionCatalogueDTOs!=null && selectedTaEditionCatalogueDTOs.length>0) {
				selectedTaEditionCatalogueDTO = selectedTaEditionCatalogueDTOs[0];
			}
			if(selectedTaEditionCatalogueDTO.getId()!=null && selectedTaEditionCatalogueDTO.getId()!=0) {
				taEditionCatalogue = taEditionCatalogueService.findById(selectedTaEditionCatalogueDTO.getId());
			}
			mapperModelToUI.map(taEditionCatalogue, selectedTaEditionCatalogueDTO);	
			selectedTaEditionCatalogueDTO.setFichierBlob(null);
			selectedTaEditionCatalogueDTO.setFichierBlob(taEditionCatalogue.getFichierBlob());

			autoCompleteMapDTOtoUI();
			refreshLogo();

			List<TaActionInterneProg> l = new ArrayList<TaActionInterneProg>();
			if(taEditionCatalogue!=null){
				for (TaActionInterneProg c : taEditionCatalogue.getActionInterne()) {
					l.add(c);
				}
			}
			dualListModelActionProg.setTarget(l);
			
			dualListModelActionProg.setSource(taActionInterneProgService.selectAll());
			List<TaActionInterneProg> lsuppr = new ArrayList<TaActionInterneProg>();
			for(TaActionInterneProg s : dualListModelActionProg.getSource()) {
				for(TaActionInterneProg t : l) {
					if(t.getId()==s.getId()) {
						lsuppr.add(s);
					}
				}
			}
			dualListModelActionProg.getSource().removeAll(lsuppr);
			
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
		return modeEcran.dataSetEnModif() ||  selectedTaEditionCatalogueDTO.getId()==0;
	}

	public void onRowSelect(SelectEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.edition.catalogue");
		b.setTitre("Gestion catalogue edition");
		b.setTemplate("admin_legrain/edition_catalogue.xhtml");
		b.setIdTab(LgrTab.ID_TAB_EDITION_CATALOGUE);
		b.setStyle(LgrTab.CSS_CLASS_TAB_EDITION_CATALOGUE);
		b.setParamId(LibConversion.integerToString(selectedTaEditionCatalogueDTO.getId()));

		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);


		updateTab();
		scrollToTop();

		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("TaEditionCatalogue", 
					"Chargement de l'édition N°"+selectedTaEditionCatalogueDTO.getCodeEdition()
					)); 
		}
	} 

	public boolean editable() {
		return !modeEcran.dataSetEnModif();
	}

	public void actDialogTypeTaEditionCatalogue(ActionEvent actionEvent) {

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
		//		context.addMessage(null, new FacesMessage("TaEditionCatalogue", "actAbout")); 	    
	}

	public void handleReturnDialogTypeTaEditionCatalogue(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			taTEditionCatalogue = (TaTEditionCatalogue) event.getObject();
			try {
				taTEditionCatalogueDTO = taTaTEditionCatalogueService.findByCodeDTO(taTEditionCatalogue.getCodeTEdition());
			} catch (FinderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void actDialogFamilleTaEditionCatalogue(ActionEvent actionEvent) {

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
		//		context.addMessage(null, new FacesMessage("TaEditionCatalogue", "actAbout")); 	    
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
		//		context.addMessage(null, new FacesMessage("TaEditionCatalogue", "actAbout")); 	    
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
		//		context.addMessage(null, new FacesMessage("TaEditionCatalogue", "actAbout")); 	    
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

	public void validateTaEditionCatalogue(FacesContext context, UIComponent component, Object value) throws ValidatorException {

		String messageComplet = "";

		try {
			//String selectedRadio = (String) value;

			String nomChamp =  (String) component.getAttributes().get("nomChamp");

			//msg = "Mon message d'erreur pour : "+nomChamp;

			//		  validateUIField(nomChamp,value);
			//		  TaEditionCatalogueDTO dtoTemp=new TaEditionCatalogueDTO();
			//		  PropertyUtils.setSimpleProperty(dtoTemp, nomChamp, value);
			//		  taEditionCatalogueService.validateDTOProperty(dtoTemp, nomChamp, ITaEditionCatalogueServiceRemote.validationContext );

			//			//validation automatique via la JSR bean validation
			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
			Set<ConstraintViolation<TaEditionCatalogueDTO>> violations = factory.getValidator().validateValue(TaEditionCatalogueDTO.class,nomChamp,value);
			if (violations.size() > 0) {
				messageComplet = "Erreur de validation : ";
				//				List<IStatus> statusList = new ArrayList<IStatus>();
				for (ConstraintViolation<TaEditionCatalogueDTO> cv : violations) {
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

			//selectedTaEditionCatalogueDTO.setAdresse1Adresse("abcd");

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
			if(value instanceof TaTEditionCatalogueDTO && ((TaTEditionCatalogueDTO) value).getCodeTEdition()!=null && ((TaTEditionCatalogueDTO) value).getCodeTEdition().equals(Const.C_AUCUN))value=null;	

		}

		validateUIField(nomChamp,value);
	}

	public boolean validateUIField(String nomChamp,Object value) {

		boolean changement=false;


		try {				
			if(nomChamp.equals(Const.C_CODE_T_EDITION_CATALOGUE)) {
				TaTEditionCatalogue entity = null;
				if(value!=null){
					if(value instanceof TaTEditionCatalogue){
						entity=(TaTEditionCatalogue) value;
						//								entity = taTServiceWebExterneService.findByCode(entity.getCodeTServiceWebExterne());
					}else if(value instanceof TaTEditionCatalogueDTO){
						entity = taTaTEditionCatalogueService.findByCode(((TaTEditionCatalogueDTO)value).getCodeTEdition());
					}else{
						entity = taTaTEditionCatalogueService.findByCode((String)value);
					}
				}else{
					selectedTaEditionCatalogueDTO.setCodeTEdition("");
					taTEditionCatalogueDTO.setCodeTEdition("");
					taTEditionCatalogue=null;
				}						
				taEditionCatalogue.setTaTEdition(entity);

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
//					selectedTaEditionCatalogueDTO.setCodeTAuthentification(null);
//					taTAuthentificationDTO.setCodeTAuthentification(null);
//					taTAuthentification=null;
//				}
//
//				taEditionCatalogue.setTaTAuthentification(entity);


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
//					selectedTaEditionCatalogueDTO.setCodeTTvaDoc(null);
//				}
//
//				taEditionCatalogue.setTaTTvaDoc(entity);

			}

			return false;

		} catch (Exception e) {

		}
		return false;
	}

	public List<TaTEditionCatalogue> typeTaEditionCatalogueAutoComplete(String query) {
		List<TaTEditionCatalogue> allValues = taTaTEditionCatalogueService.selectAll();
		List<TaTEditionCatalogue> filteredValues = new ArrayList<TaTEditionCatalogue>();

		for (int i = 0; i < allValues.size(); i++) {
			TaTEditionCatalogue civ = allValues.get(i);
			if(query.equals("*") || civ.getCodeTEdition().toLowerCase().contains(query.toLowerCase())) {
				filteredValues.add(civ);
			}
		}

		return filteredValues;
	}
	public List<TaTEditionCatalogueDTO> typeTaEditionCatalogueAutoCompleteLight(String query) {
		List<TaTEditionCatalogueDTO> allValues = taTaTEditionCatalogueService.selectAllDTO();
		List<TaTEditionCatalogueDTO> filteredValues = new ArrayList<TaTEditionCatalogueDTO>();

		for (int i = 0; i < allValues.size(); i++) {
			TaTEditionCatalogueDTO civ = allValues.get(i);
			if(query.equals("*") || civ.getCodeTEdition().toLowerCase().contains(query.toLowerCase())) {
				filteredValues.add(civ);
			}
		}

		return filteredValues;
	}

	public TaTEditionCatalogue getTaTEditionCatalogue() {
		return taTEditionCatalogue;
	}

	public void setTaTEditionCatalogue(TaTEditionCatalogue taTServiceWebExterne) {
		this.taTEditionCatalogue = taTServiceWebExterne;
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

	public void setTaEditionCatalogue(TaEditionCatalogue taTaEditionCatalogue) {
		this.taEditionCatalogue = taTaEditionCatalogue;
	}

	public TaEditionCatalogueDTO rempliDTO(){
		if(taEditionCatalogue!=null){			
			try {
				taEditionCatalogue=taEditionCatalogueService.findByCode(taEditionCatalogue.getCodeEdition());
				mapperModelToUI.map(taEditionCatalogue, selectedTaEditionCatalogueDTO);
				selectedTaEditionCatalogueDTO.setFichierBlob(null);
				selectedTaEditionCatalogueDTO.setFichierBlob(taEditionCatalogue.getFichierBlob());
				if(values.contains(selectedTaEditionCatalogueDTO))values.add(selectedTaEditionCatalogueDTO);
			} catch (FinderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return selectedTaEditionCatalogueDTO;
	}

	public StreamedContent getLogo() {
		if(taEditionCatalogue!=null && taEditionCatalogue.getFichierBlob()!=null) {
			InputStream stream = new ByteArrayInputStream(taEditionCatalogue.getFichierBlob()); 
			//logo = new DefaultStreamedContent(stream, "image/png");
			logo = new DefaultStreamedContent(stream,null,taEditionCatalogue.getFichierNom());
		}
		return logo;
	}

	public void setLogo(StreamedContent logo) {
		this.logo = logo;
	}

	public TaTEditionCatalogueDTO getTaTEditionCatalogueDTO() {
		return taTEditionCatalogueDTO;
	}

	public void setTaTEditionCatalogueDTO(TaTEditionCatalogueDTO taTServiceWebExterneDTO) {
		this.taTEditionCatalogueDTO = taTServiceWebExterneDTO;
	}

	public DualListModel<TaActionInterneProg> getDualListModelActionProg() {
		return dualListModelActionProg;
	}

	public void setDualListModelActionProg(DualListModel<TaActionInterneProg> dualListModelActionProg) {
		this.dualListModelActionProg = dualListModelActionProg;
	}

}  
