package fr.legrain.bdg.webapp.tiers;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.validator.ValidatorException;
import javax.faces.view.ViewScoped;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.io.IOUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.component.tabview.TabView;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaPreferencesServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.preferences.divers.ConstPreferencesTiers;
import fr.legrain.bdg.tiers.service.remote.ITaAdresseServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaCPaiementServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaComplServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaEmailServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaInfoJuridiqueServiceRemote;
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
import fr.legrain.bdg.webapp.app.LgrTab;
import fr.legrain.bdg.webapp.app.TabListModelBean;
import fr.legrain.bdg.webapp.app.TabViewsBean;
import fr.legrain.document.model.TaTPaiement;
import fr.legrain.dossier.dto.TaInfoEntrepriseDTO;
import fr.legrain.dossier.model.TaInfoEntreprise;
import fr.legrain.dossier.model.TaPreferences;
import fr.legrain.general.model.TaFichier;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.LibDate;
import fr.legrain.lib.data.ModeObjetEcranServeur;
import fr.legrain.tiers.dto.TaInfoJuridiqueDTO;
import fr.legrain.tiers.dto.TaTiersDTO;
import fr.legrain.tiers.model.TaCPaiement;
import fr.legrain.tiers.model.TaInfoJuridique;
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
public class InfoEntrepriseController implements Serializable {  

	@Inject @Named(value="tabListModelCenterBean")
	private TabListModelBean tabsCenter;
	
	@Inject @Named(value="tabViewsBean")
	private TabViewsBean tabViews;

	private String paramId;

	private List<TaTiersDTO> values; 
	
	protected ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();

	private @EJB ITaTiersServiceRemote taTiersService;
	private @EJB ITaTTiersServiceRemote taTTiersService;
	private @EJB ITaInfoEntrepriseServiceRemote taInfoEntrepriseService;
	private @EJB ITaInfoJuridiqueServiceRemote taInfoJuridiqueService;
	
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
	private @EJB ITaPreferencesServiceRemote taPreferencesService;
	
	@Inject private BanqueController banqueController;

	private TaTiersDTO[] selectedTaTiersDTOs; 
	private TaTiers taTiers = new TaTiers();
	private TaTiersDTO newTaTiersDTO = new TaTiersDTO();
	private TaTiersDTO selectedTaTiersDTO = new TaTiersDTO();
	private TaInfoEntrepriseDTO selectedTaInfoEntrepriseDTO = new TaInfoEntrepriseDTO();
	private TaInfoJuridiqueDTO selectedTaInfoJuridiqueDTO = new TaInfoJuridiqueDTO();
	
	private boolean transfertEnCompta=true;
	private TaTTiers taTTiers;
	private TaTCivilite taTCivilite;
	private TaTEntite taTEntite;
	private TaInfoJuridique taInfoJuridique;
	private TaInfoEntreprise taInfoEntreprise;	
	
	private StreamedContent logo;
	
	private TabView tabViewInfosEntreprise; 

	private LgrDozerMapper<TaTiersDTO,TaTiers> mapperUIToModel  = new LgrDozerMapper<TaTiersDTO,TaTiers>();
	private LgrDozerMapper<TaTiers,TaTiersDTO> mapperModelToUI  = new LgrDozerMapper<TaTiers,TaTiersDTO>();
	
	private LgrDozerMapper<TaInfoEntrepriseDTO,TaInfoEntreprise> mapperUIToModelInfosEntreprise  = new LgrDozerMapper<TaInfoEntrepriseDTO,TaInfoEntreprise>();
	private LgrDozerMapper<TaInfoEntreprise,TaInfoEntrepriseDTO> mapperModelToUIInfosEntreprise  = new LgrDozerMapper<TaInfoEntreprise,TaInfoEntrepriseDTO>();
	
	private LgrDozerMapper<TaInfoJuridiqueDTO,TaInfoJuridique> mapperUIToModelInfosJuridique  = new LgrDozerMapper<TaInfoJuridiqueDTO,TaInfoJuridique>();
	private LgrDozerMapper<TaInfoJuridique,TaInfoJuridiqueDTO> mapperModelToUIInfosJuridique  = new LgrDozerMapper<TaInfoJuridique,TaInfoJuridiqueDTO>();	

	public InfoEntrepriseController() {  
		//values = getValues();
	}  

