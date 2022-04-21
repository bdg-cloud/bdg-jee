package fr.legrain.moncompte.client.webapp.app;

public class LgrTab {
	
	private String typeOnglet;
	private String titre;
	private String style = "tab-default";
	private String paramId;
	private String template;
	private Object data;
	
	public static final String CSS_CLASS_TAB_PARAM = "tab-param";
	public static final String CSS_CLASS_TAB_TIERS = "tab-tiers";
	public static final String CSS_CLASS_TAB_ARTICLE = "tab-article";
	public static final String CSS_CLASS_TAB_MOUVEMENT_STOCK = "tab-mouvementStock";
	
	public static final String TYPE_TAB_ARTICLE = "fr.legrain.onglet.article";
	public static final String TYPE_TAB_TIERS = "fr.legrain.onglet.tiers";
	public static final String TYPE_TAB_FABRICATION = "fr.legrain.onglet.creationFabrication";
	public static final String TYPE_TAB_BON_RECEPTION = "fr.legrain.onglet.bonReception";
	public static final String TYPE_TAB_TYPE_WEB = "fr.legrain.onglet.listeTypeWeb";
	public static final String TYPE_TAB_MOUVEMENT_STOCK = "fr.legrain.onglet.mouvementStock";
	public static final String TYPE_TAB_MOUVEMENT_INVENTAIRE = "fr.legrain.onglet.inventaire";
	
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
	
}
