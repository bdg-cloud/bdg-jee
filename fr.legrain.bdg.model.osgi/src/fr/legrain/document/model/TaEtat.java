package fr.legrain.document.model;

import java.util.Date;
import java.util.Set;

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
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import fr.legrain.general.model.TaObjetLgr;
import fr.legrain.validator.LgrHibernateValidated;

/**
 * TaEtat
 */
@Entity
//@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "ta_etat", uniqueConstraints = @UniqueConstraint(columnNames = "code_etat"))
//@SequenceGenerator(name = "gen_etat", sequenceName = "num_id_etat", allocationSize = 1)
public class TaEtat extends TaObjetLgr  implements java.io.Serializable {

	private static final long serialVersionUID = 1114584422321306640L;
	
	public static final String ETAT_NON_TRANSFORME_A_RELANCER = "NON_TRANSFORME_A_RELANCER";
	public static final String TERMINE_TOTALEMENT_TRANSFORME = "TERMINE_TOTALEMENT_TRANSFORME";
	
	public static final String ETAT_REG_VALIDE = "ETAT_REG_VALIDE";
	public static final String ETAT_REG_REJETE = "ETAT_REG_REJETE";
	public static final String ETAT_REG_ENCOURS = "ETAT_REG_ENCOURS";
	
	
	public static final String BROUILLON="brouillon";
	public static final String COMMERCIAL="commercial";
	public static final String PREPARATION="preparation";
	public static final String ENCOURS="encours";
	public static final String TERMINE="termine";
	public static final String ABANDONNE="abandonne";
	
	public static final String DOCUMENT_BROUILLON="bdg.etat.document.brouillon";
	public static final String DOCUMENT_COMMERCIAL="bdg.etat.document.commercial";
	public static final String DOCUMENT_PREPARATION="bdg.etat.document.preparation";
	public static final String DOCUMENT_ENCOURS="bdg.etat.document.encours";
	public static final String DOCUMENT_TERMINE="bdg.etat.document.termine";
	public static final String DOCUMENT_ABANDONNE="bdg.etat.document.termine.abandonne";
	
	
//	public static final String NON_TRANSFORME="non.transforme";
	public static final String DOCUMENT_PARTIELLEMENT_TRANSFORME="bdg.etat.document.partiellement.transforme";
	public static final String DOCUMENT_TOTALEMENT_TRANSFORME="bdg.etat.document.termine.totalement.transforme";
	public static final String DOCUMENT_PARTIELLEMENT_ABANDONNE="bdg.etat.document.partiellement.abandonne";
	public static final String DOCUMENT_TERMINE_PARTIELLEMENT_ABANDONNE="bdg.etat.document.termine.partiellement.abandonne";
	
	public static final String ETAT_DOCUMENT="bdg.etat.document";
	
	public static final String DOCUMENT_SUSPENDU="bdg.etat.document.suspendu";

	
	public static final String ETAT_TOUS="TOUS";
	public static final String ETAT_AUCUN="AUCUN";




	
	/**		Extrait de BdDs
	* 	id_etat serial NOT NULL,
	  	code_etat character varying(20) DEFAULT ''::character varying,
	  	libelle_etat character varying(255) DEFAULT ''::character varying,
	  	qui_cree character varying(50) DEFAULT ''::character varying,
		quand_cree timestamp with time zone DEFAULT now(),
		qui_modif character varying(50) DEFAULT ''::character varying,
		quand_modif timestamp with time zone DEFAULT now(),
		version character varying(20) DEFAULT ''::character varying,
		ip_acces character varying(50) DEFAULT '0'::character varying,
		version_obj integer DEFAULT 0,
		CONSTRAINT ta_etat_pkey PRIMARY KEY (id_etat)
	 */
	
	private int idEtat;
//	private String version;
	private String codeEtat;
	private String liblEtat;
	private String identifiant;
	private Boolean systeme;
	private Boolean visible;
	private Boolean listable;
	private Boolean accepte;
	private TaTEtat taTEtat;
	private String commentaire;
	private Integer position;
	
//	private String quiCree;
//	private Date quandCree;
//	private String quiModif;
//	private Date quandModif;
//	private String ipAcces;
	private Integer versionObj;

