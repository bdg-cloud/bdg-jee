package fr.legrain.bdg.compteclient.app;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.component.layout.LayoutUnit;
import org.primefaces.context.RequestContext;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.TabCloseEvent;
import org.primefaces.event.ToggleEvent;

import fr.legrain.bdg.compteclient.webapp.ConstWeb;

@ManagedBean(name="leftBean")
//@ViewScoped
@SessionScoped
public class LeftBean {

	@ManagedProperty(value="#{tabListModelLeftBean}")
	private TabListModelBean tabs;
	
	@ManagedProperty(value="#{tabListModelCenterBean}")
	private TabListModelBean tabsCenter;
	
	private boolean collapsed = true;
	private boolean expandedForce = false;
	
	@PostConstruct
    public void init() {
		
	}

	public TabListModelBean getTabs() {
		return tabs;
	}

	public void setTabs(TabListModelBean rv) {
		this.tabs = rv;
	}
	
	public void onTabChange(TabChangeEvent event) {
//        FacesMessage msg = new FacesMessage("Tab Changed LeftBean", "Active Tab: " + event.getTab().getTitle());
//        FacesContext.getCurrentInstance().addMessage(null, msg);
	}
	
	public void onTabClose(TabCloseEvent event) {
		if(ConstWeb.DEBUG) {
	        FacesMessage msg = new FacesMessage("Tab Closed LeftBean", "Closed Tab: " + event.getTab().getTitle());
	        FacesContext.getCurrentInstance().addMessage(null, msg);
		}
        
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
	
	public void handleToggle(ToggleEvent event) {
        LayoutUnit layoutUnit = (LayoutUnit)event.getComponent();
        String id = layoutUnit.getClientId();
        String name = event.getVisibility().name();
//        if (id != null && id.equalsIgnoreCase("tabView:adminEastLayout")){
//            if (name != null && name.equals("HIDDEN")){
//                initClosed=true;
//            }else{
//                initClosed=false;
//            }
//        }else if (id != null && id.equalsIgnoreCase("tabView:adminWestLayout")){
//            if (name != null && name.equals("HIDDEN")){
//                this.groupsBean.setInitClosed(true);
//            }else{
//                this.groupsBean.setInitClosed(false);
//            }
//        }
        System.out.println("LeftBean.handleToggle() "+id+"   **** "+name);
//        if(expandedForce) {
//        	expanded = true;
//        	expandedForce = false;
//        } else {
        	collapsed = !collapsed;
//        }
    }
	
	public void remoteCommandToggleList() {
//		return expandedForce;
		RequestContext reqCtx = RequestContext.getCurrentInstance();
		if(expandedForce && collapsed)
			reqCtx.addCallbackParam("expandedForce", "true");
		else if(!expandedForce && !collapsed)
			reqCtx.addCallbackParam("expandedForce", "true");
		else
			reqCtx.addCallbackParam("expandedForce", "false");
	}

	public TabListModelBean getTabsCenter() {
		return tabsCenter;
	}

	public void setTabsCenter(TabListModelBean tabsCenter) {
		this.tabsCenter = tabsCenter;
	}

	public boolean isCollapsed() {
		return collapsed;
	}

	public void setCollapsed(boolean expanded) {
		this.collapsed = expanded;
	}

	public boolean isExpandedForce() {
		return expandedForce;
	}

	public void setExpandedForce(boolean expandedForce) {
		this.expandedForce = expandedForce;
	}
	
}
