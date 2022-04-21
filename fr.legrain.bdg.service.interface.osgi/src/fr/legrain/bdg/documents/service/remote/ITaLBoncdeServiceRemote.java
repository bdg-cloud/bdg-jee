package fr.legrain.bdg.documents.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.document.dto.TaLBoncdeDTO;
import fr.legrain.document.model.TaLBoncde;

@Remote
public interface ITaLBoncdeServiceRemote extends IGenericCRUDServiceRemote<TaLBoncde,TaLBoncdeDTO>,
														IAbstractLgrDAOServer<TaLBoncde>,IAbstractLgrDAOServerDTO<TaLBoncdeDTO>{
	public static final String validationContext = "L_BONCDE";
}
