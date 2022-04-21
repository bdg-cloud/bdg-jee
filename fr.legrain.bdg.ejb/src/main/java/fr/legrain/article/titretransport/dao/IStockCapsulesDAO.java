package fr.legrain.article.titretransport.dao;

import java.util.Date;
import java.util.List;

import fr.legrain.article.titretransport.model.TaEtatStockCapsules;
import fr.legrain.article.titretransport.model.TaStockCapsules;
import fr.legrain.data.IGenericDAO;

//@Remote
public interface IStockCapsulesDAO extends IGenericDAO<TaStockCapsules> {

	List<TaEtatStockCapsules> selectSumMouvEntrees(TaEtatStockCapsules criteres, Date dateDeb, boolean strict);

	List<TaEtatStockCapsules> selectSumMouvSorties(TaEtatStockCapsules criteres, Date dateDeb, boolean strict);

}
