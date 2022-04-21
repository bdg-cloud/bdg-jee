package fr.legrain.bdg.article.service.remote;

import java.util.List;
import java.util.Set;

import javax.ejb.Remote;

import fr.legrain.article.dto.TaArticleComposeDTO;
import fr.legrain.article.dto.TaComportementArticleComposeDTO;
import fr.legrain.article.model.TaArticleCompose;
import fr.legrain.article.model.TaComportementArticleCompose;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;

@Remote
public interface ITaComportementArticleComposeServiceRemote extends IGenericCRUDServiceRemote<TaComportementArticleCompose,TaComportementArticleComposeDTO>,
														IAbstractLgrDAOServer<TaComportementArticleCompose>,IAbstractLgrDAOServerDTO<TaComportementArticleComposeDTO>{
	public static final String validationContext = "COMPORTEMENT_ARTICLE_COMPOSE";

	public void deleteListDTO(List<TaComportementArticleComposeDTO> liste);
	public void deleteList(List<TaComportementArticleCompose> liste);
	public void deleteSet(Set<TaComportementArticleCompose> set);

//	public TaArticleCompose findByIdArticleParentAndByIdArticleEnfant(int idArticleParent, int idArticleEnfant);
//	public List<TaArticleCompose> findAllByIdArticleEnfant(int idArticleEnfant);
	




	
}
