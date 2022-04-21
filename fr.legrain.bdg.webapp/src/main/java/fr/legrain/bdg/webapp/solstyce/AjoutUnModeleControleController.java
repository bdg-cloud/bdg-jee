package fr.legrain.bdg.webapp.solstyce;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import fr.legrain.bdg.conformite.service.remote.ITaModeleConformiteServiceRemote;
import fr.legrain.conformite.model.TaModeleConformite;
import fr.legrain.lib.data.ModeObjetEcranServeur;



@Named
@ViewScoped  
public class AjoutUnModeleControleController implements Serializable {  

	private List<TaModeleConformite> values; 
	private TaModeleConformite nouveau ;
	private TaModeleConformite selection ;
	private List<TaModeleConformite> selectionS ;

	private String modeEcranDefaut;
	private static final String C_DIALOG = "dialog";

	private ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();

	private @EJB ITaModeleConformiteServiceRemote taModeleConformiteService;

	@PostConstruct
	public void postConstruct(){
		try {
			values =  taModeleConformiteService.selectAll();

//			if(!values.isEmpty()) {
//				selection = values.get(0);	
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public AjoutUnModeleControleController() {  
	}  
	
	public void actFermerDialog(ActionEvent actionEvent) {
		PrimeFaces.current().dialog().closeDynamic(null);
	}
	
	public void actEnregistrer(ActionEvent actionEvent){
//		controleConformiteJSF.setAction(ControleConformiteJSF.C_TYPE_ACTION_CORRIGE);
//		PrimeFaces.current().dialog().closeDynamic(controleConformiteJSF);
		
//		PrimeFaces.current().dialog().closeDynamic(selection);
		PrimeFaces.current().dialog().closeDynamic(selectionS);
	}
	
	public List<TaModeleConformite> getValues(){  
		//List<Value> l = new LinkedList<Value>();
		return values;
	}

	public TaModeleConformite getNouveau() {
		return nouveau;
	}

	public void setNouveau(TaModeleConformite nouveau) {
		this.nouveau = nouveau;
	}

	public TaModeleConformite getSelection() {
		return selection;
	}

	public void setSelection(TaModeleConformite selection) {
		this.selection = selection;
	}

	public void setValues(List<TaModeleConformite> values) {
		this.values = values;
	}

	public List<TaModeleConformite> getSelectionS() {
		return selectionS;
	}

	public void setSelectionS(List<TaModeleConformite> selectionS) {
		this.selectionS = selectionS;
	}

}  
