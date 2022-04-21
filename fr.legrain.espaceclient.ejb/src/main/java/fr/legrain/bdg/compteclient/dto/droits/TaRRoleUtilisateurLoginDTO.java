package fr.legrain.bdg.compteclient.dto.droits;

import fr.legrain.bdg.compteclient.dto.ModelObject;

public class TaRRoleUtilisateurLoginDTO extends ModelObject implements java.io.Serializable {
	
	private static final long serialVersionUID = 7930023662584436646L;

	private Integer id;
	private TaUtilisateurLoginDTO taUtilisateur; 
	private TaRoleLoginDTO taRole;
	private Integer versionObj;
	
	public TaRRoleUtilisateurLoginDTO() {
	}

	public TaRRoleUtilisateurLoginDTO(TaUtilisateurLoginDTO username, TaRoleLoginDTO userRoles) {
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

	public TaUtilisateurLoginDTO getTaUtilisateur() {
		return taUtilisateur;
	}

	public void setTaUtilisateur(TaUtilisateurLoginDTO username) {
		this.taUtilisateur = username;
	}

	public TaRoleLoginDTO getTaRole() {
		return taRole;
	}

	public void setTaRole(TaRoleLoginDTO userRoles) {
		this.taRole = userRoles;
	}

	public Integer getVersionObj() {
		return versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}
}
