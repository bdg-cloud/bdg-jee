package fr.legrain.bdg.documents.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.document.dto.TaPrelevementDTO;
import fr.legrain.document.model.TaInfosPrelevement;

@Remote
public interface ITaInfosPrelevementServiceRemote extends IGenericCRUDServiceRemote<TaInfosPrelevement,TaPrelevementDTO>,
														IAbstractLgrDAOServer<TaInfosPrelevement>,IAbstractLgrDAOServerDTO<TaPrelevementDTO>{
	public TaInfosPrelevement findByCodePrelevement(String code);

}
