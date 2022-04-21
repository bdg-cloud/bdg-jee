package fr.legrain.bdg.webapp;


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

import fr.legrain.bdg.cron.service.remote.ITaCronServiceRemote;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.tiers.service.remote.ITaTCiviliteServiceRemote;
import fr.legrain.cron.model.TaCron;
import fr.legrain.cron.model.dto.TaCronDTO;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.ModeObjetEcranServeur;



@Named
@ViewScoped  
public class CronDossierController implements Serializable {  

	private List<TaCronDTO> values; 
	private List<TaCronDTO> filteredValues; 
	private TaCronDTO nouveau ;
	private TaCronDTO selection ;
	
	private TaCron taCron = new TaCron();
	
	private String modeEcranDefaut;
	private static final String C_DIALOG = "dialog";

	private ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();

	private @EJB ITaCronServiceRemote taCronServiceRemote;
	
	private LgrDozerMapper<TaCronDTO,TaCron> mapperUIToModel  = new LgrDozerMapper<TaCronDTO,TaCron>();
	private LgrDozerMapper<TaCron,TaCronDTO> mapperModelToUI  = new LgrDozerMapper<TaCron,TaCronDTO>();

	@PostConstruct
	public void postConstruct(){
		try {
			refresh();
			if(values == null){
			}
			selection = values.get(0);	
		} catch (Exception e) {
			e.printStackTrace();
		}

		this.setFilteredValues(values);
	}
	
	public CronDossierController() {  
	}
	
