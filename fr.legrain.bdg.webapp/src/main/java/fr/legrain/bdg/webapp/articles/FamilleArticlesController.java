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

import fr.legrain.article.dto.TaFamilleDTO;
import fr.legrain.article.model.TaFamille;
import fr.legrain.bdg.article.service.remote.ITaFamilleServiceRemote;
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
public class FamilleArticlesController implements Serializable {  

	private List<TaFamilleDTO> values; 
	private List<TaFamilleDTO> filteredValues; 
	private TaFamilleDTO nouveau ;
	private TaFamilleDTO selection ;

	private TaFamille taFamille = new TaFamille();

	private String modeEcranDefaut;
	private static final String C_DIALOG = "dialog";

	private ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();

	private @EJB ITaFamilleServiceRemote taFamilleArticlesService;
	
	private LgrDozerMapper<TaFamilleDTO,TaFamille> mapperUIToModel  = new LgrDozerMapper<TaFamilleDTO,TaFamille>();
	private LgrDozerMapper<TaFamille,TaFamilleDTO> mapperModelToUI  = new LgrDozerMapper<TaFamille,TaFamilleDTO>();

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

	public FamilleArticlesController() {  
	}  

	public void actInserer(ActionEvent actionEvent){
		nouveau = new TaFamilleDTO();
		modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
	}

	public void actModifier(ActionEvent actionEvent){
		nouveau = selection;
		modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
	}

	public void actSupprimer(ActionEvent actionEvent){
		TaFamille taFamille = new TaFamille();
		try {
			if(selection!=null && selection.getId()!=null){
				taFamille = taFamilleArticlesService.findById(selection.getId());
			}

			taFamilleArticlesService.remove(taFamille);
			values.remove(selection);
			
			if(!values.isEmpty()) {
				selection = values.get(0);
			}else {
				selection=new TaFamilleDTO();
			}

			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Famille d'article", "actSupprimer"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Famille d'article", e.getCause().getCause().getCause().getCause().getMessage()));
		}
	}

	public void actEnregistrer(ActionEvent actionEvent){
		try {
			TaFamilleDTO retour = null;
			taFamille=new TaFamille();
			if(nouveau.getId()==null || taFamilleArticlesService.findById(nouveau.getId()) == null){
				mapperUIToModel.map(nouveau, taFamille);
				taFamille = taFamilleArticlesService.merge(taFamille, ITaTCiviliteServiceRemote.validationContext);
				mapperModelToUI.map(taFamille, nouveau);
				values= taFamilleArticlesService.selectAllDTO();
				nouveau = values.get(0);
				nouveau = new TaFamilleDTO();

				modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

			}else{
				if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_EDITION)==0){
					taFamille = taFamilleArticlesService.findById(nouveau.getId());
					mapperUIToModel.map(nouveau, taFamille);
					taFamille = taFamilleArticlesService.merge(taFamille, ITaTCiviliteServiceRemote.validationContext);
					mapperModelToUI.map(taFamille, nouveau);
					values= taFamilleArticlesService.selectAllDTO();
					nouveau = values.get(0);
					nouveau = new TaFamilleDTO();

					modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
				}
				else{
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "Code déja utilisé"));
				}
			}
			if(modeEcranDefaut!=null && modeEcranDefaut.equals(C_DIALOG)) {
				PrimeFaces.current().dialog().closeDynamic(taFamille);
			}
		} catch(Exception e) {
			e=ThrowableExceptionLgr.renvoieCauseRuntimeException(e);
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Famille d'article", e.getMessage()));
		}
	}

	public void actAnnuler(ActionEvent actionEvent){
		if(values.size()>= 1){
			selection = values.get(0);
		}		
		nouveau = new TaFamilleDTO();

		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}

	public List<TaFamilleDTO> getValues(){  
		return values;
	}

	public TaFamilleDTO getNouveau() {
		return nouveau;
	}

	public void setNouveau(TaFamilleDTO newTaFamille) {
		this.nouveau = newTaFamille;
	}

	public TaFamilleDTO getSelection() {
		return selection;
	}

	public void setSelection(TaFamilleDTO selectedTaFamille) {
		this.selection = selectedTaFamille;
	}

	public void setValues(List<TaFamilleDTO> values) {
		this.values = values;
	}

	public void onRowSelect(SelectEvent event) {  
		if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_CONSULTATION)==0) {
			nouveau = selection;
		}
	}

	public List<TaFamilleDTO> getFilteredValues() {
		return filteredValues;
	}

	public void setFilteredValues(List<TaFamilleDTO> filteredValues) {
		this.filteredValues = filteredValues;
	}

	public void refresh(){
		values = taFamilleArticlesService.selectAllDTO();
		Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		modeEcranDefaut = params.get("modeEcranDefaut");
		if(modeEcranDefaut!=null && modeEcranDefaut.equals(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION))) {
			modeEcranDefaut = C_DIALOG;
			actInserer(null);
		} else if(modeEcranDefaut!=null && modeEcranDefaut.equals(modeEcran.modeString(EnumModeObjet.C_MO_EDITION))) {
			modeEcranDefaut = C_DIALOG;
			if(params.get("idEntity")!=null) {
				try {
					selection = taFamilleArticlesService.findByIdDTO(LibConversion.stringToInteger(params.get("idEntity")));
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
//		String msg = "";
//		try {
//			String nomChamp =  (String) component.getAttributes().get("nomChamp");
//			validateUIField(nomChamp,value);
//			TaFamilleDTO temp=new TaFamilleDTO();
//			PropertyUtils.setProperty(temp, nomChamp, value);
//			taTFamilleArticlesService.validateEntityProperty(temp, nomChamp, ITaFamilleServiceRemote.validationContext );
//		} catch(Exception e) {
//			msg += e.getMessage();
//			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
//		}
		
		String messageComplet = "";
		try {
			String nomChamp =  (String) component.getAttributes().get("nomChamp");
			//validation automatique via la JSR bean validation
			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
			Set<ConstraintViolation<TaFamilleDTO>> violations = factory.getValidator().validateValue(TaFamilleDTO.class,nomChamp,value);
			if (violations.size() > 0) {
				messageComplet = "Erreur de validation : ";
				for (ConstraintViolation<TaFamilleDTO> cv : violations) {
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
			if(nomChamp.equals(Const.C_CODE_FAMILLE)) {
					boolean changement=false;
					if(selection.getCodeFamille()!=null && value!=null && !selection.getCodeFamille().equals(""))
					{
						if(value instanceof TaFamille)
							changement=((TaFamille) value).getCodeFamille().equals(selection.getCodeFamille());
						else if(value instanceof String)
						changement=!value.equals(selection.getCodeFamille());
					}
					if(changement && modeEcran.dataSetEnModeModification()){
						FacesContext context = FacesContext.getCurrentInstance();  
						context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Famille d'article", Const.C_MESSAGE_CHANGEMENT_CODE));
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

		PrimeFaces.current().dialog().openDynamic("articles/dialog_type_famille_article", options, params);

	}

	public void handleReturnDialogTypes(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			taFamille = (TaFamille) event.getObject();
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

		PrimeFaces.current().dialog().openDynamic("articles/dialog_type_famille_article", options, params);

	}
	// Dima -  Fin
}  
