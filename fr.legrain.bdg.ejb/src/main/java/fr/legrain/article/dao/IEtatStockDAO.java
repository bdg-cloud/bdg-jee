package fr.legrain.article.dao;

import java.util.List;

import fr.legrain.article.model.TaEntrepot;
import fr.legrain.data.IGenericDAO;
import fr.legrain.stock.dto.EtatStocksDTO;
import fr.legrain.stock.model.TaEtatStock;

//@Remote
public interface IEtatStockDAO extends IGenericDAO<TaEtatStock> {
	public List<String> emplacementLotArticle(String codeArticle, String numLot, String codeEntrepot, Boolean termine);
	public List<String> emplacementLotArticle(String codeArticle, String codeEntrepot);
	public List<String> emplacementEntrepot(String codeEntrepot, Boolean termine);
	public List<String> emplacementEntrepot(String codeEntrepot);
	public List<TaEntrepot> entrepotLotArticle(String codeArticle, String numLot, String emplacement, Boolean termine);
	public Integer qteArticleEnStock(Integer idArticle);
	public Integer qteArticleEnStock(Integer idArticle, Integer idEntrepot);
	public Integer qteArticleEnStock(Integer idArticle, Integer idEntrepot, String emplacement) ;
	public Integer qteArticleLotEnStock(Integer idLot);
	public Integer qteArticleLotEnStock(Integer idLot, Integer idEntrepot);
	public Integer qteArticleLotEnStock(Integer idLot, Integer idEntrepot, String emplacement);
	public List<TaEtatStock> qteLotentrepotStock( String idEntrepot, String numLot);
	public List<TaEtatStock> qteLotentrepotStock( String idEntrepot, String numLot, Boolean termine);
	public Integer supprimeEtatStockLot(Integer idLot);
	public void recalculEtatStock();
	
	public List<EtatStocksDTO> findByCodeLight(String code);
	public List<EtatStocksDTO> findAllLight();
	public List<EtatStocksDTO> findAllLight(Boolean termine);
	public List<EtatStocksDTO> qteLotentrepotStockLight( String idEntrepot, String numLot, Boolean termine);
	public List<EtatStocksDTO> qteLotentrepotStockLight( String idEntrepot, String numLot);	
}
