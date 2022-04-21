package fr.legrain.bdg.droits.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.droits.dto.TaClientLegrainDTO;
import fr.legrain.droits.model.TaClientLegrain;

@Remote
public interface ITaClientLegrainServiceRemote extends IGenericCRUDServiceRemote<TaClientLegrain,TaClientLegrainDTO>,
														IAbstractLgrDAOServer<TaClientLegrain>,IAbstractLgrDAOServerDTO<TaClientLegrainDTO>{
	public static final String validationContext = "CLIENT_LEGRAIN";
}
