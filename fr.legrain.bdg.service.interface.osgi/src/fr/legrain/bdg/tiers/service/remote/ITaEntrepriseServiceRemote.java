package fr.legrain.bdg.tiers.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.tiers.dto.TaEntrepriseDTO;
import fr.legrain.tiers.model.TaEntreprise;

@Remote
public interface ITaEntrepriseServiceRemote extends IGenericCRUDServiceRemote<TaEntreprise,TaEntrepriseDTO>,
														IAbstractLgrDAOServer<TaEntreprise>,IAbstractLgrDAOServerDTO<TaEntrepriseDTO>{
	public static final String validationContext = "ENTREPRISE";
}