	@PostConstruct
	public void postConstruct(){
	    
		refresh();

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

	public void refresh(){
		selectedTaInfoJuridiqueDTO=new TaInfoJuridiqueDTO();

		taInfoEntreprise = taInfoEntrepriseService.findInstance();
		if(taInfoEntreprise!=null){
			taTiers=taInfoEntreprise.getTaTiers();
			if(taTiers==null){
				try {
					taTiers=taTiersService.findByCode("ZPO");
				} catch (FinderException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(taTiers!=null){
				mapperModelToUI.map(taTiers, selectedTaTiersDTO);
				mapperModelToUIInfosEntreprise.map(taInfoEntreprise, selectedTaInfoEntrepriseDTO);
				//values = taTiersService.selectAllDTO();
				taInfoJuridique = taTiers.getTaInfoJuridique();
				if(taInfoJuridique!=null) {
					mapperModelToUIInfosJuridique.map(taInfoJuridique, selectedTaInfoJuridiqueDTO);
				}
			}
		}
		autoCompleteMapDTOtoUI();
		transfertEnCompta= taPreferencesService.retourPreferenceBoolean("exportation", "export_compta");

		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
		
		refresLogo();
	}
	
	public void refresLogo() {
		try {
			logo = null;
			if(taTiers!=null && taTiers.getBlobLogo()!=null) {
				InputStream stream = new ByteArrayInputStream(taTiers.getBlobLogo()); 
				//logo = new DefaultStreamedContent(stream, "image/png");
				logo = new DefaultStreamedContent(stream);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void supprimerLogo(ActionEvent e) {
		taTiers.setBlobLogo(null);
		actEnregistrer(null);
		refresLogo();
	}

	public List<TaTiersDTO> getValues(){  
		return values;
	}

	public void actAnnuler(ActionEvent actionEvent) {
		
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
		
		if(ConstWeb.DEBUG) {
		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage("Tiers", "actAnnuler"));
		}
	}
	
	public void autoCompleteMapUIToDTO() {
		if(taTTiers!=null) {
			validateUIField(Const.C_CODE_T_TIERS,taTTiers.getCodeTTiers());
			selectedTaTiersDTO.setCodeTTiers(taTTiers.getCodeTTiers());
		}
		if(taTCivilite!=null) {
			validateUIField(Const.C_CODE_T_CIVILITE,taTCivilite.getCodeTCivilite());
			selectedTaTiersDTO.setCodeTCivilite(taTCivilite.getCodeTCivilite());
		}
		if(taTEntite!=null) {
			validateUIField(Const.C_CODE_T_ENTITE,taTEntite.getCodeTEntite());
			selectedTaTiersDTO.setCodeTEntite(taTEntite.getCodeTEntite());
		}
		
		if(taInfoEntreprise!=null) {

		}
		
//		if(taInfoJuridique!=null) {
//			validateUIField(Const.C_RCS_INFO_JURIDIQUE,taInfoJuridique.getRcsInfoJuridique());
//			selectedTaInfoJuridiqueDTO.setRcsInfoJuridique(taInfoJuridique.getRcsInfoJuridique());
//			validateUIField(Const.C_CAPITAL_INFO_JURIDIQUE,taInfoJuridique.getCapitalInfoJuridique());
//			selectedTaInfoJuridiqueDTO.setCapitalInfoJuridique(taInfoJuridique.getCapitalInfoJuridique());
//			validateUIField(Const.C_APE_INFO_JURIDIQUE,taInfoJuridique.getApeInfoJuridique());
//			selectedTaInfoJuridiqueDTO.setApeInfoJuridique(taInfoJuridique.getApeInfoJuridique());
//			validateUIField(Const.C_SIRET_INFO_JURIDIQUE,taInfoJuridique.getSiretInfoJuridique());
//			selectedTaInfoJuridiqueDTO.setSiretInfoJuridique(taInfoJuridique.getSiretInfoJuridique());
//		}		
	}
	
	public void autoCompleteMapDTOtoUI() {
		try {
			taTTiers = null;
			taTCivilite = null;
			taTEntite = null;
			if(selectedTaTiersDTO.getCodeTTiers()!=null && !selectedTaTiersDTO.getCodeTTiers().equals("")) {
				taTTiers = taTTiersService.findByCode(selectedTaTiersDTO.getCodeTTiers());
			}
			if(selectedTaTiersDTO.getCodeTCivilite()!=null && !selectedTaTiersDTO.getCodeTCivilite().equals("")) {
				taTCivilite = taTCivlicteService.findByCode(selectedTaTiersDTO.getCodeTCivilite());
			}
			if(selectedTaTiersDTO.getCodeTEntite()!=null && !selectedTaTiersDTO.getCodeTEntite().equals("")) {
				taTEntite = taTEntiteService.findByCode(selectedTaTiersDTO.getCodeTEntite());
			}
		} catch (FinderException e) {
			e.printStackTrace();
		}
	}

	public void actEnregistrer(ActionEvent actionEvent) {	
		if(taTiers == null) {
			taTiers = new TaTiers();
		}
		if(taInfoJuridique == null) {
			taInfoJuridique = new TaInfoJuridique();
		}
		
		TaInfoEntreprise taInfoEntreprise = taInfoEntrepriseService.findInstance();
		
		taTiers.initAdresseTiers(null,adresseEstRempli(),ConstPreferencesTiers.CORRESPONDANCE_ADRESSE_ADMINISTRATIF_DEFAUT,
				ConstPreferencesTiers.CORRESPONDANCE_ADRESSE_COMMERCIAL_DEFAUT);

		if(taTiers.getTaAdresse()!=null){
			if(!taTiers.getTaAdresses().contains(taTiers.getTaAdresse())) {
				taTiers.getTaAdresses().add(taTiers.getTaAdresse());
			}
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
		mapperUIToModelInfosJuridique.map(selectedTaInfoJuridiqueDTO, taInfoJuridique);
		mapperUIToModelInfosEntreprise.map(selectedTaInfoEntrepriseDTO, taInfoEntreprise);
		taTiers.setTaInfoJuridique(taInfoJuridique);
		try {
//			taTiers.setTaAdresse(null);
			taTiers = taTiersService.merge(taTiers,ITaTiersServiceRemote.validationContext);		
			if(transfertEnCompta!=taPreferencesService.retourPreferenceBoolean("exportation", "export_compta"))
			taPreferencesService.mergePreferenceBoolean("exportation", "export_compta", transfertEnCompta);
			
			
			if(taInfoEntreprise.getCodexoInfoEntreprise()==null) {
				/*
				 * nouveau dossier, on initialise les date avec le même traitement 
				 * que dans actinserer de SWTPaInfoEntrepriseSimpleController
				 */	
				Date dateDeb = new Date();
				Date dateFin = new Date(); 
				dateFin=LibDate.incrementDate(dateFin, -1, 0, 1);
				
				/*    ****************   ATTENTION   **********************      */
				taInfoEntreprise.setIdInfoEntreprise(1);//on recupère l'info entreprise 1, il ne faut pas 
				//laisser l'id s'incrémenter autrement sinon multiplie les infos entreprise
				

				taInfoEntreprise.setDatedebInfoEntreprise(dateDeb);
				taInfoEntreprise.setDatefinInfoEntreprise(dateFin);
				
				/*
				 * Pour le code exo,
				 * on prend les 2 derniers chiffre de l'année courante
				 */
				String dateStr = LibDate.dateToString(new Date());
				taInfoEntreprise.setCodexoInfoEntreprise(dateStr.substring(dateStr.length()-2));
			}
			
			taInfoEntreprise.setNomInfoEntreprise(selectedTaTiersDTO.getNomEntreprise());
			taInfoEntreprise.setTvaIntraInfoEntreprise(selectedTaTiersDTO.getTvaIComCompl());
			taInfoEntreprise.setRcsInfoEntreprise(selectedTaInfoJuridiqueDTO.getRcsInfoJuridique());
			taInfoEntreprise.setApeInfoEntreprise(selectedTaInfoJuridiqueDTO.getApeInfoJuridique());
			taInfoEntreprise.setCapitalInfoEntreprise(selectedTaInfoJuridiqueDTO.getCapitalInfoJuridique());
			taInfoEntreprise.setSiretInfoEntreprise(selectedTaInfoJuridiqueDTO.getSiretInfoJuridique());

			
			if(taTiers.getTaEntreprise()!=null) {
				taInfoEntreprise.setNomInfoEntreprise(taTiers.getTaEntreprise().getNomEntreprise());
			}else{
				taInfoEntreprise.setNomInfoEntreprise("");
			}
			if(taTiers.getTaAdresse()!=null) {
				taInfoEntreprise.setAdresse1InfoEntreprise(taTiers.getTaAdresse().getAdresse1Adresse());
				taInfoEntreprise.setAdresse2InfoEntreprise(taTiers.getTaAdresse().getAdresse2Adresse());
				taInfoEntreprise.setAdresse3InfoEntreprise(taTiers.getTaAdresse().getAdresse3Adresse());
				taInfoEntreprise.setCodepostalInfoEntreprise(taTiers.getTaAdresse().getCodepostalAdresse());
				taInfoEntreprise.setVilleInfoEntreprise(taTiers.getTaAdresse().getVilleAdresse());
				taInfoEntreprise.setPaysInfoEntreprise(taTiers.getTaAdresse().getPaysAdresse());
			}else{
				taInfoEntreprise.setAdresse1InfoEntreprise("");
				taInfoEntreprise.setAdresse2InfoEntreprise("");
				taInfoEntreprise.setAdresse3InfoEntreprise("");
				taInfoEntreprise.setCodepostalInfoEntreprise("");
				taInfoEntreprise.setVilleInfoEntreprise("");
				taInfoEntreprise.setPaysInfoEntreprise("");
			}
			if(taTiers.getTaTelephone()!=null) {
				taInfoEntreprise.setTelInfoEntreprise(taTiers.getTaTelephone().getNumeroTelephone());
			}else{
				taInfoEntreprise.setTelInfoEntreprise("");
			}
			TaTTel typeFax=taTTelService.findByCode("FAX");
			taInfoEntreprise.setFaxInfoEntreprise("");
			if(typeFax!=null) {
				TaTelephone telFax=taTiers.aDesTelephoneDuType(typeFax.getCodeTTel());
				if(telFax!=null) {
					taInfoEntreprise.setFaxInfoEntreprise(telFax.getNumeroTelephone());
				}
			}
			if(taTiers.getTaInfoJuridique()!=null) {
				taInfoEntreprise.setSiretInfoEntreprise(taTiers.getTaInfoJuridique().getSiretInfoJuridique());
				taInfoEntreprise.setApeInfoEntreprise(taTiers.getTaInfoJuridique().getApeInfoJuridique());
				taInfoEntreprise.setRcsInfoEntreprise(taTiers.getTaInfoJuridique().getRcsInfoJuridique());
				taInfoEntreprise.setCapitalInfoEntreprise(taTiers.getTaInfoJuridique().getCapitalInfoJuridique());
			}else{
				taInfoEntreprise.setSiretInfoEntreprise("");
				taInfoEntreprise.setApeInfoEntreprise("");
				taInfoEntreprise.setRcsInfoEntreprise("");
				taInfoEntreprise.setCapitalInfoEntreprise("");
			}
			if(taTiers.getTaCompl()!=null) {
				taInfoEntreprise.setTvaIntraInfoEntreprise(taTiers.getTaCompl().getTvaIComCompl());
			}else{
				taInfoEntreprise.setTvaIntraInfoEntreprise("");
			}
			if(taTiers.getTaEmail()!=null) {
				taInfoEntreprise.setEmailInfoEntreprise(taTiers.getTaEmail().getAdresseEmail());
			}else{
				taInfoEntreprise.setEmailInfoEntreprise("");
			}
			if(taTiers.getTaWeb()!=null) {
				taInfoEntreprise.setWebInfoEntreprise(taTiers.getTaWeb().getAdresseWeb());
			}else{
				taInfoEntreprise.setWebInfoEntreprise("");
			}
			
			//Ajouter des valeurs par defaut dans infos entreprise pour les champs obligatoire non remplis ?
			if(taInfoEntreprise.getVilleInfoEntreprise()==null
					|| taInfoEntreprise.getPaysInfoEntreprise()==null
					|| taInfoEntreprise.getCodexoInfoEntreprise()==null) {
				
			}
			
//			taInfoEntreprise.setNomInfoEntreprise(selectedTaTiersDTO.getNomEntreprise());
//			taInfoEntreprise.setTvaIntraInfoEntreprise(selectedTaTiersDTO.getTvaIComCompl());
//			taInfoEntreprise.setRcsInfoEntreprise(selectedTaInfoJuridiqueDTO.getRcsInfoJuridique());
//			taInfoEntreprise.setApeInfoEntreprise(selectedTaInfoJuridiqueDTO.getApeInfoJuridique());
//			taInfoEntreprise.setCapitalInfoEntreprise(selectedTaInfoJuridiqueDTO.getCapitalInfoJuridique());
//			taInfoEntreprise.setSiretInfoEntreprise(selectedTaInfoJuridiqueDTO.getSiretInfoJuridique());
			
			taInfoEntreprise.setTaTiers(taTiers);
			
			taInfoEntreprise = taInfoEntrepriseService.enregistrerMerge(taInfoEntreprise);
			refresh();
			
//			mapperModelToUI.map(taTiers, selectedTaTiersDTO);
//			autoCompleteMapDTOtoUI();
			
//			if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_INSERTION)==0) {
//				values.add(selectedTaTiersDTO);
//			}
			
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
			selectedTaTiersDTO = new TaTiersDTO();
			taTiers = taTiersService.findById(-1);
//			taTiers = new TaTiers();

			selectedTaInfoJuridiqueDTO = new TaInfoJuridiqueDTO();
			taInfoJuridique = new TaInfoJuridique();
			
			String codeTiersDefaut = "C";

			selectedTaTiersDTO.setCodeTiers(taTiersService.genereCode(null)); //ejb
			validateUIField(Const.C_CODE_TIERS,selectedTaTiersDTO.getCodeTiers());//permet de verrouiller le code genere

			selectedTaTiersDTO.setCodeTTiers(taTiersService.C_ID_IDENTITE_ENTREPRISE_STR);

			selectedTaTiersDTO.setCodeCompta(taTiersService.C_CODE_IDENTITE_ENTREPRISE_STR); //ejb
			validateUIField(Const.C_CODE_COMPTA,selectedTaTiersDTO.getCodeCompta()); //ejb

			TaTTiers taTTiers;

			taTTiers = taTTiersService.findByCode(taTiersService.C_CODE_IDENTITE_ENTREPRISE_STR);

			if(taTTiers!=null && taTTiers.getCompteTTiers()!=null) {
				selectedTaTiersDTO.setCompte(taTTiers.getCompteTTiers());
				this.taTTiers = taTTiers;
			} else {
				selectedTaTiersDTO.setCompte(taTiersService.C_COMPTE_IDENTITE_ENTREPRISE_STR); //ejb
			}
			
			
			validateUIField(Const.C_COMPTE,selectedTaTiersDTO.getCompte()); //ejb

			selectedTaTiersDTO.setActifTiers(true);
			selectedTaTiersDTO.setCodeTTvaDoc("F");

			taTiers.setTaTTiers(taTTiersService.findById(taTiersService.C_ID_IDENTITE_ENTREPRISE_INT));
			taTiers.setIdTiers(taTiersService.C_ID_IDENTITE_ENTREPRISE_INT);

			modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);

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
	
    public void nouveau(ActionEvent actionEvent) {  
    	LgrTab b = new LgrTab();
    	b.setTypeOnglet(LgrTab.TYPE_TAB_TIERS);
		b.setTitre("Nouveau");
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
    public void detail(ActionEvent actionEvent) {
    	onRowSelect(null);
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
			}

			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

			if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Tiers", "actSupprimer"));
			}
		} catch (RemoveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	    
	}

	public void actFermer(ActionEvent actionEvent) {
		
		LgrTab typeOngletDejaOuvert = null;
		for (LgrTab o : tabsCenter.getOnglets()) {
			if(LgrTab.TYPE_TAB_TIERS.equals(o.getTypeOnglet())) {
				typeOngletDejaOuvert = o;
			}
		}
		
		if(typeOngletDejaOuvert!=null) {
//			if(!values.isEmpty()) {
//				selectedTaTiersDTO = values.get(0);
//			}
			tabsCenter.supprimerOnglet(typeOngletDejaOuvert);
		}
		if(ConstWeb.DEBUG) {
		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage("Tiers", "actFermer"));
		}
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
		
//			session.setAttribute("tiers", taTiersService.findById(selectedTaTiersDTO.getId()));
			System.out.println("TiersController.actImprimer()");
		} catch (FinderException e) {
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
		context.addMessage(null, new FacesMessage("Tiers", 
				"Chargement du tiers N°"+selectedTaTiersDTO.getCodeTiers()
				)); }
		} else {
			tabViews.selectionneOngletCentre(LgrTab.TYPE_TAB_TIERS);
		}
	} 
	
	public void onRowSelect(SelectEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_TIERS);
		b.setTitre("Info Entreprise "+selectedTaTiersDTO.getCodeTiers());
		b.setTemplate("tiers/tiers.xhtml");
		b.setStyle(LgrTab.CSS_CLASS_TAB_TIERS);
		b.setIdTab(LgrTab.ID_TAB_TIERS);
		b.setParamId(LibConversion.integerToString(selectedTaTiersDTO.getId()));

		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
		
		autoCompleteMapDTOtoUI();
		if(ConstWeb.DEBUG) {
		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage("Tiers", 
				"Chargement du tiers N°"+selectedTaTiersDTO.getCodeTiers()
				)); }
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
		}
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
			case "supprimer":
			case "modifier":
			case "imprimer":
			case "fermer":
				retour = false;
				break;
			case "inserer":
				retour = taInfoEntreprise!=null?true:false;
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
		  String nomChamp =  (String) component.getAttributes().get("nomChamp");
		  		  
//			//validation automatique via la JSR bean validation
		  ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		  Set<ConstraintViolation<TaTiersDTO>> violations = factory.getValidator().validateValue(TaTiersDTO.class,nomChamp,value);
			if (violations.size() > 0) {
				messageComplet = "Erreur de validation : ";
				for (ConstraintViolation<TaTiersDTO> cv : violations) {
					messageComplet+=" "+cv.getMessage()+"\n";
				}
				throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, messageComplet, messageComplet));
			} else {
					 validateUIField(nomChamp,value);
			}


		} catch(Exception e) {
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, messageComplet, messageComplet));
		}
	}	
