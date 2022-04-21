package fr.legrain.bdg.article.service.remote;

import javax.ejb.Remote;

import fr.legrain.article.dto.TaConditionnementArticleDTO;
import fr.legrain.article.model.TaConditionnementArticle;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;

@Remote
public interface ITaConditionnementArticleServiceRemote extends IGenericCRUDServiceRemote<TaConditionnementArticle,TaConditionnementArticleDTO>,
														IAbstractLgrDAOServer<TaConditionnementArticle>,IAbstractLgrDAOServerDTO<TaConditionnementArticleDTO>{
}
