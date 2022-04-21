package fr.legrain.bdg.webapp.solstyce;

import java.io.Serializable;
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

import fr.legrain.article.dto.TaCodeBarreDTO;
import fr.legrain.article.model.TaCodeBarre;
import fr.legrain.article.model.TaTypeCodeBarre;
import fr.legrain.bdg.article.service.remote.ITaCodeBarreServiceRemote;
import fr.legrain.bdg.article.service.remote.ITypeCodeBarreServiceRemote;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.tiers.service.remote.ITaTCiviliteServiceRemote;
import fr.legrain.bdg.webapp.ConstWeb;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.ModeObjetEcranServeur;

@Named
@ViewScoped  
public class CodeBarreController implements Serializable {  

	private List<TaCodeBarreDTO> values; 
	private List<TaCodeBarreDTO> filteredValues; 
	
	private TaCodeBarreDTO nouveau ;
	private TaCodeBarreDTO selection ;
	
	private TaCodeBarre taCodeBarre; 
	
	private String modeEcranDefaut;
	private static final String C_DIALOG = "dialog";
	
	private ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();
	
	private @EJB ITaCodeBarreServiceRemote taCodeBarreService;
	private @EJB ITypeCodeBarreServiceRemote taTypeCodeBarreService;
	
	private LgrDozerMapper<TaCodeBarreDTO,TaCodeBarre> mapperUIToModel  = new LgrDozerMapper<TaCodeBarreDTO,TaCodeBarre>();
	private LgrDozerMapper<TaCodeBarre,TaCodeBarreDTO> mapperModelToUI  = new LgrDozerMapper<TaCodeBarre,TaCodeBarreDTO>();
	
	@PostConstruct
	public void postConstruct(){
		try {
			refresh();
			values =  taCodeBarreService.selectAllDTO();
			if(values == null){
			}
			if(values.size()>0)	selection = values.get(0);	
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		this.setFilteredValues(values);
	}
	

	public void autcompleteImageCodeBarre(SelectEvent event) {

	}
	

	public CodeBarreController() {  
	}  
	
	public void refresh(){
		values = taCodeBarreService.selectAllDTO();
		Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		modeEcranDefaut = params.get("modeEcranDefaut");

		if(modeEcranDefaut!=null && modeEcranDefaut.equals(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION))) {
			modeEcranDefaut = C_DIALOG;
			actInserer(null);
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
			 }		nouveau = new TaCodeBarreDTO();
			 modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}
	
