package fr.legrain.bdg.article.service.remote;

import java.util.List;

import javax.ejb.Remote;

import fr.legrain.article.dto.TaEntrepotDTO;
import fr.legrain.article.model.TaEntrepot;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;

@Remote
public interface ITaEntrepotServiceRemote extends IGenericCRUDServiceRemote<TaEntrepot,TaEntrepotDTO>,
														IAbstractLgrDAOServer<TaEntrepot>,IAbstractLgrDAOServerDTO<TaEntrepotDTO>{
	public static final String validationContext = "ENTREPOT";
	
	public List<TaEntrepotDTO> findByCodeLight(String code);
	public List<TaEntrepotDTO> findAllLight();
}
