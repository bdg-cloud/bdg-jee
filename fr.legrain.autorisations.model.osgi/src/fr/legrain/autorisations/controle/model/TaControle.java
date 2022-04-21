package fr.legrain.autorisations.controle.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import fr.legrain.validator.LgrHibernateValidated;


@Entity
@Table(name = "ta_controle"/*, uniqueConstraints = @UniqueConstraint(columnNames = "code_controle")*/)
//@SequenceGenerator(name = "gen_controle", sequenceName = "num_id_controle", allocationSize = 1)
public class TaControle implements java.io.Serializable {

	private static final long serialVersionUID = -6435271282111312447L;

	/*
	create table ta_controle (
			id_controle           did3 not null,
			champ dlib255nn,
			contexte dlib100,
			client dlib100,
			serveur dlib100,
			controle_defaut dlib255nn,
			controle_utilisateur dlib255,
			qui_cree     dlib50,
			quand_cree   timestamp default 'now',
			qui_modif    dlib50,
			quand_modif  timestamp default 'now',
			"version"    num_version,
			ip_acces     dlib50nn default 0,
			version_obj  integer
	);
	*/
	
	private int idControle;
	private String champ;
	private String contexte;
	private String client;
	private String serveur;
	private String controleDefaut;
	private String controleUtilisateur;
	
	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private String ipAcces;
	private Integer versionObj;

	public TaControle() {
	}

	public TaControle(int idControle) {
		this.idControle = idControle;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_controle", unique = true, nullable = false)
	@LgrHibernateValidated(champBd = "id_controle",table = "ta_controle", champEntite="idControle", clazz = TaControle.class)
	public int getIdControle() {
		return this.idControle;
	}

	public void setIdControle(int idTPaiement) {
		this.idControle = idTPaiement;
	}

	@Column(name = "champ")
	public String getChamp() {
		return champ;
	}

	public void setChamp(String champ) {
		this.champ = champ;
	}

	@Column(name = "contexte")
	public String getContexte() {
		return contexte;
	}

	public void setContexte(String contexte) {
		this.contexte = contexte;
	}

	@Column(name = "client")
	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	@Column(name = "serveur")
	public String getServeur() {
		return serveur;
	}

	public void setServeur(String serveur) {
		this.serveur = serveur;
	}

	@Column(name = "controle_defaut")
	public String getControleDefaut() {
		return controleDefaut;
	}

	public void setControleDefaut(String controleDefaut) {
		this.controleDefaut = controleDefaut;
	}

	@Column(name = "controle_utilisateur")
	public String getControleUtilisateur() {
		return controleUtilisateur;
	}

	public void setControleUtilisateur(String controleUtilisateur) {
		this.controleUtilisateur = controleUtilisateur;
	}
	
	
	@Column(name = "qui_cree", length = 50)
	public String getQuiCree() {
		return this.quiCree;
	}

	public void setQuiCree(String quiCreeTPaiement) {
		this.quiCree = quiCreeTPaiement;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "quand_cree", length = 19)
	public Date getQuandCree() {
		return this.quandCree;
	}

	public void setQuandCree(Date quandCreeTPaiement) {
		this.quandCree = quandCreeTPaiement;
	}

	@Column(name = "qui_modif", length = 50)
	public String getQuiModif() {
		return this.quiModif;
	}

	public void setQuiModif(String quiModifTPaiement) {
		this.quiModif = quiModifTPaiement;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "quand_modif", length = 19)
	public Date getQuandModif() {
		return this.quandModif;
	}

	public void setQuandModif(Date quandModifTPaiement) {
		this.quandModif = quandModifTPaiement;
	}

	@Column(name = "ip_acces", length = 50)
	public String getIpAcces() {
		return this.ipAcces;
	}

	public void setIpAcces(String ipAcces) {
		this.ipAcces = ipAcces;
	}

	@Version
	@Column(name = "version_obj")
	public Integer getVersionObj() {
		return this.versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}


//	@PrePersist
//	@PreUpdate
	public void beforePost ()throws Exception{
//		this.setCodeTPaiement(codeTPaiement.toUpperCase());
	}

}
