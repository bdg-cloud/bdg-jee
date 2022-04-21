package fr.legrain.bdg.webapp.articles;

import java.io.Serializable;
import java.util.ArrayList;
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

import fr.legrain.article.dto.TaUniteDTO;
import fr.legrain.article.model.TaArticle;
import fr.legrain.article.model.TaUnite;
import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaUniteServiceRemote;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.ModeObjetEcranServeur;

@Named
@ViewScoped  
public class ChangementUniteReferenceController implements Serializable {  

	private List<TaUniteDTO> values; 
	private List<TaUniteDTO> filteredValues; 
	private TaUniteDTO taUniteReferenceDTO ;
	private TaUniteDTO selection ;

	private TaUnite taUnite = new TaUnite();
	private TaArticle taArticle = new TaArticle();

	private String modeEcranDefaut;
	private static final String C_DIALOG = "dialog";

	private ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();

	private @EJB ITaUniteServiceRemote taUniteService;
	private @EJB ITaArticleServiceRemote taArticleService;
	
	private LgrDozerMapper<TaUniteDTO,TaUnite> mapperUIToModel  = new LgrDozerMapper<TaUniteDTO,TaUnite>();
	private LgrDozerMapper<TaUnite,TaUniteDTO> mapperModelToUI  = new LgrDozerMapper<TaUnite,TaUniteDTO>();

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

	public ChangementUniteReferenceController() {  
	}  

	public void actEnregistrer(ActionEvent actionEvent){
		try {
			if(!taUniteReferenceDTO.getCodeUnite().equals(taArticle.getTaUniteReference().getCodeUnite())) {
				taUnite = taUniteService.findById(taUniteReferenceDTO.getId());
				PrimeFaces.current().dialog().closeDynamic(taUnite);
//				PrimeFaces.current().dialog().closeDynamic(taUnite);
			} else {
				PrimeFaces.current().dialog().closeDynamic(null);
			}
		} catch(Exception e) {
			e=ThrowableExceptionLgr.renvoieCauseRuntimeException(e);
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "TypeCodeTVA", e.getMessage()));
		}
	}

	public List<TaUniteDTO> getValues(){  
		return values;
	}
	
	public List<TaUniteDTO> uniteAutoCompleteLight(String query) {
        //List<TaUniteDTO> allValues = taUniteService.findByCodeLight(query.toUpperCase());
		List<TaUniteDTO> allValues = taUniteService.findByCodeLightUniteStock(taArticle.getTaUniteReference().getCodeUnite(), query.toUpperCase());
        List<TaUniteDTO> filteredValues = new ArrayList<TaUniteDTO>();
        TaUniteDTO  civ = new TaUniteDTO();
        civ.setCodeUnite(Const.C_AUCUN);
//        filteredValues.add(civ);
        for (int i = 0; i < allValues.size(); i++) {
        	 civ = allValues.get(i);
            if(query.equals("*") || civ.getCodeUnite().toLowerCase().contains(query.toLowerCase())) {
                filteredValues.add(civ);
            }
        }
        return filteredValues;
    }

	public TaUniteDTO getSelection() {
		return selection;
	}

	public void setSelection(TaUniteDTO selectedTaUnite) {
		this.selection = selectedTaUnite;
	}

	public void setValues(List<TaUniteDTO> values) {
		this.values = values;
	}

	public List<TaUniteDTO> getFilteredValues() {
		return filteredValues;
	}

	public void setFilteredValues(List<TaUniteDTO> filteredValues) {
		this.filteredValues = filteredValues;
	}

	public void refresh(){
		values = taUniteService.selectAllDTO();
		Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		Integer idArticle = LibConversion.stringToInteger(params.get("idArticle"));
		if(params.get("idArticle")!=null) {
			try {
				taArticle = taArticleService.findById(LibConversion.stringToInteger(params.get("idArticle")));
				taUniteReferenceDTO = taUniteService.findByIdDTO(taArticle.getTaUniteReference().getIdUnite());
			} catch (FinderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void actFermerDialog(ActionEvent actionEvent) {
		//PrimeFaces.current().dialog().closeDynamic(null);
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


	public void validateObject(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		
		String messageComplet = "";
		try {
			String nomChamp =  (String) component.getAttributes().get("nomChamp");
			//validation automatique via la JSR bean validation
			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
			Set<ConstraintViolation<TaUniteDTO>> violations = factory.getValidator().validateValue(TaUniteDTO.class,nomChamp,value);
			if (violations.size() > 0) {
				messageComplet = "Erreur de validation : ";
				for (ConstraintViolation<TaUniteDTO> cv : violations) {
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
			if(nomChamp.equals(Const.C_CODE_UNITE)) {
					boolean changement=false;
					if(selection.getCodeUnite()!=null && value!=null && !selection.getCodeUnite().equals(""))
					{
						if(value instanceof TaUnite)
							changement=((TaUnite) value).getCodeUnite().equals(selection.getCodeUnite());
						else if(value instanceof String)
						changement=!value.equals(selection.getCodeUnite());
					}
					if(changement && modeEcran.dataSetEnModeModification()){
						FacesContext context = FacesContext.getCurrentInstance();  
						context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Code unité", Const.C_MESSAGE_CHANGEMENT_CODE));
					}
				}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public TaUniteDTO getTaUniteReferenceDTO() {
		return taUniteReferenceDTO;
	}

	public void setTaUniteReferenceDTO(TaUniteDTO taUniteReferenceDTO) {
		this.taUniteReferenceDTO = taUniteReferenceDTO;
	}

	public TaArticle getTaArticle() {
		return taArticle;
	}

	public void setTaArticle(TaArticle taArticle) {
		this.taArticle = taArticle;
	}

	
}  
