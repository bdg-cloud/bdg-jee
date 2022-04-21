package fr.legrain.controle.dto;

import fr.legrain.bdg.model.ModelObject;


public class TaVerrouModificationDTO extends ModelObject implements java.io.Serializable {
	
	private static final long serialVersionUID = 5971985751766498206L;
	
	private Integer id;
	private String entite;
	private String champ;
	private String valeur;
	
	private Integer versionObj;
	
	public TaVerrouModificationDTO(){
	}
	
	public Integer getId() {
		return id;
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

	
	public TaVerrouModificationDTO(String entite, String champ, String valeur) {
		super();
		this.entite = entite;
		this.champ = champ;
		this.valeur = valeur;
	}

	public static TaVerrouModificationDTO copy(TaVerrouModificationDTO swtTDoc){
		TaVerrouModificationDTO swtTDocLoc = new TaVerrouModificationDTO();
		swtTDocLoc.setId(swtTDoc.id);
		return swtTDocLoc;
	}

	public String getEntite() {
		return entite;
	}

	public void setEntite(String entite) {
		firePropertyChange("entite", this.entite, this.entite = entite);
	}

	public String getChamp() {
		return champ;
	}

	public void setChamp(String champ) {
		firePropertyChange("champ", this.champ, this.champ = champ);
	}

	public String getValeur() {
		return valeur;
	}

	public void setValeur(String valeur) {
		firePropertyChange("valeur", this.valeur, this.valeur = valeur);
	}

}
