package fr.legrain.bdg.webapp.droits;

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
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

import fr.legrain.autorisation.xml.ListeModules;
import fr.legrain.autorisation.xml.Module;
import fr.legrain.bdg.dossier.service.remote.ITaAutorisationsDossierServiceRemote;
import fr.legrain.bdg.droits.service.remote.ITaRoleServiceRemote;
import fr.legrain.bdg.droits.service.remote.ITaUtilisateurWebServiceServiceRemote;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.login.service.remote.ITaUtilisateurLoginServiceRemote;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.model.mapping.mapper.TaUtilisateurLoginMapper;
import fr.legrain.bdg.webapp.ConstWeb;
import fr.legrain.dossier.model.TaAutorisationsDossier;
import fr.legrain.droits.dto.TaRRoleUtilisateurDTO;
import fr.legrain.droits.dto.TaRoleDTO;
import fr.legrain.droits.dto.TaUtilisateurWebServiceDTO;
import fr.legrain.droits.model.TaRRoleUtilisateur;
import fr.legrain.droits.model.TaRole;
import fr.legrain.droits.model.TaUtilisateurWebService;
import fr.legrain.droits.service.TenantInfo;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.ModeObjetEcranServeur;
import fr.legrain.login.model.TaUtilisateurLogin;

@Named
@ViewScoped  
public class UtilisateurWebServiceController implements Serializable {  
	
	private List<TaUtilisateurWebServiceDTO> values; 
	private List<TaUtilisateurWebServiceDTO> filteredValues; 
	private TaUtilisateurWebServiceDTO nouveau;
	private TaUtilisateurWebServiceDTO selection;
	private TaAutorisationsDossier autorisation;
	
	private TaUtilisateurWebService taUtilisateurWebService = new TaUtilisateurWebService();

	private String modeEcranDefaut;
	private static final String C_DIALOG = "dialog";
	
	@Inject private	TenantInfo tenantInfo;
	
	private List<TaRoleDTO> listeRoles; 
	private List<TaRoleDTO> listeChoixRoles;
	private List<TaRoleDTO> listeChoixRolesAncien; 
	
	private List<Module> listeModuleDispo; 
	private List<Module> listeChoixModule; 
	
	private String pwd;
	private String pwd2;
	
	private ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();

	@EJB private ITaUtilisateurWebServiceServiceRemote taUtilisateurWebServiceService;
	@EJB private ITaRoleServiceRemote taRoleService;
	@EJB private ITaAutorisationsDossierServiceRemote autorisationsDossierService;
	@EJB private ITaUtilisateurLoginServiceRemote utilisateurLoginService;
	
	private LgrDozerMapper<TaUtilisateurWebServiceDTO,TaUtilisateurWebService> mapperUIToModel  = new LgrDozerMapper<TaUtilisateurWebServiceDTO,TaUtilisateurWebService>();
	private LgrDozerMapper<TaUtilisateurWebService,TaUtilisateurWebServiceDTO> mapperModelToUI  = new LgrDozerMapper<TaUtilisateurWebService,TaUtilisateurWebServiceDTO>();

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
	
