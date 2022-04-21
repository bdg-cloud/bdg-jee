package fr.legrain.document.dto;

import fr.legrain.bdg.model.ModelObject;


public class TaTDocDTO extends ModelObject implements java.io.Serializable {

	private static final long serialVersionUID = 1486439621159815072L;
	
	private Integer id;
	private String codeTDoc;
	private String libTDoc;
	
	private Integer versionObj;
	
	public TaTDocDTO(){
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer idTDoc) {
		firePropertyChange("id", this.id, this.id = idTDoc);
	}
	public String getCodeTDoc() {
		return codeTDoc;
	}
	public void setCodeTDoc(String codeTDoc) {
		firePropertyChange("codeTDoc", this.codeTDoc, this.codeTDoc = codeTDoc);
	}
	public String getLibTDoc() {
		return libTDoc;
	}

	public void setLibTDoc(String libTDoc) {
		firePropertyChange("libTDoc", this.libTDoc, this.libTDoc = libTDoc);
	}
	
	public Integer getVersionObj() {
		return this.versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

	
	public TaTDocDTO(String codeTDoc, String libTDoc) {
		super();
		this.codeTDoc = codeTDoc;
		this.libTDoc = libTDoc;
	}

	public static TaTDocDTO copy(TaTDocDTO swtTDoc){
		TaTDocDTO swtTDocLoc = new TaTDocDTO();
		swtTDocLoc.setCodeTDoc(swtTDoc.codeTDoc);
		swtTDocLoc.setId(swtTDoc.id);
		swtTDocLoc.setLibTDoc(swtTDoc.libTDoc);
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
		TaTDocDTO other = (TaTDocDTO) obj;
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
