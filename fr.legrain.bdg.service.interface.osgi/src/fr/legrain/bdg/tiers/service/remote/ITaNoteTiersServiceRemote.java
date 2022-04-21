package fr.legrain.bdg.tiers.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.tiers.dto.TaNoteTiersDTO;
import fr.legrain.tiers.model.TaNoteTiers;

@Remote
public interface ITaNoteTiersServiceRemote extends IGenericCRUDServiceRemote<TaNoteTiers,TaNoteTiersDTO>,
														IAbstractLgrDAOServer<TaNoteTiers>,IAbstractLgrDAOServerDTO<TaNoteTiersDTO>{
	public static final String validationContext = "NOTE_TIERS";
}
