package fr.legrain.article.model;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

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
import javax.persistence.Transient;
import javax.persistence.Version;

import org.apache.log4j.Logger;

import fr.legrain.general.model.TaObjetLgr;
import fr.legrain.validator.LgrHibernateValidated;

@Entity
@Table(name = "ta_code_barre")
public class TaCodeBarre extends TaObjetLgr    implements Serializable{

	private static final long serialVersionUID = 1318286540362401954L;

	private int id;

	private TaTypeCodeBarre taTypeCodeBarre;
	private String codeBarre;
	private Integer versionObj;

//	private String quiCree;
//	private Date quandCree;
//	private String quiModif;
//	private Date quandModif;
//	private String ipAcces;
//	private String version;
	
	

	@Transient
	static Logger logger = Logger.getLogger(TaCodeBarre.class.getName());

	public TaCodeBarre() {
		super();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_code_barre", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "code_barre")
	@LgrHibernateValidated(champBd = "code_barre",table = "ta_code_barre", champEntite="codeBarre", clazz = TaCodeBarre.class)
	public String getCodeBarre() {
		return this.codeBarre;
	}

	public void setCodeBarre(String codeBarre) {
		this.codeBarre = codeBarre;
	}
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "id_type_code_barre")
	//@SolstyceHibernateValidated(champ = "ID_ARTICLE",table = "TA_ARTICLE",clazz = TaArticle.class)
	@LgrHibernateValidated(champBd = "code_barre",table = "ta_code_barre", champEntite="taTypeCodeBarre.idTypeCodeBarre", clazz = TaTypeCodeBarre.class)
	public TaTypeCodeBarre getTaTypeCodeBarre() {
		return this.taTypeCodeBarre;
	}

	public void setTaTypeCodeBarre(TaTypeCodeBarre taTypeCodeBarre) {
		this.taTypeCodeBarre = taTypeCodeBarre;
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
//
//	@Column(name = "version", length = 20)
//	public String getVersion() {
//		return this.version;
//	}
//
//	public void setVersion(String version) {
//		this.version = version;
//	}

}
