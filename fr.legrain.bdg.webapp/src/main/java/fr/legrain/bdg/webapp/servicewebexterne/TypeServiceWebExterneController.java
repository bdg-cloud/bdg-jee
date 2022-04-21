package fr.legrain.bdg.webapp.servicewebexterne;

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

import fr.legrain.article.model.TaTypeCodeBarre;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaTServiceWebExterneServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTCiviliteServiceRemote;
import fr.legrain.bdg.webapp.ConstWeb;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.ModeObjetEcranServeur;
import fr.legrain.servicewebexterne.dto.TaTServiceWebExterneDTO;
import fr.legrain.servicewebexterne.model.TaTServiceWebExterne;

@Named
@ViewScoped  
public class TypeServiceWebExterneController implements Serializable {  

	private List<TaTServiceWebExterneDTO> values; 
	private List<TaTServiceWebExterneDTO> filteredValues; 
	
	private TaTServiceWebExterneDTO nouveau ;
	private TaTServiceWebExterneDTO selection ;
	
	private TaTServiceWebExterne taTReception ;
	
	private String modeEcranDefaut;
	private static final String C_DIALOG = "dialog";

	private ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();

	private @EJB ITaTServiceWebExterneServiceRemote taTServiceWebExterneService;
	
	private LgrDozerMapper<TaTServiceWebExterneDTO,TaTServiceWebExterne> mapperUIToModel  = new LgrDozerMapper<TaTServiceWebExterneDTO,TaTServiceWebExterne>();
	private LgrDozerMapper<TaTServiceWebExterne,TaTServiceWebExterneDTO> mapperModelToUI  = new LgrDozerMapper<TaTServiceWebExterne,TaTServiceWebExterneDTO>();
	
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

	public TypeServiceWebExterneController() {  
	}  

	public void actInserer(ActionEvent actionEvent){
		nouveau = new TaTServiceWebExterneDTO();
		modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
	}

	public void actModifier(ActionEvent actionEvent){
		nouveau = selection;
		modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
	}

