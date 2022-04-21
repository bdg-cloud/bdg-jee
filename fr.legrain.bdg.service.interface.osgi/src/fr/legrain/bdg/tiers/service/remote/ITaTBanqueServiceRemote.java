package fr.legrain.bdg.tiers.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.tiers.dto.TaTBanqueDTO;
import fr.legrain.tiers.model.TaTBanque;

@Remote
public interface ITaTBanqueServiceRemote extends IGenericCRUDServiceRemote<TaTBanque,TaTBanqueDTO>,
														IAbstractLgrDAOServer<TaTBanque>,IAbstractLgrDAOServerDTO<TaTBanqueDTO>{
	public static final String validationContext = "T_BANQUE";
}
