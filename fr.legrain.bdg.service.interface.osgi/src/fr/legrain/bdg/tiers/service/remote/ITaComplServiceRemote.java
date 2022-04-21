package fr.legrain.bdg.tiers.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.tiers.dto.TaComplDTO;
import fr.legrain.tiers.model.TaCompl;

@Remote
public interface ITaComplServiceRemote extends IGenericCRUDServiceRemote<TaCompl,TaComplDTO>,
														IAbstractLgrDAOServer<TaCompl>,IAbstractLgrDAOServerDTO<TaComplDTO>{
	
	public static final String validationContext = "COMPL";
	
}
