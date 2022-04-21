package fr.legrain.tache.dto;

import fr.legrain.bdg.model.ModelObject;

public class TaTypeEvenementDTO extends ModelObject implements java.io.Serializable {
	
	private static final long serialVersionUID = -22505600418257048L;

	private Integer id;
	
	private String codeTypeEvenement;
	private String libelleTypeEvenement;
	private int idCategorieEvenement;
	private String codeCategorieEvenement;
	private String libelleCategorieEvenement;
	
	private Integer versionObj;

	public TaTypeEvenementDTO() {
	}

	public void setTaTypeEvenementDTO(TaTypeEvenementDTO taTypeEvenementDTO) {
		this.id = taTypeEvenementDTO.id;
		this.codeCategorieEvenement = taTypeEvenementDTO.codeCategorieEvenement;
		this.libelleCategorieEvenement = taTypeEvenementDTO.libelleCategorieEvenement;
		this.idCategorieEvenement = taTypeEvenementDTO.idCategorieEvenement;
		this.codeTypeEvenement = taTypeEvenementDTO.codeTypeEvenement;
		this.libelleTypeEvenement = taTypeEvenementDTO.libelleTypeEvenement;
		
	}

	public static TaTypeEvenementDTO copy(TaTypeEvenementDTO taTypeEvenementDTO){
		TaTypeEvenementDTO taTypeEvenementDTOLoc = new TaTypeEvenementDTO();
		taTypeEvenementDTOLoc.setId(taTypeEvenementDTO.getId());              
		taTypeEvenementDTOLoc.setCodeCategorieEvenement(taTypeEvenementDTO.getCodeCategorieEvenement());                
		taTypeEvenementDTOLoc.setLibelleCategorieEvenement(taTypeEvenementDTO.getLibelleCategorieEvenement());
		taTypeEvenementDTOLoc.setCodeTypeEvenement(taTypeEvenementDTO.getCodeTypeEvenement());    
		taTypeEvenementDTOLoc.setLibelleTypeEvenement(taTypeEvenementDTO.getLibelleTypeEvenement());    
		taTypeEvenementDTOLoc.setIdCategorieEvenement(taTypeEvenementDTO.getIdCategorieEvenement());    
		return taTypeEvenementDTOLoc;
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

	public String getCodeTypeEvenement() {
		return codeTypeEvenement;
	}

	public void setCodeTypeEvenement(String codeTypeEvenement) {
		firePropertyChange("codeTypeEvenement", this.codeTypeEvenement, this.codeTypeEvenement = codeTypeEvenement);
	}

	public String getLibelleTypeEvenement() {
		return libelleTypeEvenement;
	}

	public void setLibelleTypeEvenement(String libelleTypeEvenement) {
		firePropertyChange("libelleTypeEvenement", this.libelleTypeEvenement, this.libelleTypeEvenement = libelleTypeEvenement);
	}

	public int getIdCategorieEvenement() {
		return idCategorieEvenement;
	}

	public void setIdCategorieEvenement(int idCategorieEvenement) {
		firePropertyChange("idCategorieEvenement", this.idCategorieEvenement, this.idCategorieEvenement = idCategorieEvenement);
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
