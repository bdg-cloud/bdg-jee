package fr.legrain.article.dao;

import java.util.Date;
import java.util.List;

import fr.legrain.article.dto.ArticleLotEntrepotDTO;
import fr.legrain.article.model.TaArticle;
import fr.legrain.article.titretransport.model.TaEtatStockCapsules;
import fr.legrain.data.IGenericDAO;
import fr.legrain.dms.model.TaEtatMouvementDms;
import fr.legrain.stock.dto.MouvementStocksDTO;
import fr.legrain.stock.model.TaMouvementStock;

//@Remote
public interface IMouvementDAO extends IGenericDAO<TaMouvementStock> {

//	public List<TaMouvementStock> findByIdFab(Integer id);
	public List<Object[]> getEtatStock();
	public List<String>  articleEnStock() ;
	public List<String>  articleEnStock(Boolean mPremiere,Boolean pFini);
	public List<String>  lotEnStock(); 
	public void recalculEtatStock();
	public List<String>  entrepotAvecStock(); 
	public List<String>  entrepotAvecStockParArticleEtLot(String codeArticle, String numLot); 
	public List<String>  lotEnStockParArticle(String codeArticle);
	public List<String>  emplacementParArticleLotEtEntrepot(String codeArticle, String numLot, String codeEntrepot);
	public List<String>  emplacementParEntrepot( String codeEntrepot);
	public List<ArticleLotEntrepotDTO>  getEtatStockByArticle(String codeArticle, Boolean utiliserLotNonConforme);
	public List<ArticleLotEntrepotDTO>  getEtatStockByArticleVirtuel(String codeArticle, Boolean utiliserLotNonConforme) ;
	public List<ArticleLotEntrepotDTO>  getQuantiteTotaleEnStockArticle(String codeArticle, Boolean utiliserLotNonConforme);

	public List<TaMouvementStock> findByCodeListe(String codeLot);
	public List <MouvementStocksDTO> etatStocksEntrepotEmplacement(Date dateD, Date dateF);
	public List <MouvementStocksDTO> etatStocksEntrepotEmplacement(Date dateD, Date dateF,String codeEntrepot, String emplacement,boolean parFamille,boolean afficherLesTermine);
	public Integer getNBEtatStockByArticleVirtuel(TaArticle article, Boolean utiliserLotNonConforme);
	public List<TaEtatMouvementDms> selectSumMouvementEntreesSortiesDms(TaEtatMouvementDms criteres);
	public List<TaEtatMouvementDms> selectSumSyntheseMouvEntreesSortiesDms(TaEtatMouvementDms criteres);
	public List<TaEtatStockCapsules> selectSumMouvEntreesCRD(TaEtatStockCapsules criteres, Date dateDeb, boolean strict);
	public List<TaEtatStockCapsules> selectSumMouvSortiesCRD(TaEtatStockCapsules criteres, Date dateDeb, boolean strict);
}
