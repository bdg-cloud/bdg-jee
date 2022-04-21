package fr.legrain.article.model;

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

import fr.legrain.general.model.TaObjetLgr;
import fr.legrain.validator.LgrHibernateValidated;

@Entity
@Table(name = "ta_t_transport")
@NamedQueries(value = { 
		@NamedQuery(name=TaTTransport.QN.FIND_BY_CODE_LIGHT, query="select new fr.legrain.article.dto.TaTTransportDTO(a.idTTransport,a.codeTTransport,a.liblTTransport) from TaTTransport a where a.codeTTransport like :codeTTransport order by a.codeTTransport"),
		@NamedQuery(name=TaTTransport.QN.FIND_ALL_LIGHT, query="select new fr.legrain.article.dto.TaTTransportDTO(a.idTTransport,a.codeTTransport,a.liblTTransport) from TaTTransport a order by a.codeTTransport")
		})
public class TaTTransport extends TaObjetLgr   implements java.io.Serializable {

	private static final long serialVersionUID = 1242847674867890588L;
	
	public static class QN {
		public static final String FIND_BY_CODE_LIGHT = "TaTTransport.findByCodeLight";
		public static final String FIND_ALL_LIGHT = "TaTTransport.findAllLight";
	}
	
	private int idTTransport;
//	private String version;
	private String codeTTransport;
	private String liblTTransport;
//	private String quiCree;
//	private Date quandCree;
//	private String quiModif;
//	private Date quandModif;
//	private String ipAcces;
	private Integer versionObj;

	public TaTTransport() {
	}

	public TaTTransport(int idTTransport) {
		this.idTTransport = idTTransport;
	}
	
	
	public TaTTransport(int idTTransport, String codeTTransport, String liblTTransport) {
		this.idTTransport = idTTransport;
		this.codeTTransport = codeTTransport;
		this.liblTTransport = liblTTransport;
	}

	public TaTTransport(int idTTransport, String codeTTransport, String liblTTransport,
			String quiCreeTTransport, Date quandCreeTTransport, String quiModifTTransport,
			Date quandModifTTransport, String ipAcces, Integer versionObj) {
		this.idTTransport = idTTransport;
		this.codeTTransport = codeTTransport;
		this.liblTTransport = liblTTransport;
		this.quiCree = quiCreeTTransport;
		this.quandCree = quandCreeTTransport;
		this.quiModif = quiModifTTransport;
		this.quandModif = quandModifTTransport;
		this.ipAcces = ipAcces;
		this.versionObj = versionObj;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_t_transport", unique = true, nullable = false)
	@LgrHibernateValidated(champBd = "id_t_transport",table = "ta_t_transport", champEntite="idTTransport", clazz = TaTTransport.class)
	public int getIdTTransport() {
		return this.idTTransport;
	}

	public void setIdTTransport(int idTTransport) {
		this.idTTransport = idTTransport;
	}

//	@Column(name = "version", length = 20)
//	public String getVersion() {
//		return this.version;
//	}
//
//	public void setVersion(String version) {
//		this.version = version;
//	}

	@Column(name = "code_t_transport", unique = true, length = 20)
	@LgrHibernateValidated(champBd = "code_t_transport",table = "ta_t_transport", champEntite="codeTTransport", clazz = TaTTransport.class)
	public String getCodeTTransport() {
		return this.codeTTransport;
	}

	public void setCodeTTransport(String codeTTransport) {
		this.codeTTransport = codeTTransport;
	}

	@Column(name = "libl_t_transport", length = 100)
	@LgrHibernateValidated(champBd = "libl_t_transport",table = "ta_t_transport", champEntite="liblTTransport", clazz = TaTTransport.class)
	public String getLiblTTransport() {
		return this.liblTTransport;
	}

	public void setLiblTTransport(String liblTTransport) {
		this.liblTTransport = liblTTransport;
	}

//	@Column(name = "qui_cree", length = 50)
//	public String getQuiCree() {
//		return this.quiCree;
//	}
//
//	public void setQuiCree(String quiCreeTTransport) {
//		this.quiCree = quiCreeTTransport;
//	}
//
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name = "quand_cree", length = 19)
//	public Date getQuandCree() {
//		return this.quandCree;
//	}
//
//	public void setQuandCree(Date quandCreeTTransport) {
//		this.quandCree = quandCreeTTransport;
//	}
//
//	@Column(name = "qui_modif", length = 50)
//	public String getQuiModif() {
//		return this.quiModif;
//	}
//
//	public void setQuiModif(String quiModifTTransport) {
//		this.quiModif = quiModifTTransport;
//	}
//
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name = "quand_modif", length = 19)
//	public Date getQuandModif() {
//		return this.quandModif;
//	}
//
//	public void setQuandModif(Date quandModifTTransport) {
//		this.quandModif = quandModifTTransport;
//	}
//
//	@Column(name = "ip_acces", length = 50)
//	public String getIpAcces() {
//		return this.ipAcces;
//	}
//
//	public void setIpAcces(String ipAcces) {
//		this.ipAcces = ipAcces;
//	}

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
		this.setCodeTTransport(codeTTransport.toUpperCase());
	}



	public boolean equalsCode(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TaTTransport other = (TaTTransport) obj;
		if (codeTTransport == null) {
			if (other.codeTTransport != null)
				return false;
		} else if (!codeTTransport.equals(other.codeTTransport))
			return false;
		if (versionObj == null) {
			if (other.versionObj != null)
				return false;
		} else if (!versionObj.equals(other.versionObj))
			return false;
		return true;
	}
	
	
}
