package fr.legrain.document.dto;

import java.util.ArrayList;
import java.util.List;

import fr.legrain.bdg.model.ModelObject;

public class DocumentEditableDTO extends ModelObject {
	private Integer id;
	private String codeDocumentDoc;
	private String libelleDocumentDoc;
	private String cheminModelDocumentDoc;
	private String cheminCorrespDocumentDoc;
	private Boolean actif;
	private Boolean defaut;
	private String typeLogiciel;
	private String codeTypeDoc;
	
	private Integer versionObj;
	
	private List<DocumentEditableDTO> list = null;
	private List<TaTDocDTO> taTDoc = null;
	
	public List<DocumentEditableDTO> getList() {
		if(list==null)
			list = new ArrayList<DocumentEditableDTO>();
		return list;
	}

	public void setList(List<DocumentEditableDTO> list) {
		firePropertyChange("list", this.list, this.list = list);
	}
	
	public List<TaTDocDTO> getTaTDoc() {
//		if(liste==null)
//			liste = new ArrayList<SWTTDoc>(2);
//		liste.add(new SWTTDoc("a","b"));
//		liste.add(new SWTTDoc("c","d"));
		return taTDoc;
	}

	public void setTaTDoc(List<TaTDocDTO> taTDoc) {
		firePropertyChange("taTDoc", this.taTDoc, this.taTDoc = taTDoc);
	}
	
	public DocumentEditableDTO(){
	}
	
	public Integer getId() {
		return id;
	}
	public void setIdDocumentDoc(Integer idDocumentDoc) {
		firePropertyChange("idDocumentDoc", this.id, this.id = idDocumentDoc);
	}
	public String getCodeDocumentDoc() {
		return codeDocumentDoc;
	}
	public void setCodeDocumentDoc(String codeDocumentDoc) {
		firePropertyChange("codeDocumentDoc", this.codeDocumentDoc, this.codeDocumentDoc = codeDocumentDoc);
	}
	public String getCheminModelDocumentDoc() {
		return cheminModelDocumentDoc;
	}
	public void setCheminModelDocumentDoc(String cheminModelDocumentDoc) {
		firePropertyChange("cheminModelDocumentDoc", this.cheminModelDocumentDoc, this.cheminModelDocumentDoc = cheminModelDocumentDoc);
	}
	public String getLibelleDocumentDoc() {
		return libelleDocumentDoc;
	}

	public void setLibelleDocumentDoc(String libelleDocumentDoc) {
		firePropertyChange("libelleDocumentDoc", this.libelleDocumentDoc, this.libelleDocumentDoc = libelleDocumentDoc);
	}

	public String getCheminCorrespDocumentDoc() {
		return cheminCorrespDocumentDoc;
	}

	public void setCheminCorrespDocumentDoc(String cheminCorrespDocumentDoc) {
		firePropertyChange("cheminCorrespDocumentDoc", this.cheminCorrespDocumentDoc, this.cheminCorrespDocumentDoc = cheminCorrespDocumentDoc);
	}

	public Boolean getActif() {
		return actif;
	}

	public void setActif(Boolean actif) {
		firePropertyChange("actif", this.actif, this.actif = actif);
	}
	
	public Boolean getDefaut() {
		return defaut;
	}

	public void setDefaut(Boolean defaut) {
		firePropertyChange("defaut", this.defaut, this.defaut = defaut);
	}
	
	public String getTypeLogiciel() {
		return typeLogiciel;
	}

	public void setTypeLogiciel(String typeLogiciel) {
		firePropertyChange("typeLogiciel", this.typeLogiciel, this.typeLogiciel = typeLogiciel);
	}
	
	public String getCodeTypeDoc() {
		return codeTypeDoc;
	}

	public void setCodeTypeDoc(String codeTypeDoc) {
		firePropertyChange("codeTypeDoc", this.codeTypeDoc, this.codeTypeDoc = codeTypeDoc);
	}
	
	public Integer getVersionObj() {
		return this.versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}
	
	
	public static DocumentEditableDTO copy(DocumentEditableDTO swtDocumentEditable){
		DocumentEditableDTO swtDocumentEditableLoc = new DocumentEditableDTO();
		swtDocumentEditableLoc.setCodeDocumentDoc(swtDocumentEditable.codeDocumentDoc);
		swtDocumentEditableLoc.setIdDocumentDoc(swtDocumentEditable.id);
		swtDocumentEditableLoc.setCheminModelDocumentDoc(swtDocumentEditable.cheminModelDocumentDoc);
		swtDocumentEditableLoc.setCheminCorrespDocumentDoc(swtDocumentEditable.cheminCorrespDocumentDoc);
		swtDocumentEditableLoc.setLibelleDocumentDoc(swtDocumentEditable.libelleDocumentDoc);
		swtDocumentEditableLoc.setActif(swtDocumentEditable.actif);
		swtDocumentEditableLoc.setDefaut(swtDocumentEditable.defaut);
		swtDocumentEditableLoc.setTypeLogiciel(swtDocumentEditable.typeLogiciel);
		swtDocumentEditableLoc.setCodeTypeDoc(swtDocumentEditable.codeTypeDoc);
		return swtDocumentEditableLoc;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((actif == null) ? 0 : actif.hashCode());
		result = prime
				* result
				+ ((cheminCorrespDocumentDoc == null) ? 0 : cheminCorrespDocumentDoc
						.hashCode());
		result = prime
				* result
				+ ((cheminModelDocumentDoc == null) ? 0 : cheminModelDocumentDoc
						.hashCode());
		result = prime * result
				+ ((codeDocumentDoc == null) ? 0 : codeDocumentDoc.hashCode());
		result = prime * result + ((defaut == null) ? 0 : defaut.hashCode());
		result = prime * result
				+ ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((libelleDocumentDoc == null) ? 0 : libelleDocumentDoc.hashCode());
		result = prime * result
				+ ((typeLogiciel == null) ? 0 : typeLogiciel.hashCode());
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
		DocumentEditableDTO other = (DocumentEditableDTO) obj;
		if (actif == null) {
			if (other.actif != null)
				return false;
		} else if (!actif.equals(other.actif))
			return false;
		if (cheminCorrespDocumentDoc == null) {
			if (other.cheminCorrespDocumentDoc != null)
				return false;
		} else if (!cheminCorrespDocumentDoc.equals(other.cheminCorrespDocumentDoc))
			return false;
		if (cheminModelDocumentDoc == null) {
			if (other.cheminModelDocumentDoc != null)
				return false;
		} else if (!cheminModelDocumentDoc.equals(other.cheminModelDocumentDoc))
			return false;
		if (codeDocumentDoc == null) {
			if (other.codeDocumentDoc != null)
				return false;
		} else if (!codeDocumentDoc.equals(other.codeDocumentDoc))
			return false;
		if (defaut == null) {
			if (other.defaut != null)
				return false;
		} else if (!defaut.equals(other.defaut))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (libelleDocumentDoc == null) {
			if (other.libelleDocumentDoc != null)
				return false;
		} else if (!libelleDocumentDoc.equals(other.libelleDocumentDoc))
			return false;
		if (typeLogiciel == null) {
			if (other.typeLogiciel != null)
				return false;
		} else if (!typeLogiciel.equals(other.typeLogiciel))
			return false;
		return true;
	}

}
