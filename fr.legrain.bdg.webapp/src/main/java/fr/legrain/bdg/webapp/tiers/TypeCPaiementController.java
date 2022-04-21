package fr.legrain.bdg.webapp.tiers;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import fr.legrain.bdg.tiers.service.remote.ITaTCPaiementServiceRemote;
import fr.legrain.tiers.model.TaTCPaiement;



@Named
@ViewScoped  
public class TypeCPaiementController implements Serializable {  

	private List<TaTCPaiement> values; 
	private List<TaTCPaiement> filteredValues; 
	private Boolean creation;
	private Boolean selectionne;
	private TaTCPaiement nouveau ;
	private TaTCPaiement selection ;
	private Boolean modification;

	public Boolean getModification() {
		return modification;
	}
	
	public void setModification(Boolean modification) {
		this.modification = modification;
	}
	
	@PostConstruct
	public void postConstruct(){

		try {
			modification = false;
			creation = false;
			values =  ws.selectAll();

		} catch (Exception e) {
			e.printStackTrace();
		}

		this.filteredValues = values;
	}
	public List<TaTCPaiement> getFilteredValues() {
		return filteredValues;
	}

	public void setFilteredValues(List<TaTCPaiement> filetredValues) {
		this.filteredValues = filetredValues;
	}

	private @EJB ITaTCPaiementServiceRemote ws;


	public TypeCPaiementController() {  
	}  



	public void create(AjaxBehaviorEvent event){
		nouveau = new TaTCPaiement();
		creation = true;
	}
	
	public void create(){
		nouveau = new TaTCPaiement();
		creation = true;
	}
	
	public void modification(){

		nouveau = selection;
		creation = true;
		modification = true;
	}

	public void suppression(){
		try {
			ws.remove(selection);
		} catch (RemoveException e) {
			e.printStackTrace();
		}
		values = ws.selectAll();
		selection = null;
	}
	
	public void onChangeQ (){}

	public void doCreate(){

		try {
			if(ws.findByCode(nouveau.getCodeTCPaiement()) == null){
				//			        ws.merge(nouveau, ITaTCPaiementServiceRemote.validationContext);
				values= ws.selectAll();
				creation = false;
				modification = false;
				nouveau = new TaTCPaiement();
			}else{
				if(modification == true){
					//				        ws.merge(nouveau, ITaTCPaiementServiceRemote.validationContext);
					values= ws.selectAll();
					creation = false;
					modification = false;
					nouveau = new TaTCPaiement();
				}else{

					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "Code déja utilisé"));
				}
			}
		} catch (FinderException e) {

			e.printStackTrace();
		}


	}
	
	public void annulerCreation(){
		values= ws.selectAll();
		nouveau = new TaTCPaiement();
		creation = false;
		modification = false;
	}

	public List<TaTCPaiement> getValues(){  
		//List<Value> l = new LinkedList<Value>();
		return values;

	}

	public TaTCPaiement getNouveau() {
		return nouveau;
	}

	public void setNouveau(TaTCPaiement newTaTCPaiement) {
		this.nouveau = newTaTCPaiement;
	}

	public TaTCPaiement getSelection() {
		return selection;
	}

	public void setSelection(TaTCPaiement selectedTaTCPaiement) {
		this.selection = selectedTaTCPaiement;
	}

	public void setValues(List<TaTCPaiement> values) {
		this.values = values;
	}

	public Boolean getCreation() {
		return creation;
	}

	public void setCreation(Boolean creationE) {
		this.creation = creationE;
	}

	public Boolean getSelectionne() {
		return selectionne;
	}

	public void setSelectionne(Boolean selectionne) {
		this.selectionne = selectionne;
	}

	public void onRowSelect(SelectEvent event){
		selectionne = true;
	}
	public void onRowUnSelect(UnselectEvent event){
		selectionne = false;
	}



}  
