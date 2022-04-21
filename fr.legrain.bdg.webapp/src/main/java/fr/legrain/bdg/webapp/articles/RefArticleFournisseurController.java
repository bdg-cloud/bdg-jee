package fr.legrain.bdg.webapp.articles;

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

import fr.legrain.article.dto.TaArticleDTO;
import fr.legrain.article.dto.TaRefArticleFournisseurDTO;
import fr.legrain.article.model.TaArticle;
import fr.legrain.article.model.TaRefArticleFournisseur;
import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.bdg.webapp.ConstWeb;
import fr.legrain.bdg.webapp.app.AbstractController;
import fr.legrain.bdg.webapp.app.LgrTab;
import fr.legrain.bdg.webapp.app.TabListModelBean;
import fr.legrain.bdg.webapp.app.TabViewsBean;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.ModeObjetEcranServeur;
import fr.legrain.tiers.dto.TaTiersDTO;
import fr.legrain.tiers.model.TaTiers;

@Named
@ViewScoped  
public class RefArticleFournisseurController extends AbstractController implements Serializable { 
	


	@Inject @Named(value="tabListModelCenterBean")
	private TabListModelBean tabsCenter;
	
	@Inject @Named(value="tabViewsBean")
	private TabViewsBean tabViews;
	

	
	private String paramId;
	
	private TabView tabViewArticle; //Le Binding sur les onglets "secondaire" semble ne pas fonctionné, les onglets ne sont pas générés au bon moment avec le c:forEach

	private List<TaRefArticleFournisseurDTO> values; 
	private List<TaRefArticleFournisseurDTO> valuesFiltered; 
	
	protected ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();

	private @EJB ITaArticleServiceRemote taArticleService;
	private @EJB ITaTiersServiceRemote taFournisseurService;

	private TaTiers taFournisseur;
	private TaTiersDTO taFournisseurDTO;
	private TaRefArticleFournisseurDTO[] selectedTaRefArticleFournisseurDTOs; 
	private TaRefArticleFournisseur taRefArticleFournisseur = new TaRefArticleFournisseur();
	private TaRefArticleFournisseurDTO newTaRefArticleFournisseurDTO = new TaRefArticleFournisseurDTO();
	private TaRefArticleFournisseurDTO selectedTaRefArticleFournisseurDTO = new TaRefArticleFournisseurDTO();
//	private TaRefArticleFournisseurDTO oldSelectedTaRefArticleFournisseurDTO = new TaRefArticleFournisseurDTO();
	
	
	private LgrDozerMapper<TaRefArticleFournisseurDTO,TaRefArticleFournisseur> mapperUIToModel  = new LgrDozerMapper<TaRefArticleFournisseurDTO,TaRefArticleFournisseur>();
	private LgrDozerMapper<TaRefArticleFournisseur,TaRefArticleFournisseurDTO> mapperModelToUI  = new LgrDozerMapper<TaRefArticleFournisseur,TaRefArticleFournisseurDTO>();
	
	private TaArticle masterEntity;
	
	private TaArticleDTO taArticleDTO;
	private boolean modeDialogue;
	
	public RefArticleFournisseurController() {  
		//values = getValues();
	}  

	@PostConstruct
	public void postConstruct(){

		refresh(null);

	}
	public void refresh(){
		refresh(null);
	}
	
	
//	Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
//	modeEcranDefaut = params.get("modeEcranDefaut");
//	if(modeEcranDefaut!=null && modeEcranDefaut.equals(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION))) {
//		modeEcranDefaut = C_DIALOG;
//		actInserer(null);
//	} else if(modeEcranDefaut!=null && modeEcranDefaut.equals(modeEcran.modeString(EnumModeObjet.C_MO_EDITION))) {
//		modeEcranDefaut = C_DIALOG;
//		if(params.get("idEntity")!=null) {
//			try {
//				selection = taUniteService.findByIdDTO(LibConversion.stringToInteger(params.get("idEntity")));
//			} catch (FinderException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		actModifier(null);
//	} else {
//		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
//	}
	
