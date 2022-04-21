package fr.legrain.article.model;

// Generated 11 août 2009 15:52:23 by Hibernate Tools 3.2.4.GA

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import fr.legrain.validator.LgrHibernateValidated;


@Entity
//@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "ta_r_t_article")
//@SequenceGenerator(name = "gen_r_t_article", sequenceName = "num_id_r_t_article", allocationSize = 1)
public class TaRTArticle implements java.io.Serializable {  

	private static final long serialVersionUID = 1348671413932603615L;
	
	private Integer idRTArticle;
    private TaArticle taArticle;
    private TaTArticle taTArticle;

	public TaRTArticle() {
	}

	public TaRTArticle(Integer idRTArticle) {
		this.idRTArticle = idRTArticle;
	}


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_r_t_article", unique = true)
	public Integer getIdRTArticle() {
		return this.idRTArticle;
	}

	public void setIdRTArticle(Integer idRTArticle) {
		this.idRTArticle = idRTArticle;
	}

		
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_article")
	@LgrHibernateValidated(champBd = "id_article",table = "ta_article", champEntite="taArticle.idArticle", clazz = TaArticle.class)
	public TaArticle getTaArticle() {
		return this.taArticle;
	}

	public void setTaArticle(TaArticle taArticle) {
		this.taArticle = taArticle;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_t_article")
	@LgrHibernateValidated(champBd = "id_t_article",table = "ta_t_article", champEntite="taTArticle.idTArticle", clazz = TaTArticle.class)
	public TaTArticle getTaTArticle() {
		return this.taTArticle;
	}

	public void setTaTArticle(TaTArticle taTArticle) {
		this.taTArticle = taTArticle;
	}
}
