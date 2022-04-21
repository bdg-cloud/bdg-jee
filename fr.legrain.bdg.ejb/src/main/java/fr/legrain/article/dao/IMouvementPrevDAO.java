package fr.legrain.article.dao;

import java.util.Date;
import java.util.List;

import fr.legrain.article.dto.ArticleLotEntrepotDTO;
import fr.legrain.data.IGenericDAO;
import fr.legrain.stock.dto.MouvementStocksDTO;
import fr.legrain.stock.model.TaMouvementStock;
import fr.legrain.stock.model.TaMouvementStockPrev;

//@Remote
public interface IMouvementPrevDAO extends IGenericDAO<TaMouvementStockPrev> {

}
