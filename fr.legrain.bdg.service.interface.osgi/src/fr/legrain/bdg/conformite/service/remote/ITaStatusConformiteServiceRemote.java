package fr.legrain.bdg.conformite.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.conformite.dto.TaStatusConformiteDTO;
import fr.legrain.conformite.model.TaStatusConformite;

@Remote
public interface ITaStatusConformiteServiceRemote extends IGenericCRUDServiceRemote<TaStatusConformite,TaStatusConformiteDTO>,
														IAbstractLgrDAOServer<TaStatusConformite>,IAbstractLgrDAOServerDTO<TaStatusConformiteDTO>{
	public static final String validationContext = "STATUS_CONFORMITE";
}
