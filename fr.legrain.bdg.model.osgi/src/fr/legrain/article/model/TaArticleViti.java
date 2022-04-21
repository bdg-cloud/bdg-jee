package fr.legrain.article.model;

import fr.legrain.gestioncapsules.model.TaTitreTransport;

//@Entity
//@Table(name = "TA_ARTICLE_VITI")
public class TaArticleViti extends TaArticle implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2734801994536143247L;
	private TaTitreTransport titreTransport;
	
	public TaArticleViti() {}
		
//	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//	@JoinColumn(name = "ID_TITRE_TRANSPORT")
//	@LgrHibernateValidated(champ = "ID_TITRE_TRANSPORT",table = "TA_TITRE_TRANSPORT",clazz = TaTitreTransport.class)
	public TaTitreTransport getTaTitreTransport(){
		return this.titreTransport;
	}

	public void setTaTitreTransport(TaTitreTransport titreTransport) {
		this.titreTransport = titreTransport;
	}

}
