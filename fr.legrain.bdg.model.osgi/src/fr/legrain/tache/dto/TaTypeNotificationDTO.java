package fr.legrain.tache.dto;

import fr.legrain.bdg.model.ModelObject;

public class TaTypeNotificationDTO extends ModelObject implements java.io.Serializable {
	
	private static final long serialVersionUID = -4110326254355298097L;

	private Integer id;
	
	private String codeTypeNotification;
	private String libelleTypeNotification;
	
	private Integer versionObj;

	public TaTypeNotificationDTO() {
	}

	public void setTaTypeNotificationDTO(TaTypeNotificationDTO taTypeNotificationDTO) {
		this.id = taTypeNotificationDTO.id;
		this.codeTypeNotification = taTypeNotificationDTO.codeTypeNotification;
		this.libelleTypeNotification = taTypeNotificationDTO.libelleTypeNotification;
	}

	public static TaTypeNotificationDTO copy(TaTypeNotificationDTO taTypeNotificationDTO){
		TaTypeNotificationDTO taTypeNotificationDTOLoc = new TaTypeNotificationDTO();
		taTypeNotificationDTOLoc.setId(taTypeNotificationDTO.getId());              
		taTypeNotificationDTOLoc.setCodeTypeNotification(taTypeNotificationDTO.getCodeTypeNotification());                
		taTypeNotificationDTOLoc.setLibelleTypeNotification(taTypeNotificationDTO.getLibelleTypeNotification());                       
		return taTypeNotificationDTOLoc;
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

	public String getCodeTypeNotification() {
		return codeTypeNotification;
	}

	public void setCodeTypeNotification(String codeTypeNotification) {
		firePropertyChange("codeTypeNotification", this.codeTypeNotification, this.codeTypeNotification = codeTypeNotification);
	}

	public String getLibelleTypeNotification() {
		return libelleTypeNotification;
	}

	public void setLibelleTypeNotification(String libelleTypeNotification) {
		firePropertyChange("libelleTypeNotification", this.libelleTypeNotification, this.libelleTypeNotification = libelleTypeNotification);
	}

}
