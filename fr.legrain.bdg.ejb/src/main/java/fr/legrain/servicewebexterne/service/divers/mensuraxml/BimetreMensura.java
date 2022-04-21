package fr.legrain.servicewebexterne.service.divers.mensuraxml;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="BIMETRE")
public class BimetreMensura {
	
	@XmlElement(name="DOCUMENTS")
	private ListeDocumentMensura listeDocumentMensura;

	public ListeDocumentMensura getListeDocumentMensura() {
		return listeDocumentMensura;
	}

	public void setListeDocumentMensura(ListeDocumentMensura listeDocumentMensura) {
		this.listeDocumentMensura = listeDocumentMensura;
	}

}
