package fr.legrain.controle.dto;

import fr.legrain.bdg.model.ModelObject;


public class TaGenCodeDTO extends ModelObject implements java.io.Serializable {
	
	private static final long serialVersionUID = 5971985751766498206L;
	
	private Integer id;
	private String cleGencode;
	private String valeurGenCode;
	
	private Integer versionObj;
	
	public TaGenCodeDTO(){
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		firePropertyChange("id", this.id, this.id = id);
	}
	
	public String getCleGencode() {
		return cleGencode;
	}

	public void setCleGencode(String cleGencode) {
		firePropertyChange("cleGencode", this.cleGencode, this.cleGencode = cleGencode);
	}

	public String getValeurGenCode() {
		return valeurGenCode;
	}

	public void setValeurGenCode(String valeurGenCode) {
		firePropertyChange("valeurGenCode", this.valeurGenCode, this.valeurGenCode = valeurGenCode);
	}
	
	public Integer getVersionObj() {
		return this.versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

	
	public TaGenCodeDTO(String cleGencode, String valeur) {
		super();
		this.cleGencode = cleGencode;
		this.valeurGenCode = valeur;
	}

	public static TaGenCodeDTO copy(TaGenCodeDTO swtTDoc){
		TaGenCodeDTO swtTDocLoc = new TaGenCodeDTO();
		swtTDocLoc.setId(swtTDoc.id);
		swtTDocLoc.setCleGencode(swtTDoc.cleGencode);
		swtTDocLoc.setValeurGenCode(swtTDoc.valeurGenCode);
		return swtTDocLoc;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((cleGencode == null) ? 0 : cleGencode.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((valeurGenCode == null) ? 0 : valeurGenCode.hashCode());
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
		TaGenCodeDTO other = (TaGenCodeDTO) obj;
		if (cleGencode == null) {
			if (other.cleGencode != null)
				return false;
		} else if (!cleGencode.equals(other.cleGencode))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (valeurGenCode == null) {
			if (other.valeurGenCode != null)
				return false;
		} else if (!valeurGenCode.equals(other.valeurGenCode))
			return false;
		return true;
	}





}
