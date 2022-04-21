package fr.legrain.bdg.webapp.tiers;

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

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.tiers.service.remote.ITaTCiviliteServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTEntiteServiceRemote;
import fr.legrain.bdg.webapp.ConstWeb;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.ModeObjetEcranServeur;
import fr.legrain.tiers.dto.TaTEntiteDTO;
import fr.legrain.tiers.model.TaTEntite;

@Named
@ViewScoped  
public class TypeEntiteController implements Serializable {  

	private List<TaTEntiteDTO> values; 
	private List<TaTEntiteDTO> filteredValues; 
	private TaTEntiteDTO nouveau ;
	private TaTEntiteDTO selection ;

	private TaTEntite taTEntite = new TaTEntite();
	
	private String modeEcranDefaut;
	private static final String C_DIALOG = "dialog";

	private ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();

	private @EJB ITaTEntiteServiceRemote taTEntiteService;
	
	private LgrDozerMapper<TaTEntiteDTO,TaTEntite> mapperUIToModel  = new LgrDozerMapper<TaTEntiteDTO,TaTEntite>();
	private LgrDozerMapper<TaTEntite,TaTEntiteDTO> mapperModelToUI  = new LgrDozerMapper<TaTEntite,TaTEntiteDTO>();
	
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

	public TypeEntiteController() {  
	}  
	
