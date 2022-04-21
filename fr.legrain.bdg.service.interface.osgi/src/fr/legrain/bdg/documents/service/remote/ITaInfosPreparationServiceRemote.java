package fr.legrain.bdg.documents.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.document.dto.TaPreparationDTO;
import fr.legrain.document.model.TaInfosPreparation;

@Remote
public interface ITaInfosPreparationServiceRemote extends IGenericCRUDServiceRemote<TaInfosPreparation,TaPreparationDTO>,
														IAbstractLgrDAOServer<TaInfosPreparation>,IAbstractLgrDAOServerDTO<TaPreparationDTO>{
	public TaInfosPreparation findByCodePreparation(String code);

}
