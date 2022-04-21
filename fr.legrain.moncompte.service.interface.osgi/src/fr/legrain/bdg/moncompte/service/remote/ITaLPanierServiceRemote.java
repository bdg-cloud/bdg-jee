package fr.legrain.bdg.moncompte.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.moncompte.dto.TaLPanierDTO;
import fr.legrain.moncompte.model.TaLignePanier;


@Remote
public interface ITaLPanierServiceRemote extends IGenericCRUDServiceRemote<TaLignePanier,TaLPanierDTO>,
														IAbstractLgrDAOServer<TaLignePanier>,IAbstractLgrDAOServerDTO<TaLPanierDTO>{
	public static final String validationContext = "L_PANIER";

}
