package fr.legrain.bdg.webapp.tiers;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.faces.validator.ValidatorException;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.apache.commons.beanutils.PropertyUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.component.tabview.TabView;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import fr.legrain.bdg.controle.service.remote.ITaGenCodeExServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaFactureServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaProformaServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.bdg.droits.service.remote.ITaUtilisateurServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.preferences.divers.ConstPreferencesTiers;
import fr.legrain.bdg.tache.service.remote.ITaEvenementServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaAdresseServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaCPaiementServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaComplServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaEmailServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaEspaceClientServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaFamilleTiersServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTAdrServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTCiviliteServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTEmailServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTEntiteServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTPaiementServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTTarifServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTTelServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTTiersServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTTvaDocServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTWebServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTelephoneServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaWebServiceRemote;
import fr.legrain.bdg.webapp.ConstWeb;
import fr.legrain.bdg.webapp.SessionListener;
import fr.legrain.bdg.webapp.agenda.EvenementParam;
import fr.legrain.bdg.webapp.app.AbstractController;
import fr.legrain.bdg.webapp.app.EmailController;
import fr.legrain.bdg.webapp.app.EmailParam;
import fr.legrain.bdg.webapp.app.LgrTab;
import fr.legrain.bdg.webapp.app.PushParam;
import fr.legrain.bdg.webapp.app.SmsController;
import fr.legrain.bdg.webapp.app.SmsParam;
import fr.legrain.bdg.webapp.app.TabListModelBean;
import fr.legrain.bdg.webapp.app.TabViewsBean;
import fr.legrain.bdg.webapp.dashboard.DashBoardParTiersController;
import fr.legrain.bdg.webapp.paiement.PaiementCbController;
import fr.legrain.bdg.webapp.paiement.PaiementParam;
import fr.legrain.document.dto.TaTPaiementDTO;
import fr.legrain.document.model.TaTPaiement;
import fr.legrain.dossier.model.TaInfoEntreprise;
import fr.legrain.droits.model.TaUtilisateur;
import fr.legrain.droits.service.SessionInfo;
import fr.legrain.droits.service.TenantInfo;
import fr.legrain.email.model.TaContactMessageEmail;
import fr.legrain.email.model.TaMessageEmail;
import fr.legrain.email.service.EmailProgrammePredefiniService;
import fr.legrain.email.service.EnvoiEmailService;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.LibExecLinux;
import fr.legrain.lib.data.ModeObjetEcranServeur;
import fr.legrain.paiement.model.RetourPaiementCarteBancaire;
import fr.legrain.push.model.TaMessagePush;
import fr.legrain.sms.model.TaMessageSMS;
import fr.legrain.tiers.dto.TaCPaiementDTO;
import fr.legrain.tiers.dto.TaFamilleTiersDTO;
import fr.legrain.tiers.dto.TaTCiviliteDTO;
import fr.legrain.tiers.dto.TaTEntiteDTO;
import fr.legrain.tiers.dto.TaTTarifDTO;
import fr.legrain.tiers.dto.TaTTiersDTO;
import fr.legrain.tiers.dto.TaTiersDTO;
import fr.legrain.tiers.model.TaCPaiement;
import fr.legrain.tiers.model.TaEmail;
import fr.legrain.tiers.model.TaEspaceClient;
import fr.legrain.tiers.model.TaFamilleTiers;
import fr.legrain.tiers.model.TaTAdr;
import fr.legrain.tiers.model.TaTCivilite;
import fr.legrain.tiers.model.TaTEmail;
import fr.legrain.tiers.model.TaTEntite;
import fr.legrain.tiers.model.TaTTarif;
import fr.legrain.tiers.model.TaTTel;
import fr.legrain.tiers.model.TaTTiers;
import fr.legrain.tiers.model.TaTTvaDoc;
import fr.legrain.tiers.model.TaTWeb;
import fr.legrain.tiers.model.TaTelephone;
import fr.legrain.tiers.model.TaTiers;

@Named
@ViewScoped  
public class TiersController extends AbstractController implements Serializable {  

	@Inject @Named(value="tabListModelCenterBean")
	private TabListModelBean tabsCenter;
	
	@Inject @Named(value="tabViewsBean")
	private TabViewsBean tabViews;

	private String paramId;
	
	private TabView tabViewTiers; //Le Binding sur les onglets "secondaire" semble ne pas fonctionné, les onglets ne sont pas générés au bon moment avec le c:forEach

	private List<TaTiersDTO> values; 
	private List<TaTiersDTO> filteredValues=new LinkedList<TaTiersDTO>();
	
	private @Inject SessionInfo sessionInfo;
	
	protected ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();

	private @EJB ITaProformaServiceRemote taProformaService;
	private @EJB ITaTiersServiceRemote taTiersService;
	private @EJB ITaTTiersServiceRemote taTTiersService;
//	private @EJB DashBoardDocumentController DashboardDocumentCtrl;
	
	private @EJB ITaInfoEntrepriseServiceRemote taInfoEntrepriseService;
	
	private @EJB ITaTCiviliteServiceRemote taTCivlicteService;
	private @EJB ITaTTvaDocServiceRemote taTTvaDocService;
	private @EJB ITaComplServiceRemote taTComplService;
	private @EJB ITaTEntiteServiceRemote taTEntiteService;
	private @EJB ITaCPaiementServiceRemote taCPaiementService;
	private @EJB ITaTPaiementServiceRemote taTPaiementService;	
	private @EJB ITaTTarifServiceRemote taTTarifService;
	private @EJB ITaTelephoneServiceRemote taTelephoneService;	
	private @EJB ITaEmailServiceRemote taEmailService;
	private @EJB ITaWebServiceRemote taWebService;
	private @EJB ITaAdresseServiceRemote taAdresseService;
	private @EJB ITaTAdrServiceRemote taTAdresseService;
	private @EJB ITaTEmailServiceRemote taTEmailService;
	private @EJB ITaTWebServiceRemote taTWebService;
	private @EJB ITaTTelServiceRemote taTTelService;
	private @EJB ITaGenCodeExServiceRemote taGenCodeExService;
	private @EJB ITaFamilleTiersServiceRemote taFamilleTiersService;
	private @EJB EmailProgrammePredefiniService lgrEmail;
	private @EJB ITaFactureServiceRemote taFactureService ;
	
	private @EJB ITaEvenementServiceRemote taEvenementService;
	private @EJB ITaUtilisateurServiceRemote taUtilisateurService;
	
	private @EJB ITaEspaceClientServiceRemote taEspaceClientService;
	
	private @EJB EnvoiEmailService emailServiceFinder;
	
	//private @EJB IlgrMailjetService lgrMailService;
	
	private TaInfoEntreprise infoEntreprise;
	
	private StreamedContent exportationTiers;
	
	@Inject private TenantInfo tenantInfo;
	
	@Inject private InfosJuridiqueController infosJuridiqueController;
	@Inject private AdresseController adresseController;
	@Inject private TelephoneController telephoneController;
	@Inject private EmailController emailController;
	@Inject private EmailTiersController emailTiersController;
	@Inject private SmsController smsController;
	@Inject private BanqueController banqueController;
	@Inject private CarteBancaireController carteBancaireController;
	@Inject private DashBoardParTiersController dashBoardParTiersController;
	
	@Inject //@Named(value="abonnementTiersController")
	private AbonnementTiersController abonnementTiersController;
	
	@Inject private PaiementAbonnementTiersController paiementAbonnementTiersController;
	@Inject private ServiceTiersController serviceTiersController;
	


	private TaTiersDTO[] selectedTaTiersDTOs; 
	private TaTiers taTiers = new TaTiers();
	private TaTiersDTO newTaTiersDTO = new TaTiersDTO();
	private TaTiersDTO selectedTaTiersDTO = new TaTiersDTO();
	
	private TaTTiers taTTiers;
	private TaTCivilite taTCivilite;
	private TaTEntite taTEntite;
	private TaTPaiement taTPaiement;
	private TaTTarif taTTarif;
	private TaCPaiement taCPaiement;
	private TaTTvaDoc taTTvaDoc;
	private TaFamilleTiers taFamilleTiers;
	
	private LgrDozerMapper<TaTiersDTO,TaTiers> mapperUIToModel  = new LgrDozerMapper<TaTiersDTO,TaTiers>();
	private LgrDozerMapper<TaTiers,TaTiersDTO> mapperModelToUI  = new LgrDozerMapper<TaTiers,TaTiersDTO>();
	
	private String texteMailRecommandationDossierTiers ="";
	private String texteMailRecommandationBdgDossier ="";
	
	
	/////TABVIEW CONST WIDGET VAR
	private final String WV_TABVIEW_TIERS = "wvtabViewTiers";
	/////TAB CONST CSS CLASS
	private final String CLASS_CSS_TAB_FICHE_TIERS = "tab-fiche-tiers";
	private final String CLASS_CSS_TAB_DASH_TIERS = "tab-dash-tiers";

	private TaTEntiteDTO taTEntiteDTO;
	private TaTTiersDTO taTTiersDTO;
	private TaTCiviliteDTO taTCiviliteDTO;
	private TaFamilleTiersDTO taFamilleTiersDTO;
	private ParamDuplicationTiers param ;
    private TaTiers tiersDuplique;
	private Boolean afficheSeulementBoutique=false;
	private Boolean parametrageDepuisMenuPrincipal=false;
    
	
	public TaTEntiteDTO getTaTEntiteDTO() {
		return taTEntiteDTO;
	}

	public void setTaTEntiteDTO(TaTEntiteDTO taTEntiteDTO) {
		this.taTEntiteDTO = taTEntiteDTO;
	}

	public TaTTiersDTO getTaTTiersDTO() {
		return taTTiersDTO;
	}

	public void setTaTTiersDTO(TaTTiersDTO taTTiersDTO) {
		this.taTTiersDTO = taTTiersDTO;
	}

	public TaTCiviliteDTO getTaTCiviliteDTO() {
		return taTCiviliteDTO;
	}

	public void setTaTCiviliteDTO(TaTCiviliteDTO taTCiviliteDTO) {
		this.taTCiviliteDTO = taTCiviliteDTO;
	}
	
	public TiersController() {  
		//values = getValues();
	}  

