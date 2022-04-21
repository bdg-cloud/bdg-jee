package fr.legrain.servicewebexterne.dto;

import fr.legrain.bdg.model.ModelObject;

public class TaTServiceWebExterneDTO extends ModelObject implements java.io.Serializable {

	private static final long serialVersionUID = 9034964903325386177L;

	private Integer id;
	
	private String codeTServiceWebExterne;
	private String libelleTServiceWebExterne;
	private String descriptionTServiceWebExterne;
	private boolean systeme;
	
	private Integer versionObj;

	public TaTServiceWebExterneDTO() {
	}

	public TaTServiceWebExterneDTO(Integer id, String codeTServiceWebExterne,
			String libelleTServiceWebExterne, String descriptionTServiceWebExterne, boolean systeme) {
		super();
		this.id = id;
		this.codeTServiceWebExterne = codeTServiceWebExterne;
		this.libelleTServiceWebExterne = libelleTServiceWebExterne;
		this.descriptionTServiceWebExterne = descriptionTServiceWebExterne;
		this.systeme = systeme;
	}

	public void setTaTServiceWebExterneDTO(TaTServiceWebExterneDTO taTServiceWebExterneDTO) {
		this.id = taTServiceWebExterneDTO.id;
		this.codeTServiceWebExterne = taTServiceWebExterneDTO.codeTServiceWebExterne;
		this.libelleTServiceWebExterne = taTServiceWebExterneDTO.libelleTServiceWebExterne;
		this.descriptionTServiceWebExterne = taTServiceWebExterneDTO.descriptionTServiceWebExterne;
		this.systeme = taTServiceWebExterneDTO.systeme;
	}

	public static TaTServiceWebExterneDTO copy(TaTServiceWebExterneDTO taTServiceWebExterneDTO){
		TaTServiceWebExterneDTO taTServiceWebExterneDTOLoc = new TaTServiceWebExterneDTO();
		taTServiceWebExterneDTOLoc.setId(taTServiceWebExterneDTO.getId());        
		taTServiceWebExterneDTOLoc.setCodeTServiceWebExterne(taTServiceWebExterneDTO.getCodeTServiceWebExterne());                
		taTServiceWebExterneDTOLoc.setDescriptionTServiceWebExterne(taTServiceWebExterneDTO.getDescriptionTServiceWebExterne());           
		taTServiceWebExterneDTOLoc.setLibelleTServiceWebExterne(taTServiceWebExterneDTO.getLibelleTServiceWebExterne());             
		taTServiceWebExterneDTOLoc.setSysteme(taTServiceWebExterneDTO.isSysteme());              
		return taTServiceWebExterneDTOLoc;
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

	public String getCodeTServiceWebExterne() {
		return codeTServiceWebExterne;
	}

	public void setCodeTServiceWebExterne(String codeTServiceWebExterne) {
		firePropertyChange("codeTServiceWebExterne", this.codeTServiceWebExterne, this.codeTServiceWebExterne = codeTServiceWebExterne);
	}

	public String getLibelleTServiceWebExterne() {
		return libelleTServiceWebExterne;
	}

	public void setLibelleTServiceWebExterne(String libelleTServiceWebExterne) {
		firePropertyChange("libelleTServiceWebExterne", this.libelleTServiceWebExterne, this.libelleTServiceWebExterne = libelleTServiceWebExterne);
	}

	public String getDescriptionTServiceWebExterne() {
		return descriptionTServiceWebExterne;
	}

	public void setDescriptionTServiceWebExterne(String descriptionTServiceWebExterne) {
		firePropertyChange("descriptionTServiceWebExterne", this.descriptionTServiceWebExterne, this.descriptionTServiceWebExterne = descriptionTServiceWebExterne);
	}

	public boolean isSysteme() {
		return systeme;
	}

	public void setSysteme(boolean systeme) {
		firePropertyChange("systeme", this.systeme, this.systeme = systeme);
	}
	

}
