package fr.legrain.bdg.droits.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.droits.dto.TaAuthViewDTO;
import fr.legrain.droits.model.TaAuthView;

@Remote
public interface ITaAuthViewServiceRemote extends IGenericCRUDServiceRemote<TaAuthView,TaAuthViewDTO>,
														IAbstractLgrDAOServer<TaAuthView>,IAbstractLgrDAOServerDTO<TaAuthViewDTO>{
	public static final String validationContext = "AUTH_VIEW";
}
