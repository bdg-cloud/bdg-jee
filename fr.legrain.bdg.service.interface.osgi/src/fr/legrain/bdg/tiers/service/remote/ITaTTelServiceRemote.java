package fr.legrain.bdg.tiers.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.tiers.dto.TaTTelDTO;
import fr.legrain.tiers.model.TaTTel;

@Remote
public interface ITaTTelServiceRemote extends IGenericCRUDServiceRemote<TaTTel,TaTTelDTO>,
														IAbstractLgrDAOServer<TaTTel>,IAbstractLgrDAOServerDTO<TaTTelDTO>{
	public static final String validationContext = "T_TEL";
}
