package fr.legrain.bdg.documents.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.document.dto.TaTicketDeCaisseDTO;
import fr.legrain.document.model.TaInfosTicketDeCaisse;

@Remote
//public interface ITaInfosDevisServiceRemote extends IGenericCRUDServiceRemote<TaInfosDevis,TaInfosDevisDTO>,
//														IAbstractLgrDAOServer<TaInfosDevis>,IAbstractLgrDAOServerDTO<TaInfosDevisDTO>{
public interface ITaInfosTicketDeCaisseServiceRemote extends IGenericCRUDServiceRemote<TaInfosTicketDeCaisse,TaTicketDeCaisseDTO>,
														IAbstractLgrDAOServer<TaInfosTicketDeCaisse>,IAbstractLgrDAOServerDTO<TaTicketDeCaisseDTO>{
	public TaInfosTicketDeCaisse findByCodeFacture(String code);

}
