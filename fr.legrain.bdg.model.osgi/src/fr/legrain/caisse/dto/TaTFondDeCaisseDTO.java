package fr.legrain.caisse.dto;

import fr.legrain.bdg.model.ModelObject;


public class TaTFondDeCaisseDTO extends ModelObject implements java.io.Serializable {

	private static final long serialVersionUID = 7897291462025293861L;
	
	private Integer id;
	
	private String codeTFondDeCaisse;
	private String libelleTFondDeCaisse;
	private Boolean systeme;
	
	private Integer versionObj;

	public Integer getVersionObj() {
		return versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

	public TaTFondDeCaisseDTO() {
	}

	public TaTFondDeCaisseDTO(Integer id, String codeTFondDeCaisse, String libelleTFondDeCaisse, Boolean systeme,
			Integer versionObj) {
		super();
		this.id = id;
		this.codeTFondDeCaisse = codeTFondDeCaisse;
		this.libelleTFondDeCaisse = libelleTFondDeCaisse;
		this.systeme = systeme;
		this.versionObj = versionObj;
	}
	
	public void setSWTTaTFondDeCaisse(TaTFondDeCaisseDTO taTFondDeCaisseDTO) {
		this.id = taTFondDeCaisseDTO.id;
		this.codeTFondDeCaisse = taTFondDeCaisseDTO.codeTFondDeCaisse;
		this.libelleTFondDeCaisse = taTFondDeCaisseDTO.libelleTFondDeCaisse;
		this.systeme = taTFondDeCaisseDTO.systeme;
	}
	
	public static TaTFondDeCaisseDTO copy(TaTFondDeCaisseDTO taTFondDeCaisseDTO){
		TaTFondDeCaisseDTO taTFondDeCaisseDTOLoc = new TaTFondDeCaisseDTO();
		taTFondDeCaisseDTOLoc.setId(taTFondDeCaisseDTO.id);
		taTFondDeCaisseDTOLoc.setCodeTFondDeCaisse(taTFondDeCaisseDTO.codeTFondDeCaisse);
		taTFondDeCaisseDTOLoc.setLibelleTFondDeCaisse(taTFondDeCaisseDTO.libelleTFondDeCaisse);
		taTFondDeCaisseDTOLoc.setSysteme(taTFondDeCaisseDTO.systeme);
		return taTFondDeCaisseDTOLoc;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		firePropertyChange("id", this.id, this.id = id);
	}

	public String getCodeTFondDeCaisse() {
		return this.codeTFondDeCaisse;
	}

	public void setCodeTFondDeCaisse(String codeTFondDeCaisse) {
		firePropertyChange("codeTFondDeCaisse", this.codeTFondDeCaisse, this.codeTFondDeCaisse = codeTFondDeCaisse);
	}

	public String getLibelleTFondDeCaisse() {
		return this.libelleTFondDeCaisse;
	}

	public void setLibelleTFondDeCaisse(String libelleTFondDeCaisse) {
		firePropertyChange("libelleTFondDeCaisse", this.libelleTFondDeCaisse, this.libelleTFondDeCaisse = libelleTFondDeCaisse);
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
		TaTFondDeCaisseDTO other = (TaTFondDeCaisseDTO) obj;
		if (codeTFondDeCaisse == null) {
			if (other.codeTFondDeCaisse != null)
				return false;
		} else if (!codeTFondDeCaisse.equals(other.codeTFondDeCaisse))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (libelleTFondDeCaisse == null) {
			if (other.libelleTFondDeCaisse != null)
				return false;
		} else if (!libelleTFondDeCaisse.equals(other.libelleTFondDeCaisse))
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
		result = prime * result + ((codeTFondDeCaisse == null) ? 0 : codeTFondDeCaisse.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((libelleTFondDeCaisse == null) ? 0 : libelleTFondDeCaisse.hashCode());
		result = prime * result + ((systeme == null) ? 0 : systeme.hashCode());
		result = prime * result + ((versionObj == null) ? 0 : versionObj.hashCode());
		return result;
	}

}
