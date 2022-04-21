package fr.legrain.documents.dao;

import java.util.Date;
import java.util.List;

import fr.legrain.article.titretransport.model.TaEtatStockCapsules;
import fr.legrain.data.IGenericDAO;
import fr.legrain.document.model.TaLBonliv;

//@Remote
public interface ILBonlivDAO extends IGenericDAO<TaLBonliv> {

public	List<TaEtatStockCapsules> selectSumMouvDocumentStocksCapsulesSorties(TaEtatStockCapsules criteres, Date dateDeb,
			boolean strict);

public List<TaEtatStockCapsules> selectSumMouvDocumentStocksCapsulesEntree(TaEtatStockCapsules criteres, Date dateDeb,
		boolean strict);

}
