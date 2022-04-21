package fr.legrain.bdg.compteclient.service.interfaces.remote.droits;

import javax.ejb.Remote;

import fr.legrain.bdg.compteclient.dto.droits.TaClientLegrainDTO;
import fr.legrain.bdg.compteclient.model.droits.TaClientLegrain;
import fr.legrain.bdg.compteclient.service.interfaces.remote.general.IAbstractLgrDAOServer;
import fr.legrain.bdg.compteclient.service.interfaces.remote.general.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.compteclient.service.interfaces.remote.general.IGenericCRUDServiceRemote;

@Remote
public interface ITaClientLegrainServiceRemote extends IGenericCRUDServiceRemote<TaClientLegrain,TaClientLegrainDTO>,
														IAbstractLgrDAOServer<TaClientLegrain>,IAbstractLgrDAOServerDTO<TaClientLegrainDTO>{
	public static final String validationContext = "CLIENT_LEGRAIN";
}
