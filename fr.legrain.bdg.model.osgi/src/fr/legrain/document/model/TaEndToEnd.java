package fr.legrain.document.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import fr.legrain.validator.LgrHibernateValidated;

/**
 * TaEndToEnd
 */
@Entity
//@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "ta_end_to_end", uniqueConstraints = @UniqueConstraint(columnNames = "code_end_to_end"))
//@SequenceGenerator(name = "gen_end_to_end", sequenceName = "num_id_end_to_end", allocationSize = 1)
public class TaEndToEnd implements java.io.Serializable {

	private static final long serialVersionUID = -8138344743675511508L;
	
	private int idEndToEnd;
	private String version;
	private String codeEndToEnd;
	private TaMandat taMandat;
	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private String ipAcces;
	private Integer versionObj;

	public TaEndToEnd() {
	}

	public TaEndToEnd(int idEndToEnd) {
		this.idEndToEnd = idEndToEnd;
	}

	public TaEndToEnd(int idEndToEnd, String codeEndToEnd, TaMandat taMandat,
			String quiCree, Date quandCree, String quiModif,
			Date quandModif, String ipAcces, Integer versionObj,
			Set<TaComDoc> taComDocs) {
		this.idEndToEnd = idEndToEnd;
		this.codeEndToEnd = codeEndToEnd;
		this.taMandat = taMandat;
		this.quiCree = quiCree;
		this.quandCree = quandCree;
		this.quiModif = quiModif;
		this.quandModif = quandModif;
		this.ipAcces = ipAcces;
		this.versionObj = versionObj;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_end_to_end", unique = true, nullable = false)
	@LgrHibernateValidated(champBd = "id_end_to_end",table = "ta_end_to_end",champEntite="idEndToEnd", clazz = TaEndToEnd.class)
	public int getIdEndToEnd() {
		return this.idEndToEnd;
	}

	public void setIdEndToEnd(int idTDoc) {
		this.idEndToEnd = idTDoc;
	}

	@Column(name = "version", length = 20)
	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Column(name = "code_end_to_end", unique = true, length = 50)
	@LgrHibernateValidated(champBd = "code_end_to_end",table = "ta_end_to_end",champEntite="codeEndToEnd", clazz = TaEndToEnd.class)
	public String getCodeEndToEnd() {
		return this.codeEndToEnd;
	}

	public void setCodeEndToEnd(String codeEndToEnd) {
		this.codeEndToEnd = codeEndToEnd;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_mandat")
	@LgrHibernateValidated(champBd = "id_mandat",table = "ta_end_to_end",champEntite="TaMandat.idMandat", clazz = TaMandat.class)
	public TaMandat getTaMandat() {
		return this.taMandat;
	}

	public void setTaMandat(TaMandat taMandat) {
		this.taMandat = taMandat;
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
