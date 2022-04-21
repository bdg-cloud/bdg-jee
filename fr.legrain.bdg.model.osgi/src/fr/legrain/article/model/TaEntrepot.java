package fr.legrain.article.model;

import java.io.Serializable;
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
import javax.persistence.Transient;
import javax.persistence.Version;

import org.apache.log4j.Logger;

import fr.legrain.general.model.TaObjetLgr;
import fr.legrain.validator.LgrHibernateValidated;

@Entity
@Table(name = "ta_entrepot")
@NamedQueries(value = { 
		@NamedQuery(name=TaEntrepot.QN.FIND_BY_CODE_LIGHT, query="select new fr.legrain.article.dto.TaEntrepotDTO(a.idEntrepot,a.codeEntrepot,a.libelle) from TaEntrepot a  where a.codeEntrepot like :code order by a.codeEntrepot"),
		@NamedQuery(name=TaEntrepot.QN.FIND_ALL_LIGHT, query="select new fr.legrain.article.dto.TaEntrepotDTO(a.idEntrepot,a.codeEntrepot,a.libelle) from TaEntrepot a order by a.codeEntrepot")
})
public class TaEntrepot extends TaObjetLgr   implements Serializable{

	public static class QN {
		public static final String FIND_BY_CODE_LIGHT = "TaEntrepot.findByCodeLight";
		public static final String FIND_ALL_LIGHT = "TaEntrepot.findAllLight";
	}

	private static final long serialVersionUID = -1932389508049397552L;


	private int idEntrepot;
	private String codeEntrepot;
	private String libelle;
	private Boolean actif;
	private Integer versionObj;
//	private String quiCree;
//	private Date quandCree;
//	private String quiModif;
//	private Date quandModif;
//	private String ipAcces;
//	private String version;

	@Transient
	static Logger logger = Logger.getLogger(TaEntrepot.class.getName());

	public TaEntrepot() {
		super();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_entrepot", unique = true, nullable = false)
	@LgrHibernateValidated(champBd = "id_entrepot",table = "ta_entrepot", champEntite="idEntrepot", clazz = TaEntrepot.class)
	public int getIdEntrepot() {
		return this.idEntrepot;
	}
	public void setIdEntrepot(int id) {
		this.idEntrepot = id;
	}

	@Column(name = "libelle")
	@LgrHibernateValidated(champBd = "libelle",table = "ta_entrepot", champEntite="libelle", clazz = TaEntrepot.class)
	public String getLibelle() {
		return this.libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}


	@Column(name = "code_entrepot")
	@LgrHibernateValidated(champBd = "code_entrepot",table = "ta_entrepot", champEntite="codeEntrepot", clazz = TaEntrepot.class)
	public String getCodeEntrepot() {
		return codeEntrepot;
	}

	public void setCodeEntrepot(String code) {
		this.codeEntrepot = code;
	}


	@Column(name = "actif")
	@LgrHibernateValidated(champBd = "actif",table = "ta_entrepot", champEntite="actif", clazz = TaEntrepot.class)
	public Boolean getActif() {
		return this.actif;
	}

	public void setActif(Boolean actif) {
		this.actif = actif;
	}
	
	@Version
	@Column(name = "version_obj")
	public Integer getVersionObj() {
		return this.versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

//	@Column(name = "qui_cree", length = 50)
//	public String getQuiCree() {
//		return this.quiCree;
//	}
//
//	public void setQuiCree(String quiCree) {
//		this.quiCree = quiCree;
//	}
//
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name = "quand_cree", length = 19)
//	public Date getQuandCree() {
//		return this.quandCree;
//	}
//
//	public void setQuandCree(Date quandCree) {
//		this.quandCree = quandCree;
//	}
//
//	@Column(name = "qui_modif", length = 50)
//	public String getQuiModif() {
//		return this.quiModif;
//	}
//
//	public void setQuiModif(String quiModif) {
//		this.quiModif = quiModif;
//	}
//
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name = "quand_modif", length = 19)
//	public Date getQuandModif() {
//		return this.quandModif;
//	}
//
//	public void setQuandModif(Date quandModif) {
//		this.quandModif = quandModif;
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




	@Column(name = "version", length = 20)
	public String getVersion() {
		return this.version;

	}
	public void setVersion(String version) {
		this.version = version;

	}

	public TaEntrepot(int idEntrepot, String codeEntrepot, String libelle,
			Boolean actif, Integer versionObj, String quiCree, Date quandCree,
			String quiModif, Date quandModif, String ipAcces, String version) {
		super();
		this.idEntrepot = idEntrepot;
		this.codeEntrepot = codeEntrepot;
		this.libelle = libelle;
		this.actif = actif;
		this.versionObj = versionObj;
		this.quiCree = quiCree;
		this.quandCree = quandCree;
		this.quiModif = quiModif;
		this.quandModif = quandModif;
		this.ipAcces = ipAcces;
		this.version = version;
	}


}
