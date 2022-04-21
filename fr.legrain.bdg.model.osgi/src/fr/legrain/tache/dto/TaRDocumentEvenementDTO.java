package fr.legrain.tache.dto;


import fr.legrain.document.model.TaAcompte;
import fr.legrain.document.model.TaAvoir;
import fr.legrain.lib.data.ModelObject;


public class TaRDocumentEvenementDTO extends ModelObject implements java.io.Serializable {

	private static final long serialVersionUID = 1545417510168030716L;
	
	private Integer id=0;
	private TaAcompte taAcompte;
	private TaAvoir taAvoir;
	private Integer idDocument=0;
	private String codeDocument;
	private String libelleDocument;
	private String typeDocument;

	public TaRDocumentEvenementDTO() {
	}

	public static TaRDocumentEvenementDTO copy(TaRDocumentEvenementDTO taRDocumentEvenementDTO){
		TaRDocumentEvenementDTO taRDocumentEvenementDTOLoc = new TaRDocumentEvenementDTO();
		taRDocumentEvenementDTOLoc.setId(taRDocumentEvenementDTO.id);
		taRDocumentEvenementDTOLoc.setTaAcompte(taRDocumentEvenementDTO.taAcompte);
		taRDocumentEvenementDTOLoc.setIdDocument(taRDocumentEvenementDTO.idDocument);
		taRDocumentEvenementDTOLoc.setCodeDocument(taRDocumentEvenementDTO.codeDocument);
		taRDocumentEvenementDTOLoc.setLibelleDocument(taRDocumentEvenementDTO.libelleDocument);
		taRDocumentEvenementDTOLoc.setTypeDocument(taRDocumentEvenementDTO.typeDocument);
		return taRDocumentEvenementDTOLoc;
	}
	
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		firePropertyChange("id", this.id, this.id = id);
	}

	public TaAcompte getTaAcompte() {
		return taAcompte;
	}

	public void setTaAcompte(TaAcompte taAcompte) {
		firePropertyChange("taAcompte", this.taAcompte, this.taAcompte = taAcompte);
	}

	public String getCodeDocument() {
		return codeDocument;
	}

	public void setCodeDocument(String codeDocument) {
		firePropertyChange("codeDocument", this.codeDocument, this.codeDocument = codeDocument);
	}


	public String getLibelleDocument() {
		return libelleDocument;
	}

	public void setLibelleDocument(String libelleDocument) {
		firePropertyChange("libelleDocument", this.libelleDocument, this.libelleDocument = libelleDocument);
	}

	public String getTypeDocument() {
		return typeDocument;
	}

	public void setTypeDocument(String typeDocument) {
		firePropertyChange("typeDocument", this.typeDocument, this.typeDocument = typeDocument);
	}

	public Integer getIdDocument() {
		return idDocument;
	}

	public void setIdDocument(Integer idDocument) {
		firePropertyChange("idDocument", this.idDocument, this.idDocument = idDocument);
	}

	public TaAvoir getTaAvoir() {
		return taAvoir;
	}

	public void setTaAvoir(TaAvoir taAvoir) {
		firePropertyChange("taAvoir", this.taAvoir, this.taAvoir = taAvoir);

	}

}
