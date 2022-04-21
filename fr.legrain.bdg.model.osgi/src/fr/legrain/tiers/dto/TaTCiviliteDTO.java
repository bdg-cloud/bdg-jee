package fr.legrain.tiers.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import fr.legrain.bdg.model.ModelObject;
import fr.legrain.validator.LgrHibernateValidated;

public class TaTCiviliteDTO extends ModelObject implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1616221493329643359L;
	private Integer id;
	private Integer versionObj;
	
//	@NotNull
//	@Pattern(regexp = "[A-Za-z -]*")
//  @Pattern(regexp = "d{3})d{3}-d{4}") //phoneNumber
//	@Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\."
//	        +"[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@"
//	        +"(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?",
//	             message="{invalid.email}")	
//	@Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$",
//            message="{invalid.phonenumber}")
//	@Past //pour une date
//  @Future // pour une date 	
	private String codeTCivilite;
	
	
	public TaTCiviliteDTO() {
		
	}
	
	@NotNull
	@Size(min=1, max=100)	
	@LgrHibernateValidated(champBd = "codeTCivilite",table = "ta_t_civilite",champEntite="codeTCivilite",clazz = TaTCiviliteDTO.class)
	public String getCodeTCivilite() {
		return codeTCivilite;
	}

	public void setCodeTCivilite(String codeTCivilite) {
		//this.codeTCivilite = codeTCivilite;
		firePropertyChange("codeTCivilite", this.codeTCivilite, this.codeTCivilite = codeTCivilite);
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

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof TaTCiviliteDTO))
			return false;
		TaTCiviliteDTO castOther = (TaTCiviliteDTO) other;

		return ((this.getId() == castOther.getId()) || (this
				.getId() != null
				&& castOther.getId() != null && this
				.getId().equals(castOther.getId())))
				&& ((this.getCodeTCivilite() == castOther
						.getCodeTCivilite()) || (this.getCodeTCivilite() != null
						&& castOther.getCodeTCivilite() != null && this
						.getCodeTCivilite().equals(
								castOther.getCodeTCivilite())));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getId() == null ? 0 : this.getId()
						.hashCode());
		result = 37
				* result
				+ (getCodeTCivilite() == null ? 0 : this.getCodeTCivilite()
						.hashCode());
		return result;
	}

	
	public static TaTCiviliteDTO copy(TaTCiviliteDTO taTCiviliteDTO){
		TaTCiviliteDTO taTCiviliteDTOLoc = new TaTCiviliteDTO();
		taTCiviliteDTOLoc.setCodeTCivilite(taTCiviliteDTO.codeTCivilite);
		taTCiviliteDTOLoc.setVersionObj(taTCiviliteDTO.versionObj);
		taTCiviliteDTOLoc.setId(taTCiviliteDTO.id);
		return taTCiviliteDTOLoc;
	}
	

}
