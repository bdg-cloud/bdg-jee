package fr.legrain.article.dao;

import java.util.List;
import java.util.Set;

import fr.legrain.article.dto.TaArticleComposeDTO;
import fr.legrain.article.model.TaArticleCompose;
import fr.legrain.data.IGenericDAO;

//@Remote
public interface IArticleComposeDAO extends IGenericDAO<TaArticleCompose> {

	public void deleteListDTO(List<TaArticleComposeDTO> liste);
	public TaArticleCompose findByIdArticleParentAndByIdArticleEnfant(int idArticleParent, int idArticleEnfant);
	public void deleteList(List<TaArticleCompose> liste);
	public void deleteSet(Set<TaArticleCompose> set);
	public List<TaArticleCompose> findAllByIdArticleEnfant(int idArticleEnfant);
	
//	public List<TaArticleDTO> findByCodeLight(String code);
//	public List<TaArticleDTO> findAllLight();
//	public List<TaArticleDTO> findAllLight2();


}
