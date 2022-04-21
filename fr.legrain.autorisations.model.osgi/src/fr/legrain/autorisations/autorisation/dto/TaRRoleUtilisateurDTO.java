package fr.legrain.autorisations.autorisation.dto;

import fr.legrain.autorisations.model.ModelObject;

public class TaRRoleUtilisateurDTO extends ModelObject implements java.io.Serializable {

	private static final long serialVersionUID = 2620020440773481462L;
	
	private Integer id;
	private TaUtilisateurDTO taUtilisateur; 
	private TaRoleDTO taRole;
	private Integer versionObj;
	
	public TaRRoleUtilisateurDTO() {
	}

	public TaRRoleUtilisateurDTO(TaUtilisateurDTO username, TaRoleDTO userRoles) {
		super();
		this.taUtilisateur = username;
		this.taRole = userRoles;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public TaUtilisateurDTO getTaUtilisateur() {
		return taUtilisateur;
	}

	public void setTaUtilisateur(TaUtilisateurDTO username) {
		this.taUtilisateur = username;
	}

	public TaRoleDTO getTaRole() {
		return taRole;
	}

	public void setTaRole(TaRoleDTO userRoles) {
		this.taRole = userRoles;
	}

	public Integer getVersionObj() {
		return versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}
}
