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
import javax.inject.Named;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

import fr.legrain.bdg.droits.service.remote.ITaRoleServiceRemote;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.tiers.service.remote.ITaTCiviliteServiceRemote;
import fr.legrain.bdg.webapp.ConstWeb;
import fr.legrain.droits.dto.TaRoleDTO;
import fr.legrain.droits.model.TaRole;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.ModeObjetEcranServeur;

@Named
@ViewScoped  
public class RoleController implements Serializable {
private List<TaRoleDTO> values; 
private List<TaRoleDTO> filteredValues; 
private TaRoleDTO nouveau ;
private TaRoleDTO selection ;

private TaRole taRole = new TaRole();

private String modeEcranDefaut;
private static final String C_DIALOG = "dialog";

private ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();

private @EJB ITaRoleServiceRemote taRoleService;

private LgrDozerMapper<TaRoleDTO,TaRole> mapperUIToModel  = new LgrDozerMapper<TaRoleDTO,TaRole>();
private LgrDozerMapper<TaRole,TaRoleDTO> mapperModelToUI  = new LgrDozerMapper<TaRole,TaRoleDTO>();

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

public RoleController() {  
}  

public void actInserer(ActionEvent actionEvent){
	nouveau = new TaRoleDTO();
	modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
}

public void actModifier(ActionEvent actionEvent){
	nouveau = selection;
	modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
}

public void actSupprimer(ActionEvent actionEvent){
	TaRole taRole = new TaRole();
	try {
		if(selection!=null && selection.getId()!=null){
			taRole = taRoleService.findById(selection.getId());
		}

		taRoleService.remove(taRole);
		values.remove(selection);
		
		if(!values.isEmpty()) {
			selection = values.get(0);
		}else {
			selection=new TaRoleDTO();
		}

		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Role", "actSupprimer"));
		}
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Role", e.getCause().getCause().getCause().getCause().getMessage()));
	}
}

public void actEnregistrer(ActionEvent actionEvent){
	try {
		TaRoleDTO retour = null;
		taRole=new TaRole();
		if(nouveau.getId()==null || taRoleService.findById(nouveau.getId()) == null){
			mapperUIToModel.map(nouveau, taRole);
			taRole = taRoleService.merge(taRole, ITaTCiviliteServiceRemote.validationContext);
			mapperModelToUI.map(taRole, nouveau);
			values= taRoleService.selectAllDTO();
			nouveau = values.get(0);
			nouveau = new TaRoleDTO();

			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

		}else{
			if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_EDITION)==0){
				taRole = taRoleService.findById(nouveau.getId());
				mapperUIToModel.map(nouveau, taRole);
				taRole = taRoleService.merge(taRole, ITaTCiviliteServiceRemote.validationContext);
				mapperModelToUI.map(taRole, nouveau);
				values= taRoleService.selectAllDTO();
				nouveau = values.get(0);
				nouveau = new TaRoleDTO();

				modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
			}
			else{
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "Code déja utilisé"));
			}
		}
		if(modeEcranDefaut!=null && modeEcranDefaut.equals(C_DIALOG)) {
			PrimeFaces.current().dialog().closeDynamic(taRole);
		}
	} catch(Exception e) {
		e=ThrowableExceptionLgr.renvoieCauseRuntimeException(e);
		e.printStackTrace();
		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Role", e.getMessage()));
	}
}

public void actAnnuler(ActionEvent actionEvent){
	if(values.size()>= 1){
		selection = values.get(0);
	}		
	nouveau = new TaRoleDTO();

	modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
}

public List<TaRoleDTO> getValues(){  
	return values;
}

public TaRoleDTO getNouveau() {
	return nouveau;
}

public void setNouveau(TaRoleDTO newTaRole) {
	this.nouveau = newTaRole;
}

public TaRoleDTO getSelection() {
	return selection;
}

public void setSelection(TaRoleDTO selectedTaRole) {
	this.selection = selectedTaRole;
}

public void setValues(List<TaRoleDTO> values) {
	this.values = values;
}

