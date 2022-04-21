package fr.legrain.bdg.article.service.remote;

import javax.ejb.Remote;

import fr.legrain.article.dto.TaLabelArticleDTO;
import fr.legrain.article.model.TaLabelArticle;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;

@Remote
public interface ITaLabelArticleServiceRemote extends IGenericCRUDServiceRemote<TaLabelArticle,TaLabelArticleDTO>,
														IAbstractLgrDAOServer<TaLabelArticle>,IAbstractLgrDAOServerDTO<TaLabelArticleDTO>{
	public static final String validationContextType = "TYPE_LABEL_ARTICLE";
	public static final String validationContext = "LABEL_ARTICLE";
}
