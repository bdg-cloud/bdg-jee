package fr.legrain.autorisations.autorisation.model;

// Generated Mar 24, 2009 4:44:04 PM by Hibernate Tools 3.2.0.CR1

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

@Table(name = "ta_type_produit")


public class TaTypeProduit implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1791584148276356085L;
	
	
	private int idType;
	private String code;
	private String libelle;

	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private Integer versionObj;

	public TaTypeProduit() {
	}

	public TaTypeProduit(int idType) {
		this.idType = idType;
	}

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_type", unique = true, nullable = false)
	@LgrHibernateValidated(champBd = "id_type",table = "ta_type_produit", champEntite="idType", clazz = TaTypeProduit.class)
	public int getIdType() {
		return this.idType;
	}

	public void setIdType(int idType) {
		this.idType = idType;
	}



	@Column(name = "code", length = 100)
	@LgrHibernateValidated(champBd = "code",table = "ta_type_produit", champEntite="code", clazz = TaTypeProduit.class)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}



	@Column(name = "libelle", length = 100)
	@LgrHibernateValidated(champBd = "libelle",table = "ta_type_produit", champEntite="libelle", clazz = TaTypeProduit.class)
	public String getLibelle() {
		return this.libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
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
	


	@Version
	@Column(name = "version_obj")
	public Integer getVersionObj() {
		return this.versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}


}
