package fr.legrain.tiers.dto;

import fr.legrain.bdg.model.ModelObject;
import fr.legrain.validator.LgrHibernateValidated;


public class TaCommercialDTO extends ModelObject implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5659574273051006887L;
	private Integer id; //idRCommercial
	private Integer idTiers;
	private String codeTiers;
	private Integer idTiersCom;
	private String commercial;
	private String nomTiers;
	private String codeTTiers;
	private Boolean defaut;
	
	private Integer versionObj;

	public TaCommercialDTO() {
	}

	public void setSWTCommercial(TaCommercialDTO swtCommercial) {
		this.id = swtCommercial.id;
		this.idTiers = swtCommercial.idTiers;
		this.codeTiers = swtCommercial.codeTiers;
		this.idTiersCom = swtCommercial.idTiersCom;
		this.commercial = swtCommercial.commercial;
		this.nomTiers = swtCommercial.nomTiers;
		this.codeTTiers = swtCommercial.codeTTiers;
		this.defaut = swtCommercial.defaut;
	}

	
	public static TaCommercialDTO copy(TaCommercialDTO swtCommercial){
		TaCommercialDTO swtCommercialLoc = new TaCommercialDTO();
		swtCommercialLoc.setId(swtCommercial.getId());                
		swtCommercialLoc.setIdTiers(swtCommercial.getIdTiers());                
		swtCommercialLoc.setIdTiersCom(swtCommercial.getIdTiersCom());                
		swtCommercialLoc.setCodeTTiers(swtCommercial.getCodeTTiers());                
		swtCommercialLoc.setCodeTiers(swtCommercial.getCodeTiers());                
		swtCommercialLoc.setCommercial(swtCommercial.getCommercial());                
		swtCommercialLoc.setNomTiers(swtCommercial.getNomTiers());
		swtCommercialLoc.setDefaut(swtCommercial.getDefaut()); 
		return swtCommercialLoc;
	}
	
	
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer idRCommercial) {
		firePropertyChange("id", this.id, this.id = idRCommercial);
	}

	public Integer getIdTiers() {
		return this.idTiers;
	}

	public void setIdTiers(Integer idTiers) {
		firePropertyChange("idTiers", this.idTiers, this.idTiers = idTiers);
	}

	@LgrHibernateValidated(champBd = "code_tiers",table = "ta_tiers",champEntite="codeTiers", clazz = TaTiersDTO.class)
	public String getCodeTiers() {
		return this.codeTiers;
	}

	public void setCodeTiers(String codeTiers) {
		firePropertyChange("codeTiers", this.codeTiers, this.codeTiers = codeTiers);
	}

	public Integer getIdTiersCom() {
		return this.idTiersCom;
	}

	public void setIdTiersCom(Integer idTiersCom) {
		firePropertyChange("idTiersCom", this.idTiersCom, this.idTiersCom = idTiersCom);
	}

	@LgrHibernateValidated(champBd = "code_tiers",table = "ta_tiers",champEntite="commercial",clazz = TaTiersDTO.class)
	public String getCommercial() {
		return this.commercial;
	}

	public void setCommercial(String commercial) {
		firePropertyChange("commercial", this.commercial, this.commercial = commercial);
	}

	@LgrHibernateValidated(champBd = "nom_tiers",table = "ta_tiers",champEntite="nomTiers",clazz = TaTiersDTO.class)
	public String getNomTiers() {
		return this.nomTiers;
	}

	public void setNomTiers(String nomTiers) {
		firePropertyChange("nomTiers", this.nomTiers, this.nomTiers = nomTiers);
	}

	@LgrHibernateValidated(champBd = "code_t_tiers",table = "ta_t_tiers",champEntite="codeTTiers",clazz = TaTiersDTO.class)
	public String getCodeTTiers() {
		return this.codeTTiers;
	}

	public void setCodeTTiers(String codeTTiers) {
		firePropertyChange("codeTTiers", this.codeTTiers, this.codeTTiers = codeTTiers);
	}
	
	public Boolean getDefaut() {
		return this.defaut;
	}

	public void setDefaut(Boolean defaut) {
		firePropertyChange("defaut", this.defaut, this.defaut = defaut);
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof TaCommercialDTO))
			return false;
		TaCommercialDTO castOther = (TaCommercialDTO) other;

		return ((this.getId() == castOther.getId()) || (this
				.getId() != null
				&& castOther.getId() != null && this
				.getId().equals(castOther.getId())))
				&& ((this.getIdTiers() == castOther.getIdTiers()) || (this
						.getIdTiers() != null
						&& castOther.getIdTiers() != null && this
						.getIdTiers().equals(castOther.getIdTiers())))
				&& ((this.getCodeTiers() == castOther.getCodeTiers()) || (this
						.getCodeTiers() != null
						&& castOther.getCodeTiers() != null && this
						.getCodeTiers().equals(castOther.getCodeTiers())))
				&& ((this.getIdTiersCom() == castOther.getIdTiersCom()) || (this
						.getIdTiersCom() != null
						&& castOther.getIdTiersCom() != null && this
						.getIdTiersCom().equals(castOther.getIdTiersCom())))
				&& ((this.getCommercial() == castOther.getCommercial()) || (this
						.getCommercial() != null
						&& castOther.getCommercial() != null && this
						.getCommercial().equals(castOther.getCommercial())))
				&& ((this.getNomTiers() == castOther.getNomTiers()) || (this
						.getNomTiers() != null
						&& castOther.getNomTiers() != null && this
						.getNomTiers().equals(castOther.getNomTiers())))
				&& ((this.getCodeTTiers() == castOther.getCodeTTiers()) || (this
						.getCodeTTiers() != null
						&& castOther.getCodeTTiers() != null && this
						.getCodeTTiers().equals(castOther.getCodeTTiers())));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getId() == null ? 0 : this.getId()
						.hashCode());
		result = 37 * result
				+ (getIdTiers() == null ? 0 : this.getIdTiers().hashCode());
		result = 37
				* result
				+ (getCodeTiers() == null ? 0 : this.getCodeTiers()
						.hashCode());
		result = 37
				* result
				+ (getIdTiersCom() == null ? 0 : this.getIdTiersCom()
						.hashCode());
		result = 37
				* result
				+ (getCommercial() == null ? 0 : this.getCommercial()
						.hashCode());
		result = 37 * result
				+ (getNomTiers() == null ? 0 : this.getNomTiers().hashCode());
		result = 37
				* result
				+ (getCodeTTiers() == null ? 0 : this.getCodeTTiers()
						.hashCode());
		return result;
	}

	public Integer getVersionObj() {
		return versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

}
