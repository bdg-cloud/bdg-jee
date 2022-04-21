package fr.legrain.bdg.webapp.solstyce;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import fr.legrain.bdg.conformite.service.remote.ITaModeleGroupeServiceRemote;
import fr.legrain.conformite.model.TaModeleGroupe;
import fr.legrain.lib.data.ModeObjetEcranServeur;



@Named
@ViewScoped  
public class AjoutModeleControleController implements Serializable {  

	private List<TaModeleGroupe> values; 
	private TaModeleGroupe nouveau ;
	private TaModeleGroupe selection ;

	private String modeEcranDefaut;
	private static final String C_DIALOG = "dialog";

	private ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();

	private @EJB ITaModeleGroupeServiceRemote taModeleGroupeService;

	@PostConstruct
	public void postConstruct(){
		try {
			values =  taModeleGroupeService.selectAll();

//			if(!values.isEmpty()) {
//				selection = values.get(0);	
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public AjoutModeleControleController() {  
	}  
	
	public void actFermerDialog(ActionEvent actionEvent) {
		PrimeFaces.current().dialog().closeDynamic(null);
	}
	
	public void actEnregistrer(ActionEvent actionEvent){
//		controleConformiteJSF.setAction(ControleConformiteJSF.C_TYPE_ACTION_CORRIGE);
//		PrimeFaces.current().dialog().closeDynamic(controleConformiteJSF);
		PrimeFaces.current().dialog().closeDynamic(selection);
	}
	
	public List<TaModeleGroupe> getValues(){  
		//List<Value> l = new LinkedList<Value>();
		return values;
	}

	public TaModeleGroupe getNouveau() {
		return nouveau;
	}

	public void setNouveau(TaModeleGroupe nouveau) {
		this.nouveau = nouveau;
	}

	public TaModeleGroupe getSelection() {
		return selection;
	}

	public void setSelection(TaModeleGroupe selection) {
		this.selection = selection;
	}

	public void setValues(List<TaModeleGroupe> values) {
		this.values = values;
	}

}  
