package fr.legrain.tiers.model;

import fr.legrain.tiers.model.TaTiers;

public class TiersDossier {
	private String codeDossier;
	private TaTiers taTiers;
	
	public TiersDossier() {
		
	}
	
	public TiersDossier(String codeDossier, TaTiers taTiers) {
		this.codeDossier = codeDossier;
		this.taTiers = taTiers;
	}
	
	public String getCodeDossier() {
		return codeDossier;
	}
	public void setCodeDossier(String codeDossier) {
		this.codeDossier = codeDossier;
	}
	public TaTiers getTaTiers() {
		return taTiers;
	}
	public void setTaTiers(TaTiers taTiers) {
		this.taTiers = taTiers;
	}
}
