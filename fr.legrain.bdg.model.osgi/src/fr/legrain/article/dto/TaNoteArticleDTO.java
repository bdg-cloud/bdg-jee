package fr.legrain.article.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import fr.legrain.article.model.TaArticle;
import fr.legrain.article.model.TaNoteArticle;
import fr.legrain.article.model.TaTNoteArticle;
import fr.legrain.bdg.model.ModelObject;
import fr.legrain.validator.LgrHibernateValidated;


// Generated Mar 1, 2007 11:26:10 AM by Hibernate Tools 3.2.0.b9

public class TaNoteArticleDTO extends ModelObject implements java.io.Serializable {

	private static final long serialVersionUID = 755596667353136486L;
	
	private Integer id;
	private String noteArticle;
	private String codeTNoteArticle;
	private String codeArticle;
	private Integer idArticle;
	private Date dateNoteArticle = new Date();
	
	private Integer versionObj;

	public Integer getVersionObj() {
		return versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

	public TaNoteArticleDTO() {
	}

	public void setSWTNote(TaNoteArticleDTO swtNoteArticle) {
		this.id = swtNoteArticle.id;
		this.noteArticle = swtNoteArticle.noteArticle;
		this.codeArticle = swtNoteArticle.codeArticle;
		this.idArticle = swtNoteArticle.idArticle;
		this.codeTNoteArticle = swtNoteArticle.codeTNoteArticle;
		this.dateNoteArticle = swtNoteArticle.dateNoteArticle;
	}

	public static TaNoteArticleDTO copy(TaNoteArticleDTO swtNoteArticle){
		TaNoteArticleDTO swtNoteTiersLoc = new TaNoteArticleDTO();
		swtNoteTiersLoc.setIdArticle(swtNoteArticle.getIdArticle());                //1
		swtNoteTiersLoc.setId(swtNoteArticle.getId());                //1
		swtNoteTiersLoc.setNoteArticle(swtNoteArticle.getNoteArticle());                //1
		swtNoteTiersLoc.setCodeArticle(swtNoteArticle.getCodeArticle());                //1
		swtNoteTiersLoc.setCodeTNoteArticle(swtNoteArticle.getCodeTNoteArticle());                //1
		swtNoteTiersLoc.setDateNoteArticle(swtNoteArticle.getDateNoteArticle());        
		return swtNoteTiersLoc;
	}
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer idNoteArticle) {
		firePropertyChange("id", this.id, this.id = idNoteArticle);
	}

	@LgrHibernateValidated(champBd = "note_article",table = "ta_note_article", champEntite="noteArticle", clazz = TaNoteArticleDTO.class)
	public String getNoteArticle() {
		return this.noteArticle;
	}

	public void setNoteArticle(String noteArticle) {
		firePropertyChange("noteArticle", this.noteArticle, this.noteArticle = noteArticle);
	}
	
	@NotNull
	@Size(min=1, max=20)
	@LgrHibernateValidated(champBd = "code_t_note_article",table = "ta_t_note_article", champEntite="codeTNoteArticle", clazz = TaNoteArticleDTO.class)
	public String getCodeTNoteArticle() {
		return this.codeTNoteArticle;
	}

	public void setCodeTNoteArticle(String codeTNoteArticle) {
		firePropertyChange("codeTNoteArticle", this.codeTNoteArticle, this.codeTNoteArticle = codeTNoteArticle);
	}

	@NotNull
	@Size(min=1, max=20)
	@LgrHibernateValidated(champBd = "code_article",table = "ta_article", champEntite="codeArticle", clazz = TaArticle.class)
	public String getCodeArticle() {
		return this.codeArticle;
	}

	public void setCodeArticle(String codeArticle) {
		firePropertyChange("codeArticle", this.codeArticle, this.codeArticle = codeArticle);
	}

	public Integer getIdArticle() {
		return this.idArticle;
	}

	public void setIdArticle(Integer idArticle) {
		firePropertyChange("idArticle", this.idArticle, this.idArticle = idArticle);
	}
	
	@LgrHibernateValidated(champBd = "date_note_article",table = "ta_note_article", champEntite="dateNoteArticle", clazz = TaNoteArticleDTO.class)
	public Date getDateNoteArticle() {
		return this.dateNoteArticle;
	}

	public void setDateNoteArticle(Date dateNoteArticle) {
		firePropertyChange("dateNoteArticle", this.dateNoteArticle, this.dateNoteArticle = dateNoteArticle);
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof TaNoteArticleDTO))
			return false;
		TaNoteArticleDTO castOther = (TaNoteArticleDTO) other;

		return ((this.getId() == castOther.getId()) || (this
				.getId() != null
				&& castOther.getId() != null && this.getId().equals(
				castOther.getId())))
				&& ((this.getNoteArticle() == castOther.getNoteArticle()) || (this
						.getNoteArticle() != null
						&& castOther.getNoteArticle() != null && this
						.getNoteArticle().equals(castOther.getNoteArticle())))
				&& ((this.getCodeArticle() == castOther.getCodeArticle()) || (this
						.getCodeArticle() != null
						&& castOther.getCodeArticle() != null && this
						.getCodeArticle().equals(castOther.getCodeArticle())))
				&& ((this.getDateNoteArticle() == castOther.getDateNoteArticle()) || (this
						.getDateNoteArticle() != null
						&& castOther.getDateNoteArticle() != null && this
						.getDateNoteArticle().equals(castOther.getDateNoteArticle())))
				&& ((this.getIdArticle() == castOther.getIdArticle()) || (this
						.getIdArticle() != null
						&& castOther.getIdArticle() != null && this
						.getIdArticle().equals(castOther.getIdArticle())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getId() == null ? 0 : this.getId().hashCode());
		result = 37
				* result
				+ (getNoteArticle() == null ? 0 : this.getNoteArticle()
						.hashCode());
		result = 37
				* result
				+ (getDateNoteArticle() == null ? 0 : this.getDateNoteArticle()
						.hashCode());
		result = 37
				* result
				+ (getCodeArticle() == null ? 0 : this.getCodeArticle()
						.hashCode());
		result = 37 * result
				+ (getIdArticle() == null ? 0 : this.getIdArticle().hashCode());
		return result;
	}



}
