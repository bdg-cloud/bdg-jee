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

import fr.legrain.article.dto.TaFamilleUniteDTO;
import fr.legrain.article.model.TaFamilleUnite;
import fr.legrain.bdg.article.service.remote.ITaFamilleUniteServiceRemote;
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
public class FamilleUniteController implements Serializable {  

	private List<TaFamilleUniteDTO> values; 
	private List<TaFamilleUniteDTO> filteredValues; 
	private TaFamilleUniteDTO nouveau ;
	private TaFamilleUniteDTO selection ;
	
	private TaFamilleUnite taFamilleUnite = new TaFamilleUnite();
	
	private String modeEcranDefaut;
	private static final String C_DIALOG = "dialog";

	private ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();
	
	private @EJB ITaFamilleUniteServiceRemote taFamilleUniteService;
	
	private LgrDozerMapper<TaFamilleUniteDTO,TaFamilleUnite> mapperUIToModel  = new LgrDozerMapper<TaFamilleUniteDTO,TaFamilleUnite>();
	private LgrDozerMapper<TaFamilleUnite,TaFamilleUniteDTO> mapperModelToUI  = new LgrDozerMapper<TaFamilleUnite,TaFamilleUniteDTO>();
	
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
	
	public FamilleUniteController() {  
	} 

