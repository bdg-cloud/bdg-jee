package fr.legrain.bdg.article.service.remote;

import java.util.List;

import javax.ejb.Remote;

import fr.legrain.article.dto.TaMarqueArticleDTO;
import fr.legrain.article.model.TaMarqueArticle;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;

@Remote
public interface ITaMarqueArticleServiceRemote extends IGenericCRUDServiceRemote<TaMarqueArticle,TaMarqueArticleDTO>,
														IAbstractLgrDAOServer<TaMarqueArticle>,IAbstractLgrDAOServerDTO<TaMarqueArticleDTO>{
	public static final String validationContext = "MARQUE_ARTICLE";
	
	public List<TaMarqueArticleDTO> findByCodeLight(String code);
	
}
