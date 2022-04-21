package fr.legrain.tiers.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import fr.legrain.bdg.model.ModelObject;
import fr.legrain.validator.LgrHibernateValidated;

// Generated Mar 1, 2007 11:26:10 AM by Hibernate Tools 3.2.0.b9

public class TaNoteTiersDTO extends ModelObject implements java.io.Serializable {

	private Integer id;
	private String noteTiers;
	private String codeTNoteTiers;
	private String codeTiers;
	private Integer idTiers;
	private Date dateNoteTiers = new Date();
	private Integer versionObj;

	public TaNoteTiersDTO() {
	}

	public void setSWTNote(TaNoteTiersDTO taNoteTiersDTO) {
		this.id = taNoteTiersDTO.id;
		this.noteTiers = taNoteTiersDTO.noteTiers;
		this.codeTiers = taNoteTiersDTO.codeTiers;
		this.idTiers = taNoteTiersDTO.idTiers;
		this.codeTNoteTiers = taNoteTiersDTO.codeTNoteTiers;
		this.dateNoteTiers = taNoteTiersDTO.dateNoteTiers;
	}

	public static TaNoteTiersDTO copy(TaNoteTiersDTO taNoteTiersDTO){
		TaNoteTiersDTO taNoteTiersDTOLoc = new TaNoteTiersDTO();
		taNoteTiersDTOLoc.setIdTiers(taNoteTiersDTO.getIdTiers());                //1
		taNoteTiersDTOLoc.setId(taNoteTiersDTO.getId());                //1
		taNoteTiersDTOLoc.setNoteTiers(taNoteTiersDTO.getNoteTiers());                //1
		taNoteTiersDTOLoc.setCodeTiers(taNoteTiersDTO.getCodeTiers());                //1
		taNoteTiersDTOLoc.setCodeTNoteTiers(taNoteTiersDTO.getCodeTNoteTiers());                //1
		taNoteTiersDTOLoc.setDateNoteTiers(taNoteTiersDTO.getDateNoteTiers());        
		return taNoteTiersDTOLoc;
	}
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer idNoteTiers) {
		firePropertyChange("id", this.id, this.id = idNoteTiers);
	}

	@LgrHibernateValidated(champBd = "note_tiers",table = "ta_note_tiers",champEntite="noteTiers",clazz = TaNoteTiersDTO.class)
	public String getNoteTiers() {
		return this.noteTiers;
	}

	public void setNoteTiers(String noteTiers) {
		firePropertyChange("noteTiers", this.noteTiers, this.noteTiers = noteTiers);
	}
	
	@NotNull
	@Size(min=1, max=20)
	@LgrHibernateValidated(champBd = "code_t_note_tiers",table = "ta_t_note_tiers",champEntite="codeTNoteTiers",clazz = TaNoteTiersDTO.class)
	public String getCodeTNoteTiers() {
		return this.codeTNoteTiers;
	}

	public void setCodeTNoteTiers(String codeTNoteTiers) {
		firePropertyChange("codeTNoteTiers", this.codeTNoteTiers, this.codeTNoteTiers = codeTNoteTiers);
	}

	@NotNull
	@Size(min=1, max=20)
	@LgrHibernateValidated(champBd = "code_tiers",table = "ta_tiers",champEntite="codeTiers",clazz = TaNoteTiersDTO.class)
	public String getCodeTiers() {
		return this.codeTiers;
	}

	public void setCodeTiers(String codeTiers) {
		firePropertyChange("codeTiers", this.codeTiers, this.codeTiers = codeTiers);
	}

	public Integer getIdTiers() {
		return this.idTiers;
	}

	public void setIdTiers(Integer idTiers) {
		firePropertyChange("idTiers", this.idTiers, this.idTiers = idTiers);
	}
	
	
	@LgrHibernateValidated(champBd = "date_note_tiers",table = "ta_note_tiers",champEntite="dateNoteTiers",clazz = TaNoteTiersDTO.class)
	public Date getDateNoteTiers() {
		return this.dateNoteTiers;
	}

	public void setDateNoteTiers(Date dateNoteTiers) {
		firePropertyChange("dateNoteTiers", this.dateNoteTiers, this.dateNoteTiers = dateNoteTiers);
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
		if (!(other instanceof TaNoteTiersDTO))
			return false;
		TaNoteTiersDTO castOther = (TaNoteTiersDTO) other;

		return ((this.getId() == castOther.getId()) || (this
				.getId() != null
				&& castOther.getId() != null && this.getId().equals(
				castOther.getId())))
				&& ((this.getNoteTiers() == castOther.getNoteTiers()) || (this
						.getNoteTiers() != null
						&& castOther.getNoteTiers() != null && this
						.getNoteTiers().equals(castOther.getNoteTiers())))
				&& ((this.getCodeTiers() == castOther.getCodeTiers()) || (this
						.getCodeTiers() != null
						&& castOther.getCodeTiers() != null && this
						.getCodeTiers().equals(castOther.getCodeTiers())))
				&& ((this.getDateNoteTiers() == castOther.getDateNoteTiers()) || (this
						.getDateNoteTiers() != null
						&& castOther.getDateNoteTiers() != null && this
						.getDateNoteTiers().equals(castOther.getDateNoteTiers())))
				&& ((this.getIdTiers() == castOther.getIdTiers()) || (this
						.getIdTiers() != null
						&& castOther.getIdTiers() != null && this
						.getIdTiers().equals(castOther.getIdTiers())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getId() == null ? 0 : this.getId().hashCode());
		result = 37
				* result
				+ (getNoteTiers() == null ? 0 : this.getNoteTiers()
						.hashCode());
		result = 37
				* result
				+ (getDateNoteTiers() == null ? 0 : this.getDateNoteTiers()
						.hashCode());
		result = 37
				* result
				+ (getCodeTiers() == null ? 0 : this.getCodeTiers()
						.hashCode());
		result = 37 * result
				+ (getIdTiers() == null ? 0 : this.getIdTiers().hashCode());
		return result;
	}



}
