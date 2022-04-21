package fr.legrain.conformite.dto;

import fr.legrain.bdg.model.ModelObject;
import fr.legrain.validator.LgrHibernateValidated;

public class TaStatusConformiteDTO extends ModelObject implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7321759580043529215L;
	
	private Integer id;
	private Integer versionObj;
	

	private String codeStatusConformite;
	private String libelleStatusConformite;
	
	
	public TaStatusConformiteDTO() {
		
	}
	
	public String getCodeStatusConformite() {
		return codeStatusConformite;
	}

	public void setCodeStatusConformite(String codeGroupe) {
		firePropertyChange("codeStatusConformite", this.codeStatusConformite, this.codeStatusConformite = codeStatusConformite);
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



	public String getLibelleStatusConformite() {
		return libelleStatusConformite;
	}

	public void setLibelleStatusConformite(String libelle) {
		firePropertyChange("libelleStatusConformite", this.libelleStatusConformite, this.libelleStatusConformite = libelle);
	}

	public TaStatusConformiteDTO(Integer id, Integer versionObj, String codeStatusConformite,
			String libelleStatusConformite) {
		super();
		this.id = id;
		this.versionObj = versionObj;
		this.codeStatusConformite = codeStatusConformite;
		this.libelleStatusConformite = libelleStatusConformite;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((codeStatusConformite == null) ? 0 : codeStatusConformite.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((libelleStatusConformite == null) ? 0 : libelleStatusConformite.hashCode());
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
		TaStatusConformiteDTO other = (TaStatusConformiteDTO) obj;
		if (codeStatusConformite == null) {
			if (other.codeStatusConformite != null)
				return false;
		} else if (!codeStatusConformite.equals(other.codeStatusConformite))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (libelleStatusConformite == null) {
			if (other.libelleStatusConformite != null)
				return false;
		} else if (!libelleStatusConformite.equals(other.libelleStatusConformite))
			return false;
		if (versionObj == null) {
			if (other.versionObj != null)
				return false;
		} else if (!versionObj.equals(other.versionObj))
			return false;
		return true;
	}
	

}
