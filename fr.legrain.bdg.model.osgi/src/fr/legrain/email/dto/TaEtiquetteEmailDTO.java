package fr.legrain.email.dto;

import fr.legrain.bdg.model.ModelObject;

public class TaEtiquetteEmailDTO extends ModelObject implements java.io.Serializable {

	private static final long serialVersionUID = 1730797494999173806L;
	
	private Integer id;
	
	private String code;
	private String libelle;
	private String description;
	private String couleur;
	private int ordre = 0;
	private boolean systeme;
	private boolean visible;
	
	private Integer versionObj;

	public TaEtiquetteEmailDTO() {
	}

	public void setTaAgendaDTO(TaEtiquetteEmailDTO taAgendaDTO) {
		this.id = taAgendaDTO.id;
		this.code = taAgendaDTO.code;
		this.libelle = taAgendaDTO.libelle;
		this.description = taAgendaDTO.description;
		this.systeme = taAgendaDTO.systeme;
		this.visible = taAgendaDTO.visible;
	}

	public static TaEtiquetteEmailDTO copy(TaEtiquetteEmailDTO taAgendaDTO){
		TaEtiquetteEmailDTO taAgendaDTOLoc = new TaEtiquetteEmailDTO();
		taAgendaDTOLoc.setId(taAgendaDTO.getId());              
		taAgendaDTOLoc.setCode(taAgendaDTO.getCode());                
		taAgendaDTOLoc.setLibelle(taAgendaDTO.getLibelle());           
		taAgendaDTOLoc.setDescription(taAgendaDTO.getDescription());             
		taAgendaDTOLoc.setSysteme(taAgendaDTO.isSysteme());              
		taAgendaDTOLoc.setVisible(taAgendaDTO.isVisible());              
		return taAgendaDTOLoc;
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		firePropertyChange("code", this.code, this.code = code);
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

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		firePropertyChange("visible", this.visible, this.visible = visible);
	}

	public String getCouleur() {
		return couleur;
	}

	public void setCouleur(String couleur) {
		this.couleur = couleur;
	}

	public int getOrdre() {
		return ordre;
	}

	public void setOrdre(int ordre) {
		this.ordre = ordre;
	}

}
