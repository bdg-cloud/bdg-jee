package fr.legrain.article.model;

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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import fr.legrain.general.model.TaObjetLgr;
import fr.legrain.validator.LgrHibernateValidated;

@Entity
@Table(name = "ta_transporteur")
@NamedQueries(value = { 
		@NamedQuery(name=TaTransporteur.QN.FIND_BY_CODE_LIGHT, query="select new fr.legrain.article.dto.TaTransporteurDTO(a.idTransporteur,a.codeTransporteur,a.liblTransporteur,t.idTTransport,t.codeTTransport,t.liblTTransport) from TaTransporteur a left join a.taTTransport t where a.codeTransporteur like :codeTransporteur order by a.codeTransporteur"),
		@NamedQuery(name=TaTransporteur.QN.FIND_ALL_LIGHT, query="select new fr.legrain.article.dto.TaTransporteurDTO(a.idTransporteur,a.codeTransporteur,a.liblTransporteur,t.idTTransport,t.codeTTransport,t.liblTTransport) from TaTransporteur a left join a.taTTransport t order by a.codeTransporteur")
		})
public class TaTransporteur extends TaObjetLgr   implements java.io.Serializable {

	public static class QN {
		public static final String FIND_BY_CODE_LIGHT = "TaTransporteur.findByCodeLight";
		public static final String FIND_ALL_LIGHT = "TaTransporteur.findAllLight";
	}
	
	private static final long serialVersionUID = -3906858537862056504L;
	
	private int idTransporteur;
//	private String version;
	private String codeTransporteur;
	private String liblTransporteur;
	private TaTTransport taTTransport;
//	private String quiCree;
//	private Date quandCree;
//	private String quiModif;
//	private Date quandModif;
//	private String ipAcces;
	private Integer versionObj;

	public TaTransporteur() {
	}

	public TaTransporteur(int idTReception) {
		this.idTransporteur = idTReception;
	}
	
	
	public TaTransporteur(int idTReception, String codeTReception, String liblTReception) {
		this.idTransporteur = idTReception;
		this.codeTransporteur = codeTReception;
		this.liblTransporteur = liblTReception;
	}

	public TaTransporteur(int idTReception, String codeTReception, String liblTReception,
			String quiCreeTReception, Date quandCreeTReception, String quiModifTReception,
			Date quandModifTReception, String ipAcces, Integer versionObj/*,
			Set<TaReceptionephone> taReceptionephones*/) {
		this.idTransporteur = idTReception;
		this.codeTransporteur = codeTReception;
		this.liblTransporteur = liblTReception;
		this.quiCree = quiCreeTReception;
		this.quandCree = quandCreeTReception;
		this.quiModif = quiModifTReception;
		this.quandModif = quandModifTReception;
		this.ipAcces = ipAcces;
		this.versionObj = versionObj;
		//this.taReceptionephones = taReceptionephones;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_transporteur", unique = true, nullable = false)
	@LgrHibernateValidated(champBd = "id_transporteur",table = "ta_transporteur", champEntite="idTransporteur", clazz = TaTransporteur.class)
	public int getIdTransporteur() {
		return this.idTransporteur;
	}

	public void setIdTransporteur(int idTReception) {
		this.idTransporteur = idTReception;
	}

//	@Column(name = "version", length = 20)
//	public String getVersion() {
//		return this.version;
//	}
//
//	public void setVersion(String version) {
//		this.version = version;
//	}

	@Column(name = "code_transporteur", unique = true, length = 20)
	@LgrHibernateValidated(champBd = "code_transporteur",table = "ta_transporteur", champEntite="codeTransporteur", clazz = TaTransporteur.class)
	public String getCodeTransporteur() {
		return this.codeTransporteur;
	}

	public void setCodeTransporteur(String codeTReception) {
		this.codeTransporteur = codeTReception;
	}

	@Column(name = "libl_transporteur", length = 100)
	@LgrHibernateValidated(champBd = "libl_transporteur",table = "ta_transporteur", champEntite="liblTransporteur", clazz = TaTransporteur.class)
	public String getLiblTransporteur() {
		return this.liblTransporteur;
	}

	public void setLiblTransporteur(String liblTReception) {
		this.liblTransporteur = liblTReception;
	}

//	@Column(name = "qui_cree", length = 50)
//	public String getQuiCree() {
//		return this.quiCree;
//	}
//
//	public void setQuiCree(String quiCreeTReception) {
//		this.quiCree = quiCreeTReception;
//	}
//
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name = "quand_cree", length = 19)
//	public Date getQuandCree() {
//		return this.quandCree;
//	}
//
//	public void setQuandCree(Date quandCreeTReception) {
//		this.quandCree = quandCreeTReception;
//	}
//
//	@Column(name = "qui_modif", length = 50)
//	public String getQuiModif() {
//		return this.quiModif;
//	}
//
//	public void setQuiModif(String quiModifTReception) {
//		this.quiModif = quiModifTReception;
//	}
//
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name = "quand_modif", length = 19)
//	public Date getQuandModif() {
//		return this.quandModif;
//	}
//
//	public void setQuandModif(Date quandModifTReception) {
//		this.quandModif = quandModifTReception;
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

//	@OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY, mappedBy = "taTReception")
//	public Set<TaReceptionephone> getTaReceptionephones() {
//		return this.taReceptionephones;
//	}
//
//	public void setTaReceptionephones(Set<TaReceptionephone> taReceptionephones) {
//		this.taReceptionephones = taReceptionephones;
//	}


//	@PrePersist
//	@PreUpdate
	public void beforePost ()throws Exception{
		this.setCodeTransporteur(codeTransporteur.toUpperCase());
	}



	public boolean equalsCode(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TaTransporteur other = (TaTransporteur) obj;
		if (codeTransporteur == null) {
			if (other.codeTransporteur != null)
				return false;
		} else if (!codeTransporteur.equals(other.codeTransporteur))
			return false;
		if (versionObj == null) {
			if (other.versionObj != null)
				return false;
		} else if (!versionObj.equals(other.versionObj))
			return false;
		return true;
	}

	@ManyToOne(fetch = FetchType.EAGER, cascade = {/*CascadeType.PERSIST, CascadeType.MERGE,*/ CascadeType.REFRESH})
	@JoinColumn(name = "id_t_transport")
	public TaTTransport getTaTTransport() {
		return taTTransport;
	}

	public void setTaTTransport(TaTTransport taTTransport) {
		this.taTTransport = taTTransport;
	}
	
	
}
