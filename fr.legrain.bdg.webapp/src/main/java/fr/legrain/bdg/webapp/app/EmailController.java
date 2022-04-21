package fr.legrain.bdg.webapp.app;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.enterprise.context.Dependent;
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
import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

import fr.legrain.article.dto.TaFamilleDTO;
import fr.legrain.article.dto.TaTTvaDTO;
import fr.legrain.article.dto.TaTvaDTO;
import fr.legrain.article.dto.TaUniteDTO;
import fr.legrain.article.model.TaMarqueArticle;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.bdg.droits.service.remote.ITaUtilisateurServiceRemote;
import fr.legrain.bdg.email.service.remote.ITaEtiquetteEmailServiceRemote;
import fr.legrain.bdg.email.service.remote.ITaMessageEmailServiceRemote;
import fr.legrain.bdg.email.service.remote.ITaPiecesJointesEmailServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaCompteServiceWebExterneServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaEmailServiceRemote;
import fr.legrain.bdg.webapp.ConstWeb;
import fr.legrain.document.dto.TaTPaiementDTO;
import fr.legrain.document.model.TaTPaiement;
import fr.legrain.droits.service.SessionInfo;
import fr.legrain.email.dto.TaContactMessageEmailDTO;
import fr.legrain.email.dto.TaEtiquetteEmailDTO;
import fr.legrain.email.dto.TaMessageEmailDTO;
import fr.legrain.email.dto.TaPieceJointeEmailDTO;
import fr.legrain.email.model.TaContactMessageEmail;
import fr.legrain.email.model.TaEtiquetteEmail;
import fr.legrain.email.model.TaMessageEmail;
import fr.legrain.email.model.TaPieceJointeEmail;
import fr.legrain.email.service.EnvoiEmailService;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.ModeObjetEcranServeur;
import fr.legrain.tiers.dto.TaCPaiementDTO;
import fr.legrain.tiers.dto.TaEmailDTO;
import fr.legrain.tiers.dto.TaTCiviliteDTO;
import fr.legrain.tiers.dto.TaTEmailDTO;
import fr.legrain.tiers.dto.TaTEntiteDTO;
import fr.legrain.tiers.model.TaCPaiement;
import fr.legrain.tiers.model.TaEmail;
import fr.legrain.tiers.model.TaFamilleTiers;
import fr.legrain.tiers.model.TaTEmail;
import fr.legrain.tiers.model.TaTTvaDoc;
import fr.legrain.tiers.model.TaTiers;

@Named
//@ViewScoped  
@Dependent
public class EmailController extends AbstractController implements Serializable {  

	private static final long serialVersionUID = -3318113784412169719L;

	@Inject @Named(value="tabListModelCenterBean")
	private TabListModelBean tabsCenter;

	@Inject @Named(value="tabViewsBean")
	private TabViewsBean tabViews;

	private String paramId;
	
	private String cssEtiquetteEmail;
	
	private TaTiers masterEntity;

	private TabView tabViewTiers; //Le Binding sur les onglets "secondaire" semble ne pas fonctionné, les onglets ne sont pas générés au bon moment avec le c:forEach

	private List<TaMessageEmailDTO> values; 

	protected ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();

//	private @EJB LgrEmailSMTPService lgrEmail;

//	private @EJB LgrMailjetService lgrMailjetService;
	@EJB private EnvoiEmailService emailServiceFinder;
	protected @EJB ITaInfoEntrepriseServiceRemote taInfoEntrepriseService;

	private @EJB ITaMessageEmailServiceRemote taTiersService;
	private @EJB ITaUtilisateurServiceRemote taUtilisateurService;
	private @EJB ITaMessageEmailServiceRemote taMessageEmailService;
	private @EJB ITaEtiquetteEmailServiceRemote taEtiquetteEmailService;
	private @EJB ITaPiecesJointesEmailServiceRemote taPiecesJointesEmailService;
	private @EJB ITaCompteServiceWebExterneServiceRemote taCompteServiceWebExterneService;
	private @EJB ITaEmailServiceRemote taEmailService;

	private @Inject SessionInfo sessionInfo;

	private StreamedContent streamedPieceJointe;
	private TaPieceJointeEmailDTO selectedTaPieceJointeEmailDTO;

	private TaMessageEmailDTO[] selectedTaMessageEmailDTOs; 
	private TaMessageEmail taMessageEmail = new TaMessageEmail();
	private TaMessageEmailDTO newTaMessageEmailDTO = new TaMessageEmailDTO();
	private TaMessageEmailDTO selectedTaMessageEmailDTO = new TaMessageEmailDTO();

	private TaEmail taEmail;
	private TaTEmail taTEmail;

	private LgrDozerMapper<TaMessageEmailDTO,TaMessageEmail> mapperUIToModel  = new LgrDozerMapper<TaMessageEmailDTO,TaMessageEmail>();
	private LgrDozerMapper<TaMessageEmail,TaMessageEmailDTO> mapperModelToUI  = new LgrDozerMapper<TaMessageEmail,TaMessageEmailDTO>();
	
	private LgrDozerMapper<TaEmailDTO,TaEmail> mapperUIToModelTaEmail  = new LgrDozerMapper<TaEmailDTO,TaEmail>();
	private LgrDozerMapper<TaEmail,TaEmailDTO> mapperModelToUITaEmail  = new LgrDozerMapper<TaEmail,TaEmailDTO>();

	private TaEmailDTO taEmailDTODestinataire;
	private TaEmailDTO taEmailDTOCc;
	private TaEmailDTO taEmailDTOBcc;
	
	private TaTEmailDTO taTEmailDTO;

	private List<TaEmailDTO> taEmailDTODestinataires;
	private List<TaEmailDTO> taEmailDTOCcs;
	private List<TaEmailDTO> taEmailDTOBccs;
	
	private List<String> adressesDestinataire;
	private List<String> adressesCc;
	private List<String> adressesBcc;
	
