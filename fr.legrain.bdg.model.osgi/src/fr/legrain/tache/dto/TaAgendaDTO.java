package fr.legrain.tache.dto;

import fr.legrain.bdg.model.ModelObject;

public class TaAgendaDTO extends ModelObject implements java.io.Serializable {

	private static final long serialVersionUID = 1730797494999173806L;
	
	private Integer id;
	
	private String nom;
	//private TaUtilisateur proprietaire;
	private String description;
	private String couleur;
	private Boolean prive = false;
	private Integer idUtilisateur;
	
	private Integer versionObj;

	public TaAgendaDTO() {
	}

	public void setTaAgendaDTO(TaAgendaDTO taAgendaDTO) {
		this.id = taAgendaDTO.id;
		this.nom = taAgendaDTO.nom;
		this.description = taAgendaDTO.description;
		this.couleur = taAgendaDTO.couleur;
		this.prive = taAgendaDTO.prive;
		this.idUtilisateur = taAgendaDTO.idUtilisateur;
	}

	public static TaAgendaDTO copy(TaAgendaDTO taAgendaDTO){
		TaAgendaDTO taAgendaDTOLoc = new TaAgendaDTO();
		taAgendaDTOLoc.setId(taAgendaDTO.getId());              
		taAgendaDTOLoc.setCouleur(taAgendaDTO.getCouleur());                
		taAgendaDTOLoc.setDescription(taAgendaDTO.getDescription());           
		taAgendaDTOLoc.setNom(taAgendaDTO.getNom());             
		taAgendaDTOLoc.setPrive(taAgendaDTO.getPrive());              
		taAgendaDTOLoc.setIdUtilisateur(taAgendaDTO.getIdUtilisateur());              
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

	public String getNom() {
		return this.nom;
	}

	public void setNom(String nom) {
		firePropertyChange("nom", this.nom, this.nom = nom);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		firePropertyChange("description", this.description, this.description = description);
	}

	public String getCouleur() {
		return couleur;
	}

	public void setCouleur(String couleur) {
		firePropertyChange("couleur", this.couleur, this.couleur = couleur);
	}

	public Boolean getPrive() {
		return prive;
	}

	public void setPrive(Boolean prive) {
		firePropertyChange("prive", this.prive, this.prive = prive);
	}

	public Integer getIdUtilisateur() {
		return idUtilisateur;
	}

	public void setIdUtilisateur(Integer idUtilisateur) {
		firePropertyChange("idUtilisateur", this.idUtilisateur, this.idUtilisateur = idUtilisateur);
	}

}
