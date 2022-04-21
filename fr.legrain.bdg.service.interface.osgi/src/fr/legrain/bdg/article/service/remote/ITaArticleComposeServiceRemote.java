package fr.legrain.bdg.article.service.remote;

import java.util.List;
import java.util.Set;

import javax.ejb.Remote;

import fr.legrain.article.dto.TaArticleComposeDTO;
import fr.legrain.article.model.TaArticleCompose;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;

@Remote
public interface ITaArticleComposeServiceRemote extends IGenericCRUDServiceRemote<TaArticleCompose,TaArticleComposeDTO>,
														IAbstractLgrDAOServer<TaArticleCompose>,IAbstractLgrDAOServerDTO<TaArticleComposeDTO>{
	public static final String validationContext = "ARTICLE_COMPOSE";

	public void deleteListDTO(List<TaArticleComposeDTO> liste);
	public void deleteList(List<TaArticleCompose> liste);
	public void deleteSet(Set<TaArticleCompose> set);

	public TaArticleCompose findByIdArticleParentAndByIdArticleEnfant(int idArticleParent, int idArticleEnfant);
	public List<TaArticleCompose> findAllByIdArticleEnfant(int idArticleEnfant);
	




	
}
