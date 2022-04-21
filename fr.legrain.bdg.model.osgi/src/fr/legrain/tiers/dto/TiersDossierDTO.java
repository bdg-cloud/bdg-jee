package fr.legrain.tiers.dto;

public class TiersDossierDTO {
	private String codeDossier;
	private TaTiersDTO taTiersDTO;
	
	public TiersDossierDTO() {
		
	}
	
	public TiersDossierDTO(String codeDossier, TaTiersDTO taTiers) {
		this.codeDossier = codeDossier;
		this.taTiersDTO = taTiers;
	}
	
	public String getCodeDossier() {
		return codeDossier;
	}
	public void setCodeDossier(String codeDossier) {
		this.codeDossier = codeDossier;
	}
	public TaTiersDTO getTaTiersDTO() {
		return taTiersDTO;
	}
	public void setTaTiersDTO(TaTiersDTO taTiers) {
		this.taTiersDTO = taTiers;
	}
}
