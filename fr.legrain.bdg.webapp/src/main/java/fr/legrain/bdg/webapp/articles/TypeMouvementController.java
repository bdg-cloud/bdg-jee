package fr.legrain.bdg.webapp.articles;


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
import javax.inject.Named;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

import fr.legrain.article.dto.TaTypeMouvementDTO;
import fr.legrain.article.model.TaTypeMouvement;
import fr.legrain.bdg.article.service.remote.ITaTypeMouvementServiceRemote;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.tiers.service.remote.ITaTCiviliteServiceRemote;
import fr.legrain.bdg.webapp.ConstWeb;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.ModeObjetEcranServeur;



@Named
@ViewScoped  
public class TypeMouvementController implements Serializable {  

	private List<TaTypeMouvementDTO> values; 
	private List<TaTypeMouvementDTO> filteredValues; 
	private TaTypeMouvementDTO nouveau ;
	private TaTypeMouvementDTO selection ;
	
	private TaTypeMouvement taTypeMouvement = new TaTypeMouvement();
	
	private String modeEcranDefaut;
	private static final String C_DIALOG = "dialog";

	private ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();

	private @EJB ITaTypeMouvementServiceRemote taTypeMouvementService;
	
	private LgrDozerMapper<TaTypeMouvementDTO,TaTypeMouvement> mapperUIToModel  = new LgrDozerMapper<TaTypeMouvementDTO,TaTypeMouvement>();
	private LgrDozerMapper<TaTypeMouvement,TaTypeMouvementDTO> mapperModelToUI  = new LgrDozerMapper<TaTypeMouvement,TaTypeMouvementDTO>();

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
	
	public TypeMouvementController() {  
	}
	
	public void refresh(){
		values = taTypeMouvementService.selectAllDTO();
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
		}		
		nouveau = new TaTypeMouvementDTO();

		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}
	
	public void actEnregistrer(ActionEvent actionEvent){
		try {
			TaTypeMouvementDTO retour = null;
			taTypeMouvement=new TaTypeMouvement();
			if(nouveau.getId()==null || taTypeMouvementService.findById(nouveau.getId()) == null){
				mapperUIToModel.map(nouveau, taTypeMouvement);
				taTypeMouvement = taTypeMouvementService.merge(taTypeMouvement, ITaTCiviliteServiceRemote.validationContext);
				mapperModelToUI.map(taTypeMouvement, nouveau);
				values= taTypeMouvementService.selectAllDTO();
				nouveau = values.get(0);
				nouveau = new TaTypeMouvementDTO();

				modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

			}else{
				if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_EDITION)==0){
					taTypeMouvement = taTypeMouvementService.findById(nouveau.getId());
					mapperUIToModel.map(nouveau, taTypeMouvement);
					taTypeMouvement = taTypeMouvementService.merge(taTypeMouvement, ITaTCiviliteServiceRemote.validationContext);
					mapperModelToUI.map(taTypeMouvement, nouveau);
					values= taTypeMouvementService.selectAllDTO();
					nouveau = values.get(0);
					nouveau = new TaTypeMouvementDTO();

					modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
				}
				else{
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "Code déja utilisé"));
				}
			}
			if(modeEcranDefaut!=null && modeEcranDefaut.equals(C_DIALOG)) {
				PrimeFaces.current().dialog().closeDynamic(taTypeMouvement);
			}
		} catch(Exception e) {
			e=ThrowableExceptionLgr.renvoieCauseRuntimeException(e);
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Type mouvement", e.getMessage()));
		}
	}

	public void actInserer(ActionEvent actionEvent) {
		nouveau = new TaTypeMouvementDTO();

		modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
	}

	public void actModifier(ActionEvent actionEvent){
		nouveau = selection;
		
		modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
	}

	public void actSupprimer(){
		TaTypeMouvement taTypeMouvement = new TaTypeMouvement();
		try {
			if(selection!=null && selection.getId()!=null){
				taTypeMouvement = taTypeMouvementService.findById(selection.getId());
			}

			taTypeMouvementService.remove(taTypeMouvement);
			values.remove(selection);
			
			if(!values.isEmpty()) {
				selection = values.get(0);
			}else {
				selection=new TaTypeMouvementDTO();
			}

			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("type de mouvement", "actSupprimer"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "type de mouvement", e.getCause().getCause().getCause().getCause().getMessage()));
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

	public List<TaTypeMouvementDTO> getValues(){  
		return values;
	}

	public TaTypeMouvementDTO getNouveau() {
		return nouveau;
	}

	public void setNouveau(TaTypeMouvementDTO newTaTypeMouvement) {
		this.nouveau = newTaTypeMouvement;
	}

	public TaTypeMouvementDTO getSelection() {
		return selection;
	}

	public void setSelection(TaTypeMouvementDTO selectedTaTypeMouvement) {
		this.selection = selectedTaTypeMouvement;
	}

	public void setValues(List<TaTypeMouvementDTO> values) {
		this.values = values;
	}

	public List<TaTypeMouvementDTO> getFilteredValues() {
		return filteredValues;
	}
	
	public void setFilteredValues(List<TaTypeMouvementDTO> filteredValues) {
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
//			TaTypeMouvementDTO temp=new TaTypeMouvementDTO();
//			PropertyUtils.setProperty(temp, nomChamp, value);
//			taTypeMouvementService.validateEntityProperty(temp, nomChamp, ITaTypeMouvementServiceRemote.validationContext );
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
			Set<ConstraintViolation<TaTypeMouvementDTO>> violations = factory.getValidator().validateValue(TaTypeMouvementDTO.class,nomChamp,value);
			if (violations.size() > 0) {
				messageComplet = "Erreur de validation : ";
				for (ConstraintViolation<TaTypeMouvementDTO> cv : violations) {
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
						if(value instanceof TaTypeMouvement)
							changement=((TaTypeMouvement) value).getCode().equals(selection.getCode());
						else if(value instanceof String)
						changement=!value.equals(selection.getCode());
					}
					if(changement && modeEcran.dataSetEnModeModification()){
						FacesContext context = FacesContext.getCurrentInstance();  
						context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Type de mouvement", Const.C_MESSAGE_CHANGEMENT_CODE));
					}
				}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}			
	
}  
