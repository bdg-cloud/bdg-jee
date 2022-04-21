package fr.legrain.bdg.article.service.remote;

import javax.ejb.Remote;
import javax.ejb.RemoveException;

import fr.legrain.article.dto.TaPrixDTO;
import fr.legrain.article.model.TaPrix;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;

@Remote
public interface ITaPrixServiceRemote extends IGenericCRUDServiceRemote<TaPrix,TaPrixDTO>,
														IAbstractLgrDAOServer<TaPrix>,IAbstractLgrDAOServerDTO<TaPrixDTO>{
	public static final String validationContext = "PRIX";
	public void removeSansFind(TaPrix persistentInstance) throws RemoveException;
}
