package fr.legrain.autorisations.webapp.autorisations;


import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

import fr.legrain.autorisations.autorisation.model.TaTypeProduit;
import fr.legrain.bdg.autorisations.service.remote.ITaTypeProduitServiceRemote;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.ModeObjetEcranServeur;



@ManagedBean
@ViewScoped  
public class TypeProduitController {  

	private List<TaTypeProduit> values; 
	private List<TaTypeProduit> filteredValues; 
	private TaTypeProduit nouveau ;
	private TaTypeProduit selection ;
	private Boolean tableVide;
	
	private String modeEcranDefaut;
	private static final String C_DIALOG = "dialog";

	private ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();

	private @EJB ITaTypeProduitServiceRemote TaTypeProduitService;

	@PostConstruct
	public void postConstruct(){
		try {
			setTableVide(false);
			refresh();
			if(values == null){
				setTableVide(true);
			}
			if(!values.isEmpty())selection = values.get(0);	
		} catch (Exception e) {
			e.printStackTrace();
		}

		this.setFilteredValues(values);
	}
	
	public TypeProduitController() {  
	}
	
	public void refresh(){
		values = TaTypeProduitService.selectAll();
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

	public void actAnnuler(ActionEvent actionEvent){
		//		values= TaTypeProduitService.selectAll();

		if(values.size()>= 1){
			selection = values.get(0);
		}		
		nouveau = new TaTypeProduit();

		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}
	
	public void actEnregistrer(ActionEvent actionEvent){
		try {
			if(nouveau.getCode() == null || nouveau.getCode().equals("") ){
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "champ code obligatoire"));
			}
			else{ 
				if(nouveau.getLibelle() == null || nouveau.getLibelle().equals("") ){
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "champ libellé obligatoire"));
				}
				else{ 
						if(TaTypeProduitService.findByCode(nouveau.getCode()) == null){
							TaTypeProduit retour = TaTypeProduitService.merge(nouveau, ITaTypeProduitServiceRemote.validationContext);
							values= TaTypeProduitService.selectAll();
							selection = values.get(0);
							nouveau = new TaTypeProduit();

							modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
							
							if(modeEcranDefaut!=null && modeEcranDefaut.equals(C_DIALOG)) {
								PrimeFaces.current().dialog().closeDynamic(retour);
							}
						}else{
							if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_EDITION)==0){
								TaTypeProduitService.merge(nouveau, ITaTypeProduitServiceRemote.validationContext);
								values= TaTypeProduitService.selectAll();
								selection = values.get(0);
								nouveau = new TaTypeProduit();

								modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
							}
							else{
								FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "Code déja utilisé"));
							}
						}
					}
				}
		} catch (FinderException e) {
			e.printStackTrace();
		}
	}

	public void actInserer(ActionEvent actionEvent) {
		nouveau = new TaTypeProduit();

		modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
	}

	public void actModifier(ActionEvent actionEvent){
		nouveau = selection;
		
		modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
	}

	public void actSupprimer(){
		try {
			TaTypeProduitService.remove(selection);
			selection = values.get(0);	
		} catch (RemoveException e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", e.getMessage()));
			e.printStackTrace();
		}
		values = TaTypeProduitService.selectAll();
		selection = null;
		
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}

	public void onRowSelect(SelectEvent event) {  
		if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_CONSULTATION)==0) {
			nouveau = selection;
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

	public List<TaTypeProduit> getValues(){  
		return values;
	}

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
