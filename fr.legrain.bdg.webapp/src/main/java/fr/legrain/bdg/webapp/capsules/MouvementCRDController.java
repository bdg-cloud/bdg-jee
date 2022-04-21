package fr.legrain.bdg.webapp.capsules;

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

import fr.legrain.article.titretransport.dto.TaStockCapsulesDTO;
import fr.legrain.article.titretransport.model.TaStockCapsules;
import fr.legrain.article.titretransport.model.TaTitreTransport;
import fr.legrain.bdg.article.titretransport.service.remote.ITaStockCapsulesServiceRemote;
import fr.legrain.bdg.article.titretransport.service.remote.ITaTitreTransportServiceRemote;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.webapp.ConstWeb;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.ModeObjetEcranServeur;

@Named
@ViewScoped  
public class MouvementCRDController implements Serializable {  

	private List<TaStockCapsulesDTO> values; 
	private List<TaStockCapsulesDTO> filteredValues; 
	private TaStockCapsulesDTO nouveau ;
	private TaStockCapsulesDTO selection ;
	private TaTitreTransport titre;

	private TaStockCapsules obj = new TaStockCapsules();
	private String typeMouvement="E";

	private String modeEcranDefaut;
	private static final String C_DIALOG = "dialog";

	private ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();

	private @EJB ITaStockCapsulesServiceRemote taStockCapsulesService;
	private @EJB ITaTitreTransportServiceRemote taTitreTransportService;
	
	private LgrDozerMapper<TaStockCapsulesDTO,TaStockCapsules> mapperUIToModel  = new LgrDozerMapper<TaStockCapsulesDTO,TaStockCapsules>();
	private LgrDozerMapper<TaStockCapsules,TaStockCapsulesDTO> mapperModelToUI  = new LgrDozerMapper<TaStockCapsules,TaStockCapsulesDTO>();

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

	public MouvementCRDController() {  
	}  

	public void actInserer(ActionEvent actionEvent){
		nouveau = new TaStockCapsulesDTO();
		nouveau.setMouvStock("E");
		modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
	}

	public void actModifier(ActionEvent actionEvent){
		nouveau = selection;
		modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
	}

