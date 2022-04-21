package fr.legrain.bdg.webapp.app;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.io.IOUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import fr.legrain.article.dto.TaArticleDTO;
import fr.legrain.article.dto.TaUniteDTO;
import fr.legrain.article.model.TaArticle;
import fr.legrain.article.titretransport.model.TaTitreTransport;
import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaPreferencesServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.tiers.service.remote.ITaEspaceClientServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaParamEspaceClientServiceRemote;
import fr.legrain.document.model.TaReglement;
import fr.legrain.dossier.model.TaInfoEntreprise;
import fr.legrain.email.service.LgrEmailSMTPService;
import fr.legrain.general.model.TaFichier;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.ModeObjetEcranServeur;
import fr.legrain.tiers.dto.TaEspaceClientDTO;
import fr.legrain.tiers.dto.TaTiersDTO;
import fr.legrain.tiers.model.TaParamEspaceClient;

@Named
@ViewScoped 
public class ParametreCompteClientController implements Serializable {

	@Inject @Named(value="tabListModelCenterBean")
	private TabListModelBean tabsCenter;
	
	protected ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();
	
	@EJB private ITaPreferencesServiceRemote taPreferencesService;
	@EJB private LgrEmailSMTPService lgrEmail;
	@EJB private ITaInfoEntrepriseServiceRemote taInfoEntrepriseService;
	@EJB private ITaParamEspaceClientServiceRemote taParamEspaceClientService;
	protected @EJB ITaArticleServiceRemote taArticleService;
	
	private @EJB ITaEspaceClientServiceRemote taEspaceClientService;
	
	private TaParamEspaceClient taParamEspaceClient = null;
	
	private List<TaEspaceClientDTO> values = new ArrayList<>();
	
	private List<TaEspaceClientDTO> selectedEspaceClientDTOs = new ArrayList<>();
	private TaEspaceClientDTO selectedEspaceClientDTO = null;
	
	private StreamedContent logoLogin;
	private StreamedContent logoHeader;
	private StreamedContent logoPagesSimples;
	private StreamedContent imageBackgroundLogin;
	private StreamedContent pdfCgv;
	
	private TaArticle taArticleFdp;
	private TaArticleDTO taArticleFdpDTO;
	
	@PostConstruct
	public void init() {
		refresh();
	}

