package fr.legrain.tiers.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import fr.legrain.bdg.model.ModelObject;
import fr.legrain.validator.LgrHibernateValidated;

public class TaTBanqueDTO extends ModelObject implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6869525430848290564L;
	private Integer id;
	private String codeTBanque;
	private String liblTBanque;
	private Integer versionObj;
	
	public TaTBanqueDTO() {
		
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		firePropertyChange("id", this.id, this.id = id);
	}
	
	@NotNull
	@Size(min=1, max=20)
	@LgrHibernateValidated(champBd = "code_t_banque",table = "ta_t_banque",champEntite="codeTBanque",clazz = TaTBanqueDTO.class)
	public String getCodeTBanque() {
		return codeTBanque;
	}
	
	public void setCodeTBanque(String codeTBanque) {
		firePropertyChange("codeTBanque", this.codeTBanque, this.codeTBanque = codeTBanque);
	}
	
	@LgrHibernateValidated(champBd = "libl_t_banque",table = "ta_t_banque",champEntite="liblTBanque",clazz = TaTBanqueDTO.class)
	public String getLiblTBanque() {
		return liblTBanque;
	}
	
	public void setLiblTBanque(String liblTBanque) {
		firePropertyChange("liblTBanque", this.liblTBanque, this.liblTBanque = liblTBanque);
	}
	
	public Integer getVersionObj() {
		return this.versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

	public static TaTBanqueDTO copy(TaTBanqueDTO taTBanqueDTO){
		TaTBanqueDTO taTBanqueDTOLoc = new TaTBanqueDTO();
		taTBanqueDTOLoc.setId(taTBanqueDTO.getId());                //1
		taTBanqueDTOLoc.setCodeTBanque(taTBanqueDTO.getCodeTBanque());        //2
		taTBanqueDTOLoc.setLiblTBanque(taTBanqueDTO.getLiblTBanque());            //3
		return taTBanqueDTOLoc;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((codeTBanque == null) ? 0 : codeTBanque.hashCode());
		result = prime * result
				+ ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((liblTBanque == null) ? 0 : liblTBanque.hashCode());
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
		TaTBanqueDTO other = (TaTBanqueDTO) obj;
		if (codeTBanque == null) {
			if (other.codeTBanque != null)
				return false;
		} else if (!codeTBanque.equals(other.codeTBanque))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (liblTBanque == null) {
			if (other.liblTBanque != null)
				return false;
		} else if (!liblTBanque.equals(other.liblTBanque))
			return false;
		return true;
	}
	
}