//	public void validateTiers(FacesContext context, UIComponent component, Object value) throws ValidatorException {
//		String msg = "";
//		try {
//		  //String selectedRadio = (String) value;
//		  String nomChamp =  (String) component.getAttributes().get("nomChamp");
//		  //msg = "Mon message d'erreur pour : "+nomChamp;
//		  validateUIField(nomChamp,value);
////		  taTiersService.validateDTOProperty(selectedTaTiersDTO, nomChamp, ITaTiersServiceRemote.validationContext );
//		  //selectedTaTiersDTO.setAdresse1Adresse("abcd");
////		  if(selectedRadio.equals("aa")) {
////			  throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
////		  }
//		} catch(Exception e) {
//			msg += e.getMessage();
//			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
//		}
//	}
	
	public void validateInfoEntreprise(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		String msg = "";
		try {
		  //String selectedRadio = (String) value;
		  String nomChamp =  (String) component.getAttributes().get("nomChamp");
		  //msg = "Mon message d'erreur pour : "+nomChamp;
		  validateUIField(nomChamp,value);
//		  taInfoEntrepriseService.validateDTOProperty(selectedTaTiersDTO, nomChamp, ITaInfoEntrepriseServiceRemote.validationContext );


		} catch(Exception e) {
			msg += e.getMessage();
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
		}
	}
	
	public void validateJuridique(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		
		String msg = "";
		
		try {
		  //String selectedRadio = (String) value;
		 
		  String nomChamp =  (String) component.getAttributes().get("nomChamp");
		  
		  //msg = "Mon message d'erreur pour : "+nomChamp;
		  
		  validateUIField(nomChamp,value);
		  taInfoJuridiqueService.validateDTOProperty(selectedTaInfoJuridiqueDTO, nomChamp, ITaInfoJuridiqueServiceRemote.validationContext );
		  
		  //selectedTaTiersDTO.setAdresse1Adresse("abcd");
		  
//		  if(selectedRadio.equals("aa")) {
//			  throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
//		  }

		} catch(Exception e) {
			msg += e.getMessage();
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
		}
	}
	
	public void autcompleteSelection(SelectEvent event) {
		Object value = event.getObject();
//		FacesMessage msg = new FacesMessage("Selected", "Item:" + value);
		
		String nomChamp =  (String) event.getComponent().getAttributes().get("nomChamp");

		if(value!=null) {
		if(value instanceof String && value.equals(Const.C_AUCUN))value="";	
		if(value instanceof TaTCivilite && ((TaTCivilite) value).getCodeTCivilite()!=null && ((TaTCivilite) value).getCodeTCivilite().equals(Const.C_AUCUN))value=null;	
		if(value instanceof TaTTiers && ((TaTTiers) value).getCodeTTiers()!=null && ((TaTTiers) value).getCodeTTiers().equals(Const.C_AUCUN))value=null;	
		if(value instanceof TaTEntite && ((TaTEntite) value).getCodeTEntite()!=null && ((TaTEntite) value).getCodeTEntite().equals(Const.C_AUCUN))value=null;	
		}
		
		validateUIField(nomChamp,value);
	}
	
