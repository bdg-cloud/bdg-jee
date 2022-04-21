package fr.legrain.tache.dto;

import fr.legrain.bdg.model.ModelObject;

public class TaCategorieEvenementDTO extends ModelObject implements java.io.Serializable {

	private static final long serialVersionUID = 1730797494999173806L;
	
	private Integer id;
	
	private String codeCategorieEvenement;
	private String libelleCategorieEvenement;
	
	private Integer versionObj;

	public TaCategorieEvenementDTO() {
	}

	public void setTaCategorieEvenementDTO(TaCategorieEvenementDTO taCategorieEvenementDTO) {
		this.id = taCategorieEvenementDTO.id;
		this.codeCategorieEvenement = taCategorieEvenementDTO.codeCategorieEvenement;
		this.libelleCategorieEvenement = taCategorieEvenementDTO.libelleCategorieEvenement;
	}

	public static TaCategorieEvenementDTO copy(TaCategorieEvenementDTO taCategorieEvenementDTO){
		TaCategorieEvenementDTO taCategorieEvenementDTOLoc = new TaCategorieEvenementDTO();
		taCategorieEvenementDTOLoc.setId(taCategorieEvenementDTO.getId());              
		taCategorieEvenementDTOLoc.setCodeCategorieEvenement(taCategorieEvenementDTO.getCodeCategorieEvenement());                
		taCategorieEvenementDTOLoc.setLibelleCategorieEvenement(taCategorieEvenementDTO.getLibelleCategorieEvenement());                       
		return taCategorieEvenementDTOLoc;
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

	public String getCodeCategorieEvenement() {
		return codeCategorieEvenement;
	}

	public void setCodeCategorieEvenement(String codeCategorieEvenement) {
		firePropertyChange("codeCategorieEvenement", this.codeCategorieEvenement, this.codeCategorieEvenement = codeCategorieEvenement);
	}

	public String getLibelleCategorieEvenement() {
		return libelleCategorieEvenement;
	}

	public void setLibelleCategorieEvenement(String libelleCategorieEvenement) {
		firePropertyChange("libelleCategorieEvenement", this.libelleCategorieEvenement, this.libelleCategorieEvenement = libelleCategorieEvenement);
	}

}
