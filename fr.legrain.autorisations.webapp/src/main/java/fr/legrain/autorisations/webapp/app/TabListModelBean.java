package fr.legrain.autorisations.webapp.app;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name="tabListModelBean")
//@ViewScoped
@SessionScoped
public class TabListModelBean implements Serializable{
	
	private List<LgrTab> onglets = new LinkedList<LgrTab>();
	
	public TabListModelBean() {
		
	}
	
	@PostConstruct
    public void init() {

	}

	public List<LgrTab> getOnglets() {
		return onglets;
	}

	public void setOnglets(List<LgrTab> onglets) {
		this.onglets = onglets;
	}
	
	
	public LgrTab ongletDejaOuvert(LgrTab t) {
		
		LgrTab typeOngletDejaOuvert = null;
		for (LgrTab o : onglets) {
			if(t.getTypeOnglet().equals(o.getTypeOnglet())) {
				typeOngletDejaOuvert = o;
			}
		}
		return typeOngletDejaOuvert;
	}
	
	public void ajouterOnglet(LgrTab t) {
		
		LgrTab typeOngletDejaOuvert = ongletDejaOuvert(t);
		
		if(typeOngletDejaOuvert==null) {
			onglets.add(t);
		} else {
			typeOngletDejaOuvert.setTitre(t.getTitre());
		}		
	}
	
	public void supprimerOnglet(LgrTab t) {
		onglets.remove(t);
	}
}
