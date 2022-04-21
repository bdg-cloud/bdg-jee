package fr.legrain.abonnement.stripe.dto;

import fr.legrain.bdg.model.ModelObject;

public class TaStripeCustomerDTO extends ModelObject implements java.io.Serializable {

	private static final long serialVersionUID = 4874497258621637052L;
	
	private Integer id;
	
	private String idExterne;
	
	private int idTiers;
	private String codeTiers;
	private String currency;
	
	private int idStripeDiscount;
	private String idExterneDiscount;
	private int idStripeSourceDefault;
	private String idExterneSourceDefault;
	private String description;
	private String email;

	
	private Integer versionObj;
	
	public TaStripeCustomerDTO() {
	}
	

	
	public static TaStripeCustomerDTO copy(TaStripeCustomerDTO taEntrepot){
		TaStripeCustomerDTO taEntrepotLoc = new TaStripeCustomerDTO();
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



	public int getIdTiers() {
		return idTiers;
	}



	public void setIdTiers(int idTiers) {
		this.idTiers = idTiers;
	}



	public String getCodeTiers() {
		return codeTiers;
	}



	public void setCodeTiers(String codeTiers) {
		this.codeTiers = codeTiers;
	}



	public String getCurrency() {
		return currency;
	}



	public void setCurrency(String currency) {
		this.currency = currency;
	}



	public int getIdStripeDiscount() {
		return idStripeDiscount;
	}



	public void setIdStripeDiscount(int idStripeDiscount) {
		this.idStripeDiscount = idStripeDiscount;
	}



	public String getIdExterneDiscount() {
		return idExterneDiscount;
	}



	public void setIdExterneDiscount(String idExterneDiscount) {
		this.idExterneDiscount = idExterneDiscount;
	}



	public int getIdStripeSourceDefault() {
		return idStripeSourceDefault;
	}



	public void setIdStripeSourceDefault(int idStripeSourceDefault) {
		this.idStripeSourceDefault = idStripeSourceDefault;
	}



	public String getIdExterneSourceDefault() {
		return idExterneSourceDefault;
	}



	public void setIdExterneSourceDefault(String idExterneSourceDefault) {
		this.idExterneSourceDefault = idExterneSourceDefault;
	}



	public String getDescription() {
		return description;
	}



	public void setDescription(String description) {
		this.description = description;
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}


}
