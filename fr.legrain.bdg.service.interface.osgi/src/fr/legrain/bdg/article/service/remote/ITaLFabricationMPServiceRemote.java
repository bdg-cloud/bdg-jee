package fr.legrain.bdg.article.service.remote;

import javax.ejb.Remote;

import fr.legrain.article.model.TaLFabricationMP;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.document.dto.TaLFabricationDTO;

@Remote
public interface ITaLFabricationMPServiceRemote extends IGenericCRUDServiceRemote<TaLFabricationMP,TaLFabricationDTO>,
														IAbstractLgrDAOServer<TaLFabricationMP>,IAbstractLgrDAOServerDTO<TaLFabricationDTO>{
	public static final String validationContext = "L_FABRICATION";
}
