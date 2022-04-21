package fr.legrain.dossier.dto;

import fr.legrain.bdg.model.ModelObject;


public class TaGroupePreferenceDTO extends ModelObject implements java.io.Serializable {

	private static final long serialVersionUID = -5966329597977917854L;
	
	private Integer id;
	private String libelleGroupeArticle;
	private String descriptionGroupeArticle;
	private String position;
	private String composantAffichage;
	
	private Integer versionObj;

	public TaGroupePreferenceDTO() {
	}

	public TaGroupePreferenceDTO(Integer id, String libelleGroupeArticle, String descriptionGroupeArticle,
			String position, String composantAffichage, Integer versionObj) {
		super();
		this.id = id;
		this.libelleGroupeArticle = libelleGroupeArticle;
		this.descriptionGroupeArticle = descriptionGroupeArticle;
		this.position = position;
		this.composantAffichage = composantAffichage;
		this.versionObj = versionObj;
	}
	
	public static TaGroupePreferenceDTO copy(TaGroupePreferenceDTO taPreferencesDTO){
		TaGroupePreferenceDTO taPreferencesDTOLoc = new TaGroupePreferenceDTO();
		taPreferencesDTOLoc.setId(taPreferencesDTO.id);
		taPreferencesDTOLoc.setLibelleGroupeArticle(taPreferencesDTO.libelleGroupeArticle);
		taPreferencesDTOLoc.setDescriptionGroupeArticle(taPreferencesDTO.descriptionGroupeArticle);
		taPreferencesDTOLoc.setPosition(taPreferencesDTO.position);
		taPreferencesDTOLoc.setComposantAffichage(taPreferencesDTO.composantAffichage);
		taPreferencesDTOLoc.setPosition(taPreferencesDTO.position);
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



	public String getLibelleGroupeArticle() {
		return libelleGroupeArticle;
	}



	public void setLibelleGroupeArticle(String libelleGroupeArticle) {
		firePropertyChange("libelleGroupeArticle", this.libelleGroupeArticle, this.libelleGroupeArticle = libelleGroupeArticle);
	}



	public String getDescriptionGroupeArticle() {
		return descriptionGroupeArticle;
	}



	public void setDescriptionGroupeArticle(String descriptionGroupeArticle) {
		firePropertyChange("descriptionGroupeArticle", this.descriptionGroupeArticle, this.descriptionGroupeArticle = descriptionGroupeArticle);
	}



	public String getPosition() {
		return position;
	}



	public void setPosition(String position) {
		firePropertyChange("position", this.position, this.position = position);
	}



	public String getComposantAffichage() {
		return composantAffichage;
	}



	public void setComposantAffichage(String composantAffichage) {
		firePropertyChange("composantAffichage", this.composantAffichage, this.composantAffichage = composantAffichage);
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((composantAffichage == null) ? 0 : composantAffichage.hashCode());
		result = prime * result + ((descriptionGroupeArticle == null) ? 0 : descriptionGroupeArticle.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((libelleGroupeArticle == null) ? 0 : libelleGroupeArticle.hashCode());
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
		TaGroupePreferenceDTO other = (TaGroupePreferenceDTO) obj;
		if (composantAffichage == null) {
			if (other.composantAffichage != null)
				return false;
		} else if (!composantAffichage.equals(other.composantAffichage))
			return false;
		if (descriptionGroupeArticle == null) {
			if (other.descriptionGroupeArticle != null)
				return false;
		} else if (!descriptionGroupeArticle.equals(other.descriptionGroupeArticle))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (libelleGroupeArticle == null) {
			if (other.libelleGroupeArticle != null)
				return false;
		} else if (!libelleGroupeArticle.equals(other.libelleGroupeArticle))
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


}