package fr.legrain.bdg.webapp.app;


import java.io.Serializable;
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

import fr.legrain.article.model.TaUnite;
import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaUniteServiceRemote;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.paiement.service.remote.ITaLogPaiementInternetServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTCiviliteServiceRemote;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.ModeObjetEcranServeur;
import fr.legrain.paiement.dto.TaLogPaiementInternetDTO;
import fr.legrain.paiement.model.TaLogPaiementInternet;



@Named
@ViewScoped  
public class GestionPaiementInternetController implements Serializable {  

	private List<TaLogPaiementInternetDTO> values; 
	private List<TaLogPaiementInternetDTO> filteredValues; 
	private TaLogPaiementInternetDTO nouveau ;
	private TaLogPaiementInternet taLogPaiementInternet = new TaLogPaiementInternet();
	private String numLogPaiementInternet;
	
	private TaUnite taUnite;
	private TaUnite taUnite2;
	private TaLogPaiementInternetDTO selection ;
	
	private String paramRefresh;
//	private Boolean refreshExecute;


	private String modeEcranDefaut;
	private static final String C_DIALOG = "dialog";

	private ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();

	private @EJB ITaLogPaiementInternetServiceRemote taLogPaiementInternetService;
	private @EJB ITaArticleServiceRemote taArticleService;
	private @EJB ITaUniteServiceRemote taUniteService;
	
	private LgrDozerMapper<TaLogPaiementInternetDTO,TaLogPaiementInternet> mapperUIToModel  = new LgrDozerMapper<TaLogPaiementInternetDTO,TaLogPaiementInternet>();
	private LgrDozerMapper<TaLogPaiementInternet,TaLogPaiementInternetDTO> mapperModelToUI  = new LgrDozerMapper<TaLogPaiementInternet,TaLogPaiementInternetDTO>();

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
	
	public GestionPaiementInternetController() {  
	}
	
