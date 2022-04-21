package fr.legrain.bdg.moncompte.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.moncompte.dto.TaRoleDTO;
import fr.legrain.moncompte.model.TaRole;

@Remote
public interface ITaRoleServiceRemote extends IGenericCRUDServiceRemote<TaRole,TaRoleDTO>,
														IAbstractLgrDAOServer<TaRole>,IAbstractLgrDAOServerDTO<TaRoleDTO>{
	public static final String validationContext = "ROLE";
}
