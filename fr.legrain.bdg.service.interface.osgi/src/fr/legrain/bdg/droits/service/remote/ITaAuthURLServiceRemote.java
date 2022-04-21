package fr.legrain.bdg.droits.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.droits.dto.TaAuthURLDTO;
import fr.legrain.droits.model.TaAuthURL;

@Remote
public interface ITaAuthURLServiceRemote extends IGenericCRUDServiceRemote<TaAuthURL,TaAuthURLDTO>,
														IAbstractLgrDAOServer<TaAuthURL>,IAbstractLgrDAOServerDTO<TaAuthURLDTO>{
	public static final String validationContext = "AUTH_URL";
}
