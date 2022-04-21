package fr.legrain.abonnement.stripe.dto;

import java.util.Date;

import fr.legrain.bdg.model.ModelObject;

public class TaStripeDiscountDTO extends ModelObject implements java.io.Serializable {

	private static final long serialVersionUID = 4874497258621637052L;
	
	private Integer id;
	private String idExterne;
	private int idStripeCoupon;
	private String idExterneCoupon;
	private int idStripeCustomer;
	private String idExterneCustomer;
	private int idAbonnement;
	private String idExterneSubscription;
	private Date end;
	private Date start;
	
	private Integer versionObj;
	
	public TaStripeDiscountDTO() {
	}
	
	public static TaStripeDiscountDTO copy(TaStripeDiscountDTO taEntrepot){
		TaStripeDiscountDTO taEntrepotLoc = new TaStripeDiscountDTO();
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

	public int getIdAbonnement() {
		return idAbonnement;
	}

	public void setIdAbonnement(int idAbonnement) {
		this.idAbonnement = idAbonnement;
	}

	public String getIdExterneSubscription() {
		return idExterneSubscription;
	}

	public void setIdExterneSubscription(String idExterneSubscription) {
		this.idExterneSubscription = idExterneSubscription;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

}
