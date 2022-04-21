package fr.legrain.conformite.dto;

import fr.legrain.bdg.model.ModelObject;

public class TaBaremeDTO extends ModelObject implements java.io.Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8010022710866574436L;
	
	
	private Integer id;
	private Integer versionObj;
	
	private String code;
	
	private String expressionVerifiee;
	private String cheminDoc;
	private String actioncorrective;
	private Boolean forcage;
	
	private String codeTypeConformite;
	private Integer idTypeConformite;
	

	
	
	public TaBaremeDTO() {
		
	}
	

	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		firePropertyChange("id", this.id, this.id = id);
	}
	
	public Integer getVersionObj() {
		return this.versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}



	public String getExpressionVerifiee() {
		return expressionVerifiee;
	}



	public void setExpressionVerifiee(String expressionVerifiee) {
		firePropertyChange("expressionVerifiee", this.expressionVerifiee, this.expressionVerifiee = expressionVerifiee);
	}


	public String getCodeTypeConformite() {
		return codeTypeConformite;
	}



	public void setCodeTypeConformite(String codeTypeConformite) {
		firePropertyChange("codeTypeConformite", this.codeTypeConformite, this.codeTypeConformite = codeTypeConformite);
	}



	public Integer getIdTypeConformite() {
		return idTypeConformite;
	}



	public void setIdTypeConformite(Integer idTypeConformite) {
		firePropertyChange("idTypeConformite", this.idTypeConformite, this.idTypeConformite = idTypeConformite);
	}



	public String getCheminDoc() {
		return cheminDoc;
	}



	public void setCheminDoc(String cheminDoc) {
		firePropertyChange("cheminDoc", this.cheminDoc, this.cheminDoc = cheminDoc);
	}



	public String getActioncorrective() {
		return actioncorrective;
	}



	public void setActioncorrective(String actioncorrective) {
		firePropertyChange("actioncorrective", this.actioncorrective, this.actioncorrective = actioncorrective);
	}



	public Boolean getForcage() {
		return forcage;
	}



	public void setForcage(Boolean forcage) {
		firePropertyChange("forcage", this.forcage, this.forcage = forcage);
	}



	public String getCode() {
		return code;
	}



	public void setCode(String code) {
		this.code = code;
	}
	

}
