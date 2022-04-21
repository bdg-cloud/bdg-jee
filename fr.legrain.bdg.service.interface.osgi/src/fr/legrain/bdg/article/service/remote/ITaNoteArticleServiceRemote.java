package fr.legrain.bdg.article.service.remote;

import javax.ejb.Remote;

import fr.legrain.article.dto.TaNoteArticleDTO;
import fr.legrain.article.model.TaNoteArticle;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;

@Remote
public interface ITaNoteArticleServiceRemote extends IGenericCRUDServiceRemote<TaNoteArticle,TaNoteArticleDTO>,
														IAbstractLgrDAOServer<TaNoteArticle>,IAbstractLgrDAOServerDTO<TaNoteArticleDTO>{
	public static final String validationContext = "NOTE_ARTICLE";
}
