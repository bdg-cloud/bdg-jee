package fr.legrain.bdg.webapp.tiers;

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
import fr.legrain.bdg.tiers.service.remote.ITaCPaiementServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTCPaiementServiceRemote;
import fr.legrain.bdg.webapp.ConstWeb;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.ModeObjetEcranServeur;
import fr.legrain.tiers.dto.TaCPaiementDTO;
import fr.legrain.tiers.model.TaCPaiement;
import fr.legrain.tiers.model.TaTCPaiement;

@Named
@ViewScoped  
public class ConditionPaiementDocController implements Serializable {  

	private List<TaCPaiementDTO> values; 
	private List<TaCPaiementDTO> filteredValues; 
	private TaCPaiementDTO nouveau ;
	private TaCPaiementDTO selection ;
	
	private TaCPaiement taCPaiement = new TaCPaiement();
	private TaTCPaiement taTCPaiement = new TaTCPaiement();
	
	private String modeEcranDefaut;
	private static final String C_DIALOG = "dialog";
	
	/*
	 * Booleen pour indiquer si on travaille sur des conditions de paiement pour les documents ou pour les tiers
	 * Par defaut, écran pour les conditions de paiement des tiers
	 */
	private boolean conditionPaiementDocument = true;

	private ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();

	private @EJB ITaCPaiementServiceRemote taCPaiementService;
	private @EJB ITaTCPaiementServiceRemote taTCPaiementService;
	
