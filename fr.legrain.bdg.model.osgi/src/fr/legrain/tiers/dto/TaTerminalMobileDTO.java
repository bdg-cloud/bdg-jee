package fr.legrain.tiers.dto;

import java.util.Date;

import fr.legrain.bdg.model.ModelObject;
import fr.legrain.tiers.model.TaTiers;

public class TaTerminalMobileDTO extends ModelObject implements java.io.Serializable {

	private Integer id;
	
	private String androidRegistrationToken;
	private Date dateDerniereConnexion;
	
	private Integer versionObj;

	public TaTerminalMobileDTO() {
	}
	
	public TaTerminalMobileDTO(Integer id,Integer versionObj, Date dateDerniereConnexion,String androidRegistrationToken) {
		super();
		this.id = id;
		this.versionObj = versionObj;
		this.dateDerniereConnexion = dateDerniereConnexion;
		this.androidRegistrationToken = androidRegistrationToken;
	}

	public void setEspaceClientDTO(TaTerminalMobileDTO taEspaceClientDTO) {
		this.id = taEspaceClientDTO.id;
	}
	
	public static TaTerminalMobileDTO copy(TaTerminalMobileDTO taEspaceClientDTO){
		TaTerminalMobileDTO taEspaceClientDTOLoc = new TaTerminalMobileDTO();
		taEspaceClientDTOLoc.setId(taEspaceClientDTO.getId());                      
		return taEspaceClientDTOLoc;
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

	public Date getDateDerniereConnexion() {
		return dateDerniereConnexion;
	}

	public void setDateDerniereConnexion(Date dateDerniereConnexion) {
		this.dateDerniereConnexion = dateDerniereConnexion;
	}
	
	public String getAndroidRegistrationToken() {
		return androidRegistrationToken;
	}

	public void setAndroidRegistrationToken(String androidRegistrationToken) {
		this.androidRegistrationToken = androidRegistrationToken;
	}

}
