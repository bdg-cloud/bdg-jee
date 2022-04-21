package fr.legrain.bdg.webapp.app;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
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

import fr.legrain.bdg.general.service.remote.ITaLogDossierServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.droits.service.TenantInfo;
import fr.legrain.general.dto.TaLogDossierDTO;
import fr.legrain.general.model.TaLogDossier;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.ModeObjetEcranServeur;

@Named
@ViewScoped  
public class LogDossierController implements Serializable {  
	
	private List<TaLogDossierDTO> values; 
	private List<TaLogDossierDTO> filteredValues; 
	private TaLogDossierDTO selection;
	
	private TaLogDossier taLogDossier = new TaLogDossier();
	
	@Inject private	TenantInfo tenantInfo;
	
	private ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();

	@EJB private ITaLogDossierServiceRemote taLogDossierService;
	
	private LgrDozerMapper<TaLogDossierDTO,TaLogDossier> mapperUIToModel  = new LgrDozerMapper<TaLogDossierDTO,TaLogDossier>();
	private LgrDozerMapper<TaLogDossier,TaLogDossierDTO> mapperModelToUI  = new LgrDozerMapper<TaLogDossier,TaLogDossierDTO>();

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
		values = taLogDossierService.selectAllDTO();

		
		Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
		
	}

	public LogDossierController() {  
	}  
	
	public void autoCompleteMapUIToDTO() {
		try {
//			taUtilisateur = taUtilisateurService.findById(nouveau.getId());
			//Roles
//			TaRoleDTO roleDTO;
//			TaRole role;
//			for (TaRoleDTO roleChoisi : listeChoixRoles) {
//				if(nouveau.getRoles()==null) {
//					nouveau.setRoles(new ArrayList<TaRRoleUtilisateurDTO>());
//				}
//				if(!taUtilisateur.hasRole(roleChoisi.getRole())){ //ajout des nouveau roles
//					roleDTO = taRoleService.findByCodeDTO(roleChoisi.getRole());
//					TaRRoleUtilisateurDTO ruDTO= new TaRRoleUtilisateurDTO(nouveau,roleDTO);
//					nouveau.getRoles().add(ruDTO);
//					
//					role = taRoleService.findByCode(roleChoisi.getRole());
//					TaRRoleUtilisateur ru = new TaRRoleUtilisateur(taUtilisateur,role);
//					taUtilisateur.getRoles().add(ru);
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
//					for (TaRRoleUtilisateur rr : taUtilisateur.getRoles()) {
//						if(a.getRole().equals(rr.getTaRole().getRole())) {
//							lr.add(rr);
//							rr.setTaLogDossier(null);
//							rr.setTaRole(null);
//						}
//					}
//				}
//				taUtilisateur.getRoles().removeAll(lr);
//			}
//			
//			//Modules
//			ListeModules listeXml = new ListeModules();
//			for (Module r : listeChoixModule) {
//				//Module m = new Module(r);
//				listeXml.module.add(r);
//			}
//			nouveau.setAutorisations(listeXml.creeXmlModuleString(listeXml));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void autoCompleteMapDTOtoUI() {
		try {
//			//Roles
//			listeChoixRoles = new ArrayList<TaRoleDTO>();
//			nouveau.setRoles(new ArrayList<>());
//			for(TaRRoleUtilisateur rr: taUtilisateur.getRoles()) {
//				listeChoixRoles.add(taRoleService.findByCodeDTO(rr.getTaRole().getRole()));
//			}
////			if(nouveau.getRoles()!=null) {
////				for (TaRRoleUtilisateurDTO r : nouveau.getRoles()) {
////					listeChoixRoles.add(r.getTaRole());
////				}
////			}
//			
//			//Modules
//			listeChoixModule = new ArrayList<Module>();
//			if(nouveau.getAutorisations()!=null) {
//				ListeModules listeXml = new ListeModules();
//				listeXml = listeXml.recupModulesXml(nouveau.getAutorisations());
//				for (Module m : listeXml.module) {
//					listeChoixModule.add(m);
////					listeModuleDispo.add(m);
//				}
//			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void actAnnuler(ActionEvent actionEvent){
		//values= taTAdrService.selectAll();
		
//		if(values.size()>= 1){
//			selection = values.get(0);
//		}		
//		nouveau = new TaLogDossierDTO();
		
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}
	
	
	public void actFermerDialog(ActionEvent actionEvent) {
		PrimeFaces.current().dialog().closeDynamic(null);
	}
	
	public void actEnregistrer(ActionEvent actionEvent){

//		try {

//			autoCompleteMapUIToDTO();
//			if(pwd!=null && !pwd.equals("")) {
//				nouveau.setPasswd(taUtilisateur.passwordHashSHA256_Base64(pwd));
//			}
//			
//			TaLogDossierLoginMapper mapper =  new TaLogDossierLoginMapper();
//
//			if(taUtilisateurService.findByCode(nouveau.getUsername()) == null){
//				mapperUIToModel.map(nouveau, taUtilisateur);
//				
//				if(!listeChoixRoles.isEmpty()) {
//					//TODO tant qu'il n'y a pas de gestion des roles et qu'il n'y a que "admin" dans cette liste (vide -ou- admin), à changer
//					taUtilisateur.setAdminDossier(true);
//				} else {
//					taUtilisateur.setAdminDossier(false);
//				}
//				taUtilisateur.setSysteme(false);
//				
//				taUtilisateur = taUtilisateurService.merge(taUtilisateur, ITaLogDossierServiceRemote.validationContext);
//				mapperModelToUI.map(taUtilisateur, nouveau);
//				
//				String username_logindb = nouveau.getUsername()+"_"+tenantInfo.getTenantId();
//				TaLogDossierLogin taUtilisateurLogin = new TaLogDossierLogin();
//				taUtilisateurLogin = mapper.mapTaLogDossierToTaLogDossierLogin(taUtilisateur, taUtilisateurLogin);
//				taUtilisateurLogin.setUsername(username_logindb);
//				utilisateurLoginService.merge(taUtilisateurLogin, ITaLogDossierLoginServiceRemote.validationContext);
//
//				values= taUtilisateurService.selectAllDTO();
//				nouveau = values.get(0);
//				nouveau = new TaLogDossierDTO();
//
//				modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
//			}else{
//				if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_EDITION)==0){
//					
//					taUtilisateur = taUtilisateurService.findById(nouveau.getId());
//					mapperUIToModel.map(nouveau, taUtilisateur);
//					autoCompleteMapUIToDTO();
//					
//					if(!listeChoixRoles.isEmpty()) {
//						//TODO tant qu'il n'y a pas de gestion des roles et qu'il n'y a que "admin" dans cette liste (vide -ou- admin), à changer
//						taUtilisateur.setAdminDossier(true);
//					} else {
//						taUtilisateur.setAdminDossier(false);
//					}
//					taUtilisateur.setSysteme(false);
//					
//					taUtilisateur = taUtilisateurService.merge(taUtilisateur, ITaLogDossierServiceRemote.validationContext);
//					
//
//					String username_logindb = nouveau.getUsername()+"_"+tenantInfo.getTenantId();
//					TaLogDossierLogin taUtilisateurLogin = utilisateurLoginService.findByCode(username_logindb);
//					taUtilisateurLogin = mapper.mapTaLogDossierToTaLogDossierLogin(taUtilisateur, taUtilisateurLogin);
//					taUtilisateurLogin.setUsername(username_logindb);
//					utilisateurLoginService.merge(taUtilisateurLogin, ITaLogDossierLoginServiceRemote.validationContext);
//					
//					values= taUtilisateurService.selectAllDTO();
//					nouveau = values.get(0);
//					
//					nouveau = new TaLogDossierDTO();
//
//					modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
//				} else{
//					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "Code déja utilisé"));
//				}
//			}
//			if(modeEcranDefaut!=null && modeEcranDefaut.equals(C_DIALOG)) {
//				PrimeFaces.current().dialog().closeDynamic(taUtilisateur);
//			}
//		} catch(Exception e) {
//			e=ThrowableExceptionLgr.renvoieCauseRuntimeException(e);
//			e.printStackTrace();
//			FacesContext context = FacesContext.getCurrentInstance();  
//			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Utilisateur", e.getMessage()));
//		}
	}

	public void actInserer(ActionEvent actionEvent){
//		nouveau = new TaLogDossierDTO();
	
		modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
	}

	public void actModifier(ActionEvent actionEvent){
//		nouveau = selection;
//		try {
//			listeChoixRolesAncien = new ArrayList<>();
//			for(TaRRoleUtilisateur rr: taUtilisateur.getRoles()) {
//				listeChoixRolesAncien.add(taRoleService.findByCodeDTO(rr.getTaRole().getRole()));
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		autoCompleteMapDTOtoUI();
		
		modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
	}

	public void actSupprimer(){
//		TaLogDossier taUtilisateur = new TaLogDossier();
//		try {
//			if(selection!=null && selection.getId()!=null){
//				taUtilisateur = taUtilisateurService.findById(selection.getId());
//			}
//
//			taUtilisateurService.remove(taUtilisateur);
//			
//			String username_logindb = nouveau.getUsername()+"_"+tenantInfo.getTenantId();
//			TaLogDossierLogin taUtilisateurLogin = utilisateurLoginService.findByCode(username_logindb);
//			utilisateurLoginService.remove(taUtilisateurLogin);
//			
//			values.remove(selection);
//			
//			if(!values.isEmpty()) {
//				selection = values.get(0);
//			}else {
//				selection=new TaLogDossierDTO();
//			}
//
//			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
//
//			if(ConstWeb.DEBUG) {
//				FacesContext context = FacesContext.getCurrentInstance();  
//				context.addMessage(null, new FacesMessage("Utilisateur", "actSupprimer"));
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			FacesContext context = FacesContext.getCurrentInstance();  
//			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "TVA", e.getCause().getCause().getCause().getCause().getMessage()));
//		}
	}
	
	public void onRowSelect(SelectEvent event) {  
//		if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_CONSULTATION)==0) {
//			nouveau = selection;
//		}
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
			Set<ConstraintViolation<TaLogDossierDTO>> violations = factory.getValidator().validateValue(TaLogDossierDTO.class,nomChamp,value);
			if (violations.size() > 0) {
				messageComplet = "Erreur de validation : ";
				for (ConstraintViolation<TaLogDossierDTO> cv : violations) {
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
	
	


	public List<TaLogDossierDTO> getValues(){  
		return values;
	}

	public TaLogDossierDTO getSelection() {
		return selection;
	}

	public void setSelection(TaLogDossierDTO selectedTaLogDossier) {
		this.selection = selectedTaLogDossier;
	}

	public void setValues(List<TaLogDossierDTO> values) {
		this.values = values;
	}

	public List<TaLogDossierDTO> getFilteredValues() {
		return filteredValues;
	}

	public void setFilteredValues(List<TaLogDossierDTO> filteredValues) {
		this.filteredValues = filteredValues;
	}

	public ModeObjetEcranServeur getModeEcran() {
		return modeEcran;
	}


}  
