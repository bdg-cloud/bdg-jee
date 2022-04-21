package fr.legrain.bdg.documents.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.document.dto.TaAvoirDTO;
import fr.legrain.document.model.TaInfosAvoir;

@Remote
//public interface ITaInfosDevisServiceRemote extends IGenericCRUDServiceRemote<TaInfosDevis,TaInfosDevisDTO>,
//														IAbstractLgrDAOServer<TaInfosDevis>,IAbstractLgrDAOServerDTO<TaInfosDevisDTO>{
public interface ITaInfosAvoirServiceRemote extends IGenericCRUDServiceRemote<TaInfosAvoir,TaAvoirDTO>,
														IAbstractLgrDAOServer<TaInfosAvoir>,IAbstractLgrDAOServerDTO<TaAvoirDTO>{
	public TaInfosAvoir findByCodeAvoir(String code);

}