	public TaEtat() {
	}

	
	public TaEtat(String codeEtat) {
		this.codeEtat = codeEtat;
	}


	public TaEtat(int idEtat) {
		this.idEtat = idEtat;
	}

	public TaEtat(int idEtat, String codeEtat, String liblEtat,
			String quiCree, Date quandCree, String quiModif,
			Date quandModif, String ipAcces, Integer versionObj,
			Set<TaComDoc> taComDocs) {
		this.idEtat = idEtat;
		this.codeEtat = codeEtat;
		this.liblEtat = liblEtat;
		this.quiCree = quiCree;
		this.quandCree = quandCree;
		this.quiModif = quiModif;
		this.quandModif = quandModif;
		this.ipAcces = ipAcces;
		this.versionObj = versionObj;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_etat", unique = true, nullable = false)
	@LgrHibernateValidated(champBd = "id_etat",table = "ta_etat",champEntite="idEtat", clazz = TaEtat.class)
	public int getIdEtat() {
		return this.idEtat;
	}

	public void setIdEtat(int idTDoc) {
		this.idEtat = idTDoc;
	}

//	@Column(name = "version", length = 20)
//	public String getVersion() {
//		return this.version;
//	}
//
//	public void setVersion(String version) {
//		this.version = version;
//	}

	@Column(name = "code_etat", unique = true, length = 50)
	@LgrHibernateValidated(champBd = "code_etat",table = "ta_etat",champEntite="codeEtat", clazz = TaEtat.class)
	public String getCodeEtat() {
		return this.codeEtat;
	}

	public void setCodeEtat(String codeEtat) {
		this.codeEtat = codeEtat;
	}

	@Column(name = "libelle_etat")
	@LgrHibernateValidated(champBd = "libelle_etat",table = "ta_etat",champEntite="liblEtat", clazz = TaEtat.class)
	public String getLiblEtat() {
		return this.liblEtat;
	}

	public void setLiblEtat(String liblEtat) {
		this.liblEtat = liblEtat;
	}

//	@Column(name = "qui_cree", length = 50)
//	public String getQuiCree() {
//		return this.quiCree;
//	}
//
//	public void setQuiCree(String quiCreeTDoc) {
//		this.quiCree = quiCreeTDoc;
//	}
//
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name = "quand_cree", length = 19)
//	public Date getQuandCree() {
//		return this.quandCree;
//	}
//
//	public void setQuandCree(Date quandCreeTDoc) {
//		this.quandCree = quandCreeTDoc;
//	}
//
//	@Column(name = "qui_modif", length = 50)
//	public String getQuiModif() {
//		return this.quiModif;
//	}
//
//	public void setQuiModif(String quiModifTDoc) {
//		this.quiModif = quiModifTDoc;
//	}
//
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name = "quand_modif", length = 19)
//	public Date getQuandModif() {
//		return this.quandModif;
//	}
//
//	public void setQuandModif(Date quandModifTDoc) {
//		this.quandModif = quandModifTDoc;
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

	@Column(name = "identifiant")
	public String getIdentifiant() {
		return identifiant;
	}

	public void setIdentifiant(String identifiant) {
		this.identifiant = identifiant;
	}

	@Column(name = "systeme")
	public Boolean getSysteme() {
		return systeme;
	}

	public void setSysteme(Boolean systeme) {
		this.systeme = systeme;
	}

	@Column(name = "visible")
	public Boolean getVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}





	@ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.REFRESH})
	@JoinColumn(name = "id_t_etat")
	public TaTEtat getTaTEtat() {
		return taTEtat;
	}


	public void setTaTEtat(TaTEtat taTEtat) {
		this.taTEtat = taTEtat;
	}



	@Column(name = "position")
	public Integer getPosition() {
		return position;
	}


	public void setPosition(Integer position) {
		this.position = position;
	}


	@Column(name = "listable")
	public Boolean getListable() {
		return listable;
	}


	public void setListable(Boolean listable) {
		this.listable = listable;
	}


	@Column(name = "accepte")
	public Boolean getAccepte() {
		return accepte;
	}


	public void setAccepte(Boolean accepte) {
		this.accepte = accepte;
	}


	@Column(name = "commentaire")
	public String getCommentaire() {
		return commentaire;
	}


	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}

}