	@PostConstruct
	public void postConstruct(){
		infoEntreprise = taInfoEntrepriseService.findInstance();
		texteMailRecommandationDossierTiers = texteMailDossierToTiers();
		texteMailRecommandationBdgDossier =texteMailBdgToDossier();
		
		refresh();
		
		//float[]columnWidths; columnWidths = new float[]{0.1f, 0.1f, 0.2f, 0.6f}; 
		
		//		FaceletContext faceletContext = (FaceletContext) FacesContext.getCurrentInstance().getAttributes().get(FaceletContext.FACELET_CONTEXT_KEY);
		//		String formId = (String) faceletContext.getAttribute("paramId");
		//		if(formId!=null && !formId.equals("")) {
		//			try {
		//				selectedTaTiersDTO = TaTiersDTOService.findById(LibConversion.stringToInteger(paramId));
		//			} catch (FinderException e) {
		//				// TODO Auto-generated catch block
		//				e.printStackTrace();
		//			}
		//		}
	}


	
	public void refreshResponsive(){		
		if(paramId!=null) {
			try {
				if(paramId.equals("-1")){
					actInserer(null);
				}else{
				taTiers = taTiersService.findById(LibConversion.stringToInteger(paramId));
				mapperModelToUI.map(taTiers, selectedTaTiersDTO);
				autoCompleteMapDTOtoUI();
				modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
				}
				paramId=null;
			} catch (FinderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("TiersController.refresh() "+taTiers.getCodeTiers());
		}
	}
	
	public void actFermerDialog(ActionEvent actionEvent) {
		PrimeFaces.current().dialog().closeDynamic(null);
	}
	
	public void refreshAdresseReorder(){
		adresseController.setMasterEntity(adresseController.getAdresseReorderController().getMasterEntity());
		taTiers=taTiersService.merge(adresseController.getMasterEntity());
		adresseController.setMasterEntity(taTiers);
		adresseController.refresh(null);
		adresseController.getAdresseReorderController().setMasterEntity(adresseController.getMasterEntity());
		adresseController.getAdresseReorderController().refresh();
	}

    public void refresh(String etatDocument){
    	
    }
    
	public void refresh(){
		
		if (afficheSeulementBoutique) {
			values = taTiersService.findListeTiersBoutique(TaTTiers.VISITEUR_BOUTIQUE);
		}else values = taTiersService.findAllLight();
		
		for (TaTiersDTO o : values) {
			filteredValues.add(o);
		}
		
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
//		dashBoardParTiersController.setCodeTiersCourant(selectedTaTiersDTO.getCodeTiers());
//		dashBoardParTiersController.initIndicateursFacture(selectedTaTiersDTO.getCodeTiers());

//    	infoEntreprise = taInfoEntrepriseService.findInstance();
//    	dateDebut = infoEntreprise.getDatedebInfoEntreprise();
//    	dateFin = infoEntreprise.getDatefinInfoEntreprise();
//
//    	dateDebutProforma = infoEntreprise.getDatedebInfoEntreprise();
//    	dateFinProforma = infoEntreprise.getDatefinInfoEntreprise();
//
//    	barChartModelProforma = DashboardDocumentCtrl.createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
//		listeCaMoisDocumentProforma = taProformaService.listeChiffreAffaireTotalJmaDTO(dateDebut, dateFin, 1,null);
//		barChartModelProforma.addSeries(DashboardDocumentCtrl.createBarChartSerieMensuel("Mois", listeCaMoisDocumentProforma, dateDebut, dateFin));

    	if(parametrageDepuisMenuPrincipal) {
    		parametrageDepuisMenuPrincipal = false;
    		PrimeFaces.current().ajax().update("@widgetVar(wvOverlayListeTiers)");
    	}
	}

	public List<TaTiersDTO> getValues(){  
		return values;
	}

	public void actAnnuler(ActionEvent actionEvent) {
		try {
			switch (modeEcran.getMode()) {
			case C_MO_INSERTION:
				if(selectedTaTiersDTO.getCodeTiers()!=null) {
					taTiersService.annuleCode(selectedTaTiersDTO.getCodeTiers());
				}
				taTiers = new TaTiers();
				mapperModelToUI.map(taTiers,selectedTaTiersDTO );
				selectedTaTiersDTO=new TaTiersDTO();
				
				if(!values.isEmpty()) selectedTaTiersDTO = values.get(0);
				onRowSelect(null);
				break;
			case C_MO_EDITION:
				if(selectedTaTiersDTO.getId()!=null && selectedTaTiersDTO.getId()!=0){
					taTiers = taTiersService.findById(selectedTaTiersDTO.getId());
					selectedTaTiersDTO=taTiersService.findByIdDTO(selectedTaTiersDTO.getId());
				}				
				break;

			default:
				break;
			}
			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
			mapperModelToUI.map(taTiers, selectedTaTiersDTO);

			if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Tiers", "actAnnuler"));
			}
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public void autoCompleteMapUIToDTO() {
		if(taTTiersDTO!=null) {
			validateUIField(Const.C_CODE_T_TIERS,taTTiersDTO.getCodeTTiers());
			selectedTaTiersDTO.setCodeTTiers(taTTiersDTO.getCodeTTiers());
		}else selectedTaTiersDTO.setCodeTTiers(null);
		if(taTCiviliteDTO!=null) {
			validateUIField(Const.C_CODE_T_CIVILITE,taTCiviliteDTO.getCodeTCivilite());
			selectedTaTiersDTO.setCodeTCivilite(taTCiviliteDTO.getCodeTCivilite());
		}else selectedTaTiersDTO.setCodeTCivilite(null);
		if(taTEntiteDTO!=null) {
			validateUIField(Const.C_CODE_T_ENTITE,taTEntiteDTO.getCodeTEntite());
			selectedTaTiersDTO.setCodeTEntite(taTEntiteDTO.getCodeTEntite());
		}else selectedTaTiersDTO.setCodeTEntite(null);
		if(taTPaiement!=null) {
			validateUIField(Const.C_CODE_T_PAIEMENT,taTPaiement.getCodeTPaiement());
			selectedTaTiersDTO.setCodeTPaiement(taTPaiement.getCodeTPaiement());
		}else selectedTaTiersDTO.setCodeTPaiement(null);
		if(taTTarif!=null) {
			validateUIField(Const.C_CODE_T_TARIF,taTTarif.getCodeTTarif());
			selectedTaTiersDTO.setCodeTTarif(taTTarif.getCodeTTarif());
		}else selectedTaTiersDTO.setCodeTTarif(null);		
		if(taCPaiement!=null) {
			validateUIField(Const.C_CODE_C_PAIEMENT,taCPaiement.getCodeCPaiement());
			selectedTaTiersDTO.setCodeCPaiement(taCPaiement.getCodeCPaiement());
		}else selectedTaTiersDTO.setCodeCPaiement(null);
		if(taTTvaDoc!=null) {
			validateUIField(Const.C_CODE_T_TVA_DOC,taTTvaDoc.getCodeTTvaDoc());
			selectedTaTiersDTO.setCodeTTvaDoc(taTTvaDoc.getCodeTTvaDoc());
		}else selectedTaTiersDTO.setCodeTTvaDoc(null);
		
		if(taFamilleTiersDTO!=null) {
			validateUIField(Const.C_CODE_FAMILLE_TIERS,taFamilleTiersDTO.getCodeFamille());
			selectedTaTiersDTO.setCodeFamilleTiers(taFamilleTiersDTO.getCodeFamille());
		}else selectedTaTiersDTO.setCodeFamilleTiers(null);
	}
	
	public void autoCompleteMapDTOtoUI() {
		try {
			taTTiers = null;
			taTCivilite = null;
			taTEntite = null;
			taCPaiement = null;
			taTPaiement = null;
			taTTarif = null;
			taTTvaDoc = null;
			taFamilleTiers = null;
			
			taTTiersDTO = null;
			taTCiviliteDTO = null;
			taTEntiteDTO = null;
			taFamilleTiersDTO = null;
			
			if(selectedTaTiersDTO.getCodeTTiers()!=null && !selectedTaTiersDTO.getCodeTTiers().equals("")) {
				taTTiers = taTTiersService.findByCode(selectedTaTiersDTO.getCodeTTiers());
				taTTiersDTO=taTTiersService.findByCodeDTO(selectedTaTiersDTO.getCodeTTiers());
			}
			if(selectedTaTiersDTO.getCodeTCivilite()!=null && !selectedTaTiersDTO.getCodeTCivilite().equals("")) {
				taTCivilite = taTCivlicteService.findByCode(selectedTaTiersDTO.getCodeTCivilite());
				taTCiviliteDTO = taTCivlicteService.findByCodeDTO(selectedTaTiersDTO.getCodeTCivilite());
			}
			if(selectedTaTiersDTO.getCodeTEntite()!=null && !selectedTaTiersDTO.getCodeTEntite().equals("")) {
				taTEntite = taTEntiteService.findByCode(selectedTaTiersDTO.getCodeTEntite());
				taTEntiteDTO = taTEntiteService.findByCodeDTO(selectedTaTiersDTO.getCodeTEntite());
			}
			if(selectedTaTiersDTO.getCodeCPaiement()!=null && !selectedTaTiersDTO.getCodeCPaiement().equals("")) {
				taCPaiement = taCPaiementService.findByCode(selectedTaTiersDTO.getCodeCPaiement());
			}
			if(selectedTaTiersDTO.getCodeTPaiement()!=null && !selectedTaTiersDTO.getCodeTPaiement().equals("")) {
				taTPaiement = taTPaiementService.findByCode(selectedTaTiersDTO.getCodeTPaiement());
			}
			if(selectedTaTiersDTO.getCodeTTarif()!=null && !selectedTaTiersDTO.getCodeTTarif().equals("")) {
				taTTarif = taTTarifService.findByCode(selectedTaTiersDTO.getCodeTTarif());
			}			
			if(selectedTaTiersDTO.getCodeTTvaDoc()!=null && !selectedTaTiersDTO.getCodeTTvaDoc().equals("")) {
				taTTvaDoc = taTTvaDocService.findByCode(selectedTaTiersDTO.getCodeTTvaDoc());
			}
			if(selectedTaTiersDTO.getCodeFamilleTiers()!=null && !selectedTaTiersDTO.getCodeFamilleTiers().equals("")) {
				taFamilleTiers = taFamilleTiersService.findByCode(selectedTaTiersDTO.getCodeFamilleTiers());
				taFamilleTiersDTO = taFamilleTiersService.findByCodeDTO(selectedTaTiersDTO.getCodeFamilleTiers());
			}			
		} catch (FinderException e) {
			e.printStackTrace();
		}
	}

	public void actEnregistrer(ActionEvent actionEvent) {		
		//TaTiers taTiers = new TaTiers();
		taTiers.initAdresseTiers(null,adresseEstRempli(),ConstPreferencesTiers.CORRESPONDANCE_ADRESSE_ADMINISTRATIF_DEFAUT,
				ConstPreferencesTiers.CORRESPONDANCE_ADRESSE_COMMERCIAL_DEFAUT);
//		taTiers.initTelephoneTiers(null, telEstRempli(), ConstPreferencesTiers.CORRESPONDANCE_TELEPHONE_ADMINISTRATIF_DEFAUT, ConstPreferencesTiers.CORRESPONDANCE_TELEPHONE_COMMERCIAL_DEFAUT);
		if(taTiers.getTaAdresse()!=null){
			try {
				if(taTiers.getTaAdresse()!=null && taTiers.getTaAdresse().getTaTAdr()==null){
				TaTAdr tAdr= taTAdresseService.findByCode(ConstPreferencesTiers.TADR_DEFAUT);
				taTiers.getTaAdresse().setTaTAdr(tAdr);
				}
				int ordre=taAdresseService.rechercheOdrePourType(taTiers.getTaAdresse().getTaTAdr().getCodeTAdr(),taTiers.getCodeTiers());
				if(taTiers.getTaAdresse().getOrdre()==null || taTiers.getTaAdresse().getOrdre()==0)taTiers.getTaAdresse().setOrdre(ordre);
			} catch (FinderException e) {
				
			}
		}
		
		if(taTiers.getTaTelephone()!=null){
			try {
				if(taTiers.getTaTelephone()!=null && taTiers.getTaTelephone().getTaTTel()==null){
				TaTTel taTTel= taTTelService.findByCode(ConstPreferencesTiers.TTEL_DEFAUT);
				taTiers.getTaTelephone().setTaTTel(taTTel);
				}
			} catch (FinderException e) {
				
			}
		}
		autoCompleteMapUIToDTO();
		initAdresseNull();
		if(taTiers.getTaEmail()==null){
			selectedTaTiersDTO.setAdresseEmail(null);
		}
		if(taTiers.getTaTelephone()==null){
			selectedTaTiersDTO.setNumeroTelephone(null);
		}
		if(taTiers.getTaWeb()==null){
			selectedTaTiersDTO.setAdresseWeb(null);
		}

		mapperUIToModel.map(selectedTaTiersDTO, taTiers);
		
		/*
		 * A faire, mapper les objets dans taTiers avant le merge :
		 * mapping de tous les objets avec des "codes", typeTiers, typeCvilite, ...
		 * tous les objets imbriqués par défaut, adresse défaut, email défaut, ...
		 * 
		 * Pour les converter, essayé de faire fonctionner les injections via omniface
		 * ou, récupérer dans le client RCP le système pour générer les chaines JNDI
		 * 
		 * Ma questions sur les forums
		 * http://forum.primefaces.org/viewtopic.php?f=3&t=40677&sid=fc4d0075c1e57d0433bd8a8d3bdf0393
		 * http://stackoverflow.com/questions/27551615/primefaces-autocomplete-using-a-list-pojo-and-a-string-as-default-value
		 * 
		 */
		
		try {
			if(taTiers.getCodeCompta()==null 
					|| (taTiers.getCodeCompta()!=null && taTiers.getCodeCompta().equals(""))
					) {
					taTiers.setCodeCompta(taTiers.getCodeTiers().substring(0, 7));
				if(taTiers.getCodeTiers().length()>6) {
					taTiers.setCodeCompta(taTiers.getCodeTiers().substring(0, 7));
				} else  {
					taTiers.setCodeCompta(taTiers.getCodeTiers().substring(0, taTiers.getCodeTiers().length()));
				}
			}
			if(taTiers.getCleLiaisonCompteClient()==null || taTiers.getCleLiaisonCompteClient().equals("")) {
				taTiers.setCleLiaisonCompteClient(LibExecLinux.pwgen("5"));
			}

			taTiers = taTiersService.merge(taTiers,ITaTiersServiceRemote.validationContext);
			
			if(selectedTaTiersDTO.getCodeTiers()!=null) {
				taTiersService.annuleCode(selectedTaTiersDTO.getCodeTiers());
			}
			
			mapperModelToUI.map(taTiers, selectedTaTiersDTO);
			autoCompleteMapDTOtoUI();
			
			if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_INSERTION)==0) {
				values.add(selectedTaTiersDTO);
				selectedTaTiersDTOs = null;
			}
			adresseController.refresh(null);
			telephoneController.refresh(null);
			emailTiersController.refresh(null);
			infosJuridiqueController.refresh(null);

			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

		} catch(Exception e) {
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			//context.addMessage(null, new FacesMessage("Tiers", "actEnregistrer")); 
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Tiers", e.getMessage()));
		}

		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Tiers", "actEnregistrer"));
		}
	}

	public void actInserer(ActionEvent actionEvent) {
		try {
			selectedTaTiersDTOs= new TaTiersDTO[]{};
			selectedTaTiersDTO = new TaTiersDTO();
			taTiers = new TaTiers();
			
			String codeTiersDefaut = "C";

			taGenCodeExService.libereVerrouTout(new ArrayList<String>(SessionListener.getSessions().keySet()));
			selectedTaTiersDTO.setCodeTiers(taTiersService.genereCode(null)); //ejb
			validateUIField(Const.C_CODE_TIERS,selectedTaTiersDTO.getCodeTiers());//permet de verrouiller le code genere

			selectedTaTiersDTO.setCodeTTiers(codeTiersDefaut);

			selectedTaTiersDTO.setCodeCompta(selectedTaTiersDTO.getCodeTiers()); //ejb
			validateUIField(Const.C_CODE_COMPTA,selectedTaTiersDTO.getCodeCompta()); //ejb
			selectedTaTiersDTO.setCompte("411"); //ejb
			validateUIField(Const.C_COMPTE,selectedTaTiersDTO.getCompte()); //ejb

			//TaTTiersDAO daoTTiers = new TaTTiersDAO(getEm());
			TaTTiers taTTiers;

			taTTiers = taTTiersService.findByCode(codeTiersDefaut);

			if(taTTiers!=null && taTTiers.getCompteTTiers()!=null) {
				selectedTaTiersDTO.setCompte(taTTiers.getCompteTTiers());
				this.taTTiers = taTTiers;
			} else {
				//selectedTaTiersDTO.setCompte(TiersPlugin.getDefault().getPreferenceStore().
				//getString(PreferenceConstants.TIERS_COMPTE_COMPTALE_DEFAUT));
			}
			selectedTaTiersDTO.setActifTiers(true);
			//swtTiers.setTtcTiers(TiersPlugin.getDefault().getPreferenceStore().
			//getBoolean(PreferenceConstants.TIERS_SAISIE_TTC_DEFAUT));
			selectedTaTiersDTO.setCodeTTvaDoc("F");
			
			autoCompleteMapDTOtoUI();
			
			modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
			scrollToTop();
			if(tabViewTiers!=null) {
				tabViewTiers.setActiveIndex(1);
				updateTab();
//				adresseController.setMasterEntity(taTiers);
//				adresseController.refresh(null);
			}
			if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Tiers", "actInserer"));
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

		if(ConstWeb.DEBUG) {
		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage("Tiers", "actModifier"));
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
		context.addMessage(null, new FacesMessage("Tiers", "actAide"));
		}
	}
	
