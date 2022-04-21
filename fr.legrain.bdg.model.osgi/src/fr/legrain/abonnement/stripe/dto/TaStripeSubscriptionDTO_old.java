package fr.legrain.abonnement.stripe.dto;

import java.util.Date;

import com.ibm.icu.math.BigDecimal;

import fr.legrain.abonnement.stripe.model.TaStripeCoupon;
import fr.legrain.bdg.model.ModelObject;
import fr.legrain.lib.data.LgrConstantes;
import fr.legrain.lib.data.LibDate;

public class TaStripeSubscriptionDTO_old extends ModelObject implements java.io.Serializable {

	private static final long serialVersionUID = 4874497258621637052L;
	
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
//	private Date dateFin = new Date();
	private Date dateFin = null;
	private Date billingCycleAnchor;
	
	private Date dateAnnulation;
	private String commentaireAnnulation;
	
	private BigDecimal taxe;
	private Integer trial;
	private int idStripeCoupon;
	private String idExterneCoupon;
	private Integer nbEcheance;
	private Integer daysUntilDue;
	
	private Integer versionObj;
	
	public TaStripeSubscriptionDTO_old() {
	}
	
	public boolean abonnementEstVide(){
//		Date dateExemple = new Date();
////		if(!getCODE_DEVIS().equals(LgrConstantes.C_STR_VIDE))return true;
//		if(!(LibDate.compareTo(getDateDocument(), dateExemple)==0))return false;
//		if(!(LibDate.compareTo(getDateEchDocument(), dateExemple)==0))return false;
//		if(!(LibDate.compareTo(getDateLivDocument(), dateExemple)==0))return false;
//		
//		if(getLibelleDocument()!=null && !getLibelleDocument().equals(LgrConstantes.C_STR_VIDE))return false;
//
//		if(!getIdTiers().equals(0))return false;
//		if(getCodeTiers()!=null && !getCodeTiers().equals(LgrConstantes.C_STR_VIDE))return false;
//		if(getNomTiers()!=null && !getNomTiers().equals(LgrConstantes.C_STR_VIDE))return false;
//		if(getPrenomTiers()!=null && !getPrenomTiers().equals(LgrConstantes.C_STR_VIDE))return false;
//		if(getSurnomTiers()!=null && !getSurnomTiers().equals(LgrConstantes.C_STR_VIDE))return false;
//		if(getCodeCompta()!=null && !getCodeCompta().equals(LgrConstantes.C_STR_VIDE))return false;
//		if(getCompte()!=null && !getCompte().equals(LgrConstantes.C_STR_VIDE))return false;
//		return true;
		
		return false;
	}
	
	public TaStripeSubscriptionDTO_old(Integer id, String idExterne, String codeDocument, Integer idStripeCustomer, String idExterneCustomer,
			String codeTiers, String nomTiers, Integer quantity, String billing, String status, Date dateDebut, Date dateFin, Date dateAnnulation,
			String commentaireAnnulation, Integer nbEcheance, Integer versionObj) {
		super();
		this.id = id;
		this.idExterne = idExterne;
		this.codeDocument = codeDocument;
	    this.idStripeCustomer = idStripeCustomer;
		this.idExterneCustomer = idExterneCustomer;
		this.codeTiers = codeTiers;
		this.nomTiers = nomTiers;
		this.quantity = quantity;
		this.billing = billing;
		this.status = status;
		this.dateDebut = dateDebut;
		this.dateFin = dateFin;
		this.dateAnnulation = dateAnnulation;
		this.commentaireAnnulation = commentaireAnnulation;
		this.nbEcheance = nbEcheance;
		this.versionObj = versionObj;
	}
	
//	public static TaStripeSubscriptionDTO copy(TaStripeSubscriptionDTO taEntrepot){
//		TaStripeSubscriptionDTO taEntrepotLoc = new TaStripeSubscriptionDTO();
//		taEntrepotLoc.setId(taEntrepot.id);
//		
//		return taEntrepotLoc;
//	}
	
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

	public int getIdStripeCustomer() {
		return idStripeCustomer;
	}

	public void setIdStripeCustomer(int idStripeCustomer) {
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

	public int getIdStripeCoupon() {
		return idStripeCoupon;
	}

	public void setIdStripeCoupon(int idStripeCoupon) {
		this.idStripeCoupon = idStripeCoupon;
	}

	public String getIdExterneCoupon() {
		return idExterneCoupon;
	}

	public void setIdExterneCoupon(String idExterneCoupon) {
		this.idExterneCoupon = idExterneCoupon;
	}

	public int getIdStripeSource() {
		return idStripeSource;
	}

	public void setIdStripeSource(int idStripeSource) {
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

}
