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
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "ta_commissions")
@NamedQueries(value = { 
		@NamedQuery(name=TaCommission.QN.FIND_BY_CODE, query="select c from TaCommission c where c.id= :id")
		})
@XmlAccessorType(XmlAccessType.FIELD)
public class TaCommission implements Serializable {

	private static final long serialVersionUID = 2479968053910975754L;

	public static class QN {
		public static final String FIND_BY_CODE = "TaCommission.findByCode";
		
	}

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_commission")
	private Integer id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_partenaire")
	private TaPartenaire taPartenaire;
	
	@ManyToOne(fetch = FetchType.EAGER)
	//@XmlInverseReference(mappedBy="lignes")
	@JoinColumn(name = "id_panier")
	public TaPanier taPanier;
	
	@Column(name = "montant_reference")
	public BigDecimal montantReference;
	
	@Column(name = "pourcentage_commission")
	public BigDecimal pourcentageCommission;
	
	@Column(name = "montant_commission")
	public BigDecimal montantCommission;
	
	@Column(name = "code_document_associe")
	public String codeDocumentAssocie;
	
	@Column(name="date_paiement_commission")
	@Temporal(TemporalType.TIMESTAMP)
	private Date datePaiementCommission;
	
	@Column(name="commentaire_legrain")
	private String commentaireLegrain;
	
	@Column(name="commentaire_partenaire")
	private String commentairePartenaire;
	
	
	@Column(name="commission_payee")
	//http://stackoverflow.com/questions/14400222/boolean-properties-starting-with-is-does-not-work
	private boolean commissionPayee = true;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "taCommission", orphanRemoval=true)
	@Fetch(FetchMode.SUBSELECT)
	private List<TaLigneCommission> lignesCommission;
	
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

	public TaPartenaire getTaPartenaire() {
		return taPartenaire;
	}

	public void setTaPartenaire(TaPartenaire taPartenaire) {
		this.taPartenaire = taPartenaire;
	}

	public TaPanier getTaPanier() {
		return taPanier;
	}

	public void setTaPanier(TaPanier taPanier) {
		this.taPanier = taPanier;
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

	public String getCodeDocumentAssocie() {
		return codeDocumentAssocie;
	}

	public void setCodeDocumentAssocie(String codeDocumentAssocie) {
		this.codeDocumentAssocie = codeDocumentAssocie;
	}

	public Date getDatePaiementCommission() {
		return datePaiementCommission;
	}

	public void setDatePaiementCommission(Date datePaiementCommission) {
		this.datePaiementCommission = datePaiementCommission;
	}

	public String getCommentaireLegrain() {
		return commentaireLegrain;
	}

	public void setCommentaireLegrain(String commentaire_Legrain) {
		this.commentaireLegrain = commentaire_Legrain;
	}

	public String getCommentairePartenaire() {
		return commentairePartenaire;
	}

	public void setCommentairePartenaire(String commentairePartenaire) {
		this.commentairePartenaire = commentairePartenaire;
	}

	public Boolean getCommissionPayee() {
		return commissionPayee;
	}

	public void setCommissionPayee(Boolean commissionPayee) {
		this.commissionPayee = commissionPayee;
	}

	public List<TaLigneCommission> getLignesCommission() {
		return lignesCommission;
	}

	public void setLignesCommission(List<TaLigneCommission> lignesCommission) {
		this.lignesCommission = lignesCommission;
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
