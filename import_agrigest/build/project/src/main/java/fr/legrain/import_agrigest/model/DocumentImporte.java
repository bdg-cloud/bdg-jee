package fr.legrain.import_agrigest.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name ="DocumentImporte")
public class DocumentImporte implements java.io.Serializable {

		
	/**
	 * 
	 */
	private static final long serialVersionUID = -7723648073396642787L;
	
	
	String codeDocumentBdg;
	String codeDocumentAgrigest;
	Date document;
	String dossier;
	
	
	
	public String getCodeDocumentBdg() {
		return codeDocumentBdg;
	}
	public void setCodeDocumentBdg(String codeDocumentBdg) {
		this.codeDocumentBdg = codeDocumentBdg;
	}
	public String getCodeDocumentAgrigest() {
		return codeDocumentAgrigest;
	}
	public void setCodeDocumentAgrigest(String codeDocumentAgrigest) {
		this.codeDocumentAgrigest = codeDocumentAgrigest;
	}
	public Date getDocument() {
		return document;
	}
	public void setDocument(Date document) {
		this.document = document;
	}
	public String getDossier() {
		return dossier;
	}
	public void setDossier(String dossier) {
		this.dossier = dossier;
	}

	
	

	
	
	
	
	
	
}
