package fr.legrain.edition.dto;

import fr.legrain.bdg.model.ModelObject;

public class TaActionInterneDTO extends ModelObject implements java.io.Serializable {

	private static final long serialVersionUID = -7661408016505107661L;

	private Integer id;
	
	private String idAction;
	private String libelle;
	private String description;
	private boolean systeme;
	
	private Integer versionObj;

	public TaActionInterneDTO() {
	}

	public TaActionInterneDTO(Integer id, String idAction,
			String libelle, String description, boolean systeme) {
		super();
		this.id = id;
		this.idAction = idAction;
		this.libelle = libelle;
		this.description = description;
		this.systeme = systeme;
	}

	public void setTaActionInterneDTO(TaActionInterneDTO taActionInterneDTO) {
		this.id = taActionInterneDTO.id;
		this.idAction = taActionInterneDTO.idAction;
		this.libelle = taActionInterneDTO.libelle;
		this.description = taActionInterneDTO.description;
		this.systeme = taActionInterneDTO.systeme;
	}

	public static TaActionInterneDTO copy(TaActionInterneDTO taActionInterneDTO){
		TaActionInterneDTO taActionInterneDTOLoc = new TaActionInterneDTO();
		taActionInterneDTOLoc.setId(taActionInterneDTO.getId());        
		taActionInterneDTOLoc.setIdAction(taActionInterneDTO.getIdAction());                
		taActionInterneDTOLoc.setDescription(taActionInterneDTO.getDescription());           
		taActionInterneDTOLoc.setLibelle(taActionInterneDTO.getLibelle());             
		taActionInterneDTOLoc.setSysteme(taActionInterneDTO.isSysteme());              
		return taActionInterneDTOLoc;
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

	public String getIdAction() {
		return idAction;
	}

	public void setIdAction(String idAction) {
		firePropertyChange("idAction", this.idAction, this.idAction = idAction);
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		firePropertyChange("libelle", this.libelle, this.libelle = libelle);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		firePropertyChange("description", this.description, this.description = description);
	}

	public boolean isSysteme() {
		return systeme;
	}

	public void setSysteme(boolean systeme) {
		firePropertyChange("systeme", this.systeme, this.systeme = systeme);
	}
	

}
