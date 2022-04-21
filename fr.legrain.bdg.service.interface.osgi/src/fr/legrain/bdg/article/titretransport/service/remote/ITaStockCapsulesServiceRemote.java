package fr.legrain.bdg.article.titretransport.service.remote;

import javax.ejb.Remote;

import fr.legrain.article.dto.TaArticleDTO;
import fr.legrain.article.model.TaArticle;
import fr.legrain.article.titretransport.dto.TaStockCapsulesDTO;
import fr.legrain.article.titretransport.model.TaStockCapsules;
import fr.legrain.article.titretransport.model.TaTitreTransport;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;

@Remote
public interface ITaStockCapsulesServiceRemote extends IGenericCRUDServiceRemote<TaStockCapsules,TaStockCapsulesDTO>,
														IAbstractLgrDAOServer<TaStockCapsules>,IAbstractLgrDAOServerDTO<TaStockCapsulesDTO>{
	
	public static final String validationContext = "STOCK_CAPSULES";
	
}
