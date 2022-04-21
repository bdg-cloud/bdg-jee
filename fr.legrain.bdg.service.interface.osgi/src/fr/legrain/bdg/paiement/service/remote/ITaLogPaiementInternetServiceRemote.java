package fr.legrain.bdg.paiement.service.remote;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.paiement.dto.TaLogPaiementInternetDTO;
import fr.legrain.paiement.model.TaLogPaiementInternet;

@Remote
public interface ITaLogPaiementInternetServiceRemote extends IGenericCRUDServiceRemote<TaLogPaiementInternet,TaLogPaiementInternetDTO>,
														IAbstractLgrDAOServer<TaLogPaiementInternet>,IAbstractLgrDAOServerDTO<TaLogPaiementInternetDTO>{
	public static final String validationContext = "LOG_PAIEMENT_INTERNET";
	
//	public List<TaLogPaiementInternetDTO> selectAllDTOLight();
//	public List<TaLogPaiementInternetDTO> selectAllDTOLight(Date debut, Date fin);
	
}
