package fr.legrain.conformite.model;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;


import fr.legrain.validator.LgrHibernateValidated;

@Entity
@Table(name = "ta_status_conformite")
public class TaStatusConformite implements java.io.Serializable {
	
	private static final long serialVersionUID = -7221229878014595975L;
	
//	public static final String C_TYPE_ACTION_VIDE_FACULTATIF = "vide_facultatif"; //pas de valeur (controle facultatif) 50
	public static final String C_TYPE_ACTION_AUCUN_CONTROLE_DEFINIT = "aucun_controle"; //pas de valeur 100
	public static final String C_TYPE_ACTION_VIDE = "non_fait"; //pas de valeur 100
	public static final String C_TYPE_ACTION_OK = "ok"; //valide 200
	public static final String C_TYPE_ACTION_CORRIGE = "ok_apres_correction"; //correction effectuée avec succés 300
	public static final String C_TYPE_ACTION_NON_CORRIGE = "invalide_meme_apres_correction"; //correction effectuée sans succés => encore invalide 400
	public static final String C_TYPE_ACTION_A_CORRIGER = "invalide_a_corrige"; //correction à faire (invalide) 500
//	public static final String C_TYPE_ACTION_NON_CORRIGE_BLOQUANT = "noncorrige_bloquant"; //correction effectuée sans succés => encore invalide et bloquant 550
//	public static final String C_TYPE_ACTION_A_CORRIGER_BLOQUANT = "acorriger_bloquant"; //correction à faire (invalide et bloquant) 600

	private int idStatusConformite;
	private String codeStatusConformite;
	private String libelleStatusConformite;	
	private String libelleStatusConformiteLot;	
	private int importance;	
	
	private String version;
	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private String ipAcces;
	private Integer versionObj;

	public TaStatusConformite() {
	}

	public TaStatusConformite(int idGroupe) {
		this.idStatusConformite = idGroupe;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_status_conformite", unique = true, nullable = false)
	@LgrHibernateValidated(champBd = "id_status_conformite",table = "ta_status_conformite", champEntite="idStatusConformite", clazz = TaStatusConformite.class)
	public int getIdStatusConformite() {
		return this.idStatusConformite;
	}

	public void setIdStatusConformite(int idGroupe) {
		this.idStatusConformite = idGroupe;
	}

	
	@Column(name = "version", length = 20)
	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Column(name = "code_status_conformite", length = 20)
	@LgrHibernateValidated(champBd = "code_status_conformite",table = "ta_status_conformite", champEntite="codeStatusConformite", clazz = TaStatusConformite.class)
	public String getCodeStatusConformite() {
		return this.codeStatusConformite;
	}

	public void setCodeStatusConformite(String codeTCivilite) {
		this.codeStatusConformite = codeTCivilite;
	}

	@Column(name = "qui_cree", length = 50)
	public String getQuiCree() {
		return this.quiCree;
	}

	public void setQuiCree(String quiCreeTCivilite) {
		this.quiCree = quiCreeTCivilite;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "quand_cree", length = 19)
	public Date getQuandCree() {
		return this.quandCree;
	}

	public void setQuandCree(Date quandCreeTCivilite) {
		this.quandCree = quandCreeTCivilite;
	}

	@Column(name = "qui_modif", length = 50)
	public String getQuiModif() {
		return this.quiModif;
	}

	public void setQuiModif(String quiModifTCivilite) {
		this.quiModif = quiModifTCivilite;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "quand_modif", length = 19)
	public Date getQuandModif() {
		return this.quandModif;
	}

	public void setQuandModif(Date quandModifTCivilite) {
		this.quandModif = quandModifTCivilite;
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

	@Column(name = "libelle_status_conformite")
	@LgrHibernateValidated(champBd = "libelle_status_conformite",table = "ta_status_conformite", champEntite="libelleStatusConformite", clazz = TaStatusConformite.class)
	public String getLibelleStatusConformite() {
		return libelleStatusConformite;
	}

	public void setLibelleStatusConformite(String libelle) {
		this.libelleStatusConformite = libelle;
	}
	
	@Column(name = "libelle_status_conformite_lot")
	@LgrHibernateValidated(champBd = "libelle_status_conformite_lot",table = "ta_status_conformite", champEntite="libelleStatusConformiteLot", clazz = TaStatusConformite.class)
	public String getLibelleStatusConformiteLot() {
		return libelleStatusConformiteLot;
	}

	public void setLibelleStatusConformiteLot(String libelle) {
		this.libelleStatusConformiteLot = libelle;
	}

	public TaStatusConformite(int idStatusConformite, String codeStatusConformite, String libelleStatusConformite,
			String version, String quiCree, Date quandCree, String quiModif, Date quandModif, String ipAcces,
			Integer versionObj) {
		super();
		this.idStatusConformite = idStatusConformite;
		this.codeStatusConformite = codeStatusConformite;
		this.libelleStatusConformite = libelleStatusConformite;
		this.version = version;
		this.quiCree = quiCree;
		this.quandCree = quandCree;
		this.quiModif = quiModif;
		this.quandModif = quandModif;
		this.ipAcces = ipAcces;
		this.versionObj = versionObj;
	}

	@Column(name = "importance")
	public int getImportance() {
		return importance;
	}

	public void setImportance(int importance) {
		this.importance = importance;
	}

}
