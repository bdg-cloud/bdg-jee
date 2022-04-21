package fr.legrain.bdg.webapp.documents;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@SessionScoped
public class DocFusionAImprimer implements Serializable {

	private static final long serialVersionUID = -5640884251207851208L;
	
	private String fichierAImprimer = null;

	public String getFichierAImprimer() {
		return fichierAImprimer;
	}

	public void setFichierAImprimer(String fichierAImprimer) {
		this.fichierAImprimer = fichierAImprimer;
	}
}
