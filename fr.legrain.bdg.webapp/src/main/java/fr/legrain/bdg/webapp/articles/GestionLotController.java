package fr.legrain.bdg.webapp.articles;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
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
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;

import fr.legrain.article.dto.TaLotDTO;
import fr.legrain.article.model.TaArticle;
import fr.legrain.article.model.TaLot;
import fr.legrain.article.model.TaUnite;
import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaLotServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaUniteServiceRemote;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.tiers.service.remote.ITaTCiviliteServiceRemote;
import fr.legrain.bdg.webapp.codebarre.CodeBarreParam;
import fr.legrain.bdg.webapp.codebarre.GenerationCodeBarreController;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.ModeObjetEcranServeur;



@Named
@ViewScoped  
public class GestionLotController implements Serializable {  

	private List<TaLotDTO> values; 
	private List<TaLotDTO> filteredValues; 
	private TaLotDTO nouveau ;
	private TaLot taLot = new TaLot();
	private String numLot;
	
	private TaUnite taUnite;
	private TaUnite taUnite2;
	private TaLotDTO selection ;
	
	private String paramRefresh;
//	private Boolean refreshExecute;


	private String modeEcranDefaut;
	private static final String C_DIALOG = "dialog";

	private ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();

	private @EJB ITaLotServiceRemote taLotService;
	private @EJB ITaArticleServiceRemote taArticleService;
	private @EJB ITaUniteServiceRemote taUniteService;
	
	private LgrDozerMapper<TaLotDTO,TaLot> mapperUIToModel  = new LgrDozerMapper<TaLotDTO,TaLot>();
	private LgrDozerMapper<TaLot,TaLotDTO> mapperModelToUI  = new LgrDozerMapper<TaLot,TaLotDTO>();

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
	
	public GestionLotController() {  
	}
	
