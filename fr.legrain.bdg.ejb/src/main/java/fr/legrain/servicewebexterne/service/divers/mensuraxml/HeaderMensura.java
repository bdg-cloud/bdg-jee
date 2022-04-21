package fr.legrain.servicewebexterne.service.divers.mensuraxml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

@XmlAccessorType(XmlAccessType.FIELD)
public class HeaderMensura {
	 @XmlElement (name="TYPE_DOCUMENT")
	 private String TYPE_DOCUMENT;
	 @XmlElement (name="LIBELLE")
	 private String LIBELLE;
	 @XmlElement (name="CODEAFFAIRE")
	 private String CODEAFFAIRE;
	 @XmlElement (name="CODEBBIBLIO")
	 private String CODEBBIBLIO;
	 @XmlElement (name="DATEDOC")
	 private String DATEDOC;
	 
	 @XmlElement(name="TIERS")
	 TiersMensura TIERS;

	public String getTYPE_DOCUMENT() {
		return TYPE_DOCUMENT;
	}

	public void setTYPE_DOCUMENT(String tYPE_DOCUMENT) {
		TYPE_DOCUMENT = tYPE_DOCUMENT;
	}

	public String getLIBELLE() {
		return LIBELLE;
	}

	public void setLIBELLE(String lIBELLE) {
		LIBELLE = lIBELLE;
	}

	public String getCODEAFFAIRE() {
		return CODEAFFAIRE;
	}

	public void setCODEAFFAIRE(String cODEAFFAIRE) {
		CODEAFFAIRE = cODEAFFAIRE;
	}

	public String getCODEBBIBLIO() {
		return CODEBBIBLIO;
	}

	public void setCODEBBIBLIO(String cODEBBIBLIO) {
		CODEBBIBLIO = cODEBBIBLIO;
	}

	public String getDATEDOC() {
		return DATEDOC;
	}

	public void setDATEDOC(String dATEDOC) {
		DATEDOC = dATEDOC;
	}

	public TiersMensura getTIERS() {
		return TIERS;
	}

	public void setTIERS(TiersMensura tIERS) {
		TIERS = tIERS;
	}

}
