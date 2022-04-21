package fr.legrain.bdg.documents.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.document.dto.TaBonReceptionDTO;
import fr.legrain.document.model.TaInfosBonReception;

@Remote
//public interface ITaInfosBonReceptionServiceRemote extends IGenericCRUDServiceRemote<TaInfosBonReception,TaInfosBonReceptionDTO>,
//														IAbstractLgrDAOServer<TaInfosBonReception>,IAbstractLgrDAOServerDTO<TaInfosBonReceptionDTO>{
public interface ITaInfosBonReceptionServiceRemote extends IGenericCRUDServiceRemote<TaInfosBonReception,TaBonReceptionDTO>,
														IAbstractLgrDAOServer<TaInfosBonReception>,IAbstractLgrDAOServerDTO<TaBonReceptionDTO>{
	public TaInfosBonReception findByCodeBonReception(String code);

}
