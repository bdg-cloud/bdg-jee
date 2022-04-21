package fr.legrain.bdg.autorisations.service.remote;

import javax.ejb.Remote;

import fr.legrain.autorisations.autorisation.dto.TaRoleDTO;
import fr.legrain.autorisations.autorisation.model.TaRole;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;

@Remote
public interface ITaRoleServiceRemote extends IGenericCRUDServiceRemote<TaRole,TaRoleDTO>,
														IAbstractLgrDAOServer<TaRole>,IAbstractLgrDAOServerDTO<TaRoleDTO>{
	public static final String validationContext = "ROLE";
}
