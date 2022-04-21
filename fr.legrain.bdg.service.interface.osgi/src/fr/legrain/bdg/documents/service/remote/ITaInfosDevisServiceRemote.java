package fr.legrain.bdg.documents.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.document.dto.TaDevisDTO;
import fr.legrain.document.model.TaInfosDevis;

@Remote
//public interface ITaInfosDevisServiceRemote extends IGenericCRUDServiceRemote<TaInfosDevis,TaInfosDevisDTO>,
//														IAbstractLgrDAOServer<TaInfosDevis>,IAbstractLgrDAOServerDTO<TaInfosDevisDTO>{
public interface ITaInfosDevisServiceRemote extends IGenericCRUDServiceRemote<TaInfosDevis,TaDevisDTO>,
														IAbstractLgrDAOServer<TaInfosDevis>,IAbstractLgrDAOServerDTO<TaDevisDTO>{
	public TaInfosDevis findByCodeDevis(String code);

}
