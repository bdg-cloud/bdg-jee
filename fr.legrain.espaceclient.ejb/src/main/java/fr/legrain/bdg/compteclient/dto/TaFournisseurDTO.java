package fr.legrain.bdg.compteclient.dto;

// Classe DTO (digital transfert object) permet de mettre à plat les objets de l'entité
public class TaFournisseurDTO extends ModelObject implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3458627183938473254L;

	private int idFournisseur;
	private String raisonSocialeFournisseur;	
	private String adresse1Fournisseur;	
	private String adresse2Fournisseur;
	private String adresse3Fournisseur;
	private String codePostalFournisseur;
	private String villeFournisseur;
	private String paysFournisseur;
	private Integer versionObj;
	
	public TaFournisseurDTO() {
		
	}
	

	public int getIdFournisseur() {
		return idFournisseur;
	}
	public void setIdFournisseur(int idFournisseur) {
		this.idFournisseur = idFournisseur;
	}
	public String getRaisonSocialeFournisseur() {
		return raisonSocialeFournisseur;
	}
	public void setRaisonSocialeFournisseur(String raisonSocialeFournisseur) {
		this.raisonSocialeFournisseur = raisonSocialeFournisseur;
	}
	public String getAdresse1Fournisseur() {
		return adresse1Fournisseur;
	}
	public void setAdresse1Fournisseur(String adresse1Fournisseur) {
		this.adresse1Fournisseur = adresse1Fournisseur;
	}
	public String getAdresse2Fournisseur() {
		return adresse2Fournisseur;
	}
	public void setAdresse2Fournisseur(String adresse2Fournisseur) {
		this.adresse2Fournisseur = adresse2Fournisseur;
	}
	public String getAdresse3Fournisseur() {
		return adresse3Fournisseur;
	}
	public void setAdresse3Fournisseur(String adresse3Fournisseur) {
		this.adresse3Fournisseur = adresse3Fournisseur;
	}
	public String getCodePostalFournisseur() {
		return codePostalFournisseur;
	}
	public void setCodePostalFournisseur(String codePostalFournisseur) {
		this.codePostalFournisseur = codePostalFournisseur;
	}
	public String getVilleFournisseur() {
		return villeFournisseur;
	}
	public void setVilleFournisseur(String villeFournisseur) {
		this.villeFournisseur = villeFournisseur;
	}
	public String getPaysFournisseur() {
		return paysFournisseur;
	}
	public void setPaysFournisseur(String paysFournisseur) {
		this.paysFournisseur = paysFournisseur;
	}
	public Integer getVersionObj() {
		return versionObj;
	}
	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}
}
