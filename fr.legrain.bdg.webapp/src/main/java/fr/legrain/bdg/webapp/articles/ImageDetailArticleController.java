package fr.legrain.bdg.webapp.articles;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
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
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import fr.legrain.article.dto.TaImageArticleDTO;
import fr.legrain.article.model.TaImageArticle;
import fr.legrain.bdg.article.service.remote.ITaImageArticleServiceRemote;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.tiers.service.remote.ITaTCiviliteServiceRemote;
import fr.legrain.bdg.webapp.ConstWeb;
import fr.legrain.bdg.webapp.app.AbstractController;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.ModeObjetEcranServeur;


@Named
@ViewScoped
public class ImageDetailArticleController extends AbstractController implements Serializable {

	private static final long serialVersionUID = 7401934868396790434L;
	
	private TaImageArticleDTO dto ;

	private String modeEcranDefaut;

	private FileUploadEvent eventFichier;
	
	private TaImageArticle taImageArticle = new TaImageArticle();
	
	private StreamedContent logo;
	private Map<Integer,StreamedContent> mapLogos;

	private ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();

	private @EJB ITaImageArticleServiceRemote taImageArticleService;
	
	private LgrDozerMapper<TaImageArticleDTO,TaImageArticle> mapperUIToModel  = new LgrDozerMapper<TaImageArticleDTO,TaImageArticle>();
	private LgrDozerMapper<TaImageArticle,TaImageArticleDTO> mapperModelToUI  = new LgrDozerMapper<TaImageArticle,TaImageArticleDTO>();
	
	@PostConstruct
	public void postConstruct(){
		try {
			refresh();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void refresLogo() {
		try {
			logo = null;
			if(dto!=null && dto.getBlobImageOrigine()!=null) {
				InputStream stream = new ByteArrayInputStream(dto.getBlobImageOrigine()); 
				//logo = new DefaultStreamedContent(stream, "image/png");
				logo = new DefaultStreamedContent(stream);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ImageDetailArticleController() {  
	}  

	public void actInserer(ActionEvent actionEvent){
		modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
	}

	public void actModifier(ActionEvent actionEvent){
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

	public void actSupprimer(ActionEvent actionEvent){
		
		Integer idImage = (Integer) actionEvent.getComponent().getAttributes().get("idImage"); 
		
		TaImageArticle taImageArticle = new TaImageArticle();
		try {
			if(idImage!=null && idImage!=null){
				taImageArticle = taImageArticleService.findById(idImage);
			}

			taImageArticleService.remove(taImageArticle);
	
			
//			if(!values.isEmpty()) {
//				selection = values.get(0);
//			}else {
//				selection=new TaImageArticleDTO();
//			}

			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Image article", "actSupprimer"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Image article", e.getCause().getCause().getCause().getCause().getMessage()));
		}
	}
	
	public void actImageDefaut(ActionEvent actionEvent){
		Integer idImage = (Integer) actionEvent.getComponent().getAttributes().get("idImage"); 
		
		try {
			TaImageArticle taImageArticleDefaut = taImageArticleService.findById(idImage);
			taImageArticleService.changeImageDefaut(taImageArticleDefaut);
			
//			masterEntity.setTaImageArticle(taImageArticleDefaut);
//			taImageArticleDefaut.setTaArticle(masterEntity);
//			masterEntity = taArticleService.merge(masterEntity);

		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	

	public void actEnregistrer(ActionEvent actionEvent){
		try {
			TaImageArticleDTO retour = null;
			taImageArticle=new TaImageArticle();
//			if(dto.getId()==null || taImageArticleService.findById(dto.getId()) == null){
//				mapperUIToModel.map(dto, taImageArticle);
//				taImageArticle.setTaArticle(dto);
//				taImageArticle = taImageArticleService.merge(taImageArticle, ITaTCiviliteServiceRemote.validationContext);
//				mapperModelToUI.map(taImageArticle, dto);
//				values= taImageArticleService.findByArticleLight(masterEntity.getIdArticle());
//				nouveau = values.get(0);
//				nouveau = new TaImageArticleDTO();
//
//				modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
//
//			}else{
				if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_EDITION)==0){
					taImageArticle = taImageArticleService.findById(dto.getId());
					mapperUIToModel.map(dto, taImageArticle);
					taImageArticle = taImageArticleService.merge(taImageArticle, ITaTCiviliteServiceRemote.validationContext);
					mapperModelToUI.map(taImageArticle, dto);
//					values= taImageArticleService.findByArticleLight(masterEntity.getIdArticle());
//					nouveau = values.get(0);
//					nouveau = new TaImageArticleDTO();

					modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
				}
				else{
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "Code déja utilisé"));
				}
//			}
//			if(modeEcranDefaut!=null && modeEcranDefaut.equals(C_DIALOG)) {
				PrimeFaces.current().dialog().closeDynamic(taImageArticle);
//			}
//			refresLogo();
		} catch(Exception e) {
			e=ThrowableExceptionLgr.renvoieCauseRuntimeException(e);
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			//context.addMessage(null, new FacesMessage("Tiers", "actEnregistrer")); 
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "TypeImageArticle", e.getMessage()));
		}
	}
	
