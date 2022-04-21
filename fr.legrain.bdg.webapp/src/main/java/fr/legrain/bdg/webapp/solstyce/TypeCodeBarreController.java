package fr.legrain.bdg.webapp.solstyce;

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
import javax.faces.context.ExternalContext;
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

import fr.legrain.article.dto.TaTypeCodeBarreDTO;
import fr.legrain.article.model.TaTypeCodeBarre;
import fr.legrain.bdg.article.service.remote.ITypeCodeBarreServiceRemote;
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
public class TypeCodeBarreController implements Serializable {  

	private List<TaTypeCodeBarreDTO> values; 
	private List<TaTypeCodeBarreDTO> filteredValues; 
	private TaTypeCodeBarreDTO nouveau ;
	private TaTypeCodeBarreDTO selection ;
	
	// Dima - Début
	private TaTypeCodeBarre taTypeCodeBarre ;
	private boolean listeSaisie; // variable pour déterminé la table active (0 - la liste, 1 - le saisie)
	// Dima -  Fin
	
	private String modeEcranDefaut;
	private static final String C_DIALOG = "dialog";

	private ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();

	private @EJB ITypeCodeBarreServiceRemote taTypeCodeBarreService;
	
	private LgrDozerMapper<TaTypeCodeBarreDTO,TaTypeCodeBarre> mapperUIToModel  = new LgrDozerMapper<TaTypeCodeBarreDTO,TaTypeCodeBarre>();
	private LgrDozerMapper<TaTypeCodeBarre,TaTypeCodeBarreDTO> mapperModelToUI  = new LgrDozerMapper<TaTypeCodeBarre,TaTypeCodeBarreDTO>();
	
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

	public TypeCodeBarreController() {  
	}  

	// Dima - Début
	public boolean isListeSaisie() {
		return listeSaisie;
	}

	public void setListeSaisie(boolean listeSaisie) {
		this.listeSaisie = listeSaisie;
	}
	// Dima -  Fin
	
	public void actInserer(ActionEvent actionEvent){
		nouveau = new TaTypeCodeBarreDTO();
		listeSaisie = true;
		modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
	}

	public void actModifier(ActionEvent actionEvent){
		listeSaisie = true;
		nouveau = selection;
		modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
	}

