package fr.legrain.edition.dto;

import java.util.Date;

import fr.legrain.bdg.model.ModelObject;
import fr.legrain.edition.model.TaTEdition;

public class TaEditionCatalogueDTO extends ModelObject implements java.io.Serializable {

	private static final long serialVersionUID = 3515524162891052950L;

	private Integer id;

	private String codeEdition;
	private String libelleEdition;
	private String descriptionEdition;
//	private TaTEdition taTEdition;
	private byte[] miniature;
	private byte[] fichierBlob;
	private String fichierChemin;
	private String fichierNom;
	private Boolean defaut = false;
	private Boolean actif = false;
	private Boolean systeme = false;
	private Boolean importationManuelle = false;
	private Date dateImportation;
	private String versionEdition;
	private String codeDossierEditionPersonalisee;
	private Boolean payant = false; 
	
	private String resourcesPath;
	private String theme;
	private String librairie;
	
	private Integer idTEdition;
	private String codeTEdition;
	
	private String etatTelechargement; //Transiant
	
	private Integer versionObj;

	public TaEditionCatalogueDTO() {
	}
	
	

	public TaEditionCatalogueDTO(Integer id, String codeEdition, String libelleEdition, String descriptionEdition,
			byte[] miniature, Boolean defaut, Boolean actif, Boolean systeme, String versionEdition, Integer idTEdition,
			String codeTEdition) {
		super();
		this.id = id;
		this.codeEdition = codeEdition;
		this.libelleEdition = libelleEdition;
		this.descriptionEdition = descriptionEdition;
		this.miniature = miniature;
		this.defaut = defaut;
		this.actif = actif;
		this.systeme = systeme;
		this.versionEdition = versionEdition;
		this.idTEdition = idTEdition;
		this.codeTEdition = codeTEdition;
	}



	public void setTaCompteServiceWebExterneDTO(TaEditionCatalogueDTO taEditionDTO) {
		this.id = taEditionDTO.id;
		this.codeEdition = taEditionDTO.codeEdition;
		this.libelleEdition = taEditionDTO.libelleEdition;
		this.descriptionEdition = taEditionDTO.descriptionEdition;
		this.miniature = taEditionDTO.miniature;
		this.fichierBlob = taEditionDTO.fichierBlob;
		this.fichierChemin = taEditionDTO.fichierChemin;
		this.defaut = taEditionDTO.defaut;
		this.actif = taEditionDTO.actif;
		this.systeme = taEditionDTO.systeme;
		this.importationManuelle = taEditionDTO.importationManuelle;
		this.dateImportation = taEditionDTO.dateImportation;
		this.idTEdition = taEditionDTO.idTEdition;
		this.codeTEdition = taEditionDTO.codeTEdition;
	}

