package fr.legrain.bdg.webapp.articles;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
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
import javax.imageio.ImageIO;
import javax.inject.Inject;
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

import fr.legrain.article.dto.TaImageArticleDTO;
import fr.legrain.article.model.TaArticle;
import fr.legrain.article.model.TaImageArticle;
import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaImageArticleServiceRemote;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.tiers.service.remote.ITaTCiviliteServiceRemote;
import fr.legrain.bdg.webapp.ConstWeb;
import fr.legrain.bdg.webapp.app.AbstractController;
import fr.legrain.bdg.webapp.app.AutorisationBean;
import fr.legrain.general.model.TaFichier;
import fr.legrain.general.service.remote.ITaFichierServiceRemote;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.ModeObjetEcranServeur;
import net.coobird.thumbnailator.Thumbnails;


@Named
@ViewScoped
public class ImageArticleController extends AbstractController implements Serializable {

	private static final long serialVersionUID = 7401934868396790434L;
	
	@Inject @Named(value="autorisationBean")
	private AutorisationBean autorisationBean;
	
	private List<TaImageArticleDTO> values;
	private List<TaImageArticleDTO> filteredValues;
	private TaImageArticleDTO nouveau ;
	private TaImageArticleDTO selection ;
	
	private TaArticle masterEntity ;
	
	private int nbImageMax = 1;

	private String modeEcranDefaut;
	private static final String C_DIALOG = "dialog";
	private TaFichier fichier;
	private int idImageFichier;
	private FileUploadEvent eventFichier;
	
	private TaImageArticle taImageArticle = new TaImageArticle();
	
	private StreamedContent logo;
	private Map<Integer,StreamedContent> mapLogos;

	private ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();

	private @EJB ITaImageArticleServiceRemote taImageArticleService;
	private @EJB ITaArticleServiceRemote taArticleService;
	private @EJB ITaFichierServiceRemote taFichierService;
	
	private LgrDozerMapper<TaImageArticleDTO,TaImageArticle> mapperUIToModel  = new LgrDozerMapper<TaImageArticleDTO,TaImageArticle>();
	private LgrDozerMapper<TaImageArticle,TaImageArticleDTO> mapperModelToUI  = new LgrDozerMapper<TaImageArticle,TaImageArticleDTO>();
	
