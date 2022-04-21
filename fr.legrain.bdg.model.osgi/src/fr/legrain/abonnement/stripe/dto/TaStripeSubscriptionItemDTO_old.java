package fr.legrain.abonnement.stripe.dto;

import java.math.BigDecimal;

import fr.legrain.bdg.model.ModelObject;

public class TaStripeSubscriptionItemDTO_old extends ModelObject implements java.io.Serializable {

	private static final long serialVersionUID = 4874497258621637052L;
	
	private Integer id;
	private String idExterne;
	private int idStripePlan;
	private String idExternePlan;
	private Integer quantity;
	private int idStripeSubscription;
	private String idExterneSubscription;
	private String complement1;
	private String complement2;
	private String complement3;
	
	private String nickname;
	private String interval; //day, week, month or year
	private String currency;
	private Boolean active;
	private BigDecimal amount;
	private String codeArticle;
	private String libellecArticle;
	
	private Integer numLigneLDocument;
	
	private Integer versionObj;
	
	
	
	public TaStripeSubscriptionItemDTO_old() {
	}
	
	
	
	public TaStripeSubscriptionItemDTO_old(Integer id, String idExterne, int idStripePlan, String idExternePlan,
			Integer quantity, int idStripeSubscription, String idExterneSubscription, String nickname, String interval,
			String currency, Boolean active, BigDecimal amount, String codeArticle, String libellecArticle,
			String complement1, String complement2, String complement3, Integer versionObj) {
		super();
		this.id = id;
		this.idExterne = idExterne;
		this.idStripePlan = idStripePlan;
		this.idExternePlan = idExternePlan;
		this.quantity = quantity;
		this.idStripeSubscription = idStripeSubscription;
		this.idExterneSubscription = idExterneSubscription;
		this.nickname = nickname;
		this.interval = interval;
		this.currency = currency;
		this.active = active;
		this.amount = amount;
		this.codeArticle = codeArticle;
		this.libellecArticle = libellecArticle;
		this.complement1 = complement1;
		this.complement2 = complement2;
		this.complement3 = complement3;
		this.versionObj = versionObj;
	}



//	public static TaStripeSubscriptionItemDTO copy(TaStripeSubscriptionItemDTO taEntrepot){
//		TaStripeSubscriptionItemDTO taEntrepotLoc = new TaStripeSubscriptionItemDTO();
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

	public int getIdStripePlan() {
		return idStripePlan;
	}

	public void setIdStripePlan(int idStripePlan) {
		this.idStripePlan = idStripePlan;
	}

	public String getIdExternePlan() {
		return idExternePlan;
	}

	public void setIdExternePlan(String idExternePlan) {
		this.idExternePlan = idExternePlan;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public int getIdStripeSubscription() {
		return idStripeSubscription;
	}

	public void setIdStripeSubscription(int idStripeSubscription) {
		this.idStripeSubscription = idStripeSubscription;
	}

	public String getIdExterneSubscription() {
		return idExterneSubscription;
	}

	public void setIdExterneSubscription(String idExterneSubscription) {
		this.idExterneSubscription = idExterneSubscription;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getInterval() {
		return interval;
	}

	public void setInterval(String interval) {
		this.interval = interval;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getCodeArticle() {
		return codeArticle;
	}

	public void setCodeArticle(String codeArticle) {
		this.codeArticle = codeArticle;
	}

	public String getLibellecArticle() {
		return libellecArticle;
	}

	public void setLibellecArticle(String libellecArticle) {
		this.libellecArticle = libellecArticle;
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



	public Integer getNumLigneLDocument() {
		return numLigneLDocument;
	}



	public void setNumLigneLDocument(Integer numLigneLDocument) {
		this.numLigneLDocument = numLigneLDocument;
	}

}
