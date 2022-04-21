package fr.legrain.abonnement.dto;

import fr.legrain.bdg.model.ModelObject;



public class TaFrequenceDTO extends ModelObject implements java.io.Serializable {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 6892691149545488533L;
	private Integer id;
	private String codeFrequence;
	private String liblFrequence;
   
	private Integer versionObj;
    
	public TaFrequenceDTO() {
	}
	
	public TaFrequenceDTO(Integer id, String codeFrequence, String liblFrequence) {
		this.id = id;
		this.codeFrequence = codeFrequence;
		this.liblFrequence = liblFrequence;
	}
	
	
	public Integer getVersionObj() {
		return versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

	public String getCodeFrequence() {
		return codeFrequence;
	}

	public void setCodeFrequence(String codeFrequence) {
		this.codeFrequence = codeFrequence;
	}
	public String getLiblFrequence() {
		return liblFrequence;
	}

	public void setLiblFrequence(String liblFrequence) {
		this.liblFrequence = liblFrequence;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}



	
	
}
