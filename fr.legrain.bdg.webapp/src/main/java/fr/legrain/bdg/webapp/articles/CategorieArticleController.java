package fr.legrain.bdg.webapp.articles;

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
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.validator.ValidatorException;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.apache.commons.io.IOUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import fr.legrain.article.dto.TaCategorieArticleDTO;
import fr.legrain.article.dto.TaImageArticleDTO;
import fr.legrain.article.model.TaCategorieArticle;
import fr.legrain.article.model.TaImageArticle;
import fr.legrain.bdg.article.service.remote.ITaCategorieArticleServiceRemote;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.preferences.divers.ConstPreferencesArticles;
import fr.legrain.bdg.tiers.service.remote.ITaTCiviliteServiceRemote;
import fr.legrain.bdg.webapp.ConstWeb;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.LibChaine;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.ModeObjetEcranServeur;


@Named
@ViewScoped 
public class CategorieArticleController implements Serializable {

	private List<TaCategorieArticleDTO> values; 
	private List<TaCategorieArticleDTO> filteredValues;
	private TaCategorieArticleDTO nouveau ;
	private TaCategorieArticleDTO selection ;
	
	private StreamedContent logo;
	
	private TaCategorieArticle taCategorieArticle = new TaCategorieArticle();

	private String modeEcranDefaut;
	private static final String C_DIALOG = "dialog";

	private ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();

	private @EJB ITaCategorieArticleServiceRemote taCategorieArticleService;
	
	private LgrDozerMapper<TaCategorieArticleDTO,TaCategorieArticle> mapperUIToModel  = new LgrDozerMapper<TaCategorieArticleDTO,TaCategorieArticle>();
	private LgrDozerMapper<TaCategorieArticle,TaCategorieArticleDTO> mapperModelToUI  = new LgrDozerMapper<TaCategorieArticle,TaCategorieArticleDTO>();
	
	@PostConstruct
	public void postConstruct(){
		try {
			refresh();
			if(values != null && !values.isEmpty()){
				selection = values.get(0);	
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.setFilteredValues(values);
	}

	public CategorieArticleController() {} 

	public void actInserer(ActionEvent actionEvent){
		nouveau = new TaCategorieArticleDTO();
		modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
		nouveau.setCodeCategorieArticle(taCategorieArticleService.genereCode(null));
	}

	public void actModifier(ActionEvent actionEvent){
		nouveau = selection;
		modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
		refresLogo();
	}

	public void actSupprimer(){
		TaCategorieArticle taCategorieArticle = new TaCategorieArticle();
		try {
			if(selection!=null && selection.getId()!=null){
				taCategorieArticle = taCategorieArticleService.findById(selection.getId());
			}

			taCategorieArticleService.remove(taCategorieArticle);
			values.remove(selection);
			
			if(!values.isEmpty()) {
				selection = values.get(0);
			}else {
				selection=new TaCategorieArticleDTO();
			}

			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Catégorie article", "actSupprimer"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Catégorie article", e.getCause().getCause().getCause().getCause().getMessage()));
		}
	}
	
	public void actSupprimerImage(ActionEvent actionEvent){
		nouveau.setBlobImageOrigine(null);
		refresLogo();
	}

