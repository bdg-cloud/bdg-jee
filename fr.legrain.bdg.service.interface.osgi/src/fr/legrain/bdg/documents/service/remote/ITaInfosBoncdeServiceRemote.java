package fr.legrain.bdg.documents.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.document.dto.TaBoncdeDTO;
import fr.legrain.document.model.TaInfosBoncde;

@Remote
//public interface ITaInfosDevisServiceRemote extends IGenericCRUDServiceRemote<TaInfosDevis,TaInfosDevisDTO>,
//														IAbstractLgrDAOServer<TaInfosDevis>,IAbstractLgrDAOServerDTO<TaInfosDevisDTO>{
public interface ITaInfosBoncdeServiceRemote extends IGenericCRUDServiceRemote<TaInfosBoncde,TaBoncdeDTO>,
														IAbstractLgrDAOServer<TaInfosBoncde>,IAbstractLgrDAOServerDTO<TaBoncdeDTO>{
	public TaInfosBoncde findByCodeBoncde(String code);

}
