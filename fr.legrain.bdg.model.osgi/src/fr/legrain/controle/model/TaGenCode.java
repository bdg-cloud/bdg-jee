package fr.legrain.controle.model;

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
import javax.persistence.Transient;
import javax.persistence.Version;

import fr.legrain.validator.LgrHibernateValidated;


@Entity
@Table(name = "ta_gen_code"/*, uniqueConstraints = @UniqueConstraint(columnNames = "code_controle")*/)
//@SequenceGenerator(name = "gen_gen_code", sequenceName = "num_id_gen_code", allocationSize = 1)
public class TaGenCode implements java.io.Serializable {


	private static final long serialVersionUID = 3321021938226855061L;
	
	/*
	create table ta_gen_code (
			id_gen_code           did3 not null,
			entite dlib255nn,
			fixe dlib100,
			exo dlib100,
			compteur dlib100,
			qui_cree     dlib50,
			quand_cree   timestamp default 'now',
			qui_modif    dlib50,
			quand_modif  timestamp default 'now',
			"version"    num_version,
			ip_acces     dlib50nn default 0,
			version_obj  integer
	);
	*/
	
	private int idGenCode;
	private String entite;
	private String fixe;
	private String exo;
	private String compteur;
	
	private String date;
	private String jour;
	private String mois;
	private String annee;

	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private String ipAcces;
	private Integer versionObj;

	public TaGenCode() {
	}

	public TaGenCode(int idGenCode) {
		this.idGenCode = idGenCode;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_gen_code", unique = true, nullable = false)
	@LgrHibernateValidated(champBd = "id_gen_code",table = "ta_gen_code", champEntite="idGenCode", clazz = TaGenCode.class)
	public int getIdGenCode() {
		return this.idGenCode;
	}

	public void setIdGenCode(int idGenCode) {
		this.idGenCode = idGenCode;
	}
	
	@Column(name = "entite", length = 50)
	public String getEntite() {
		return entite;
	}

	public void setEntite(String entite) {
		this.entite = entite;
	}

	@Column(name = "fixe", length = 50)
	public String getFixe() {
		return fixe;
	}

	public void setFixe(String fixe) {
		this.fixe = fixe;
	}

	@Column(name = "exo", length = 50)
	public String getExo() {
		return exo;
	}

	public void setExo(String exo) {
		this.exo = exo;
	}

	@Column(name = "compteur", length = 50)
	public String getCompteur() {
		return compteur;
	}

	public void setCompteur(String compteur) {
		this.compteur = compteur;
	}
	
	@Transient
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@Transient
	public String getJour() {
		return jour;
	}

	public void setJour(String jour) {
		this.jour = jour;
	}

	@Transient
	public String getMois() {
		return mois;
	}

	public void setMois(String mois) {
		this.mois = mois;
	}

	@Transient
	public String getAnnee() {
		return annee;
	}

	public void setAnnee(String annee) {
		this.annee = annee;
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
