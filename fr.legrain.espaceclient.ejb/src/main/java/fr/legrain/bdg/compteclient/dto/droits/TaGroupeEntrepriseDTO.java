package fr.legrain.bdg.compteclient.dto.droits;

import fr.legrain.bdg.compteclient.dto.ModelObject;


public class TaGroupeEntrepriseDTO extends ModelObject implements java.io.Serializable {

	private static final long serialVersionUID = -8651654080966734272L;

	private Integer id;
	private String nom;
	private Integer versionObj;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public Integer getVersionObj() {
		return versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}
	
}
