package fr.legrain.tiers.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import fr.legrain.bdg.model.ModelObject;
import fr.legrain.validator.LgrHibernateValidated;

public class TaTLiensDTO extends ModelObject implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -799650419094751829L;
	private Integer id;
	private String codeTLiens;
	private String liblTLiens;
	private Integer versionObj;
	
	public TaTLiensDTO(){
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		firePropertyChange("id", this.id, this.id = id);
	}
	
	@NotNull
	@Size(min=1, max=20)
	@LgrHibernateValidated(champBd = "code_t_liens",table = "ta_t_liens",champEntite="codeTLiens",clazz = TaTLiensDTO.class)
	public String getCodeTLiens() {
		return codeTLiens;
	}
	
	public void setCodeTLiens(String codeTLiens) {
		firePropertyChange("codeTLiens", this.codeTLiens, this.codeTLiens = codeTLiens);
	}
	
	@LgrHibernateValidated(champBd = "libl_t_liens",table = "ta_t_liens",champEntite="liblTLiens",clazz = TaTLiensDTO.class)
	public String getLiblTLiens() {
		return liblTLiens;
	}
	
	public void setLiblTLiens(String liblTLiens) {
		firePropertyChange("liblTLiens", this.liblTLiens, this.liblTLiens = liblTLiens);
	}
	
	public Integer getVersionObj() {
		return this.versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}
	
	public static TaTLiensDTO copy(TaTLiensDTO swtTLiens){
		TaTLiensDTO swtTLiensLoc = new TaTLiensDTO();
		swtTLiensLoc.setCodeTLiens(swtTLiens.codeTLiens);
		swtTLiensLoc.setId(swtTLiens.id);
		swtTLiensLoc.setLiblTLiens(swtTLiens.liblTLiens);
		return swtTLiensLoc;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((codeTLiens == null) ? 0 : codeTLiens.hashCode());
		result = prime * result
				+ ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((liblTLiens == null) ? 0 : liblTLiens.hashCode());
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
		TaTLiensDTO other = (TaTLiensDTO) obj;
		if (codeTLiens == null) {
			if (other.codeTLiens != null)
				return false;
		} else if (!codeTLiens.equals(other.codeTLiens))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (liblTLiens == null) {
			if (other.liblTLiens != null)
				return false;
		} else if (!liblTLiens.equals(other.liblTLiens))
			return false;
		return true;
	}
	
}
