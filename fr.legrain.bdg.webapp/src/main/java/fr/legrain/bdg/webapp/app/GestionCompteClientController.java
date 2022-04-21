package fr.legrain.bdg.webapp.app;

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

import fr.legrain.article.dto.TaUniteDTO;
import fr.legrain.article.titretransport.model.TaTitreTransport;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaPreferencesServiceRemote;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.tiers.service.remote.ITaEspaceClientServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaParamEspaceClientServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaEspaceClientServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTCiviliteServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.bdg.webapp.ConstWeb;
import fr.legrain.email.service.LgrEmailSMTPService;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.ModeObjetEcranServeur;
import fr.legrain.tiers.dto.TaEspaceClientDTO;
import fr.legrain.tiers.dto.TaTiersDTO;
import fr.legrain.tiers.dto.TaEspaceClientDTO;
import fr.legrain.tiers.model.TaEspaceClient;
import fr.legrain.tiers.model.TaParamEspaceClient;
import fr.legrain.tiers.model.TaTiers;
import fr.legrain.tiers.model.TaEspaceClient;

@Named
@ViewScoped 
public class GestionCompteClientController implements Serializable {

	private List<TaEspaceClientDTO> values; 
	private List<TaEspaceClientDTO> filteredValues; 
	private TaEspaceClientDTO nouveau ;
	private TaEspaceClientDTO selection ;
	
	private TaTiersDTO taTiersDTO;
	private TaTiersDTO taTiersDtoOrigine;
	private TaTiers taTiers;
	
	private TaEspaceClient taEspaceClient = new TaEspaceClient();
	
	private String modeEcranDefaut;
	private static final String C_DIALOG = "dialog";

	private ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();

	private @EJB ITaEspaceClientServiceRemote taEspaceClientService;
	private @EJB ITaTiersServiceRemote taTiersService;
	
