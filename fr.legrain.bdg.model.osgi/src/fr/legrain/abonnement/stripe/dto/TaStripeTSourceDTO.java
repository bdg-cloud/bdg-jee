package fr.legrain.abonnement.stripe.dto;

import fr.legrain.bdg.model.ModelObject;

public class TaStripeTSourceDTO extends ModelObject implements java.io.Serializable {

	


	private static final long serialVersionUID = 4874497258621637052L;
	
	private Integer id;
	private String codeStripeTSource;
	private String liblStripeTSource;
	private Boolean automatique;
	
	private Integer versionObj;
	
	public TaStripeTSourceDTO() {
	}
	
	
	public TaStripeTSourceDTO(Integer id, String codeStripeTSource, String liblStripeTSource) {
		super();
		this.id = id;
		this.codeStripeTSource = codeStripeTSource;
		this.liblStripeTSource = liblStripeTSource;
	}


	public static TaStripeTSourceDTO copy(TaStripeTSourceDTO taEntrepot){
		TaStripeTSourceDTO taEntrepotLoc = new TaStripeTSourceDTO();
		taEntrepotLoc.setId(taEntrepot.id);
		
		return taEntrepotLoc;
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


	public String getCodeStripeTSource() {
		return codeStripeTSource;
	}


	public void setCodeStripeTSource(String codeStripeTSource) {
		this.codeStripeTSource = codeStripeTSource;
	}


	public String getLiblStripeTSource() {
		return liblStripeTSource;
	}


	public void setLiblStripeTSource(String liblStripeTSource) {
		this.liblStripeTSource = liblStripeTSource;
	}


	public Boolean getAutomatique() {
		return automatique;
	}


	public void setAutomatique(Boolean automatique) {
		this.automatique = automatique;
	}

}
