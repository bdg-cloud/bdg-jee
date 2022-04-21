package fr.legrain.abonnement.stripe.dto;

import fr.legrain.bdg.model.ModelObject;

public class TaStripeAccountDTO extends ModelObject implements java.io.Serializable {
	
	private static final long serialVersionUID = -8593176978447168654L;
	
	private Integer id;
	private String idExterne;
	
	private String businessType;
	private String country;
	private String email;
	private String type;
	private String defaultCurrency;
	private Boolean detailsSubmitted;
	private Boolean chargesEnabled;
	
	private Integer versionObj;
	
	public TaStripeAccountDTO() {
	}
	
	public static TaStripeAccountDTO copy(TaStripeAccountDTO taEntrepot){
		TaStripeAccountDTO taEntrepotLoc = new TaStripeAccountDTO();
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

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDefaultCurrency() {
		return defaultCurrency;
	}

	public void setDefaultCurrency(String defaultCurrency) {
		this.defaultCurrency = defaultCurrency;
	}

	public Boolean getDetailsSubmitted() {
		return detailsSubmitted;
	}

	public void setDetailsSubmitted(Boolean detailsSubmitted) {
		this.detailsSubmitted = detailsSubmitted;
	}

	public Boolean getChargesEnabled() {
		return chargesEnabled;
	}

	public void setChargesEnabled(Boolean chargesEnabled) {
		this.chargesEnabled = chargesEnabled;
	}

	

}
