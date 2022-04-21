package fr.legrain.caisse.dto;

import fr.legrain.bdg.model.ModelObject;


public class TaTDepotRetraitCaisseDTO extends ModelObject implements java.io.Serializable {

	private static final long serialVersionUID = 7897291462025293861L;
	
	private Integer id;
	
	private String codeTDepotRetraitCaisse;
	private String libelleTDepotRetraitCaisse;
	private Boolean systeme;
	
	private Integer versionObj;

	public Integer getVersionObj() {
		return versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

	public TaTDepotRetraitCaisseDTO() {
	}

	public TaTDepotRetraitCaisseDTO(Integer id, String codeTDepotRetraitCaisse, String libelleTDepotRetraitCaisse,
			Boolean systeme, Integer versionObj) {
		super();
		this.id = id;
		this.codeTDepotRetraitCaisse = codeTDepotRetraitCaisse;
		this.libelleTDepotRetraitCaisse = libelleTDepotRetraitCaisse;
		this.systeme = systeme;
		this.versionObj = versionObj;
	}
	
	public void setSWTTaTDepotRetraitCaisse(TaTDepotRetraitCaisseDTO taTDepotRetraitCaisseDTO) {
		this.id = taTDepotRetraitCaisseDTO.id;
		this.codeTDepotRetraitCaisse = taTDepotRetraitCaisseDTO.codeTDepotRetraitCaisse;
		this.libelleTDepotRetraitCaisse = taTDepotRetraitCaisseDTO.libelleTDepotRetraitCaisse;
		this.systeme = taTDepotRetraitCaisseDTO.systeme;
	}
	
	public static TaTDepotRetraitCaisseDTO copy(TaTDepotRetraitCaisseDTO taTDepotRetraitCaisseDTO){
		TaTDepotRetraitCaisseDTO taTDepotRetraitCaisseDTOLoc = new TaTDepotRetraitCaisseDTO();
		taTDepotRetraitCaisseDTOLoc.setId(taTDepotRetraitCaisseDTO.id);
		taTDepotRetraitCaisseDTOLoc.setCodeTDepotRetraitCaisse(taTDepotRetraitCaisseDTO.codeTDepotRetraitCaisse);
		taTDepotRetraitCaisseDTOLoc.setLibelleTDepotRetraitCaisse(taTDepotRetraitCaisseDTO.libelleTDepotRetraitCaisse);
		taTDepotRetraitCaisseDTOLoc.setSysteme(taTDepotRetraitCaisseDTO.systeme);
		return taTDepotRetraitCaisseDTOLoc;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		firePropertyChange("id", this.id, this.id = id);
	}

	public String getCodeTDepotRetraitCaisse() {
		return this.codeTDepotRetraitCaisse;
	}

	public void setCodeTDepotRetraitCaisse(String codeTDepotRetraitCaisse) {
		firePropertyChange("codeTDepotRetraitCaisse", this.codeTDepotRetraitCaisse, this.codeTDepotRetraitCaisse = codeTDepotRetraitCaisse);
	}

	public String getLibelleTDepotRetraitCaisse() {
		return this.libelleTDepotRetraitCaisse;
	}

	public void setLibelleTDepotRetraitCaisse(String libelleTDepotRetraitCaisse) {
		firePropertyChange("libelleTDepotRetraitCaisse", this.libelleTDepotRetraitCaisse, this.libelleTDepotRetraitCaisse = libelleTDepotRetraitCaisse);
	}
	
	public Boolean getSysteme() {
		return systeme;
	}

	public void setSysteme(Boolean systeme) {
		firePropertyChange("systeme", this.systeme, this.systeme = systeme);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TaTDepotRetraitCaisseDTO other = (TaTDepotRetraitCaisseDTO) obj;
		if (codeTDepotRetraitCaisse == null) {
			if (other.codeTDepotRetraitCaisse != null)
				return false;
		} else if (!codeTDepotRetraitCaisse.equals(other.codeTDepotRetraitCaisse))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (libelleTDepotRetraitCaisse == null) {
			if (other.libelleTDepotRetraitCaisse != null)
				return false;
		} else if (!libelleTDepotRetraitCaisse.equals(other.libelleTDepotRetraitCaisse))
			return false;
		if (systeme == null) {
			if (other.systeme != null)
				return false;
		} else if (!systeme.equals(other.systeme))
			return false;
		if (versionObj == null) {
			if (other.versionObj != null)
				return false;
		} else if (!versionObj.equals(other.versionObj))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codeTDepotRetraitCaisse == null) ? 0 : codeTDepotRetraitCaisse.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((libelleTDepotRetraitCaisse == null) ? 0 : libelleTDepotRetraitCaisse.hashCode());
		result = prime * result + ((systeme == null) ? 0 : systeme.hashCode());
		result = prime * result + ((versionObj == null) ? 0 : versionObj.hashCode());
		return result;
	}

}
