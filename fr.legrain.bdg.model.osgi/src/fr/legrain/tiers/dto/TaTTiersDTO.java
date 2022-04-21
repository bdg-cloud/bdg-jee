package fr.legrain.tiers.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import fr.legrain.bdg.model.ModelObject;
import fr.legrain.validator.LgrHibernateValidated;

public class TaTTiersDTO extends ModelObject implements java.io.Serializable {

	private static final long serialVersionUID = -7685347435277014847L;
	private Integer id;
	private String compteTTiers;
	private String codeTTiers;
	private String libelleTTiers;
	private Integer versionObj;

	@NotNull
	@Size(min=1, max=20)
	@LgrHibernateValidated(champBd = "code_t_tiers",table = "ta_t_tiers",champEntite="codeTTiers",clazz = TaTTiersDTO.class)
	public String getCodeTTiers() {
		return codeTTiers;
	}

	public void setCodeTTiers(String codeTTiers) {
		firePropertyChange("codeTTiers", this.codeTTiers, this.codeTTiers = codeTTiers);
	}

	@Size(min=0, max=8)
	@LgrHibernateValidated(champBd = "compte_t_tiers",table = "ta_t_tiers",champEntite="compteTTiers",clazz = TaTTiersDTO.class)
	public String getCompteTTiers() {
		return compteTTiers;
	}

	public void setCompteTTiers(String compteTTiers) {
		firePropertyChange("compteTTiers", this.compteTTiers, this.compteTTiers = compteTTiers);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		firePropertyChange("id", this.id, this.id = id);
	}

	@LgrHibernateValidated(champBd = "libelle_t_tiers",table = "ta_t_tiers",champEntite="libelleTTiers",clazz = TaTTiersDTO.class)
	public String getLibelleTTiers() {
		return libelleTTiers;
	}

	public void setLibelleTTiers(String libelleTTiers) {
		firePropertyChange("libelleTTiers", this.libelleTTiers, this.libelleTTiers = libelleTTiers);
	}
	
	public Integer getVersionObj() {
		return this.versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}
	
	public void setSwtTypeTiers(TaTTiersDTO taTTiersDTO){
		this.setId(taTTiersDTO.getId());                
		this.setCodeTTiers(taTTiersDTO.getCodeTTiers());            
		this.setCompteTTiers(taTTiersDTO.getCompteTTiers());        
		this.setLibelleTTiers(taTTiersDTO.getLibelleTTiers());      
	}
	
	public static TaTTiersDTO copy(TaTTiersDTO taTTiersDTO){
		TaTTiersDTO taTTiersDTOLoc = new TaTTiersDTO();
		taTTiersDTOLoc.setId(taTTiersDTO.getId());                
		taTTiersDTOLoc.setCompteTTiers(taTTiersDTO.getCompteTTiers());        
		taTTiersDTOLoc.setCodeTTiers(taTTiersDTO.getCodeTTiers());            
		taTTiersDTOLoc.setLibelleTTiers(taTTiersDTO.getLibelleTTiers());  
		taTTiersDTOLoc.setVersionObj(taTTiersDTO.versionObj);
		return taTTiersDTOLoc;
	}
	
	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof TaTTiersDTO))
			return false;
		TaTTiersDTO castOther = (TaTTiersDTO) other;

		return ((this.getId() == castOther.getId()) || (this
				.getId() != null
				&& castOther.getId() != null && this.getId()
				.equals(castOther.getId())))
				&& ((this.getCodeTTiers() == castOther.getCodeTTiers()) || (this
						.getCodeTTiers() != null
						&& castOther.getCodeTTiers() != null && this
						.getCodeTTiers().equals(castOther.getCodeTTiers())))
				&& ((this.getLibelleTTiers() == castOther
						.getLibelleTTiers()) || (this.getLibelleTTiers() != null
						&& castOther.getLibelleTTiers() != null && this
						.getLibelleTTiers().equals(
								castOther.getLibelleTTiers())))
				&& ((this.getCompteTTiers() == castOther.getCompteTTiers()) || (this
						.getCompteTTiers() != null
						&& castOther.getCompteTTiers() != null && this
						.getCompteTTiers().equals(
								castOther.getCompteTTiers())));
	}
}
