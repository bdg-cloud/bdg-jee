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

import fr.legrain.bdg.general.service.remote.ITaGenCodeServiceRemote;
import fr.legrain.controle.model.TaGenCode;

@Named
@ViewScoped  
public class GenCodeController implements Serializable {  

	private List<TaGenCode> values; 
	
	private @EJB ITaGenCodeServiceRemote genCodeService;
		
	private TaGenCode[] selectedTaGenCodes; 
    private TaGenCode newTaGenCode = new TaGenCode();
    private TaGenCode selectedTaGenCode = new TaGenCode();

	public GenCodeController() {  
		//values = getValues();
	}  
	
	@PostConstruct
	public void postConstruct(){
		values = getValues();
	}

	public List<TaGenCode> getValues(){  
		//List<Value> l = new LinkedList<Value>();
		List<TaGenCode> l = null;

		try {
			l =  genCodeService.selectAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return l;
	}
	
	/**
     * Create, Update and Delete operations
	 * @throws Exception 
     */
    public void doCreateGenCode() {
    	try {
			//ws.persist(newTaGenCode);
    		//ws.enregistrerPersist(newTaGenCode);
    		genCodeService.enregistrerMerge(newTaGenCode,ITaGenCodeServiceRemote.validationContext);
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
    public void doUpdateGenCode(ActionEvent actionEvent){
    	try {
    		//ws.merge(selectedTaGenCode);
			genCodeService.enregistrerMerge(selectedTaGenCode,ITaGenCodeServiceRemote.validationContext);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
        
    /**
     *
     * @param actionEvent
     */
    public void doDeleteGenCode(ActionEvent actionEvent){
    	for (TaGenCode c : selectedTaGenCodes) {
    		try {
    			c = genCodeService.findById(c.getIdGenCode());
				genCodeService.supprimer(c);
			} catch (RemoveException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    }   

	public TaGenCode[] getSelectedTaGenCodes() {
		return selectedTaGenCodes;
	}

	public void setSelectedTaGenCodes(TaGenCode[] selectedTaGenCodes) {
		this.selectedTaGenCodes = selectedTaGenCodes;
	}

	public TaGenCode getNewTaGenCode() {
		return newTaGenCode;
	}

	public void setNewTaGenCode(TaGenCode newTaGenCode) {
		this.newTaGenCode = newTaGenCode;
	}

	public TaGenCode getSelectedTaGenCode() {
		return selectedTaGenCode;
	}

	public void setSelectedTaGenCode(TaGenCode selectedTaGenCode) {
		this.selectedTaGenCode = selectedTaGenCode;
	}

}  
