package fr.legrain.bdg.article.service.remote;

import javax.ejb.Remote;

import fr.legrain.article.dto.TaArticleDTO;
import fr.legrain.article.model.TaArticleViti;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;

@Remote
public interface ITaArticleVitiServiceRemote extends IGenericCRUDServiceRemote<TaArticleViti,TaArticleDTO>,
														IAbstractLgrDAOServer<TaArticleViti>,IAbstractLgrDAOServerDTO<TaArticleDTO>{
}
