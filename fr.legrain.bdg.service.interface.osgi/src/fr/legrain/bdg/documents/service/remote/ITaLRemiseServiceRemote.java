package fr.legrain.bdg.documents.service.remote;

import java.util.List;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.document.dto.TaLRemiseDTO;
import fr.legrain.document.model.TaLRemise;

@Remote
public interface ITaLRemiseServiceRemote extends IGenericCRUDServiceRemote<TaLRemise,TaLRemiseDTO>,
														IAbstractLgrDAOServer<TaLRemise>,IAbstractLgrDAOServerDTO<TaLRemiseDTO>,
														IDocumentService<TaLRemise>{

	public List<TaLRemiseDTO> RechercheLigneRemiseDTO(String codeRemise);
}
