package fr.legrain.bdg.webapp.agenda;

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
import fr.legrain.bdg.tache.service.remote.ITaCategorieEvenementServiceRemote;
import fr.legrain.bdg.tache.service.remote.ITaTypeEvenementServiceRemote;
import fr.legrain.bdg.webapp.ConstWeb;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.ModeObjetEcranServeur;
import fr.legrain.tache.dto.TaTypeEvenementDTO;
import fr.legrain.tache.model.TaCategorieEvenement;
import fr.legrain.tache.model.TaTypeEvenement;



@Named
@ViewScoped  
public class TypeEvenementController implements Serializable {  

	private List<TaTypeEvenementDTO> values; 
	private List<TaTypeEvenementDTO> filteredValues; 
	private TaTypeEvenementDTO nouveau ;
	private TaTypeEvenementDTO selection ;

	private TaTypeEvenement taTypeEvenement = new TaTypeEvenement();
	
	private TaCategorieEvenement taCategorieEvenement;
	
	private String modeEcranDefaut;
	private static final String C_DIALOG = "dialog";

	private ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();

	private @EJB ITaTypeEvenementServiceRemote taTypeEvenementService;
	private @EJB ITaCategorieEvenementServiceRemote taCategorieEvenementService;
	
	private LgrDozerMapper<TaTypeEvenementDTO,TaTypeEvenement> mapperUIToModel  = new LgrDozerMapper<TaTypeEvenementDTO,TaTypeEvenement>();
	private LgrDozerMapper<TaTypeEvenement,TaTypeEvenementDTO> mapperModelToUI  = new LgrDozerMapper<TaTypeEvenement,TaTypeEvenementDTO>();
	
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

	public TypeEvenementController(){  
	}  

	public void actInserer(ActionEvent actionEvent){
		nouveau = new TaTypeEvenementDTO();
		modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
	}

	public void actModifier(ActionEvent actionEvent){
		nouveau = selection;
		modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
	}

