package fr.legrain.bdg.tiers.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.tiers.dto.TaLiensDTO;
import fr.legrain.tiers.model.TaLiens;

@Remote
public interface ITaLiensServiceRemote extends IGenericCRUDServiceRemote<TaLiens,TaLiensDTO>,
														IAbstractLgrDAOServer<TaLiens>,IAbstractLgrDAOServerDTO<TaLiensDTO>{
	
	public static final String validationContext = "LIENS";
	
	public TaLiens findLiensTiers(String codeTiers, String codeTypeLiens);
}
