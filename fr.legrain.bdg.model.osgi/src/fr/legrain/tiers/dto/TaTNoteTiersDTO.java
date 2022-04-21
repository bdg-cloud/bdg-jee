package fr.legrain.tiers.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import fr.legrain.bdg.model.ModelObject;
import fr.legrain.validator.LgrHibernateValidated;

public class TaTNoteTiersDTO extends ModelObject implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7328944917378848315L;
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = -2278035457000817719L;
	
	private Integer id;
	private String codeTNoteTiers;
	private String liblTNoteTiers;
	private Integer versionObj;
	
	public TaTNoteTiersDTO() {
		
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		firePropertyChange("id", this.id, this.id = id);
	}
	
	@NotNull
	@Size(min=1, max=20)
	@LgrHibernateValidated(champBd = "code_t_note_tiers",table = "ta_t_note_tiers",champEntite="codeTNoteTiers",clazz = TaTNoteTiersDTO.class)
	public String getCodeTNoteTiers() {
		return codeTNoteTiers;
	}
	
	public void setCodeTNoteTiers(String codeTNoteTiers) {
		firePropertyChange("codeTNoteTiers", this.codeTNoteTiers, this.codeTNoteTiers = codeTNoteTiers);
	}
	
	@LgrHibernateValidated(champBd = "libl_t_note_tiers",table = "ta_t_note_tiers",champEntite="liblTNoteTiers",clazz = TaTNoteTiersDTO.class)
	public String getLiblTNoteTiers() {
		return liblTNoteTiers;
	}
	
	public void setLiblTNoteTiers(String liblTNoteTiers) {
		firePropertyChange("liblTNoteTiers", this.liblTNoteTiers, this.liblTNoteTiers = liblTNoteTiers);
	}

	public Integer getVersionObj() {
		return this.versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}
	
	public static TaTNoteTiersDTO copy(TaTNoteTiersDTO swtTNoteTiers){
		TaTNoteTiersDTO swtTNoteTiersLoc = new TaTNoteTiersDTO();
		swtTNoteTiersLoc.setCodeTNoteTiers(swtTNoteTiers.codeTNoteTiers);
		swtTNoteTiersLoc.setId(swtTNoteTiers.id);
		swtTNoteTiersLoc.setLiblTNoteTiers(swtTNoteTiers.liblTNoteTiers);
		return swtTNoteTiersLoc;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((codeTNoteTiers == null) ? 0 : codeTNoteTiers.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((liblTNoteTiers == null) ? 0 : liblTNoteTiers.hashCode());
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
		TaTNoteTiersDTO other = (TaTNoteTiersDTO) obj;
		if (codeTNoteTiers == null) {
			if (other.codeTNoteTiers != null)
				return false;
		} else if (!codeTNoteTiers.equals(other.codeTNoteTiers))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (liblTNoteTiers == null) {
			if (other.liblTNoteTiers != null)
				return false;
		} else if (!liblTNoteTiers.equals(other.liblTNoteTiers))
			return false;
		if (versionObj == null) {
			if (other.versionObj != null)
				return false;
		} else if (!versionObj.equals(other.versionObj))
			return false;
		return true;
	}

	
//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result
//				+ ((codeTNoteTiers == null) ? 0 : codeTNoteTiers.hashCode());
//		result = prime * result + ((id == null) ? 0 : id.hashCode());
//		result = prime * result
//				+ ((liblTNoteTiers == null) ? 0 : liblTNoteTiers.hashCode());
//		return result;
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		TaTNoteTiersDTO other = (TaTNoteTiersDTO) obj;
//		if (codeTNoteTiers == null) {
//			if (other.codeTNoteTiers != null)
//				return false;
//		} else if (!codeTNoteTiers.equals(other.codeTNoteTiers))
//			return false;
//		if (id == null) {
//			if (other.id != null)
//				return false;
//		} else if (!id.equals(other.id))
//			return false;
//		if (liblTNoteTiers == null) {
//			if (other.liblTNoteTiers != null)
//				return false;
//		} else if (!liblTNoteTiers.equals(other.liblTNoteTiers))
//			return false;
//		return true;
//	}

}
