package fr.legrain.article.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.log4j.Logger;

import fr.legrain.general.model.TaObjetLgr;
import fr.legrain.validator.LgrHibernateValidated;


@Entity
@Table(name = "ta_coefficient_unite")
@NamedQueries(value = { 
	@NamedQuery(name=TaCoefficientUnite.QN.FIND_BY_ARTICLE, query="select a from TaUnite a left join a.taArticle ar where a.taArticle is null or ar.codeArticle = :codeArticle")
})
public class TaCoefficientUnite extends TaObjetLgr   implements java.io.Serializable {
	
	private static final long serialVersionUID = -1164853348965871664L;

	public static class QN {
		public static final String FIND_BY_ARTICLE = "TaCoefficientUnite.findByArticle";
	}

	private int id;
	private TaUnite uniteA;
	private TaUnite uniteB;
	private BigDecimal coeffUniteAVersB;
	private BigDecimal coeffUniteBVersA;

	
	private Boolean operateurAVersBDivise = false;
	private Boolean operateurBVersADivise = false;
	
	
	private Integer nbDecimaleAVersB;
	private Integer nbDecimaleBVersA;
	
//	private String quiCree;
//	private Date quandCree;
//	private String quiModif;
//	private Date quandModif;
//	private String ipAcces;
	private Integer versionObj;
	
	@Transient
	private BigDecimal coeff;
	@Transient
	private Integer nbDecimale;
	@Transient
	private Boolean operateurDivise = false;
	

	public TaCoefficientUnite() {
	}

	public TaCoefficientUnite(int id) {
		this.id = id;
	}


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	@LgrHibernateValidated(champBd = "id",table = "ta_coefficient_unite", champEntite="id", clazz = TaCoefficientUnite.class)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_unite_a")
	public TaUnite getUniteA() {
		return uniteA;
	}

	public void setUniteA(TaUnite uniteA) {
		this.uniteA = uniteA;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_unite_b")
	public TaUnite getUniteB() {
		return uniteB;
	}

	public void setUniteB(TaUnite uniteB) {
		this.uniteB = uniteB;
	}

	@Column(name = "coeff_a_vers_b")
	public BigDecimal getCoeffUniteAVersB() {
		return coeffUniteAVersB;
	}

	public void setCoeffUniteAVersB(BigDecimal coeffUniteAVersB) {
		this.coeffUniteAVersB = coeffUniteAVersB;
	}

	@Column(name = "coeff_b_vers_a")
	public BigDecimal getCoeffUniteBVersA() {
		return coeffUniteBVersA;
	}

	public void setCoeffUniteBVersA(BigDecimal coeffUniteBVersA) {
		this.coeffUniteBVersA = coeffUniteBVersA;
	}

//	@Column(name = "qui_cree", length = 50)
//	public String getQuiCree() {
//		return this.quiCree;
//	}
//
//	public void setQuiCree(String quiCreeUnite) {
//		this.quiCree = quiCreeUnite;
//	}
//
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name = "quand_cree", length = 19)
//	public Date getQuandCree() {
//		return this.quandCree;
//	}
//
//	public void setQuandCree(Date quandCreeUnite) {
//		this.quandCree = quandCreeUnite;
//	}
//
//	@Column(name = "qui_modif", length = 50)
//	public String getQuiModif() {
//		return this.quiModif;
//	}
//
//	public void setQuiModif(String quiModifUnite) {
//		this.quiModif = quiModifUnite;
//	}
//
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name = "quand_modif", length = 19)
//	public Date getQuandModif() {
//		return this.quandModif;
//	}
//
//	public void setQuandModif(Date quandModifUnite) {
//		this.quandModif = quandModifUnite;
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
		return versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

	@Column(name = "nb_decimale_a_vers_b")
	public Integer getNbDecimaleAVersB() {
		return nbDecimaleAVersB;
	}

	public void setNbDecimaleAVersB(Integer nbDecimaleAVersB) {
		this.nbDecimaleAVersB = nbDecimaleAVersB;
	}

	@Column(name = "nb_decimale_b_vers_a")
	public Integer getNbDecimaleBVersA() {
		return nbDecimaleBVersA;
	}

	public void setNbDecimaleBVersA(Integer nbDecimaleBVersA) {
		this.nbDecimaleBVersA = nbDecimaleBVersA;
	}

	@Column(name = "operateur_a_vers_b_divise")
	public Boolean getOperateurAVersBDivise() {
		return operateurAVersBDivise;
	}

	public void setOperateurAVersBDivise(Boolean operateurAVersBDivise) {
		this.operateurAVersBDivise = operateurAVersBDivise;
	}

	@Column(name = "operateur_b_vers_a_divise")
	public Boolean getOperateurBVersADivise() {
		return operateurBVersADivise;
	}

	public void setOperateurBVersADivise(Boolean operateurBVersADivise) {
		this.operateurBVersADivise = operateurBVersADivise;
	}
	
	public void recupRapportEtSens(String codeUnite1,String codeUnite2) {
		if(this.uniteA!=null && this.uniteA.getCodeUnite()!=null && this.uniteA.getCodeUnite().equals(codeUnite1)) {
			coeff=this.coeffUniteAVersB;
			nbDecimale=this.nbDecimaleAVersB;
			operateurDivise=this.operateurAVersBDivise;
		}else
			if(this.uniteB!=null && this.uniteB.getCodeUnite()!=null && this.uniteB.getCodeUnite().equals(codeUnite1)) {
				coeff=this.coeffUniteBVersA;
				nbDecimale=this.nbDecimaleBVersA;
				operateurDivise=this.operateurBVersADivise;	
			}
	}

	@Transient
	public BigDecimal getCoeff() {
		return coeff;
	}

	public void setCoeff(BigDecimal coeff) {
		this.coeff = coeff;
	}

	@Transient
	public Integer getNbDecimale() {
		return nbDecimale;
	}

	public void setNbDecimale(Integer nbDecimale) {
		this.nbDecimale = nbDecimale;
	}

	@Transient
	public Boolean getOperateurDivise() {
		return operateurDivise;
	}

	public void setOperateurDivise(Boolean operateurDivise) {
		this.operateurDivise = operateurDivise;
	}

}
