package fr.legrain.bdg.article.service.remote;

import java.util.List;

import javax.ejb.Remote;

import fr.legrain.article.dto.TaTransporteurDTO;
import fr.legrain.article.model.TaTransporteur;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;

@Remote
public interface ITaTransporteurServiceRemote extends IGenericCRUDServiceRemote<TaTransporteur,TaTransporteurDTO>,
														IAbstractLgrDAOServer<TaTransporteur>,IAbstractLgrDAOServerDTO<TaTransporteurDTO>{
	public static final String validationContext = "TRANSPORTEUR";
	
	public List<TaTransporteurDTO> findByCodeLight(String code);
}
