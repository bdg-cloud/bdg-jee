package fr.legrain.bdg.conformite.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.conformite.dto.TaTypeConformiteDTO;
import fr.legrain.conformite.model.TaTypeConformite;

@Remote
public interface ITaTypeConformiteServiceRemote extends IGenericCRUDServiceRemote<TaTypeConformite,TaTypeConformiteDTO>,
														IAbstractLgrDAOServer<TaTypeConformite>,IAbstractLgrDAOServerDTO<TaTypeConformiteDTO>{
	public static final String validationContext = "TYPE_CONFORMITE";
}