	private LgrDozerMapper<TaCPaiementDTO,TaCPaiement> mapperUIToModel  = new LgrDozerMapper<TaCPaiementDTO,TaCPaiement>();
	private LgrDozerMapper<TaCPaiement,TaCPaiementDTO> mapperModelToUI  = new LgrDozerMapper<TaCPaiement,TaCPaiementDTO>();

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
	}

	public void refresh(){
		if(!conditionPaiementDocument) {
			values = taCPaiementService.findAllCPaiementTiers();
		} else {
			values = taCPaiementService.findAllCPaiementDoc();
		}
		Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		modeEcranDefaut = params.get("modeEcranDefaut");
		if(modeEcranDefaut!=null && modeEcranDefaut.equals(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION))) {
			modeEcranDefaut = C_DIALOG;
			actInserer(null);
		} else if(modeEcranDefaut!=null && modeEcranDefaut.equals(modeEcran.modeString(EnumModeObjet.C_MO_EDITION))) {
			modeEcranDefaut = C_DIALOG;
			if(params.get("idEntity")!=null) {
				try {
					selection = taCPaiementService.findByIdDTO(LibConversion.stringToInteger(params.get("idEntity")));
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
	
	public ConditionPaiementDocController() {  
	}  

	public void actAnnuler(ActionEvent actionEvent){
		
		if(values.size()>= 1){
			selection = values.get(0);
		}		
		nouveau = new TaCPaiementDTO();
		
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}

	public void actEnregistrer(ActionEvent actionEvent){
		try {
			TaCPaiementDTO retour = null;
			taCPaiement=new TaCPaiement();
			if(nouveau.getId()==null || taCPaiementService.findById(nouveau.getId()) == null){
				mapperUIToModel.map(nouveau, taCPaiement);
				autoCompleteMapUIToDTO();
				if(nouveau.getCodeTCPaiement()!=null && !nouveau.getCodeTCPaiement().equals("")) {
					taTCPaiement = taTCPaiementService.findByCode(nouveau.getCodeTCPaiement());
					taCPaiement.setTaTCPaiement(taTCPaiement);
					taTCPaiement.setTaCPaiement(taCPaiement);
				}
				taCPaiement = taCPaiementService.merge(taCPaiement, ITaCPaiementServiceRemote.validationContext);
				mapperModelToUI.map(taCPaiement, nouveau);
				values= taCPaiementService.selectAllDTO();
				nouveau = values.get(0);
				nouveau = new TaCPaiementDTO();

				modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

			}else{
				if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_EDITION)==0){
					taCPaiement = taCPaiementService.findById(nouveau.getId());
					mapperUIToModel.map(nouveau, taCPaiement);
					autoCompleteMapUIToDTO();
					if(nouveau.getCodeTCPaiement()!=null && !nouveau.getCodeTCPaiement().equals("")) {
						taTCPaiement = taTCPaiementService.findByCode(nouveau.getCodeTCPaiement());
						taCPaiement.setTaTCPaiement(taTCPaiement);
						taTCPaiement.setTaCPaiement(taCPaiement);
					}
					taCPaiement = taCPaiementService.merge(taCPaiement, ITaCPaiementServiceRemote.validationContext);
					taTCPaiement = taTCPaiementService.merge(taTCPaiement, ITaTCPaiementServiceRemote.validationContext);
					mapperModelToUI.map(taCPaiement, nouveau);
					values= taCPaiementService.selectAllDTO();
					nouveau = values.get(0);
					nouveau = new TaCPaiementDTO();

					modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
				}
				else{
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "Code déja utilisé"));
				}
			}
			if(modeEcranDefaut!=null && modeEcranDefaut.equals(C_DIALOG)) {
				PrimeFaces.current().dialog().closeDynamic(taCPaiement);
			}
		} catch(Exception e) {
			e=ThrowableExceptionLgr.renvoieCauseRuntimeException(e);
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Condition de paiement", e.getMessage()));
		}
	}
	
	public void actInserer(ActionEvent actionEvent){
		nouveau = new TaCPaiementDTO();
	
		modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
	}

	public void actModifier(ActionEvent actionEvent){
		nouveau = selection;
		autoCompleteMapDTOtoUI();
		modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
	}

	public void actSupprimer(){
		TaCPaiement taTAdr = new TaCPaiement();
		try {
			if(selection!=null && selection.getId()!=null){
				taTAdr = taCPaiementService.findById(selection.getId());
			}

			taCPaiementService.remove(taTAdr);
			values.remove(selection);
			
			if(!values.isEmpty()) {
				selection = values.get(0);
			}else {
				selection=new TaCPaiementDTO();
			}

			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Condition de paiement", "actSupprimer"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Condition de paiement", e.getCause().getCause().getCause().getCause().getMessage()));
		}
	}
	
	public void onRowSelect(SelectEvent event) {  
		if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_CONSULTATION)==0) {
			nouveau = selection;
			autoCompleteMapDTOtoUI();
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

	public List<TaCPaiementDTO> getValues(){  
		return values;
	}

	public TaCPaiementDTO getNouveau() {
		return nouveau;
	}

	public void setNouveau(TaCPaiementDTO newTaCPaiement) {
		this.nouveau = newTaCPaiement;
	}

	public TaCPaiementDTO getSelection() {
		return selection;
	}

	public void setSelection(TaCPaiementDTO selectedTaCPaiement) {
		this.selection = selectedTaCPaiement;
	}

	public void setValues(List<TaCPaiementDTO> values) {
		this.values = values;
	}

	public List<TaCPaiementDTO> getFilteredValues() {
		return filteredValues;
	}

	public void setFilteredValues(List<TaCPaiementDTO> filteredValues) {
		this.filteredValues = filteredValues;
	}

	public ModeObjetEcranServeur getModeEcran() {
		return modeEcran;
	}


	public void validateObject(FacesContext context, UIComponent component, Object value) throws ValidatorException {
//
//		String msg = "";
//
//		try {
//			String nomChamp =  (String) component.getAttributes().get("nomChamp");
//			validateUIField(nomChamp,value);
//			TaCPaiementDTO temp=new TaCPaiementDTO();
//			PropertyUtils.setProperty(temp, nomChamp, value);
//			taTAdrService.validateEntityProperty(temp, nomChamp, ITaCPaiementServiceRemote.validationContext );
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
			Set<ConstraintViolation<TaCPaiementDTO>> violations = factory.getValidator().validateValue(TaCPaiementDTO.class,nomChamp,value);
			if (violations.size() > 0) {
				messageComplet = "Erreur de validation : ";
				for (ConstraintViolation<TaCPaiementDTO> cv : violations) {
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

			if(nomChamp.equals(Const.C_CODE_C_PAIEMENT)) { 
				boolean changement=false;
				if(selection.getCodeCPaiement()!=null && value!=null && !selection.getCodeCPaiement().equals(""))
				{
					if(value instanceof TaCPaiement)
						changement=((TaCPaiement) value).getCodeCPaiement().equals(selection.getCodeCPaiement());
					else if(value instanceof String)
					changement=!value.equals(selection.getCodeCPaiement());
				}
				if(changement && modeEcran.dataSetEnModeModification()){
					FacesContext context = FacesContext.getCurrentInstance();  
					context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Condition de paiement", Const.C_MESSAGE_CHANGEMENT_CODE));
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
        options.put("contentHeight", 500);
        options.put("modal", true);
        
        Map<String,List<String>> params = new HashMap<String,List<String>>();
        List<String> list = new ArrayList<String>();
        list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
        params.put("modeEcranDefaut", list);
        
        PrimeFaces.current().dialog().openDynamic("tiers/dialog_condition_paiement_doc", options, params);
        
	}
	
	public void handleReturnDialogTypes(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			taCPaiement = (TaCPaiement) event.getObject();
			
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
        options.put("contentHeight", 500);
        options.put("modal", true);
        
        Map<String,List<String>> params = new HashMap<String,List<String>>();
        List<String> list = new ArrayList<String>();
        list.add(modeEcran.modeString(EnumModeObjet.C_MO_EDITION));
        params.put("modeEcranDefaut", list);
        List<String> list2 = new ArrayList<String>();
        list2.add(LibConversion.integerToString(selection.getId()));
        params.put("idEntity", list2);
        
        PrimeFaces.current().dialog().openDynamic("tiers/dialog_condition_paiement_doc", options, params);

	}
	
	public void actFermerDialog(ActionEvent actionEvent) {
		PrimeFaces.current().dialog().closeDynamic(null);
	}
	
	public List<TaTCPaiement> typeCPaiementAutoComplete(String query) {
		List<TaTCPaiement> allValues = taTCPaiementService.selectAll();
		List<TaTCPaiement> filteredValues = new ArrayList<TaTCPaiement>();
		TaTCPaiement cp = new TaTCPaiement();
//		cp.setCodeTCPaiement(Const.C_AUCUN);
//		filteredValues.add(cp);
		for (int i = 0; i < allValues.size(); i++) {
			cp = allValues.get(i);
			if(query.equals("*") || cp.getCodeTCPaiement().toLowerCase().contains(query.toLowerCase())) {
				if(conditionPaiementDocument) {
					//Cond. P des documents
					if(!cp.getCodeTCPaiement().equalsIgnoreCase("Tiers")) {
						filteredValues.add(cp);
					}
				} else {
					//Cond. P des tiers
					if(cp.getCodeTCPaiement().equalsIgnoreCase("Tiers")) {
						filteredValues.add(cp);
					}
				}
			}
		}

		return filteredValues;
	}
	
	public void autcompleteSelection(SelectEvent event) {
		Object value = event.getObject();
		//		FacesMessage msg = new FacesMessage("Selected", "Item:" + value);

		String nomChamp =  (String) event.getComponent().getAttributes().get("nomChamp");
		System.out.println("ConditionPaiementController.autcompleteSelection() : "+nomChamp);

		if(value!=null) {
			if(value instanceof String && value.equals(Const.C_AUCUN))value="";	
//			if(value instanceof TaUniteDTO && ((TaUniteDTO) value).getCodeUnite()!=null && ((TaUniteDTO) value).getCodeUnite().equals(Const.C_AUCUN))value=null;	
		}
		validateUIField(nomChamp,value);
	}
	
	public void autoCompleteMapUIToDTO() {
//		if(taTiers!=null) {
//			validateUIField(Const.C_CODE_TIERS,taTiers);
//			selectedTaDevisDTO.setCodeTiers(taTiers.getCodeTiers());
//		}
//		if(taTiersDTO!=null) {
//			validateUIField(Const.C_CODE_TIERS,taTiersDTO);
//			selectedTaDevisDTO.setCodeTiers(taTiersDTO.getCodeTiers());
//		}
		if(taTCPaiement!=null) {
			validateUIField(Const.C_CODE_T_C_PAIEMENT,taTCPaiement.getCodeTCPaiement());
			nouveau.setCodeTCPaiement(taTCPaiement.getCodeTCPaiement());
		}
		//		if(taTEntite!=null) {
		//			validateUIField(Const.C_CODE_T_ENTITE,taTEntite.getCodeTEntite());
		//			selectedTaDevisDTO.setCodeTEntite(taTEntite.getCodeTEntite());
		//		}
	}

	public void autoCompleteMapDTOtoUI() {
		try {
//			taTiers = null;
//			taTiersDTO = null;
			taTCPaiement = null;
			//			taTEntite = null;
//			if(selectedTaDevisDTO.getCodeTiers()!=null && !selectedTaDevisDTO.getCodeTiers().equals("")) {
//				taTiers = taTiersService.findByCode(selectedTaDevisDTO.getCodeTiers());
//				taTiersDTO = mapperModelToUITiers.map(taTiers, TaTiersDTO.class);
//			}
			if(nouveau.getCodeTCPaiement()!=null && !nouveau.getCodeTCPaiement().equals("")) {
				taTCPaiement = taTCPaiementService.findByCode(nouveau.getCodeTCPaiement());
			}
			//			if(selectedTaDevisDTO.getCodeTEntite()!=null && !selectedTaDevisDTO.getCodeTEntite().equals("")) {
			//				taTEntite = taTEntiteService.findByCode(selectedTaDevisDTO.getCodeTEntite());
			//			}
		} catch (FinderException e) {
			e.printStackTrace();
		}
	}

	public TaTCPaiement getTaTCPaiement() {
		return taTCPaiement;
	}

	public void setTaTCPaiement(TaTCPaiement taTCPaiement) {
		this.taTCPaiement = taTCPaiement;
	}

	public boolean isConditionPaiementDocument() {
		return conditionPaiementDocument;
	}

	public void setConditionPaiementDocument(boolean conditionPaiementDocument) {
		this.conditionPaiementDocument = conditionPaiementDocument;
	}
}  