//	public void actAideRetour(ActionEvent actionEvent) {
//		
//	}
	
    public String nouveauResponsive() {
		return "/m/tiers.xhtml?idTiers=-1"+"&faces-redirect=true";
    }
    
    public void nouveau(ActionEvent actionEvent) {  
    	LgrTab b = new LgrTab();
    	b.setTypeOnglet(LgrTab.TYPE_TAB_TIERS);
		b.setTitre("Tiers");
		b.setStyle(LgrTab.CSS_CLASS_TAB_TIERS);
		b.setTemplate("tiers/tiers.xhtml");
		b.setIdTab(LgrTab.ID_TAB_TIERS);
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
    

    public String detailResponsive() {
    	//selectedTaTiersDTO = (TaTiersDTO) actionEvent.getComponent().getAttributes().get("selectionTiers");
//    	onRowSelectResponsive(null);
    	if(selectedTaTiersDTO!=null)   	return "/m/tiers.xhtml?idTiers="+selectedTaTiersDTO.getId()+"&faces-redirect=true";
    	else return "";
    }
    

	public void repondreMessage(ActionEvent event) {
		Integer idMessage = (Integer) event.getComponent().getAttributes().get("idTiers");
	    
	    ExternalContext context2 = FacesContext.getCurrentInstance().getExternalContext();
	    try {
			context2.redirect(context2.getRequestContextPath() + "/m/tiers.xhtml?idTiers="+idMessage);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void actSupprimer() {
		actSupprimer(null);
	}
	
	public void actSupprimer(ActionEvent actionEvent) {
		TaTiers taTiers = new TaTiers();
		try {
			if(selectedTaTiersDTO!=null && selectedTaTiersDTO.getId()!=null){
				taTiers = taTiersService.findById(selectedTaTiersDTO.getId());
			}

			taTiersService.remove(taTiers);
			values.remove(selectedTaTiersDTO);
			
			if(!values.isEmpty()) {
				selectedTaTiersDTO = values.get(0);
			}else {
				selectedTaTiersDTO=new TaTiersDTO();
			}
			selectedTaTiersDTOs = null;
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
		selectedTaTiersDTO=new TaTiersDTO();
		selectedTaTiersDTOs=new TaTiersDTO[]{selectedTaTiersDTO};
		
//		//gestion du filtre de la liste
//        RequestContext requestContext = RequestContext.getCurrentInstance();
//        requestContext.execute("PF('wvDataTableListeTiers').filter()");
        
        
//		LgrTab typeOngletDejaOuvert = null;
//		for (LgrTab o : tabsCenter.getOnglets()) {
//			if(LgrTab.TYPE_TAB_TIERS.equals(o.getTypeOnglet())) {
//				typeOngletDejaOuvert = o;
//			}
//		}
//		
//		if(typeOngletDejaOuvert!=null) {
////			if(!values.isEmpty()) {
////				selectedTaTiersDTO = values.get(0);
////			}
//			tabsCenter.supprimerOnglet(typeOngletDejaOuvert);
//		}
//		if(ConstWeb.DEBUG) {
//		FacesContext context = FacesContext.getCurrentInstance();  
//		context.addMessage(null, new FacesMessage("Tiers", "actFermer"));
//		}
	}

	public void actImprimer(ActionEvent actionEvent) {
		if(ConstWeb.DEBUG) {
		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage("Tiers", "actImprimer"));
		}
		try {
			
//		FacesContext facesContext = FacesContext.getCurrentInstance();
//		HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
		
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		sessionMap.put("tiers", taTiersService.findById(selectedTaTiersDTO.getId()));
		
		////////////////////////////////////////////////////////////////////////////////////////
		//Test génération de fichier PDF
		
//		HashMap<String,Object> hm = new HashMap<>();
//		hm.put( "tiers", taTiersService.findById(selectedTaTiersDTO.getId()));
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
//		taTiersService.generePDF(selectedTaTiersDTO.getId());
		////////////////////////////////////////////////////////////////////////////////////////
		
//			session.setAttribute("tiers", taTiersService.findById(selectedTaTiersDTO.getId()));
			System.out.println("TiersController.actImprimer()");
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}    
	
	public void actImprimerListeDesTiers(ActionEvent actionEvent) {
		
		try {
			
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			
			sessionMap.put("listeDesTiers", filteredValues);

			} catch (Exception e) {
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
		context.addMessage(null, new FacesMessage("Tiers", 
				"Chargement du tiers N°"+selectedTaTiersDTO.getCodeTiers()
				)); }
		} else {
			tabViews.selectionneOngletCentre(LgrTab.TYPE_TAB_TIERS);
		}
	} 
	public void updateTab(){
		try {

			if(selectedTaTiersDTOs!=null && selectedTaTiersDTOs.length>0) {
				selectedTaTiersDTO = selectedTaTiersDTOs[0];
			}
			if(selectedTaTiersDTO.getId()!=null && selectedTaTiersDTO.getId()!=0) {
				taTiers = taTiersService.findById(selectedTaTiersDTO.getId());
			}

			mapperModelToUI.map(taTiers, selectedTaTiersDTO);	
			
			adresseController.setMasterEntity(taTiers);
			adresseController.refresh(null);
			
			telephoneController.setMasterEntity(taTiers);
			telephoneController.refresh(null);
			
			infosJuridiqueController.setMasterEntity(taTiers);
			infosJuridiqueController.refresh(null);
			
			emailController.setMasterEntity(taTiers);
			emailController.refresh();
			
			emailTiersController.setMasterEntity(taTiers);
			emailTiersController.refresh(null);
			
			smsController.setMasterEntity(taTiers);
			smsController.refresh();
			
			banqueController.setMasterEntity(taTiers);
			banqueController.refresh();
			
			carteBancaireController.setMasterEntity(taTiers);
			carteBancaireController.refresh();
			
//			abonnementTiersController.setMasterEntity(taTiers);
//			abonnementTiersController.refresh(null);
			
//			paiementAbonnementTiersController.setMasterEntity(taTiers);
//			paiementAbonnementTiersController.refresh();
			
			autoCompleteMapDTOtoUI();
			if(selectedTaTiersDTO.getCodeTiers()!=null) {

				dashBoardParTiersController.setCodeTiersCourant(selectedTaTiersDTO.getCodeTiers()); 
				
//				dashBoardParTiersController.initIndicateursFacture(selectedTaTiersDTO.getCodeTiers());
				
				if(!taPreferencesService.grosFichierTiers()) {
					dashBoardParTiersController.refresh(null);
				}
				
			}
//			montantCaHt = BigDecimal.ZERO;
//			if(selectedTaTiersDTO.getCodeTiers()!=null) {
//				listeCaPeriodeFactureTiers = taTiersService.findChiffreAffaireTotalParCodeTiersDTO(dateDebut, dateFin, selectedTaTiersDTO.getCodeTiers());
//				if (listeCaPeriodeFactureTiers != null && ! listeCaPeriodeFactureTiers.isEmpty()) {
//			montantCaHt = listeCaPeriodeFactureTiers.get(0).getMtHtCalc();
//			if (montantCaHt == null) {
//							montantCaHt = BigDecimal.ZERO;
//									}
//				}
//			}
			
		} catch (FinderException e) {
			e.printStackTrace();
		}
	}
	public void updateTabPaiementAbonnement(){
		paiementAbonnementTiersController.setMasterEntity(taTiers);
		paiementAbonnementTiersController.refresh();
		//paiementAbonnementTiersController.verifPassageCRON();
	}
	public void updateTabService(){
		serviceTiersController.setMasterEntity(taTiers);
		serviceTiersController.refresh();
		//paiementAbonnementTiersController.verifPassageCRON();
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
		
		if(event.getTab()!=null) {
			if( event.getTab().getTitle().equals("Paiement Echéances")) {
				updateTabPaiementAbonnement();
			}
			if( event.getTab().getTitle().equals("Services")) {
				updateTabService();
			}
		} 
		
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
		System.out.println(modeEcran.dataSetEnModif() ||  selectedTaTiersDTO.getId()==0);
		return modeEcran.dataSetEnModif() ||  selectedTaTiersDTO.getId()==0;
	}
	
	public void onRowSelect(SelectEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_TIERS);
		//b.setTitre("Tiers "+selectedTaTiersDTO.getCodeTiers());
		b.setTitre("Tiers");
		b.setTemplate("tiers/tiers.xhtml");
		b.setIdTab(LgrTab.ID_TAB_TIERS);
		b.setStyle(LgrTab.CSS_CLASS_TAB_TIERS);
		b.setParamId(LibConversion.integerToString(selectedTaTiersDTO.getId()));

		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
		
		
		updateTab();
		scrollToTop();
		
		if(ConstWeb.DEBUG) {
		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage("Tiers", 
				"Chargement du tiers N°"+selectedTaTiersDTO.getCodeTiers()
				)); 
		}
	} 
	
	public void onRowSelectResponsive(SelectEvent event) {		
		autoCompleteMapDTOtoUI();
		
		try {
			taTiers = taTiersService.findById(selectedTaTiersDTO.getId());
			mapperModelToUI.map(taTiers, selectedTaTiersDTO);
		} catch (FinderException e) {
			e.printStackTrace();
		}
		
		if(ConstWeb.DEBUG) {
		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage("Tiers", 
				"Chargement du tiers N°"+selectedTaTiersDTO.getCodeTiers()
				)); }
	} 
	
	public void onRowToggle(ToggleEvent event) {

	
	}
	
	public void initListeLigneFacture() {
		
	}
	
	public boolean espaceClientDejaCreer() {
		return taEspaceClientService.findByCodeTiers(taTiers.getCodeTiers())!=null;
	}
	
	public void emailNotificationActivationCompteClient(ActionEvent actionEvent) {
		if(taEspaceClientService.findByCodeTiers(taTiers.getCodeTiers())==null) {
			//pas d'espace client pour ce tiers, on le cree et on lui envoi un email
			TaUtilisateur u = new TaUtilisateur();
			String mdpClair = LibExecLinux.pwgen("8");
			TaEspaceClient taEspaceClient = new TaEspaceClient();
			taEspaceClient.setTaTiers(taTiers);
			taEspaceClient.setEmail(taTiers.getTaEmail().getAdresseEmail());
			taEspaceClient.setNom(taTiers.getNomTiers());
			taEspaceClient.setPrenom(taTiers.getPrenomTiers());
			taEspaceClient.setPassword(u.passwordHashSHA256_Base64(mdpClair));
			taEspaceClient.setActif(true);
			taEspaceClientService.merge(taEspaceClient);
			
			lgrEmail.emailEspaceClientCreer(taInfoEntrepriseService.findInstance().getTaTiers(), taEspaceClient, taTiers, tenantInfo.getTenantId(), null, mdpClair);	
		//if(taTiers.getUtiliseCompteClient()!=null && taTiers.getUtiliseCompteClient()==true) {
//			lgrEmail.emailInformationCollectiveCompteClientDeLaPartDunFournisseur(taInfoEntrepriseService.findInstance().getTaTiers(), taTiers);
//			taTiers.setEmailCleCompteClientEnvoye(true);
//			selectedTaTiersDTO.setEmailCleCompteClientEnvoye(true);
//			taTiers = taTiersService.merge(taTiers);
//			selectedTaTiersDTO.setEmailCleCompteClientEnvoye(taTiers.getEmailCleCompteClientEnvoye());
		//}
		} else {
			//ce tiers à deja un espace client
		}
	}
	
	public void emailNotificationActivationCompteClientTous(ActionEvent actionEvent) {
		List<TaTiers> listeTiers = taTiersService.rechercheTiersPourCreationEspaceClientEnSerie();
		System.out.println("TiersController.emailNotificationActivationCompteClientTous() listeTiers "+listeTiers.size());
		int i = 1;
		for (TaTiers taTiers : listeTiers) {
			if(taEspaceClientService.findByCodeTiers(taTiers.getCodeTiers())==null) {
				//pas d'espace client pour ce tiers, on le cree et on lui envoi un email
				TaUtilisateur u = new TaUtilisateur();
				String mdpClair = LibExecLinux.pwgen("8");
				
				TaEspaceClient taEspaceClient = new TaEspaceClient();
				taEspaceClient.setTaTiers(taTiers);
				taEspaceClient.setEmail(taTiers.getTaEmail().getAdresseEmail());
				taEspaceClient.setNom(taTiers.getNomTiers());
				taEspaceClient.setPrenom(taTiers.getPrenomTiers());
				taEspaceClient.setPassword(u.passwordHashSHA256_Base64(mdpClair));
				taEspaceClient.setActif(true);
				System.out.println("AZERTY "+i+";"+taTiers.getCodeTiers()+";"+taTiers.getNomTiers()+";"+taTiers.getTaEmail().getAdresseEmail()+";"+mdpClair);
				taEspaceClientService.merge(taEspaceClient);
				
				lgrEmail.emailEspaceClientCreerTous(taInfoEntrepriseService.findInstance().getTaTiers(), taEspaceClient, taTiers, tenantInfo.getTenantId(), null, mdpClair);	
				i++;
			} else {
				//ce tiers à deja un espace client
			}
		}
		
	}
	
	public boolean editable() {
		return !modeEcran.dataSetEnModif();
	}
	
	public void actDialogTypeTiers(ActionEvent actionEvent) {
		
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
//		context.addMessage(null, new FacesMessage("Tiers", "actAbout")); 	    
	}
	
	public void handleReturnDialogTypeTiers(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			taTTiers = (TaTTiers) event.getObject();
			try {
				taTTiersDTO = taTTiersService.findByCodeDTO(taTTiers.getCodeTTiers());
			} catch (FinderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void actDialogFamilleTiers(ActionEvent actionEvent) {
		
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
//		context.addMessage(null, new FacesMessage("Tiers", "actAbout")); 	    
	}
	
	public void handleReturnDialogTypeCivilite(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			taTCivilite = (TaTCivilite) event.getObject();
			try {
				taTCiviliteDTO = taTCivlicteService.findByCodeDTO(taTCivilite.getCodeTCivilite());
			} catch (FinderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void handleReturnDialogFamilleTiers(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			taFamilleTiers = (TaFamilleTiers) event.getObject();
			try {
				taFamilleTiersDTO = taFamilleTiersService.findByCodeDTO(taFamilleTiers.getCodeFamille());
			} catch (FinderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
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
//		context.addMessage(null, new FacesMessage("Tiers", "actAbout")); 	    
	}
	
	public void handleReturnDialogTypeEntite(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			taTEntite = (TaTEntite) event.getObject();
			try {
				taTEntiteDTO = taTEntiteService.findByCodeDTO(taTEntite.getCodeTEntite());
			} catch (FinderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
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
//		context.addMessage(null, new FacesMessage("Tiers", "actAbout")); 	    
	}
	
	public void handleReturnDialogConditionPaiement(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			taCPaiement = (TaCPaiement) event.getObject();
//			try {
//				taCPaiement = taCPaiementService.findByCodeDTO(taCPaiement.getCodeCPaiement());
//			} catch (FinderException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
	}
	
	public void actDialogTypePaiement(ActionEvent actionEvent) {
		
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
        
        PrimeFaces.current().dialog().openDynamic("articles/dialog_type_paiement", options, params);
		
//		FacesContext context = FacesContext.getCurrentInstance();  
//		context.addMessage(null, new FacesMessage("Tiers", "actAbout")); 	    
	}
	
	public void actDialogTypeTarif(ActionEvent actionEvent) {
		
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
        
        PrimeFaces.current().dialog().openDynamic("tiers/dialog_type_tarif", options, params);
	}
	
	public void handleReturnDialogTypeTarif(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			taTTarif = (TaTTarif) event.getObject();
		}
	}
	
	public void handleReturnDialogTypePaiement(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			taTPaiement = (TaTPaiement) event.getObject();
//			try {
//				taTPaiement = taTPaiementService.findByCodeDTO(taTPaiement.getCodeTPaiement());
//			} catch (FinderException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
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
				if(selectedTaTiersDTO!=null && selectedTaTiersDTO.getId()!=null  && selectedTaTiersDTO.getId()!=0 ) retour = false;
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

	public TaTiersDTO[] getSelectedTaTiersDTOs() {
		return selectedTaTiersDTOs;
	}

	public void setSelectedTaTiersDTOs(TaTiersDTO[] selectedTaTiersDTOs) {
		this.selectedTaTiersDTOs = selectedTaTiersDTOs;
	}

	public TaTiersDTO getNewTaTiersDTO() {
		return newTaTiersDTO;
	}

	public void setNewTaTiersDTO(TaTiersDTO newTaTiersDTO) {
		this.newTaTiersDTO = newTaTiersDTO;
	}

	public TaTiersDTO getSelectedTaTiersDTO() {
		return selectedTaTiersDTO;
	}

	public void setSelectedTaTiersDTO(TaTiersDTO selectedTaTiersDTO) {
		this.selectedTaTiersDTO = selectedTaTiersDTO;
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
		  
//		  validateUIField(nomChamp,value);
//		  TaTiersDTO dtoTemp=new TaTiersDTO();
//		  PropertyUtils.setSimpleProperty(dtoTemp, nomChamp, value);
//		  taTiersService.validateDTOProperty(dtoTemp, nomChamp, ITaTiersServiceRemote.validationContext );
		  
//			//validation automatique via la JSR bean validation
		  ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		  Set<ConstraintViolation<TaTiersDTO>> violations = factory.getValidator().validateValue(TaTiersDTO.class,nomChamp,value);
			if (violations.size() > 0) {
				messageComplet = "Erreur de validation : ";
//				List<IStatus> statusList = new ArrayList<IStatus>();
				for (ConstraintViolation<TaTiersDTO> cv : violations) {
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
		  
		  //selectedTaTiersDTO.setAdresse1Adresse("abcd");
		  
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
		if(value instanceof TaTEntiteDTO && ((TaTEntiteDTO) value).getCodeTEntite()!=null && ((TaTEntiteDTO) value).getCodeTEntite().equals(Const.C_AUCUN))value=null;	
		if(value instanceof TaTCiviliteDTO && ((TaTCiviliteDTO) value).getCodeTCivilite()!=null && ((TaTCiviliteDTO) value).getCodeTCivilite().equals(Const.C_AUCUN))value=null;
		if(value instanceof TaCPaiementDTO && ((TaCPaiementDTO) value).getCodeCPaiement()!=null && ((TaCPaiementDTO) value).getCodeCPaiement().equals(Const.C_AUCUN))value=null;
		if(value instanceof TaTPaiementDTO && ((TaTPaiementDTO) value).getCodeTPaiement()!=null && ((TaTPaiementDTO) value).getCodeTPaiement().equals(Const.C_AUCUN))value=null;
		if(value instanceof TaTTarifDTO && ((TaTTarifDTO) value).getCodeTTarif()!=null && ((TaTTarifDTO) value).getCodeTTarif().equals(Const.C_AUCUN))value=null;
		//if(value instanceof TaTTiersDTO && ((TaTTiersDTO) value).getCodeTTiers()!=null && ((TaTTiersDTO) value).getCodeTTiers().equals(Const.C_AUCUN))value=null;
		if(value instanceof TaCPaiement && ((TaCPaiement) value).getCodeCPaiement()!=null && ((TaCPaiement) value).getCodeCPaiement().equals(Const.C_AUCUN))value=null;
		if(value instanceof TaTPaiement && ((TaTPaiement) value).getCodeTPaiement()!=null && ((TaTPaiement) value).getCodeTPaiement().equals(Const.C_AUCUN))value=null;
		if(value instanceof TaTTarif && ((TaTTarif) value).getCodeTTarif()!=null && ((TaTTarif) value).getCodeTTarif().equals(Const.C_AUCUN))value=null;
		if(value instanceof TaTTvaDoc && ((TaTTvaDoc) value).getCodeTTvaDoc()!=null && ((TaTTvaDoc) value).getCodeTTvaDoc().equals(Const.C_AUCUN))value=null;
		if(value instanceof TaFamilleTiers && ((TaFamilleTiers) value).getCodeFamille()!=null && ((TaFamilleTiers) value).getCodeFamille().equals(Const.C_AUCUN))value=null;
		}
		
		validateUIField(nomChamp,value);
	}
	
	public boolean validateUIField(String nomChamp,Object value) {
		
		boolean changement=false;
		
		
		try {				
				if(nomChamp.equals(Const.C_CODE_T_TIERS)) {
						TaTTiers entity = null;
						if(value!=null){
							if(value instanceof TaTTiers){
								entity=(TaTTiers) value;
//								entity = taTTiersService.findByCode(entity.getCodeTTiers());
							}else{
								entity = taTTiersService.findByCode((String)value);
							}
						}else{
							selectedTaTiersDTO.setCodeTTiers("");
							taTTiersDTO.setCodeTTiers("");
							taTTiers=null;
						}						
						taTiers.setTaTTiers(entity);
					
				} else if(nomChamp.equals(Const.C_CODE_TIERS)) {
					changement=true;					
					if(value!=null) {
						if(taTiers.getCodeTiers()!=null)changement=!((String)value).equals(taTiers.getCodeTiers());
						taTiersService.verrouilleCode((String)value);
						if((changement)||(taTiers.getCodeCompta()==null)||(taTiers.getCodeCompta()!=null && taTiers.getCodeCompta().equals("")))
							if(((String)value).length()>6)
							taTiers.setCodeCompta(((String)value).substring(0, 7));
							else taTiers.setCodeCompta(((String)value).substring(0, ((String)value).length()));
					}else{
						taTiers.setCodeCompta("");
					}
					if(changement && modeEcran.dataSetEnModeModification()){
						FacesContext context = FacesContext.getCurrentInstance();  
						context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Tiers", Const.C_MESSAGE_CHANGEMENT_CODE));
					}
					selectedTaTiersDTO.setCodeCompta(taTiers.getCodeCompta());
				}else if(nomChamp.equals(Const.C_CODE_COMPTA)) {

				}
				else if(nomChamp.equals(Const.C_CODE_T_CIVILITE)) {

						TaTCivilite entity =null;
						if(value!=null){
							if(value instanceof TaTCivilite){
							entity=(TaTCivilite) value;
//							entity = taTCivlicteService.findByCode(entity.getCodeTCivilite());
							}else{
								entity = taTCivlicteService.findByCode((String)value);								
							}
						}else{
							selectedTaTiersDTO.setCodeTCivilite(null);
							taTCiviliteDTO.setCodeTCivilite(null);
							taTCivilite=null;
						}
						
						taTiers.setTaTCivilite(entity);

				}else if(nomChamp.equals(Const.C_CODE_FAMILLE_TIERS)) {

					TaFamilleTiers entity =null;
					if(value!=null){
						if(value instanceof TaFamilleTiers){
						entity=(TaFamilleTiers) value;
						}else{
							entity = taFamilleTiersService.findByCode((String)value);								
						}
					}else{
						selectedTaTiersDTO.setCodeFamilleTiers(null);
						taFamilleTiersDTO.setCodeFamille(null);
						taFamilleTiers=null;
					}
					
					taTiers.setTaFamilleTiers(entity);

			}else  if(nomChamp.equals(Const.C_CODE_T_TVA_DOC)) {

						TaTTvaDoc entity = null;
						if(value!=null){
							if(value instanceof TaTTvaDoc){
							entity=(TaTTvaDoc) value;
//							entity = taTTvaDocService.findByCode(entity.getCodeTTvaDoc());
							}else{
								entity = taTTvaDocService.findByCode((String)value);
							}
						}else{
							selectedTaTiersDTO.setCodeTTvaDoc(null);
						}
						
						taTiers.setTaTTvaDoc(entity);

				}
				else if(nomChamp.equals(Const.C_TVA_I_COM_COMPL)|| nomChamp.equals(Const.C_ACCISE)
						|| nomChamp.equals(Const.C_SIRET_COMPL)) {

					taTiers.initComplTiers(value);
						
						if(value!=null && taTiers.getTaCompl()!=null) {
						PropertyUtils.setSimpleProperty(taTiers.getTaCompl(), nomChamp, value);
						PropertyUtils.setSimpleProperty(selectedTaTiersDTO, nomChamp, value);
						}				
				}else if(nomChamp.equals(Const.C_CODE_T_ENTITE)) {

						TaTEntite entity = null;
						if(value!=null){
							if(value instanceof TaTEntite){
							entity=(TaTEntite) value;
//							entity = taTEntiteService.findByCode(entity.getCodeTEntite());
							}else{
								entity = taTEntiteService.findByCode((String) value);
							}
						}else{
							selectedTaTiersDTO.setCodeTEntite(null);
							taTEntiteDTO.setCodeTEntite(null);
							taTEntite=null;
						}
						
						taTiers.setTaTEntite(entity);
			
				}else if(nomChamp.equals(Const.C_CODE_C_PAIEMENT)) {

						TaCPaiement entity = null;
						if(value!=null){
							if(value instanceof TaCPaiement){
							entity=(TaCPaiement) value;
//							entity = taCPaiementService.findByCode(entity.getCodeCPaiement());
							}else{
								entity = taCPaiementService.findByCode((String) value);
							}
						}else{
							selectedTaTiersDTO.setCodeCPaiement(null);
						}
						
						taTiers.setTaCPaiement(entity);
					
				}else if(nomChamp.equals(Const.C_CODE_T_PAIEMENT)) {
						TaTPaiement entity = null;
						if(value!=null){
							if(value instanceof TaTPaiement){
							entity=(TaTPaiement) value;
//							entity = taTPaiementService.findByCode(entity.getCodeTPaiement());
							}else{
								entity = taTPaiementService.findByCode((String) value);
							}
						}else{
							selectedTaTiersDTO.setCodeTPaiement(null);
						}
						
						taTiers.setTaTPaiement(entity);
					
				}else if(nomChamp.equals(Const.C_CODE_T_TARIF)) {

						TaTTarif entity = null;
						if(value!=null){
							if(value instanceof TaTTarif){
							entity=(TaTTarif) value;
//							entity = taTTarifService.findByCode(entity.getCodeTTarif());
							}else{
								entity = taTTarifService.findByCode((String) value);
							}
						}else{
							selectedTaTiersDTO.setCodeTTarif(null);
						}
						
						taTiers.setTaTTarif(entity);
					
				}else if(nomChamp.equals(Const.C_NUMERO_TELEPHONE)) {
					taTiers.initTelephoneTiers(value, telEstRempli(),ConstPreferencesTiers.CORRESPONDANCE_TELEPHONE_ADMINISTRATIF_DEFAUT,
							ConstPreferencesTiers.CORRESPONDANCE_TELEPHONE_COMMERCIAL_DEFAUT);
						if(value!=null && !value.equals("")) { 
							PropertyUtils.setSimpleProperty(taTiers.getTaTelephone(), nomChamp, value);
						}
						if(taTiers.getTaTelephone()==null)selectedTaTiersDTO.setNumeroTelephone(null);
						if(taTiers.getTaTelephone()!=null &&
								taTiers.getTaTelephone().getTaTTel()==null){
							TaTTel taTTel = new TaTTel();
							taTTel.setCodeTTel(ConstPreferencesTiers.TTEL_DEFAUT);
							if(!taTTel.getCodeTTel().equals("")){
								//TaTTelDAO taTTelDAO = new TaTTelDAO(getEm());
								taTTel=taTTelService.findByCode(taTTel.getCodeTTel());
								taTiers.getTaTelephone().setTaTTel(taTTel);
							}
						}					
				}else if(nomChamp.equals(Const.C_NOM_ENTREPRISE)) {
					taTiers.initEntrepriseTiers(value);
						if(value!=null && !value.equals("")) { 
						PropertyUtils.setSimpleProperty(taTiers.getTaEntreprise(), nomChamp, value);
						}
						if(taTiers.getTaEntreprise()==null)selectedTaTiersDTO.setNomEntreprise(null);
				}else if(nomChamp.equals(Const.C_ADRESSE_EMAIL)) {
					taTiers.initEmailTiers(value,ConstPreferencesTiers.CORRESPONDANCE_EMAIL_ADMINISTRATIF_DEFAUT,ConstPreferencesTiers.CORRESPONDANCE_EMAIL_COMMERCIAL_DEFAUT);
						if(value!=null && !value.equals("")) {
							PropertyUtils.setSimpleProperty(taTiers.getTaEmail(), nomChamp, value);
						}
						if(taTiers.getTaEmail()==null)selectedTaTiersDTO.setAdresseEmail(null);
						if(taTiers.getTaEmail()!=null &&
								taTiers.getTaEmail().getTaTEmail()==null){
							TaTEmail taTEmail = new TaTEmail();
							taTEmail.setCodeTEmail(ConstPreferencesTiers.TEMAIL_DEFAUT);
							if(!taTEmail.getCodeTEmail().equals("")){
								taTEmail=taTEmailService.findByCode(taTEmail.getCodeTEmail());
								taTiers.getTaEmail().setTaTEmail(taTEmail);
							}
						}	

				}else if(nomChamp.equals(Const.C_ADRESSE_WEB)) {
					taTiers.initWebTiers(value);
						if(value!=null && !value.equals("")) {
							PropertyUtils.setSimpleProperty(taTiers.getTaWeb(), nomChamp, value);
						}
						if(taTiers.getTaWeb()==null)selectedTaTiersDTO.setAdresseWeb(null);
						if(taTiers.getTaWeb()!=null &&
								taTiers.getTaWeb().getTaTWeb()==null){
							TaTWeb taTWeb = new TaTWeb();
							taTWeb.setCodeTWeb(ConstPreferencesTiers.TWEB_DEFAUT);
							if(!taTWeb.getCodeTWeb().equals("")){
								taTWeb=taTWebService.findByCode(taTWeb.getCodeTWeb());
								taTiers.getTaWeb().setTaTWeb(taTWeb);
							}
						}			

				}else if(nomChamp.equals(Const.C_LIBL_COMMENTAIRE)) {
					//taTiers.setTaCommentaire(taCommentaire);
				}else if(nomChamp.equals(Const.C_ADRESSE1_ADRESSE)
						|| nomChamp.equals(Const.C_ADRESSE2_ADRESSE)
						|| nomChamp.equals(Const.C_ADRESSE3_ADRESSE)
						|| nomChamp.equals(Const.C_CODEPOSTAL_ADRESSE)
						|| nomChamp.equals(Const.C_VILLE_ADRESSE)
						|| nomChamp.equals(Const.C_PAYS_ADRESSE)) {

					taTiers.initAdresseTiers(value,adresseEstRempli(),ConstPreferencesTiers.CORRESPONDANCE_ADRESSE_ADMINISTRATIF_DEFAUT,
							ConstPreferencesTiers.CORRESPONDANCE_ADRESSE_COMMERCIAL_DEFAUT);
						if(value!=null 
	//							&& !value.equals("")
								/*
								 * Bug #1192
								 * Si on laisse !value.equals("") on ne peu plus effacer de ligne d'adresse
								 */
								) {	
									if(taTiers.getTaAdresse()!=null) {
										PropertyUtils.setSimpleProperty(taTiers.getTaAdresse(), nomChamp, value);
									}
						}
						if(taTiers.getTaAdresse()==null){
							initAdresseNull();
						}
						if(taTiers.getTaAdresse()!=null &&
								taTiers.getTaAdresse().getTaTAdr()==null){
							TaTAdr taTAdr = new TaTAdr();
							taTAdr.setCodeTAdr(ConstPreferencesTiers.TADR_DEFAUT);
							if(!taTAdr.getCodeTAdr().equals("")){
								taTAdr = taTAdresseService.findByCode(taTAdr.getCodeTAdr());
								taTiers.getTaAdresse().setTaTAdr(taTAdr);
							}
						}			
					
				} 

			return false;

		} catch (Exception e) {
			
		}
		return false;
	}
	private void initAdresseNull() {
		if(taTiers.getTaAdresse()==null){
			selectedTaTiersDTO.setAdresse1Adresse(null);
			selectedTaTiersDTO.setAdresse2Adresse(null);
			selectedTaTiersDTO.setAdresse3Adresse(null);
			selectedTaTiersDTO.setCodepostalAdresse(null);
			selectedTaTiersDTO.setVilleAdresse(null);
			selectedTaTiersDTO.setPaysAdresse(null);
		}
	}
	
	private void initTelephoneNull() {
		if(taTiers.getTaTelephone()==null){
			selectedTaTiersDTO.setAdresse1Adresse(null);
		
		}
	}
	private boolean adresseEstRempli() {
		boolean retour=false;
		if(selectedTaTiersDTO.getAdresse1Adresse()!=null && !selectedTaTiersDTO.getAdresse1Adresse().equals(""))return true;
		if(selectedTaTiersDTO.getAdresse2Adresse()!=null && !selectedTaTiersDTO.getAdresse2Adresse().equals(""))return true;
		if(selectedTaTiersDTO.getAdresse3Adresse()!=null && !selectedTaTiersDTO.getAdresse3Adresse().equals(""))return true;
		if(selectedTaTiersDTO.getCodepostalAdresse()!=null && !selectedTaTiersDTO.getCodepostalAdresse().equals(""))return true;
		if(selectedTaTiersDTO.getVilleAdresse()!=null && !selectedTaTiersDTO.getVilleAdresse().equals(""))return true;
		if(selectedTaTiersDTO.getPaysAdresse()!=null && !selectedTaTiersDTO.getPaysAdresse().equals(""))return true;
		return retour;
	}
	
	private boolean telEstRempli() {
		boolean retour=false;
		if(selectedTaTiersDTO.getNumeroTelephone()!=null && !selectedTaTiersDTO.getNumeroTelephone().equals(""))return true;
		return retour;
	}
	
	
	public List<TaTCivilite> civiliteAutoComplete(String query) {
        List<TaTCivilite> allValues = taTCivlicteService.selectAll();
        List<TaTCivilite> filteredValues = new ArrayList<TaTCivilite>();
        TaTCivilite civ = new TaTCivilite();
        civ.setCodeTCivilite(Const.C_AUCUN);
        filteredValues.add(civ);
        for (int i = 0; i < allValues.size(); i++) {
        	 civ = allValues.get(i);
            if(query.equals("*") || civ.getCodeTCivilite().toLowerCase().contains(query.toLowerCase())) {
                filteredValues.add(civ);
            }
        }
         
        return filteredValues;
    }
	
	public List<TaFamilleTiersDTO> familleTiersAutoCompleteLight(String query) {
        List<TaFamilleTiersDTO> allValues = taFamilleTiersService.selectAllDTO();
        List<TaFamilleTiersDTO> filteredValues = new ArrayList<TaFamilleTiersDTO>();
        TaFamilleTiersDTO civ = new TaFamilleTiersDTO();
        civ.setCodeFamille(Const.C_AUCUN);
        filteredValues.add(civ);
        for (int i = 0; i < allValues.size(); i++) {
        	 civ = allValues.get(i);
            if(query.equals("*") || civ.getCodeFamille().toLowerCase().contains(query.toLowerCase())) {
                filteredValues.add(civ);
            }
        }
         
        return filteredValues;
    }
	
	public List<TaTCiviliteDTO> civiliteAutoCompleteLight(String query) {
        List<TaTCiviliteDTO> allValues = taTCivlicteService.selectAllDTO();
        List<TaTCiviliteDTO> filteredValues = new ArrayList<TaTCiviliteDTO>();
        TaTCiviliteDTO civ = new TaTCiviliteDTO();
        civ.setCodeTCivilite(Const.C_AUCUN);
        filteredValues.add(civ);
        for (int i = 0; i < allValues.size(); i++) {
        	 civ = allValues.get(i);
            if(query.equals("*") || civ.getCodeTCivilite().toLowerCase().contains(query.toLowerCase())) {
                filteredValues.add(civ);
            }
        }
         
        return filteredValues;
    }
	public List<TaTTiers> typeTiersAutoComplete(String query) {
        List<TaTTiers> allValues = taTTiersService.selectAll();
        List<TaTTiers> filteredValues = new ArrayList<TaTTiers>();
         
        for (int i = 0; i < allValues.size(); i++) {
        	TaTTiers civ = allValues.get(i);
            if(query.equals("*") || civ.getCodeTTiers().toLowerCase().contains(query.toLowerCase())) {
                filteredValues.add(civ);
            }
        }
         
        return filteredValues;
    }
	public List<TaTTiersDTO> typeTiersAutoCompleteLight(String query) {
        List<TaTTiersDTO> allValues = taTTiersService.selectAllDTO();
        List<TaTTiersDTO> filteredValues = new ArrayList<TaTTiersDTO>();
         
        for (int i = 0; i < allValues.size(); i++) {
        	TaTTiersDTO civ = allValues.get(i);
            if(query.equals("*") || civ.getCodeTTiers().toLowerCase().contains(query.toLowerCase())) {
                filteredValues.add(civ);
            }
        }
         
        return filteredValues;
    }
	public List<TaTEntite> typeEntiteAutoComplete(String query) {
        List<TaTEntite> allValues = taTEntiteService.selectAll();
        List<TaTEntite> filteredValues = new ArrayList<TaTEntite>();
         
        for (int i = 0; i < allValues.size(); i++) {
        	TaTEntite civ = allValues.get(i);
            if(query.equals("*") || civ.getCodeTEntite().toLowerCase().contains(query.toLowerCase())) {
                filteredValues.add(civ);
            }
        }
         
        return filteredValues;
    }
	
	// Dime - Début
	public List<TaTEntiteDTO> typeEntiteAutoCompleteLight(String query) {
        List<TaTEntiteDTO> allValues = taTEntiteService.findByCodeLight("");
        List<TaTEntiteDTO> filteredValues = new ArrayList<TaTEntiteDTO>();
        TaTEntiteDTO civ = new TaTEntiteDTO();
        civ.setCodeTEntite(Const.C_AUCUN);
        filteredValues.add(civ);
        for (int i = 0; i < allValues.size(); i++) {
        	 civ = allValues.get(i);
            if(query.equals("*") || civ.getCodeTEntite().toLowerCase().contains(query.toLowerCase())) {
            	   filteredValues.add(civ);
            }
        }
         
        return filteredValues;
    }
	
	public List<TaTTarif> typeTarifAutoComplete(String query) {
		List<TaTTarif> allValues = taTTarifService.selectAll();
		List<TaTTarif> filteredValues = new ArrayList<TaTTarif>();
		TaTTarif cp = new TaTTarif();
		cp.setCodeTTarif(Const.C_AUCUN);
		filteredValues.add(cp);
		for (int i = 0; i < allValues.size(); i++) {
			cp = allValues.get(i);
			if(query.equals("*") || cp.getCodeTTarif().toLowerCase().contains(query.toLowerCase())) {
				filteredValues.add(cp);
			}
		}

		return filteredValues;
	}
	
	public List<TaTPaiement> typePaiementAutoComplete(String query) {
		List<TaTPaiement> allValues = taTPaiementService.selectAll();
		List<TaTPaiement> filteredValues = new ArrayList<TaTPaiement>();
		TaTPaiement cp = new TaTPaiement();
		cp.setCodeTPaiement(Const.C_AUCUN);
		filteredValues.add(cp);
		for (int i = 0; i < allValues.size(); i++) {
			cp = allValues.get(i);
			if(query.equals("*") || cp.getCodeTPaiement().toLowerCase().contains(query.toLowerCase())) {
				filteredValues.add(cp);
			}
		}

		return filteredValues;
	}
	
	public List<TaCPaiement> condPaiementAutoComplete(String query) {
		List<TaCPaiement> allValues = taCPaiementService.rechercheParType("Tiers");
		List<TaCPaiement> filteredValues = new ArrayList<TaCPaiement>();
		TaCPaiement cp = new TaCPaiement();
		cp.setCodeCPaiement(Const.C_AUCUN);
		filteredValues.add(cp);
		for (int i = 0; i < allValues.size(); i++) {
			cp = allValues.get(i);
			if(query.equals("*") || cp.getCodeCPaiement().toLowerCase().contains(query.toLowerCase())) {
				filteredValues.add(cp);
			}
		}

		return filteredValues;
	}
	
	public List<TaTTvaDoc> typeTvaDocAutoComplete(String query) {
		List<TaTTvaDoc> allValues = taTTvaDocService.selectAll();
		List<TaTTvaDoc> filteredValues = new ArrayList<TaTTvaDoc>();
		TaTTvaDoc cp = new TaTTvaDoc();
		cp.setCodeTTvaDoc(Const.C_AUCUN);
		filteredValues.add(cp);
		for (int i = 0; i < allValues.size(); i++) {
			cp = allValues.get(i);
			if(query.equals("*") || cp.getCodeTTvaDoc().toLowerCase().contains(query.toLowerCase())) {
				filteredValues.add(cp);
			}
		}

		return filteredValues;
	}

	public TaTTiers getTaTTiers() {
		return taTTiers;
	}

	public void setTaTTiers(TaTTiers taTTiers) {
		this.taTTiers = taTTiers;
	}

	public TaTCivilite getTaTCivilite() {
		return taTCivilite;
	}

	public void setTaTCivilite(TaTCivilite taTCivilite) {
		this.taTCivilite = taTCivilite;
	}

	public TaTEntite getTaTEntite() {
		return taTEntite;
	}

	public void setTaTEntite(TaTEntite taTEntite) {
		this.taTEntite = taTEntite;
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

	public TaTPaiement getTaTPaiement() {
		return taTPaiement;
	}

	public void setTaTPaiement(TaTPaiement taTPaiement) {
		this.taTPaiement = taTPaiement;
	}

	public TaCPaiement getTaCPaiement() {
		return taCPaiement;
	}

	public void setTaCPaiement(TaCPaiement taCPaiement) {
		this.taCPaiement = taCPaiement;
	}

	public TaTTvaDoc getTaTTvaDoc() {
		return taTTvaDoc;
	}

	public void setTaTTvaDoc(TaTTvaDoc taTTvaDoc) {
		this.taTTvaDoc = taTTvaDoc;
	}

	public TaFamilleTiersDTO getTaFamilleTiersDTO() {
		return taFamilleTiersDTO;
	}

	public void setTaFamilleTiersDTO(TaFamilleTiersDTO taFamilleTiersDTO) {
		this.taFamilleTiersDTO = taFamilleTiersDTO;
	}

	public TaFamilleTiers getTaFamilleTiers() {
		return taFamilleTiers;
	}

	public void setTaFamilleTiers(TaFamilleTiers taFamilleTiers) {
		this.taFamilleTiers = taFamilleTiers;
	}

	public TaTiers getTaTiers() {
		return taTiers;
	}

	public void setTaTiers(TaTiers taTiers) {
		this.taTiers = taTiers;
	}

	public TaTiersDTO rempliDTO(){
		if(taTiers!=null){			
			try {
				taTiers=taTiersService.findByCode(taTiers.getCodeTiers());
				mapperModelToUI.map(taTiers, selectedTaTiersDTO);
				if(values.contains(selectedTaTiersDTO))values.add(selectedTaTiersDTO);
			} catch (FinderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return selectedTaTiersDTO;
	}

	public StreamedContent getExportationTiers() {
		File f = null;
		InputStream stream = null;
		try {	
			f = taTiersService.exportToCSV(values);
			stream = new FileInputStream(f); 
		} catch(Exception e) {
			e.printStackTrace();
		}
		exportationTiers = new DefaultStreamedContent(stream,null,"tiers.csv");
		
		return exportationTiers;
	}

	public TabView getTabViewTiers() {
		return tabViewTiers;
	}

	public void setTabViewTiers(TabView tabViewTiers) {
		this.tabViewTiers = tabViewTiers;
	}

	public AdresseController getAdresseController() {
		return adresseController;
	}

	public void setAdresseController(AdresseController adresseController) {
		this.adresseController = adresseController;
	}

	public void actCreerEvenementTiers(ActionEvent e) {
		try {
//			TaEvenement evt = new TaEvenement();
//
//			evt.setProprietaire(taUtilisateurService.findById(sessionInfo.getUtilisateur().getId()));
//			
//			evt.setCodeEvenement("code aa");
//			evt.setLibelleEvenement("libelle aaaa");
//			evt.setDescription("Description bbb");
//
//			List<TaTiers> lt = new ArrayList<>();
//			lt.add(taTiers);
//			evt.setListeTiers(lt);
//
//			evt.setDateDebut(new Date());
//
//			taEvenementService.merge(evt);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	
	public void actDialogEvenement(ActionEvent actionEvent) {
	    
//        Map<String,Object> options = new HashMap<String, Object>();
//        options.put("modal", true);
//        options.put("draggable", false);
//        options.put("resizable", false);
//        options.put("contentHeight", 640);
//        options.put("modal", true);
//        
//        AgendaBean.initAppelDialogue();
//        
//        //Map<String,List<String>> params = null;
//        Map<String,List<String>> params = new HashMap<String,List<String>>();
//        List<String> list = new ArrayList<String>();
//        list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
//        params.put("modeEcranDefaut", list);
//        
//        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
//		Map<String, Object> sessionMap = externalContext.getSessionMap();
//		
//		//String numLot =  (String) actionEvent.getComponent().getAttributes().get("idEvenement");
//		
		EvenementParam param = new EvenementParam();
		param.setListeTiers(new ArrayList<>());
		param.getListeTiers().add(taTiers);
		actDialogEvenement(param);
//		sessionMap.put(EvenementParam.NOM_OBJET_EN_SESSION, param);
//        
//        PrimeFaces.current().dialog().openDynamic("agenda/dialog_evenement", options, params);	    
	}
	public String texteMailDossierToTiersHTML() {
		return texteMailDossierToTiers(null, true);
	}
	public String texteMailDossierToTiers () {
		return texteMailDossierToTiers(null, false);
	}
	public String texteMailDossierToTiers (String nomSociete, boolean html) {
		if(nomSociete == null) {
			nomSociete = infoEntreprise.getNomInfoEntreprise();
		}
		
		
		String texte = "Bonjour,<br>" + 
				"<br>" + 
				"c'est très aimablement que la société "+nomSociete+" attire votre attention sur le logiciel de gestion en ligne Bureau de Gestion Cloud, qu'elle utilise quotidiennement pour sa propre gestion.<br>" + 
				"<br>" + 
				"Grâce à elle, vous pouvez bénéficier de 10% de remise sur votre premier abonnement et de l'utilisation d'un logiciel de gestion en ligne doté de nombreuses fonctionnalités pour un budget maîtrisé.<br>" + 
				"<br>" + 
				"N'hésitez pas à créer votre dossier de gestion en ligne sur bdg.cloud, il est gratuit et complet pendant 45 jours, sans engagement. A l'issue de la période d'essai, vous pouvez choisir la configuration qui vous correspond le mieux.<br>" + 
				"<br>" + 
				"Nous serons heureux de votre prochaine visite sur bdg.cloud et pourquoi pas, de vos premier pas sur le Bureau de Gestion Cloud.<br>" + 
				"<br>" + 
				"Très cordialement,<br>" + 
				"<br>" + 
				"Legrain Informatique<br>" + 
				"<br>" + 
				"Editeur de logiciel de gestion<br>" + 
				"<br>" + 
				"05.63.30.31.44";
		
		if(!html) {
			texte = texte.replace("<br>","\n");
		}
		return texte;
		
	}
	
	
	public String texteMailBdgToDossierHTML() {
		return texteMailBdgToDossier(null, true);
	}
	public String texteMailBdgToDossier () {
		return texteMailBdgToDossier(null, false);
	}
	public String texteMailBdgToDossier (String nomTiers, boolean html) {
		String nomClient = "ce client";
		if(nomTiers == null && taTiers.getNomTiers() == null) {
			nomTiers = " votre client";
		}else if(nomTiers == null && taTiers.getNomTiers() != null){
			nomTiers = taTiers.getNomTiers();
		}
		nomClient = nomTiers;
		
		
		
		String texte = "Bonjour,<br>" + 
				"<br>" + 
				"vous avez recommandé le logiciel de gestion Bureau de Gestion Cloud auprès de "+nomTiers+" et nous vous en remercions.<br>" + 
				"<br>" + 
				"Si "+nomClient+" décide d'utiliser le logiciel, vous bénéficierez d'une remise égale à 10% du montant HT de son premier abonnement.<br>" + 
				"<br>" + 
				"Par exemple, si "+nomClient+" souscrit à la version Gestion Commerciale, à 35 € HT / mois, pour une durée d'un an (420 € HT), vous bénéficiez d'une remise d'un montant de 42 € TTC qui sera déduit sur votre prochain abonnement engagement annuel.<br>" + 
				"<br>" + 
				"Vous serez tenu informé si "+nomClient+" prend un abonnement pour le Bureau de Gestion Cloud.<br>" + 
				"<br>" + 
				"Très cordialement,<br>" + 
				"<br>" + 
				"Legrain Informatique.<br>" + 
				"<br>" + 
				"05.63.30.31.44";
		
		if(!html) {
			texte = texte.replace("<br>","\n");
		}
		return texte;
		
	}
	

	public String texteMailBdgToLegrain (String nomSocieteDossier, String nomTiers) {			
		String texte = " Le dossier "+nomSocieteDossier+" vient de recommander le logiciel bdg au tiers "+nomTiers;
		return texte;
		
	}
	
	public void actEnvoiRecommandation(ActionEvent actionEvent) {
		
		if(taTiers != null && taTiers.getTaEmail() != null && taTiers.getTaEmail().getAdresseEmail() != null) {
			
			if(infoEntreprise != null && infoEntreprise.getNomInfoEntreprise() != null && infoEntreprise.getEmailInfoEntreprise()!= null) {
				
				List<TaEmail> emailTiersDestinataire = null;
				List<TaContactMessageEmail> contactMessageEmailDestinataire = new ArrayList<>();
				List<String> adressesCcLoc = new ArrayList<>();
				List<TaEmail> emailTiersCc = null;
				List<TaContactMessageEmail> contactMessageEmailCc = new ArrayList<>();
				List<String> adressesBccLoc = new ArrayList<>();
				List<TaEmail> emailTiersBcc = null;
				List<TaContactMessageEmail> contactMessageEmailBcc = new ArrayList<>();
				List<String> adressesCc = new ArrayList<>();
				List<String> adressesBcc = new ArrayList<>();
				File[] attachment = null;
				
				List<String> dest = new ArrayList<>();
				dest.add(taTiers.getTaEmail().getAdresseEmail());
				//a contact@bdg.cloud sauf le notre qui a le dossier en replyto
				String adresseExpediteur = "";
				String replyTo = "contact@bdg.cloud";
				//String replyTo = null;
				TaUtilisateur userDossier = sessionInfo.getUtilisateur();
				String nomSociete = infoEntreprise.getNomInfoEntreprise();
				String emailDossier = infoEntreprise.getEmailInfoEntreprise();
				String subject = nomSociete+" vous recommande le logiciel de gestion bdg.cloud - Bureau de Gestion Cloud";
				String textPart = texteMailDossierToTiers();
				String htmlPart = texteMailDossierToTiersHTML();
				
				//mail du dossier vers le tiers
				//lgrMailService.send(fromEmail, fromName, subject, textPart, htmlPart, destinataires, null, replyToEmail);
				emailServiceFinder.sendAndLogEmailDossier(
						null,
						adresseExpediteur,//null,
						nomSociete,
						subject,
						textPart, 
						htmlPart, 
						dest, 
						emailTiersDestinataire,
						contactMessageEmailDestinataire,
						adressesCc, 
						emailTiersCc,
						contactMessageEmailCc,
						adressesBcc, 
						emailTiersBcc,
						contactMessageEmailBcc,
						attachment,
						replyTo,//replyTo
						userDossier
						);
				
				
				
				

				String subjectDossier = "Bureau de Gestion Cloud - recommandation du logiciel";
				String textPartDossier = texteMailBdgToDossier();
				String htmlPartDossier = texteMailBdgToDossierHTML();
				List<String> destDossier = new ArrayList<>();
				destDossier.add(emailDossier);
				//a contact@bdg.cloud sauf le notre qui a le dossier en replyto
				String replyToEmailDossier = "contact@bdg.cloud";
				//mail de bdg notification vers le dossier
				//lgrMailService.send(fromEmailDossier, fromNameDossier, subjectDossier, textPartDossier, htmlPartDossier, destinatairesDossier, null, replyToEmailDossier);
				emailServiceFinder.sendAndLogEmailDossier(
						null,
						adresseExpediteur,//null,
						"BDG",
						subjectDossier,
						textPartDossier, 
						htmlPartDossier, 
						destDossier, 
						emailTiersDestinataire,
						contactMessageEmailDestinataire,
						adressesCc, 
						emailTiersCc,
						contactMessageEmailCc,
						adressesBcc, 
						emailTiersBcc,
						contactMessageEmailBcc,
						attachment,
						replyToEmailDossier,//replyTo
						userDossier
						);
				
				String subjectBdg = "Notification de recommandation";
				String textPartBdg = texteMailBdgToLegrain(nomSociete, taTiers.getNomTiers());
				String htmlPartBdg = texteMailBdgToLegrain(nomSociete, taTiers.getNomTiers());;
				List<String> destBdg = new ArrayList<>();
				destBdg.add("contact@bdg.cloud");
				//a contact@bdg.cloud sauf le notre qui a le dossier en replyto
				String replyToEmailBdg = emailDossier;

				// mail de bdg notification vers nous contact@bdg.cloud
				//lgrMailService.send(fromEmailBdg, fromNameBdg, subjectBdg, textPartBdg, htmlPartBdg, destinatairesBdg, null, replyToEmailBdg);
				emailServiceFinder.sendAndLogEmailDossier(
						null,
						adresseExpediteur,//null,
						"BDG",
						subjectBdg,
						textPartBdg, 
						htmlPartBdg, 
						destBdg, 
						emailTiersDestinataire,
						contactMessageEmailDestinataire,
						adressesCc, 
						emailTiersCc,
						contactMessageEmailCc,
						adressesBcc, 
						emailTiersBcc,
						contactMessageEmailBcc,
						attachment,
						replyToEmailBdg,//replyTo
						userDossier
						);
				
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Tiers", "Message de recommandation envoyé."));
			}else {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Tiers", "Erreur pendant l'envoi de recommandation, vérifiez les informations de votre dossier (infos entreprise)."));
			}
			
		}else {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Tiers", "Erreur pendant l'envoi de recommandation, vérifiez l'adresse mail du tiers."));
		}
		
	

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
		email.setDestinataires(new String[]{taTiers.getTaEmail().getAdresseEmail()});
		email.setEmailDestinataires(new TaEmail[]{taTiers.getTaEmail()});
//		
//		EmailPieceJointeParam pj1 = new EmailPieceJointeParam();
//		pj1.setFichier(new File(((ITaFactureServiceRemote)taDocumentService).generePDF(masterEntity.getIdDocument())));
//		pj1.setTypeMime("pdf");
//		pj1.setNomFichier(pj1.getFichier().getName());
//		email.setPiecesJointes(
//				new EmailPieceJointeParam[]{pj1}
//				);
		
		sessionMap.put(EmailParam.NOM_OBJET_EN_SESSION, email);
        
        PrimeFaces.current().dialog().openDynamic("/dialog_email", options, params);    
	}
	
	public void handleReturnDialogEmail(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			//List<ControleConformiteJSF> listeControle = (List<ControleConformiteJSF>) event.getObject();
			TaMessageEmail j = (TaMessageEmail) event.getObject();
			
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Email", 
					"Email envoyée "
					)); 
		}
	}
	
	public void actDialogSms(ActionEvent actionEvent) {
	    
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
		
		SmsParam email = new SmsParam();
		email.setNumeroExpediteur(null);
//		email.setNomExpediteur(taInfoEntrepriseService.findInstance().getNomInfoEntreprise()); 
//		email.setSubject(taInfoEntrepriseService.findInstance().getNomInfoEntreprise()+" Facture "+masterEntity.getCodeDocument()); 
//		email.setBodyPlain("Votre facture "+masterEntity.getCodeDocument());
//		email.setBodyHtml("Votre facture "+masterEntity.getCodeDocument());
		email.setDestinataires(new String[]{taTiers.getTaTelephone().getNumeroTelephone()});
		email.setTelephoneDestinataires(new TaTelephone[]{taTiers.getTaTelephone()});
//		
//		EmailPieceJointeParam pj1 = new EmailPieceJointeParam();
//		pj1.setFichier(new File(((ITaFactureServiceRemote)taDocumentService).generePDF(masterEntity.getIdDocument())));
//		pj1.setTypeMime("pdf");
//		pj1.setNomFichier(pj1.getFichier().getName());
//		email.setPiecesJointes(
//				new EmailPieceJointeParam[]{pj1}
//				);
		
		sessionMap.put(SmsParam.NOM_OBJET_EN_SESSION, email);
        
        PrimeFaces.current().dialog().openDynamic("/dialog_sms", options, params);    
	}
	
	public void handleReturnDialogSms(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			//List<ControleConformiteJSF> listeControle = (List<ControleConformiteJSF>) event.getObject();
			TaMessageSMS j = (TaMessageSMS) event.getObject();
			
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("SMS", 
					"SMS envoyé "
					)); 
		}
	}
	
	public void actDialogPush(ActionEvent actionEvent) {
	    
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
		
		PushParam push = new PushParam();
//		push.setNumeroExpediteur(null);
//		email.setNomExpediteur(taInfoEntrepriseService.findInstance().getNomInfoEntreprise()); 
//		email.setSubject(taInfoEntrepriseService.findInstance().getNomInfoEntreprise()+" Facture "+masterEntity.getCodeDocument()); 
//		email.setBodyPlain("Votre facture "+masterEntity.getCodeDocument());
//		email.setBodyHtml("Votre facture "+masterEntity.getCodeDocument());
		List<TaTiers> l = new ArrayList<>();
		l.add(taTiers);
		push.setDestinataires(l);
//		email.setTelephoneDestinataires(new TaTelephone[]{taTiers.getTaTelephone()});
//		
//		EmailPieceJointeParam pj1 = new EmailPieceJointeParam();
//		pj1.setFichier(new File(((ITaFactureServiceRemote)taDocumentService).generePDF(masterEntity.getIdDocument())));
//		pj1.setTypeMime("pdf");
//		pj1.setNomFichier(pj1.getFichier().getName());
//		email.setPiecesJointes(
//				new EmailPieceJointeParam[]{pj1}
//				);
		
		sessionMap.put(PushParam.NOM_OBJET_EN_SESSION, push);
        
        PrimeFaces.current().dialog().openDynamic("/dialog_message_push", options, params);    
	}
	
	public void handleReturnDialogPush(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			//List<ControleConformiteJSF> listeControle = (List<ControleConformiteJSF>) event.getObject();
			TaMessagePush j = (TaMessagePush) event.getObject();
			
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Message Push", 
					"Message push envoyé "
					)); 
		}
	}
	
	public void actDialogAdresseReorder(ActionEvent actionEvent) {		
		try {
			taTiers = taTiersService.findById(selectedTaTiersDTO.getId());
			adresseController.getAdresseReorderController().setMasterEntity(taTiers);
			if(adresseController.getAdresseReorderController().getCodeTAdr()!=null){
				adresseController.getAdresseReorderController().selectionTADr(adresseController.getSelectedTaAdresseDTO().getCodeTAdr());
				adresseController.getAdresseReorderController().refresh();
			}
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void actInitPaiementCarteBancaire(ActionEvent actionEvent) {
			//setFactureReglee(true);
	//actInitReglement();
		//calculDateEcheance(true);
		PaiementParam param = new PaiementParam();
		param.setDocumentPayableCB(null);
		param.setMontantPaiement(new BigDecimal(0));
		param.setMontantLibre(true);
		param.setTiersPourReglementLibre(taTiers);
		param.setOriginePaiement("BDG Tiers");
		PaiementCbController.actDialoguePaiementEcheanceParCarte(param);
	}
	
	public void handleReturnDialoguePaiementParCarte(SelectEvent event) {
		System.out.println("PaiementCbController.handleReturnDialoguePaiementEcheanceParCarte()");
		try {
			if(event!=null && event.getObject()!=null) {
				RetourPaiementCarteBancaire e = (RetourPaiementCarteBancaire) event.getObject();
				
//				actInitReglement(e.);
//				
//				//taDossierRcd = taDossierRcdService.findById(taDossierRcd.getIdDossierRcd());
//				
//				for (TaEcheance taEcheance : taDossierRcd.getTaEcheances()) {
//					if(taEcheance.getIdEcheance()==selectedTaEcheanceDTO.getId()) {
//						taEcheance.setTaReglementAssurance(e.getTaReglementAssurance());
//						taEcheance.getTaReglementAssurance().setTaEcheance(taEcheance);
//					}
//				}
//				
//				actEnregistrer(null);
//				actActionApresPaiementAccepte();
//				listeTaEcheanceDTO = taEcheanceService.findAllEcheanceRCDIdDTO(taDossierRcd.getIdDossierRcd());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public EmailController getEmailController() {
		return emailController;
	}

	public void setEmailController(EmailController emailController) {
		this.emailController = emailController;
	}


	public BanqueController getBanqueController() {
		return banqueController;
	}

	public void setBanqueController(BanqueController banqueController) {
		this.banqueController = banqueController;
	}

	public DashBoardParTiersController getDashBoardParTiersController() {
		return dashBoardParTiersController;
	}

	public void setDashBoardParTiersController(DashBoardParTiersController dashBoardParTiersController) {
		this.dashBoardParTiersController = dashBoardParTiersController;
	}

	public TaTTarif getTaTTarif() {
		return taTTarif;
	}

	public void setTaTTarif(TaTTarif taTTarif) {
		this.taTTarif = taTTarif;
	}

	public TelephoneController getTelephoneController() {
		return telephoneController;
	}

	public void setTelephoneController(TelephoneController telephoneController) {
		this.telephoneController = telephoneController;
	}

	public SmsController getSmsController() {
		return smsController;
	}

	public void setSmsController(SmsController smsController) {
		this.smsController = smsController;
	}

	public EmailTiersController getEmailTiersController() {
		return emailTiersController;
	}

	public void setEmailTiersController(EmailTiersController emailTiersController) {
		this.emailTiersController = emailTiersController;
	}

	public String getCLASS_CSS_TAB_FICHE_TIERS() {
		return CLASS_CSS_TAB_FICHE_TIERS;
	}

	public String getWV_TABVIEW_TIERS() {
		return WV_TABVIEW_TIERS;
	}

	public String getCLASS_CSS_TAB_DASH_TIERS() {
		return CLASS_CSS_TAB_DASH_TIERS;
	}

	public AbonnementTiersController getAbonnementTiersController() {
		return abonnementTiersController;
	}

	public void setAbonnementTiersController(AbonnementTiersController abonnementTiersController) {
		this.abonnementTiersController = abonnementTiersController;
	}

	public CarteBancaireController getCarteBancaireController() {
		return carteBancaireController;
	}

	public void setCarteBancaireController(CarteBancaireController carteBancaireController) {
		this.carteBancaireController = carteBancaireController;
	}

//	public DashBoardDocumentController getDashboardDocumentCtrl() {
//		return DashboardDocumentCtrl;
//	}
//
//	public void setDashboardDocumentCtrl(DashBoardDocumentController dashboardDocumentCtrl) {
//		DashboardDocumentCtrl = dashboardDocumentCtrl;
//	}
//	



	
	public void actDuplicationTiersdialog(ActionEvent actionEvent) {
		
//		PrimeFaces.current().dialog().openDynamic("aide");
    
        Map<String,Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("draggable", true);
        options.put("resizable", false);
        options.put("contentHeight", 540);
        options.put("modal", true);
        
        //Map<String,List<String>> params = null;
        Map<String,List<String>> params = new HashMap<String,List<String>>();
        List<String> list = new ArrayList<String>();
        list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
        List<String> listTiers = new ArrayList<String>();
        listTiers.add(selectedTaTiersDTO.getCodeTiers());
        params.put("modeEcranDefaut", list);
        params.put("codeTiers", listTiers);
        
        PrimeFaces.current().dialog().openDynamic("tiers/duplication_tiers", options, params);
			    
	}
	
	public void handleReturnDialogDuplicationTiers(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			param = (ParamDuplicationTiers) event.getObject();
			try {
				if(selectedTaTiersDTO!=null) {
					taTiers=taTiersService.findByCode(selectedTaTiersDTO.getCodeTiers());
					if(taTiers!=null) {
						tiersDuplique=(TaTiers) taTiers.clone();
						
											
						if(!param.getRepriseAdresse()) {
							tiersDuplique.getTaAdresses().clear();
							tiersDuplique.setTaAdresse(null);
						}
						if(!param.getRepriseCommentaire()) {
							tiersDuplique.setTaCommentaire(null);
						}
						if(!param.getRepriseComptesBancaires()) {
							tiersDuplique.getTaCompteBanques().clear();
						}
						if(!param.getRepriseConditionPaiement()) {
							tiersDuplique.setTaCPaiement(null);
						}
						if(!param.getRepriseFamilles()) {
							tiersDuplique.getTaFamilleTierses().clear();
							tiersDuplique.setTaFamilleTiers(null);
						}

						if(!param.getRepriseContacts()) {
							tiersDuplique.getTaConctacts().clear();
						}
						if(!param.getRepriseEmails()) {
							tiersDuplique.getTaEmails().clear();
							tiersDuplique.setTaEmail(null);
						}
						if(!param.getRepriseInfosJuridiques()) {
							tiersDuplique.setTaInfoJuridique(null);
							tiersDuplique.setTaCompl(null);
						}
						if(!param.getRepriseSitesWeb()) {
							tiersDuplique.getTaWebs().clear();
							tiersDuplique.setTaWeb(null);
						}
						if(!param.getRepriseTarifs()) {
							tiersDuplique.setTaTTarif(null);
						}
						if(!param.getRepriseTelephones()) {
							tiersDuplique.getTaTelephones().clear();
							tiersDuplique.setTaTelephone(null);
						}
						if(!param.getRepriseTypePaiement()) {
							tiersDuplique.setTaTPaiement(null);
						}
						actInserer(null);
						selectedTaTiersDTOs=null;
						taTiers=tiersDuplique;
//						if(taTiers!=null){
//							mapperModelToUI.map(taTiers, selectedTaTiersDTO);
//						}
						updateTab();
//						autoCompleteMapDTOtoUI();
						
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public ParamDuplicationTiers getParam() {
		return param;
	}

	public void setParam(ParamDuplicationTiers param) {
		this.param = param;
	}

	public TaTiers getTiersDuplique() {
		return tiersDuplique;
	}

	public void setTiersDuplique(TaTiers tiersDuplique) {
		this.tiersDuplique = tiersDuplique;
	}

	public String getTexteMailRecommandationDossierTiers() {
		return texteMailRecommandationDossierTiers;
	}

	public void setTexteMailRecommandationDossierTiers(String texteMailRecommandationDossierTiers) {
		this.texteMailRecommandationDossierTiers = texteMailRecommandationDossierTiers;
	}

	public String getTexteMailRecommandationBdgDossier() {
		return texteMailRecommandationBdgDossier;
	}

	public void setTexteMailRecommandationBdgDossier(String texteMailRecommandationBdgDossier) {
		this.texteMailRecommandationBdgDossier = texteMailRecommandationBdgDossier;
	}

	public PaiementAbonnementTiersController getPaiementAbonnementTiersController() {
		return paiementAbonnementTiersController;
	}

	public void setPaiementAbonnementTiersController(PaiementAbonnementTiersController paiementAbonnementTiersController) {
		this.paiementAbonnementTiersController = paiementAbonnementTiersController;
	}

	public InfosJuridiqueController getInfosJuridiqueController() {
		return infosJuridiqueController;
	}

	public void setInfosJuridiqueController(InfosJuridiqueController infosJuridiqueController) {
		this.infosJuridiqueController = infosJuridiqueController;
	}


	public void switchAfficheSeulementBoutique() {
        refresh();
    }
	
	public Boolean getAfficheSeulementBoutique() {
		return afficheSeulementBoutique;
	}

	public void setAfficheSeulementBoutique(Boolean afficheSeulementBoutique) {
		this.afficheSeulementBoutique = afficheSeulementBoutique;
	}
	
	public Boolean getParametrageDepuisMenuPrincipal() {
		return parametrageDepuisMenuPrincipal;
	}

	public void setParametrageDepuisMenuPrincipal(Boolean parametrageDepuisMenuPrincipal) {
		this.parametrageDepuisMenuPrincipal = parametrageDepuisMenuPrincipal;
	}

	public List<TaTiersDTO> getFilteredValues() {
		return filteredValues;
	}

	public void setFilteredValues(List<TaTiersDTO> filteredValues) {
		this.filteredValues = filteredValues;
	}
	
	public ServiceTiersController getServiceTiersController() {
		return serviceTiersController;
	}

	public void setServiceTiersController(ServiceTiersController serviceTiersController) {
		this.serviceTiersController = serviceTiersController;
	}
	
}  
