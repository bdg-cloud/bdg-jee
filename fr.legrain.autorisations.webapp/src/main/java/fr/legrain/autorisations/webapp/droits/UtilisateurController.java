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

import fr.legrain.autorisations.autorisation.model.TaUtilisateur;
import fr.legrain.bdg.autorisations.service.remote.ITaUtilisateurServiceRemote;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.ModeObjetEcranServeur;

@ManagedBean
@ViewScoped  
public class UtilisateurController implements Serializable{  

	private List<TaUtilisateur> values; 
	private List<TaUtilisateur> filteredValues; 
	private TaUtilisateur nouveau ;
	private TaUtilisateur selection ;
	private Boolean tableVide;
	
	private ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();

	private @EJB ITaUtilisateurServiceRemote taUtilisateurService;

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

	public UtilisateurController() {  
	}  

	public void actAnnuler(ActionEvent actionEvent){
		//values= taTAdrService.selectAll();
		
		if(values.size()>= 1){
			selection = values.get(0);
		}		
		nouveau = new TaUtilisateur();
		
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
						nouveau = new TaUtilisateur();
						
						modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
					}else{
						if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_EDITION)==0){
							taUtilisateurService.merge(nouveau, ITaUtilisateurServiceRemote.validationContext);
							values= taUtilisateurService.selectAll();
							selection = values.get(0);
							nouveau = new TaUtilisateur();
							
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
		nouveau = new TaUtilisateur();
	
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

	public List<TaUtilisateur> getValues(){  
		return values;
	}

	public TaUtilisateur getNouveau() {
		return nouveau;
	}

	public void setNouveau(TaUtilisateur newTaUtilisateur) {
		this.nouveau = newTaUtilisateur;
	}

	public TaUtilisateur getSelection() {
		return selection;
	}

	public void setSelection(TaUtilisateur selectedTaUtilisateur) {
		this.selection = selectedTaUtilisateur;
	}

	public void setValues(List<TaUtilisateur> values) {
		this.values = values;
	}

	public List<TaUtilisateur> getFilteredValues() {
		return filteredValues;
	}

	public void setFilteredValues(List<TaUtilisateur> filteredValues) {
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
