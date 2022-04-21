package fr.legrain.autorisations.webapp;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
 
@ManagedBean
@ViewScoped
public class AideBean implements Serializable {
     
	private static final long serialVersionUID = 885976124174761748L;

     
    @PostConstruct
    public void init() {
        
    }
     
    
}