	public void actAnnuler(ActionEvent actionEvent){
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}

	public TaImageArticleDTO getDto() {
		return dto;
	}

	public void setDto(TaImageArticleDTO selectedTaImageArticle) {
		this.dto = selectedTaImageArticle;
	}


	public void refresh(){
		refresh(null);
	}
	
	public void refresh(ActionEvent ev){
		Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		try {
			modeEcranDefaut = params.get("modeEcranDefaut");
			if(modeEcranDefaut!=null && modeEcranDefaut.equals(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION))) {
				actInserer(null);
			} else if(modeEcranDefaut!=null && modeEcranDefaut.equals(modeEcran.modeString(EnumModeObjet.C_MO_EDITION))) {
				if(params.get("idEntity")!=null) {
					dto = taImageArticleService.findByIdDTO(LibConversion.stringToInteger(params.get("idEntity")));
					taImageArticle = taImageArticleService.findById(LibConversion.stringToInteger(params.get("idEntity")));
				}
				actModifier(null);
			} else {
				modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
			}
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		mapLogos = new HashMap<>();
//		for (TaImageArticleDTO taImageArticle : values) {
//			mapLogos.put(taImageArticle.getId(), taImageArticle.getBlobImageOrigine()!=null ? new DefaultStreamedContent(new ByteArrayInputStream(taImageArticle.getBlobImageOrigine())):null);
//		}
	}
	
	public void actFermerDialog(ActionEvent actionEvent) {
		PrimeFaces.current().dialog().closeDynamic(taImageArticle);
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

//		System.out.println("Event message : getSize() - "+event.getFile().getSize());
//        FacesMessage message = new FacesMessage("Succés!", event.getFile().getFileName() + " a été chargé.");
//               
//        try {
////			fichier.setBlobFichierOriginal(IOUtils.toByteArray(event.getFile().getInputstream()));
////			fichier.setBlobFichierMiniature(fichier.resizeImage(IOUtils.toByteArray(event.getFile().getInputstream()), event.getFile().getFileName(), 640, 480));
//        	taImageArticle.setBlobImageOrigine(IOUtils.toByteArray(event.getFile().getInputstream()));
//        	dto.setBlobImageOrigine(taImageArticle.getBlobImageOrigine());
//        	
//        	mapperUIToModel.map(dto, taImageArticle);
//			taImageArticle.setTaArticle(masterEntity);
//			taImageArticle = taImageArticleService.merge(taImageArticle, ITaTCiviliteServiceRemote.validationContext);
//			mapperModelToUI.map(taImageArticle, nouveau);
//        	
//        	refresLogo();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//        FacesContext.getCurrentInstance().addMessage(null, message);
//        
////        try {
////			fichier.resizeImage(IOUtils.toByteArray(event.getFile().getInputstream()), event.getFile().getFileName(), 50, 50);
////		} catch (IOException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
//        
    }


	public void validateObject(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		String messageComplet = "";
		try {
			String nomChamp =  (String) component.getAttributes().get("nomChamp");
			//validation automatique via la JSR bean validation
			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
			Set<ConstraintViolation<TaImageArticleDTO>> violations = factory.getValidator().validateValue(TaImageArticleDTO.class,nomChamp,value);
			if (violations.size() > 0) {
				messageComplet = "Erreur de validation : ";
				for (ConstraintViolation<TaImageArticleDTO> cv : violations) {
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
//			if(nomChamp.equals(Const.C_CODE_Image_ARTICLE)) {
//					boolean changement=false;
//					if(selection.getCodeImageArticle()!=null && value!=null && !selection.getCodeImageArticle().equals(""))
//					{
//						if(value instanceof TaImageArticle)
//							changement=((TaImageArticle) value).getCodeImageArticle().equals(selection.getCodeImageArticle());
//						else if(value instanceof String)
//						changement=!value.equals(selection.getCodeImageArticle());
//					}
//					if(changement && modeEcran.dataSetEnModeModification()){
//						FacesContext context = FacesContext.getCurrentInstance();  
//						context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Image article", Const.C_MESSAGE_CHANGEMENT_CODE));
//					}
//				}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}


	public StreamedContent getLogo() {
		return logo;
	}

	public void setLogo(StreamedContent logo) {
		this.logo = logo;
	}

	public TaImageArticle getTaImageArticle() {
		return taImageArticle;
	}

	public void setTaImageArticle(TaImageArticle taImageArticle) {
		this.taImageArticle = taImageArticle;
	}

	public Map<Integer, StreamedContent> getMapLogos() {
		return mapLogos;
	}

	public void setMapLogos(Map<Integer, StreamedContent> mapLogos) {
		this.mapLogos = mapLogos;
	}

}
