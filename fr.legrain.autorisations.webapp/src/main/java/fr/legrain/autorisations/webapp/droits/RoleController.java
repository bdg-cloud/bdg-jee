package fr.legrain.autorisations.webapp.droits;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.event.SelectEvent;

import fr.legrain.autorisations.autorisation.model.TaRole;
import fr.legrain.bdg.autorisations.service.remote.ITaRoleServiceRemote;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.ModeObjetEcranServeur;

@ManagedBean
@ViewScoped  
public class RoleController implements Serializable{  

	private List<TaRole> values; 
	private List<TaRole> filteredValues; 
	private TaRole nouveau ;
	private TaRole selection ;
	private Boolean tableVide;
	
	private ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();

	private @EJB ITaRoleServiceRemote taRoleService;

	@PostConstruct
	public void postConstruct(){

		try {
			setTableVide(false);
			refresh();
			if(values == null){
				setTableVide(true);
			}
			selection = values.get(0);	
		} catch (Exception e) {
			e.printStackTrace();
		}

		this.setFilteredValues(values);
	}
	
	public void refresh(){
		values = taRoleService.selectAll();
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}

	public RoleController() {  
	}  

	public void actAnnuler(ActionEvent actionEvent){
		//values= taTAdrService.selectAll();
		
		if(values.size()>= 1){
			selection = values.get(0);
		}		
		nouveau = new TaRole();
		
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}
	
	public void actEnregistrer(ActionEvent actionEvent){

		try {
			if(nouveau.getRole() == null || nouveau.getRole().equals("") ){
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "champ code obligatoire"));
			}
			else{ 
				if(nouveau.getRole() == null || nouveau.getRole().equals("") ){
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "champ libellé obligatoire"));
				}
				else{ 
					if(taRoleService.findByCode(nouveau.getRole()) == null){
						taRoleService.merge(nouveau, ITaRoleServiceRemote.validationContext);
						values= taRoleService.selectAll();
						selection = values.get(0);
						nouveau = new TaRole();
						
						modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
					}else{
						if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_EDITION)==0){
							taRoleService.merge(nouveau, ITaRoleServiceRemote.validationContext);
							values= taRoleService.selectAll();
							selection = values.get(0);
							nouveau = new TaRole();
							
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

	public void actInserer(ActionEvent actionEvent){
		nouveau = new TaRole();
	
		modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
	}

	public void actModifier(ActionEvent actionEvent){
		nouveau = selection;
		
		modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
	}

	public void actSupprimer(){
		try {
			taRoleService.remove(selection);
			selection = values.get(0);	
		} catch (RemoveException e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", e.getMessage()));
			e.printStackTrace();
		}
		values = taRoleService.selectAll();
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

	public List<TaRole> getValues(){  
		return values;
	}

	public TaRole getNouveau() {
		return nouveau;
	}

	public void setNouveau(TaRole newTaRole) {
		this.nouveau = newTaRole;
	}

	public TaRole getSelection() {
		return selection;
	}

	public void setSelection(TaRole selectedTaRole) {
		this.selection = selectedTaRole;
	}

	public void setValues(List<TaRole> values) {
		this.values = values;
	}

	public List<TaRole> getFilteredValues() {
		return filteredValues;
	}

	public void setFilteredValues(List<TaRole> filteredValues) {
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

}  
