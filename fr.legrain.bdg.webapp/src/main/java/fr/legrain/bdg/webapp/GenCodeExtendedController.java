package fr.legrain.bdg.webapp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.validator.ValidatorException;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.beanutils.PropertyUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaFabricationServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaLotServiceRemote;
import fr.legrain.bdg.controle.service.remote.ITaGenCodeExServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaBonReceptionServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaDevisServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceLocal;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.controle.model.TaGenCodeEx;
import fr.legrain.dossier.model.TaInfoEntreprise;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.ModeObjetEcranServeur;

@Named
@ViewScoped  
public class GenCodeExtendedController implements Serializable {  

	private List<TaGenCodeEx> values; 
	private List<TaGenCodeEx> filteredValues; 
	//	private Boolean creation;
	//	private Boolean selectionne;
	private TaGenCodeEx nouveau ;
	private TaGenCodeEx selection ;
	//	private Boolean modification;
	private Boolean tableVide;
	private int caretPosition;
//	private String codeVerifie;

	private String modeEcranDefaut;
	private static final String C_DIALOG = "dialog";
	
	private ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();

	private @EJB ITaGenCodeExServiceRemote genCodeExService;
	private @EJB ITaArticleServiceRemote taArticleService;
	private @EJB ITaTiersServiceRemote taTiersService;
	private @EJB ITaBonReceptionServiceRemote taBonReceptionService;
	private @EJB ITaFabricationServiceRemote taFabricationService;
	private @EJB ITaDevisServiceRemote taDevisService;
	private @EJB ITaLotServiceRemote taLotService;
	@Inject private ITaInfoEntrepriseServiceLocal entrepriseService;
	
	private TaInfoEntreprise taInfoEntreprise;
	
