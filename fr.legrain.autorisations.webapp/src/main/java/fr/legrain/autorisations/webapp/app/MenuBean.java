package fr.legrain.autorisations.webapp.app;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

@ManagedBean
@ViewScoped 
public class MenuBean implements Serializable{

	@ManagedProperty(value="#{tabListModelCenterBean}")
	private TabListModelBean tabsCenter;
	
	@ManagedProperty(value="#{tabListModelLeftBean}")
	private TabListModelBean tabsLeft;
	
	@ManagedProperty(value="#{tabListModelRightBean}")
	private TabListModelBean tabsRight;
	
	@ManagedProperty(value="#{tabListModelBottomBean}")
	private TabListModelBean tabsBottom;
	
	@ManagedProperty(value="#{leftBean}")
	private LeftBean leftBean;
	

	
	public void openTypeproduit(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.TypeProduit");
		b.setTitre("Type Produit");
		b.setTemplate("autorisations/type_produit.xhtml");
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		tabsCenter.ajouterOnglet(b);
    }
	
	public void openAutorisation(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.Autorisations");
		b.setTitre("Autorisations");
		b.setTemplate("autorisations/autorisations.xhtml");
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		tabsCenter.ajouterOnglet(b);
    }
	
	public TabListModelBean getTabsLeft() {
		return tabsLeft;
	}

	public void setTabsLeft(TabListModelBean tabsLeft) {
		this.tabsLeft = tabsLeft;
	}

	public TabListModelBean getTabsRight() {
		return tabsRight;
	}

	public void setTabsRight(TabListModelBean tabsRight) {
		this.tabsRight = tabsRight;
	}

	public TabListModelBean getTabsBottom() {
		return tabsBottom;
	}

	public void setTabsBottom(TabListModelBean tabsBottom) {
		this.tabsBottom = tabsBottom;
	}

	public TabListModelBean getTabsCenter() {
		return tabsCenter;
	}

	public void setTabsCenter(TabListModelBean tabsCenter) {
		this.tabsCenter = tabsCenter;
	}
	public LeftBean getLeftBean() {
		return leftBean;
	}
	public void setLeftBean(LeftBean leftBean) {
		this.leftBean = leftBean;
	}  
}