	public void refresh(){
		values = taTEntiteService.selectAllDTO();
		Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		modeEcranDefaut = params.get("modeEcranDefaut");
		if(modeEcranDefaut!=null && modeEcranDefaut.equals(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION))) {
			modeEcranDefaut = C_DIALOG;
			actInserer(null);
		} else if(modeEcranDefaut!=null && modeEcranDefaut.equals(modeEcran.modeString(EnumModeObjet.C_MO_EDITION))) {
			modeEcranDefaut = C_DIALOG;
			if(params.get("idEntity")!=null) {
				try {
					selection = taTEntiteService.findByIdDTO(LibConversion.stringToInteger(params.get("idEntity")));
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

	public void actAnnuler(ActionEvent actionEvent){
		if(values.size()>= 1){
			selection = values.get(0);
		}		nouveau = new TaTEntiteDTO();
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}
	
	public void actEnregistrer(ActionEvent actionEvent){
		try {
			TaTEntiteDTO retour = null;
			taTEntite = new TaTEntite();
			if(nouveau.getId()==null || taTEntiteService.findById(nouveau.getId()) == null){
				mapperUIToModel.map(nouveau, taTEntite);
				taTEntite = taTEntiteService.merge(taTEntite, ITaTCiviliteServiceRemote.validationContext);
				mapperModelToUI.map(taTEntite, nouveau);
				values= taTEntiteService.selectAllDTO();
				nouveau = values.get(0);
				nouveau = new TaTEntiteDTO();

				modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

			}else{
				if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_EDITION)==0){
					taTEntite = taTEntiteService.findById(nouveau.getId());
					mapperUIToModel.map(nouveau, taTEntite);
					taTEntite = taTEntiteService.merge(taTEntite, ITaTCiviliteServiceRemote.validationContext);
					mapperModelToUI.map(taTEntite, nouveau);
					values= taTEntiteService.selectAllDTO();
					nouveau = values.get(0);
					nouveau = new TaTEntiteDTO();

					modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
				}
				else{
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "Code déja utilisé"));
				}
			}
			if(modeEcranDefaut!=null && modeEcranDefaut.equals(C_DIALOG)) {
				PrimeFaces.current().dialog().closeDynamic(taTEntite);
			}
		} catch(Exception e) {
			e=ThrowableExceptionLgr.renvoieCauseRuntimeException(e);
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			//context.addMessage(null, new FacesMessage("Tiers", "actEnregistrer")); 
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "TypeEntite", e.getMessage()));
		}
	}

	public void actInserer(ActionEvent actionEvent) {
		nouveau = new TaTEntiteDTO();
		modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
	}

	public void actModifier(ActionEvent actionEvent){
		nouveau = selection;
		modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
	}

	public void actSupprimer(){
		TaTEntite taTEntite = new TaTEntite();
		try {
			if(selection!=null && selection.getId()!=null){
				taTEntite = taTEntiteService.findById(selection.getId());
			}

			taTEntiteService.remove(taTEntite);
			values.remove(selection);
			
			if(!values.isEmpty()) {
				selection = values.get(0);
			}else {
				selection=new TaTEntiteDTO();
			}

			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Type entité", "actSupprimer"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Type entité", e.getCause().getCause().getCause().getCause().getMessage()));
		}
	}

	public void onRowSelect (){
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

	public List<TaTEntiteDTO> getValues(){  
		return values;
	}

	public TaTEntiteDTO getNouveau() {
		return nouveau;
	}

	public void setNouveau(TaTEntiteDTO newTaTEntite) {
		this.nouveau = newTaTEntite;
	}

	public TaTEntiteDTO getSelection() {
		return selection;
	}

	public void setSelection(TaTEntiteDTO selectedTaTEntite) {
		this.selection = selectedTaTEntite;
	}

	public void setValues(List<TaTEntiteDTO> values) {
		this.values = values;
	}

	public String getModeEcranDefaut() {
		return modeEcranDefaut;
	}

	public void setModeEcranDefaut(String modeEcranDefaut) {
		this.modeEcranDefaut = modeEcranDefaut;
	}

	public ModeObjetEcranServeur getModeEcran() {
		return modeEcran;
	}

	public void setModeEcran(ModeObjetEcranServeur modeEcran) {
		this.modeEcran = modeEcran;
	}

	public ITaTEntiteServiceRemote getTaTEntiteService() {
		return taTEntiteService;
	}

	public void setTaTEntiteService(ITaTEntiteServiceRemote taTEntiteService) {
		this.taTEntiteService = taTEntiteService;
	}

	public static String getcDialog() {
		return C_DIALOG;
	}

	public List<TaTEntiteDTO> getFilteredValues() {
		return filteredValues;
	}

	public void setFilteredValues(List<TaTEntiteDTO> filteredValues) {
		this.filteredValues = filteredValues;
	}

	// Dima - Début
	public void validateObject(FacesContext context, UIComponent component, Object value) throws ValidatorException {
//		String msg = "";
//		try {
//			String nomChamp =  (String) component.getAttributes().get("nomChamp");
//			validateUIField(nomChamp,value);
//			TaTEntiteDTO temp=new TaTEntiteDTO();
//			PropertyUtils.setProperty(temp, nomChamp, value);
//			taTEntiteService.validateEntityProperty(temp, nomChamp, ITaTEntiteServiceRemote.validationContext );
//		} catch(Exception e) {
//			msg += e.getMessage();
//			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
//		}
		String messageComplet = "";
		try {
			String nomChamp =  (String) component.getAttributes().get("nomChamp");
			//validation automatique via la JSR bean validation
			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
			Set<ConstraintViolation<TaTEntiteDTO>> violations = factory.getValidator().validateValue(TaTEntiteDTO.class,nomChamp,value);
			if (violations.size() > 0) {
				messageComplet = "Erreur de validation : ";
				for (ConstraintViolation<TaTEntiteDTO> cv : violations) {
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
			if(nomChamp.equals(Const.C_CODE_T_ENTITE)) {
					boolean changement=false;
					if(selection.getCodeTEntite()!=null && value!=null && !selection.getCodeTEntite().equals(""))
					{
						if(value instanceof TaTEntite)
							changement=((TaTEntite) value).getCodeTEntite().equals(selection.getCodeTEntite());
						else if(value instanceof String)
						changement=!value.equals(selection.getCodeTEntite());
					}
					if(changement && modeEcran.dataSetEnModeModification()){
						FacesContext context = FacesContext.getCurrentInstance();  
						context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Type d'entité", Const.C_MESSAGE_CHANGEMENT_CODE));
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
        
        PrimeFaces.current().dialog().openDynamic("tiers/dialog_type_entite", options, params);
        
	}
	
	public void handleReturnDialogTypes(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			taTEntite = (TaTEntite) event.getObject();
			
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
        
        PrimeFaces.current().dialog().openDynamic("tiers/dialog_type_entite", options, params);

	}
	// Dima -  Fin
}  
