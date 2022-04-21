package fr.legrain.bdg.article.service.remote;

import java.util.List;

import javax.ejb.Remote;

import fr.legrain.article.dto.TaTvaDTO;
import fr.legrain.article.model.TaEntrepot;
import fr.legrain.article.model.TaTva;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.stock.dto.EtatStocksDTO;
import fr.legrain.stock.model.TaEtatStock;
import fr.legrain.tiers.dto.TaTiersDTO;

@Remote
public interface ITaEtatStockServiceRemote extends IGenericCRUDServiceRemote<TaEtatStock,EtatStocksDTO>,
														IAbstractLgrDAOServer<TaEtatStock>,IAbstractLgrDAOServerDTO<EtatStocksDTO>{
	public static final String validationContext = "ETAT_STOCK";
	
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
