package fr.legrain.bdg.compteclient.service.interfaces.remote.droits;

import javax.ejb.Remote;

import fr.legrain.bdg.compteclient.dto.droits.TaEntrepriseClientDTO;
import fr.legrain.bdg.compteclient.model.droits.TaEntrepriseClient;
import fr.legrain.bdg.compteclient.service.interfaces.remote.general.IAbstractLgrDAOServer;
import fr.legrain.bdg.compteclient.service.interfaces.remote.general.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.compteclient.service.interfaces.remote.general.IGenericCRUDServiceRemote;

@Remote
public interface ITaEntrepriseClientServiceRemote extends IGenericCRUDServiceRemote<TaEntrepriseClient,TaEntrepriseClientDTO>,
														IAbstractLgrDAOServer<TaEntrepriseClient>,IAbstractLgrDAOServerDTO<TaEntrepriseClientDTO>{
	public static final String validationContext = "ENTREPRISE_CLIENT";
}
