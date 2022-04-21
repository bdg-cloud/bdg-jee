package fr.legrain.bdg.tiers.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.tiers.dto.TaTTiersDTO;
import fr.legrain.tiers.model.TaTTiers;

@Remote
public interface ITaTTiersServiceRemote extends IGenericCRUDServiceRemote<TaTTiers,TaTTiersDTO>,
														IAbstractLgrDAOServer<TaTTiers>,IAbstractLgrDAOServerDTO<TaTTiersDTO>{
	public static final String validationContext = "T_TIERS";
}
