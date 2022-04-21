package fr.legrain.moncompte.admin.webapp.admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.validator.ValidatorException;

import org.apache.commons.beanutils.PropertyUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.moncompte.service.remote.ITaTypeProduitServiceRemote;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.ModeObjetEcranServeur;
import fr.legrain.moncompte.admin.webapp.ConstWeb;
import fr.legrain.moncompte.model.TaTypeProduit;

@ManagedBean
@ViewScoped  
public class TypeProduitController implements Serializable {  

	private List<TaTypeProduit> values; 
	private List<TaTypeProduit> filteredValues; 
	//	private Boolean creation;
	//	private Boolean selectionne;
	private TaTypeProduit nouveau ;
	private TaTypeProduit selection ;
	//	private Boolean modification;
	private Boolean tableVide;
	
	// Dima - Début
	private TaTypeProduit TaTypeProduit ;
	private boolean listeSaisie; // variable pour déterminé la table active (0 - la liste, 1 - le saisie)
	// Dima -  Fin
	
	private String modeEcranDefaut;
	private static final String C_DIALOG = "dialog";

	private ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();

	private @EJB ITaTypeProduitServiceRemote taTypeProduitService;
	@PostConstruct
	public void postConstruct(){
		try {
			//			modification = false;
			//			creation = false;
//			setTableVide(false);
//			values =  TaTypeProduitService.selectAll();
//			if(values == null){
//				setTableVide(true);
//			}
//			if ( values.size()>=1){
//				selection = values.get(0);	
//			}
			refresh();
			if(values != null && !values.isEmpty()){
//			    setTableVide(true);
				selection = values.get(0);	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.setFilteredValues(values);
	}

	public TypeProduitController() {  
	}  

	//	public void create(ActionEvent actionEvent){
	//		nouveau = new TaTypeProduit();
	//		modification = false;
	//		creation = true;
	//	}
	//
	//	public void modification(ActionEvent actionEvent){
	//		nouveau = selection;
	//		creation = true;
	//		modification = true;
	//	}
	//
	//	public void suppression(){
	//		try {
	//			TaTypeProduitService.remove(selection);
	//			selection = values.get(0);	
	//		} catch (RemoveException e) {
	//			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", e.getMessage()));
	//			e.printStackTrace();
	//		}
	//		values = TaTypeProduitService.selectAll();
	//		selection = null;
	//	}
	//	public void onChangeQ ()
	//	{
	//
	//	}

	// Dima - Début
	public boolean isListeSaisie() {
		return listeSaisie;
	}

	public void setListeSaisie(boolean listeSaisie) {
		this.listeSaisie = listeSaisie;
	}
	// Dima -  Fin
	
	public void actInserer(ActionEvent actionEvent){
		nouveau = new TaTypeProduit();
		listeSaisie = true;
		modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
	}

	public void actModifier(ActionEvent actionEvent){
		listeSaisie = true;
		nouveau = selection;
		modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
	}

	public void actSupprimer(){
		try {
			taTypeProduitService.remove(selection);
			selection = values.get(0);	
		} catch (RemoveException e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", e.getMessage()));
			e.printStackTrace();
		}
		values = taTypeProduitService.selectAll();
		selection = null;
	}

//	public void actEnregistrer(ActionEvent actionEvent){
//		try {
//			if(nouveau.getCodeTypeCodeBarre() == null || nouveau.getCodeTypeCodeBarre().equals("") ){
//				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "champ code obligatoire"));
//			}
//			else{ 
//				if(nouveau.getLibelle() == null || nouveau.getLibelle().equals("") ){
//					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "champ libellé obligatoire"));
//				}
//				else{ 
//					if(TaTypeProduitService.findByCode(nouveau.getCodeTypeCodeBarre()) == null){
//						TaTypeProduit retour = TaTypeProduitService.merge(nouveau, ITypeCodeBarreServiceRemote.validationContext);
//						values= TaTypeProduitService.selectAll();
//						selection = values.get(0);
//						//					creation = false;
//						//					modification = false;
//						nouveau = new TaTypeProduit();
//						listeSaisie = false;
//						modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
//
//						if(modeEcranDefaut!=null && modeEcranDefaut.equals(C_DIALOG)) {
//							listeSaisie = false;
//							RequestContext.getCurrentInstance().closeDialog(retour);
//						}
//					}else{
//						if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_EDITION)==0){
//							TaTypeProduitService.merge(nouveau, ITypeCodeBarreServiceRemote.validationContext);
//							values= TaTypeProduitService.selectAll();
//							selection = values.get(0);
//							//						creation = false;
//							//						modification = false;
//							nouveau = new TaTypeProduit();
//							listeSaisie = false;
//							modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
//						}
//						else{
//							FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "Code déja utilisé"));
//						}
//					}
//				}
//			}
//			listeSaisie = false;
//		} catch (FinderException e) {
//			e.printStackTrace();
//			listeSaisie = false;
//		}
//		listeSaisie = false;
//	}
	
	public void actEnregistrer(ActionEvent actionEvent){
		try {
			TaTypeProduit retour = null;
			if(taTypeProduitService.findByCode(nouveau.getCode()) == null){
				retour = taTypeProduitService.merge(nouveau, ITaTypeProduitServiceRemote.validationContext);
				values= taTypeProduitService.selectAll();
				selection = values.get(0);
				nouveau = new TaTypeProduit();

				modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

			}else{
				if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_EDITION)==0){
					retour = taTypeProduitService.merge(nouveau, ITaTypeProduitServiceRemote.validationContext);
					values= taTypeProduitService.selectAll();
					selection = values.get(0);
					nouveau = new TaTypeProduit();

					modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
				}
				else{
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "Code déja utilisé"));
				}
			}
			if(modeEcranDefaut!=null && modeEcranDefaut.equals(C_DIALOG)) {
				 PrimeFaces.current().dialog().closeDynamic(retour);
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
//		values= TaTypeProduitService.selectAll();
		if(values.size()>= 1){
			selection = values.get(0);
			listeSaisie = false;
		}		
		nouveau = new TaTypeProduit();
//		creation = false;
//		modification = false;
		listeSaisie = false;
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}

