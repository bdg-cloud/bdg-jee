package fr.legrain.tache.dto;

import java.util.Date;

import fr.legrain.bdg.model.ModelObject;

public class TaRecurrenceEvenementDTO extends ModelObject implements java.io.Serializable {

	private static final long serialVersionUID = 3519096432052065049L;

	private Integer id;
	
	private Date dateDebutRecurrence;
	private Date dateFinRecurrence;
	
	private Integer versionObj;

	public TaRecurrenceEvenementDTO() {
	}

	public void setTaRecurrenceEvenementDTO(TaRecurrenceEvenementDTO taRecurrenceEvenementDTO) {
		this.id = taRecurrenceEvenementDTO.id;
		this.dateDebutRecurrence = taRecurrenceEvenementDTO.dateDebutRecurrence;
		this.dateFinRecurrence = taRecurrenceEvenementDTO.dateFinRecurrence;
	}

	public static TaRecurrenceEvenementDTO copy(TaRecurrenceEvenementDTO taRecurrenceEvenementDTO){
		TaRecurrenceEvenementDTO taRecurrenceEvenementDTOLoc = new TaRecurrenceEvenementDTO();
		taRecurrenceEvenementDTOLoc.setId(taRecurrenceEvenementDTO.getId());              
		taRecurrenceEvenementDTOLoc.setDateDebutRecurrence(taRecurrenceEvenementDTO.getDateDebutRecurrence());                
		taRecurrenceEvenementDTOLoc.setDateFinRecurrence(taRecurrenceEvenementDTO.getDateFinRecurrence());                       
		return taRecurrenceEvenementDTOLoc;
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

	public Date getDateDebutRecurrence() {
		return dateDebutRecurrence;
	}

	public void setDateDebutRecurrence(Date dateDebutRecurrence) {
		firePropertyChange("dateDebutRecurrence", this.dateDebutRecurrence, this.dateDebutRecurrence = dateDebutRecurrence);
	}

	public Date getDateFinRecurrence() {
		return dateFinRecurrence;
	}

	public void setDateFinRecurrence(Date dateFinRecurrence) {
		firePropertyChange("dateFinRecurrence", this.dateFinRecurrence, this.dateFinRecurrence = dateFinRecurrence);
	}

}
