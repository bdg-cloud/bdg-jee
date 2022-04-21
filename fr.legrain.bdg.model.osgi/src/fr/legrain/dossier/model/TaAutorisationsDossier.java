package fr.legrain.dossier.model;

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
@Table(name = "ta_autorisations_dossier")
public class TaAutorisationsDossier implements java.io.Serializable {

	private static final long serialVersionUID = -4882117445079965697L;
	
	private int idAutorisation;
	private String fichier;
	private Date dateDerSynchro;

	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private Integer versionObj;

	public TaAutorisationsDossier() {
	}

	public TaAutorisationsDossier(int idAutorisation) {
		this.idAutorisation = idAutorisation;
	}

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_autorisation", unique = true, nullable = false)
	@LgrHibernateValidated(champBd = "id_autorisation",table = "ta_autorisations_dossier", champEntite="idAutorisation", clazz = TaAutorisationsDossier.class)
	public int getIdAutorisation() {
		return this.idAutorisation;
	}

	public void setIdAutorisation(int idAutorisation) {
		this.idAutorisation = idAutorisation;
	}



	@Column(name = "fichier", length = 100)
	@LgrHibernateValidated(champBd = "fichier",table = "ta_autorisations_dossier", champEntite="fichier", clazz = TaAutorisationsDossier.class)
	public String getFichier() {
		return this.fichier;
	}

	public void setFichier(String codeTiers) {
		this.fichier = codeTiers;
	}



	@Column(name = "date_der_synchro", length = 100)
	@LgrHibernateValidated(champBd = "date_der_synchro",table = "ta_autorisations_dossier", champEntite="dateDerSynchro", clazz = TaAutorisationsDossier.class)
	public Date getDateDerSynchro() {
		return this.dateDerSynchro;
	}

	public void setDateDerSynchro(Date dateDerSynchro) {
		this.dateDerSynchro = dateDerSynchro;
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
