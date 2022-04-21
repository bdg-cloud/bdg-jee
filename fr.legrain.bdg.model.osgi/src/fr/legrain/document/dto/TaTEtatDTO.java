package fr.legrain.document.dto;

import fr.legrain.bdg.model.ModelObject;


public class TaTEtatDTO extends ModelObject implements java.io.Serializable {

	private static final long serialVersionUID = 1486439621159815072L;
	
	private Integer id;
	private String codeTEtat;
	private String libTEtat;
	private String prefixe;
	
	private Integer versionObj;
	
	public TaTEtatDTO(){
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer idTEtat) {
		firePropertyChange("id", this.id, this.id = idTEtat);
	}
	public String getCodeTEtat() {
		return codeTEtat;
	}
	public void setCodeTEtat(String codeTEtat) {
		firePropertyChange("codeTEtat", this.codeTEtat, this.codeTEtat = codeTEtat);
	}
	public String getLibTEtat() {
		return libTEtat;
	}

	public void setLibTEtat(String libTEtat) {
		firePropertyChange("libTEtat", this.libTEtat, this.libTEtat = libTEtat);
	}
	
	public Integer getVersionObj() {
		return this.versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

	
	public TaTEtatDTO(String codeTEtat, String libTEtat, String prefixe) {
		super();
		this.codeTEtat = codeTEtat;
		this.libTEtat = libTEtat;
		this.prefixe = prefixe;
	}

	public static TaTEtatDTO copy(TaTEtatDTO swtTEtat){
		TaTEtatDTO swtTEtatLoc = new TaTEtatDTO();
		swtTEtatLoc.setCodeTEtat(swtTEtat.codeTEtat);
		swtTEtatLoc.setId(swtTEtat.id);
		swtTEtatLoc.setLibTEtat(swtTEtat.libTEtat);
		swtTEtatLoc.setPrefixe(swtTEtat.prefixe);
		return swtTEtatLoc;
	}

	

	public String getPrefixe() {
		return prefixe;
	}

	public void setPrefixe(String prefixe) {
		firePropertyChange("prefixe", this.prefixe, this.prefixe = prefixe);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codeTEtat == null) ? 0 : codeTEtat.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((libTEtat == null) ? 0 : libTEtat.hashCode());
		result = prime * result + ((prefixe == null) ? 0 : prefixe.hashCode());
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
		TaTEtatDTO other = (TaTEtatDTO) obj;
		if (codeTEtat == null) {
			if (other.codeTEtat != null)
				return false;
		} else if (!codeTEtat.equals(other.codeTEtat))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (libTEtat == null) {
			if (other.libTEtat != null)
				return false;
		} else if (!libTEtat.equals(other.libTEtat))
			return false;
		if (prefixe == null) {
			if (other.prefixe != null)
				return false;
		} else if (!prefixe.equals(other.prefixe))
			return false;
		return true;
	}

	
	
}
