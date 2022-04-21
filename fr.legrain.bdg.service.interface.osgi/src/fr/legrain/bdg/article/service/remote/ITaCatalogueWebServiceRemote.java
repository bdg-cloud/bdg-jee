package fr.legrain.bdg.article.service.remote;

import java.util.List;

import javax.ejb.Remote;

import fr.legrain.article.dto.TaArticleDTO;
import fr.legrain.article.dto.TaCatalogueWebDTO;
import fr.legrain.article.dto.TaCategorieArticleDTO;
import fr.legrain.article.model.TaCatalogueWeb;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;

@Remote
public interface ITaCatalogueWebServiceRemote extends IGenericCRUDServiceRemote<TaCatalogueWeb,TaCatalogueWebDTO>,
														IAbstractLgrDAOServer<TaCatalogueWeb>,IAbstractLgrDAOServerDTO<TaCatalogueWebDTO>{
	public static final String validationContext = "CATALOGUE_WEB";
	
	public List<TaArticleDTO> findListeArticleCatalogue();
	public List<TaArticleDTO> findListeArticleCatalogue(int idCategorie);
	public TaArticleDTO findArticleCatalogue(int idArticle);
	
	public List<TaCategorieArticleDTO> findListeCategorieArticleCatalogue();
	public TaCategorieArticleDTO findCategorieArticleCatalogue(int idCategorie);
}
