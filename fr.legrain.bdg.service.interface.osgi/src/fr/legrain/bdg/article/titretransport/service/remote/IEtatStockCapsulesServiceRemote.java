package fr.legrain.bdg.article.titretransport.service.remote;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import fr.legrain.article.titretransport.model.TaEtatStockCapsules;





@Remote
public interface IEtatStockCapsulesServiceRemote {
	public static final String validationContext = "STOCK_CAPSULES";
	
	
	public List<TaEtatStockCapsules> calculEtatStocks(TaEtatStockCapsules criteres, boolean strict);
	public List<TaEtatStockCapsules> calculEtatStocks(TaEtatStockCapsules criteres,Date dateDeb, boolean strict);
	
	public List<TaEtatStockCapsules> calculEtatStocks(TaEtatStockCapsules criteres, boolean strict, boolean synthese);
	public List<TaEtatStockCapsules> calculEtatStocks(TaEtatStockCapsules criteres,Date dateDeb, boolean strict, boolean synthese);
	
	public List<TaEtatStockCapsules> calculEtatStocksMouvements(TaEtatStockCapsules criteres, boolean strict);
	public List<TaEtatStockCapsules> calculEtatStocksMouvements(TaEtatStockCapsules criteres,Date dateDeb, boolean strict);
	
	public int existeArticleUnite(TaEtatStockCapsules taEtatStock);
	
	public int existeArticle(TaEtatStockCapsules taEtatStock);
	
	public Date recupDerniereDateCalcul();
	
	public Date recupDerniereDateReportStock();
	
	
	public List<TaEtatStockCapsules> calculEtatStocksReport(TaEtatStockCapsules criteres);
	public void recalculReport(List<TaEtatStockCapsules> listeReports, Date dateFin);
	public List<TaEtatStockCapsules> calculEtatStocksPourReport(TaEtatStockCapsules criteres, Date dateDeb);


}
