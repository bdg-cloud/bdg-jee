package fr.legrain.bdg.article.service.remote;

import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import fr.legrain.article.dto.TaCategorieArticleDTO;
import fr.legrain.article.model.TaCategorieArticle;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;

@Remote
public interface ITaCategorieArticleServiceRemote extends IGenericCRUDServiceRemote<TaCategorieArticle,TaCategorieArticleDTO>,
														IAbstractLgrDAOServer<TaCategorieArticle>,IAbstractLgrDAOServerDTO<TaCategorieArticleDTO>{
	
	public static final String validationContextType = "TYPE_CATEGORIE_ARTICLE";
	public static final String validationContext = "CATEGORIE_ARTICLE";
	
	public List<TaCategorieArticle> findByCodeCategorieParent(String codeCategorieParent);
	public List<TaCategorieArticle> findCategorieMere();
	
	public List<TaCategorieArticleDTO> findByCodeLight(String code);
	public List<TaCategorieArticleDTO> findAllLight();
	
	public List<TaCategorieArticleDTO> findCategorieDTOByIdArticle(int idArticle);
	public List<TaCategorieArticle> findCategorieByIdArticle(int idArticle);
	
	public String genereCode( Map<String, String> params);
	public void annuleCode(String code);
	public void verrouilleCode(String code);
}
