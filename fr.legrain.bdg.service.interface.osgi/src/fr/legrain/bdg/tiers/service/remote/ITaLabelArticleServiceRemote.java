package fr.legrain.bdg.tiers.service.remote;

import fr.legrain.article.dto.TaLabelArticleDTO;
import fr.legrain.article.model.TaLabelArticle;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;

public interface ITaLabelArticleServiceRemote extends IGenericCRUDServiceRemote<TaLabelArticle, TaLabelArticleDTO>, 
IAbstractLgrDAOServer<TaLabelArticle>, IAbstractLgrDAOServerDTO<TaLabelArticleDTO>{
	public static final String validationContext = "LABEL_ARTICLE";
}
