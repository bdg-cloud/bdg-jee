package fr.legrain.bdg.welcome.webapp;

import java.io.Serializable;

public class DescFonctionnalites implements Serializable{

	public DescFonctionnalites(String descfonc, String etatfonc, String commentaire) {
		super();
		this.descfonc = descfonc;
		this.etatfonc = etatfonc;
		this.commentaire = commentaire;
	}
	
	private String descfonc;
	private String etatfonc; 
	private String commentaire; 

	public String getDescFonc() {
		return descfonc;
	}

	public void setDescFonc(String descfonc) {
		this.descfonc = descfonc;
	}

	public String getEtatFonc() {
		return etatfonc;
	}

	public void setEtatFonc(String etatfonc) {
		this.etatfonc = etatfonc;
	}

	public String getCommentaire() {
		return commentaire;
	}

	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}

}