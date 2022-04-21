package fr.legrain.bdg.documents.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.document.dto.TaLPreparationDTO;
import fr.legrain.document.dto.TaLFactureDTO;
import fr.legrain.document.model.TaLPreparation;
import fr.legrain.document.model.TaLFacture;

@Remote
public interface ITaLPreparationServiceRemote extends IGenericCRUDServiceRemote<TaLPreparation,TaLPreparationDTO>,
														IAbstractLgrDAOServer<TaLPreparation>,IAbstractLgrDAOServerDTO<TaLPreparationDTO>{
	public static final String validationContext = "L_PREPARATION";
}
