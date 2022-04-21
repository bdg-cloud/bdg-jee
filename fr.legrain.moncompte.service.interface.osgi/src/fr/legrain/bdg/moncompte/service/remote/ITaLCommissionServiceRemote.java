package fr.legrain.bdg.moncompte.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.moncompte.dto.TaLCommissionDTO;
import fr.legrain.moncompte.dto.TaLPanierDTO;
import fr.legrain.moncompte.model.TaLigneCommission;
import fr.legrain.moncompte.model.TaLignePanier;


@Remote
public interface ITaLCommissionServiceRemote extends IGenericCRUDServiceRemote<TaLigneCommission,TaLCommissionDTO>,
														IAbstractLgrDAOServer<TaLigneCommission>,IAbstractLgrDAOServerDTO<TaLCommissionDTO>{
	public static final String validationContext = "L_COMMISSION";

}
