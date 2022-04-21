package fr.legrain.autorisations.controle.dto;

import fr.legrain.autorisations.model.ModelObject;


public class TaModificationDTO extends ModelObject implements java.io.Serializable {
	
	private static final long serialVersionUID = 5971985751766498206L;
	
	private Integer id;
	private String codeTDoc;
	private String libTDoc;
	
	private Integer versionObj;
	
	public TaModificationDTO(){
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer idTDoc) {
		firePropertyChange("id", this.id, this.id = idTDoc);
	}
	
	
	public Integer getVersionObj() {
		return this.versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

	
	public TaModificationDTO(String codeTDoc, String libTDoc) {
		super();
		this.codeTDoc = codeTDoc;
		this.libTDoc = libTDoc;
	}

	public static TaModificationDTO copy(TaModificationDTO swtTDoc){
		TaModificationDTO swtTDocLoc = new TaModificationDTO();
		swtTDocLoc.setId(swtTDoc.id);
		return swtTDocLoc;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((codeTDoc == null) ? 0 : codeTDoc.hashCode());
		result = prime * result
				+ ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((libTDoc == null) ? 0 : libTDoc.hashCode());
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
		TaModificationDTO other = (TaModificationDTO) obj;
		if (codeTDoc == null) {
			if (other.codeTDoc != null)
				return false;
		} else if (!codeTDoc.equals(other.codeTDoc))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (libTDoc == null) {
			if (other.libTDoc != null)
				return false;
		} else if (!libTDoc.equals(other.libTDoc))
			return false;
		return true;
	}

}
