package fr.legrain.bdg.documents.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.document.dto.TaLFactureDTO;
import fr.legrain.document.model.TaLFacture;

@Remote
public interface ITaLFactureServiceRemote extends IGenericCRUDServiceRemote<TaLFacture,TaLFactureDTO>,
														IAbstractLgrDAOServer<TaLFacture>,IAbstractLgrDAOServerDTO<TaLFactureDTO>{
	public static final String validationContext = "L_FACTURE";
}
