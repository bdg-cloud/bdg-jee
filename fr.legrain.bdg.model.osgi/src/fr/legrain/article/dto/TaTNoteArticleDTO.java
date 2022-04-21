package fr.legrain.article.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import fr.legrain.article.model.TaTNoteArticle;
import fr.legrain.bdg.model.ModelObject;
import fr.legrain.validator.LgrHibernateValidated;


public class TaTNoteArticleDTO extends ModelObject implements java.io.Serializable {

	private static final long serialVersionUID = -4369781278226792380L;
	
	private Integer id;
	private String codeTNoteArticle;
	private String liblTNoteArticle;
	
	private Integer versionObj;
	
	public Integer getVersionObj() {
		return versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

	public TaTNoteArticleDTO() {
		
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer idTNoteArticle) {
		firePropertyChange("id", this.id, this.id = idTNoteArticle);
	}
	
	
	@NotNull
	@Size(min=1, max=20)
	@LgrHibernateValidated(champBd = "code_t_note_article",table = "ta_t_note_article", champEntite="codeTNoteArticle", clazz = TaTNoteArticleDTO.class)
	public String getCodeTNoteArticle() {
		return codeTNoteArticle;
	}
	public void setCodeTNoteArticle(String codeTNoteArticle) {
		firePropertyChange("codeTNoteArticle", this.codeTNoteArticle, this.codeTNoteArticle = codeTNoteArticle);
	}

	@LgrHibernateValidated(champBd = "libl_t_note_article",table = "ta_t_note_article", champEntite="liblTNoteArticle", clazz = TaTNoteArticleDTO.class)
	public String getLiblTNoteArticle() {
		return liblTNoteArticle;
	}
	public void setLiblTNoteArticle(String liblTNoteArticle) {
		firePropertyChange("liblTNoteArticle", this.liblTNoteArticle, this.liblTNoteArticle = liblTNoteArticle);
	}

	
	public static TaTNoteArticleDTO copy(TaTNoteArticleDTO swtTNoteArticle){
		TaTNoteArticleDTO swtTNoteTiersLoc = new TaTNoteArticleDTO();
		swtTNoteTiersLoc.setCodeTNoteArticle(swtTNoteArticle.codeTNoteArticle);
		swtTNoteTiersLoc.setId(swtTNoteArticle.id);
		swtTNoteTiersLoc.setLiblTNoteArticle(swtTNoteArticle.liblTNoteArticle);
		return swtTNoteTiersLoc;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((codeTNoteArticle == null) ? 0 : codeTNoteArticle.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((liblTNoteArticle == null) ? 0 : liblTNoteArticle.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TaTNoteArticleDTO other = (TaTNoteArticleDTO) obj;
		if (codeTNoteArticle == null) {
			if (other.codeTNoteArticle != null)
				return false;
		} else if (!codeTNoteArticle.equals(other.codeTNoteArticle))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (liblTNoteArticle == null) {
			if (other.liblTNoteArticle != null)
				return false;
		} else if (!liblTNoteArticle.equals(other.liblTNoteArticle))
			return false;
		return true;
	}

}
