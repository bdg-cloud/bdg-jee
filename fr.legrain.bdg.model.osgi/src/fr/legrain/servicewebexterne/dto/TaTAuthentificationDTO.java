package fr.legrain.servicewebexterne.dto;

import fr.legrain.bdg.model.ModelObject;

public class TaTAuthentificationDTO extends ModelObject implements java.io.Serializable {

	private static final long serialVersionUID = 2052735802717270284L;

	private Integer id;
	
	private String codeTAuthentification;
	private String libelleTAuthentification;
	private String descriptionTAuthentification;
	private boolean systeme;
	
	private Integer versionObj;

	public TaTAuthentificationDTO() {
	}

	public TaTAuthentificationDTO(Integer id, String codeTAuthentification,
			String libelleTAuthentification, String descriptionTAuthentification, boolean systeme) {
		super();
		this.id = id;
		this.codeTAuthentification = codeTAuthentification;
		this.libelleTAuthentification = libelleTAuthentification;
		this.descriptionTAuthentification = descriptionTAuthentification;
		this.systeme = systeme;
	}

	public void setTaTAuthentificationDTO(TaTAuthentificationDTO taTAuthentificationDTO) {
		this.id = taTAuthentificationDTO.id;
		this.codeTAuthentification = taTAuthentificationDTO.codeTAuthentification;
		this.libelleTAuthentification = taTAuthentificationDTO.libelleTAuthentification;
		this.descriptionTAuthentification = taTAuthentificationDTO.descriptionTAuthentification;
		this.systeme = taTAuthentificationDTO.systeme;
	}

	public static TaTAuthentificationDTO copy(TaTAuthentificationDTO taTAuthentificationDTO){
		TaTAuthentificationDTO taTAuthentificationDTOLoc = new TaTAuthentificationDTO();
		taTAuthentificationDTOLoc.setId(taTAuthentificationDTO.getId());              
		taTAuthentificationDTOLoc.setCodeTAuthentification(taTAuthentificationDTO.getCodeTAuthentification());           
		taTAuthentificationDTOLoc.setLibelleTAuthentification(taTAuthentificationDTO.getLibelleTAuthentification());             
		taTAuthentificationDTOLoc.setDescriptionTAuthentification(taTAuthentificationDTO.getDescriptionTAuthentification());              
		taTAuthentificationDTOLoc.setSysteme(taTAuthentificationDTO.isSysteme());              
		return taTAuthentificationDTOLoc;
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

	public String getCodeTAuthentification() {
		return codeTAuthentification;
	}

	public void setCodeTAuthentification(String codeTAuthentification) {
		firePropertyChange("codeTAuthentification", this.codeTAuthentification, this.codeTAuthentification = codeTAuthentification);
	}

	public String getLibelleTAuthentification() {
		return libelleTAuthentification;
	}

	public void setLibelleTAuthentification(String libelleTAuthentification) {
		firePropertyChange("libelleTAuthentification", this.libelleTAuthentification, this.libelleTAuthentification = libelleTAuthentification);
	}

	public String getDescriptionTAuthentification() {
		return descriptionTAuthentification;
	}

	public void setDescriptionTAuthentification(String descriptionTAuthentification) {
		firePropertyChange("descriptionTAuthentification", this.descriptionTAuthentification, this.descriptionTAuthentification = descriptionTAuthentification);
	}

	public boolean isSysteme() {
		return systeme;
	}

	public void setSysteme(boolean systeme) {
		firePropertyChange("systeme", this.systeme, this.systeme = systeme);
	}

	
}
