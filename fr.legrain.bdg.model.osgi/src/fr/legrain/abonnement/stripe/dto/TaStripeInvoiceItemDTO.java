package fr.legrain.abonnement.stripe.dto;

import fr.legrain.bdg.model.ModelObject;

public class TaStripeInvoiceItemDTO extends ModelObject implements java.io.Serializable {

	private static final long serialVersionUID = 4874497258621637052L;
	
	private Integer id;
	private String idExterne;
	
	private int idStripeInvoice;
	private String idExterneInvoice;
	
	private Integer versionObj;
	
	public TaStripeInvoiceItemDTO() {
	}
	
	
	public TaStripeInvoiceItemDTO(Integer id, String idExterne, int idStripeInvoice, String idExterneInvoice,
			Integer versionObj) {
		super();
		this.id = id;
		this.idExterne = idExterne;
		this.idStripeInvoice = idStripeInvoice;
		this.idExterneInvoice = idExterneInvoice;
		this.versionObj = versionObj;
	}


	public static TaStripeInvoiceItemDTO copy(TaStripeInvoiceItemDTO taEntrepot){
		TaStripeInvoiceItemDTO taEntrepotLoc = new TaStripeInvoiceItemDTO();
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

	public int getIdStripeInvoice() {
		return idStripeInvoice;
	}

	public void setIdStripeInvoice(int idStripeInvoice) {
		this.idStripeInvoice = idStripeInvoice;
	}

	public String getIdExterneInvoice() {
		return idExterneInvoice;
	}

	public void setIdExterneInvoice(String idExterneInvoice) {
		this.idExterneInvoice = idExterneInvoice;
	}
}
