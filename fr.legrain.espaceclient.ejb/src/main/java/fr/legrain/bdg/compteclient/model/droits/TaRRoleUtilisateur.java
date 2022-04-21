package fr.legrain.bdg.compteclient.model.droits;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

@Entity
@Table(name = "ta_r_role_utilisateur")
public class TaRRoleUtilisateur implements Serializable {
	
	private static final long serialVersionUID = 2058493601328154001L;

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name = "id_utilisateur")
	private TaUtilisateur taUtilisateur; 
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name = "id_role")
	private TaRole taRole;
	
	@Column(name="quand_cree")
	@Temporal(TemporalType.TIMESTAMP)
	private Date quandCree;
	
	@Column(name="quand_modif")
	@Temporal(TemporalType.TIMESTAMP)
	private Date quandModif;
	
	@Column(name="qui_cree")
	private Integer quiCree;
	
	@Column(name="qui_modif")
	private Integer quiModif;
	
	@Column(name = "ip_acces")
	private String ipAcces;
	
	@Version
	@Column(name = "version_obj")
	private Integer versionObj;
	
	public TaRRoleUtilisateur() {
	}

	public TaRRoleUtilisateur(TaUtilisateur username, TaRole userRoles) {
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

	public TaUtilisateur getTaUtilisateur() {
		return taUtilisateur;
	}

	public void setTaUtilisateur(TaUtilisateur username) {
		this.taUtilisateur = username;
	}

	public TaRole getTaRole() {
		return taRole;
	}

	public void setTaRole(TaRole userRoles) {
		this.taRole = userRoles;
	}

	public Date getQuandCree() {
		return quandCree;
	}

	public Date getQuandModif() {
		return quandModif;
	}

	public Integer getQuiCree() {
		return quiCree;
	}

	public Integer getQuiModif() {
		return quiModif;
	}

	public String getIpAcces() {
		return ipAcces;
	}

	public Integer getVersionObj() {
		return versionObj;
	}

	public void setQuandCree(Date quanCree) {
		this.quandCree = quanCree;
	}

	public void setQuandModif(Date quandModif) {
		this.quandModif = quandModif;
	}

	public void setQuiCree(Integer quiCree) {
		this.quiCree = quiCree;
	}

	public void setQuiModif(Integer quiModif) {
		this.quiModif = quiModif;
	}

	public void setIpAcces(String ip) {
		this.ipAcces = ip;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}
}
