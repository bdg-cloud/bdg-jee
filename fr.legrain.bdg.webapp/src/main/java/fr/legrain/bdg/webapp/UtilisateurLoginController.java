package fr.legrain.bdg.webapp;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;

import fr.legrain.bdg.droits.service.remote.ITaUtilisateurServiceRemote;
import fr.legrain.bdg.login.service.remote.ITaUtilisateurLoginServiceRemote;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.ModeObjetEcranServeur;
import fr.legrain.login.model.TaUtilisateurLogin;

@Named
@ViewScoped  
public class UtilisateurLoginController implements Serializable {  

	private List<TaUtilisateurLogin> values; 
	private List<TaUtilisateurLogin> filteredValues; 
	private TaUtilisateurLogin nouveau ;
	private TaUtilisateurLogin selection ;
	private Boolean tableVide;
	
	private ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();

	private @EJB ITaUtilisateurLoginServiceRemote taUtilisateurService;

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
		values = taUtilisateurService.selectAll();
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}

	public UtilisateurLoginController() {  
	}  

	public void actAnnuler(ActionEvent actionEvent){
		//values= taTAdrService.selectAll();
		
		if(values.size()>= 1){
			selection = values.get(0);
		}		
		nouveau = new TaUtilisateurLogin();
		
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}
	
	public void actEnregistrer(ActionEvent actionEvent){

		try {
			if(nouveau.getUsername() == null || nouveau.getUsername().equals("") ){
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "champ code obligatoire"));
			}
			else{ 
				if(nouveau.getUsername() == null || nouveau.getUsername().equals("") ){
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "champ libellé obligatoire"));
				}
				else{ 
					if(taUtilisateurService.findByCode(nouveau.getUsername()) == null){
						taUtilisateurService.merge(nouveau, ITaUtilisateurServiceRemote.validationContext);
						values= taUtilisateurService.selectAll();
						selection = values.get(0);
						nouveau = new TaUtilisateurLogin();
						
						modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
					}else{
						if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_EDITION)==0){
							taUtilisateurService.merge(nouveau, ITaUtilisateurServiceRemote.validationContext);
							values= taUtilisateurService.selectAll();
							selection = values.get(0);
							nouveau = new TaUtilisateurLogin();
							
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
		nouveau = new TaUtilisateurLogin();
	
		modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
	}

	public void actModifier(ActionEvent actionEvent){
		nouveau = selection;
		
		modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
	}

	public void actSupprimer(){
		try {
			taUtilisateurService.remove(selection);
			selection = values.get(0);	
		} catch (RemoveException e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", e.getMessage()));
			e.printStackTrace();
		}
		values = taUtilisateurService.selectAll();
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

	public List<TaUtilisateurLogin> getValues(){  
		return values;
	}

	public TaUtilisateurLogin getNouveau() {
		return nouveau;
	}

	public void setNouveau(TaUtilisateurLogin newTaUtilisateur) {
		this.nouveau = newTaUtilisateur;
	}

	public TaUtilisateurLogin getSelection() {
		return selection;
	}

	public void setSelection(TaUtilisateurLogin selectedTaUtilisateur) {
		this.selection = selectedTaUtilisateur;
	}

	public void setValues(List<TaUtilisateurLogin> values) {
		this.values = values;
	}

	public List<TaUtilisateurLogin> getFilteredValues() {
		return filteredValues;
	}

	public void setFilteredValues(List<TaUtilisateurLogin> filteredValues) {
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
