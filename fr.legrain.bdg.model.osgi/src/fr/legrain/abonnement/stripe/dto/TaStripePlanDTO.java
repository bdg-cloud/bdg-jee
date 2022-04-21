package fr.legrain.abonnement.stripe.dto;

import java.math.BigDecimal;

import fr.legrain.bdg.model.ModelObject;

public class TaStripePlanDTO extends ModelObject implements java.io.Serializable {

	


	private static final long serialVersionUID = 4874497258621637052L;
	
	private Integer id;
	private String idExterne;
	private Integer idArticle;
	private String codeArticle;
	private Integer idStripeProduct;
	private String idExterneProduct;
	private String nickname;
	private String interval; //day, week, month or year
	private Integer intervalCount;
	private BigDecimal amount;
	private String currency;
	private Boolean active;
	private Integer trialPrediodDays;
	
	private Integer idFrequence;
	private String codeFrequence;
	private String liblFrequence;
	
	private Integer versionObj;
	
	private Integer nbMois;
	
	public TaStripePlanDTO() {
	}
	
	
	
	public TaStripePlanDTO(Integer id, String idExterne, Integer idArticle, String codeArticle, Integer idStripeProduct,
			String idExterneProduct, String nickname, String interval, Integer intervalCount, BigDecimal amount,
			String currency, Boolean active, Integer trialPrediodDays, Integer versionObj) {
		super();
		this.id = id;
		this.idExterne = idExterne;
		this.idArticle = idArticle;
		this.codeArticle = codeArticle;
		this.idStripeProduct = idStripeProduct;
		this.idExterneProduct = idExterneProduct;
		this.nickname = nickname;
		this.interval = interval;
		this.intervalCount = intervalCount;
		this.amount = amount;
		this.currency = currency;
		this.active = active;
		this.trialPrediodDays = trialPrediodDays;
		this.versionObj = versionObj;
	}



	public static TaStripePlanDTO copy(TaStripePlanDTO taEntrepot){
		TaStripePlanDTO taEntrepotLoc = new TaStripePlanDTO();
		taEntrepotLoc.setId(taEntrepot.id);
		
		return taEntrepotLoc;
	}
	
	public TaStripePlanDTO(Integer id, String idExterne, Integer idArticle, String codeArticle, Integer idStripeProduct,
			String idExterneProduct, String nickname, String interval, Integer intervalCount, String currency,
			Boolean active, Integer trialPrediodDays, BigDecimal amount, Integer versionObj) {
		super();
		this.id = id;
		this.idExterne = idExterne;
		this.idArticle = idArticle;
		this.codeArticle = codeArticle;
		this.idStripeProduct = idStripeProduct;
		this.idExterneProduct = idExterneProduct;
		this.nickname = nickname;
		this.interval = interval;
		this.intervalCount = intervalCount;
		this.currency = currency;
		this.active = active;
		this.trialPrediodDays = trialPrediodDays;
		this.amount = amount;
		this.versionObj = versionObj;
	}
	
	public TaStripePlanDTO(Integer id, String idExterne, Integer idArticle, String codeArticle, Integer idStripeProduct,
			String idExterneProduct, String nickname, String interval, Integer intervalCount, String currency,
			Boolean active, Integer trialPrediodDays, BigDecimal amount, Integer versionObj, Integer idFrequence, String codeFrequence, String liblFrequence) {
		super();
		this.id = id;
		this.idExterne = idExterne;
		this.idArticle = idArticle;
		this.codeArticle = codeArticle;
		this.idStripeProduct = idStripeProduct;
		this.idExterneProduct = idExterneProduct;
		this.nickname = nickname;
		this.interval = interval;
		this.intervalCount = intervalCount;
		this.currency = currency;
		this.active = active;
		this.trialPrediodDays = trialPrediodDays;
		this.amount = amount;
		this.versionObj = versionObj;
		this.idFrequence = idFrequence;
		this.codeFrequence = codeFrequence;
		this.liblFrequence = liblFrequence;
	}
	
	public TaStripePlanDTO(Integer id, String idExterne, Integer idArticle, String codeArticle, Integer idStripeProduct,
			String idExterneProduct, String nickname, String interval, Integer intervalCount, String currency,
			Boolean active, Integer trialPrediodDays, BigDecimal amount, Integer versionObj, Integer idFrequence, String codeFrequence, String liblFrequence, Integer nbMois) {
		super();
		this.id = id;
		this.idExterne = idExterne;
		this.idArticle = idArticle;
		this.codeArticle = codeArticle;
		this.idStripeProduct = idStripeProduct;
		this.idExterneProduct = idExterneProduct;
		this.nickname = nickname;
		this.interval = interval;
		this.intervalCount = intervalCount;
		this.currency = currency;
		this.active = active;
		this.trialPrediodDays = trialPrediodDays;
		this.amount = amount;
		this.versionObj = versionObj;
		this.idFrequence = idFrequence;
		this.codeFrequence = codeFrequence;
		this.liblFrequence = liblFrequence;
		this.nbMois = nbMois;
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


	public Integer getIdStripeProduct() {
		return idStripeProduct;
	}


	public void setIdStripeProduct(Integer idStripeProduct) {
		this.idStripeProduct = idStripeProduct;
	}


	public String getIdExterneProduct() {
		return idExterneProduct;
	}


	public void setIdExterneProduct(String idExterneProduct) {
		this.idExterneProduct = idExterneProduct;
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


	public Integer getIntervalCount() {
		return intervalCount;
	}


	public void setIntervalCount(Integer intervalCount) {
		this.intervalCount = intervalCount;
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


	public Integer getTrialPrediodDays() {
		return trialPrediodDays;
	}


	public void setTrialPrediodDays(Integer trialPrediodDays) {
		this.trialPrediodDays = trialPrediodDays;
	}


	public BigDecimal getAmount() {
		return amount;
	}


	public void setAmount(BigDecimal amount) {
		this.amount = amount;
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



	public Integer getNbMois() {
		return nbMois;
	}



	public void setNbMois(Integer nbMois) {
		this.nbMois = nbMois;
	}


}
