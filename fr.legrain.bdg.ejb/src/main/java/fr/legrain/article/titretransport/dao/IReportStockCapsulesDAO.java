package fr.legrain.article.titretransport.dao;

import java.util.Date;
import java.util.List;

import fr.legrain.article.titretransport.model.TaEtatStockCapsules;
import fr.legrain.article.titretransport.model.TaReportStockCapsules;
import fr.legrain.data.IGenericDAO;

//@Remote
public interface IReportStockCapsulesDAO extends IGenericDAO<TaReportStockCapsules> {

	List<TaEtatStockCapsules> selectReportStocks(TaEtatStockCapsules criteres);

	Date recupDerniereDateReportStock();
	public void recalculReport(List<TaEtatStockCapsules> listeReports, Date dateFin);
}
