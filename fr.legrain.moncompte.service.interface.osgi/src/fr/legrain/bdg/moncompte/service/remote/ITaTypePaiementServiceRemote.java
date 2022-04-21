package fr.legrain.bdg.moncompte.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.moncompte.dto.TaTypePaiementDTO;
import fr.legrain.moncompte.model.TaTypePaiement;


@Remote
public interface ITaTypePaiementServiceRemote extends IGenericCRUDServiceRemote<TaTypePaiement,TaTypePaiementDTO>,
														IAbstractLgrDAOServer<TaTypePaiement>,IAbstractLgrDAOServerDTO<TaTypePaiementDTO>{
	public static final String validationContext = "TYPE_PAIEMENT";

}
