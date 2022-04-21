package fr.legrain.articles.editor;

import fr.legrain.article.model.TaArticle;
import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;


public interface IEditorArticlesExtension {
	
	public TaArticle getMasterEntity();
	
	public void setMasterEntity(TaArticle masterEntity);
	
	public ITaArticleServiceRemote getMasterDAO();
	
	public void setMasterDAO(ITaArticleServiceRemote masterDAO);
	
	/**
	 * Methode qui doit être appelée automatiquement lors de l'appel de la méthode <code>pageChange()</code> du MultipageEditor
	 * contenant c'est Editeur.
	 */
	public void activate();
	
}
