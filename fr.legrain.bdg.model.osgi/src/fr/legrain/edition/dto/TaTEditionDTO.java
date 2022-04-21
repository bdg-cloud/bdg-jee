package fr.legrain.edition.dto;

import fr.legrain.bdg.model.ModelObject;

public class TaTEditionDTO extends ModelObject implements java.io.Serializable {

	private static final long serialVersionUID = -1268418543643804339L;

	private Integer id;
	
	private String codeTEdition;
	private String libelle;
	private String description;
	private boolean systeme;
	
	private Integer versionObj;

	public TaTEditionDTO() {
	}

	public TaTEditionDTO(Integer id, String codeTEdition,
			String libelle, String description, boolean systeme) {
		super();
		this.id = id;
		this.codeTEdition = codeTEdition;
		this.libelle = libelle;
		this.description = description;
		this.systeme = systeme;
	}

	public void setTaTServiceWebExterneDTO(TaTEditionDTO taTServiceWebExterneDTO) {
		this.id = taTServiceWebExterneDTO.id;
		this.codeTEdition = taTServiceWebExterneDTO.codeTEdition;
		this.libelle = taTServiceWebExterneDTO.libelle;
		this.description = taTServiceWebExterneDTO.description;
		this.systeme = taTServiceWebExterneDTO.systeme;
	}

	public static TaTEditionDTO copy(TaTEditionDTO taTServiceWebExterneDTO){
		TaTEditionDTO taTServiceWebExterneDTOLoc = new TaTEditionDTO();
		taTServiceWebExterneDTOLoc.setId(taTServiceWebExterneDTO.getId());        
		taTServiceWebExterneDTOLoc.setCodeTEdition(taTServiceWebExterneDTO.getCodeTEdition());                
		taTServiceWebExterneDTOLoc.setDescription(taTServiceWebExterneDTO.getDescription());           
		taTServiceWebExterneDTOLoc.setLibelle(taTServiceWebExterneDTO.getLibelle());             
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

	public String getCodeTEdition() {
		return codeTEdition;
	}

	public void setCodeTEdition(String codeTServiceWebExterne) {
		firePropertyChange("codeTEdition", this.codeTEdition, this.codeTEdition = codeTServiceWebExterne);
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelleTServiceWebExterne) {
		firePropertyChange("libelle", this.libelle, this.libelle = libelleTServiceWebExterne);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String descriptionTServiceWebExterne) {
		firePropertyChange("description", this.description, this.description = descriptionTServiceWebExterne);
	}

	public boolean isSysteme() {
		return systeme;
	}

	public void setSysteme(boolean systeme) {
		firePropertyChange("systeme", this.systeme, this.systeme = systeme);
	}
	

}
