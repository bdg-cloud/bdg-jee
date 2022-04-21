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
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.PhaseId;
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

import fr.legrain.article.dto.TaLabelArticleDTO;
import fr.legrain.article.model.TaLabelArticle;
import fr.legrain.bdg.article.service.remote.ITaLabelArticleServiceRemote;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.tiers.service.remote.ITaTCiviliteServiceRemote;
import fr.legrain.bdg.webapp.ConstWeb;
import fr.legrain.general.model.TaFichier;
import fr.legrain.general.service.remote.ITaFichierServiceRemote;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.ModeObjetEcranServeur;


@Named
@ViewScoped
//@SessionScoped 
/*
 * Session scope pour pouvoir afficher les logos qui viennent des blobs en dynamique dans le datatable, 
 * je ne comprends pas pourquoi pour l'instant, 
 * je ne pense pas que se soir la bonne solution, 
 * si on peut enfin utiliser omnifaces > 2.0 on pourra utilisé le o:graphicImage avec les byte[] directement sans objet StreamedContent
 */
public class LabelArticleController implements Serializable {

	private List<TaLabelArticleDTO> values;
	private List<TaLabelArticleDTO> filteredValues;
	private TaLabelArticleDTO nouveau ;
	private TaLabelArticleDTO selection ;

	private String modeEcranDefaut;
	private static final String C_DIALOG = "dialog";
	private TaFichier fichier;
	private int idLabelFichier;
	private FileUploadEvent eventFichier;
	
	private TaLabelArticle taLabelArticle = new TaLabelArticle();
	
	private StreamedContent logo;
	private Map<Integer,StreamedContent> mapLogos;

	private ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();

	private @EJB ITaLabelArticleServiceRemote taLabelArticleService;
	private @EJB ITaFichierServiceRemote taFichierService;
	
	private LgrDozerMapper<TaLabelArticleDTO,TaLabelArticle> mapperUIToModel  = new LgrDozerMapper<TaLabelArticleDTO,TaLabelArticle>();
	private LgrDozerMapper<TaLabelArticle,TaLabelArticleDTO> mapperModelToUI  = new LgrDozerMapper<TaLabelArticle,TaLabelArticleDTO>();
	
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
	
	public void refresLogo() {
		try {
			logo = null;
			if(nouveau!=null && nouveau.getBlobLogo()!=null) {
				InputStream stream = new ByteArrayInputStream(nouveau.getBlobLogo()); 
				//logo = new DefaultStreamedContent(stream, "image/png");
				logo = new DefaultStreamedContent(stream);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public LabelArticleController() {  
	}  

	public void actInserer(ActionEvent actionEvent){
		nouveau = new TaLabelArticleDTO();
		modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
	}

	public void actModifier(ActionEvent actionEvent){
		nouveau = selection;
		modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
		refresLogo();
	}
	
	public StreamedContent getImg(){
		FacesContext context = FacesContext.getCurrentInstance();
	
		String id = context.getExternalContext().getRequestParameterMap().get("id");
		if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            return new DefaultStreamedContent();
        }
        else {
        	return mapLogos.get(LibConversion.stringToInteger(id));
        }
	}

	public void actSupprimer(){
		TaLabelArticle taLabelArticle = new TaLabelArticle();
		try {
			if(selection!=null && selection.getId()!=null){
				taLabelArticle = taLabelArticleService.findById(selection.getId());
			}

			taLabelArticleService.remove(taLabelArticle);
			values.remove(selection);
			
			if(!values.isEmpty()) {
				selection = values.get(0);
			}else {
				selection=new TaLabelArticleDTO();
			}

			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Label article", "actSupprimer"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Label article", e.getCause().getCause().getCause().getCause().getMessage()));
		}
	}
		
//		/** Insertion d'un fichier dans la BdDs **/
//		fichier = new TaFichier();
//		fichier.fichierVersBinaire("C:\\test\\in\\logo-legrain.jpg");
//		fichier.setTypeFichierOriginal(1);
//		fichier.setTypeFichierMiniature(1);
//		taFichierService.merge(fichier, ITaFichierServiceRemote.validationContext);
//		System.out.println("!!!!!!!!!!!!!! encodage de \"<"+fichier.getNomFichierOriginal()+">\" est reussi <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		
//		/** Extraction d'un fichier de la BdDs **/
//		fichier = new TaFichier();
//		try {
//			fichier = taFichierService.findById(2);
//		} catch (FinderException e) {
//			e.printStackTrace();
//		}
//		fichier.binaireVersFichier("C:\\test\\out\\");

