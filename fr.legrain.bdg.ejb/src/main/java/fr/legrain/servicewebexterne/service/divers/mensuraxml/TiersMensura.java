package fr.legrain.servicewebexterne.service.divers.mensuraxml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class TiersMensura {
	@XmlElement (name="CODECLIENT")
	 private String CODECLIENT;
	@XmlElement (name="CODECATEGORIE")
	 private String CODECATEGORIE;
	@XmlElement (name="STATUSCLIENT")
	 private String STATUSCLIENT;
	@XmlElement (name="NOM")
	 private String NOM;
	@XmlElement (name="VILLE")
	 private String VILLE;
	 
	 
	 
	 
	public String getCODECLIENT() {
		return CODECLIENT;
	}
	public void setCODECLIENT(String cODECLIENT) {
		CODECLIENT = cODECLIENT;
	}
	public String getCODECATEGORIE() {
		return CODECATEGORIE;
	}
	public void setCODECATEGORIE(String cODECATEGORIE) {
		CODECATEGORIE = cODECATEGORIE;
	}
	public String getSTATUSCLIENT() {
		return STATUSCLIENT;
	}
	public void setSTATUSCLIENT(String sTATUSCLIENT) {
		STATUSCLIENT = sTATUSCLIENT;
	}
	public String getNOM() {
		return NOM;
	}
	public void setNOM(String nOM) {
		NOM = nOM;
	}
	public String getVILLE() {
		return VILLE;
	}
	public void setVILLE(String vILLE) {
		VILLE = vILLE;
	}
}
