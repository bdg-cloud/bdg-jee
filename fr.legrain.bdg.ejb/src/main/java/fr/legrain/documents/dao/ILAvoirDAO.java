package fr.legrain.documents.dao;

import java.util.Date;
import java.util.List;

import fr.legrain.article.titretransport.model.TaEtatStockCapsules;
import fr.legrain.data.IGenericDAO;
import fr.legrain.document.model.TaLAvoir;

//@Remote
public interface ILAvoirDAO extends IGenericDAO<TaLAvoir> {

	List<TaEtatStockCapsules> selectSumMouvDocumentStocksCapsulesSorties(TaEtatStockCapsules criteres, Date dateDeb,
			boolean strict);

	List<TaEtatStockCapsules> selectSumMouvDocumentStocksCapsulesEntrees(TaEtatStockCapsules criteres, Date dateDeb,
			boolean strict);

}
