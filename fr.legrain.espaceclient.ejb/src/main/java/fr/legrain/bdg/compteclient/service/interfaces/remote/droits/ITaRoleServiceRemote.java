package fr.legrain.bdg.compteclient.service.interfaces.remote.droits;

import javax.ejb.Remote;

import fr.legrain.bdg.compteclient.dto.droits.TaRoleDTO;
import fr.legrain.bdg.compteclient.model.droits.TaRole;
import fr.legrain.bdg.compteclient.service.interfaces.remote.general.IAbstractLgrDAOServer;
import fr.legrain.bdg.compteclient.service.interfaces.remote.general.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.compteclient.service.interfaces.remote.general.IGenericCRUDServiceRemote;

@Remote
public interface ITaRoleServiceRemote extends IGenericCRUDServiceRemote<TaRole,TaRoleDTO>,
														IAbstractLgrDAOServer<TaRole>,IAbstractLgrDAOServerDTO<TaRoleDTO>{
	public static final String validationContext = "ROLE";
}
