package fr.legrain.bdg.tiers.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.tiers.dto.TaTLiensDTO;
import fr.legrain.tiers.model.TaTLiens;

@Remote
public interface ITaTLiensServiceRemote extends IGenericCRUDServiceRemote<TaTLiens,TaTLiensDTO>,
														IAbstractLgrDAOServer<TaTLiens>,IAbstractLgrDAOServerDTO<TaTLiensDTO>{
	public static final String validationContext = "T_LIENS";
}
