package fr.legrain.bdg.webapp.app;

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

import fr.legrain.bdg.email.service.remote.ITaEtiquetteEmailServiceRemote;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.tiers.service.remote.ITaTCiviliteServiceRemote;
import fr.legrain.bdg.webapp.ConstWeb;
import fr.legrain.email.dto.TaEtiquetteEmailDTO;
import fr.legrain.email.model.TaEtiquetteEmail;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.ModeObjetEcranServeur;

@Named
@ViewScoped  
public class EtiquetteMessageEmailController implements Serializable {  

	private List<TaEtiquetteEmailDTO> values; 
	private List<TaEtiquetteEmailDTO> filteredValues; 
	private TaEtiquetteEmailDTO nouveau ;
	private TaEtiquetteEmailDTO selection ;
	
	private TaEtiquetteEmail TaEtiquetteEmail = new TaEtiquetteEmail();
	
	private String modeEcranDefaut;
	private static final String C_DIALOG = "dialog";

	private ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();

	private @EJB ITaEtiquetteEmailServiceRemote taEtiquetteEmailService;
	
	private LgrDozerMapper<TaEtiquetteEmailDTO,TaEtiquetteEmail> mapperUIToModel  = new LgrDozerMapper<TaEtiquetteEmailDTO,TaEtiquetteEmail>();
	private LgrDozerMapper<TaEtiquetteEmail,TaEtiquetteEmailDTO> mapperModelToUI  = new LgrDozerMapper<TaEtiquetteEmail,TaEtiquetteEmailDTO>();

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
	}

	public void refresh(){
		values = taEtiquetteEmailService.selectAllDTO();
		Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		modeEcranDefaut = params.get("modeEcranDefaut");
		if(modeEcranDefaut!=null && modeEcranDefaut.equals(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION))) {
			modeEcranDefaut = C_DIALOG;
			actInserer(null);
		} else if(modeEcranDefaut!=null && modeEcranDefaut.equals(modeEcran.modeString(EnumModeObjet.C_MO_EDITION))) {
			modeEcranDefaut = C_DIALOG;
			if(params.get("idEntity")!=null) {
				try {
					selection = taEtiquetteEmailService.findByIdDTO(LibConversion.stringToInteger(params.get("idEntity")));
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
	
	public EtiquetteMessageEmailController() {  
	}  

	public void actAnnuler(ActionEvent actionEvent){
		
		if(values.size()>= 1){
			selection = values.get(0);
		}		
		nouveau = new TaEtiquetteEmailDTO();
		
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}

	public void actEnregistrer(ActionEvent actionEvent){
		try {
			TaEtiquetteEmailDTO retour = null;
			TaEtiquetteEmail=new TaEtiquetteEmail();
			if(nouveau.getId()==null || taEtiquetteEmailService.findById(nouveau.getId()) == null){
				mapperUIToModel.map(nouveau, TaEtiquetteEmail);
				TaEtiquetteEmail = taEtiquetteEmailService.merge(TaEtiquetteEmail, ITaTCiviliteServiceRemote.validationContext);
				mapperModelToUI.map(TaEtiquetteEmail, nouveau);
				values= taEtiquetteEmailService.selectAllDTO();
				nouveau = values.get(0);
				nouveau = new TaEtiquetteEmailDTO();

				modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

			}else{
				if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_EDITION)==0){
					TaEtiquetteEmail = taEtiquetteEmailService.findById(nouveau.getId());
					mapperUIToModel.map(nouveau, TaEtiquetteEmail);
					TaEtiquetteEmail = taEtiquetteEmailService.merge(TaEtiquetteEmail, ITaTCiviliteServiceRemote.validationContext);
					mapperModelToUI.map(TaEtiquetteEmail, nouveau);
					values= taEtiquetteEmailService.selectAllDTO();
					nouveau = values.get(0);
					nouveau = new TaEtiquetteEmailDTO();

					modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
				}
				else{
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "Code déja utilisé"));
				}
			}
			if(modeEcranDefaut!=null && modeEcranDefaut.equals(C_DIALOG)) {
				PrimeFaces.current().dialog().closeDynamic(TaEtiquetteEmail);
			}
		} catch(Exception e) {
			e=ThrowableExceptionLgr.renvoieCauseRuntimeException(e);
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "TypeAdresse", e.getMessage()));
		}
	}
	
	public void actInserer(ActionEvent actionEvent){
		nouveau = new TaEtiquetteEmailDTO();
	
		modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
	}

	public void actModifier(ActionEvent actionEvent){
		nouveau = selection;
		
		modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
	}

	public void actSupprimer(){
		TaEtiquetteEmail TaEtiquetteEmail = new TaEtiquetteEmail();
		try {
			if(selection!=null && selection.getId()!=null){
				TaEtiquetteEmail = taEtiquetteEmailService.findById(selection.getId());
			}

			taEtiquetteEmailService.remove(TaEtiquetteEmail);
			values.remove(selection);
			
			if(!values.isEmpty()) {
				selection = values.get(0);
			}else {
				selection=new TaEtiquetteEmailDTO();
			}

			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Type adresse", "actSupprimer"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Type adresse", e.getCause().getCause().getCause().getCause().getMessage()));
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

	public List<TaEtiquetteEmailDTO> getValues(){  
		return values;
	}

	public TaEtiquetteEmailDTO getNouveau() {
		return nouveau;
	}

	public void setNouveau(TaEtiquetteEmailDTO newTaEtiquetteEmail) {
		this.nouveau = newTaEtiquetteEmail;
	}

	public TaEtiquetteEmailDTO getSelection() {
		return selection;
	}

	public void setSelection(TaEtiquetteEmailDTO selectedTaEtiquetteEmail) {
		this.selection = selectedTaEtiquetteEmail;
	}

	public void setValues(List<TaEtiquetteEmailDTO> values) {
		this.values = values;
	}

	public List<TaEtiquetteEmailDTO> getFilteredValues() {
		return filteredValues;
	}

	public void setFilteredValues(List<TaEtiquetteEmailDTO> filteredValues) {
		this.filteredValues = filteredValues;
	}

	public ModeObjetEcranServeur getModeEcran() {
		return modeEcran;
	}

	// Dima - Début
	public void validateObject(FacesContext context, UIComponent component, Object value) throws ValidatorException {
//
//		String msg = "";
//
//		try {
//			String nomChamp =  (String) component.getAttributes().get("nomChamp");
//			validateUIField(nomChamp,value);
//			TaEtiquetteEmailDTO temp=new TaEtiquetteEmailDTO();
//			PropertyUtils.setProperty(temp, nomChamp, value);
//			TaEtiquetteEmailService.validateEntityProperty(temp, nomChamp, ITaEtiquetteEmailServiceRemote.validationContext );
//
//		} catch(Exception e) {
//			msg += e.getMessage();
//			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
//		}
		String messageComplet = "";
		try {
			String nomChamp =  (String) component.getAttributes().get("nomChamp");
			//validation automatique via la JSR bean validation
			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
			Set<ConstraintViolation<TaEtiquetteEmailDTO>> violations = factory.getValidator().validateValue(TaEtiquetteEmailDTO.class,nomChamp,value);
			if (violations.size() > 0) {
				messageComplet = "Erreur de validation : ";
				for (ConstraintViolation<TaEtiquetteEmailDTO> cv : violations) {
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

			if(nomChamp.equals(Const.C_CODE_T_ADR)) {
				boolean changement=false;
				if(selection.getCode()!=null && value!=null && !selection.getCode().equals(""))
				{
					if(value instanceof TaEtiquetteEmail)
						changement=((TaEtiquetteEmail) value).getCode().equals(selection.getCode());
					else if(value instanceof String)
					changement=!value.equals(selection.getCode());
				}
				if(changement && modeEcran.dataSetEnModeModification()){
					FacesContext context = FacesContext.getCurrentInstance();  
					context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Type d'adresse", Const.C_MESSAGE_CHANGEMENT_CODE));
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
        options.put("draggable", false);
        options.put("resizable", false);
        options.put("contentHeight", 320);
        options.put("modal", true);
        
        Map<String,List<String>> params = new HashMap<String,List<String>>();
        List<String> list = new ArrayList<String>();
        list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
        params.put("modeEcranDefaut", list);
        
        PrimeFaces.current().dialog().openDynamic("admin/dialog_etiquette_message_email", options, params);
        
	}
	
	public void handleReturnDialogTypes(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			TaEtiquetteEmail = (TaEtiquetteEmail) event.getObject();
			
		}
		refresh();
	}
	
	public void actDialogModifier(ActionEvent actionEvent){
		
		nouveau = selection;
//		modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
		
		Map<String,Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("draggable", false);
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
        
        PrimeFaces.current().dialog().openDynamic("admin/dialog_etiquette_message_email", options, params);

	}
	
	public void actFermerDialog(ActionEvent actionEvent) {
		PrimeFaces.current().dialog().closeDynamic(null);
	}
	// Dima -  Fin
}  
