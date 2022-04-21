package fr.legrain.bdg.article.service.remote;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.stock.dto.GrMouvStockDTO;
import fr.legrain.stock.dto.MouvementStocksDTO;
import fr.legrain.stock.model.TaGrMouvStock;
import fr.legrain.stock.model.TaMouvementStock;

@Remote
public interface ITaGrMouvStockServiceRemote extends IGenericCRUDServiceRemote<TaGrMouvStock,GrMouvStockDTO>,
														IAbstractLgrDAOServer<TaGrMouvStock>,IAbstractLgrDAOServerDTO<GrMouvStockDTO>{
	public static final String validationContext = "GR_MOUV_STOCK";

	public List<TaMouvementStock> findAllWithDates(Date dateDebut, Date dateFin);
	public List<GrMouvStockDTO> findByCodeLight(String code);
	public List<GrMouvStockDTO> findAllLight();
	
	
	
	public String genereCode( Map<String, String> params);
	public void annuleCode(String code);
	public void verrouilleCode(String code);
}
