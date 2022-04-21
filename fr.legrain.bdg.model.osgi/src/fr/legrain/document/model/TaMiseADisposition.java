package fr.legrain.document.model;


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
@Table(name = "ta_mise_a_disposition")
public class TaMiseADisposition implements java.io.Serializable {

	private static final long serialVersionUID = 8812673570605663461L;
	
	private int idMiseADisposition;
	private Date accessibleSurCompteClient;
	private Date envoyeParEmail;
	private Date imprimePourClient;
	private String version;
	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private String ipAcces;
	private Integer versionObj;

	public TaMiseADisposition() {
	}

	public TaMiseADisposition(int idMiseADisposition) {
		this.idMiseADisposition = idMiseADisposition;
	}
	
	public boolean estMisADisposition() {
		if(accessibleSurCompteClient!=null
				|| envoyeParEmail!=null
				|| imprimePourClient!=null) {
			return true;
		} else {
			return false;
		}
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_mise_a_disposition", unique = true, nullable = false)
	@LgrHibernateValidated(champBd = "id_mise_a_disposition",table = "ta_mise_a_disposition", champEntite="idMiseADisposition", clazz = TaMiseADisposition.class)
	public int getIdMiseADisposition() {
		return this.idMiseADisposition;
	}

	public void setIdMiseADisposition(int idTDoc) {
		this.idMiseADisposition = idTDoc;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "accessible_sur_compte_client")
	public Date getAccessibleSurCompteClient() {
		return accessibleSurCompteClient;
	}

	public void setAccessibleSurCompteClient(Date accessibleSurCompteClient) {
		this.accessibleSurCompteClient = accessibleSurCompteClient;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "envoye_par_email")
	public Date getEnvoyeParEmail() {
		return envoyeParEmail;
	}

	public void setEnvoyeParEmail(Date envoyeParEmail) {
		this.envoyeParEmail = envoyeParEmail;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "imprime_pour_client")
	public Date getImprimePourClient() {
		return imprimePourClient;
	}

	public void setImprimePourClient(Date imprimePourClient) {
		this.imprimePourClient = imprimePourClient;
	}

	@Column(name = "version", length = 20)
	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	
	@Column(name = "qui_cree", length = 50)
	public String getQuiCree() {
		return this.quiCree;
	}

	public void setQuiCree(String quiCreeTDoc) {
		this.quiCree = quiCreeTDoc;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "quand_cree", length = 19)
	public Date getQuandCree() {
		return this.quandCree;
	}

	public void setQuandCree(Date quandCreeTDoc) {
		this.quandCree = quandCreeTDoc;
	}

	@Column(name = "qui_modif", length = 50)
	public String getQuiModif() {
		return this.quiModif;
	}

	public void setQuiModif(String quiModifTDoc) {
		this.quiModif = quiModifTDoc;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "quand_modif", length = 19)
	public Date getQuandModif() {
		return this.quandModif;
	}

	public void setQuandModif(Date quandModifTDoc) {
		this.quandModif = quandModifTDoc;
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

}
