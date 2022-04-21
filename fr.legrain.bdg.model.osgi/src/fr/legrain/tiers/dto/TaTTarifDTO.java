package fr.legrain.tiers.dto;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import fr.legrain.bdg.model.ModelObject;
import fr.legrain.validator.LgrHibernateValidated;

public class TaTTarifDTO extends ModelObject implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4315231821971903074L;
	private Integer id;
	private String codeTTarif;
	private String liblTTarif;
	private Boolean sens = false;
	private Boolean pourcentage = false;
	private BigDecimal valeur = new BigDecimal(0);
	
	private Integer versionObj;
	
	public TaTTarifDTO() {
		
	}
	
	
	public boolean estVide() {
		if(this.getCodeTTarif()!=null && !this.getCodeTTarif().isEmpty())return false;
		if(this.getLiblTTarif()!=null && !this.getLiblTTarif().isEmpty())return false;
		return true;
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		firePropertyChange("id", this.id, this.id = id);
	}
	
	@NotNull
	@Size(min=1, max=20)
	@LgrHibernateValidated(champBd = "code_t_tarif",table = "ta_t_tarif",champEntite="codeTTarif",clazz = TaTTarifDTO.class)
	public String getCodeTTarif() {
		return codeTTarif;
	}
	
	public void setCodeTTarif(String codeTTarif) {
		firePropertyChange("codeTTarif", this.codeTTarif, this.codeTTarif = codeTTarif);
	}
	
	@LgrHibernateValidated(champBd = "libl_t_tarif",table = "ta_t_tarif",champEntite="liblTTarif",clazz = TaTTarifDTO.class)
	public String getLiblTTarif() {
		return liblTTarif;
	}
	
	public void setLiblTTarif(String liblTTarif) {
		firePropertyChange("liblTTarif", this.liblTTarif, this.liblTTarif = liblTTarif);
	}
	
	@NotNull
	@LgrHibernateValidated(champBd = "sens",table = "ta_t_tarif",champEntite="sens",clazz = TaTTarifDTO.class)
	public Boolean getSens() {
		return sens;
	}

	public void setSens(Boolean sens) {
		firePropertyChange("sens", this.sens, this.sens = sens);
	}

	@NotNull
	@LgrHibernateValidated(champBd = "pourcentage",table = "ta_t_tarif",champEntite="pourcentage",clazz = TaTTarifDTO.class)
	public Boolean getPourcentage() {
		return pourcentage;
	}

	public void setPourcentage(Boolean pourcentage) {
		firePropertyChange("pourcentage", this.pourcentage, this.pourcentage = pourcentage);
	}

	@LgrHibernateValidated(champBd = "valeur",table = "ta_t_tarif",champEntite="valeur",clazz = TaTTarifDTO.class)
	public BigDecimal getValeur() {
		return valeur;
	}

	public void setValeur(BigDecimal valeur) {
		firePropertyChange("valeur", this.valeur, this.valeur = valeur);
	}
	
	public Integer getVersionObj() {
		return this.versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

	public static TaTTarifDTO copy(TaTTarifDTO taTTarifDTO){
		TaTTarifDTO taTTarifDTOLoc = new TaTTarifDTO();
		taTTarifDTOLoc.setId(taTTarifDTO.getId());                //1
		taTTarifDTOLoc.setCodeTTarif(taTTarifDTO.getCodeTTarif());        //2
		taTTarifDTOLoc.setLiblTTarif(taTTarifDTO.getLiblTTarif());            //3
		taTTarifDTOLoc.setSens(taTTarifDTO.getSens());
		taTTarifDTOLoc.setPourcentage(taTTarifDTO.getPourcentage());
		taTTarifDTOLoc.setValeur(taTTarifDTO.getValeur());
		return taTTarifDTOLoc;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codeTTarif == null) ? 0 : codeTTarif.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((liblTTarif == null) ? 0 : liblTTarif.hashCode());
		result = prime * result + ((pourcentage == null) ? 0 : pourcentage.hashCode());
		result = prime * result + ((sens == null) ? 0 : sens.hashCode());
		result = prime * result + ((valeur == null) ? 0 : valeur.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TaTTarifDTO other = (TaTTarifDTO) obj;
		if (codeTTarif == null) {
			if (other.codeTTarif != null)
				return false;
		} else if (!codeTTarif.equals(other.codeTTarif))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (liblTTarif == null) {
			if (other.liblTTarif != null)
				return false;
		} else if (!liblTTarif.equals(other.liblTTarif))
			return false;
		if (pourcentage == null) {
			if (other.pourcentage != null)
				return false;
		} else if (!pourcentage.equals(other.pourcentage))
			return false;
		if (sens == null) {
			if (other.sens != null)
				return false;
		} else if (!sens.equals(other.sens))
			return false;
		if (valeur == null) {
			if (other.valeur != null)
				return false;
		} else if (!valeur.equals(other.valeur))
			return false;
		return true;
	}




	
}
