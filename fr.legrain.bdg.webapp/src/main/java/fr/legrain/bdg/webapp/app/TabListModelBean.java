package fr.legrain.bdg.webapp.app;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.primefaces.component.tabview.TabView;

@Named("tabListModelBean")
//@ViewScoped
@SessionScoped
public class TabListModelBean implements Serializable {

	private static final long serialVersionUID = -4120754978542588848L;

	private List<LgrTab> onglets = new LinkedList<LgrTab>();
	
	private TabView tabview;
	
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
	
	public TabView getTabview() {
	    return tabview;
	}

	public void setTabview(TabView tabview) {
	    this.tabview = tabview;
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
	
	public int positionOngletType(LgrTab t) {
		
		int position = -1;
		int i = 0;
		for (LgrTab o : onglets) {
			if(t.getTypeOnglet().equals(o.getTypeOnglet())) {
				position = i;
			}
			i++;
		}
		return position;
	}
	
	public void ajouterOnglet(LgrTab t) {
		
		LgrTab typeOngletDejaOuvert = ongletDejaOuvert(t);
		
		if(typeOngletDejaOuvert==null) {
			onglets.add(t);
		} else {
			typeOngletDejaOuvert.setTitre(t.getTitre());
			typeOngletDejaOuvert.setToolTipTitre(t.getToolTipTitre());
			if(tabview!=null) {
				tabview.setActiveIndex(positionOngletType(t));
			}
		}		
	}
	
	public void supprimerOnglet(LgrTab t) {
		onglets.remove(t);
	}
}
