package fr.legrain.moncompte.admin.webapp.app;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;


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

	@ManagedProperty(value="#{leftBean}")
	private LeftBean leftBean;

	@ManagedProperty(value="#{tabViewsBean}")
	private TabViewsBean tabViews;
	

	
	public void openTypeProduit(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.TypeProduit");
		b.setTitre("Type Produit");
		b.setTemplate("admin/type_produit.xhtml");
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		tabsCenter.ajouterOnglet(b);
    }
	
	public void openProduit(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.produit");
		b.setTitre("Produit");
		b.setTemplate("admin/produit.xhtml");
		b.setStyle(LgrTab.CSS_CLASS_TAB_PRODUIT);
		b.setIdTab("idTabProduit");
		tabsCenter.ajouterOnglet(b);
    }
	
	public void openListeProduit(ActionEvent event) {  
		leftBean.setExpandedForce(true);
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.listeProduit");
		b.setTitre("Liste produit");
		b.setTemplate("admin/liste_produit.xhtml");
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		b.setIdTab("idTabListeProduit");
		tabsLeft.ajouterOnglet(b);
		tabViews.selectionneOngletLeft(b);
	}
	
	public void openListeAutorisation(ActionEvent event) {  
		leftBean.setExpandedForce(true);
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.listeAutorisation");
		b.setTitre("Liste produit");
		b.setTemplate("admin/liste_autorisation.xhtml");
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		b.setIdTab("idTabListeAutorisation");
		tabsLeft.ajouterOnglet(b);
		tabViews.selectionneOngletLeft(b);
	}
	
	public void openListeProduitOverlay(ActionEvent event) {  
		leftBean.setExpandedForce(true);
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.listeProduit");
		b.setTitre("Liste produit");
		b.setTemplate("admin/liste_produit.xhtml");
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		tabsLeft.ajouterOnglet(b);
		tabViews.selectionneOngletLeft(b);
	}
	
	public void openClient(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.client");
		b.setTitre("Client");
		b.setTemplate("admin/client.xhtml");
		b.setStyle(LgrTab.CSS_CLASS_TAB_CLIENT);
		tabsCenter.ajouterOnglet(b);
    }
	
	public void openDossier(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.dossier");
		b.setTitre("Dossier");
		b.setTemplate("admin/dossier.xhtml");
		b.setStyle(LgrTab.CSS_CLASS_TAB_DOSSIER);
		b.setIdTab("idTabListeDossiers");
		tabsCenter.ajouterOnglet(b);
	}
	
	public void openListeDossier(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.dossiers");
		b.setTitre("Dossier");
		b.setTemplate("admin/dossiers.xhtml");
		b.setStyle(LgrTab.CSS_CLASS_TAB_DOSSIERS);
		b.setIdTab("idTabListeDossiers");
		tabsCenter.ajouterOnglet(b);
	}
	
	public void openListeClient(ActionEvent event) {  
		leftBean.setExpandedForce(true);
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.listeClient");
		b.setTitre("Liste client");
		b.setTemplate("admin/liste_client.xhtml");
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		tabsLeft.ajouterOnglet(b);
		tabViews.selectionneOngletLeft(b);
	}
	
	public void openListeUtilisateur(ActionEvent event) {  
		leftBean.setExpandedForce(true);
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.listeUtiliateur");
		b.setTitre("Liste utilisateur");
		b.setTemplate("admin/liste_utilisateur.xhtml");
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		tabsLeft.ajouterOnglet(b);
		tabViews.selectionneOngletLeft(b);
	}
	
	public void openPanier(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.panier");
		b.setTitre("Panier");
		b.setTemplate("admin/panier.xhtml");
		b.setStyle(LgrTab.CSS_CLASS_TAB_PANIER);
		b.setIdTab("idTabPanier");
		tabsCenter.ajouterOnglet(b);
    }
	
	public void openConditionGeneraleVente(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.cgv");
		b.setTitre("CGV");
		b.setTemplate("admin/panier.xhtml"); /***************A CHANGER********************/
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		tabsCenter.ajouterOnglet(b);
    }
	
	public void openCategoriePro(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.categoriepro");
		b.setTitre("Catégorie pro");
		b.setTemplate("admin/panier.xhtml"); /***************A CHANGER********************/
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		tabsCenter.ajouterOnglet(b);
    }
	
	public void openPartenariatDemande(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.partenariat.demande");
		b.setTitre("Demandes partenariat");
		b.setTemplate("admin/partenariat_demande.xhtml");
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		tabsCenter.ajouterOnglet(b);
    }
	
	public void openPartenariatPartenaire(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.partenariat.partenaire");
		b.setTitre("Partenaires");
		b.setTemplate("admin/partenariat_partenaire.xhtml");
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARTENAIRES);
		tabsCenter.ajouterOnglet(b);
    }
	
	public void openPartenariatConditionGenerale(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.partenariat.cg");
		b.setTitre("Conditions générales de partenariat");
		b.setTemplate("admin/partenariat_cg.xhtml");
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		tabsCenter.ajouterOnglet(b);
    }
	
	public void openAutorisation(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.Autorisations");
		b.setTitre("Autorisations");
		b.setTemplate("admin/autorisations.xhtml");
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		tabsCenter.ajouterOnglet(b);
    }
	
	public void openAdministration(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.administration");
		b.setTitre("Administration");
		b.setTemplate("admin/administration.xhtml");
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

	public TabViewsBean getTabViews() {
		return tabViews;
	}

	public void setTabViews(TabViewsBean tabViews) {
		this.tabViews = tabViews;
	}  
}
