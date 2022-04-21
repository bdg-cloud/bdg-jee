package fr.legrain.tiers.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fr.legrain.bdg.model.ModelObject;


public class TaParamCorrespondanceDTO extends ModelObject implements Serializable {
	

	private static final long serialVersionUID = 4267079161247367902L;
	
	public static final int TYPE_ADRESSE = 1;
	public static final int TYPE_TELEPHONE = 2;
	public static final int TYPE_EMAIL = 3;
	
	private Integer id;
	private Integer type;
	private String libelle;
	private Boolean administratif;
	private Boolean commercial;
	
	private List<TaParamCorrespondanceDTO> list = null;
	
	public List<TaParamCorrespondanceDTO> getList() {
		if(list==null)
			list = new ArrayList<TaParamCorrespondanceDTO>();
		return list;
	}

	public void setList(List<TaParamCorrespondanceDTO> list) {
		firePropertyChange("list", this.list, this.list = list);
	}
	
	public TaParamCorrespondanceDTO(){
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		firePropertyChange("id", this.id, this.id = id);
	}
	
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		firePropertyChange("type", this.type, this.type = type);
	}
	
	public String getLibelle() {
		return libelle;
	}
	public void setLibelle(String libelle) {
		firePropertyChange("libelle", this.libelle, this.libelle = libelle);
	}

	public Boolean getAdministratif() {
		return administratif;
	}

	public void setAdministratif(Boolean administratif) {
		firePropertyChange("administratif", this.administratif, this.administratif = administratif);
	}
	
	public Boolean getCommercial() {
		return commercial;
	}

	public void setCommercial(Boolean commercial) {
		firePropertyChange("commercial", this.commercial, this.commercial = commercial);
	}	
	
	public static TaParamCorrespondanceDTO copy(TaParamCorrespondanceDTO swtDocumentEditable){
		TaParamCorrespondanceDTO swtDocumentEditableLoc = new TaParamCorrespondanceDTO();
		swtDocumentEditableLoc.setLibelle(swtDocumentEditable.libelle);
		swtDocumentEditableLoc.setId(swtDocumentEditable.id);
		swtDocumentEditableLoc.setAdministratif(swtDocumentEditable.administratif);
		swtDocumentEditableLoc.setCommercial(swtDocumentEditable.commercial);
		return swtDocumentEditableLoc;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((administratif == null) ? 0 : administratif.hashCode());
		result = prime * result
				+ ((libelle == null) ? 0 : libelle.hashCode());
		result = prime * result + ((commercial == null) ? 0 : commercial.hashCode());
		result = prime * result
				+ ((id == null) ? 0 : id.hashCode());
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
		TaParamCorrespondanceDTO other = (TaParamCorrespondanceDTO) obj;
		if (administratif == null) {
			if (other.administratif != null)
				return false;
		} else if (!administratif.equals(other.administratif))
			return false;
		if (libelle == null) {
			if (other.libelle != null)
				return false;
		} else if (!libelle.equals(other.libelle))
			return false;
		if (commercial == null) {
			if (other.commercial != null)
				return false;
		} else if (!commercial.equals(other.commercial))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
