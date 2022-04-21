package fr.legrain.bdg.webapp.solstyce;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

import fr.legrain.bdg.conformite.service.remote.ITaModeleBaremeServiceRemote;
import fr.legrain.bdg.controle.service.remote.ITaGenCodeExServiceRemote;
import fr.legrain.bdg.webapp.SessionListener;
import fr.legrain.bdg.webapp.app.AbstractController;
import fr.legrain.conformite.model.TaModeleBareme;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.ModeObjetEcranServeur;

@Named
@ViewScoped  
public class ModeleBaremeController extends AbstractController implements Serializable {  

	private List<TaModeleBareme> values; 
	private List<TaModeleBareme> filteredValues; 
	//	private Boolean creation;
	//	private Boolean selectionne;
	private TaModeleBareme nouveau ;
	private TaModeleBareme selection ;
	//	private Boolean modification;
	private Boolean tableVide;

	private String modeEcranDefaut;
	private static final String C_DIALOG = "dialog";

	private ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();

	private @EJB ITaModeleBaremeServiceRemote taModeleBaremeService;
	private @EJB ITaGenCodeExServiceRemote taGenCodeExService;

	@PostConstruct
	public void postConstruct(){
		try {
			//			modification = false;
			//			creation = false;
			setTableVide(false);
			values =  taModeleBaremeService.selectAll();
			if(values == null){
				setTableVide(true);
			}
			if(!values.isEmpty()) {
				selection = values.get(0);	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.setFilteredValues(values);
	}

	public ModeleBaremeController() {  
	}  

	public void actInserer(ActionEvent actionEvent){
		nouveau = new TaModeleBareme();
		modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
		scrollToTop();
		
		taGenCodeExService.libereVerrouTout(new ArrayList<String>(SessionListener.getSessions().keySet()));
		if(nouveau.getCode()!=null) {
			taModeleBaremeService.annuleCode(nouveau.getCode());
		}
		nouveau.setCode(taModeleBaremeService.genereCode(null));
	}

	public void actModifier(ActionEvent actionEvent){
		nouveau = selection;
		modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
	}

	public void actSupprimer(){
		try {
			taModeleBaremeService.remove(selection);
			selection = values.get(0);	
		} catch (RemoveException e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", e.getMessage()));
			e.printStackTrace();
		}
		values = taModeleBaremeService.selectAll();
		selection = null;
	}

	//	public void onChangeQ (){}

	public void actEnregistrer(ActionEvent actionEvent){
		try {
//			if(nouveau.getCodeFamille() == null || nouveau.getCodeFamille().equals("") ){
//
//				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "champ code obligatoire"));
//			}
//			else{ 
//				if(nouveau.getLibcFamille() == null || nouveau.getLibcFamille().equals("") ){
//					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "champ libellé obligatoire"));
//				}
//				else{ 
//					if(nouveau.getLiblFamille() == null || nouveau.getLiblFamille().equals("") ){
//						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "champ Description obligatoire"));
//					}
//					else{ 
						if(nouveau.getCode()!=null && nouveau.getCode().equals("")) {
							nouveau.setCode(null);
						}
						if(taModeleBaremeService.findById(nouveau.getIdModeleBareme()) == null){
							TaModeleBareme retour = taModeleBaremeService.merge(nouveau, ITaModeleBaremeServiceRemote.validationContext);
							values= taModeleBaremeService.selectAll();
							selection = values.get(0);
							//							creation = false;
							//							modification = false;
							nouveau = new TaModeleBareme();

							modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

							if(modeEcranDefaut!=null && modeEcranDefaut.equals(C_DIALOG)) {
								PrimeFaces.current().dialog().closeDynamic(retour);
							}
						}else{
							if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_EDITION)==0){
								taModeleBaremeService.merge(nouveau, ITaModeleBaremeServiceRemote.validationContext);
								values= taModeleBaremeService.selectAll();
								selection = values.get(0);
								//								creation = false;
								//								modification = false;
								nouveau = new TaModeleBareme();

								modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
							}
							else{
								FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "Code déja utilisé"));
							}
						}
//					}
//				}
//			}
		} catch (FinderException e) {
			e.printStackTrace();
		}
	}

	public void actAnnuler(ActionEvent actionEvent){
//		values= taTFamilleArticlesService.selectAll();
		if(values.size()>= 1){
			selection = values.get(0);
		}	else selection=new TaModeleBareme();	
		nouveau = new TaModeleBareme();
		//		creation = false;
		//		modification = false;

		if(nouveau.getCode()!=null) {
			taModeleBaremeService.annuleCode(nouveau.getCode());
		}	
		
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}

	public List<TaModeleBareme> getValues(){  
		//List<Value> l = new LinkedList<Value>();
		return values;
	}

	//	public Boolean getModification() {
	//		return modification;
	//	}
	//	public void setModification(Boolean modification) {
	//		this.modification = modification;
	//	}

	public TaModeleBareme getNouveau() {
		return nouveau;
	}

	public void setNouveau(TaModeleBareme newTaModeleBareme) {
		this.nouveau = newTaModeleBareme;
	}

	public TaModeleBareme getSelection() {
		return selection;
	}

	public void setSelection(TaModeleBareme selectedTaModeleBareme) {
		this.selection = selectedTaModeleBareme;
	}

	public void setValues(List<TaModeleBareme> values) {
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
		scrollToTop();
	}

	//	public void onRowUnSelect(UnselectEvent event)
	//	{
	//		selectionne = false;
	//	}

	public List<TaModeleBareme> getFilteredValues() {
		return filteredValues;
	}

	public void setFilteredValues(List<TaModeleBareme> filteredValues) {
		this.filteredValues = filteredValues;
	}

	public Boolean getTableVide() {
		return tableVide;
	}

	public void setTableVide(Boolean tableVide) {
		this.tableVide = tableVide;
	}

	public void refresh(){
		values = taModeleBaremeService.selectAll();
		Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		modeEcranDefaut = params.get("modeEcranDefaut");

		if(modeEcranDefaut!=null && modeEcranDefaut.equals(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION))) {
			modeEcranDefaut = C_DIALOG;
			actInserer(null);
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
}  
