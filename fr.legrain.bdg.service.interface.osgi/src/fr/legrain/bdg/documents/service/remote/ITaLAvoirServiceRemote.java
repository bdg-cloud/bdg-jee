package fr.legrain.bdg.documents.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.document.dto.TaLAvoirDTO;
import fr.legrain.document.model.TaLAvoir;

@Remote
public interface ITaLAvoirServiceRemote extends IGenericCRUDServiceRemote<TaLAvoir,TaLAvoirDTO>,
														IAbstractLgrDAOServer<TaLAvoir>,IAbstractLgrDAOServerDTO<TaLAvoirDTO>{
	public static final String validationContext = "L_AVOIR";
}
