package fr.legrain.dossier.dto;

import fr.legrain.bdg.model.ModelObject;


public class TaCategoriePreferenceDTO extends ModelObject implements java.io.Serializable {

	private static final long serialVersionUID = -5966329597977917854L;
	
	private Integer id;
	private String codeCategoriePreference;
	private String libelleCategoriePreference;
	private String descriptionCategoriePreference;
	private String identifiantModule;
	private Integer position;
	
	private Integer idCategorieMere;
	private String codeCategorieMere;
	
	private Integer versionObj;

	public TaCategoriePreferenceDTO() {
	}

	public TaCategoriePreferenceDTO(Integer id, String codeCategoriePreference, String libelleCategoriePreference,
			String descriptionCategoriePreference, String identifiantModule, Integer position, Integer idCategorieMere,
			String codeCategorieMere, Integer versionObj) {
		super();
		this.id = id;
		this.codeCategoriePreference = codeCategoriePreference;
		this.libelleCategoriePreference = libelleCategoriePreference;
		this.descriptionCategoriePreference = descriptionCategoriePreference;
		this.identifiantModule = identifiantModule;
		this.position = position;
		this.idCategorieMere = idCategorieMere;
		this.codeCategorieMere = codeCategorieMere;
		this.versionObj = versionObj;
	}

	
	public static TaCategoriePreferenceDTO copy(TaCategoriePreferenceDTO taPreferencesDTO){
		TaCategoriePreferenceDTO taPreferencesDTOLoc = new TaCategoriePreferenceDTO();
		taPreferencesDTOLoc.setId(taPreferencesDTO.id);
		taPreferencesDTOLoc.setCodeCategoriePreference(taPreferencesDTO.codeCategoriePreference);
		taPreferencesDTOLoc.setLibelleCategoriePreference(taPreferencesDTO.libelleCategoriePreference);
		taPreferencesDTOLoc.setDescriptionCategoriePreference(taPreferencesDTO.descriptionCategoriePreference);
		taPreferencesDTOLoc.setIdentifiantModule(taPreferencesDTO.identifiantModule);
		taPreferencesDTOLoc.setPosition(taPreferencesDTO.position);
		taPreferencesDTOLoc.setIdCategorieMere(taPreferencesDTO.idCategorieMere);
		taPreferencesDTOLoc.setCodeCategorieMere(taPreferencesDTO.codeCategorieMere);
		return taPreferencesDTOLoc;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codeCategorieMere == null) ? 0 : codeCategorieMere.hashCode());
		result = prime * result + ((codeCategoriePreference == null) ? 0 : codeCategoriePreference.hashCode());
		result = prime * result
				+ ((descriptionCategoriePreference == null) ? 0 : descriptionCategoriePreference.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((idCategorieMere == null) ? 0 : idCategorieMere.hashCode());
		result = prime * result + ((identifiantModule == null) ? 0 : identifiantModule.hashCode());
		result = prime * result + ((libelleCategoriePreference == null) ? 0 : libelleCategoriePreference.hashCode());
		result = prime * result + ((position == null) ? 0 : position.hashCode());
		result = prime * result + ((versionObj == null) ? 0 : versionObj.hashCode());
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
		TaCategoriePreferenceDTO other = (TaCategoriePreferenceDTO) obj;
		if (codeCategorieMere == null) {
			if (other.codeCategorieMere != null)
				return false;
		} else if (!codeCategorieMere.equals(other.codeCategorieMere))
			return false;
		if (codeCategoriePreference == null) {
			if (other.codeCategoriePreference != null)
				return false;
		} else if (!codeCategoriePreference.equals(other.codeCategoriePreference))
			return false;
		if (descriptionCategoriePreference == null) {
			if (other.descriptionCategoriePreference != null)
				return false;
		} else if (!descriptionCategoriePreference.equals(other.descriptionCategoriePreference))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (idCategorieMere == null) {
			if (other.idCategorieMere != null)
				return false;
		} else if (!idCategorieMere.equals(other.idCategorieMere))
			return false;
		if (identifiantModule == null) {
			if (other.identifiantModule != null)
				return false;
		} else if (!identifiantModule.equals(other.identifiantModule))
			return false;
		if (libelleCategoriePreference == null) {
			if (other.libelleCategoriePreference != null)
				return false;
		} else if (!libelleCategoriePreference.equals(other.libelleCategoriePreference))
			return false;
		if (position == null) {
			if (other.position != null)
				return false;
		} else if (!position.equals(other.position))
			return false;
		if (versionObj == null) {
			if (other.versionObj != null)
				return false;
		} else if (!versionObj.equals(other.versionObj))
			return false;
		return true;
	}

	public String getCodeCategoriePreference() {
		return codeCategoriePreference;
	}

	public void setCodeCategoriePreference(String codeCategoriePreference) {
		firePropertyChange("codeCategoriePreference", this.codeCategoriePreference, this.codeCategoriePreference = codeCategoriePreference);
	}

	public String getLibelleCategoriePreference() {
		return libelleCategoriePreference;
	}

	public void setLibelleCategoriePreference(String libelleCategoriePreference) {
		firePropertyChange("libelleCategoriePreference", this.libelleCategoriePreference, this.libelleCategoriePreference = libelleCategoriePreference);
	}

	public String getDescriptionCategoriePreference() {
		return descriptionCategoriePreference;
	}

	public void setDescriptionCategoriePreference(String descriptionCategoriePreference) {
		firePropertyChange("descriptionCategoriePreference", this.descriptionCategoriePreference, this.descriptionCategoriePreference = descriptionCategoriePreference);
	}

	public String getIdentifiantModule() {
		return identifiantModule;
	}

	public void setIdentifiantModule(String identifiantModule) {
		firePropertyChange("identifiantModule", this.identifiantModule, this.identifiantModule = identifiantModule);
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		firePropertyChange("position", this.position, this.position = position);
	}

	public Integer getIdCategorieMere() {
		return idCategorieMere;
	}

	public void setIdCategorieMere(Integer idCategorieMere) {
		firePropertyChange("idCategorieMere", this.idCategorieMere, this.idCategorieMere = idCategorieMere);
	}

	public String getCodeCategorieMere() {
		return codeCategorieMere;
	}

	public void setCodeCategorieMere(String codeCategorieMere) {
		firePropertyChange("codeCategorieMere", this.codeCategorieMere, this.codeCategorieMere = codeCategorieMere);
	}

}
