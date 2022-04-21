package fr.legrain.servicewebexterne.service.divers.mensuraxml;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

@XmlAccessorType(XmlAccessType.FIELD)
public class LigneDevisMensura {
	@XmlElement (name="IDLIG")
	 private String IDLIG;
	@XmlElement (name="IDPARENT")
	 private String IDPARENT;
	@XmlElement (name="TYPELIG")
	 private String TYPELIG;
	@XmlElement (name="NUMEROTATION")
	 private String NUMEROTATION;
	@XmlElement (name="NIVCHAP")
	 private String NIVCHAP;
	@XmlElement (name="CODEARTICLE")
	 private String CODEARTICLE;
	@XmlElement (name="CODE_DANS_BIBLIO")
	 private String CODE_DANS_BIBLIO;
	@XmlElement (name="LIBELLE")
	 private String LIBELLE;
	@XmlElement (name="LIBELLECOM")
	 private String LIBELLECOM;
	@XmlElement (name="LIBELLETEC")
	 private String LIBELLETEC;
	@XmlElement (name="PVHT")
	 private String PVHT;
	@XmlElement (name="TAUXTVA")
	 private String TAUXTVA;
	@XmlElement (name="QUANTITETOT")
	 private String QUANTITETOT;
	@XmlElement (name="UNITE")
	 private String UNITE;
	@XmlElement (name="DECUNITE")
	 private String DECUNITE;
	@XmlElement (name="MONTANTHT")
	 private String MONTANTHT;
	@XmlElement (name="EDITEE")
	 private String EDITEE;
	@XmlElement (name="INFO")
	 private String INFO;
	@XmlElement(name="LIGNE")
	 ArrayList < LigneDevisMensura > LIGNE = new ArrayList < LigneDevisMensura > ();


	 // Getter Methods 

	 public String getIDLIG() {
	  return IDLIG;
	 }

	 public String getIDPARENT() {
	  return IDPARENT;
	 }

	 public String getTYPELIG() {
	  return TYPELIG;
	 }

	 public String getNUMEROTATION() {
	  return NUMEROTATION;
	 }

	 public String getNIVCHAP() {
	  return NIVCHAP;
	 }

	 public String getCODEARTICLE() {
	  return CODEARTICLE;
	 }

	 public String getCODE_DANS_BIBLIO() {
	  return CODE_DANS_BIBLIO;
	 }

	 public String getLIBELLE() {
	  return LIBELLE;
	 }

	 public String getLIBELLECOM() {
	  return LIBELLECOM;
	 }

	 public String getLIBELLETEC() {
	  return LIBELLETEC;
	 }

	 public String getEDITEE() {
	  return EDITEE;
	 }

	 public String getINFO() {
	  return INFO;
	 }

	 // Setter Methods 

	 public void setIDLIG(String IDLIG) {
	  this.IDLIG = IDLIG;
	 }

	 public void setIDPARENT(String IDPARENT) {
	  this.IDPARENT = IDPARENT;
	 }

	 public void setTYPELIG(String TYPELIG) {
	  this.TYPELIG = TYPELIG;
	 }

	 public void setNUMEROTATION(String NUMEROTATION) {
	  this.NUMEROTATION = NUMEROTATION;
	 }

	 public void setNIVCHAP(String NIVCHAP) {
	  this.NIVCHAP = NIVCHAP;
	 }

	 public void setCODEARTICLE(String CODEARTICLE) {
	  this.CODEARTICLE = CODEARTICLE;
	 }

	 public void setCODE_DANS_BIBLIO(String CODE_DANS_BIBLIO) {
	  this.CODE_DANS_BIBLIO = CODE_DANS_BIBLIO;
	 }

	 public void setLIBELLE(String LIBELLE) {
	  this.LIBELLE = LIBELLE;
	 }

	 public void setLIBELLECOM(String LIBELLECOM) {
	  this.LIBELLECOM = LIBELLECOM;
	 }

	 public void setLIBELLETEC(String LIBELLETEC) {
	  this.LIBELLETEC = LIBELLETEC;
	 }

	 public void setEDITEE(String EDITEE) {
	  this.EDITEE = EDITEE;
	 }

	 public void setINFO(String INFO) {
	  this.INFO = INFO;
	 }

	public String getPVHT() {
		return PVHT;
	}

	public void setPVHT(String pVHT) {
		PVHT = pVHT;
	}

	public String getTAUXTVA() {
		return TAUXTVA;
	}

	public void setTAUXTVA(String tAUXTVA) {
		TAUXTVA = tAUXTVA;
	}

	public String getQUANTITETOT() {
		return QUANTITETOT;
	}

	public void setQUANTITETOT(String qUANTITETOT) {
		QUANTITETOT = qUANTITETOT;
	}

	public String getUNITE() {
		return UNITE;
	}

	public void setUNITE(String uNITE) {
		UNITE = uNITE;
	}

	public String getMONTANTHT() {
		return MONTANTHT;
	}

	public void setMONTANTHT(String mONTANTHT) {
		MONTANTHT = mONTANTHT;
	}

	public ArrayList<LigneDevisMensura> getLIGNE() {
		return LIGNE;
	}

	public void setLIGNE(ArrayList<LigneDevisMensura> lIGNE) {
		LIGNE = lIGNE;
	}

	public String getDECUNITE() {
		return DECUNITE;
	}

	public void setDECUNITE(String dECUNITE) {
		DECUNITE = dECUNITE;
	}
	}