	public void actEnregistrer(ActionEvent actionEvent){

		try {
			TaCodeBarreDTO retour = null;
			taCodeBarre=new TaCodeBarre();
			if(nouveau.getId()==null || taTypeCodeBarreService.findById(nouveau.getId()) == null){
				taCodeBarre=new TaCodeBarre();
				mapperUIToModel.map(nouveau, taCodeBarre);
				taCodeBarre = taCodeBarreService.merge(taCodeBarre, ITaTCiviliteServiceRemote.validationContext);
				mapperModelToUI.map(taCodeBarre, nouveau);
				values= taCodeBarreService.selectAllDTO();
				nouveau = values.get(0);
				nouveau = new TaCodeBarreDTO();

				modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

			}else{
				if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_EDITION)==0){
					taCodeBarre = taCodeBarreService.findById(nouveau.getId());
					mapperUIToModel.map(nouveau, taCodeBarre);
					taCodeBarre = taCodeBarreService.merge(taCodeBarre, ITaTCiviliteServiceRemote.validationContext);
					mapperModelToUI.map(taCodeBarre, nouveau);
					values= taCodeBarreService.selectAllDTO();
					nouveau = values.get(0);
					nouveau = new TaCodeBarreDTO();

					modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
				}
				else{
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "Code déja utilisé"));
				}
			}
			if(modeEcranDefaut!=null && modeEcranDefaut.equals(C_DIALOG)) {
				PrimeFaces.current().dialog().closeDynamic(taCodeBarre);
			}
		} catch(Exception e) {
			e=ThrowableExceptionLgr.renvoieCauseRuntimeException(e);
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Code barre", e.getMessage()));
		}
	}
	
	
        public void actInserer(ActionEvent actionEvent)
        {
        		
        	nouveau = new TaCodeBarreDTO();
        	taCodeBarre = new TaCodeBarre();
        	try {
				taCodeBarre.setTaTypeCodeBarre(taTypeCodeBarreService.findByCode("EAN128"));
			} catch (FinderException e) {
				
			}
        	modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
        
        }
        
        public void actModifier(ActionEvent actionEvent){
    
		nouveau = selection;
		modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
	}

	public void actSupprimer(){
		TaCodeBarre taCodeBarre = new TaCodeBarre();
		try {
			if(selection!=null && selection.getId()!=null){
				taCodeBarre = taCodeBarreService.findById(selection.getId());
			}

			taCodeBarreService.remove(taCodeBarre);
			values.remove(selection);
			
			if(!values.isEmpty()) {
				selection = values.get(0);
			}else {
				selection=new TaCodeBarreDTO();
			}

			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("code barre", "actSupprimer"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "code barre", e.getCause().getCause().getCause().getCause().getMessage()));
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

	public List<TaCodeBarreDTO> getValues(){  
		//List<Value> l = new LinkedList<Value>();
		return values;
	}

	public TaCodeBarreDTO getNouveau() {
		return nouveau;
	}

	public void setNouveau(TaCodeBarreDTO newTaCodeBarre) {
		this.nouveau = newTaCodeBarre;
	}

	public TaCodeBarreDTO getSelection() {
		return selection;
	}

	public void setSelection(TaCodeBarreDTO selectedTaCodeBarre) {
		this.selection = selectedTaCodeBarre;
	}

	public void setValues(List<TaCodeBarreDTO> values) {
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

	public ITaCodeBarreServiceRemote getTaCodeBarreService() {
		return taCodeBarreService;
	}

	public void setTaCodeBarreService(ITaCodeBarreServiceRemote TaCodeBarreService) {
		this.taCodeBarreService = TaCodeBarreService;
	}

	public static String getcDialog() {
		return C_DIALOG;
	}

	public List<TaCodeBarreDTO> getFilteredValues() {
	    return filteredValues;
	}

	public void setFilteredValues(List<TaCodeBarreDTO> filteredValues) {
	    this.filteredValues = filteredValues;
	}

	public boolean validateUIField(String nomChamp,Object value) {
		
		boolean changement=false;
		
		try {
			if(nomChamp.equals(Const.C_CODE_TYPE_CODE_BARRE)) {
				TaTypeCodeBarre entity = new TaTypeCodeBarre();
				if(value!=null){
					if(value instanceof TaTypeCodeBarre){
						entity=(TaTypeCodeBarre)value;
					}else{
						entity = taTypeCodeBarreService.findByCode((String)value);
					}				
				
				taCodeBarre.setTaTypeCodeBarre(entity);
				}
		}
			if(nomChamp.equals(Const.C_CODE_BARRE)) {
				if(selection.getCodeBarre()!=null && value!=null && !selection.getCodeBarre().equals(""))
				{
					if(value instanceof TaCodeBarre)
						changement=((TaCodeBarre) value).getCodeBarre().equals(selection.getCodeBarre());
					else if(value instanceof String)
					changement=!value.equals(selection.getCodeBarre());
				}
				if(changement && modeEcran.dataSetEnModeModification()){
					FacesContext context = FacesContext.getCurrentInstance();  
					context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Code barre", Const.C_MESSAGE_CHANGEMENT_CODE));
				}
			}			
			return false;

		} catch (Exception e) {
			
		}
		return false;
	}			
		

	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		
//		String msg = "";
//		
//		try {
//		 
//		  String nomChamp =  (String) component.getAttributes().get("nomChamp");
//		  
//		  validateUIField(nomChamp,value);
////		  TaCodeBarreService.validateDTOProperty(selection, nomChamp, ITaCodeBarreServiceRemote.validationContext );
//
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
			Set<ConstraintViolation<TaCodeBarreDTO>> violations = factory.getValidator().validateValue(TaCodeBarreDTO.class,nomChamp,value);
			if (violations.size() > 0) {
				messageComplet = "Erreur de validation : ";
				for (ConstraintViolation<TaCodeBarreDTO> cv : violations) {
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

}  
