package fr.legrain.bdg.documents.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.TaRelanceDTO;
import fr.legrain.document.model.TaRelance;
import fr.legrain.document.model.TaTRelance;

@Remote
public interface ITaRelanceServiceRemote extends IGenericCRUDServiceRemote<TaRelance,TaRelanceDTO>,
														IAbstractLgrDAOServer<TaRelance>,IAbstractLgrDAOServerDTO<TaRelanceDTO>{
	public TaTRelance maxTaTRelance(IDocumentTiers taDocument);
	
}