	public void actSupprimer(){
		TaTypeCodeBarre taTypeCodeBarre = new TaTypeCodeBarre();
		try {
			if(selection!=null && selection.getId()!=null){
				taTypeCodeBarre = taTypeCodeBarreService.findById(selection.getId());
			}

			taTypeCodeBarreService.remove(taTypeCodeBarre);
			values.remove(selection);
			
			if(!values.isEmpty()) {
				selection = values.get(0);
			}else {
				selection=new TaTypeCodeBarreDTO();
			}

			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Type code barre", "actSupprimer"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Type code barre", e.getCause().getCause().getCause().getCause().getMessage()));
		}
	}
	
	public void actEnregistrer(ActionEvent actionEvent){
		try {
			TaTypeCodeBarre retour = null;
			taTypeCodeBarre=new TaTypeCodeBarre();
			if(nouveau.getId()==null || taTypeCodeBarreService.findById(nouveau.getId()) == null){
				taTypeCodeBarre=new TaTypeCodeBarre();
				mapperUIToModel.map(nouveau, taTypeCodeBarre);
				taTypeCodeBarre = taTypeCodeBarreService.merge(taTypeCodeBarre, ITaTCiviliteServiceRemote.validationContext);
				mapperModelToUI.map(taTypeCodeBarre, nouveau);
				values= taTypeCodeBarreService.selectAllDTO();
				nouveau = values.get(0);
				nouveau = new TaTypeCodeBarreDTO();

				modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

			}else{
				if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_EDITION)==0){
					taTypeCodeBarre = taTypeCodeBarreService.findById(nouveau.getId());
					mapperUIToModel.map(nouveau, taTypeCodeBarre);
					taTypeCodeBarre = taTypeCodeBarreService.merge(taTypeCodeBarre, ITaTCiviliteServiceRemote.validationContext);
					mapperModelToUI.map(taTypeCodeBarre, nouveau);
					values= taTypeCodeBarreService.selectAllDTO();
					nouveau = values.get(0);
					nouveau = new TaTypeCodeBarreDTO();

					modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
				}
				else{
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "Code déja utilisé"));
				}
			}
			if(modeEcranDefaut!=null && modeEcranDefaut.equals(C_DIALOG)) {
				PrimeFaces.current().dialog().closeDynamic(taTypeCodeBarre);
			}
		} catch(Exception e) {
			e=ThrowableExceptionLgr.renvoieCauseRuntimeException(e);
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "TypeFamilleUnite", e.getMessage()));
		}
	}

	public void actAnnuler(ActionEvent actionEvent){
		if(values.size()>= 1){
			selection = values.get(0);
			listeSaisie = false;
		}		
		nouveau = new TaTypeCodeBarreDTO();
		listeSaisie = false;
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}

	public List<TaTypeCodeBarreDTO> getValues(){  
		return values;
	}

	public TaTypeCodeBarreDTO getNouveau() {
		return nouveau;
	}

	public void setNouveau(TaTypeCodeBarreDTO newTaTypeCodeBarre) {
		this.nouveau = newTaTypeCodeBarre;
	}

	public TaTypeCodeBarreDTO getSelection() {
		return selection;
	}

	public void setSelection(TaTypeCodeBarreDTO selectedTaTypeCodeBarre) {
		this.selection = selectedTaTypeCodeBarre;
	}

	public void setValues(List<TaTypeCodeBarreDTO> values) {
		this.values = values;
	}

	public void onRowSelect(SelectEvent event) {  
		if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_CONSULTATION)==0) {
			nouveau = selection;
		}
	}

	public List<TaTypeCodeBarreDTO> getFilteredValues() {
		return filteredValues;
	}

	public void setFilteredValues(List<TaTypeCodeBarreDTO> filteredValues) {
		this.filteredValues = filteredValues;
	}
	
	public void refresh(){
		values = taTypeCodeBarreService.selectAllDTO();
		Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		modeEcranDefaut = params.get("modeEcranDefaut");
		if(modeEcranDefaut!=null && modeEcranDefaut.equals(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION))) {
			modeEcranDefaut = C_DIALOG;
			actInserer(null);
		} else if(modeEcranDefaut!=null && modeEcranDefaut.equals(modeEcran.modeString(EnumModeObjet.C_MO_EDITION))) {
			modeEcranDefaut = C_DIALOG;
			if(params.get("idEntity")!=null) {
				try {
					selection = taTypeCodeBarreService.findByIdDTO(LibConversion.stringToInteger(params.get("idEntity")));
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

	public void actImprimer(ActionEvent actionEvent) {
		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage("CodeBarre", "actImprimer")); 
		try {

			//		FacesContext facesContext = FacesContext.getCurrentInstance();
			//		HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);

			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			sessionMap.put("codeBarre", taTypeCodeBarreService.findById(selection.getId()));

			//			session.setAttribute("tiers", taTiersService.findById(selectedTaTiersDTO.getId()));
			System.out.println("CodeBarreController.actImprimer()");
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// Dima - Début
	public void validateObject(FacesContext context, UIComponent component, Object value) throws ValidatorException {
//		String msg = "";
//		try {
//			String nomChamp =  (String) component.getAttributes().get("nomChamp");
//			validateUIField(nomChamp,value);
//			TaTypeCodeBarreDTO temp=new TaTypeCodeBarreDTO();
//			PropertyUtils.setProperty(temp, nomChamp, value);
//			taTypeCodeBarreService.validateEntityProperty(temp, nomChamp, ITypeCodeBarreServiceRemote.validationContext );
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
			Set<ConstraintViolation<TaTypeCodeBarreDTO>> violations = factory.getValidator().validateValue(TaTypeCodeBarreDTO.class,nomChamp,value);
			if (violations.size() > 0) {
				messageComplet = "Erreur de validation : ";
				for (ConstraintViolation<TaTypeCodeBarreDTO> cv : violations) {
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
			if(nomChamp.equals(Const.C_TYPE_CODE_BARRE_ARTICLE)) {
					boolean changement=false;
					if(selection.getCodeTypeCodeBarre()!=null && value!=null && !selection.getCodeTypeCodeBarre().equals(""))
					{
						if(value instanceof TaTypeCodeBarre)
							changement=((TaTypeCodeBarre) value).getCodeTypeCodeBarre().equals(selection.getCodeTypeCodeBarre());
						else if(value instanceof String)
						changement=!value.equals(selection.getCodeTypeCodeBarre());
					}
					if(changement && modeEcran.dataSetEnModeModification()){
						FacesContext context = FacesContext.getCurrentInstance();  
						context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Type code barre", Const.C_MESSAGE_CHANGEMENT_CODE));
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
        
        PrimeFaces.current().dialog().openDynamic("articles/dialog_type_code_barre", options, params);
        
	}
	
	public void handleReturnDialogTypes(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			taTypeCodeBarre = (TaTypeCodeBarre) event.getObject();
			
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
        
        PrimeFaces.current().dialog().openDynamic("articles/dialog_type_code_barre", options, params);

	}
	// Dima -  Fin
}  
