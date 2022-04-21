package fr.legrain.bdg.tiers.service.remote;

import java.util.List;

import javax.ejb.EJBException;
import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.tiers.dto.TaEspaceClientDTO;
import fr.legrain.tiers.model.TaEspaceClient;

@Remote
public interface ITaEspaceClientServiceRemote extends IGenericCRUDServiceRemote<TaEspaceClient,TaEspaceClientDTO>,
														IAbstractLgrDAOServer<TaEspaceClient>,IAbstractLgrDAOServerDTO<TaEspaceClientDTO>{
	public static final String validationContext = "ESPACE_CLIENT";
	
	public TaEspaceClient login(String login, String password) throws EJBException;
	public TaEspaceClientDTO loginDTO(String login, String password);
	
	public TaEspaceClient findByCodeTiers(String codeTiers);
	public List<TaEspaceClientDTO> findAllLight();
}
