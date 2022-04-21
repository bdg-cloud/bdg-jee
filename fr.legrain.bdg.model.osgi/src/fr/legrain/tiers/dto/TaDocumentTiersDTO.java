package fr.legrain.tiers.dto;

import fr.legrain.bdg.model.ModelObject;
import fr.legrain.validator.LgrHibernateValidated;

public class TaDocumentTiersDTO extends ModelObject implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1837674247831200055L;
	private Integer id;
	private String codeDocumentTiers;
	private String libelleDocumentTiers;
	private String cheminModelDocumentTiers;
	private String cheminCorrespDocumentTiers;
	private Boolean actif;
	private Boolean defaut;
	private String typeLogiciel;
	private Integer versionObj;
	
	public TaDocumentTiersDTO(){
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		firePropertyChange("id", this.id, this.id = id);
	}
	
	@LgrHibernateValidated(champBd = "code_document_tiers",table = "ta_document_tiers",champEntite="codeDocumentTiers",clazz = TaDocumentTiersDTO.class)
	public String getCodeDocumentTiers() {
		return codeDocumentTiers;
	}
	
	public void setCodeDocumentTiers(String codeDocumentTiers) {
		firePropertyChange("codeDocumentTiers", this.codeDocumentTiers, this.codeDocumentTiers = codeDocumentTiers);
	}
	
	@LgrHibernateValidated(champBd = "chemin_model_document_tiers",table = "ta_document_tiers",champEntite="cheminModelDocumentTiers",clazz = TaDocumentTiersDTO.class)
	public String getCheminModelDocumentTiers() {
		return cheminModelDocumentTiers;
	}
	
	public void setCheminModelDocumentTiers(String cheminModelDocumentTiers) {
		firePropertyChange("cheminModelDocumentTiers", this.cheminModelDocumentTiers, this.cheminModelDocumentTiers = cheminModelDocumentTiers);
	}
	
	@LgrHibernateValidated(champBd = "libelle_document_tiers",table = "ta_document_tiers",champEntite="libelleDocumentTiers",clazz = TaDocumentTiersDTO.class)
	public String getLibelleDocumentTiers() {
		return libelleDocumentTiers;
	}

	public void setLibelleDocumentTiers(String libelleDocumentTiers) {
		firePropertyChange("libelleDocumentTiers", this.libelleDocumentTiers, this.libelleDocumentTiers = libelleDocumentTiers);
	}

	@LgrHibernateValidated(champBd = "chemin_corresp_document_tiers",table = "ta_document_tiers",champEntite="cheminCorrespDocumentTiers",clazz = TaDocumentTiersDTO.class)
	public String getCheminCorrespDocumentTiers() {
		return cheminCorrespDocumentTiers;
	}

	public void setCheminCorrespDocumentTiers(String cheminCorrespDocumentTiers) {
		firePropertyChange("cheminCorrespDocumentTiers", this.cheminCorrespDocumentTiers, this.cheminCorrespDocumentTiers = cheminCorrespDocumentTiers);
	}

	@LgrHibernateValidated(champBd = "actif",table = "ta_document_tiers",champEntite="actif",clazz = TaDocumentTiersDTO.class)
	public Boolean getActif() {
		return actif;
	}

	public void setActif(Boolean actif) {
		firePropertyChange("actif", this.actif, this.actif = actif);
	}
	
	@LgrHibernateValidated(champBd = "defaut",table = "ta_document_tiers",champEntite="defaut",clazz = TaDocumentTiersDTO.class)
	public Boolean getDefaut() {
		return defaut;
	}

	public void setDefaut(Boolean defaut) {
		firePropertyChange("defaut", this.defaut, this.defaut = defaut);
	}
	
	@LgrHibernateValidated(champBd = "type_logiciel",table = "ta_document_tiers",champEntite="typeLogiciel",clazz = TaDocumentTiersDTO.class)
	public String getTypeLogiciel() {
		return typeLogiciel;
	}

	public void setTypeLogiciel(String typeLogiciel) {
		firePropertyChange("typeLogiciel", this.typeLogiciel, this.typeLogiciel = typeLogiciel);
	}
	
	public Integer getVersionObj() {
		return this.versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}
	
	
	public static TaDocumentTiersDTO copy(TaDocumentTiersDTO taDocumentTiersDTO){
		TaDocumentTiersDTO taDocumentTiersDTOLoc = new TaDocumentTiersDTO();
		taDocumentTiersDTOLoc.setCodeDocumentTiers(taDocumentTiersDTO.codeDocumentTiers);
		taDocumentTiersDTOLoc.setId(taDocumentTiersDTO.id);
		taDocumentTiersDTOLoc.setCheminModelDocumentTiers(taDocumentTiersDTO.cheminModelDocumentTiers);
		taDocumentTiersDTOLoc.setCheminCorrespDocumentTiers(taDocumentTiersDTO.cheminCorrespDocumentTiers);
		taDocumentTiersDTOLoc.setLibelleDocumentTiers(taDocumentTiersDTO.libelleDocumentTiers);
		taDocumentTiersDTOLoc.setActif(taDocumentTiersDTO.actif);
		taDocumentTiersDTOLoc.setDefaut(taDocumentTiersDTO.defaut);
		taDocumentTiersDTOLoc.setTypeLogiciel(taDocumentTiersDTO.typeLogiciel);
		return taDocumentTiersDTOLoc;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((actif == null) ? 0 : actif.hashCode());
		result = prime
				* result
				+ ((cheminCorrespDocumentTiers == null) ? 0 : cheminCorrespDocumentTiers
						.hashCode());
		result = prime
				* result
				+ ((cheminModelDocumentTiers == null) ? 0 : cheminModelDocumentTiers
						.hashCode());
		result = prime * result
				+ ((codeDocumentTiers == null) ? 0 : codeDocumentTiers.hashCode());
		result = prime * result + ((defaut == null) ? 0 : defaut.hashCode());
		result = prime * result
				+ ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((libelleDocumentTiers == null) ? 0 : libelleDocumentTiers.hashCode());
		result = prime * result
				+ ((typeLogiciel == null) ? 0 : typeLogiciel.hashCode());
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
		TaDocumentTiersDTO other = (TaDocumentTiersDTO) obj;
		if (actif == null) {
			if (other.actif != null)
				return false;
		} else if (!actif.equals(other.actif))
			return false;
		if (cheminCorrespDocumentTiers == null) {
			if (other.cheminCorrespDocumentTiers != null)
				return false;
		} else if (!cheminCorrespDocumentTiers.equals(other.cheminCorrespDocumentTiers))
			return false;
		if (cheminModelDocumentTiers == null) {
			if (other.cheminModelDocumentTiers != null)
				return false;
		} else if (!cheminModelDocumentTiers.equals(other.cheminModelDocumentTiers))
			return false;
		if (codeDocumentTiers == null) {
			if (other.codeDocumentTiers != null)
				return false;
		} else if (!codeDocumentTiers.equals(other.codeDocumentTiers))
			return false;
		if (defaut == null) {
			if (other.defaut != null)
				return false;
		} else if (!defaut.equals(other.defaut))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (libelleDocumentTiers == null) {
			if (other.libelleDocumentTiers != null)
				return false;
		} else if (!libelleDocumentTiers.equals(other.libelleDocumentTiers))
			return false;
		if (typeLogiciel == null) {
			if (other.typeLogiciel != null)
				return false;
		} else if (!typeLogiciel.equals(other.typeLogiciel))
			return false;
		return true;
	}
	
}