	public List<TaTypeProduit> getValues(){  
		//List<Value> l = new LinkedList<Value>();
		return values;
	}

//	public Boolean getModification() {
//		return modification;
//	}
//	public void setModification(Boolean modification) {
//		this.modification = modification;
//	}

	public TaTypeProduit getNouveau() {
		return nouveau;
	}

	public void setNouveau(TaTypeProduit newTaTypeProduit) {
		this.nouveau = newTaTypeProduit;
	}

	public TaTypeProduit getSelection() {
		return selection;
	}

	public void setSelection(TaTypeProduit selectedTaTypeProduit) {
		this.selection = selectedTaTypeProduit;
	}

	public void setValues(List<TaTypeProduit> values) {
		this.values = values;
	}

//	public Boolean getCreation() {
//		return creation;
//	}
//
//	public void setCreation(Boolean creationE) {
//		this.creation = creationE;
//	}
//
//	public Boolean getSelectionne() {
//		return selectionne;
//	}
//
//	public void setSelectionne(Boolean selectionne) {
//		this.selectionne = selectionne;
//	}
//
//	public void onRowSelect(SelectEvent event)
//	{
//		selectionne = true;
//	}
//	public void onRowUnSelect(UnselectEvent event)
//	{
//		selectionne = false;
//	}

	public void onRowSelect(SelectEvent event) {  
		if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_CONSULTATION)==0) {
			nouveau = selection;
		}
	}

	public List<TaTypeProduit> getFilteredValues() {
		return filteredValues;
	}

	public void setFilteredValues(List<TaTypeProduit> filteredValues) {
		this.filteredValues = filteredValues;
	}

	public Boolean getTableVide() {
		return tableVide;
	}

	public void setTableVide(Boolean tableVide) {
		this.tableVide = tableVide;
	}

//	public void refresh(){
//		values = TaTypeProduitService.selectAll();
//		Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
//		modeEcranDefaut = params.get("modeEcranDefaut");
//
//		if(modeEcranDefaut!=null && modeEcranDefaut.equals(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION))) {
//			modeEcranDefaut = C_DIALOG;
//			actInserer(null);
//		} else {
//			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
//		}
//	}
	
	public void refresh(){
		values = taTypeProduitService.selectAll();
		Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		modeEcranDefaut = params.get("modeEcranDefaut");
		if(modeEcranDefaut!=null && modeEcranDefaut.equals(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION))) {
			modeEcranDefaut = C_DIALOG;
			actInserer(null);
		} else if(modeEcranDefaut!=null && modeEcranDefaut.equals(modeEcran.modeString(EnumModeObjet.C_MO_EDITION))) {
			modeEcranDefaut = C_DIALOG;
			if(params.get("idEntity")!=null) {
				try {
					selection = taTypeProduitService.findById(LibConversion.stringToInteger(params.get("idEntity")));
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
		context.addMessage(null, new FacesMessage("CodeBarre", "actImprimer"));
		}
		try {

			//		FacesContext facesContext = FacesContext.getCurrentInstance();
			//		HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);

			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			sessionMap.put("codeBarre", taTypeProduitService.findById(selection.getIdTypeProduit()));

			//			session.setAttribute("tiers", taTiersService.findById(selectedTaTiersDTO.getId()));
			System.out.println("CodeBarreController.actImprimer()");
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// Dima - Début
	public void validateObject(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		String msg = "";
		try {
			String nomChamp =  (String) component.getAttributes().get("nomChamp");
			validateUIField(nomChamp,value);
			TaTypeProduit temp=new TaTypeProduit();
			PropertyUtils.setProperty(temp, nomChamp, value);
			taTypeProduitService.validateEntityProperty(temp, nomChamp, ITaTypeProduitServiceRemote.validationContext );

		} catch(Exception e) {
			msg += e.getMessage();
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
		}
	}

	public boolean validateUIField(String nomChamp,Object value) {
		try {
			if(nomChamp!=null) {
				if(nomChamp.equals(Const.C_TYPE_CODE_BARRE_ARTICLE)) {
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
        
        PrimeFaces.current().dialog().openDynamic("admin/dialog_type_produit", options, params);
        
	}
	
	public void handleReturnDialogTypes(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			TaTypeProduit = (TaTypeProduit) event.getObject();
			refresh();
		}
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
        list2.add(LibConversion.integerToString(selection.getIdTypeProduit()));
        params.put("idEntity", list2);
        
        PrimeFaces.current().dialog().openDynamic("admin/dialog_type_produit", options, params);

	}
	// Dima -  Fin
}  
