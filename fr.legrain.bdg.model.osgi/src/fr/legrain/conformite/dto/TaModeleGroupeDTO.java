package fr.legrain.conformite.dto;

import fr.legrain.bdg.model.ModelObject;
import fr.legrain.validator.LgrHibernateValidated;

public class TaModeleGroupeDTO extends ModelObject implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7321759580043529215L;
	
	private Integer id;
	private Integer versionObj;
	

	private String codeGroupe;
	private String libelle;
	
	
	public TaModeleGroupeDTO() {
		
	}
	
	@LgrHibernateValidated(champBd = "code_groupe",table = "ta_groupe", champEntite="codeGroupe", clazz = TaModeleGroupeDTO.class)
	public String getCodeGroupe() {
		return codeGroupe;
	}

	public void setCodeGroupe(String codeGroupe) {
		firePropertyChange("codeGroupe", this.codeGroupe, this.codeGroupe = codeGroupe);
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



	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		firePropertyChange("libelle", this.libelle, this.libelle = libelle);
	}

	public TaModeleGroupeDTO(Integer id, Integer versionObj, String codeGroupe,
			String libelle) {
		super();
		this.id = id;
		this.versionObj = versionObj;
		this.codeGroupe = codeGroupe;
		this.libelle = libelle;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((codeGroupe == null) ? 0 : codeGroupe.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((libelle == null) ? 0 : libelle.hashCode());
		result = prime * result
				+ ((versionObj == null) ? 0 : versionObj.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TaModeleGroupeDTO other = (TaModeleGroupeDTO) obj;
		if (codeGroupe == null) {
			if (other.codeGroupe != null)
				return false;
		} else if (!codeGroupe.equals(other.codeGroupe))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (libelle == null) {
			if (other.libelle != null)
				return false;
		} else if (!libelle.equals(other.libelle))
			return false;
		if (versionObj == null) {
			if (other.versionObj != null)
				return false;
		} else if (!versionObj.equals(other.versionObj))
			return false;
		return true;
	}
	

}
