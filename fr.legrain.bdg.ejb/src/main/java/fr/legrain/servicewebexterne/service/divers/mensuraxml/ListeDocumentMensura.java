package fr.legrain.servicewebexterne.service.divers.mensuraxml;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

@XmlAccessorType(XmlAccessType.FIELD)
public class ListeDocumentMensura {
	
	@XmlElement(name="DOCUMENT")
	private DocumentDevisMensura DOCUMENT;

	public DocumentDevisMensura getDOCUMENT() {
		return DOCUMENT;
	}

	public void setDOCUMENT(DocumentDevisMensura dOCUMENT) {
		DOCUMENT = dOCUMENT;
	}




}
