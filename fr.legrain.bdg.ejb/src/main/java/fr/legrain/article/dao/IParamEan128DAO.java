package fr.legrain.article.dao;

import java.util.List;

import fr.legrain.article.model.TaParamEan128;
import fr.legrain.data.IGenericDAO;
//import javax.ejb.Remote;

//@Remote
public interface IParamEan128DAO extends IGenericDAO<TaParamEan128> {

	public List<String> recupListIdentifiant();
	public TaParamEan128 findByCodeLike(String debutCodeAi);
}
