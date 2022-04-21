package fr.legrain.bdg.webapp.articles;

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

import fr.legrain.article.dto.TaCoefficientUniteDTO;
import fr.legrain.article.model.TaCoefficientUnite;
import fr.legrain.article.model.TaUnite;
import fr.legrain.bdg.article.service.remote.ITaCoefficientUniteServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaUniteServiceRemote;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.tiers.service.remote.ITaTCiviliteServiceRemote;
import fr.legrain.bdg.webapp.ConstWeb;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.ModeObjetEcranServeur;

@Named
@ViewScoped  
public class CoefficientUniteController implements Serializable {  

	private List<TaCoefficientUniteDTO> values; 
	private List<TaCoefficientUniteDTO> filteredValues; 
	private TaCoefficientUniteDTO nouveau ;
	private TaCoefficientUniteDTO selection ;
	
	private TaCoefficientUnite taCoefficientUnite = new TaCoefficientUnite();
	
	private String modeEcranDefaut;
	private static final String C_DIALOG = "dialog";

	private TaUnite taUniteA;
	private TaUnite taUniteB;

	private ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();
	
	private @EJB ITaCoefficientUniteServiceRemote taCoefficientUniteService;
	
	private @EJB ITaUniteServiceRemote taUniteService;
	
	private TaCoefficientUniteDTO taCoeffcientUniteDTO = new TaCoefficientUniteDTO();
	
	private LgrDozerMapper<TaCoefficientUniteDTO,TaCoefficientUnite> mapperUIToModel  = new LgrDozerMapper<TaCoefficientUniteDTO,TaCoefficientUnite>();
	private LgrDozerMapper<TaCoefficientUnite,TaCoefficientUniteDTO> mapperModelToUI  = new LgrDozerMapper<TaCoefficientUnite,TaCoefficientUniteDTO>();


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
	
	public CoefficientUniteController() {  
	} 

