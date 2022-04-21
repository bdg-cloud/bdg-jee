package fr.legrain.bdg.compteclient.app;

public class LgrTab {
	
	private String typeOnglet;
	private String titre;
	private String toolTipTitre;
	private String style = "tab tab-default";
	private String paramId;
	private String template;
	private Object data;
	private String idTab = null;
	
	public static final String CSS_CLASS_TAB_PARAM = "tab tab-param";
	public static final String CSS_CLASS_TAB_TIERS = "tab tab-tiers";
	public static final String CSS_CLASS_TAB_ARTICLE = "tab tab-article";
	public static final String CSS_CLASS_TAB_STOCK = "tab tab-stock";
	public static final String CSS_CLASS_TAB_RECEPTION = "tab tab-reception";
	public static final String CSS_CLASS_TAB_DEVIS = "tab tab-devis";
	public static final String CSS_CLASS_TAB_FACTURE = "tab tab-facture";
//	public static final String CSS_CLASS_TAB_DEVIS = "tab tab-param";
//	public static final String CSS_CLASS_TAB_FACTURE = "tab tab-param";
	public static final String CSS_CLASS_TAB_FABRICATION = "tab tab-fabrication";
	public static final String CSS_CLASS_TAB_INVENTAIRE = "tab tab-inventaire";
	public static final String CSS_CLASS_TAB_LOTS = "tab tab-lots";
	public static final String CSS_CLASS_TAB_CONTROLE_CONFORMITE = "tab tab-controle-conf";
	public static final String CSS_CLASS_TAB_TRACABILITE = "tab tab-param";
	
	public static final String TYPE_TAB_ARTICLE = "fr.legrain.onglet.article";
	public static final String TYPE_TAB_TIERS = "fr.legrain.onglet.tiers";
	public static final String TYPE_TAB_FABRICATION = "fr.legrain.onglet.creationFabrication";
	public static final String TYPE_TAB_MODELE_FABRICATION = "fr.legrain.onglet.creationModeleFabrication";
	public static final String TYPE_TAB_BON_RECEPTION = "fr.legrain.onglet.bonReception";
	public static final String TYPE_TAB_TYPE_WEB = "fr.legrain.onglet.listeTypeWeb";
	public static final String TYPE_TAB_MOUVEMENT_STOCK = "fr.legrain.onglet.mouvementStock";
	public static final String TYPE_TAB_MOUVEMENT_INVENTAIRE = "fr.legrain.onglet.inventaire";
	public static final String TYPE_TAB_DEVIS = "fr.legrain.onglet.devis";
	public static final String TYPE_TAB_FACTURE = "fr.legrain.onglet.facture";
	public static final String TYPE_TAB_TRACABILITE = "fr.legrain.onglet.tracabilite";
	
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
