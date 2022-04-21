package fr.legrain.moncompte.model;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import org.eclipse.persistence.oxm.annotations.XmlInverseReference;

@Entity
@Table(name = "ta_l_commission")
@NamedQueries(value = { 
		@NamedQuery(name=TaLigneCommission.QN.FIND_BY_CODE, query="select c from TaLigneCommission c where c.id= :id")
		})
@XmlAccessorType(XmlAccessType.FIELD)
public class TaLigneCommission implements Serializable {

	private static final long serialVersionUID = -7831355810974329538L;

	public static class QN {
		public static final String FIND_BY_CODE = "TaLigneCommission.findByCode";
	}
	
	/*
CREATE TABLE ta_l_commission
(
	id_l_commission serial NOT NULL,
	id_commission did_facultatif NOT NULL,
	id_l_panier did_facultatif,
	id_produit_achete did_facultatif,
	montant_reference did9facult,
	pourcentage_commission did9facult,
	montant_commission did9facult,
	commentaire_legrain dlib_commentaire,
	commentaire_partenaire dlib_commentaire,
	ip_acces dlib50,
	quand_cree date_heure_lgr,
	quand_modif date_heure_lgr,
	qui_cree dlib50,
	qui_modif dlib50,
	version_obj did_version_obj,
	CONSTRAINT ta_l_commission_pkey PRIMARY KEY (id_l_commission)
)
WITH (
  OIDS=FALSE
);
	 */

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_l_commission")
	private Integer id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@XmlElement
	@XmlInverseReference(mappedBy="lignesCommission")
	@JoinColumn(name = "id_commission")
	public TaCommission taCommission;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_l_panier")
	public TaLignePanier taLignePanier;
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "id_produit_achete")
	public TaProduit taProduit;
	
	@Column(name = "montant_reference")
	public BigDecimal montantReference;
	
	@Column(name = "pourcentage_commission")
	public BigDecimal pourcentageCommission;
	
	@Column(name = "montant_commission")
	public BigDecimal montantCommission;
	
	@Column(name="commentaire_legrain")
	private String commentaireLegrain;
	
	@Column(name="commentaire_partenaire")
	private String commentairePartenaire;
	
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

	public TaCommission getTaCommission() {
		return taCommission;
	}

	public void setTaCommission(TaCommission taCommission) {
		this.taCommission = taCommission;
	}

	public TaLignePanier getTaLignePanier() {
		return taLignePanier;
	}

	public void setTaLignePanier(TaLignePanier taLignePanier) {
		this.taLignePanier = taLignePanier;
	}

	public TaProduit getTaProduit() {
		return taProduit;
	}

	public void setTaProduit(TaProduit taProduit) {
		this.taProduit = taProduit;
	}

	public BigDecimal getMontantReference() {
		return montantReference;
	}

	public void setMontantReference(BigDecimal montantReference) {
		this.montantReference = montantReference;
	}

	public BigDecimal getPourcentageCommission() {
		return pourcentageCommission;
	}

	public void setPourcentageCommission(BigDecimal pourcentageCommission) {
		this.pourcentageCommission = pourcentageCommission;
	}

	public BigDecimal getMontantCommission() {
		return montantCommission;
	}

	public void setMontantCommission(BigDecimal montantCommission) {
		this.montantCommission = montantCommission;
	}

	public String getCommentaireLegrain() {
		return commentaireLegrain;
	}

	public void setCommentaireLegrain(String commentaireLegrain) {
		this.commentaireLegrain = commentaireLegrain;
	}

	public String getCommentairePartenaire() {
		return commentairePartenaire;
	}

	public void setCommentairePartenaire(String commentairePartenaire) {
		this.commentairePartenaire = commentairePartenaire;
	}

	public Date getQuandCree() {
		return quandCree;
	}

	public void setQuandCree(Date quandCree) {
		this.quandCree = quandCree;
	}

	public Date getQuandModif() {
		return quandModif;
	}

	public void setQuandModif(Date quandModif) {
		this.quandModif = quandModif;
	}

	public String getQuiCree() {
		return quiCree;
	}

	public void setQuiCree(String quiCree) {
		this.quiCree = quiCree;
	}

	public String getQuiModif() {
		return quiModif;
	}

	public void setQuiModif(String quiModif) {
		this.quiModif = quiModif;
	}

	public Integer getVersionObj() {
		return versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

}