	public static TaEditionCatalogueDTO copy(TaEditionCatalogueDTO taEditionDTO){
		TaEditionCatalogueDTO taEditionDTOLoc = new TaEditionCatalogueDTO();
		taEditionDTOLoc.setId(taEditionDTO.getId());              
		taEditionDTOLoc.setCodeEdition(taEditionDTO.getCodeEdition());
		taEditionDTOLoc.setLibelleEdition(taEditionDTO.getLibelleEdition());
		taEditionDTOLoc.setDescriptionEdition(taEditionDTO.getDescriptionEdition());
		taEditionDTOLoc.setMiniature(taEditionDTO.getMiniature());
		taEditionDTOLoc.setFichierBlob(taEditionDTO.getFichierBlob());
		taEditionDTOLoc.setFichierChemin(taEditionDTO.getFichierChemin());
		taEditionDTOLoc.setDefaut(taEditionDTO.getDefaut());
		taEditionDTOLoc.setActif(taEditionDTO.getActif());
		taEditionDTOLoc.setSysteme(taEditionDTO.getSysteme());
		taEditionDTOLoc.setImportationManuelle(taEditionDTO.getImportationManuelle());
		taEditionDTOLoc.setDateImportation(taEditionDTO.getDateImportation());
		taEditionDTOLoc.setIdTEdition(taEditionDTO.getIdTEdition());
		taEditionDTOLoc.setCodeTEdition(taEditionDTO.getCodeTEdition());

		return taEditionDTOLoc;
	}
	
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		firePropertyChange("id", this.id, this.id = id);
	}
	
	public Integer getVersionObj() {
		return this.versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

	public String getCodeEdition() {
		return codeEdition;
	}

	public void setCodeEdition(String codeEdition) {
		this.codeEdition = codeEdition;
	}

	public String getLibelleEdition() {
		return libelleEdition;
	}

	public void setLibelleEdition(String libelleEdition) {
		this.libelleEdition = libelleEdition;
	}

	public String getDescriptionEdition() {
		return descriptionEdition;
	}

	public void setDescriptionEdition(String descriptionEdition) {
		this.descriptionEdition = descriptionEdition;
	}

//	public TaTEdition getTaTEdition() {
//		return taTEdition;
//	}
//
//	public void setTaTEdition(TaTEdition taTEdition) {
//		this.taTEdition = taTEdition;
//	}

	public byte[] getMiniature() {
		return miniature;
	}

	public void setMiniature(byte[] miniature) {
		this.miniature = miniature;
	}

	public byte[] getFichierBlob() {
		return fichierBlob;
	}

	public void setFichierBlob(byte[] fichierBlob) {
		this.fichierBlob = fichierBlob;
	}

	public String getFichierChemin() {
		return fichierChemin;
	}

	public void setFichierChemin(String fichierChemin) {
		this.fichierChemin = fichierChemin;
	}

	public Boolean getDefaut() {
		return defaut;
	}

	public void setDefaut(Boolean defaut) {
		this.defaut = defaut;
	}

	public Boolean getActif() {
		return actif;
	}

	public void setActif(Boolean actif) {
		this.actif = actif;
	}

	public Boolean getSysteme() {
		return systeme;
	}

	public void setSysteme(Boolean systeme) {
		this.systeme = systeme;
	}

	public Boolean getImportationManuelle() {
		return importationManuelle;
	}

	public void setImportationManuelle(Boolean importationManuelle) {
		this.importationManuelle = importationManuelle;
	}

	public Date getDateImportation() {
		return dateImportation;
	}

	public void setDateImportation(Date dateImportation) {
		this.dateImportation = dateImportation;
	}

	public Integer getIdTEdition() {
		return idTEdition;
	}

	public void setIdTEdition(Integer idTEdition) {
		this.idTEdition = idTEdition;
	}

	public String getCodeTEdition() {
		return codeTEdition;
	}

	public void setCodeTEdition(String codeTEdition) {
		this.codeTEdition = codeTEdition;
	}

	public String getFichierNom() {
		return fichierNom;
	}

	public void setFichierNom(String fichierNom) {
		this.fichierNom = fichierNom;
	}

	public String getVersionEdition() {
		return versionEdition;
	}

	public void setVersionEdition(String versionEdition) {
		this.versionEdition = versionEdition;
	}



	public String getCodeDossierEditionPersonalisee() {
		return codeDossierEditionPersonalisee;
	}



	public void setCodeDossierEditionPersonalisee(String codeDossierEditionPersonalisee) {
		this.codeDossierEditionPersonalisee = codeDossierEditionPersonalisee;
	}



	public Boolean getPayant() {
		return payant;
	}



	public void setPayant(Boolean payant) {
		this.payant = payant;
	}



	public String getEtatTelechargement() {
		return etatTelechargement;
	}



	public void setEtatTelechargement(String etatTelechargement) {
		this.etatTelechargement = etatTelechargement;
	}



	public String getResourcesPath() {
		return resourcesPath;
	}



	public void setResourcesPath(String resourcesPath) {
		this.resourcesPath = resourcesPath;
	}



	public String getTheme() {
		return theme;
	}



	public void setTheme(String theme) {
		this.theme = theme;
	}



	public String getLibrairie() {
		return librairie;
	}



	public void setLibrairie(String librairie) {
		this.librairie = librairie;
	}

	
}
