package fr.legrain.bdg.welcome.webapp;

import java.io.Serializable;

public class Document implements Serializable{
	
	private String titre;
	private String description;
	
	public Document(String titre, String description) {
		super();
		this.titre = titre;
		this.description = description;
	}
	public String getTitre() {
		return titre;
	}
	public void setTitre(String titre) {
		this.titre = titre;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

}
