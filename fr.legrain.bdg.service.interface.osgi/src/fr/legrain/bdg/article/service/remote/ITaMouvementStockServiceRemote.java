package fr.legrain.bdg.article.service.remote;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import fr.legrain.article.dto.ArticleLotEntrepotDTO;
import fr.legrain.article.model.TaArticle;
import fr.legrain.article.titretransport.model.TaEtatStockCapsules;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.dms.model.TaEtatMouvementDms;
import fr.legrain.stock.dto.MouvementStocksDTO;
import fr.legrain.stock.model.TaMouvementStock;

@Remote
public interface ITaMouvementStockServiceRemote extends IGenericCRUDServiceRemote<TaMouvementStock,MouvementStocksDTO>,
														IAbstractLgrDAOServer<TaMouvementStock>,IAbstractLgrDAOServerDTO<MouvementStocksDTO>{
	public static final String validationContext = "MOUVEMENT_STOCK";

//	public List<TaMouvement> findByIdFab(Integer id);
	//public List<ArticleLotEntrepotDTO> etatStock();
	public List<String>  articleEnStock() ;
	public List<String>  articleEnStock(Boolean mPremiere,Boolean pFini);
	public List<String>  lotEnStock(); 
	public List<String>  entrepotAvecStock(); 
	public List<String>  entrepotAvecStockParArticleEtLot(String codeArticle, String numLot); 
	public List<String>  lotEnStockParArticle(String codeArticle);
	public List<String>  emplacementParArticleLotEtEntrepot(String codeArticle, String numLot, String codeEntrepot);
	
	public List<String>  emplacementParEntrepot( String codeEntrepot);
	public List<ArticleLotEntrepotDTO>  getEtatStockByArticle(String codeArticle, Boolean utiliserLotNonConforme);
	public List<ArticleLotEntrepotDTO>  getQuantiteTotaleEnStockArticle(String codeArticle, Boolean utiliserLotNonConforme);

	public List<TaMouvementStock> findByCodeListe(String codeLot);
	public List <MouvementStocksDTO> etatStocksEntrepotEmplacement(Date dateD, Date dateF);
	public List <MouvementStocksDTO> etatStocksEntrepotEmplacement(Date dateD, Date dateF,String codeEntrepot, String emplacement,boolean parFamille,boolean termine);
	public Integer getNBEtatStockByArticleVirtuel(TaArticle article, Boolean utiliserLotNonConforme);
	public List<ArticleLotEntrepotDTO> getEtatStockByArticleVirtuel(String codeArticle, Boolean utiliserLotNonConforme);
	public List<TaEtatMouvementDms> selectSumMouvementEntreesSortiesDms(TaEtatMouvementDms criteres);
	public List<TaEtatMouvementDms> selectSumSyntheseMouvEntreesSortiesDms(TaEtatMouvementDms criteres);
	
	
	public List<TaEtatStockCapsules> selectSumMouvEntreesCRD(TaEtatStockCapsules criteres, Date dateDeb, boolean strict);
	public List<TaEtatStockCapsules> selectSumMouvSortiesCRD(TaEtatStockCapsules criteres, Date dateDeb, boolean strict);
	

	
}
