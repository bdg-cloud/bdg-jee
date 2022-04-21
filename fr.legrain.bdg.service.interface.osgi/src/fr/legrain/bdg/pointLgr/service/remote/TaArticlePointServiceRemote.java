package fr.legrain.bdg.pointLgr.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.pointLgr.dto.ArticlePointDTO;
import fr.legrain.pointLgr.model.TaArticlePoint;

@Remote
public interface TaArticlePointServiceRemote extends IGenericCRUDServiceRemote<TaArticlePoint,ArticlePointDTO>,
														IAbstractLgrDAOServer<TaArticlePoint>,IAbstractLgrDAOServerDTO<ArticlePointDTO>{
}
