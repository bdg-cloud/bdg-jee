package fr.legrain.caisse.dto;

import fr.legrain.bdg.model.ModelObject;


public class TaTLSessionCaisseDTO extends ModelObject implements java.io.Serializable {

	private static final long serialVersionUID = 7897291462025293861L;
	
	private Integer id;
	
	private String codeTLSessionCaisse;
	private String libelleTLSessionCaisse;
	private Boolean systeme;
	
	private Integer versionObj;

	public Integer getVersionObj() {
		return versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

	public TaTLSessionCaisseDTO() {
	}

	public TaTLSessionCaisseDTO(Integer id, String codeTLSessionCaisse, String libelleTLSessionCaisse, Boolean systeme, Integer versionObj) {
		super();
		this.id = id;
		this.codeTLSessionCaisse = codeTLSessionCaisse;
		this.libelleTLSessionCaisse = libelleTLSessionCaisse;
		this.systeme = systeme;
		this.versionObj = versionObj;
	}
	
	public static TaTLSessionCaisseDTO copy(TaTLSessionCaisseDTO taTLSessionCaisseDTO){
		TaTLSessionCaisseDTO taTLSessionCaisseDTOLoc = new TaTLSessionCaisseDTO();
		taTLSessionCaisseDTOLoc.setId(taTLSessionCaisseDTO.id);
		taTLSessionCaisseDTOLoc.setCodeTLSessionCaisse(taTLSessionCaisseDTO.codeTLSessionCaisse);
		taTLSessionCaisseDTOLoc.setLibelleTLSessionCaisse(taTLSessionCaisseDTO.libelleTLSessionCaisse);
		taTLSessionCaisseDTOLoc.setSysteme(taTLSessionCaisseDTO.systeme);
		return taTLSessionCaisseDTOLoc;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		firePropertyChange("id", this.id, this.id = id);
	}

	public String getCodeTLSessionCaisse() {
		return this.codeTLSessionCaisse;
	}

	public void setCodeTLSessionCaisse(String codeTLSessionCaisse) {
		firePropertyChange("codeTLSessionCaisse", this.codeTLSessionCaisse, this.codeTLSessionCaisse = codeTLSessionCaisse);
	}

	public String getLibelleTLSessionCaisse() {
		return this.libelleTLSessionCaisse;
	}

	public void setLibelleTLSessionCaisse(String libelleTLSessionCaisse) {
		firePropertyChange("libelleTLSessionCaisse", this.libelleTLSessionCaisse, this.libelleTLSessionCaisse = libelleTLSessionCaisse);
	}
	
	public Boolean getSysteme() {
		return systeme;
	}

	public void setSysteme(Boolean systeme) {
		firePropertyChange("systeme", this.systeme, this.systeme = systeme);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TaTLSessionCaisseDTO other = (TaTLSessionCaisseDTO) obj;
		if (codeTLSessionCaisse == null) {
			if (other.codeTLSessionCaisse != null)
				return false;
		} else if (!codeTLSessionCaisse.equals(other.codeTLSessionCaisse))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (libelleTLSessionCaisse == null) {
			if (other.libelleTLSessionCaisse != null)
				return false;
		} else if (!libelleTLSessionCaisse.equals(other.libelleTLSessionCaisse))
			return false;
		if (systeme == null) {
			if (other.systeme != null)
				return false;
		} else if (!systeme.equals(other.systeme))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codeTLSessionCaisse == null) ? 0 : codeTLSessionCaisse.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((libelleTLSessionCaisse == null) ? 0 : libelleTLSessionCaisse.hashCode());
		result = prime * result + ((systeme == null) ? 0 : systeme.hashCode());
		return result;
	}

	

}
