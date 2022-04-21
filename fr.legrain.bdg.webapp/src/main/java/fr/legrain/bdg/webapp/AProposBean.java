package fr.legrain.bdg.webapp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

 
@Named
@ViewScoped
public class AProposBean implements Serializable {

	private static final long serialVersionUID = 5060395909755114386L;

	@PostConstruct
    public void init() {
        
    }
     
	public void actAbout(ActionEvent actionEvent) {
		
//		PrimeFaces.current().dialog().openDynamic("aide");
    
        Map<String,Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("draggable", false);
        options.put("resizable", false);
        options.put("contentHeight", 520);
        Map<String,List<String>> params = null;
        PrimeFaces.current().dialog().openDynamic("about", options, params);
		
//		FacesContext context = FacesContext.getCurrentInstance();  
//		context.addMessage(null, new FacesMessage("Tiers", "actAbout")); 	    
	}
    
}