	public void actSupprimer(){
		TaTServiceWebExterne taTReception = new TaTServiceWebExterne();
		try {
			if(selection!=null && selection.getId()!=null){
				taTReception = taTServiceWebExterneService.findById(selection.getId());
			}

			taTServiceWebExterneService.remove(taTReception);
			values.remove(selection);
			
			if(!values.isEmpty()) {
				selection = values.get(0);
			}else {
				selection=new TaTServiceWebExterneDTO();
			}

			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Type service web externe", "actSupprimer"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Type service web externe", e.getCause().getCause().getCause().getCause().getMessage()));
		}
	}


	public void actEnregistrer(ActionEvent actionEvent){
		try {
			TaTypeCodeBarre retour = null;
			taTReception=new TaTServiceWebExterne();
			if(nouveau.getId()==null || taTServiceWebExterneService.findById(nouveau.getId()) == null){
				taTReception=new TaTServiceWebExterne();
				mapperUIToModel.map(nouveau, taTReception);
				taTReception = taTServiceWebExterneService.merge(taTReception, ITaTCiviliteServiceRemote.validationContext);
				mapperModelToUI.map(taTReception, nouveau);
				values= taTServiceWebExterneService.selectAllDTO();
				nouveau = values.get(0);
				nouveau = new TaTServiceWebExterneDTO();

				modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

			}else{
				if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_EDITION)==0){
					taTReception = taTServiceWebExterneService.findById(nouveau.getId());
					mapperUIToModel.map(nouveau, taTReception);
					taTReception = taTServiceWebExterneService.merge(taTReception, ITaTCiviliteServiceRemote.validationContext);
					mapperModelToUI.map(taTReception, nouveau);
					values= taTServiceWebExterneService.selectAllDTO();
					nouveau = values.get(0);
					nouveau = new TaTServiceWebExterneDTO();

					modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
				}
				else{
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "Code déja utilisé"));
				}
			}
			if(modeEcranDefaut!=null && modeEcranDefaut.equals(C_DIALOG)) {
				PrimeFaces.current().dialog().closeDynamic(taTReception);
			}
		} catch(Exception e) {
			e=ThrowableExceptionLgr.renvoieCauseRuntimeException(e);
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			//context.addMessage(null, new FacesMessage("Tiers", "actEnregistrer")); 
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "TypeFamilleUnite", e.getMessage()));
		}
	}

	public void actAnnuler(ActionEvent actionEvent){
		if(values.size()>= 1){
			selection = values.get(0);
		}		
		nouveau = new TaTServiceWebExterneDTO();
		
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}

	public List<TaTServiceWebExterneDTO> getValues(){  
		return values;
	}

	public TaTServiceWebExterneDTO getNouveau() {
		return nouveau;
	}

	public void setNouveau(TaTServiceWebExterneDTO newTaTServiceWebExterne) {
		this.nouveau = newTaTServiceWebExterne;
	}

	public TaTServiceWebExterneDTO getSelection() {
		return selection;
	}

	public void setSelection(TaTServiceWebExterneDTO selectedTaTServiceWebExterne) {
		this.selection = selectedTaTServiceWebExterne;
	}

	public void setValues(List<TaTServiceWebExterneDTO> values) {
		this.values = values;
	}


	public void onRowSelect(SelectEvent event) {  
		if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_CONSULTATION)==0) {
			nouveau = selection;
		}
	}

	public List<TaTServiceWebExterneDTO> getFilteredValues() {
		return filteredValues;
	}

	public void setFilteredValues(List<TaTServiceWebExterneDTO> filteredValues) {
		this.filteredValues = filteredValues;
	}
	
	public void refresh(){
		values = taTServiceWebExterneService.findByCodeLight(null);
		Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		modeEcranDefaut = params.get("modeEcranDefaut");
		if(modeEcranDefaut!=null && modeEcranDefaut.equals(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION))) {
			modeEcranDefaut = C_DIALOG;
			actInserer(null);
		} else if(modeEcranDefaut!=null && modeEcranDefaut.equals(modeEcran.modeString(EnumModeObjet.C_MO_EDITION))) {
			modeEcranDefaut = C_DIALOG;
			if(params.get("idEntity")!=null) {
				try {
					selection = taTServiceWebExterneService.findByIdDTO(LibConversion.stringToInteger(params.get("idEntity")));
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
			sessionMap.put("codeTVA", taTServiceWebExterneService.findById(selection.getId()));

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
//				TaTServiceWebExterneDTO temp=new TaTServiceWebExterneDTO();
//				PropertyUtils.setProperty(temp, nomChamp, value);
//				taTReceptionService.validateEntityProperty(temp, nomChamp, ITaTServiceWebExterneServiceRemote.validationContext );
//			} catch(Exception e) {
//				msg += e.getMessage();
//				throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
//			}
			
			String messageComplet = "";
			try {
				String nomChamp =  (String) component.getAttributes().get("nomChamp");
				//validation automatique via la JSR bean validation
				ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
				Set<ConstraintViolation<TaTServiceWebExterneDTO>> violations = factory.getValidator().validateValue(TaTServiceWebExterneDTO.class,nomChamp,value);
				if (violations.size() > 0) {
					messageComplet = "Erreur de validation : ";
					for (ConstraintViolation<TaTServiceWebExterneDTO> cv : violations) {
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
						if(selection.getCodeTServiceWebExterne()!=null && value!=null && !selection.getCodeTServiceWebExterne().equals(""))
						{
							if(value instanceof TaTServiceWebExterne)
								changement=((TaTServiceWebExterne) value).getCodeTServiceWebExterne().equals(selection.getCodeTServiceWebExterne());
							else if(value instanceof String)
							changement=!value.equals(selection.getCodeTServiceWebExterne());
						}
						if(changement && modeEcran.dataSetEnModeModification()){
							FacesContext context = FacesContext.getCurrentInstance();  
							context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Type service web externe", Const.C_MESSAGE_CHANGEMENT_CODE));
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
	        
	        PrimeFaces.current().dialog().openDynamic("admin/dialog_type_servicewebexterne", options, params);
	        
		}
		
		public void handleReturnDialogTypes(SelectEvent event) {
			if(event!=null && event.getObject()!=null) {
				taTReception = (TaTServiceWebExterne) event.getObject();
				
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
	        
	        PrimeFaces.current().dialog().openDynamic("admin/dialog_type_servicewebexterne", options, params);

		}
		// Dima -  Fin
}  
