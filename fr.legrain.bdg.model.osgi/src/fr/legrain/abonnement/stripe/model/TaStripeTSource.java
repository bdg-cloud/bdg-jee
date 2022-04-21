package fr.legrain.abonnement.stripe.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import fr.legrain.validator.LgrHibernateValidated;

@Entity
@Table(name = "ta_stripe_t_source")
@NamedQueries(value = { 
		@NamedQuery(name=TaStripeTSource.QN.FIND_BY_CODE_LIGHT, query="select new fr.legrain.abonnement.stripe.dto.TaStripeTSourceDTO(a.idStripeTSource,a.codeStripeTSource,a.liblStripeTSource) from TaStripeTSource a where a.codeStripeTSource like :codeStripeTSource order by a.codeStripeTSource"),
		@NamedQuery(name=TaStripeTSource.QN.FIND_ALL_LIGHT,     query="select new fr.legrain.abonnement.stripe.dto.TaStripeTSourceDTO(a.idStripeTSource,a.codeStripeTSource,a.liblStripeTSource) from TaStripeTSource a order by a.codeStripeTSource")
		})
public class TaStripeTSource implements java.io.Serializable {

	private static final long serialVersionUID = 1242847674867890588L;
	
	public static class QN {
		public static final String FIND_BY_CODE_LIGHT = "TaStripeTSource.findByCodeLight";
		public static final String FIND_ALL_LIGHT = "TaStripeTSource.findAllLight";
	}
	
	public static final String TYPE_SOURCE_PREL_SEPA = "PREL_SEPA";
	public static final String TYPE_SOURCE_CB = "CB";
	public static final String TYPE_SOURCE_CHEQUE = "CHEQUE";
	public static final String TYPE_SOURCE_VIREMENT = "VIREMENT";
	
	private int idStripeTSource;
	private String version;
	private String codeStripeTSource;
	private String liblStripeTSource;
	private Boolean automatique;
	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private String ipAcces;
	private Integer versionObj;

	public TaStripeTSource() {
	}

	public TaStripeTSource(int idStripeTSource) {
		this.idStripeTSource = idStripeTSource;
	}
	
	
	public TaStripeTSource(int idStripeTSource, String codeStripeTSource, String liblStripeTSource) {
		this.idStripeTSource = idStripeTSource;
		this.codeStripeTSource = codeStripeTSource;
		this.liblStripeTSource = liblStripeTSource;
	}

	public TaStripeTSource(int idStripeTSource, String codeStripeTSource, String liblStripeTSource,
			String quiCree, Date quandCree, String quiModif,
			Date quandModif, String ipAcces, Integer versionObj) {
		this.idStripeTSource = idStripeTSource;
		this.codeStripeTSource = codeStripeTSource;
		this.liblStripeTSource = liblStripeTSource;
		this.quiCree = quiCree;
		this.quandCree = quandCree;
		this.quiModif = quiModif;
		this.quandModif = quandModif;
		this.ipAcces = ipAcces;
		this.versionObj = versionObj;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_stripe_t_source", unique = true, nullable = false)
	@LgrHibernateValidated(champBd = "id_stripe_t_source",table = "ta_stripe_t_source", champEntite="idStripeTSource", clazz = TaStripeTSource.class)
	public int getIdStripeTSource() {
		return this.idStripeTSource;
	}

	public void setIdStripeTSource(int idStripeTSource) {
		this.idStripeTSource = idStripeTSource;
	}

	@Column(name = "version", length = 20)
	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Column(name = "code_stripe_t_source", unique = true, length = 20)
	@LgrHibernateValidated(champBd = "code_stripe_t_source",table = "ta_stripe_t_source", champEntite="codeStripeTSource", clazz = TaStripeTSource.class)
	public String getCodeStripeTSource() {
		return this.codeStripeTSource;
	}

	public void setCodeStripeTSource(String codeStripeTSource) {
		this.codeStripeTSource = codeStripeTSource;
	}

	@Column(name = "libl_stripe_t_source", length = 100)
	@LgrHibernateValidated(champBd = "libl_stripe_t_source",table = "ta_stripe_t_source", champEntite="liblStripeTSource", clazz = TaStripeTSource.class)
	public String getLiblStripeTSource() {
		return this.liblStripeTSource;
	}

	public void setLiblStripeTSource(String liblStripeTSource) {
		this.liblStripeTSource = liblStripeTSource;
	}

	@Column(name = "qui_cree", length = 50)
	public String getQuiCree() {
		return this.quiCree;
	}

	public void setQuiCree(String quiCreeTTransport) {
		this.quiCree = quiCreeTTransport;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "quand_cree", length = 19)
	public Date getQuandCree() {
		return this.quandCree;
	}

	public void setQuandCree(Date quandCreeTTransport) {
		this.quandCree = quandCreeTTransport;
	}

	@Column(name = "qui_modif", length = 50)
	public String getQuiModif() {
		return this.quiModif;
	}

	public void setQuiModif(String quiModifTTransport) {
		this.quiModif = quiModifTTransport;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "quand_modif", length = 19)
	public Date getQuandModif() {
		return this.quandModif;
	}

	public void setQuandModif(Date quandModifTTransport) {
		this.quandModif = quandModifTTransport;
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
		this.setCodeStripeTSource(codeStripeTSource.toUpperCase());
	}



	public boolean equalsCode(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TaStripeTSource other = (TaStripeTSource) obj;
		if (codeStripeTSource == null) {
			if (other.codeStripeTSource != null)
				return false;
		} else if (!codeStripeTSource.equals(other.codeStripeTSource))
			return false;
		if (versionObj == null) {
			if (other.versionObj != null)
				return false;
		} else if (!versionObj.equals(other.versionObj))
			return false;
		return true;
	}

	@Column(name = "automatique")
	public Boolean getAutomatique() {
		return automatique;
	}

	public void setAutomatique(Boolean automatique) {
		this.automatique = automatique;
	}
	
	
}
