package fr.legrain.moncompte.admin.webapp.app;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.component.tabview.TabView;

@ManagedBean(name="tabViewsBean")
@ViewScoped 
public class TabViewsBean implements Serializable {

	@ManagedProperty(value="#{tabListModelCenterBean}")
	private TabListModelBean tabsCenter;
	private TabView tabviewCenter;
	
	@ManagedProperty(value="#{tabListModelLeftBean}")
	private TabListModelBean tabsLeft;
	private TabView tabviewLeft;

//	@EJB private ITaPreferencesServiceRemote taPreferencesService;
	
	@PostConstruct
	public void init() {
		refresh();
	}

	public void refresh(){

	}
	
	public int positionOngletCentreType(String idTypeOnglet) {
		
		int position = -1;
		int i = 0;
		for (LgrTab o : tabsCenter.getOnglets()) {
			if(idTypeOnglet.equals(o.getTypeOnglet())) {
				position = i;
			}
			i++;
		}
		return position;
	}
	
	public void selectionneOngletCentre(String idTypeOnglet) {
		if(tabviewCenter!=null) {
			tabviewCenter.setActiveIndex(positionOngletCentreType(idTypeOnglet));
		}
	}	
	
	public int positionOngletCentreType(LgrTab t) {
		return positionOngletCentreType(t.getTypeOnglet());
	}
	
	public void selectionneOngletCentre(LgrTab t) {
		selectionneOngletCentre(t.getTypeOnglet());
	}	
	
	public int positionOngletLeftType(String idTypeOnglet) {
		
		int position = -1;
		int i = 0;
		for (LgrTab o : tabsLeft.getOnglets()) {
			if(idTypeOnglet.equals(o.getTypeOnglet())) {
				position = i;
			}
			i++;
		}
		return position;
	}
	
	public void selectionneOngletLeft(String idTypeOnglet) {
		if(tabviewLeft!=null) {
			tabviewLeft.setActiveIndex(positionOngletLeftType(idTypeOnglet));
		}
	}	
	
	public int positionOngletLeftType(LgrTab t) {
		return positionOngletLeftType(t.getTypeOnglet());
	}
	
	public void selectionneOngletLeft(LgrTab t) {
		selectionneOngletLeft(t.getTypeOnglet());
	}

	public TabListModelBean getTabsCenter() {
		return tabsCenter;
	}

	public void setTabsCenter(TabListModelBean tabsCenter) {
		this.tabsCenter = tabsCenter;
	}

	public TabView getTabviewCenter() {
		return tabviewCenter;
	}

	public void setTabviewCenter(TabView tabviewCenter) {
		this.tabviewCenter = tabviewCenter;
	}

	public TabListModelBean getTabsLeft() {
		return tabsLeft;
	}

	public void setTabsLeft(TabListModelBean tabsLeft) {
		this.tabsLeft = tabsLeft;
	}

	public TabView getTabviewLeft() {
		return tabviewLeft;
	}

	public void setTabviewLeft(TabView tabviewLeft) {
		this.tabviewLeft = tabviewLeft;
	}
	
	
}
