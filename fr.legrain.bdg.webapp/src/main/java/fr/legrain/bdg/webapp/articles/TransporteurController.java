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

import fr.legrain.article.dto.TaTransporteurDTO;
import fr.legrain.article.model.TaTTransport;
import fr.legrain.article.model.TaTransporteur;
import fr.legrain.article.model.TaTypeCodeBarre;
import fr.legrain.bdg.article.service.remote.ITaTTransportServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaTransporteurServiceRemote;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.webapp.ConstWeb;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.ModeObjetEcranServeur;

@Named
@ViewScoped  
public class TransporteurController implements Serializable {  

	private static final long serialVersionUID = -3425189566705100810L;
	
	private List<TaTransporteurDTO> values; 
	private List<TaTransporteurDTO> filteredValues; 
	
	private TaTransporteurDTO nouveau ;
	private TaTransporteurDTO selection ;
	
	private TaTransporteur taTransporteur ;
	private TaTTransport taTTransport = new TaTTransport();
	
	private String modeEcranDefaut;
	private static final String C_DIALOG = "dialog";

	private ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();

	private @EJB ITaTransporteurServiceRemote taTransporteurService;
	private @EJB ITaTTransportServiceRemote taTTransportService;
	
	private LgrDozerMapper<TaTransporteurDTO,TaTransporteur> mapperUIToModel  = new LgrDozerMapper<TaTransporteurDTO,TaTransporteur>();
	private LgrDozerMapper<TaTransporteur,TaTransporteurDTO> mapperModelToUI  = new LgrDozerMapper<TaTransporteur,TaTransporteurDTO>();
	
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

	public TransporteurController() {  
	}  

	public void actInserer(ActionEvent actionEvent){
		nouveau = new TaTransporteurDTO();
		modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
	}

	public void actModifier(ActionEvent actionEvent){
		nouveau = selection;
		autoCompleteMapDTOtoUI();
		modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
	}