	public void refresh(){
		values = taFamilleUniteService.selectAllDTO();
		Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		modeEcranDefaut = params.get("modeEcranDefaut");
		if(modeEcranDefaut!=null && modeEcranDefaut.equals(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION))) {
			modeEcranDefaut = C_DIALOG;
			actInserer(null);
		} else if(modeEcranDefaut!=null && modeEcranDefaut.equals(modeEcran.modeString(EnumModeObjet.C_MO_EDITION))) {
			modeEcranDefaut = C_DIALOG;
			if(params.get("idEntity")!=null) {
				try {
					selection = taFamilleUniteService.findByIdDTO(LibConversion.stringToInteger(params.get("idEntity")));
				} catch (FinderException e) {
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
		}		
		nouveau = new TaFamilleUniteDTO();

		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}
	
	public void actEnregistrer(ActionEvent actionEvent){
		try {
			TaFamilleUniteDTO retour = null;
			taFamilleUnite=new TaFamilleUnite();
			if(nouveau.getId()==null || taFamilleUniteService.findById(nouveau.getId()) == null){
				
				mapperUIToModel.map(nouveau, taFamilleUnite);
				taFamilleUnite = taFamilleUniteService.merge(taFamilleUnite, ITaTCiviliteServiceRemote.validationContext);
				mapperModelToUI.map(taFamilleUnite, nouveau);
				values= taFamilleUniteService.selectAllDTO();
				nouveau = values.get(0);
				nouveau = new TaFamilleUniteDTO();

				modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

			}else{
				if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_EDITION)==0){
					taFamilleUnite = taFamilleUniteService.findById(nouveau.getId());
					mapperUIToModel.map(nouveau, taFamilleUnite);
					taFamilleUnite = taFamilleUniteService.merge(taFamilleUnite, ITaTCiviliteServiceRemote.validationContext);
					mapperModelToUI.map(taFamilleUnite, nouveau);
					values= taFamilleUniteService.selectAllDTO();
					nouveau = values.get(0);
					nouveau = new TaFamilleUniteDTO();

					modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
				}
				else{
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "Code déja utilisé"));
				}
			}
			if(modeEcranDefaut!=null && modeEcranDefaut.equals(C_DIALOG)) {
				PrimeFaces.current().dialog().closeDynamic(taFamilleUnite);
			}
		} catch(Exception e) {
			e=ThrowableExceptionLgr.renvoieCauseRuntimeException(e);
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "TypeFamilleUnite", e.getMessage()));
		}
	}

	public void actInserer(ActionEvent actionEvent){
		nouveau = new TaFamilleUniteDTO();
		modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
	}

	public void actModifier(ActionEvent actionEvent){
		nouveau = selection;
		modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
	}

	public void actSupprimer(){
		TaFamilleUnite taFamilleUnite = new TaFamilleUnite();
		try {
			if(selection!=null && selection.getId()!=null){
				taFamilleUnite = taFamilleUniteService.findById(selection.getId());
			}

			taFamilleUniteService.remove(taFamilleUnite);
			values.remove(selection);
			
			if(!values.isEmpty()) {
				selection = values.get(0);
			}else {
				selection=new TaFamilleUniteDTO();
			}

			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Famille d'unité", "actSupprimer"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Famille d'unité", e.getCause().getCause().getCause().getCause().getMessage()));
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

	public List<TaFamilleUniteDTO> getValues(){  
		return values;
		
	}
	
	public TaFamilleUniteDTO getNouveau() {
		return nouveau;
	}

	public void setNouveau(TaFamilleUniteDTO newTaFamilleUnite) {
		this.nouveau = newTaFamilleUnite;
	}

	public TaFamilleUniteDTO getSelection() {
		return selection;
	}

	public void setSelection(TaFamilleUniteDTO selectedTaFamilleUnite) {
		this.selection = selectedTaFamilleUnite;
	}

	public void setValues(List<TaFamilleUniteDTO> values) {
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

	public ITaFamilleUniteServiceRemote getTaFamilleUniteService() {
		return taFamilleUniteService;
	}

	public void setTaFamilleUniteService(
			ITaFamilleUniteServiceRemote taFamilleUniteService) {
		this.taFamilleUniteService = taFamilleUniteService;
	}

	public static String getcDialog() {
		return C_DIALOG;
	}

	public List<TaFamilleUniteDTO> getFilteredValues() {
	    return filteredValues;
	}

	public void setFilteredValues(List<TaFamilleUniteDTO> filteredValues) {
	    this.filteredValues = filteredValues;
	}
	
	// Dima - Début
	public void validateObject(FacesContext context, UIComponent component, Object value) throws ValidatorException {
//		String msg = "";
//		try {
//			String nomChamp =  (String) component.getAttributes().get("nomChamp");
//			validateUIField(nomChamp,value);
//			TaFamilleUniteDTO temp=new TaFamilleUniteDTO();
//			PropertyUtils.setProperty(temp, nomChamp, value);
//			taFamilleUniteService.validateEntityProperty(temp, nomChamp, ITaFamilleUniteServiceRemote.validationContext );
//		} catch(Exception e) {
//			msg += e.getMessage();
//			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
//		}
		
		String messageComplet = "";
		try {
			String nomChamp =  (String) component.getAttributes().get("nomChamp");
			//validation automatique via la JSR bean validation
			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
			Set<ConstraintViolation<TaFamilleUniteDTO>> violations = factory.getValidator().validateValue(TaFamilleUniteDTO.class,nomChamp,value);
			if (violations.size() > 0) {
				messageComplet = "Erreur de validation : ";
				for (ConstraintViolation<TaFamilleUniteDTO> cv : violations) {
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
			if(nomChamp.equals(Const.C_TYPE_CODE_FAMILLE_UNITE)) {
					boolean changement=false;
					if(selection.getCodeFamille()!=null && value!=null && !selection.getCodeFamille().equals(""))
					{
						if(value instanceof TaFamilleUnite)
							changement=((TaFamilleUnite) value).getCodeFamille().equals(selection.getCodeFamille());
						else if(value instanceof String)
						changement=!value.equals(selection.getCodeFamille());
					}
					if(changement && modeEcran.dataSetEnModeModification()){
						FacesContext context = FacesContext.getCurrentInstance();  
						context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Famille d'unité", Const.C_MESSAGE_CHANGEMENT_CODE));
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
        options.put("modal", true);
        
        Map<String,List<String>> params = new HashMap<String,List<String>>();
        List<String> list = new ArrayList<String>();
        list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
        params.put("modeEcranDefaut", list);
        
        PrimeFaces.current().dialog().openDynamic("articles/dialog_type_famille_unite", options, params);
        
	}
	
	public void handleReturnDialogTypes(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			taFamilleUnite = (TaFamilleUnite) event.getObject();
			
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
        options.put("contentHeight", 320);
        options.put("modal", true);
        
        Map<String,List<String>> params = new HashMap<String,List<String>>();
        List<String> list = new ArrayList<String>();
        list.add(modeEcran.modeString(EnumModeObjet.C_MO_EDITION));
        params.put("modeEcranDefaut", list);
        List<String> list2 = new ArrayList<String>();
        list2.add(LibConversion.integerToString(selection.getId()));
        params.put("idEntity", list2);
        
        PrimeFaces.current().dialog().openDynamic("articles/dialog_type_famille_unite", options, params);

	}
	// Dima -  Fin
}  
