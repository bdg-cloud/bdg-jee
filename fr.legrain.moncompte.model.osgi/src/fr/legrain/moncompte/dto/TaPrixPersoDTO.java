package fr.legrain.moncompte.dto;

import java.math.BigDecimal;

import javax.persistence.Column;

import fr.legrain.moncompte.commun.model.ModelObject;


public class TaPrixPersoDTO extends ModelObject implements java.io.Serializable {

	private static final long serialVersionUID = -1159785041426015863L;
	
	private Integer id;
	private BigDecimal prixHT;
	private BigDecimal tva;
	private BigDecimal prixTTC;
	
	private BigDecimal prixHTLight;

	private BigDecimal tvaLight;

	private BigDecimal prixTTCLight;
	
	private BigDecimal prixHTParPosteInstalle;

	private BigDecimal tvaParPosteInstalle;

	private BigDecimal prixTTCParPosteInstalle;
	
	private BigDecimal prixHTWs;

	private BigDecimal tvaWs;

	private BigDecimal prixTTCWs;
	
	private BigDecimal tauxTVA;

	private Integer versionObj;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		firePropertyChange("id", this.id, this.id = id);
	}

	public Integer getVersionObj() {
		return versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

	public BigDecimal getPrixHT() {
		return prixHT;
	}

	public void setPrixHT(BigDecimal prixHT) {
		firePropertyChange("prixHT", this.prixHT, this.prixHT = prixHT);
	}

	public BigDecimal getTauxTVA() {
		return tauxTVA;
	}

	public void setTauxTVA(BigDecimal tauxTVA) {
		firePropertyChange("tauxTVA", this.tauxTVA, this.tauxTVA = tauxTVA);
	}

	public BigDecimal getPrixTTC() {
		return prixTTC;
	}

	public void setPrixTTC(BigDecimal prixTTC) {
		firePropertyChange("prixTTC", this.prixTTC, this.prixTTC = prixTTC);
	}

	public BigDecimal getTva() {
		return tva;
	}

	public void setTva(BigDecimal tva) {
		firePropertyChange("tva", this.tva, this.tva = tva);
	}

	public BigDecimal getPrixHTLight() {
		return prixHTLight;
	}

	public void setPrixHTLight(BigDecimal prixHTLight) {
		firePropertyChange("prixHTLight", this.prixHTLight, this.prixHTLight = prixHTLight);
	}

	public BigDecimal getTvaLight() {
		return tvaLight;
	}

	public void setTvaLight(BigDecimal tvaLight) {
		firePropertyChange("tvaLight", this.tvaLight, this.tvaLight = tvaLight);
	}

	public BigDecimal getPrixTTCLight() {
		return prixTTCLight;
	}

	public void setPrixTTCLight(BigDecimal prixTTCLight) {
		firePropertyChange("prixTTCLight", this.prixTTCLight, this.prixTTCLight = prixTTCLight);
	}

	public BigDecimal getPrixHTParPosteInstalle() {
		return prixHTParPosteInstalle;
	}

	public void setPrixHTParPosteInstalle(BigDecimal prixHTParPosteInstalle) {
		firePropertyChange("prixHTParPosteInstalle", this.prixHTParPosteInstalle, this.prixHTParPosteInstalle = prixHTParPosteInstalle);
	}

	public BigDecimal getTvaParPosteInstalle() {
		return tvaParPosteInstalle;
	}

	public void setTvaParPosteInstalle(BigDecimal tvaParPosteInstalle) {
		firePropertyChange("tvaParPosteInstalle", this.tvaParPosteInstalle, this.tvaParPosteInstalle = tvaParPosteInstalle);
	}

	public BigDecimal getPrixTTCParPosteInstalle() {
		return prixTTCParPosteInstalle;
	}

	public void setPrixTTCParPosteInstalle(BigDecimal prixTTCParPosteInstalle) {
		firePropertyChange("prixTTCParPosteInstalle", this.prixTTCParPosteInstalle, this.prixTTCParPosteInstalle = prixTTCParPosteInstalle);
	}

	public BigDecimal getPrixHTWs() {
		return prixHTWs;
	}

	public void setPrixHTWs(BigDecimal prixHTWs) {
		firePropertyChange("prixHTWs", this.prixHTWs, this.prixHTWs = prixHTWs);
	}

	public BigDecimal getTvaWs() {
		return tvaWs;
	}

	public void setTvaWs(BigDecimal tvaWs) {
		firePropertyChange("tvaWs", this.tvaWs, this.tvaWs = tvaWs);
	}

	public BigDecimal getPrixTTCWs() {
		return prixTTCWs;
	}

	public void setPrixTTCWs(BigDecimal prixTTCWs) {
		firePropertyChange("prixTTCWs", this.prixTTCWs, this.prixTTCWs = prixTTCWs);
	}



}
