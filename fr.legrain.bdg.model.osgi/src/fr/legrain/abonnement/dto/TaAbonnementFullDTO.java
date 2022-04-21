package fr.legrain.abonnement.dto;

import java.util.Date;

import com.ibm.icu.math.BigDecimal;

import fr.legrain.bdg.model.ModelObject;

public class TaAbonnementFullDTO extends ModelObject implements java.io.Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5077848625747302351L;
	
	
	private Integer id;
	private String idExterne;
	private String codeDocument;
	private Integer idStripeCustomer;
	private String idExterneCustomer;
	private String codeTiers;
	private String nomTiers;
	private String prenomTiers;
	private String nomEntreprise;
	private Integer idStripeSource;
	private String idExterneSource;
	private Integer quantity;
	private String status;
	private String billing;
	
	private Boolean prorata = true;
	private Date dateDebut = new Date();
	private Date dateFin = null;
	private Date billingCycleAnchor;
	
	private Date dateAnnulation;
	private String commentaireAnnulation;
	
	private BigDecimal taxe;
	private Integer trial;
	private Integer idStripeCoupon;
	private String idExterneCoupon;
	private Integer nbEcheance;
	private Integer daysUntilDue;
	
	
	//rajout abo/plan/ligneabo
	private Integer idTiers=0;
	private Date dateSuspension;
	private Boolean suspension;
	private Integer idFrequence;
	private String codeFrequence;
	private String liblFrequence;
	private Integer idPlan;
	private String nickname;
	private BigDecimal amount;
	private Integer idArticle;
	private String codeArticle;
	private String liblArticle;
	//ligne
	private String codeTLigne;
	private Integer numLigneLDocument;
	private String libLDocument;
	private BigDecimal qteLDocument;
	private BigDecimal qte2LDocument;
	private BigDecimal prixULDocument;
	private BigDecimal tauxTvaLDocument;
	private String codeTvaLDocument;
	private String codeTTvaLDocument;
	private BigDecimal mtHtLDocument;
	private BigDecimal remTxLDocument;
	private BigDecimal mtTtcLDocument;
	private BigDecimal remHtLDocument;
	private BigDecimal mtHtLApresRemiseGlobaleDocument;
	private BigDecimal mtTtcLApresRemiseGlobaleDocument;
	private String u1LDocument;
	private String u2LDocument;
	private BigDecimal txRemHtDocument;
	//item
	private String complement1;
	private String complement2;
	private String complement3;
	
	private String identifiantEtat;
	
	private Integer versionObj;
	
	public TaAbonnementFullDTO() {
	}
	
	public boolean abonnementEstVide(){		
		return false;
	}

	
	public TaAbonnementFullDTO(Integer id, String idExterne, String codeDocument,
			String codeTiers, String nomTiers, String prenomTiers, 
			Date dateDebut, Date dateFin, Date dateAnnulation,
			String commentaireAnnulation, 
			Date dateSuspension, Boolean suspension, Integer idFrequence,
			String codeFrequence, String liblFrequence,
			String nickname,
			Integer idArticle, String codeArticle, String liblArticle,
			BigDecimal qteLDocument, BigDecimal prixULDocument,
			BigDecimal tauxTvaLDocument, BigDecimal mtHtLDocument,
			 BigDecimal remTxLDocument,
			 BigDecimal mtTtcLDocument,
			 BigDecimal remHtLDocument,
			 String u1LDocument,
			 String u2LDocument,
			 String complement1,
			 String complement2,
			 String complement3) {
		super();
		this.id=id; 
		this.idExterne=idExterne; 
		this.codeDocument=codeDocument;
		this.codeTiers=codeTiers;
		this.nomTiers=nomTiers; 
		this.prenomTiers=prenomTiers;
		this.dateDebut=dateDebut;
		this.dateFin=dateFin;
		this.dateAnnulation=dateAnnulation;
		this.commentaireAnnulation=commentaireAnnulation;
		this.dateSuspension=dateSuspension;
		this.suspension=suspension; 
		this.idFrequence=idFrequence;
		this.codeFrequence=codeFrequence;
		this.liblFrequence=liblFrequence; 
		this.nickname=nickname; 
		this.idArticle=idArticle;
		this.codeArticle=codeArticle;
		this.liblArticle=liblArticle;
		this.qteLDocument=qteLDocument; 
		this.prixULDocument=prixULDocument;
		this.tauxTvaLDocument=tauxTvaLDocument; 
		this.mtHtLDocument=mtHtLDocument;
		this.remTxLDocument=remTxLDocument;
		this.mtTtcLDocument=mtTtcLDocument;
		this.remHtLDocument=remHtLDocument;
		this.u1LDocument=u1LDocument;
		this.u2LDocument=u2LDocument;
		this.complement1=complement1;
		this.complement2=complement2;
		this.complement3=complement3;
	}
		
	public TaAbonnementFullDTO(
			Integer id, String idExterne, String codeDocument,
			String codeTiers, String nomTiers, String prenomTiers, 
			Date dateDebut, Date dateFin, Date dateAnnulation,
			String commentaireAnnulation, 
			Date dateSuspension, Boolean suspension, Integer idFrequence,
			String codeFrequence, String liblFrequence,
			String nickname,
			Integer idArticle, String codeArticle, String liblArticle,
			 String u1LDocument,
			 String u2LDocument,
			 String complement1,
			 String complement2,
			 String complement3
) {
		super();

		this.id=id; 
		this.idExterne=idExterne; 
		this.codeDocument=codeDocument;
		this.codeTiers=codeTiers;
		this.nomTiers=nomTiers; 
		this.prenomTiers=prenomTiers;
		this.dateDebut=dateDebut;
		this.dateFin=dateFin;
		this.dateAnnulation=dateAnnulation;
		this.commentaireAnnulation=commentaireAnnulation;
		this.dateSuspension=dateSuspension;
		this.suspension=suspension; 
		this.idFrequence=idFrequence;
		this.codeFrequence=codeFrequence;
		this.liblFrequence=liblFrequence; 
		this.nickname=nickname; 
		this.idArticle=idArticle;
		this.codeArticle=codeArticle;
		this.liblArticle=liblArticle;
		this.u1LDocument=u1LDocument;
		this.u2LDocument=u2LDocument;
		this.complement1=complement1;
		this.complement2=complement2;
		this.complement3=complement3;
	}

	public TaAbonnementFullDTO(
			Integer id, String idExterne, String codeDocument,
			String codeTiers, String nomTiers, String prenomTiers, 
			Date dateDebut, Date dateFin, Date dateAnnulation,
			String commentaireAnnulation, 
			Date dateSuspension, Boolean suspension, Integer idFrequence,
			String codeFrequence, String liblFrequence,
			String nickname,
			Integer idArticle, String codeArticle, String liblArticle,
			 String u1LDocument,
			 String u2LDocument,
			 String complement1,
			 String complement2,
			 String complement3,
			 String identifiantEtat
) {
		super();

		this.id=id; 
		this.idExterne=idExterne; 
		this.codeDocument=codeDocument;
		this.codeTiers=codeTiers;
		this.nomTiers=nomTiers; 
		this.prenomTiers=prenomTiers;
		this.dateDebut=dateDebut;
		this.dateFin=dateFin;
		this.dateAnnulation=dateAnnulation;
		this.commentaireAnnulation=commentaireAnnulation;
		this.dateSuspension=dateSuspension;
		this.suspension=suspension; 
		this.idFrequence=idFrequence;
		this.codeFrequence=codeFrequence;
		this.liblFrequence=liblFrequence; 
		this.nickname=nickname; 
		this.idArticle=idArticle;
		this.codeArticle=codeArticle;
		this.liblArticle=liblArticle;
		this.u1LDocument=u1LDocument;
		this.u2LDocument=u2LDocument;
		this.complement1=complement1;
		this.complement2=complement2;
		this.complement3=complement3;
		this.identifiantEtat=identifiantEtat;
	}
	
	
	public static TaAbonnementFullDTO copy(TaAbonnementFullDTO taEntrepot){
		TaAbonnementFullDTO taEntrepotLoc = new TaAbonnementFullDTO();
		taEntrepotLoc.setId(taEntrepot.id);
		
		return taEntrepotLoc;
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public void setId(Integer id) {
		firePropertyChange("id", this.id, this.id = id);
	}
	
	public Integer getVersionObj() {
		return this.versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

	public String getIdExterne() {
		return idExterne;
	}

	public void setIdExterne(String idExterne) {
		this.idExterne = idExterne;
	}

	public Integer getIdStripeCustomer() {
		return idStripeCustomer;
	}

	public void setIdStripeCustomer(Integer idStripeCustomer) {
		this.idStripeCustomer = idStripeCustomer;
	}

	public String getIdExterneCustomer() {
		return idExterneCustomer;
	}

	public void setIdExterneCustomer(String idExterneCustomer) {
		this.idExterneCustomer = idExterneCustomer;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Boolean getProrata() {
		return prorata;
	}

	public void setProrata(Boolean prorata) {
		this.prorata = prorata;
	}

	public Date getDateDebut() {
		return dateDebut;
	}

	public void setDateDebut(Date dateDebut) {
		this.dateDebut = dateDebut;
	}

	public Date getDateFin() {
		return dateFin;
	}

	public void setDateFin(Date dateFin) {
		this.dateFin = dateFin;
	}

	public Date getBillingCycleAnchor() {
		return billingCycleAnchor;
	}

	public void setBillingCycleAnchor(Date billingCycleAnchor) {
		this.billingCycleAnchor = billingCycleAnchor;
	}

	public BigDecimal getTaxe() {
		return taxe;
	}

	public void setTaxe(BigDecimal taxe) {
		this.taxe = taxe;
	}

	public Integer getTrial() {
		return trial;
	}

	public void setTrial(Integer trial) {
		this.trial = trial;
	}

	public Integer getNbEcheance() {
		return nbEcheance;
	}

	public void setNbEcheance(Integer nbEcheance) {
		this.nbEcheance = nbEcheance;
	}

	public Integer getIdStripeCoupon() {
		return idStripeCoupon;
	}

	public void setIdStripeCoupon(Integer idStripeCoupon) {
		this.idStripeCoupon = idStripeCoupon;
	}

	public String getIdExterneCoupon() {
		return idExterneCoupon;
	}

	public void setIdExterneCoupon(String idExterneCoupon) {
		this.idExterneCoupon = idExterneCoupon;
	}

	public Integer getIdStripeSource() {
		return idStripeSource;
	}

	public void setIdStripeSource(Integer idStripeSource) {
		this.idStripeSource = idStripeSource;
	}

	public String getIdExterneSource() {
		return idExterneSource;
	}

	public void setIdExterneSource(String idExterneSource) {
		this.idExterneSource = idExterneSource;
	}

	public String getBilling() {
		return billing;
	}

	public void setBilling(String billing) {
		this.billing = billing;
	}

	public Integer getDaysUntilDue() {
		return daysUntilDue;
	}

	public void setDaysUntilDue(Integer daysUntilDue) {
		this.daysUntilDue = daysUntilDue;
	}

	public Date getDateAnnulation() {
		return dateAnnulation;
	}

	public void setDateAnnulation(Date dateAnnulation) {
		this.dateAnnulation = dateAnnulation;
	}

	public String getCommentaireAnnulation() {
		return commentaireAnnulation;
	}

	public void setCommentaireAnnulation(String commentaireAnnulation) {
		this.commentaireAnnulation = commentaireAnnulation;
	}

	public String getCodeDocument() {
		return codeDocument;
	}

	public void setCodeDocument(String codeDocument) {
		this.codeDocument = codeDocument;
	}

	public String getCodeTiers() {
		return codeTiers;
	}

	public void setCodeTiers(String codeTiers) {
		this.codeTiers = codeTiers;
	}

	public String getNomTiers() {
		return nomTiers;
	}

	public void setNomTiers(String nomTiers) {
		this.nomTiers = nomTiers;
	}

	public String getPrenomTiers() {
		return prenomTiers;
	}

	public void setPrenomTiers(String prenomTiers) {
		this.prenomTiers = prenomTiers;
	}

	public String getNomEntreprise() {
		return nomEntreprise;
	}

	public void setNomEntreprise(String nomEntreprise) {
		this.nomEntreprise = nomEntreprise;
	}

	public Date getDateSuspension() {
		return dateSuspension;
	}

	public void setDateSuspension(Date dateSuspension) {
		this.dateSuspension = dateSuspension;
	}

	public Boolean getSuspension() {
		return suspension;
	}

	public void setSuspension(Boolean suspension) {
		this.suspension = suspension;
	}

	public Integer getIdFrequence() {
		return idFrequence;
	}

	public void setIdFrequence(Integer idFrequence) {
		this.idFrequence = idFrequence;
	}

	public String getCodeFrequence() {
		return codeFrequence;
	}

	public void setCodeFrequence(String codeFrequence) {
		this.codeFrequence = codeFrequence;
	}

	public String getLiblFrequence() {
		return liblFrequence;
	}

	public void setLiblFrequence(String liblFrequence) {
		this.liblFrequence = liblFrequence;
	}

	public Integer getIdTiers() {
		return idTiers;
	}

	public void setIdTiers(Integer idTiers) {
		this.idTiers = idTiers;
	}

	public Integer getIdPlan() {
		return idPlan;
	}

	public void setIdPlan(Integer idPlan) {
		this.idPlan = idPlan;
	}

	public Integer getIdArticle() {
		return idArticle;
	}

	public void setIdArticle(Integer idArticle) {
		this.idArticle = idArticle;
	}

	public String getCodeArticle() {
		return codeArticle;
	}

	public void setCodeArticle(String codeArticle) {
		this.codeArticle = codeArticle;
	}

	public String getLiblArticle() {
		return liblArticle;
	}

	public void setLiblArticle(String liblArticle) {
		this.liblArticle = liblArticle;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getCodeTLigne() {
		return codeTLigne;
	}

	public void setCodeTLigne(String codeTLigne) {
		this.codeTLigne = codeTLigne;
	}

	public Integer getNumLigneLDocument() {
		return numLigneLDocument;
	}

	public void setNumLigneLDocument(Integer numLigneLDocument) {
		this.numLigneLDocument = numLigneLDocument;
	}

	public String getLibLDocument() {
		return libLDocument;
	}

	public void setLibLDocument(String libLDocument) {
		this.libLDocument = libLDocument;
	}

	public BigDecimal getQteLDocument() {
		return qteLDocument;
	}

	public void setQteLDocument(BigDecimal qteLDocument) {
		this.qteLDocument = qteLDocument;
	}

	public BigDecimal getQte2LDocument() {
		return qte2LDocument;
	}

	public void setQte2LDocument(BigDecimal qte2lDocument) {
		qte2LDocument = qte2lDocument;
	}

	public BigDecimal getPrixULDocument() {
		return prixULDocument;
	}

	public void setPrixULDocument(BigDecimal prixULDocument) {
		this.prixULDocument = prixULDocument;
	}

	public BigDecimal getTauxTvaLDocument() {
		return tauxTvaLDocument;
	}

	public void setTauxTvaLDocument(BigDecimal tauxTvaLDocument) {
		this.tauxTvaLDocument = tauxTvaLDocument;
	}

	public String getCodeTvaLDocument() {
		return codeTvaLDocument;
	}

	public void setCodeTvaLDocument(String codeTvaLDocument) {
		this.codeTvaLDocument = codeTvaLDocument;
	}

	public String getCodeTTvaLDocument() {
		return codeTTvaLDocument;
	}

	public void setCodeTTvaLDocument(String codeTTvaLDocument) {
		this.codeTTvaLDocument = codeTTvaLDocument;
	}

	public BigDecimal getMtHtLDocument() {
		return mtHtLDocument;
	}

	public void setMtHtLDocument(BigDecimal mtHtLDocument) {
		this.mtHtLDocument = mtHtLDocument;
	}

	public BigDecimal getRemTxLDocument() {
		return remTxLDocument;
	}

	public void setRemTxLDocument(BigDecimal remTxLDocument) {
		this.remTxLDocument = remTxLDocument;
	}

	public BigDecimal getMtTtcLDocument() {
		return mtTtcLDocument;
	}

	public void setMtTtcLDocument(BigDecimal mtTtcLDocument) {
		this.mtTtcLDocument = mtTtcLDocument;
	}

	public BigDecimal getRemHtLDocument() {
		return remHtLDocument;
	}

	public void setRemHtLDocument(BigDecimal remHtLDocument) {
		this.remHtLDocument = remHtLDocument;
	}

	public BigDecimal getMtHtLApresRemiseGlobaleDocument() {
		return mtHtLApresRemiseGlobaleDocument;
	}

	public void setMtHtLApresRemiseGlobaleDocument(BigDecimal mtHtLApresRemiseGlobaleDocument) {
		this.mtHtLApresRemiseGlobaleDocument = mtHtLApresRemiseGlobaleDocument;
	}

	public BigDecimal getMtTtcLApresRemiseGlobaleDocument() {
		return mtTtcLApresRemiseGlobaleDocument;
	}

	public void setMtTtcLApresRemiseGlobaleDocument(BigDecimal mtTtcLApresRemiseGlobaleDocument) {
		this.mtTtcLApresRemiseGlobaleDocument = mtTtcLApresRemiseGlobaleDocument;
	}

	public String getU1LDocument() {
		return u1LDocument;
	}

	public void setU1LDocument(String u1lDocument) {
		u1LDocument = u1lDocument;
	}

	public String getU2LDocument() {
		return u2LDocument;
	}

	public void setU2LDocument(String u2lDocument) {
		u2LDocument = u2lDocument;
	}

	public BigDecimal getTxRemHtDocument() {
		return txRemHtDocument;
	}

	public void setTxRemHtDocument(BigDecimal txRemHtDocument) {
		this.txRemHtDocument = txRemHtDocument;
	}

	public String getComplement1() {
		return complement1;
	}

	public void setComplement1(String complement1) {
		this.complement1 = complement1;
	}

	public String getComplement2() {
		return complement2;
	}

	public void setComplement2(String complement2) {
		this.complement2 = complement2;
	}

	public String getComplement3() {
		return complement3;
	}

	public void setComplement3(String complement3) {
		this.complement3 = complement3;
	}

	public String getIdentifiantEtat() {
		return identifiantEtat;
	}

	public void setIdentifiantEtat(String identifiantEtat) {
		this.identifiantEtat = identifiantEtat;
	}

}
