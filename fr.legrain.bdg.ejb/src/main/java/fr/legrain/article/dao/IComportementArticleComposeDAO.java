package fr.legrain.article.dao;

import java.util.List;
import java.util.Set;

import fr.legrain.article.dto.TaComportementArticleComposeDTO;
import fr.legrain.article.model.TaComportementArticleCompose;
import fr.legrain.data.IGenericDAO;

//@Remote
public interface IComportementArticleComposeDAO extends IGenericDAO<TaComportementArticleCompose> {

	public void deleteListDTO(List<TaComportementArticleComposeDTO> liste);
//	public TaComportementArticleCompose findByIdArticleParentAndByIdArticleEnfant(int idArticleParent, int idArticleEnfant);
	public void deleteList(List<TaComportementArticleCompose> liste);
	public void deleteSet(Set<TaComportementArticleCompose> set);
//	public List<TaComportementArticleCompose> findAllByIdArticleEnfant(int idArticleEnfant);
	
//	public List<TaArticleDTO> findByCodeLight(String code);
//	public List<TaArticleDTO> findAllLight();
//	public List<TaArticleDTO> findAllLight2();


}