	private boolean showPlusDestinataire = false;
	private boolean showPlusCc = false;
	private boolean showPlusBcc = false;
	
	private EmailParam param;
	private EmailPieceJointeParam[] piecesJointes;
	
//	private final MimetypesFileTypeMap fileTypeMap = new MimetypesFileTypeMap();  
	
	private MenuModel model;
	
	
	public EmailController() {  
		
	}  

	@PostConstruct
	public void postConstruct(){
		//refresh(); //appeler par le f:metadata  preRenderView pour eviter un appel sans paramètre (tiers) trop fréquent et trop lourd
	}
	
	public void refresh(ActionEvent e){
		refresh();
	}

	public void refresh(){
//		values = taMessageEmailService.findAllLight();
		if(masterEntity==null) {
			//values = taMessageEmailService.selectAllDTO();
			values = taMessageEmailService.findAllLight();
		} else {
			//values = taMessageEmailService.selectAllDTO(masterEntity.getIdTiers());
			values = taMessageEmailService.findAllLight(masterEntity.getIdTiers());
			//masterEntity=null;
		}
		
		if(values==null || values.isEmpty()) {
			selectedTaMessageEmailDTO = new TaMessageEmailDTO();
		}
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
		
		initMenu();
	}
	
	public void initMenu() {
		model = new DefaultMenuModel();

        DefaultMenuItem item = null;

        DefaultSubMenu secondSubmenu = new DefaultSubMenu("Etiquette");
        //secondSubmenu.setId("idAZERTY");
        
        List<TaEtiquetteEmail> listeEtiquette = taEtiquetteEmailService.selectAll();
        if(listeEtiquette!=null) {
        	for (TaEtiquetteEmail taEtiquetteEmail : listeEtiquette) {
              //item = new DefaultMenuItem(taEtiquetteEmail.getCode()+" - "+selectedTaMessageEmailDTO.getSubject());
        	  item = new DefaultMenuItem( (taEtiquetteEmail.getOrdre()!=null?taEtiquetteEmail.getCode():"")+" - "+taEtiquetteEmail.getCode());
              item.setIcon("ui-icon-tag");
              item.setCommand("#{emailController.actChangeEtiquette('"+taEtiquetteEmail.getCode()+"')}");
              item.setUpdate("@widgetVar(emailEnvoyeForm)");
              item.setStyle("color: #"+taEtiquetteEmail.getCouleur());
              secondSubmenu.getElements().add(item);
			}
        }
 
        model.addElement(secondSubmenu);
        
        
        for (TaEtiquetteEmail taEtiquetteEmail : listeEtiquette) {
			String cssClassName = "."+TaMessageEmailDTO.C_DEBUT_NOM_CLASSE_CSS_ETIQUETTE_EMAIL+taEtiquetteEmail.getCode().replaceAll(" ", "");
			cssEtiquetteEmail += cssClassName+"{ "
							+ "background-color: #"+taEtiquetteEmail.getCouleur()+" !important;"
							//+ "border-color: #"+taEtiquetteEmail.getCouleur()+" !important;"
							+ "}";
		}
	}
	
	public void onContextMenu(SelectEvent event) {  
		initMenu();
	}

	public List<TaMessageEmailDTO> getValues(){  
		return values;
	}

