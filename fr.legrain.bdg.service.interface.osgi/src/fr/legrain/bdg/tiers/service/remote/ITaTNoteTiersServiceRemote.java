package fr.legrain.bdg.tiers.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.tiers.dto.TaTNoteTiersDTO;
import fr.legrain.tiers.model.TaTNoteTiers;

@Remote
public interface ITaTNoteTiersServiceRemote extends IGenericCRUDServiceRemote<TaTNoteTiers,TaTNoteTiersDTO>,
														IAbstractLgrDAOServer<TaTNoteTiers>,IAbstractLgrDAOServerDTO<TaTNoteTiersDTO>{
	public static final String validationContext = "T_NOTE_TIERS";
}
