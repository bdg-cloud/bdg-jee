package fr.legrain.dossier.service;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.enterprise.context.SessionScoped;

import fr.legrain.dossier.model.TaPreferences;

@SessionScoped
public class PreferencesDossier implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4040191935604794618L;
	
	
	Map<String, TaPreferences> mapPreferences=new LinkedHashMap<>();
	
	
	public Map<String, TaPreferences> getMapPreferences() {
		return mapPreferences;
	}
	public void setMapPreferences(Map<String, TaPreferences> mapPreferences) {
		this.mapPreferences = mapPreferences;
	}
	
	

	protected Integer nbMaxChargeListeArticle=10;
	protected Integer nbMaxChargeListeTiers=10;
	protected boolean grosDossier=false;
	protected boolean grosFichierArticle=false;
	protected boolean grosFichierTiers=false;


	

public Integer getNbMaxChargeListeArticle() {
	return nbMaxChargeListeArticle;
}

public void setNbMaxChargeListeArticle(Integer nbMaxChargeListeArticle) {
	this.nbMaxChargeListeArticle = nbMaxChargeListeArticle;
}

public Integer getNbMaxChargeListeTiers() {
	return nbMaxChargeListeTiers;
}

public void setNbMaxChargeListeTiers(Integer nbMaxChargeListeTiers) {
	this.nbMaxChargeListeTiers = nbMaxChargeListeTiers;
}

public boolean isGrosDossier() {
	return grosDossier;
}

public void setGrosDossier(boolean grosDossier) {
	this.grosDossier = grosDossier;
}

public boolean isGrosFichierArticle() {
	return grosFichierArticle;
}

public void setGrosFichierArticle(boolean grosFichierArticle) {
	this.grosFichierArticle = grosFichierArticle;
}

public boolean isGrosFichierTiers() {
	return grosFichierTiers;
}

public void setGrosFichierTiers(boolean grosFichierTiers) {
	this.grosFichierTiers = grosFichierTiers;
}

}
