package fr.legrain.bdg.article.service.remote;

import java.util.List;

import javax.ejb.Remote;

import fr.legrain.article.dto.TaUniteDTO;
import fr.legrain.article.model.TaUnite;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;

@Remote
public interface ITaUniteServiceRemote extends IGenericCRUDServiceRemote<TaUnite,TaUniteDTO>,
														IAbstractLgrDAOServer<TaUnite>,IAbstractLgrDAOServerDTO<TaUniteDTO>{
	public static final String validationContext = "UNITE";
	
	public List<TaUniteDTO> findByCodeLight(String code);
	public List<TaUniteDTO> findByCodeLightUniteStock(String codeUniteStock, String code);
	public List<TaUnite> findByCodeUniteStock(String codeUniteStock, String code);
}
