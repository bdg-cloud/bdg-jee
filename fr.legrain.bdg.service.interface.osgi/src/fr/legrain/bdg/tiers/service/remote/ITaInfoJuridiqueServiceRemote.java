package fr.legrain.bdg.tiers.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.tiers.dto.TaInfoJuridiqueDTO;
import fr.legrain.tiers.model.TaInfoJuridique;

@Remote
public interface ITaInfoJuridiqueServiceRemote extends IGenericCRUDServiceRemote<TaInfoJuridique,TaInfoJuridiqueDTO>,
														IAbstractLgrDAOServer<TaInfoJuridique>,IAbstractLgrDAOServerDTO<TaInfoJuridiqueDTO>{
	public static final String validationContext = "INFO_JURIDIQUE";
}
