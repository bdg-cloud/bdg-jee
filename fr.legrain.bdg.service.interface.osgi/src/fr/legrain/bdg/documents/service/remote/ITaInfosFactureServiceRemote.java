package fr.legrain.bdg.documents.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.document.dto.TaFactureDTO;
import fr.legrain.document.model.TaInfosFacture;

@Remote
//public interface ITaInfosDevisServiceRemote extends IGenericCRUDServiceRemote<TaInfosDevis,TaInfosDevisDTO>,
//														IAbstractLgrDAOServer<TaInfosDevis>,IAbstractLgrDAOServerDTO<TaInfosDevisDTO>{
public interface ITaInfosFactureServiceRemote extends IGenericCRUDServiceRemote<TaInfosFacture,TaFactureDTO>,
														IAbstractLgrDAOServer<TaInfosFacture>,IAbstractLgrDAOServerDTO<TaFactureDTO>{
	public TaInfosFacture findByCodeFacture(String code);

}
