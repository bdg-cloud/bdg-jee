package fr.legrain.bdg.webapp.app;

import fr.legrain.tiers.model.TaTelephone;

public class SmsParam {
	
	public static final String NOM_OBJET_EN_SESSION = "paramSms";
	
	private String numeroExpediteur;
	private String nomExpediteur;
	private String subject;
	private String bodyPlain;
	private String bodyHtml;
	private String[] destinataires;
	private TaTelephone[] telephoneDestinataires;

	
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getBodyPlain() {
		return bodyPlain;
	}
	public void setBodyPlain(String bodyPlain) {
		this.bodyPlain = bodyPlain;
	}
	public String getBodyHtml() {
		return bodyHtml;
	}
	public void setBodyHtml(String bodyHtml) {
		this.bodyHtml = bodyHtml;
	}

	public String[] getDestinataires() {
		return destinataires;
	}
	public void setDestinataires(String[] destinataires) {
		this.destinataires = destinataires;
	}

	public TaTelephone[] getTelephoneDestinataires() {
		return telephoneDestinataires;
	}
	public void setTelephoneDestinataires(TaTelephone[] emailDestinataires) {
		this.telephoneDestinataires = emailDestinataires;
	}

	public String getNumeroExpediteur() {
		return numeroExpediteur;
	}
	public void setNumeroExpediteur(String emailExpediteur) {
		this.numeroExpediteur = emailExpediteur;
	}
	public String getNomExpediteur() {
		return nomExpediteur;
	}
	public void setNomExpediteur(String nomExpediteur) {
		this.nomExpediteur = nomExpediteur;
	}
}
