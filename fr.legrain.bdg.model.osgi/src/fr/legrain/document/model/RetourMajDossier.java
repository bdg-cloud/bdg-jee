package fr.legrain.document.model;

import java.io.Serializable;

public class RetourMajDossier implements Serializable{



	/**
	 * 
	 */
	private static final long serialVersionUID = 7109070811572769274L;
	
	
	private boolean retour=true;
	private int nbDepart=-1;
	private int nbRetour=-1;
	private String message="";


	public RetourMajDossier(boolean retour) {
		super();
		this.retour = retour;
	}
	public boolean isRetour() {
		return retour;
	}
	public void setRetour(boolean retour) {
		this.retour = retour;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getNbRetour() {
		return nbRetour;
	}
	public void setNbRetour(int nbRetour) {
		this.nbRetour = nbRetour;
	}
	public int getNbDepart() {
		return nbDepart;
	}
	public void setNbDepart(int nbDepart) {
		this.nbDepart = nbDepart;
	}

	
	
}
