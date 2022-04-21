package fr.legrain.bdg.documents.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.document.dto.TaLPrelevementDTO;
import fr.legrain.document.model.TaLPrelevement;

@Remote
public interface ITaLPrelevementServiceRemote extends IGenericCRUDServiceRemote<TaLPrelevement,TaLPrelevementDTO>,
														IAbstractLgrDAOServer<TaLPrelevement>,IAbstractLgrDAOServerDTO<TaLPrelevementDTO>{
	public static final String validationContext = "L_PRELEVEMENT";
}
