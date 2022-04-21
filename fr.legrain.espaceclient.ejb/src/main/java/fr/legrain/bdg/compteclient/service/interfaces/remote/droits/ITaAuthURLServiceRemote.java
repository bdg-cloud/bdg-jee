package fr.legrain.bdg.compteclient.service.interfaces.remote.droits;

import javax.ejb.Remote;

import fr.legrain.bdg.compteclient.dto.droits.TaAuthURLDTO;
import fr.legrain.bdg.compteclient.model.droits.TaAuthURL;
import fr.legrain.bdg.compteclient.service.interfaces.remote.general.IAbstractLgrDAOServer;
import fr.legrain.bdg.compteclient.service.interfaces.remote.general.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.compteclient.service.interfaces.remote.general.IGenericCRUDServiceRemote;



@Remote
public interface ITaAuthURLServiceRemote extends IGenericCRUDServiceRemote<TaAuthURL,TaAuthURLDTO>,
														IAbstractLgrDAOServer<TaAuthURL>,IAbstractLgrDAOServerDTO<TaAuthURLDTO>{
	public static final String validationContext = "AUTH_URL";
}
