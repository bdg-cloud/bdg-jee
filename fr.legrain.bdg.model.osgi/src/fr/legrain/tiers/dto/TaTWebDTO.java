package fr.legrain.tiers.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import fr.legrain.bdg.model.ModelObject;
import fr.legrain.validator.LgrHibernateValidated;

public class TaTWebDTO extends ModelObject implements java.io.Serializable {
	
	private Integer id;
	private String codeTWeb;
	private String liblTWeb;
	private Integer versionObj;
	
	public TaTWebDTO() {
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		firePropertyChange("id", this.id, this.id = id);
	}
	
	@NotNull
	@Size(min=1, max=20)
	@LgrHibernateValidated(champBd = "code_t_web",table = "ta_t_web",champEntite="codeTWeb",clazz = TaTWebDTO.class)
	public String getCodeTWeb() {
		return codeTWeb;
	}
	
	public void setCodeTWeb(String codeTWeb) {
		firePropertyChange("codeTWeb", this.codeTWeb, this.codeTWeb = codeTWeb);
	}
	
	@LgrHibernateValidated(champBd = "libl_t_web",table = "ta_t_web",champEntite="liblTWeb",clazz = TaTWebDTO.class)
	public String getLiblTWeb() {
		return liblTWeb;
	}
	
	public void setLiblTWeb(String liblTWeb) {
		firePropertyChange("liblTWeb", this.liblTWeb, this.liblTWeb = liblTWeb);
	}
	
	public Integer getVersionObj() {
		return this.versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

	public static TaTWebDTO copy(TaTWebDTO taTWebDTO){
		TaTWebDTO taTWebDTOLoc = new TaTWebDTO();
		taTWebDTOLoc.setCodeTWeb(taTWebDTOLoc.codeTWeb);
		taTWebDTOLoc.setId(taTWebDTOLoc.id);
		taTWebDTOLoc.setLiblTWeb(taTWebDTOLoc.liblTWeb);
		return taTWebDTOLoc;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((codeTWeb == null) ? 0 : codeTWeb.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((liblTWeb == null) ? 0 : liblTWeb.hashCode());
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
		TaTWebDTO other = (TaTWebDTO) obj;
		if (codeTWeb == null) {
			if (other.codeTWeb != null)
				return false;
		} else if (!codeTWeb.equals(other.codeTWeb))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (liblTWeb == null) {
			if (other.liblTWeb != null)
				return false;
		} else if (!liblTWeb.equals(other.liblTWeb))
			return false;
		return true;
	}

}
