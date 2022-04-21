package fr.legrain.controle.dto;

import fr.legrain.bdg.model.ModelObject;


public class TaGenCodeExDTO extends ModelObject implements java.io.Serializable {
	

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1173616461362031834L;
	private Integer id;
	private String cleGencode;
	private String valeurGenCode;
	private String codeGencode;
	
	private Integer versionObj;
	
	public TaGenCodeExDTO(){
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

	public String getCodeGencode() {
		return codeGencode;
	}

	public void setCodeGencode(String codeGencode) {
		firePropertyChange("codeGencode", this.codeGencode, this.codeGencode = codeGencode);
	}


	
	public Integer getVersionObj() {
		return this.versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}



	public static TaGenCodeExDTO copy(TaGenCodeExDTO swtTDoc){
		TaGenCodeExDTO swtTDocLoc = new TaGenCodeExDTO();
		swtTDocLoc.setId(swtTDoc.id);
		swtTDocLoc.setCleGencode(swtTDoc.cleGencode);
		swtTDocLoc.setValeurGenCode(swtTDoc.valeurGenCode);
		swtTDocLoc.setCodeGencode(swtTDoc.codeGencode);
		return swtTDocLoc;
	}

	public TaGenCodeExDTO(Integer id, String cleGencode, String valeurGenCode,
			String codeGencode, Integer versionObj) {
		super();
		this.id = id;
		this.cleGencode = cleGencode;
		this.valeurGenCode = valeurGenCode;
		this.codeGencode = codeGencode;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((cleGencode == null) ? 0 : cleGencode.hashCode());
		result = prime * result
				+ ((codeGencode == null) ? 0 : codeGencode.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((valeurGenCode == null) ? 0 : valeurGenCode.hashCode());
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
		TaGenCodeExDTO other = (TaGenCodeExDTO) obj;
		if (cleGencode == null) {
			if (other.cleGencode != null)
				return false;
		} else if (!cleGencode.equals(other.cleGencode))
			return false;
		if (codeGencode == null) {
			if (other.codeGencode != null)
				return false;
		} else if (!codeGencode.equals(other.codeGencode))
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