	public void actSupprimer(){
		TaTypeEvenement taTiers = new TaTypeEvenement();
		try {
			if(selection!=null && selection.getId()!=null){
				taTiers = taTypeEvenementService.findById(selection.getId());
			}

			taTypeEvenementService.remove(taTiers);
			values.remove(selection);
			
			if(!values.isEmpty()) {
				selection = values.get(0);
			}else {
				selection=new TaTypeEvenementDTO();
			}

			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Type evenement", "actSupprimer"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Type evenement", e.getCause().getCause().getCause().getCause().getMessage()));
		}
	}
	
	public void actEnregistrer(ActionEvent actionEvent){
		try {
			TaTypeEvenementDTO retour = null;
			taTypeEvenement=new TaTypeEvenement();
			if(nouveau.getId()==null || taTypeEvenementService.findById(nouveau.getId()) == null){
				mapperUIToModel.map(nouveau, taTypeEvenement);
				taTypeEvenement = taTypeEvenementService.merge(taTypeEvenement, ITaTypeEvenementServiceRemote.validationContext);
				mapperModelToUI.map(taTypeEvenement, nouveau);
				values= taTypeEvenementService.selectAllDTO();
				nouveau = values.get(0);
				nouveau = new TaTypeEvenementDTO();

				modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

			}else{
				if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_EDITION)==0){
					taTypeEvenement = taTypeEvenementService.findById(nouveau.getId());
					mapperUIToModel.map(nouveau, taTypeEvenement);
					taTypeEvenement = taTypeEvenementService.merge(taTypeEvenement, ITaTypeEvenementServiceRemote.validationContext);
					mapperModelToUI.map(taTypeEvenement, nouveau);
					values= taTypeEvenementService.selectAllDTO();
					nouveau = values.get(0);
					nouveau = new TaTypeEvenementDTO();

					modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
				}
				else{
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "Code déja utilisé"));
				}
			}
			if(modeEcranDefaut!=null && modeEcranDefaut.equals(C_DIALOG)) {
				PrimeFaces.current().dialog().closeDynamic(taTypeEvenement);
			}
		} catch(Exception e) {
			e=ThrowableExceptionLgr.renvoieCauseRuntimeException(e);
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			//context.addMessage(null, new FacesMessage("Tiers", "actEnregistrer")); 
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "TypeCivilite", e.getMessage()));
		}
	}

	public void actAnnuler(ActionEvent actionEvent){
		if(values.size()>= 1){
			selection = values.get(0);
		}		
		nouveau = new TaTypeEvenementDTO();
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}

	public List<TaTypeEvenementDTO> getValues(){  
		return values;
	}

	public TaTypeEvenementDTO getNouveau() {
		return nouveau;
	}

	public void setNouveau(TaTypeEvenementDTO newTaTypeEvenementDTO) {
		this.nouveau = newTaTypeEvenementDTO;
	}

	public TaTypeEvenementDTO getSelection() {
		return selection;
	}

	public void setSelection(TaTypeEvenementDTO selectedTaTypeEvenementDTO) {
		this.selection = selectedTaTypeEvenementDTO;
	}

	public void setValues(List<TaTypeEvenementDTO> values) {
		this.values = values;
	}

	public void onRowSelect(SelectEvent event) {  
		if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_CONSULTATION)==0) {
			nouveau = selection;
		}
	}

	public List<TaTypeEvenementDTO> getFilteredValues() {
		return filteredValues;
	}

	public void setFilteredValues(List<TaTypeEvenementDTO> filteredValues) {
		this.filteredValues = filteredValues;
	}
	
	public void refresh(){
		values = taTypeEvenementService.selectAllDTO();
		Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		modeEcranDefaut = params.get("modeEcranDefaut");
		if(modeEcranDefaut!=null && modeEcranDefaut.equals(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION))) {
			modeEcranDefaut = C_DIALOG;
			actInserer(null);
		} else if(modeEcranDefaut!=null && modeEcranDefaut.equals(modeEcran.modeString(EnumModeObjet.C_MO_EDITION))) {
			modeEcranDefaut = C_DIALOG;
			if(params.get("idEntity")!=null) {
				try {
					selection = taTypeEvenementService.findByIdDTO(LibConversion.stringToInteger(params.get("idEntity")));
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
//			String msg = "";
//			try {
//				String nomChamp =  (String) component.getAttributes().get("nomChamp");
//				validateUIField(nomChamp,value);
//				TaTypeEvenementDTO temp=new TaTypeEvenementDTO();
//				PropertyUtils.setProperty(temp, nomChamp, value);
//				taTEmailService.validateDTOProperty(temp, nomChamp, ITaTypeEvenementServiceRemote.validationContext );
//			} catch(Exception e) {
//				msg += e.getMessage();
//				throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
//			}
			String messageComplet = "";
			try {
				String nomChamp =  (String) component.getAttributes().get("nomChamp");
				//validation automatique via la JSR bean validation
				ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
				Set<ConstraintViolation<TaTypeEvenementDTO>> violations = factory.getValidator().validateValue(TaTypeEvenementDTO.class,nomChamp,value);
				if (violations.size() > 0) {
					messageComplet = "Erreur de validation : ";
					for (ConstraintViolation<TaTypeEvenementDTO> cv : violations) {
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
//				if(nomChamp.equals(Const.C_CODE_T_EMAIL)) {
//						boolean changement=false;
//						if(selection.getCodeTEmail()!=null && value!=null && !selection.getCodeTEmail().equals(""))
//						{
//							if(value instanceof TaTypeEvenement)
//								changement=((TaTypeEvenement) value).getCodeTEmail().equals(selection.getCodeTEmail());
//							else if(value instanceof String)
//							changement=!value.equals(selection.getCodeTEmail());
//						}
//						if(changement && modeEcran.dataSetEnModeModification()){
//							FacesContext context = FacesContext.getCurrentInstance();  
//							context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Type d'email", Const.C_MESSAGE_CHANGEMENT_CODE));
//						}
//					}
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
	        
	        PrimeFaces.current().dialog().openDynamic("agenda/dialog_type_evenement", options, params);
	        
		}
		
		public void handleReturnDialogTypes(SelectEvent event) {
			if(event!=null && event.getObject()!=null) {
				taTypeEvenement = (TaTypeEvenement) event.getObject();
				
			}
			refresh();
		}
		
		public void actDialogModifier(ActionEvent actionEvent){
			
			nouveau = selection;
//			modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
			
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
	        
	        PrimeFaces.current().dialog().openDynamic("agenda/dialog_type_evenement", options, params);

		}
		
		public List<TaCategorieEvenement> categorieEvenementAutoComplete(String query) {
	        List<TaCategorieEvenement> allValues = taCategorieEvenementService.selectAll();
	        List<TaCategorieEvenement> filteredValues = new ArrayList<TaCategorieEvenement>();
	        
	        TaCategorieEvenement civ = new TaCategorieEvenement();
	        civ.setCodeCategorieEvenement(Const.C_AUCUN);
	        filteredValues.add(civ);
	        for (int i = 0; i < allValues.size(); i++) {
	        	 civ = allValues.get(i);
	            if(query.equals("*") || civ.getCodeCategorieEvenement().toLowerCase().contains(query.toLowerCase())) {
	                filteredValues.add(civ);
	            }
	        }
	        return filteredValues;
	    }

		public TaCategorieEvenement getTaCategorieEvenement() {
			return taCategorieEvenement;
		}

		public void setTaCategorieEvenement(TaCategorieEvenement taCategorieEvenement) {
			this.taCategorieEvenement = taCategorieEvenement;
		}
		
		public void autcompleteSelection(SelectEvent event) {
			Object value = event.getObject();
			
			String nomChamp =  (String) event.getComponent().getAttributes().get("nomChamp");

			validateUIField(nomChamp,value);
		}
}  
