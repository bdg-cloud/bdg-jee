package fr.legrain.document.dto;

import fr.legrain.bdg.model.ModelObject;
import fr.legrain.tiers.dto.TaAdresseDTO;

public class TaParamPublipostageDTO extends ModelObject implements java.io.Serializable {

	private static final long serialVersionUID = -6438482925187798249L;

	private Integer id;

	
	private Integer versionObj;

	public TaParamPublipostageDTO() {
	}

	public void setTaParamPublipostageDTO(TaParamPublipostageDTO taParamPublipostageDTO) {
		this.id = taParamPublipostageDTO.id;

	}

	
	public static TaParamPublipostageDTO copy(TaParamPublipostageDTO taParamPublipostageDTO){
		TaParamPublipostageDTO taParamPublipostageDTOLoc = new TaParamPublipostageDTO();
		taParamPublipostageDTOLoc.setId(taParamPublipostageDTO.getId());                //1
		             //1
		return taParamPublipostageDTOLoc;
	}
	public Integer getId() {
		return this.id;
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

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof TaAdresseDTO))
			return false;
		TaAdresseDTO castOther = (TaAdresseDTO) other;

		return ((this.getId() == castOther.getId()) || (this
				.getId() != null
				&& castOther.getId() != null && this.getId()
				.equals(castOther.getId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getId() == null ? 0 : this.getId()
						.hashCode());
		
		return result;
	}
	
}
