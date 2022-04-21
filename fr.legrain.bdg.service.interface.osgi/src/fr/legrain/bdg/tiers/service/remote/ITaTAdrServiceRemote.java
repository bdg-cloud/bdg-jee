package fr.legrain.bdg.tiers.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.tiers.dto.TaTAdrDTO;
import fr.legrain.tiers.model.TaTAdr;

@Remote
public interface ITaTAdrServiceRemote extends IGenericCRUDServiceRemote<TaTAdr,TaTAdrDTO>,
														IAbstractLgrDAOServer<TaTAdr>,IAbstractLgrDAOServerDTO<TaTAdrDTO>{
	public static final String validationContext = "T_ADR";
}
