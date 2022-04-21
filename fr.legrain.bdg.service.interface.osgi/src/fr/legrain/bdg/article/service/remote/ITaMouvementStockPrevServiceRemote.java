package fr.legrain.bdg.article.service.remote;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import fr.legrain.article.dto.ArticleLotEntrepotDTO;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.stock.dto.MouvementStocksDTO;
import fr.legrain.stock.dto.MouvementStocksPrevDTO;
import fr.legrain.stock.model.TaMouvementStock;
import fr.legrain.stock.model.TaMouvementStockPrev;

@Remote
public interface ITaMouvementStockPrevServiceRemote extends IGenericCRUDServiceRemote<TaMouvementStockPrev,MouvementStocksPrevDTO>,
														IAbstractLgrDAOServer<TaMouvementStockPrev>,IAbstractLgrDAOServerDTO<MouvementStocksPrevDTO>{
	public static final String validationContext = "MOUVEMENT_STOCK_PREV";

	
}
