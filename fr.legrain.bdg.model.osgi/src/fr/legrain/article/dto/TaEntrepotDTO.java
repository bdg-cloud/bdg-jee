package fr.legrain.article.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import fr.legrain.article.model.TaEntrepot;
import fr.legrain.bdg.model.ModelObject;
import fr.legrain.validator.LgrHibernateValidated;

public class TaEntrepotDTO extends ModelObject implements java.io.Serializable {
	

	private static final long serialVersionUID = 656467988688867937L;
	
	private Integer id;
	private String libelle;
	private Boolean topEffacable;
	private String codeEntrepot;
	
	private Integer versionObj;
	
	public TaEntrepotDTO() {
	}
	
	public TaEntrepotDTO(Integer id,String code ,String libelle) {
		this.id = id;
		this.libelle = libelle;
		this.codeEntrepot = code;
		
	}
	
	public static TaEntrepotDTO copy(TaEntrepotDTO taEntrepot){
		TaEntrepotDTO taEntrepotLoc = new TaEntrepotDTO();
		taEntrepotLoc.setLibelle(taEntrepot.libelle);
		taEntrepotLoc.setId(taEntrepot.id);
		taEntrepotLoc.setTopEffacable(taEntrepot.topEffacable);
		
		return taEntrepotLoc;
	}
	
	
	
	public TaEntrepotDTO(Integer id, String libelle,String code,
			Boolean topEffacable) {
		this.id = id;
		this.libelle = libelle;
		this.topEffacable = topEffacable;
		this.codeEntrepot = code;
		
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public void setId(Integer id) {
		firePropertyChange("id", this.id, this.id = id);
	}
	
	//@LgrHibernateValidated(champBd = "libelle",table = "ta_entrepot", champEntite="libelle", clazz = TaEntrepotDTO.class)
	public String getLibelle() {
		return this.libelle;
	}
	
	public void setLibelle(String libelle) {
		firePropertyChange("libelle", this.libelle, this.libelle = libelle);
	}
	
	public Boolean getTopEffacable() {
		return this.topEffacable;
	}
	
	public void setTopEffacable(Boolean topEffacable) {
		firePropertyChange("topEffacable", this.topEffacable, this.topEffacable = topEffacable);
	}
	
	
	public Integer getVersionObj() {
		return this.versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}


	


	@NotNull
	@Size(min=1, max=20)
	//@LgrHibernateValidated(champBd = "code_entrepot",table = "ta_entrepot", champEntite="code" ,clazz = TaEntrepotDTO.class)
	public String getCodeEntrepot() {
		return codeEntrepot;
	}


	public void setCodeEntrepot(String code) {
		this.codeEntrepot = code;
	}





}
