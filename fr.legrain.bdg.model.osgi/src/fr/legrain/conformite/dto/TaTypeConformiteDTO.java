package fr.legrain.conformite.dto;

import fr.legrain.bdg.model.ModelObject;
import fr.legrain.validator.LgrHibernateValidated;

public class TaTypeConformiteDTO extends ModelObject implements java.io.Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8061652999788659301L;
	
	
	private Integer id;
	private Integer versionObj;
	

	private String code;
	private String libelle;
	private String libelleDeuxieme;
	
	public TaTypeConformiteDTO() {
		
	}
	
	@LgrHibernateValidated(champBd = "code",table = "ta_type_conformite", champEntite="code", clazz = TaTypeConformiteDTO.class)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		firePropertyChange("code", this.code, this.code = code);
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


	@LgrHibernateValidated(champBd = "libelle",table = "ta_type_conformite", champEntite="libelle", clazz = TaTypeConformiteDTO.class)
	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		firePropertyChange("libelle", this.libelle, this.libelle = libelle);
	}

	@LgrHibernateValidated(champBd = "libelle_deuxieme",table = "ta_type_conformite", champEntite="libelleDeuxieme", clazz = TaTypeConformiteDTO.class)
	public String getLibelleDeuxieme() {
		return libelleDeuxieme;
	}

	public void setLibelleDeuxieme(String libelleDeuxieme) {
		firePropertyChange("libelleDeuxieme", this.libelleDeuxieme, this.libelleDeuxieme = libelleDeuxieme);
	}


	

}
