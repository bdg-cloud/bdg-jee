package fr.legrain.abonnement.stripe.dto;

import java.math.BigDecimal;
import java.util.Date;

import fr.legrain.bdg.model.ModelObject;

public class TaStripePaiementPrevuDTO extends ModelObject implements java.io.Serializable {

	private static final long serialVersionUID = 3460877634821485974L;
	
	private Integer id;
	private Integer idStripeSource;
	private String idExterneSource;
	
	private Integer idStripeCharge;
	private String idExterneCharge;
	private String status;
	private Boolean paid = false;
	
	private Integer idAvisEcheance;
	private String codeAvisEcheance;
	
	private Integer idReglement;
	private String codeReglement;
	
	private Integer idStripeSubscription;
	private String idExterneSubscription;
	private String codeDocument;
	private Integer idStripeInvoice;
	private String idExterneInvoice;
	private Integer idStripeCustomer;
	private String idExterneCustomer;
	private String codeTiers;
	private String nomTiers;

	private Boolean annule = false;
	private Boolean capture = true;
	
	private Date dateAnnulation;
	private String commentaireAnnulation;
	
	private BigDecimal montant = new BigDecimal(0);
	private Date dateDeclenchement = new Date();
	private String raisonAnnulation;
	private String raisonPaiement;
	private String commentaire;
	
	private Integer versionObj;
	
	public TaStripePaiementPrevuDTO() {
	}
	
	
	
	public TaStripePaiementPrevuDTO(Integer id, Integer idStripeSource, String idExterneSource, Integer idStripeCharge,
			String idExterneCharge, String status, Boolean paid, Integer idReglement, String codeReglement, 
			Integer idStripeSubscription, String idExterneSubscription, String codeDocument, Integer idAvisEcheance, String codeAvisEcheance, Integer idStripeInvoice,
			String idExterneInvoice, Integer idStripeCustomer, String idExterneCustomer, String codeTiers, String nomTiers, Boolean annule,
			Boolean capture, BigDecimal montant, Date dateDeclenchement, String raisonAnnulation, String raisonPaiement,
			String commentaire, Integer versionObj) {
		super();
		this.id = id;
		this.idStripeSource = idStripeSource;
		this.idExterneSource = idExterneSource;
		this.idStripeCharge = idStripeCharge;
		this.idExterneCharge = idExterneCharge;
		this.status = status;
		this.paid = paid;
		this.idReglement = idReglement;
		this.codeReglement = codeReglement;
		this.idStripeSubscription = idStripeSubscription;
		this.idExterneSubscription = idExterneSubscription;
		this.codeDocument = codeDocument;
		this.idAvisEcheance = idAvisEcheance;
		this.codeAvisEcheance = codeAvisEcheance;
		this.idStripeInvoice = idStripeInvoice;
		this.idExterneInvoice = idExterneInvoice;
		this.idStripeCustomer = idStripeCustomer;
		this.idExterneCustomer = idExterneCustomer;
		this.codeTiers = codeTiers;
		this.nomTiers = nomTiers;
		this.annule = annule;
		this.capture = capture;
		this.montant = montant;
		this.dateDeclenchement = dateDeclenchement;
		this.raisonAnnulation = raisonAnnulation;
		this.raisonPaiement = raisonPaiement;
		this.commentaire = commentaire;
		this.versionObj = versionObj;
	}



	public static TaStripePaiementPrevuDTO copy(TaStripePaiementPrevuDTO taEntrepot){
		TaStripePaiementPrevuDTO taEntrepotLoc = new TaStripePaiementPrevuDTO();
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


	public Integer getIdStripeCharge() {
		return idStripeCharge;
	}


	public void setIdStripeCharge(Integer idStripeCharge) {
		this.idStripeCharge = idStripeCharge;
	}


	public String getIdExterneCharge() {
		return idExterneCharge;
	}


	public void setIdExterneCharge(String idExterneCharge) {
		this.idExterneCharge = idExterneCharge;
	}


	public Integer getIdStripeSubscription() {
		return idStripeSubscription;
	}


	public void setIdStripeSubscription(Integer idStripeSubscription) {
		this.idStripeSubscription = idStripeSubscription;
	}


	public String getIdExterneSubscription() {
		return idExterneSubscription;
	}


	public void setIdExterneSubscription(String idExterneSubscription) {
		this.idExterneSubscription = idExterneSubscription;
	}


	public Integer getIdStripeInvoice() {
		return idStripeInvoice;
	}


	public void setIdStripeInvoice(Integer idStripeInvoice) {
		this.idStripeInvoice = idStripeInvoice;
	}


	public String getIdExterneInvoice() {
		return idExterneInvoice;
	}


	public void setIdExterneInvoice(String idExterneInvoice) {
		this.idExterneInvoice = idExterneInvoice;
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


	public Boolean getAnnule() {
		return annule;
	}


	public void setAnnule(Boolean annule) {
		this.annule = annule;
	}


	public Boolean getCapture() {
		return capture;
	}


	public void setCapture(Boolean capture) {
		this.capture = capture;
	}


	public BigDecimal getMontant() {
		return montant;
	}


	public void setMontant(BigDecimal montant) {
		this.montant = montant;
	}


	public Date getDateDeclenchement() {
		return dateDeclenchement;
	}


	public void setDateDeclenchement(Date dateDeclenchement) {
		this.dateDeclenchement = dateDeclenchement;
	}


	public String getRaisonAnnulation() {
		return raisonAnnulation;
	}


	public void setRaisonAnnulation(String raisonAnnulation) {
		this.raisonAnnulation = raisonAnnulation;
	}


	public String getRaisonPaiement() {
		return raisonPaiement;
	}


	public void setRaisonPaiement(String raisonPaiement) {
		this.raisonPaiement = raisonPaiement;
	}


	public String getCommentaire() {
		return commentaire;
	}


	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}



	public String getStatus() {
		return status;
	}



	public void setStatus(String status) {
		this.status = status;
	}



	public Boolean getPaid() {
		return paid;
	}



	public void setPaid(Boolean paid) {
		this.paid = paid;
	}



	public Integer getIdReglement() {
		return idReglement;
	}



	public void setIdReglement(Integer idReglement) {
		this.idReglement = idReglement;
	}



	public String getCodeReglement() {
		return codeReglement;
	}



	public void setCodeReglement(String codeReglement) {
		this.codeReglement = codeReglement;
	}



	public Integer getIdAvisEcheance() {
		return idAvisEcheance;
	}



	public void setIdAvisEcheance(Integer idAvisEcheance) {
		this.idAvisEcheance = idAvisEcheance;
	}



	public String getCodeAvisEcheance() {
		return codeAvisEcheance;
	}



	public void setCodeAvisEcheance(String codeAvisEcheance) {
		this.codeAvisEcheance = codeAvisEcheance;
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


}
