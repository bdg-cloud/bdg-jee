package fr.legrain.moncompte.admin.webapp.app;

import java.io.Serializable;

public class LgrTab implements Serializable {
	
	private String typeOnglet;
	private String titre;
	private String toolTipTitre;
	private String style = "tab tab-default";
	private String paramId;
	private String template;
	private Object data;
	private String idTab = null;
	
	public static final String CSS_CLASS_TAB_PARTENAIRE = "tab tab-partenaire";
	public static final String CSS_CLASS_TAB_PARTENAIRES = "tab tab-partenaires";
	public static final String CSS_CLASS_TAB_CLIENT = "tab tab-client";
	public static final String CSS_CLASS_TAB_PANIER = "tab tab-panier";
	public static final String CSS_CLASS_TAB_UTILISATEUR = "tab tab-utilisateur";
	public static final String CSS_CLASS_TAB_PRODUIT = "tab tab-produit";
	public static final String CSS_CLASS_TAB_DOSSIERS = "tab tab-dossiers";
	public static final String CSS_CLASS_TAB_DOSSIER = "tab tab-dossier";
	
	
	public static final String CSS_CLASS_TAB_PARAM = "tab tab-param";
	public static final String CSS_CLASS_TAB_TIERS = "tab tab-tiers";
	public static final String CSS_CLASS_TAB_ARTICLE = "tab tab-article";
	public static final String CSS_CLASS_TAB_STOCK = "tab tab-stock";
	public static final String CSS_CLASS_TAB_RECEPTION = "tab tab-reception";
	public static final String CSS_CLASS_TAB_DEVIS = "tab tab-devis";
	public static final String CSS_CLASS_TAB_FABRICATION = "tab tab-fabrication";
	public static final String CSS_CLASS_TAB_INVENTAIRE = "tab tab-inventaire";
	public static final String CSS_CLASS_TAB_LOTS = "tab tab-lots";
	public static final String CSS_CLASS_TAB_CONTROLE_CONFORMITE = "tab tab-controle-conf";
	
	public static final String TYPE_TAB_ARTICLE = "fr.legrain.onglet.article";
	public static final String TYPE_TAB_TIERS = "fr.legrain.onglet.tiers";
	public static final String TYPE_TAB_FABRICATION = "fr.legrain.onglet.creationFabrication";
	public static final String TYPE_TAB_MODELE_FABRICATION = "fr.legrain.onglet.creationModeleFabrication";
	public static final String TYPE_TAB_BON_RECEPTION = "fr.legrain.onglet.bonReception";
	public static final String TYPE_TAB_TYPE_WEB = "fr.legrain.onglet.listeTypeWeb";
	public static final String TYPE_TAB_MOUVEMENT_STOCK = "fr.legrain.onglet.mouvementStock";
	public static final String TYPE_TAB_MOUVEMENT_INVENTAIRE = "fr.legrain.onglet.inventaire";
	public static final String TYPE_TAB_DEVIS = "fr.legrain.onglet.devis";
	
	public LgrTab() {
		
	}
	
	public String getTypeOnglet() {
		return typeOnglet;
	}
	
	public void setTypeOnglet(String typeOnglet) {
		this.typeOnglet = typeOnglet;
	}
	
	public Object getData() {
		return data;
	}
	
	public void setData(Object data) {
		this.data = data;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	public String getParamId() {
		return paramId;
	}

	public void setParamId(String paramId) {
		this.paramId = paramId;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getToolTipTitre() {
		return toolTipTitre;
	}

	public void setToolTipTitre(String toolTipTitre) {
		this.toolTipTitre = toolTipTitre;
	}

	public String getIdTab() {
		return idTab;
	}

	public void setIdTab(String idTab) {
		this.idTab = idTab;
	}
	
}
