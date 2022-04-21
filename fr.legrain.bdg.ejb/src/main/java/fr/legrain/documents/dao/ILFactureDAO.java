package fr.legrain.documents.dao;

import java.util.Date;
import java.util.List;

import fr.legrain.article.titretransport.model.TaEtatStockCapsules;
import fr.legrain.data.IGenericDAO;
import fr.legrain.document.model.TaLFacture;

//@Remote
public interface ILFactureDAO extends IGenericDAO<TaLFacture> {

	List<TaEtatStockCapsules> selectSumMouvDocumentStocksCapsulesSorties(TaEtatStockCapsules criteres, Date dateDeb,
			boolean strict);

	List<TaEtatStockCapsules> selectSumMouvDocumentStocksCapsulesEntree(TaEtatStockCapsules criteres, Date dateDeb,
			boolean strict);

}
