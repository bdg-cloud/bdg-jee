package fr.legrain.bdg.servicewebexterne.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.servicewebexterne.dto.TaLigneShippingboDTO;
import fr.legrain.servicewebexterne.model.TaLigneShippingBo;

@Remote
public interface ITaLigneShippingboServiceRemote extends IGenericCRUDServiceRemote<TaLigneShippingBo,TaLigneShippingboDTO>,
IAbstractLgrDAOServer<TaLigneShippingBo>,IAbstractLgrDAOServerDTO<TaLigneShippingboDTO> {
	
	public static final String validationContext = "TA_LIGNE_SHIPPINGBO_SERVICE";

}
