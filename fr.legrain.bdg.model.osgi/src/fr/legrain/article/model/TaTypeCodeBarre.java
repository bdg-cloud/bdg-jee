package fr.legrain.article.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.apache.log4j.Logger;

import fr.legrain.general.model.TaObjetLgr;
import fr.legrain.validator.LgrHibernateValidated;

@Entity
@Table(name = "ta_type_code_barre")

public class TaTypeCodeBarre extends TaObjetLgr    implements Serializable{


	private static final long serialVersionUID = -7535064684986081321L;
	
	private int idTypeCodeBarre;
	private String libelle;
	private String codeTypeCodeBarre;
	private Integer versionObj;
//	private String quiCree;
//	private Date quandCree;
//	private String quiModif;
//	private Date quandModif;
//	private String ipAcces;
//	private String version;
	@Transient
	static Logger logger = Logger.getLogger(TaTypeCodeBarre.class.getName());
	
	
	
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_type_code_barre", unique = true, nullable = false)
	public int getIdTypeCodeBarre() {
		return this.idTypeCodeBarre;
	}

	public void setIdTypeCodeBarre(int idTypecodebarre) {
		this.idTypeCodeBarre = idTypecodebarre;
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
//	//@Version
//		@Column(name = "version", length = 20)
//		public String getVersion() {
//			return this.version;
//		}
//
//		public void setVersion(String version) {
//			this.version = version;
//		}
		@Column(name = "libelle_type_code_barre")
		@LgrHibernateValidated(champBd = "libelle_type_code_barre",table = "ta_type_code_barre", champEntite="libelle", clazz = TaTypeCodeBarre.class)
		public String getLibelle() {
			return this.libelle;
		}

		public void setLibelle(String codebarre) {
			this.libelle = codebarre;
		}
		@Column(name = "code_type_code_barre")
		@LgrHibernateValidated(champBd = "code_type_code_barre",table = "ta_type_code_barre", champEntite="codeTypeCodeBarre", clazz = TaTypeCodeBarre.class)
		public String getCodeTypeCodeBarre() {
		    return codeTypeCodeBarre;
		}

		public void setCodeTypeCodeBarre(String code) {
		    this.codeTypeCodeBarre = code;
		}
		
		
}