	public void actAnnuler(ActionEvent actionEvent) {
		try {
//			switch (modeEcran.getMode()) {
//			case C_MO_INSERTION:
//				if(selectedTaMessageEmailDTO.getCodeTiers()!=null) {
//					taTiersService.annuleCode(selectedTaMessageEmailDTO.getCodeTiers());
//				}
//				taTiers = new TaMessageEmail();
//				mapperModelToUI.map(taTiers,selectedTaMessageEmailDTO );
//				selectedTaMessageEmailDTO=new TaMessageEmailDTO();
//
//				if(!values.isEmpty()) selectedTaMessageEmailDTO = values.get(0);
//				onRowSelect(null);
//				break;
//			case C_MO_EDITION:
//				if(selectedTaMessageEmailDTO.getId()!=null && selectedTaMessageEmailDTO.getId()!=0){
//					taTiers = taTiersService.findById(selectedTaMessageEmailDTO.getId());
//					selectedTaMessageEmailDTO=taTiersService.findByIdDTO(selectedTaMessageEmailDTO.getId());
//				}				
//				break;
//
//			default:
//				break;
//			}
			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
			mapperModelToUI.map(taMessageEmail, selectedTaMessageEmailDTO);

			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Tiers", "actAnnuler"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public void actChangeEtiquette(String codeEtiquette) {
		System.out.println(codeEtiquette);
		System.out.println(selectedTaMessageEmailDTO.getSubject());
		try {
			TaEtiquetteEmail etiquette = taEtiquetteEmailService.findByCode(codeEtiquette);
			if(etiquette!=null) {
				TaEtiquetteEmail etiqCherche = null;
				if(taMessageEmail.getEtiquettes()!=null) {
					for (TaEtiquetteEmail e : taMessageEmail.getEtiquettes()) {
						if(e.getCode().equals(etiquette.getCode())) {
							etiqCherche = e;
						}
					}
					if(etiqCherche!=null) {
						taMessageEmail.getEtiquettes().remove(etiqCherche);
					} else {
						taMessageEmail.getEtiquettes().add(etiquette);
					}
				} else {
					taMessageEmail.setEtiquettes(new HashSet<>());
					taMessageEmail.getEtiquettes().add(etiquette);
				}
				
				taMessageEmail = taMessageEmailService.merge(taMessageEmail);
				updateTab();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}

	public void autoCompleteMapUIToDTO() {
//		if(taTEmailDTO!=null) {
//			validateUIField(Const.C_CODE_T_EMAIL,taTEmailDTO.getCodeTEmail());
//			selectedTaMessageEmailDTO.setCodeTTiers(taTEmailDTO.getCodeTEmail());
//		}else {
//			selectedTaMessageEmailDTO.setCodeTTiers(null);
//		}
//		
//		if(taEmailDTO!=null) {
//			validateUIField(Const.C_CODE_eT_ENTITE,taEmailDTO.getCodeTEntite());
//			selectedTaMessageEmailDTO.setCodeTEntite(taEmailDTO.getCodeTEntite());
//		}else {
//			selectedTaMessageEmailDTO.setCodeTEntite(null);
//		}
		
	}

	public void autoCompleteMapDTOtoUI() {
		try {
			taEmail = null;
			taTEmail = null;

			taTEmailDTO = null;
			taEmailDTODestinataire = null;

//			if(selectedTaMessageEmailDTO.getCodeTTiers()!=null && !selectedTaMessageEmailDTO.getCodeTTiers().equals("")) {
//				taEmail = taTTiersService.findByCode(selectedTaMessageEmailDTO.getCodeTTiers());
//				taTEmailDTO=taTTiersService.findByCodeDTO(selectedTaMessageEmailDTO.getCodeTTiers());
//			}
//			if(selectedTaMessageEmailDTO.getCodeTCivilite()!=null && !selectedTaMessageEmailDTO.getCodeTCivilite().equals("")) {
//				taTEmail = taTCivlicteService.findByCode(selectedTaMessageEmailDTO.getCodeTCivilite());
//				taTCiviliteDTO = taTCivlicteService.findByCodeDTO(selectedTaMessageEmailDTO.getCodeTCivilite());
//			}
				
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void actEnregistrer(ActionEvent actionEvent) {		
		//TaMessageEmail taTiers = new TaMessageEmail();


//		if(taTiers.getTaAdresse()!=null){
//			try {
//				if(taTiers.getTaAdresse()!=null && taTiers.getTaAdresse().getTaTAdr()==null){
//					TaTAdr tAdr= taTAdresseService.findByCode(ConstPreferencesTiers.TADR_DEFAUT);
//					taTiers.getTaAdresse().setTaTAdr(tAdr);
//				}
//				int ordre=taAdresseService.rechercheOdrePourType(taTiers.getTaAdresse().getTaTAdr().getCodeTAdr(),taTiers.getCodeTiers());
//				if(taTiers.getTaAdresse().getOrdre()==null || taTiers.getTaAdresse().getOrdre()==0)taTiers.getTaAdresse().setOrdre(ordre);
//			} catch (FinderException e) {
//
//			}
//		}
//		autoCompleteMapUIToDTO();
//		initAdresseNull();
//		if(taTiers.getTaEmail()==null){
//			selectedTaMessageEmailDTO.setAdresseEmail(null);
//		}
//		if(taTiers.getTaTelephone()==null){
//			selectedTaMessageEmailDTO.setNumeroTelephone(null);
//		}
//		if(taTiers.getTaWeb()==null){
//			selectedTaMessageEmailDTO.setAdresseWeb(null);
//		}

		mapperUIToModel.map(selectedTaMessageEmailDTO, taMessageEmail);

		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Tiers", "actEnregistrer"));
		}
	}

	public void actInserer(ActionEvent actionEvent) {
		try {
			selectedTaMessageEmailDTOs= new TaMessageEmailDTO[]{};
			selectedTaMessageEmailDTO = new TaMessageEmailDTO();
			taMessageEmail = new TaMessageEmail();

			String codeTiersDefaut = "C";

			
			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Tiers", "actInserer"));
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

		modeEcran.setMode(EnumModeObjet.C_MO_EDITION);

		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Tiers", "actModifier"));
		}
	}

	public void actAide(ActionEvent actionEvent) {

		//			PrimeFaces.current().dialog().openDynamic("aide");

		Map<String,Object> options = new HashMap<String, Object>();
		options.put("modal", true);
		options.put("draggable", false);
		options.put("resizable", false);
		options.put("contentHeight", 320);
		Map<String,List<String>> params = null;
		PrimeFaces.current().dialog().openDynamic("aide", options, params);

		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Tiers", "actAide"));
		}
	}

	public void nouveau(ActionEvent actionEvent) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_EMAILS_ENVOYES);
		b.setTitre("Gestion des emails envoyés");
		b.setTemplate("/gestion_emails_envoyes.xhtml");
		b.setIdTab(LgrTab.ID_TAB_GESTION_EMAILS_ENVOYES);
		b.setStyle(LgrTab.CSS_CLASS_TAB_EMAILS_ENVOYES);
		b.setParamId("0");

		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
		actInserer(actionEvent);

		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Tiers", 
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
		TaMessageEmail taTiers = new TaMessageEmail();
		try {
			if(selectedTaMessageEmailDTO!=null && selectedTaMessageEmailDTO.getId()!=null){
				taTiers = taTiersService.findById(selectedTaMessageEmailDTO.getId());
			}

			taTiersService.remove(taTiers);
			values.remove(selectedTaMessageEmailDTO);

			if(!values.isEmpty()) {
				selectedTaMessageEmailDTO = values.get(0);
			}else {
				selectedTaMessageEmailDTO=new TaMessageEmailDTO();
			}
			updateTab();
			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Tiers", "actSupprimer"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Tiers", e.getCause().getCause().getCause().getCause().getMessage()));
		} 	    
	}
	
	public void actPlusDeDestinataire(ActionEvent actionEvent) {
		showPlusDestinataire = !showPlusDestinataire;
	}
	
	public void actPlusDeCc(ActionEvent actionEvent) {
		showPlusCc = !showPlusCc;
	}

	public void actPlusDeBcc(ActionEvent actionEvent) {
		showPlusBcc = !showPlusBcc;
	}
	
	public void actSupprimerPieceJointe(ActionEvent actionEvent) {
		
	}
	
	public void actSupprimerPieceJointe(EmailPieceJointeParam pj) {
		EmailPieceJointeParam[] temp = new EmailPieceJointeParam[piecesJointes.length-1];
		int j = 0;
		for (int i = 0; i < temp.length; i++) {
			if(!piecesJointes[i].getNomFichier().equals(pj.getNomFichier())) {
				temp[i]=piecesJointes[j];
			} else {
				j++;
			}
			j++;
		}
		piecesJointes = temp;
	}
	
	public void actSupprimerPieceJointe() {
		//actSupprimerPieceJointe(null);
	}
	
	public void actFermerDialog(ActionEvent actionEvent) {
		//PrimeFaces.current().dialog().closeDynamic(null);
		
		//PrimeFaces.current().dialog().closeDynamic(listeControle);
		PrimeFaces.current().dialog().closeDynamic(null);
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
		selectedTaMessageEmailDTO=new TaMessageEmailDTO();
		selectedTaMessageEmailDTOs=new TaMessageEmailDTO[]{selectedTaMessageEmailDTO};

	}

	public boolean pasDejaOuvert() {
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_EMAILS_ENVOYES);
		return tabsCenter.ongletDejaOuvert(b)==null;
	} 

	public void onRowSimpleSelect(SelectEvent event) {

		if(pasDejaOuvert()==false){
			onRowSelect(event);

			autoCompleteMapDTOtoUI();
//			if(ConstWeb.DEBUG) {
//				FacesContext context = FacesContext.getCurrentInstance();  
//				context.addMessage(null, new FacesMessage("Tiers", 
//						"Chargement du tiers N°"+selectedTaMessageEmailDTO.getCodeTiers()
//						)); }
		} else {
			tabViews.selectionneOngletCentre(LgrTab.TYPE_TAB_EMAILS_ENVOYES);
		}
	} 
	public void updateTab(){
		try {

			if(selectedTaMessageEmailDTOs!=null && selectedTaMessageEmailDTOs.length>0) {
				selectedTaMessageEmailDTO = selectedTaMessageEmailDTOs[0];
			}
			if(selectedTaMessageEmailDTO.getId()!=null && selectedTaMessageEmailDTO.getId()!=0) {
				taMessageEmail = taTiersService.findById(selectedTaMessageEmailDTO.getId());
			}
			
			if(selectedTaMessageEmailDTO!=null) {
				mapperModelToUI.map(taMessageEmail, selectedTaMessageEmailDTO);	
				
				/*
				 * 
				 */
				//DEST
				selectedTaMessageEmailDTO.setDestinataires(new ArrayList<>());
				for (TaContactMessageEmail dest : taMessageEmail.getDestinataires()) {
					TaContactMessageEmailDTO dto = new TaContactMessageEmailDTO();
					dto.setAdresseEmail(dest.getAdresseEmail());
					dto.setId(dest.getIdContactMessageEmail());
					if(dest.getTaTiers()!=null) {
						dto.setIdTiers(dest.getTaTiers().getIdTiers());
						dto.setCodeTiers(dest.getTaTiers().getCodeTiers());
						dto.setNomTiers(dest.getTaTiers().getNomTiers());
						dto.setPrenomTiers(dest.getTaTiers().getPrenomTiers());
					}
					selectedTaMessageEmailDTO.getDestinataires().add(dto);
					
				}
				
				//CC
				selectedTaMessageEmailDTO.setCc(new ArrayList<>());
				for (TaContactMessageEmail dest : taMessageEmail.getCc()) {
					TaContactMessageEmailDTO dto = new TaContactMessageEmailDTO();
					dto.setAdresseEmail(dest.getAdresseEmail());
					dto.setId(dest.getIdContactMessageEmail());
					if(dest.getTaTiers()!=null) {
						dto.setIdTiers(dest.getTaTiers().getIdTiers());
						dto.setCodeTiers(dest.getTaTiers().getCodeTiers());
						dto.setNomTiers(dest.getTaTiers().getNomTiers());
						dto.setPrenomTiers(dest.getTaTiers().getPrenomTiers());
					}
					selectedTaMessageEmailDTO.getCc().add(dto);
					
				}
				
				//BCC
				selectedTaMessageEmailDTO.setBcc(new ArrayList<>());
				for (TaContactMessageEmail dest : taMessageEmail.getBcc()) {
					TaContactMessageEmailDTO dto = new TaContactMessageEmailDTO();
					dto.setAdresseEmail(dest.getAdresseEmail());
					dto.setId(dest.getIdContactMessageEmail());
					if(dest.getTaTiers()!=null) {
						dto.setIdTiers(dest.getTaTiers().getIdTiers());
						dto.setCodeTiers(dest.getTaTiers().getCodeTiers());
						dto.setNomTiers(dest.getTaTiers().getNomTiers());
						dto.setPrenomTiers(dest.getTaTiers().getPrenomTiers());
					}
					selectedTaMessageEmailDTO.getBcc().add(dto);
					
				}
				
				//PJ
				selectedTaMessageEmailDTO.setPiecesJointes(new ArrayList<>());
				for (TaPieceJointeEmail dest : taMessageEmail.getPiecesJointes()) {
					TaPieceJointeEmailDTO dto = new TaPieceJointeEmailDTO();
					dto.setNomFichier(dest.getNomFichier());
					dto.setTaille(dest.getTaille());
					dto.setTypeMime(dest.getTypeMime());
					dto.setFichier(dest.getFichier());
					dto.setFichierImageMiniature(dest.getFichierImageMiniature());
	
					selectedTaMessageEmailDTO.getPiecesJointes().add(dto);
				}
	
				//Etiquettes
				selectedTaMessageEmailDTO.setEtiquettes(new ArrayList<>());
				for (TaEtiquetteEmail dest : taMessageEmail.getEtiquettes()) {
					TaEtiquetteEmailDTO dto = new TaEtiquetteEmailDTO();
					dto.setCode(dest.getCode());
					dto.setLibelle(dest.getLibelle());
					dto.setCouleur(dest.getCouleur());
					dto.setDescription(dest.getDescription());
					dto.setVisible(dest.isVisible());
	
					selectedTaMessageEmailDTO.getEtiquettes().add(dto);
				}
				
				
				//Status du service d'envoi externe (si c'est le cas)
				taMessageEmail = emailServiceFinder.updateInfosMessageExterne(taMessageEmail);
				if(taMessageEmail.getStatusServiceExterne()!=null && taMessageEmail.getStatusServiceExterne().equals("")) {
					selectedTaMessageEmailDTO.setStatusServiceExterne(taMessageEmail.getStatusServiceExterne());
				}
				String infoTech = "";
				infoTech += "Envoi : <br/>";
				infoTech += taMessageEmail.getInfosService()!=null ? taMessageEmail.getInfosService() : "";
				infoTech += "<br/> =========== <br/>";
				infoTech += "Détails : <br/>";
				infoTech += taMessageEmail.getEtatMessageServiceExterne()!=null ? taMessageEmail.getEtatMessageServiceExterne() : "";
				selectedTaMessageEmailDTO.setInfosTechniques(infoTech);
					
	
				autoCompleteMapDTOtoUI();
			} else {
				selectedTaMessageEmailDTO = new TaMessageEmailDTO();
			}

		} catch (FinderException e) {
			e.printStackTrace();
		}
	}

	public void onTabShow() {
		System.out.println("ArticleController.onTabShow()");
	}

	public void onTabChange(TabChangeEvent event) {
		//			FacesMessage msg = null;
		//			if(event.getTab()!=null) {
		//				msg = new FacesMessage("Tab Changed ArticleController", "Active Tab: " + event.getTab().getTitle());
		//			} else {
		//				msg = new FacesMessage("Tab Changed ArticleController", "Active Tab: ");
		//			}
		//	        FacesContext.getCurrentInstance().addMessage(null, msg);

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
		return modeEcran.dataSetEnModif() ||  selectedTaMessageEmailDTO.getId()==0;
	}

	public void onRowSelect(SelectEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_EMAILS_ENVOYES);
		b.setTitre("Gestion des emails envoyés");
		b.setTemplate("/gestion_emails_envoyes.xhtml");
		b.setIdTab(LgrTab.ID_TAB_GESTION_EMAILS_ENVOYES);
		b.setStyle(LgrTab.CSS_CLASS_TAB_EMAILS_ENVOYES);
