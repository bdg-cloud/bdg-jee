package fr.legrain.bdg.webapp.app;

import java.io.Serializable;

import fr.legrain.tiers.model.TaEmail;

public class EmailParam implements Serializable {
	
	public static final String NOM_OBJET_EN_SESSION = "paramEmail";
	
	private String emailExpediteur;
	private String nomExpediteur;
	private String subject;
	private String bodyPlain;
	private String bodyHtml;
	private EmailPieceJointeParam[] piecesJointes;
	private String[] destinataires;
	private String[] cc;
	private String[] bcc;
	private TaEmail[] emailDestinataires;
	private TaEmail[] emailCc;
	private TaEmail[] emailBcc;
	
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
	public EmailPieceJointeParam[] getPiecesJointes() {
		return piecesJointes;
	}
	public void setPiecesJointes(EmailPieceJointeParam[] piecesJointes) {
		this.piecesJointes = piecesJointes;
	}
	public String[] getDestinataires() {
		return destinataires;
	}
	public void setDestinataires(String[] destinataires) {
		this.destinataires = destinataires;
	}
	public String[] getCc() {
		return cc;
	}
	public void setCc(String[] cc) {
		this.cc = cc;
	}
	public String[] getBcc() {
		return bcc;
	}
	public void setBcc(String[] bcc) {
		this.bcc = bcc;
	}
	public TaEmail[] getEmailDestinataires() {
		return emailDestinataires;
	}
	public void setEmailDestinataires(TaEmail[] emailDestinataires) {
		this.emailDestinataires = emailDestinataires;
	}
	public TaEmail[] getEmailCc() {
		return emailCc;
	}
	public void setEmailCc(TaEmail[] emailCc) {
		this.emailCc = emailCc;
	}
	public TaEmail[] getEmailBcc() {
		return emailBcc;
	}
	public void setEmailBcc(TaEmail[] emailBcc) {
		this.emailBcc = emailBcc;
	}
	public String getEmailExpediteur() {
		return emailExpediteur;
	}
	public void setEmailExpediteur(String emailExpediteur) {
		this.emailExpediteur = emailExpediteur;
	}
	public String getNomExpediteur() {
		return nomExpediteur;
	}
	public void setNomExpediteur(String nomExpediteur) {
		this.nomExpediteur = nomExpediteur;
	}
}
