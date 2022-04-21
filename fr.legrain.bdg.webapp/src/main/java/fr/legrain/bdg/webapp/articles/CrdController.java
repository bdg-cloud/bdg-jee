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

import fr.legrain.article.titretransport.dto.TaTitreTransportDTO;
import fr.legrain.article.titretransport.model.TaTitreTransport;
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
public class CrdController implements Serializable {  

	private List<TaTitreTransportDTO> values; 
	private List<TaTitreTransportDTO> filteredValues; 
	private TaTitreTransportDTO nouveau ;
	private TaTitreTransportDTO selection ;

	private TaTitreTransport taTva = new TaTitreTransport();

	private String modeEcranDefaut;
	private static final String C_DIALOG = "dialog";

	private ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();

	private @EJB ITaTitreTransportServiceRemote taTitreTransportService;
	
	private LgrDozerMapper<TaTitreTransportDTO,TaTitreTransport> mapperUIToModel  = new LgrDozerMapper<TaTitreTransportDTO,TaTitreTransport>();
	private LgrDozerMapper<TaTitreTransport,TaTitreTransportDTO> mapperModelToUI  = new LgrDozerMapper<TaTitreTransport,TaTitreTransportDTO>();

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

	public CrdController() {  
	}  

	public void actInserer(ActionEvent actionEvent){
		nouveau = new TaTitreTransportDTO();
		modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
	}

	public void actModifier(ActionEvent actionEvent){
		nouveau = selection;
		modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
	}

	public void actSupprimer(){
		TaTitreTransport taTva = new TaTitreTransport();
		try {
			if(selection!=null && selection.getId()!=null){
				taTva = taTitreTransportService.findById(selection.getId());
			}

			taTitreTransportService.remove(taTva);
			values.remove(selection);
			
			if(!values.isEmpty()) {
				selection = values.get(0);
			}else {
				selection=new TaTitreTransportDTO();
			}

			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("TitreTransport", "actSupprimer"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "TitreTransport", e.getCause().getCause().getCause().getCause().getMessage()));
		}
	}

	public void actEnregistrer(ActionEvent actionEvent){
		try {
			TaTitreTransportDTO retour = null;
			taTva=new TaTitreTransport();
			if(nouveau.getId()==null || taTitreTransportService.findById(nouveau.getId()) == null){
				mapperUIToModel.map(nouveau, taTva);
				taTva = taTitreTransportService.merge(taTva, ITaTitreTransportServiceRemote.validationContext);
				mapperModelToUI.map(taTva, nouveau);
				values= taTitreTransportService.selectAllDTO();
				nouveau = values.get(0);
				nouveau = new TaTitreTransportDTO();

				modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

			}else{
				if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_EDITION)==0){
					taTva = taTitreTransportService.findById(nouveau.getId());
					mapperUIToModel.map(nouveau, taTva);
					taTva = taTitreTransportService.merge(taTva, ITaTitreTransportServiceRemote.validationContext);
					mapperModelToUI.map(taTva, nouveau);
					values= taTitreTransportService.selectAllDTO();
					nouveau = values.get(0);
					nouveau = new TaTitreTransportDTO();

					modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
				}
				else{
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "Code déja utilisé"));
				}
			}
			if(modeEcranDefaut!=null && modeEcranDefaut.equals(C_DIALOG)) {
				PrimeFaces.current().dialog().closeDynamic(taTva);
			}
		} catch(Exception e) {
			e=ThrowableExceptionLgr.renvoieCauseRuntimeException(e);
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "TypeCodeTitreTransport", e.getMessage()));
		}
	}

	public void actAnnuler(ActionEvent actionEvent){
		if(values.size()>= 1){
			selection = values.get(0);
		}		
		nouveau = new TaTitreTransportDTO();

		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}

	public List<TaTitreTransportDTO> getValues(){  
		return values;
	}

	public TaTitreTransportDTO getNouveau() {
		return nouveau;
	}

	public void setNouveau(TaTitreTransportDTO newTaTitreTransport) {
		this.nouveau = newTaTitreTransport;
	}

	public TaTitreTransportDTO getSelection() {
		return selection;
	}

	public void setSelection(TaTitreTransportDTO selectedTaTitreTransport) {
		this.selection = selectedTaTitreTransport;
	}

	public void setValues(List<TaTitreTransportDTO> values) {
		this.values = values;
	}

	public void onRowSelect(SelectEvent event) {  
		if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_CONSULTATION)==0) {
			nouveau = selection;
		}
	}

	public List<TaTitreTransportDTO> getFilteredValues() {
		return filteredValues;
	}

	public void setFilteredValues(List<TaTitreTransportDTO> filteredValues) {
		this.filteredValues = filteredValues;
	}

	public void refresh(){
		values = taTitreTransportService.selectAllDTO();
		Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		modeEcranDefaut = params.get("modeEcranDefaut");
		if(modeEcranDefaut!=null && modeEcranDefaut.equals(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION))) {
			modeEcranDefaut = C_DIALOG;
			actInserer(null);
		} else if(modeEcranDefaut!=null && modeEcranDefaut.equals(modeEcran.modeString(EnumModeObjet.C_MO_EDITION))) {
			modeEcranDefaut = C_DIALOG;
			if(params.get("idEntity")!=null) {
				try {
					selection = taTitreTransportService.findByIdDTO(LibConversion.stringToInteger(params.get("idEntity")));
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
		context.addMessage(null, new FacesMessage("CodeTitreTransport", "actImprimer"));
		}
		try {

			//		FacesContext facesContext = FacesContext.getCurrentInstance();
			//		HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);

			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			sessionMap.put("codeTitreTransport", taTitreTransportService.findById(selection.getId()));

			//			session.setAttribute("tiers", taTiersService.findById(selectedTaTiersDTO.getId()));
			System.out.println("CodeTitreTransportController.actImprimer()");
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
//			TaTitreTransportDTO temp=new TaTitreTransportDTO();
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
			Set<ConstraintViolation<TaTitreTransportDTO>> violations = factory.getValidator().validateValue(TaTitreTransportDTO.class,nomChamp,value);
			if (violations.size() > 0) {
				messageComplet = "Erreur de validation : ";
				for (ConstraintViolation<TaTitreTransportDTO> cv : violations) {
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
			if(nomChamp.equals(Const.C_CODE_TITRE_TRANSPORT)) {
					boolean changement=false;
					if(selection.getCodeTitreTransport()!=null && value!=null && !selection.getCodeTitreTransport().equals(""))
					{
						if(value instanceof TaTitreTransport)
							changement=((TaTitreTransport) value).getCodeTitreTransport().equals(selection.getCodeTitreTransport());
						else if(value instanceof String)
						changement=!value.equals(selection.getCodeTitreTransport());
					}
					if(changement && modeEcran.dataSetEnModeModification()){
						FacesContext context = FacesContext.getCurrentInstance();  
						context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Code tva", Const.C_MESSAGE_CHANGEMENT_CODE));
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
		options.put("modal", true);

		Map<String,List<String>> params = new HashMap<String,List<String>>();
		List<String> list = new ArrayList<String>();
		list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
		params.put("modeEcranDefaut", list);

		PrimeFaces.current().dialog().openDynamic("articles/dialog_type_crd", options, params);

	}

	public void handleReturnDialogTypes(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			taTva = (TaTitreTransport) event.getObject();
			
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

		PrimeFaces.current().dialog().openDynamic("articles/dialog_type_crd", options, params);

	}
	// Dima -  Fin	
}  
