package fr.legrain.article.dto;


import fr.legrain.bdg.model.ModelObject;




public class TaComportementArticleComposeDTO extends ModelObject implements java.io.Serializable {	
		


	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2578392127003293730L;
	private Integer idComportementArticleCompose;
	private String codeComportement;
	private String liblComportement;
	private String descComportement;


	public TaComportementArticleComposeDTO() {
	}
	
	public TaComportementArticleComposeDTO(Integer idComportementArticleCompose, String codeComportement, String liblComportement, String descComportement) {
		super();
		this.idComportementArticleCompose =  idComportementArticleCompose;
		this.codeComportement = codeComportement;
		this.liblComportement = liblComportement;
		this.descComportement = descComportement;
		
	}




	public String getCodeComportement() {
		return codeComportement;
	}

	public void setCodeComportement(String codeComportement) {
		this.codeComportement = codeComportement;
	}

	public String getLiblComportement() {
		return liblComportement;
	}

	public void setLiblComportement(String liblComportement) {
		this.liblComportement = liblComportement;
	}

	public String getDescComportement() {
		return descComportement;
	}

	public void setDescComportement(String descComportement) {
		this.descComportement = descComportement;
	}

	public Integer getIdComportementArticleCompose() {
		return idComportementArticleCompose;
	}

	public void setIdComportementArticleCompose(Integer idComportementArticleCompose) {
		this.idComportementArticleCompose = idComportementArticleCompose;
	}


}