	public void refresh(){
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
		taParamEspaceClient = taParamEspaceClientService.findInstance();
		values = taEspaceClientService.findAllLight();
		refresLogo();
		refresLogoHeader();
		refresLogoPageSimple();
		refresImageBackgroundLogin();
		if(taParamEspaceClient.getTaArticleFdp()!=null) {
			try {
				taArticleFdpDTO = taArticleService.findByIdDTO(taParamEspaceClient.getTaArticleFdp().getIdArticle());
			} catch (FinderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void refresLogo() {
		try {
			logoLogin = null;
			if(taParamEspaceClient!=null && taParamEspaceClient.getLogoLogin()!=null) {
				InputStream stream = new ByteArrayInputStream(taParamEspaceClient.getLogoLogin()); 
				//logo = new DefaultStreamedContent(stream, "image/png");
				logoLogin = new DefaultStreamedContent(stream);
				PrimeFaces.current().ajax().update("@widgetVar(panelLogo)");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void refresLogoHeader() {
		try {
			logoHeader = null;
			if(taParamEspaceClient!=null && taParamEspaceClient.getLogoHeader()!=null) {
				InputStream stream = new ByteArrayInputStream(taParamEspaceClient.getLogoHeader()); 
				//logo = new DefaultStreamedContent(stream, "image/png");
				logoHeader = new DefaultStreamedContent(stream);
				PrimeFaces.current().ajax().update("@widgetVar(panelLogoHeader)");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void refresLogoPageSimple() {
		try {
			logoPagesSimples = null;
			if(taParamEspaceClient!=null && taParamEspaceClient.getLogoPagesSimples()!=null) {
				InputStream stream = new ByteArrayInputStream(taParamEspaceClient.getLogoPagesSimples()); 
				//logo = new DefaultStreamedContent(stream, "image/png");
				logoPagesSimples = new DefaultStreamedContent(stream);
				PrimeFaces.current().ajax().update("@widgetVar(panelLogoPageSimple)");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void refresImageBackgroundLogin() {
		try {
			imageBackgroundLogin = null;
			if(taParamEspaceClient!=null && taParamEspaceClient.getImageBackgroundLogin()!=null) {
				InputStream stream = new ByteArrayInputStream(taParamEspaceClient.getImageBackgroundLogin()); 
				//logo = new DefaultStreamedContent(stream, "image/png");
				imageBackgroundLogin = new DefaultStreamedContent(stream);
				PrimeFaces.current().ajax().update("@widgetVar(panelImageBackgroundLogin)");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void supprimerLogo(ActionEvent e) {
		taParamEspaceClient.setLogoLogin(null);
		actEnregistrer(null);
		refresLogo();
	}
	
	public void supprimerLogoHeader(ActionEvent e) {
		taParamEspaceClient.setLogoHeader(null);
		actEnregistrer(null);
		refresLogoHeader();
	}
	
	public void supprimerLogoPageSimple(ActionEvent e) {
		taParamEspaceClient.setLogoPagesSimples(null);
		actEnregistrer(null);
		refresLogoPageSimple();
	}
	
	public void supprimerImageBackgroundLogin(ActionEvent e) {
		taParamEspaceClient.setImageBackgroundLogin(null);
		actEnregistrer(null);
		refresImageBackgroundLogin();
	}
	
	public void handleFileUpload(FileUploadEvent event) {
        FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
        FacesContext.getCurrentInstance().addMessage(null, message);
        
        try {
        	
        	taParamEspaceClient.setLogoLogin(IOUtils.toByteArray(event.getFile().getInputStream()));
        	
//			fichier.setBlobFichierMiniature(fichier.resizeImage(IOUtils.toByteArray(event.getFile().getInputstream()), event.getFile().getFileName(), 640, 480));
        	int hMax = 80;
        	int lMax = 350;
        	Image image = ImageIO.read(event.getFile().getInputStream());
        	BufferedImage bi = (BufferedImage)image; //TaFichier.resizeImageMax(image,lMax,hMax);
        	
        	ByteArrayOutputStream baos = new ByteArrayOutputStream();
        	String type = "png";
        	if(event.getFile().getFileName().endsWith(".jpg") || event.getFile().getFileName().endsWith(".jpeg")) {
        		type = "jpg";
        	} else if(event.getFile().getFileName().endsWith(".png")) {
        		type = "png";
        	} else if(event.getFile().getFileName().endsWith(".gif")) {
        		type = "gif";
        	} else if(event.getFile().getFileName().endsWith(".svg")) {
        		type = "svg";
        	}
        	ImageIO.write(bi, type, baos);
        	byte[] bytes = baos.toByteArray();
        	
        	taParamEspaceClient.setLogoLogin(bytes);
        	
        	actEnregistrer(null);
    		refresLogo();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void handleFileUploadHeader(FileUploadEvent event) {
        FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
        FacesContext.getCurrentInstance().addMessage(null, message);
        
        try {
        	
        	taParamEspaceClient.setLogoHeader(IOUtils.toByteArray(event.getFile().getInputStream()));
        	
//			fichier.setBlobFichierMiniature(fichier.resizeImage(IOUtils.toByteArray(event.getFile().getInputstream()), event.getFile().getFileName(), 640, 480));
        	int hMax = 80;
        	int lMax = 350;
        	Image image = ImageIO.read(event.getFile().getInputStream());
        	BufferedImage bi = (BufferedImage)image; //TaFichier.resizeImageMax(image,lMax,hMax);
        	
        	ByteArrayOutputStream baos = new ByteArrayOutputStream();
        	String type = "png";
        	if(event.getFile().getFileName().endsWith(".jpg") || event.getFile().getFileName().endsWith(".jpeg")) {
        		type = "jpg";
        	} else if(event.getFile().getFileName().endsWith(".png")) {
        		type = "png";
        	} else if(event.getFile().getFileName().endsWith(".gif")) {
        		type = "gif";
        	} else if(event.getFile().getFileName().endsWith(".svg")) {
        		type = "svg";
        	}
        	ImageIO.write(bi, type, baos);
        	byte[] bytes = baos.toByteArray();
        	
        	taParamEspaceClient.setLogoHeader(bytes);
        	
        	actEnregistrer(null);
    		refresLogoHeader();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void handleFileUploadPageSimple(FileUploadEvent event) {
        FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
        FacesContext.getCurrentInstance().addMessage(null, message);

        try {
        	
        	taParamEspaceClient.setLogoPagesSimples(IOUtils.toByteArray(event.getFile().getInputStream()));
        	
//			fichier.setBlobFichierMiniature(fichier.resizeImage(IOUtils.toByteArray(event.getFile().getInputstream()), event.getFile().getFileName(), 640, 480));
        	int hMax = 80;
        	int lMax = 350;
        	Image image = ImageIO.read(event.getFile().getInputStream());
        	BufferedImage bi = (BufferedImage)image; //TaFichier.resizeImageMax(image,lMax,hMax);
        	
        	ByteArrayOutputStream baos = new ByteArrayOutputStream();
        	String type = "png";
        	if(event.getFile().getFileName().endsWith(".jpg") || event.getFile().getFileName().endsWith(".jpeg")) {
        		type = "jpg";
        	} else if(event.getFile().getFileName().endsWith(".png")) {
        		type = "png";
        	} else if(event.getFile().getFileName().endsWith(".gif")) {
        		type = "gif";
        	} else if(event.getFile().getFileName().endsWith(".svg")) {
        		type = "svg";
        	}
        	ImageIO.write(bi, type, baos);
        	byte[] bytes = baos.toByteArray();
        	
        	taParamEspaceClient.setLogoPagesSimples(bytes);
        	
        	actEnregistrer(null);
    		refresLogoHeader();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void handleFileUploadImageBackgroundLogin(FileUploadEvent event) {
        FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
        FacesContext.getCurrentInstance().addMessage(null, message);
        
        try {
        	
        	taParamEspaceClient.setImageBackgroundLogin(IOUtils.toByteArray(event.getFile().getInputStream()));
        	
//			fichier.setBlobFichierMiniature(fichier.resizeImage(IOUtils.toByteArray(event.getFile().getInputstream()), event.getFile().getFileName(), 640, 480));
        	int hMax = 80;
        	int lMax = 350;
        	Image image = ImageIO.read(event.getFile().getInputStream());
        	BufferedImage bi = (BufferedImage)image; //TaFichier.resizeImageMax(image,lMax,hMax);
        	
        	ByteArrayOutputStream baos = new ByteArrayOutputStream();
        	String type = "png";
        	if(event.getFile().getFileName().endsWith(".jpg") || event.getFile().getFileName().endsWith(".jpeg")) {
        		type = "jpg";
        	} else if(event.getFile().getFileName().endsWith(".png")) {
        		type = "png";
        	} else if(event.getFile().getFileName().endsWith(".gif")) {
        		type = "gif";
        	} else if(event.getFile().getFileName().endsWith(".svg")) {
        		type = "svg";
        	}
        	ImageIO.write(bi, type, baos);
        	byte[] bytes = baos.toByteArray();
        	
        	taParamEspaceClient.setImageBackgroundLogin(bytes);
        	
        	actEnregistrer(null);
    		refresLogoHeader();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void handleFileUploadCgv(FileUploadEvent event) {
        FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
        FacesContext.getCurrentInstance().addMessage(null, message);
        
        try {
        	
        	taParamEspaceClient.setCgvFileCatWeb(IOUtils.toByteArray(event.getFile().getInputStream()));
        	
//        	Image image = ImageIO.read(event.getFile().getInputStream());
//        	BufferedImage bi = (BufferedImage)image; //TaFichier.resizeImageMax(image,lMax,hMax);
//        	
//        	ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        	String type = "png";
//        	if(event.getFile().getFileName().endsWith(".jpg") || event.getFile().getFileName().endsWith(".jpeg")) {
//        		type = "jpg";
//        	} else if(event.getFile().getFileName().endsWith(".png")) {
//        		type = "png";
//        	} else if(event.getFile().getFileName().endsWith(".gif")) {
//        		type = "gif";
//        	} else if(event.getFile().getFileName().endsWith(".svg")) {
//        		type = "svg";
//        	}
//        	ImageIO.write(bi, type, baos);
//        	byte[] bytes = baos.toByteArray();
        	
//        	taParamEspaceClient.setCgvFileCatWeb(bytes);
        	
        	actEnregistrer(null);
//    		refresLogoHeader();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public List<TaArticleDTO> articleAutoCompleteDTOLight(String query) {
		List<TaArticleDTO> allValues = taArticleService.findByCodeAndLibelleLight(query.toUpperCase());
		List<TaArticleDTO> filteredValues = new ArrayList<TaArticleDTO>();

		for (int i = 0; i < allValues.size(); i++) {
			TaArticleDTO civ = allValues.get(i);
			if(query.equals("*") || civ.getCodeArticle().toLowerCase().contains(query.toLowerCase())
					|| (civ.getLibellelArticle()!=null && civ.getLibellelArticle().toLowerCase().contains(query.toLowerCase()))
					|| (civ.getLibellecArticle()!=null && civ.getLibellecArticle().toLowerCase().contains(query.toLowerCase()))
					) {
				filteredValues.add(civ);
			}
		}

		return filteredValues;
	}
	
	public void onClearArticle(AjaxBehaviorEvent event){
		taArticleFdp = null;
		taArticleFdpDTO = null;
		taParamEspaceClient.setTaArticleFdp(null);
		actEnregistrer(null);
	}
	
	public void autcompleteSelection(SelectEvent event) {
//		taArticleFdp = null;
//		taArticleFdpDTO = null;
		try {
			taParamEspaceClient.setTaArticleFdp(taArticleService.findById(taArticleFdpDTO.getId()));
			actEnregistrer(null);
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void autoCompleteMapUIToDTO() {
		if(taArticleFdpDTO!=null) {
			//validateUIField(Const.C_CODE_TIERS,taTiersDTO);
			//ta.setCodeTiers(taTiersDTO.getCodeTiers());
		}
		
//		if(selectedEtatDocument!=null) {
//			validateUIField(Const.C_CODE_ETAT,selectedEtatDocument.getCodeEtat());
//			selectedDocumentDTO.setCodeEtat(selectedEtatDocument.getCodeEtat());
//			masterEntity.setTaEtat(selectedEtatDocument);
//		}
	}

	public void autoCompleteMapDTOtoUI() {
		try {
			taArticleFdp = null;
			taArticleFdpDTO = null;

//			if(selectedDocumentDTO.getCodeTiers()!=null && !selectedDocumentDTO.getCodeTiers().equals("")) {
//				taTiers = taTiersService.findByCode(selectedDocumentDTO.getCodeTiers());
//				taTiersDTO = mapperModelToUITiers.map(taTiers, TaTiersDTO.class);
//			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public TabListModelBean getTabsCenter() {
		return tabsCenter;
	}

	public void setTabsCenter(TabListModelBean tabsCenter) {
		this.tabsCenter = tabsCenter;
	}
	
	public void actAnnuler(ActionEvent actionEvent) {
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
		
	   	FacesContext context = FacesContext.getCurrentInstance();  
	    context.addMessage(null, new FacesMessage("Gestion Compte Client", "actAnnuler")); 
	}
	
	public void actModifier(ActionEvent actionEvent) {
		modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
		refresLogo();
		refresLogoHeader();
		refresLogoPageSimple();
		refresImageBackgroundLogin();
	}
	
	public void actEnregistrer(ActionEvent actionEvent) {
		taParamEspaceClient = taParamEspaceClientService.merge(taParamEspaceClient);
		refresLogo();
		refresLogoHeader();
		refresLogoPageSimple();
		refresImageBackgroundLogin();
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}

	public TaParamEspaceClient getTaParamEspaceClient() {
		return taParamEspaceClient;
	}

	public void setTaParamEspaceClient(TaParamEspaceClient taParamEspaceClient) {
		this.taParamEspaceClient = taParamEspaceClient;
	}

	public StreamedContent getLogoLogin() {
		return logoLogin;
	}

	public void setLogoLogin(StreamedContent logoLogin) {
		this.logoLogin = logoLogin;
	}

	public List<TaEspaceClientDTO> getValues() {
		return values;
	}

	public void setValues(List<TaEspaceClientDTO> values) {
		this.values = values;
	}

	public List<TaEspaceClientDTO> getSelectedEspaceClientDTOs() {
		return selectedEspaceClientDTOs;
	}

	public void setSelectedEspaceClientDTOs(List<TaEspaceClientDTO> selectedEspaceClientDTOs) {
		this.selectedEspaceClientDTOs = selectedEspaceClientDTOs;
	}

	public TaEspaceClientDTO getSelectedEspaceClientDTO() {
		return selectedEspaceClientDTO;
	}

	public void setSelectedEspaceClientDTO(TaEspaceClientDTO selectedEspaceClientDTO) {
		this.selectedEspaceClientDTO = selectedEspaceClientDTO;
	}

	public StreamedContent getLogoHeader() {
		return logoHeader;
	}

	public void setLogoHeader(StreamedContent logoLoginHeader) {
		this.logoHeader = logoLoginHeader;
	}

	public StreamedContent getLogoPagesSimples() {
		return logoPagesSimples;
	}

	public void setLogoPagesSimples(StreamedContent logoPagesSimples) {
		this.logoPagesSimples = logoPagesSimples;
	}

	public StreamedContent getImageBackgroundLogin() {
		return imageBackgroundLogin;
	}

	public void setImageBackgroundLogin(StreamedContent imageBackgroundLogin) {
		this.imageBackgroundLogin = imageBackgroundLogin;
	}

	public TaArticle getTaArticleFdp() {
		return taArticleFdp;
	}

	public void setTaArticleFdp(TaArticle taArticleFdp) {
		this.taArticleFdp = taArticleFdp;
	}

	public TaArticleDTO getTaArticleFdpDTO() {
		return taArticleFdpDTO;
	}

	public void setTaArticleFdpDTO(TaArticleDTO taArticleFdpDTO) {
		this.taArticleFdpDTO = taArticleFdpDTO;
	}

	public StreamedContent getPdfCgv() {
		return pdfCgv;
	}

	public void setPdfCgv(StreamedContent pdfCgv) {
		this.pdfCgv = pdfCgv;
	}
	
	
}
