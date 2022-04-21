package fr.legrain.document.dto;

import fr.legrain.bdg.model.ModelObject;


public class TaTLigneDTO extends ModelObject implements java.io.Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7248174528721107064L;

	
	private Integer id;

	
	private Integer versionObj;
	
	public TaTLigneDTO(){
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

	


	public static TaTLigneDTO copy(TaTLigneDTO swtTDoc){
		TaTLigneDTO swtTDocLoc = new TaTLigneDTO();
		swtTDocLoc.setId(swtTDoc.id);

		return swtTDocLoc;
	}

	public TaTLigneDTO(Integer id, Integer versionObj) {
		super();
		this.id = id;
		this.versionObj = versionObj;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		TaTLigneDTO other = (TaTLigneDTO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (versionObj == null) {
			if (other.versionObj != null)
				return false;
		} else if (!versionObj.equals(other.versionObj))
			return false;
		return true;
	}



}
