package fr.legrain.bdg.compteclient.app;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import fr.legrain.bdg.compteclient.model.droits.TaUtilisateur;
import fr.legrain.bdg.compteclient.service.interfaces.remote.droits.ITaUtilisateurServiceRemote;
import fr.legrain.bdg.compteclient.webapp.Auth;

@ManagedBean
@ViewScoped 
public class MenuBean {

	@ManagedProperty(value="#{tabListModelCenterBean}")
	private TabListModelBean tabsCenter;

	@ManagedProperty(value="#{tabListModelLeftBean}")
	private TabListModelBean tabsLeft;

	@ManagedProperty(value="#{tabListModelRightBean}")
	private TabListModelBean tabsRight;

	@ManagedProperty(value="#{tabListModelBottomBean}")
	private TabListModelBean tabsBottom;

	

	@ManagedProperty(value="#{tabViewsBean}")
	private TabViewsBean tabViews;
	
	private TaUtilisateur u = null;

//	@EJB private ITaAutorisationsDossierServiceRemote taAutorisationDossierService;
	
	@EJB private ITaUtilisateurServiceRemote userService;
	
	@PostConstruct
	public void init() {
		u = Auth.findUserInSession();
	}

	public void openUtilisateur(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.listeUtilisateur");
		b.setTitre("Utilisateurs");
		b.setTemplate("admin/utilisateurs.xhtml");
		b.setIdTab("idTabAdminUtilisateurs");
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}

	public TabViewsBean getTabViews() {
		return tabViews;
	}

	public void setTabViews(TabViewsBean tabViews) {
		this.tabViews = tabViews;
	}

	public TabListModelBean getTabsCenter() {
		return tabsCenter;
	}

	public void setTabsCenter(TabListModelBean tabsCenter) {
		this.tabsCenter = tabsCenter;
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
}
