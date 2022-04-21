package fr.legrain.moncompte.dto;


import fr.legrain.moncompte.commun.model.ModelObject;


public class TaTypePaiementDTO extends ModelObject implements java.io.Serializable {

	private static final long serialVersionUID = -4998349386830895895L;
	
	private Integer id;
	private String code;
	private String libelle;

	private Integer versionObj;

	public TaTypePaiementDTO() {
	}

	public TaTypePaiementDTO(Integer id, String code, String libelle, Integer versionObj) {
		super();
		this.id = id;
		this.code = code;
		this.libelle = libelle;
		this.versionObj = versionObj;
	}


	public void setSWTInfoEntreprise(TaTypePaiementDTO taProduitDTO) {
		this.id = taProduitDTO.id;
		this.code = taProduitDTO.code;
		this.libelle = taProduitDTO.libelle;
		this.versionObj = taProduitDTO.versionObj;
	}
	
	public static TaTypePaiementDTO copy(TaTypePaiementDTO taProduitDTO){
		TaTypePaiementDTO taProduitDTOLoc = new TaTypePaiementDTO();
		taProduitDTOLoc.setId(taProduitDTO.id);
		taProduitDTOLoc.setCode(taProduitDTO.code);
		taProduitDTOLoc.setLibelle(taProduitDTO.libelle);
		taProduitDTOLoc.setVersionObj(taProduitDTO.versionObj);
		return taProduitDTOLoc;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		firePropertyChange("id", this.id, this.id = id);
	}

	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		firePropertyChange("code", this.code, this.code = code);
	}


	public String getLibelle() {
		return libelle;
	}


	public void setLibelle(String libelle) {
		firePropertyChange("libelle", this.libelle, this.libelle = libelle);
	}


	
	public Integer getVersionObj() {
		return this.versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((libelle == null) ? 0 : libelle.hashCode());
		result = prime * result
				+ ((versionObj == null) ? 0 : versionObj.hashCode());
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
		TaTypePaiementDTO other = (TaTypePaiementDTO) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (libelle == null) {
			if (other.libelle != null)
				return false;
		} else if (!libelle.equals(other.libelle))
			return false;
		if (versionObj == null) {
			if (other.versionObj != null)
				return false;
		} else if (!versionObj.equals(other.versionObj))
			return false;
		return true;
	}

}
