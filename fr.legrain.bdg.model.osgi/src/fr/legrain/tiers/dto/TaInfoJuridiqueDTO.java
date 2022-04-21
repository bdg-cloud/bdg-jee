package fr.legrain.tiers.dto;

import fr.legrain.bdg.model.ModelObject;
import fr.legrain.validator.LgrHibernateValidated;


public class TaInfoJuridiqueDTO extends ModelObject implements java.io.Serializable {


	private static final long serialVersionUID = -5832583195713095297L;
	
	private Integer id;
	private String rcsInfoJuridique;
	private String siretInfoJuridique;
	private String capitalInfoJuridique;
	private String apeInfoJuridique;
	private Integer versionObj;

	public TaInfoJuridiqueDTO() {
	}

	public void setSWTAdresse(TaInfoJuridiqueDTO taInfoJuridiqueDTO) {
		this.id = taInfoJuridiqueDTO.id;
		this.rcsInfoJuridique = taInfoJuridiqueDTO.rcsInfoJuridique;
		this.siretInfoJuridique = taInfoJuridiqueDTO.siretInfoJuridique;
		this.capitalInfoJuridique = taInfoJuridiqueDTO.capitalInfoJuridique;
		this.apeInfoJuridique = taInfoJuridiqueDTO.apeInfoJuridique;
	}

	public static TaInfoJuridiqueDTO copy(TaInfoJuridiqueDTO taInfoJuridiqueDTO){
		TaInfoJuridiqueDTO taInfoJuridiqueDTOLoc = new TaInfoJuridiqueDTO();
		taInfoJuridiqueDTOLoc.setId(taInfoJuridiqueDTO.getId());           
		taInfoJuridiqueDTOLoc.setRcsInfoJuridique(taInfoJuridiqueDTO.getRcsInfoJuridique());          
		taInfoJuridiqueDTOLoc.setSiretInfoJuridique(taInfoJuridiqueDTO.getSiretInfoJuridique());               
		taInfoJuridiqueDTOLoc.setCapitalInfoJuridique(taInfoJuridiqueDTO.getCapitalInfoJuridique());       
		taInfoJuridiqueDTOLoc.setApeInfoJuridique(taInfoJuridiqueDTO.getApeInfoJuridique());            
		return taInfoJuridiqueDTOLoc;
	}
	
	public boolean estVide() {
		if(this.getApeInfoJuridique()!=null && !this.getApeInfoJuridique().isEmpty())return false;
		if(this.getCapitalInfoJuridique()!=null && !this.getCapitalInfoJuridique().isEmpty())return false;
		if(this.getRcsInfoJuridique()!=null && !this.getRcsInfoJuridique().isEmpty())return false;
		if(this.getSiretInfoJuridique()!=null && !this.getSiretInfoJuridique().isEmpty())return false;
		return true;
	}
	public Integer getId() {
		return id;
	}

	public void setId(Integer idInfoJuridique) {
		firePropertyChange("id", this.id, this.id = idInfoJuridique);
	}

	@LgrHibernateValidated(champBd = "rcs_info_juridique",table = "ta_info_juridique",champEntite="rcsInfoJuridique",clazz = TaInfoJuridiqueDTO.class)
	public String getRcsInfoJuridique() {
		return rcsInfoJuridique;
	}

	public void setRcsInfoJuridique(String rcsInfoJuridique) {
		firePropertyChange("rcsInfoJuridique", this.rcsInfoJuridique, this.rcsInfoJuridique = rcsInfoJuridique);
	}

	@LgrHibernateValidated(champBd = "siret_info_juridique",table = "ta_info_juridique",champEntite="siretInfoJuridique",clazz = TaInfoJuridiqueDTO.class)
	public String getSiretInfoJuridique() {
		return siretInfoJuridique;
	}

	public void setSiretInfoJuridique(String siretInfoJuridique) {
		firePropertyChange("siretInfoJuridique", this.siretInfoJuridique, this.siretInfoJuridique = siretInfoJuridique);
	}

	@LgrHibernateValidated(champBd = "capital_info_juridique",table = "ta_info_juridique",champEntite="capitalInfoJuridique",clazz = TaInfoJuridiqueDTO.class)
	public String getCapitalInfoJuridique() {
		return capitalInfoJuridique;
	}

	public void setCapitalInfoJuridique(String capitalInfoJuridique) {
		firePropertyChange("capitalInfoJuridique", this.capitalInfoJuridique, this.capitalInfoJuridique = capitalInfoJuridique);
	}

	@LgrHibernateValidated(champBd = "ape_info_juridique",table = "ta_info_juridique",champEntite="apeInfoJuridique",clazz = TaInfoJuridiqueDTO.class)
	public String getApeInfoJuridique() {
		return apeInfoJuridique;
	}

	public void setApeInfoJuridique(String apeInfoJuridique) {
		firePropertyChange("apeInfoJuridique", this.apeInfoJuridique, this.apeInfoJuridique = apeInfoJuridique);
	}
	
	public Integer getVersionObj() {
		return this.versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof TaInfoJuridiqueDTO))
			return false;
		TaInfoJuridiqueDTO castOther = (TaInfoJuridiqueDTO) other;

		return ((this.getId() == castOther.getId()) || (this
				.getId() != null
				&& castOther.getId() != null && this.getId()
				.equals(castOther.getId())))
				&& ((this.getRcsInfoJuridique() == castOther
						.getRcsInfoJuridique()) || (this.getRcsInfoJuridique() != null
						&& castOther.getRcsInfoJuridique() != null && this
						.getRcsInfoJuridique().equals(
								castOther.getRcsInfoJuridique())))
				&& ((this.getSiretInfoJuridique() == castOther
						.getSiretInfoJuridique()) || (this.getSiretInfoJuridique() != null
						&& castOther.getSiretInfoJuridique() != null && this
						.getSiretInfoJuridique().equals(
								castOther.getSiretInfoJuridique())))
				&& ((this.getCapitalInfoJuridique() == castOther
						.getCapitalInfoJuridique()) || (this.getCapitalInfoJuridique() != null
						&& castOther.getCapitalInfoJuridique() != null && this
						.getCapitalInfoJuridique().equals(
								castOther.getCapitalInfoJuridique())))
				&& ((this.getApeInfoJuridique() == castOther
						.getApeInfoJuridique()) || (this
						.getApeInfoJuridique() != null
						&& castOther.getApeInfoJuridique() != null && this
						.getApeInfoJuridique().equals(
								castOther.getApeInfoJuridique())));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getId() == null ? 0 : this.getId()
						.hashCode());
		result = 37
				* result
				+ (getRcsInfoJuridique() == null ? 0 : this
						.getRcsInfoJuridique().hashCode());
		result = 37
				* result
				+ (getCapitalInfoJuridique() == null ? 0 : this
						.getCapitalInfoJuridique().hashCode());
		result = 37
				* result
				+ (getApeInfoJuridique() == null ? 0 : this
						.getApeInfoJuridique().hashCode());
		return result;
	}

}
