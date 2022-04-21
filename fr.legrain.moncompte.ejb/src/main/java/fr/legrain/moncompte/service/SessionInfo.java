package fr.legrain.moncompte.service;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;

import fr.legrain.moncompte.model.TaUtilisateur;


@SessionScoped
public class SessionInfo implements Serializable{

	private static final long serialVersionUID = 5067572320207556384L;

	private TaUtilisateur utilisateur;
	
	private String ipAddress;

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
}
