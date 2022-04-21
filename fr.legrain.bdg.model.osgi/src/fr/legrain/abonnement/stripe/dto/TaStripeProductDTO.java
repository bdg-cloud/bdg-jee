package fr.legrain.abonnement.stripe.dto;

import fr.legrain.bdg.model.ModelObject;

public class TaStripeProductDTO extends ModelObject implements java.io.Serializable {

	private static final long serialVersionUID = 4874497258621637052L;
	
	private Integer id;
	private String idExterne;
	private Integer idArticle;
	private String codeArticle;
	private String name;
	private String productType;//service or good
	private Boolean active;
	private String caption;
	private String description;
	
	private Integer versionObj;
	
	public TaStripeProductDTO() {
	}
	
	public TaStripeProductDTO(Integer id,String code ,String libelle) {
		this.id = id;

	}
	public TaStripeProductDTO(Integer id,Boolean active, String caption, String description, String idExterne, String productType, String codeArticle, Integer idArticle, String name) {
		this.id = id;
		this.active = active;
		this.caption = caption;
		this.description = description;
		this.idExterne = idExterne;
		this.productType = productType;
		this.codeArticle = codeArticle;
		this.idArticle = idArticle;
		this.name = name;

	}
	
	public static TaStripeProductDTO copy(TaStripeProductDTO taEntrepot){
		TaStripeProductDTO taEntrepotLoc = new TaStripeProductDTO();
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

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
