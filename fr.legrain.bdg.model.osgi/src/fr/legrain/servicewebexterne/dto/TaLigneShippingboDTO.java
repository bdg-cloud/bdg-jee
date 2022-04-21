package fr.legrain.servicewebexterne.dto;

import fr.legrain.bdg.model.ModelObject;

public class TaLigneShippingboDTO  extends ModelObject implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1985338485948522208L;

	
	private Integer id;
	
	private String libelle;

	private Integer versionObj;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public Integer getVersionObj() {
		return versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

}
