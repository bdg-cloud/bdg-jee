package fr.legrain.bdg.moncompte.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.moncompte.dto.TaAutorisationDTO;
import fr.legrain.moncompte.model.TaAutorisation;


@Remote
//@Path("/product")
public interface ITaAutorisationServiceRemote extends IGenericCRUDServiceRemote<TaAutorisation,TaAutorisationDTO>,
														IAbstractLgrDAOServer<TaAutorisation>,IAbstractLgrDAOServerDTO<TaAutorisationDTO>{
	public static final String validationContext = "AUTORISATION";
	
}
