package fr.legrain.document.model;

import java.io.Serializable;

import fr.legrain.document.dto.IDocumentTiers;

public class RetourGenerationDoc implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4117643441729240913L;

	private IDocumentTiers doc;
	private Integer idDoc;
	private boolean retour=true;
	private String message="";
	private String typeTabCSS;
	private boolean ouvrirDoc=true;
	public IDocumentTiers getDoc() {
		return doc;
	}
	public void setDoc(IDocumentTiers doc) {
		this.doc = doc;
	}
	public boolean isRetour() {
		return retour;
	}
	public void setRetour(boolean retour) {
		this.retour = retour;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getTypeTabCSS() {
		return typeTabCSS;
	}
	public void setTypeTabCSS(String typeTabCSS) {
		this.typeTabCSS = typeTabCSS;
	}
	public boolean isOuvrirDoc() {
		return ouvrirDoc;
	}
	public void setOuvrirDoc(boolean ouvrirDoc) {
		this.ouvrirDoc = ouvrirDoc;
	}
	public Integer getIdDoc() {
		return idDoc;
	}
	public void setIdDoc(Integer idDoc) {
		this.idDoc = idDoc;
	}
	
	
}