	@PostConstruct
	public void postConstruct(){
		try {
			boolean modeCatalogue = autorisationBean.autoriseMenu(AutorisationBean.ID_MODULE_CATALOGUE);
			if(modeCatalogue) {
				nbImageMax = 6;
			}
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
			if(nouveau!=null && nouveau.getBlobImageOrigine()!=null) {
				InputStream stream = new ByteArrayInputStream(nouveau.getBlobImageOrigine()); 
				//logo = new DefaultStreamedContent(stream, "image/png");
				logo = new DefaultStreamedContent(stream);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ImageArticleController() {  
	}  

	public void actInserer(ActionEvent actionEvent){
		nouveau = new TaImageArticleDTO();
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

	public void actSupprimer(ActionEvent actionEvent){
		
		Integer idImage = (Integer) actionEvent.getComponent().getAttributes().get("idImage"); 
		
		TaImageArticle taImageArticle = new TaImageArticle();
		try {
			if(idImage!=null && idImage!=null){
				taImageArticle = taImageArticleService.findById(idImage);
			}
			
//			taArticleService.findById(masterEntity.getIdArticle());
//			masterEntity.removeImageArticle(taImageArticle);
//			if(masterEntity.getTaImageArticles()!=null && masterEntity.getTaImageArticles().size()==1) {
//				masterEntity.setTaImageArticle(null);
//				masterEntity.getTaImageArticles().clear();
//			}
//			taArticleService.merge(masterEntity);
			
			taImageArticleService.remove(taImageArticle);
			values = taImageArticleService.findByArticleLight(masterEntity.getIdArticle());
			completeGrille();
			
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
	
	public void actImagePositionSuivant(ActionEvent actionEvent){
		Integer idImage = (Integer) actionEvent.getComponent().getAttributes().get("idImage"); 
		
		try {
			if(idImage!=null && idImage!=null){
				taImageArticle = taImageArticleService.findById(idImage);
			
				TaImageArticleDTO suivant = null;
				TaImageArticleDTO precedent = null;
				int i = 0;
				for (TaImageArticleDTO taImageArticleDTO : values) {
					if(taImageArticleDTO.getPosition()==taImageArticle.getPosition()) {
						if(i>1) {
							precedent = values.get(i-1);
						}
						if(i<values.size()-1) {
							suivant = values.get(i+1);
						}
					}
					i++;
				}
				if(suivant==null) {
					taImageArticle.setPosition(values.size());
				} else {
					TaImageArticle taImageArticleSuivant = taImageArticleService.findById(suivant.getId());
					Integer a = taImageArticle.getPosition();
					if(a==null) {
						a = 1;
					}
					taImageArticle.setPosition(suivant.getPosition());
					if(taImageArticleSuivant!=null) {
						taImageArticleSuivant.setPosition(a);
						taImageArticleService.merge(taImageArticleSuivant);
					}
				}
				
				
				taImageArticleService.merge(taImageArticle);
			
				values= taImageArticleService.findByArticleLight(masterEntity.getIdArticle());
				completeGrille();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void actImagePositionPrecedent(ActionEvent actionEvent){
		Integer idImage = (Integer) actionEvent.getComponent().getAttributes().get("idImage"); 
		
		try {
			if(idImage!=null && idImage!=null){
				taImageArticle = taImageArticleService.findById(idImage);
			
				TaImageArticleDTO suivant = null;
				TaImageArticleDTO precedent = null;
				int i = 0;
				for (TaImageArticleDTO taImageArticleDTO : values) {
					if(taImageArticleDTO.getPosition()==taImageArticle.getPosition()) {
						if(i>1) {
							precedent = values.get(i-1);
						}
						if(i<values.size()-1) {
							suivant = values.get(i+1);
						}
					}
					i++;
				}
				if(precedent==null) {
					taImageArticle.setPosition(values.size());
				} else {
					TaImageArticle taImageArticlePrecedent = taImageArticleService.findById(precedent.getId());
					Integer a = taImageArticle.getPosition();
					if(a==null) {
						a = 1;
					}
					taImageArticle.setPosition(precedent.getPosition());
					if(taImageArticlePrecedent!=null) {
						taImageArticlePrecedent.setPosition(a);
						taImageArticleService.merge(taImageArticlePrecedent);
					}
				}
				
				
				taImageArticleService.merge(taImageArticle);
			
				values= taImageArticleService.findByArticleLight(masterEntity.getIdArticle());
				completeGrille();
			}
		} catch (Exception e) {
			e.printStackTrace();
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
			
			values= taImageArticleService.findByArticleLight(masterEntity.getIdArticle());
			completeGrille();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void actImageDetail(ActionEvent actionEvent){
		Integer idImage = (Integer) actionEvent.getComponent().getAttributes().get("idImage"); 
		
		
		
		try {
			TaImageArticle taImageArticle = taImageArticleService.findById(idImage);

			actDialogDetail(taImageArticle);
			

		} catch(Exception e) {
			e.printStackTrace();
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
			TaImageArticleDTO retour = null;
			taImageArticle=new TaImageArticle();
			if(nouveau.getId()==null || taImageArticleService.findById(nouveau.getId()) == null){
				mapperUIToModel.map(nouveau, taImageArticle);
				taImageArticle.setTaArticle(masterEntity);
				taImageArticle = taImageArticleService.merge(taImageArticle, ITaTCiviliteServiceRemote.validationContext);
				mapperModelToUI.map(taImageArticle, nouveau);
				values= taImageArticleService.findByArticleLight(masterEntity.getIdArticle());
				nouveau = values.get(0);
				nouveau = new TaImageArticleDTO();

				modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

			}else{
				if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_EDITION)==0){
					taImageArticle = taImageArticleService.findById(nouveau.getId());
					mapperUIToModel.map(nouveau, taImageArticle);
					taImageArticle = taImageArticleService.merge(taImageArticle, ITaTCiviliteServiceRemote.validationContext);
					mapperModelToUI.map(taImageArticle, nouveau);
					values= taImageArticleService.findByArticleLight(masterEntity.getIdArticle());
					nouveau = values.get(0);
					nouveau = new TaImageArticleDTO();

					modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
				}
				else{
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "Code déja utilisé"));
				}
			}
			if(modeEcranDefaut!=null && modeEcranDefaut.equals(C_DIALOG)) {
				PrimeFaces.current().dialog().closeDynamic(taImageArticle);
			}
			refresLogo();
		} catch(Exception e) {
			e=ThrowableExceptionLgr.renvoieCauseRuntimeException(e);
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			//context.addMessage(null, new FacesMessage("Tiers", "actEnregistrer")); 
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "TypeImageArticle", e.getMessage()));
		}
	}
	
	public void actAnnuler(ActionEvent actionEvent){

		if(values.size()>= 1){
			selection = values.get(0);
		}		
		nouveau = new TaImageArticleDTO();

		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}

	public List<TaImageArticleDTO> getValues(){  
		return values;
	}

	public TaImageArticleDTO getNouveau() {
		return nouveau;
	}

	public void setNouveau(TaImageArticleDTO newTaImageArticle) {
		this.nouveau = newTaImageArticle;
	}

	public TaImageArticleDTO getSelection() {
		return selection;
	}

	public void setSelection(TaImageArticleDTO selectedTaImageArticle) {
		this.selection = selectedTaImageArticle;
	}

	public void setValues(List<TaImageArticleDTO> values) {
		this.values = values;
	}

	public void onRowSelect(SelectEvent event) {  
		if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_CONSULTATION)==0) {
			nouveau = selection;
			refresLogo();
		}
	}

	public List<TaImageArticleDTO> getFilteredValues() {
		return filteredValues;
	}

	public void setFilteredValues(List<TaImageArticleDTO> filteredValues) {
		this.filteredValues = filteredValues;
	}

	public void refresh(){
		refresh(null);
	}

	private int nbImage = 0;
	private void completeGrille() {
		nbImage = 0;
		if(values!=null) {
			nbImage = values.size();
			if(values.size()<nbImageMax) {
				do {
					TaImageArticleDTO dto = new TaImageArticleDTO();
					dto.setId(0);
					values.add(dto);
				} while(values.size()<nbImageMax);
			}	
		}
	}
	
	public void refresh(ActionEvent ev){
		Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		try {
			if(params.get("idMasterEntity")!=null) {
				masterEntity = taArticleService.findById(LibConversion.stringToInteger(params.get("idMasterEntity")));
			}
			//values = taImageArticleService.selectAllDTO();
			values=new LinkedList<TaImageArticleDTO>();
			if(masterEntity!=null)
				values = taImageArticleService.findByArticleLight(masterEntity.getIdArticle());
			
			completeGrille();
			
			modeEcranDefaut = params.get("modeEcranDefaut");
			if(modeEcranDefaut!=null && modeEcranDefaut.equals(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION))) {
				modeEcranDefaut = C_DIALOG;
				actInserer(null);
			} else if(modeEcranDefaut!=null && modeEcranDefaut.equals(modeEcran.modeString(EnumModeObjet.C_MO_EDITION))) {
				modeEcranDefaut = C_DIALOG;
				if(params.get("idEntity")!=null) {
					selection = taImageArticleService.findByIdDTO(LibConversion.stringToInteger(params.get("idEntity")));
				}
				actModifier(null);
			} else {
				modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
			}
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		mapLogos = new HashMap<>();
		for (TaImageArticleDTO taImageArticle : values) {
			mapLogos.put(taImageArticle.getId(), taImageArticle.getBlobImageOrigine()!=null ? new DefaultStreamedContent(new ByteArrayInputStream(taImageArticle.getBlobImageOrigine())):null);
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
		
		int idImage = (int) event.getComponent().getAttributes().get("idImage"); 
		
		if(idImage==0) {
			taImageArticle = new TaImageArticle();
			nouveau = new TaImageArticleDTO();
			nouveau.setPosition(nbImage+1);
		}
        FacesMessage message = new FacesMessage("Succés!", event.getFile().getFileName() + " a été chargé.");
        
        fichier = new TaFichier();
        
        try {
//			fichier.setBlobFichierOriginal(IOUtils.toByteArray(event.getFile().getInputstream()));
//			fichier.setBlobFichierMiniature(fichier.resizeImage(IOUtils.toByteArray(event.getFile().getInputStream()), event.getFile().getFileName(), 640, 480));
            
        	byte[] origine = IOUtils.toByteArray(event.getFile().getInputStream());
        	taImageArticle.setBlobImageOrigine(origine);
        	nouveau.setBlobImageOrigine(taImageArticle.getBlobImageOrigine());
        	
        	taImageArticle = genereMiniature(taImageArticle);
        	nouveau.setBlobImagePetit(taImageArticle.getBlobImagePetit());
        	nouveau.setBlobImageMoyen(taImageArticle.getBlobImageMoyen());
        	nouveau.setBlobImageGrand(taImageArticle.getBlobImageGrand());
        	
			
        	mapperUIToModel.map(nouveau, taImageArticle);
			taImageArticle.setTaArticle(masterEntity);
			taImageArticle = taImageArticleService.merge(taImageArticle, ITaTCiviliteServiceRemote.validationContext);
			mapperModelToUI.map(taImageArticle, nouveau);
			values= taImageArticleService.findByArticleLight(masterEntity.getIdArticle());
			completeGrille();
			nouveau = values.get(0);
			nouveau = new TaImageArticleDTO();
        	
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
        
//        this.idImageFichier = fichier.getIdFichier();
//        this.eventFichier = event;
        
        FacesContext.getCurrentInstance().addMessage(null, message);
        
//        try {
//			fichier.resizeImage(IOUtils.toByteArray(event.getFile().getInputstream()), event.getFile().getFileName(), 50, 50);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        
    }
	
	public TaImageArticle genereMiniature(TaImageArticle img) throws IOException {
		if(img!=null && img.getBlobImageOrigine()!=null) {
			
			int PETIT_H = 31;
			int PETIT_W = 44;
			
			int MOYEN_H = 81;
			int MOYEN_W = 113;
			
			int GRAND_H = 259;
			int GRAND_W = 361;
			
			String formatSortieDefaut = "png";
			
			ByteArrayInputStream stream = new ByteArrayInputStream(img.getBlobImageOrigine());
			BufferedImage originalImage = ImageIO.read(stream);
        	int height = originalImage.getHeight();
            int width = originalImage.getWidth();
			
            ByteArrayInputStream stream1 = new ByteArrayInputStream(img.getBlobImageOrigine());
            ByteArrayInputStream stream2 = new ByteArrayInputStream(img.getBlobImageOrigine()); 
            ByteArrayInputStream stream3 = new ByteArrayInputStream(img.getBlobImageOrigine()); 
        	
        	ByteArrayOutputStream petitOs = new ByteArrayOutputStream();
        	ByteArrayOutputStream moyenOs = new ByteArrayOutputStream();
        	ByteArrayOutputStream grandOs = new ByteArrayOutputStream();
        	
            // Conditional resize
            if ((height > PETIT_H) || (width > PETIT_W)) {
            	Thumbnails.of(stream1).size(PETIT_W, PETIT_H).outputFormat(formatSortieDefaut).toOutputStream(petitOs);
            } else {
            	//IOUtils.copy(stream1, petitOs);
            	byte[] buf = new byte[8192];
                int length;
                while ((length = stream1.read(buf)) > 0) {
                	petitOs.write(buf, 0, length);
                }
            }
            
            if ((height > MOYEN_H) || (width > MOYEN_W)) {
            	Thumbnails.of(stream2).size(MOYEN_W, MOYEN_H).outputFormat(formatSortieDefaut).toOutputStream(moyenOs);
            } else {
            	//IOUtils.copy(stream2, moyenOs);
            	byte[] buf = new byte[8192];
                int length;
                while ((length = stream2.read(buf)) > 0) {
                	moyenOs.write(buf, 0, length);
                }
            }
            
            if ((height > GRAND_H) || (width > GRAND_W)) {
            	Thumbnails.of(stream3).size(GRAND_W, GRAND_H).outputFormat(formatSortieDefaut).toOutputStream(grandOs);
            } else {
            	//IOUtils.copy(stream3, grandOs);
            	byte[] buf = new byte[8192];
                int length;
                while ((length = stream3.read(buf)) > 0) {
                	grandOs.write(buf, 0, length);
                }
            }
        	
        	img.setBlobImagePetit(petitOs.toByteArray());
        	img.setBlobImageMoyen(moyenOs.toByteArray());
        	img.setBlobImageGrand(grandOs.toByteArray());
        	
		}
		return img;
	}
	
	public void regenereTout(ActionEvent ev){
		if(values!=null && !values.isEmpty()) {
			TaImageArticle img = null;
			fichier = new TaFichier();
			for (TaImageArticleDTO taImageArticleDTO : values) {
				try {
					img = taImageArticleService.findById(taImageArticleDTO.getId());
		        	
					img = genereMiniature(img);
					if(img!=null) {
						taImageArticleService.merge(img);
					}
				} catch (FinderException e) {
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		
			}
			refresh();
		}
	}
	
	public byte[] apercu(){
		return eventFichier.getFile().getContent();
	}

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
	 * @return the idImageFichier
	 */
	public int getIdImageFichier() {
		return idImageFichier;
	}

	/**
	 * @param idImageFichier the idImageFichier to set
	 */
	public void setIdImageFichier(int idImageFichier) {
		this.idImageFichier = idImageFichier;
	}

	public void actImprimer(ActionEvent actionEvent) {
		if(ConstWeb.DEBUG) {
		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage("ImageArticle", "actImprimer"));
		}
		try {
			
//		FacesContext facesContext = FacesContext.getCurrentInstance();
//		HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
		
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		sessionMap.put("ImageArticle", taImageArticleService.findById(selection.getId()));
		sessionMap.put("fichier", taFichierService.findById(10));
		
//			session.setAttribute("tiers", taTiersService.findById(selectedTaTiersDTO.getId()));
			System.out.println("ImageArticleController.actImprimer()");
		} catch (FinderException e) {
			e.printStackTrace();
		}
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
	
	
	
	public void actDialogDetail(TaImageArticle taImageArticle){
		
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
        list2.add(LibConversion.integerToString(taImageArticle.getIdImageArticle()));
        params.put("idEntity", list2);
        
        PrimeFaces.current().dialog().openDynamic("articles/dialog_image_detail_article", options, params);

	}
	
	public void handleReturnDialogDetail(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			taImageArticle = (TaImageArticle) event.getObject();
			
			values= taImageArticleService.findByArticleLight(masterEntity.getIdArticle());
			completeGrille();
			
		}
		refresh();
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

	public TaArticle getMasterEntity() {
		return masterEntity;
	}

	public void setMasterEntity(TaArticle masterEntity) {
		this.masterEntity = masterEntity;
	}
}
