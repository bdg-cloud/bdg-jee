package fr.legrain.general.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.general.model.TaAliasEspaceClient;
import fr.legrain.general.model.TaAliasEspaceClient;
import fr.legrain.general.dto.TaAliasEspaceClientDTO;;

@Remote
public interface ITaAliasEspaceClientServiceRemote extends IGenericCRUDServiceRemote<TaAliasEspaceClient,TaAliasEspaceClientDTO>,
						IAbstractLgrDAOServer<TaAliasEspaceClient>,IAbstractLgrDAOServerDTO<TaAliasEspaceClientDTO>{
	public static final String validationContext = "TA_ALIAS_ESPACE_CLIENT";
}
