package fr.legrain.bdg.tiers.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.tiers.dto.TaTCPaiementDTO;
import fr.legrain.tiers.model.TaTCPaiement;

@Remote
public interface ITaTCPaiementServiceRemote extends IGenericCRUDServiceRemote<TaTCPaiement,TaTCPaiementDTO>,
														IAbstractLgrDAOServer<TaTCPaiement>,IAbstractLgrDAOServerDTO<TaTCPaiementDTO>{
	public static final String validationContext = "T_C_PAIEMENT";
}
