package fr.legrain.tiers.dto;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import fr.legrain.bdg.model.ModelObject;
import fr.legrain.validator.LgrHibernateValidated;

public class TaTTvaDocDTO extends ModelObject implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3545710568200984038L;
	private Integer id;
	private String journalTTvaDoc;
	private String codeTTvaDoc;
	private String libelleTTvaDoc;
	private String libelleEdition;
	private Integer versionObj;
	
	
	public void setSwtTypeTvaDoc(TaTTvaDocDTO taTTvaDocDTO){
		this.setId(taTTvaDocDTO.getId());                //1
		this.setCodeTTvaDoc(taTTvaDocDTO.getCodeTTvaDoc());            //2
		this.setJournalTTvaDoc(taTTvaDocDTO.getJournalTTvaDoc());        //3
		this.setLibelleTTvaDoc(taTTvaDocDTO.getLibelleTTvaDoc());      //4
		this.setLibelleEdition(taTTvaDocDTO.getLibelleEdition());
	}
	
	public static TaTTvaDocDTO copy(TaTTvaDocDTO taTTvaDocDTO){
		TaTTvaDocDTO taTTvaDocDTOLoc = new TaTTvaDocDTO();
		taTTvaDocDTOLoc.setId(taTTvaDocDTO.getId());                //1
		taTTvaDocDTOLoc.setJournalTTvaDoc(taTTvaDocDTO.getJournalTTvaDoc());        //2
		taTTvaDocDTOLoc.setCodeTTvaDoc(taTTvaDocDTO.getCodeTTvaDoc());            //3
		taTTvaDocDTOLoc.setLibelleTTvaDoc(taTTvaDocDTO.getLibelleTTvaDoc());      //4
		taTTvaDocDTOLoc.setLibelleEdition(taTTvaDocDTO.getLibelleEdition());
		return taTTvaDocDTOLoc;
	}
	

	@NotNull
	@Size(min=1, max=20)
	@LgrHibernateValidated(champBd = "code_t_tva_doc",table = "ta_t_tva_doc",champEntite="codeTTvaDoc",clazz = TaTTvaDocDTO.class)
	public String getCodeTTvaDoc() {
		return codeTTvaDoc;
	}

	public void setCodeTTvaDoc(String codeTTvaDoc) {
		firePropertyChange("codeTTvaDoc", this.codeTTvaDoc, this.codeTTvaDoc = codeTTvaDoc);
	}

	@NotNull
	@Size(min=1, max=20)
	@LgrHibernateValidated(champBd = "journal_t_tva_doc",table = "ta_t_tva_doc",champEntite="journalTTvaDoc",clazz = TaTTvaDocDTO.class)
	public String getJournalTTvaDoc() {
		return journalTTvaDoc;
	}

	public void setJournalTTvaDoc(String journalTTvaDoc) {
		firePropertyChange("journalTTvaDoc", this.journalTTvaDoc, this.journalTTvaDoc = journalTTvaDoc);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer idTTvaDoc) {
		firePropertyChange("id", this.id, this.id = idTTvaDoc);
	}

	@LgrHibernateValidated(champBd = "libelle_t_tva_doc",table = "ta_t_tva_doc",champEntite="libelleTTvaDoc",clazz = TaTTvaDocDTO.class)
	public String getLibelleTTvaDoc() {
		return libelleTTvaDoc;
	}

	public void setLibelleTTvaDoc(String libelleTTvaDoc) {
		firePropertyChange("libelleTTvaDoc", this.libelleTTvaDoc, this.libelleTTvaDoc = libelleTTvaDoc);
	}

	@LgrHibernateValidated(champBd = "libelle_edition",table = "ta_t_tva_doc",champEntite="libelleEdition",clazz = TaTTvaDocDTO.class)
	public String getLibelleEdition() {
		return libelleEdition;
	}

	public void setLibelleEdition(String libelleEdition) {
		firePropertyChange("libelleEdition", this.libelleEdition, this.libelleEdition = libelleEdition);
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
		result = prime * result
				+ ((codeTTvaDoc == null) ? 0 : codeTTvaDoc.hashCode());
		result = prime * result
				+ ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((journalTTvaDoc == null) ? 0 : journalTTvaDoc.hashCode());
		result = prime * result
				+ ((libelleEdition == null) ? 0 : libelleEdition.hashCode());
		result = prime * result
				+ ((libelleTTvaDoc == null) ? 0 : libelleTTvaDoc.hashCode());
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
		TaTTvaDocDTO other = (TaTTvaDocDTO) obj;
		if (codeTTvaDoc == null) {
			if (other.codeTTvaDoc != null)
				return false;
		} else if (!codeTTvaDoc.equals(other.codeTTvaDoc))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (journalTTvaDoc == null) {
			if (other.journalTTvaDoc != null)
				return false;
		} else if (!journalTTvaDoc.equals(other.journalTTvaDoc))
			return false;
		if (libelleEdition == null) {
			if (other.libelleEdition != null)
				return false;
		} else if (!libelleEdition.equals(other.libelleEdition))
			return false;
		if (libelleTTvaDoc == null) {
			if (other.libelleTTvaDoc != null)
				return false;
		} else if (!libelleTTvaDoc.equals(other.libelleTTvaDoc))
			return false;
		return true;
	}
	

}
