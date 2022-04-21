package fr.legrain.bdg.compteclient.dto;

public class ObjectDTO extends ModelObject implements java.io.Serializable {

	private static final long serialVersionUID = 162274986246914404L;
	
	private Integer id;

	
	private Integer versionObj;
	               
	public Integer getVersionObj() {
		return versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

	public ObjectDTO() {
	}

	public ObjectDTO(Integer id) {
		this.id = id;
	}
	
	public void setSWTTva(ObjectDTO swtTva) {
		this.id = swtTva.id;
	}
	
	public static ObjectDTO copy(ObjectDTO swtTva){
		ObjectDTO swtTvaLoc = new ObjectDTO();
		swtTvaLoc.setId(swtTva.id);
		return swtTvaLoc;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer ID_TVA) {
		firePropertyChange("id", this.id, this.id = ID_TVA);
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ObjectDTO))
			return false;
		ObjectDTO castOther = (ObjectDTO) other;

		return ((this.getId() == castOther.getId()) || (this
				.getId() != null
				&& castOther.getId() != null && this.getId().equals(
				castOther.getId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getId() == null ? 0 : this.getId().hashCode());

		return result;
	}

}
