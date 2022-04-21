package fr.legrain.bdg.article.service.remote;

import java.util.List;

import javax.ejb.Remote;

import fr.legrain.article.dto.TaImageArticleDTO;
import fr.legrain.article.model.TaImageArticle;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;

@Remote
public interface ITaImageArticleServiceRemote extends IGenericCRUDServiceRemote<TaImageArticle,TaImageArticleDTO>,
														IAbstractLgrDAOServer<TaImageArticle>,IAbstractLgrDAOServerDTO<TaImageArticleDTO>{
	public static final String validationContext = "IMAGE_ARTICLE";
	
	public List<TaImageArticle> findByArticle(int id);
	public List<TaImageArticleDTO> findByArticleLight(int id);
	
	public void changeImageDefaut(TaImageArticle image);
}
