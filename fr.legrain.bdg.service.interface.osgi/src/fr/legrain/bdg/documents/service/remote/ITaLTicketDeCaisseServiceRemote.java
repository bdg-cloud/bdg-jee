package fr.legrain.bdg.documents.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.document.dto.TaLTicketDeCaisseDTO;
import fr.legrain.document.model.TaLTicketDeCaisse;

@Remote
public interface ITaLTicketDeCaisseServiceRemote extends IGenericCRUDServiceRemote<TaLTicketDeCaisse,TaLTicketDeCaisseDTO>,
														IAbstractLgrDAOServer<TaLTicketDeCaisse>,IAbstractLgrDAOServerDTO<TaLTicketDeCaisseDTO>{
	public static final String validationContext = "L_TICKET_CAISSE";
}
