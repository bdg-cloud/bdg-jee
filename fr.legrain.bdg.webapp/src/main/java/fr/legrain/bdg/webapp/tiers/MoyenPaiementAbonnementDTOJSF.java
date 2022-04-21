package fr.legrain.bdg.webapp.tiers;

import java.io.Serializable;

public class MoyenPaiementAbonnementDTOJSF implements Serializable {

	private static final long serialVersionUID = 1635404526017224589L;

	private String type;
	private String iban;
	private String libelle;
	private String idExterne;
	
	public MoyenPaiementAbonnementDTOJSF(String type, String iban, String libelle, String idExterne) {
		super();
		this.type = type;
		this.iban = iban;
		this.libelle = libelle;
		this.idExterne = idExterne;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getIban() {
		return iban;
	}
	public void setIban(String iban) {
		this.iban = iban;
	}
	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}
	public String getIdExterne() {
		return idExterne;
	}
	public void setIdExterne(String idExterne) {
		this.idExterne = idExterne;
	}

	
}
