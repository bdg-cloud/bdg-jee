package fr.legrain.bdg.webapp;

import java.io.Serializable;

public class LgrTabEvent implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -803926170740759018L;
	
	private Integer idObjet;
	private String codeObjet;
	private String cssLgrTab;
	private Object data;
	private boolean afficheDoc=true;

	
	public Integer getIdObjet() {
		return idObjet;
	}

	public void setIdObjet(Integer integer) {
		this.idObjet = integer;
	}

	public String getCssLgrTab() {
		return cssLgrTab;
	}

	public void setCssLgrTab(String cssLgrTab) {
		this.cssLgrTab = cssLgrTab;
	}

	public String getCodeObjet() {
		return codeObjet;
	}

	public void setCodeObjet(String codeObjet) {
		this.codeObjet = codeObjet;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public boolean isAfficheDoc() {
		return afficheDoc;
	}

	public void setAfficheDoc(boolean afficheDoc) {
		this.afficheDoc = afficheDoc;
	}

}
