package fr.legrain.bdg.webapp;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
 
@Named
@ViewScoped
public class AideBean implements Serializable {
     
	private static final long serialVersionUID = 885976124174761748L;

     
    @PostConstruct
    public void init() {
        
    }
     
    
}
