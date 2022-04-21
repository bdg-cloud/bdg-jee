package fr.legrain.bdg.tiers.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.document.model.TaTPaiement;
import fr.legrain.document.dto.TaTPaiementDTO;

@Remote
public interface ITaTPaiementServiceRemote extends IGenericCRUDServiceRemote<TaTPaiement,TaTPaiementDTO>,
														IAbstractLgrDAOServer<TaTPaiement>,IAbstractLgrDAOServerDTO<TaTPaiementDTO>{
	public static final String validationContext = "T_PAIEMENT";
}