	public void refresh(){
		
//		values = taLotService.findAllLight();
		suppressionLotsNonUtilises();//rajout isa pour palier au problème de lot orphelins suite à modif dans br ou fab
		values = taLotService.selectAllDTOLight();
		Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		modeEcranDefaut = params.get("modeEcranDefaut");
		if(modeEcranDefaut!=null && modeEcranDefaut.equals(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION))) {
			modeEcranDefaut = C_DIALOG;
			actInserer(null);
		} else if(modeEcranDefaut!=null && modeEcranDefaut.equals(modeEcran.modeString(EnumModeObjet.C_MO_EDITION))) {
			modeEcranDefaut = C_DIALOG;
			if(params.get("idEntity")!=null) {
				try {
					selection = taLotService.findByIdDTO(LibConversion.stringToInteger(params.get("idEntity")));
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

	public void actImprimerEtiquetteCB128(ActionEvent actionEvent) {

		String numLot = (String)actionEvent.getComponent().getAttributes().get("numLot");
		CodeBarreParam param = new CodeBarreParam();
		try {
			param.setTaLot(taLotService.findByCode(numLot));
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		param.setTaLot(taLot);
		GenerationCodeBarreController.actDialoguePaiementEcheanceParCarte(param);
		
		
	}
  
	public void actImprimerListeGestionDesLots(ActionEvent actionEvent) {
		
		try {
			
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			
			sessionMap.put("listeGestionDesLots", values);

			} catch (Exception e) {
				e.printStackTrace();
			}
	}    

	public void actFermerDialog(ActionEvent actionEvent) {
		PrimeFaces.current().dialog().closeDynamic(null);
	}

	public void actAnnuler(ActionEvent actionEvent){
		if(values.size()>= 1){
			selection = values.get(0);
		}		
		nouveau = new TaLotDTO();

		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}
	
	public void actEnregistrer(ActionEvent actionEvent){
		try {
			TaLotDTO retour = null;
			taLot=new TaLot();
			if(nouveau.getId()==null || taLotService.findById(nouveau.getId()) == null){
				mapperUIToModel.map(nouveau, taLot);
				taLot = taLotService.merge(taLot, ITaTCiviliteServiceRemote.validationContext);
				mapperModelToUI.map(taLot, nouveau);
				values= taLotService.selectAllDTOLight();
				nouveau = values.get(0);
				nouveau = new TaLotDTO();

				modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

			}else{
				if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_EDITION)==0){
					taLot = taLotService.findById(nouveau.getId());
					mapperUIToModel.map(nouveau, taLot);
					taLot = taLotService.merge(taLot, ITaTCiviliteServiceRemote.validationContext);
					mapperModelToUI.map(taLot, nouveau);
					values= taLotService.selectAllDTOLight();
					nouveau = values.get(0);
					nouveau = new TaLotDTO();

					modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
				}
				else{
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "Code déja utilisé"));
				}
			}
			if(modeEcranDefaut!=null && modeEcranDefaut.equals(C_DIALOG)) {
				PrimeFaces.current().dialog().closeDynamic(taLot);
			}
		} catch(Exception e) {
			e=ThrowableExceptionLgr.renvoieCauseRuntimeException(e);
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "GestionLot", e.getMessage()));
		}
	}

	public void actInserer(ActionEvent actionEvent) {
//		nouveau = new TaLot();
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
//						nouveau = taLotService.findByIdDTO(LibConversion.stringToInteger(params.get("idEntity")));
//						taLot = taLotService.findById(LibConversion.stringToInteger(params.get("idEntity")));
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
				nouveau = taLotService.findByIdDTO(selection.getId());
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

	public List<TaLotDTO> getValues(){  
		return values;
	}

	public TaLotDTO getNouveau() {
		return nouveau;
	}

	public void setNouveau(TaLotDTO newTaTTiers) {
		this.nouveau = newTaTTiers;
	}

	public TaLotDTO getSelection() {
		return selection;
	}

	public void setSelection(TaLotDTO selectedTaTTiers) {
		this.selection = selectedTaTTiers;
	}

	public void setValues(List<TaLotDTO> values) {
		this.values = values;
	}

	public List<TaLotDTO> getFilteredValues() {
		return filteredValues;
	}
	
	public void setFilteredValues(List<TaLotDTO> filteredValues) {
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

	public List<TaUnite> UniteAutoComplete(String query) {
		List<TaUnite> allValues =new LinkedList<TaUnite>();
		if(taLot.getTaArticle().getTaUnite1()!=null)
			allValues.add(taLot.getTaArticle().getTaUnite1());
//		if(taLot.getTaArticle().getTaPrixes()!=null){
//			for (TaPrix prix : taLot.getTaArticle().getTaPrixes()) {
//				if(prix.getTaUnite1()!=null && !allValues.contains(prix.getTaUnite1()))
//					allValues.add(prix.getTaUnite1());
//			}
//		}
        List<TaUnite> filteredValues = new ArrayList<TaUnite>();
         
        for (int i = 0; i < allValues.size(); i++) {
        	TaUnite obj = allValues.get(i);
            if(query.equals("*") || obj.getCodeUnite().toLowerCase().contains(query.toLowerCase())) {
                filteredValues.add(obj);
            }
        }
         
        return filteredValues;
    }
	
	public List<TaUnite> Unite2AutoComplete(String query) {
		List<TaUnite> allValues =new LinkedList<TaUnite>();

		
        List<TaUnite> filteredValues = new ArrayList<TaUnite>();
         
        for (int i = 0; i < allValues.size(); i++) {
        	TaUnite obj = allValues.get(i);
            if(query.equals("*") || obj.getCodeUnite().toLowerCase().contains(query.toLowerCase())) {
                filteredValues.add(obj);
            }
        }
         
        return filteredValues;
    }

	public List<TaArticle> ArticleAutoComplete(String query) {
        List<TaArticle> allValues = taArticleService.selectAll();
        List<TaArticle> filteredValues = new ArrayList<TaArticle>();
         
        for (int i = 0; i < allValues.size(); i++) {
        	TaArticle obj = allValues.get(i);
            if(query.equals("*") || obj.getCodeArticle().toLowerCase().contains(query.toLowerCase())) {
                filteredValues.add(obj);
            }
        }
         
        return filteredValues;
    }
	
	public void actDialogModifier(ActionEvent actionEvent){
		
		try {
		nouveau = selection;
		
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
        
        PrimeFaces.current().dialog().openDynamic("articles/dialog_lot", options, params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void handleReturnDialogLot(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			//nouveau=(TaLot) event.getObject();
			
		}
		refresh();
	}
	
	public void autcompleteSelection(SelectEvent event) {
		Object value = event.getObject();
//		FacesMessage msg = new FacesMessage("Selected", "Item:" + value);
		
		String nomChamp =  (String) event.getComponent().getAttributes().get("nomChamp");

		//validateUIField(nomChamp,value);
	}


	public String getNumLot() {
		return numLot;
	}

	public void setNumLot(String numLot) {
		this.numLot = numLot;
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
			Set<ConstraintViolation<TaLotDTO>> violations = factory.getValidator().validateValue(TaLotDTO.class,nomChamp,value);
			if (violations.size() > 0) {
				messageComplet = "Erreur de validation : ";
				for (ConstraintViolation<TaLotDTO> cv : violations) {
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


	public void onRowEdit(RowEditEvent event) {

			try {
				selection.getTermine();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

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
        
        PrimeFaces.current().dialog().openDynamic("articles/dialog_lot", options, params);
        
	}
	
	public void handleReturnDialogTypes(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			taLot = (TaLot) event.getObject();
			
		}
		refresh();
	}	

	public String getParamRefresh() {
		return paramRefresh;
	}

	public void setParamRefresh(String paramRefresh) {
		this.paramRefresh = paramRefresh;
	}

	public TaLotDTO rempliDTO(TaLot lot){
		if(lot!=null){			
			try {
				lot=taLotService.findByCode(lot.getNumLot());
				mapperModelToUI.map(lot, selection);
				if(values.contains(selection))values.add(selection);
			} catch (FinderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return selection;
	}
//	public Boolean getRefreshExecute() {
//		return refreshExecute;
//	}
//
//	public void setRefreshExecute(Boolean refreshExecute) {
//		this.refreshExecute = refreshExecute;
//	}	

	
	public void suppressionLotsNonUtilises(){
		taLotService.suppression_lot_non_utilise();
	}
}  
