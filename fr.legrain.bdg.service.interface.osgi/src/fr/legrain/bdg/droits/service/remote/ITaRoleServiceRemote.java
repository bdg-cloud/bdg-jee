package fr.legrain.bdg.droits.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.droits.dto.TaRoleDTO;
import fr.legrain.droits.model.TaRole;

@Remote
public interface ITaRoleServiceRemote extends IGenericCRUDServiceRemote<TaRole,TaRoleDTO>,
														IAbstractLgrDAOServer<TaRole>,IAbstractLgrDAOServerDTO<TaRoleDTO>{
	public static final String validationContext = "ROLE";
}