	public void actSupprimer(){
		TaStockCapsules taTva = new TaStockCapsules();
		try {
			if(selection!=null && selection.getId()!=null){
				taTva = taStockCapsulesService.findById(selection.getId());
			}

			taStockCapsulesService.remove(taTva);
			values.remove(selection);
			
			if(!values.isEmpty()) {
				selection = values.get(0);
			}else {
				selection=new TaStockCapsulesDTO();
			}

			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Mouvement CRD", "actSupprimer"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mouvement CRD", e.getCause().getCause().getCause().getCause().getMessage()));
		}
	}

	public void autoCompleteMapUIToDTO() {		
		if(titre!=null) {
			obj.setTaTitreTransport(titre);
		}
	}
	
	public void autoCompleteMapDTOToUI() {		
		if(nouveau.getCodeTitreTransport()!=null) {
			try {
				titre=taTitreTransportService.findByCode(nouveau.getCodeTitreTransport());
			} catch (FinderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void actEnregistrer(ActionEvent actionEvent){
		try {
			TaStockCapsulesDTO retour = null;
			obj=new TaStockCapsules();

			if(nouveau.getId()==null || taStockCapsulesService.findById(nouveau.getId()) == null){
				mapperUIToModel.map(nouveau, obj);
				autoCompleteMapUIToDTO();
				obj = taStockCapsulesService.merge(obj, ITaStockCapsulesServiceRemote.validationContext);
				mapperModelToUI.map(obj, nouveau);
				values= taStockCapsulesService.selectAllDTO();
				nouveau = values.get(0);
				autoCompleteMapDTOToUI();

				modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

			}else{
				if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_EDITION)==0){
					obj = taStockCapsulesService.findById(nouveau.getId());
					mapperUIToModel.map(nouveau, obj);
					autoCompleteMapUIToDTO();
					obj = taStockCapsulesService.merge(obj, ITaStockCapsulesServiceRemote.validationContext);
					mapperModelToUI.map(obj, nouveau);
					autoCompleteMapDTOToUI();
					values= taStockCapsulesService.selectAllDTO();
					nouveau = values.get(0);

					modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
				}
				else{
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "Code déja utilisé"));
				}
			}
			if(modeEcranDefaut!=null && modeEcranDefaut.equals(C_DIALOG)) {
				PrimeFaces.current().dialog().closeDynamic(obj);
			}
		} catch(Exception e) {
			e=ThrowableExceptionLgr.renvoieCauseRuntimeException(e);
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mouvement CRD", e.getMessage()));
		}
	}

	public void actAnnuler(ActionEvent actionEvent){
		if(values.size()>= 1){
			selection = values.get(0);
		}		
		nouveau = new TaStockCapsulesDTO();

		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}

	public List<TaStockCapsulesDTO> getValues(){  
		return values;
	}

	public TaStockCapsulesDTO getNouveau() {
		return nouveau;
	}

	public void setNouveau(TaStockCapsulesDTO newTaTitreTransport) {
		this.nouveau = newTaTitreTransport;
	}

	public TaStockCapsulesDTO getSelection() {
		return selection;
	}

	public void setSelection(TaStockCapsulesDTO selectedTaTitreTransport) {
		this.selection = selectedTaTitreTransport;
	}

	public void setValues(List<TaStockCapsulesDTO> values) {
		this.values = values;
	}

	public void onRowSelect(SelectEvent event) {  
		if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_CONSULTATION)==0) {
			nouveau = selection;
			if(selection.getCodeTitreTransport()!=null) {
				try {
					titre=taTitreTransportService.findByCode(selection.getCodeTitreTransport());
				} catch (FinderException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
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
	
	public List<TaTitreTransport> titreTransportAutoComplete(String query) {
        List<TaTitreTransport> allValues = taTitreTransportService.selectAll();
        List<TaTitreTransport> filteredValues = new ArrayList<TaTitreTransport>();
        
        TaTitreTransport civ = new TaTitreTransport();
        civ.setCodeTitreTransport(Const.C_AUCUN);
        filteredValues.add(civ);
        for (int i = 0; i < allValues.size(); i++) {
        	 civ = allValues.get(i);
            if(query.equals("*") || civ.getCodeTitreTransport().toLowerCase().contains(query.toLowerCase())) {
                filteredValues.add(civ);
            }
        }
        return filteredValues;
    }
	
	public List<TaStockCapsulesDTO> getFilteredValues() {
		return filteredValues;
	}

	public void setFilteredValues(List<TaStockCapsulesDTO> filteredValues) {
		this.filteredValues = filteredValues;
	}

	public void refresh(){
		values = taStockCapsulesService.selectAllDTO();
		Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		modeEcranDefaut = params.get("modeEcranDefaut");
		if(modeEcranDefaut!=null && modeEcranDefaut.equals(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION))) {
			modeEcranDefaut = C_DIALOG;
			actInserer(null);
		} else if(modeEcranDefaut!=null && modeEcranDefaut.equals(modeEcran.modeString(EnumModeObjet.C_MO_EDITION))) {
			modeEcranDefaut = C_DIALOG;
			if(params.get("idEntity")!=null) {
				try {
					selection = taStockCapsulesService.findByIdDTO(LibConversion.stringToInteger(params.get("idEntity")));
					nouveau=selection;
					autoCompleteMapDTOToUI();
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

	}

	// Dima - Début
	public void validateObject(FacesContext context, UIComponent component, Object value) throws ValidatorException {
//		String msg = "";
//		try {
//			String nomChamp =  (String) component.getAttributes().get("nomChamp");
//			validateUIField(nomChamp,value);
//			TaStockCapsulesDTO temp=new TaStockCapsulesDTO();
//			PropertyUtils.setProperty(temp, nomChamp, value);
//			taTCodeTitreTransportService.validateEntityProperty(temp, nomChamp, ITaTitreTransportServiceRemote.validationContext );
//		} catch(Exception e) {
//			msg += e.getMessage();
//			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
//		}
		
		String messageComplet = "";
		try {
			String nomChamp =  (String) component.getAttributes().get("nomChamp");
			//validation automatique via la JSR bean validation
			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
			Set<ConstraintViolation<TaStockCapsulesDTO>> violations = factory.getValidator().validateValue(TaStockCapsulesDTO.class,nomChamp,value);
			if (violations.size() > 0) {
				messageComplet = "Erreur de validation : ";
				for (ConstraintViolation<TaStockCapsulesDTO> cv : violations) {
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
//			if(nomChamp.equals(Const.C_CODE_TITRE_TRANSPORT)) {
//					boolean changement=false;
//					if(titre!=null && titre.getCodeTitreTransport()!=null && value!=null && !titre.getCodeTitreTransport().equals(""))
//					{
//						if(value instanceof TaTitreTransport)
//							changement=((TaTitreTransport) value).getCodeTitreTransport().equals(nouveau.getCodeTitreTransport());
//						else if(value instanceof String)
//						changement=!value.equals(nouveau.getCodeTitreTransport());
//					}
//					if(changement && modeEcran.dataSetEnModeModification()){
//						FacesContext context = FacesContext.getCurrentInstance();  
//						context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Mouvement CRD", Const.C_MESSAGE_CHANGEMENT_CODE));
//					}
//				}
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

		PrimeFaces.current().dialog().openDynamic("gestionCapsules/dialog_mouvements_crd", options, params);

	}

	public void handleReturnDialogTypes(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			obj = (TaStockCapsules) event.getObject();
			
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

		PrimeFaces.current().dialog().openDynamic("gestionCapsules/dialog_mouvements_crd", options, params);

	}
	// Dima -  Fin	

	public TaTitreTransport getTitre() {
		return titre;
	}

	public void setTitre(TaTitreTransport titre) {
		this.titre = titre;
	}

	public String getTypeMouvement() {
		return typeMouvement;
	}

	public void setTypeMouvement(String typeMouvement) {
		this.typeMouvement = typeMouvement;
	}
}  