	@PostConstruct
	public void postConstruct(){
		try {
			//			modification = false;
			//			creation = false;
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

	public GenCodeExtendedController() {  
	}  

	public void actInserer(ActionEvent actionEvent){
//		nouveau = new TaGenCodeEx();
//		modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
	}

	public void actModifier(ActionEvent actionEvent){
		nouveau = selection;
		try {
			validateUIField(Const.C_VALEUR_GENCODE, nouveau.getValeurGenCode());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
	}

	public void actSupprimer(){
		try {
			genCodeExService.remove(selection);
			selection = values.get(0);	
		} catch (RemoveException e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", e.getMessage()));
			e.printStackTrace();
		}
		values = genCodeExService.selectAll();
		selection = null;
	}

	//	public void onChangeQ (){}

	public void actEnregistrer(ActionEvent actionEvent){
		try {
			TaGenCodeEx retour = null;
			validateUIField(Const.C_VALEUR_GENCODE, nouveau.getValeurGenCode());
			if(genCodeExService.findByCode(nouveau.getCleGencode()) == null){
				retour = genCodeExService.merge(nouveau, ITaGenCodeExServiceRemote.validationContext);
				values= genCodeExService.selectAll();
				selection = values.get(0);
				nouveau = new TaGenCodeEx();

				modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

			}else{
				if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_EDITION)==0){
					retour = genCodeExService.merge(nouveau, ITaGenCodeExServiceRemote.validationContext);
					values= genCodeExService.selectAll();
					selection = values.get(0);
					nouveau = new TaGenCodeEx();

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
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "GencodeExtended", e.getMessage()));
		}
	}
	public void recupMatch(String match){
		if(nouveau!=null){
//			if(caretPosition==0)caretPosition=1;
		String valeurInitiale=nouveau.getValeurGenCode();
		String un=valeurInitiale.substring(0, caretPosition);
		String deux=valeurInitiale.substring(caretPosition);
		
		nouveau.setValeurGenCode(un+match+deux);
		try {
			validateUIField(Const.C_VALEUR_GENCODE, nouveau.getValeurGenCode());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	
	}
	public void numero(String match){
		recupMatch(match);
	
	}
	public void date(String match){
		recupMatch(match);
	}
	public void fournisseur(String match){
		recupMatch(match);
	}
	public void description(String match){
		recupMatch(match);
	}	
	public void actAnnuler(ActionEvent actionEvent){
//		values= genCodeExService.selectAll();
		if(values.size()>= 1){
			selection = values.get(0);
		}		
		nouveau = new TaGenCodeEx();
		//		creation = false;
		//		modification = false;

		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}

	public List<TaGenCodeEx> getValues(){  
		//List<Value> l = new LinkedList<Value>();
		return values;
	}

	//	public Boolean getModification() {
	//		return modification;
	//	}
	//	public void setModification(Boolean modification) {
	//		this.modification = modification;
	//	}

	public TaGenCodeEx getNouveau() {
		return nouveau;
	}

	public void setNouveau(TaGenCodeEx newTaGenCodeEx) {
		this.nouveau = newTaGenCodeEx;
	}

	public TaGenCodeEx getSelection() {
		return selection;
	}

	public void setSelection(TaGenCodeEx selectedTaGenCodeEx) {
		this.selection = selectedTaGenCodeEx;
	}

	public void setValues(List<TaGenCodeEx> values) {
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

	public void onRowSelect(SelectEvent event) {  
		if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_CONSULTATION)==0) {
			nouveau = selection;
		}
	}

	//	public void onRowUnSelect(UnselectEvent event)
	//	{
	//		selectionne = false;
	//	}

	public List<TaGenCodeEx> getFilteredValues() {
		return filteredValues;
	}

	public void setFilteredValues(List<TaGenCodeEx> filteredValues) {
		this.filteredValues = filteredValues;
	}

	public Boolean getTableVide() {
		return tableVide;
	}

	public void setTableVide(Boolean tableVide) {
		this.tableVide = tableVide;
	}

	public void refresh(){
		values = genCodeExService.selectAll();
		Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		modeEcranDefaut = params.get("modeEcranDefaut");
		if(modeEcranDefaut!=null && modeEcranDefaut.equals(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION))) {
			modeEcranDefaut = C_DIALOG;
			actInserer(null);
		} else if(modeEcranDefaut!=null && modeEcranDefaut.equals(modeEcran.modeString(EnumModeObjet.C_MO_EDITION))) {
			modeEcranDefaut = C_DIALOG;
			if(params.get("idEntity")!=null) {
				try {
					selection = genCodeExService.findById(LibConversion.stringToInteger(params.get("idEntity")));
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
	
	public void validateObject(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		String msg = "";
		try {
			String nomChamp =  (String) component.getAttributes().get("nomChamp");
			validateUIField(nomChamp,value);
			TaGenCodeEx temp=new TaGenCodeEx();
			PropertyUtils.setProperty(temp, nomChamp, value);
			genCodeExService.validateEntityProperty(temp, nomChamp, ITaGenCodeExServiceRemote.validationContext );
		} catch(Exception e) {
			msg += e.getMessage();
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
		}
	}
	
	 public static final int stringOccur(String text, String string) {
		    return regexOccur(text, Pattern.quote(string));
		}

	 public static final int regexOccur(String text, String regex) {
		    Matcher matcher = Pattern.compile(regex).matcher(text);
		    int occur = 0;
		    while(matcher.find()) {
		        occur ++;
		    }
		    return occur;
		}
	 
	public boolean validateUIField(String nomChamp,Object value) throws Exception {
			if(nomChamp.equals(Const.C_VALEUR_GENCODE)) {
				if(value!=null && value instanceof String){	
					
					Integer nbOuvrant=stringOccur(value.toString(),"{");
					Integer nbfermant=stringOccur(value.toString(),"}");
					if(nbOuvrant!=nbfermant)throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "il manque des accolades", "il manque des accolades"));
						//throw new Exception("il manque des accolades");
//					String[] ouvrant=(value.toString()).split("\\{");
//					String[] fermant=(value.toString()).split("\\}");
//					if(ouvrant.length-1!=fermant.length)throw new Exception("il manque des accolades");
				}

				if(nouveau!=null){					
					nouveau.setValeurVerifie("");
					taInfoEntreprise = entrepriseService.findInstance();
					nouveau.setValeurVerifie(genCodeExService.genereCodeExJPA(nouveau,0,nouveau.getCleGencode(), taInfoEntreprise.getCodexoInfoEntreprise()));
					System.out.println(nouveau.getValeurVerifie());
				}
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
        
        PrimeFaces.current().dialog().openDynamic("admin/dev/dialog_gencode_ex", options, params);
        
	}
	
	public void handleReturnDialogTypes(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			nouveau = (TaGenCodeEx) event.getObject();
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
        options.put("contentHeight", 780);
        options.put("height", 790);
        options.put("width",600);
        options.put("modal", true);
        
        Map<String,List<String>> params = new HashMap<String,List<String>>();
        List<String> list = new ArrayList<String>();
        list.add(modeEcran.modeString(EnumModeObjet.C_MO_EDITION));
        params.put("modeEcranDefaut", list);
        List<String> list2 = new ArrayList<String>();
        list2.add(LibConversion.integerToString(selection.getIdGenCodeEx()));
        params.put("idEntity", list2);
        
        PrimeFaces.current().dialog().openDynamic("admin/dev/dialog_gencode_ex", options, params);

	}
	
	public int recupPositionCaret(){
		
		return caretPosition;
	}
	public void action1(ActionEvent actionEvent) /*throws Exception*/ {
		String value = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("param1");
		caretPosition=LibConversion.stringToInteger(value);
//		validateUIField(Const.C_VALEUR_GENCODE, nouveau.getValeurGenCode());
		System.out.println("position : "+value);
		System.out.println("verif : "+nouveau.getValeurVerifie());
	}

	public int getCaretPosition() {
		return caretPosition;
	}

	public void setCaretPosition(int caretPosition) {
		this.caretPosition = caretPosition;
	}


}  