	public void actEnregistrer(ActionEvent actionEvent){
		try {
			TaCategorieArticleDTO retour = null;
			taCategorieArticle=new TaCategorieArticle();
			if(nouveau.getId()==null || taCategorieArticleService.findById(nouveau.getId()) == null){
				mapperUIToModel.map(nouveau, taCategorieArticle);
				
				if(LibChaine.empty(taCategorieArticle.getUrlRewritingCategorieArticle())) {
					taCategorieArticle.setUrlRewritingCategorieArticle(LibChaine.toUrlRewriting((String)taCategorieArticle.getLibelleCategorieArticle()));
				}
				taCategorieArticle.setBlobImageOrigine(nouveau.getBlobImageOrigine()); // le mapper dozer ne fait pas les blob/byte[]
				taCategorieArticle = taCategorieArticleService.merge(taCategorieArticle, ITaTCiviliteServiceRemote.validationContext);
				mapperModelToUI.map(taCategorieArticle, nouveau);
				values= taCategorieArticleService.selectAllDTO();
				nouveau = values.get(0);
				nouveau = new TaCategorieArticleDTO();

				modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

			}else{
				if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_EDITION)==0){
					taCategorieArticle = taCategorieArticleService.findById(nouveau.getId());
					mapperUIToModel.map(nouveau, taCategorieArticle);
					if(LibChaine.empty(taCategorieArticle.getUrlRewritingCategorieArticle())) {
						taCategorieArticle.setUrlRewritingCategorieArticle(LibChaine.toUrlRewriting((String)taCategorieArticle.getLibelleCategorieArticle()));
					}
					taCategorieArticle.setBlobImageOrigine(nouveau.getBlobImageOrigine()); // le mapper dozer ne fait pas les blob/byte[]
					taCategorieArticle = taCategorieArticleService.merge(taCategorieArticle, ITaTCiviliteServiceRemote.validationContext);
					mapperModelToUI.map(taCategorieArticle, nouveau);
					values= taCategorieArticleService.selectAllDTO();
					//refresh();
					nouveau = values.get(0);
					nouveau = new TaCategorieArticleDTO();

					modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
				}
				else{
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "Code déja utilisé"));
				}
			}
			if(modeEcranDefaut!=null && modeEcranDefaut.equals(C_DIALOG)) {
				PrimeFaces.current().dialog().closeDynamic(taCategorieArticle);
			}
		} catch(Exception e) {
			e=ThrowableExceptionLgr.renvoieCauseRuntimeException(e);
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Catégorie article", e.getMessage()));
		}
	}
	
	public void actAnnuler(ActionEvent actionEvent){

		if(values.size()>= 1){
			selection = values.get(0);
		}		
		nouveau = new TaCategorieArticleDTO();

		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}

	public List<TaCategorieArticleDTO> getValues(){  
		return values;
	}

	public TaCategorieArticleDTO getNouveau() {
		return nouveau;
	}

	public void setNouveau(TaCategorieArticleDTO newTaCategorieArticle) {
		this.nouveau = newTaCategorieArticle;
	}

	public TaCategorieArticleDTO getSelection() {
		return selection;
	}

	public void setSelection(TaCategorieArticleDTO selectedTaCategorieArticle) {
		this.selection = selectedTaCategorieArticle;
	}

	public void setValues(List<TaCategorieArticleDTO> values) {
		this.values = values;
	}

	public void onRowSelect(SelectEvent event) {  
		if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_CONSULTATION)==0) {
			nouveau = selection;
		}
	}

	public List<TaCategorieArticleDTO> getFilteredValues() {
		return filteredValues;
	}

	public void setFilteredValues(List<TaCategorieArticleDTO> filteredValues) {
		this.filteredValues = filteredValues;
	}

	public void refresh(){
		values = taCategorieArticleService.selectAllDTO();
		Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		modeEcranDefaut = params.get("modeEcranDefaut");
		if(modeEcranDefaut!=null && modeEcranDefaut.equals(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION))) {
			modeEcranDefaut = C_DIALOG;
			actInserer(null);
		} else if(modeEcranDefaut!=null && modeEcranDefaut.equals(modeEcran.modeString(EnumModeObjet.C_MO_EDITION))) {
			modeEcranDefaut = C_DIALOG;
			if(params.get("idEntity")!=null) {
				try {
					selection = taCategorieArticleService.findByIdDTO(LibConversion.stringToInteger(params.get("idEntity")));
				} catch (FinderException e) {
					e.printStackTrace();
				}
			}
			actModifier(null);
		} else {
			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
		}
	}
	
	public void actFermerDialog(ActionEvent actionEvent) {
		PrimeFaces.current().dialog().closeDynamic(null);
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
			case "supprimer":
			case "modifier":
			case "inserer":
			case "imprimer":
			case "fermer":
				retour = false;
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

	public ModeObjetEcranServeur getModeEcran() {
		return modeEcran;
	}

	public String getModeEcranDefaut() {
		return modeEcranDefaut;
	}

	public void setModeEcranDefaut(String modeEcranDefaut) {
		this.modeEcranDefaut = modeEcranDefaut;
	}
	
	public void handleFileUpload(FileUploadEvent event) {
        FacesMessage message = new FacesMessage("Succés!", event.getFile().getFileName() + " a été chargé.");
        FacesContext.getCurrentInstance().addMessage(null, message);
        
//        int idImage = (int) event.getComponent().getAttributes().get("idImage"); 
		
//		if(idImage==0) {
//			taImageArticle = new TaImageArticle();
//			nouveau = new TaImageArticleDTO();
//			nouveau.setPosition(nbImage+1);
//		}
//        FacesMessage message = new FacesMessage("Succés!", event.getFile().getFileName() + " a été chargé.");
        
//        fichier = new TaFichier();
        
        try {
//			fichier.setBlobFichierOriginal(IOUtils.toByteArray(event.getFile().getInputstream()));
//			fichier.setBlobFichierMiniature(fichier.resizeImage(IOUtils.toByteArray(event.getFile().getInputstream()), event.getFile().getFileName(), 640, 480));
        	taCategorieArticle.setBlobImageOrigine(IOUtils.toByteArray(event.getFile().getInputStream()));
        	//nouveau.setBlobImageOrigine(IOUtils.toByteArray(event.getFile().getInputStream()));
        	nouveau.setBlobImageOrigine(taCategorieArticle.getBlobImageOrigine());
        	
        	
        	mapperUIToModel.map(nouveau, taCategorieArticle);
//			taImageArticle.setTaArticle(masterEntity);
//			taImageArticle = taImageArticleService.merge(taImageArticle, ITaTCiviliteServiceRemote.validationContext);
//			mapperModelToUI.map(taCategorieArticle, nouveau);
//			values= taImageArticleService.findByArticleLight(masterEntity.getIdArticle());
//			completeGrille();
//			nouveau = values.get(0);
//			nouveau = new TaImageArticleDTO();
        	
        	refresLogo();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	public void refresLogo() {
		try {
			logo = null;
			if(nouveau!=null && nouveau.getBlobImageOrigine()!=null) {
				InputStream stream = new ByteArrayInputStream(nouveau.getBlobImageOrigine()); 
				//logo = new DefaultStreamedContent(stream, "image/png");
				logo = new DefaultStreamedContent(stream);
				logo = DefaultStreamedContent.builder()
                //.contentType("image/png")
                .stream(() -> {
                    try {
//                        JFreeChart jfreechart = ChartFactory.createPieChart("Cities", createDataset(), true, true, false);
//                        File chartFile = new File("dynamichart");
//                        ChartUtilities.saveChartAsPNG(chartFile, jfreechart, 375, 300);
//                        return new FileInputStream(chartFile);
                        return stream;
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .build();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Dima - Début
		public void validateObject(FacesContext context, UIComponent component, Object value) throws ValidatorException {
//			String msg = "";
//			try {
//				String nomChamp =  (String) component.getAttributes().get("nomChamp");
//				validateUIField(nomChamp,value);
//				TaCategorieArticleDTO temp=new TaCategorieArticleDTO();
//				PropertyUtils.setProperty(temp, nomChamp, value);
//				taCategorieArticleService.validateEntityProperty(temp, nomChamp, ITaCategorieArticleServiceRemote.validationContext );
//			} catch(Exception e) {
//				msg += e.getMessage();
//				throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
//			}
			
			String messageComplet = "";
			try {
				String nomChamp =  (String) component.getAttributes().get("nomChamp");
				//validation automatique via la JSR bean validation
				ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
				Set<ConstraintViolation<TaCategorieArticleDTO>> violations = factory.getValidator().validateValue(TaCategorieArticleDTO.class,nomChamp,value);
				if (violations.size() > 0) {
					messageComplet = "Erreur de validation : ";
					for (ConstraintViolation<TaCategorieArticleDTO> cv : violations) {
						messageComplet+=" "+cv.getMessage()+"\n";
					}
					throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, messageComplet, messageComplet));
				} else {
					//aucune erreur de validation "automatique", on déclanche les validations du controller
					validateUIField(nomChamp,value);
				}
			} catch(Exception e) {
				//messageComplet += e.getMessage();
				throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, messageComplet, messageComplet));
			}
		}

		public boolean validateUIField(String nomChamp,Object value) {
			try {
				if(nomChamp.equals(Const.C_TYPE_CODE_FAMILLE_UNITE)) {
						boolean changement=false;
						if(selection.getCodeCategorieArticle()!=null && value!=null && !selection.getCodeCategorieArticle().equals(""))
						{
							if(value instanceof TaCategorieArticle)
								changement=((TaCategorieArticle) value).getCodeCategorieArticle().equals(selection.getCodeCategorieArticle());
							else if(value instanceof String)
							changement=!value.equals(selection.getCodeCategorieArticle());
						}
						if(changement && modeEcran.dataSetEnModeModification()){
							FacesContext context = FacesContext.getCurrentInstance();  
							context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Catégorie d'article", Const.C_MESSAGE_CHANGEMENT_CODE));
						}
					}
				return false;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}

		public void actDialogTypes(ActionEvent actionEvent) {
		    
	        Map<String,Object> options = new HashMap<String, Object>();
	        options.put("modal", true);
	        options.put("draggable", true);
	        options.put("resizable", false);
	        options.put("contentHeight", 620);
	        options.put("modal", true);
	        
	        Map<String,List<String>> params = new HashMap<String,List<String>>();
	        List<String> list = new ArrayList<String>();
	        list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
	        params.put("modeEcranDefaut", list);
	        
	        PrimeFaces.current().dialog().openDynamic("articles/dialog_type_categorie", options, params);
	        
		}
		
		public void handleReturnDialogTypes(SelectEvent event) {
			if(event!=null && event.getObject()!=null) {
				taCategorieArticle = (TaCategorieArticle) event.getObject();
				
			}
			refresh();
		}
		
		public void actDialogModifier(ActionEvent actionEvent){
			
			nouveau = selection;
//			modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
			
			Map<String,Object> options = new HashMap<String, Object>();
	        options.put("modal", true);
	        options.put("draggable", true);
	        options.put("resizable", false);
	        options.put("contentHeight", 620);
	        options.put("modal", true);
	        
	        Map<String,List<String>> params = new HashMap<String,List<String>>();
	        List<String> list = new ArrayList<String>();
	        list.add(modeEcran.modeString(EnumModeObjet.C_MO_EDITION));
	        params.put("modeEcranDefaut", list);
	        List<String> list2 = new ArrayList<String>();
	        list2.add(LibConversion.integerToString(selection.getId()));
	        params.put("idEntity", list2);
	        
	        PrimeFaces.current().dialog().openDynamic("articles/dialog_type_categorie", options, params);

		}
		// Dima -  Fin

		public StreamedContent getLogo() {
			return logo;
		}

		public void setLogo(StreamedContent logo) {
			this.logo = logo;
		}
}
