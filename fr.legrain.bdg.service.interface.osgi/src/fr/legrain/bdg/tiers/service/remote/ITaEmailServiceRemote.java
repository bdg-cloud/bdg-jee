package fr.legrain.bdg.tiers.service.remote;

import java.util.List;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.tiers.dto.TaEmailDTO;
import fr.legrain.tiers.model.TaEmail;

@Remote
public interface ITaEmailServiceRemote extends IGenericCRUDServiceRemote<TaEmail,TaEmailDTO>,
														IAbstractLgrDAOServer<TaEmail>,IAbstractLgrDAOServerDTO<TaEmailDTO>{
	public static final String validationContext = "EMAIL";
	public List<TaEmailDTO> findAllLight();
}
