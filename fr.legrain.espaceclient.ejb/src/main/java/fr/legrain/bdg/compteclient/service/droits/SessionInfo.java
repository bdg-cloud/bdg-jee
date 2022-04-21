package fr.legrain.bdg.compteclient.service.droits;

import java.io.Serializable;
import java.util.Locale;

import javax.enterprise.context.SessionScoped;

import fr.legrain.bdg.compteclient.model.droits.TaUtilisateur;

@SessionScoped
public class SessionInfo implements Serializable{

	private static final long serialVersionUID = 5067572320207556384L;

	private TaUtilisateur utilisateur;
	
	private String ipAddress;
	private String sessionID;
	private Locale sessionLangue;
	
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public TaUtilisateur getUtilisateur() {
		return utilisateur;
	}
	public void setUtilisateur(TaUtilisateur user) {
		this.utilisateur = user;
	}
	public String getSessionID() {
		return sessionID;
	}
	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}
	public Locale getSessionLangue() {
		return sessionLangue;
	}
	public void setSessionLangue(Locale sessionLangue) {
		this.sessionLangue = sessionLangue;
	}
	
}
