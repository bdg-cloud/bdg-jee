package fr.legrain.bdg.webapp.articles;

import java.io.IOException;
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
import javax.inject.Named;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.apache.commons.io.IOUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;

import fr.legrain.article.dto.TaMarqueArticleDTO;
import fr.legrain.article.model.TaMarqueArticle;
import fr.legrain.bdg.article.service.remote.ITaMarqueArticleServiceRemote;
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
public class MarqueArticleController implements Serializable {

	private List<TaMarqueArticleDTO> values;
	private List<TaMarqueArticleDTO> filteredValues;
	private TaMarqueArticleDTO nouveau ;
	private TaMarqueArticleDTO selection ;

	private String modeEcranDefaut;
	private static final String C_DIALOG = "dialog";
	private TaFichier fichier;
	private int idMarqueFichier;
	private FileUploadEvent eventFichier;
	
	private TaMarqueArticle taMarqueArticle = new TaMarqueArticle();

	private ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();

	private @EJB ITaMarqueArticleServiceRemote taMarqueArticleService;
	private @EJB ITaFichierServiceRemote taFichierService;
	
	private LgrDozerMapper<TaMarqueArticleDTO,TaMarqueArticle> mapperUIToModel  = new LgrDozerMapper<TaMarqueArticleDTO,TaMarqueArticle>();
	private LgrDozerMapper<TaMarqueArticle,TaMarqueArticleDTO> mapperModelToUI  = new LgrDozerMapper<TaMarqueArticle,TaMarqueArticleDTO>();
	
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

	public MarqueArticleController() {  
	}  

	public void actInserer(ActionEvent actionEvent){
		nouveau = new TaMarqueArticleDTO();
		modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
	}

	public void actModifier(ActionEvent actionEvent){
		nouveau = selection;
		modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
	}

