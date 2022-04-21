package fr.legrain.bdg.tiers.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.tiers.dto.TaFamilleTiersDTO;
import fr.legrain.tiers.model.TaFamilleTiers;

@Remote
public interface ITaFamilleTiersServiceRemote extends IGenericCRUDServiceRemote<TaFamilleTiers,TaFamilleTiersDTO>,
														IAbstractLgrDAOServer<TaFamilleTiers>,IAbstractLgrDAOServerDTO<TaFamilleTiersDTO>{
	public static final String validationContext = "FAMILLE_TIERS";
}