//		b.setParamId(LibConversion.integerToString(selectedTaMessageEmailDTO.getId()));

//		tabsCenter.ajouterOnglet(b);
//		tabViews.selectionneOngletCentre(b);


		updateTab();
		//scrollToTop();

		if(ConstWeb.DEBUG) {
//			FacesContext context = FacesContext.getCurrentInstance();  
//			context.addMessage(null, new FacesMessage("Tiers", 
//					"Chargement du tiers N°"+selectedTaMessageEmailDTO.getCodeTiers()
//					)); 
		}
	} 

	public void onRowSelectResponsive(SelectEvent event) {		
//		autoCompleteMapDTOtoUI();
//
//		try {
//			taMessageEmail = taTiersService.findById(selectedTaMessageEmailDTO.getId());
//			mapperModelToUI.map(taMessageEmail, selectedTaMessageEmailDTO);
//		} catch (FinderException e) {
//			e.printStackTrace();
//		}
//
//		if(ConstWeb.DEBUG) {
////			FacesContext context = FacesContext.getCurrentInstance();  
////			context.addMessage(null, new FacesMessage("Tiers", 
////					"Chargement du tiers N°"+selectedTaMessageEmailDTO.getCodeTiers()
////					)); 
//			}
	} 

	public boolean editable() {
		return !modeEcran.dataSetEnModif();
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
				if(selectedTaMessageEmailDTO!=null && selectedTaMessageEmailDTO.getId()!=null  && selectedTaMessageEmailDTO.getId()!=0 ) retour = false;
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

	public TaMessageEmailDTO[] getSelectedTaMessageEmailDTOs() {
		return selectedTaMessageEmailDTOs;
	}

	public void setSelectedTaMessageEmailDTOs(TaMessageEmailDTO[] selectedTaMessageEmailDTOs) {
		this.selectedTaMessageEmailDTOs = selectedTaMessageEmailDTOs;
	}

	public TaMessageEmailDTO getNewTaMessageEmailDTO() {
		return newTaMessageEmailDTO;
	}

	public void setNewTaMessageEmailDTO(TaMessageEmailDTO newTaMessageEmailDTO) {
		this.newTaMessageEmailDTO = newTaMessageEmailDTO;
	}

	public TaMessageEmailDTO getSelectedTaMessageEmailDTO() {
		return selectedTaMessageEmailDTO;
	}

	public void setSelectedTaMessageEmailDTO(TaMessageEmailDTO selectedTaMessageEmailDTO) {
		this.selectedTaMessageEmailDTO = selectedTaMessageEmailDTO;
		if(this.selectedTaMessageEmailDTO!=null) {
			updateTab();
		}
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

	public void validateTiers(FacesContext context, UIComponent component, Object value) throws ValidatorException {

		String messageComplet = "";

		try {
			//String selectedRadio = (String) value;

			String nomChamp =  (String) component.getAttributes().get("nomChamp");

			//msg = "Mon message d'erreur pour : "+nomChamp;

			//			  validateUIField(nomChamp,value);
			//			  TaMessageEmailDTO dtoTemp=new TaMessageEmailDTO();
			//			  PropertyUtils.setSimpleProperty(dtoTemp, nomChamp, value);
			//			  taTiersService.validateDTOProperty(dtoTemp, nomChamp, ITaMessageEmailServiceRemote.validationContext );

			//				//validation automatique via la JSR bean validation
			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
			Set<ConstraintViolation<TaMessageEmailDTO>> violations = factory.getValidator().validateValue(TaMessageEmailDTO.class,nomChamp,value);
			if (violations.size() > 0) {
				messageComplet = "Erreur de validation : ";
				//					List<IStatus> statusList = new ArrayList<IStatus>();
				for (ConstraintViolation<TaMessageEmailDTO> cv : violations) {
					//						statusList.add(ValidationStatus.error(cv.getMessage()));
					messageComplet+=" "+cv.getMessage()+"\n";
				}
				throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, messageComplet, messageComplet));
				//					return new MultiStatus(LibrairiesEcranPlugin.PLUGIN_ID, IStatus.ERROR,
				//							//statusList.toArray(new IStatus[statusList.size()]), "Validation errors", null);
				//							statusList.toArray(new IStatus[statusList.size()]), messageComplet, null);
			} else {
				//aucune erreur de validation "automatique", on déclanche les validations du controller
				//					if(controller!=null) {
				validateUIField(nomChamp,value);
				//						if(!s.isOK()) {
				//							return s;
				//						}
				//					}
			}
			//				return ValidationStatus.ok();

			//selectedTaMessageEmailDTO.setAdresse1Adresse("abcd");

			//			  if(selectedRadio.equals("aa")) {
			//				  throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
			//			  }

		} catch(Exception e) {
			//messageComplet += e.getMessage();
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, messageComplet, messageComplet));
		}
	}

	public void autcompleteSelection(SelectEvent event) {
		Object value = event.getObject();
		//			FacesMessage msg = new FacesMessage("Selected", "Item:" + value);

		String nomChamp =  (String) event.getComponent().getAttributes().get("nomChamp");

		if(value!=null) {
			if(value instanceof String && value.equals(Const.C_AUCUN))value="";	
			if(value instanceof TaTEntiteDTO && ((TaTEntiteDTO) value).getCodeTEntite()!=null && ((TaTEntiteDTO) value).getCodeTEntite().equals(Const.C_AUCUN))value=null;	
			if(value instanceof TaTCiviliteDTO && ((TaTCiviliteDTO) value).getCodeTCivilite()!=null && ((TaTCiviliteDTO) value).getCodeTCivilite().equals(Const.C_AUCUN))value=null;
			if(value instanceof TaCPaiementDTO && ((TaCPaiementDTO) value).getCodeCPaiement()!=null && ((TaCPaiementDTO) value).getCodeCPaiement().equals(Const.C_AUCUN))value=null;
			if(value instanceof TaTPaiementDTO && ((TaTPaiementDTO) value).getCodeTPaiement()!=null && ((TaTPaiementDTO) value).getCodeTPaiement().equals(Const.C_AUCUN))value=null;
			//if(value instanceof TaTTiersDTO && ((TaTTiersDTO) value).getCodeTTiers()!=null && ((TaTTiersDTO) value).getCodeTTiers().equals(Const.C_AUCUN))value=null;
			if(value instanceof TaCPaiement && ((TaCPaiement) value).getCodeCPaiement()!=null && ((TaCPaiement) value).getCodeCPaiement().equals(Const.C_AUCUN))value=null;
			if(value instanceof TaTPaiement && ((TaTPaiement) value).getCodeTPaiement()!=null && ((TaTPaiement) value).getCodeTPaiement().equals(Const.C_AUCUN))value=null;
			if(value instanceof TaTTvaDoc && ((TaTTvaDoc) value).getCodeTTvaDoc()!=null && ((TaTTvaDoc) value).getCodeTTvaDoc().equals(Const.C_AUCUN))value=null;
			if(value instanceof TaFamilleTiers && ((TaFamilleTiers) value).getCodeFamille()!=null && ((TaFamilleTiers) value).getCodeFamille().equals(Const.C_AUCUN))value=null;
		}

		validateUIField(nomChamp,value);
	}
	
	public void autcompleteUnSelect(UnselectEvent event) {
		Object value = event.getObject();
		
		String nomChamp =  (String) event.getComponent().getAttributes().get("nomChamp");

		if(value!=null) {
			if(value instanceof String && value.equals(Const.C_AUCUN))value="";	
			if(value instanceof TaFamilleDTO && ((TaFamilleDTO) value).getCodeFamille()!=null && ((TaFamilleDTO) value).getCodeFamille().equals(Const.C_AUCUN))value=null;	
			if(value instanceof TaUniteDTO && ((TaUniteDTO) value).getCodeUnite()!=null && ((TaUniteDTO) value).getCodeUnite().equals(Const.C_AUCUN))value=null;
			if(value instanceof TaTvaDTO && ((TaTvaDTO) value).getCodeTva()!=null && ((TaTvaDTO) value).getCodeTva().equals(Const.C_AUCUN))value=null;
			if(value instanceof TaTTvaDTO && ((TaTTvaDTO) value).getCodeTTva()!=null && ((TaTTvaDTO) value).getCodeTTva().equals(Const.C_AUCUN))value=null;	
			if(value instanceof TaMarqueArticle && ((TaMarqueArticle) value).getCodeMarqueArticle()!=null && ((TaMarqueArticle) value).getCodeMarqueArticle().equals(Const.C_AUCUN))value=null;	
		}
		
//		//validateUIField(nomChamp,value);
//		try {
//			if(nomChamp.equals(Const.C_ADRESSE_EMAIL+"_DESTINATAIRE")) {
//				TaEmail entity =null;
//				if(value!=null){
//					if(value instanceof TaEmailDTO){
//	//				entity=(TaFamille) value;
//						entity = taEmailService.findByCode(((TaFamilleDTO)value).getCodeFamille());
//					}else{
//						entity = taEmailService.findByCode((String)value);
//					}
//				}else{
//					selectedTaArticleDTO.setCodeFamille("");
//				}
//				//taArticle.setTaFamille(entity);
//				for (TaEmail f : taMessageEmail.getDestinataires()) {
//					if(f.getIdEmail()==((TaEmailDTO)value).getId())
//						entity = f;
//				}
//				taMessageEmail.getDestinataires().remove(entity);
//				if(taArticle.getTaFamille()!=null && taArticle.getTaFamille().getCodeFamille().equals(entity.getCodeFamille())) {
//					taArticle.setTaFamille(null);
//					taFamilleDTO = null;
//					if(!taArticle.getTaFamilles().isEmpty()) {
//						taArticle.setTaFamille(taArticle.getTaFamilles().iterator().next());
//						//taFamilleDTO = null;
//					}
//				}
//			}
//
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
	
	}

	public boolean validateUIField(String nomChamp,Object value) {

		boolean changement=false;

		try {				
			if(nomChamp.equals(Const.C_ADRESSE_EMAIL+"_DESTINATAIRE") 
					|| nomChamp.equals(Const.C_ADRESSE_EMAIL+"_CC")
					|| nomChamp.equals(Const.C_ADRESSE_EMAIL+"_BCC")) {
				
				TaEmail entity =null;
				if(value!=null){
					if(value instanceof TaEmailDTO){
//					entity=(TaFamille) value;
						entity = taEmailService.findByCode(((TaEmailDTO)value).getAdresseEmail());
					}else{
						entity = taEmailService.findByCode((String)value);
					}
				}else{
					//selectedTaMessageEmailDTO.setCodeFamille("");
				}
				TaContactMessageEmail taContactMessageEmail = new TaContactMessageEmail();
				taContactMessageEmail.setAdresseEmail(entity.getAdresseEmail());
				taContactMessageEmail.setTaTiers(entity.getTaTiers());
				
				if(nomChamp.equals(Const.C_ADRESSE_EMAIL+"_DESTINATAIRE")) {
					taContactMessageEmail.setTaMessageEmail(taMessageEmail);
					if(taMessageEmail.getDestinataires()==null) {
						taMessageEmail.setDestinataires(new HashSet<>());
					}
					taMessageEmail.getDestinataires().add(taContactMessageEmail);
				} else if(nomChamp.equals(Const.C_ADRESSE_EMAIL+"_CC")) {
					taContactMessageEmail.setTaMessageEmailCc(taMessageEmail);
					if(taMessageEmail.getCc()==null) {
						taMessageEmail.setCc(new HashSet<>());
					}
					taMessageEmail.getCc().add(taContactMessageEmail);
				} else if(nomChamp.equals(Const.C_ADRESSE_EMAIL+"_BCC")) {
					taContactMessageEmail.setTaMessageEmailBcc(taMessageEmail);
					if(taMessageEmail.getBcc()==null) {
						taMessageEmail.setBcc(new HashSet<>());
					}
					taMessageEmail.getBcc().add(taContactMessageEmail);
				} 

			} else if(nomChamp.equals(Const.C_CODE_TIERS)) {
//		
			}
			

			return false;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}


	public List<TaEmail> adresseEmailAutoComplete(String query) {
		List<TaEmail> allValues = taEmailService.selectAll();
		List<TaEmail> filteredValues = new ArrayList<TaEmail>();
		TaEmail civ = new TaEmail();
		civ.setAdresseEmail(Const.C_AUCUN);
		filteredValues.add(civ);
		for (int i = 0; i < allValues.size(); i++) {
			civ = allValues.get(i);
			if(query.equals("*") || civ.getAdresseEmail().toLowerCase().contains(query.toLowerCase())) {
				filteredValues.add(civ);
			}
		}

		return filteredValues;
	}

	public List<TaEmailDTO> adresseEmailAutoCompleteLight(String query) {
		List<TaEmailDTO> allValues = taEmailService.findAllLight();
		List<TaEmailDTO> filteredValues = new ArrayList<TaEmailDTO>();
		TaEmailDTO civ = new TaEmailDTO();
//		civ.setAdresseEmail(Const.C_AUCUN);
//		filteredValues.add(civ);
		for (int i = 0; i < allValues.size(); i++) {
			civ = allValues.get(i);
			if(query.equals("*") || civ.getAdresseEmail().toLowerCase().contains(query.toLowerCase())) {
				filteredValues.add(civ);
			}
		}

		return filteredValues;
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

	public TaMessageEmail getTaMessageEmail() {
		return taMessageEmail;
	}

	public void setTaMessageEmail(TaMessageEmail taTiers) {
		this.taMessageEmail = taTiers;
	}


	public TabView getTabViewTiers() {
		return tabViewTiers;
	}

	public void setTabViewTiers(TabView tabViewTiers) {
		this.tabViewTiers = tabViewTiers;
	}

	public TaEmail getTaEmail() {
		return taEmail;
	}

	public void setTaEmail(TaEmail taEmail) {
		this.taEmail = taEmail;
	}

	public TaTEmail getTaTEmail() {
		return taTEmail;
	}

	public void setTaTEmail(TaTEmail taTEmail) {
		this.taTEmail = taTEmail;
	}

	public TaEmailDTO getTaEmailDTODestinataire() {
		return taEmailDTODestinataire;
	}

	public void setTaEmailDTODestinataire(TaEmailDTO taEmailDTO) {
		this.taEmailDTODestinataire = taEmailDTO;
	}

	public TaTEmailDTO getTaTEmailDTO() {
		return taTEmailDTO;
	}

	public void setTaTEmailDTO(TaTEmailDTO taTEmailDTO) {
		this.taTEmailDTO = taTEmailDTO;
	}

	public TaEmailDTO getTaEmailDTOCc() {
		return taEmailDTOCc;
	}

	public void setTaEmailDTOCc(TaEmailDTO taEmailDTOCc) {
		this.taEmailDTOCc = taEmailDTOCc;
	}

	public TaEmailDTO getTaEmailDTOBcc() {
		return taEmailDTOBcc;
	}

	public void setTaEmailDTOBcc(TaEmailDTO taEmailDTOBcc) {
		this.taEmailDTOBcc = taEmailDTOBcc;
	}

	public List<TaEmailDTO> getTaEmailDTODestinataires() {
		return taEmailDTODestinataires;
	}

	public void setTaEmailDTODestinataires(List<TaEmailDTO> taEmailDTODestinataires) {
		this.taEmailDTODestinataires = taEmailDTODestinataires;
	}

	public List<TaEmailDTO> getTaEmailDTOCcs() {
		return taEmailDTOCcs;
	}

	public void setTaEmailDTOCcs(List<TaEmailDTO> taEmailDTOCcs) {
		this.taEmailDTOCcs = taEmailDTOCcs;
	}

	public List<TaEmailDTO> getTaEmailDTOBccs() {
		return taEmailDTOBccs;
	}

	public void setTaEmailDTOBccs(List<TaEmailDTO> taEmailDTOBccs) {
		this.taEmailDTOBccs = taEmailDTOBccs;
	}

	public List<String> getAdressesDestinataire() {
		return adressesDestinataire;
	}

	public void setAdressesDestinataire(List<String> adressesDestinataire) {
		this.adressesDestinataire = adressesDestinataire;
	}

	public List<String> getAdressesCc() {
		return adressesCc;
	}

	public void setAdressesCc(List<String> adressesCc) {
		this.adressesCc = adressesCc;
	}

	public List<String> getAdressesBcc() {
		return adressesBcc;
	}

	public void setAdressesBcc(List<String> adressesBcc) {
		this.adressesBcc = adressesBcc;
	}

	public boolean isShowPlusDestinataire() {
		return showPlusDestinataire;
	}

	public void setShowPlusDestinataire(boolean showPlusDestinataire) {
		this.showPlusDestinataire = showPlusDestinataire;
	}

	public boolean isShowPlusCc() {
		return showPlusCc;
	}

	public void setShowPlusCc(boolean showPlusCc) {
		this.showPlusCc = showPlusCc;
	}

	public boolean isShowPlusBcc() {
		return showPlusBcc;
	}

	public void setShowPlusBcc(boolean showPlusBcc) {
		this.showPlusBcc = showPlusBcc;
	}

	public EmailPieceJointeParam[] getPiecesJointes() {
		return piecesJointes;
	}

	public void setPiecesJointes(EmailPieceJointeParam[] piecesJointes) {
		this.piecesJointes = piecesJointes;
	}
	
	//public void prepareDownloadPj(TaPieceJointeEmailDTO pjDTO) {
	public void prepareDownloadPj(ActionEvent e) {
		selectedTaPieceJointeEmailDTO = (TaPieceJointeEmailDTO) e.getComponent().getAttributes().get("pjdto");
//		selectedTaPieceJointeEmailDTO = pjDTO;
	}

	public StreamedContent getStreamedPieceJointe() {
		if(selectedTaPieceJointeEmailDTO.getFichier()==null) {
			try {
				TaPieceJointeEmail pje = taPiecesJointesEmailService.findById(selectedTaPieceJointeEmailDTO.getId());
				if(pje!=null) {
					selectedTaPieceJointeEmailDTO.setFichier(pje.getFichier());
				}
			} catch (FinderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		InputStream stream = new ByteArrayInputStream(selectedTaPieceJointeEmailDTO.getFichier()); 
		streamedPieceJointe = new DefaultStreamedContent(stream,null,selectedTaPieceJointeEmailDTO.getNomFichier());
		return streamedPieceJointe;
	}

	public TaPieceJointeEmailDTO getSelectedTaPieceJointeEmailDTO() {
		return selectedTaPieceJointeEmailDTO;
	}

	public void setSelectedTaPieceJointeEmailDTO(TaPieceJointeEmailDTO selectedTaPieceJointeEmailDTO) {
		this.selectedTaPieceJointeEmailDTO = selectedTaPieceJointeEmailDTO;
	}
	
	public void actDialogEmail(ActionEvent actionEvent) {
	    
        Map<String,Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("draggable", false);
        options.put("closable", false);
        options.put("resizable", true);
        options.put("contentHeight", 710);
        options.put("contentWidth", "100%");
        //options.put("contentHeight", "100%");
        options.put("width", 1024);
        
        //Map<String,List<String>> params = null;
        Map<String,List<String>> params = new HashMap<String,List<String>>();
        List<String> list = new ArrayList<String>();
        list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
        params.put("modeEcranDefaut", list);
        
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		
		EmailParam email = new EmailParam();
		email.setEmailExpediteur(null);
//		email.setNomExpediteur(taInfoEntrepriseService.findInstance().getNomInfoEntreprise()); 
//		email.setSubject(taInfoEntrepriseService.findInstance().getNomInfoEntreprise()+" Facture "+masterEntity.getCodeDocument()); 
//		email.setBodyPlain("Votre facture "+masterEntity.getCodeDocument());
//		email.setBodyHtml("Votre facture "+masterEntity.getCodeDocument());
//		email.setDestinataires(new String[]{masterEntity.getTaTiers().getTaEmail().getAdresseEmail()});
//		email.setEmailDestinataires(new TaEmail[]{masterEntity.getTaTiers().getTaEmail()});
		
//		EmailPieceJointeParam pj1 = new EmailPieceJointeParam();
//		pj1.setFichier(new File(((ITaFactureServiceRemote)taDocumentService).generePDF(masterEntity.getIdDocument())));
//		pj1.setTypeMime("pdf");
//		pj1.setNomFichier(pj1.getFichier().getName());
//		email.setPiecesJointes(
//				new EmailPieceJointeParam[]{pj1}
//				);
		
//		sessionMap.put(PaiementParam.NOM_OBJET_EN_SESSION, email);
        
        PrimeFaces.current().dialog().openDynamic("/dialog_email", options, params);
    
	}
	
	public void handleReturnDialogEmail(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			
			TaMessageEmail j = (TaMessageEmail) event.getObject();
			
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Email", 
					"Email envoyée "
					)); 
		}
	}

	public TaTiers getMasterEntity() {
		return masterEntity;
	}

	public void setMasterEntity(TaTiers masterEntity) {
		this.masterEntity = masterEntity;
	}

	public MenuModel getModel() {
		return model;
	}

	public void setModel(MenuModel model) {
		this.model = model;
	}

	public String getCssEtiquetteEmail() {
		return cssEtiquetteEmail;
	}

	public void setCssEtiquetteEmail(String cssEtiquetteEmail) {
		this.cssEtiquetteEmail = cssEtiquetteEmail;
	}


}  
  
