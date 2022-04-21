package fr.legrain.moncompte.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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

import org.eclipse.persistence.oxm.annotations.XmlInverseReference;

@Entity
@Table(name = "ta_prix_nbutil")
@NamedQueries(value = { 
		@NamedQuery(name=TaPrixParUtilisateur.QN.FIND_BY_CODE, query="select f from TaPrixParUtilisateur f where f.id= :code")
})
public class TaPrixParUtilisateur implements Serializable {

	private static final long serialVersionUID = -569443777253534183L;

	public static class QN {
		public static final String FIND_BY_CODE = "TaPrixParUtilisateur.findByCode";
	}
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;

	@ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "id_produit")
	private TaProduit taProduit;

	@Column(name = "prix_ht")
	private BigDecimal prixHT;

	@Column(name = "taux_tva")
	private BigDecimal tauxTVA;

	@Column(name = "prix_ttc")
	private BigDecimal prixTTC;

	@Column(name="nb_utilisateur")
	private Integer nbUtilisateur;

	@Column(name="quand_cree")
	@Temporal(TemporalType.TIMESTAMP)
	private Date quandCree;

	@Column(name="quand_modif")
	@Temporal(TemporalType.TIMESTAMP)
	private Date quandModif;

	@Column(name="qui_cree")
	private String quiCree;

	@Column(name="qui_modif")
	private String quiModif;

	//	@Column(name = "ip_acces")
	//	private String ipAcces;

	@Version
	@Column(name = "version_obj")
	private Integer versionObj;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public BigDecimal getPrixHT() {
		return prixHT;
	}

	public void setPrixHT(BigDecimal prixHT) {
		this.prixHT = prixHT;
	}

	public BigDecimal getTauxTVA() {
		return tauxTVA;
	}

	public void setTauxTVA(BigDecimal tauxTVA) {
		this.tauxTVA = tauxTVA;
	}

	public BigDecimal getPrixTTC() {
		return prixTTC;
	}

	public void setPrixTTC(BigDecimal prixTTC) {
		this.prixTTC = prixTTC;
	}


	public Date getQuandCree() {
		return quandCree;
	}

	public Date getQuandModif() {
		return quandModif;
	}

	public String getQuiCree() {
		return quiCree;
	}

	public String getQuiModif() {
		return quiModif;
	}

	public Integer getVersionObj() {
		return versionObj;
	}

	public void setQuandCree(Date quanCree) {
		this.quandCree = quanCree;
	}

	public void setQuandModif(Date quandModif) {
		this.quandModif = quandModif;
	}

	public void setQuiCree(String quiCree) {
		this.quiCree = quiCree;
	}

	public void setQuiModif(String quiModif) {
		this.quiModif = quiModif;
	}


	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

	public TaProduit getTaProduit() {
		return taProduit;
	}

	public void setTaProduit(TaProduit taProduit) {
		this.taProduit = taProduit;
	}

	public Integer getNbUtilisateur() {
		return nbUtilisateur;
	}

	public void setNbUtilisateur(Integer nbUtilisateur) {
		this.nbUtilisateur = nbUtilisateur;
	}

}
