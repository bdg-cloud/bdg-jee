package fr.legrain.edition.dto;

import java.util.HashSet;
import java.util.Set;

import fr.legrain.bdg.model.ModelObject;

public class TaActionEditionDTO extends ModelObject implements java.io.Serializable {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = -3245094266534107634L;

	private Integer id;
	
	private String libelle;
	private String codeAction;
	
	private Integer versionObj;
	
	private Set<TaActionEditionDTO> taActionEditionDTO = new HashSet<TaActionEditionDTO>();

	public TaActionEditionDTO() {
	}

	public TaActionEditionDTO(Integer id, String codeAction,
			String libelle) {
		super();
		this.id = id;
		this.libelle = libelle;
		this.codeAction = codeAction;
	}

	public void setTaActionEditionDTO(TaActionEditionDTO taActionEditionDTO) {
		this.id = taActionEditionDTO.id;
		this.libelle = taActionEditionDTO.libelle;
		this.codeAction = taActionEditionDTO.codeAction;
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

	

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		firePropertyChange("libelle", this.libelle, this.libelle = libelle);
	}

	

	public String getCodeAction() {
		return codeAction;
	}

	public void setCodeAction(String codeAction) {
		this.codeAction = codeAction;
	}
	

}
