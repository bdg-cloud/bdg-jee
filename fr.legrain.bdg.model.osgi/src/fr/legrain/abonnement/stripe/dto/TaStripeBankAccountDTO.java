package fr.legrain.abonnement.stripe.dto;

import fr.legrain.bdg.model.ModelObject;

public class TaStripeBankAccountDTO extends ModelObject implements java.io.Serializable {

	private static final long serialVersionUID = 4874497258621637052L;
	
	private Integer id;
	private String idExterne;
	private int idCompteBanque;
	
	private Integer versionObj;
	
	public TaStripeBankAccountDTO() {
	}
	
	public static TaStripeBankAccountDTO copy(TaStripeBankAccountDTO taEntrepot){
		TaStripeBankAccountDTO taEntrepotLoc = new TaStripeBankAccountDTO();
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

	public int getIdCompteBanque() {
		return idCompteBanque;
	}

	public void setIdCompteBanque(int idCompteBanque) {
		this.idCompteBanque = idCompteBanque;
	}

}
