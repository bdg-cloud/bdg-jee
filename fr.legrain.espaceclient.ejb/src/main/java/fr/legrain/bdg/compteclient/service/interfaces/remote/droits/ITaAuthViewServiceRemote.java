package fr.legrain.bdg.compteclient.service.interfaces.remote.droits;

import javax.ejb.Remote;

import fr.legrain.bdg.compteclient.dto.droits.TaAuthViewDTO;
import fr.legrain.bdg.compteclient.model.droits.TaAuthView;
import fr.legrain.bdg.compteclient.service.interfaces.remote.general.IAbstractLgrDAOServer;
import fr.legrain.bdg.compteclient.service.interfaces.remote.general.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.compteclient.service.interfaces.remote.general.IGenericCRUDServiceRemote;

@Remote
public interface ITaAuthViewServiceRemote extends IGenericCRUDServiceRemote<TaAuthView,TaAuthViewDTO>,
														IAbstractLgrDAOServer<TaAuthView>,IAbstractLgrDAOServerDTO<TaAuthViewDTO>{
	public static final String validationContext = "AUTH_VIEW";
}
