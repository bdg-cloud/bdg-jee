package fr.legrain.bdg.login.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.droits.dto.TaRoleDTO;
import fr.legrain.droits.model.TaRole;
import fr.legrain.login.dto.TaRoleLoginDTO;
import fr.legrain.login.model.TaRoleLogin;

@Remote
public interface ITaRoleLoginServiceRemote extends IGenericCRUDServiceRemote<TaRoleLogin,TaRoleLoginDTO>,
														IAbstractLgrDAOServer<TaRoleLogin>,IAbstractLgrDAOServerDTO<TaRoleLoginDTO>{
	public static final String validationContext = "ROLE_LOGIN";
}
