package fr.legrain.articles.statistiques.editors;

import fr.legrain.article.model.TaArticle;
import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;


public interface IFormPageArticlesContoller {
	
	public TaArticle getMasterEntity();
	
	public void setMasterEntity(TaArticle masterEntity);
	
	public ITaArticleServiceRemote getMasterDAO();
	
	public void setMasterDAO(ITaArticleServiceRemote masterDAO);
	
}
