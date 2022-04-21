package fr.legrain.bdg.documents.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.document.dto.TaAvisEcheanceDTO;
import fr.legrain.document.model.TaInfosAvisEcheance;

@Remote
public interface ITaInfosAvisEcheanceServiceRemote extends IGenericCRUDServiceRemote<TaInfosAvisEcheance,TaAvisEcheanceDTO>,
														IAbstractLgrDAOServer<TaInfosAvisEcheance>,IAbstractLgrDAOServerDTO<TaAvisEcheanceDTO>{
	public TaInfosAvisEcheance findByCodeAvisEcheance(String code);

}
