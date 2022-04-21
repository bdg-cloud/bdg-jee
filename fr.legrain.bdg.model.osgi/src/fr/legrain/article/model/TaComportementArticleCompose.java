package fr.legrain.article.model;


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



@Entity
@Table(name = "ta_comportement_article_compose")
public class TaComportementArticleCompose implements java.io.Serializable {
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 4221701836451075654L;

	public static class QN {
		
	}
	
	
	private Integer idComportementArticleCompose;
	private String codeComportement;
	private String liblComportement;
	private String descComportement;


	private String ipAcces;
	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private Integer versionObj;

	public TaComportementArticleCompose() {
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "quand_cree", length = 19)
	public Date getQuandCree() {
		return this.quandCree;
	}

	public void setQuandCree(Date quandCreeAdresse) {
		this.quandCree = quandCreeAdresse;
	}

	@Column(name = "qui_cree", length = 50)
	public String getQuiCree() {
		return this.quiCree;
	}

	public void setQuiCree(String quiCreeAdresse) {
		this.quiCree = quiCreeAdresse;
	}
	

	@Column(name = "qui_modif", length = 50)
	public String getQuiModif() {
		return this.quiModif;
	}

	public void setQuiModif(String quiModifAdresse) {
		this.quiModif = quiModifAdresse;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "quand_modif", length = 19)
	public Date getQuandModif() {
		return this.quandModif;
	}

	public void setQuandModif(Date quandModifAdresse) {
		this.quandModif = quandModifAdresse;
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


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_comportement_article_compose", unique = true, nullable = false)
	public Integer getIdComportementArticleCompose() {
		return idComportementArticleCompose;
	}

	public void setIdComportementArticleCompose(Integer idComportementArticleCompose) {
		this.idComportementArticleCompose = idComportementArticleCompose;
	}
	@Column(name = "code_comportement")
	public String getCodeComportement() {
		return codeComportement;
	}

	public void setCodeComportement(String codeComportement) {
		this.codeComportement = codeComportement;
	}
	@Column(name = "libl_comportement")
	public String getLiblComportement() {
		return liblComportement;
	}

	public void setLiblComportement(String liblComportement) {
		this.liblComportement = liblComportement;
	}
	@Column(name = "desc_comportement")
	public String getDescComportement() {
		return descComportement;
	}

	public void setDescComportement(String descComportement) {
		this.descComportement = descComportement;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codeComportement == null) ? 0 : codeComportement.hashCode());
		result = prime * result
				+ ((idComportementArticleCompose == null) ? 0 : idComportementArticleCompose.hashCode());
		result = prime * result + ((versionObj == null) ? 0 : versionObj.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TaComportementArticleCompose other = (TaComportementArticleCompose) obj;
		if (codeComportement == null) {
			if (other.codeComportement != null)
				return false;
		} else if (!codeComportement.equals(other.codeComportement))
			return false;
		if (idComportementArticleCompose == null) {
			if (other.idComportementArticleCompose != null)
				return false;
		} else if (!idComportementArticleCompose.equals(other.idComportementArticleCompose))
			return false;
		if (versionObj == null) {
			if (other.versionObj != null)
				return false;
		} else if (!versionObj.equals(other.versionObj))
			return false;
		return true;
	}


}
