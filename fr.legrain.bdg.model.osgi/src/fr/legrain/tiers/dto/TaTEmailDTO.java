package fr.legrain.tiers.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import fr.legrain.bdg.model.ModelObject;
import fr.legrain.validator.LgrHibernateValidated;

public class TaTEmailDTO extends ModelObject implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4562609674283690935L;
	private Integer id;
	private String codeTEmail;
	private String liblTEmail;
	private Integer versionObj;
	
	public TaTEmailDTO() {
		
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		firePropertyChange("id", this.id, this.id = id);
	}
	
	@NotNull
	@Size(min=1, max=20)
	@LgrHibernateValidated(champBd = "code_t_email",table = "ta_t_email",champEntite="codeTEmail",clazz = TaTEmailDTO.class)
	public String getCodeTEmail() {
		return codeTEmail;
	}
	
	public void setCodeTEmail(String codeTEmail) {
		firePropertyChange("codeTEmail", this.codeTEmail, this.codeTEmail = codeTEmail);
	}
	
	@LgrHibernateValidated(champBd = "libl_t_email",table = "ta_t_email",champEntite="liblTEmail",clazz = TaTEmailDTO.class)
	public String getLiblTEmail() {
		return liblTEmail;
	}
	
	public void setLiblTEmail(String liblTEmail) {
		firePropertyChange("liblTEmail", this.liblTEmail, this.liblTEmail = liblTEmail);
	}
	
	public Integer getVersionObj() {
		return this.versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}
	
	public static TaTEmailDTO copy(TaTEmailDTO taTEmailDTO){
		TaTEmailDTO taTEmailDTOLoc = new TaTEmailDTO();
		taTEmailDTOLoc.setId(taTEmailDTO.id);             
		taTEmailDTOLoc.setCodeTEmail(taTEmailDTO.codeTEmail);        
		taTEmailDTOLoc.setLiblTEmail(taTEmailDTO.liblTEmail);            
		return taTEmailDTOLoc;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((codeTEmail == null) ? 0 : codeTEmail.hashCode());
		result = prime * result
				+ ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((liblTEmail == null) ? 0 : liblTEmail.hashCode());
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
		TaTEmailDTO other = (TaTEmailDTO) obj;
		if (codeTEmail == null) {
			if (other.codeTEmail != null)
				return false;
		} else if (!codeTEmail.equals(other.codeTEmail))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (liblTEmail == null) {
			if (other.liblTEmail != null)
				return false;
		} else if (!liblTEmail.equals(other.liblTEmail))
			return false;
		return true;
	}

}
