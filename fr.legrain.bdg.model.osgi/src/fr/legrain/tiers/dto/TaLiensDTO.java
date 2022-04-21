package fr.legrain.tiers.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import fr.legrain.bdg.model.ModelObject;
import fr.legrain.validator.LgrHibernateValidated;

public class TaLiensDTO extends ModelObject implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1152391171035684789L;
	private Integer id;
	private String codeTLiens;
	private String codeTiers;
	private String adresseLiens;
	private Integer idTLiens;
	
	private Integer versionObj;
	
	public TaLiensDTO() {
		
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer idLiens) {
		firePropertyChange("id", this.id, this.id = idLiens);
	}
	
	@NotNull
	@Size(min=1, max=20)
	@LgrHibernateValidated(champBd = "code_t_liens",table = "ta_t_liens",champEntite="codeTLiens",clazz = TaLiensDTO.class)
	public String getCodeTLiens() {
		return codeTLiens;
	}
	
	public void setCodeTLiens(String codeTLiens) {
		firePropertyChange("codeTLiens", this.codeTLiens, this.codeTLiens = codeTLiens);
	}
	
	@Size(min=0, max=20)
	@LgrHibernateValidated(champBd = "code_tiers",table = "ta_tiers",champEntite="codeTiers",clazz = TaLiensDTO.class)
	public String getCodeTiers() {
		return codeTiers;
	}
	
	public void setCodeTiers(String codeTiers) {
		firePropertyChange("codeTiers", this.codeTiers, this.codeTiers = codeTiers);
	}
	
	@LgrHibernateValidated(champBd = "adresse_liens",table = "ta_liens",champEntite="adresseLiens",clazz = TaLiensDTO.class)
	public String getAdresseLiens() {
		return adresseLiens;
	}
	public void setAdresseLiens(String adresseLiens) {
		firePropertyChange("adresseLiens", this.adresseLiens, this.adresseLiens = adresseLiens);
	}
	
	public Integer getVersionObj() {
		return this.versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}
	
	public static TaLiensDTO copy(TaLiensDTO taLiensDTO) {
		TaLiensDTO swtLiensLoc = new TaLiensDTO();
		swtLiensLoc.setId(taLiensDTO.getId());
		swtLiensLoc.setAdresseLiens(taLiensDTO.getAdresseLiens());
		swtLiensLoc.setCodeTiers(taLiensDTO.getCodeTiers());
		swtLiensLoc.setId(taLiensDTO.getId());
		swtLiensLoc.setIdTLiens(taLiensDTO.getIdTLiens());
		swtLiensLoc.setCodeTLiens(taLiensDTO.getCodeTLiens());
		return swtLiensLoc;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((adresseLiens == null) ? 0 : adresseLiens.hashCode());
		result = prime * result
				+ ((codeTLiens == null) ? 0 : codeTLiens.hashCode());
		result = prime * result
				+ ((codeTiers == null) ? 0 : codeTiers.hashCode());
		result = prime * result
		+ ((idTLiens == null) ? 0 : idTLiens.hashCode());		
		result = prime * result
		+ ((id == null) ? 0 : id.hashCode());		
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
		final TaLiensDTO other = (TaLiensDTO) obj;
		if (adresseLiens == null) {
			if (other.adresseLiens != null)
				return false;
		} else if (!adresseLiens.equals(other.adresseLiens))
			return false;
		if (codeTLiens == null) {
			if (other.codeTLiens != null)
				return false;
		} else if (!codeTLiens.equals(other.codeTLiens))
			return false;
		if (idTLiens == null) {
			if (other.idTLiens != null)
				return false;
		} else if (!idTLiens.equals(other.idTLiens))
			return false;		
		if (codeTiers == null) {
			if (other.codeTiers != null)
				return false;
		} else if (!codeTiers.equals(other.codeTiers))
			return false;
		if (id != other.id)
			return false;
		return true;
	}

	public Integer getIdTLiens() {
		return idTLiens;
	}

	public void setIdTLiens(Integer idTLiens) {		
		firePropertyChange("idTLiens", this.idTLiens, this.idTLiens = idTLiens);
	}
}