	private LgrDozerMapper<TaEspaceClientDTO,TaEspaceClient> mapperUIToModel  = new LgrDozerMapper<TaEspaceClientDTO,TaEspaceClient>();
	private LgrDozerMapper<TaEspaceClient,TaEspaceClientDTO> mapperModelToUI  = new LgrDozerMapper<TaEspaceClient,TaEspaceClientDTO>();

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
		taTiersDtoOrigine = null;
		taTiersDTO = null;
		values = taEspaceClientService.findAllLight();
		Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		modeEcranDefaut = params.get("modeEcranDefaut");
		if(modeEcranDefaut!=null && modeEcranDefaut.equals(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION))) {
			modeEcranDefaut = C_DIALOG;
			actInserer(null);
		} else if(modeEcranDefaut!=null && modeEcranDefaut.equals(modeEcran.modeString(EnumModeObjet.C_MO_EDITION))) {
			modeEcranDefaut = C_DIALOG;
			if(params.get("idEntity")!=null) {
				try {
					selection = taEspaceClientService.findByIdDTO(LibConversion.stringToInteger(params.get("idEntity")));
					if(selection.getCodeTiers()!=null) {
						taTiersDTO = taTiersService.findByCodeDTO(selection.getCodeTiers());
						taTiersDtoOrigine = taTiersDTO;
					}
				} catch (FinderException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			actModifier(null);
		} else {
			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
//			if(values.size()>= 1){
//				selection = values.get(0);
//			}	
		}
	}
	
	public void actAnnuler(ActionEvent actionEvent){
		
		if(values.size()>= 1){
			selection = values.get(0);
		}		
		nouveau = new TaEspaceClientDTO();
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}
	
	public void actRafraichir(ActionEvent actionEvent){
		values = taEspaceClientService.findAllLight();
	}

	public void actEnregistrer(ActionEvent actionEvent){
		try {
			TaEspaceClientDTO retour = null;
			taEspaceClient=new TaEspaceClient();
			if(nouveau.getId()==null || taEspaceClientService.findById(nouveau.getId()) == null){
				//nouveau.setPassword(nouveau.getEmail()); //pour tester création
				mapperUIToModel.map(nouveau, taEspaceClient);
				taEspaceClient = taEspaceClientService.merge(taEspaceClient, ITaTCiviliteServiceRemote.validationContext);
				mapperModelToUI.map(taEspaceClient, nouveau);
				values = taEspaceClientService.findAllLight();
				nouveau = values.get(0);
				nouveau = new TaEspaceClientDTO();

				modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

			}else{
				if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_EDITION)==0){
					taEspaceClient = taEspaceClientService.findById(nouveau.getId());
					mapperUIToModel.map(nouveau, taEspaceClient);
					
					if(taTiersDtoOrigine!=null) {
						if(taTiersDTO != null || !taTiersDtoOrigine.getCodeTiers().equals(taTiersDTO.getCodeTiers())) { //changement de tiers
							taEspaceClient.setTaTiers(taTiersService.findByCode(taTiersDTO.getCodeTiers()));
						} else if(taTiersDTO == null) { //suppression de la liaison avec un tiers
							taEspaceClient.setTaTiers(null);
						}
					} else {
						if(taTiersDTO != null) { // il n' y avait pas de tiers, on en affecte un
							taEspaceClient.setTaTiers(taTiersService.findByCode(taTiersDTO.getCodeTiers()));
						}
					}
					
					taEspaceClient = taEspaceClientService.merge(taEspaceClient, ITaTCiviliteServiceRemote.validationContext);
					mapperModelToUI.map(taEspaceClient, nouveau);
					values = taEspaceClientService.findAllLight();
					nouveau = values.get(0);
					nouveau = new TaEspaceClientDTO();

					modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
				}
				else{
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "Code déja utilisé"));
				}
			}
			if(modeEcranDefaut!=null && modeEcranDefaut.equals(C_DIALOG)) {
				PrimeFaces.current().dialog().closeDynamic(taEspaceClient);
			}
		} catch(Exception e) {
			e=ThrowableExceptionLgr.renvoieCauseRuntimeException(e);
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "TypeAdresse", e.getMessage()));
		}
	}
	
	public void actInserer(ActionEvent actionEvent){
		nouveau = new TaEspaceClientDTO();
	
		modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
	}

	public void actModifier(ActionEvent actionEvent){
		nouveau = selection;
		
		modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
	}

	public void actSupprimer(){
		TaEspaceClient taEspaceClient = new TaEspaceClient();
		try {
			if(selection!=null && selection.getId()!=null){
				taEspaceClient = taEspaceClientService.findById(selection.getId());
			}

			taEspaceClientService.remove(taEspaceClient);
			//values.remove(selection);
			values = taEspaceClientService.findAllLight();
			
			if(!values.isEmpty()) {
				selection = values.get(0);
			}else {
				selection=new TaEspaceClientDTO();
			}

			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Type adresse", "actSupprimer"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Type adresse", e.getCause().getCause().getCause().getCause().getMessage()));
		}
	}
	
	public void onRowSelect(SelectEvent event) {  
		if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_CONSULTATION)==0) {
			nouveau = selection;
		}
	}
	
	public List<TaTiersDTO> tiersAutoCompleteDTOLight(String query) {
		List<TaTiersDTO> allValues = taTiersService.findByCodeLight("*");
		List<TaTiersDTO> filteredValues = new ArrayList<TaTiersDTO>();

		for (int i = 0; i < allValues.size(); i++) {
			TaTiersDTO civ = allValues.get(i);
			if(query.equals("*") || civ.getCodeTiers().toLowerCase().contains(query.toLowerCase())
					|| civ.getNomTiers().toLowerCase().contains(query.toLowerCase())
					|| (civ.getPrenomTiers() != null && civ.getPrenomTiers().toLowerCase().contains(query.toLowerCase()))
					|| (civ.getNomEntreprise() != null && civ.getNomEntreprise().toLowerCase().contains(query.toLowerCase()))
					) {
				filteredValues.add(civ);
			}
		}

		return filteredValues;
	}
	
	public void autcompleteSelection(SelectEvent event) {
		Object value = event.getObject();
		//		FacesMessage msg = new FacesMessage("Selected", "Item:" + value);

		String nomChamp =  (String) event.getComponent().getAttributes().get("nomChamp");
		System.out.println("autcompleteSelection() : "+nomChamp);

//		if(value!=null) {
//			if(value instanceof String && value.equals(Const.C_AUCUN))value="";	
//			if(value instanceof TaUniteDTO && ((TaUniteDTO) value).getCodeUnite()!=null && ((TaUniteDTO) value).getCodeUnite().equals(Const.C_AUCUN))value=null;	
//			if(value instanceof TaTitreTransport && ((TaTitreTransport) value).getCodeTitreTransport()!=null && ((TaTitreTransport) value).getCodeTitreTransport().equals(Const.C_AUCUN))value=null;	
//		}
//		validateUIField(nomChamp,value);
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

	public List<TaEspaceClientDTO> getValues(){  
		return values;
	}

	public TaEspaceClientDTO getNouveau() {
		return nouveau;
	}

	public void setNouveau(TaEspaceClientDTO newTaEspaceClient) {
		this.nouveau = newTaEspaceClient;
	}

	public TaEspaceClientDTO getSelection() {
		return selection;
	}

	public void setSelection(TaEspaceClientDTO selectedTaEspaceClient) {
		this.selection = selectedTaEspaceClient;
	}

	public void setValues(List<TaEspaceClientDTO> values) {
		this.values = values;
	}

	public List<TaEspaceClientDTO> getFilteredValues() {
		return filteredValues;
	}

	public void setFilteredValues(List<TaEspaceClientDTO> filteredValues) {
		this.filteredValues = filteredValues;
	}

	public ModeObjetEcranServeur getModeEcran() {
		return modeEcran;
	}

	public void validateObject(FacesContext context, UIComponent component, Object value) throws ValidatorException {

		String messageComplet = "";
		try {
			String nomChamp =  (String) component.getAttributes().get("nomChamp");
			//validation automatique via la JSR bean validation
			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
			Set<ConstraintViolation<TaEspaceClientDTO>> violations = factory.getValidator().validateValue(TaEspaceClientDTO.class,nomChamp,value);
			if (violations.size() > 0) {
				messageComplet = "Erreur de validation : ";
				for (ConstraintViolation<TaEspaceClientDTO> cv : violations) {
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

			if(nomChamp.equals(Const.C_CODE_T_ADR)) {
				boolean changement=false;
				if(selection.getEmail()!=null && value!=null && !selection.getEmail().equals(""))
				{
					if(value instanceof TaEspaceClient)
						changement=((TaEspaceClient) value).getEmail().equals(selection.getEmail());
					else if(value instanceof String)
					changement=!value.equals(selection.getEmail());
				}
				if(changement && modeEcran.dataSetEnModeModification()){
					FacesContext context = FacesContext.getCurrentInstance();  
					context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Type d'adresse", Const.C_MESSAGE_CHANGEMENT_CODE));
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
        
        PrimeFaces.current().dialog().openDynamic("admin/dialog_espace_client", options, params);
        
	}
	
	public void handleReturnDialogTypes(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			taEspaceClient = (TaEspaceClient) event.getObject();
			
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
        
        PrimeFaces.current().dialog().openDynamic("admin/dialog_espace_client", options, params);

	}
	
	public void actFermerDialog(ActionEvent actionEvent) {
		PrimeFaces.current().dialog().closeDynamic(null);
	}
	
	public TaTiersDTO getTaTiersDTO() {
		return taTiersDTO;
	}

	public void setTaTiersDTO(TaTiersDTO taTiersDTO) {
		this.taTiersDTO = taTiersDTO;
	}

	public TaTiers getTaTiers() {
		return taTiers;
	}

	public void setTaTiers(TaTiers taTiers) {
		this.taTiers = taTiers;
	}

	public TaTiersDTO getTaTiersDtoOrigine() {
		return taTiersDtoOrigine;
	}

	public void setTaTiersDtoOrigine(TaTiersDTO taTiersDtoOrigine) {
		this.taTiersDtoOrigine = taTiersDtoOrigine;
	}
}
