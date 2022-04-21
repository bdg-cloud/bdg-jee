package fr.legrain.bdg.tiers.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.tiers.dto.TaTEmailDTO;
import fr.legrain.tiers.model.TaTEmail;

@Remote
public interface ITaTEmailServiceRemote extends IGenericCRUDServiceRemote<TaTEmail,TaTEmailDTO>,
														IAbstractLgrDAOServer<TaTEmail>,IAbstractLgrDAOServerDTO<TaTEmailDTO>{
	public static final String validationContext = "T_EMAIL";
}
