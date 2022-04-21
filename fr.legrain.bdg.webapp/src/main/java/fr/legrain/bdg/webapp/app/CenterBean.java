package fr.legrain.bdg.webapp.app;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.TabCloseEvent;

import fr.legrain.bdg.webapp.ConstWeb;

@Named
//@ViewScoped
@SessionScoped
public class CenterBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -177349589395031141L;
	@Inject @Named(value="tabListModelCenterBean")
	private TabListModelBean tabs;
	
	@PostConstruct
    public void init() {
		System.out.println("CenterBean.init()");
	}

	public TabListModelBean getTabs() {
		return tabs;
	}

	public void setTabs(TabListModelBean rv) {
		this.tabs = rv;
	}
	
	public void onTabShow() {
		System.out.println("CenterBean.onTabShow()");
	}
	
	public void onTabChange(TabChangeEvent event) {
//		FacesMessage msg = null;
//		if(event.getTab()!=null) {
//			msg = new FacesMessage("Tab Changed CenterBean", "Active Tab: " + event.getTab().getTitle());
//		} else {
//			msg = new FacesMessage("Tab Changed CenterBean", "Active Tab: ");
//		}
//        FacesContext.getCurrentInstance().addMessage(null, msg);
	}
	
	public void onTabClose(TabCloseEvent event) {
		if(ConstWeb.DEBUG) {
	        FacesMessage msg = new FacesMessage("Tab Closed CenterBean", "Closed Tab: " + event.getTab().getTitle());
	        FacesContext.getCurrentInstance().addMessage(null, msg);
		}
        
//        LgrTab t = (LgrTab)event.getData();
//        System.err.println(t.getTitre());
        
        //si on ne peu pas faire de remove en accédant au modèle (objet LgrTab) et donc se servir de l'id,
        //il faudra faire avec le "tire" pour faire le lien ou ajouter un bouton dans l'onglet, pour pouvoir le fermer à partir de l'onglet sans utiliser la petite croix
       // tabs.supprimerOnglet(t);
        
        LgrTab aSupprimer = null;
        for (LgrTab t : tabs.getOnglets()) {
			if(t.getTitre().equals(event.getTab().getTitle())) {
				aSupprimer = t;
			}
		}
        if(aSupprimer!=null) {
        	tabs.supprimerOnglet(aSupprimer);
        }
	}
	
}
