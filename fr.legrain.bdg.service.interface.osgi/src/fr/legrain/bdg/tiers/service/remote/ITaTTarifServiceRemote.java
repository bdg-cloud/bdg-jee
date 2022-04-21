package fr.legrain.bdg.tiers.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.tiers.dto.TaTTarifDTO;
import fr.legrain.tiers.model.TaTTarif;

@Remote
public interface ITaTTarifServiceRemote extends IGenericCRUDServiceRemote<TaTTarif,TaTTarifDTO>,
														IAbstractLgrDAOServer<TaTTarif>,IAbstractLgrDAOServerDTO<TaTTarifDTO>{
	public static final String validationContext = "T_TARIF";
}