	public void actSupprimer(){
		TaMarqueArticle taMarqueArticle = new TaMarqueArticle();
		try {
			if(selection!=null && selection.getId()!=null){
				taMarqueArticle = taMarqueArticleService.findById(selection.getId());
			}

			taMarqueArticleService.remove(taMarqueArticle);
			values.remove(selection);
			
			if(!values.isEmpty()) {
				selection = values.get(0);
			}else {
				selection=new TaMarqueArticleDTO();
			}

			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Marque article", "actSupprimer"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Marque article", e.getCause().getCause().getCause().getCause().getMessage()));
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
			TaMarqueArticleDTO retour = null;
			taMarqueArticle=new TaMarqueArticle();
			if(nouveau.getId()==null || taMarqueArticleService.findById(nouveau.getId()) == null){
				mapperUIToModel.map(nouveau, taMarqueArticle);
				taMarqueArticle = taMarqueArticleService.merge(taMarqueArticle, ITaTCiviliteServiceRemote.validationContext);
				mapperModelToUI.map(taMarqueArticle, nouveau);
				values= taMarqueArticleService.selectAllDTO();
				nouveau = values.get(0);
				nouveau = new TaMarqueArticleDTO();

				modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

			}else{
				if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_EDITION)==0){
					taMarqueArticle = taMarqueArticleService.findById(nouveau.getId());
					mapperUIToModel.map(nouveau, taMarqueArticle);
					taMarqueArticle = taMarqueArticleService.merge(taMarqueArticle, ITaTCiviliteServiceRemote.validationContext);
					mapperModelToUI.map(taMarqueArticle, nouveau);
					values= taMarqueArticleService.selectAllDTO();
					nouveau = values.get(0);
					nouveau = new TaMarqueArticleDTO();

					modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
				}
				else{
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "Code déja utilisé"));
				}
			}
			if(modeEcranDefaut!=null && modeEcranDefaut.equals(C_DIALOG)) {
				PrimeFaces.current().dialog().closeDynamic(taMarqueArticle);
			}
		} catch(Exception e) {
			e=ThrowableExceptionLgr.renvoieCauseRuntimeException(e);
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			//context.addMessage(null, new FacesMessage("Tiers", "actEnregistrer")); 
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "TypeMarqueArticle", e.getMessage()));
		}
	}
	
	public void actAnnuler(ActionEvent actionEvent){

		if(values.size()>= 1){
			selection = values.get(0);
		}		
		nouveau = new TaMarqueArticleDTO();

		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}

	public List<TaMarqueArticleDTO> getValues(){  
		return values;
	}

	public TaMarqueArticleDTO getNouveau() {
		return nouveau;
	}

	public void setNouveau(TaMarqueArticleDTO newTaMarqueArticle) {
		this.nouveau = newTaMarqueArticle;
	}

	public TaMarqueArticleDTO getSelection() {
		return selection;
	}

	public void setSelection(TaMarqueArticleDTO selectedTaMarqueArticle) {
		this.selection = selectedTaMarqueArticle;
	}

	public void setValues(List<TaMarqueArticleDTO> values) {
		this.values = values;
	}

	public void onRowSelect(SelectEvent event) {  
		if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_CONSULTATION)==0) {
			nouveau = selection;
		}
	}

	public List<TaMarqueArticleDTO> getFilteredValues() {
		return filteredValues;
	}

	public void setFilteredValues(List<TaMarqueArticleDTO> filteredValues) {
		this.filteredValues = filteredValues;
	}

	public void refresh(){
		values = taMarqueArticleService.selectAllDTO();
		Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		modeEcranDefaut = params.get("modeEcranDefaut");
		if(modeEcranDefaut!=null && modeEcranDefaut.equals(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION))) {
			modeEcranDefaut = C_DIALOG;
			actInserer(null);
		} else if(modeEcranDefaut!=null && modeEcranDefaut.equals(modeEcran.modeString(EnumModeObjet.C_MO_EDITION))) {
			modeEcranDefaut = C_DIALOG;
			if(params.get("idEntity")!=null) {
				try {
					selection = taMarqueArticleService.findByIdDTO(LibConversion.stringToInteger(params.get("idEntity")));
				} catch (FinderException e) {
					// TODO Auto-generated catch block
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
//		fichier = new TaFichier();
//		fichier.fichierVersBinaire(event.getFile().getFileName());
//		fichier.fichierVersBinaire("C:\\test\\in\\Mushroom - 1UP.ico");
//		taFichierService.merge(fichier);
		
		/**
		
		Integer idPiece = (Integer) event.getComponent().getAttributes().get("idPiece");

        //Stock s = stockService.findById(idPiece);
        piece = stockService.findById(idPiece);

        ImagePiece img = new ImagePiece();
        if(piece.getImages()==null) {
            piece.setImages(new ArrayList<ImagePiece>());
        }
        String nomFichier = "img_"+idPiece+"_"+piece.getImages().size()+event.getFile().getFileName().substring(event.getFile().getFileName().lastIndexOf("."));
        img.setChemin(nomFichier);

        Path target3 = Paths.get("/var/careco/images/"+nomFichier);
        try {
            Files.copy(event.getFile().getInputstream(), target3);
        } catch (IOException e) {
            e.printStackTrace();
        }

        img.setIdPiece(piece);
        piece.getImages().add(img);

        piece = stockService.enregistrerMerge(piece);
        
		 **/
		System.out.println("Event message : getSize() - "+event.getFile().getSize());
		
        FacesMessage message = new FacesMessage("Succés!", event.getFile().getFileName() + " a été chargé.");
        
        fichier = new TaFichier();
        
        try {
			fichier.setBlobFichierOriginal(IOUtils.toByteArray(event.getFile().getInputStream()));
			fichier.setBlobFichierMiniature(fichier.resizeImage(IOUtils.toByteArray(event.getFile().getInputStream()), event.getFile().getFileName(), 640, 480));
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
        
        this.idMarqueFichier = fichier.getIdFichier();
        this.eventFichier = event;
        
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
//			in = new ByteArrayInputStream(taFichierService.findById(this.idMarqueFichier).getBlobFichierOriginal());
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
//			return taFichierService.findById(this.idMarqueFichier).getBlobFichierOriginal();
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
	 * @return the idMarqueFichier
	 */
	public int getIdMarqueFichier() {
		return idMarqueFichier;
	}

	/**
	 * @param idMarqueFichier the idMarqueFichier to set
	 */
	public void setIdMarqueFichier(int idMarqueFichier) {
		this.idMarqueFichier = idMarqueFichier;
	}

	public void actImprimer(ActionEvent actionEvent) {
		if(ConstWeb.DEBUG) {
		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage("MarqueArticle", "actImprimer"));
		}
		try {
			
//		FacesContext facesContext = FacesContext.getCurrentInstance();
//		HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
		
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		sessionMap.put("labelArticle", taMarqueArticleService.findById(selection.getId()));
		sessionMap.put("fichier", taFichierService.findById(10));
		
//			session.setAttribute("tiers", taTiersService.findById(selectedTaTiersDTO.getId()));
			System.out.println("MarqueArticleController.actImprimer()");
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
//			TaMarqueArticleDTO temp=new TaMarqueArticleDTO();
//			PropertyUtils.setProperty(temp, nomChamp, value);
//			taMarqueArticleService.validateEntityProperty(temp, nomChamp, ITaMarqueArticleServiceRemote.validationContext );
//		} catch(Exception e) {
//			msg += e.getMessage();
//			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
//		}
		
		String messageComplet = "";
		try {
			String nomChamp =  (String) component.getAttributes().get("nomChamp");
			//validation automatique via la JSR bean validation
			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
			Set<ConstraintViolation<TaMarqueArticleDTO>> violations = factory.getValidator().validateValue(TaMarqueArticleDTO.class,nomChamp,value);
			if (violations.size() > 0) {
				messageComplet = "Erreur de validation : ";
				for (ConstraintViolation<TaMarqueArticleDTO> cv : violations) {
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
			if(nomChamp.equals(Const.C_CODE_MARQUE_ARTICLE)) {
					boolean changement=false;
					if(selection.getCodeMarqueArticle()!=null && value!=null && !selection.getCodeMarqueArticle().equals(""))
					{
						if(value instanceof TaMarqueArticle)
							changement=((TaMarqueArticle) value).getCodeMarqueArticle().equals(selection.getCodeMarqueArticle());
						else if(value instanceof String)
						changement=!value.equals(selection.getCodeMarqueArticle());
					}
					if(changement && modeEcran.dataSetEnModeModification()){
						FacesContext context = FacesContext.getCurrentInstance();  
						context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Marque article", Const.C_MESSAGE_CHANGEMENT_CODE));
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
        options.put("contentHeight", 500);
        options.put("modal", true);
        
        Map<String,List<String>> params = new HashMap<String,List<String>>();
        List<String> list = new ArrayList<String>();
        list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
        params.put("modeEcranDefaut", list);
        
        PrimeFaces.current().dialog().openDynamic("articles/dialog_marque", options, params);
        
	}
	
	public void handleReturnDialogTypes(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			taMarqueArticle = (TaMarqueArticle) event.getObject();
			
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
        options.put("contentHeight", 500);
        options.put("modal", true);
        
        Map<String,List<String>> params = new HashMap<String,List<String>>();
        List<String> list = new ArrayList<String>();
        list.add(modeEcran.modeString(EnumModeObjet.C_MO_EDITION));
        params.put("modeEcranDefaut", list);
        List<String> list2 = new ArrayList<String>();
        list2.add(LibConversion.integerToString(selection.getId()));
        params.put("idEntity", list2);
        
        PrimeFaces.current().dialog().openDynamic("articles/dialog_marque", options, params);

	}
	// Dima -  Fin
}
