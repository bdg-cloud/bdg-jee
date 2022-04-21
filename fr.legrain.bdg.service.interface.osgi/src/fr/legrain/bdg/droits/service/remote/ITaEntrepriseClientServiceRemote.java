package fr.legrain.bdg.droits.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.droits.dto.TaEntrepriseClientDTO;
import fr.legrain.droits.model.TaEntrepriseClient;

@Remote
public interface ITaEntrepriseClientServiceRemote extends IGenericCRUDServiceRemote<TaEntrepriseClient,TaEntrepriseClientDTO>,
														IAbstractLgrDAOServer<TaEntrepriseClient>,IAbstractLgrDAOServerDTO<TaEntrepriseClientDTO>{
	public static final String validationContext = "ENTREPRISE_CLIENT";
}
