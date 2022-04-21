package fr.legrain.servicewebexterne.service.divers.mensuraxml;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class DocumentDevisMensura {
	@XmlElement(name="HEADER")
	private HeaderMensura HEADER;
	@XmlElement(name="BODY")
	private BodyMensura BODY;
	
	
	
	
	public HeaderMensura getHEADER() {
		return HEADER;
	}
	public void setHEADER(HeaderMensura hEADER) {
		HEADER = hEADER;
	}
	public BodyMensura getBODY() {
		return BODY;
	}
	public void setBODY(BodyMensura bODY) {
		BODY = bODY;
	}
	
//	@XmlElement (name="LIBELLE")
//	 private String LIBELLE;
//	
//	@XmlElement (name="CODEAFFAIRE")
//	private String CODEAFFAIRE;
//	@XmlElement (name="CODEBIBLIO")
//	private String CODEBIBLIO;
//	@XmlElement (name="DATEDOC")
//	private String DATEDOC;
//	
//	@XmlElement (name="CODECLIENT")
//	private String CODECLIENT;
//	@XmlElement (name="CODECATEGORIE")
//	private String CODECATEGORIE;
//	@XmlElement (name="STATUSCLIENT")
//	private String STATUSCLIENT;
//	@XmlElement (name="NOM")
//	private String NOM;
//	@XmlElement (name="VILLE")
//	private String VILLE;
//	
//	@XmlElementWrapper(name="LIGNE")
//	 ArrayList < LigneDevisMensura > LIGNE = new ArrayList < LigneDevisMensura > ();
}