public void onRowSelect(SelectEvent event) {  
	if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_CONSULTATION)==0) {
		nouveau = selection;
	}
}

public List<TaRoleDTO> getFilteredValues() {
	return filteredValues;
}

public void setFilteredValues(List<TaRoleDTO> filteredValues) {
	this.filteredValues = filteredValues;
}

public void refresh(){
	values = taRoleService.selectAllDTO();
	Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
	modeEcranDefaut = params.get("modeEcranDefaut");
	if(modeEcranDefaut!=null && modeEcranDefaut.equals(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION))) {
		modeEcranDefaut = C_DIALOG;
		actInserer(null);
	} else if(modeEcranDefaut!=null && modeEcranDefaut.equals(modeEcran.modeString(EnumModeObjet.C_MO_EDITION))) {
		modeEcranDefaut = C_DIALOG;
		if(params.get("idEntity")!=null) {
			try {
				selection = taRoleService.findByIdDTO(LibConversion.stringToInteger(params.get("idEntity")));
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

// Dima - Début
public void validateObject(FacesContext context, UIComponent component, Object value) throws ValidatorException {
//	String msg = "";
//	try {
//		String nomChamp =  (String) component.getAttributes().get("nomChamp");
//		validateUIField(nomChamp,value);
//		TaFamilleDTO temp=new TaFamilleDTO();
//		PropertyUtils.setProperty(temp, nomChamp, value);
//		taTFamilleArticlesService.validateEntityProperty(temp, nomChamp, ITaFamilleServiceRemote.validationContext );
//	} catch(Exception e) {
//		msg += e.getMessage();
//		throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
//	}
	
	String messageComplet = "";
	try {
		String nomChamp =  (String) component.getAttributes().get("nomChamp");
		//validation automatique via la JSR bean validation
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Set<ConstraintViolation<TaRoleDTO>> violations = factory.getValidator().validateValue(TaRoleDTO.class,nomChamp,value);
		if (violations.size() > 0) {
			messageComplet = "Erreur de validation : ";
			for (ConstraintViolation<TaRoleDTO> cv : violations) {
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
		if(nomChamp.equals(Const.C_Role)) {
				boolean changement=false;
				if(selection.getRole()!=null && value!=null && !selection.getRole().equals(""))
				{
					if(value instanceof TaRole)
						changement=((TaRole) value).getRole().equals(selection.getRole());
					else if(value instanceof String)
					changement=!value.equals(selection.getRole());
				}
				if(changement && modeEcran.dataSetEnModeModification()){
					FacesContext context = FacesContext.getCurrentInstance();  
					context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Role", Const.C_MESSAGE_CHANGEMENT_CODE));
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
	options.put("contentHeight", 320);

	Map<String,List<String>> params = new HashMap<String,List<String>>();
	List<String> list = new ArrayList<String>();
	list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
	params.put("modeEcranDefaut", list);

	PrimeFaces.current().dialog().openDynamic("admin/dialog_role", options, params);

}

public void handleReturnDialogTypes(SelectEvent event) {
	if(event!=null && event.getObject()!=null) {
		taRole = (TaRole) event.getObject();
		refresh();
	}
	
}

public void actDialogModifier(ActionEvent actionEvent){

	nouveau = selection;
	//		modeEcran.setMode(EnumModeObjet.C_MO_EDITION);

	Map<String,Object> options = new HashMap<String, Object>();
	options.put("modal", true);
	options.put("draggable", true);
	options.put("resizable", false);
	options.put("contentHeight", 320);
	options.put("modal", true);

	Map<String,List<String>> params = new HashMap<String,List<String>>();
	List<String> list = new ArrayList<String>();
	list.add(modeEcran.modeString(EnumModeObjet.C_MO_EDITION));
	params.put("modeEcranDefaut", list);
	List<String> list2 = new ArrayList<String>();
	list2.add(LibConversion.integerToString(selection.getId()));
	params.put("idEntity", list2);

	PrimeFaces.current().dialog().openDynamic("admin/dialog_role", options, params);

}
// Dima -  Fin

}  
