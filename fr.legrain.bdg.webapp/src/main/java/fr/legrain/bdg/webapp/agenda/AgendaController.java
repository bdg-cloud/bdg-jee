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
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

import fr.legrain.bdg.droits.service.remote.ITaUtilisateurServiceRemote;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.tache.service.remote.ITaAgendaServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTCiviliteServiceRemote;
import fr.legrain.bdg.webapp.ConstWeb;
import fr.legrain.droits.service.SessionInfo;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.ModeObjetEcranServeur;
import fr.legrain.tache.dto.TaAgendaDTO;
import fr.legrain.tache.model.TaAgenda;



@Named
@ViewScoped  
public class AgendaController implements Serializable {  

	private List<TaAgendaDTO> values; 
	private List<TaAgendaDTO> filteredValues; 
	private TaAgendaDTO nouveau ;
	private TaAgendaDTO selection ;

	private TaAgenda taAgenda = new TaAgenda();
	
	private String modeEcranDefaut;
	private static final String C_DIALOG = "dialog";

	private ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();

	private @EJB ITaAgendaServiceRemote taAgendaService;
	private @EJB ITaUtilisateurServiceRemote taUtilisateurService;
	
	@Inject private	SessionInfo sessionInfo;
	
	private LgrDozerMapper<TaAgendaDTO,TaAgenda> mapperUIToModel  = new LgrDozerMapper<TaAgendaDTO,TaAgenda>();
	private LgrDozerMapper<TaAgenda,TaAgendaDTO> mapperModelToUI  = new LgrDozerMapper<TaAgenda,TaAgendaDTO>();
	
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

	public AgendaController(){  
	}  

	public void actInserer(ActionEvent actionEvent){
		nouveau = new TaAgendaDTO();
		modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
	}

	public void actModifier(ActionEvent actionEvent){
		nouveau = selection;
		modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
	}

	public void actSupprimer(){
		TaAgenda taTiers = new TaAgenda();
		try {
			if(selection!=null && selection.getId()!=null){
				taTiers = taAgendaService.findById(selection.getId());
			}

			taAgendaService.remove(taTiers);
			values.remove(selection);
			
			if(!values.isEmpty()) {
				selection = values.get(0);
			}else {
				selection=new TaAgendaDTO();
			}

			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Agenda", "actSupprimer"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Agenda", e.getCause().getCause().getCause().getCause().getMessage()));
		}
	}
	
	public void actEnregistrer(ActionEvent actionEvent){
		try {
			TaAgendaDTO retour = null;
			taAgenda=new TaAgenda();
			if(nouveau.getId()==null || taAgendaService.findById(nouveau.getId()) == null){
				mapperUIToModel.map(nouveau, taAgenda);
				taAgenda.setProprietaire(taUtilisateurService.findById(sessionInfo.getUtilisateur().getId()));
				taAgenda = taAgendaService.merge(taAgenda, ITaTCiviliteServiceRemote.validationContext);
				mapperModelToUI.map(taAgenda, nouveau);
				values= taAgendaService.selectAllDTO();
				nouveau = values.get(0);
				nouveau = new TaAgendaDTO();

				modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

			}else{
				if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_EDITION)==0){
					taAgenda = taAgendaService.findById(nouveau.getId());
					mapperUIToModel.map(nouveau, taAgenda);
					taAgenda = taAgendaService.merge(taAgenda, ITaTCiviliteServiceRemote.validationContext);
					mapperModelToUI.map(taAgenda, nouveau);
					values= taAgendaService.selectAllDTO();
					nouveau = values.get(0);
					nouveau = new TaAgendaDTO();

					modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
				}
				else{
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "Code déja utilisé"));
				}
			}
			if(modeEcranDefaut!=null && modeEcranDefaut.equals(C_DIALOG)) {
				PrimeFaces.current().dialog().closeDynamic(taAgenda);
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
		nouveau = new TaAgendaDTO();
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}

	public List<TaAgendaDTO> getValues(){  
		return values;
	}

	public TaAgendaDTO getNouveau() {
		return nouveau;
	}

	public void setNouveau(TaAgendaDTO newTaAgendaDTO) {
		this.nouveau = newTaAgendaDTO;
	}

	public TaAgendaDTO getSelection() {
		return selection;
	}

	public void setSelection(TaAgendaDTO selectedTaAgendaDTO) {
		this.selection = selectedTaAgendaDTO;
	}

	public void setValues(List<TaAgendaDTO> values) {
		this.values = values;
	}

	public void onRowSelect(SelectEvent event) {  
		if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_CONSULTATION)==0) {
			nouveau = selection;
		}
	}

	public List<TaAgendaDTO> getFilteredValues() {
		return filteredValues;
	}

	public void setFilteredValues(List<TaAgendaDTO> filteredValues) {
		this.filteredValues = filteredValues;
	}
	
	public void refresh(){
		values = taAgendaService.selectAllDTO();
		//taAgendaService.findAgendaUtilisateur(sessionInfo.getUtilisateur());
		Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		modeEcranDefaut = params.get("modeEcranDefaut");
		if(modeEcranDefaut!=null && modeEcranDefaut.equals(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION))) {
			modeEcranDefaut = C_DIALOG;
			actInserer(null);
		} else if(modeEcranDefaut!=null && modeEcranDefaut.equals(modeEcran.modeString(EnumModeObjet.C_MO_EDITION))) {
			modeEcranDefaut = C_DIALOG;
			if(params.get("idEntity")!=null) {
				try {
					selection = taAgendaService.findByIdDTO(LibConversion.stringToInteger(params.get("idEntity")));
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
//				TaAgendaDTO temp=new TaAgendaDTO();
//				PropertyUtils.setProperty(temp, nomChamp, value);
//				taTEmailService.validateDTOProperty(temp, nomChamp, ITaAgendaServiceRemote.validationContext );
//			} catch(Exception e) {
//				msg += e.getMessage();
//				throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
//			}
			String messageComplet = "";
			try {
				String nomChamp =  (String) component.getAttributes().get("nomChamp");
				//validation automatique via la JSR bean validation
				ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
				Set<ConstraintViolation<TaAgendaDTO>> violations = factory.getValidator().validateValue(TaAgendaDTO.class,nomChamp,value);
				if (violations.size() > 0) {
					messageComplet = "Erreur de validation : ";
					for (ConstraintViolation<TaAgendaDTO> cv : violations) {
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
//							if(value instanceof TaAgenda)
//								changement=((TaAgenda) value).getCodeTEmail().equals(selection.getCodeTEmail());
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
	        
	        PrimeFaces.current().dialog().openDynamic("agenda/dialog_agenda", options, params);
	        
		}
		
		public void handleReturnDialogTypes(SelectEvent event) {
			if(event!=null && event.getObject()!=null) {
				taAgenda = (TaAgenda) event.getObject();
				
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
	        
	        PrimeFaces.current().dialog().openDynamic("agenda/dialog_agenda", options, params);

		}
		// Dima -  Fin
}  