	public void refresh(ActionEvent ev){
		selectedTaRefArticleFournisseurDTO=null;
		
		Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		if(params.get("modeEcranDefaut")!=null) {
			modeDialogue = true;
			String modeEcranDefaut = params.get("modeEcranDefaut");
			if(modeEcranDefaut!=null && modeEcranDefaut.equals(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION))) {
				actInserer(null);
				
				String codeFrs = params.get("codeFrs");
				String codeBarre = params.get("codeBarre");
				
				selectedTaRefArticleFournisseurDTO.setCodeBarreFournisseur(codeBarre);
				taRefArticleFournisseur.setCodeBarreFournisseur(codeBarre);
				
				selectedTaRefArticleFournisseurDTO.setCodeFournisseur(codeFrs);
				try {
					taRefArticleFournisseur.setTaFournisseur(taFournisseurService.findByCode(codeFrs));
				} catch (FinderException e) {
					e.printStackTrace();
				}
			}
			
			
		} else {
			modeDialogue = false;
		
	   	    if(masterEntity!=null) {
	   	    	try {
					if(masterEntity.getIdArticle()!=0) masterEntity = taArticleService.findById(masterEntity.getIdArticle());
				} catch (FinderException e) {
					
				}
				values = new ArrayList<>();
				for (TaRefArticleFournisseur refArticleFournisseur : masterEntity.getTaRefArticleFournisseurs()) {
					TaRefArticleFournisseurDTO dto=new TaRefArticleFournisseurDTO();
					mapperModelToUI.map(refArticleFournisseur, dto);
					dto.setId(refArticleFournisseur.getIdRefArticleFournisseur());
					dto.setIdArticle(masterEntity.getIdArticle());
					dto.setCodeArticle(masterEntity.getCodeArticle());
					if(refArticleFournisseur.getTaFournisseur()!=null) {
						dto.setIdFournisseur(refArticleFournisseur.getTaFournisseur().getIdTiers());
						dto.setCodeFournisseur(refArticleFournisseur.getTaFournisseur().getCodeTiers());
					}
					taFournisseur=refArticleFournisseur.getTaFournisseur();
					values.add(dto);
				}
	
				if(!values.isEmpty() && selectedTaRefArticleFournisseurDTO==null)
					selectedTaRefArticleFournisseurDTO=values.get(0);
			}
			autoCompleteMapDTOtoUI();
			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
		}
	}

	public List<TaRefArticleFournisseurDTO> getValues(){  
		return values;
	}
	


	public void actAnnuler(ActionEvent actionEvent) {
		try {
			switch (modeEcran.getMode()) {
			case C_MO_INSERTION:
				
				refresh();
				break;
			case C_MO_EDITION:
				if(selectedTaRefArticleFournisseurDTO.getId()!=0){
					taRefArticleFournisseur=rechercheRefArticleFournisseurDansArticle(selectedTaRefArticleFournisseurDTO);
				}				
				break;

			default:
				break;
			}
			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
			mapperModelToUI.map(taRefArticleFournisseur, selectedTaRefArticleFournisseurDTO);

			if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Référence article fournisseur", "actAnnuler"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public void autoCompleteMapUIToDTO() {
		if(taFournisseurDTO!=null) {
			try {
				taFournisseur= taFournisseurService.findById(taFournisseurDTO.getId());
				
			} catch (FinderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void autoCompleteMapDTOtoUI() {
		taFournisseurDTO=null;
		if(selectedTaRefArticleFournisseurDTO!=null && selectedTaRefArticleFournisseurDTO.getCodeFournisseur()!=null && !selectedTaRefArticleFournisseurDTO.getCodeFournisseur().equals("")) {
			List<TaTiersDTO> liste=taFournisseurService.findByCodeLight(selectedTaRefArticleFournisseurDTO.getCodeFournisseur());
			if(liste!=null && !liste.isEmpty()) {
				taFournisseurDTO=liste.get(0);
			}
		}
	}

	public TaRefArticleFournisseur rechercheRefArticleFournisseurDansArticle(TaRefArticleFournisseurDTO dto){
		for (TaRefArticleFournisseur adr : masterEntity.getTaRefArticleFournisseurs()) {
			if(adr.getIdRefArticleFournisseur()==dto.getId()) {
				return adr;
			}
		}
		return null;
	}
	

	

	public TaRefArticleFournisseurDTO rechercheRefArticleFournisseurDansArticleDTO(TaRefArticleFournisseurDTO dto){
		for (TaRefArticleFournisseurDTO ref : values) {
			if(ref.getCodeFournisseur()!=null && ref.getCodeFournisseur().equals(dto.getCodeFournisseur()) &&
					ref.getCodeArticleFournisseur()!=null && ref.getCodeArticleFournisseur().equals(dto.getCodeArticleFournisseur()) && 
							ref.getCodeBarreFournisseur()!=null && ref.getCodeBarreFournisseur().equals(dto.getCodeBarreFournisseur())) {
				return ref;
			}
		}
		return null;
	}
	

	

	
	public void actEnregistrer(ActionEvent actionEvent) {		
		TaRefArticleFournisseurDTO courant =selectedTaRefArticleFournisseurDTO;
		autoCompleteMapUIToDTO();
		if(taFournisseurDTO!=null) {
			taRefArticleFournisseur.setTaFournisseur(taFournisseur);
		}
		mapperUIToModel.map(selectedTaRefArticleFournisseurDTO, taRefArticleFournisseur);
		
		try {
			if(masterEntity!=null) {
				masterEntity.addRefArticleFournisseur(taRefArticleFournisseur);
				taRefArticleFournisseur.setTaArticle(masterEntity);
				
				masterEntity = taArticleService.merge(masterEntity,ITaTiersServiceRemote.validationContext);
				masterEntity = taArticleService.findById(masterEntity.getIdArticle());
				mapperModelToUI.map(taRefArticleFournisseur,selectedTaRefArticleFournisseurDTO);
				
				if(modeDialogue) {
					PrimeFaces.current().dialog().closeDynamic(taRefArticleFournisseur);
				} else {
					refresh();
					if(courant!=null) {
						selectedTaRefArticleFournisseurDTO=rechercheRefArticleFournisseurDansArticleDTO(courant);
					}
					
					if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_INSERTION)==0) {
						values.add(selectedTaRefArticleFournisseurDTO);
					}
					modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
				}
			}

		} catch(Exception e) {
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Référence article fournisseur", e.getMessage()));
		}

		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Référence article fournisseur", "actEnregistrer"));
		}
	}

	public void actInserer(ActionEvent actionEvent) {
		try {
			selectedTaRefArticleFournisseurDTO = new TaRefArticleFournisseurDTO();
			taRefArticleFournisseur = new TaRefArticleFournisseur();



			mapperUIToModel.map(selectedTaRefArticleFournisseurDTO, taRefArticleFournisseur);
			taRefArticleFournisseur.setVersionObj(0);

			autoCompleteMapDTOtoUI();
			
			modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
			scrollToTop();
			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Référence article fournisseur", "actInserer"));
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
		
		try {
			for (TaRefArticleFournisseur adr : masterEntity.getTaRefArticleFournisseurs()) {
				if(adr.getIdRefArticleFournisseur()==selectedTaRefArticleFournisseurDTO.getId()) {
					taRefArticleFournisseur = adr;
				}
			}
		
			modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
	
			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Référence article fournisseur", "actModifier"));
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
		context.addMessage(null, new FacesMessage("Référence article fournisseur", "actAide"));
		}
	}
	
//	public void actAideRetour(ActionEvent actionEvent) {
//		
//	}
	   
    public void nouveau(ActionEvent actionEvent) {  
    	LgrTab b = new LgrTab();
    	b.setTypeOnglet(LgrTab.TYPE_TAB_REF_FOURNISSEUR_ARTICLE);
		b.setTitre("Tiers");
		b.setStyle(LgrTab.CSS_CLASS_TAB_REF_ARTICLE_FOURNISSEUR);
		b.setTemplate("article/ref_article_fournisseur_include.xhtml");
		b.setIdTab(LgrTab.ID_TAB_REF_ARTICLE_FOURNISSEUR);
		b.setParamId("0");
		
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
		actInserer(actionEvent);
		
		if(ConstWeb.DEBUG) {
    	FacesContext context = FacesContext.getCurrentInstance();  
	    context.addMessage(null, new FacesMessage("Référence article fournisseur", 
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
		TaRefArticleFournisseur taRefArticleFournisseur = new TaRefArticleFournisseur();
		try {
			if(selectedTaRefArticleFournisseurDTO.getId()!=0){
				taRefArticleFournisseur = rechercheRefArticleFournisseurDansArticle(selectedTaRefArticleFournisseurDTO);
			}
			
			masterEntity.removeRefArticleFournisseur(taRefArticleFournisseur);
			masterEntity = taArticleService.merge(masterEntity,ITaTiersServiceRemote.validationContext);
			masterEntity = taArticleService.findById(masterEntity.getIdArticle());
			selectedTaRefArticleFournisseurDTO=null;
			refresh();
			

			updateTab();
			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Référence article fournisseur", "actSupprimer"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Référence article fournisseur", e.getCause().getCause().getCause().getCause().getMessage()));
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
		selectedTaRefArticleFournisseurDTO=new TaRefArticleFournisseurDTO();
		selectedTaRefArticleFournisseurDTOs=new TaRefArticleFournisseurDTO[]{selectedTaRefArticleFournisseurDTO};
	
	}

	public void actImprimer(ActionEvent actionEvent) {
		if(ConstWeb.DEBUG) {
		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage("Référence article fournisseur", "actImprimer"));
		}
		try {
			
//		FacesContext facesContext = FacesContext.getCurrentInstance();
//		HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
		
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		sessionMap.put("tiers", taArticleService.findById(selectedTaRefArticleFournisseurDTO.getId()));
		
		////////////////////////////////////////////////////////////////////////////////////////
		//Test génération de fichier PDF
		
//		HashMap<String,Object> hm = new HashMap<>();
//		hm.put( "tiers", taTiersService.findById(selectedTaRefArticleFournisseurDTO.getId()));
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
//		taTiersService.generePDF(selectedTaRefArticleFournisseurDTO.getId());
		////////////////////////////////////////////////////////////////////////////////////////
		
//			session.setAttribute("tiers", taTiersService.findById(selectedTaRefArticleFournisseurDTO.getId()));
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
		b.setTypeOnglet(LgrTab.TYPE_TAB_REF_FOURNISSEUR_ARTICLE);
		return tabsCenter.ongletDejaOuvert(b)==null;
	} 
	
	public void onRowSimpleSelect(SelectEvent event) {
		
		if(pasDejaOuvert()==false){
			onRowSelect(event);
			
			autoCompleteMapDTOtoUI();

		} else {
			tabViews.selectionneOngletCentre(LgrTab.TYPE_TAB_REF_FOURNISSEUR_ARTICLE);
		}
	} 
	public void updateTab(){
		try {

		autoCompleteMapDTOtoUI();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void onRowSelect(SelectEvent event) {  
		
		updateTab();
		

	} 
	
	public boolean editable() {
		return !modeEcran.dataSetEnModif();
	}
	
	public void actDialogTypeCompteBanque(ActionEvent actionEvent) {
		
  
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
        
        PrimeFaces.current().dialog().openDynamic("tiers/dialog_type_banque", options, params);
 	    
	}
	


	
	 public boolean etatBouton(String bouton) {
		boolean retour = true;
		switch (modeEcran.getMode()) {
		case C_MO_INSERTION:
			switch (bouton) {
			case "enregistrer":
				if(!selectedTaRefArticleFournisseurDTO.estVide())
					retour = false;
				break;
				case "annuler":
				case "fermer":
					retour = false;
					break;
				default:
					break;
			}
			break;
		case C_MO_EDITION:
			switch (bouton) {
			case "enregistrer":
				if(!selectedTaRefArticleFournisseurDTO.estVide())
					retour = false;
				break;
			case "annuler":
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
				if(selectedTaRefArticleFournisseurDTO!=null && selectedTaRefArticleFournisseurDTO.getId()!=0 ) retour = false;
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

	public void validateCompteBanque(FacesContext context, UIComponent component, Object value) throws ValidatorException {
	
		String messageComplet = "";
		
		try {
		 
		  String nomChamp =  (String) component.getAttributes().get("nomChamp");
		  		  
//			//validation automatique via la JSR bean validation
		  ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		  Set<ConstraintViolation<TaRefArticleFournisseurDTO>> violations = factory.getValidator().validateValue(TaRefArticleFournisseurDTO.class,nomChamp,value);
			if (violations.size() > 0) {
				messageComplet = "Erreur de validation : ";

				for (ConstraintViolation<TaRefArticleFournisseurDTO> cv : violations) {
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
		
		String nomChamp =  (String) event.getComponent().getAttributes().get("nomChamp");

		if(value!=null) {
			if(value instanceof String && value.equals(Const.C_AUCUN))value="";	
		}
		
		validateUIField(nomChamp,value);
	}
	
	public boolean validateUIField(String nomChamp,Object value) {
		
		boolean changement=false;
		
		try {				
			if(nomChamp.equals(Const.C_CODE_TIERS)) {
				TaTiersDTO entity =null;
				if(value!=null){
					if(value instanceof TaTiersDTO){
						selectedTaRefArticleFournisseurDTO.setCodeFournisseur(((TaTiersDTO) value).getCodeTiers());
					}
				}else{
					selectedTaRefArticleFournisseurDTO.setCodeFournisseur(null);
				}
			}else if(nomChamp.equals(Const.C_CODE_BARRE)) {
			}else if(nomChamp.equals(Const.C_CODE_ARTICLE)) {
				if(modeDialogue) {
					if(value!=null){
						if(value instanceof TaArticleDTO){
							masterEntity = taArticleService.findByCode(((TaArticleDTO) value).getCodeArticle());
							selectedTaRefArticleFournisseurDTO.setCodeArticle(((TaArticleDTO) value).getCodeArticle());
							taRefArticleFournisseur.setTaArticle(masterEntity);
						}
					}
				}
			}else if(nomChamp.equals(Const.C_Description)) {
			}			

			return false;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	

	
	public List<TaTiersDTO> fournisseurAutoCompleteLight(String query) {
        List<TaTiersDTO> allValues = taFournisseurService.findAllLight();
        List<TaTiersDTO> filteredValues = new ArrayList<TaTiersDTO>();
         
        for (int i = 0; i < allValues.size(); i++) {
        	TaTiersDTO civ = allValues.get(i);
            if(query.equals("*") || civ.getCodeTiers().toLowerCase().contains(query.toLowerCase())) {
                filteredValues.add(civ);
            }
        }         
        return filteredValues;
    }
	
	public List<TaArticleDTO> articleAutoCompleteDTOLight(String query) {
		List<TaArticleDTO> allValues = taArticleService.findByCodeLight(query.toUpperCase());
		List<TaArticleDTO> filteredValues = new ArrayList<TaArticleDTO>();

		for (int i = 0; i < allValues.size(); i++) {
			TaArticleDTO civ = allValues.get(i);
			if(query.equals("*") || civ.getCodeArticle().toUpperCase().contains(query.toUpperCase())) {
				filteredValues.add(civ);
			}
		}

		return filteredValues;
	}
	
	
	public TaRefArticleFournisseurDTO[] getSelectedTaRefArticleFournisseurDTOs() {
		return selectedTaRefArticleFournisseurDTOs;
	}

	public void setSelectedTaRefArticleFournisseurDTOs(TaRefArticleFournisseurDTO[] selectedTaRefArticleFournisseurDTOs) {
		this.selectedTaRefArticleFournisseurDTOs = selectedTaRefArticleFournisseurDTOs;
	}

	public TaRefArticleFournisseurDTO getNewTaRefArticleFournisseurDTO() {
		return newTaRefArticleFournisseurDTO;
	}

	public void setNewTaRefArticleFournisseurDTO(TaRefArticleFournisseurDTO newTaRefArticleFournisseurDTO) {
		this.newTaRefArticleFournisseurDTO = newTaRefArticleFournisseurDTO;
	}

	public TaRefArticleFournisseurDTO getSelectedTaRefArticleFournisseurDTO() {
		return selectedTaRefArticleFournisseurDTO;
	}

	public void setSelectedTaRefArticleFournisseurDTO(TaRefArticleFournisseurDTO selectedTaRefArticleFournisseurDTO) {
		this.selectedTaRefArticleFournisseurDTO = selectedTaRefArticleFournisseurDTO;
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
	
	public TaRefArticleFournisseur getTaTiers() {
		return taRefArticleFournisseur;
	}

	public void setTaTiers(TaRefArticleFournisseur taCompteBanque) {
		this.taRefArticleFournisseur = taCompteBanque;
	}

	public TabView getTabViewArticle() {
		return tabViewArticle;
	}

	public void setTabViewArticle(TabView tabViewArticle) {
		this.tabViewArticle = tabViewArticle;
	}

	public TaArticle getMasterEntity() {
		return masterEntity;
	}

	public void setMasterEntity(TaArticle masterEntity) {
		this.masterEntity = masterEntity;
	}


	public List<TaRefArticleFournisseurDTO> getValuesFiltered() {
		return valuesFiltered;
	}

	public void setValuesFiltered(List<TaRefArticleFournisseurDTO> valuesFiltered) {
		this.valuesFiltered = valuesFiltered;
	}

	public TaTiers getTaFournisseur() {
		return taFournisseur;
	}

	public void setTaFournisseur(TaTiers taFournisseur) {
		this.taFournisseur = taFournisseur;
	}

	public TaTiersDTO getTaFournisseurDTO() {
		return taFournisseurDTO;
	}

	public void setTaFournisseurDTO(TaTiersDTO taFournisseurDTO) {
		this.taFournisseurDTO = taFournisseurDTO;
	}

	public TaArticleDTO getTaArticleDTO() {
		return taArticleDTO;
	}

	public void setTaArticleDTO(TaArticleDTO taArticleDTO) {
		this.taArticleDTO = taArticleDTO;
	}

	public boolean isModeDialogue() {
		return modeDialogue;
	}

	public void setModeDialogue(boolean modeDialogue) {
		this.modeDialogue = modeDialogue;
	}


}  
