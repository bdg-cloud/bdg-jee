package fr.legrain.dossier.dto;

import fr.legrain.bdg.model.ModelObject;


public class TaPreferencesDTO extends ModelObject implements java.io.Serializable {

	private static final long serialVersionUID = -5966329597977917854L;
	
	private Integer id;
	private String cle;
	private String libelle;
	private String valeur;
	private String valeurDefaut;
	private String valeursPossibles;
	private String typeDonnees;
	
	private Integer versionObj;

	public TaPreferencesDTO() {
	}

	public TaPreferencesDTO(Integer id, String cle, String libelle, String valeur,
			String valeurDefaut, String typeDonnees, Integer versionObj) {
		super();
		this.id = id;
		this.cle = cle;
		this.libelle = libelle;
		this.valeur = valeur;
		this.valeurDefaut = valeurDefaut;
		this.typeDonnees = typeDonnees;
		this.versionObj = versionObj;
	}

	public void setTaPreferencesDTO(TaPreferencesDTO taPreferencesDTO) {
		this.id = taPreferencesDTO.id;
		this.cle = taPreferencesDTO.cle;
		this.libelle = taPreferencesDTO.libelle;
		this.valeur = taPreferencesDTO.valeur;
		this.valeurDefaut = taPreferencesDTO.valeurDefaut;
		this.valeursPossibles = taPreferencesDTO.valeursPossibles;
		this.typeDonnees = taPreferencesDTO.typeDonnees;
	}
	
	public static TaPreferencesDTO copy(TaPreferencesDTO taPreferencesDTO){
		TaPreferencesDTO taPreferencesDTOLoc = new TaPreferencesDTO();
		taPreferencesDTOLoc.setId(taPreferencesDTO.id);
		taPreferencesDTOLoc.setCle(taPreferencesDTO.cle);
		taPreferencesDTOLoc.setLibelle(taPreferencesDTO.cle);
		taPreferencesDTOLoc.setValeur(taPreferencesDTO.valeur);
		taPreferencesDTOLoc.setValeurDefaut(taPreferencesDTO.valeurDefaut);
		taPreferencesDTOLoc.setValeursPossibles(taPreferencesDTO.valeursPossibles);
		taPreferencesDTOLoc.setTypeDonnees(taPreferencesDTO.typeDonnees);
		return taPreferencesDTOLoc;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		firePropertyChange("id", this.id, this.id = id);
	}

	public String getCle() {
		return cle;
	}

	public void setCle(String cle) {
		firePropertyChange("cle", this.cle, this.cle = cle);
	}
	
	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		firePropertyChange("libelle", this.libelle, this.libelle = libelle);
	}

	public String getValeur() {
		return valeur;
	}

	public void setValeur(String valeur) {
		firePropertyChange("valeur", this.valeur, this.valeur = valeur);
	}

	public String getValeurDefaut() {
		return valeurDefaut;
	}

	public void setValeurDefaut(String valeurDefaut) {
		firePropertyChange("valeurDefaut", this.valeurDefaut, this.valeurDefaut = valeurDefaut);
	}

	public String getTypeDonnees() {
		return typeDonnees;
	}

	public void setTypeDonnees(String typeDonnees) {
		firePropertyChange("typeDonnees", this.typeDonnees, this.typeDonnees = typeDonnees);
	}
	
	public String getValeursPossibles() {
		return valeursPossibles;
	}

	public void setValeursPossibles(String valeursPossibles) {
		firePropertyChange("valeursPossibles", this.valeursPossibles, this.valeursPossibles = valeursPossibles);
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
		result = prime * result + ((cle == null) ? 0 : cle.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((typeDonnees == null) ? 0 : typeDonnees.hashCode());
		result = prime * result + ((valeur == null) ? 0 : valeur.hashCode());
		result = prime * result
				+ ((valeurDefaut == null) ? 0 : valeurDefaut.hashCode());
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
		TaPreferencesDTO other = (TaPreferencesDTO) obj;
		if (cle == null) {
			if (other.cle != null)
				return false;
		} else if (!cle.equals(other.cle))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (typeDonnees == null) {
			if (other.typeDonnees != null)
				return false;
		} else if (!typeDonnees.equals(other.typeDonnees))
			return false;
		if (valeur == null) {
			if (other.valeur != null)
				return false;
		} else if (!valeur.equals(other.valeur))
			return false;
		if (valeurDefaut == null) {
			if (other.valeurDefaut != null)
				return false;
		} else if (!valeurDefaut.equals(other.valeurDefaut))
			return false;
		if (versionObj == null) {
			if (other.versionObj != null)
				return false;
		} else if (!versionObj.equals(other.versionObj))
			return false;
		return true;
	}

}
