package fr.legrain.abonnement.stripe.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import fr.legrain.article.model.TaArticle;
import fr.legrain.validator.LgrHibernateValidated;

//https://stripe.com/docs/api/service_products?lang=java

@Entity
@Table(name = "ta_stripe_product")
public class TaStripeProduct implements java.io.Serializable {

	private static final long serialVersionUID = 6982665480850078407L;
	
/*
 com.stripe.model.Product JSON: {
  "id": "prod_E10EyEBcrC2d8J",
  "object": "product",
  "active": true,
  "attributes": [

  ],
  "caption": null,
  "created": 1542816434,
  "deactivate_on": [

  ],
  "description": null,
  "images": [

  ],
  "livemode": false,
  "metadata": {
  },
  "name": "neon-insurance-package",
  "package_dimensions": null,
  "shippable": null,
  "statement_descriptor": null,
  "type": "service",
  "unit_label": null,
  "updated": 1542816434,
  "url": null
}
 */

	private int idStripeProduct;
	private String idExterne;
	private TaArticle taArticle;
	private String productType;//service or good
	private Boolean active;
	private String caption;
	private String description;
	
	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private String ipAcces;
	private Integer versionObj;
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_stripe_product", unique = true, nullable = false)
	@LgrHibernateValidated(champBd = "id_stripe_product",table = "ta_stripe_product",champEntite="idStripeProduct", clazz = TaStripeProduct.class)
	public int getIdStripeProduct() {
		return idStripeProduct;
	}

	public void setIdStripeProduct(int idProduct) {
		this.idStripeProduct = idProduct;
	}
	
	@Column(name = "id_externe")
	public String getIdExterne() {
		return idExterne;
	}

	public void setIdExterne(String idExterne) {
		this.idExterne = idExterne;
	}

	@OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY /*, mappedBy = "taDocument", orphanRemoval=true*/)
	@JoinColumn(name = "id_article")
	public TaArticle getTaArticle() {
		return taArticle;
	}

	public void setTaArticle(TaArticle taArticle) {
		this.taArticle = taArticle;
	}

	@Column(name = "product_type")
	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	@Column(name = "active")
	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	@Column(name = "caption")
	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	@Column(name = "description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Column(name = "qui_cree", length = 50)
	public String getQuiCree() {
		return this.quiCree;
	}

	public void setQuiCree(String quiCreeCompteBanque) {
		this.quiCree = quiCreeCompteBanque;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "quand_cree", length = 19)
	public Date getQuandCree() {
		return this.quandCree;
	}

	public void setQuandCree(Date quandCreeCompteBanque) {
		this.quandCree = quandCreeCompteBanque;
	}

	@Column(name = "qui_modif", length = 50)
	public String getQuiModif() {
		return this.quiModif;
	}

	public void setQuiModif(String quiModifCompteBanque) {
		this.quiModif = quiModifCompteBanque;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "quand_modif", length = 19)
	public Date getQuandModif() {
		return this.quandModif;
	}

	public void setQuandModif(Date quandModifCompteBanque) {
		this.quandModif = quandModifCompteBanque;
	}

	@Column(name = "ip_acces", length = 50)
	public String getIpAcces() {
		return this.ipAcces;
	}

	public void setIpAcces(String ipAcces) {
		this.ipAcces = ipAcces;
	}

	@Version
	@Column(name = "version_obj")
	public Integer getVersionObj() {
		return this.versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

}
