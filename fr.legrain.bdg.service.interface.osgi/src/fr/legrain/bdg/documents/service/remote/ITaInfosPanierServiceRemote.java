package fr.legrain.bdg.documents.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.document.dto.TaPanierDTO;
import fr.legrain.document.model.TaInfosPanier;

@Remote
public interface ITaInfosPanierServiceRemote extends IGenericCRUDServiceRemote<TaInfosPanier,TaPanierDTO>,
														IAbstractLgrDAOServer<TaInfosPanier>,IAbstractLgrDAOServerDTO<TaPanierDTO>{
	public TaInfosPanier findByCodePanier(String code);

}
