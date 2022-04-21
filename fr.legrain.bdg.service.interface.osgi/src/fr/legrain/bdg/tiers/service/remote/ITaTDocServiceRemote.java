package fr.legrain.bdg.tiers.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.document.dto.TaTDocDTO;
import fr.legrain.document.model.TaTDoc;

@Remote
public interface ITaTDocServiceRemote extends IGenericCRUDServiceRemote<TaTDoc,TaTDocDTO>,
														IAbstractLgrDAOServer<TaTDoc>,IAbstractLgrDAOServerDTO<TaTDocDTO>{
	
}
