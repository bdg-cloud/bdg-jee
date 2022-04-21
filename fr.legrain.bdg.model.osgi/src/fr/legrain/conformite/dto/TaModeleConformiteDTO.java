package fr.legrain.conformite.dto;

import fr.legrain.bdg.model.ModelObject;
import fr.legrain.conformite.model.TaGroupe;
import fr.legrain.conformite.model.TaTypeConformite;

public class TaModeleConformiteDTO extends ModelObject implements java.io.Serializable {


	private static final long serialVersionUID = 3365278882827100274L;
	
	private Integer id;
	private Integer versionObj;
	
	private String code;
	
	private String valeurDefaut;
	private String deuxiemeValeur;
	private String libelleConformite;
	private String valeurCalculee;
	
	private String codeTypeConformite;
	private Integer idTypeConformite;
//	private TaTypeConformite taTypeConformite;
	
	private String codeGroupe;
	private Integer idGroupe;
//	private TaGroupe taGroupe;
	
	
	
	public TaModeleConformiteDTO() {
		
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



	public String getValeurDefaut() {
		return valeurDefaut;
	}



	public void setValeurDefaut(String valeurDefaut) {
		firePropertyChange("valeurDefaut", this.valeurDefaut, this.valeurDefaut = valeurDefaut);
	}



	public String getDeuxiemeValeur() {
		return deuxiemeValeur;
	}



	public void setDeuxiemeValeur(String deuxiemeValeur) {
		firePropertyChange("deuxiemeValeur", this.deuxiemeValeur, this.deuxiemeValeur = deuxiemeValeur);
	}



	public String getLibelleConformite() {
		return libelleConformite;
	}



	public void setLibelleConformite(String libelleConformite) {
		firePropertyChange("libelleConformite", this.libelleConformite, this.libelleConformite = libelleConformite);
	}



	public String getValeurCalculee() {
		return valeurCalculee;
	}



	public void setValeurCalculee(String valeurCalculee) {
		firePropertyChange("valeurCalculee", this.valeurCalculee, this.valeurCalculee = valeurCalculee);
	}



	public String getCodeTypeConformite() {
		return codeTypeConformite;
	}



	public void setCodeTypeConformite(String codeTypeConformite) {
		firePropertyChange("codeTypeConformite", this.codeTypeConformite, this.codeTypeConformite = codeTypeConformite);
	}



	public Integer getIdTypeConformite() {
		return idTypeConformite;
	}



	public void setIdTypeConformite(Integer idTypeConformite) {
		firePropertyChange("idTypeConformite", this.idTypeConformite, this.idTypeConformite = idTypeConformite);
	}



	public String getCodeGroupe() {
		return codeGroupe;
	}



	public void setCodeGroupe(String codeGroupe) {
		firePropertyChange("codeGroupe", this.codeGroupe, this.codeGroupe = codeGroupe);
	}



	public Integer getIdGroupe() {
		return idGroupe;
	}



	public void setIdGroupe(Integer idGroupe) {
		firePropertyChange("idGroupe", this.idGroupe, this.idGroupe = idGroupe);
	}



	public String getCode() {
		return code;
	}



	public void setCode(String code) {
		this.code = code;
	}


}