	public void refresh(){
		values = taUtilisateurWebServiceService.selectAllDTO();
		listeRoles = taRoleService.selectAllDTO();
		
		autorisation = autorisationsDossierService.findInstance();
		
		listeModuleDispo = new ArrayList<Module>();
		ListeModules listeXml = new ListeModules();
		listeXml = listeXml.recupModulesXml(autorisation.getFichier());
		for (Module m : listeXml.module) {
			listeModuleDispo.add(m);
		}
		
		Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		modeEcranDefaut = params.get("modeEcranDefaut");
		if(modeEcranDefaut!=null && modeEcranDefaut.equals(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION))) {
			modeEcranDefaut = C_DIALOG;
			actInserer(null);
		} else if(modeEcranDefaut!=null && modeEcranDefaut.equals(modeEcran.modeString(EnumModeObjet.C_MO_EDITION))) {
			modeEcranDefaut = C_DIALOG;
			if(params.get("idEntity")!=null) {
				try {
					selection = taUtilisateurWebServiceService.findByIdDTO(LibConversion.stringToInteger(params.get("idEntity")));
					taUtilisateurWebService = taUtilisateurWebServiceService.findById(selection.getId());
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

	public UtilisateurWebServiceController() {  
	}  
	
	public void autoCompleteMapUIToDTO() {
		try {
			//Roles
			TaRoleDTO roleDTO;
			TaRole role;
//			for (TaRoleDTO roleChoisi : listeChoixRoles) {
//				if(nouveau.getRoles()==null) {
//					nouveau.setRoles(new ArrayList<TaRRoleUtilisateurDTO>());
//				}
//				if(!TaUtilisateurWebService.hasRole(roleChoisi.getRole())){ //ajout des nouveau roles
//					roleDTO = taRoleService.findByCodeDTO(roleChoisi.getRole());
//					TaRRoleUtilisateurDTO ruDTO= new TaRRoleUtilisateurDTO(nouveau,roleDTO);
//					nouveau.getRoles().add(ruDTO);
//					
//					role = taRoleService.findByCode(roleChoisi.getRole());
//					TaRRoleUtilisateur ru = new TaRRoleUtilisateur(TaUtilisateurWebService,role);
//					TaUtilisateurWebService.getRoles().add(ru);
//				}
//			}
//			List<TaRoleDTO> l = new ArrayList<>();
//			List<TaRRoleUtilisateur> lr = new ArrayList<>();
//			if(listeChoixRolesAncien!=null) {
//				for (TaRoleDTO r : listeChoixRolesAncien) { //recherche des roles à supprimer
//					if(!listeChoixRoles.contains(r)) {
//						l.add(r);
//					}
//				}
//				for(TaRoleDTO a : l) { //supression des roles
//					nouveau.getRoles().remove(a);
//					for (TaRRoleUtilisateur rr : TaUtilisateurWebService.getRoles()) {
//						if(a.getRole().equals(rr.getTaRole().getRole())) {
//							lr.add(rr);
//							rr.setTaUtilisateurWebService(null);
//							rr.setTaRole(null);
//						}
//					}
//				}
//				TaUtilisateurWebService.getRoles().removeAll(lr);
//			}
			
			//Modules
			ListeModules listeXml = new ListeModules();
			for (Module r : listeChoixModule) {
				//Module m = new Module(r);
				listeXml.module.add(r);
			}
			nouveau.setAutorisations(listeXml.creeXmlModuleString(listeXml));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void autoCompleteMapDTOtoUI() {
		try {
			//Roles
//			listeChoixRoles = new ArrayList<TaRoleDTO>();
//			nouveau.setRoles(new ArrayList<>());
//			for(TaRRoleUtilisateur rr: TaUtilisateurWebService.getRoles()) {
//				listeChoixRoles.add(taRoleService.findByCodeDTO(rr.getTaRole().getRole()));
//			}

			
			//Modules
			listeChoixModule = new ArrayList<Module>();
			if(nouveau.getAutorisations()!=null) {
				ListeModules listeXml = new ListeModules();
				listeXml = listeXml.recupModulesXml(nouveau.getAutorisations());
				for (Module m : listeXml.module) {
					listeChoixModule.add(m);
//					listeModuleDispo.add(m);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void actAnnuler(ActionEvent actionEvent){
		//values= taTAdrService.selectAll();
		
		if(values.size()>= 1){
			selection = values.get(0);
		}		
		nouveau = new TaUtilisateurWebServiceDTO();
		
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}
	
	public void actSynchrosationUtilisateurDossierEtWebService(ActionEvent actionEvent){
		taUtilisateurWebServiceService.synchroniseCompteUtilisateurDossierEtWebService();
		refresh();
	}
	
	public void actFermerDialog(ActionEvent actionEvent) {
		PrimeFaces.current().dialog().closeDynamic(null);
	}
	
	public void actEnregistrer(ActionEvent actionEvent){

		try {

			autoCompleteMapUIToDTO();
			if(pwd!=null && !pwd.equals("")) {
				nouveau.setPasswd(taUtilisateurWebService.passwordHashSHA256_Base64(pwd));
			}
			
			TaUtilisateurLoginMapper mapper =  new TaUtilisateurLoginMapper();

			if(taUtilisateurWebServiceService.findByCode(nouveau.getLogin()) == null){
				mapperUIToModel.map(nouveau, taUtilisateurWebService);
				
//				if(!listeChoixRoles.isEmpty()) {
//					//TODO tant qu'il n'y a pas de gestion des roles et qu'il n'y a que "admin" dans cette liste (vide -ou- admin), à changer
//					taUtilisateurWebService.setAdminDossier(true);
//				} else {
//					taUtilisateurWebService.setAdminDossier(false);
//				}
				taUtilisateurWebService.setSysteme(false);
				
				taUtilisateurWebService = taUtilisateurWebServiceService.merge(taUtilisateurWebService, ITaUtilisateurWebServiceServiceRemote.validationContext);
				mapperModelToUI.map(taUtilisateurWebService, nouveau);
				
//				String username_logindb = nouveau.getLogin()+"_"+tenantInfo.getTenantId();
//				TaUtilisateurLogin TaUtilisateurWebServiceLogin = new TaUtilisateurLogin();
//				TaUtilisateurWebServiceLogin = mapper.mapTaUtilisateurWebServiceToTaUtilisateurWebServiceLogin(taUtilisateurWebService, TaUtilisateurWebServiceLogin);
//				TaUtilisateurWebServiceLogin.setUsername(username_logindb);
//				utilisateurLoginService.merge(TaUtilisateurWebServiceLogin, ITaUtilisateurLoginServiceRemote.validationContext);

				values= taUtilisateurWebServiceService.selectAllDTO();
				nouveau = values.get(0);
				nouveau = new TaUtilisateurWebServiceDTO();

				modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
			}else{
				if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_EDITION)==0){
					
					taUtilisateurWebService = taUtilisateurWebServiceService.findById(nouveau.getId());
					mapperUIToModel.map(nouveau, taUtilisateurWebService);
					autoCompleteMapUIToDTO();
					
//					if(!listeChoixRoles.isEmpty()) {
//						//TODO tant qu'il n'y a pas de gestion des roles et qu'il n'y a que "admin" dans cette liste (vide -ou- admin), à changer
//						taUtilisateurWebService.setAdminDossier(true);
//					} else {
//						taUtilisateurWebService.setAdminDossier(false);
//					}
					taUtilisateurWebService.setSysteme(false);
					
					taUtilisateurWebService = taUtilisateurWebServiceService.merge(taUtilisateurWebService, ITaUtilisateurWebServiceServiceRemote.validationContext);
					

//					String username_logindb = nouveau.getUsername()+"_"+tenantInfo.getTenantId();
//					TaUtilisateurLogin TaUtilisateurWebServiceLogin = utilisateurLoginService.findByCode(username_logindb);
//					TaUtilisateurWebServiceLogin = mapper.mapTaUtilisateurWebServiceToTaUtilisateurWebServiceLogin(taUtilisateurWebService, TaUtilisateurWebServiceLogin);
//					TaUtilisateurWebServiceLogin.setUsername(username_logindb);
//					utilisateurLoginService.merge(TaUtilisateurWebServiceLogin, ITaUtilisateurLoginServiceRemote.validationContext);
					
					values= taUtilisateurWebServiceService.selectAllDTO();
					nouveau = values.get(0);
					
					nouveau = new TaUtilisateurWebServiceDTO();

					modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
				} else{
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "Code déja utilisé"));
				}
			}
			if(modeEcranDefaut!=null && modeEcranDefaut.equals(C_DIALOG)) {
				PrimeFaces.current().dialog().closeDynamic(taUtilisateurWebService);
			}
		} catch(Exception e) {
			e=ThrowableExceptionLgr.renvoieCauseRuntimeException(e);
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Utilisateur", e.getMessage()));
		}
	}

	public void actInserer(ActionEvent actionEvent){
		nouveau = new TaUtilisateurWebServiceDTO();
	
		modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
	}

	public void actModifier(ActionEvent actionEvent){
		nouveau = selection;
		try {
//			listeChoixRolesAncien = new ArrayList<>();
//			for(TaRRoleUtilisateur rr: TaUtilisateurWebService.getRoles()) {
//				listeChoixRolesAncien.add(taRoleService.findByCodeDTO(rr.getTaRole().getRole()));
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		autoCompleteMapDTOtoUI();
		
		modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
	}

	public void actSupprimer(){
		TaUtilisateurWebService taUtilisateurWebService = new TaUtilisateurWebService();
		try {
			if(selection!=null && selection.getId()!=null){
				taUtilisateurWebService = taUtilisateurWebServiceService.findById(selection.getId());
			}

			taUtilisateurWebServiceService.remove(taUtilisateurWebService);
			
//			String username_logindb = nouveau.getUsername()+"_"+tenantInfo.getTenantId();
//			TaUtilisateurWebServiceLogin TaUtilisateurWebServiceLogin = utilisateurLoginService.findByCode(username_logindb);
//			utilisateurLoginService.remove(TaUtilisateurWebServiceLogin);
			
			values.remove(selection);
			
			if(!values.isEmpty()) {
				selection = values.get(0);
			}else {
				selection=new TaUtilisateurWebServiceDTO();
			}

			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Utilisateur", "actSupprimer"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "TVA", e.getCause().getCause().getCause().getCause().getMessage()));
		}
	}
	
	public void onRowSelect(SelectEvent event) {  
		if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_CONSULTATION)==0) {
			nouveau = selection;
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
	
	public void validateObject(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		String messageComplet = "";
		try {
			String nomChamp =  (String) component.getAttributes().get("nomChamp");
			//validation automatique via la JSR bean validation
			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
			Set<ConstraintViolation<TaUtilisateurWebServiceDTO>> violations = factory.getValidator().validateValue(TaUtilisateurWebServiceDTO.class,nomChamp,value);
			if (violations.size() > 0) {
				messageComplet = "Erreur de validation : ";
				for (ConstraintViolation<TaUtilisateurWebServiceDTO> cv : violations) {
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
			if(nomChamp.equals(Const.C_CODE_TVA)) {
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void actDialogTypes(ActionEvent actionEvent) {
		
		ListeModules listeXml = new ListeModules();
		listeXml = listeXml.recupModulesXml(autorisation.getFichier());
		
//		if(LibConversion.stringToInteger(listeXml.nbUtilisateur)>=values.size()) {
	
			Map<String,Object> options = new HashMap<String, Object>();
			options.put("modal", true);
			options.put("draggable", true);
			options.put("resizable", false);
			options.put("contentHeight", 320);
			options.put("modal", true);
	
			Map<String,List<String>> params = new HashMap<String,List<String>>();
			List<String> list = new ArrayList<String>();
			list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
			params.put("modeEcranDefaut", list);
	
			PrimeFaces.current().dialog().openDynamic("admin/dialog_utilisateurs_web_services", options, params);
//		} else {
//			FacesContext context = FacesContext.getCurrentInstance();  
//			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Le nombre maximum d'utilisateur pour ce dossier est déjà atteint.", null));
//		}

	}

	public void handleReturnDialogTypes(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			taUtilisateurWebService = (TaUtilisateurWebService) event.getObject();
			
		}
		refresh();
	}

	public void actDialogModifier(ActionEvent actionEvent){

		nouveau = selection;
		//		modeEcran.setMode(EnumModeObjet.C_MO_EDITION);

		Map<String,Object> options = new HashMap<String, Object>();
		options.put("modal", true);
		options.put("draggable", true);
		options.put("resizable", true);
		options.put("contentHeight", 620);
		options.put("modal", true);

		Map<String,List<String>> params = new HashMap<String,List<String>>();
		List<String> list = new ArrayList<String>();
		list.add(modeEcran.modeString(EnumModeObjet.C_MO_EDITION));
		params.put("modeEcranDefaut", list);
		List<String> list2 = new ArrayList<String>();
		list2.add(LibConversion.integerToString(selection.getId()));
		params.put("idEntity", list2);

		PrimeFaces.current().dialog().openDynamic("admin/dialog_utilisateurs_web_services", options, params);

	}

	public List<TaUtilisateurWebServiceDTO> getValues(){  
		return values;
	}

	public TaUtilisateurWebServiceDTO getNouveau() {
		return nouveau;
	}

	public void setNouveau(TaUtilisateurWebServiceDTO newTaUtilisateurWebService) {
		this.nouveau = newTaUtilisateurWebService;
	}

	public TaUtilisateurWebServiceDTO getSelection() {
		return selection;
	}

	public void setSelection(TaUtilisateurWebServiceDTO selectedTaUtilisateurWebService) {
		this.selection = selectedTaUtilisateurWebService;
	}

	public void setValues(List<TaUtilisateurWebServiceDTO> values) {
		this.values = values;
	}

	public List<TaUtilisateurWebServiceDTO> getFilteredValues() {
		return filteredValues;
	}

	public void setFilteredValues(List<TaUtilisateurWebServiceDTO> filteredValues) {
		this.filteredValues = filteredValues;
	}

	public ModeObjetEcranServeur getModeEcran() {
		return modeEcran;
	}

	public List<TaRoleDTO> getListeRoles() {
		return listeRoles;
	}

	public void setListeRoles(List<TaRoleDTO> listeRoles) {
		this.listeRoles = listeRoles;
	}

	public List<TaRoleDTO> getListeChoixRoles() {
		return listeChoixRoles;
	}

	public void setListeChoixRoles(List<TaRoleDTO> listeChoixRoles) {
		this.listeChoixRoles = listeChoixRoles;
	}

	public List<Module> getListeModuleDispo() {
		return listeModuleDispo;
	}

	public void setListeModuleDispo(List<Module> listeModuleDispo) {
		this.listeModuleDispo = listeModuleDispo;
	}

	public List<Module> getListeChoixModule() {
		return listeChoixModule;
	}

	public void setListeChoixModule(List<Module> listeChoixModule) {
		this.listeChoixModule = listeChoixModule;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getPwd2() {
		return pwd2;
	}

	public void setPwd2(String pwd2) {
		this.pwd2 = pwd2;
	}

}  
