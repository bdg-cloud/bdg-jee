package fr.legrain.general.dto;

import fr.legrain.bdg.model.ModelObject;

public class TaParametreDTO extends ModelObject implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1692275288470761802L;
	private int id;
	private String dossierMaitre;

	
	private Integer versionObj;

	public TaParametreDTO() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDossierMaitre() {
		return dossierMaitre;
	}

	public void setDossierMaitre(String dossierMaitre) {
		this.dossierMaitre = dossierMaitre;
	}


	public Integer getVersionObj() {
		return versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

	

}
