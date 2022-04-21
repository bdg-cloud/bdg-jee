package fr.legrain.bdg.article.service.remote;

import javax.ejb.Remote;

import fr.legrain.article.dto.TaTArticleDTO;
import fr.legrain.article.model.TaTArticle;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;

@Remote
public interface ITaTArticleServiceRemote extends IGenericCRUDServiceRemote<TaTArticle,TaTArticleDTO>,
														IAbstractLgrDAOServer<TaTArticle>,IAbstractLgrDAOServerDTO<TaTArticleDTO>{
	public static final String validationContext = "T_ARTICLE";
}