	public void refresh(){
		values = taCronServiceRemote.selectAllDTO();
		Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		modeEcranDefaut = params.get("modeEcranDefaut");
		if(modeEcranDefaut!=null && modeEcranDefaut.equals(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION))) {
			modeEcranDefaut = C_DIALOG;
			actInserer(null);
		} else if(modeEcranDefaut!=null && modeEcranDefaut.equals(modeEcran.modeString(EnumModeObjet.C_MO_EDITION))) {
			modeEcranDefaut = C_DIALOG;
			if(params.get("idEntity")!=null) {
				try {
					selection = taCronServiceRemote.findByIdDTO(LibConversion.stringToInteger(params.get("idEntity")));
				} catch (FinderException e) {
					e.printStackTrace();
				}
			}
			actModifier(null);
		} else {
			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
		}
		
	}

	public void verifierTimersCronDossier(ActionEvent actionEvent) {
		//création des crons "systèmes" s'il n'existe pas dans le dossier
		taCronServiceRemote.miseAJourCronSystemeDuDossier();
		
		//Vérification de l'état des timers correspondant aux différents crons
		List<TaCron> listeCron = taCronServiceRemote.selectAll(); //TODO récupérer uniquement les cron "système" ?
		for (TaCron taCron : listeCron) {
			if(taCron.isActif()) {
				//vérifier que le timer jee est bien actif en utilisant le handler
				taCronServiceRemote.activerCronSysteme(taCron.getCode(), null);
			} else {
				//vérifier que le timer jee est inactif en utilisant le handler
				taCronServiceRemote.desactiverCronSysteme(taCron.getCode());
			}
		}
	}
	
	public void actFermerDialog(ActionEvent actionEvent) {
		PrimeFaces.current().dialog().closeDynamic(null);
	}

	public void actAnnuler(ActionEvent actionEvent){
		if(values.size()>= 1){
			selection = values.get(0);
		}		
		nouveau = new TaCronDTO();

		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}
	
	public void actEnregistrer(ActionEvent actionEvent){
		try {
			TaCronDTO retour = null;
			taCron=new TaCron();
			if(nouveau.getId()==null || taCronServiceRemote.findById(nouveau.getId()) == null){
				mapperUIToModel.map(nouveau, taCron);
				taCron = taCronServiceRemote.merge(taCron, ITaTCiviliteServiceRemote.validationContext);
				mapperModelToUI.map(taCron, nouveau);
				values= taCronServiceRemote.selectAllDTO();
				nouveau = values.get(0);
				nouveau = new TaCronDTO();

				modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

			}else{
				if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_EDITION)==0){
					taCron = taCronServiceRemote.findById(nouveau.getId());
					mapperUIToModel.map(nouveau, taCron);
					taCron = taCronServiceRemote.merge(taCron, ITaTCiviliteServiceRemote.validationContext);
					mapperModelToUI.map(taCron, nouveau);
					values= taCronServiceRemote.selectAllDTO();
					nouveau = values.get(0);
					nouveau = new TaCronDTO();

					modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
				}
				else{
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "Code déja utilisé"));
				}
			}
			if(modeEcranDefaut!=null && modeEcranDefaut.equals(C_DIALOG)) {
				PrimeFaces.current().dialog().closeDynamic(taCron);
			}
		} catch(Exception e) {
			e=ThrowableExceptionLgr.renvoieCauseRuntimeException(e);
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Type mouvement", e.getMessage()));
		}
	}

	public void actInserer(ActionEvent actionEvent) {
		nouveau = new TaCronDTO();

		modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
	}

	public void actModifier(ActionEvent actionEvent){
		nouveau = selection;
		
		modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
	}

	public void actSupprimer(){
		TaCron taTypeMouvement = new TaCron();
		try {
			if(selection!=null && selection.getId()!=null){
				taTypeMouvement = taCronServiceRemote.findById(selection.getId());
			}

			taCronServiceRemote.remove(taTypeMouvement);
			values.remove(selection);
			
			if(!values.isEmpty()) {
				selection = values.get(0);
			}else {
				selection=new TaCronDTO();
			}

			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Cron dossier", "actSupprimer"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cron dossier", e.getCause().getCause().getCause().getCause().getMessage()));
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

	public List<TaCronDTO> getValues(){  
		return values;
	}

	public TaCronDTO getNouveau() {
		return nouveau;
	}

	public void setNouveau(TaCronDTO newTaCron) {
		this.nouveau = newTaCron;
	}

	public TaCronDTO getSelection() {
		return selection;
	}

	public void setSelection(TaCronDTO selectedTaCron) {
		this.selection = selectedTaCron;
	}

	public void setValues(List<TaCronDTO> values) {
		this.values = values;
	}

	public List<TaCronDTO> getFilteredValues() {
		return filteredValues;
	}
	
	public void setFilteredValues(List<TaCronDTO> filteredValues) {
		this.filteredValues = filteredValues;
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


	public void validateMouvement(FacesContext context, UIComponent component, Object value) throws ValidatorException {

//		String msg = "";
//
//		try {
//			String nomChamp =  (String) component.getAttributes().get("nomChamp");
//			validateUIField(nomChamp,value);
//			TaCronDTO temp=new TaCronDTO();
//			PropertyUtils.setProperty(temp, nomChamp, value);
//			taTypeMouvementService.validateEntityProperty(temp, nomChamp, ITaCronServiceRemote.validationContext );
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
			Set<ConstraintViolation<TaCronDTO>> violations = factory.getValidator().validateValue(TaCronDTO.class,nomChamp,value);
			if (violations.size() > 0) {
				messageComplet = "Erreur de validation : ";
				for (ConstraintViolation<TaCronDTO> cv : violations) {
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
			if(nomChamp.equals(Const.C_CODE_TYPE_MOUVEMENT)) {
					boolean changement=false;
					if(selection.getCode()!=null && value!=null && !selection.getCode().equals(""))
					{
						if(value instanceof TaCron)
							changement=((TaCron) value).getCode().equals(selection.getCode());
						else if(value instanceof String)
						changement=!value.equals(selection.getCode());
					}
					if(changement && modeEcran.dataSetEnModeModification()){
						FacesContext context = FacesContext.getCurrentInstance();  
						context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Cron dossier", Const.C_MESSAGE_CHANGEMENT_CODE));
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
        options.put("contentHeight", 420);
        options.put("modal", true);
        
        Map<String,List<String>> params = new HashMap<String,List<String>>();
        List<String> list = new ArrayList<String>();
        list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
        params.put("modeEcranDefaut", list);
        
        PrimeFaces.current().dialog().openDynamic("abonnement/dialog_cron_dossier", options, params);
        
	}
	
	public void handleReturnDialogTypes(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			taCron = (TaCron) event.getObject();
			
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
        options.put("contentHeight", 420);
        options.put("modal", true);
        
        Map<String,List<String>> params = new HashMap<String,List<String>>();
        List<String> list = new ArrayList<String>();
        list.add(modeEcran.modeString(EnumModeObjet.C_MO_EDITION));
        params.put("modeEcranDefaut", list);
        List<String> list2 = new ArrayList<String>();
        list2.add(LibConversion.integerToString(selection.getId()));
        params.put("idEntity", list2);
        
        PrimeFaces.current().dialog().openDynamic("abonnement/dialog_cron_dossier", options, params);

	}
	
}  
