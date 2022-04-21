package fr.legrain.bdg.webapp;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.RemoveException;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.event.RowEditEvent;

import fr.legrain.bdg.controle.service.remote.ITaControleServiceRemote;
import fr.legrain.controle.model.TaControle;

@Named
@ViewScoped  
public class ControleController implements Serializable {  

	private List<TaControle> values; 
	
	private @EJB ITaControleServiceRemote controleService;
		
	private TaControle[] selectedTaControles; 
    private TaControle newTaControle = new TaControle();
    private TaControle selectedTaControle = new TaControle();
    private TaControle copieTaControle = new TaControle();

	public ControleController() {  
		//values = getValues();
	}  
	
	@PostConstruct
	public void init(){
		refresh();
	}
	
	public void refresh() {
		try {
			values =  controleService.selectAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<TaControle> getValues(){  
		return values;
	}
	
	/**
     * Create, Update and Delete operations
	 * @throws Exception 
     */
    public void doCreateControle() {
    	try {
			//ws.persist(newTaControle);
    		//ws.enregistrerPersist(newTaControle);
    		controleService.enregistrerMerge(newTaControle,ITaControleServiceRemote.validationContext);
//		} catch (CreateException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			FacesMessage facesMessage = new FacesMessage(e.getMessage());
			FacesContext.getCurrentInstance().validationFailed();
			FacesContext.getCurrentInstance().addMessage(null,  facesMessage);
		}
    }
    
    public void prepareCopieControle(ActionEvent actionEvent) {
    	copieTaControle = new TaControle();
    	copieTaControle.setChamp(selectedTaControle.getChamp());
    	copieTaControle.setContexte(selectedTaControle.getContexte());
    	copieTaControle.setClient(selectedTaControle.getClient());
    	copieTaControle.setServeur(selectedTaControle.getServeur());
    	copieTaControle.setControleDefaut(selectedTaControle.getControleDefaut());
    	copieTaControle.setControleUtilisateur(selectedTaControle.getControleUtilisateur());
    }
    
    public void doCopieControle() {
    	try {
			//ws.persist(newTaControle);
    		//ws.enregistrerPersist(newTaControle);
    		controleService.enregistrerMerge(copieTaControle,ITaControleServiceRemote.validationContext);
//		} catch (CreateException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			FacesMessage facesMessage = new FacesMessage(e.getMessage());
			FacesContext.getCurrentInstance().validationFailed();
			FacesContext.getCurrentInstance().addMessage(null,  facesMessage);
		}
    }
    
    
    public void valueChanged(ValueChangeEvent event) {
    	// http://stackoverflow.com/questions/12717913/best-way-to-validate-ajax-updates-in-jsf-2-0
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();
        UIComponent component = event.getComponent();
        System.out.println("oldValue : "+oldValue+" **** newValue : "+newValue);
        // ...
    }
    
        
    /**
     *
     * @param actionEvent
     */
    public void doUpdateControle(ActionEvent actionEvent){
    	try {
    		//ws.merge(selectedTaControle);
			controleService.enregistrerMerge(selectedTaControle,ITaControleServiceRemote.validationContext);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
       
    /**
    *
    * @param actionEvent
    */
   public void OnRowEditControle(RowEditEvent actionEvent){
   	try {
   		//ws.merge(selectedTaControle);
   		selectedTaControle=controleService.enregistrerMerge(selectedTaControle,ITaControleServiceRemote.validationContext);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
   }
   
   /**
   *
   * @param actionEvent
   */
  public void doDeleteControle(ActionEvent actionEvent){

  		try {
				controleService.supprimer(selectedTaControle);
			} catch (RemoveException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
  }
    /**
     *
     * @param actionEvent
     */
    public void doDeleteControles(ActionEvent actionEvent){
    	for (TaControle c : selectedTaControles) {
    		try {
    			//c = controleService.findById(c.getIdControle());
				controleService.supprimer(c);
			} catch (RemoveException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    }   

	public TaControle[] getSelectedTaControles() {
		return selectedTaControles;
	}

	public void setSelectedTaControles(TaControle[] selectedTaControles) {
		this.selectedTaControles = selectedTaControles;
	}

	public TaControle getNewTaControle() {
		return newTaControle;
	}

	public void setNewTaControle(TaControle newTaControle) {
		this.newTaControle = newTaControle;
	}

	public TaControle getSelectedTaControle() {
		return selectedTaControle;
	}

	public void setSelectedTaControle(TaControle selectedTaControle) {
		this.selectedTaControle = selectedTaControle;
	}
	
	public TaControle getCopieTaControle() {
		return copieTaControle;
	}

	public void setCopieTaControle(TaControle copieTaControle) {
		this.copieTaControle = copieTaControle;
	}

}  
