package fr.legrain.bdg.documents.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.document.dto.TaLRelanceDTO;
import fr.legrain.document.model.TaLRelance;

@Remote
public interface ITaLRelanceServiceRemote extends IGenericCRUDServiceRemote<TaLRelance,TaLRelanceDTO>,
														IAbstractLgrDAOServer<TaLRelance>,IAbstractLgrDAOServerDTO<TaLRelanceDTO>{

//	public List<TaLRelanceDTO> rechercheLigneRelanceDTO(String codeRelance);
}