	public void actEnregistrer(ActionEvent actionEvent){
		try {
			TaLabelArticleDTO retour = null;
			taLabelArticle=new TaLabelArticle();
			if(nouveau.getId()==null || taLabelArticleService.findById(nouveau.getId()) == null){
				mapperUIToModel.map(nouveau, taLabelArticle);
				taLabelArticle = taLabelArticleService.merge(taLabelArticle, ITaTCiviliteServiceRemote.validationContext);
				mapperModelToUI.map(taLabelArticle, nouveau);
				values= taLabelArticleService.selectAllDTO();
				nouveau = values.get(0);
				nouveau = new TaLabelArticleDTO();

				modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

			}else{
				if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_EDITION)==0){
					taLabelArticle = taLabelArticleService.findById(nouveau.getId());
					mapperUIToModel.map(nouveau, taLabelArticle);
					taLabelArticle = taLabelArticleService.merge(taLabelArticle, ITaTCiviliteServiceRemote.validationContext);
					mapperModelToUI.map(taLabelArticle, nouveau);
					values= taLabelArticleService.selectAllDTO();
					nouveau = values.get(0);
					nouveau = new TaLabelArticleDTO();

					modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
				}
				else{
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "Code déja utilisé"));
				}
			}
			if(modeEcranDefaut!=null && modeEcranDefaut.equals(C_DIALOG)) {
				PrimeFaces.current().dialog().closeDynamic(taLabelArticle);
			}
			refresLogo();
		} catch(Exception e) {
			e=ThrowableExceptionLgr.renvoieCauseRuntimeException(e);
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			//context.addMessage(null, new FacesMessage("Tiers", "actEnregistrer")); 
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "TypeLabelArticle", e.getMessage()));
		}
	}
	
	public void actAnnuler(ActionEvent actionEvent){

		if(values.size()>= 1){
			selection = values.get(0);
		}		
		nouveau = new TaLabelArticleDTO();

		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}

	public List<TaLabelArticleDTO> getValues(){  
		return values;
	}

	public TaLabelArticleDTO getNouveau() {
		return nouveau;
	}

	public void setNouveau(TaLabelArticleDTO newTaLabelArticle) {
		this.nouveau = newTaLabelArticle;
	}

	public TaLabelArticleDTO getSelection() {
		return selection;
	}

	public void setSelection(TaLabelArticleDTO selectedTaLabelArticle) {
		this.selection = selectedTaLabelArticle;
	}

	public void setValues(List<TaLabelArticleDTO> values) {
		this.values = values;
	}

	public void onRowSelect(SelectEvent event) {  
		if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_CONSULTATION)==0) {
			nouveau = selection;
			refresLogo();
		}
	}

	public List<TaLabelArticleDTO> getFilteredValues() {
		return filteredValues;
	}

	public void setFilteredValues(List<TaLabelArticleDTO> filteredValues) {
		this.filteredValues = filteredValues;
	}

	public void refresh(){
		values = taLabelArticleService.selectAllDTO();
		Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		modeEcranDefaut = params.get("modeEcranDefaut");
		if(modeEcranDefaut!=null && modeEcranDefaut.equals(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION))) {
			modeEcranDefaut = C_DIALOG;
			actInserer(null);
		} else if(modeEcranDefaut!=null && modeEcranDefaut.equals(modeEcran.modeString(EnumModeObjet.C_MO_EDITION))) {
			modeEcranDefaut = C_DIALOG;
			if(params.get("idEntity")!=null) {
				try {
					selection = taLabelArticleService.findByIdDTO(LibConversion.stringToInteger(params.get("idEntity")));
				} catch (FinderException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			actModifier(null);
		} else {
			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
		}
		
		
		mapLogos = new HashMap<>();
		for (TaLabelArticleDTO taLabelArticle : values) {
			mapLogos.put(taLabelArticle.getId(), taLabelArticle.getBlobLogo()!=null ? new DefaultStreamedContent(new ByteArrayInputStream(taLabelArticle.getBlobLogo())):null);
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

		System.out.println("Event message : getSize() - "+event.getFile().getSize());
		
        FacesMessage message = new FacesMessage("Succés!", event.getFile().getFileName() + " a été chargé.");
        
//        fichier = new TaFichier();
        
        try {
//			fichier.setBlobFichierOriginal(IOUtils.toByteArray(event.getFile().getInputstream()));
//			fichier.setBlobFichierMiniature(fichier.resizeImage(IOUtils.toByteArray(event.getFile().getInputstream()), event.getFile().getFileName(), 640, 480));
        	taLabelArticle.setBlobLogo(IOUtils.toByteArray(event.getFile().getInputStream()));
        	nouveau.setBlobLogo(taLabelArticle.getBlobLogo());
        	
        	refresLogo();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//        System.out.println(fichier.getBlobFichierOriginal());
//        System.out.println(fichier.getBlobFichierMiniature());
//        fichier.setNomFichierOriginal(event.getFile().getFileName());
//        fichier.setNomFichierMiniature(event.getFile().getFileName()+"-mini.jpg");
//        System.out.println(fichier.getNomFichierOriginal());
//        System.out.println(fichier.getNomFichierMiniature());
//        System.out.println(fichier.getIdFichier());
//        taFichierService.merge(fichier, ITaFichierServiceRemote.validationContext);
        
//        this.idLabelFichier = fichier.getIdFichier();
//        this.eventFichier = event;
        
        FacesContext.getCurrentInstance().addMessage(null, message);
        
//        try {
//			fichier.resizeImage(IOUtils.toByteArray(event.getFile().getInputstream()), event.getFile().getFileName(), 50, 50);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        
    }
	
	public byte[] apercu(){
		return eventFichier.getFile().getContent();
	}
	
//	public BufferedImage apercu(){
//
//		InputStream in;
//		try {
//			in = new ByteArrayInputStream(taFichierService.findById(this.idLabelFichier).getBlobFichierOriginal());
//			BufferedImage bImageFromConvert = ImageIO.read(in);
//
//			return bImageFromConvert;
//
//		} catch (FinderException | IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		}
//	}
	
//	public byte[] apercu(){
//		try {
//			
//			return taFichierService.findById(this.idLabelFichier).getBlobFichierOriginal();
//		} catch (FinderException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		}
//	}

	/**
	 * @return the fichier
	 */
	public TaFichier getFichier() {
		return fichier;
	}

	/**
	 * @param fichier the fichier to set
	 */
	public void setFichier(TaFichier fichier) {
		this.fichier = fichier;
	}
	
	/**
	 * @return the idLabelFichier
	 */
	public int getIdLabelFichier() {
		return idLabelFichier;
	}

	/**
	 * @param idLabelFichier the idLabelFichier to set
	 */
	public void setIdLabelFichier(int idLabelFichier) {
		this.idLabelFichier = idLabelFichier;
	}

	public void actImprimer(ActionEvent actionEvent) {
		if(ConstWeb.DEBUG) {
		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage("LabelArticle", "actImprimer"));
		}
		try {
			
//		FacesContext facesContext = FacesContext.getCurrentInstance();
//		HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
		
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		sessionMap.put("labelArticle", taLabelArticleService.findById(selection.getId()));
		sessionMap.put("fichier", taFichierService.findById(10));
		
//			session.setAttribute("tiers", taTiersService.findById(selectedTaTiersDTO.getId()));
			System.out.println("LabelArticleController.actImprimer()");
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Dima - Début
	public void validateObject(FacesContext context, UIComponent component, Object value) throws ValidatorException {
//		String msg = "";
//		try {
//			String nomChamp =  (String) component.getAttributes().get("nomChamp");
//			validateUIField(nomChamp,value);
//			TaLabelArticleDTO temp=new TaLabelArticleDTO();
//			PropertyUtils.setProperty(temp, nomChamp, value);
//			taLabelArticleService.validateEntityProperty(temp, nomChamp, ITaLabelArticleServiceRemote.validationContext );
//		} catch(Exception e) {
//			msg += e.getMessage();
//			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
//		}
		
		String messageComplet = "";
		try {
			String nomChamp =  (String) component.getAttributes().get("nomChamp");
			//validation automatique via la JSR bean validation
			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
			Set<ConstraintViolation<TaLabelArticleDTO>> violations = factory.getValidator().validateValue(TaLabelArticleDTO.class,nomChamp,value);
			if (violations.size() > 0) {
				messageComplet = "Erreur de validation : ";
				for (ConstraintViolation<TaLabelArticleDTO> cv : violations) {
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
			if(nomChamp.equals(Const.C_CODE_LABEL_ARTICLE)) {
					boolean changement=false;
					if(selection.getCodeLabelArticle()!=null && value!=null && !selection.getCodeLabelArticle().equals(""))
					{
						if(value instanceof TaLabelArticle)
							changement=((TaLabelArticle) value).getCodeLabelArticle().equals(selection.getCodeLabelArticle());
						else if(value instanceof String)
						changement=!value.equals(selection.getCodeLabelArticle());
					}
					if(changement && modeEcran.dataSetEnModeModification()){
						FacesContext context = FacesContext.getCurrentInstance();  
						context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Label article", Const.C_MESSAGE_CHANGEMENT_CODE));
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
        
        PrimeFaces.current().dialog().openDynamic("articles/dialog_type_label", options, params);
        
	}
	
	public void handleReturnDialogTypes(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			taLabelArticle = (TaLabelArticle) event.getObject();
			
		}
		refresh();
	}
	
	public void actDialogModifier(ActionEvent actionEvent){
		
		nouveau = selection;
//		modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
		
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
        
        PrimeFaces.current().dialog().openDynamic("articles/dialog_type_label", options, params);

	}
	// Dima -  Fin

	public StreamedContent getLogo() {
		return logo;
	}

	public void setLogo(StreamedContent logo) {
		this.logo = logo;
	}

	public TaLabelArticle getTaLabelArticle() {
		return taLabelArticle;
	}

	public void setTaLabelArticle(TaLabelArticle taLabelArticle) {
		this.taLabelArticle = taLabelArticle;
	}

	public Map<Integer, StreamedContent> getMapLogos() {
		return mapLogos;
	}

	public void setMapLogos(Map<Integer, StreamedContent> mapLogos) {
		this.mapLogos = mapLogos;
	}
}