public boolean validateUIField(String nomChamp,Object value) {
		
		boolean changement=false;
		
		try {				
				if(nomChamp.equals(Const.C_CODE_T_TIERS)) {
						TaTTiers entity = new TaTTiers();
						if(value!=null){
							if(value instanceof TaTTiers){
							entity=(TaTTiers) value;
							}else{
								entity = taTTiersService.findByCode((String)value);
							}
						}						
						
						taTiers.setTaTTiers(entity);
					
				}
				else if(nomChamp.equals(Const.C_CODE_T_CIVILITE)) {

						TaTCivilite entity = new TaTCivilite();
						if(value!=null){
							if(value instanceof TaTCivilite){
							entity=(TaTCivilite) value;
							}else{
								entity = taTCivlicteService.findByCode((String)value);
							}
						}else{
							
						}
						
						taTiers.setTaTCivilite(entity);

				}else if(nomChamp.equals(Const.C_CODE_T_ENTITE)) {

					TaTEntite entity = new TaTEntite();
					if(value!=null){
						if(value instanceof TaTEntite){
						entity=(TaTEntite) value;
						}else{
							entity = taTEntiteService.findByCode((String)value);
						}
					}					
					
					taTiers.setTaTEntite(entity);
		
			}else if(nomChamp.equals(Const.C_RCS_INFO_JURIDIQUE)||nomChamp.equals(Const.C_CAPITAL_INFO_JURIDIQUE)||
					nomChamp.equals(Const.C_APE_INFO_JURIDIQUE)||nomChamp.equals(Const.C_SIRET_INFO_JURIDIQUE)) {

//				TaInfoJuridique entity = new TaInfoJuridique();
//				entity = taInfoJuridiqueService.findByCode((String)value);
//				taTiers.setTaInfoJuridique(entity);
	
		}
				else if(nomChamp.equals(Const.C_CODE_T_TVA_DOC)) {

						TaTTvaDoc entity = new TaTTvaDoc();
						if(value!=null){
							if(value instanceof TaTTvaDoc){
							entity=(TaTTvaDoc) value;
							}else{
								entity = taTTvaDocService.findByCode((String)value);
							}
						}						
						
						taTiers.setTaTTvaDoc(entity);

				}
				else if(nomChamp.equals(Const.C_TVA_I_COM_COMPL)|| nomChamp.equals(Const.C_ACCISE)
						|| nomChamp.equals(Const.C_SIRET_COMPL) || nomChamp.equals(Const.C_NUM_AGREMENT_SANITAIRE)) {

					taTiers.initComplTiers(value);
						
						if(value!=null && taTiers.getTaCompl()!=null) {
						PropertyUtils.setSimpleProperty(taTiers.getTaCompl(), nomChamp, value);
						PropertyUtils.setSimpleProperty(selectedTaTiersDTO, nomChamp, value);
					
				}
				}else if(nomChamp.equals(Const.C_CODE_T_ENTITE)) {

						TaTEntite entity = new TaTEntite();
						if(value!=null){
							if(value instanceof TaTEntite){
							entity=(TaTEntite) value;
							}else{
								entity = taTEntiteService.findByCode((String)value);
							}
						}						
						
						taTiers.setTaTEntite(entity);
			
				}else if(nomChamp.equals(Const.C_CODE_C_PAIEMENT)) {

						TaCPaiement entity = new TaCPaiement();
						if(value!=null){
							if(value instanceof TaCPaiement){
							entity=(TaCPaiement) value;
							}else{
								entity = taCPaiementService.findByCode((String)value);
							}
						}						
						
						taTiers.setTaCPaiement(entity);
					
				}else if(nomChamp.equals(Const.C_CODE_T_PAIEMENT)) {
						TaTPaiement entity = new TaTPaiement();
						if(value!=null){
							if(value instanceof TaTPaiement){
							entity=(TaTPaiement) value;
							}else{
								entity = taTPaiementService.findByCode((String)value);
							}
						}						
						
						taTiers.setTaTPaiement(entity);
					
				}else if(nomChamp.equals(Const.C_CODE_T_TARIF)) {

						TaTTarif entity = new TaTTarif();
						if(value!=null){
							if(value instanceof TaTTarif){
							entity=(TaTTarif) value;
							}else{
								entity = taTTarifService.findByCode((String)value);
							}
						}						
						
						taTiers.setTaTTarif(entity);
					
				}else if(nomChamp.equals(Const.C_NUMERO_TELEPHONE)) {
					taTiers.initTelephoneTiers(value,ConstPreferencesTiers.CORRESPONDANCE_TELEPHONE_ADMINISTRATIF_DEFAUT,
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
	
	private boolean adresseEstRempli() {
		boolean retour=false;
		if(!selectedTaTiersDTO.getAdresse1Adresse().equals(""))return true;
		if(!selectedTaTiersDTO.getAdresse2Adresse().equals(""))return true;
		if(!selectedTaTiersDTO.getAdresse3Adresse().equals(""))return true;
		if(!selectedTaTiersDTO.getCodepostalAdresse().equals(""))return true;
		if(!selectedTaTiersDTO.getVilleAdresse().equals(""))return true;
		if(!selectedTaTiersDTO.getPaysAdresse().equals(""))return true;
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
	
	public List<TaTEntite> typeEntiteAutoComplete(String query) {
        List<TaTEntite> allValues = taTEntiteService.selectAll();
        List<TaTEntite> filteredValues = new ArrayList<TaTEntite>();
        TaTEntite  civ = new TaTEntite();
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
	
	public void handleFileUpload(FileUploadEvent event) {
	        FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
	        FacesContext.getCurrentInstance().addMessage(null, message);
	        
	        try {
	        	
	        	taTiers.setBlobLogo(IOUtils.toByteArray(event.getFile().getInputStream()));
	        	
//				fichier.setBlobFichierMiniature(fichier.resizeImage(IOUtils.toByteArray(event.getFile().getInputstream()), event.getFile().getFileName(), 640, 480));
	        	int hMax = 80;
	        	int lMax = 350;
	        	Image image = ImageIO.read(event.getFile().getInputStream());
	        	BufferedImage bi = TaFichier.resizeImageMax(image,lMax,hMax);
	        	
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
	        	
	        	taTiers.setBlobLogo(bytes);
	        	
	        	actEnregistrer(null);
	    		refresLogo();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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

	public TaInfoJuridiqueDTO getSelectedTaInfoJuridiqueDTO() {
		return selectedTaInfoJuridiqueDTO;
	}

	public void setSelectedTaInfoJuridiqueDTO(
			TaInfoJuridiqueDTO selectedTaInfoJuridiqueDTO) {
		this.selectedTaInfoJuridiqueDTO = selectedTaInfoJuridiqueDTO;
	}

	public TaInfoEntreprise getTaInfoEntreprise() {
		return taInfoEntreprise;
	}

	public void setTaInfoEntreprise(TaInfoEntreprise taInfoEntreprise) {
		this.taInfoEntreprise = taInfoEntreprise;
	}

	public TaInfoEntrepriseDTO getSelectedTaInfoEntrepriseDTO() {
		return selectedTaInfoEntrepriseDTO;
	}

	public void setSelectedTaInfoEntrepriseDTO(
			TaInfoEntrepriseDTO selectedTaInfoEntrepriseDTO) {
		this.selectedTaInfoEntrepriseDTO = selectedTaInfoEntrepriseDTO;
	}

	public TabViewsBean getTabViews() {
		return tabViews;
	}

	public void setTabViews(TabViewsBean tabViews) {
		this.tabViews = tabViews;
	}

	public StreamedContent getLogo() {
		return logo;
	}

	public void setLogo(StreamedContent logo) {
		this.logo = logo;
	}

	public void onTabChange(TabChangeEvent event) {
		updateTab(); 
	}
	
	public boolean disabledTab(String nomTab) {
		return modeEcran.dataSetEnModif() ||  selectedTaTiersDTO.getId()==0;
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
			
			
			banqueController.setMasterEntity(taTiers);
			banqueController.refresh();
			
			autoCompleteMapDTOtoUI();


			
		} catch (FinderException e) {
			e.printStackTrace();
		}
	}

	public BanqueController getBanqueController() {
		return banqueController;
	}

	public void setBanqueController(BanqueController banqueController) {
		this.banqueController = banqueController;
	}

	public TabView getTabViewInfosEntreprise() {
		return tabViewInfosEntreprise;
	}

	public void setTabViewInfosEntreprise(TabView tabViewInfosEntreprise) {
		this.tabViewInfosEntreprise = tabViewInfosEntreprise;
	}

	public boolean isTransfertEnCompta() {
		return transfertEnCompta;
	}

	public void setTransfertEnCompta(boolean transfertEnCompta) {
		this.transfertEnCompta = transfertEnCompta;
	}
}  