	public void refresh(){
		values = taCoefficientUniteService.selectAllDTO();
		Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		modeEcranDefaut = params.get("modeEcranDefaut");
		if(modeEcranDefaut!=null && modeEcranDefaut.equals(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION))) {
			modeEcranDefaut = C_DIALOG;
			actInserer(null);
		} else if(modeEcranDefaut!=null && modeEcranDefaut.equals(modeEcran.modeString(EnumModeObjet.C_MO_EDITION))) {
			modeEcranDefaut = C_DIALOG;
			if(params.get("idEntity")!=null) {
				try {
					selection = taCoefficientUniteService.findByIdDTO(LibConversion.stringToInteger(params.get("idEntity")));
					taCoefficientUnite = taCoefficientUniteService.findById(selection.getId());
				} catch (FinderException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			actModifier(null);
			autoCompleteMapEntitytoUI();
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
		}		
		nouveau = new TaCoefficientUniteDTO();
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}

	public void autoCompleteMapUIToEntity() {
		if(taUniteA!=null) {
			validateUIField(Const.C_CODE_UNITE,taUniteA.getCodeUnite());
			taCoefficientUnite.setUniteA(taUniteA);
		}
		if(taUniteB!=null) {
			validateUIField(Const.C_CODE_UNITE,taUniteB.getCodeUnite());
			taCoefficientUnite.setUniteB(taUniteB);
		}
	}
	
	public void autoCompleteMapEntitytoUI() {
		try {
			taUniteA = null;
			taUniteB = null;
			
			if(taCoefficientUnite.getUniteA()!=null ) {
				taUniteA = taUniteService.findByCode(taCoefficientUnite.getUniteA().getCodeUnite());
			}
			if(taCoefficientUnite.getUniteB()!=null ) {
				taUniteB = taUniteService.findByCode(taCoefficientUnite.getUniteB().getCodeUnite());
			}
			
		} catch (FinderException e) {
			e.printStackTrace();
		}
	}
	
	public void actEnregistrer(ActionEvent actionEvent){
		try {
			TaCoefficientUniteDTO retour = null;
			taCoefficientUnite=new TaCoefficientUnite();
			if(nouveau.getId()==null || taCoefficientUniteService.findById(nouveau.getId()) == null){
				mapperUIToModel.map(nouveau, taCoefficientUnite);
				autoCompleteMapUIToEntity();
				taCoefficientUnite = taCoefficientUniteService.merge(taCoefficientUnite, ITaTCiviliteServiceRemote.validationContext);
				mapperModelToUI.map(taCoefficientUnite, nouveau);
				values= taCoefficientUniteService.selectAllDTO();
				nouveau = values.get(0);
				nouveau = new TaCoefficientUniteDTO();

				modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

			}else{
				if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_EDITION)==0){
					taCoefficientUnite = taCoefficientUniteService.findById(nouveau.getId());
					mapperUIToModel.map(nouveau, taCoefficientUnite);
					autoCompleteMapUIToEntity();
					taCoefficientUnite = taCoefficientUniteService.merge(taCoefficientUnite, ITaTCiviliteServiceRemote.validationContext);
					mapperModelToUI.map(taCoefficientUnite, nouveau);
					values= taCoefficientUniteService.selectAllDTO();
					nouveau = values.get(0);
					nouveau = new TaCoefficientUniteDTO();

					modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
				} else {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "Code déja utilisé"));
				}
			}
			if(modeEcranDefaut!=null && modeEcranDefaut.equals(C_DIALOG)) {
				PrimeFaces.current().dialog().closeDynamic(taCoefficientUnite);
			}
		} catch(Exception e) {
			e=ThrowableExceptionLgr.renvoieCauseRuntimeException(e);
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "TypeUnite", e.getMessage()));
		}
	}

	public void actInserer(ActionEvent actionEvent) {
		nouveau = new TaCoefficientUniteDTO();
		modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
	}

	public void actModifier(ActionEvent actionEvent) {
		nouveau = selection;
		modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
	}

	public void actSupprimer(){
		TaCoefficientUnite taUnite = new TaCoefficientUnite();
		try {
			if(selection!=null && selection.getId()!=null){
				taUnite = taCoefficientUniteService.findById(selection.getId());
			}

			taCoefficientUniteService.remove(taUnite);
			values.remove(selection);
			
			if(!values.isEmpty()) {
				selection = values.get(0);
			}else {
				selection=new TaCoefficientUniteDTO();
			}

			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("unité", "actSupprimer"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "unité", e.getCause().getCause().getCause().getCause().getMessage()));
		}
	}
	
	public void onRowSelect (){
		if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_CONSULTATION)==0) {
			nouveau = selection;
		} 
	}
	
	public boolean editable() {
		return !modeEcran.dataSetEnModif();
	}
	
	public void autcompleteSelection(SelectEvent event) {
		Object value = event.getObject();
		
		String nomChamp =  (String) event.getComponent().getAttributes().get("nomChamp");

		validateUIField(nomChamp,value);
	}
	
	public boolean validateUIField(String nomChamp,Object value) {
		boolean changement=false;
		try {				
			if(nomChamp.equals(Const.C_CODE_UNITE+"A")) {
				TaUnite entity = new TaUnite();
				if(value!=null){
					if(value instanceof TaUnite){
						entity=(TaUnite)value;
					}else{
						entity = taUniteService.findByCode((String)value);
					}
					taCoefficientUnite.setUniteA(entity);															
				}else {
					nouveau.setCodeUniteA("");
				}
			}
			if(nomChamp.equals(Const.C_CODE_UNITE+"B")) {
				TaUnite entity = new TaUnite();
				if(value!=null){
					if(value instanceof TaUnite){
						entity=(TaUnite)value;
					}else{
						entity = taUniteService.findByCode((String)value);
					}
					taCoefficientUnite.setUniteB(entity);															
				}else {
					nouveau.setCodeUniteA("");
				}
			}
//			if(nomChamp.equals(Const.C_CODE_UNITE)) {
//				if(selection.getCodeUnite()!=null && value!=null && !selection.getCodeUnite().equals(""))
//				{
//					if(value instanceof TaCoefficientUnite)
//						changement=((TaCoefficientUnite) value).getCodeUnite().equals(selection.getCodeUnite());
//					else if(value instanceof String)
//					changement=!value.equals(selection.getCodeUnite());
//				}
//				if(changement && modeEcran.dataSetEnModeModification()){
//					FacesContext context = FacesContext.getCurrentInstance();  
//					context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Unité", Const.C_MESSAGE_CHANGEMENT_CODE));
//				}
//			}			
			return false;
		} catch (Exception e) {
		}
		return false;
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
//		String msg = "";
//		try {
//			String nomChamp =  (String) component.getAttributes().get("nomChamp");
//			validateUIField(nomChamp,value);
//			TaCoefficientUniteDTO temp=new TaCoefficientUniteDTO();
//			PropertyUtils.setProperty(temp, nomChamp, value);
//			taUniteService.validateEntityProperty(temp, nomChamp, ITaCoefficientUniteServiceRemote.validationContext );
//		} catch(Exception e) {
//			msg += e.getMessage();
//			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
//		}
		String messageComplet = "";
		try {
			String nomChamp =  (String) component.getAttributes().get("nomChamp");
			//validation automatique via la JSR bean validation
			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
			Set<ConstraintViolation<TaCoefficientUniteDTO>> violations = factory.getValidator().validateValue(TaCoefficientUniteDTO.class,nomChamp,value);
			if (violations.size() > 0) {
				messageComplet = "Erreur de validation : ";
				for (ConstraintViolation<TaCoefficientUniteDTO> cv : violations) {
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

	public void actDialogTypes(ActionEvent actionEvent) {
	    
        Map<String,Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("draggable", true);
        options.put("resizable", false);
        options.put("contentHeight", 500);
        
        Map<String,List<String>> params = new HashMap<String,List<String>>();
        List<String> list = new ArrayList<String>();
        list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
        params.put("modeEcranDefaut", list);
        
        PrimeFaces.current().dialog().openDynamic("articles/dialog_coefficient_unite", options, params);
        
	}
	
	public void handleReturnDialogTypes(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			taCoefficientUnite = (TaCoefficientUnite) event.getObject();
			
		}
		refresh();
	}
	
	public void actDialogModifier(ActionEvent actionEvent){
		
		nouveau = selection;
//		modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
		
		Map<String,Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("draggable", true);
        options.put("resizable", false);
        options.put("contentHeight", 500);
        options.put("modal", true);
        
        Map<String,List<String>> params = new HashMap<String,List<String>>();
        List<String> list = new ArrayList<String>();
        list.add(modeEcran.modeString(EnumModeObjet.C_MO_EDITION));
        params.put("modeEcranDefaut", list);
        List<String> list2 = new ArrayList<String>();
        list2.add(LibConversion.integerToString(selection.getId()));
        params.put("idEntity", list2);
        
        PrimeFaces.current().dialog().openDynamic("articles/dialog_coefficient_unite", options, params);

	}

	public List<TaUnite> typeUniteAutoComplete(String query) {
        List<TaUnite> allValues = taUniteService.selectAll();
        List<TaUnite> filteredValues = new ArrayList<TaUnite>();
        
        TaUnite civ = new TaUnite();
        civ.setCodeUnite(Const.C_AUCUN);
        filteredValues.add(civ);
        for (int i = 0; i < allValues.size(); i++) {
        	 civ = allValues.get(i);
            if(query.equals("*") || civ.getCodeUnite().toLowerCase().contains(query.toLowerCase())) {
                filteredValues.add(civ);
            }
        }
        return filteredValues;
    }
	
	public List<TaCoefficientUniteDTO> getValues(){  
		return values;
	}

	public TaCoefficientUniteDTO getNouveau() {
		return nouveau;
	}

	public void setNouveau(TaCoefficientUniteDTO newTaCoefficientUnite) {
		this.nouveau = newTaCoefficientUnite;
	}

	public TaCoefficientUniteDTO getSelection() {
		return selection;
	}

	public void setSelection(TaCoefficientUniteDTO selectedTaCoefficientUnite) {
		this.selection = selectedTaCoefficientUnite;
	}

	public void setValues(List<TaCoefficientUniteDTO> values) {
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

	public ITaCoefficientUniteServiceRemote getTaCoefficientUniteService() {
		return taCoefficientUniteService;
	}

	public void setTaCoefficientUniteService(
			ITaCoefficientUniteServiceRemote taUniteService) {
		this.taCoefficientUniteService = taUniteService;
	}

	public static String getcDialog() {
		return C_DIALOG;
	}

	public List<TaCoefficientUniteDTO> getFilteredValues() {
	    return filteredValues;
	}

	public void setFilteredValues(List<TaCoefficientUniteDTO> filteredValues) {
	    this.filteredValues = filteredValues;
	}
	

	public TaCoefficientUniteDTO getTaCoefficientUnite() {
		return taCoeffcientUniteDTO;
	}

	public void setTaCoefficientUnite(TaCoefficientUniteDTO taUnite) {
		this.taCoeffcientUniteDTO = taUnite;
	}

	public TaUnite getTaUniteB() {
		return taUniteB;
	}

	public void setTaUniteB(TaUnite taUniteB) {
		this.taUniteB = taUniteB;
	}

	public void setTaUniteA(TaUnite taUniteA) {
		this.taUniteA = taUniteA;
	}

	public TaUnite getTaUniteA() {
		return taUniteA;
	}
}  