	public void refresh(){
		
//		values = taLogPaiementInternetService.findAllLight();
		values = taLogPaiementInternetService.selectAllDTO();
		Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		modeEcranDefaut = params.get("modeEcranDefaut");
		if(modeEcranDefaut!=null && modeEcranDefaut.equals(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION))) {
			modeEcranDefaut = C_DIALOG;
			actInserer(null);
		} else if(modeEcranDefaut!=null && modeEcranDefaut.equals(modeEcran.modeString(EnumModeObjet.C_MO_EDITION))) {
			modeEcranDefaut = C_DIALOG;
			if(params.get("idEntity")!=null) {
				try {
					selection = taLogPaiementInternetService.findByIdDTO(LibConversion.stringToInteger(params.get("idEntity")));
				} catch (FinderException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			actModifier(null);
		} else {
			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
		}
//		refreshExecute=false;
	}


  
	
	public void actFermerDialog(ActionEvent actionEvent) {
		PrimeFaces.current().dialog().closeDynamic(null);
	}

	public void actAnnuler(ActionEvent actionEvent){
		if(values.size()>= 1){
			selection = values.get(0);
		}		
		nouveau = new TaLogPaiementInternetDTO();

		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}
	
	public void actEnregistrer(ActionEvent actionEvent){
		try {
			TaLogPaiementInternetDTO retour = null;
			taLogPaiementInternet=new TaLogPaiementInternet();
			if(nouveau.getId()==null || taLogPaiementInternetService.findById(nouveau.getId()) == null){
				mapperUIToModel.map(nouveau, taLogPaiementInternet);
				taLogPaiementInternet = taLogPaiementInternetService.merge(taLogPaiementInternet, ITaTCiviliteServiceRemote.validationContext);
				mapperModelToUI.map(taLogPaiementInternet, nouveau);
				values= taLogPaiementInternetService.selectAllDTO();
				nouveau = values.get(0);
				nouveau = new TaLogPaiementInternetDTO();

				modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

			}else{
				if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_EDITION)==0){
					taLogPaiementInternet = taLogPaiementInternetService.findById(nouveau.getId());
					mapperUIToModel.map(nouveau, taLogPaiementInternet);
					taLogPaiementInternet = taLogPaiementInternetService.merge(taLogPaiementInternet, ITaTCiviliteServiceRemote.validationContext);
					mapperModelToUI.map(taLogPaiementInternet, nouveau);
					values= taLogPaiementInternetService.selectAllDTO();
					nouveau = values.get(0);
					nouveau = new TaLogPaiementInternetDTO();

					modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
				}
				else{
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "Code déja utilisé"));
				}
			}
			if(modeEcranDefaut!=null && modeEcranDefaut.equals(C_DIALOG)) {
				PrimeFaces.current().dialog().closeDynamic(taLogPaiementInternet);
			}
		} catch(Exception e) {
			e=ThrowableExceptionLgr.renvoieCauseRuntimeException(e);
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "GestionLogPaiementInternet", e.getMessage()));
		}
	}

	public void actInserer(ActionEvent actionEvent) {
//		nouveau = new TaLogPaiementInternet();
//		selection = nouveau;
//		modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
	}

	public void actModifier(ActionEvent actionEvent){
		nouveau = selection;
		modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
//		try {
//			Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
//			if(params.get("idEntity")!=null) {
//									
//					
//						nouveau = taLogPaiementInternetService.findByIdDTO(LibConversion.stringToInteger(params.get("idEntity")));
//						taLogPaiementInternet = taLogPaiementInternetService.findById(LibConversion.stringToInteger(params.get("idEntity")));
//				}
//			modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
//		} catch (FinderException e) {
//			e.printStackTrace();
//		}		
	}

	public void actSupprimer(){

	}

	public void onRowSelect(SelectEvent event) {  
		if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_CONSULTATION)==0) {
			try {
				nouveau = taLogPaiementInternetService.findByIdDTO(selection.getId());
			} catch (FinderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public boolean editable() {
		return !modeEcran.dataSetEnModif();
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
			case "rowEditor":
				retour = false;
				break;
			default:
				break;
			}
			break;
		case C_MO_CONSULTATION:
			switch (bouton) {
			case "supprimer":
			case "inserer":
				retour = true; //On ne peut pas créer ou supprimer des lots sur cet écran, on peut uniquement les modifier pour les marqués terminés
				break;
			case "modifier":
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

	public List<TaLogPaiementInternetDTO> getValues(){  
		return values;
	}

	public TaLogPaiementInternetDTO getNouveau() {
		return nouveau;
	}

	public void setNouveau(TaLogPaiementInternetDTO newTaTTiers) {
		this.nouveau = newTaTTiers;
	}

	public TaLogPaiementInternetDTO getSelection() {
		return selection;
	}

	public void setSelection(TaLogPaiementInternetDTO selectedTaTTiers) {
		this.selection = selectedTaTTiers;
	}

	public void setValues(List<TaLogPaiementInternetDTO> values) {
		this.values = values;
	}

	public List<TaLogPaiementInternetDTO> getFilteredValues() {
		return filteredValues;
	}
	
	public void setFilteredValues(List<TaLogPaiementInternetDTO> filteredValues) {
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

	public String getNumLogPaiementInternet() {
		return numLogPaiementInternet;
	}

	public void setNumLogPaiementInternet(String numLogPaiementInternet) {
		this.numLogPaiementInternet = numLogPaiementInternet;
	}	

	public void onChangeQ ()
	{
	    
	}

	public TaUnite getTaUnite() {
		return taUnite;
	}

	public void setTaUnite(TaUnite taUnite) {
		this.taUnite = taUnite;
	}

	public TaUnite getTaUnite2() {
		return taUnite2;
	}

	public void setTaUnite2(TaUnite taUnite2) {
		this.taUnite2 = taUnite2;
	}
	
	public void validateObject(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		String messageComplet = "";
		try {
			String nomChamp =  (String) component.getAttributes().get("nomChamp");
			//validation automatique via la JSR bean validation
			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
			Set<ConstraintViolation<TaLogPaiementInternetDTO>> violations = factory.getValidator().validateValue(TaLogPaiementInternetDTO.class,nomChamp,value);
			if (violations.size() > 0) {
				messageComplet = "Erreur de validation : ";
				for (ConstraintViolation<TaLogPaiementInternetDTO> cv : violations) {
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

		boolean changement=false;

		try {				
			if(nomChamp.equals(Const.C_CODE_ARTICLE)) {
				
			}

			return false;

		} catch (Exception e) {

		}
		return false;
	}			 

	public String getParamRefresh() {
		return paramRefresh;
	}

	public void setParamRefresh(String paramRefresh) {
		this.paramRefresh = paramRefresh;
	}	

}  