	public void actSupprimer(){
		TaTransporteur taTReception = new TaTransporteur();
		try {
			if(selection!=null && selection.getId()!=null){
				taTReception = taTransporteurService.findById(selection.getId());
			}

			taTransporteurService.remove(taTReception);
			values.remove(selection);
			
			if(!values.isEmpty()) {
				selection = values.get(0);
			}else {
				selection=new TaTransporteurDTO();
			}

			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Type reception", "actSupprimer"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Transporteur", e.getCause().getCause().getCause().getCause().getMessage()));
		}
	}


	public void actEnregistrer(ActionEvent actionEvent){
		try {
			TaTypeCodeBarre retour = null;
			taTransporteur=new TaTransporteur();
			if(nouveau.getId()==null || taTransporteurService.findById(nouveau.getId()) == null){
				taTransporteur=new TaTransporteur();
				mapperUIToModel.map(nouveau, taTransporteur);
				autoCompleteMapUIToDTO();
				if(nouveau.getCodeTransporteur()!=null && !nouveau.getCodeTransporteur().equals("")) {
					taTTransport = taTTransportService.findByCode(nouveau.getCodeTransporteur());
					taTransporteur.setTaTTransport(taTTransport);
				}
				taTransporteur = taTransporteurService.merge(taTransporteur, ITaTransporteurServiceRemote.validationContext);
				mapperModelToUI.map(taTransporteur, nouveau);
				values= taTransporteurService.selectAllDTO();
				nouveau = values.get(0);
				nouveau = new TaTransporteurDTO();

				modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

			}else{
				if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_EDITION)==0){
					taTransporteur = taTransporteurService.findById(nouveau.getId());
					mapperUIToModel.map(nouveau, taTransporteur);
					autoCompleteMapUIToDTO();
					if(nouveau.getCodeTransporteur()!=null && !nouveau.getCodeTransporteur().equals("")) {
						taTTransport = taTTransportService.findByCode(nouveau.getCodeTransporteur());
						taTransporteur.setTaTTransport(taTTransport);
					}
					taTransporteur = taTransporteurService.merge(taTransporteur, ITaTransporteurServiceRemote.validationContext);
					mapperModelToUI.map(taTransporteur, nouveau);
					values= taTransporteurService.selectAllDTO();
					nouveau = values.get(0);
					nouveau = new TaTransporteurDTO();

					modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
				}
				else{
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "Code déja utilisé"));
				}
			}
			if(modeEcranDefaut!=null && modeEcranDefaut.equals(C_DIALOG)) {
				PrimeFaces.current().dialog().closeDynamic(taTransporteur);
			}
		} catch(Exception e) {
			e=ThrowableExceptionLgr.renvoieCauseRuntimeException(e);
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			//context.addMessage(null, new FacesMessage("Tiers", "actEnregistrer")); 
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Transporteur", e.getMessage()));
		}
	}
	
	public List<TaTTransport> typeTransportAutoComplete(String query) {
		List<TaTTransport> allValues = taTTransportService.selectAll();
		List<TaTTransport> filteredValues = new ArrayList<TaTTransport>();
		TaTTransport cp = new TaTTransport();

		for (int i = 0; i < allValues.size(); i++) {
			cp = allValues.get(i);
			if(query.equals("*") || cp.getCodeTTransport().toLowerCase().contains(query.toLowerCase())) {
				filteredValues.add(cp);
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
		if(taTTransport!=null) {
			validateUIField(Const.C_CODE_T_TRANSPORT,taTTransport.getCodeTTransport());
			nouveau.setCodeTransporteur(taTTransport.getCodeTTransport());
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
			taTTransport = null;
			//			taTEntite = null;
//			if(selectedTaDevisDTO.getCodeTiers()!=null && !selectedTaDevisDTO.getCodeTiers().equals("")) {
//				taTiers = taTiersService.findByCode(selectedTaDevisDTO.getCodeTiers());
//				taTiersDTO = mapperModelToUITiers.map(taTiers, TaTiersDTO.class);
//			}
			if(nouveau.getCodeTTransport()!=null && !nouveau.getCodeTTransport().equals("")) {
				taTTransport = taTTransportService.findByCode(nouveau.getCodeTTransport());
			}
			//			if(selectedTaDevisDTO.getCodeTEntite()!=null && !selectedTaDevisDTO.getCodeTEntite().equals("")) {
			//				taTEntite = taTEntiteService.findByCode(selectedTaDevisDTO.getCodeTEntite());
			//			}
		} catch (FinderException e) {
			e.printStackTrace();
		}
	}

	public TaTTransport getTaTTransport() {
		return taTTransport;
	}

	public void setTaTTransport(TaTTransport taTTransport) {
		this.taTTransport = taTTransport;
	}

	public void actAnnuler(ActionEvent actionEvent){
		if(values.size()>= 1){
			selection = values.get(0);
		}		
		nouveau = new TaTransporteurDTO();
		
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}

	public List<TaTransporteurDTO> getValues(){  
		return values;
	}

	public TaTransporteurDTO getNouveau() {
		return nouveau;
	}

	public void setNouveau(TaTransporteurDTO newTaTransporteur) {
		this.nouveau = newTaTransporteur;
	}

	public TaTransporteurDTO getSelection() {
		return selection;
	}

	public void setSelection(TaTransporteurDTO selectedTaTransporteur) {
		this.selection = selectedTaTransporteur;
	}

	public void setValues(List<TaTransporteurDTO> values) {
		this.values = values;
	}


	public void onRowSelect(SelectEvent event) {  
		if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_CONSULTATION)==0) {
			nouveau = selection;
		}
	}

	public List<TaTransporteurDTO> getFilteredValues() {
		return filteredValues;
	}

	public void setFilteredValues(List<TaTransporteurDTO> filteredValues) {
		this.filteredValues = filteredValues;
	}
	
	public void refresh(){
		values = taTransporteurService.findByCodeLight(null);
		Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		modeEcranDefaut = params.get("modeEcranDefaut");
		if(modeEcranDefaut!=null && modeEcranDefaut.equals(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION))) {
			modeEcranDefaut = C_DIALOG;
			actInserer(null);
		} else if(modeEcranDefaut!=null && modeEcranDefaut.equals(modeEcran.modeString(EnumModeObjet.C_MO_EDITION))) {
			modeEcranDefaut = C_DIALOG;
			if(params.get("idEntity")!=null) {
				try {
					selection = taTransporteurService.findByIdDTO(LibConversion.stringToInteger(params.get("idEntity")));
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
		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("CodeTVA", "actImprimer")); 
		}
		try {

			//		FacesContext facesContext = FacesContext.getCurrentInstance();
			//		HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);

			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			sessionMap.put("codeTVA", taTransporteurService.findById(selection.getId()));

			//			session.setAttribute("tiers", taTiersService.findById(selectedTaTiersDTO.getId()));
			System.out.println("CodeTVAController.actImprimer()");
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// Dima - Début
		public void validateObject(FacesContext context, UIComponent component, Object value) throws ValidatorException {
//			String msg = "";
//			try {
//				String nomChamp =  (String) component.getAttributes().get("nomChamp");
//				validateUIField(nomChamp,value);
//				TaTransporteurDTO temp=new TaTransporteurDTO();
//				PropertyUtils.setProperty(temp, nomChamp, value);
//				taTransporteurService.validateEntityProperty(temp, nomChamp, ITaTransporteurServiceRemote.validationContext );
//			} catch(Exception e) {
//				msg += e.getMessage();
//				throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
//			}
			
			String messageComplet = "";
			try {
				String nomChamp =  (String) component.getAttributes().get("nomChamp");
				//validation automatique via la JSR bean validation
				ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
				Set<ConstraintViolation<TaTransporteurDTO>> violations = factory.getValidator().validateValue(TaTransporteurDTO.class,nomChamp,value);
				if (violations.size() > 0) {
					messageComplet = "Erreur de validation : ";
					for (ConstraintViolation<TaTransporteurDTO> cv : violations) {
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
				if(nomChamp.equals(Const.C_CODE_ENTREPOT)) {
						boolean changement=false;
						if(selection.getCodeTransporteur()!=null && value!=null && !selection.getCodeTransporteur().equals(""))
						{
							if(value instanceof TaTransporteur)
								changement=((TaTransporteur) value).getCodeTransporteur().equals(selection.getCodeTransporteur());
							else if(value instanceof String)
							changement=!value.equals(selection.getCodeTransporteur());
						}
						if(changement && modeEcran.dataSetEnModeModification()){
							FacesContext context = FacesContext.getCurrentInstance();  
							context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Transporteur", Const.C_MESSAGE_CHANGEMENT_CODE));
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
	        
	        PrimeFaces.current().dialog().openDynamic("articles/dialog_transporteur", options, params);
	        
		}
		
		public void handleReturnDialogTypes(SelectEvent event) {
			if(event!=null && event.getObject()!=null) {
				taTransporteur = (TaTransporteur) event.getObject();
				
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
	        
	        PrimeFaces.current().dialog().openDynamic("articles/dialog_transporteur", options, params);

		}
		// Dima -  Fin
}  
